package com.jci.bbc.timetracker.sessionbeans;

import java.util.List;

import javax.ejb.Local;

import com.jci.bbc.timetracker.model.Category;

@Local
public interface CategoryServiceLocal
{
	void addCategory(Category category);

	void updateCategory(Category category);

	Category getCategoryById(int id);

	List<Category> getAllCategories();
}
