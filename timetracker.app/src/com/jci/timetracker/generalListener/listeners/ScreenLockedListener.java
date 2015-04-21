package com.jci.timetracker.generalListener.listeners;

import java.util.Date;

/**
 * Triggers whenever the screen locks<br>
 * 
 * @author Laykou
 */
public class ScreenLockedListener implements EventListener
{
	private Date dateTime;
	private int sessionId;

	public ScreenLockedListener(int sessionId)
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
