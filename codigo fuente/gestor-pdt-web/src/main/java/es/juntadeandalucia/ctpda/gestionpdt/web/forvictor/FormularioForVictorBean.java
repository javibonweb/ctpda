package es.juntadeandalucia.ctpda.gestionpdt.web.forvictor;

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

import es.juntadeandalucia.ctpda.gestionpdt.model.FormacionPruebaVdc;
import es.juntadeandalucia.ctpda.gestionpdt.model.Usuario;
import es.juntadeandalucia.ctpda.gestionpdt.service.FormacionPruebaVdcService;
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
public class FormularioForVictorBean extends BaseBean implements Serializable{
	
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
	private FormacionPruebaVdc formacionPruebaVdc;
	
	@Autowired
	private FormacionPruebaVdcService formacionPruebaVdcService;
	
	@Getter @Setter
	private Long selectedUsuario;
	
	@Getter @Setter
	private List<Usuario> listadoUsuarios;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Getter @Setter
	private Long idForVictor;
	
	@Getter @Setter
	private boolean editableFormulario;
	
	
	@PostConstruct
	@Override
	public void init(){
		super.init();
		
		modoAcceso = (String) JsfUtils.getSessionAttribute("modoAcceso");
		idForVictor = (Long) JsfUtils.getFlashAttribute("idForVictor");
		editableFormulario = (boolean) JsfUtils.getFlashAttribute("editable");
		
		formacionPruebaVdc = new FormacionPruebaVdc();
		
		if(idForVictor != null) {
			formacionPruebaVdc = formacionPruebaVdcService.obtener(idForVictor);
			codigo = formacionPruebaVdc.getCodigo();
			descripcion = formacionPruebaVdc.getDescripcion();
			activo = formacionPruebaVdc.getActiva();
			selectedUsuario = (formacionPruebaVdc.getUsuario() != null)?formacionPruebaVdc.getUsuario().getId():null;
		}else {
			codigo = "";
			descripcion = "";
			activo = true;
			selectedUsuario = null;
		}
		
		listadoUsuarios = new ArrayList<>();
		listadoUsuarios = usuarioService.findUsuariosActivos();
	}
	
	public void saveForVictor() {
		if(codigo != null && codigo.isEmpty()) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "El código tiene que ser obligatorio");
			PrimeFaces.current().dialog().showMessageDynamic(message);
		
		}else {
			try {
				formacionPruebaVdc.setCodigo(codigo);
				formacionPruebaVdc.setDescripcion(descripcion);
				formacionPruebaVdc.setActiva(activo);
				
				if(selectedUsuario != null) {
					Usuario usuarioSeleccionado = usuarioService.obtener(selectedUsuario);
					formacionPruebaVdc.setUsuario(usuarioSeleccionado);
				}				
								
				formacionPruebaVdcService.guardar(formacionPruebaVdc);
				log.info("Formacion VDC guardado");
				FacesContext.getCurrentInstance().addMessage("messagesFormularioForVictor",new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Formacion " +formacionPruebaVdc.getCodigo()+ " VDC guardado correctamente"));
				
			} catch (BaseException e) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",mensajesProperties.getString("error"));
				PrimeFaces.current().dialog().showMessageDynamic(message);
				log.error(e.getMessage());
			}
		}
	}
}