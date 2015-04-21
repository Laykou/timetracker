package com.jci.timetracker.view.gui.window;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JSeparator;

import com.jci.timetracker.controller.Controller;
import com.jci.timetracker.view.gui.language.GuiLang;
import com.jci.timetracker.view.gui.objects.CustomColorButton;
import com.jci.timetracker.view.gui.tools.ControlPanel;
import com.jci.timetracker.view.gui.tools.GuiActions;

public class ControllerPanel extends ControlPanel
{
	public ControllerPanel()
	{
		super();

		// Panel with all levels
		LevelsPanel levelsPanel = new LevelsPanel();
		addComponent(levelsPanel, 0, getRow(), 2, 1, GridBagConstraints.LINE_END, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0));

		// Separator
		JSeparator sep1 = new JSeparator();
		addComponent(sep1, 0, getRow(), 2, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);

		// Panel with all buttons
		ActionButtonsPanel actionButtonsPanel = new ActionButtonsPanel();
		addComponent(actionButtonsPanel, 0, getRow(), 2, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0));

		// Separator
		JSeparator sep2 = new JSeparator();
		addComponent(sep2, 0, getRow(), 2, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);

		CustomColorButton finishButton = new CustomColorButton(new Color(240, 51, 51), Color.WHITE);
		GuiLang.update(finishButton, "gui.controller.FinishActions");
		addComponent(finishButton, 0, getRow(), 2, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);

		finishButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				GuiActions.finishTrackedAction();
			}
		});

		CustomColorButton confirmButton = new CustomColorButton(new Color(51, 240, 51), Color.WHITE);
		GuiLang.update(confirmButton, "gui.controller.Confirm");
		addComponent(confirmButton, 0, getRow(), 2, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);

		confirmButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				GuiActions.confirmTrackedActions();
			}
		});
	}

	private File getSaveLocation()
	{
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int result = chooser.showSaveDialog(null);

		if (result == chooser.APPROVE_OPTION)
		{
			return chooser.getSelectedFile();
		}
		else
		{
			return null;
		}
	}
}
