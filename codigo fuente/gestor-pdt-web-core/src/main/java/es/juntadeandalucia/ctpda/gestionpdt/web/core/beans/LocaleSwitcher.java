package es.juntadeandalucia.ctpda.gestionpdt.web.core.beans;

import java.io.Serializable;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import lombok.extern.slf4j.Slf4j;
@Component
@SessionScope
@Slf4j
/**
 * Clase para modificar el idioma de visualización @Sessionscoped.
 * */
public class LocaleSwitcher implements Serializable {

	private static final long serialVersionUID = 1454354354543L;

	/**
	 * Creates a new instance of LocaleSwitcher
	 */
	public LocaleSwitcher() {
		log.debug("Iniciando nuevo LocaleSwitcher");
	}

	// default language - la lengua de cervantes
	private String language = "es";

	// get the current locale from facescontext
	private Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	// when dropdown value gets changed - so the language and locale
	public void onLanguageChange(ValueChangeEvent vce) {
		if (vce != null) {
			String l = vce.getNewValue().toString();
			setLanguage(l);
			locale = new Locale(l);
			FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
		} else {
			FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale("es"));
		}
	}

	public void forceLanguageChange(String languaje) {
		if (languaje!=null) {
			setLanguage(languaje);
			locale = new Locale(languaje);
			FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
		}
	}
}
