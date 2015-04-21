package com.jci.timetracker.view.gui.window;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JScrollPane;

/**
 * Sprava sa ako JScrollPane, akurat po nastaveni {@code prefferedSize} vrati
 * objekt tejto triedy. Da sa tak v jednom riadky vytvorit ScrollPane a zaroven
 * nastavit velkost.
 * 
 * @author Laykou
 */
public class ScrollPane extends JScrollPane
{
	private static final long serialVersionUID = 101919126124L;

	/**
	 * Vytvorenie scrollovacieho boxu
	 * 
	 * @param component
	 *            Komponenta v scrollovacom boxe
	 */
	public ScrollPane(Component component)
	{
		super(component);
	}

	/**
	 * Nastavi preferovanu velkost komponenty a vrati tuto instanciu.
	 * 
	 * @param preferredSize
	 *            Preferovana velksot
	 * @return Instancia tohto scroll boxu
	 */
	public ScrollPane setPrefferedSize(Dimension preferredSize)
	{
		this.setPreferredSize(preferredSize);
		return this;
	}
}