package com.jci.bbc.timetracker.web.controller;

import java.util.ArrayList;

import javax.ejb.EJB;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jci.bbc.timetracker.model.Category;
import com.jci.bbc.timetracker.sessionbeans.CategoryServiceLocal;

@Controller
@RequestMapping(value="/")
public class MemberController
{
    @EJB(mappedName = "java:global/timetracker.ear/timetracker.ejb/CategoryService!com.jci.bbc.timetracker.sessionbeans.CategoryServiceLocal")
    CategoryServiceLocal cateoryServiceLocal;
    
    @RequestMapping(method=RequestMethod.GET)
    public String displaySortedMembers(Model model)
    {
        Category category = cateoryServiceLocal.getCategoryById(1);
        model.addAttribute("category", category);
        model.addAttribute("members", new ArrayList());
        return "index";
    }

    
    /*
    @RequestMapping(method=RequestMethod.POST)
    public String registerNewMember(@Valid @ModelAttribute("newMember") Member newMember, BindingResult result, Model model)
    {
        if (!result.hasErrors()) {
            memberDao.register(newMember);
            return "redirect:/";
        }
        else {
            model.addAttribute("members", memberDao.findAllOrderedByName());
            return "index";
        }
    }
    */
}
