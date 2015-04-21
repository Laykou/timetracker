package com.jci.timetracker.view.gui.language;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;

import com.jci.timetracker.generalListener.Listener;
import com.jci.timetracker.generalListener.ListenerResponse;
import com.jci.timetracker.generalListener.listeners.LanguageChangedListener;
import com.jci.timetracker.language.Lang;

/**
 * Uprava vsetkych labelov, buttonov, textov v GUI pri zmene jazyka. Vyuziva sa na nastavenie titulku a zaroven sa vytvori listener, ktory sa zavola vzdy, ked dojde k zmene jazyka a zabezpeci zmenu titulku jazyka pre danu komponentu.<br>
 * <br>
 * F
 * 
 * @author Laykou
 */
public class GuiLang
{

	/**
	 * Uprava komponentov Label
	 * 
	 * @param label Label, ktoremu sa upravi titulok
	 * @param key Kluc polozky jazyka
	 */
	public static void update(final JLabel label, final String key)
	{
		ListenerResponse<LanguageChangedListener> listener = new ListenerResponse<LanguageChangedListener>()
		{
			@Override
			public void call(LanguageChangedListener e)
			{
				label.setText(Lang.getS(key));
			}
		};

		Listener.getInstance().addLanguageChanged(listener);
		listener.call(null);
	}

	/**
	 * Uprava komponentov Button
	 * 
	 * @param button Button, ktoremu sa upravi titulok
	 * @param key Kluc polozky jazyka
	 */
	public static void update(final JButton button, final String key)
	{
		ListenerResponse<LanguageChangedListener> listener = new ListenerResponse<LanguageChangedListener>()
		{
			@Override
			public void call(LanguageChangedListener e)
			{
				button.setText(Lang.getS(key));
			}
		};

		Listener.getInstance().addLanguageChanged(listener);
		listener.call(null);
	}

	/**
	 * Uprava komponentov Checkbox
	 * 
	 * @param checkbox Checkbox, ktoremu sa upravi titulok
	 * @param key Kluc polozky jazyka
	 */
	public static void update(final JCheckBox checkbox, final String key)
	{
		ListenerResponse<LanguageChangedListener> listener = new ListenerResponse<LanguageChangedListener>()
		{
			@Override
			public void call(LanguageChangedListener e)
			{
				checkbox.setText(Lang.getS(key));
			}
		};

		Listener.getInstance().addLanguageChanged(listener);
		listener.call(null);
	}

	/**
	 * Uprava komponentov TextArea
	 * 
	 * @param textarea Textarea, ktorej sa upravi obsah
	 * @param key Kluc polozky jazyka
	 */
	public static void update(final JTextArea textarea, final String key)
	{
		ListenerResponse<LanguageChangedListener> listener = new ListenerResponse<LanguageChangedListener>()
		{
			@Override
			public void call(LanguageChangedListener e)
			{
				textarea.setText(Lang.getS(key));
			}
		};

		Listener.getInstance().addLanguageChanged(listener);
		listener.call(null);
	}

	/**
	 * Uprava komponentov TextField
	 * 
	 * @param textfield Textfield, ktoremu sa upravi obsah
	 * @param key Kluc polozky jazyka
	 */
	public static void update(final JTextField textfield, final String key)
	{
		ListenerResponse<LanguageChangedListener> listener = new ListenerResponse<LanguageChangedListener>()
		{
			@Override
			public void call(LanguageChangedListener e)
			{
				textfield.setText(Lang.getS(key));
			}
		};

		Listener.getInstance().addLanguageChanged(listener);
		listener.call(null);
	}

	/**
	 * Uprava nazvu zalozky
	 * 
	 * @param tabbedPane Panel so zalozkami
	 * @param index Index zalozky
	 * @param key Kluc polozky jazyka
	 */
	public static void update(final JTabbedPane tabbedPane, final int index, final String key)
	{
		ListenerResponse<LanguageChangedListener> listener = new ListenerResponse<LanguageChangedListener>()
		{
			@Override
			public void call(LanguageChangedListener e)
			{
				tabbedPane.setTitleAt(index, Lang.getS(key));
				tabbedPane.setToolTipTextAt(index, Lang.getS(key + "_tooltip"));
			}
		};

		Listener.getInstance().addLanguageChanged(listener);
		listener.call(null);
	}

	/**
	 * Uprava komponentov TableColumn
	 * 
	 * @param tableColumn TableColumn, ktoremu sa upravi obsah
	 * @param key Kluc polozky jazyka
	 */
	public static void update(final TableColumn tableColumn, final String key)
	{
		ListenerResponse<LanguageChangedListener> listener = new ListenerResponse<LanguageChangedListener>()
		{
			@Override
			public void call(LanguageChangedListener e)
			{
				tableColumn.setHeaderValue(Lang.getS(key));
			}
		};

		Listener.getInstance().addLanguageChanged(listener);
		listener.call(null);
	}

	/**
	 * Uprava komponentov DefaultMutableTreeNode
	 * 
	 * @param defaultMutableTreeNode DefaultMutableTreeNode, ktoremu sa upravi obsah
	 * @param key Kluc polozky jazyka
	 */
	public static void update(final DefaultMutableTreeNode defaultMutableTreeNode, final String key)
	{
		ListenerResponse<LanguageChangedListener> listener = new ListenerResponse<LanguageChangedListener>()
		{
			@Override
			public void call(LanguageChangedListener e)
			{
				defaultMutableTreeNode.setUserObject(Lang.getS(key));
			}
		};

		Listener.getInstance().addLanguageChanged(listener);
		listener.call(null);
	}

	/**
	 * Uprava komponentov JMenu
	 * 
	 * @param menu JMenu, ktoremu sa upravi obsah
	 * @param key Kluc polozky jazyka
	 */
	public static void update(final JMenu menu, final String key)
	{
		ListenerResponse<LanguageChangedListener> listener = new ListenerResponse<LanguageChangedListener>()
		{
			@Override
			public void call(LanguageChangedListener e)
			{
				menu.setText(Lang.getS(key));
			}
		};

		Listener.getInstance().addLanguageChanged(listener);
		listener.call(null);
	}

	/**
	 * Uprava komponentov JMenuItem
	 * 
	 * @param menu JMenuItem, ktoremu sa upravi obsah
	 * @param key Kluc polozky jazyka
	 */
	public static void update(final JMenuItem menu, final String key)
	{
		ListenerResponse<LanguageChangedListener> listener = new ListenerResponse<LanguageChangedListener>()
		{
			@Override
			public void call(LanguageChangedListener e)
			{
				menu.setText(Lang.getS(key));
			}
		};

		Listener.getInstance().addLanguageChanged(listener);
		listener.call(null);
	}
}
