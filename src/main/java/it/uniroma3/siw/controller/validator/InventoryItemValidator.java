package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.InventoryItem;
import it.uniroma3.siw.service.InventoryItemService;

@Component
public class InventoryItemValidator implements Validator {

	@Autowired
	private InventoryItemService inventoryItemService;

	@Override
	public void validate(Object o, Errors errors) {
		InventoryItem inventoryItem = (InventoryItem) o;
		if (inventoryItem.getProductCode() != null
				&& inventoryItemService.existsByProductCode(inventoryItem.getProductCode())) {
			errors.reject("item.duplicate");
		}
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return InventoryItem.class.equals(aClass);
	}

}
