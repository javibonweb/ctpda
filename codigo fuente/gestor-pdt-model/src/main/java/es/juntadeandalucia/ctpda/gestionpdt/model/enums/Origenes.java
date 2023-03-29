package es.juntadeandalucia.ctpda.gestionpdt.model.enums;

public enum Origenes {
	M("Manual"), A("Automático");

	private Origenes(String nombre) {
		this.nombre = nombre;
	}

	private String nombre;

	public String getNombre() {
		return this.nombre;
	}

}