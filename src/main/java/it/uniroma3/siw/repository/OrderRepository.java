package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Order;
import it.uniroma3.siw.model.User;

public interface OrderRepository extends CrudRepository<Order, Long> {

	List<Order> findByUser(User currentUser);

}