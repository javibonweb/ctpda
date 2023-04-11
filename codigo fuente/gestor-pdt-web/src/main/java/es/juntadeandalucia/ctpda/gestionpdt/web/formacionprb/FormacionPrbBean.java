package es.juntadeandalucia.ctpda.gestionpdt.web.formacionprb;
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

import es.juntadeandalucia.ctpda.gestionpdt.model.Usuario;
import es.juntadeandalucia.ctpda.gestionpdt.service.FormacionPruebasPrbService;
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
public class FormacionPrbBean extends BaseBean implements Serializable{
	
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
	private LazyDataModelByQueryService<FormacionPruebasPrb> lazyModalFormacionPruebasBlh;

	@Autowired
	private FormacionPruebasPrbService formacionPruebasBlhService;

	@Getter
	private SortMeta defaultOrdenListadoFormacionPrb;

	@Getter @Setter
	private FormacionPruebasPrb selectedFormacionPrb;


	@PostConstruct
	@Override
	public void init(){
		super.init();
		
		activoFiltro = true;
		
		listaUsuarios = new ArrayList<>();
		listaUsuarios = usuarioService.findUsuariosActivos();

		lazyModalFormacionPruebasPrb = new LazyDataModelByQueryService<>(FormacionPruebasPrb.class, formacionPruebasPrbService);
		lazyModalFormacionPruebasPrb.setPreproceso((a, b, c, filters) -> {
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

		defaultOrdenListadoFormacionPrb = SortMeta.builder().field("codigo").order(SortOrder.ASCENDING).priority(1).build();

		JsfUtils.removeSessionAttribute(MODOACCESO);
	}

	public String redireccionMenu() {
		init();
		return ListadoNavegaciones.LISTADO_FORMACIONPRB.getRegla();
	}

	public String onCrear() {
		return "";
		JsfUtils.setFlashAttribute(EDITABLE, true);
		JsfUtils.setSessionAttribute(MODOACCESO, "alta");
		log.info("Accedemos al formulario en modo alta");
		return ListadoNavegaciones.FORM_FORMACIONPRB.getRegla();
	}

	public void limpiarFiltro () {
		codigoFiltro = "";
		descripcionFiltro = "";
		activoFiltro = true;
		versionFiltro = null;
		fechaCreacionFiltro = null;
		selectedUsuarioFiltro = null;		
	}

	public String onEditar(Long idFormacionBlh) {
		JsfUtils.setFlashAttribute(EDITABLE, true);
		JsfUtils.setSessionAttribute(MODOACCESO, "edicion");
		JsfUtils.setFlashAttribute("idFormacionBlh", idFormacionBlh);
		return ListadoNavegaciones.FORM_FORMACIONPRB.getRegla();
	}

	public String onConsultar(Long idFormacionPrb) {
		JsfUtils.setFlashAttribute(EDITABLE, false);
		JsfUtils.setSessionAttribute(MODOACCESO, "consulta");
		JsfUtils.setFlashAttribute("idFormacionBlh", idFormacionPrb);
		return ListadoNavegaciones.FORM_FORMACIONPRB.getRegla();
	}

	public void eliminarFormacionBlh (FormacionPruebasPrb formacionPruebasBlh) {
		try {
			formacionPruebasPrbService.delete(formacionPruebasBlh.getId());
			FacesContext.getCurrentInstance().addMessage("messagesListadoFormacionblh",new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Formacion BLH borrado correctamente"));
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",mensajesProperties.getString("error"));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
	}

	public void desactivarFormacionBlh (FormacionPruebasPrb formacionPruebasBlh) {
		try {
			formacionPruebasPrb.setActiva(false);		
			formacionPruebasPrbService.guardar(formacionPruebasBlh);
			FacesContext.getCurrentInstance().addMessage("messagesListadoFormacionblh",new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Formacion BLH desactivado correctamente"));
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",mensajesProperties.getString("error"));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
	}

}