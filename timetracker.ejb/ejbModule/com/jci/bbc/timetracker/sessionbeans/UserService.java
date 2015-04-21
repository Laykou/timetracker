package com.jci.bbc.timetracker.sessionbeans;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.jci.bbc.timetracker.model.DepartmentSetting;
import com.jci.bbc.timetracker.model.User;

/**
 * Session Bean implementation class UserService
 */
@Stateless
public class UserService implements UserServiceRemote, UserServiceLocal
{
    private static Logger logger = Logger.getLogger(UserService.class.getName());

    @PersistenceContext
    EntityManager em;

    public User findOrCreateUser(String username)
    {
        try {
            Query query = em.createQuery("SELECT u FROM User u WHERE username = :username").setParameter("username", username);
            return (User) query.getSingleResult();
        }
        catch (NoResultException e) {
            User user = new User();
            user.setUsername(username);
            em.persist(user);
            return user;
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "User not found and not created: " + username, e);
            return null;
        }
    }

    @Override
    /**
     * If the user exists in database, find the it by ID and update only.
     */
    public User save(User user)
    {
        if (user.getId() == null) {
            User existingUser = findOrCreateUser(user.getUsername());
            user.setId(existingUser.getId());
        }

        return em.merge(user);        
    }

    @Override
    public DepartmentSetting loadSettings(User user)
    {
        DepartmentSetting departmentSetting = user.getDepartmentSettings();
        return departmentSetting;
    }
}
