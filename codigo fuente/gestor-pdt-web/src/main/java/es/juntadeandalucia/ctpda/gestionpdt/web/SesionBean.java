package es.juntadeandalucia.ctpda.gestionpdt.web;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import es.juntadeandalucia.ctpda.gestionpdt.model.Usuario;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.Constantes;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.JsfUtils;
import lombok.Data;
import lombok.Getter;

@Data
@Component
@SessionScope
public class SesionBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Getter
	private Boolean menuOculto = false;
	
	
	
	@PostConstruct
	public void init() {
		//SIN INICIALIZAR NADA
	}

	public void toggleMenu() {
		this.menuOculto = !this.menuOculto;
	}
	
	//*************************************************
	//Mejoras posibles:
	//- Cachear en este bean la lista de permisos
	//- Cachear el Usuario
	// Estos datos no cambian en toda la sesión

	public Usuario getUsuarioSesion() {
		return (Usuario)JsfUtils.getSessionAttribute("usuario");
	}
	
	//Atajo
	public Long getIdUsuarioSesion() {		
		return this.getUsuarioSesion().getId();
	}
	
	public void setUsuarioSesion(Usuario usr) {
		JsfUtils.setSessionAttribute("usuario", usr);
	}	

	//--------------
	
	public void setCookie(String name, String value) {
		final String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
		final HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance()
				.getExternalContext().getResponse();

		final Cookie cookie = new Cookie(name, value);
		cookie.setPath(path);
		cookie.setMaxAge(-1);
		response.addCookie(cookie);
	}
	
	public void borrarCookie(String name) {
		final String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
		final HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance()
				.getExternalContext().getResponse();

		final Cookie cookie = new Cookie(name, null);
		cookie.setPath(path);
		cookie.setMaxAge(0); //0 = borrar
		response.addCookie(cookie);
	}
	
	public void borrarCookies() {
		this.borrarCookie(Constantes.COOKIE_UID);
		this.borrarCookie(Constantes.COOKIE_CODPERFIL);
	}
	
}
