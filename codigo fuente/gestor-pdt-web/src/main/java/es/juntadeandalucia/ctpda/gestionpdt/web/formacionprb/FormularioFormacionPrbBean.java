package es.juntadeandalucia.ctpda.gestionpdt.web.formacionprb;

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

import es.juntadeandalucia.ctpda.gestionpdt.model.FormacionPruebasPrb;
import es.juntadeandalucia.ctpda.gestionpdt.model.Usuario;
import es.juntadeandalucia.ctpda.gestionpdt.service.FormacionPruebasPrbService;
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
public class FormularioFormacionPrbBean<formacionPruebasPrbService> extends BaseBean implements Serializable{

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
	private FormacionPruebasPrb formacionPruebasPrb;

	@Autowired
	private FormacionPruebasPrbService formacionPruebasPrbService;

	@Getter @Setter
	private Long selectedUsuario;

	@Getter @Setter
	private List<Usuario> listadoUsuarios;

	@Autowired
	private UsuarioService usuarioService;

	@Getter @Setter
	private Long idFormacionBlh;

	@Getter @Setter
	private boolean editableFormulario;


	@PostConstruct
	@Override
	public void init(){
		super.init();

		modoAcceso = (String) JsfUtils.getSessionAttribute("modoAcceso");
		idFormacionBlh = (Long) JsfUtils.getFlashAttribute("idFormacionBlh");
		editableFormulario = (boolean) JsfUtils.getFlashAttribute("editable");

		formacionPruebasPrb = new FormacionPruebasPrb();

		Object idFormacionPrb = null;
		if(idFormacionPrb != null) {
			formacionPruebasPrb = formacionPruebasPrbService.obtener(idFormacionBlh);
			codigo = formacionPruebasPrb.getCodigo();
			descripcion = formacionPruebasPrb.getDescripcion();
			activo = formacionPruebasPrb.getActiva();
			selectedUsuario = (formacionPruebasPrb.getUsuario() != null)?formacionPruebasPrb.getUsuario().getId():null;
		}else {
			codigo = "";
			descripcion = "";
			activo = true;
			selectedUsuario = null;
		}

		listadoUsuarios = new ArrayList<>();
		listadoUsuarios = usuarioService.findUsuariosActivos();
	}

	public void saveFormacionBlh() {
		if(codigo != null && codigo.isEmpty()) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "El código tiene que ser obligatorio");
			PrimeFaces.current().dialog().showMessageDynamic(message);

		}else {
			try {
				formacionPruebasPrb.setCodigo(codigo);
				formacionPruebasPrb.setDescripcion(descripcion);
				formacionPruebasPrb.setActiva(activo);

				if(selectedUsuario != null) {
					Usuario usuarioSeleccionado = usuarioService.obtener(selectedUsuario);
					formacionPruebasPrb.setUsuario(usuarioSeleccionado);
				}				

				formacionPruebasPrbService.guardar(formacionPruebasPrb);
				log.info("Formacion BLH guardado");
				FacesContext.getCurrentInstance().addMessage("messagesFormularioFormacionBlh",new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Formacion " +formacionPruebasPrb.getCodigo()+ " BLH guardado correctamente"));

			} catch (BaseException e) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",mensajesProperties.getString("error"));
				PrimeFaces.current().dialog().showMessageDynamic(message);
				log.error(e.getMessage());
			}
		}
	}
}