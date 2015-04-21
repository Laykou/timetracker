package com.jci.bbc.timetracker.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Action is mapping of Activity to Department Settings and allows hierarchy (subactivities).
 * 
 * @author Ladislav Gallay
 * @see Activity
 * 
 */

@Entity
@Table(name = "actions")
public class Action implements Serializable
{
    private static final long serialVersionUID = 98454127843L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "activity_id")
    private Activity activity;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Action parent;

    @OneToMany
    @JoinColumn(referencedColumnName = "parent_id")
    private List<Action> subactions = new ArrayList<Action>();

    @ManyToOne
    @JoinColumn(name = "department_setting_id")
    DepartmentSetting departmentSetting;

    public Action()
    {

    }

    public Action(Integer id, Activity activity, Action parent)
    {
        this.id = id;
        this.activity = activity;
        this.parent = parent;

        if (parent != null) {
            parent.addSubaction(this);
        }
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Activity getActivity()
    {
        return activity;
    }

    public void setActivity(Activity activity)
    {
        this.activity = activity;
    }

    public Action getParent()
    {
        return parent;
    }

    public void setParent(Action parent)
    {
        this.parent = parent;
    }

    public List<Action> getSubactions()
    {
        return subactions;
    }

    public void addSubaction(Action subaction)
    {
        subactions.add(subaction);
    }

    public void setSubactions(List<Action> subactions)
    {
        this.subactions = subactions;
    }

    @Override
    public String toString()
    {
        if (getActivity() != null)
            return getActivity().getLabel();
        else
            return "No activity";
    }
}
