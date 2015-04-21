package com.jci.timetracker.view.gui.objects;

import javax.swing.JRadioButton;

import com.jci.bbc.timetracker.model.Level;

public class LevelRadioButton extends JRadioButton
{
	private static final long serialVersionUID = 1L;
	private Level level;

	public LevelRadioButton(Level level)
	{
		super(level.getLabel());
		this.level = level;
	}

	public Level getLevel()
	{
		return level;
	}
}
