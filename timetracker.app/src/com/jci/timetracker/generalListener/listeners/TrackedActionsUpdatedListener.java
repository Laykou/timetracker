package com.jci.timetracker.generalListener.listeners;

import java.util.List;

import com.jci.bbc.timetracker.model.TrackedAction;

/**
 * Triggers whenever all actions list has been updated<br>
 * 
 * @author Laykou
 * @see TrackedAction
 */
public class TrackedActionsUpdatedListener implements EventListener
{
	private List<TrackedAction> action;

	public TrackedActionsUpdatedListener(List<TrackedAction> a)
	{
		action = a;
	}

	/**
	 * Get added actions
	 * 
	 * @return
	 */
	public List<TrackedAction> getActions()
	{
		return action;
	}
}
