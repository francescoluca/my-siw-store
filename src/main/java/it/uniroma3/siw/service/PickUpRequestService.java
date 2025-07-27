package it.uniroma3.siw.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.PickUpRequest;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.PickUpRequestRepository;
import jakarta.transaction.Transactional;

@Service
public class PickUpRequestService {

	@Autowired
	public PickUpRequestRepository pickUpRequestRepository;

	public boolean existsById(Long id) {
		return pickUpRequestRepository.existsById(id);
	}

	public void save(PickUpRequest pickUpRequest) {
		pickUpRequestRepository.save(pickUpRequest);
	}

	public PickUpRequest findById(Long id) {
		return pickUpRequestRepository.findById(id).get();
	}

	public Iterable<PickUpRequest> findAll() {
		return pickUpRequestRepository.findAll();
	}

	@Transactional
	public void save(PickUpRequest pickUpRequest, MultipartFile file) throws IOException {
		if (file != null && !file.isEmpty()) {
			pickUpRequest.setPhoto(file.getBytes());
		} else {
			System.out.println("Nessuna foto caricata");
		}
		this.pickUpRequestRepository.save(pickUpRequest);
	}

	public List<PickUpRequest> findByUser(User currentUser) {
		return this.pickUpRequestRepository.findByUser(currentUser);
	}

	public byte[] getPhoto(Long id) {
		return this.pickUpRequestRepository.findById(id).map(PickUpRequest::getPhoto).orElse(null);
	}
}