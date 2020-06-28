package it.uniroma3.siw.taskmanager.controller;

import it.uniroma3.siw.taskmanager.controller.session.SessionData;
import it.uniroma3.siw.taskmanager.controller.validation.UserValidator;
import it.uniroma3.siw.taskmanager.model.Credentials;
import it.uniroma3.siw.taskmanager.model.Project;
import it.uniroma3.siw.taskmanager.model.User;
import it.uniroma3.siw.taskmanager.repository.UserRepository;
import it.uniroma3.siw.taskmanager.service.CredentialsService;
import it.uniroma3.siw.taskmanager.service.ProjectService;
import it.uniroma3.siw.taskmanager.service.UserService;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapProperties.Credential;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * The UserController handles all interactions involving User data.
 */
@Controller
public class UserController {

    @Autowired
    UserValidator userValidator;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    SessionData sessionData;
    
    @Autowired
    CredentialsService credentialService;
    
    @Autowired
    UserService userService;
    
    @Autowired
    ProjectService projectService;

    /**
     * This method is called when a GET request is sent by the user to URL "/users/user_id".
     * This method prepares and dispatches the User registration view.
     *
     * @param model the Request model
     * @return the name of the target view, that in this case is "register"
     */
    @RequestMapping(value = { "/home" }, method = RequestMethod.GET)
    public String home(Model model) {
        User loggedUser = sessionData.getLoggedUser();
        model.addAttribute("user", loggedUser);
        return "home";
    }

    /**
     * This method is called when a GET request is sent by the user to URL "/users/user_id".
     * This method prepares and dispatches the User registration view.
     *
     * @param model the Request model
     * @return the name of the target view, that in this case is "register"
     */
    @RequestMapping(value = { "/users/me" }, method = RequestMethod.GET)
    public String me(Model model) {
        User loggedUser = sessionData.getLoggedUser();
        Credentials credentials = sessionData.getLoggedCredentials();
        System.out.println(credentials.getPassword());
        model.addAttribute("user", loggedUser);
        model.addAttribute("credentials", credentials);

        return "userProfile";
    }

    
    
    /**
     * This method is called when a GET request is sent by the user to URL "/users/user_id".
     * This method prepares and dispatches the User registration view.
     *
     * @param model the Request model
     * @return the name of the target view, that in this case is "register"
     */
    @RequestMapping(value = { "/admin" }, method = RequestMethod.GET)
    public String admin(Model model) {
        User loggedUser = sessionData.getLoggedUser();
        model.addAttribute("user", loggedUser);
        return "admin";
    }
    
    @RequestMapping(value = { "/admin/users" }, method = RequestMethod.GET)
    public String userList(Model model) {
    	User loggedUser = sessionData.getLoggedUser();
    	List<Credentials> allCredential = this.credentialService.getAllCredentials();
    	
    	model.addAttribute("loggedUser", loggedUser);
    	model.addAttribute("credentialsList", allCredential);
    	
    	return "allUsers";
    }
    
    @RequestMapping(value = { "/admin/users/{username}/delete" }, method = RequestMethod.POST)
    public String removeUser(Model model, @PathVariable String username) {
    	this.credentialService.deleteCredentials(username);
    	return "redirect:/admin/users";
    }
    
    @RequestMapping(value = { "/projects/shared" }, method = RequestMethod.GET)
    public String sharedProjects(Model model) {
    	User loggedUser = sessionData.getLoggedUser();
    	List<Project> projects = this.projectService.getSharedProjects(loggedUser);
    	model.addAttribute("loggedUser", loggedUser);
    	model.addAttribute("projects", projects);
    	return "sharedProjects";
    }
    
    @RequestMapping(value = { "/users/me/update/{id}" }, method = RequestMethod.GET)
    public String UpdateProfile(Model model, @PathVariable Long id ) {
       
    	Credentials credentials = this.credentialService.getCredentials(id);
    	model.addAttribute("credentials", credentials);
    	
        return "updateProfile";
    }
    
    @RequestMapping(value = { "/users/me/update" }, method = RequestMethod.POST)
	public String UpdateProfileForm(@Valid @ModelAttribute("updateProfileForm") Credentials credentials,
									BindingResult projectBindResut,
									Model model) {
		
    	Credentials oldCredentials = this.sessionData.getLoggedCredentials();
    	
    	oldCredentials.getUser().setFirstName(credentials.getUser().getFirstName());
    	oldCredentials.getUser().setLastName(credentials.getUser().getLastName());
    	
    	
    	this.userService.saveUser(oldCredentials.getUser());
    
		return "index";
	}
    
}
