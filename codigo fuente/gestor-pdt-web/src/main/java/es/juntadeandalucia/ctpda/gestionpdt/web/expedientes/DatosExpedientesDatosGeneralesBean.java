package es.juntadeandalucia.ctpda.gestionpdt.web.expedientes;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.primefaces.PrimeFaces;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.AgrupacionesExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.ArticulosAfectadosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.CfgTipoExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.DerechosReclamadosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.DetalleExpdteTram;
import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.ExpedientesRelacion;
import es.juntadeandalucia.ctpda.gestionpdt.model.MateriaTipoExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.MateriasExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.ObservacionesExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.PersonasExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.PlazosExpdte;
import es.juntadeandalucia.ctpda.gestionpdt.model.ResponsablesTramitacion;
import es.juntadeandalucia.ctpda.gestionpdt.model.SituacionesExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.SujetosObligadosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.TareasExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.TramiteExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.Usuario;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.service.AgrupacionesExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ArticulosAfectadosExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.CfgTipoExpedienteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.DerechosReclamadosExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.DetalleExpdteTramService;
import es.juntadeandalucia.ctpda.gestionpdt.service.DocumentosExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ExpedientesRelacionService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.MateriaTipoExpedienteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.MateriasExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ObservacionesExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.PersonasExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.PlazosExpdteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ResponsablesTramitacionService;
import es.juntadeandalucia.ctpda.gestionpdt.service.SeriesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.SituacionesExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.SujetosObligadosExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.TareasExpedienteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.TramiteExpedienteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ValoresDominioService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.ValidacionException;
import es.juntadeandalucia.ctpda.gestionpdt.util.FechaUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.Constantes;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.JsfUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.LazyDataModelByQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.MyFilterMeta;
import es.juntadeandalucia.ctpda.gestionpdt.web.menu.NavegacionBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.menu.NavegacionBean.ListadoNavegaciones;
import es.juntadeandalucia.ctpda.gestionpdt.web.personas.DatosPersonasBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.BaseBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.ContextoVolver;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.FacesUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.UtilsComun;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.VolverBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@ViewScoped
public class DatosExpedientesDatosGeneralesBean extends BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static final String EXPEDIENTEDG = "expedientesDG";
	private static final String EDITABLE = "editable";
	private static final String MENSAJEERROR = "error";
	private static final String MENSAJESFORMULARIO = "messagesFormulario";
	private static final String MENSAJEEXPEDIENTES = "Expediente ";
	private static final String MENSAJEENTRADA = "Entrada ";
	private static final String VUELTACONTROLPLAZOS = "_volverControlPlazos_";
	private static final String IDEXPSESSION = "idExp";
	private static final String IDPERSONA = "idPersona";	
 
	private static final String ACTUALIZADOCORRECTAMENTE = "actualizado.correctamente";
	private static final String NOEXISTEREGISTROCONFIG = "no.existe.registro.config";
	private static final String EXPEDIENTEFORMULARIO = "expedienteFormulario";
	private static final String ULTIMAMODIFICACIONMOTIVO = "ultima.modificacion.motivo";
	

	
	@Autowired
	private VolverBean volverBean;
	@Autowired
	private ComunExpedientesBean comunExpedientesBean;
	@Autowired
	private NavegacionBean navegacionBean;	
	
	@Getter
	@Setter
	private Expedientes expedientes;
	@Getter
	@Setter
	private ValoresDominio valoresDominio;

	@Getter
	@Setter
	private ValoresDominio valorMotivoInadmision;
	@Getter
	@Setter
	private ValoresDominio valorTipoExpediente;
	@Getter
	@Setter
	private ValoresDominio valorTipoInfraccion;
	@Getter
	@Setter
	private ValoresDominio valorCanalEntrada;
	@Getter
	@Setter
	private ResponsablesTramitacion responsableExpediente;
	@Getter
	@Setter
	private ValoresDominio valorSituacionesExpedientes;
	@Getter
	@Setter
	private ValoresDominio valorInstructorAPI;
	
	@Autowired
	private ExpedientesService expedientesService;
	@Autowired
	private ValoresDominioService valoresDominioService;
	@Autowired
	private PlazosExpdteService plazosExpdteService;
	@Autowired
	private ArticulosAfectadosExpedientesService articulosAfectadosExpedientesService;
	@Autowired
	private DerechosReclamadosExpedientesService derechosReclamadosExpedientesService;
	@Autowired
	private SituacionesExpedientesService situacionesExpedientesService;
	@Autowired
	private ResponsablesTramitacionService responsablesTramitacionService;	
	
	@Autowired
	private CfgTipoExpedienteService cfgTipoExpedienteService;

	@Autowired
	private MateriasExpedientesService materiasExpedientesService;	
	@Autowired
	private MateriaTipoExpedienteService materiaTipoExpedienteService;	
	@Autowired
	private SujetosObligadosExpedientesService sujetosObligadosExpedientesService;	
	@Autowired
	private PersonasExpedientesService personasExpedientesService;	
	@Autowired
	private SeriesService seriesService;		
	
	@Autowired
	private TareasExpedienteService tareasExpedienteService;
	@Autowired
	private TramiteExpedienteService tramiteExpedienteService;	
	@Autowired
	private DetalleExpdteTramService detalleExpdteTramService;	
	@Autowired
	private DocumentosExpedientesService documentosExpedientesService;
	@Autowired
	private ObservacionesExpedientesService observacionesExpedientesService;	
	@Autowired
	private ExpedientesRelacionService expedientesRelacionService;
	@Autowired
	private AgrupacionesExpedientesService agrupacionesExpedientesService;
	
	
	@Getter
	@Setter
	private List<ValoresDominio> listaTiposExpedientes;
	@Getter
	@Setter
	private List<ValoresDominio> listaTiposInfraccion;
	@Getter
	@Setter
	private List<ResponsablesTramitacion> listaResponsables;
	@Getter
	@Setter
	private List<SituacionesExpedientes> listaSituacionesExpedientes;
	@Getter
	@Setter
	private List<ValoresDominio> listaTiposCanalEntrada;
	@Getter
	@Setter
	private List<ValoresDominio> listaInstructoresAPI;
	@Getter
	@Setter
	private Long selectedNuevoTipoExpedienteId;
	@Getter
	@Setter
	private Long selectedNuevoTipoInfraccionId;
	@Getter
	@Setter
	private Long selectedNuevoResponsableId;
	@Getter
	@Setter
	private Long selectedNuevaSituacionId;
	@Getter
	@Setter
	private List<MateriaTipoExpediente> selectedMaterias;
	@Getter
	@Setter
	private Long selectedNuevoCanalEntId;
	
	
	@Getter
	@Setter
	private boolean comptCtpda = true;
	@Getter
	@Setter
	private List<ValoresDominio> listaAutoridadCompetente;
	@Getter
	@Setter
	private Long selectedAutoridadCompetenteId;
	@Getter
	@Setter
	private boolean aepd = false;
	@Getter
	@Setter
	private boolean tramAnonima = false;
	@Getter
	@Setter
	private boolean abstencionRecusacion = false;
	@Getter
	@Setter
	private List<ValoresDominio> listaTipoAdmision;
	@Getter
	@Setter
	private Long selectedTipoAdmisionId;
	@Getter
	@Setter
	private Long selectedNuevoMotivoInadmisionId;
	@Getter
	@Setter
	private boolean impMedidas = false;
	@Getter
	@Setter
	private boolean api = false;
	@Getter
	@Setter
	private Long selectedInstructorAPIId;
	@Getter
	@Setter
	private List<ValoresDominio> listaArticuloAfectado;
	@Getter
	@Setter
	private List<ValoresDominio> selectedArticulosAfectados;
	@Getter
	@Setter
	private List<ValoresDominio> listaDerechoReclamado;
	@Getter
	@Setter
	private List<ValoresDominio> listaValoresDominioMotivoInadmision;
	@Getter
	@Setter
	private List<ValoresDominio> selectedDerechosReclamados;
	@Getter
	@Setter
	private boolean opsicSolicitante = false;
	@Getter
	@Setter
	private boolean opsicRepresentante = false;
	@Getter
	@Setter
	private Boolean habilitarComboMotivoInad;
	
	@Getter
	@Setter
	private Boolean habilitarMotivoExp;
	
	@Getter
	@Setter
	private boolean habilitarBotonEdicionMotivoExp;
	
	@Getter
	@Setter
	private boolean pintarOpSol = false;
	@Getter
	@Setter
	private boolean pintarOpRep = false;
	
	@Getter
	@Setter
	private boolean disableSaveExpSitLib = false;
	
	@Getter
	@Setter
	private boolean disableComboExpSitLib = true;
	
	@Getter	
	@Setter
	private Boolean permisoSaveExpSitLib;
	
	@Getter	
	@Setter
	private Boolean permisoCreaExpEPC;
	
	@Getter	
	@Setter
	private Boolean permisoDocsAExpEPC;
	
		
	@Getter	
	@Setter
	private Boolean permisoEditMotivoExp;
	
	@Getter	
	@Setter
	private SituacionesExpedientes sitExpActual;
	
	@Autowired
	private DatosExpedientesBean datosExpedientesBean;

	@Getter	@Setter
	private Boolean permisoSaveExp;
	
	@Getter
	@Setter
	private List<String> listaCodigoPermisos;
	
	@Autowired
	private UtilsComun utilsComun;
	
	@Getter
	@Setter
	private List<MateriaTipoExpediente> listaMaterias;
	

	@Getter @Setter
	private String selectedValorDomTipoExpedienteId;
	
	@Getter @Setter
	private List<ValoresDominio> listaValoresDomTiposExpedientesPosibles;
	
	@Getter
	@Setter
	private String numeroExpedienteFiltro;
	
	@Getter
	@Setter
	private String nombreExpedienteFiltro;
	
	@Getter
	@Setter
	private String auditoriaModifMotivoExp;

	@Getter
	@Setter
	private Date fechaEntradaFiltro;
	
	@Getter
	private LazyDataModelByQueryService<Expedientes> lazyModelExpedientesBuscador;
	
	
	@Getter @Setter
	private Usuario usuario;
	
	@Getter
	private SortMeta sortDefaultLazyExpedientes;
	
	@Getter
	@Setter
	private Expedientes selectedLazyExpedientes;
	
	@Getter 
	@Setter
	private String auditoriaModifDescripcionExp;
	
	@Getter
	@Setter
	private Boolean habilitarDescripcionExp;
	
	@Getter
	@Setter
	private Boolean habilitarBotonEdicionDescripcionExp;
	
	@Getter
	@Setter
	private Boolean permisoEditDescripcionExp;
	
	@Getter
	@Setter
	private Long selectedGravedadId;
	
	@Getter
	@Setter
	private ValoresDominio gravedadExpediente;
	
	@Getter
	@Setter
	private List<ValoresDominio> listaValoresDominioGravedad;
	
	@Getter
	@Setter
	private Long selectedOrigenId;
	
	@Getter
	@Setter
	private List<ValoresDominio> listaValoresDominioOrigen;
	
	@Getter
	@Setter
	private ValoresDominio origenExpediente;
	
	@Getter	
	@Setter
	private Boolean permisoRcoCamposPestanya;
	
	@Getter	
	@Setter
	private Boolean permisoRceCamposPestanya;
	
	@Getter	
	@Setter
	private Boolean permisoPsanCamposPestanya;
	
	@Getter	
	@Setter
	private Boolean permisoXpcCamposPestanya;
	
	@Getter	
	@Setter
	private Boolean permisoRcoEditRespExp;
	
	@Getter	
	@Setter
	private Boolean permisoRceEditRespExp;
	
	@Getter	
	@Setter
	private Boolean permisoPsanEditRespExp;
	
	@Getter	
	@Setter
	private Boolean permisoXpcEditRespExp;
	
	@Override
	@PostConstruct
	@SuppressWarnings("unchecked")
	public void init() {
		super.init();
		
		usuario = (Usuario)JsfUtils.getSessionAttribute(Constantes.USUARIO);
		
		/**
		 * CONSULTA DE PERMISOS DEL USUARIO
		 * */
		listaCodigoPermisos = (List<String>) JsfUtils.getSessionAttribute(Constantes.LISTACODIGOPERMISOS);				
		permisoSaveExpSitLib = listaCodigoPermisos.contains(Constantes.PERMISO_SAVE_EXPSITLIB);
		permisoCreaExpEPC = listaCodigoPermisos.contains(Constantes.PERMISO_CREA_EXPEPC);
		permisoDocsAExpEPC = listaCodigoPermisos.contains(Constantes.PERMISO_DOCS_EXPEPC);
		permisoSaveExp = listaCodigoPermisos.contains(Constantes.PERMISO_SAVE_EXP);
		
		permisoEditMotivoExp = listaCodigoPermisos.contains(Constantes.PERMISO_EDIT_MOTIVOEXP);
		permisoEditDescripcionExp  = listaCodigoPermisos.contains(Constantes.PERMISO_EDIT_DESCIPCIONEXP);
		
		FacesUtils.setAttribute(EDITABLE, JsfUtils.getFlashAttribute(EDITABLE));
						
		try {
			cargarExpediente();
		} catch (ValidacionException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(NOEXISTEREGISTROCONFIG));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error("init - " + message.getDetail());
		}
		
		
		valorTipoExpediente = expedientes.getValorTipoExpediente();
		valorSituacionesExpedientes = expedientes.getValorSituacionExpediente();
		valorCanalEntrada = expedientes.getValorCanalEntrada();
		valorTipoInfraccion = expedientes.getValorTipoInfraccion();
		responsableExpediente = expedientes.getResponsable();
		valorInstructorAPI = expedientes.getValorInstructorAPI();
		valorMotivoInadmision = expedientes.getValorMotivoInadmision();
		gravedadExpediente = expedientes.getValorDominioGravedad();
		origenExpediente = expedientes.getValorDominioOrigen();
		
		
		// Cargar los campos valoresDominios
		if (valorTipoExpediente != null) {
			this.selectedNuevoTipoExpedienteId = valorTipoExpediente.getId();
		}
		if (valorSituacionesExpedientes != null && sitExpActual!=null) {
			this.selectedNuevaSituacionId = sitExpActual.getId();
		}
		if (valorCanalEntrada != null) {
			this.selectedNuevoCanalEntId = valorCanalEntrada.getId();
		}
		if (valorTipoInfraccion != null) {
			this.selectedNuevoTipoInfraccionId = valorTipoInfraccion.getId();
		}
		if (responsableExpediente != null) {
			this.selectedNuevoResponsableId = responsableExpediente.getId();
		}
		if (valorInstructorAPI != null) {
			this.selectedInstructorAPIId = valorInstructorAPI.getId();
		}
		if(valorMotivoInadmision != null)
		{
			this.selectedNuevoMotivoInadmisionId = valorMotivoInadmision.getId();
		}
		if(gravedadExpediente != null) {
			this.selectedGravedadId = gravedadExpediente.getId();
		}
		if(origenExpediente != null) {
			this.selectedOrigenId = origenExpediente.getId();
		}
		
		this.setValoresDominioAutCompYTipAdm();
		
		// Cargar los campos booleans
		cargaCamposBooleans();
		this.setFieldsOposiciones();

		listaTiposExpedientes = valoresDominioService.findValoresTipoExpediente();
		listaTiposInfraccion = valoresDominioService.findValoresInfraccion();
		listaResponsables = responsablesTramitacionService.findResponsablesActivos();
		listaInstructoresAPI = valoresDominioService.findValoresInstructoresAPI();

		this.cargaComboTipoSituacion();
		
		
		listaTiposCanalEntrada = valoresDominioService.findValoresCanalEntrada();
		listaAutoridadCompetente = valoresDominioService.findValoresDominioActivosByCodigoDominio(ValoresDominioService.COD_AUTCOMP);
		listaTipoAdmision = valoresDominioService.findValoresDominioActivosByCodigoDominio(ValoresDominioService.COD_TIPADM);
		listaArticuloAfectado = valoresDominioService.findValoresDominioActivosByCodigoDominio(ValoresDominioService.COD_ARTIC);
		listaDerechoReclamado = valoresDominioService.findValoresDominioActivosByCodigoDominio(ValoresDominioService.COD_DERRECL);
		listaMaterias = materiaTipoExpedienteService.findValoresDominioMateriaByValorDominioTipoExpediente(expedientes.getValorTipoExpediente().getId());
		listaValoresDominioMotivoInadmision = valoresDominioService.findValoresDominioActivosByCodigoDominio(Constantes.COD_VAL_DOM_INA);
		listaValoresDominioGravedad = valoresDominioService.findValoresDominioActivosByCodigoDominio(Constantes.COD_DOM_GRAV);
		listaValoresDominioOrigen = valoresDominioService.findValoresDominioActivosByCodigoDominio(Constantes.COD_DOM_ORIGREC);
		
		sortDefaultLazyExpedientes = SortMeta.builder().field("numExpediente").order(SortOrder.ASCENDING).priority(1).build();
		
		actualizarMotivoExpediente();
		actualizarBotonEditarMotivoExp();
		recargaMotivosInadmision();
		actualizarDescripcionExpediente();
		actualizarBotonEditarDescripcionExp();
		comprobarPermisosCamposPestanya();
		comprobarPermisosEditarResponsableExpediente();
		
		CfgTipoExpediente cfgTipoExpediente = cfgTipoExpedienteService.obtenerCfgTipoExpedientePorValorTipoExpediente(expedientes.getValorTipoExpediente().getId());
		if(expedientes.getId() == null && cfgTipoExpediente.getResponsablePorDefecto() != null) {
			selectedNuevoResponsableId = cfgTipoExpediente.getResponsablePorDefecto().getId();
		}
	}
	
	private void cargaCamposBooleans() {
		if(expedientes.getCompetenciaCtpda()!=null) {
			this.comptCtpda = expedientes.getCompetenciaCtpda();
		}else {
			this.comptCtpda = true;
		}
		
		if(expedientes.getAepd()!=null) {
			this.aepd = expedientes.getAepd();
		}else {
			this.aepd = false;
		}
		
		if(expedientes.getAbstencionRecusacion()!=null) {
			this.abstencionRecusacion = expedientes.getAbstencionRecusacion();
		}else {
			this.abstencionRecusacion = false;
		}
		
		if(expedientes.getTramitacionAnonima()!=null) {
			this.tramAnonima = expedientes.getTramitacionAnonima();
		}else {
			this.tramAnonima = false;
		}
		
		if(expedientes.getImposicionMedidas()!=null) {
			this.impMedidas = expedientes.getImposicionMedidas();
		}else {
			this.impMedidas = false;
		}
		
		if(expedientes.getApi()!=null) {
			this.api = expedientes.getApi();
		}else {
			this.api = false;
		}
	}
	
	private void inicializarCampos() {
		this.comptCtpda = true;
		this.aepd = false;
		this.abstencionRecusacion = false;
		this.tramAnonima = false;
		this.impMedidas = false;
		this.api = false;
		this.opsicSolicitante = false;
		this.opsicRepresentante = false;
		this.selectedArticulosAfectados = null;
		this.selectedDerechosReclamados = null;
		this.selectedMaterias = null;
	}

	
	private void cargaComboTipoSituacion() {
		if(listaSituacionesExpedientes != null && listaSituacionesExpedientes.size()== 1) {
			/* 	
			 * Si la listaSituacionesExpediente tiene 1 elemento, ver si se está creando 
			 * o editando para agregar la situacionesExpedientes inicial o no agregarla a la 
			 * lista 
			 */
			if(this.sitExpActual != null) {
				this.buscarSituacionActual();
			}else {
				this.selectedNuevaSituacionId = listaSituacionesExpedientes.get(0).getId();
			}
		}else if(listaSituacionesExpedientes == null || listaSituacionesExpedientes.isEmpty()) {
			// Meter la situación actual en el combo.
			listaSituacionesExpedientes = new ArrayList<>();
			listaSituacionesExpedientes.add(sitExpActual);			
		}else {
			this.buscarSituacionActual();
		}
	}
	
	private void buscarSituacionActual() {
		// Se busca si en el listado actual existe la situación en la cual se encuentra el expediente.
		Optional<SituacionesExpedientes> sitExp = this.listaSituacionesExpedientes.stream()
				.filter(p-> p.getValorSituacion().getId().equals(this.valorSituacionesExpedientes.getId()) && 
						p.getValorTipoExpediente().getId().equals(this.expedientes.getValorTipoExpediente().getId()))
				.findFirst();
		if(!sitExp.isPresent()) {
			this.listaSituacionesExpedientes.add(this.sitExpActual);
		}

		this.selectedNuevaSituacionId = this.sitExpActual.getId();
	}
	
	private void setFieldsOposiciones() {
		// Consulta para obtener de la BBDD la persona y el representante principales
		PersonasExpedientes personasPrincipales = (PersonasExpedientes) JsfUtils
				.getSessionAttribute("expedientesPersPrinc");			
		JsfUtils.removeSessionAttribute("expedientesPersPrinc");
		if(personasPrincipales!=null) {
			if(personasPrincipales.getPersonas().getEsPF()) {
				if(expedientes.getOposicionPersona()!=null) {
					this.opsicSolicitante = expedientes.getOposicionPersona();
				}else {
					this.opsicSolicitante = false;
				}
				this.pintarOpSol = true;
			}
			if(personasPrincipales.getPersonasRepre() != null && personasPrincipales.getPersonasRepre().getEsPF()) {
				if(expedientes.getOposicionRepresentante()!=null) {
					this.opsicRepresentante = expedientes.getOposicionRepresentante();
				}else {
					this.opsicRepresentante = false;
				}
				this.pintarOpRep = true;
			}
		}
	}
	
	private void setValoresDominioAutCompYTipAdm() {
		if (expedientes.getValorAutoridadCompetente() != null) {
			this.selectedAutoridadCompetenteId = expedientes.getValorAutoridadCompetente().getId();
		}
		if (expedientes.getValorTipoAdmision() != null) {
			this.selectedTipoAdmisionId = expedientes.getValorTipoAdmision().getId();
		}
	}
	
	private List<ValoresDominio> returnListArtAfect(List<ArticulosAfectadosExpedientes> lista){
		List<ValoresDominio> valores = null;
		if(lista != null && !lista.isEmpty()) {
			valores = new ArrayList<>();
			for(ArticulosAfectadosExpedientes reg: lista){
				valores.add(reg.getValoresArtAfectExp());
			}
		}
		return valores;
	}
	
	private List<ValoresDominio> returnListderRecl(List<DerechosReclamadosExpedientes> lista){
		List<ValoresDominio> valores = null;
		if(lista != null && !lista.isEmpty()) {
			valores = new ArrayList<>();
			for(DerechosReclamadosExpedientes reg: lista){
				valores.add(reg.getValoresDerReclExp());
			}
		}
		return valores;
	}
	
	private void cargarExpediente() throws ValidacionException {
		final ContextoVolver cv = volverBean.getContexto();
		
		if(cv != null) {
			//Tenemos que recoger el expediente que ya teníamos en sesión
			expedientes = (Expedientes) JsfUtils.getSessionAttribute(EXPEDIENTEDG);
			
			// Cargar las listas de valores de Dominios, campos: Artículo afectado 
			// y Derecho reclamado.
			@SuppressWarnings("unchecked")
			List<ArticulosAfectadosExpedientes> artAfectList = (List<ArticulosAfectadosExpedientes>)JsfUtils.getSessionAttribute("expedientesDA");
			@SuppressWarnings("unchecked")
			List<DerechosReclamadosExpedientes> derReclList = (List<DerechosReclamadosExpedientes>)JsfUtils.getSessionAttribute("expedientesDD");
			this.selectedArticulosAfectados = returnListArtAfect(artAfectList);
			this.selectedDerechosReclamados = returnListderRecl(derReclList);
			@SuppressWarnings("unchecked")
			List<MateriasExpedientes> materiasList = (List<MateriasExpedientes>)JsfUtils.getSessionAttribute("expedientesMaterias");
			this.selectedMaterias = returnListMaterias(materiasList);
			if(expedientes.getId()!=null) {
				this.cargarListaSituacionEdicionConsulta();
				expedientes = expedientesService.obtener(expedientes.getId());
			}else {
				listaSituacionesExpedientes = situacionesExpedientesService.findSituacionesExpedientesTipoExped(expedientes.getValorTipoExpediente().getId());
			}			
		} else {
			// Carga normal
			inicializarCampos();

			final Expedientes expedienteFormulario = (Expedientes) JsfUtils.getSessionAttribute(EXPEDIENTEFORMULARIO);
			if (expedienteFormulario.getId() != null) {
				expedientes = expedientesService.obtener(expedienteFormulario.getId());
				
				// Cargar las listas de valores de Dominios, campos: Artículo afectado 
				// y Derecho reclamado.
				List<ArticulosAfectadosExpedientes> artAfectList = articulosAfectadosExpedientesService.obtenerListArticulosAfectadosExpedientePorExpediente(expedientes.getId());
				List<DerechosReclamadosExpedientes> derReclList = derechosReclamadosExpedientesService.obtenerListDerechosReclamadosExpedientePorExpediente(expedientes.getId());
				this.selectedArticulosAfectados = returnListArtAfect(artAfectList);
				this.selectedDerechosReclamados = returnListderRecl(derReclList);
				List<MateriasExpedientes> materiasList = materiasExpedientesService.obtenerListMateriasExpedientesPorExpediente(expedientes.getId());
				this.selectedMaterias = returnListMaterias(materiasList);
				
				this.cargarListaSituacionEdicionConsulta();
			} else {
				expedientes = new Expedientes();
				expedientes.setValorTipoExpediente(expedienteFormulario.getValorTipoExpediente());
				listaSituacionesExpedientes = situacionesExpedientesService.findSituacionesExpedientesTipoExped(expedientes.getValorTipoExpediente().getId());
			}
		}
		
		JsfUtils.setSessionAttribute(EXPEDIENTEDG, this.expedientes);
	}
	
	
	public void actualizarPestanyaDatosGral (Expedientes expedienteGuardado) {
		if(expedienteGuardado != null) {
			actualizarPestanyaDatosGralAux(expedienteGuardado);
					
			this.api = expedienteGuardado.getApi();
			this.expedientes.setApi(this.api);
			
			if (expedienteGuardado.getCompetenciaCtpda() != null) {
				this.comptCtpda = expedienteGuardado.getCompetenciaCtpda();
			} else
			{
				this.comptCtpda = true;
			}
			
			ValoresDominio valorAutComp = expedienteGuardado.getValorAutoridadCompetente();
			this.selectedAutoridadCompetenteId = valorAutComp == null? null : valorAutComp.getId();
			
			ValoresDominio valorIns = expedienteGuardado.getValorInstructorAPI();
			this.selectedInstructorAPIId = valorIns == null? null : valorIns.getId();
			
			ValoresDominio valorCanal = expedienteGuardado.getValorCanalEntrada();
			this.selectedNuevoCanalEntId = valorCanal == null? null : valorCanal.getId();
			
			ValoresDominio valorTipoAdmi = expedienteGuardado.getValorTipoAdmision();
			this.selectedTipoAdmisionId = valorTipoAdmi == null? null : valorTipoAdmi.getId();
			
			ValoresDominio valorMotivInad = expedienteGuardado.getValorMotivoInadmision();
			this.selectedNuevoMotivoInadmisionId = valorMotivInad == null? null : valorMotivInad.getId();
			
			actualizarPestanyaDatosGralAdicional (expedienteGuardado);
		}
	}
	
	public void actualizarPestanyaDatosGralAdicional (Expedientes expedienteGuardado) {
			
			if (expedienteGuardado.getImposicionMedidas() != null) {
				this.impMedidas = expedienteGuardado.getImposicionMedidas();
			} else
			{
				this.impMedidas = false;
			}
			
			this.expedientes.setFechaEntrada(expedienteGuardado.getFechaEntrada());
	}
	
	private void actualizarPestanyaDatosGralAux (Expedientes expedienteGuardado) {
		if (this.selectedNuevoCanalEntId == null) {
			ValoresDominio valorCanal = expedienteGuardado.getValorCanalEntrada();
			this.selectedNuevoCanalEntId = valorCanal == null? null : valorCanal.getId();
		} 
		if(this.expedientes.getFechaRegistro() == null)
		{
			this.expedientes.setFechaRegistro(expedienteGuardado.getFechaRegistro());
		}
		if(this.expedientes.getNumRegistro() == null || this.expedientes.getNumRegistro().isEmpty())
		{
			this.expedientes.setNumRegistro(expedienteGuardado.getNumRegistro());
		}
	}
	
	
	private void cargarListaSituacionEdicionConsulta() {
		try {
			if(expedientes.getId()!=null) {
				sitExpActual = this.situacionesExpedientesService.findSituacionesExpedientesTipExpValorSit(this.expedientes.getValorTipoExpediente().getId(), this.expedientes.getValorSituacionExpediente().getId());
				
				if(Boolean.TRUE.equals(this.getFormEditable()) ) {
					listaSituacionesExpedientes = situacionesExpedientesService.findSituacionesExpedientesRelSuperior(expedientes.getValorTipoExpediente().getId(), sitExpActual.getId());
				}else {
					listaSituacionesExpedientes = situacionesExpedientesService.findSituacionesExpedientesTipExpAll(expedientes.getValorTipoExpediente().getId());
				}
			}
		} catch (ValidacionException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(NOEXISTEREGISTROCONFIG));
			log.warn("cargarListaSituacionEdicionConsulta - " + message.getDetail());
		}
	}
	
	public void actualizarSessionExpedientes() {
		if (this.selectedNuevoTipoInfraccionId != null) {
			expedientes.setValorTipoInfraccion(valoresDominioService.obtener(this.selectedNuevoTipoInfraccionId));
		} else {
			expedientes.setValorTipoInfraccion(null);
		}
		if (this.selectedNuevoResponsableId != null) {
			expedientes.setResponsable(responsablesTramitacionService.obtener(this.selectedNuevoResponsableId));
		} else {
			expedientes.setResponsable(null);
		}
		if (this.selectedNuevoCanalEntId != null) {
			expedientes.setValorCanalEntrada(valoresDominioService.obtener(this.selectedNuevoCanalEntId));
		} else {
			expedientes.setValorCanalEntrada(null);
		}
		if (this.selectedNuevaSituacionId != null) {
			this.sitExpActual = situacionesExpedientesService.obtener(this.selectedNuevaSituacionId);
			expedientes.setValorSituacionExpediente(situacionesExpedientesService.obtener(this.selectedNuevaSituacionId).getValorSituacion()); 
		} else {
			expedientes.setValorSituacionExpediente(null);
		}
		if (this.selectedInstructorAPIId != null) {
			expedientes.setValorInstructorAPI(valoresDominioService.obtener(this.selectedInstructorAPIId));
		} else {
			expedientes.setValorInstructorAPI(null);
		}
		if (this.selectedGravedadId != null) {
			expedientes.setValorDominioGravedad(valoresDominioService.obtener(this.selectedGravedadId));
		} else {
			expedientes.setValorDominioGravedad(null);
		}
		if (this.selectedOrigenId != null) {
			expedientes.setValorDominioOrigen(valoresDominioService.obtener(this.selectedOrigenId));
		} else {
			expedientes.setValorDominioOrigen(null);
		}
						
		expedientes.setCompetenciaCtpda(this.comptCtpda);
		actualizarSessionAutoridadCompetente();
		expedientes.setAepd(this.aepd);
		expedientes.setTramitacionAnonima(this.tramAnonima);
		expedientes.setAbstencionRecusacion(this.abstencionRecusacion);
		actualizarSessionTipoAdmision();
		expedientes.setImposicionMedidas(this.impMedidas);
		expedientes.setApi(this.api);
		expedientes.setOposicionPersona(this.opsicSolicitante);
		expedientes.setOposicionRepresentante(this.opsicRepresentante);
		
		List<ArticulosAfectadosExpedientes> artAfectList = creaListArtAfect();
		List<DerechosReclamadosExpedientes> derReclList = creaListDerReacl();
		List<MateriasExpedientes> materiasList = creaListMaterias();
		
		JsfUtils.setSessionAttribute("expedientesDA", artAfectList);
		JsfUtils.setSessionAttribute("expedientesDD", derReclList);
		JsfUtils.setSessionAttribute("expedientesMaterias", materiasList);
		JsfUtils.setSessionAttribute(EXPEDIENTEDG, this.expedientes);
		
	}
	
	private void actualizarSessionAutoridadCompetente() {
		if (this.selectedAutoridadCompetenteId != null) {
			expedientes.setValorAutoridadCompetente(valoresDominioService.obtener(this.selectedAutoridadCompetenteId));
		} else {
			expedientes.setValorAutoridadCompetente(null);
		}
	}
	
	private void actualizarSessionTipoAdmision() {
		if (this.selectedTipoAdmisionId != null) {
			expedientes.setValorTipoAdmision(valoresDominioService.obtener(this.selectedTipoAdmisionId));
		} else {
			expedientes.setValorTipoAdmision(null);
		}
	}
	
	private List<ArticulosAfectadosExpedientes> creaListArtAfect(){
		List<ArticulosAfectadosExpedientes> art= null;
		if(this.selectedArticulosAfectados != null && !this.selectedArticulosAfectados.isEmpty()) {
			art= new ArrayList<>();
			ArticulosAfectadosExpedientes aa;
			for(ValoresDominio vd: this.selectedArticulosAfectados) {
				aa = new ArticulosAfectadosExpedientes();
				aa.setValoresArtAfectExp(vd);
				art.add(aa);
			}
		}
		return art;
	}
	
	private List<DerechosReclamadosExpedientes> creaListDerReacl(){
		List<DerechosReclamadosExpedientes> der = null;
		if(this.selectedDerechosReclamados != null && !this.selectedDerechosReclamados.isEmpty()) {
			der = new ArrayList<>();
			DerechosReclamadosExpedientes dr;
			for(ValoresDominio vd: this.selectedDerechosReclamados) {
				dr = new DerechosReclamadosExpedientes();
				dr.setValoresDerReclExp(vd);
				der.add(dr);
			}
		}
		return der;
	}
	
	public void vaciaAutCompt() {
		this.selectedAutoridadCompetenteId = null;
		if(expedientes.getId() == null) {
			actualizarSessionExpedientes();
		}
	}
		
	public void onItemUnselect(UnselectEvent<?> event) {
        /** Método de selección de elementos. **/
    }
	
	public void cargarSituacionesLibres() {
		try {
			this.listaSituacionesExpedientes = situacionesExpedientesService.findSituacionesExpedientesTipExpAll(this.expedientes.getValorTipoExpediente().getId());
			this.listaSituacionesExpedientes = quitarDuplicadosListSituaciones();
			disableSaveExpSitLib = true;
			disableComboExpSitLib = false;
			PrimeFaces.current().ajax().update("formFormularioExpedientes:tabViewPestanasExpediente:botonSituacionLibre");
			PrimeFaces.current().ajax().update("formFormularioExpedientes:tabViewPestanasExpediente:comboTipoSit");
		} catch (ValidacionException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(NOEXISTEREGISTROCONFIG));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error("cargarSituacionesLibres - " + message.getDetail());
		}
	}
	
	/*
	 * Método que elimina los duplicados de la lista de situaciones.
	 */
	private List<SituacionesExpedientes> quitarDuplicadosListSituaciones() {
		List<SituacionesExpedientes> listAux;
		Set<String> nameSet = new HashSet<>();
		SituacionesExpedientes expSit = situacionesExpedientesService.obtener(this.selectedNuevaSituacionId);
		// Añadimos la descripción del elemento seleccionado para que lo elimine de la lista y asi evitar que
		// si está duplicado se añada el elemento que tiene el id correcto al final.
		nameSet.add(expSit.getDescripcion());
		listAux = this.listaSituacionesExpedientes.stream()
		            .filter(p->nameSet.add(p.getDescripcion()))
		            .collect(Collectors.toList());
		listAux.add(expSit);
		Collections.sort(listAux, (x, y) -> x.getDescripcion().compareToIgnoreCase(y.getDescripcion()));
		return listAux;
	}
	
	/*
	 * Función llamada desde el formFormularioExpediente para actualizar el combo situación del formulario formDatosGeneralesExpedientes.
	 */
	public void actualizaSituacion() {
		if(expedientes.getId() !=null) {
			try {
				listaSituacionesExpedientes = situacionesExpedientesService.findSituacionesExpedientesRelSuperior(expedientes.getValorTipoExpediente().getId(), sitExpActual.getId());
			} catch (ValidacionException e) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(NOEXISTEREGISTROCONFIG));
				log.warn("actualizaSituacion para "+expedientes.getValorTipoExpediente().getCodigo()+", "+sitExpActual.getCodigoRelacion()+ " - " + message.getDetail());
			}
			
			Optional<SituacionesExpedientes> sitExp = this.listaSituacionesExpedientes.stream()
					.filter(p-> p.getValorSituacion().getId().equals(this.sitExpActual.getValorSituacion().getId()) && 
							p.getValorTipoExpediente().getId().equals(this.expedientes.getValorTipoExpediente().getId()))
					.findFirst();
			if(!sitExp.isPresent()) {
				this.listaSituacionesExpedientes.add(this.sitExpActual);
			}

			PrimeFaces.current().ajax().update("formFormularioExpedientes:tabViewPestanasExpediente:botonSituacionLibre");
			PrimeFaces.current().ajax().update("formFormularioExpedientes:tabViewPestanasExpediente:comboTipoSit");
		}
	}
	
	private boolean validacionesGuardar() {
		boolean valido = true;
		if ( selectedNuevaSituacionId == null || expedientes.getFechaEntrada() == null || selectedNuevoResponsableId == null){
			valido = false;			
			facesMsgErrorKey(CLAVE_CAMPOS_OBLIGATORIOS);
		}
		
		//------------
		
		boolean puedeCambiarSituacion = true;
		//Tenemos que ver si cambiamos a una situación que implique finalizar el expediente
		//En este caso no podemos tener trámites aún abiertos.
		try {
			SituacionesExpedientes situExp = situacionesExpedientesService.obtener(selectedNuevaSituacionId);
			ValoresDominio situacion = situExp.getValorSituacion();
			puedeCambiarSituacion = comunExpedientesBean.puedeCambiarASituacion(expedientes, null, situacion);
		} catch (ValidacionException e1) {
			facesMsgError(e1.getMessage());
			e1.printStackTrace();
			return false;
		}
		
		if(!puedeCambiarSituacion) {
			facesMsgErrorKey("tramites.abiertos.fin.expediente");
			valido = false;
		}
		
		if (expedientes.getFechaEntrada() != null && expedientes.getFechaInfraccion() != null && FechaUtils.despues(expedientes.getFechaInfraccion(), expedientes.getFechaEntrada())){
			valido = false;			
			facesMsgErrorKey("fecha.infraccion.mayor.fecha.entrada.expediente");
		}
		
		if (expedientes.getFechaSolicitudEjercicio() != null && FechaUtils.despues(expedientes.getFechaSolicitudEjercicio(),FechaUtils.hoy())){
			valido = false;			
			facesMsgErrorKey("fecha.solicitud.mayor.fecha.actual");
		}
		
		if (expedientes.getFechaRespuestaSolicitudEjercicio() != null && FechaUtils.despues(expedientes.getFechaRespuestaSolicitudEjercicio(),FechaUtils.hoy())){
			valido = false;			
			facesMsgErrorKey("fecha.respuesta.mayor.fecha.actual");
		}

		
		return valido;
	}
	
	public String guardarDatosGenerales() {
		String vueltaListado = "";
		boolean validoGuardar = validacionesGuardar();
		if(validoGuardar) {
			try {
				vueltaListado = guardarDatosGeneralesAux(vueltaListado);
				
				expedientes.setValorSituacionExpediente(situacionesExpedientesService.obtener(this.selectedNuevaSituacionId).getValorSituacion()); 
				expedientes.setResponsable(responsablesTramitacionService.obtener(this.selectedNuevoResponsableId));
				expedientes.setCompetenciaCtpda(this.comptCtpda);
				expedientes.setAepd(this.aepd);
				expedientes.setTramitacionAnonima(this.tramAnonima);
				expedientes.setAbstencionRecusacion(this.abstencionRecusacion);
				expedientes.setImposicionMedidas(this.impMedidas);
				expedientes.setApi(this.api);
				expedientes.setOposicionPersona(this.opsicSolicitante);
				expedientes.setOposicionRepresentante(this.opsicRepresentante);
				
				/** CONTROL DE PRESCRIPCIÓN **/
				
				controlPrescripcion();
								
				actualizarMotivoExpediente();
				
				actualizarDescripcionExpediente();

				boolean mostrarMsgCambioResp = mostrarMsgCambioResponsable();
				String msgCambioResp = null;
				
				if(mostrarMsgCambioResp) {
					final String msg = getMessage("aviso.responsable.expediente.cambiado");
					final String respActual = responsablesTramitacionService.obtenerDescripcion(expedientes.getResponsableActual().getId());
					final String respNuevo = expedientes.getResponsable().getDescripcion();
					msgCambioResp = MessageFormat.format(msg, respActual, respNuevo);
				}

				expedientes = expedientesService.guardar(expedientes);
				
				recalculoPlazos(expedientes);
				
				if(expedientes.getId() != null && situacionesExpedientesService.esSituacionExpedienteFinal(selectedNuevaSituacionId)) {
					comunExpedientesBean.cerrarPlazosAbiertosExpediente(expedientes.getId());
				}
				
				expedientes = utilsComun.expedienteUltimaModificacion(expedientes,expedientes.getFechaModificacion(),expedientes.getFechaCreacion(),expedientes.getUsuModificacion(),expedientes.getUsuCreacion());
				
				JsfUtils.setSessionAttribute(EXPEDIENTEFORMULARIO, expedientes);
				
				actualizaArticulosAfectados(creaListArtAfect());
				actualizaDerechosReclamados(creaListDerReacl());
				actualizaMaterias(creaListMaterias());
				
				datosExpedientesBean.actualizarCabecera(expedientes,null,null,null);
				
				if(mostrarMsgCambioResp) {
					facesMsgInfo(msgCambioResp); //para este mensaje usamos el diálogo deliberadamente
				}	
				
				mensajeTipoXPCActualizado();

				//actualiza la situacion-expediente
				sitExpActual = situacionesExpedientesService.obtener(this.selectedNuevaSituacionId);
								
				volverBean.limpiarContexto();
			} catch (BaseException e) {
				FacesMessage message = null;
				if(e.getMessage().contains(NOEXISTEREGISTROCONFIG)) {
					message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(NOEXISTEREGISTROCONFIG));
				}else {
					message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MENSAJEERROR));
				}
				PrimeFaces.current().dialog().showMessageDynamic(message);
				log.error("guardarDatosGenerales - " + e.getMessage());
			}
		}
		return vueltaListado;
	}
	
	/** METODO QUE ACTUA EN FUNCION DE SI EXISTE FECHA DE PRESCRIPCION PARA EL EXPEDIENTE. **/
	private void controlPrescripcion()
	{
		/** COMPROBAR SI LA FECHA DE PRESCRIPCION ESTA INFORMADA **/
		
		Date fechaPrescripcion = expedientes.getFechaPrescripcion(); 
		
		if(fechaPrescripcion != null)
		{
			/** BUSCAMOS SI EL EXPEDIENTE TIENE UN PLAZO ABIERTO TIPO 'ACU'. **/
			PlazosExpdte plazoExp = plazosExpdteService.findPlazosExpdteByExpTipPla(expedientes.getId(), 
						valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_TIPO_PLAZO, Constantes.COD_VAL_DOM_ACU).getId());
			
			/** SI LA FECHA DE PRESCRIPCION ES MENOR O IGUAL A LA GECHA LIMITE DEL PLAZO DE ACUERDO => GENERAMOS PLAZO DE PRESCRIPCION. **/
			if(plazoExp != null && plazoExp.getFechaLimite() != null && (FechaUtils.dateToStringFecha(plazoExp.getFechaLimite()).equals(FechaUtils.dateToStringFecha(fechaPrescripcion)) || plazoExp.getFechaLimite().after(fechaPrescripcion))) {
				try {
					utilsComun.generarPlazoExpdte(null, null, fechaPrescripcion, Constantes.COD_VAL_DOM_PRSC, Constantes.COD_VAL_DOM_DN, 0, "A", expedientes);
				} catch (BaseException e) {
					FacesMessage message = null;
					message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MENSAJEERROR));
					PrimeFaces.current().dialog().showMessageDynamic(message);
					log.error("controlPrescripcion - " + e.getMessage());
				}
			}
			else
			{
				borrarPlazoPRSC(expedientes);
			}
		}
		else
		{
			borrarPlazoPRSC(expedientes);
		}
	}
	
	private void borrarPlazoPRSC(Expedientes expedientes) {
		
		PlazosExpdte plazoExpPRSC = plazosExpdteService.findPlazosExpdteByExpTipPla(expedientes.getId(), 
				valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_TIPO_PLAZO, Constantes.COD_VAL_DOM_PRSC).getId());
		if (plazoExpPRSC != null) {
		
		try {
			utilsComun.generarPlazoExpdte(null,"FIN",null,Constantes.COD_VAL_DOM_PRSC, Constantes.COD_VAL_DOM_DN,	0, "A",expedientes);
		} catch (BaseException e) {
			FacesMessage message = null;
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error("controlPrescripcion - " + e.getMessage());
		}}
		
		
		
	}
	
	private boolean mostrarMsgCambioResponsable() {
		//Mostrar si no es alta, cambia el responsable y hay tareas que reasignar
		return expedientes.getId() != null && expedientes.cambiaResponsable()
				&& tareasExpedienteService.existeTareaPendienteExpedienteDeResponsable(
													expedientes.getId(),
													expedientes.getResponsableActual().getId()); //Responsable anterior
		
	}
	
	private String guardarDatosGeneralesAux (String vueltaListado) throws ValidacionException {
		boolean esFinalizado=false;
		
		guardarDatosGeneralesAux2();
		
		if (this.selectedTipoAdmisionId != null) {
			ValoresDominio valorTipoAdmision = valoresDominioService.obtener(this.selectedTipoAdmisionId);
			expedientes.setValorTipoAdmision(valorTipoAdmision);
			if(valorTipoAdmision.getCodigo().equals(Constantes.COD_VAL_DOM_INA) && this.selectedNuevoMotivoInadmisionId != null)
			{
				expedientes.setValorMotivoInadmision(valoresDominioService.obtener(this.selectedNuevoMotivoInadmisionId));
			}else {
				expedientes.setValorMotivoInadmision(null);
			}
			
		} else {
			expedientes.setValorTipoAdmision(null);
		}
		
		if (this.selectedInstructorAPIId != null) {
			expedientes.setValorInstructorAPI(valoresDominioService.obtener(this.selectedInstructorAPIId));
		} else {
			expedientes.setValorInstructorAPI(null);
		}
		
		List<SituacionesExpedientes> situacionesFinalizadas= situacionesExpedientesService.findSituacionesExpedientesFinalizados();
		for(SituacionesExpedientes s: situacionesFinalizadas) {
			if(s.getId().equals(selectedNuevaSituacionId)){
				esFinalizado=true;
				break;
			}
		}
		
		if(esFinalizado) {
			vueltaListado=this.accionesSituacionFinal();
		}
		
		return vueltaListado;
	}
	
	
	private void recalculoPlazos(Expedientes exp) throws BaseException {
		
		if (Constantes.PSAN.equals(exp.getValorTipoExpediente().getCodigo())) {
			
						
				utilsComun.generarPlazoExpdte(null, null, exp.getFechaEntrada(), Constantes.COD_VAL_DOM_RES, Constantes.COD_VAL_DOM_DN, 0, "A", exp);	
			
		}
		
	}
	
	private void guardarDatosGeneralesAux2 () {
		if (this.selectedNuevoTipoInfraccionId != null) {
			expedientes.setValorTipoInfraccion(valoresDominioService.obtener(this.selectedNuevoTipoInfraccionId));
		} else {
			expedientes.setValorTipoInfraccion(null);
		}
		
		if (this.selectedNuevoCanalEntId != null) {
			expedientes.setValorCanalEntrada(valoresDominioService.obtener(this.selectedNuevoCanalEntId));
		} else {
			expedientes.setValorCanalEntrada(null);
		}
		
		if (this.selectedAutoridadCompetenteId != null) {
			expedientes.setValorAutoridadCompetente(valoresDominioService.obtener(this.selectedAutoridadCompetenteId));
		} else {
			expedientes.setValorAutoridadCompetente(null);
		}
		
		if(this.selectedOrigenId != null) {
			expedientes.setValorDominioOrigen(valoresDominioService.obtener(this.selectedOrigenId));
		}else {
			expedientes.setValorDominioOrigen(null);
		}
		
		if(this.selectedGravedadId != null) {
			expedientes.setValorDominioGravedad(valoresDominioService.obtener(this.selectedGravedadId));
		}else {
			expedientes.setValorDominioGravedad(null);
		}
	}
	
	private void actualizaArticulosAfectados(List<ArticulosAfectadosExpedientes> expedientesDA) throws BaseException {
		List<ArticulosAfectadosExpedientes> artAfectClean = articulosAfectadosExpedientesService.obtenerListArticulosAfectadosExpedientePorExpediente(expedientes.getId());
		if(artAfectClean != null && !artAfectClean.isEmpty()) {
			for(ArticulosAfectadosExpedientes artAfect: artAfectClean) {
				articulosAfectadosExpedientesService.delete(artAfect.getId());
				
				expedientes = utilsComun.expedienteUltimaModificacion(expedientes,FechaUtils.fechaYHoraActualDate(),FechaUtils.fechaYHoraActualDate(),usuario.getLogin(),usuario.getLogin());
			}
		}		
		if(expedientesDA != null && !expedientesDA.isEmpty()) {
			for(ArticulosAfectadosExpedientes artAfect: expedientesDA) {
				artAfect.setExpediente(expedientes);
				articulosAfectadosExpedientesService.guardar(artAfect);
				
				expedientes = utilsComun.expedienteUltimaModificacion(expedientes,artAfect.getFechaModificacion(),artAfect.getFechaCreacion(),artAfect.getUsuModificacion(),artAfect.getUsuCreacion());
			}
		}
	}
	
	private void actualizaDerechosReclamados(List<DerechosReclamadosExpedientes> expedientesDD) throws BaseException {
		List<DerechosReclamadosExpedientes> dereReclClean = derechosReclamadosExpedientesService.obtenerListDerechosReclamadosExpedientePorExpediente(expedientes.getId()); 
		if(dereReclClean != null && !dereReclClean.isEmpty()) {
			for(DerechosReclamadosExpedientes derRecl: dereReclClean) {
				derechosReclamadosExpedientesService.delete(derRecl.getId());
				
				expedientes = utilsComun.expedienteUltimaModificacion(expedientes,FechaUtils.fechaYHoraActualDate(),FechaUtils.fechaYHoraActualDate(),usuario.getLogin(),usuario.getLogin());
			}
		}		
		if(expedientesDD != null && !expedientesDD.isEmpty()) {
			for(DerechosReclamadosExpedientes derRecl: expedientesDD) {
				derRecl.setExpediente(expedientes);
				derechosReclamadosExpedientesService.guardar(derRecl);
				
				expedientes = utilsComun.expedienteUltimaModificacion(expedientes,derRecl.getFechaModificacion(),derRecl.getFechaCreacion(),derRecl.getUsuModificacion(),derRecl.getUsuCreacion());
			}
		}
	}

	public String accionesSituacionFinal() {
		JsfUtils.setSessionAttribute(Constantes.ULTIMO_EXPEDIENTE_FINALIZADO, expedientes.getNumExpediente());
		ContextoVolver contexto = volverBean.crearContexto(ListadoNavegaciones.LISTADO_EXPEDIENTES.getRegla());
		contexto.put("esExpedienteFinal", true);
		navegacionBean.setTxtMigaPan(Constantes.LISTADO_EXPEDIENTES);
		return ListadoNavegaciones.LISTADO_EXPEDIENTES.getRegla();
	}
	
	public void recargarSituacion() {
		//Recargamos solo la situación para no perder el expediente que ya tenemos cargado.
		final Expedientes exp = this.expedientesService.obtener(this.expedientes.getId());
		this.expedientes.setValorSituacionExpediente(exp.getValorSituacionExpediente());
		
		this.cargarListaSituacionEdicionConsulta();
		this.cargaComboTipoSituacion();
	}
	
	//--------------------------------
	
	private List<MateriasExpedientes> creaListMaterias(){
		List<MateriasExpedientes> mat= null;
		if(this.selectedMaterias != null && !this.selectedMaterias.isEmpty()) {
			mat= new ArrayList<>();
			MateriasExpedientes matexp;
			for(MateriaTipoExpediente vd: this.selectedMaterias) {
				matexp = new MateriasExpedientes();
				matexp.setMateriaTipoExpediente(vd);
				mat.add(matexp);
			}
		}
		return mat;
	}

	private List<MateriaTipoExpediente> returnListMaterias(List<MateriasExpedientes> lista){
		List<MateriaTipoExpediente> valores = null;
		if(lista != null && !lista.isEmpty()) {
			valores = new ArrayList<>();
			for(MateriasExpedientes reg: lista){
				valores.add(reg.getMateriaTipoExpediente());
			}
		}
		return valores;
	}
	
	private void actualizaMaterias(List<MateriasExpedientes> expedientesMaterias) throws BaseException {
		List<MateriasExpedientes> materiasClean = materiasExpedientesService.obtenerListMateriasExpedientesPorExpediente(expedientes.getId());
		if(materiasClean != null && !materiasClean.isEmpty()) {
			for(MateriasExpedientes mat: materiasClean) {
				materiasExpedientesService.delete(mat.getId());
				
				expedientes = utilsComun.expedienteUltimaModificacion(expedientes,FechaUtils.fechaYHoraActualDate(),FechaUtils.fechaYHoraActualDate(),usuario.getLogin(),usuario.getLogin());
			}
		}		
		if(expedientesMaterias != null && !expedientesMaterias.isEmpty()) {
			for(MateriasExpedientes mat: expedientesMaterias) {
				mat.setExpediente(expedientes);
				materiasExpedientesService.guardar(mat);
				
				expedientes = utilsComun.expedienteUltimaModificacion(expedientes,mat.getFechaModificacion(),mat.getFechaCreacion(),mat.getUsuModificacion(),mat.getUsuCreacion());
			}
		}
	}
	
	//--------------------------------------
	
	private void mensajeTipoXPCActualizado() {
		final boolean esXPC = expedientes.getValorTipoExpediente().getCodigo().equals("XPC");
		
		FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO,new FacesMessage(FacesMessage.SEVERITY_INFO, "",
				(esXPC? MENSAJEENTRADA : MENSAJEEXPEDIENTES)
						+ " " + expedientes.getNumExpediente() + " " + getMessage(ACTUALIZADOCORRECTAMENTE)));
	}

	//--------------------------------------
	
	public String onConsultarByForm(Long idExp) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, ((Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS))+1);
		JsfUtils.setFlashAttribute(EDITABLE, false);
		JsfUtils.setFlashAttribute(IDEXPSESSION, idExp);
		JsfUtils.setSessionAttribute(EDITABLE, false);
		if(Boolean.TRUE.equals(JsfUtils.getSessionAttribute(Constantes.VUELTAS_CONTROLPLAZOS))) {
			ContextoVolver v = volverBean.crearContexto(ListadoNavegaciones.LISTADO_CONTROL_PLAZOS.getRegla(), VUELTACONTROLPLAZOS);
			v.put("idControlPlazos", JsfUtils.getSessionAttribute(Constantes.IDPLAZOSELECCIONADO));
			v.put(EDITABLE, this.getFormEditable());
		}else if(Boolean.TRUE.equals(JsfUtils.getSessionAttribute(Constantes.VUELTA_PERSONAS))) {
			ContextoVolver v = volverBean.crearContexto(ListadoNavegaciones.FORM_PERSONAS.getRegla(), DatosPersonasBean.VOLVERPERSONAS);
			v.put(IDPERSONA, JsfUtils.getSessionAttribute(Constantes.IDPERSONASELECCIONADA));
			v.put(EDITABLE, this.getFormEditable());
		}else if(Boolean.TRUE.equals(JsfUtils.getSessionAttribute(Constantes.VUELTA_EXPRELACIONADOS))) {
			ContextoVolver v = volverBean.crearContexto(ListadoNavegaciones.FORM_EXPEDIENTES.getRegla(), DatosExpedientesRelacionadosBean.VOLVERFORMEXPRELACIONADOS);
			v.put(IDEXPSESSION, JsfUtils.getSessionAttribute(Constantes.IDEXPEDIENTERELACIONADOSELECCIONADO));
			v.put(EDITABLE, this.getFormEditable());
		}
		Expedientes expedientesCons = expedientesService.obtener(idExp);
		navegacionBean.setTxtMigaPan(Constantes.CONSULTA_EXPEDIENTE+expedientesCons.getMigaDePan()+navegacionBean.expedientesRelacionadosMotivoPsan(expedientesCons.getId()));
		return ListadoNavegaciones.FORM_EXPEDIENTES.getRegla();
	}
	
	public void abrirDialogoSelecTipoExpediente() {
		selectedValorDomTipoExpedienteId = null;
		ValoresDominio valorDominioTipExpPsan = valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_TIPO_EXPEDIENTE, Constantes.PSAN);
		listaValoresDomTiposExpedientesPosibles = valoresDominioService.findValoresTipoExpedienteNoActualNoParam(expedientes.getValorTipoExpediente().getId(),valorDominioTipExpPsan.getId());
		
		PrimeFaces.current().ajax().update("formFormularioExpedientes:tabViewPestanasExpediente:dialogSelecTipoExpediente");
		PrimeFaces.current().ajax().update("formFormularioExpedientes:tabViewPestanasExpediente:comboTiposExpedientes");
		PrimeFaces.current().executeScript("PF('dialogSelecTipoExpediente').show();");
	}
	
	@Transactional(TxType.REQUIRED)
	public String cambiarTipoExpediente() {
		String vueltaListado = "";
		Date fechaLimite = null;
		boolean puedoCambiarTipoExpediente = validacionesPuedoCambiarTipoExpediente();
		boolean validacionesGuardar = validacionesGuardar();
		
		if(puedoCambiarTipoExpediente && validacionesGuardar) {
			guardarDatosGenerales();
			actualizaSituacion();
			
			try {
				String motivoCierre = "Tarea finalizada automáticamente al calificar el expediente.";
				List<TareasExpediente> tareasExpedientes = tareasExpedienteService.findByExpedienteIdAndActiva(expedientes.getId());			
				for(TareasExpediente tarea : tareasExpedientes){
					tareasExpedienteService.cerrarTareaInmediatamente(tarea, usuario, motivoCierre);
				}			
			
				String numeroExpediente = seriesService.nextNumeroSerie(selectedValorDomTipoExpedienteId, expedientes.getFechaEntrada());
				ValoresDominio valorDominioTipoExpediente = valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_TIPO_EXPEDIENTE,selectedValorDomTipoExpedienteId);
				ValoresDominio valorDominioSituacion = valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_SIT, Constantes.PAP);
				
				expedientes.setNumExpediente(numeroExpediente);			
				expedientes.setValorTipoExpediente(valorDominioTipoExpediente);
				expedientes.setValorSituacionExpediente(valorDominioSituacion);
				
				saveCfgTipoExpedienteSeguimientos();
				
				expedientes = expedientesService.guardar(expedientes);			
				JsfUtils.setSessionAttribute(EXPEDIENTEFORMULARIO, expedientes);		
				
				fechaLimite = utilsComun.generarPlazoExpdte(null, null, expedientes.getFechaEntrada(), Constantes.COD_VAL_DOM_ACU, Constantes.COD_VAL_DOM_DN, 0, "A", expedientes);
				utilsComun.generarPlazoExpdte(null, null, fechaLimite, Constantes.COD_VAL_DOM_RES, Constantes.COD_VAL_DOM_DN, 0, "A", expedientes);
				
				TareasExpedienteService.AccionTarea accion = tareasExpedienteService.nuevaAccionTareaExpediente(TareasExpedienteService.AccionTarea.ACCION_ALTA, usuario, expedientes.getId());
				tareasExpedienteService.crearTareasAuto(accion);
								
				JsfUtils.setSessionAttribute(Constantes.ULTIMO_EXPEDIENTE_CREADO, expedientes.getNumExpediente());
				JsfUtils.setSessionAttribute(Constantes.ULTIMO_TIPO_EXPEDIENTE_CREADO, expedientes.getValorSituacionExpediente().getCodigo());
				
				vueltaListado = ListadoNavegaciones.LISTADO_EXPEDIENTES.getRegla();	
				
				volverBean.limpiarContexto();
			} catch (final BaseException e) {
				FacesMessage message = null;
				if(e.getMessage().contains("serie")) {
					message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(NOEXISTEREGISTROCONFIG));
				}else {
					message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MENSAJEERROR));
				}
				PrimeFaces.current().dialog().showMessageDynamic(message);
				log.error("saveExpedientes - " + e.getMessage());
			}
		}		
		return vueltaListado;
	}
	
	public boolean validacionesPuedoCambiarTipoExpediente () {
		boolean valido = true;
		
		if (selectedValorDomTipoExpedienteId.isEmpty()) {
			valido = false;		
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString("campos.obligatorios"));
			PrimeFaces.current().dialog().showMessageDynamic(message);			
		}else if (selectedValorDomTipoExpedienteId.equals(Constantes.RCO) || selectedValorDomTipoExpedienteId.equals(Constantes.RCE)) {
			Date fechaEntrada = expedientes.getFechaEntrada();
			ResponsablesTramitacion responsable = expedientes.getResponsable();
			SujetosObligadosExpedientes sujetosObligadosExpedientes = sujetosObligadosExpedientesService.obtenerSujetosObligadosExpedientePrincipalPorExpediente(expedientes.getId());
			PersonasExpedientes personasExpedientes = personasExpedientesService.obtenerPersonaExpedientePrincipalPorExpediente(expedientes.getId());
			
			if(fechaEntrada == null || responsable == null
					|| sujetosObligadosExpedientes == null || sujetosObligadosExpedientes.getId() == null 
					|| personasExpedientes == null || personasExpedientes.getId() == null) {
				valido = false;		
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Faltan datos obligatorios para la creación del expediente");
				PrimeFaces.current().dialog().showMessageDynamic(message);
			}
		}		
		return valido;
	}
	
	public void abrirBuscadorExpedientes() {
		limpiarFiltroBuscadorExpedientes();
		
		lazyModelExpedientesBuscador = new LazyDataModelByQueryService<>(Expedientes.class,expedientesService);
		lazyModelExpedientesBuscador.setPreproceso((a, b, c, filters) -> {
			if (numeroExpedienteFiltro != null && !numeroExpedienteFiltro.isEmpty()){
				filters.put("numExpediente", new MyFilterMeta(numeroExpedienteFiltro));				
			}
			if (fechaEntradaFiltro != null) {
				filters.put("fechaEntrada", new MyFilterMeta(fechaEntradaFiltro));
			}
			if (nombreExpedienteFiltro != null && !nombreExpedienteFiltro.isEmpty()){
				filters.put("nombreExpediente", new MyFilterMeta(nombreExpedienteFiltro));				
			}
				
			filters.put("#notID", new MyFilterMeta(expedientes.getId()));
		});
						
		PrimeFaces.current().ajax().update("dialogBuscadorExpedientes");
		PrimeFaces.current().executeScript("PF('dialogBuscadorExpedientes').show();");
	}
	
	public void limpiarFiltroBuscadorExpedientes() {
		numeroExpedienteFiltro="";
		nombreExpedienteFiltro = "";
		fechaEntradaFiltro=null;
	}
	
	public void recargaMotivosInadmision() {
		this.habilitarComboMotivoInad = false;
		
		if(this.selectedTipoAdmisionId != null)
		{
			ValoresDominio valDom = valoresDominioService.obtener(this.selectedTipoAdmisionId);
			if(valDom.getCodigo().equals(Constantes.COD_VAL_DOM_INA))
			{
				this.habilitarComboMotivoInad = true;	
			}else {
				this.selectedNuevoMotivoInadmisionId = null;
			}
		}else {
			this.selectedNuevoMotivoInadmisionId = null;
		}
	}
	
	public void activarEdicionMotivoExp()
	{
		this.habilitarMotivoExp = false;
		PrimeFaces.current().ajax().update("formFormularioExpedientes:tabViewPestanasExpediente:motivoExpediente");
		actualizarBotonEditarMotivoExp();
		
	}
	
	
	public void actualizarBotonEditarMotivoExp()
	{
		if(this.expedientes != null && this.expedientes.getId()!= null)	{
			Boolean edit = (Boolean) JsfUtils.getFlashAttribute(EDITABLE);
			if(edit == null){
				JsfUtils.setFlashAttribute(EDITABLE, true);
				edit = true;
			}
			this.habilitarBotonEdicionMotivoExp = edit && (expedientes.getMotivo() != null) && this.permisoEditMotivoExp && this.habilitarMotivoExp;
		}
		PrimeFaces.current().ajax().update("formFormularioExpedientes:tabViewPestanasExpediente");
	}
	
	public void actualizarMotivoExpediente()
	{
		this.auditoriaModifMotivoExp = "";
		
		/**SI EL MOTIVO DE EXPEDIENTE SE HA MODIFICADO ENTONCES GUARDAMOS LOS DATOS DE USUARIO, FECHA Y HORA.**/
		
		if(this.expedientes != null && this.expedientes.getId()!= null)
		{
			Expedientes expGuardado = expedientesService.obtener(this.expedientes.getId());
			
			if(this.expedientes.getMotivo() != null && !this.expedientes.getMotivo().isEmpty())
			{
				this.habilitarMotivoExp = true;
				actualizarBotonEditarMotivoExp();
				
				if(expGuardado != null &&  (expGuardado.getMotivo() != null && !expGuardado.getMotivo().equals(this.expedientes.getMotivo())))
				{
					actualizaDatosAuditoriaMotivo();
				}
				else if(expGuardado != null && expGuardado.getUsuarioModifMotivo() != null && expGuardado.getFechaHoraModifMotivo() != null)
				{
					this.auditoriaModifMotivoExp = mensajesProperties.getString(ULTIMAMODIFICACIONMOTIVO) + expGuardado.getUsuarioModifMotivo().getNombreAp() + " " + FechaUtils.dateToStringFechaYHora(expGuardado.getFechaHoraModifMotivo());	
				}else {
					actualizaDatosAuditoriaMotivo();
				}
			}
		}
		
		PrimeFaces.current().ajax().update("formFormularioExpedientes:tabViewPestanasExpediente:motivoExpediente");
	}
	
	public void actualizaDatosAuditoriaMotivo()
	{
		this.expedientes.setUsuarioModifMotivo(usuario);
		this.expedientes.setFechaHoraModifMotivo(FechaUtils.fechaYHoraActualDate());
		this.auditoriaModifMotivoExp = mensajesProperties.getString(ULTIMAMODIFICACIONMOTIVO) + usuario.getNombreAp()  + " " +  FechaUtils.dateToStringFechaYHora(FechaUtils.fechaYHoraActualDate());
	}
	
	@Transactional(TxType.REQUIRED)
	public String seleccionarExpVincularDocumentos (Expedientes expedienteSeleccionado) {
		String vueltaListado = "";
		boolean validacionesGuardar = validacionesGuardar();
		
		if(validacionesGuardar) {
			guardarDatosGenerales();
			actualizaSituacion();
			
			try {
				String motivoCierre = "Tarea finalizada automáticamente al calificar el expediente.";
				List<TareasExpediente> tareasExpedientes = tareasExpedienteService.findByExpedienteIdAndActiva(expedientes.getId());			
				for(TareasExpediente tarea : tareasExpedientes){
					tareasExpedienteService.cerrarTareaInmediatamente(tarea, usuario, motivoCierre);
				}
				
				List<TramiteExpediente> tramitesExpedientes = tramiteExpedienteService.findAllTramitesExpedientesByExp(expedientes.getId());
				for(int i = 0; i < tramitesExpedientes.size(); i++) {
					tramitesExpedientes.get(i).setExpediente(expedienteSeleccionado);
					tramitesExpedientes.get(i).setFinalizado(false);
					tramitesExpedientes.get(i).setResponsable(expedienteSeleccionado.getResponsable());
					tramitesExpedientes.get(i).setDescripcion(tramitesExpedientes.get(i).getDescripcion() + " - " + expedientes.getNumExpediente());
					tramiteExpedienteService.guardar(tramitesExpedientes.get(i));
					
					TareasExpedienteService.AccionTarea accion = tareasExpedienteService.nuevaAccionTareaTramite(TareasExpedienteService.AccionTarea.ACCION_ALTA, usuario, expedienteSeleccionado.getId(),tramitesExpedientes.get(i).getId());
					tareasExpedienteService.crearTareasAuto(accion);
				}
				
				List<DetalleExpdteTram> detalleExpdteTrams = detalleExpdteTramService.findAllDetalleTramExpByExp(expedientes.getId());
				for(int i = 0; i < detalleExpdteTrams.size(); i++) {
					detalleExpdteTrams.get(i).setExpediente(expedienteSeleccionado);
					detalleExpdteTramService.guardar(detalleExpdteTrams.get(i));
				}
				
				seleccionarExpVincularDocumentosExpedientes(expedienteSeleccionado);
				
				List<ObservacionesExpedientes> observacionesExpedientes = observacionesExpedientesService.obtenerObservacionesExpedientesPorExpediente(expedientes.getId());
				for(int i = 0; i < observacionesExpedientes.size(); i++) {
					observacionesExpedientes.get(i).setExpediente(expedienteSeleccionado);
					observacionesExpedientesService.guardar(observacionesExpedientes.get(i));
				}
				
				ExpedientesRelacion expedientesRelacion = new ExpedientesRelacion();
				ValoresDominio valorDominioMotivoRelacion = valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_MOT_REL, Constantes.C_EDOC);
				expedientesRelacion.setExpedienteOrigen(expedientes);
				expedientesRelacion.setExpedienteRelacionado(expedienteSeleccionado);
				expedientesRelacion.setMotivo(valorDominioMotivoRelacion);
				expedientesRelacionService.guardar(expedientesRelacion);				
				
				ValoresDominio valorDominioSituacion = valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_SIT, Constantes.CA);
				expedientes.setValorSituacionExpediente(valorDominioSituacion);		
				expedientes = expedientesService.guardar(expedientes);
				
				
				JsfUtils.setSessionAttribute(Constantes.ULTIMO_EXPEDIENTE_FINALIZADO, expedientes.getNumExpediente());
				ContextoVolver contexto = volverBean.crearContexto(ListadoNavegaciones.LISTADO_EXPEDIENTES.getRegla());
				contexto.put("esExpedienteFinal", true);
				vueltaListado = ListadoNavegaciones.LISTADO_EXPEDIENTES.getRegla();
				
				volverBean.limpiarContexto();
			} catch (BaseException e) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MENSAJEERROR));
				PrimeFaces.current().dialog().showMessageDynamic(message);
				log.error(e.getMessage());
			}
		}		
		return vueltaListado;
	}
	
	private void seleccionarExpVincularDocumentosExpedientes (Expedientes expedienteSeleccionado) throws BaseException {
		final List<DocumentosExpedientes> docsExp = documentosExpedientesService.findDocumentosByExpedienteId(expedientes.getId());
		for(var docExp : docsExp) {					
			docExp.setExpediente(expedienteSeleccionado);
			agrupacionesExpedientesService.findByExpediente(expedientes.getId());
			documentosExpedientesService.guardar(docExp);
		}
		
		//No debería hacer falta reasigar el expediente a las agrupaciones, AgrExp ya tiene el campo trámite, que apunta al expediente nuevo
		//Mientras esté este campo hay que mantenerlo. Si se elimina, se puede quitar este bloque
		final List<AgrupacionesExpedientes> agrsExp = agrupacionesExpedientesService.findByExpedienteDeExpediente(expedientes.getId());
		for(var agrExp : agrsExp) {					
			agrExp.setExpediente(expedienteSeleccionado);
			agrupacionesExpedientesService.guardar(agrExp);
			agrExp.setOrden((agrupacionesExpedientesService.getUltimoOrden(expedienteSeleccionado.getId(), agrExp.getCategoria().getId()))+1L);
			agrupacionesExpedientesService.guardar(agrExp);


		}	
	}
	
	private void actualizarDescripcionExpediente () {
		this.auditoriaModifDescripcionExp = "";
		
		/**SI LA DESCRIPCION DE EXPEDIENTE SE HA MODIFICADO ENTONCES GUARDAMOS LOS DATOS DE USUARIO, FECHA Y HORA.**/
		if(this.expedientes != null && this.expedientes.getId()!= null) {
			Expedientes expGuardado = expedientesService.obtener(this.expedientes.getId());
			
			if(this.expedientes.getDescripcion() != null && !this.expedientes.getDescripcion().isEmpty())	{
				this.habilitarDescripcionExp = true;
				actualizarBotonEditarDescripcionExp();
				
				if(expGuardado != null &&  (expGuardado.getDescripcion() != null && !expGuardado.getDescripcion().equals(this.expedientes.getDescripcion()))){
					actualizaDatosAuditoriaDescripcion();
				}
				else if(expGuardado != null && expGuardado.getUsuarioModifDescripcion() != null && expGuardado.getFechaHoraModifDescr() != null){
					this.auditoriaModifDescripcionExp = mensajesProperties.getString(ULTIMAMODIFICACIONMOTIVO) + expGuardado.getUsuarioModifDescripcion().getNombreAp() + " " + FechaUtils.dateToStringFechaYHora(expGuardado.getFechaHoraModifDescr());	
				}else {
					actualizaDatosAuditoriaDescripcion();
				}
			}
		}		
		PrimeFaces.current().ajax().update("formFormularioExpedientes:tabViewPestanasExpediente:descripcionExpediente");
	}
	
	private void actualizarBotonEditarDescripcionExp() {
		if(this.expedientes != null && this.expedientes.getId()!= null)	{
			Boolean edit = (Boolean) JsfUtils.getFlashAttribute(EDITABLE);
			if(edit == null){
				JsfUtils.setFlashAttribute(EDITABLE, true);
				edit = true;
			}
			this.habilitarBotonEdicionDescripcionExp = edit && (expedientes.getDescripcion() != null) && this.permisoEditDescripcionExp && this.habilitarDescripcionExp;
		}
		PrimeFaces.current().ajax().update("formFormularioExpedientes:tabViewPestanasExpediente");
	}
	
	private void actualizaDatosAuditoriaDescripcion() {
		this.expedientes.setUsuarioModifDescripcion(usuario);
		this.expedientes.setFechaHoraModifDescr(FechaUtils.fechaYHoraActualDate());
		this.auditoriaModifDescripcionExp = mensajesProperties.getString(ULTIMAMODIFICACIONMOTIVO) + usuario.getNombreAp()  + " " +  FechaUtils.dateToStringFechaYHora(FechaUtils.fechaYHoraActualDate());
	}
	
	public void activarEdicionDescripcionExp() {
		this.habilitarDescripcionExp = false;
		PrimeFaces.current().ajax().update("formFormularioExpedientes:tabViewPestanasExpediente:descripcionExpediente");
		actualizarBotonEditarDescripcionExp();		
	}
	
	public void valorFechaPrescripcion () {
		
		if(selectedGravedadId != null && expedientes.getFechaInfraccion() != null) {
			ValoresDominio valorGravedad = valoresDominioService.obtener(this.selectedGravedadId);
			
			if(valorGravedad.getCodigo().equals(Constantes.GRAV_LEVE)) {
				expedientes.setFechaPrescripcion(FechaUtils.sumarDiasAFecha(expedientes.getFechaInfraccion(), 365));
			}else if(valorGravedad.getCodigo().equals(Constantes.GRAV_GRAVE)) {
				expedientes.setFechaPrescripcion(FechaUtils.sumarDiasAFecha(expedientes.getFechaInfraccion(), 731));
			}else if(valorGravedad.getCodigo().equals(Constantes.GRAV_MUYGRAVE)) {
				expedientes.setFechaPrescripcion(FechaUtils.sumarDiasAFecha(expedientes.getFechaInfraccion(), 1096));
			}
		}else if(selectedGravedadId == null || expedientes.getFechaInfraccion() == null) {
			expedientes.setFechaPrescripcion(null);
		}
		PrimeFaces.current().ajax().update("formFormularioExpedientes:tabViewPestanasExpediente:fechaPrescripcion");
	}
	
	private void saveCfgTipoExpedienteSeguimientos () {
		CfgTipoExpediente cfgTipoExp = cfgTipoExpedienteService.obtenerCfgTipoExpedientePorValorTipoExpediente(expedientes.getValorTipoExpediente().getId());
		if (cfgTipoExp.getTipoTramiteSeg1() != null) {
			expedientes.setDescSeguimiento1(cfgTipoExp.getTipoTramiteSeg1().getDescripcionAbrev()+":");
		}

		if (cfgTipoExp.getTipoTramiteSeg2() != null) {
			expedientes.setDescSeguimiento2(cfgTipoExp.getTipoTramiteSeg2().getDescripcionAbrev()+":");
		}

		if (cfgTipoExp.getTipoTramiteSeg3() != null) {
			expedientes.setDescSeguimiento3(cfgTipoExp.getTipoTramiteSeg3().getDescripcionAbrev()+":");
		}
	}
	
	private void comprobarPermisosCamposPestanya () {
		permisoRcoCamposPestanya = false;
		permisoRceCamposPestanya = false;
		permisoPsanCamposPestanya = false;
		permisoXpcCamposPestanya = false;
		
		if(expedientes.getValorTipoExpediente().getCodigo().equals(Constantes.RCO) && listaCodigoPermisos.contains(Constantes.PERMISO_RCO_VIEW_CAMPOSESP)) {
			permisoRcoCamposPestanya = true;
		}else if(expedientes.getValorTipoExpediente().getCodigo().equals(Constantes.RCE) && listaCodigoPermisos.contains(Constantes.PERMISO_RCE_VIEW_CAMPOSESP)) {
			permisoRceCamposPestanya = true;
		}else if(expedientes.getValorTipoExpediente().getCodigo().equals(Constantes.PSAN) && listaCodigoPermisos.contains(Constantes.PERMISO_PSAN_VIEW_CAMPOSESP)) {
			permisoPsanCamposPestanya = true;
		}else if(expedientes.getValorTipoExpediente().getCodigo().equals(Constantes.XPC) && listaCodigoPermisos.contains(Constantes.PERMISO_XPC_VIEW_CAMPOSESP)) {
			permisoXpcCamposPestanya = true;
		}
	}
	
	private void comprobarPermisosEditarResponsableExpediente () {
		permisoRcoEditRespExp = false;
		permisoRceEditRespExp = false;
		permisoPsanEditRespExp = false;
		permisoXpcEditRespExp = false;
		
		if(expedientes.getValorTipoExpediente().getCodigo().equals(Constantes.RCO) && listaCodigoPermisos.contains(Constantes.PERMISO_RCO_EDIT_RESPEXP)) {
			permisoRcoEditRespExp = true;
		}else if(expedientes.getValorTipoExpediente().getCodigo().equals(Constantes.RCE) && listaCodigoPermisos.contains(Constantes.PERMISO_RCE_EDIT_RESPEXP)) {
			permisoRceEditRespExp = true;
		}else if(expedientes.getValorTipoExpediente().getCodigo().equals(Constantes.PSAN) && listaCodigoPermisos.contains(Constantes.PERMISO_PSAN_EDIT_RESPEXP)) {
			permisoPsanEditRespExp = true;
		}else if(expedientes.getValorTipoExpediente().getCodigo().equals(Constantes.XPC) && listaCodigoPermisos.contains(Constantes.PERMISO_XPC_EDIT_RESPEXP)) {
			permisoXpcEditRespExp = true;
		}
	}
	
}
