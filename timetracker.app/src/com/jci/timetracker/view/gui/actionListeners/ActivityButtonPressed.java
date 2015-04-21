package com.jci.timetracker.view.gui.actionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.jci.bbc.timetracker.model.Action;
import com.jci.timetracker.settings.CurrentUser;
import com.jci.timetracker.view.gui.window.LevelsPanel;

public class ActivityButtonPressed implements ActionListener
{
	private Action action;

	public ActivityButtonPressed(Action action)
	{
		this.action = action;
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		System.out.println("SELECTED " + LevelsPanel.getSelectedLevel1());
		CurrentUser.getInstance().getCurrentUser().createNewTrackedAction(LevelsPanel.getSelectedLevel1(), LevelsPanel.getSelectedLevel2(), LevelsPanel.getSelectedLevel3(), action);
	}

}
