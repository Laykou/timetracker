package com.jci.timetracker.view.gui.tools;

import javax.swing.JOptionPane;

import com.jci.timetracker.controller.Controller;
import com.jci.timetracker.language.Lang;

public class GuiActions
{
	public static void confirmTrackedActions()
	{
		String ObjButtons[] = { Lang.getS("general.Yes"), Lang.getS("general.No") };
		int PromptResult = JOptionPane.showOptionDialog(null, Lang.getS("gui.controller.ConfirmDialog"), Lang.getS("general.title"), JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
		if (PromptResult == 0)
		{
			Controller.confirmTrackedActions();
		}
	}

	public static void finishTrackedAction()
	{
		Controller.finishTracking();
	}
}
