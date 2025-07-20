package it.uniroma3.siw.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Order;

public interface OrderRepository extends CrudRepository<Order,Long>{

	
}