package it.uniroma3.siw.controller;

import java.io.IOException;
import java.time.LocalDate;
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

import it.uniroma3.siw.controller.validator.PickUpRequestValidator;
import it.uniroma3.siw.model.PickUpRequest;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.model.Util.PicKUpStatus;
import it.uniroma3.siw.service.PickUpRequestService;
import it.uniroma3.siw.service.UserService;

@Controller
public class PickUpRequestController {
	@Autowired
	private PickUpRequestValidator pickUpRequestValidator;
	@Autowired
	private PickUpRequestService pickUpRequestService;
	@Autowired
	private UserService userService;

	@GetMapping("/formPickUpRequest")
	public String getFormPickUpRequest(@AuthenticationPrincipal UserDetails userDetails, Model model) {
		User currentUser = userService.getCurrentUser(userDetails);
		model.addAttribute("user", currentUser);
		model.addAttribute("pickUpRequest", new PickUpRequest());
		model.addAttribute("activePage", "formPickUpRequest");

		return "/formPickUpRequest";
	}

	@GetMapping("/request/{id}")
	public String getPickUpRequest(@PathVariable("id") Long id, Model model) {
		model.addAttribute("requests", this.pickUpRequestService.findById(id));
		return "/request.html";
	}

	@GetMapping("/requests")
	public String getRequets(Model model) {
		model.addAttribute("requests", this.pickUpRequestService.findAll());
		return "requests.html";
	}

	@PostMapping("/newRequest")
	public String newRequest(@AuthenticationPrincipal UserDetails userDetails,
			@ModelAttribute("pickUpRequest") PickUpRequest pickUpRequest, BindingResult bindingResult, Model model,
			@RequestParam("photo") MultipartFile photo) throws IOException {

		User currentUser = userService.getCurrentUser(userDetails);
		pickUpRequest.setStatus(PicKUpStatus.PENDING);
		pickUpRequest.setRequestDate(LocalDate.now());
		pickUpRequest.setUser(currentUser);

		this.pickUpRequestService.save(pickUpRequest, photo);

		List<PickUpRequest> userRequests = pickUpRequestService.findByUser(currentUser);
		model.addAttribute("userRequests", userRequests);

		return "redirect:/profile";
	}

	@GetMapping("/admin/managePickUpRequests")
	public String managePickUpRequests(Model model) {
		model.addAttribute("pickUpRequests", pickUpRequestService.findAll());
		return "admin/managePickUpRequests";
	}
}