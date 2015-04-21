package com.jci.timetracker.generalListener.listeners;

import com.jci.bbc.timetracker.model.Action;
import com.jci.bbc.timetracker.model.TrackedAction;

/**
 * Triggers whenever an action is updated<br>
 * 
 * @author Ladislav Gallay
 * @see Action
 */
public class TrackedActionUpdatedListener implements EventListener
{
	private TrackedAction action;

	public TrackedActionUpdatedListener(TrackedAction a)
	{
		action = a;
	}

	/**
	 * Get added action
	 * 
	 * @return
	 */
	public TrackedAction getAction()
	{

		return action;
	}
}
