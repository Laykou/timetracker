package com.jci.timetracker.generalListener;

import java.util.ArrayList;
import java.util.List;

import com.jci.timetracker.generalListener.listeners.ActionsUpdatedListener;
import com.jci.timetracker.generalListener.listeners.ClosingListener;
import com.jci.timetracker.generalListener.listeners.LanguageChangedListener;
import com.jci.timetracker.generalListener.listeners.LevelsUpdatedListener;
import com.jci.timetracker.generalListener.listeners.RequestsConnectionErrorListener;
import com.jci.timetracker.generalListener.listeners.ScreenLockedListener;
import com.jci.timetracker.generalListener.listeners.ScreenUnlockedListener;
import com.jci.timetracker.generalListener.listeners.SessionLogOffListener;
import com.jci.timetracker.generalListener.listeners.SettingsLoadedListener;
import com.jci.timetracker.generalListener.listeners.TrackedActionAddedListener;
import com.jci.timetracker.generalListener.listeners.TrackedActionClearedListener;
import com.jci.timetracker.generalListener.listeners.TrackedActionRemovedListener;
import com.jci.timetracker.generalListener.listeners.TrackedActionUpdatedListener;
import com.jci.timetracker.generalListener.listeners.TrackedActionsUpdatedListener;
import com.jci.timetracker.generalListener.listeners.UserSettingsChangedListener;

/**
 * Tato trieda zoskupuje vsetkych listenerov a vyvolavanie udalosti. Pomocou
 * 
 * <pre>
 * Listener.add<i>Nazov_Udalosti</i>(new ListenerResponse&lt;Nazov_Udalosti&gt;
 * 	{
 * 		public void call(Nazov_Udalosti e)
 * 		{
 * 			blok_kodu
 * 		}
 * 	}
 * );
 * </pre>
 * 
 * sa da pridat reakcia na danu udalost <i>Nazov_Udalosti</i>. Pomocou
 * 
 * <pre>
 * Listener.call(new Nazov_Udalosti());
 * </pre>
 * 
 * sa da vyvolat dana udalost.
 * 
 * @author Laykou
 */

public class Listener
{
    /**
     * Listener ma jedinu instanciu. Je v tejto premennej.
     */
    private static Listener listenerInstance = new Listener();

    private List<ListenerResponse<LanguageChangedListener>> languageChanged = new ArrayList<ListenerResponse<LanguageChangedListener>>();
     private List<ListenerResponse<ActionsUpdatedListener>> updatedActions = new ArrayList<ListenerResponse<ActionsUpdatedListener>>();
     private List<ListenerResponse<LevelsUpdatedListener>> updatedLevels = new ArrayList<ListenerResponse<LevelsUpdatedListener>>();
     private List<ListenerResponse<TrackedActionsUpdatedListener>> trackedActionsUpdated = new ArrayList<ListenerResponse<TrackedActionsUpdatedListener>>();
     private List<ListenerResponse<TrackedActionAddedListener>> trackedActionAdded = new ArrayList<ListenerResponse<TrackedActionAddedListener>>();
     private List<ListenerResponse<TrackedActionRemovedListener>> trackedActionRemoved = new ArrayList<ListenerResponse<TrackedActionRemovedListener>>();
     private List<ListenerResponse<TrackedActionUpdatedListener>> trackedActionUpdated = new ArrayList<ListenerResponse<TrackedActionUpdatedListener>>();
     private List<ListenerResponse<TrackedActionClearedListener>> trackedActionCleared = new ArrayList<ListenerResponse<TrackedActionClearedListener>>();
    private List<ListenerResponse<ScreenLockedListener>> screenLockedListener = new ArrayList<ListenerResponse<ScreenLockedListener>>();
    private List<ListenerResponse<ScreenUnlockedListener>> screenUnlockedListener = new ArrayList<ListenerResponse<ScreenUnlockedListener>>();
    private List<ListenerResponse<SessionLogOffListener>> sessionLogOffListener = new ArrayList<ListenerResponse<SessionLogOffListener>>();
    private List<ListenerResponse<ClosingListener>> closingListener = new ArrayList<ListenerResponse<ClosingListener>>();
     private List<ListenerResponse<SettingsLoadedListener>> settingsLoadedListener = new ArrayList<ListenerResponse<SettingsLoadedListener>>();
    private List<ListenerResponse<RequestsConnectionErrorListener>> requestsConnectionErrorListener = new ArrayList<ListenerResponse<RequestsConnectionErrorListener>>();

     private List<ListenerResponse<UserSettingsChangedListener>> userSettingsChangedListener = new ArrayList<ListenerResponse<UserSettingsChangedListener>>();

    /**
     * Existuje iba jedna instancia Listenera. Ta je ulozena v {@link #listenerInstance}. Pristupit sa k nej da cez {@link #getInstance()}
     */
    private Listener()
    {
    }

    /**
     * Vrati jedinu instanciu Listenera
     * 
     * @return Instancia listenera
     */
    public static Listener getInstance()
    {
        return listenerInstance;
    }

    public Listener addLanguageChanged(ListenerResponse<LanguageChangedListener> l)
    {
        languageChanged.add(l);
        return this;
    }

