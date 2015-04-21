package com.jci.timetracker.view.gui.window.trackedActionsTable.cells;

import java.awt.Component;
import java.util.HashMap;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;

import com.jci.bbc.timetracker.model.Action;
import com.jci.bbc.timetracker.model.Activity;
import com.jci.timetracker.generalListener.Listener;
import com.jci.timetracker.generalListener.ListenerResponse;
import com.jci.timetracker.generalListener.listeners.ActionsUpdatedListener;

public class MainActivityCellEditor extends DefaultCellEditor
{
	/**
	 * Decalre a model that is used for adding the elements to the `Combo box`
	 */
	private DefaultComboBoxModel model;

	/**
	 * To find action by activity. Only activity is stored to tracked action but to render combobox action is needed
	 */
	private HashMap<Activity, Action> activityToAction = new HashMap<Activity, Action>();

	public MainActivityCellEditor()
	{
		super(new JComboBox());
		this.model = (DefaultComboBoxModel) ((JComboBox) getComponent()).getModel();

		Listener.getInstance().addActionsUpdated(new ListenerResponse<ActionsUpdatedListener>()
		{
			@Override
			public void call(ActionsUpdatedListener e)
			{
				model.removeAllElements();
				activityToAction.clear();

				for (Action ma : e.getActions())
				{
					// Get only main actions
					if (ma.getParent() == null)
					{
						model.addElement(ma);
						activityToAction.put(ma.getActivity(), ma);
					}
				}
			}
		});
	}

	/**
	 * Return activity as selected value, because only activity is stored to tracked action
	 */
	@Override
	public Object getCellEditorValue()
	{
		return ((Action) model.getSelectedItem()).getActivity();
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
	{
		// Value is Activity, but combobox stores actions
		model.setSelectedItem(activityToAction.get(value));

		return super.getTableCellEditorComponent(table, value, isSelected, row, column);
	}

}
