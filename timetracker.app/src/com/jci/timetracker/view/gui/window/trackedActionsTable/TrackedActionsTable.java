package com.jci.timetracker.view.gui.window.trackedActionsTable;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import com.jci.bbc.timetracker.model.Activity;
import com.jci.bbc.timetracker.model.Level;
import com.jci.timetracker.generalListener.Listener;
import com.jci.timetracker.generalListener.ListenerResponse;
import com.jci.timetracker.generalListener.listeners.LanguageChangedListener;
import com.jci.timetracker.generalListener.listeners.LevelsUpdatedListener;
import com.jci.timetracker.generalListener.listeners.TrackedActionAddedListener;
import com.jci.timetracker.generalListener.listeners.TrackedActionUpdatedListener;
import com.jci.timetracker.generalListener.listeners.TrackedActionsUpdatedListener;
import com.jci.timetracker.view.gui.MainWindow;
import com.jci.timetracker.view.gui.objects.TrackedActionInTable;
import com.jci.timetracker.view.gui.window.trackedActionsTable.cells.ContinueForInactiveRendererEditor;
import com.jci.timetracker.view.gui.window.trackedActionsTable.cells.CustomTableCellRenderer;
import com.jci.timetracker.view.gui.window.trackedActionsTable.cells.MainActivityCellEditor;
import com.jci.timetracker.view.gui.window.trackedActionsTable.cells.SubActivityCellEditor;

public class TrackedActionsTable extends JTable
{
	private static final long serialVersionUID = 1L;
	private TrackedActionsTableModel model;
	private TableRowSorter<TrackedActionsTableModel> sorter;

	public TrackedActionsTable()
	{
		initiate();
	}