    public Listener call(LanguageChangedListener e)
    {
        for (ListenerResponse<LanguageChangedListener> x : languageChanged)
            x.call(e);
        return this;
    }

     public Listener addActionsUpdated(ListenerResponse<ActionsUpdatedListener> l)
     {
     updatedActions.add(l);
     return this;
     }
    
     public Listener call(ActionsUpdatedListener e)
     {
     for (ListenerResponse<ActionsUpdatedListener> x : updatedActions)
     x.call(e);
     return this;
     }
    
     public Listener addLevelsUpdated(ListenerResponse<LevelsUpdatedListener> l)
     {
     updatedLevels.add(l);
     return this;
     }
    
     public Listener call(LevelsUpdatedListener e)
     {
     for (ListenerResponse<LevelsUpdatedListener> x : updatedLevels)
     x.call(e);
     return this;
     }

    public Listener addClosing(ListenerResponse<ClosingListener> l)
    {
        closingListener.add(l);
        return this;
    }

    public Listener call(ClosingListener e)
    {
        for (ListenerResponse<ClosingListener> x : closingListener)
            x.call(e);
        return this;
    }

     public Listener addTrackedActionAdded(ListenerResponse<TrackedActionAddedListener> l)
     {
     trackedActionAdded.add(l);
     return this;
     }
    
     public Listener call(TrackedActionAddedListener e)
     {
     for (ListenerResponse<TrackedActionAddedListener> x : trackedActionAdded)
     x.call(e);
     return this;
     }
    
     public Listener addTrackedActionRemoved(ListenerResponse<TrackedActionRemovedListener> l)
     {
     trackedActionRemoved.add(l);
     return this;
     }
    
     public Listener call(TrackedActionRemovedListener e)
     {
     for (ListenerResponse<TrackedActionRemovedListener> x : trackedActionRemoved)
     x.call(e);
     return this;
     }
    
     public Listener addTrackedActionsUpdated(ListenerResponse<TrackedActionsUpdatedListener> l)
     {
     trackedActionsUpdated.add(l);
     return this;
     }
    
     public Listener call(TrackedActionsUpdatedListener e)
     {
     for (ListenerResponse<TrackedActionsUpdatedListener> x : trackedActionsUpdated)
     x.call(e);
     return this;
     }
    
     public Listener addTrackedActionUpdated(ListenerResponse<TrackedActionUpdatedListener> l)
     {
     trackedActionUpdated.add(l);
     return this;
     }
    
     public Listener call(TrackedActionUpdatedListener e)
     {
     for (ListenerResponse<TrackedActionUpdatedListener> x : trackedActionUpdated)
     x.call(e);
     return this;
     }
    
     public Listener addTrackedActionCleared(ListenerResponse<TrackedActionClearedListener> l)
     {
     trackedActionCleared.add(l);
     return this;
     }
    
     public Listener call(TrackedActionClearedListener e)
     {
     for (ListenerResponse<TrackedActionClearedListener> x : trackedActionCleared)
     x.call(e);
     return this;
     }

    public Listener addScreenLocked(ListenerResponse<ScreenLockedListener> l)
    {
        screenLockedListener.add(l);
        return this;
    }

    public Listener call(ScreenLockedListener e)
    {
        for (ListenerResponse<ScreenLockedListener> x : screenLockedListener)
            x.call(e);
        return this;
    }

    public Listener addScreenUnlocked(ListenerResponse<ScreenUnlockedListener> l)
    {
        screenUnlockedListener.add(l);
        return this;
    }

    public Listener call(ScreenUnlockedListener e)
    {
        for (ListenerResponse<ScreenUnlockedListener> x : screenUnlockedListener)
            x.call(e);
        return this;
    }

    public Listener addSessionLogOff(ListenerResponse<SessionLogOffListener> l)
    {
        sessionLogOffListener.add(l);
        return this;
    }

    public Listener call(SessionLogOffListener e)
    {
        for (ListenerResponse<SessionLogOffListener> x : sessionLogOffListener)
            x.call(e);
        return this;
    }

     public Listener addSettingsLoadedListener(ListenerResponse<SettingsLoadedListener> l)
     {
     settingsLoadedListener.add(l);
     return this;
     }
    
     public Listener call(SettingsLoadedListener e)
     {
     for (ListenerResponse<SettingsLoadedListener> x : settingsLoadedListener)
     x.call(e);
     return this;
     }

    public Listener addRequestsConnectionErrorListener(ListenerResponse<RequestsConnectionErrorListener> l)
    {
        requestsConnectionErrorListener.add(l);
        return this;
    }

    public Listener call(RequestsConnectionErrorListener e)
    {
        for (ListenerResponse<RequestsConnectionErrorListener> x : requestsConnectionErrorListener)
            x.call(e);
        return this;
    }

    public Listener addUserSettingsChangedListener(ListenerResponse<UserSettingsChangedListener> l)
    {
        userSettingsChangedListener.add(l);
        return this;
    }

    public Listener call(UserSettingsChangedListener e)
    {
        for (ListenerResponse<UserSettingsChangedListener> x : userSettingsChangedListener)
            x.call(e);
        return this;
    }
}
