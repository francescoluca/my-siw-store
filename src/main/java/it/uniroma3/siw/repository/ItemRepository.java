package it.uniroma3.siw.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.InventoryItem;
import it.uniroma3.siw.model.Item;

public interface ItemRepository extends CrudRepository<Item, Long> {

	boolean existsByProductCode(String productCode);

	@Query(value = "SELECT i.*, inv.* " + "FROM item i "
			+ "JOIN inventory_item inv ON i.id = inv.id", nativeQuery = true)
	Iterable<InventoryItem> findAllInventoryItems();

}
