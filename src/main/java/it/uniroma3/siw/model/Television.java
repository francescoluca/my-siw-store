package it.uniroma3.siw.model;

import java.util.List;

import it.uniroma3.siw.model.Util.Brand;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Television {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	@NotBlank
	private String model;
	@NotNull
	@Enumerated(EnumType.STRING)
	private Brand brand;
	private String description;
	@NotNull
	private Integer releaseDate;
	@NotNull
	private Integer screenInches;
	private String code;
	@OneToMany(mappedBy = "television")
	private List<Item> inventoryItems;
	
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(Integer releaseDate) {
		this.releaseDate = releaseDate;
	}
	public Integer getScreenInches() {
		return screenInches;
	}
	public void setScreenInches(Integer screenInches) {
		this.screenInches = screenInches;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public List<Item> getInventoryItems() {
		return inventoryItems;
	}
	public void setInventoryItems(List<Item> inventoryItems) {
		this.inventoryItems = inventoryItems;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Brand getBrand() {
		return brand;
	}
	public void setBrand(Brand brand) {
		this.brand = brand;
	}
}
