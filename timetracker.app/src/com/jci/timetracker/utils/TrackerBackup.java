package com.jci.timetracker.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.jci.bbc.timetracker.model.TrackedAction;
import com.jci.timetracker.settings.CurrentUser;

/**
 * This class is responsible for storing all changes made to tracked actions into external file for backup
 * 
 * @author Ladislav Gallay
 * 
 */
public class TrackerBackup
{
	private static SimpleDateFormat datetimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat sheetNameDateformatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	private static ByteArrayOutputStream backupFileStream = new ByteArrayOutputStream();

	private static long currentTimeStamp = System.currentTimeMillis();
	private static File backupFileZip = new File("backup/" + CurrentUser.getInstance().getUsername() + "_actions_" + currentTimeStamp + ".zip");
	private static List<TrackedAction> trackedActions = new ArrayList<TrackedAction>();

	private static List<Object> header = new ArrayList<Object>();

	static
	{
		header = generateHeader();

		if (!backupFileZip.getParentFile().exists())
			backupFileZip.getParentFile().mkdirs();
	}

	/**
	 * Convert Tracked action into one line that can be saved into backup file
	 * 
	 * @param action
	 *            One action to be saved
	 * @return Array of objects that represents columns for this tracked action
	 */
	private static List<Object> actionToString(TrackedAction action)
	{
		List<Object> s = new ArrayList<Object>();

		s.add(action.getId());

		if (action.getLevel1() != null)
			s.add(action.getLevel1().getLabel());
		else
			s.add(null);

		if (action.getLevel2() != null)
			s.add(action.getLevel2().getLabel());
		else
			s.add(null);

		if (action.getLevel3() != null)
			s.add(action.getLevel3().getLabel());
		else
			s.add(null);

		if (action.getMainActivity() != null)
			s.add(action.getMainActivity().getLabel());
		else
			s.add("INACTIVE");

		if (action.getSubActivity() != null)
			s.add(action.getSubActivity().getLabel());
		else
			s.add(null);

		s.add(action.getCount());
		s.add(action.getComment());

		if (action.getStart() != null)
			s.add(datetimeFormatter.format(action.getStart()));
		else
			s.add(null);

		if (action.getFinish() != null)
			s.add(datetimeFormatter.format(action.getFinish()));
		else
			s.add(null);

		s.add(action.getContinueForInactive());
		s.add(action.getConfirmed());

		return s;
	}

	/**
	 * 
	 * @return List of header objects
	 */
	private static List<Object> generateHeader()
	{
		List<Object> s = new ArrayList<Object>();

		s.add("id");
		s.add("Level 1");
		s.add("Level 2");
		s.add("Level 3");
		s.add("Main activity");
		s.add("Subactivity");
		s.add("Count");
		s.add("Comment");
		s.add("Start");
		s.add("Finish");
		s.add("Continue inactive");
		s.add("Confirmed");

		return s;
	}

	/**
	 * Add new action to the list of action - to be backed up
	 * 
	 * @param a
	 *            New tracked action
	 */
	public static void createAction(TrackedAction a)
	{
		synchronized (trackedActions)
		{
			if (!trackedActions.contains(a))
				trackedActions.add(a);
		}

		saveFile();
	}

	/**
	 * Update action in backup file
	 * 
	 * @param a
	 *            Existing tracked action to be saved
	 */
	public static void updateAction(TrackedAction a)
	{
		/**
		 * It can be the same as createAction because createAction checks if such an action exists and if yes only saves backup file
		 */
		createAction(a);
	}

	/**
	 * This saves current list of tracked actions. This method should be called whenever a change is made to any action that have been previously added to list of action with {@link #createAction(TrackedAction)}.
	 */
	public static void updateActions()
	{
		saveFile();
	}

	/**
	 * Generate password to protect output excel file
	 * 
	 * @return
	 */
	private static String generatePassword()
	{
		String s = String.valueOf(currentTimeStamp);
		return CurrentUser.getInstance().getUsername() + s.substring(s.length() - 4);
	}

	private static void addCellsToSheet(WritableSheet sheet, int row, List<Object> cells)
	{
		int i = 0;

		for (Object o : cells)
		{
			try
			{
				if (o == null)
					sheet.addCell(new jxl.write.Blank(i, row));
				else if (o instanceof Date)
					sheet.addCell(new jxl.write.DateTime(i, row, (Date) o));
				else if (o instanceof Boolean)
					sheet.addCell(new jxl.write.Boolean(i, row, (Boolean) o));
				else if (o instanceof Integer)
					sheet.addCell(new jxl.write.Number(i, row, (Integer) o));
				else
					sheet.addCell(new jxl.write.Label(i, row, o.toString()));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				i++;
			}
		}
	}

	/**
	 * Save list of tracked actions into backup file
	 */
	private static void saveFile()
	{
		try
		{
			synchronized (trackedActions)
			{
				WorkbookSettings wbSettings = new WorkbookSettings();
				wbSettings.setLocale(new Locale("en", "EN"));

				// Create an Excel file
				backupFileStream.reset();
				WritableWorkbook workbook = Workbook.createWorkbook(backupFileStream, wbSettings);

				WritableSheet sheet = workbook.createSheet(sheetNameDateformatter.format(new Date()), 0);

				// Addding headers
				addCellsToSheet(sheet, 0, header);

				int row = 1;
				for (TrackedAction a : trackedActions)
					addCellsToSheet(sheet, row++, actionToString(a));

				workbook.write();
				workbook.close();

				backupFileZip.delete();
				ZipUtils.zipStreamWithAESEncryption(backupFileZip, generatePassword(), new ByteArrayInputStream(backupFileStream.toByteArray()));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
