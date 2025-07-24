package it.uniroma3.siw.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	private String name;
	@NotBlank
	private String surname;
	@NotBlank
	private String email;
	@OneToOne
	private Address address;
	@ManyToMany
	private List<InventoryItem> cartItems;

	@OneToMany(mappedBy = "user")
	private List<Order> orders;

	@OneToMany(mappedBy = "user")
	private List<PickUpRequest> pickUpRequests;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public List<PickUpRequest> getPickUpRequests() {
		return pickUpRequests;
	}

	public void setPickUpRequests(List<PickUpRequest> pickUpRequests) {
		this.pickUpRequests = pickUpRequests;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public List<InventoryItem> getCartItems() {
		return cartItems;
	}

	public void setCartItems(List<InventoryItem> cartItems) {
		this.cartItems = cartItems;
	}
}
