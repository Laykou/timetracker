package com.jci.timetracker.view.gui.window.trackedActionsTable;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import com.jci.bbc.timetracker.model.Activity;
import com.jci.bbc.timetracker.model.Level;
import com.jci.bbc.timetracker.model.TrackedAction;
import com.jci.timetracker.generalListener.Listener;
import com.jci.timetracker.generalListener.ListenerResponse;
import com.jci.timetracker.generalListener.listeners.TrackedActionAddedListener;
import com.jci.timetracker.generalListener.listeners.TrackedActionClearedListener;
import com.jci.timetracker.generalListener.listeners.TrackedActionRemovedListener;
import com.jci.timetracker.generalListener.listeners.TrackedActionUpdatedListener;
import com.jci.timetracker.generalListener.listeners.TrackedActionsUpdatedListener;
import com.jci.timetracker.language.Lang;
import com.jci.timetracker.view.gui.objects.TrackedActionInTable;

/**
 * Data model. Columns are defined in {@link TrackedActionsTable} too.
 * 
 * @author Ladislav Gallay
 * 
 */
public class TrackedActionsTableModel extends AbstractTableModel implements ListenerResponse<TrackedActionAddedListener>
{
	private String[] columnNames = { "trackedActions.Level1", "trackedActions.Level2", "trackedActions.Level3", "trackedActions.MainActivity", "trackedActions.SubActivity", "trackedActions.DateTimeFrom", "trackedActions.Count", "trackedActions.Comment", "trackedActions.ContinueForInactive", };
	private Class[] classes = { Level.class, Level.class, Level.class, Activity.class, Activity.class, Date.class, Integer.class, String.class, Boolean.class };

	private static final long serialVersionUID = 1L;

	private ArrayList<TrackedActionInTable> data = new ArrayList<TrackedActionInTable>();

	public TrackedActionsTableModel()
	{
		// When new action is tracked, update table model
		Listener.getInstance().addTrackedActionAdded(this);

		// When action is finished, refresh table
		Listener.getInstance().addTrackedActionRemoved(new ListenerResponse<TrackedActionRemovedListener>()
		{
			@Override
			public void call(TrackedActionRemovedListener e)
			{
				TrackedActionsTableModel.this.removeRow(e.getTrackedAction());
			}
		});

		// When actions are updated, rebuild table
		Listener.getInstance().addTrackedActionsUpdated(new ListenerResponse<TrackedActionsUpdatedListener>()
		{
			@Override
			public void call(TrackedActionsUpdatedListener e)
			{
				data.clear();
				for (TrackedAction trackedAction : e.getActions())
				{
					// Only not confirmed actions
					if (!trackedAction.getConfirmed())
						data.add(new TrackedActionInTable(trackedAction));
				}
				refreshData();
			}
		});

		// When action is finished, refresh table
		Listener.getInstance().addTrackedActionUpdated(new ListenerResponse<TrackedActionUpdatedListener>()
		{
			@Override
			public void call(TrackedActionUpdatedListener e)
			{
				refreshData();
			}
		});

		// When actions are removed, refresh table
		Listener.getInstance().addTrackedActionCleared(new ListenerResponse<TrackedActionClearedListener>()
		{
			@Override
			public void call(TrackedActionClearedListener e)
			{
				data.clear();
				refreshData();
			}
		});
	}

	public int getColumnCount()
	{
		return columnNames.length;
	}

	public int getRowCount()
	{
		return data.size();
	}

	public String getColumnName(int col)
	{
		return Lang.getS(columnNames[col]);
	}

	public Object getValueAt(int row, int col)
	{
		return data.get(row).getColumn(col);
	}

	public void addRows(ArrayList<TrackedActionInTable> rows)
	{
		data = rows;

		refreshData();
	}

	public void addRow(TrackedActionInTable frameInTable)
	{
		data.add(frameInTable);

		refreshData();
	}

	/**
	 * Find a row and remove it
	 * 
	 * @param trackedAction
	 */
	public void removeRow(TrackedAction trackedAction)
	{
		synchronized (data)
		{
			TrackedActionInTable tait = null;

			for (TrackedActionInTable t : data)
				if (t.getTrackedAction().equals(trackedAction))
				{
					tait = t;
					break;
				}

			if (tait != null)
				data.remove(tait);
		}

		refreshData();
	}

	public TrackedActionInTable getRow(int row)
	{
		return data.get(row);
	}

	/**
	 * JTable uses this method to determine the default renderer/ editor for each cell. If we didn't implement this method, then the last column would contain text ("true"/"false"), rather than a check box.
	 */
	public Class getColumnClass(int c)
	{
		return classes[c];
	}

	/**
	 * Don't need to implement this method unless your table's editable.
	 */
	public boolean isCellEditable(int row, int col)
	{
		return data.get(row).isEditable(col);
	}

	public void setValueAt(Object value, final int row, final int col)
	{
		data.get(row).setColumn(col, value);

		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				fireTableCellUpdated(row, col);
			}
		});
	}

	public void clear()
	{
		try
		{
			int rows = data.size();

			data.clear();

			if (rows > 0)
				fireTableRowsDeleted(0, rows - 1);
		}
		catch (Exception e)
		{

		}
	}

	/**
	 * Clean table, remove colors.
	 */
	public void removeColors()
	{
		for (TrackedActionInTable frame : data)
			frame.setColor(Color.WHITE);
	}

	public Color getRowColor(int row)
	{
		return getRow(row).getColor();
	}

	public void setRowColor(int row, Color color)
	{
		getRow(row).setColor(color);
	}

	public void refreshData()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				fireTableDataChanged();
			}
		});
	}

	@Override
	public void call(TrackedActionAddedListener e)
	{
		// When new action is tracked, add it to model
		this.addRow(new TrackedActionInTable(e.getTrackedAction()));
	}
}
