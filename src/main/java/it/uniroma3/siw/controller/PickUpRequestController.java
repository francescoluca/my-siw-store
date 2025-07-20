package it.uniroma3.siw.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.controller.validator.OrderValidator;
import it.uniroma3.siw.controller.validator.PickUpRequestValidator;
import it.uniroma3.siw.controller.validator.TelevisionValidator;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.InventoryItem;
import it.uniroma3.siw.model.Order;
import it.uniroma3.siw.model.OrderItem;
import it.uniroma3.siw.model.PickUpRequest;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.model.Util.PicKUpStatus;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.OrderService;
import it.uniroma3.siw.service.PickUpRequestService;
import it.uniroma3.siw.service.TelevisionService;
import it.uniroma3.siw.service.UserService;

@Controller
public class PickUpRequestController {
	@Autowired
	private PickUpRequestValidator pickUpRequestValidator;
	@Autowired
	private PickUpRequestService pickUpRequestService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private CredentialsService credentialsService;

	
	@GetMapping("/formPickUpRequest")
	public String getFormPickUpRequestPage(
	        @AuthenticationPrincipal UserDetails userDetails,
	        Model model) {
	    Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
	    User currentUser = userService.getUser(credentials.getId());
	    model.addAttribute("user", currentUser); 
	    model.addAttribute("request", new PickUpRequest());

	    List<PickUpRequest> userRequests = currentUser.getPickUpRequests();
	    model.addAttribute("userRequests", userRequests);
	    return "/formPickUpRequest";
	}

	
	@GetMapping("/request/{id}")
	public String getPickUpRequest(@PathVariable ("id") Long id,Model model) {
		model.addAttribute("requests",this.pickUpRequestService.findById(id));
		return "/request.html";
	}
	
	@GetMapping("/requests")
	public String getRequets(Model model) {
		model.addAttribute("requests", this.pickUpRequestService.findAll());
		return "requests.html";
	}

	@PostMapping("/newRequest")
	public String newRequest(@ModelAttribute("request") PickUpRequest pickUpRequest, BindingResult bindingResult, Model model,  @RequestParam("photoFile") MultipartFile photoFile) throws IOException {
		this.pickUpRequestValidator.validate(pickUpRequest, bindingResult);
		if (!bindingResult.hasErrors()) {
			pickUpRequest.setStatus(PicKUpStatus.PENDING);
			this.pickUpRequestService.save(pickUpRequest,photoFile);
			return "/formPickUpRequest";
		} else {
			return "/formPickUpRequest";
		}
	}
}