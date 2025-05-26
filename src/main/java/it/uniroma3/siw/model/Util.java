package it.uniroma3.siw.model;

public class Util {
	
	public enum Condition {
	    NUOVO,
	    RICONDIZIONATO,	//ricondizionato
	    USATO,
	    PER_PEZZI_DI_RICAMBIO,		//per pezzi di ricambio
	    COME_NUOVO,
	    ROTTO,
	    FUNZIONANTE
	}
	public enum Optional{
		TELECOMANDO,
		STAFFA_DA_MURO,
		STAFFA
	}
	public enum Brand{
	    SAMSUNG, LG, SONY, PANASONIC, PHILIPS, HISENSE, TCL, SHARP, TOSHIBA, VIZIO
	}
}
