package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Television;
import it.uniroma3.siw.service.TelevisionService;

@Component
public class TelevisionValidator implements Validator{

	@Autowired
	private TelevisionService televisionService;
	
	@Override
	public void validate(Object o, Errors errors) {
		Television television = (Television)o;
		if (television.getModel()!=null &&
				television.getBrand()!=null && 
				televisionService.existsByModelAndBrand(television.getModel(),television.getBrand())) {
			errors.reject("television.duplicate");
		}
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return Television.class.equals(aClass);
	}

}
