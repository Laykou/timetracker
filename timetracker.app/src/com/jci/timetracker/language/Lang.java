package com.jci.timetracker.language;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jci.timetracker.generalListener.Listener;
import com.jci.timetracker.generalListener.listeners.LanguageChangedListener;

/**
 * Zabezpecuje pristup k jazykom - zobrazovanie hlasok a textov.<br>
 * <br>
 * Aplikacia umoznuje nastavenie jazykovej verzie. Tato trieda potom zabezpecuje zobrazovanie jazykov. Jazyky su ulozene ako zlozky v priecinku <i>languages</i>. Kazdy podpriecinok nesie v nazve kod jazyka. V nom sa nachadzaju subory jazyka.<br>
 * Subory maju nazov v tvare {@code gui.floorTab}, kde jednotlivymi bodkami su oddelovane sekcie programu - kvoli lepsej citatelnosti. Subor potom obsahuje nazvy jazykovych premennych a ich hodnoty v tvare
 * 
 * <pre>
 * nastala_chyba=Pozor, nastala chyba.
 * vsetko_ok=Vsetko je v poriadku.
 * vitaj=Vitaj uzivatel %s
 * </pre>
 * 
 * K danym hlaskam sa da potom pristupit pomocou metody {@link #getS(String)}
 * 
 * <pre>
 * Lang.getS("gui.floorTab.nastala_chyba")
 * Lang.getS("gui.floorTab.vsetko_ok")
 * String.format(Lang.getS("gui.floorTab.vsetko_ok"),"Martin")
 * </pre>
 * 
 * Novy jazyk sa nacita pomocou {@link #loadLanguage(String)}, pricom sa spusti listener {@link LanguageChangedListener}, takze sa da reagovat na zmenu jazyka a znovu vykreslit zobrazovaci interface.
 * 
 * @author Laykou
 * @see #getS(String)
 * @see Lang#loadLanguage(String)
 */
public class Lang
{
	/**
	 * Kod jazyka. Vychodzia hodnota je <i>en</i>.
	 */
	private static String language = "en";

	/**
	 * Mapa s kodmi jazyka a ich hodnotami.
	 */
	private static Map<String, String> lang = new HashMap<String, String>();

	/**
	 * Zoznam jazykov
	 * 
	 * @see LanguageListItem
	 */
	private static List<LanguageListItem> languages = new ArrayList<LanguageListItem>();

	/**
	 * List of language file names
	 */
	private static List<String> files = new ArrayList<String>();

