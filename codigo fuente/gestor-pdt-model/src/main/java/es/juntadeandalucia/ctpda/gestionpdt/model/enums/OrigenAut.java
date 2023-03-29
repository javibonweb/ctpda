package es.juntadeandalucia.ctpda.gestionpdt.model.enums;

public enum OrigenAut {
	E("Edición"), F("Finalización");

	private OrigenAut(String origen) {
		this.origen = origen;
	}

	private String origen;

	public String getOrigen() {
		return this.origen;
	}

}
