package it.uniroma3.siw.model;

import java.time.LocalDateTime;

import it.uniroma3.siw.model.Util.Condition;
import it.uniroma3.siw.model.Util.Optional;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class PickUpRequestItem {

	@NotNull
	private LocalDateTime purchaseDate;
	@ManyToOne
	private PickUpRequest pickUpRequest;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String productCode;
	private String description;
	@ManyToOne
	private Television television;
	private Integer price;
	@NotNull
	@Enumerated(EnumType.STRING)
	private Condition condition;
	@Enumerated(EnumType.STRING)
	private Optional optional;
	@Lob
	private byte[] photo;

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public Optional getOptional() {
		return optional;
	}

	public void setOptional(Optional optional) {
		this.optional = optional;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Television getTelevision() {
		return television;
	}

	public void setTelevision(Television television) {
		this.television = television;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String serialNumber) {
		this.productCode = serialNumber;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(LocalDateTime purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public PickUpRequest getPickUpRequest() {
		return pickUpRequest;
	}

	public void setPickUpRequest(PickUpRequest pickUpRequest) {
		this.pickUpRequest = pickUpRequest;
	}
}
