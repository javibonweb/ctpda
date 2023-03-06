package es.juntadeandalucia.ctpda.gestionpdt.web.expedientes;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.onlyoffice.integration.DocumentServiceSupport;

import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.Usuario;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.Constantes;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.JsfUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.BaseBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@ViewScoped
public class DocumentosExpedientesBean  extends BaseBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private Expedientes expedientes;
	
	@Autowired
	private DocumentServiceSupport documentosServiceSupport;


	@Override
	@PostConstruct
	public void init() {
		//vacío
	}

	public String getApiURL() {
		return documentosServiceSupport.getServerUrl();
	}
	
	public String getIdUsuario() {
		final Usuario usuario = (Usuario) JsfUtils.getSessionAttribute(Constantes.USUARIO);
		return usuario.getId().toString();
	}
	
	public String getCodPerfilUsuario() {
		return (String) JsfUtils.getSessionAttribute(Constantes.COD_PERFIL_USUARIO);
	}
	
}
