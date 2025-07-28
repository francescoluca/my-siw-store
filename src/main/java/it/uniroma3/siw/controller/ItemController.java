package it.uniroma3.siw.controller;

import java.io.IOException;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import it.uniroma3.siw.controller.validator.InventoryItemValidator;
import it.uniroma3.siw.model.InventoryItem;
import it.uniroma3.siw.service.InventoryItemService;
import it.uniroma3.siw.service.TelevisionService;

@Controller
public class ItemController {

	@Autowired
	private TelevisionService televisionService;

	@Autowired
	private InventoryItemService inventoryItemService;

	@Autowired
	private InventoryItemValidator inventoryItemValidator;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields("photo");
	}

	@GetMapping("/admin/formNewInventoryItem")
	public String formNewInventoryItem(Model model) {
		model.addAttribute("inventoryItem", new InventoryItem());
		model.addAttribute("televisions", televisionService.findAll());
		return "/admin/formNewInventoryItem";
	}

	@PostMapping("/admin/inventoryItem")
	public String newInventoryItem(@ModelAttribute("inventoryItem") InventoryItem inventoryItem,
			@RequestParam("photo") MultipartFile photo, BindingResult bindingResult,
			@RequestHeader(value = "referer", required = false) String referer, Model model) throws IOException {
		inventoryItemValidator.validate(inventoryItem, bindingResult);
		if (!bindingResult.hasErrors()) {
			this.inventoryItemService.save(inventoryItem, photo);
			model.addAttribute("inventoryItem", inventoryItem);
			return "redirect:/inventoryItem/" + inventoryItem.getId();
		} else {
			return "admin/formNewInventoryItem";
		}
	}

	@PostMapping("/admin/updateInventoryItem/{id}")
	public String updateInventoryItem(@PathVariable("id") Long id, Model model,
			@ModelAttribute("inventoryItem") InventoryItem updatedInventoryItem,
			@RequestParam(value = "photo", required = false) MultipartFile photo) throws IOException {
		InventoryItem inventoryItem = inventoryItemService.findById(id);
		inventoryItem.setCondition(updatedInventoryItem.getCondition());
		inventoryItem.setDescription(updatedInventoryItem.getDescription());
		inventoryItem.setOptional(updatedInventoryItem.getOptional());
		inventoryItem.setPrice(updatedInventoryItem.getPrice());
		inventoryItem.setProductCode(updatedInventoryItem.getProductCode());

		if (updatedInventoryItem.getTelevision() != null) {
			inventoryItem.setTelevision(updatedInventoryItem.getTelevision());
		}

		inventoryItemService.save(inventoryItem, photo);
		return "redirect:/inventoryItem/" + id;
	}

	@GetMapping("/admin/manageInventoryItems")
	public String manageInventoryItems(Model model) {
		model.addAttribute("inventoryItems", this.inventoryItemService.findAll());
		model.addAttribute("televisions", this.televisionService.findAll());
		return "/admin/manageInventoryItems";
	}

	@GetMapping("/admin/formUpdateInventoryItem/{id}")
	public String formUpdateInventoryItem(@PathVariable("id") Long id, Model model) {
		model.addAttribute("televisions", this.televisionService.findAll());
		model.addAttribute("inventoryItem", this.inventoryItemService.findById(id));
		return "/admin/formUpdateInventoryItem";
	}

	@GetMapping("/inventoryItem/{id}")
	public String getInventoryItem(@PathVariable("id") Long id, Model model) {
		model.addAttribute("inventoryItem", this.inventoryItemService.findById(id));
		return "inventoryItem.html";
	}

	@GetMapping("/inventoryItems")
	public String listInventoryItems(@RequestParam(required = false, defaultValue = "") String keyword,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size,
			@RequestParam(value = "sortField", defaultValue = "price") String sortField,
			@RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
			@RequestParam(required = false, defaultValue = "0") BigDecimal minPrice,
			@RequestParam(required = false, defaultValue = "5000") BigDecimal maxPrice,
			@RequestParam(required = false, defaultValue = "0") Integer minInches,
			@RequestParam(required = false, defaultValue = "100") Integer maxInches, Model model,
			@AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails) {

		Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
		Page<InventoryItem> inventoryItemPage;

		inventoryItemPage = inventoryItemService.searchWithSort(keyword, sortField, minPrice, maxPrice, minInches,
				maxInches, pageable);

		model.addAttribute("keyword", keyword);
		model.addAttribute("inventoryItemsCount", inventoryItemPage.getTotalElements());
		model.addAttribute("inventoryItems", inventoryItemPage.getContent());
		model.addAttribute("inventoryItemPage", inventoryItemPage);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages",
				inventoryItemPage.getTotalPages() == 0 ? 1 : inventoryItemPage.getTotalPages());
		model.addAttribute("pageSize", size);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		model.addAttribute("minPrice", minPrice);
		model.addAttribute("maxPrice", maxPrice);
		model.addAttribute("minInches", minInches);
		model.addAttribute("maxInches", maxInches);
		return "inventoryItems.html";
	}

	@GetMapping("/inventoryItem/{id}/photo")
	public ResponseEntity<byte[]> photo(@PathVariable Long id) {
		byte[] image = inventoryItemService.getPhoto(id);
		if (image == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		return new ResponseEntity<>(image, headers, HttpStatus.OK);
	}

}
