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
import es.juntadeandalucia.ctpda.gestionpdt.model.UsuariosResponsables;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.service.AccesosDocumentosService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ResponsablesTramitacionService;
import es.juntadeandalucia.ctpda.gestionpdt.service.SituacionesExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.TareasExpedienteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.TramiteExpedienteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.UsuariosResponsablesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ValoresDominioService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.ValidacionException;
import es.juntadeandalucia.ctpda.gestionpdt.util.ListUtils;
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
public class TareasBean extends BaseBean {

	private static final long serialVersionUID = 1L;

	private static final String MENSAJESFORMULARIO = "messagesFormulario";

	private static final String CALLBACK_SAVED = "saved";
	private static final String ID_EXP= "idExp";
	
	@Autowired
	private SesionBean sesionBean;
	@Autowired
	private DatosExpedientesTareasBean datosExpedientesTareasBean;
	@Autowired
	private VolverBean volverBean;

	@Autowired
	private TareasExpedienteService tareasExpedienteService;
	@Autowired
	private ValoresDominioService valoresDominioService;
	@Autowired
	private UsuariosResponsablesService usuariosResponsablesService;
	@Autowired
	private ResponsablesTramitacionService responsablesTramitacionService;
	@Autowired
	private TramiteExpedienteService tramiteExpedienteService;
	@Autowired
	private ExpedientesService expedientesService;
	@Autowired
	private SituacionesExpedientesService situacionesExpedientesService;
	@Autowired
	private AccesosDocumentosService accesosDocumentosService;

	
	@Getter
	private String tituloMiMesa;

	@Getter @Setter
	private Long selectedTipoExpIdFiltro;
	@Getter @Setter
	private String numeroExpedienteFiltro;
	@Getter @Setter
	private String nombreExpedienteFiltro;
	@Getter @Setter
	private Long selectedTipoTareaIdFiltro;
	@Getter @Setter
	private Long selectedResponsableIdFiltro;
	@Getter @Setter
	private boolean activasFiltro;
	@Getter @Setter
	private boolean pendientesFiltro;
	@Getter @Setter
	private boolean equipoFiltro;
	@Getter @Setter
	private boolean urgentesFiltro;
	@Getter @Setter
	private Boolean avisoFiltro;
	
	@Getter @Setter
	private boolean permisoVerTareasOtros;
	@Getter @Setter
	private boolean permisoEquipo;
	@Getter @Setter
	private boolean permisoFiltroActivas;
	@Getter @Setter
	private boolean permisoFiltroPendientes;
	
	@Getter @Setter
	private boolean permisoTramitarExpediente;
	@Getter @Setter
	private boolean permisoEditarDocTarea;
	@Getter @Setter
	private boolean permisoEditarTarea;
	@Getter @Setter
	private boolean permisoEliminarTarea;
	@Getter @Setter
	private boolean permisoReactivarTarea;
	@Getter @Setter
	private boolean permisoRehabilitarTarea;
	
	
	
	@Getter
	private List<ValoresDominio> tiposExpFiltro;
	@Getter
	private List<ValoresDominio> tiposTareaFiltro;
	@Getter
	private List<UsuariosResponsables> responsablesFiltro;
	
	@Getter
	private LazyDataModelByQueryService<TareasExpediente> lazyModel;
	
	@Getter
	private LazyDataModelByQueryService<TareasExpediente> lazyModelTareasExpedientes;
	@Getter
	private SortMeta defaultOrden;
	@Getter @Setter
	private TareasExpediente tareaExpediente;
	
	@Autowired
	private NavegacionBean navegacionBean;
	
	@Getter
	@Setter
	private List<String> listaCodigoPermisos;
	
	@Setter
	private String esTareaRealizable;

	
	public String redireccionMenu() {
		return ListadoNavegaciones.LISTADO_TAREAS_MI_MESA.getRegla();
	}
	
	@Override
	@PostConstruct
	public void init() {
		super.init();
		
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, 0);
		
