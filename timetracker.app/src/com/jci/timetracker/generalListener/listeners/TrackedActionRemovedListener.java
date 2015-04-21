package com.jci.timetracker.generalListener.listeners;

import com.jci.bbc.timetracker.model.Action;
import com.jci.bbc.timetracker.model.TrackedAction;

/**
 * Triggers whenever new action is removed<br>
 * 
 * @author Laykou
 * @see Action
 */
public class TrackedActionRemovedListener implements EventListener
{
	private TrackedAction action;

	public TrackedActionRemovedListener(TrackedAction a)
	{
		action = a;
	}

	/**
	 * Get added action
	 * 
	 * @return
	 */
	public TrackedAction getTrackedAction()
	{
		return action;
	}
}
