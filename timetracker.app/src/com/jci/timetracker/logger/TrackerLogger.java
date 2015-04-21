package com.jci.timetracker.logger;

import java.io.File;
import java.io.IOException;
import java.util.Formatter;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.jci.timetracker.run.GuiRunner;
import com.jci.timetracker.settings.CurrentUser;

public class TrackerLogger
{
	static private FileHandler fileTxt;
	static private SimpleFormatter formatterTxt;

	static private FileHandler fileHTML;
	static private Formatter formatterHTML;

	static public void setup() throws IOException
	{
		// get the global logger to configure it
		Logger logger = Logger.getLogger("");

		logger.setLevel(Level.INFO);
		fileTxt = new FileHandler("log" + File.separator + CurrentUser.getInstance().getUsername() + "_logger_" + GuiRunner.startupTime + ".txt");
		// fileHTML = new FileHandler("Logging.html");

		// create a TXT formatter
		formatterTxt = new SimpleFormatter();
		fileTxt.setFormatter(formatterTxt);
		logger.addHandler(fileTxt);

		// create an HTML formatter
		// formatterHTML = new MyHtmlFormatter();
		// fileHTML.setFormatter(formatterHTML);
		// logger.addHandler(fileHTML);
	}
}