		cargarPermisosUsuario();
		cargaComboResponsables();
		inicializaLazyModel();
		
		tiposExpFiltro = valoresDominioService.findValoresTipoExpediente();
		tiposTareaFiltro = valoresDominioService.findValoresTiposTarea();

		cargarFiltros();
		actualizaTituloMiMesa();
		
		navegacionBean.setTxtMigaPan(Constantes.LISTADO_MIMESA);
	}
	
	private void cargarFiltros() {
		final ContextoVolver cv = volverBean.recogerContexto(Constantes.VOLVERMESATAREAS);
		
		if(cv != null) {
			this.selectedTipoExpIdFiltro = (Long) cv.get("selectedTipoExpIdFiltro");
			this.selectedTipoTareaIdFiltro = (Long) cv.get("selectedTipoTareaIdFiltro");
			this.selectedResponsableIdFiltro = (Long) cv.get("selectedResponsableIdFiltro");			
			
			this.numeroExpedienteFiltro = (String) cv.get("numeroExpedienteFiltro");
			this.nombreExpedienteFiltro = (String) cv.get("nombreExpedienteFiltro");
			
			this.activasFiltro = (Boolean) cv.get("activasFiltro");
			this.pendientesFiltro = (Boolean) cv.get("pendientesFiltro");
			this.equipoFiltro = (Boolean) cv.get("equipoFiltro");
			this.avisoFiltro = (Boolean) cv.get("avisoFiltro");
		} else {
			this.activasFiltro = Boolean.TRUE;
			this.pendientesFiltro = Boolean.TRUE;
			this.avisoFiltro = null;
		}
	}
	
	private void cargarPermisosUsuario() {
		listaCodigoPermisos = (List<String>) JsfUtils.getSessionAttribute(Constantes.LISTACODIGOPERMISOS);
		permisoVerTareasOtros = listaCodigoPermisos.contains(Constantes.PERMISO_VER_TAREASOTROS);
		permisoEquipo = listaCodigoPermisos.contains(Constantes.PERMISO_VER_TAREASEQUIPO);
		permisoFiltroActivas = listaCodigoPermisos.contains(Constantes.PERMISO_VER_TAREASNOACTIVAS);
		permisoFiltroPendientes = listaCodigoPermisos.contains(Constantes.PERMISO_VER_TAREASNOPENDIENTES);
		
		permisoTramitarExpediente = listaCodigoPermisos.contains(Constantes.PERMISO_TRAMITAR_TAREAEXP);
		permisoEditarDocTarea = listaCodigoPermisos.contains(Constantes.PERMISO_EDIT_TAREADOC); 
		permisoEditarTarea = listaCodigoPermisos.contains(Constantes.PERMISO_EDIT_TAREA);
		permisoEliminarTarea = listaCodigoPermisos.contains(Constantes.PERMISO_ELIM_TAREA);
		permisoReactivarTarea = listaCodigoPermisos.contains(Constantes.PERMISO_REACT_TAREA);
		permisoRehabilitarTarea = listaCodigoPermisos.contains(Constantes.PERMISO_REHAB_TAREA);	
	}
	
	private void inicializaLazyModel() {
		lazyModel= new LazyDataModelByQueryService<>(TareasExpediente.class, tareasExpedienteService);
		lazyModel.setPreproceso((a,b,c,filters)->{
						
			if(null != this.selectedTipoExpIdFiltro) {
				filters.put("expediente.valorTipoExpediente.id", new MyFilterMeta(this.selectedTipoExpIdFiltro));				
			}
			
			if(!StringUtils.isBlank(this.numeroExpedienteFiltro)) {
				filters.put("expediente.numExpediente", new MyFilterMeta(this.numeroExpedienteFiltro));
			}
			
			if(!StringUtils.isBlank(this.nombreExpedienteFiltro)) {
				filters.put("expediente.nombreExpediente", new MyFilterMeta(this.nombreExpedienteFiltro));
			}
			
			if(null != this.selectedTipoTareaIdFiltro) {
				filters.put("valorTipoTarea.id", new MyFilterMeta(this.selectedTipoTareaIdFiltro));				
			}
					
			inicializaLazyModelResponsableFiltro(filters);
			
			inicializaFiltrosBoolean(filters);
					
		});
		
		defaultOrden = SortMeta.builder().field("fechaLimite").order(SortOrder.ASCENDING).priority(1).build();
	}
	
	private void inicializaFiltrosBoolean(Map<String, FilterMeta> filters) {
		if(this.activasFiltro) {
			filters.put("activa", new MyFilterMeta(this.activasFiltro));				
		}
		
		if(this.urgentesFiltro) {
			filters.put("urgente", new MyFilterMeta(this.urgentesFiltro));				
		}
		
		if(this.pendientesFiltro) {
			filters.put("situacion", new MyFilterMeta(TareasExpediente.SITUACION_PENDIENTE));				
		}
		
		if(this.avisoFiltro != null) {
			filters.put("aviso", new MyFilterMeta(this.avisoFiltro));				
		}		
	}
	
	private void inicializaLazyModelResponsableFiltro (Map<String, FilterMeta> filters) {
		if(null != this.selectedResponsableIdFiltro) {
			final List<Long> idsRespFiltro = List.of(this.selectedResponsableIdFiltro);
			filtrarPorResponsable(idsRespFiltro, filters);
		} else { 
			//Caso deshabilitado, no debería ocurrir
			final List<Long> idsRespFiltro = ListUtils.collect(this.responsablesFiltro, ur -> ur.getResponsable().getId());
			filtrarTodosResponsables(idsRespFiltro, filters);			
		}
	}

	private void filtrarPorResponsable(List<Long> idsRespFiltro, Map<String, FilterMeta> filters) {
		if(this.equipoFiltro) {
			//Buscamos por el equipo completo, incluido el responsable
			final List<Long> idsEquipo = responsablesTramitacionService.findIdsEquipos(idsRespFiltro);
			idsEquipo.addAll(idsRespFiltro); //Lo añadimos
			
			filters.put(TareasExpedienteService.FILTRO_LISTA_IDS_RESP, new MyFilterMeta(idsEquipo));				
		} else {
			//Buscamos solo por el responsable
			filters.put("responsableTramitacion.id", new MyFilterMeta(this.selectedResponsableIdFiltro));		
		}		
	}
	
	private void filtrarTodosResponsables(List<Long> idsRespFiltro, Map<String, FilterMeta> filters) {
		if(this.equipoFiltro) {
			//Buscamos por los equipos de todos los responsables de la lista
			final List<Long> idsEquipo = responsablesTramitacionService.findIdsEquipos(idsRespFiltro);
			filters.put(TareasExpedienteService.FILTRO_LISTA_IDS_RESP, new MyFilterMeta(idsEquipo));				
		} else {
			//Buscamos solo por los responsables
			filters.put(TareasExpedienteService.FILTRO_LISTA_IDS_RESP, new MyFilterMeta(idsRespFiltro));		
		}
	}
	
	//No se usa fuera de aquí, pero no está de más mantenerlo public por si se acaba usando en más sitios
	public boolean esTareaDependiente(TareasExpediente tarea) {
		if(null == tarea.getEsTareaDependiente()) {
			tarea.setEsTareaDependiente(tareasExpedienteService.esTareaDependiente(tarea.getId()));
			
			//Al final necesitamos cargar la lista para mostrar más tarde el último responsable.
			if(Boolean.TRUE.equals(tarea.getEsTareaDependiente())) {
				tarea.setDependencias(tareasExpedienteService.findDependencias(tarea.getId()));
			}
		}
		
		return tarea.getEsTareaDependiente();
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
	
	//----------------------------
	
	private void cargaComboResponsables() {
		if(this.permisoVerTareasOtros) {
			responsablesFiltro = usuariosResponsablesService.findResponsablesYEquiposUsuario(sesionBean.getIdUsuarioSesion());
		} else {
			responsablesFiltro = usuariosResponsablesService.findByUsuarioId(sesionBean.getIdUsuarioSesion());
		}
		
		cargaFiltroResponsableId();
	}
	
	private void cargaFiltroResponsableId() {
		this.selectedResponsableIdFiltro =  (responsablesFiltro.size()==1)?
				 getFiltroIdPrimerResponsable() 
				 : getFiltroIdUsuarioResponsablePorDefecto();
	}
	
	private Long getFiltroIdUsuarioResponsablePorDefecto() {
		return responsablesFiltro.stream()
							.filter(UsuariosResponsables::getPorDefecto).findFirst()
							.map(ur->ur.getResponsable().getId()).orElse(null);
	}
	
	private Long getFiltroIdPrimerResponsable() {
		return responsablesFiltro.isEmpty()? null 
				: responsablesFiltro.get(0).getResponsable().getId();
	}
	
	public void limpiarFiltro() {
		this.selectedTipoExpIdFiltro = null;
		this.selectedTipoTareaIdFiltro = null;
		cargaFiltroResponsableId();
		
		this.numeroExpedienteFiltro = null;
		this.nombreExpedienteFiltro = null;
		
		this.equipoFiltro = false;
		this.activasFiltro = true;
		this.pendientesFiltro = true;
		this.urgentesFiltro=false;
		this.avisoFiltro=null;
	}
	
	public ResponsablesTramitacion getResponsableUltimo(TareasExpediente tarea) {
		ResponsablesTramitacion resp = null;
		
		if(Boolean.TRUE.equals(tarea.getEsTareaDependiente())) {
			List<TareasExpediente> listaDep = tarea.getDependencias();
			
			if(!listaDep.isEmpty()) {
				listaDep = listaDep.stream().sorted( 
						(t1, t2) -> t1.getFechaInicio().compareTo(t2.getFechaInicio()) 
						).collect(Collectors.toList());
				resp = listaDep.get(0).getResponsableTramitacion();
			}
			
		}
		
		return resp;
	}
	
	//*************************************************************
	
	public void onBuscarTareas() {
		actualizaTituloMiMesa();
	}
	
	private void actualizaTituloMiMesa() {
		//this.selectedResponsableIdFiltro debe ser siempre !=null
		boolean esRespDelUsuario = (null == this.selectedResponsableIdFiltro)
							|| usuariosResponsablesService.esResponsableDeUsuario(this.selectedResponsableIdFiltro, sesionBean.getIdUsuarioSesion());
		
		String tituloBase = esRespDelUsuario? getMessage("mi.mesa.tareas")
										: (getMessage("mesa.tareas.de") + StringUtils.SPACE 
											+ getResponsableFiltroSeleccionado().getDescripcion());
		
		String txtEquipo = equipoFiltro? StringUtils.SPACE + getMessage("incluyendo.equipo")
									: StringUtils.EMPTY ;
		
		tituloMiMesa = tituloBase + txtEquipo;
	}
	
	private ResponsablesTramitacion getResponsableFiltroSeleccionado() {
		return responsablesTramitacionService.obtener(this.selectedResponsableIdFiltro);
	}
	
	public String onConsultarExpediente(Long idTarea) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, ((Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS))+1);
		
		Boolean esFinalizado=false;
		final TareasExpediente tarea = tareasExpedienteService.obtener(idTarea);
		
		final Long idExpediente = tarea.getExpediente().getId();
		Expedientes expedienteConsulta = expedientesService.obtener(idExpediente);
		Long idSitExpConsulta= expedienteConsulta.getValorSituacionExpediente().getId();
		ValoresDominio situacionExpConsulta= valoresDominioService.obtener(idSitExpConsulta);
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
		
		final ContextoVolver contexto = volverBean.crearContexto(ListadoNavegaciones.LISTADO_TAREAS_MI_MESA.getRegla(), Constantes.VOLVERMESATAREAS);
		contexto.put(ID_EXP, idExpediente);
		
		final Long[] idsTramite = tramiteExpedienteService.getIdsTipoTramiteSubtramite(tarea.getTramiteExpediente());
		contexto.put("idTram", idsTramite[0]);
		contexto.put("idSubTram", idsTramite[1]);
		
		contexto.put("selectedTipoExpIdFiltro", this.selectedTipoExpIdFiltro);
		contexto.put("selectedTipoTareaIdFiltro", this.selectedTipoTareaIdFiltro);
		contexto.put("selectedResponsableIdFiltro", this.selectedResponsableIdFiltro);
		
		contexto.put("numeroExpedienteFiltro", this.numeroExpedienteFiltro);
		contexto.put("nombreExpedienteFiltro", this.nombreExpedienteFiltro);
		
		contexto.put("activasFiltro", activasFiltro);
		contexto.put("pendientesFiltro", pendientesFiltro);
		contexto.put("equipoFiltro", equipoFiltro);
		contexto.put("avisoFiltro", avisoFiltro);
		
		navegacionBean.setTxtMigaPan(Constantes.EDICION_EXPEDIENTE+expedienteConsulta.getMigaDePan()+navegacionBean.expedientesRelacionadosMotivoPsan(expedienteConsulta.getId()));
		
		return NavegacionBean.ListadoNavegaciones.FORM_EXPEDIENTES.getRegla();
	}
	
	public void editarTarea(Long id){
		this.datosExpedientesTareasBean.editarTarea(id);
	}
	
	public void consultarTarea(Long id){
		this.datosExpedientesTareasBean.consultarTarea(id);
	}
	
	public void accesoRapido(final SelectEvent<TareasExpediente> event) {
		this.consultarTarea(event.getObject().getId());
	}

	public void eliminarTarea(Long id){
		try {
			this.tareasExpedienteService.desactivarTarea(id);
			tareaExpediente= tareasExpedienteService.obtener(id);
			tareaEliminada();
		} catch (ValidacionException e) {
			for(String msg : e.getErrors()) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", msg);
				PrimeFaces.current().dialog().showMessageDynamic(message);
			}
		} catch (BaseException e) {
			facesMsgErrorKey(CLAVE_ERROR_GENERICO);
			e.printStackTrace();
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
			e.printStackTrace();
		}
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
			e.printStackTrace();
		}
	}
	
	private void tareaGuardada() {
		PrimeFaces.current().ajax().addCallbackParam(CALLBACK_SAVED, true);
		
		final String msg = mensajesProperties.getString("tarea") + " "+tareaExpediente.getDescripcion()+" para el expediente "+tareaExpediente.getExpediente().getNumExpediente()+" " +mensajesProperties.getString("guardada.correctamente");
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "", msg);
		FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, message);
	}
	
	private void tareaEliminada() {
		PrimeFaces.current().ajax().addCallbackParam(CALLBACK_SAVED, true);
		
		final String msg = mensajesProperties.getString("tarea") + " "+tareaExpediente.getDescripcion()+" para el expediente "+tareaExpediente.getExpediente().getNumExpediente()+" " +mensajesProperties.getString("eliminada.correctamente");
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "", msg);
		FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, message);
	}
	
	public void registrarAccesoDoc(TareasExpediente tarea) {
		try {
			Usuario usu = sesionBean.getUsuarioSesion();
			accesosDocumentosService.registrarAccesoMiMesa(usu, tarea);
		} catch (BaseException e) {
			facesMsgError("Se produjo un error en el registro del acceso al documento. Consulte con su administrador.");
		}

	}

	public String isEsTareaRealizable(TareasExpediente tarea) {
		if(Boolean.TRUE.equals(!tarea.getActiva()) || tarea.getEsTareaCerrada()) {
			esTareaRealizable = mensajesProperties.getString("no");
		} else {
			esTareaRealizable = mensajesProperties.getString("si");
		}
		
		return esTareaRealizable;
	}
	
}
