package com.jci.timetracker.view.gui.window.trackedActionsTable.cells;

import java.awt.Component;
import java.util.HashMap;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;

import com.jci.bbc.timetracker.model.Action;
import com.jci.bbc.timetracker.model.Activity;
import com.jci.timetracker.generalListener.Listener;
import com.jci.timetracker.generalListener.ListenerResponse;
import com.jci.timetracker.generalListener.listeners.ActionsUpdatedListener;

public class SubActivityCellEditor extends DefaultCellEditor
{
	/**
	 * Decalre a model that is used for adding the elements to the `Combo box`
	 */
	private DefaultComboBoxModel model;

	/**
	 * To find action by main activity and subactivity. Only activity is stored to tracked action but to render combobox action is needed. For subactivity it is important to render only those subactivities for main activity that is selected
	 */
	private HashMap<Activity, List<Action>> activityToAction = new HashMap<Activity, List<Action>>();

	public SubActivityCellEditor()
	{
		super(new JComboBox());
		this.model = (DefaultComboBoxModel) ((JComboBox) getComponent()).getModel();

		Listener.getInstance().addActionsUpdated(new ListenerResponse<ActionsUpdatedListener>()
		{
			@Override
			public void call(ActionsUpdatedListener e)
			{
				activityToAction.clear();

				for (Action ma : e.getActions())
				{
					// Get only main actions
					if (ma.getParent() == null)
					{
						activityToAction.put(ma.getActivity(), ma.getSubactions()); // Map subactions to main activity
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
		// Get selected main activity for record. If this is editable, there must be main activity
		Activity mainactivity = (Activity) table.getModel().getValueAt(row, 3);
		List<Action> selectedSubactivities = activityToAction.get(mainactivity);

		// Refill combobox with subactivities for selected main activity for current tracked action
		model.removeAllElements();
		if (selectedSubactivities != null)
		{
			for (Action sa : selectedSubactivities)
			{
				model.addElement(sa);

				if (sa.getActivity().equals(value))
					model.setSelectedItem(sa);
			}
		}

		return super.getTableCellEditorComponent(table, value, isSelected, row, column);
	}
}
