package it.uniroma3.siw.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public abstract class Item {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	public enum Condition {
	    NEW,
	    REFURBISHED,	//ricondizionato
	    USED,
	    FOR_PARTS,		//per pezzi di ricambio
	    LIKE_NEW,
	    BROKEN,
	    WORKING
	}
	public enum Optional{
		REMOTE_CONTROL,
		WALL_BRACKET,
		BRACKET
	}
	@NotNull
	private Condition condition;
	@NotNull
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
}
