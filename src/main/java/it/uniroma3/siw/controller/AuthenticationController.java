package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.ItemService;
import it.uniroma3.siw.service.UserService;
import jakarta.validation.Valid;

@Controller
public class AuthenticationController {

	@Autowired
	private CredentialsService credentialsService;

	@Autowired
	private UserService userService;

	@Autowired
	private ItemService itemService;

	@GetMapping(value = "/register")
	public String showRegisterForm(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("credentials", new Credentials());
		model.addAttribute("activePage", "register");

		return "formRegisterUser";
	}

	@GetMapping(value = "/login")
	public String showLoginForm(Model model) {
		model.addAttribute("activePage", "login");

		return "formLogin";
	}

//	@GetMapping(value = "/admin/userModeAdmin")
//	public String userModeAdmin(Model model) {
//		model.addAttribute("inventoryItems", itemService.findAllInventoryItems());
//		model.addAttribute("isAdmin", true);
//		model.addAttribute("activePage", "admin");
//
//		return "index";
//	}
	@GetMapping({ "/admin", "/admin/userModeAdmin" })

	public String adminPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		// 1. Calcolo del flag isAdmin (dipende da come gestisci i ruoli)
		boolean isAdmin = userDetails.getAuthorities().stream()
				.anyMatch(granted -> granted.getAuthority().equals("ROLE_ADMIN"));
		model.addAttribute("isAdmin", isAdmin);

		// 2. Quale "activePage" usare: lo lasciamo sempre "admin",
		// così link e label (tramite la ternaria) rimangono coerenti
		model.addAttribute("activePage", "admin");
		model.addAttribute("inventoryItems", itemService.findAllInventoryItems());
		model.addAttribute("isAdmin", true);

		// 3. Ritorno della view (admin.html)
		return "admin";
	}

	@GetMapping(value = "/")
	public String index(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("inventoryItems", itemService.findAllInventoryItems());
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "index.html";
		} else {
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
				return "admin/indexAdmin.html";
			}
		}
		return "index.html";
	}

	@GetMapping(value = "/success")
	public String defaultAfterLogin(Model model) {

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		model.addAttribute("inventoryItems", itemService.findAllInventoryItems());
		if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			return "admin/indexAdmin.html";
		}
		return "index.html";
	}

	@PostMapping(value = { "/register" })
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult userBindingResult,
			@Valid @ModelAttribute("credentials") Credentials credentials, BindingResult credentialsBindingResult,
			Model model) {

		// se user e credential hanno entrambi contenuti validi, memorizza User e the
		// Credentials nel DB
		if (!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
			userService.saveUser(user);
			credentials.setUser(user);
			credentialsService.saveCredentials(credentials);
			model.addAttribute("user", user);
			return "registrationSuccessful";
		}
		return "registerUser";
	}
}