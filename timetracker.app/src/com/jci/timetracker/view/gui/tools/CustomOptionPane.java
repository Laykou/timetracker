package com.jci.timetracker.view.gui.tools;

import javax.swing.JOptionPane;

/**
 * 
 * @author Ladislav Gallay
 * 
 */
public class CustomOptionPane
{
	/**
	 * JOptionPane has read-only value <b>maxCharactersPerLineCount</b> that limits characters per line. This method returns <b>JOptionPane</b> with custom maxCharactersPerLineCount. This cause long line to wrap in message dialogs.
	 * 
	 * @param maxCharactersPerLineCount
	 *            Number of character per line
	 * @return Custom JOptionPane object that will wrap long lines
	 */
	public static JOptionPane getNarrowOptionPane(int maxCharactersPerLineCount)
	{
		// Our inner class definition
		class NarrowOptionPane extends JOptionPane
		{
			private static final long serialVersionUID = 1L;
			int maxCharactersPerLineCount;

			NarrowOptionPane(int maxCharactersPerLineCount)
			{
				this.maxCharactersPerLineCount = maxCharactersPerLineCount;
			}

			public int getMaxCharactersPerLineCount()
			{
				return maxCharactersPerLineCount;
			}
		}

		return new NarrowOptionPane(maxCharactersPerLineCount);
	}
}
