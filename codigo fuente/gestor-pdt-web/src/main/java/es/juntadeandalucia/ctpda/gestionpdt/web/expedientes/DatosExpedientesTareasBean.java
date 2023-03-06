package es.juntadeandalucia.ctpda.gestionpdt.web.expedientes;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.apache.commons.lang3.ArrayUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.CfgTareas;
import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentosExpedientesTramites;
import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentosTramiteMaestra;
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
import es.juntadeandalucia.ctpda.gestionpdt.service.ObservacionesExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ResponsablesTramitacionService;
import es.juntadeandalucia.ctpda.gestionpdt.service.TareasExpedienteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.TramiteExpedienteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.UsuariosResponsablesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ValoresDominioService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.ValidacionException;
import es.juntadeandalucia.ctpda.gestionpdt.util.FechaUtils;
import es.juntadeandalucia.ctpda.gestionpdt.util.ListUtils;
import es.juntadeandalucia.ctpda.gestionpdt.util.StringUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.SesionBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.Constantes;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.JsfUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.LazyDataModelByQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.BaseBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@ViewScoped
@Slf4j
public class DatosExpedientesTareasBean extends BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static final String MENSAJESFORMULARIO = "messagesFormulario";
	private static final String DIALOGTAREASHOW = "PF('dialogTarea').show();";
	
	@Autowired
	private SesionBean sesionBean;	
	
	
	
	@Autowired
	private TareasExpedienteService tareasExpedienteService;
	@Autowired
	private CfgTareasService cfgTareasService;
	@Autowired
	private ExpedientesService expedientesService;
	@Autowired
	private ValoresDominioService valoresDominioService;
	@Autowired
	private TramiteExpedienteService tramiteExpedienteService;
	@Autowired
	private DocumentosExpedientesTramitesService documentosExpedientesTramitesService;
	@Autowired
	private ResponsablesTramitacionService responsablesTramitacionService;
	@Autowired
	private UsuariosResponsablesService usuariosResponsablesService;
	@Autowired
	private ObservacionesExpedientesService observacionesExpedientesService;
	
	
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
	
	@Getter @Setter
	private List<ResponsablesTramitacion> listaResponsablesTramitacionTarea;
	@Getter @Setter
	private Long idResponsableTramitacionTareaSeleccionado;
	
	@Getter @Setter
	private boolean usuarioEsResponsable;
	@Getter @Setter
	private boolean usuarioEsResponsableSuperiorCreador;
	
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
	@Getter @Setter
	private String nombreUsuarioCierre;
	
	@Getter	@Setter
	private Expedientes expediente;

	@Getter	@Setter
	private TareasExpediente tarea;
	
	@Getter	@Setter
	private ObservacionesExpedientes observacionesExpedientes;
	
	@Getter
	private Long idTramiteExpediente;
	private TramiteExpediente tramiteExpediente;
	private Long idDocExpTramite;
	

	@Getter	@Setter
	private Boolean permisoCrearTareaDocExp;
	@Getter	@Setter
	private Boolean permisoEditarTarea;
	@Getter @Setter
	private boolean permisoRechazarTarea;
	
	@Getter
	private Long idMotivoRechazoTareaSeleccionado;
	
	
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
	private String cabeceraDialogoMotivoRelacion;
	
	@Getter
	@Setter
	boolean soloLectura;
	
	@Getter
	@Setter
	private List<String> listaCodigoPermisos;
	
	@Getter @Setter
	private String motivoRechazo;
	
	@Getter	@Setter
	private List<TareasExpediente> listaTareasCierreAuto;
	@Getter	@Setter
	private List<TareasExpediente> selectedTareasCierreAuto;
	
	
	
	
	@Override
	@PostConstruct
	public void init() {
		super.init();
		
		tarea = new TareasExpediente();
		observacionesExpedientes = new ObservacionesExpedientes();
		tarea.setObservaciones(observacionesExpedientes);		
		plegado = true;
		selectedTareasCierreAuto = new ArrayList<>();
				
		listaCodigoPermisos = (List<String>) JsfUtils.getSessionAttribute(Constantes.LISTACODIGOPERMISOS);
		permisoCrearTareaDocExp = listaCodigoPermisos.contains(Constantes.PERMISO_CREAR_TAREADOCEXP);
		permisoEditarTarea = listaCodigoPermisos.contains(Constantes.PERMISO_EDIT_TAREA);
		permisoRechazarTarea = listaCodigoPermisos.contains(Constantes.PERMISO_RECHAZA_TAREA);	
	}
	
	
	public Expedientes getExpedienteFormulario() {
		return (Expedientes)JsfUtils.getSessionAttribute("expedienteFormulario");
	}
	
	//---------------
	
	public void altaTareaExpediente() {
		reset();		
		altaTarea();
	}
	
	//Método básico de alta
	private void altaTarea() {
		iniciarAlta();
		
		try {
			cargarDatosDialogo();
			PrimeFaces.current().executeScript(DIALOGTAREASHOW);
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", e.getMessage());
			PrimeFaces.current().dialog().showMessageDynamic(message);
		}			
	}
	
	private void iniciarAlta() {
		this.expediente = getExpedienteFormulario();
		this.tarea = nuevaTarea();
		this.esAlta = true;
		this.setFormEditable(true);
	}
	
	private TareasExpediente nuevaTarea() {
		final TareasExpediente t = new TareasExpediente(); 
		
		t.setUsuarioCreador(sesionBean.getUsuarioSesion());
		t.setExpediente(this.getExpedienteFormulario());
		
		ObservacionesExpedientes obsExp = new ObservacionesExpedientes();
		t.setObservaciones(obsExp);
		
		if(null != this.idTramiteExpediente) {
			this.tramiteExpediente = this.tramiteExpedienteService.obtener(this.idTramiteExpediente);
			t.setTramiteExpediente(tramiteExpediente);
		}		
		
		t.setFechaInicio(FechaUtils.hoy());
		t.setTipoAlta(TareasExpediente.TIPO_ALTA_MANUAL);
		
		return t;
	}
	
	//---------------------------------
	//Alta de tareas para trámites
	public void altaTareaTramite(TramiteExpediente tramExp) {
		reset();		
		altaTareaTramite(tramExp.getId());
	}
	
	private void altaTareaTramite(Long idTram) {
		cargarDatosTramite(idTram);
		altaTarea();		
	}
	
	public void altaTareaTramiteRevisarDocs(TramiteExpediente tramExp) {
		reset();
		altaTareaTramite(tramExp.getId());		
		//REVT debe existir
		cargarDatosSegunTipoTarea("REVT");
	}
	
	public void altaTareaTramiteFirmarNotificar(TramiteExpediente tramExp) {
		reset();
		altaTareaTramite(tramExp.getId());
		//FYN debe existir
		cargarDatosSegunTipoTarea(Constantes.COD_TIP_TAR_TRAM_FYN);
	}
	
	public void verTareaTramiteFirmarNotificar(TramiteExpediente tramExp) {
		tarea = this.tareasExpedienteService.getTareaFYNPendienteTramite(tramExp.getId());
		this.consultarTarea();
	}
	
	private void cargarDatosSegunTipoTarea(String codigo) {
		if(null == listaTiposTareas) {
			return;
		}
		
		Optional<CfgTareas> opCfg = listaTiposTareas.stream()
										.filter(cfg -> cfg.getValorTipoTarea().getCodigo().equals(codigo))
										.findFirst();
		this.idCfgTareaSeleccionada = opCfg.isPresent()? opCfg.get().getId() : null;
		
		cargarDatosCfgTarea();		
	}
		
	public void altaTareaDocumento(DocumentosTramiteMaestra doc) {	
		//No debemos basarnos en la información del objeto
		//Debemos consultar siempre la BD para conocer el estado actual
		if(tareasExpedienteService.existeTareaREVPendienteDocumento(doc.getIdDocumento())){
			facesMsgErrorKey("existe.tarea.documento");
			return;
		}
		
		reset();
		cargarDatosDocExpTramite(doc.getIdDocExpTramite());
		
		altaTareaTramite(doc.getIdTramiteExpediente());		
		//REV debe existir
		cargarDatosSegunTipoTarea("REV");
	}
	
	//--------------------------------
	
	public void editarTarea(Long idTarea) {
		this.tarea = tareasExpedienteService.obtener(idTarea);
		this.expediente = tarea.getExpediente();
		this.esAlta = false;
		this.mostrarIndicaciones=puedeMostrarIndicaciones();
		this.setFormEditable(tarea.getActiva() && !tarea.getEsTareaCerrada());
		reset();
		
		cargarDatosTramite(this.tarea.getTramiteExpediente());
		cargarDatosDocExpTramite(this.tarea.getDocumentoExpedienteTramite());
		
		try {
			cargarDatosDialogo();
			PrimeFaces.current().executeScript(DIALOGTAREASHOW);
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", e.getMessage());
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error("editarTarea - " + message.getDetail());
		}
	}
	
	public void consultarTarea(Long idTarea) {
		this.tarea = tareasExpedienteService.obtener(idTarea);
		consultarTarea();
	}
	
	public void consultarTareaDocumento(DocumentosTramiteMaestra doc) {
		if(!tareasExpedienteService.existeTareaREVPendienteDocumento(doc.getIdDocumento())){
			facesMsgErrorKey("no.existe.tarea.documento");
			return;
		}
		
		tarea = this.tareasExpedienteService.getTareaREVPendienteDocumento(doc.getIdDocumento());
		consultarTarea();
	}
	
	private void consultarTarea() {
		this.expediente = tarea.getExpediente();
		this.esAlta = false;
		this.mostrarIndicaciones=puedeMostrarIndicaciones();
		this.setFormEditable(false);
		reset();
		
		cargarDatosTramite(this.tarea.getTramiteExpediente());
		cargarDatosDocExpTramite(this.tarea.getDocumentoExpedienteTramite());
	
		try {
			cargarDatosDialogo();
			PrimeFaces.current().executeScript(DIALOGTAREASHOW);
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", e.getMessage());
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error("consultarTarea - " + message.getDetail());
		}
	}
	
	private boolean puedeMostrarIndicaciones() {
		try {
			return this.tareasExpedienteService.hayQueCrearTareaSiguiente(tarea, sesionBean.getUsuarioSesion());
		} catch (BaseException e) {
			facesMsgErrorGenerico();
			return false;
		}
	}
	
	public void cambiarAModoEditable() {
		this.setFormEditable(true);
	}
	
	private void reset() {
		this.listaTiposTareas = null;
		this.listaResponsablesTramitacionTarea = null;
		
		this.idTramiteExpediente = null;
		this.tramiteExpediente = null;
		
		this.idDocExpTramite  = null;
		
		this.descTramite = null;
		this.nombreDocumento = null;
		
		this.idCfgTareaSeleccionada = null;
		this.cfgTareaSeleccionada = null;
		this.idResponsableTramitacionTareaSeleccionado = null;
		
		this.usuarioEsResponsable = false;
		this.usuarioEsResponsableSuperiorCreador = false;
		
		this.motivoRechazo = null;
	}
	
	private void cargarDatosDialogo() throws BaseException {
		aplicarTituloDialogo();	
		
		if(null != this.tramiteExpediente) {
			this.descTramite = this.tramiteExpediente.getDescripcion();
		}
		
		this.nombreUsuarioAlta = getNombreUsuario(this.tarea.getUsuarioCreador());
		this.nombreUsuarioCierre = getNombreUsuario(this.tarea.getUsuarioCierre());
		
		this.cargarTiposTarea();
		
		this.idResponsableTramitacionTareaSeleccionado = null;
		
		if(esAlta) {
			if(this.listaTiposTareas.size() == 1) {
				this.cfgTareaSeleccionada = this.listaTiposTareas.get(0);
				this.idCfgTareaSeleccionada = this.cfgTareaSeleccionada.getId();
				cargarDatosCfgTarea();
			} else {
				this.idCfgTareaSeleccionada = null;
				this.cfgTareaSeleccionada = null;
			}
		} else {
			this.cfgTareaSeleccionada = this.listaTiposTareas.get(0);
			this.idCfgTareaSeleccionada = this.cfgTareaSeleccionada.getId();
			refrescarIdResponsable();
		}
			
		this.cargarResponsables();		
		
        if(!esAlta) {
        	//Cargar dependencias
            tarea.setDependencias(tareasExpedienteService.findDependencias(tarea.getId()));
            tarea.setEsTareaDependiente(!tarea.getDependencias().isEmpty());
            
			setUsuarioEsResponsable();
			setUsuarioEsResponsableSuperiorCreador();
        }

	}
	
	private void setUsuarioEsResponsable() {		
		if(usuariosResponsablesService.esResponsableDeUsuario(this.idResponsableTramitacionTareaSeleccionado, sesionBean.getIdUsuarioSesion())) {
			usuarioEsResponsable = true;
		}
	}
	
	private void setUsuarioEsResponsableSuperiorCreador() {
		final ResponsablesTramitacion respUsuario = responsablesTramitacionService.findResponsablePorDefectoUsuario(sesionBean.getIdUsuarioSesion());
		final ResponsablesTramitacion respUsuarioCreador = responsablesTramitacionService.findResponsablePorDefectoUsuario(tarea.getUsuarioCreador().getId());
		
		if(responsablesTramitacionService.esResponsableSuperiorDe(respUsuario, respUsuarioCreador)) {
			usuarioEsResponsableSuperiorCreador = true;
		}
	}
	
	
	//---------------------
	//Fecha límite, responsable y descripciones modificables si el usuario es jefe del creador de la tarea
	public boolean getResponsableHabilitado() {
		return esAlta || usuarioEsResponsableSuperiorCreador;
	}
	
	public boolean getDescripcionHabilitada() {
		boolean cfgDescModificable = true;
		
		if(null != cfgTareaSeleccionada) {
			cfgDescModificable = Boolean.TRUE.equals(cfgTareaSeleccionada.getDescripcionModificable());
		}
		
		return (esAlta || usuarioEsResponsableSuperiorCreador) && cfgDescModificable;
	}
	
	public boolean getFechaLimiteHabilitada() {
		return esAlta || usuarioEsResponsableSuperiorCreador;
	}

	//-------------
	
	private void aplicarTituloDialogo() {
		final String claveAccion; 
		
		if(esAlta) {
			claveAccion = "crear.tarea";
		} else if(Boolean.TRUE.equals(getFormEditable())) {
			claveAccion = "editar.tarea";
		} else {
			claveAccion = "consultar.tarea";			
		}
		
		final String claveElemento; 
		
		if(null != this.idDocExpTramite) {
			claveElemento = "de documento";
		} else if(null != this.idTramiteExpediente && expediente!=null) {
			claveElemento = "de tramite para el expediente "+expediente.getNumExpediente();
		} else if(expediente!=null){
			claveElemento = "para el expediente "+expediente.getNumExpediente();
		}else {
			claveElemento = "de expediente";
		}
		
		this.tituloDialogo = getMessage(claveAccion) + " " + claveElemento;
		
	}
	
	private void cargarResponsables() {
		if(!Boolean.TRUE.equals(getFormEditable())) { //El alta siempre es editable
			this.listaResponsablesTramitacionTarea = List.of(this.tarea.getResponsableTramitacion());
		} else {
			this.listaResponsablesTramitacionTarea = this.responsablesTramitacionService.findResponsablesActivos();
			
			if(!esAlta && !Boolean.TRUE.equals(this.tarea.getResponsableTramitacion().getActivo())) {
				//Lo añadimos con vistas a cambiarlo (la validación al guardar obligará a ello)
				this.listaResponsablesTramitacionTarea.add(this.tarea.getResponsableTramitacion());		
			}
		}
	}
	
	public void cambiarResponsableSeleccionado() {
		if(null == this.idResponsableTramitacionTareaSeleccionado) {
			tarea.setResponsableTramitacion(null);
		} else {
			try {
				final ResponsablesTramitacion resp = responsablesTramitacionService.obtenerObjeto(this.idResponsableTramitacionTareaSeleccionado);
				tarea.setResponsableTramitacion(resp); //este set provoca que tarea.getCambiaResponsable devuelva true
			}catch(Exception e) {
				facesMsgErrorGenerico();
			}
		}
		
		//Refrescamos la condicion
		setUsuarioEsResponsable();
	}
	
	private String getNombreUsuario(Usuario u) {
		return u==null? null : u.getNombreAp();
	}
	
	private void cargarTiposTarea() throws BaseException {
		if(esAlta) {
			final Long[] idsTipoTramite = tramiteExpedienteService.getIdsTipoTramiteSubtramite(this.tramiteExpediente);	
			this.listaTiposTareas = this.cfgTareasService.findTiposTareasManualesActivasCfg(
					expediente.getValorTipoExpediente().getId(),
					idsTipoTramite[0],idsTipoTramite[1],
					null != this.idDocExpTramite);
		} else {
			this.listaTiposTareas = List.of(tareasExpedienteService.getCfgTareaActual(tarea));
		}

	}
	
	private void cargarDatosDocExpTramite(Long idDocExpTr) {		
		cargarDatosDocExpTramite(documentosExpedientesTramitesService.obtener(idDocExpTr));
	}
	
	private void cargarDatosDocExpTramite(DocumentosExpedientesTramites docExpTr) {		
		if(docExpTr != null) {
			this.idDocExpTramite = docExpTr.getId();
			cargarNombreDocumento(docExpTr);
			//Sobrescribimos el idTramite si lo había
			cargarDatosTramite(docExpTr.getTramiteExpediente());
		}
	}
	
	private void cargarNombreDocumento(DocumentosExpedientesTramites docExpTr) {
		this.nombreDocumento = (null == docExpTr)? null 
				: docExpTr.getDocumentoExpediente().getDescripcionDocumento();
	}	
	
	private void cargarDatosTramite(Long idTr) {		
		cargarDatosTramite(tramiteExpedienteService.obtener(idTr));
	}
	
	private void cargarDatosTramite(TramiteExpediente tram) {
		if(tram != null) {
			this.idTramiteExpediente = tram.getId();
			this.tramiteExpediente = tramiteExpedienteService.obtener(this.idTramiteExpediente);
		}
	}
	
	public boolean existeTareaREV(DocumentosTramiteMaestra docTr) {

		boolean b = false;
		
		TramiteExpediente tramExp = tramiteExpedienteService.obtener(docTr.getIdTramiteExpediente());
		
		Long idTipoTramSup = null;
		if (tramExp.getTramiteExpedienteSup() != null) {
			idTipoTramSup = tramExp.getTramiteExpedienteSup().getTipoTramite().getId();
		}

		ValoresDominio tareasREV = valoresDominioService.findValoresDominioByCodigoDomCodValDom("TAREAS", "REV");
		List<CfgTareas> listaREV = cfgTareasService.findCfgTareas(tramExp.getExpediente().getValorTipoExpediente().getId(), tramExp.getTipoTramite().getId(), idTipoTramSup, tareasREV.getId(), true);
		
		if(listaREV != null && !listaREV.isEmpty()) {
			b = true;
			}

		return b;
	}

	
	public void cargarDatosCfgTarea() {
		//Si es != null vendrá del select de tipos de tarea
		//No sirve por que está incompleto (solo tiene los datos para el select)
		//Lo reseteamos
		
		this.cfgTareaSeleccionada = null;
		
		if(this.idCfgTareaSeleccionada != null) {
			this.cfgTareaSeleccionada = cfgTareasService.obtener(this.idCfgTareaSeleccionada);
			tareasExpedienteService.aplicarCfgTareas(tarea, this.cfgTareaSeleccionada);
						
			//¿podría estar dentro de aplicarCfgTareas?
			ResponsablesTramitacion resp = tareasExpedienteService.seleccionarResponsable(tarea, cfgTareaSeleccionada);
			if(resp != null) {
				//cargamos el objeto para que esté disponible para el listado Mi Mesa
				resp = responsablesTramitacionService.obtenerObjeto(resp.getId()); 
			}
			tarea.setResponsableTramitacion(resp);
		} else {
			tarea.setFechaLimite(null);
		}
	
		refrescarIdResponsable();
		
		cargarListaTareasCierreAuto();
		
	}
	
	private void refrescarIdResponsable() {
		final ResponsablesTramitacion resp = tarea.getResponsableTramitacion();
		this.idResponsableTramitacionTareaSeleccionado = (null == resp)? null : resp.getId();
	}
	
	private void cargarListaTareasCierreAuto() {		
		
		final String strCodigosCierre = this.cfgTareaSeleccionada.getCierreAutomaticoTramite();
		
		if(StringUtils.isBlank(strCodigosCierre)) {
			return;
		}		
		
		//Hay códigos de cierre: busco las tareas abiertas del trámite y los responsables del usuario.
		final List<TareasExpediente> lista = tareasExpedienteService
								.findTareasPendientesTramiteDeResponsablesDeUsuario(this.idTramiteExpediente, sesionBean.getIdUsuarioSesion());
	
		if(lista.isEmpty()) {
			return;
		}
		
		//Hay tareas: filtramos la lista obtenida por los tipos especificados
		final String[] codigosCierre = strCodigosCierre.split(";");
		listaTareasCierreAuto = ListUtils.filter(lista, t -> ArrayUtils.contains(codigosCierre, t.getValorTipoTarea().getCodigo()));
		selectedTareasCierreAuto.clear();
		selectedTareasCierreAuto.addAll(listaTareasCierreAuto); //Por defecto seleccionamos todos.
	}
	
	//Se invoca también desde el datePicker de fechaInicio
	//Si no fuera así no haría falta mantener cfgTareaSeleccionada
	public void refrescarFechaLimite() {
		tareasExpedienteService.aplicarFechaLimite(tarea, this.cfgTareaSeleccionada);
	}

	//*********************************************
	
	public void abrirRechazarTarea(){
		if(validarTarea()) {
			PrimeFaces.current().ajax().addCallbackParam("open", true);
		}
	}
	
	public void rechazarTarea() {
		try {
			if(StringUtils.isBlank(this.motivoRechazo)) {
				facesMsgErrorKey("motivo.rechazo.obligatorio");
				return;
			}
			
			//No debería hacer falta, pero validamos de nuevo por si acaso
			if(validarTarea()) {
				tareasExpedienteService.rechazarTarea(tarea, sesionBean.getUsuarioSesion(), this.motivoRechazo);
				PrimeFaces.current().ajax().addCallbackParam(CALLBACK_PARAM_SAVED, true);
			}
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
	
	public void cargarTareaListado(TareasExpediente t) {
		//No cargamos aún de bd
		this.tarea = t;
	}
	
	public String textoConfirmarFinalizar() {
		if(tarea == null || tarea.getId() == null) return StringUtils.EMPTY;
		
		String tipoTarea = tarea.getValorTipoTarea().getDescripcion();
		String numExp = tarea.getExpediente().getNumExpediente();
		String msg = getMessage("pregunta.confirma.finalizar.tarea.listado");
		return MessageFormat.format(msg, tipoTarea, numExp);
	}
	
	public void cerrarTareaListado() {
		//Hacemos la carga aquí para tener fresca la sesión de Hibernate.
		this.tarea = this.tareasExpedienteService.obtener(tarea.getId());
		cerrarTarea();
	}
	
	public void cerrarTarea() {
		try {
			if(validarTarea()) {
				tareasExpedienteService.cerrarTarea(tarea, sesionBean.getUsuarioSesion());
				PrimeFaces.current().ajax().addCallbackParam(CALLBACK_PARAM_SAVED, true);
				
				//El tipo de tarea viene ya cargado 
				if(tarea.getValorTipoTarea().getCodigo().equals(Constantes.COD_TIP_TAR_TRAM_FYN)) {
					TramiteExpediente tram = tarea.getTramiteExpediente();
					if(tram != null) {			
						PrimeFaces.current().ajax().update(Constantes.JSFID_PESTANAS_EXP + ":botonesFYN_" + tram.getId());
					}
				}
				
				final String msg = mensajesProperties.getString("tarea") + " " + mensajesProperties.getString("guardada.correctamente");
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "", msg);
				FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, message);	
			}
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
	
	private boolean validarTarea() { 
		//Validamos solo los campos obligatorios para mostrar el mensaje genérico.
		
		if(null == tarea.getValorTipoTarea() || null == tarea.getResponsableTramitacion()) {
			facesMsgErrorKey(CLAVE_CAMPOS_OBLIGATORIOS);
			return false;
		}
		
		//Este mensaje de inactivo ya estaba. Lo dejamos.
		final ResponsablesTramitacion rt = responsablesTramitacionService.obtener(tarea.getResponsableTramitacion().getId());

		if(!Boolean.TRUE.equals(rt.getActivo())) {
			facesMsgErrorKey("responsable.tramitacion.inactivo");
			return false;
		}
		
		if(StringUtils.isBlank(tarea.getDescripcion())) {
			facesMsgErrorKey(CLAVE_CAMPOS_OBLIGATORIOS);
			return false;
		}

		final Date fechaInicio = tarea.getFechaInicio();
		
		if(null == fechaInicio) {
			facesMsgErrorKey(CLAVE_CAMPOS_OBLIGATORIOS);
			return false;
		}
		
		if(null == tarea.getUsuarioCreador()) {
			facesMsgErrorKey(CLAVE_CAMPOS_OBLIGATORIOS);
			return false;
		}
		
		return true;
	}
	
	public void previoGuardarTarea() {
		if(!selectedTareasCierreAuto.isEmpty()) {		
			PrimeFaces.current().ajax().update(Constantes.JSFID_FORMEXP + ":textoConfirmarCierreTareasAuto");
			PrimeFaces.current().executeScript("PF('confirmarCierreTareasAuto').show()");
		} else {
			guardarTarea();
		}
	}
	
	public void guardarTarea() {
		try {
			tareasExpedienteService.aplicarDatosTramite(tarea,this.idTramiteExpediente, this.idDocExpTramite);
			
			if(validarTarea()) {
				
				ObservacionesExpedientes obsExp = tarea.getObservaciones();
				obsExp = observacionesExpedientesService.guardarObservacionesExpedientes(obsExp, obsExp.getTexto(), Constantes.COD_VAL_DOM_TIPOBS_TAR, tarea.getExpediente());
				tarea.setObservaciones(obsExp);
				tarea = tareasExpedienteService.guardar(tarea, sesionBean.getUsuarioSesion());				
				obsExp.setTareaExpdte(tarea);
				observacionesExpedientesService.guardar(obsExp);				
								
				//Precargamos datos para listados (Mi Mesa y tareas expediente)
				tarea.setValorTipoTarea(valoresDominioService.obtener(tarea.getValorTipoTarea().getId()));
				tarea.getValorTipoTarea().getDescripcion();
				
				Expedientes exp = expedientesService.obtener(tarea.getExpediente().getId());
				exp.getValorSituacionExpediente().getCodigo();
				tarea.setExpediente(exp);
				//-------
								
				PrimeFaces.current().ajax().addCallbackParam(CALLBACK_PARAM_SAVED, true);
				
				if(esAlta) {
					//Si ha cambiado el responsable tengo que recargar e informar al usuario
					if(Boolean.TRUE.equals(cfgTareaSeleccionada.getCambioAutomaticoTramite()) && tarea.getCambiaResponsable()) {
						//El cambio automático actualiza también la botonera (botonesFYN)
						TramiteExpediente tram = tarea.getTramiteExpediente();
						PrimeFaces.current().executeScript("recargar_responsable_tramite_" + tram.getId() + "()");
							facesMsgInfo("El responsable del trámite '" + tram.getDescripcion()
									+ "' ha pasado a ser '" + tram.getResponsable().getDescripcion() + "'.");
					} else //Si no hay cambio tenemos que mirar al menos el tipo de tarea
						if(tarea.getEsTareaTipo(Constantes.COD_TIP_TAR_TRAM_FYN)) {
						TramiteExpediente tram = tarea.getTramiteExpediente();
						PrimeFaces.current().ajax().update(Constantes.JSFID_PESTANAS_EXP + ":botonesFYN_" + tram.getId());
					}
					
					for(TareasExpediente tCierre : selectedTareasCierreAuto) {
						tareasExpedienteService.cerrarTareaInmediatamente(tCierre, sesionBean.getUsuarioSesion());
					}
					
				}
				
				final String msg = getMessage("tarea") + " " + getMessage("guardada.correctamente");
				PrimeFaces.current().ajax().addCallbackParam(CALLBACK_PARAM_SAVED, true);
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
	
	//**********************************************
	
	public void onRowSelect(final SelectEvent<TareasExpediente> event) {
		plegado = false;						
		tareaExpedienteSeleccionado = event.getObject();
	}	

	public boolean existeTareaFYNTramite(Long idTram) {
		return this.tareasExpedienteService.existeTareaFYNPendienteTramite(idTram);
	}
	
}
