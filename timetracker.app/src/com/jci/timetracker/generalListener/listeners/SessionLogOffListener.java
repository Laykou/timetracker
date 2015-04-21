package com.jci.timetracker.generalListener.listeners;

import java.util.Date;

/**
 * Triggers whenever the user loggs off<br>
 * 
 * @author Laykou
 */
public class SessionLogOffListener implements EventListener
{
	private Date dateTime;
	private int sessionId;

	public SessionLogOffListener(int sessionId)
	{
		this.sessionId = sessionId;
		dateTime = new Date();
	}

	public int getSessionId()
	{
		return sessionId;
	}

	public Date getDateTime()
	{
		return dateTime;
	}
}
