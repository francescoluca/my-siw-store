package it.uniroma3.siw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Order;
import it.uniroma3.siw.model.PickUpRequest;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.OrderService;
import it.uniroma3.siw.service.PickUpRequestService;
import it.uniroma3.siw.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private CredentialsService credentialsService;

	@Autowired
	private PickUpRequestService pickUpRequestService;

	@Autowired
	private OrderService orderService;

	@GetMapping("/profile")
	public String getProfilePage(
			@AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails,
			Model model) {
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		User currentUser = userService.getUser(credentials.getId());
		List<Order> userOrders = orderService.findByUser(currentUser);
		List<PickUpRequest> userRequests = pickUpRequestService.findByUser(currentUser);
		model.addAttribute("user", currentUser);
		model.addAttribute("userOrders", userOrders);
		model.addAttribute("userRequests", userRequests);
		model.addAttribute("activePage", "profile");
		return "profile";
	}

	@GetMapping("/about")
	public String getAboutPage(Model model) {
		model.addAttribute("activePage", "about");
		return "about";
	}

	@GetMapping("/cart")
	public String cart(@AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails,
			Model model) {
		model.addAttribute("activePage", "cart");
		return "cart";
	}
}