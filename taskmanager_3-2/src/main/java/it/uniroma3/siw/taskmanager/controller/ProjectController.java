package it.uniroma3.siw.taskmanager.controller;

import java.util.List;

import javax.validation.Valid;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.taskmanager.controller.session.SessionData;
import it.uniroma3.siw.taskmanager.controller.validation.ProjectValidator;
import it.uniroma3.siw.taskmanager.model.Credentials;
import it.uniroma3.siw.taskmanager.model.Project;
import it.uniroma3.siw.taskmanager.model.Tag;
import it.uniroma3.siw.taskmanager.model.User;
import it.uniroma3.siw.taskmanager.service.CredentialsService;
import it.uniroma3.siw.taskmanager.service.ProjectService;
import it.uniroma3.siw.taskmanager.service.TagService;
import it.uniroma3.siw.taskmanager.service.UserService;

@Controller
public class ProjectController {

	@Autowired
    ProjectService projectService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	SessionData sessionData;
	
	@Autowired
	TagService tagService;
	
	@Autowired
	ProjectValidator projectValidator;
	
	@Autowired
	CredentialsService credentialService;
	
	 @RequestMapping(value = { "/projects/tag/{projectId}" }, method = RequestMethod.GET)
	 public String addTagToProject(Model model, @PathVariable Long projectId){
		 
		 User loggedUser = sessionData.getLoggedUser();
		 model.addAttribute("loggedUser", loggedUser);
		 model.addAttribute("tagForm", new Tag());
		 
		 return "addTagToProject";
	 }
	 
	 @RequestMapping(value = { "/projects/tag/{projectId}" }, method = RequestMethod.POST)
		public String addTagToProjectForm(@PathVariable Long projectId,
									@Valid @ModelAttribute("tagForm") Tag tag,
									BindingResult projectBindResut,
									Model model) {
			
		 	this.tagService.saveTag(tag);
			this.projectService.addTagProject(this.projectService.getProject(projectId), tag);
			
			return "redirect:/projects/" + projectId;
		}
	
	 @RequestMapping(value = { "/projects" }, method = RequestMethod.GET)
	 public String myOwnedProjects(Model model){
		 User loggedUser = sessionData.getLoggedUser();
		 List<Project> projects = this.projectService.retrieveProjectsOwnedBy(loggedUser);
		 model.addAttribute("loggedUser", loggedUser);
		 model.addAttribute("projects", projects);
		 
		 return "prova";
	 }
	 
	@RequestMapping(value = { "/projects/{projectId}" }, method = RequestMethod.GET)
	 public String project(Model model, @PathVariable Long projectId){
		 
		 User loggedUser = sessionData.getLoggedUser();
		 
		 Project project = projectService.getProject(projectId);
		 if(project==null)
			 return "redirect:/projects";
		 
		 List<User> members = userService.getMembers(project);
		 if(project.getOwner().equals(loggedUser)&& !members.contains(loggedUser))
			 return "redirect:/projects";
		 
		 List<Tag> tags = this.projectService.getAllTags(projectId);
		 
		 members.remove(project.getOwner());
		 model.addAttribute("loggedUser", loggedUser);
		 model.addAttribute("project", project);
		 model.addAttribute("members", members);
		 model.addAttribute("tags", tags);
		 
		 return "project";
	 }
	
	@RequestMapping(value = { "/projects/member/{projectId}" }, method = RequestMethod.GET)
	 public String memberProject(Model model, @PathVariable Long projectId){
		 
		 User loggedUser = sessionData.getLoggedUser();
		 
		 Project project = projectService.getProject(projectId);
		 if(project==null)
			 return "redirect:/projects";
		 
		 List<User> members = userService.getMembers(project);
		 members.remove(project.getOwner());
		 if(project.getOwner().equals(loggedUser)&& !members.contains(loggedUser))
			 return "redirect:/projects";
		 
		 List<Tag> tags = this.projectService.getAllTags(projectId);
		 
		 model.addAttribute("loggedUser", loggedUser);
		 model.addAttribute("project", project);
		 model.addAttribute("members", members);
		 model.addAttribute("tags", tags);
		 
		 return "projectMember";
	 }
	
