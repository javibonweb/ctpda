package es.juntadeandalucia.ctpda.gestionpdt.web.util;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FacesUtils {
	
	private FacesUtils () {
		throw new IllegalStateException("Excepción en FacesUtils");
	}

	public static Integer getClientRowIndex(UIComponent ui) {
		final String[] cId = ui.getClientId().split(":");
		// Como no se la longitud de la ruta del id busco el índice desde el final
		return Integer.valueOf(cId[cId.length - 2]);
	}

	//---------------------------------------------------------------
	// Obtener variables EL

	public static <T> T getVar(String myVar, Class<T> clase) {
		final FacesContext ctx = FacesContext.getCurrentInstance();
		return ctx.getApplication().evaluateExpressionGet(ctx, "#{" + myVar + "}", clase);
	}

	//Atajos para Integer y String
	public static Integer getIntegerVar(String myVar) {
		return getVar(myVar, Integer.class);
	}

	public static String getStringVar(String myVar) {
		return getVar(myVar, String.class);
	}

	//***********************************************************************
	
	public static HttpServletRequest getRequest() {
		final FacesContext ctx = FacesContext.getCurrentInstance();
		return (HttpServletRequest)ctx.getExternalContext().getRequest();
	}

	public static HttpServletResponse getResponse() {
		final FacesContext ctx = FacesContext.getCurrentInstance();
		return (HttpServletResponse)ctx.getExternalContext().getResponse();
	}
	
	//------------------------------------------------------------------------
	
	public static Map<String, String> getRequestParameterMap() {
		FacesContext facesContext = FacesContext. getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		return externalContext.getRequestParameterMap();
	}
	
	public static String getRequestParameter(String nombre) {
		return getRequestParameterMap().get(nombre);
	}
	
	public static Object getAttribute(String nombre) {
		return FacesContext.getCurrentInstance().getAttributes()
								.get(nombre);
	}
	
	public static Object getAttribute(String nombre, Object defaultVal) {
		return FacesContext.getCurrentInstance().getAttributes()
								.getOrDefault(nombre, defaultVal);
	}
	
	public static void setAttribute(String nombre, Object valor) {
		FacesContext.getCurrentInstance().getAttributes()
			.put(nombre, valor);
	}
	
}
