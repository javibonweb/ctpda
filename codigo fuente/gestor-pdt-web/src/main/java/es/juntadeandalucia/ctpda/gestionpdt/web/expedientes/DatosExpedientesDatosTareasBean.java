package es.juntadeandalucia.ctpda.gestionpdt.web.expedientes;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.primefaces.PrimeFaces;
import org.primefaces.model.FilterMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.CfgTareas;
import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentosExpedientesTramites;
import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.ObservacionesExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.ResponsablesTramitacion;
import es.juntadeandalucia.ctpda.gestionpdt.model.TareasExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.TramiteExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.Usuario;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.service.CfgTareasService;
import es.juntadeandalucia.ctpda.gestionpdt.service.DocumentosExpedientesTramitesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ResponsablesTramitacionService;
import es.juntadeandalucia.ctpda.gestionpdt.service.TareasExpedienteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.TramiteExpedienteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.UsuarioService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ValoresDominioService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.ValidacionException;
import es.juntadeandalucia.ctpda.gestionpdt.util.StringUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.SesionBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.Constantes;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.JsfUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.LazyDataModelByQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.MyFilterMeta;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.BaseBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.FacesUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@ViewScoped
public class DatosExpedientesDatosTareasBean extends BaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String MENSAJESFORMULARIO = "messagesFormulario";
	private static final String MENSAJETAREA = "tarea";
	private static final String CALLBACK_SAVED = "saved";
	private static final String GUARDADACORRECTAMENTE = "guardada.correctamente";
	private static final String ELIMINADACORRECTAMENTE = "eliminada.correctamente";
	

	
	@Autowired
	private SesionBean sesionBean;
	@Autowired
	private DatosExpedientesTareasBean datosExpedientesTareasBean;


	@Autowired
	private TareasExpedienteService tareasExpedienteService;
	@Autowired
	private ExpedientesService expedientesService;
	@Autowired
	private CfgTareasService cfgTareasService;
	@Autowired
	private TramiteExpedienteService tramiteExpedienteService;
	@Autowired
	private ResponsablesTramitacionService responsablesTramitacionService;
	
	
	@Getter
	private LazyDataModelByQueryService<TareasExpediente> lazyModelTareaExpedientes;

	@Getter
	@Setter
	private Expedientes expedientes;
	@Getter
	@Setter
	private TareasExpediente tarea;

	@Getter
	@Setter
	private List<TareasExpediente> listaTareasExpedientes;

	@Getter
	@Setter
	boolean soloLectura;

	@Getter
	@Setter
	private String cabeceraDialogoMotivoRelacion;

	@Getter
	private boolean esAlta;
	@Getter
	private boolean mostrarIndicaciones;

	@Getter
	private String tituloDialogo;

	@Getter
	@Setter
	private List<CfgTareas> listaTiposTareas;
	@Getter
	@Setter
	private Long idCfgTareaSeleccionada;
	@Getter
	@Setter
	private CfgTareas cfgTareaSeleccionada;

	@Getter
	@Setter
	private List<ResponsablesTramitacion> listaResponsablesTramitacionTarea;
	@Getter
	@Setter
	private Long idResponsableTramitacionTareaSeleccionado;

	@Getter
	private String numeroExpediente;
	@Getter
	private String nombreExpediente;
	@Getter
	private String descTramite;
	@Getter
	private String nombreDocumento;
	@Getter
	private String nombreUsuarioAlta;
	@Getter
	@Setter
	private String nombreUsuarioCierre;

	private Long idTramiteExpediente;
	private TramiteExpediente tramiteExpediente;
	private Long idDocExpTramite;

	@Getter
	@Setter
	private Boolean permisoCrearTareaDocExp;

	@Getter
	private LazyDataModelByQueryService<TareasExpediente> lazyModelTareasExpedientes;

	@Getter
	@Setter
	private TareasExpediente selectedExpedientesTareas;

	@Getter
	@Setter
	private TareasExpediente tareaExpedienteSeleccionado;

	@Getter
	@Setter
	private Boolean plegado;

	@Getter
	@Setter
	private List<TareasExpediente> listaTareaExpedientes;

	@Autowired
	private DocumentosExpedientesTramitesService documentosExpedientesTramitesService;
	@Autowired
	private UsuarioService usuarioService;
	@Getter
	@Setter
	private Boolean editable;

	@Getter
	@Setter
	private Date fechaLimiteFiltro;

	@Getter
	@Setter
	private Long selectedTipoTareaIdFiltro;

	@Getter
	private List<ValoresDominio> tiposTareaFiltro;

	@Autowired
	private ValoresDominioService valoresDominioService;

	@Getter
	@Setter
	private String descripcionFiltro;
	@Getter
	@Setter
	private Long selectedResponsableIdFiltro;

	@Getter
	private List<ResponsablesTramitacion> responsablesFiltro;

	@Getter @Setter
	private boolean permisoAccederPestaTarea;
	@Getter @Setter
	private boolean permisoFiltroActivas;
	@Getter @Setter
	private boolean permisoFiltroPendientes;
	@Getter @Setter
	private Boolean permisoEditarTarea;
	@Getter @Setter
	private boolean permisoEliminarTarea;
	@Getter @Setter
	private boolean permisoReactivarTarea;
	@Getter @Setter
	private boolean permisoRehabilitarTarea;
	
	@Getter @Setter
	private Boolean activasFiltro;
	@Getter @Setter
	private boolean pendientesFiltro;
	@Getter @Setter
	private boolean urgentesFiltro;

	@Getter
	@Setter
	private List<String> listaCodigoPermisos;

	@Override
	@PostConstruct
	public void init() {
		super.init();			

		tarea = new TareasExpediente();
		ObservacionesExpedientes obsExp = new ObservacionesExpedientes();
		tarea.setObservaciones(obsExp);
		plegado = true;
		tiposTareaFiltro = valoresDominioService.findValoresTiposTarea();
		Sort sortResponsablesFiltro = Sort.by(new Sort.Order(Direction.ASC, "descripcion"));
		responsablesFiltro =(List<ResponsablesTramitacion>) responsablesTramitacionService.findAllRepository(sortResponsablesFiltro);
		pendientesFiltro=Boolean.TRUE;
		activasFiltro=Boolean.TRUE;
		
		listaCodigoPermisos = (List<String>) JsfUtils.getSessionAttribute(Constantes.LISTACODIGOPERMISOS);
		permisoAccederPestaTarea = listaCodigoPermisos.contains(Constantes.PERMISO_ACCEDER_PESTA_TAREA);
		permisoFiltroActivas = listaCodigoPermisos.contains(Constantes.PERMISO_VER_TAREASNOACTIVAS);
		permisoFiltroPendientes = listaCodigoPermisos.contains(Constantes.PERMISO_VER_TAREASNOPENDIENTES);
		permisoEditarTarea = listaCodigoPermisos.contains(Constantes.PERMISO_EDIT_TAREA);
		permisoEliminarTarea = listaCodigoPermisos.contains(Constantes.PERMISO_ELIM_TAREA);
		permisoReactivarTarea = listaCodigoPermisos.contains(Constantes.PERMISO_REACT_TAREA);
		permisoRehabilitarTarea = listaCodigoPermisos.contains(Constantes.PERMISO_REHAB_TAREA);	

		FacesUtils.setAttribute("editable", JsfUtils.getFlashAttribute("editable"));

		Expedientes expedienteFormulario = (Expedientes) JsfUtils.getSessionAttribute("expedienteFormulario");
		if (expedienteFormulario.getId() != null) {
			expedientes = expedientesService.obtener(expedienteFormulario.getId());
		} else {
			expedientes = new Expedientes();
		}

		inicializaLazyModelTareasExpedientes(expedientes);

		cabeceraDialogoMotivoRelacion = "";

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void inicializaLazyModelTareasExpedientes(Expedientes expedienteFormulario) {
		lazyModelTareasExpedientes = new LazyDataModelByQueryService(TareasExpediente.class, tareasExpedienteService);
		lazyModelTareasExpedientes.setPreproceso((a, b, c, filters) -> {
			if (expedienteFormulario.getId() != null) {
				filters.put("expediente.id", new MyFilterMeta(expedienteFormulario.getId()));
				filtrosLazyTareasExpediente(filters);
				
			} else {
				filters.put("expediente.id", new MyFilterMeta(null));

			}
		});
	}

	private void filtrosLazyTareasExpediente(Map<String, FilterMeta> filters) {
 		if (null != this.selectedTipoTareaIdFiltro) {
			filters.put("valorTipoTarea.id", new MyFilterMeta(this.selectedTipoTareaIdFiltro));
		}
		if (null != this.selectedResponsableIdFiltro) {
			filters.put("responsableTramitacion",
					new MyFilterMeta(responsablesTramitacionService.obtener(selectedResponsableIdFiltro)));
		}
		if(Boolean.TRUE.equals(this.activasFiltro)) {
			filters.put("activa", new MyFilterMeta(this.activasFiltro));				
		}
		
		if(this.pendientesFiltro) {
			filters.put("situacion", new MyFilterMeta(TareasExpediente.SITUACION_PENDIENTE));				
		}
		if(this.urgentesFiltro) {
			filters.put("urgente", new MyFilterMeta(this.urgentesFiltro));				
		}
	}
	
	public void limpiarFiltro() {
		selectedTipoTareaIdFiltro = null;
		pendientesFiltro = true;
		selectedResponsableIdFiltro=null;
		activasFiltro=true;
		urgentesFiltro=false;	
	}


	public void abrirTarea(TareasExpediente tareaExpedientesSeleccionado, String accion) {
		try {
			soloLectura = false;
	
			if ("editarTareaExp".equals(accion)) {
				if(expedientes.getId()!=null) {
					cabeceraDialogoMotivoRelacion = "Editar tarea del expediente "+ expedientes.getNumExpediente();
					}else {
					cabeceraDialogoMotivoRelacion = "Editar tarea";
				}
				
				editable = true;
			} else if ("consultarTareaExp".equals(accion)) {
				if(expedientes.getId()!=null) {
					cabeceraDialogoMotivoRelacion = "Consultar tarea del expediente "+ expedientes.getNumExpediente();
				}else {
					cabeceraDialogoMotivoRelacion = "Consultar tarea";
				}
				
				editable = false;
	
			}
			if (tareaExpedientesSeleccionado != null) {
					cargarDatosDialogo(tareaExpedientesSeleccionado);
			}
			PrimeFaces.current().ajax().update("dialogTareaVar");
	
			PrimeFaces.current().executeScript("PF('dialogTareaVar').show();");
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString("no.existe.registro.config"));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error("abrirTarea - " + message.getDetail());
		}
	}

	private void cargarDatosDialogo(TareasExpediente tareaExpedientesSeleccionado) throws BaseException {
		this.tarea = tareaExpedientesSeleccionado;
		this.expedientes = tareaExpedientesSeleccionado.getExpediente();
		this.mostrarIndicaciones = puedeMostrarIndicaciones();
		this.setFormEditable(
				tareaExpedientesSeleccionado.getActiva() && !tareaExpedientesSeleccionado.getEsTareaCerrada());
		reset();
		
		//tramite expediente
		
		this.idTramiteExpediente = getIdTramite(this.tarea.getTramiteExpediente());
		TramiteExpediente nuevoTram = tramiteExpedienteService.obtener(idTramiteExpediente);
		descTramite = nuevoTram.getDescripcion();
		
		//documentacion expediente tramite
		
		this.idDocExpTramite = getIdDocExpTramite(this.tarea.getDocumentoExpedienteTramite());		
		if(this.tarea.getDocumentoExpedienteTramite()!=null) {					
			Long idDocumentoExpedienteTramite = this.tarea.getDocumentoExpedienteTramite().getId();
			DocumentosExpedientesTramites nuevoDoc = documentosExpedientesTramitesService
					.obtener(idDocumentoExpedienteTramite);
			nombreDocumento = nuevoDoc.getDocumentoExpediente().getDescripcionDocumento();
		}
		
		cargarResponsables();
		this.cargarTiposTarea();

		this.cfgTareaSeleccionada = this.listaTiposTareas.get(0);
		this.idCfgTareaSeleccionada = this.cfgTareaSeleccionada.getId();
		refrescarIdResponsable();

		if (this.tarea.getUsuarioCreador() != null) {
			Long idNombreUsuarioAlta = this.tarea.getUsuarioCreador().getId();
			Usuario nuevoUsuarioAlta = usuarioService.obtener(idNombreUsuarioAlta);
			nombreUsuarioAlta = nuevoUsuarioAlta.getNombre();
		}
		
		if (this.tarea.getUsuarioCierre() != null) {
			Long idNombreUsuarioCierre = this.tarea.getUsuarioCierre().getId();
			Usuario nuevoUsuarioCierre = usuarioService.obtener(idNombreUsuarioCierre);
			nombreUsuarioCierre = nuevoUsuarioCierre.getNombre();
		}

		//Cargar dependencias
		if(!esAlta) {
			tarea.setDependencias(tareasExpedienteService.findDependencias(tarea.getId()));
			tarea.setEsTareaDependiente(!tarea.getDependencias().isEmpty());
		}
	}

	private Long getIdTramite(TramiteExpediente tram) {
		return (null == tram) ? null : tram.getId();
	}

	private Long getIdDocExpTramite(DocumentosExpedientesTramites docExpTr) {
		return (null == docExpTr) ? null : docExpTr.getId();
	}

	public void cargarDatosCfgTarea() {
		// Si es != null vendrá del select de tipos de tarea
		// No sirve por que está incompleto (solo tiene los datos para el select)
		// Lo reseteamos

		this.cfgTareaSeleccionada = null;

		if (this.idCfgTareaSeleccionada != null) {
			this.cfgTareaSeleccionada = cfgTareasService.obtener(this.idCfgTareaSeleccionada);
			tareasExpedienteService.aplicarCfgTareas(tarea, this.cfgTareaSeleccionada);
		} else {
			this.idResponsableTramitacionTareaSeleccionado = tarea.getResponsableTramitacion().getId();
			tarea.setFechaLimite(null);
		}

		refrescarIdResponsable();
	}

	private boolean puedeMostrarIndicaciones() {
		try {
			return this.tareasExpedienteService.hayQueCrearTareaSiguiente(tarea, sesionBean.getUsuarioSesion());
		} catch (BaseException e) {
			facesMsgErrorGenerico();
			return false;
		}
	}

	private void reset() {
		this.idTramiteExpediente = null;
		this.tramiteExpediente = null;

		this.idDocExpTramite = null;

		this.descTramite = null;
		this.nombreDocumento = null;
	}

	private void cargarResponsables() {
		if (!Boolean.TRUE.equals(getFormEditable())) { // El alta siempre es editable
			this.listaResponsablesTramitacionTarea = List.of(this.tarea.getResponsableTramitacion());
		} else {
			this.listaResponsablesTramitacionTarea = this.responsablesTramitacionService.findResponsablesActivos();

			if (!esAlta && !Boolean.TRUE.equals(this.tarea.getResponsableTramitacion().getActivo())) {
				// Lo añadimos con vistas a cambiarlo (la validación al guardar obligará a ello)
				this.listaResponsablesTramitacionTarea.add(this.tarea.getResponsableTramitacion());
			}
		}
	}

	private void cargarTiposTarea() throws BaseException {
		if (esAlta) {
			final Long[] idsTipoTramite = tramiteExpedienteService.getIdsTipoTramiteSubtramite(this.tramiteExpediente);
			this.listaTiposTareas = this.cfgTareasService.findTiposTareasManualesActivasCfg(expedientes.getValorTipoExpediente().getId(), idsTipoTramite[0], idsTipoTramite[1],(null != this.idDocExpTramite));
		} else {
			final CfgTareas cfgTipoTareaEdit = new CfgTareas();
			cfgTipoTareaEdit.setId(1L);
			cfgTipoTareaEdit.setValorTipoTarea(tarea.getValorTipoTarea());
			cfgTipoTareaEdit.setDescripcion(tarea.getValorTipoTarea().getDescripcion());
			this.listaTiposTareas = List.of(cfgTipoTareaEdit);
		}

	}

	private void refrescarIdResponsable() {
		final ResponsablesTramitacion resp = tarea.getResponsableTramitacion();
		this.idResponsableTramitacionTareaSeleccionado = (null == resp) ? null : resp.getId();
	}

	// Se invoca también desde el datePicker de fechaInicio
	// Si no fuera así no haría falta mantener cfgTareaSeleccionada
	public void refrescarFechaLimite() {
		tareasExpedienteService.aplicarFechaLimite(tarea, this.cfgTareaSeleccionada);
	}
	
	public void editarTarea(Long id){
		this.datosExpedientesTareasBean.editarTarea(id);
	}
	
	public void consultarTarea(Long id){
		this.datosExpedientesTareasBean.consultarTarea(id);
	}


	public void eliminarTarea(Long id) {
		try {
			this.tareasExpedienteService.desactivarTarea(id);
			tareaEliminada(id);
		} catch (ValidacionException e) {
			for (String msg : e.getErrors()) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", msg);
				PrimeFaces.current().dialog().showMessageDynamic(message);
			}
		} catch (BaseException e) {
			facesMsgErrorKey(CLAVE_ERROR_GENERICO);
			e.printStackTrace();
		}
	}

	public void reactivarTarea(Long id) {
		try {
			this.tareasExpedienteService.reactivarTarea(id);
			tareaGuardada(id);
		} catch (ValidacionException e) {
			for (String msg : e.getErrors()) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", msg);
				PrimeFaces.current().dialog().showMessageDynamic(message);
			}
		} catch (BaseException e) {
			facesMsgErrorKey(CLAVE_ERROR_GENERICO);
			e.printStackTrace();
		}
	}

	public void rehabilitarTarea(Long id) {
		try {
			this.tareasExpedienteService.rehabilitarTarea(id);
			tareaGuardada(id);
		} catch (ValidacionException e) {
			for (String msg : e.getErrors()) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", msg);
				PrimeFaces.current().dialog().showMessageDynamic(message);
			}
		} catch (BaseException e) {
			facesMsgErrorKey(CLAVE_ERROR_GENERICO);
			e.printStackTrace();
		}
	}

	private void tareaGuardada(Long id) {
		PrimeFaces.current().ajax().addCallbackParam(CALLBACK_SAVED, true);

		TareasExpediente tareaBD = tareasExpedienteService.obtener(id);
		if(tareaBD.getValorTipoTarea().getCodigo().equals(Constantes.COD_TIP_TAR_TRAM_FYN)) {
			TramiteExpediente tram = tareaBD.getTramiteExpediente();
			PrimeFaces.current().ajax().update(Constantes.JSFID_PESTANAS_EXP + ":botonesFYN_" + tram.getId());
		}

		final String msg = mensajesProperties.getString(MENSAJETAREA) + " "
				+ mensajesProperties.getString(GUARDADACORRECTAMENTE);
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "", msg);
		FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, message);
	}
	
	private void tareaEliminada(Long id) {
		PrimeFaces.current().ajax().addCallbackParam(CALLBACK_SAVED, true);

		TareasExpediente tareaBD = tareasExpedienteService.obtener(id);
		if(tareaBD.getValorTipoTarea().getCodigo().equals(Constantes.COD_TIP_TAR_TRAM_FYN)) {
			TramiteExpediente tram = tareaBD.getTramiteExpediente();
			PrimeFaces.current().ajax().update(Constantes.JSFID_PESTANAS_EXP + ":botonesFYN_" + tram.getId());
		}
		
		final String msg = mensajesProperties.getString(MENSAJETAREA) + " "
				+ mensajesProperties.getString(ELIMINADACORRECTAMENTE);
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "", msg);
		FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, message);
	}

	private boolean validarTarea() {
		if (null == tarea.getValorTipoTarea() || null == this.idResponsableTramitacionTareaSeleccionado) {
			facesMsgErrorKey(CLAVE_CAMPOS_OBLIGATORIOS);
			return false;
		}

		final ResponsablesTramitacion rt = responsablesTramitacionService
				.obtener(this.idResponsableTramitacionTareaSeleccionado);

		if (!Boolean.TRUE.equals(rt.getActivo())) {
			facesMsgErrorKey("responsable.tramitacion.inactivo");
			return false;
		}

		if (StringUtils.isBlank(tarea.getDescripcion())) {
			facesMsgErrorKey(CLAVE_CAMPOS_OBLIGATORIOS);
			return false;
		}

		final Date fechaLimite = tarea.getFechaInicio();

		if (null == fechaLimite) {
			facesMsgErrorKey(CLAVE_CAMPOS_OBLIGATORIOS);
			return false;
		}

		return true;
	}

	public void guardarTarea() {
		try {
			tareasExpedienteService.aplicarDatosTramite(tarea, this.idTramiteExpediente, this.idDocExpTramite);

			if (validarTarea()) {
				final ResponsablesTramitacion resp = responsablesTramitacionService.obtener(this.idResponsableTramitacionTareaSeleccionado);
				tarea.setResponsableTramitacion(resp);

				tareasExpedienteService.guardar(tarea);
				PrimeFaces.current().ajax().addCallbackParam(CALLBACK_SAVED, true);

				PrimeFaces.current().ajax()
						.update("formFormularioExpedientes:tabViewPestanasExpediente:tablaTareasExpedientes");

				final String msg = mensajesProperties.getString(MENSAJETAREA) + " "
						+ mensajesProperties.getString(GUARDADACORRECTAMENTE);
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "", msg);
				FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, message);
			}
		} catch (ValidacionException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", e.getMessage());
			PrimeFaces.current().dialog().showMessageDynamic(message);
		} catch (BaseException e) {
			facesMsgErrorKey(CLAVE_ERROR_GENERICO);
			e.printStackTrace();
		}

	}

}
