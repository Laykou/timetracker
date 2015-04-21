package com.jci.bbc.timetracker.sessionbeans;

import javax.ejb.Remote;

import com.jci.bbc.timetracker.model.DepartmentSetting;
import com.jci.bbc.timetracker.model.User;

@Remote
public interface UserServiceRemote {
    public User findOrCreateUser(String username);
    
    public User save(User user);
    
    public DepartmentSetting loadSettings(User user);
}
