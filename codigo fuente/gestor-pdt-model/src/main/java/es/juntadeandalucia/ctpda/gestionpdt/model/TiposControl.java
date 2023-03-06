package es.juntadeandalucia.ctpda.gestionpdt.model;

public enum TiposControl {
	D("Días"), S("Semanas"), M("Meses");

	private TiposControl(String nombre) {
		this.nombre = nombre;
	}

	private String nombre;

	public String getNombre() {
		return this.nombre;
	}

}