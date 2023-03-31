package es.juntadeandalucia.ctpda.gestionpdt.web.formacionJmc;

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

import es.juntadeandalucia.ctpda.gestionpdt.model.FormacionPruebasJmc;
import es.juntadeandalucia.ctpda.gestionpdt.model.Usuario;
import es.juntadeandalucia.ctpda.gestionpdt.service.FormacionPruebasJmcService;
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
public class FormularioFormacionJmcBean extends BaseBean implements Serializable{
	
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
	private FormacionPruebasJmc formacionPruebasJmc;
	
	@Autowired
	private FormacionPruebasJmcService formacionPruebasJmcService;
	
	@Getter @Setter
	private Long selectedUsuario;
	
	@Getter @Setter
	private List<Usuario> listadoUsuarios;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Getter @Setter
	private Long idFormacionJmc;
	
	@Getter @Setter
	private boolean editableFormulario;
	
	
	@PostConstruct
	@Override
	public void init(){
		super.init();
		
		modoAcceso = (String) JsfUtils.getSessionAttribute("modoAcceso");
		idFormacionJmc = (Long) JsfUtils.getFlashAttribute("idFormacionJmc");
		editableFormulario = (boolean) JsfUtils.getFlashAttribute("editable");
		
		formacionPruebasJmc = new FormacionPruebasJmc();
		
		if(idFormacionJmc != null) {
			formacionPruebasJmc = formacionPruebasJmcService.obtener(idFormacionJmc);
			codigo = formacionPruebasJmc.getCodigo();
			descripcion = formacionPruebasJmc.getDescripcion();
			activo = formacionPruebasJmc.getActiva();
			selectedUsuario = (formacionPruebasJmc.getUsuario() != null)?formacionPruebasJmc.getUsuario().getId():null;
		}else {
			codigo = "";
			descripcion = "";
			activo = true;
			selectedUsuario = null;
		}
		
		listadoUsuarios = new ArrayList<>();
		listadoUsuarios = usuarioService.findUsuariosActivos();
	}
	
	public void saveFormacionJmc() {
		if(codigo != null && codigo.isEmpty()) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "El código tiene que ser obligatorio");
			PrimeFaces.current().dialog().showMessageDynamic(message);
		
		}else {
			try {
				formacionPruebasJmc.setCodigo(codigo);
				formacionPruebasJmc.setDescripcion(descripcion);
				formacionPruebasJmc.setActiva(activo);
				
				if(selectedUsuario != null) {
					Usuario usuarioSeleccionado = usuarioService.obtener(selectedUsuario);
					formacionPruebasJmc.setUsuario(usuarioSeleccionado);
				}				
								
				formacionPruebasJmcService.guardar(formacionPruebasJmc);
				log.info("Formacion JMC guardado");
				FacesContext.getCurrentInstance().addMessage("messagesFormularioFormacionJmc",new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Formacion " +formacionPruebasJmc.getCodigo()+ " JMC guardado correctamente"));
				
			} catch (BaseException e) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",mensajesProperties.getString("error"));
				PrimeFaces.current().dialog().showMessageDynamic(message);
				log.error(e.getMessage());
			}
		}
	}
}
