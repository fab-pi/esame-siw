package it.uniroma3.siw.taskmanager.controller;

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
import it.uniroma3.siw.taskmanager.model.Comments;
import it.uniroma3.siw.taskmanager.model.Tag;
import it.uniroma3.siw.taskmanager.model.User;
import it.uniroma3.siw.taskmanager.service.CommentsService;
import it.uniroma3.siw.taskmanager.service.ProjectService;
import it.uniroma3.siw.taskmanager.service.TaskService;
import it.uniroma3.siw.taskmanager.service.UserService;

@Controller
public class CommentsController {

	@Autowired
    ProjectService projectService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	SessionData sessionData;
	
	@Autowired
	CommentsService commentsService;
	
	 @RequestMapping(value = { "/projects/task/comments/{projectId}/{taskId}" }, method = RequestMethod.GET)
	 public String addTagToTask(Model model, @PathVariable Long projectId, @PathVariable Long taskId){
		 
		 User loggedUser = sessionData.getLoggedUser();
		 
		 model.addAttribute("loggedUser", loggedUser);
		 model.addAttribute("newCommentsForm", new Comments());
		 
		 return "addCommentsToTask";
	 }

 	@RequestMapping(value = { "/projects/task/comments/{projectId}/{taskId}" }, method = RequestMethod.POST)
	public String addTagToProjectForm(@PathVariable Long projectId,
								@PathVariable Long taskId,
								@Valid @ModelAttribute("newTagForm") Comments comments,
								BindingResult projectBindResut,
								Model model) {
		
 		this.commentsService.save(comments);
 		
 		this.taskService.addCommentsToTask(this.taskService.getTask(taskId), comments);
	 
	 	
		return "redirect:/projects/member/" + projectId;
	}
}
