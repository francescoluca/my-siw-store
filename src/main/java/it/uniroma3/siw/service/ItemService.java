package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.InventoryItem;
import it.uniroma3.siw.model.Item;
import it.uniroma3.siw.repository.ItemRepository;

@Service
public class ItemService {

	@Autowired
	private ItemRepository itemRepository;

	public boolean existsByProductCode(String productCode) {
		return itemRepository.existsByProductCode(productCode);
	}

	public void save(InventoryItem inventoryItem) {
		itemRepository.save(inventoryItem);
	}

	public Item findById(Long id) {
		return itemRepository.findById(id).get();
	}

	public Iterable<Item> findAll() {
		return itemRepository.findAll();
	}

}
