package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import it.uniroma3.siw.model.Item;
import it.uniroma3.siw.service.ItemService;
import org.springframework.validation.Validator;

@Component
public class ItemValidator implements Validator{
	
	@Autowired
	private ItemService itemService;
	
	@Override
	public void validate(Object o, Errors errors) {
		Item item = (Item)o;
		if (item.getProductCode()!=null && itemService.existsByProductCode(item.getProductCode())) {
			errors.reject("item.duplicate");
		}
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return Item.class.equals(aClass);
	}

}
