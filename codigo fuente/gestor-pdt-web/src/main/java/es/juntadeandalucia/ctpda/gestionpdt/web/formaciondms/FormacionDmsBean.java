package es.juntadeandalucia.ctpda.gestionpdt.web.formaciondms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.primefaces.PrimeFaces;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.FormacionPruebasDms;
import es.juntadeandalucia.ctpda.gestionpdt.model.Usuario;
import es.juntadeandalucia.ctpda.gestionpdt.service.FormacionPruebasDmsService;
import es.juntadeandalucia.ctpda.gestionpdt.service.UsuarioService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.JsfUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.LazyDataModelByQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.MyFilterMeta;
import es.juntadeandalucia.ctpda.gestionpdt.web.menu.NavegacionBean.ListadoNavegaciones;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.BaseBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@ViewScoped
@Slf4j
public class FormacionDmsBean extends BaseBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String MODOACCESO = "modoAcceso";
	private static final String EDITABLE = "editable";
	
	@Getter @Setter
	private String codigoFiltro;
	
	@Getter @Setter
	private String descripcionFiltro;
	
	@Getter @Setter
	private Boolean activoFiltro;
	
	@Getter @Setter
	private Long versionFiltro;
	
	@Getter @Setter
	private Date fechaCreacionFiltro;
	
	@Getter @Setter
	private Long selectedUsuarioFiltro;
	
	@Getter @Setter
	private List<Usuario> listaUsuarios;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Getter
	private LazyDataModelByQueryService<FormacionPruebasDms> lazyModalFormacionPruebasDms;
	
	@Autowired
	private FormacionPruebasDmsService formacionPruebasDmsService;
	
	@Getter
	private SortMeta defaultOrdenListadoFormacionDms;
	
	@Getter @Setter
	private FormacionPruebasDms selectedFormacionDms;
	

	@PostConstruct
	@Override
	public void init(){
		super.init();
		
		activoFiltro = true;
		
		listaUsuarios = new ArrayList<>();
		listaUsuarios = usuarioService.findUsuariosActivos();
		
		lazyModalFormacionPruebasDms = new LazyDataModelByQueryService<>(FormacionPruebasDms.class, formacionPruebasDmsService);
		lazyModalFormacionPruebasDms.setPreproceso((a, b, c, filters) -> {
			if (codigoFiltro != null && !codigoFiltro.isEmpty()) {
				filters.put("codigo", new MyFilterMeta(codigoFiltro));
			}		
			if (descripcionFiltro != null && !descripcionFiltro.isEmpty()) {
				filters.put("descripcion", new MyFilterMeta(descripcionFiltro));
			}
			if(Boolean.TRUE.equals(activoFiltro)) {
				filters.put("activa", new MyFilterMeta(activoFiltro));
			}
			if(versionFiltro != null && !Long.toString(versionFiltro).isEmpty()) {
				filters.put("nVersion", new MyFilterMeta(versionFiltro));
			}
			if(fechaCreacionFiltro != null) {
				filters.put("fechaCreacion", new MyFilterMeta(fechaCreacionFiltro));
			}
			if(selectedUsuarioFiltro != null) {
				filters.put("usuario.id", new MyFilterMeta(selectedUsuarioFiltro));
			}			
		});
		
		defaultOrdenListadoFormacionDms = SortMeta.builder().field("codigo").order(SortOrder.ASCENDING).priority(1).build();
		
		JsfUtils.removeSessionAttribute(MODOACCESO);
	}
	
	public String redireccionMenu() {
		init();
		return ListadoNavegaciones.LISTADO_FORMACIONDMS.getRegla();
	}
	
	public String onCrear() {
		JsfUtils.setFlashAttribute(EDITABLE, true);
		JsfUtils.setSessionAttribute(MODOACCESO, "alta");
		log.info("Accedemos al formulario en modo alta");
		return ListadoNavegaciones.FORM_FORMACIONDMS.getRegla();
	}
	
	public void limpiarFiltro () {
		codigoFiltro = "";
		descripcionFiltro = "";
		activoFiltro = true;
		versionFiltro = null;
		fechaCreacionFiltro = null;
		selectedUsuarioFiltro = null;		
	}
	
	public String onEditar(Long idFormacionDms) {
		JsfUtils.setFlashAttribute(EDITABLE, true);
		JsfUtils.setSessionAttribute(MODOACCESO, "edicion");
		JsfUtils.setFlashAttribute("idFormacionDms", idFormacionDms);
		return ListadoNavegaciones.FORM_FORMACIONDMS.getRegla();
	}
	
	public String onConsultar(Long idFormacionDms) {
		JsfUtils.setFlashAttribute(EDITABLE, false);
		JsfUtils.setSessionAttribute(MODOACCESO, "consulta");
		JsfUtils.setFlashAttribute("idFormacionDms", idFormacionDms);
		return ListadoNavegaciones.FORM_FORMACIONDMS.getRegla();
	}
	
	public void eliminarFormacionDms (FormacionPruebasDms formacionPruebasDms) {
		try {
			formacionPruebasDmsService.delete(formacionPruebasDms.getId());
			FacesContext.getCurrentInstance().addMessage("messagesListadoFormaciondms",new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Formacion DMS borrado correctamente"));
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",mensajesProperties.getString("error"));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
	}
	
	public void desactivarFormacionDms (FormacionPruebasDms formacionPruebasDms) {
		try {
			formacionPruebasDms.setActiva(false);		
			formacionPruebasDmsService.guardar(formacionPruebasDms);
			FacesContext.getCurrentInstance().addMessage("messagesListadoFormaciondms",new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Formacion DMS desactivado correctamente"));
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",mensajesProperties.getString("error"));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
	}
	
}