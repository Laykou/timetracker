package com.jci.timetracker.controller;

import java.util.Date;

import com.jci.bbc.timetracker.model.TrackedAction;
import com.jci.timetracker.generalListener.Listener;
import com.jci.timetracker.language.Lang;
import com.jci.timetracker.language.LanguageListItem;
import com.jci.timetracker.settings.CurrentUser;

public class Controller
{
    /**
     * Select new language
     * 
     * @param language Language to be selected
     */
    public static void selectLanguage(LanguageListItem language)
    {
        try {
            Lang.loadLanguage(language.getKey());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Iterate through all not finished tracked actions and finish them
     */
    public static void finishTracking()
    {
        for (TrackedAction ta : CurrentUser.getInstance().getCurrentUser().getTrackedActions()) {
            if (ta.getFinish() == null) {
                ta.setFinish(new Date());
            }
        }
    }

    public static void clearTrackedActions()
    {
        // TrackedAction.clearTrackedActions();
    }

    public static void loadTrackedActions()
    {
        // CurrentUser.getInstance().getCurrentUser().refreshTrackedActions();
    }

    /**
     * Confirms all finished tracked actions.
     */
    public static void confirmTrackedActions()
    {
        for (TrackedAction ta : CurrentUser.getInstance().getCurrentUser().getTrackedActions()) {
            if (ta.getFinish() != null) {
                ta.setConfirmed(true);
                // Listener.getInstance().call(new TrackedActionRemovedListener(ta));
            }
        }
    }
}
