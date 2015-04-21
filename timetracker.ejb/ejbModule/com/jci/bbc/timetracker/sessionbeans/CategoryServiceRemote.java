package com.jci.bbc.timetracker.sessionbeans;

import java.util.List;

import javax.ejb.Remote;

import com.jci.bbc.timetracker.model.Category;

@Remote
public interface CategoryServiceRemote
{
	void addCategory(Category category);

	void updateCategory(Category category);

	Category getCategoryById(int id);

	List<Category> getAllCategories();
}
