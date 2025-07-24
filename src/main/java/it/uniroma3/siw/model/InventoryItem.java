package it.uniroma3.siw.model;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

@Entity
public class InventoryItem extends Item {
	// aggiungere doppio costruttore che copia tutti i valori delle variabili del
	// pickUpItem prelevato e l'altro no parametri

	@NotNull
	private Integer price;

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

}
