package com.jci.timetracker.language;

/**
 * Vytvorenie položky jazyka, ktora obsahuje kód a titulok jazyka.
 * 
 * @author Laykou
 * @see Lang
 */
public class LanguageListItem
{
	/**
	 * Kod jazyka
	 */
	private String key;

	/**
	 * Nazov jazyka - v danom jazyku
	 */
	private String title;

	/**
	 * Vytvorenie polozky jazyka
	 * 
	 * @param key
	 *            Kod jazyka
	 * @param title
	 *            Nazov jazyka v danom jazyku
	 */
	public LanguageListItem(String key, String title)
	{
		this.key = key;
		this.title = title;
	}

	/**
	 * Zistenie kodu jazyka
	 * 
	 * @return Kod jazyka
	 */
	public String getKey()
	{
		return key;
	}

	/**
	 * Zistenie nazvu jazyka
	 * 
	 * @return Nazov jazyka
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * Vrati nazov jazyka
	 * 
	 * @see #getTitle()
	 */
	@Override public String toString()
	{
		return getTitle();
	}
}
