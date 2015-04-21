package com.jci.timetracker.view.gui.window;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;

import com.jci.bbc.timetracker.model.Action;
import com.jci.timetracker.generalListener.Listener;
import com.jci.timetracker.generalListener.ListenerResponse;
import com.jci.timetracker.generalListener.listeners.ActionsUpdatedListener;
import com.jci.timetracker.view.gui.actionListeners.ActivityButtonPressed;
import com.jci.timetracker.view.gui.objects.CustomColorButton;
import com.jci.timetracker.view.gui.tools.ControlPanel;

public class ActionButtonsPanel extends ControlPanel implements ListenerResponse<ActionsUpdatedListener>
{
	private static final long serialVersionUID = 1L;

	protected static final Insets mainActivityInsets = new Insets(5, 0, 5, 0);
	protected static final Insets mainActivityGroupInsets = new Insets(5, 0, 5, 0);

	public ActionButtonsPanel()
	{
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createEmptyBorder());

		// When added new action, add new button
		Listener.getInstance().addActionsUpdated(this);
	}

	@Override
	public void call(final ActionsUpdatedListener e)
	{
		ActionButtonsPanel.this.removeAll();

		for (Action action : e.getActions())
		{
			if (action.getSubactions().size() > 0)
			{
				// There will be always label. If there is no subaction button for mainaction will be added
				String title = action.getActivity().getLabel();

				MainActivityGroup mainActivityGroup = new MainActivityGroup(title);
				addComponent(mainActivityGroup, 0, getRow(), 2, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, mainActivityGroupInsets);

				for (Action subAction : action.getSubactions())
					mainActivityGroup.addButton(subAction);
			}
			else
				addMainButton(action);
		}
	}

	public void addMainButton(Action action)
	{
		CustomColorButton button = new CustomColorButton(new Color(240, 240, 240), Color.BLACK, Font.BOLD);
		button.setText(action.getActivity().getLabel());
		this.addComponent(button, 0, this.getRow(), 2, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, mainActivityInsets);

		button.addActionListener(new ActivityButtonPressed(action));
	}
}
