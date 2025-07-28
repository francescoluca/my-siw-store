package it.uniroma3.siw.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Television;
import it.uniroma3.siw.repository.TelevisionRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class TelevisionService {

	@Autowired
	public TelevisionRepository televisionRepository;

	public boolean existsByCode(String code) {
		return televisionRepository.existsByCode(code);
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

	public byte[] getPhoto(Long id) {
		return this.televisionRepository.findById(id).map(Television::getPhoto).orElse(null);
	}

	@Transactional
	public void save(Television television, MultipartFile file) throws IOException {
		if (file != null && !file.isEmpty()) {
			television.setPhoto(file.getBytes());
		} else {
			System.out.println("Nessuna foto caricata");
		}

		this.televisionRepository.save(television);
	}

	public Page<Television> searchTelevisionsByKeyword(String keyword, Pageable pageable) {
		return this.televisionRepository.searchTelevisionsByKeyword(keyword, pageable);
	}

	public Page<Television> findAll(Pageable pageable) {
		return televisionRepository.findAll(pageable);
	}

}
