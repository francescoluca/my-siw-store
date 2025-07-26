package it.uniroma3.siw.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.uniroma3.siw.model.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

	boolean existsByProductCode(String productCode);

}
