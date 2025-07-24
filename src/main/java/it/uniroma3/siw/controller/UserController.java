package it.uniroma3.siw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.InventoryItem;
import it.uniroma3.siw.model.Order;
import it.uniroma3.siw.model.PickUpRequest;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.ItemService;
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
	@Autowired
	private ItemService itemService;

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

	@GetMapping("addToCart/{inventoryItemId}")
	public String addToCart(
			@AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails,
			@PathVariable("inventoryItemId") Long inventoryItemId,
			@RequestHeader(value = "referer", required = false) String referer, Model model) {
		InventoryItem inventoryItem = (InventoryItem) itemService.findById(inventoryItemId);
		User currentUser = userService.getCurrentUser(userDetails);
		if (!this.userService.isItemAlreadyInCart(currentUser, inventoryItem)) {
			currentUser.getCartItems().add(inventoryItem);
			this.userService.saveUser(currentUser);
		}
		model.addAttribute("cartItems", userService.getCartItemsByUser(currentUser));
		model.addAttribute("subTotal", userService.getCartSubtotalByUser(currentUser));
		return "cart";
	}

	@GetMapping("removeFromCart/{cartItemId}")
	public String removeFromCart(
			@AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails,
			@PathVariable("cartItemId") Long cartItemId,
			@RequestHeader(value = "referer", required = false) String referer, Model model) {
		InventoryItem cartItem = (InventoryItem) itemService.findById(cartItemId);
		User currentUser = userService.getCurrentUser(userDetails);
		currentUser.getCartItems().remove(cartItem);
		this.userService.saveUser(currentUser);
		model.addAttribute("cartItems", userService.getCartItemsByUser(currentUser));
		model.addAttribute("subTotal", userService.getCartSubtotalByUser(currentUser));
		return "cart";
	}

	@GetMapping("/cart")
	public String cart(@AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails,
			Model model) {
		User currentUser = userService.getCurrentUser(userDetails);
		model.addAttribute("cartItems", userService.getCartItemsByUser(currentUser));
		model.addAttribute("subTotal", userService.getCartSubtotalByUser(currentUser));
		model.addAttribute("activePage", "cart");
		return "cart";
	}
}