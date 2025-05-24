package it.uniroma3.siw.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
@Entity
public class PickUpRequestItem extends Item{
	@NotNull
	private LocalDateTime purchaseDate;
	@NotNull
	private Integer  purchasePrice;
	
	@ManyToOne
	private PickUpRequest pickUpRequest;
	
	public LocalDateTime getPurchaseDate() {
		return purchaseDate;
	}
	public void setPurchaseDate(LocalDateTime purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	public Integer getPurchasePrice() {
		return purchasePrice;
	}
	public void setPurchasePrice(Integer purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	public PickUpRequest getPickUpRequest() {
		return pickUpRequest;
	}
	public void setPickUpRequest(PickUpRequest pickUpRequest) {
		this.pickUpRequest = pickUpRequest;
	}
}
