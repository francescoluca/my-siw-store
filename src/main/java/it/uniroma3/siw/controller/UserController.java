package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.UserService;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private CredentialsService credentialsService;

    @GetMapping("/profile")
    public String getProfilePage(@AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails, Model model) {
    	Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
    	User currentUser = userService.getUser(credentials.getId());      
    	model.addAttribute("user", currentUser);
        return "/profile";
    }
}