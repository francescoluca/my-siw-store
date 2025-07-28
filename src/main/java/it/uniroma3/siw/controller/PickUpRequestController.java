package it.uniroma3.siw.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.server.ResponseStatusException;

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

	@GetMapping("/admin/formUpdatePickupStatus/{requestId}/{status}")
	public String updatePickupStatus(@PathVariable("requestId") Long requestId, @PathVariable("status") String status,
			Model model) {
		if (status.equals(PicKUpStatus.APPROVED.toString())) {
			model.addAttribute("request", this.pickUpRequestService.findById(requestId));
			return "admin/formApproveRequest";
		}
		if (status.equals(PicKUpStatus.REFUSED.toString())) {
			model.addAttribute("request", this.pickUpRequestService.findById(requestId));
			return "admin/formRefuseRequest";
		}
		PickUpRequest pickUpRequest = pickUpRequestService.findById(requestId);
		pickUpRequest.setStatus(PicKUpStatus.PENDING);
		pickUpRequest.setAdminNote(null);
		pickUpRequestService.save(pickUpRequest);
		model.addAttribute("pickUpRequests", pickUpRequestService.findAll());
		return "admin/managePickUpRequests";
	}

	@PostMapping("/admin/refusePickUpRequest/{id}")
	public String refusePickUpRequest(@PathVariable("id") Long id, Model model,
			@ModelAttribute("pickUpRequest") PickUpRequest updatedPickUpRequest) throws IOException {
		PickUpRequest pickUpRequest = pickUpRequestService.findById(id);
		pickUpRequest.setStatus(PicKUpStatus.REFUSED);
		pickUpRequest.setAdminNote(updatedPickUpRequest.getAdminNote());
		pickUpRequestService.save(pickUpRequest);
		model.addAttribute("pickUpRequests", pickUpRequestService.findAll());
		return "admin/managePickUpRequests";
	}

	@PostMapping("/admin/updatePickUpRequest/{id}")
	public String updatePickUpRequest(@PathVariable("id") Long id, Model model,
			@ModelAttribute("pickUpRequest") PickUpRequest updatedPickUpRequest,
			@RequestParam("photo") MultipartFile photo) throws IOException {
		PickUpRequest pickUpRequest = pickUpRequestService.findById(id);
		pickUpRequest.setAddress(updatedPickUpRequest.getAddress());
		pickUpRequest.setEmail(updatedPickUpRequest.getEmail());
		pickUpRequest.setName(updatedPickUpRequest.getName());
		pickUpRequest.setPhone(updatedPickUpRequest.getPhone());
		pickUpRequest.setPhoto(updatedPickUpRequest.getPhoto());
		pickUpRequest.setAdminNote(updatedPickUpRequest.getAdminNote());
		pickUpRequest.setStatus(updatedPickUpRequest.getStatus());
		pickUpRequest.setNote(updatedPickUpRequest.getNote());
		pickUpRequest.setSurname(updatedPickUpRequest.getSurname());
		pickUpRequest.setUser(updatedPickUpRequest.getUser());
		pickUpRequestService.save(pickUpRequest, photo);
		return "admin/managePickUpRequests";
	}

	@GetMapping("/pickUpRequest/{id}/photo")
	public ResponseEntity<byte[]> photo(@PathVariable Long id) {
		byte[] image = pickUpRequestService.getPhoto(id);
		if (image == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		return new ResponseEntity<>(image, headers, HttpStatus.OK);
	}
}