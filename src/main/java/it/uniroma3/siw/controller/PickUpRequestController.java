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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

//	// Metodo per visualizzare la pagina di gestione richieste con statistiche
//	@GetMapping("/admin/managePickUpRequests")

//	}

	// Metodo corretto per aggiornare lo status delle richieste di ritiro
	@PostMapping("/admin/updatePickupStatus")
	public String updatePickupRequestStatus(@RequestParam("requestId") Long requestId,
			@RequestParam("status") String status, RedirectAttributes redirectAttributes) {
		try {
			// Trova la richiesta di ritiro per ID
			PickUpRequest request = pickUpRequestService.findById(requestId);

			// Verifica che la richiesta esista
			if (request == null) {
				redirectAttributes.addFlashAttribute("error",
						"Richiesta con ID " + requestId + " non trovata nel sistema");
				return "redirect:/admin/managePickUpRequests";
			}

			// Converte la stringa in enum PicKUpStatus
			PicKUpStatus newStatus;
			try {
				newStatus = PicKUpStatus.valueOf(status);
			} catch (IllegalArgumentException e) {
				redirectAttributes.addFlashAttribute("error",
						"Stato '" + status + "' non valido. Stati permessi: PENDING, APPROVATO, RIFIUTATO, DRAFT");
				return "redirect:/admin/managePickUpRequests";
			}

			// Salva lo stato precedente per logging/audit
			PicKUpStatus oldStatus = request.getStatus();

			// Aggiorna SOLO lo status della richiesta
			request.setStatus(newStatus);

			// Salva la richiesta aggiornata
			pickUpRequestService.save(request);

			// Messaggio di successo personalizzato
			String customerName = request.getName() + " " + request.getSurname();
			String statusMessage = getStatusDisplayName(newStatus);

			redirectAttributes.addFlashAttribute("success",
					"Stato della richiesta di " + customerName + " aggiornato con successo a: " + statusMessage);

			// Log dell'operazione (opzionale, per audit)
			System.out.println("Status aggiornato per richiesta ID " + requestId + ": " + oldStatus + " -> " + newStatus
					+ " (Cliente: " + customerName + ")");

		} catch (Exception e) {
			// Gestione errori generici
			redirectAttributes.addFlashAttribute("error",
					"Errore durante l'aggiornamento dello stato: " + e.getMessage());

			// Log dell'errore per debugging
			e.printStackTrace();
		}

		return "redirect:/admin/managePickUpRequests";
	}

	// Metodo ausiliario per convertire l'enum in nome leggibile
	private String getStatusDisplayName(PicKUpStatus status) {
		switch (status) {
		case APPROVATO:
			return "Approvato per Ritiro";
		case RIFIUTATO:
			return "Rifiutato";
		case PENDING:
			return "In Attesa di Valutazione";
		case DRAFT:
			return "Bozza (Non Completata)";
		default:
			return status.toString();
		}
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