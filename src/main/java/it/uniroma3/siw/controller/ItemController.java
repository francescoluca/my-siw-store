package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.controller.validator.ItemValidator;
import it.uniroma3.siw.model.InventoryItem;
import it.uniroma3.siw.service.ItemService;

@Controller
public class ItemController {
	@Autowired
	private ItemService itemService;

	@Autowired
	private ItemValidator itemValidator;

	@GetMapping(value = "/admin/formNewInventoryItem")
	public String formNewInventoryItem(Model model) {
		model.addAttribute("inventoryItem", new InventoryItem());
		return "admin/formNewInventoryItem.html";
	}

	@PostMapping("/admin/inventoryItem")
	public String newInventoryItem(@ModelAttribute("inventoryItem") InventoryItem inventoryItem,
			BindingResult bindingResult, Model model) {
		this.itemValidator.validate(inventoryItem, bindingResult);
		if (!bindingResult.hasErrors()) {
			this.itemService.save(inventoryItem);
			model.addAttribute("inventoryItem", inventoryItem);
			return "redirect:/inventoryItem/" + inventoryItem.getId();
		} else {
			return "admin/formNewInventoryItem";
		}
	}

	@GetMapping("/inventoryItem/{id}")
	public String getInventoryItem(@PathVariable("id") Long id, Model model) {
		model.addAttribute("inventoryItem", this.itemService.findById(id));
		return "inventoryItem.html";
	}

	@GetMapping("/inventoryItems")
	public String getInventoryItems(Model model) {
		model.addAttribute("inventoryItems", this.itemService.findAll());
		return "inventoryItems.html";
	}

}
