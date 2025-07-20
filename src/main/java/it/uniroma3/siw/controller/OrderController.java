package it.uniroma3.siw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


import it.uniroma3.siw.controller.validator.OrderValidator;
import it.uniroma3.siw.controller.validator.TelevisionValidator;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.InventoryItem;
import it.uniroma3.siw.model.Order;
import it.uniroma3.siw.model.OrderItem;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.OrderService;
import it.uniroma3.siw.service.TelevisionService;
import it.uniroma3.siw.service.UserService;

@Controller
public class OrderController {
	@Autowired
	private OrderValidator orderValidator;
	@Autowired
	private OrderService orderService;
	
	
	
	@GetMapping("/formNewOrder")
	public String formNewOrder(Model model) { // come cassetto vuoto, metto dentro i fogli con chiave valore
		return "/formNewOrder";
	}
	
	@GetMapping("/order/{id}")
	public String getOrder(@PathVariable ("id") Long id,Model model) {
		model.addAttribute("order",this.orderService.findById(id));
		return "/order.html";
	}
	

	@GetMapping("/orders")
	public String getOrders(Model model) {
		model.addAttribute("orders", this.orderService.findAll());
		return "orders.html";
	}

	
	@PostMapping("/newOrder")
	public String newOrder(@ModelAttribute("order") Order order, BindingResult bindingResult, Model model) {
		this.orderValidator.validate(order, bindingResult);
		if (!bindingResult.hasErrors()) {
			this.orderService.save(order);
			return "/profile";
		} else {
			return "/profile";
		}
	}
}