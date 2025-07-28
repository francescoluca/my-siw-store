package it.uniroma3.siw.model;

public class Util {

	public enum Condition {
		NUOVO("NUOVO"), RICONDIZIONATO("RICONDIZIONATO"), USATO("USATO"),
		PER_PEZZI_DI_RICAMBIO("PER PEZZI DI RICAMBIO"), COME_NUOVO("COME NUOVO"), ROTTO("ROTTO"),
		FUNZIONANTE("FUNZIONANTE");

		private final String label;

		Condition(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}
	}

	public enum Optional {
		TELECOMANDO("TELECOMANDO"), STAFFA_DA_MURO("STAFFA DA MURO"), STAFFA("STAFFA");

		private final String label;

		Optional(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}
	}

	public enum Brand {
		SAMSUNG, LG, SONY, PANASONIC, PHILIPS, HISENSE, TCL, SHARP, TOSHIBA, VIZIO
	}

	public enum PicKUpStatus {
		REFUSED("RIFIUTATA"), APPROVED("APPROVATA"), PENDING("IN ATTESA");

		private final String label;

		PicKUpStatus(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}
	}
}
