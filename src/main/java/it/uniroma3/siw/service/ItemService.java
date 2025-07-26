package it.uniroma3.siw.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.InventoryItem;
import it.uniroma3.siw.model.Item;
import it.uniroma3.siw.repository.ItemRepository;
import jakarta.transaction.Transactional;

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

	public byte[] getPhoto(Long id) {
		return this.itemRepository.findById(id).map(Item::getPhoto).orElse(null);
	}

	@Transactional
	public void save(Item item, MultipartFile file) throws IOException {
		if (file != null && !file.isEmpty()) {
			item.setPhoto(file.getBytes());
		} else {
			System.out.println("Nessuna foto caricata");
		}

		this.itemRepository.save(item);
	}

}
