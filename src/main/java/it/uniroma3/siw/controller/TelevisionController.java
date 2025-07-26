package it.uniroma3.siw.controller;

import java.io.IOException;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import it.uniroma3.siw.controller.validator.TelevisionValidator;
import it.uniroma3.siw.model.Television;
import it.uniroma3.siw.model.Util.Brand;
import it.uniroma3.siw.service.TelevisionService;

@Controller
public class TelevisionController {
	@Autowired
	private TelevisionService televisionService;

	@Autowired
	private TelevisionValidator televisionValidator;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields("photo");
	}

	@GetMapping(value = "/admin/formNewTelevision")
	public String formNewTelevision(Model model) {
		model.addAttribute("television", new Television());
		return "admin/formNewTelevision.html";
	}

	@PostMapping("/admin/television")
	public String newInventoryItem(@ModelAttribute("television") Television television, BindingResult bindingResult,
			@RequestParam("photo") MultipartFile photo, Model model) throws IOException {
		this.televisionValidator.validate(television, bindingResult);
		if (!bindingResult.hasErrors()) {
			this.televisionService.save(television, photo);
			model.addAttribute("television", television);
			return "redirect:/television/" + television.getId();
		} else {
			return "admin/formNewTelevision";
		}
	}

	@GetMapping("/television/{id}")
	public String getTelevision(@PathVariable("id") Long id, Model model) {
		model.addAttribute("television", this.televisionService.findById(id));
		return "television.html";
	}

	@GetMapping("/televisions")
	public String listTelevisions(@RequestParam(required = false) String keyword,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size,
			@RequestParam(value = "sortField", defaultValue = "screenInches") String sortField,
			@RequestParam(value = "sortDir", defaultValue = "asc") String sortDir, Model model,
			@AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails) {

		Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
		Page<Television> televisionPage;

		if (keyword != null && !keyword.trim().isEmpty()) {
			televisionPage = televisionService.searchTelevisionsByKeyword(keyword, pageable);
			model.addAttribute("keyword", keyword);
		} else {
			televisionPage = televisionService.findAll(pageable);
		}
		model.addAttribute("televisionsCount", televisionPage.getTotalElements());
		model.addAttribute("televisions", televisionPage.getContent());
		model.addAttribute("televisionPage", televisionPage);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", televisionPage.getTotalPages() == 0 ? 1 : televisionPage.getTotalPages());
		model.addAttribute("pageSize", size);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		return "televisions.html";
	}

	@GetMapping("/admin/manageTelevisions")
	public String manageTelevisions(Model model) {
		model.addAttribute("televisions", this.televisionService.findAll());
		return "admin/manageTelevisions.html";
	}

	@GetMapping("/admin/formUpdateTelvision/{id}")
	public String formUpdateTelvision(@PathVariable("id") Long id, Model model) {
		model.addAttribute("television", televisionService.findById(id));
		return "admin/formUpdateTelevision.html";
	}

	@PostMapping("/admin/updateTelevision/{id}")
	public String updateTelevision(@PathVariable("id") Long id, Model model,
			@ModelAttribute("television") Television updatedTelevision, @RequestParam("photo") MultipartFile photo)
			throws IOException {
		Television television = televisionService.findById(id);
		television.setModel(updatedTelevision.getModel());
		television.setBrand(updatedTelevision.getBrand());
		television.setReleaseDate(updatedTelevision.getReleaseDate());
		television.setScreenInches(updatedTelevision.getScreenInches());
		television.setCode(updatedTelevision.getCode());
		television.setDescription(updatedTelevision.getDescription());
		television.setPhoto(updatedTelevision.getPhoto());
		televisionService.save(television, photo);
		return "redirect:/television/" + id;
	}

	@GetMapping("/admin/setTelevisionBrand/{televisionId}/{brand}")
	public String getMethodName(@PathVariable("televisionId") Long televisionId, @PathVariable("brand") Brand brand,
			Model model) {
		Television television = televisionService.findById(televisionId);
		television.setBrand(brand);
		televisionService.save(television);
		model.addAttribute("television", television);
		return "admin/formUpdateTelevision.html";
	}

	@GetMapping("/television/{id}/photo")
	public ResponseEntity<byte[]> photo(@PathVariable Long id) {
		byte[] image = televisionService.getPhoto(id);
		if (image == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		return new ResponseEntity<>(image, headers, HttpStatus.OK);
	}
}
