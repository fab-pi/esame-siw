package it.uniroma3.siw.taskmanager.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.taskmanager.controller.session.SessionData;
import it.uniroma3.siw.taskmanager.model.Project;
import it.uniroma3.siw.taskmanager.model.Tag;
import it.uniroma3.siw.taskmanager.model.User;
import it.uniroma3.siw.taskmanager.service.TagService;
import it.uniroma3.siw.taskmanager.service.TaskService;

@Controller
public class TagController {

	@Autowired
	SessionData sessionData;
	
	@Autowired
	TagService tagService;
	
	@Autowired
	TaskService taskService;
	
	 @RequestMapping(value = { "/projects/task/tag/{projectId}/{taskId}" }, method = RequestMethod.GET)
	 public String addTagToTask(Model model, @PathVariable Long projectId, @PathVariable Long taskId){
		 
		 User loggedUser = sessionData.getLoggedUser();
		 
		 model.addAttribute("loggedUser", loggedUser);
		 model.addAttribute("newTagForm", new Tag());
		 
		 return "addTagToTask";
	 }

 	@RequestMapping(value = { "/projects/task/tag/{projectId}/{taskId}" }, method = RequestMethod.POST)
	public String addTagToProjectForm(@PathVariable Long projectId,
								@PathVariable Long taskId,
								@Valid @ModelAttribute("newTagForm") Tag tag,
								BindingResult projectBindResut,
								Model model) {
		
	 	this.tagService.saveTag(tag);
		
	 	this.taskService.addTagToTask(this.taskService.getTask(taskId), tag);
	 	
		return "redirect:/projects/" + projectId;
	}
 	
 	@RequestMapping(value = { "/projects/task/tag/show/{projectId}/{taskId}" }, method = RequestMethod.GET)
	 public String showTagToTask(Model model, @PathVariable Long projectId, @PathVariable Long taskId){
		 
		 User loggedUser = sessionData.getLoggedUser();
		 
		 model.addAttribute("loggedUser", loggedUser);
		 model.addAttribute("projectId", projectId);
		 model.addAttribute("tags", this.taskService.getTags(taskId));
		 model.addAttribute("comments", this.taskService.getComments(taskId));
		 
		 return "showTag";
	 }
}
