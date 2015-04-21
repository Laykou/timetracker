package com.jci.timetracker.settings;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.HashMap;

import com.jci.bbc.timetracker.model.Activity;
import com.jci.bbc.timetracker.model.Level;
import com.jci.bbc.timetracker.model.User;
import com.jci.bbc.timetracker.sessionbeans.UserServiceRemote;
import com.jci.timetracker.generalListener.Listener;
import com.jci.timetracker.generalListener.ListenerResponse;
import com.jci.timetracker.generalListener.listeners.ActionsUpdatedListener;
import com.jci.timetracker.generalListener.listeners.LevelsUpdatedListener;
import com.jci.timetracker.generalListener.listeners.SettingsLoadedListener;
import com.jci.timetracker.generalListener.listeners.TrackedActionAddedListener;
import com.jci.timetracker.generalListener.listeners.TrackedActionClearedListener;
import com.jci.timetracker.generalListener.listeners.TrackedActionRemovedListener;
import com.jci.timetracker.generalListener.listeners.TrackedActionUpdatedListener;
import com.jci.timetracker.generalListener.listeners.TrackedActionsUpdatedListener;
import com.jci.timetracker.generalListener.listeners.UserSettingsChangedListener;
import com.jci.timetracker.requester.ejb.Request;
import com.jci.timetracker.requester.ejb.RequestMessage;
import com.jci.timetracker.utils.MigrationSerializerObjectInputStream;
import com.jci.timetracker.utils.Version;
import com.sun.security.auth.module.NTSystem;

/**
 * Get username and domain for current user. Get host name of current computer.
 * 
 * @author Ladislav Gallay
 * 
 */
public class CurrentUser implements Serializable
{
    private static final long serialVersionUID = 6413216576633365L;

    private String username = null;
    private String domain = null;
    private String host = null;

    private User user = null;

    /**
     * Filename where the settings is stored
     */
    private static String filename = "user_settings.ser";

    /**
     * Singleton for current user
     */
    private static CurrentUser instance = null;

    static {
        instance = new CurrentUser();

        // On any update save local settings
        Listener.getInstance().addSettingsLoadedListener(new ListenerResponse<SettingsLoadedListener>() {
            @Override
            public void call(SettingsLoadedListener e)
            {
                CurrentUser.writeSettings();
            }
        });
        Listener.getInstance().addTrackedActionAdded(new ListenerResponse<TrackedActionAddedListener>() {
            @Override
            public void call(TrackedActionAddedListener e)
            {
                CurrentUser.writeSettings();
            }
        });
        Listener.getInstance().addTrackedActionCleared(new ListenerResponse<TrackedActionClearedListener>() {
            @Override
            public void call(TrackedActionClearedListener e)
            {
                CurrentUser.writeSettings();
            }
        });
        Listener.getInstance().addTrackedActionUpdated(new ListenerResponse<TrackedActionUpdatedListener>() {
            @Override
            public void call(TrackedActionUpdatedListener e)
            {
                CurrentUser.writeSettings();
            }
        });
        Listener.getInstance().addTrackedActionRemoved(new ListenerResponse<TrackedActionRemovedListener>() {
            @Override
            public void call(TrackedActionRemovedListener e)
            {
                CurrentUser.writeSettings();
            }
        });
        Listener.getInstance().addTrackedActionsUpdated(new ListenerResponse<TrackedActionsUpdatedListener>() {
            @Override
            public void call(TrackedActionsUpdatedListener e)
            {
                CurrentUser.writeSettings();
            }
        });
        Listener.getInstance().addUserSettingsChangedListener(new ListenerResponse<UserSettingsChangedListener>() {
            @Override
            public void call(UserSettingsChangedListener e)
            {
                CurrentUser.writeSettings();
            }
        });
    }

    public static CurrentUser getInstance()
    {
        return instance;
    }

    private CurrentUser()
    {
        try {
            java.net.InetAddress localMachine = null;
            localMachine = java.net.InetAddress.getLocalHost();
            host = localMachine.getHostName();
        }
        catch (UnknownHostException e) {
        }

        NTSystem system = new NTSystem();
        username = system.getName();
        domain = system.getDomain();
    }

    public void init()
    {
        user = new User(username, domain);

        Request.add("UserService", new RequestMessage() {
            private static final long serialVersionUID = 8456468761234L;

            @Override
            public void call(Object service)
            {
                try {
                    UserServiceRemote userService = (UserServiceRemote) service;
                    CurrentUser.this.user = userService.findOrCreateUser(CurrentUser.this.username);
                    CurrentUser.this.updateUser();
                    CurrentUser.this.user = userService.save(CurrentUser.this.user);
                    
                    Listener.getInstance().call(new SettingsLoadedListener());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        updateUser();
    }

    private void updateUser()
    {
        user.setDomain(domain);
        user.setTrackerVersion(String.valueOf(Version.VERSION));
    }

    /**
     * Get username of current user logged in
     * 
     * @return
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * Get domain name for current user logged in
     * 
     * @return
     */
    public String getDomain()
    {
        return domain;
    }

    /**
     * Get name of workstation (machine, computer).
     * 
     * @return Hostname
     */
    public String getHost()
    {
        return host;
    }

    public User getCurrentUser()
    {
        if (user == null) {
            init();
        }

        return user;
    }

    /**
     * Reads all requests from the saving file into memory
     */
    public static void readSettings() throws Exception
    {
        FileInputStream fileIn = new FileInputStream(filename);
        ObjectInputStream in = new MigrationSerializerObjectInputStream(fileIn);
        instance = (CurrentUser) in.readObject();
        in.close();
        fileIn.close();

        Listener.getInstance().call(new ActionsUpdatedListener(instance.getCurrentUser().getDepartmentSettings().getParentActions()));
        Listener.getInstance().call(new LevelsUpdatedListener(instance.getCurrentUser().getDepartmentSettings().getLevels1(), instance.getCurrentUser().getDepartmentSettings().getLevels2(), instance.getCurrentUser().getDepartmentSettings().getLevels3()));
        Listener.getInstance().call(new SettingsLoadedListener());
        Listener.getInstance().call(new TrackedActionsUpdatedListener(instance.getCurrentUser().getTrackedActions()));
    }

    /**
     * Store all current unprocessed request into file (they will be executed later).
     */
    private static void writeSettings()
    {
        try {
            FileOutputStream fileOut = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(instance);
            out.close();
            fileOut.close();
        }
        catch (IOException i) {
            i.printStackTrace();
        }
    }
}
