package com.jci.timetracker.requester.ejb;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.NamingException;

import com.jci.timetracker.generalListener.Listener;
import com.jci.timetracker.generalListener.listeners.RequestsConnectionErrorListener;
import com.jci.timetracker.requester.ejb.exceptions.ResponseCallbackException;
import com.jci.timetracker.utils.MigrationSerializerObjectInputStream;
import com.jci.timetracker.utils.WorkQueue;

/**
 * Logs all requests for web api server (in case of application crash or unreachable server). It will try to execute each command until the server reponses in another thread. It the server does not responses, it will add the request to the end of queue.
 * 
 * @author Ladislav Gallay
 * @see WorkQueue
 */
public class RequestsWorker extends Thread
{
    /**
     * Singleton
     */
    private static RequestsWorker instance = null;

    /**
     * Filename where the log is stored
     */
    private String filename = "business_requests_log.ser";

    /**
     * Requests queue
     */
    private LinkedList<RequestMessageWrapper> requests;

    /**
     * Time how long will the thread sleep when too many bad requests. It starts at 5 seconds and doubles every time
     */
    private int sleepTime = 5000;

    /**
     * Maximal count of requests. After this number application will report RequestsError
     */
    private final int maxBadRequestsLimit = 3;

    /**
     * Indicates whether the requester still sends requests. You might want to stopRunning() when application is offline. Requests will be still tracked and stored to local file and they will be executed by next startup.
     */
    private boolean running = true;

    private static Logger logger = Logger.getLogger(RequestsWorker.class.getName());

    /**
     * There can be only one instance of this logger.
     * 
     * @return Existing instance of RequestsLogger
     * @see RequestsWorker#RequestsLogger()
     */
    public static RequestsWorker getInstance()
    {
        if (instance == null) {
            instance = new RequestsWorker();
            instance.readRequests();
            instance.start();
        }

        return instance;
    }

    /**
     * This will return stopped instance. If instance is creating it will be not started.
     * 
     * @return Existing instance of RequestsLogger
     * @see RequestsWorker#RequestsLogger()
     */
    public static RequestsWorker getStoppedInstance()
    {
        if (instance == null) {
            instance = new RequestsWorker();
            instance.readRequests();
        }
        instance.stopRunning();

        return instance;
    }

    /**
     * This will return stopped instance. If instance is creating it will be not started.
     * 
     * @return Existing instance of RequestsLogger
     * @see RequestsWorker#RequestsLogger()
     */
    public static RequestsWorker getStoppedInstance(String filename)
    {
        if (instance == null) {
            instance = new RequestsWorker();
            instance.setFilename(filename);
            instance.readRequests();
        }
        instance.stopRunning();

        return instance;
    }

    /**
     * This will start a new thread and run {@link #run()} which will wait's for requests to be added
     */
    private RequestsWorker()
    {
        requests = new LinkedList<RequestMessageWrapper>();
    }

    /**
     * Add new request to be proccesed. It will be automatically proceed in queue.
     * 
     * @param ar Request
     * @param onePerTime If the request with same site can be added multiple times in queue. For example for get requests there is no need to be pending in the queue multiple times. This is to prevent infinity get requests when application is offline. Request will be run as last required.
     */
    public void addRequest(RequestMessageWrapper request)
    {
        synchronized (requests) {
            requests.add(request);
            writeRequests();
            requests.notify();
        }
    }

    /**
     * Remove first request from queue (requests) and save requests. Returns this removed request.
     * 
     * @return Return first request in queue (and remove it from queue
     */
    public RequestMessageWrapper removeFirstFromRequests()
    {
        RequestMessageWrapper removedRequest = requests.removeFirst();
        writeRequests();

        return removedRequest;
    }

    /**
     * Reads all requests from the saving file into memory
     */
    @SuppressWarnings("unchecked")
    private void readRequests()
    {
        try {
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new MigrationSerializerObjectInputStream(fileIn);
            requests = (LinkedList<RequestMessageWrapper>) in.readObject();
            in.close();
            fileIn.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("Could not read requests log from file " + filename + ".");
        }
    }

    /**
     * Store all current unprocessed request into file (they will be executed later).
     */
    private void writeRequests()
    {
        try {
            FileOutputStream fileOut = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(requests);
            out.close();
            fileOut.close();
        }
        catch (IOException i) {
            i.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        RequestMessageWrapper request = null;

        // Number of bad requests. If it reaches some number, stop processing the queue
        int badRequests = 0;

        while (running) {
            final List<RequestMessageWrapper> requestList = new LinkedList<RequestMessageWrapper>();

            synchronized (requests) {
                while (requests.isEmpty()) {
                    try {
                        requests.wait();
                    }
                    catch (InterruptedException ignored) {
                        logger.severe("Request worker interupted while waiting.");
                    }
                }

                // Get first api request in row
                request = removeFirstFromRequests();
                requestList.add(request);
            }

            try {
                if (requestList.size() == 1) {
                    // Execute the request (this will execute the callback as well)
                    request.execute();
                }
            }
            catch (ResponseCallbackException e) {
                // There were something wrong in callback method, but request was OK
                e.getException().printStackTrace();
            }
            catch (NamingException e) {
                logger.log(Level.INFO, e.getMessage(), e);
            }
            catch (Exception e) {
                // Request was not OK. Add it again to the beginning of queue at first position - so the order of request will not be skipped.
                requests.addFirst(request);
                badRequests++;

                logger.log(Level.SEVERE, "Error while executing request", e);
            }

            // If the there are too many bad requests, there is something wrong with the server (probably not available). Try again later
            if (badRequests >= maxBadRequestsLimit) {
                try {
                    System.out.println("Sleeping");
                    Listener.getInstance().call(new RequestsConnectionErrorListener());

                    // Wait 10 minutes
                    Thread.sleep(sleepTime);
                }
                catch (InterruptedException ignored) {
                }
                finally {
                    sleepTime *= 2;
                    badRequests = 0;
                }
            }
        }

        System.out.println("Requests worker logger has stopped!");
    }

    /**
     * Stop worker from sending requests. To start worker you must use {@link #startRunning()}
     */
    public void stopRunning()
    {
        running = false;
    }

    /**
     * Start worker if is not running. To stop worker, call {@link #stopRunning()}
     */
    public void startRunning()
    {
        if (!running) {
            running = true;
            start();
        }
    }

    /**
     * This is only debugging method for retrieving worker request list loaded from file
     * 
     * @return
     */
    public LinkedList<RequestMessageWrapper> getRequests()
    {
        return requests;
    }

    /**
     * Setting filename is only for debuging purposes. See {@link RequestsWorker#getStoppedInstance(String)}
     * 
     * @param filename
     */
    private void setFilename(String filename)
    {
        this.filename = filename;
    }
}
