package com.jci.timetracker.run;

import java.util.LinkedList;

import com.jci.timetracker.requester.ApiRequest;
import com.jci.timetracker.requester.RequestsWorkerLogger;

/**
 * This is only development class to debug request_log.ser file which is created by {@link RequestsWorkerLogger}
 * 
 * @author Ladislav Gallay
 * 
 */
public class DebugRequestLogger
{

	public static void main(String[] args)
	{
		System.out.println("Get instance of worker");
		RequestsWorkerLogger worker = RequestsWorkerLogger.getStoppedInstance("C:\\Users\\jgallal\\Downloads\\akundra\\requests_log.ser");

		System.out.println("Getting requests");
		LinkedList<ApiRequest> requests = worker.getRequests();

		for (ApiRequest ar : requests)
		{
			System.out.println("\n===============================");
			System.out.println(ar.getID());
			System.out.println(ar.getSite());
			System.out.println(ar.getParameters());
		}

		System.out.println("Reading done");
	}

}
