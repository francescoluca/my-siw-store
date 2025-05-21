package it.uniroma3.siw.model;

import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class InventoryItem extends Item{
	//aggiungere doppio costruttore che copia tutti i valori delle variabili del pickUpItem prelevato e l'altro no parametri
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	public enum StockStatus{
		AVAILABLE,
		SOLD,
		COMING
	}

	@NotNull
	private Integer price;
	@NotNull
	private StockStatus status;
	
	@ManyToOne
	private Television television;
	
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Television getTelevision() {
		return television;
	}
	public void setTelevision(Television television) {
		this.television = television;
	}
}
