package com.jci.timetracker.view.gui.objects;

import javax.swing.JMenuItem;

import com.jci.timetracker.language.LanguageListItem;

public class MenuItemLanguage extends JMenuItem
{
	private static final long serialVersionUID = 1L;
	private LanguageListItem language;

	public MenuItemLanguage(LanguageListItem language)
	{
		super(language.getTitle());

		this.language = language;
	}

	public LanguageListItem getLanguage()
	{
		return language;
	}

}
