package es.juntadeandalucia.ctpda.gestionpdt.web.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.web.core.Constantes;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.JsfUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.menu.NavegacionBean;
import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
@ViewScoped
public class VolverBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private static final String ATR_VOLVER = "__volver__";

	//-----------------
	
	private Map<String, ContextoVolver> contextoMap;
	
	@Autowired
	private NavegacionBean navegacionBean;
	
	@PostConstruct
	public void init() {
		contextoMap = new HashMap<>();
	}
	
	//-------------------
	
	public ContextoVolver crearContexto(String rutaVolver) {
		return crearContexto(rutaVolver, ATR_VOLVER);
	}
	
	public ContextoVolver crearContexto(String rutaVolver, String varVolver) {
		final ContextoVolver ctx = new ContextoVolver();
		ctx.setNombre(varVolver);
		ctx.setVista(rutaVolver);
		contextoMap.put(varVolver,  ctx);
		pasarContexto(ctx, varVolver);
		return ctx;
	}
	
	public ContextoVolver getContexto() {
		return getContexto(ATR_VOLVER);
	}
	
	public ContextoVolver getContexto(String varVolver) {
		return contextoMap.get(varVolver);
	}
	
	//Poner en flash
	public void pasarContexto(ContextoVolver ctx) {
		this.pasarContexto(ctx, ATR_VOLVER);
	}
	
	//Recoger del flash
	public ContextoVolver recogerContexto() {
		return recogerContexto(ATR_VOLVER);
	}
	
	//Poner en flash
	public void pasarContexto(ContextoVolver ctx, String varVolver) {
		JsfUtils.setFlashAttribute(varVolver, ctx);
	}
	
	//Recoger del flash
	public ContextoVolver recogerContexto(String varVolver) {
		final ContextoVolver ctx = (ContextoVolver) JsfUtils.getFlashAttribute(varVolver);
		contextoMap.put(varVolver,  ctx);
		return ctx;
	}
	
	public void limpiarContexto() {
		limpiarContexto(ATR_VOLVER);
	}
	
	public void limpiarContexto(String varVolver) {
		contextoMap.put(varVolver,  null);
	}
	
	// Para los botones volver
	public String onVolver(String defaultRuta) {
		return onVolver(ATR_VOLVER, defaultRuta);
	}
	
	/**
	 * Prepara la vuelta atrás desde el método que invoca a este.
	 * @param varVolver Nombre del contexto de retorno a utilizar 
	 * @param defaultRuta Ruta a la que navegar normalmente cuando no existe contexto de retorno
	 * @return defaultRuta si no hay contexto, en otro caso devuelve la ruta del atributo vista del contexto.
	 */
	public String onVolver(String varVolver, String defaultRuta) {
		final ContextoVolver ctx = this.getContexto(varVolver);
		final String ruta; 
		
		if(ctx == null) {
			ruta = defaultRuta;
		} else {
			ruta = ctx.getVista();
			pasarContexto(ctx,varVolver);
		}

		return ruta;
	}
	
	public void migaPanVolver() {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, ((Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS))-1);
		navegacionBean.setTxtMigaPan("");
	}
	
	public void migaPanVolver(String textoMigaPan) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, ((Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS))-1);
		navegacionBean.setTxtMigaPan(textoMigaPan);
	}
	
	public void cancelar() {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, ((Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS))-1);
	}
}

