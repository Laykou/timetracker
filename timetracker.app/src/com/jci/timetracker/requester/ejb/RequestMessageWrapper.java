package com.jci.timetracker.requester.ejb;

import java.io.Serializable;

import javax.naming.Context;
import javax.naming.InitialContext;

/**
 * Message that is waiting to be processed.
 * 
 * @author Ladislav Gallay
 * 
 */
public class RequestMessageWrapper implements Serializable
{
    private static final long serialVersionUID = 456784121324L;

    private String serviceName;

    private RequestMessage message;

    private static Context context = null;

    public RequestMessageWrapper(String serviceName, RequestMessage message)
    {
        this.serviceName = serviceName;
        this.message = message;
    }

    public String getServiceName()
    {
        return serviceName;
    }

    public void setServiceName(String serviceName)
    {
        this.serviceName = serviceName;
    }

    public RequestMessage getMessage()
    {
        return message;
    }

    public void setMessage(RequestMessage message)
    {
        this.message = message;
    }

    public void execute() throws Exception
    {
        if (context == null) {
            context = new InitialContext();
        }

        Object service = context.lookup("ejb:timetracker.ear/timetracker.ejb//" + serviceName + "!com.jci.bbc.timetracker.sessionbeans." + serviceName + "Remote");

        message.call(service);
    }
}
