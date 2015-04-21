package com.jci.timetracker.view.gui.window;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.jci.timetracker.view.gui.language.GuiLang;

public class TextAreaBox extends JPanel
{
	final private JLabel label = new JLabel();
	private JTextArea textArea = null;

	public TextAreaBox(JTextArea textArea, String label)
	{
		this.setLayout(new BorderLayout());
		this.textArea = textArea;

		GuiLang.update(this.label, label);

		this.add(this.label, BorderLayout.BEFORE_FIRST_LINE);
		this.add(new JScrollPane(this.textArea), BorderLayout.CENTER);
	}
}
