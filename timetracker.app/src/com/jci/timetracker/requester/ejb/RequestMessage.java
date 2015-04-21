package com.jci.timetracker.requester.ejb;

import java.io.Serializable;

public interface RequestMessage extends Serializable
{
    /**
     * Holds the request logic. You might want to persist entities in this method or do other things with the EJB bean.
     * 
     * @param service
     */
    public void call(Object service);
}
