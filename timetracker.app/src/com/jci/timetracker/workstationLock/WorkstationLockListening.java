package com.jci.timetracker.workstationLock;

import java.util.logging.Logger;

import com.jci.timetracker.generalListener.Listener;
import com.jci.timetracker.generalListener.ListenerResponse;
import com.jci.timetracker.generalListener.listeners.ClosingListener;
import com.jci.timetracker.generalListener.listeners.ScreenLockedListener;
import com.jci.timetracker.generalListener.listeners.ScreenUnlockedListener;
import com.jci.timetracker.generalListener.listeners.SessionLogOffListener;
import com.jci.timetracker.run.GuiRunner;
import com.jci.timetracker.view.gui.MainWindow;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HMODULE;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.MSG;
import com.sun.jna.platform.win32.WinUser.WNDCLASSEX;
import com.sun.jna.platform.win32.WinUser.WindowProc;
import com.sun.jna.platform.win32.Wtsapi32;

/**
 * The Class Win32WindowTest.
 * 
 * @author Ladislav Gallay
 * @source http://stackoverflow.com/questions/10228145/how-to-detect-workstation-system-screen-lock-unlock-in-windows-os-using-java
 */
public class WorkstationLockListening implements WindowProc
{
	private final static Logger logger = Logger.getLogger(WorkstationLockListening.class.getName());

	/**
	 * Instantiates a new win32 window test.
	 */
	public WorkstationLockListening(MainWindow window)
	{
		// define new window class
		final WString windowClass = new WString("MyWindowClass");
		final HMODULE hInst = Kernel32.INSTANCE.GetModuleHandle("");

		WNDCLASSEX wClass = new WNDCLASSEX();
		wClass.hInstance = hInst;
		wClass.lpfnWndProc = WorkstationLockListening.this;
		wClass.lpszClassName = windowClass;

		// register window class
		User32.INSTANCE.RegisterClassEx(wClass);
		getLastError();

		// create new window
		final HWND hWnd = User32.INSTANCE.CreateWindowEx(User32.WS_EX_TOPMOST, windowClass, "'TimeTracker hidden helper window to catch Windows events", 0, 0, 0, 0, 0, null, // WM_DEVICECHANGE contradicts parent=WinUser.HWND_MESSAGE
				null, hInst, null);

		getLastError();
		logger.info("WorkstationLock window sucessfully created! Window hwnd: " + hWnd.getPointer().toString());

		Wtsapi32.INSTANCE.WTSRegisterSessionNotification(hWnd, Wtsapi32.NOTIFY_FOR_THIS_SESSION);

		Listener.getInstance().addClosing(new ListenerResponse<ClosingListener>()
		{
			@Override
			public void call(ClosingListener e)
			{
				Wtsapi32.INSTANCE.WTSUnRegisterSessionNotification(hWnd);
				User32.INSTANCE.UnregisterClass(windowClass, hInst);
				User32.INSTANCE.DestroyWindow(hWnd);

				logger.info("WorkstationLock exit!");
			}
		});

		MSG msg = new MSG();
		while (User32.INSTANCE.GetMessage(msg, hWnd, 0, 0) != 0)
		{
			User32.INSTANCE.TranslateMessage(msg);
			User32.INSTANCE.DispatchMessage(msg);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jna.platform.win32.User32.WindowProc#callback(com.sun.jna.platform .win32.WinDef.HWND, int, com.sun.jna.platform.win32.WinDef.WPARAM, com.sun.jna.platform.win32.WinDef.LPARAM)
	 */
	public LRESULT callback(HWND hwnd, int uMsg, WPARAM wParam, LPARAM lParam)
	{
		switch (uMsg)
		{
			case WinUser.WM_DESTROY:
			{
				User32.INSTANCE.PostQuitMessage(0);
				return new LRESULT(0);
			}
			case WinUser.WM_SESSION_CHANGE:
			{
				this.onSessionChange(wParam, lParam);
				return new LRESULT(0);
			}
			default:
				return User32.INSTANCE.DefWindowProc(hwnd, uMsg, wParam, lParam);
		}
	}

	/**
	 * Gets the last error.
	 * 
	 * @return the last error
	 */
	public int getLastError()
	{
		int rc = Kernel32.INSTANCE.GetLastError();

		if (rc != 0)
			logger.severe("WorkstationLock: Error: " + rc);

		return rc;
	}

	/**
	 * On session change.
	 * 
	 * @param wParam
	 *            the w param
	 * @param lParam
	 *            the l param
	 */
	protected void onSessionChange(WPARAM wParam, LPARAM lParam)
	{
		switch (wParam.intValue())
		{
			case Wtsapi32.WTS_SESSION_LOCK:
			{
				this.onMachineLocked(lParam.intValue());
				break;
			}
			case Wtsapi32.WTS_SESSION_UNLOCK:
			{
				this.onMachineUnlocked(lParam.intValue());
				break;
			}

			case Wtsapi32.WTS_SESSION_LOGOFF:
			{
				this.onSessionLogOff(lParam.intValue());
				break;
			}
		}
	}

	/**
	 * On machine locked.
	 * 
	 * @param sessionId
	 *            the session id
	 */
	protected void onMachineLocked(int sessionId)
	{
		Listener.getInstance().call(new ScreenLockedListener(sessionId));
	}

	/**
	 * On machine unlocked.
	 * 
	 * @param sessionId
	 *            the session id
	 */
	protected void onMachineUnlocked(int sessionId)
	{
		Listener.getInstance().call(new ScreenUnlockedListener(sessionId));
	}

	/**
	 * On machine log off.
	 * 
	 * @param sessionId
	 *            the session id
	 */
	protected void onSessionLogOff(int sessionId)
	{
		Listener.getInstance().call(new SessionLogOffListener(sessionId));
	}
}