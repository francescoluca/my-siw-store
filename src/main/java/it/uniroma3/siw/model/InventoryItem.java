package it.uniroma3.siw.model;

import jakarta.persistence.Entity;

@Entity
public class InventoryItem extends Item {
	// aggiungere doppio costruttore che copia tutti i valori delle variabili del
	// pickUpItem prelevato e l'altro no parametri

	private Integer price;

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

}
