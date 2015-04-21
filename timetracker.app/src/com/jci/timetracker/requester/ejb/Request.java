package com.jci.timetracker.requester.ejb;


public class Request
{
    public static void add(String serviceName, RequestMessage message)
    {
        RequestMessageWrapper request = new RequestMessageWrapper(serviceName, message);
        RequestsWorker.getInstance().addRequest(request);
    }
}