	/**
	 * Inicializacia combooxu
	 */
	private void initiate()
	{
		model = new TrackedActionsTableModel();
		this.setModel(model);

		Listener.getInstance().addLanguageChanged(new ListenerResponse<LanguageChangedListener>()
		{
			@Override
			public void call(LanguageChangedListener e)
			{
				// If language changed, fire this event, so column headers get updated
				((TrackedActionsTableModel) model).fireTableStructureChanged();
			}
		});

		this.setFillsViewportHeight(true);
		this.setRowSelectionAllowed(true);

		// Select one row only
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Hide vertical lines
		this.setShowVerticalLines(false);

		this.setFont(MainWindow.DEFAULT_FONT);

		this.setRowHeight(22);

		this.setDefaultRenderer(Activity.class, new CustomTableCellRenderer());
		this.setDefaultRenderer(Level.class, new CustomTableCellRenderer());
		this.setDefaultRenderer(Object.class, new CustomTableCellRenderer());
		this.setDefaultRenderer(Integer.class, new CustomTableCellRenderer());
		this.setDefaultRenderer(Date.class, new CustomTableCellRenderer());
		this.setDefaultRenderer(String.class, new CustomTableCellRenderer());
		this.setDefaultRenderer(Boolean.class, new ContinueForInactiveRendererEditor());

		if (this.convertColumnIndexToView(0) != -1) // Level 1
			this.getColumnModel().getColumn(this.convertColumnIndexToView(0)).setPreferredWidth(20);
		if (this.convertColumnIndexToView(1) != -1) // Level 2
			this.getColumnModel().getColumn(this.convertColumnIndexToView(1)).setPreferredWidth(20);
		if (this.convertColumnIndexToView(2) != -1) // Level 3
			this.getColumnModel().getColumn(this.convertColumnIndexToView(2)).setPreferredWidth(20);
		if (this.convertColumnIndexToView(3) != -1) // Main activity
		{
			TableColumn mainActivityColumn = this.getColumnModel().getColumn(this.convertColumnIndexToView(3));
			mainActivityColumn.setPreferredWidth(100);

			mainActivityColumn.setCellEditor(new MainActivityCellEditor());
		}
		if (this.convertColumnIndexToView(4) != -1) // Sub activity
		{
			TableColumn subActivityColumn = this.getColumnModel().getColumn(this.convertColumnIndexToView(4));
			subActivityColumn.setPreferredWidth(100);

			subActivityColumn.setCellEditor(new SubActivityCellEditor());
		}
		if (this.convertColumnIndexToView(6) != -1) // From
			this.getColumnModel().getColumn(this.convertColumnIndexToView(5)).setPreferredWidth(50);
		if (this.convertColumnIndexToView(6) != -1) // Count
			this.getColumnModel().getColumn(this.convertColumnIndexToView(6)).setMaxWidth(50);
		if (this.convertColumnIndexToView(7) != -1) // Comment
			this.getColumnModel().getColumn(this.convertColumnIndexToView(7)).setPreferredWidth(100);
		if (this.convertColumnIndexToView(8) != -1) // Continue for inactive
			this.getColumnModel().getColumn(this.convertColumnIndexToView(8)).setMaxWidth(30);

		// For filtering
		// sorter = new TableRowSorter<TrackedActionsTableModel>(model);
		// this.setRowSorter(sorter);

		this.setAutoResizeMode(AUTO_RESIZE_NEXT_COLUMN);
		this.getTableHeader().setReorderingAllowed(false);

		// When action is added, finished or updated, scroll to bottom
		Listener.getInstance().addTrackedActionsUpdated(new ListenerResponse<TrackedActionsUpdatedListener>()
		{
			@Override
			public void call(TrackedActionsUpdatedListener e)
			{
				scrollToBottom();
			}
		});

		Listener.getInstance().addTrackedActionAdded(new ListenerResponse<TrackedActionAddedListener>()
		{
			@Override
			public void call(TrackedActionAddedListener e)
			{
				scrollToBottom();
			}
		});

		Listener.getInstance().addTrackedActionUpdated(new ListenerResponse<TrackedActionUpdatedListener>()
		{
			@Override
			public void call(TrackedActionUpdatedListener e)
			{
				scrollToBottom();
			}
		});

		/**
		 * Hide unused levels columns - remove all and add only neede columns. This can be repeated multiple times
		 */
		Listener.getInstance().addLevelsUpdated(new ListenerResponse<LevelsUpdatedListener>()
		{
			TableColumn levels1Column = TrackedActionsTable.this.getColumnModel().getColumn(0);
			TableColumn levels2Column = TrackedActionsTable.this.getColumnModel().getColumn(1);
			TableColumn levels3Column = TrackedActionsTable.this.getColumnModel().getColumn(2);

			@Override
			public void call(LevelsUpdatedListener e)
			{
				TrackedActionsTable.this.removeColumn(levels1Column);
				TrackedActionsTable.this.removeColumn(levels2Column);
				TrackedActionsTable.this.removeColumn(levels3Column);

				if (e.getLevels3().size() > 0)
				{
					TrackedActionsTable.this.addColumn(levels3Column);
					TrackedActionsTable.this.moveColumn(TrackedActionsTable.this.getColumnCount() - 1, 0);
				}
				if (e.getLevels2().size() > 0)
				{
					TrackedActionsTable.this.addColumn(levels2Column);
					TrackedActionsTable.this.moveColumn(TrackedActionsTable.this.getColumnCount() - 1, 0);
				}
				if (e.getLevels1().size() > 0)
				{
					TrackedActionsTable.this.addColumn(levels1Column);
					TrackedActionsTable.this.moveColumn(TrackedActionsTable.this.getColumnCount() - 1, 0);
				}
			}
		});
	}

	private void scrollToBottom()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				TrackedActionsTable.this.scrollRectToVisible(TrackedActionsTable.this.getCellRect(TrackedActionsTable.this.getRowCount() - 1, 0, true));
			}
		});
	}

	/**
	 * Get table sorter
	 * 
	 * @return
	 */
	public TableRowSorter<TrackedActionsTableModel> getSorter()
	{
		return sorter;
	}

	/**
	 * Add rows to the table
	 */
	public void addRows(ArrayList<TrackedActionInTable> rows)
	{
		model.addRows(rows);
	}

	/**
	 * Add row to the table
	 * 
	 * @param object
	 */
	public void addRow(TrackedActionInTable rows)
	{
		model.addRow(rows);
	}

	/**
	 * Remove all rows from table (except header)
	 */
	public void emptyTable()
	{

		// sorter.setRowFilter(RowFilter.);
		model.clear();
	}

	/**
	 * Remove any background color for all frames in table
	 */
	public void cleanColors()
	{
		model.removeColors();
		this.repaint();
	}

	/**
	 * Put the column name in tooltip
	 */
	@Override
	protected JTableHeader createDefaultTableHeader()
	{
		return new JTableHeader(columnModel)
		{
			private static final long serialVersionUID = 1L;

			public String getToolTipText(MouseEvent e)
			{
				String tip = null;
				java.awt.Point p = e.getPoint();
				int index = columnModel.getColumnIndexAtX(p.x);
				int realIndex = columnModel.getColumn(index).getModelIndex();
				return this.getTable().getModel().getColumnName(realIndex);
			}
		};
	}
}
