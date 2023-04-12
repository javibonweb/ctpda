package es.juntadeandalucia.ctpda.gestionpdt.web.formaciondms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.FormacionPruebasDms;
import es.juntadeandalucia.ctpda.gestionpdt.model.Usuario;
import es.juntadeandalucia.ctpda.gestionpdt.service.FormacionPruebasDmsService;
import es.juntadeandalucia.ctpda.gestionpdt.service.UsuarioService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.JsfUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.BaseBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@ViewScoped
@Slf4j
public class FormularioFormacionDmsBean extends BaseBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Getter @Setter
	private String modoAcceso;
	
	@Getter @Setter
	private String codigo;
	
	@Getter @Setter
	private String descripcion;
	
	@Getter @Setter
	private Boolean activo;
	
	@Getter @Setter
	private FormacionPruebasDms formacionPruebasDms;
	
	@Autowired
	private FormacionPruebasDmsService formacionPruebasDmsService;
	
	@Getter @Setter
	private Long selectedUsuario;
	
	@Getter @Setter
	private List<Usuario> listadoUsuarios;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Getter @Setter
	private Long idFormacionDms;
	
	@Getter @Setter
	private boolean editableFormulario;
	
	
	@PostConstruct
	@Override
	public void init(){
		super.init();
		
		modoAcceso = (String) JsfUtils.getSessionAttribute("modoAcceso");
		idFormacionDms = (Long) JsfUtils.getFlashAttribute("idFormacionDms");
		editableFormulario = (boolean) JsfUtils.getFlashAttribute("editable");
		
		formacionPruebasDms = new FormacionPruebasDms();
		
		if(idFormacionDms != null) {
			formacionPruebasDms = formacionPruebasDmsService.obtener(idFormacionDms);
			codigo = formacionPruebasDms.getCodigo();
			descripcion = formacionPruebasDms.getDescripcion();
			activo = formacionPruebasDms.getActiva();
			selectedUsuario = (formacionPruebasDms.getUsuario() != null)?formacionPruebasDms.getUsuario().getId():null;
		}else {
			codigo = "";
			descripcion = "";
			activo = true;
			selectedUsuario = null;
		}
		
		listadoUsuarios = new ArrayList<>();
		listadoUsuarios = usuarioService.findUsuariosActivos();
	}
	
	public void saveFormacionDms() {
		if(codigo != null && codigo.isEmpty()) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "El código tiene que ser obligatorio");
			PrimeFaces.current().dialog().showMessageDynamic(message);
		
		}else {
			try {
				formacionPruebasDms.setCodigo(codigo);
				formacionPruebasDms.setDescripcion(descripcion);
				formacionPruebasDms.setActiva(activo);
				
				if(selectedUsuario != null) {
					Usuario usuarioSeleccionado = usuarioService.obtener(selectedUsuario);
					formacionPruebasDms.setUsuario(usuarioSeleccionado);
				}				
								
				formacionPruebasDmsService.guardar(formacionPruebasDms);
				log.info("Formacion DMS guardado");
				FacesContext.getCurrentInstance().addMessage("messagesFormularioFormacionDms",new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Formacion " +formacionPruebasDms.getCodigo()+ " DMS guardado correctamente"));
				
			} catch (BaseException e) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",mensajesProperties.getString("error"));
				PrimeFaces.current().dialog().showMessageDynamic(message);
				log.error(e.getMessage());
			}
		}
	}
}
