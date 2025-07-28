package it.uniroma3.siw.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import it.uniroma3.siw.controller.validator.InventoryItemValidator;
import it.uniroma3.siw.controller.validator.PickUpRequestValidator;
import it.uniroma3.siw.model.InventoryItem;
import it.uniroma3.siw.model.PickUpRequest;
import it.uniroma3.siw.model.PickUpRequestItem;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.model.Util.PicKUpStatus;
import it.uniroma3.siw.service.InventoryItemService;
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
	private InventoryItemService inventoryItemService;

	@Autowired
	private InventoryItemValidator inventoryItemValidator;

	@Autowired
	private TelevisionService televisionService;

	@Autowired
	private UserService userService;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields("photo");
	}

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
		pickUpRequest.setStatus(PicKUpStatus.IN_ATTESA);
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
		PickUpRequest pickUpRequest = this.pickUpRequestService.findById(requestId);
		if (status.equals(PicKUpStatus.APPROVATA.toString())) {
			model.addAttribute("request", pickUpRequest);
			return "admin/formApproveRequest";
		}
		if (status.equals(PicKUpStatus.RIFIUTATA.toString())) {
			model.addAttribute("request", pickUpRequest);
			return "admin/formRefuseRequest";
		}
		if (status.equals(PicKUpStatus.COMPLETATA.toString())) {
			PickUpRequestItem pickUpRequestItem = new PickUpRequestItem();
			pickUpRequestItem.setPickUpRequest(pickUpRequest);
			pickUpRequestItem.setPurchaseDate(LocalDateTime.now());
			model.addAttribute("televisions", televisionService.findAll());
			model.addAttribute("request", pickUpRequest);
			model.addAttribute("pickUpRequestItem", pickUpRequestItem);
			return "admin/formCompleteRequest";
		}
		pickUpRequest.setStatus(PicKUpStatus.IN_ATTESA);
		pickUpRequest.setAdminNote(null);
		pickUpRequestService.save(pickUpRequest);
		model.addAttribute("pickUpRequests", pickUpRequestService.findAll());
		return "admin/managePickUpRequests";
	}

	@PostMapping("/admin/refusePickUpRequest/{id}")
	public String refusePickUpRequest(@PathVariable("id") Long id, Model model,
			@ModelAttribute("pickUpRequest") PickUpRequest updatedPickUpRequest) throws IOException {
		PickUpRequest pickUpRequest = pickUpRequestService.findById(id);
		pickUpRequest.setStatus(PicKUpStatus.RIFIUTATA);
		pickUpRequest.setAdminNote(updatedPickUpRequest.getAdminNote());
		pickUpRequest.setAdminNote(null);
		pickUpRequestService.save(pickUpRequest);
		model.addAttribute("pickUpRequests", pickUpRequestService.findAll());
		return "admin/managePickUpRequests";
	}

	@PostMapping("/admin/approvePickUpRequest/{id}")
	public String approvePickUpRequest(@PathVariable("id") Long id, Model model,
			@ModelAttribute("pickUpRequest") PickUpRequest updatedPickUpRequest) throws IOException {
		PickUpRequest pickUpRequest = pickUpRequestService.findById(id);
		pickUpRequest.setStatus(PicKUpStatus.APPROVATA);
		pickUpRequest.setRequestDate(updatedPickUpRequest.getRequestDate());
		pickUpRequestService.save(pickUpRequest);
		model.addAttribute("pickUpRequests", pickUpRequestService.findAll());
		return "admin/managePickUpRequests";
	}

	@PostMapping("/admin/completePickUpRequest/{id}")
	public String completePickUpRequest(@PathVariable("id") Long id, Model model,
			@ModelAttribute("inventoryItem") InventoryItem inventoryItem, @RequestParam("photo") MultipartFile photo,
			BindingResult bindingResult) throws IOException {
		PickUpRequest pickUpRequest = pickUpRequestService.findById(id);
		pickUpRequest.setStatus(PicKUpStatus.COMPLETATA);
		pickUpRequestService.save(pickUpRequest);
		inventoryItemValidator.validate(inventoryItem, bindingResult);
		if (!bindingResult.hasErrors()) {
			this.inventoryItemService.save(inventoryItem, photo);
			model.addAttribute("inventoryItem", inventoryItem);
			return "redirect:/inventoryItem/" + inventoryItem.getId();
		} else {
			return "admin/formCompleteRequest";
		}
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