	@RequestMapping(value = { "/projects/member/{username}/{projectId}" }, method = RequestMethod.GET)
	 public String memberProjectPlusUser(Model model, @PathVariable String username, @PathVariable Long projectId){
		 
		 User loggedUser = sessionData.getLoggedUser();
		 
		 Project project = projectService.getProject(projectId);
		 if(project==null)
			 return "redirect:/projects";
		 
		 List<User> members = userService.getMembers(project);
		 members.remove(project.getOwner());
		 if(project.getOwner().equals(loggedUser)&& !members.contains(loggedUser))
			 return "redirect:/projects";
		 
		 List<Tag> tags = this.projectService.getAllTags(projectId);
		 
		 model.addAttribute("loggedUser", loggedUser);
		 model.addAttribute("project", project);
		 model.addAttribute("members", members);
		 model.addAttribute("username", username);
		 model.addAttribute("tags", tags);
		 
		 return "projectMember";
	 }
	
	@RequestMapping(value = { "/projects/add" }, method = RequestMethod.GET)
	public String createProjectForm(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("projectForm", new Project());
		
		return "addProject";
	}
	
	@RequestMapping(value = { "/projects/add" }, method = RequestMethod.POST)
	public String createProject(@Valid @ModelAttribute("projectForm") Project project,
								BindingResult projectBindResut,
								Model model) {
		
		User loggedUser = sessionData.getLoggedUser();
		projectValidator.validate(project, projectBindResut);
		if(!projectBindResut.hasErrors()) {
			this.projectService.saveProject(project, loggedUser);
			return "redirect:/projects/" + project.getId();
		}
		
		model.addAttribute("loggedUser", loggedUser);
		
		return "addProject";
	}
	
	@RequestMapping(value = { "/projects/update/{projectId}" }, method = RequestMethod.GET)
	public String UpdateProject(Model model, @PathVariable Long projectId) {
		User loggedUser = sessionData.getLoggedUser();
		Project p = this.projectService.getProject(projectId);
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("updateProjectForm", p);
		
		return "updateProject";
	}
	
	@RequestMapping(value = { "/projects/update/{projectId}" }, method = RequestMethod.POST)
	public String UpdateProjectForm(@PathVariable Long projectId,
								    @Valid @ModelAttribute("updateProjectForm") Project project,
									BindingResult projectBindResut,
									Model model) {
		
		Project oldProject = this.projectService.getProject(projectId);
		this.projectService.updateProject(oldProject, project);
		
		return "redirect:/projects";
	}
	
	@RequestMapping(value = { "/projects/delete/{projectId}" }, method = RequestMethod.GET)
	public String deleteProject(Model model, @PathVariable Long projectId) {
		
		Project project = this.projectService.getProject(projectId);
		this.projectService.deleteProject(project);
		
		return "redirect:/projects";
	}
	
	@RequestMapping(value = { "/projects/share/{projectId}" }, method = RequestMethod.GET)
    public String userListShare(Model model , @PathVariable Long projectId) {
    	User loggedUser = sessionData.getLoggedUser();
    	List<Credentials> allCredential = this.credentialService.getAllCredentials();
    	
    	model.addAttribute("loggedUser", loggedUser);
    	model.addAttribute("credentialsList", allCredential);
    	
    	return "shareProject";
    }
	
	@RequestMapping(value = { "/projects/share/{projectId}/{username}"}, method = RequestMethod.POST)
	public String addMember(Model model,@PathVariable Long projectId, @PathVariable String username) {
		
		Project project = this.projectService.getProject(projectId);
		
		User user = this.userService.getUser(username);
		
		this.projectService.shareProjectWithUser(project, user);

		return "redirect:/projects/" + project.getId();
	}
}
	
	
	
	

