package com.jci.timetracker.generalListener.listeners;

import com.jci.timetracker.language.Lang;
import com.jci.timetracker.language.LanguageListItem;

/**
 * Spusti sa v pripade zmeny jazyka. Obsahuje novy jazyk, ktory bol zmeneny.<br>
 * Napr. v {@link Lang#loadLanguage(String)}
 * 
 * @author Laykou
 * @see Lang
 */
public class LanguageChangedListener implements EventListener
{
	private LanguageListItem language;

	/**
	 * Vyvolat zmenu jazyka
	 * 
	 * @param l
	 *            Jazyk, ktory je prave aktualny
	 */
	public LanguageChangedListener(LanguageListItem l)
	{
		language = l;
	}

	/**
	 * @return Vrati titulok jazyka, ktory je prave aktualny
	 */
	public String getLanguage()
	{
		return language.getTitle();
	}

	/**
	 * @return Vrati objekt <b>LanguageListItem</b> jazyka, ktory je prave
	 *         aktualny
	 */
	public LanguageListItem getLanguageItem()
	{
		return language;
	}
}
