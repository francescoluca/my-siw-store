package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.PickUpRequest;
import it.uniroma3.siw.model.PickUpRequestItem;
import it.uniroma3.siw.repository.PickUpRequestRepository;

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
}