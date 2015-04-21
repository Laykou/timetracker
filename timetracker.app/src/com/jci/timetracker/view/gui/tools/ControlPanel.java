package com.jci.timetracker.view.gui.tools;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Podobne ako ControlTab ale pre JPanel only
 * 
 * @author Laykou
 */
public class ControlPanel extends JPanel
{
	private static final long serialVersionUID = 1988L;

	/**
	 * Default okraj okolo komponentov
	 */
	protected static final Insets insets = new Insets(5, 5, 5, 5);

	/**
	 * Pocitadlo riadkov na zalozke
	 */
	private int rowCounter = 0;

	public ControlPanel()
	{
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
	 * @param insets
	 *            Borders around the element
	 * @param fill
	 *            Vyplnenie priestoru policka/policok
	 */
	protected void addComponent(final Component component, final int gridx, final int gridy, final int gridwidth, final int gridheight, final int anchor, final int fill, final Insets insets)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				GridBagConstraints gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, 1.0, 1.0, anchor, fill, insets, 0, 0);
				ControlPanel.this.add(component, gbc);
				revalidateBubbleUp(ControlPanel.this);
			}
		});
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
	public void addComponent(Component component, int gridx, int gridy, int gridwidth, int gridheight, int anchor, int fill)
	{
		addComponent(component, gridx, gridy, gridwidth, gridheight, anchor, fill, insets);
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
	protected void addComponent(final Component component, final int gridx, final int gridy, final int gridwidth, final int gridheight, final double weightx, final double weighty, final int anchor, final int fill)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				GridBagConstraints gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, insets, 0, 0);
				ControlPanel.this.add(component, gbc);
				revalidateBubbleUp(ControlPanel.this);
			}
		});
	}

	/**
	 * Vrati riadok, na ktory mozeme komponentu vykreslit a zvysi pocitadlo. Takto sa vzdy ziska posledny riadok, kam mozeme komponentu umiestnit.<br>
	 * Pre opatovne zistenie riadka zavolajte {@link #getLastRow()}.
	 * 
	 * @return Riadok, kam mozeme komponentu umiestnit
	 */
	public int getRow()
	{
		return rowCounter++;
	}

	/**
	 * Vrati posledny riadok, kam sme umiestnili komponentu (ked sme predtym volali {@link #getRow()}). Pocitadlo nezvysi.
	 * 
	 * @return Posledny riadok, kam sme umiestnili komponentu.
	 */
	protected int getLastRow()
	{
		return (rowCounter == 0) ? 0 : rowCounter - 1;
	}

	private static void revalidateBubbleUp(JComponent comp)
	{
		if (comp == null)
			return;

		comp.revalidate();
		comp.repaint();
	}
}
