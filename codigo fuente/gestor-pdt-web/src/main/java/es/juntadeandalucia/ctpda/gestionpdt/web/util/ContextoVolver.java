package es.juntadeandalucia.ctpda.gestionpdt.web.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public class ContextoVolver implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Getter @Setter
	private String vista;	
	@Getter @Setter
	private String nombre;

	ContextoVolver() {
		super();
	}
	
	private transient Map<String, Object> atributos = new HashMap<>();
			
	public void put(String k, Object v){ atributos.put(k,  v); }
	public Object get(String k) { return atributos.get(k); }
	public boolean esContexto(String nombre) { return this.nombre.equals(nombre); }
	
}