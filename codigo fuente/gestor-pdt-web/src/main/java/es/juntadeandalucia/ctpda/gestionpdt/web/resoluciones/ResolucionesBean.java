package es.juntadeandalucia.ctpda.gestionpdt.web.resoluciones;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.Resolucion;
import es.juntadeandalucia.ctpda.gestionpdt.model.ResolucionMaestra;
import es.juntadeandalucia.ctpda.gestionpdt.service.ResolucionMaestraService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ResolucionService;
import es.juntadeandalucia.ctpda.gestionpdt.util.StringUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.Constantes;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.JsfUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.LazyDataModelByQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.MyFilterMeta;
import es.juntadeandalucia.ctpda.gestionpdt.web.menu.NavegacionBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.menu.NavegacionBean.ListadoNavegaciones;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.BaseBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@ViewScoped
public class ResolucionesBean extends BaseBean {

	private static final long serialVersionUID = 1L;
	
	private static final String EDITABLE= "editable";

	@Autowired
	private ResolucionMaestraService resolucionMaestraService;
	@Getter @Setter
	private String numeroResolucionFiltro;
	@Getter @Setter
	private Date fechaInicialFiltro;
	@Getter @Setter
	private Date fechaFinalFiltro;
	@Getter @Setter
	private Long selectedResponsableIdFiltro;
	@Getter @Setter
	private String sujetoObligadoFiltro;
	@Getter @Setter
	private String personaFiltro;
	@Getter @Setter
	private String identPersonaFiltro;
	
	@Getter @Setter
	private boolean permisoVerResol;
	@Getter @Setter
	private boolean permisoEditarResol;
	
	
	@Getter
	private LazyDataModelByQueryService<ResolucionMaestra> lazyModel;
	
	@Getter
	private SortMeta defaultOrden;
	
	@Autowired
	private NavegacionBean navegacionBean;
	
	@Autowired
	private ResolucionService resolucionService;
	
	@Getter
	@Setter
	private List<String> listaCodigoPermisos;
	
	public String redireccionMenu() {
		return ListadoNavegaciones.LISTADO_RESOLUCIONES.getRegla();
	}
	
	@Override
	@PostConstruct
	public void init() {
		super.init();
		
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, 0);
		
		navegacionBean.setTxtMigaPan(Constantes.LISTADO_RESOLUCIONES);
		
		cargarPermisosUsuario();
		inicializaLazyModel();

	}
		
	private void cargarPermisosUsuario() {
		listaCodigoPermisos = (List<String>) JsfUtils.getSessionAttribute(Constantes.LISTACODIGOPERMISOS);
		permisoVerResol = listaCodigoPermisos.contains(Constantes.PERMISO_VER_RESOL);
		permisoEditarResol = listaCodigoPermisos.contains(Constantes.PERMISO_EDIT_RESOL);
	}
	
	private void inicializaLazyModel() {
		lazyModel= new LazyDataModelByQueryService<>(ResolucionMaestra.class, resolucionMaestraService);
		lazyModel.setPreproceso((a,b,c,filters)->{
			
			if(!StringUtils.isBlank(this.numeroResolucionFiltro)) {
				filters.put("codigoResolucion", new MyFilterMeta(this.numeroResolucionFiltro));				
			}
			
			if(null != this.fechaInicialFiltro) {
				filters.put("#fechaInicial", new MyFilterMeta(this.fechaInicialFiltro));				
			}
								
			if(null != this.fechaFinalFiltro) {
				filters.put("#fechaFinal", new MyFilterMeta(this.fechaFinalFiltro));				
			}
			
			if(!StringUtils.isBlank(this.personaFiltro)) {
				filters.put("nombrePersona", new MyFilterMeta(this.personaFiltro));				
			}
			
			if(!StringUtils.isBlank(this.identPersonaFiltro)) {
				filters.put("nifPersona", new MyFilterMeta(this.identPersonaFiltro));				
			}			
								
			if(!StringUtils.isBlank(this.sujetoObligadoFiltro)) {
				filters.put("nombreSujetoObligado", new MyFilterMeta(this.sujetoObligadoFiltro));				
			}
			
		});
		
		defaultOrden = SortMeta.builder().field("fechaResolucion").order(SortOrder.DESCENDING).priority(1).build();
	}

		
	public void limpiarFiltro() {		
		this.numeroResolucionFiltro = null;
		this.fechaInicialFiltro = null;
		this.fechaFinalFiltro = null;
		this.sujetoObligadoFiltro=null;
		this.personaFiltro=null;
		this.identPersonaFiltro=null;
	}

	//*************************************************************
	
	public void onBuscarResoluciones() {
		/**
		 * PENDIENTE DESARROLLO
		 */
	}
	
	public String onEditar(Long idResol) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, ((Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS))+1);
		JsfUtils.setFlashAttribute("idRes", idResol);
		JsfUtils.setFlashAttribute(EDITABLE, true);
		Resolucion resolucion = resolucionService.obtener(idResol);
		navegacionBean.setTxtMigaPan(Constantes.EDICION_RESOLUCION+resolucion.getCodigoResolucion());
		return NavegacionBean.ListadoNavegaciones.FORM_RESOLUCION.getRegla();
	}
	
	public String onConsultar(Long idResol) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, ((Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS))+1);
		JsfUtils.setFlashAttribute("idRes", idResol);
		JsfUtils.setFlashAttribute(EDITABLE, false);
		Resolucion resolucion = resolucionService.obtener(idResol);
		navegacionBean.setTxtMigaPan(Constantes.CONSULTA_RESOLUCION+resolucion.getCodigoResolucion());
		return NavegacionBean.ListadoNavegaciones.FORM_RESOLUCION.getRegla();
	}
	
	public void onResolRowDblClkSelect(final SelectEvent<ResolucionMaestra> event) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, ((Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS))+1);
		JsfUtils.setFlashAttribute(EDITABLE, false);
		ResolucionMaestra resol = event.getObject();
		PrimeFaces.current().ajax().addCallbackParam("id", resol.getId());
		navegacionBean.setTxtMigaPan(Constantes.CONSULTA_RESOLUCION+resol.getCodigoResolucion());
	}
	
}