	/**
	 * Vytvori zoznam jazykov a nacita defaultny jazyk. Toto sa udeje prakticky hned pri spusteni programu.
	 */
	static
	{
		try
		{
			List<String> directories = new ArrayList<String>();

			BufferedReader br = new BufferedReader(new InputStreamReader(Lang.class.getResource("languages.conf").openStream()));
			String line;
			Boolean readingLanguages = true;
			while ((line = br.readLine()) != null)
			{
				line = line.trim();
				if (!line.startsWith("--")) // Skip comments
				{
					if (line.equals("")) // Blank line separates list of languages from list of files
					{
						readingLanguages = false;
					}
					else
					{
						if (readingLanguages)
							directories.add(line);
						else
							files.add(line);
					}
				}
			}
			br.close();

			synchronized (getLanguages())
			{
				if (directories != null)
				{
					for (String d : directories)
					{
						HashMap<String, String> hm = parseFile("general", new InputStreamReader(Lang.class.getResource(d + "/general").openStream()));
						getLanguages().add(new LanguageListItem(d, hm.get("general.language")));
					}
				}
			}

			loadLanguage(); // Nacitame defaultny jazyk
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Iba staticky pristup - konstruktor nemoze byt zavolany
	 */
	private Lang()
	{
	}

	/**
	 * Funkcia vrati zadanu message podla kluca. Kluc je zadanvany v tvare
	 * 
	 * <pre>
	 * nazov_suboru.kluc_hlasky
	 * </pre>
	 * 
	 * Napriklad ak su v danom jazykovom subore {@code gui.floorTab} nasledujuce zaznamy:
	 * 
	 * <pre>
	 * nastala_chyba=Pozor, nastala chyba.
	 * vsetko_ok=Vsetko je v poriadku.
	 * vitaj=Vitaj uzivatel %s
	 * </pre>
	 * 
	 * Tak sa k nim da pristupit takto:
	 * 
	 * <pre>
	 * Lang.getS("gui.floorTab.nastala_chyba")
	 * Lang.getS("gui.floorTab.vsetko_ok")
	 * String.format(Lang.getS("gui.floorTab.vsetko_ok"),"Martin")
	 * </pre>
	 * 
	 * @param key
	 *            Kluc zaznamu
	 * @return Hodnota zaznamu. V pripade, ze zaznam nebol najdeny, vrati sa hodnota {@code NO_TRANSLATION: zadany_kluc}
	 */
	public static String getS(String key)
	{
		synchronized (getLang())
		{
			if (getLang().containsKey(key))
				return getLang().get(key);
			else
			{
				return "i18n: " + key;
				// String[] dots = key.split("\\.");
				// String output = dots[dots.length - 1];
				// output = output.substring(0, 1).toUpperCase() + output.substring(1);
				// return output;
			}
		}
	}

	/**
	 * Prida novu polozku do zoznamu hlasok.
	 * 
	 * @param key
	 *            Kluc polozky
	 * @param message
	 *            Hlaska
	 */
	public static void add(String key, String message)
	{
		synchronized (getLang())
		{
			getLang().put(key, message);
		}
	}

	/**
	 * Nacita vychodzi jazyk (prednastaveny je {@code sk}). Pozri metodu {@link #loadLanguage(String)}
	 * 
	 * @throws Exception
	 *             Napriklad ak nebol najdeny dany subor, alebo nastala chyba pri citani suboru.
	 * @see #loadLanguage(String)
	 */
	public static void loadLanguage() throws Exception
	{
		loadLanguage(language);
	}

	/**
	 * Nacita zadany jazyk. Zaroven sa spusti {@link Listener} {@link LanguageChangedListener}.
	 * 
	 * @param language
	 *            Kod jazyka (nazov zlozky v priecinku {@code messages}.
	 * @throws Exception
	 *             Napriklad ak nebol najdeny dany subor, alebo nastala chyba pri citani suboru.
	 */
	public static void loadLanguage(String language) throws Exception
	{
		Lang.language = language;

		synchronized (getLang())
		{
			getLang().clear();

			for (String f : files)
			{
				parseFile(null, f, new InputStreamReader(Lang.class.getResource(language + "/" + f).openStream()));
			}
		}

		Listener.getInstance().call(new LanguageChangedListener(findByKey(language)));
	}

	/**
	 * Rozdeli subor na riadky a z kazdy riadok rozdeli podla prveho vyskytu znaku <b>=</b>. Prvu cast pouzije ako key a druhu ako hodnotu textu.
	 * 
	 * @param prefix
	 *            Pokial nie je null, pouzije sa pred key v tvare <b>prefix.</b>key
	 * @param file
	 *            Subor, v ktorom su messages
	 * @throws Exception
	 *             Napriklad ak nebol najdeny dany subor, alebo nastala chyba pri citani suboru.
	 */
	private static void parseFile(String prefix, String fileName, InputStreamReader file) throws Exception
	{
		HashMap<String, String> hm = parseFile(fileName, file);

		for (Map.Entry<String, String> entry : hm.entrySet())
		{
			if (prefix == null)
				add(entry.getKey(), entry.getValue());
			else
				add(prefix + "." + entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Nacita a vyparsuj subor file do mapy. K�d mapy je kluc polozky jazyka a jeho hodnota je samotna hodnota jazyka.
	 * 
	 * @param file
	 *            Subor, v ktorom su messages
	 * @return Vyparsovana mapa {@code HashMap<String, String>}
	 * @throws Exception
	 *             Napriklad ak nebol najdeny dany subor, alebo nastala chyba pri citani suboru.
	 */
	private static HashMap<String, String> parseFile(String fileName, InputStreamReader stream) throws Exception
	{
		HashMap<String, String> hm = new HashMap<String, String>();

		try
		{
			BufferedReader br = new BufferedReader(stream);
			String line;
			while ((line = br.readLine()) != null)
			{
				line = line.trim();
				if (!line.equals("")) // Skip comments
				{
					String[] kv = line.split("\\=", 2); // Rozdeli subor podla prveho
					// rovna sa
					kv[0] = fileName + "." + kv[0]; // Prida ako prefix nazov suboru
					hm.put(kv[0], kv[1]);
				}
			}
			br.close();

			return hm;
		}
		finally
		{
			stream.close();
		}
	}

	/**
	 * Vrati zoznam jaykov. Pri pouzivani je dobre obali kod do
	 * 
	 * <pre>
	 * synchronized(getLanguages())
	 * {
	 * 	blok_kodu
	 * }
	 * </pre>
	 * 
	 * aby sa predislo problemom pri praci vo viacerych vlaknach.
	 * 
	 * @return Zoznam jazykov ako poloziek jazyka
	 * @see LanguageListItem
	 */
	public static List<LanguageListItem> getLanguages()
	{
		return languages;
	}

	/**
	 * N�jde jazyk pod�a k�du.
	 * 
	 * @param key
	 *            K�d jazyka.
	 * @return Vrati objekt {@link LanguageListItem} pre zadany k�d jazyka, alebo <b>null</b>, pokia� nena�iel ni�.
	 * @see LanguageListItem
	 */
	public static LanguageListItem findByKey(String key)
	{
		LanguageListItem l = null;

		synchronized (getLanguages())
		{
			for (LanguageListItem i : getLanguages())
				if (i.getKey().equals(key))
				{
					l = i;
					break;
				}
		}

		return l;
	}

	/**
	 * Vrati zoznam klucov a hodnot. Sluzi pre <i>private</i> ucely. Pri pouzivani je dobre obali kod do
	 * 
	 * <pre>
	 * synchronized(getLang())
	 * {
	 * 	blok_kodu
	 * }
	 * </pre>
	 * 
	 * aby sa predislo problemom pri praci vo viacerych vlaknach.
	 * 
	 * @return Hodnota v premennej {@link #lang}
	 */
	private static Map<String, String> getLang()
	{
		return lang;
	}

	/**
	 * Vrati k�d aktualneho jazyka
	 * 
	 * @return Kod aktualneho jazyka
	 */
	public static String getLanguage()
	{
		return language;
	}

}
