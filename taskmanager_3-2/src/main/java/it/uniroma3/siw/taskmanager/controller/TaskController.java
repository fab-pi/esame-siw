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
import it.uniroma3.siw.taskmanager.model.Task;
import it.uniroma3.siw.taskmanager.model.User;
import it.uniroma3.siw.taskmanager.service.ProjectService;
import it.uniroma3.siw.taskmanager.service.TaskService;
import it.uniroma3.siw.taskmanager.service.UserService;

@Controller
public class TaskController {

	@Autowired
	SessionData sessionData;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	ProjectService projectService;
	
	@Autowired
	UserService userService;
	
	@RequestMapping(value = { "/projects/task/member/{projectId}" }, method = RequestMethod.GET)
	public String selectMemberToTaskForm(Model model, @PathVariable Long projectId) {
		
		User loggedUser = sessionData.getLoggedUser();
		List<User> membri = this.projectService.getAllMembers(projectId);
		
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("projectId", projectId);
		model.addAttribute("membri", membri);
		
		return "selectMember";
	}
	
	@RequestMapping(value = { "/projects/task/member/{projectId}/{memberId}" }, method = RequestMethod.GET)
	public String createTaskForm(Model model, @PathVariable Long projectId, @PathVariable Long memberId) {
		
		User loggedUser = sessionData.getLoggedUser();
		
		
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("projectId", projectId);
		model.addAttribute("newTaskForm", new Task());
		
		return "addTask";
	}
	
	@RequestMapping(value = { "/projects/task/{memberId}/{projectId}" }, method = RequestMethod.POST)
	public String addTaskToProject(@PathVariable Long memberId,
								@PathVariable Long projectId,
								@Valid @ModelAttribute("newTaskForm") Task task,
								BindingResult projectBindResut,
								Model model) {
		
		Project project = this.projectService.getProject(projectId);
		
		User user = this.userService.getUser(memberId);
		
		this.taskService.saveTaskWithDelegated(task, user);
		
		this.projectService.addTaskProject(project, task);
		
		return "redirect:/projects/" + project.getId();
	}
	
	@RequestMapping(value = { "/projects/task/update/{taskId}" }, method = RequestMethod.GET)
	public String UpdateTask(Model model, @PathVariable Long taskId) {
		User loggedUser = sessionData.getLoggedUser();
		Task task = this.taskService.getTask(taskId);
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("updateTaskForm", task);
		
		return "updateTask";
	}
	
	@RequestMapping(value = { "/projects/task/update/{taskId}" }, method = RequestMethod.POST)
	public String UpdateTaskForm(@PathVariable Long taskId,
								@Valid @ModelAttribute("updateTaskForm") Task task,
								BindingResult projectBindResut,
								Model model) {
		
		Task oldTask = this.taskService.getTask(taskId);
		this.taskService.updateTask(oldTask, task);
		
		return "redirect:/projects";
	}
	
	@RequestMapping(value = { "/projects/task/delete/{taskId}" }, method = RequestMethod.GET)
	public String deleteTask(Model model, @PathVariable Long taskId) {
		
		Task task = this.taskService.getTask(taskId);
		this.taskService.deleteTask(task);
		
		return "redirect:/projects";
	}
	
	@RequestMapping(value = { "/projects/task/complete/{projectId}/{taskId}" }, method = RequestMethod.GET)
	public String UpdateTaskMember(Model model, @PathVariable Long projectId ,@PathVariable Long taskId) {
		User loggedUser = sessionData.getLoggedUser();
		Task task = this.taskService.getTask(taskId);
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("updateTaskForm", task);
		
		return "updateTaskMember";
	}
	
	@RequestMapping(value = { "/projects/task/complete/{projectId}/{taskId}" }, method = RequestMethod.POST)
	public String UpdateTaskFormMember(@PathVariable Long projectId,
										@PathVariable Long taskId,
										@Valid @ModelAttribute("updateTaskForm") Task task,
										BindingResult projectBindResut,
										Model model) {
		
		Task oldTask = this.taskService.getTask(taskId);
		this.taskService.updateTaskMember(oldTask, task);
		
		return "redirect:/projects/member/" + projectId;
	}
	
	
}
