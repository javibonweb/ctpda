package es.juntadeandalucia.ctpda.gestionpdt.web.core;

import org.primefaces.model.FilterMeta;

public class MyFilterMeta extends FilterMeta {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MyFilterMeta(Object valor) {
		this.setFilterValue(valor);
	}

}
