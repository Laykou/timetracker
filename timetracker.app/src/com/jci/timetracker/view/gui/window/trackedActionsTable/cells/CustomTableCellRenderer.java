package com.jci.timetracker.view.gui.window.trackedActionsTable.cells;

import java.awt.Component;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.jci.bbc.timetracker.model.Activity;
import com.jci.bbc.timetracker.model.Level;
import com.jci.timetracker.view.gui.MainWindow;
import com.jci.timetracker.view.gui.window.trackedActionsTable.TrackedActionsTableModel;

/**
 * To create left padding around the cell
 */
public class CustomTableCellRenderer extends DefaultTableCellRenderer
{
	private static final long serialVersionUID = 1L;

	private static SimpleDateFormat dateTimeRenderer = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	private static SimpleDateFormat timeRenderer = new SimpleDateFormat("dd.MM. HH:mm");

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col)
	{
		TrackedActionsTableModel model = (TrackedActionsTableModel) table.getModel();
		int modelCol = table.getColumnModel().getColumn(col).getModelIndex();

		if (value instanceof Date)
			value = timeRenderer.format((Date) value);

		if (value instanceof Level)
			value = ((Level) value).getLabel();

		if (value instanceof Activity)
			value = ((Activity) value).getLabel();

		// If MainActivity is NULL, it is INACTIVE.
		// Get column index from model (where main activity is at 3rd index). In table this may be changed, because some Level Columns may be hidden
		if (modelCol == 3 && value == null)
		{
			value = "INACTIVE";
			setFont(MainWindow.DEFAULT_FONT.deriveFont(Font.BOLD));
		}

		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

		if (value instanceof Integer) // Align integer to right
			setHorizontalAlignment(JLabel.RIGHT);

		setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

		if (!isSelected)
		{
			// When sorted, rows can be in different order than in model.
			int modelRowIndex = table.convertRowIndexToModel(row);
			setBackground(model.getRowColor(modelRowIndex));
		}

		return this;
	}
}