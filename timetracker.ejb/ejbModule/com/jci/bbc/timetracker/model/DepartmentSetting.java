package com.jci.bbc.timetracker.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "department_settings")
public class DepartmentSetting implements Serializable
{
    private static final long serialVersionUID = 45671233333464L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String subdepartment;

    @Column(columnDefinition = "bit")
    private Boolean allow_activity_editing = true;

    @ManyToMany
    @JoinTable(name = "r_department_settings_level_1", joinColumns = { @JoinColumn(name = "department_setting_id") }, inverseJoinColumns = { @JoinColumn(name = "level_id") })
    private List<Level> levels1;

    @ManyToMany
    @JoinTable(name = "r_department_settings_level_2", joinColumns = { @JoinColumn(name = "department_setting_id") }, inverseJoinColumns = { @JoinColumn(name = "level_id") })
    private List<Level> levels2;

    @ManyToMany
    @JoinTable(name = "r_department_settings_level_3", joinColumns = { @JoinColumn(name = "department_setting_id") }, inverseJoinColumns = { @JoinColumn(name = "level_id") })
    private List<Level> levels3;

    @OneToMany(mappedBy = "departmentSetting")
    private List<Action> parentActions;

    public DepartmentSetting()
    {

    }

    public DepartmentSetting(Integer id, String subdepartment, Boolean dsAllowActivityEditing)
    {
        this.id = id;
        this.subdepartment = subdepartment;
        this.allow_activity_editing = dsAllowActivityEditing;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getSubdepartment()
    {
        return subdepartment;
    }

    public void setSubdepartment(String subdepartment)
    {
        this.subdepartment = subdepartment;
    }

    public List<Level> getLevels1()
    {
        return levels1;
    }

    public void setLevels1(List<Level> levels1)
    {
        this.levels1 = levels1;
    }

    public List<Level> getLevels2()
    {
        return levels2;
    }

    public void setLevels2(List<Level> levels2)
    {
        this.levels2 = levels2;
    }

    public List<Level> getLevels3()
    {
        return levels3;
    }

    public void setLevels3(List<Level> levels3)
    {
        this.levels3 = levels3;
    }

    public void setParentActions(List<Action> parentActions)
    {
        this.parentActions = parentActions;
    }

    public List<Action> getParentActions()
    {
        return parentActions;
    }

    public Boolean getAllowActivityEditing()
    {
        return allow_activity_editing;
    }

    public void setAllowActivityEditing(Boolean allow_activity_editing)
    {
        this.allow_activity_editing = allow_activity_editing;
    }
}
