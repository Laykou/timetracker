package com.jci.timetracker.view.gui.window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import com.jci.timetracker.controller.Controller;
import com.jci.timetracker.language.Lang;
import com.jci.timetracker.language.LanguageListItem;
import com.jci.timetracker.view.gui.language.GuiLang;
import com.jci.timetracker.view.gui.objects.MenuItemLanguage;

public class MainMenu extends JMenuBar implements ActionListener
{

	private static final long serialVersionUID = 1L;

	public MainMenu()
	{
		initialize();
	}

	private void initialize()
	{
		/**
		 * File work
		 */
		JMenu jmFile = new JMenu();
		GuiLang.update(jmFile, "gui.menu.file");

		// jmFile.addSeparator();
		jmFile.add(createMenuItem("exit"));

		this.add(jmFile);

		/**
		 * Tracking
		 */
		JMenu jmTracking = new JMenu();
		GuiLang.update(jmTracking, "gui.menu.tracking");

		jmTracking.add(createMenuItem("finishAction"));
		jmTracking.add(createMenuItem("confirm"));

		this.add(jmTracking);

		/**
		 * Change language (only if there are more than one languages available)
		 */
		if (Lang.getLanguages().size() > 1)
		{
			JMenu jmLanguage = new JMenu();
			GuiLang.update(jmLanguage, "gui.menu.language");

			for (LanguageListItem l : Lang.getLanguages())
			{
				MenuItemLanguage langItem = new MenuItemLanguage(l);
				langItem.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent arg0)
					{
						// Language selected
						LanguageListItem language = ((MenuItemLanguage) arg0.getSource()).getLanguage();
						Controller.selectLanguage(language);
					}
				});
				jmLanguage.add(langItem); // Add to menu
			}
			this.add(jmLanguage);
		}
	}

	/**
	 * Create command, assign lang update and actionCommand
	 * 
	 * @param command
	 * @return
	 */
	private JMenuItem createMenuItem(String command)
	{
		JMenuItem menuItem = new JMenuItem();
		menuItem.setActionCommand(command);
		GuiLang.update(menuItem, "gui.menu." + command);
		menuItem.addActionListener(this);

		return menuItem;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		String comStr = e.getActionCommand();

		if (comStr.equals("exit"))
		{
			JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
			topFrame.dispatchEvent(new WindowEvent(topFrame, WindowEvent.WINDOW_CLOSING));
		}

		if (comStr.equals("finishAction"))
		{
			Controller.finishTracking();
		}

		if (comStr.equals("confirm"))
		{
			Controller.confirmTrackedActions();
		}
	}
}
