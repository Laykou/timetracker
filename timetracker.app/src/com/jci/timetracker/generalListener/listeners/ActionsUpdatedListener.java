package com.jci.timetracker.generalListener.listeners;

import java.util.List;

import com.jci.bbc.timetracker.model.Action;
import com.jci.timetracker.controller.Controller;

/**
 * Triggers whenever new actions are updated. For example in {@link Controller} when user's settings are obtained with Actions list.<br>
 * 
 * @author Ladislav Gallay
 * @see Action
 */
public class ActionsUpdatedListener implements EventListener
{
	private List<Action> action;

	public ActionsUpdatedListener(List<Action> a)
	{
		action = a;
	}

	/**
	 * Get new main actions
	 * 
	 * @return
	 */
	public List<Action> getActions()
	{
		return action;
	}
}
