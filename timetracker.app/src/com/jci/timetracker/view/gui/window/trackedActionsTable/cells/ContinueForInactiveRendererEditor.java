package com.jci.timetracker.view.gui.window.trackedActionsTable.cells;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.jci.bbc.timetracker.model.Activity;
import com.jci.timetracker.view.gui.window.trackedActionsTable.TrackedActionsTableModel;

public class ContinueForInactiveRendererEditor extends JPanel implements TableCellRenderer, TableCellEditor, ActionListener
{
	private JCheckBox checkbox;

	CustomTableCellRenderer defaultRenderer;

	public ContinueForInactiveRendererEditor()
	{
		defaultRenderer = new CustomTableCellRenderer();

		checkbox = new JCheckBox();
		checkbox.setBorder(BorderFactory.createEmptyBorder());
		checkbox.setOpaque(false);
		checkbox.addActionListener(this);
		checkbox.setRequestFocusEnabled(false);
		setOpaque(true);
		add(checkbox);
		validate();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		stopCellEditing();
	}

	@Override
	public Object getCellEditorValue()
	{
		return checkbox.isSelected();
	}

	@Override
	public boolean isCellEditable(EventObject anEvent)
	{
		return true;
	}

	@Override
	public boolean shouldSelectCell(EventObject anEvent)
	{
		return true;
	}

	@Override
	public boolean stopCellEditing()
	{
		for (CellEditorListener listener : getListeners(CellEditorListener.class))
		{
			listener.editingStopped(new ChangeEvent(this));
		}
		return true;
	}

	@Override
	public void cancelCellEditing()
	{
		for (CellEditorListener listener : getListeners(CellEditorListener.class))
		{
			listener.editingCanceled(new ChangeEvent(this));
		}
	}

	@Override
	public void addCellEditorListener(CellEditorListener l)
	{
		this.listenerList.add(CellEditorListener.class, l);
	}

	@Override
	public void removeCellEditorListener(CellEditorListener l)
	{
		this.listenerList.remove(CellEditorListener.class, l);
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
	{
		configure(table, value, true, true, row, column);
		return this;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		configure(table, value, isSelected, hasFocus, row, column);
		return this;
	}

	private void configure(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		/**
		 * Rendere checkbox only if tracked action is not INACTIVE. It is INACTIVE when main activity is NULL.
		 */
		Activity mainAction = ((TrackedActionsTableModel) table.getModel()).getRow(row).getTrackedAction().getMainActivity();
		checkbox.setVisible(mainAction == null);

		if (value != null)
			checkbox.setSelected((Boolean) value);

		setBackground(defaultRenderer.getBackground());
		setBorder(BorderFactory.createEmptyBorder());
	}

}