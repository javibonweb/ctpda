package es.juntadeandalucia.ctpda.gestionpdt.web.tareas;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.ResponsablesTramitacion;
import es.juntadeandalucia.ctpda.gestionpdt.model.SituacionesExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.TareasExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.Usuario;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.service.AccesosDocumentosService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.SituacionesExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.TareasExpedienteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.TramiteExpedienteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ValoresDominioService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.ValidacionException;
import es.juntadeandalucia.ctpda.gestionpdt.util.StringUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.SesionBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.Constantes;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.JsfUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.LazyDataModelByQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.MyFilterMeta;
import es.juntadeandalucia.ctpda.gestionpdt.web.expedientes.DatosExpedientesTareasBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.menu.NavegacionBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.menu.NavegacionBean.ListadoNavegaciones;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.BaseBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.ContextoVolver;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.VolverBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@ViewScoped
public class TareasEnviadasPendientesBean extends BaseBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String ID_EXP = "idExp";
	private static final String MENSAJESFORMULARIO = "messagesFormulario";
	private static final String CALLBACK_SAVED = "saved";
	public static final String VOLVERLISTADOTAREASENVIADASPDTES = "_volverListadoTareasEnviadas_";
	
	@Getter @Setter
	private Long selectedTipoExpIdFiltro;
	@Getter @Setter
	private String numeroExpedienteFiltro;
	@Getter @Setter
	private String nombreExpedienteFiltro;
	@Getter @Setter
	private Long selectedTipoTareaIdFiltro;
	@Getter
	private List<ValoresDominio> tiposExpendienteFiltro;
	@Getter
	private List<ValoresDominio> tiposTareaFiltro;
	@Getter @Setter
	private boolean permisoFiltroActivas;
	@Getter @Setter
	private boolean permisoFiltroPendientes;
	@Getter @Setter
	private boolean activasFiltro;
	@Getter @Setter
	private boolean pendientesFiltro;
	@Getter @Setter
	private boolean urgentesFiltro;
	@Getter @Setter
	private Boolean avisoFiltro;
	@Getter
	private LazyDataModelByQueryService<TareasExpediente> lazyModelTareas;
	@Getter
	private SortMeta defaultOrdenTabla;
	@Autowired
	private NavegacionBean navegacionBean;
	@Autowired
	private DatosExpedientesTareasBean datosExpedientesTareasBean;
	@Autowired
	private TareasExpedienteService tareasExpedienteService;
	@Setter
	private String esTareaRealizable;
	@Getter
	@Setter
	private List<String> listaCodigoPermisos;
	@Autowired
	private SesionBean sesionBean;
	@Autowired
	private ValoresDominioService valoresDominioService;
	@Autowired
	private VolverBean volverBean;
	@Getter @Setter
	private boolean permisoTramitarExpediente;
	@Autowired
	private TramiteExpedienteService tramiteExpedienteService;
	@Autowired
	private ExpedientesService expedientesService;
	@Autowired
	private SituacionesExpedientesService situacionesExpedientesService;
	@Autowired
	private AccesosDocumentosService accesosDocumentosService;
	@Getter @Setter
	private boolean permisoEditarTarea;
	@Getter @Setter
	private boolean permisoEliminarTarea;
	@Getter @Setter
	private boolean permisoReactivarTarea;
	@Getter @Setter
	private boolean permisoRehabilitarTarea;
	@Getter @Setter
	private TareasExpediente tareaExpediente;
	
	
	@Override
	@PostConstruct
	public void init() {
		super.init();
		
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, 0);
		
		navegacionBean.setTxtMigaPan(Constantes.LISTADO_TAREAS_ENVIADAS_PENDIENTES);
		
		/**permisos*/
		listaCodigoPermisos = (List<String>) JsfUtils.getSessionAttribute(Constantes.LISTACODIGOPERMISOS);
		permisoFiltroActivas = listaCodigoPermisos.contains(Constantes.PERMISO_VER_TAREASNOACTIVAS);
		permisoFiltroPendientes = listaCodigoPermisos.contains(Constantes.PERMISO_VER_TAREASNOPENDIENTES);
		permisoTramitarExpediente = listaCodigoPermisos.contains(Constantes.PERMISO_TRAMITAR_TAREAEXP); 
		permisoEditarTarea = listaCodigoPermisos.contains(Constantes.PERMISO_EDIT_TAREA);
		permisoEliminarTarea = listaCodigoPermisos.contains(Constantes.PERMISO_ELIM_TAREA);
		permisoReactivarTarea = listaCodigoPermisos.contains(Constantes.PERMISO_REACT_TAREA);
		permisoRehabilitarTarea = listaCodigoPermisos.contains(Constantes.PERMISO_REHAB_TAREA);	
		
		inicializaLazyModel();
		
		tiposExpendienteFiltro = valoresDominioService.findValoresTipoExpediente();
		tiposTareaFiltro = valoresDominioService.findValoresTiposTarea();
		
		final ContextoVolver cv = volverBean.recogerContexto(Constantes.VOLVERLISTADOTAREASENVIADAS);		
		if(cv != null) {
			selectedTipoExpIdFiltro = (Long) cv.get("selectedTipoExpIdFiltro");
			selectedTipoTareaIdFiltro = (Long) cv.get("selectedTipoTareaIdFiltro");			
			numeroExpedienteFiltro = (String) cv.get("numeroExpedienteFiltro");
			nombreExpedienteFiltro = (String) cv.get("nombreExpedienteFiltro");			
			activasFiltro = (Boolean) cv.get("activasFiltro");
			pendientesFiltro = (Boolean) cv.get("pendientesFiltro");
			avisoFiltro = (Boolean) cv.get("avisoFiltro");
		} else {
			activasFiltro = Boolean.TRUE;
			pendientesFiltro = Boolean.TRUE;
			avisoFiltro = null;
		}
	}		
	
	private void inicializaLazyModel() {
		lazyModelTareas = new LazyDataModelByQueryService<>(TareasExpediente.class, tareasExpedienteService);
		lazyModelTareas.setPreproceso((a,b,c,filters)->{						
			if(null != selectedTipoExpIdFiltro) {
				filters.put("expediente.valorTipoExpediente.id", new MyFilterMeta(selectedTipoExpIdFiltro));				
			}			
			if(!StringUtils.isBlank(numeroExpedienteFiltro)) {
				filters.put("expediente.numExpediente", new MyFilterMeta(numeroExpedienteFiltro));
			}			
			if(!StringUtils.isBlank(nombreExpedienteFiltro)) {
				filters.put("expediente.nombreExpediente", new MyFilterMeta(nombreExpedienteFiltro));
			}			
			if(null != selectedTipoTareaIdFiltro) {
				filters.put("valorTipoTarea.id", new MyFilterMeta(selectedTipoTareaIdFiltro));				
			}	
			inicializaLazyModelFiltrosBoolean(filters);		
			filters.put("usuarioCreador.id", new MyFilterMeta(sesionBean.getIdUsuarioSesion()));
		});
		
		defaultOrdenTabla = SortMeta.builder().field("fechaLimite").order(SortOrder.ASCENDING).priority(1).build();
	}
	
	private void inicializaLazyModelFiltrosBoolean(Map<String, FilterMeta> filters) {
		if(activasFiltro) {
			filters.put("activa", new MyFilterMeta(activasFiltro));				
		}		
		if(urgentesFiltro) {
			filters.put("urgente", new MyFilterMeta(urgentesFiltro));				
		}		
		if(pendientesFiltro) {
			filters.put("situacion", new MyFilterMeta(TareasExpediente.SITUACION_PENDIENTE));				
		}		
		if(avisoFiltro != null) {
			filters.put("aviso", new MyFilterMeta(avisoFiltro));				
		}		
	}
	
	public String redireccionMenu() {
		return ListadoNavegaciones.LISTADO_TAREAS_ENVIADAS_PENDIENTES.getRegla();
	}

	
	public void accesoRapido(final SelectEvent<TareasExpediente> event) {
		consultarTarea(event.getObject().getId());
	}
	
	public void consultarTarea(Long id){
		datosExpedientesTareasBean.consultarTarea(id);
	}
	
	public void limpiarFiltro() {
		selectedTipoExpIdFiltro = null;
		selectedTipoTareaIdFiltro = null;
		numeroExpedienteFiltro = null;
		nombreExpedienteFiltro = null;		
		activasFiltro = true;
		pendientesFiltro = true;
		urgentesFiltro=false;
		avisoFiltro=null;
	}
	
	public String cssSemaforoDependiente(TareasExpediente tarea) {
		final String css;		
		if(Boolean.TRUE.equals(!tarea.getActiva()) || tarea.getEsTareaCerrada() || Boolean.TRUE.equals(tarea.getAviso())) {
			css = StringUtils.EMPTY;
		} else {
			css = "marca-" + (esTareaDependiente(tarea)? "roja" : "verde");
		}
		return css;
	}
	
	private boolean esTareaDependiente(TareasExpediente tarea) {
		if(null == tarea.getEsTareaDependiente()) {
			tarea.setEsTareaDependiente(tareasExpedienteService.esTareaDependiente(tarea.getId()));
			if(Boolean.TRUE.equals(tarea.getEsTareaDependiente())) {
				tarea.setDependencias(tareasExpedienteService.findDependencias(tarea.getId()));
			}
		}		
		return tarea.getEsTareaDependiente();
	}
	
	public String isEsTareaRealizable(TareasExpediente tarea) {
		if(Boolean.TRUE.equals(!tarea.getActiva()) || tarea.getEsTareaCerrada()) {
			esTareaRealizable = mensajesProperties.getString("no");
		} else {
			esTareaRealizable = mensajesProperties.getString("si");
		}		
		return esTareaRealizable;
	}
	
	public ResponsablesTramitacion getResponsableUltimo(TareasExpediente tarea) {
		ResponsablesTramitacion responsableTramitacion = null;		
		if(Boolean.TRUE.equals(tarea.getEsTareaDependiente())) {
			List<TareasExpediente> listaTareasDependencias = tarea.getDependencias();
			
			if(!listaTareasDependencias.isEmpty()) {
				listaTareasDependencias = listaTareasDependencias.stream().sorted((t1, t2) -> t1.getFechaInicio().compareTo(t2.getFechaInicio())).collect(Collectors.toList());
				responsableTramitacion = listaTareasDependencias.get(0).getResponsableTramitacion();
			}			
		}		
		return responsableTramitacion;
	}
	
	public String onConsultarExpediente(Long idTarea) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, ((Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS))+1);		
		Boolean esFinalizado=false;
		final TareasExpediente tarea = tareasExpedienteService.obtener(idTarea);		
		final Long idExpediente = tarea.getExpediente().getId();
		Expedientes expedienteConsulta = expedientesService.obtener(idExpediente);
		Long idSitExpConsulta = expedienteConsulta.getValorSituacionExpediente().getId();
		ValoresDominio situacionExpConsulta = valoresDominioService.obtener(idSitExpConsulta);
		List<SituacionesExpedientes> situacionesFinalizadas = new ArrayList<>();		
		
		try {
			situacionesFinalizadas = situacionesExpedientesService.findSituacionesExpedientesFinalizados();
		} catch (ValidacionException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString("no.existe.registro.config"));
			log.warn("onConsultarExpediente - " + message.getDetail());
		}
		
		for(SituacionesExpedientes s: situacionesFinalizadas) {
			ValoresDominio situacionExpBusqueda= s.getValorSituacion();
			if(situacionExpBusqueda.equals(situacionExpConsulta)){
				esFinalizado=true;
				break;
			}
		}
		
		if(Boolean.TRUE.equals(esFinalizado)) {
			JsfUtils.setFlashAttribute(ID_EXP, idExpediente);
			JsfUtils.setFlashAttribute("editable", false);
		}else {					
			JsfUtils.setFlashAttribute(ID_EXP, idExpediente);
			JsfUtils.setFlashAttribute("editable", true);
		}
		
		final ContextoVolver contexto = volverBean.crearContexto(ListadoNavegaciones.LISTADO_TAREAS_ENVIADAS_PENDIENTES.getRegla(), Constantes.VOLVERLISTADOTAREASENVIADAS);
		contexto.put(ID_EXP, idExpediente);
		
		final Long[] idsTramite = tramiteExpedienteService.getIdsTipoTramiteSubtramite(tarea.getTramiteExpediente());
		contexto.put("idTram", idsTramite[0]);
		contexto.put("idSubTram", idsTramite[1]);		
		contexto.put("selectedTipoExpIdFiltro", this.selectedTipoExpIdFiltro);
		contexto.put("selectedTipoTareaIdFiltro", this.selectedTipoTareaIdFiltro);	
		contexto.put("numeroExpedienteFiltro", this.numeroExpedienteFiltro);
		contexto.put("nombreExpedienteFiltro", this.nombreExpedienteFiltro);		
		contexto.put("activasFiltro", activasFiltro);
		contexto.put("pendientesFiltro", pendientesFiltro);
		contexto.put("avisoFiltro", avisoFiltro);
		
		JsfUtils.setSessionAttribute(Constantes.VUELTA_LISTADO_TAREAS_ENVIADAS_PDTES, true);
		
		navegacionBean.setTxtMigaPan(Constantes.EDICION_EXPEDIENTE+expedienteConsulta.getMigaDePan()+navegacionBean.expedientesRelacionadosMotivoPsan(expedienteConsulta.getId()));
		
		return NavegacionBean.ListadoNavegaciones.FORM_EXPEDIENTES.getRegla();
	}
	
	public void registrarAccesoDoc(TareasExpediente tarea) {
		try {
			Usuario usuarioLogado = sesionBean.getUsuarioSesion();
			accesosDocumentosService.registrarAccesoMiMesa(usuarioLogado, tarea);
		} catch (BaseException e) {
			facesMsgErrorKey(CLAVE_ERROR_GENERICO);
		}
	}
	
	public void eliminarTarea(Long id){
		try {
			this.tareasExpedienteService.desactivarTarea(id);
			tareaExpediente = tareasExpedienteService.obtener(id);
			
			PrimeFaces.current().ajax().addCallbackParam(CALLBACK_SAVED, true);			
			final String msg = mensajesProperties.getString("tarea") + " "+tareaExpediente.getDescripcion()+" para el expediente "+tareaExpediente.getExpediente().getNumExpediente()+" " +mensajesProperties.getString("eliminada.correctamente");
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "", msg);
			FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, message);
			
		} catch (ValidacionException e) {
			for(String msg : e.getErrors()) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", msg);
				PrimeFaces.current().dialog().showMessageDynamic(message);
			}
		} catch (BaseException e) {
			facesMsgErrorKey(CLAVE_ERROR_GENERICO);
		}
	}
	
	public void reactivarTarea(Long id){
		try {
			this.tareasExpedienteService.reactivarTarea(id);
			tareaExpediente= tareasExpedienteService.obtener(id);
			tareaGuardada();
		} catch (ValidacionException e) {
			for(String msg : e.getErrors()) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", msg);
				PrimeFaces.current().dialog().showMessageDynamic(message);
			}
		} catch (BaseException e) {
			facesMsgErrorKey(CLAVE_ERROR_GENERICO);
		}
	}
	
	private void tareaGuardada() {
		PrimeFaces.current().ajax().addCallbackParam(CALLBACK_SAVED, true);		
		final String msg = mensajesProperties.getString("tarea") + " "+tareaExpediente.getDescripcion()+" para el expediente "+tareaExpediente.getExpediente().getNumExpediente()+" " +mensajesProperties.getString("guardada.correctamente");
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "", msg);
		FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, message);
	}
	
	public void rehabilitarTarea(Long id){
		try {
			this.tareasExpedienteService.rehabilitarTarea(id);
			tareaExpediente= tareasExpedienteService.obtener(id);
			tareaGuardada();
		} catch (ValidacionException e) {
			for(String msg : e.getErrors()) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", msg);
				PrimeFaces.current().dialog().showMessageDynamic(message);
			}
		} catch (BaseException e) {
			facesMsgErrorKey(CLAVE_ERROR_GENERICO);
		}
	}
	
}
