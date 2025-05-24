package it.uniroma3.siw.model;

import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class InventoryItem extends Item{
	//aggiungere doppio costruttore che copia tutti i valori delle variabili del pickUpItem prelevato e l'altro no parametri
	
	public enum StockStatus{
		AVAILABLE,
		SOLD,
		COMING
	}

	private Integer price;
	@Enumerated(EnumType.STRING)
	private StockStatus status;
	
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public StockStatus getStatus() {
		return status;
	}
	public void setStatus(StockStatus status) {
		this.status = status;
	}
}
