package es.juntadeandalucia.ctpda.gestionpdt.model.enums;

public enum TipoAccion {
	C("Creación"), M ("Modificación"), F("Finalización");

	private TipoAccion(String tipAccion) {
		this.tipAccion = tipAccion;
	}

	private String tipAccion;

	public String getTipoAccion() {
		return this.tipAccion;
	}

}