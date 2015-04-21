package com.jci.timetracker.run;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import com.jci.timetracker.controller.Controller;
import com.jci.timetracker.generalListener.Listener;
import com.jci.timetracker.generalListener.ListenerResponse;
import com.jci.timetracker.generalListener.listeners.ClosingListener;
import com.jci.timetracker.generalListener.listeners.RequestsConnectionErrorListener;
import com.jci.timetracker.generalListener.listeners.ScreenLockedListener;
import com.jci.timetracker.generalListener.listeners.ScreenUnlockedListener;
import com.jci.timetracker.generalListener.listeners.SessionLogOffListener;
import com.jci.timetracker.generalListener.listeners.SettingsLoadedListener;
import com.jci.timetracker.language.Lang;
import com.jci.timetracker.logger.TrackerLogger;
import com.jci.timetracker.requester.ejb.RequestsWorker;
import com.jci.timetracker.settings.CurrentUser;
import com.jci.timetracker.view.gui.MainWindow;
import com.jci.timetracker.view.gui.tools.CustomOptionPane;
import com.jci.timetracker.view.gui.window.WelcomeSplashScreen;
import com.jci.timetracker.workstationLock.WorkstationLockListening;

/**
 * Run the program
 * 
 * @author Ladislav Gallay
 * 
 */
public class GuiRunner extends SwingWorker<Object, Object>
{
	private static MainWindow window;
	private static WelcomeSplashScreen splash;

	final static public long startupTime = System.currentTimeMillis();
	final static private File errorLogFile = new File("log" + File.separator + CurrentUser.getInstance().getUsername() + "_error_log_" + startupTime + ".txt");
	final static private File infoLogFile = new File("log" + File.separator + CurrentUser.getInstance().getUsername() + "_info_log_" + startupTime + ".txt");

	private final static Logger logger = Logger.getLogger(GuiRunner.class.getName());

	public static void main(String[] args) throws Exception
	{
		TrackerLogger.setup();

		logger.info("Application started");

		/**
		 * Redirect error log to the log file
		 */
		try
		{
			// errorLogFile.getParentFile().mkdirs();
			// errorLogFile.createNewFile();
			// System.setErr(new PrintStream(new FileOutputStream(errorLogFile)));
			//
			// infoLogFile.getParentFile().mkdirs();
			// infoLogFile.createNewFile();
			// System.setOut(new PrintStream(new FileOutputStream(infoLogFile)));
		}
		catch (Exception e1)
		{
			logger.log(Level.SEVERE, "", e1);
		}

		// Load splash screen in which the
		splash = new WelcomeSplashScreen(new GuiRunner());
	}

