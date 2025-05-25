package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Television;
import it.uniroma3.siw.repository.TelevisionRepository;

@Service
public class TelevisionService {
	
	@Autowired
	public TelevisionRepository televisionRepository;

	public boolean existsByModelAndBrand(String model, String brand) {
		return televisionRepository.existsByModelAndBrand(model,brand);
	}

	public void save(Television television) {
		televisionRepository.save(television);
	}

	public Television findById(Long id) {
		return televisionRepository.findById(id).get();
	}

	public Iterable<Television> findAll() {
		return televisionRepository.findAll();
	}

}
