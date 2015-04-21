package com.jci.bbc.timetracker.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Tracked activities (actions).
 * 
 * @author Ladislav Gallay
 * @see Action
 * @see Activity
 * 
 */

@Entity
@Table(name = "tracked_actions")
public class TrackedAction implements Serializable
{
    private static final long serialVersionUID = 6871406907147807547L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id = null;

    @ManyToOne
    @JoinColumn(name = "level_1_id")
    private Level level1 = null;

    @ManyToOne
    @JoinColumn(name = "level_2_id")
    private Level level2 = null;

    @ManyToOne
    @JoinColumn(name = "level_3_id")
    private Level level3 = null;

    @ManyToOne
    @JoinColumn(name = "main_activity_id")
    private Activity main_activity = null;

    @ManyToOne
    @JoinColumn(name = "sub_activity_id")
    private Activity sub_activity = null;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user = null;

    private Date start = null;
    private Date finish = null;

    private Integer count = 1;
    private String comment = null;
    
    @Column(columnDefinition = "bit")
    private Boolean continue_for_inactive = false;

    // private static SimpleDateFormat datetimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * If the variable is confirmed on server side, so that client cannot edit it anymore.
     * This should be true for one-day old tracked events and older.
     */
    @Column(columnDefinition = "bit")
    private Boolean confirmed = false;

    public TrackedAction()
    {

    }

    public TrackedAction(Level level1, Level level2, Level level3, Activity mainActivity, Activity subActivity)
    {
        this.level1 = level1;
        this.level2 = level2;
        this.level3 = level3;
        this.main_activity = mainActivity;
        this.sub_activity = subActivity;
        this.start = new Date();
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Level getLevel1()
    {
        return level1;
    }

    public void setLevel1(Level level1)
    {
        this.level1 = level1;
    }

    public Level getLevel2()
    {
        return level2;
    }

    public void setLevel2(Level level2)
    {
        this.level2 = level2;
    }

    public Level getLevel3()
    {
        return level3;
    }

    public void setLevel3(Level level3)
    {
        this.level3 = level3;
    }

    public Date getStart()
    {
        return start;
    }

    public void setStart(Date start)
    {
        this.start = start;
    }

    public Date getFinish()
    {
        return finish;
    }

    public void setFinish(Date finish)
    {
        this.finish = finish;

        /**
         * Update in database when editing
         */
        save();

        // Listener.getInstance().call(new TrackedActionUpdatedListener(this));
    }

    public Integer getCount()
    {
        if (count == null)
            return 1; // Default count. This happened to be null when deserializng during debugging. Should not usually happen

        return count;
    }

    public void setCount(Integer count)
    {
        this.count = count;

        /**
         * Update in database when editing
         */
        save();

        // Listener.getInstance().call(new TrackedActionUpdatedListener(this));
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;

        /**
         * Update in database when editing
         */
        save();

        // Listener.getInstance().call(new TrackedActionUpdatedListener(this));
    }

    public Boolean getConfirmed()
    {
        return (confirmed != null && confirmed == true);
    }

    public void setConfirmed(Boolean confirmed)
    {
        this.confirmed = confirmed;

        /**
         * Update in database when editing
         */
        save();

        // Listener.getInstance().call(new TrackedActionUpdatedListener(this));
    }

    public Activity getMainActivity()
    {
        return main_activity;
    }

    public void setMainActivity(Activity mainActivity)
    {
        if (mainActivity != null && !mainActivity.equals(this.main_activity)) {
            this.main_activity = mainActivity;

            /**
             * Update in database when editing
             */
            save();

            // Listener.getInstance().call(new TrackedActionUpdatedListener(this));
        }
    }

    public Activity getSubActivity()
    {
        return sub_activity;
    }

    public void setSubActivity(Activity subActivity)
    {
        if (subActivity != null && !subActivity.equals(this.sub_activity)) {
            this.sub_activity = subActivity;

            /**
             * Update in database when editing
             */
            save();

            // Listener.getInstance().call(new TrackedActionUpdatedListener(this));
        }
    }

    public Boolean getContinueForInactive()
    {
        return (continue_for_inactive != null && continue_for_inactive == true);
    }

    public void setContinueForInactive(Boolean continueForInactive)
    {
        this.continue_for_inactive = continueForInactive;

        /**
         * Update in database when editing
         */
        save();

    }

    public void save()
    {
        // TODO: save?
    }
}
