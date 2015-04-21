package com.jci.timetracker.view.gui.objects;

import java.awt.Color;

import com.jci.bbc.timetracker.model.Action;
import com.jci.bbc.timetracker.model.Activity;
import com.jci.bbc.timetracker.model.TrackedAction;
import com.jci.timetracker.settings.CurrentUser;

public class TrackedActionInTable
{
	private TrackedAction action;
	private Object[] columns = new Object[9];

	// Colors
	private static Color notFinishedColor = Color.YELLOW;
	private static Color finishedColor = new Color(189, 255, 187);
	private static Color inactiveColor = new Color(255, 196, 136);

	// Row background color
	private Color rowColor = finishedColor;

	/**
	 * Number of frame
	 * 
	 * @param communicationTable
	 * @param frame
	 * @param number
	 */
	public TrackedActionInTable(TrackedAction action)
	{
		this.action = action;

		// Initialize column values
		setColumns();
	}

	/**
	 * Extract column information for table from the frame
	 * 
	 * @return
	 */
	private void setColumns()
	{
		columns[0] = action.getLevel1();
		columns[1] = action.getLevel2();
		columns[2] = action.getLevel3();
		columns[3] = action.getMainActivity();
		columns[4] = action.getSubActivity();
		columns[5] = action.getStart();
		columns[6] = action.getCount();
		columns[7] = action.getComment();
		columns[8] = action.getContinueForInactive();

		rowColor = finishedColor;

		if (action.getMainActivity() == null)
			rowColor = inactiveColor;

		if (action.getFinish() == null)
			rowColor = notFinishedColor;
	}

	public int getColumnsNumber()
	{
		return 9; // Number of columns
	}

	/**
	 * Get value of column
	 * 
	 * @param i
	 * @return
	 */
	public Object getColumn(int col)
	{
		setColumns();
		return columns[col];
	}

	/**
	 * Is editable
	 */
	public boolean isEditable(int col)
	{
		// Do not edit if data are confirmed on server
		if (action.getConfirmed())
			return false;

		// Allow editing only count and comment
		if (col == 6 || col == 7)
			return true;

		// Activities editing must be allowed for this department settings
		if (CurrentUser.getInstance().getCurrentUser().getDepartmentSettings().getAllowActivityEditing())
		{
			// Allow editing for main activity (if is set)
			if (col == 3 && columns[3] != null)
				return true;

			if (col == 4)
			{
				// Allow editing for sub activity (if there are some subactivities for selected main activity)

				for (Action ma : CurrentUser.getInstance().getCurrentUser().getDepartmentSettings().getParentActions())
					if (ma.getActivity().equals(action.getMainActivity()))
						return (ma.getSubactions().size() > 0);

				return false;
			}
		}

		// Allow editing getContinueForInactive only for IDLE Event
		if (col == 8 && action.getMainActivity() == null)
			return true;

		return false;
	}

	/**
	 * Set value of column
	 * 
	 * @param i
	 * @return
	 */
	public void setColumn(int col, Object value)
	{
		try
		{
			if (col == 3)
			{
				action.setMainActivity((Activity) value);
			}
			if (col == 4)
				action.setSubActivity((Activity) value);
			if (col == 6)
				action.setCount((Integer) value);
			if (col == 7)
				action.setComment((String) value);
			if (col == 8)
				action.setContinueForInactive((Boolean) value);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public TrackedAction getTrackedAction()
	{
		return action;
	}

	public Color getColor()
	{
		return rowColor;
	}

	public void setColor(Color color)
	{
		this.rowColor = color;
	}

	@Override
	public String toString()
	{
		return action.toString();
	}
}
