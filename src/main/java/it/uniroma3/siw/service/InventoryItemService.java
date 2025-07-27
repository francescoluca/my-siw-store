package it.uniroma3.siw.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.InventoryItem;
import it.uniroma3.siw.repository.InventoryItemRepository;

@Service
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

}
