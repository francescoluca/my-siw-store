package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.PickUpRequest;
import it.uniroma3.siw.model.User;

public interface PickUpRequestRepository extends CrudRepository<PickUpRequest, Long> {

	List<PickUpRequest> findByUser(User currentUser);

}