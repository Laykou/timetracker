package com.jci.timetracker.generalListener.listeners;

import java.util.Date;

/**
 * Triggers whenever the screen unlocks<br>
 * 
 * @author Laykou
 */
public class ScreenUnlockedListener implements EventListener
{
	private Date dateTime;
	private int sessionId;

	public ScreenUnlockedListener(int sessionId)
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