	/**
	 * When splash screen is loaded, it will call this method - initialize GuiRunner
	 */
	@Override
	protected Object doInBackground() throws Exception
	{
		try
		{
			EventQueue.invokeAndWait(new Runnable()
			{
				@Override
				public void run()
				{
					initialize();
				}
			});

			// After everything is loaded, create lock listeners to listen when workstation is locked/unlocked
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					new WorkstationLockListening(window);
				}
			}).start();
		}
		catch (Exception e)
		{
		}

		return null;
	}

	@Override
	protected void done()
	{
	}

	public void initialize()
	{
		/**
		 * If first attempt to connect to server is not successfull, show message, that we are running offline and try to load settings offline from file
		 */
		final ListenerResponse<RequestsConnectionErrorListener> applicationOffline = new ListenerResponse<RequestsConnectionErrorListener>()
		{
			private Boolean called = false;

			@Override
			public void call(RequestsConnectionErrorListener e)
			{
				// User should see this message only once by first request error
				if (!called)
				{
					called = true;
					JOptionPane noSettingsMessage = CustomOptionPane.getNarrowOptionPane(100);
					noSettingsMessage.setMessage(Lang.getS("general.offlineApplication"));
					noSettingsMessage.setMessageType(JOptionPane.INFORMATION_MESSAGE);

					JDialog noSettingsDialog = noSettingsMessage.createDialog(null, Lang.getS("general.title"));
					noSettingsDialog.setVisible(true);

					// Stop request worker for this application session
					RequestsWorker.getInstance().stopRunning();

					try
					{
						CurrentUser.readSettings();
					}
					catch (Exception e1)
					{
						JOptionPane settingsLoadingError = CustomOptionPane.getNarrowOptionPane(100);
						settingsLoadingError.setMessage(Lang.getS("general.settingsLoadingError"));
						settingsLoadingError.setMessageType(JOptionPane.INFORMATION_MESSAGE);

						JDialog settingsLoadingErrorDialog = settingsLoadingError.createDialog(null, Lang.getS("general.title"));
						settingsLoadingErrorDialog.setVisible(true);

						closeApplication();
					}
				}
			}
		};
		Listener.getInstance().addRequestsConnectionErrorListener(applicationOffline);

		window = new MainWindow();

		// Save current state to file on close
		window.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent we)
			{
				String ObjButtons[] = { Lang.getS("general.Yes"), Lang.getS("general.No") };
				int PromptResult = JOptionPane.showOptionDialog(null, Lang.getS("general.ExitDialog"), Lang.getS("general.title"), JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
				if (PromptResult == 0)
				{
					closeApplication();
				}
			}
		});

		// If screen locks, add INACTIVE action
		Listener.getInstance().addScreenLocked(new ListenerResponse<ScreenLockedListener>()
		{
			@Override
			public void call(ScreenLockedListener e)
			{
				CurrentUser.getInstance().getCurrentUser().createNewTrackedAction(null);
			}
		});

		// If screen unlocks, finish last action. It should be inactive.
		Listener.getInstance().addScreenUnlocked(new ListenerResponse<ScreenUnlockedListener>()
		{
			@Override
			public void call(ScreenUnlockedListener e)
			{
				if (CurrentUser.getInstance().getCurrentUser().getLastTrackedAction().getMainActivity() == null) // Last is INACTIVE
					CurrentUser.getInstance().getCurrentUser().closeLastTrackedAction();

				// Prompt user to start tracking after unlock
				JOptionPane startTrackingMessage = CustomOptionPane.getNarrowOptionPane(100);
				startTrackingMessage.setMessage(Lang.getS("general.startTrackingAfterUnlock"));
				startTrackingMessage.setMessageType(JOptionPane.INFORMATION_MESSAGE);

				JDialog startTrackingDialog = startTrackingMessage.createDialog(null, Lang.getS("general.title"));
				startTrackingDialog.setAlwaysOnTop(true);
				startTrackingDialog.setVisible(true);
			}
		});

		// If user is logging off (computer shutdown or anything else), finish last active action
		Listener.getInstance().addSessionLogOff(new ListenerResponse<SessionLogOffListener>()
		{
			@Override
			public void call(SessionLogOffListener e)
			{
				CurrentUser.getInstance().getCurrentUser().closeLastTrackedAction();
			}
		});

		/**
		 * After all settings are loaded, splash screen will be closed and main window will be shown
		 */
		Listener.getInstance().addSettingsLoadedListener(new ListenerResponse<SettingsLoadedListener>()
		{
			@Override
			public void call(SettingsLoadedListener e)
			{
				// No settings were loaded
				if (CurrentUser.getInstance().getCurrentUser().getDepartmentSettings() == null)
				{
					// Show information message that no settings were loaded and application will be closed
					JOptionPane noSettingsMessage = CustomOptionPane.getNarrowOptionPane(100);
					noSettingsMessage.setMessage(Lang.getS("general.noSettingsLoaded"));
					noSettingsMessage.setMessageType(JOptionPane.ERROR_MESSAGE);

					JDialog noSettingsDialog = noSettingsMessage.createDialog(null, Lang.getS("general.title"));
					noSettingsDialog.setVisible(true);

					closeApplication();
				}
				else
				{
					// After all settings are loaded
					splash.setVisible(false);
					window.showWindow();
				}
			}
		});

	   CurrentUser.getInstance().init();

		System.out.println("Loading user tracked actions");
		Controller.loadTrackedActions();
	}

	public void closeApplication()
	{
		window.setVisible(false);

		CurrentUser.getInstance().getCurrentUser().closeLastTrackedAction();

		Listener.getInstance().call(new ClosingListener());

		// Wait until or requests are done or execution is stopped (and it must be done in different thread.)
		// If closing is executed in RequestsWorkerLogger thread, than we have to exit
		while (!RequestsWorker.getInstance().equals(Thread.currentThread()) && RequestsWorker.getInstance().getState() == Thread.State.RUNNABLE)
			;

		System.out.println("Application exit!");
		System.exit(0);
	}
}
