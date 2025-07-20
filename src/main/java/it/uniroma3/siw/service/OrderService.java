package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Order;
import it.uniroma3.siw.model.OrderItem;
import it.uniroma3.siw.repository.OrderRepository;

@Service
public class OrderService {
	
	@Autowired
	public OrderRepository orderRepository;
	
	public boolean existsById(Long id) {
		return orderRepository.existsById(id);
	}
	public void save(Order order) {
		orderRepository.save(order);
	}

	public Order findById(Long id) {
		return orderRepository.findById(id).get();
	}

	public Iterable<Order> findAll() {
		return orderRepository.findAll();
	}
}