package com.jci.timetracker.view.gui.window;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;

import com.jci.bbc.timetracker.model.Level;
import com.jci.timetracker.generalListener.Listener;
import com.jci.timetracker.generalListener.ListenerResponse;
import com.jci.timetracker.generalListener.listeners.LevelsUpdatedListener;
import com.jci.timetracker.generalListener.listeners.UserSettingsChangedListener;
import com.jci.timetracker.settings.CurrentUser;
import com.jci.timetracker.view.gui.objects.LevelRadioButton;
import com.jci.timetracker.view.gui.tools.ControlPanel;
import com.jci.timetracker.view.gui.tools.WrapLayout;

public class LevelsPanel extends ControlPanel implements ListenerResponse<LevelsUpdatedListener>
{
	private static final long serialVersionUID = 1L;
	private static ButtonGroup levelGroup1 = new ButtonGroup();
	private static ButtonGroup levelGroup2 = new ButtonGroup();
	private static ButtonGroup levelGroup3 = new ButtonGroup();

	public LevelsPanel()
	{
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createEmptyBorder());

		// When added new levels, add new radio buttons
		Listener.getInstance().addLevelsUpdated(this);
	}

	@Override
	public void call(final LevelsUpdatedListener e)
	{
		this.removeAll();

		if (e.getLevels1().size() > 0)
		{
			ControlPanel level1 = new ControlPanel();
			level1.setBorder(BorderFactory.createTitledBorder(""));
			level1.setLayout(new WrapLayout());
			addComponent(level1, 0, getRow(), 2, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL);

			boolean first = true;

			for (Level level : e.getLevels1())
			{
				LevelRadioButton levelRadioButton = new LevelRadioButton(level);

				levelGroup1.add(levelRadioButton);
				level1.add(levelRadioButton);

				if (first || level.equals(CurrentUser.getInstance().getCurrentUser().getDefault_level_1()))
				{
					// Make first selected by default
					levelRadioButton.setSelected(true);
					first = false;
				}

				// Update users default level when changed
				levelRadioButton.addItemListener(new ItemListener()
				{
					@Override
					public void itemStateChanged(ItemEvent e)
					{
						if (e.getStateChange() == ItemEvent.SELECTED)
						{
							CurrentUser.getInstance().getCurrentUser().setDefault_level_1(((LevelRadioButton) e.getSource()).getLevel());
							Listener.getInstance().call(new UserSettingsChangedListener());
						}
					}
				});
			}
		}

		if (e.getLevels2().size() > 0)
		{
			ControlPanel level2 = new ControlPanel();
			level2.setBorder(BorderFactory.createTitledBorder(""));
			level2.setLayout(new WrapLayout());
			addComponent(level2, 0, getRow(), 2, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL);

			boolean first = true;

			for (Level level : e.getLevels2())
			{
				LevelRadioButton levelRadioButton = new LevelRadioButton(level);

				levelGroup2.add(levelRadioButton);
				level2.add(levelRadioButton);

				if (first || level.equals(CurrentUser.getInstance().getCurrentUser().getDefault_level_2()))
				{
					// Make first selected by default
					levelRadioButton.setSelected(true);
					first = false;
				}

				// Update users default level when changed
				levelRadioButton.addItemListener(new ItemListener()
				{
					@Override
					public void itemStateChanged(ItemEvent e)
					{
						if (e.getStateChange() == ItemEvent.SELECTED)
						{
							CurrentUser.getInstance().getCurrentUser().setDefault_level_2(((LevelRadioButton) e.getSource()).getLevel());
							Listener.getInstance().call(new UserSettingsChangedListener());
						}
					}
				});
			}
		}

		if (e.getLevels3().size() > 0)
		{
			ControlPanel level3 = new ControlPanel();
			level3.setBorder(BorderFactory.createTitledBorder(""));
			level3.setLayout(new WrapLayout());
			addComponent(level3, 0, getRow(), 2, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL);

			boolean first = true;

			for (Level level : e.getLevels3())
			{
				LevelRadioButton levelRadioButton = new LevelRadioButton(level);

				levelGroup3.add(levelRadioButton);
				level3.add(levelRadioButton);

				if (first || level.equals(CurrentUser.getInstance().getCurrentUser().getDefault_level_3()))
				{
					// Make first selected by default
					levelRadioButton.setSelected(true);
					first = false;
				}

				// Update users default level when changed
				levelRadioButton.addItemListener(new ItemListener()
				{
					@Override
					public void itemStateChanged(ItemEvent e)
					{
						if (e.getStateChange() == ItemEvent.SELECTED)
						{
							CurrentUser.getInstance().getCurrentUser().setDefault_level_3(((LevelRadioButton) e.getSource()).getLevel());
							Listener.getInstance().call(new UserSettingsChangedListener());
						}
					}
				});
			}
		}
	}

	public static Level getSelectedLevel1()
	{
		for (Enumeration levelItem = levelGroup1.getElements(); levelItem.hasMoreElements();)
		{
			LevelRadioButton radioButton = (LevelRadioButton) levelItem.nextElement();

			if (radioButton.getModel() == levelGroup1.getSelection())
				return radioButton.getLevel();
		}

		return null;
	}

	public static Level getSelectedLevel2()
	{
		for (Enumeration levelItem = levelGroup2.getElements(); levelItem.hasMoreElements();)
		{
			LevelRadioButton radioButton = (LevelRadioButton) levelItem.nextElement();

			if (radioButton.getModel() == levelGroup2.getSelection())
				return radioButton.getLevel();
		}

		return null;
	}

	public static Level getSelectedLevel3()
	{
		for (Enumeration levelItem = levelGroup3.getElements(); levelItem.hasMoreElements();)
		{
			LevelRadioButton radioButton = (LevelRadioButton) levelItem.nextElement();

			if (radioButton.getModel() == levelGroup3.getSelection())
				return radioButton.getLevel();
		}

		return null;
	}
}
