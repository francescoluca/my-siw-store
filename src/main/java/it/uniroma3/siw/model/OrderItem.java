package it.uniroma3.siw.model;

import it.uniroma3.siw.model.InventoryItem.StockStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class OrderItem extends Item{
	//aggiungere costruttore che copia tutti i valori delle variabili dell'InventoryItem acquistato

	@NotNull
	private Integer price;
	@NotNull
	private StockStatus status;
	@ManyToOne
	private Order order;
	
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
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
}
