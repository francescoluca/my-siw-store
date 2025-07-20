package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Order;
import it.uniroma3.siw.service.OrderService;

@Component
public class OrderValidator implements Validator{

	@Autowired
	private OrderService orderService;
	
	@Override
	public void validate(Object o, Errors errors) {
		Order order = (Order)o;
		if (order.getTotal()!=null &&
				orderService.existsById(order.getId())) {
			errors.reject("order.duplicate");
		}
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return 	Order.class.equals(aClass);
	}

}
