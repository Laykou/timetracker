package com.jci.bbc.timetracker.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.jci.bbc.timetracker.utils.Utils;

@Entity
@Table(name = "users")
public class User implements Serializable
{
    private static final long serialVersionUID = 4654897561L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String password;
    private String email;
    private String username;
    private String domain;
    private String permissions;
    private String tracker_version;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "default_level_1_id")
    private Level default_level_1;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "default_level_2_id")
    private Level default_level_2;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "default_level_3_id")
    private Level default_level_3;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "department_setting_id")
    private DepartmentSetting departmentSettings; // Current setting

    @OneToMany(mappedBy = "user", fetch=FetchType.EAGER)
    private List<TrackedAction> trackedActions = new ArrayList<TrackedAction>();

    public User()
    {

    }

    public User(String username, String domain)
    {
        this.username = username;
        this.domain = domain;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setPassword(String password)
    {
        this.password = Utils.hashPassword(password);
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getDomain()
    {
        return domain;
    }

    public void setDomain(String domain)
    {
        this.domain = domain;
    }

    public String getPermissions()
    {
        return permissions;
    }

    public void setPermissions(String permissions)
    {
        this.permissions = permissions;
    }

    public DepartmentSetting getDepartmentSettings()
    {
        return departmentSettings;
    }

    public void setDepartmentSettings(DepartmentSetting departmentSettings)
    {
        this.departmentSettings = departmentSettings;
    }

    public Level getDefault_level_1()
    {
        return default_level_1;
    }

    public void setDefault_level_1(Level default_level_1)
    {
        this.default_level_1 = default_level_1;
    }

    public Level getDefault_level_2()
    {
        return default_level_2;
    }

    public void setDefault_level_2(Level default_level_2)
    {
        this.default_level_2 = default_level_2;
    }

    public Level getDefault_level_3()
    {
        return default_level_3;
    }

    public void setDefault_level_3(Level default_level_3)
    {
        this.default_level_3 = default_level_3;
    }

    public void addTrackedAction(TrackedAction trackedAction)
    {
        trackedActions.add(trackedAction);
    }

    public List<TrackedAction> getTrackedActions()
    {
        return trackedActions;
    }

    /**
     * Track new action with no levels. Alias for {@link #createNewTrackedAction(Level, Level, Level, Action)}
     * 
     * @param action Action to be tracked
     */
    public void createNewTrackedAction(Action action)
    {
        createNewTrackedAction(null, null, null, action);
    }

    /**
     * Track new action with levels.
     * 
     * @param level1
     * @param level2
     * @param level3
     * @param action
     */
    public void createNewTrackedAction(Level level1, Level level2, Level level3, Action action)
    {
        closeLastTrackedAction();

        Activity mainActivity = null;
        Activity subActivity = null;

        // If action is not INACTIVE
        if (action != null) {
            // If action has parent, it is a subaction. Get main action from parent.
            if (action.getParent() != null) {
                mainActivity = action.getParent().getActivity();
                subActivity = action.getActivity();
            }
            else
            // If it does not have parent, it is a main activity
            {
                mainActivity = action.getActivity();
            }
        }

        TrackedAction trackedAction = new TrackedAction(level1, level2, level3, mainActivity, subActivity);
        trackedAction.save();

        addTrackedAction(trackedAction);

        // Listener.getInstance().call(new TrackedActionAddedListener(trackedAction));
    }

    /**
     * Close last tracked action (if any). Set finish to current time.
     */
    public void closeLastTrackedAction()
    {
        TrackedAction last = getLastTrackedAction();
        if (last != null) {
            if (last.getFinish() == null)
                last.setFinish(new Date());
        }
    }

    /**
     * Get last tracked action or null
     * 
     * @return
     */
    public TrackedAction getLastTrackedAction()
    {
        if (trackedActions.size() > 0) {
            return trackedActions.get(trackedActions.size() - 1);
        }

        return null;
    }

    public String getTrackerVersion()
    {
        return tracker_version;
    }

    public void setTrackerVersion(String tracker_version)
    {
        this.tracker_version = tracker_version;
    }

    public void setTrackedActions(List<TrackedAction> trackedActions)
    {
        this.trackedActions = trackedActions;
    }
}
