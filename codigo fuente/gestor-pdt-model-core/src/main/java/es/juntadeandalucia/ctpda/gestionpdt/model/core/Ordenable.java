package es.juntadeandalucia.ctpda.gestionpdt.model.core;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;


public interface Ordenable extends Comparable<Ordenable>, Serializable {
	
	static final Integer TAM_ORDEN=3;
	static final Long DEFAULT_ORDEN=1L;
	static final Long ORDEN_VACIO=0L;

	public static final Comparator<Ordenable> comparator = (obj0, obj1) -> {			
			if (obj0 == null && obj1 == null) return 0; 
			if (obj0 == null) return -1;
			if (obj1 == null) return +1;
			
			Long o0 = obj0.getOrden();
			Long o1 = obj1.getOrden();
			
			if (o0 == null && o1 == null) return 0; //No debe ocurrir

			if(o0 == null) { //no debe ocurrir
				o0 = ORDEN_VACIO;
			}			

			return o0.compareTo(o1);
		};
	
	//-------------------------------
	
	Long getOrden();
	void setOrden(Long orden);

	public static String orden2str(Integer orden) {
		return StringUtils.leftPad(orden.toString(), TAM_ORDEN, "0");
	}

	public static <T extends Ordenable> void cambiarOrden(List<T> lista, Integer idx1, Integer idx2) {
		final T resp1 = lista.get(idx1);
		final T resp2 = lista.get(idx2);

		final Long aux = resp1.getOrden();
		resp1.setOrden(resp2.getOrden());
		resp2.setOrden(aux);

		lista.set(idx1,  resp2);
		lista.set(idx2,  resp1);
	}

	public static <T extends Ordenable> void subirOrden1(List<T> lista, Integer idx) {
		if(idx > 0) {
			cambiarOrden(lista, idx, idx - 1);
		}
	}

	public static <T extends Ordenable> void bajarOrden1(List<T> lista, Integer idx) {
		if(idx > 0) {
			cambiarOrden(lista, idx, idx + 1);
		}
	}

	public static <T extends Ordenable> void subirOrdenTop(List<T> lista, Integer idx) {
		if(idx > 0) {
			cambiarOrden(lista, idx, 0);
		}
	}
	
	public default int compareTo(Ordenable ord) { 
		return comparator.compare(this, ord);
	}
	
    public default void subirOrden() {
    	Long ord = this.getOrden();
    	
    	if(ord > DEFAULT_ORDEN)
    		ord--;
    	
    	this.setOrden(ord);   	
    }

    public default void bajarOrden() {
    	this.setOrden(this.getOrden()+1);
    }

    public default void moverOrden(Integer i) {
    	Long ord = this.getOrden() + i;
    	    	
    	if(ord < DEFAULT_ORDEN)
    		ord = DEFAULT_ORDEN;
    	
    	this.setOrden(ord);
    }


	public default void setDefaultOrden() {
		this.setOrden(DEFAULT_ORDEN);
	}
	
	public static <T extends Ordenable> void resetOrden(List<T> lista, Long orden) {
		for(final Ordenable o : lista) {
			o.setOrden(orden++);
		}
	}

}
