package com.jci.timetracker.generalListener.listeners;

import java.util.Collection;

import com.jci.bbc.timetracker.model.Action;
import com.jci.bbc.timetracker.model.Level;

/**
 * Triggers whenever new levels are added or changed<br>
 * 
 * @author Laykou
 * @see Action
 */
public class LevelsUpdatedListener implements EventListener
{
	private Collection<Level> levels1;
	private Collection<Level> levels2;
	private Collection<Level> levels3;

	public LevelsUpdatedListener(Collection<Level> levels1, Collection<Level> levels2, Collection<Level> levels3)
	{
		this.levels1 = levels1;
		this.levels2 = levels2;
		this.levels3 = levels3;
	}

	public Collection<Level> getLevels1()
	{
		return levels1;
	}

	public Collection<Level> getLevels2()
	{
		return levels2;
	}

	public Collection<Level> getLevels3()
	{
		return levels3;
	}

}
