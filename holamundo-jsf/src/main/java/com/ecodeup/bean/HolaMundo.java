package com.ecodeup.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

//indicamos al framework que esto es un bean y ponemos nombre al bean
//para llamarlo desde la vista holaMundo.xhtml
@ManagedBean(name="holaM")
@SessionScoped
public class HolaMundo {
	private String saludo ="Hola Mundo desde JSF";

	public String getSaludo() {
		return saludo;
	}

	public void setSaludo(String saludo) {
		this.saludo = saludo;
	}
	
}
