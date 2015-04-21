package com.jci.timetracker.view.gui.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

import com.jci.timetracker.language.Lang;

public class WelcomeSplashScreen extends JWindow
{
    private static final long serialVersionUID = 764313548641132177L;

    private SwingWorker<Object, Object> worker;

    public WelcomeSplashScreen(SwingWorker<Object, Object> worker)
	{
		this.worker = worker;

		EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}

				showSplash();

			}
		});
	}

	public void showSplash()
	{

		JPanel content = (JPanel) getContentPane();
		content.setBackground(new Color(0, 153, 255));

		// Set the window's bounds, centering the window
		int width = 700;
		int height = 450;
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width - width) / 2;
		int y = (screen.height - height) / 2;
		setBounds(x, y, width, height);

		// Build the splash screen
		JLabel loadingMessage = new JLabel(Lang.getS("general.loadingMessage"), JLabel.CENTER);
		loadingMessage.setForeground(Color.WHITE);

		loadingMessage.setLayout(new BorderLayout());

		Font font = loadingMessage.getFont();
		loadingMessage.setFont(font.deriveFont(Font.BOLD, 24f));

		content.add(loadingMessage, BorderLayout.CENTER);

		ImageIcon wait = new ImageIcon(getClass().getResource("/com/jci/timetracker/view/gui/resources/wait.gif"));
		loadingMessage.add(new JLabel(wait), BorderLayout.SOUTH);

		Color oraRed = new Color(0, 86, 159);
		content.setBorder(BorderFactory.createLineBorder(oraRed, 10));

		// Display it
		toFront();
		setVisible(true);

		// Do all necessary
		worker.execute();
	}
}