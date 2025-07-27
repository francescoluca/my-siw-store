package it.uniroma3.siw.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.InventoryItem;
import it.uniroma3.siw.repository.InventoryItemRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class InventoryItemService {

	@Autowired
	private InventoryItemRepository inventoryItemRepository;

	public Iterable<InventoryItem> findAll() {
		return this.inventoryItemRepository.findAll();
	}

	public Page<InventoryItem> findAll(Pageable pageable) {
		return inventoryItemRepository.findAll(pageable);
	}

	public Page<InventoryItem> searchWithSort(String keyword, String sortField, BigDecimal minPrice,
			BigDecimal maxPrice, Integer minInches, Integer maxInches, Pageable pageable) {
		return this.inventoryItemRepository.searchWithSort(keyword, sortField, minPrice, maxPrice, minInches, maxInches,
				pageable);
	}

	public byte[] getPhoto(Long id) {
		return this.inventoryItemRepository.findById(id).map(InventoryItem::getPhoto).orElse(null);
	}

	@Transactional
	public void save(InventoryItem inventoryItem, MultipartFile file) throws IOException {
		if (file != null && !file.isEmpty()) {
			inventoryItem.setPhoto(file.getBytes());
		} else {
			System.out.println("Nessuna foto caricata");
		}

		this.inventoryItemRepository.save(inventoryItem);
	}

	public InventoryItem findById(Long id) {
		return inventoryItemRepository.findById(id).get();
	}

	public boolean existsByProductCode(String productCode) {
		return inventoryItemRepository.existsByProductCode(productCode);
	}

	public List<InventoryItem> findTop3ByReleaseDate() {
		Pageable topThree = PageRequest.of(0, 3);
		return inventoryItemRepository.findTop3ByReleaseDate(topThree);
	}

}
