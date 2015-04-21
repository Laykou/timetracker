package com.jci.timetracker.view.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import com.jci.timetracker.language.Lang;
import com.jci.timetracker.view.gui.window.ControllerPanel;
import com.jci.timetracker.view.gui.window.MainMenu;
import com.jci.timetracker.view.gui.window.trackedActionsTable.TrackedActionsTable;

public class MainWindow extends JFrame
{
	private static final long serialVersionUID = 1L;
	private MainMenu mainMenu;

	private TrackedActionsTable trackedActionsTable;
	private ControllerPanel controllerPanel;
	private JScrollPane controllerPanelPane;

	public static Font DEFAULT_FONT = new Font("Serif", Font.PLAIN, 14);

	/**
	 * Get public instance
	 */
	public static MainWindow instance;

	public MainWindow()
	{
		instance = this;

		initialize();
	}

	/**
	 * Initialize window
	 */
	private void initialize()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			System.err.println("Unsported Windows style");
		}

		this.setIconImage(getToolkit().getImage(getClass().getResource("/com/jci/timetracker/icon32.png")));

		trackedActionsTable = new TrackedActionsTable();
		controllerPanel = new ControllerPanel();

		this.setTitle(Lang.getS("general.title") + " " + com.jci.timetracker.utils.Version.VERSION);
		this.setSize(1100, 600);
		this.getContentPane().setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		// Minimal window dimensions
		this.setMinimumSize(new Dimension(550, 400));

		JScrollPane trackedActionsTablePane = new JScrollPane(trackedActionsTable);
		trackedActionsTablePane.setPreferredSize(new Dimension(800, 300));

		/**
		 * Grid constraint pre zarovnanie obsahu zalozky do praveho horneho rohu
		 */
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.weighty = 1;
		gbc.weightx = 1;

		// Pseudo wrapper to align content (controllerPanel) top-right
		final JPanel controllerPanelPanel = new JPanel(new GridBagLayout());
		controllerPanelPanel.add(controllerPanel, gbc);

		// Make the controllerPanel scrollable if too many elements
		controllerPanelPane = new JScrollPane(controllerPanelPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		controllerPanelPane.getVerticalScrollBar().setUnitIncrement(16);

		this.addComponent(trackedActionsTablePane, 0, 0, BorderLayout.CENTER);
		this.addComponent(controllerPanelPane, 0, 0, BorderLayout.LINE_END);

		mainMenu = new MainMenu();
		this.setJMenuBar(mainMenu);

		// Center the window to the screen center
		this.setLocationRelativeTo(null);
	}

	public void showWindow()
	{
		controllerPanelPane.revalidate();
		controllerPanelPane.updateUI();
		this.setVisible(true);
	}

	/**
	 * Specifikuje pridanie komponentu do kontainera.
	 * 
	 * @param component
	 *            Pridavana komponenta
	 * @param setPrefferedWidth
	 *            Preferovana sirka komponenty
	 * @param setPrefferedHeight
	 *            Preferovana vyska komponenty
	 * @param BL
	 *            BorderLayou constraint - sposob zarovnania a spravania sa
	 */
	private void addComponent(Component component, int setPrefferedWidth, int setPrefferedHeight, String BL)
	{
		if (setPrefferedWidth != 0 && setPrefferedHeight != 0)
			component.setPreferredSize(new Dimension(setPrefferedWidth, setPrefferedHeight));
		this.add(component, BL);
	}
}
