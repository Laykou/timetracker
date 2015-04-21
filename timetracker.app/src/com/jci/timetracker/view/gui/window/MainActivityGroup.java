package com.jci.timetracker.view.gui.window;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;

import com.jci.bbc.timetracker.model.Action;
import com.jci.timetracker.view.gui.MainWindow;
import com.jci.timetracker.view.gui.actionListeners.ActivityButtonPressed;
import com.jci.timetracker.view.gui.objects.CustomColorButton;
import com.jci.timetracker.view.gui.tools.ControlPanel;

public class MainActivityGroup extends ControlPanel
{
	private String title;
	protected static final Insets buttonInsets = new Insets(0, 0, 0, 0);

	public MainActivityGroup(String title)
	{
		this.title = title;

		TitledBorder border = new TitledBorder(title);
		border.setTitleFont(MainWindow.DEFAULT_FONT.deriveFont(Font.BOLD));

		this.setBorder(BorderFactory.createTitledBorder(border));
	}

	public void addButton(Action action)
	{
		CustomColorButton button = new CustomColorButton(Color.WHITE, new Color(201, 225, 250), new Color(230, 230, 230), Color.BLACK);
		button.setText(action.getActivity().getLabel());

		this.addComponent(button, 0, this.getRow(), 2, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, buttonInsets);

		button.addActionListener(new ActivityButtonPressed(action));
	}
}
