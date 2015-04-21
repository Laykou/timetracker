package com.jci.timetracker.settings;

import java.util.HashMap;

import com.jci.bbc.timetracker.model.Setting;

/**
 * Getting and setting system settings/options.
 * 
 * @author Ladislav Gallay
 * 
 */
public class Settings
{
	private static HashMap<String, String> systemSettings = null;

	/**
	 * Load all settings at init
	 */
	static
	{
		reloadSettings();
	}

	/**
	 * Update existing setting in database. If not exists create such a setting.
	 * 
	 * @param name
	 *            Name of the setting
	 * @param value
	 *            Value of the setting
	 */
	public static void updateSetting(String name, String value)
	{
		try
		{
			Setting settingObject = null;

			if (settingObject == null)
			{
				settingObject = new Setting();
				settingObject.setName(name);
			}

			settingObject.setValue(value);

			reloadSettings();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Get setting's value. Null returned if not exist.
	 * 
	 * @param name
	 *            Name of the setting
	 * @return String value or null if not found
	 */
	public static String getSetting(String name)
	{
		return systemSettings.get(name);
	}

	/**
	 * Reload all settings into system's memory
	 */
	private static void reloadSettings()
	{
		try
		{
			systemSettings = new HashMap<String, String>();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
