package com.jci.timetracker.view.gui.tabs;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.jci.timetracker.view.gui.MainWindow;

/**
 * Implementuje zakladne metody, ktore maju byt dostupne pre kazdu zalozku.<br>
 * <br>
 * Napr. metoda {@link #addComponent(Component, int, int, int, int, int, int)},
 * ktora prida komponentu a nastavi jej {@link GridBagConstraints}.
 * 
 * @author Laykou
 */
public abstract class ControlTab extends JPanel
{
	private static final long serialVersionUID = 1988L;

	/**
	 * Default okraj okolo komponentov
	 */
	protected static final Insets insets = new Insets(5, 5, 5, 5);

	/**
	 * Hlavne okno
	 */
	protected MainWindow window;

	/**
	 * Referencia na komponentu, v ktorej sa tato zalozka nachadza
	 */
	private JTabbedPane tabbedPane;

	/**
	 * Pocitadlo riadkov na zalozke
	 */
	private int rowCounter = 0;

	protected ControlTab(MainWindow window, JTabbedPane tabbedPane)
	{
		this.window = window;
		this.tabbedPane = tabbedPane;
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	}

	/**
	 * Specifikuje pridanie komponentu do kontainera.
	 * 
	 * @param component
	 *            Komponenta
	 * @param gridx
	 *            X-ove policko
	 * @param gridy
	 *            Y-ove policko
	 * @param gridwidth
	 *            V kolkych polickach do sirky bude komponent
	 * @param gridheight
	 *            V kolkych polickach do vysky bude komponent
	 * @param anchor
	 *            Zarovnanie k okrajom
	 * @param fill
	 *            Vyplnenie priestoru policka/policok
	 */
	protected void addComponent(Component component, int gridx, int gridy, int gridwidth, int gridheight, int anchor, int fill)
	{
		GridBagConstraints gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, 1.0, 1.0, anchor, fill, insets, 0, 0);
		this.add(component, gbc);
	}

	/**
	 * Specifikuje pridanie komponentu do kontainera.
	 * 
	 * @param component
	 *            Komponenta
	 * @param gridx
	 *            X-ove policko
	 * @param gridy
	 *            Y-ove policko
	 * @param gridwidth
	 *            V kolkych polickach do sirky bude komponent
	 * @param gridheight
	 *            V kolkych polickach do vysky bude komponent
	 * @param weightx
	 *            Vaha velkosti mriezky v x-ovom smere
	 * @param weighty
	 *            Vaha velkosti mriezky v y-ovom smere
	 * @param anchor
	 *            Zarovnanie k okrajom
	 * @param fill
	 *            Vyplnenie priestoru policka/policok
	 */
	protected void addComponent(Component component, int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty, int anchor, int fill)
	{
		GridBagConstraints gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, insets, 0, 0);
		this.add(component, gbc);
	}

	/**
	 * Vrati instanciu hlavneho okna
	 * 
	 * @return Hlavne okno
	 */
	protected MainWindow getWindow()
	{
		return window;
	}

	/**
	 * Vrati instanciu zalozkoveho panela
	 * 
	 * @return Zalozkovy panel
	 */
	protected JTabbedPane getTabbedPane()
	{
		return tabbedPane;
	}

	/**
	 * Vrati riadok, na ktory mozeme komponentu vykreslit a zvysi pocitadlo.
	 * Takto sa vzdy ziska posledny riadok, kam mozeme komponentu umiestnit.<br>
	 * Pre opatovne zistenie riadka zavolajte {@link #getLastRow()}.
	 * 
	 * @return Riadok, kam mozeme komponentu umiestnit
	 */
	protected int getRow()
	{
		return rowCounter++;
	}

	/**
	 * Vrati posledny riadok, kam sme umiestnili komponentu (ked sme predtym
	 * volali {@link #getRow()}). Pocitadlo nezvysi.
	 * 
	 * @return Posledny riadok, kam sme umiestnili komponentu.
	 */
	protected int getLastRow()
	{
		return (rowCounter == 0) ? 0 : rowCounter - 1;
	}
}
