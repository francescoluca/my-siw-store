package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.controller.validator.TelevisionValidator;
import it.uniroma3.siw.model.InventoryItem;
import it.uniroma3.siw.model.Television;
import it.uniroma3.siw.model.Util.Brand;
import it.uniroma3.siw.service.TelevisionService;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class TelevisionController {
	@Autowired 
	private TelevisionService televisionService;
	
	@Autowired 
	private TelevisionValidator televisionValidator;
	
	@GetMapping(value="/admin/formNewTelevision")
	public String formNewTelevision(Model model) {
		model.addAttribute("television", new Television());
		return "admin/formNewTelevision.html";
	}	

	@PostMapping("/admin/television")
	public String newInventoryItem(@ModelAttribute("television") Television television,BindingResult bindingResult,Model model) {
		this.televisionValidator.validate(television, bindingResult);
		if(!bindingResult.hasErrors()) {
			this.televisionService.save(television);
			model.addAttribute("television",television);
			return "redirect:/television/"+television.getId();
		}else {
			return "admin/formNewTelevision";
		}
	}
	
	@GetMapping("/television/{id}")
	public String getTelevision(@PathVariable("id") Long id, Model model) {
		model.addAttribute("television", this.televisionService.findById(id));
		return "television.html";
	}

	@GetMapping("/televisions")
	public String getTelevisions(Model model) {		
		model.addAttribute("televisions", this.televisionService.findAll());
		return "televisions.html";
	}
	
	@GetMapping("/admin/manageTelevisions")
	public String manageTelevisions(Model model) {
		model.addAttribute("televisions", this.televisionService.findAll());
		return "admin/manageTelevisions.html";
	}
	
	@GetMapping("/admin/formUpdateTelvision/{id}")
	public String formUpdateTelvision(@PathVariable("id") Long id,Model model) {
		model.addAttribute("television", televisionService.findById(id));
		return "admin/formUpdateTelevision.html";
	}
	
	@GetMapping("/admin/setTelevisionBrand/{televisionId}/{brand}")
	public String getMethodName(@PathVariable("televisionId") Long televisionId,@PathVariable("brand") Brand brand,Model model) {
		Television television = televisionService.findById(televisionId);
		television.setBrand(brand);
		televisionService.save(television);
		model.addAttribute("television",television);
		return "admin/formUpdateTelevision.html";
	}
	
}
