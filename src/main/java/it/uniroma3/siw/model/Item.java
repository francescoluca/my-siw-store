package it.uniroma3.siw.model;

import it.uniroma3.siw.model.Util.Condition;
import it.uniroma3.siw.model.Util.Optional;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Item {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@NotBlank
	private String productCode;
	@NotNull
	@Enumerated(EnumType.STRING)
	private Condition condition;
	@Enumerated(EnumType.STRING)
	private Optional optional;
	private String description;
	@ManyToOne
	private Television television;
	
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
}
