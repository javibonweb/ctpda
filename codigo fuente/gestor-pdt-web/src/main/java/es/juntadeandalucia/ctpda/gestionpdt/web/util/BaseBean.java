package es.juntadeandalucia.ctpda.gestionpdt.web.util;

import java.io.Serializable;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;

import org.primefaces.PrimeFaces;

import es.juntadeandalucia.ctpda.gestionpdt.util.StringUtils;
import lombok.extern.slf4j.Slf4j;



@Slf4j
public abstract class BaseBean implements Serializable {
	
	private static final long serialVersionUID = -5031650966485063220L;
	
	protected static final String CLAVE_ERROR_GENERICO="error";
	protected static final String CLAVE_CAMPOS_OBLIGATORIOS="campos.obligatorios";
	
	protected static final String CALLBACK_PARAM_SAVED="saved";
	
	protected transient ResourceBundle mensajesProperties;
	private Boolean formEditable;

	protected void errorGeneral(Throwable e) {
		log.error(e.getMessage(), e);
		log.trace(e.getMessage(), e);
	}

	public void init() {
		mensajesProperties = ResourceBundle.getBundle("es.juntadeandalucia.ctpda.gestionpdt.web.Messages_es");
	}

	public Boolean getFormEditable() {
		if(formEditable == null) {
			initFormEditable();
		}
		
		return formEditable; 
	}
	
	private void initFormEditable() {
		Boolean b = (Boolean)FacesUtils.getAttribute("editable", Boolean.TRUE);
		formEditable=b;
	}
	
	public void setFormEditable(boolean b) {
		formEditable=b;
	}

	//**************************************
		
	protected void facesMsgInfo(String msg) {
		facesMessage(msg, FacesMessage.SEVERITY_INFO);
	}
	
	protected void facesMsgInfoKey(String... msgKeys) {
		facesMsgInfo(getMessages(msgKeys));
	}
	
	//----
	
	protected void facesMsgWarn(String msg) {
		facesMessage(msg, FacesMessage.SEVERITY_WARN);
	}
	
	protected void facesMsgWarnKey(String... msgKeys) {
		facesMsgInfo(getMessages(msgKeys));
	}
	
	//----
	
	protected void facesMsgErrorKey(String... errorMsgKeys) {
		facesMsgError(getMessages(errorMsgKeys));
	}
	
	protected void facesMsgError(String msg) {
		facesMessage(msg, FacesMessage.SEVERITY_ERROR);
	}
		
	protected void facesMsgErrorGenerico() {
		facesMsgErrorKey(CLAVE_ERROR_GENERICO);
	}
	
	//----
	
	protected String getMessage(String key) {
		return mensajesProperties.getString(key);
	}
	
	private void facesMessage(String txt, Severity severity) {
		FacesMessage message = new FacesMessage(severity, "", txt);
		PrimeFaces.current().dialog().showMessageDynamic(message);
	}
	
	/**
	 * Recoge los mensajes correspondientes a las claves y los devuelve concatenados y separados por espacios.
	 * @param key
	 * @return
	 */
	protected String getMessages(String... key) {
		final String[] mm = new String[key.length];
		
		for(int i=0 ; i < key.length ; ++i) {
			mm[i] = mensajesProperties.getString(key[i]);
		}
		
		return StringUtils.join(mm, StringUtils.SPACE);
	}

	//-----------------------------------
	/**
	 * Ejecuta el ajax update de PrimeFaces. 
	 * Permite encadenar prefijos para componer un id JSF. El encadenado aplica el separador ":" a cada componente.
	 * P. Ej: 
	 *    pfUpdate("formMain", "tabPrincipal", "inputNombre");
	 *    pfUpdate("formMain:tabPrincipal", "inputNombre");
	 *    pfUpdate("formMain:tabPrincipal:inputNombre");
	 * @param comps
	 */
	protected void pfUpdate(String... comps) {
		PrimeFaces.current().ajax().update(
				StringUtils.join(comps, ":")
			);
	}
	
}
