package it.uniroma3.siw.model;

import jakarta.validation.constraints.NotNull;


public abstract class Item {
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
}
