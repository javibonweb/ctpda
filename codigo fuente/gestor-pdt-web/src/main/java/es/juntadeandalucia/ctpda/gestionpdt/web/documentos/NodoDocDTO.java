package es.juntadeandalucia.ctpda.gestionpdt.web.documentos;

import java.io.Serializable;

import lombok.Data;

@Data
public class NodoDocDTO implements Comparable<NodoDocDTO>, Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String descripcion;
	private Long orden;
	
	@Override
	public int compareTo(NodoDocDTO nodo) {
		return this.orden.compareTo(nodo.orden);
	}
}	


