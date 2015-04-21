package com.jci.bbc.timetracker.sessionbeans;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.jci.bbc.timetracker.model.Category;

/**
 * Session Bean implementation class AdminService
 */
@Stateless
@LocalBean
public class CategoryService implements CategoryServiceLocal, CategoryServiceRemote
{
    @PersistenceContext
    EntityManager em;

    /**
     * Default constructor.
     */
    public CategoryService()
    {
        // TODO Auto-generated constructor stub
    }

    @Override
	public void addCategory(Category category)
	{
        em.persist(category);
	}

    @Override
    public void updateCategory(Category category)
    {
        em.merge(category);
    }

    @Override
    public Category getCategoryById(int id)
    {
        return em.find(Category.class,  id);
    }

    @Override
    public List<Category> getAllCategories()
    {
        Query query = em.createQuery("SELECT c FROM Category c");
        return query.getResultList();
    }
}
