package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.PickUpRequest;
import it.uniroma3.siw.service.PickUpRequestService;

@Component
public class PickUpRequestValidator implements Validator{

	@Autowired
	private PickUpRequestService pickUpRequestService;
	
	@Override
	public void validate(Object o, Errors errors) {
		PickUpRequest pickUpRequest = (PickUpRequest)o;
		if (pickUpRequest.getRequestDate()!=null &&
				pickUpRequestService.existsById(pickUpRequest.getId())) {
			errors.reject("pickUpRequest.duplicate"); 
		}
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return 	PickUpRequest.class.equals(aClass);
	}

}
