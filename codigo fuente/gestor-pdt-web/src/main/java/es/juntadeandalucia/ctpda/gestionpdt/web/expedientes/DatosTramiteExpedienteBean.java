package es.juntadeandalucia.ctpda.gestionpdt.web.expedientes;

import java.io.Serializable;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.primefaces.PrimeFaces;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.AgrupacionesExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.ArticuloAfectadoResolucion;
import es.juntadeandalucia.ctpda.gestionpdt.model.ArticulosAfectadosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.CfgAutoSituacion;
import es.juntadeandalucia.ctpda.gestionpdt.model.CfgExpedienteSubtramite;
import es.juntadeandalucia.ctpda.gestionpdt.model.CfgExpedienteTramite;
import es.juntadeandalucia.ctpda.gestionpdt.model.CfgMetadatosTram;
import es.juntadeandalucia.ctpda.gestionpdt.model.CfgPlazosAut;
import es.juntadeandalucia.ctpda.gestionpdt.model.CfgPlazosExpdte;
import es.juntadeandalucia.ctpda.gestionpdt.model.CfgTareas;
import es.juntadeandalucia.ctpda.gestionpdt.model.CfgTipoExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.DerechoReclamadoResolucion;
import es.juntadeandalucia.ctpda.gestionpdt.model.DerechosReclamadosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.DetalleExpdteTram;
import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentoResolucion;
import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentosExpedientesTramites;
import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.ExpedientesRelacion;
import es.juntadeandalucia.ctpda.gestionpdt.model.FirmasTramiteMaestra;
import es.juntadeandalucia.ctpda.gestionpdt.model.NotificacionesTramiteMaestra;
import es.juntadeandalucia.ctpda.gestionpdt.model.ObservacionesExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.PersonaDTO;
import es.juntadeandalucia.ctpda.gestionpdt.model.Personas;
import es.juntadeandalucia.ctpda.gestionpdt.model.PersonasExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.PlazosExpdte;
import es.juntadeandalucia.ctpda.gestionpdt.model.Resolucion;
import es.juntadeandalucia.ctpda.gestionpdt.model.ResolucionExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.ResolucionPersona;
import es.juntadeandalucia.ctpda.gestionpdt.model.ResolucionSujetoObligado;
import es.juntadeandalucia.ctpda.gestionpdt.model.ResponsablesTramitacion;
import es.juntadeandalucia.ctpda.gestionpdt.model.SujetosObligados;
import es.juntadeandalucia.ctpda.gestionpdt.model.SujetosObligadosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.TareasExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.TipoTramite;
import es.juntadeandalucia.ctpda.gestionpdt.model.TramiteExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.Usuario;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.model.enums.Origenes;
import es.juntadeandalucia.ctpda.gestionpdt.service.AgrupacionesExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ArticuloAfectadoResolucionService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ArticulosAfectadosExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.CfgAutoSituacionService;
import es.juntadeandalucia.ctpda.gestionpdt.service.CfgExpedienteSubtramiteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.CfgExpedienteTramiteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.CfgMetadatosTramService;
import es.juntadeandalucia.ctpda.gestionpdt.service.CfgPlazosAutService;
import es.juntadeandalucia.ctpda.gestionpdt.service.CfgPlazosExpdteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.CfgTareasService;
import es.juntadeandalucia.ctpda.gestionpdt.service.CfgTipoExpedienteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.DerechoReclamadoResolucionService;
import es.juntadeandalucia.ctpda.gestionpdt.service.DerechosReclamadosExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.DetalleExpdteTramService;
import es.juntadeandalucia.ctpda.gestionpdt.service.DocumentoResolucionService;
import es.juntadeandalucia.ctpda.gestionpdt.service.DocumentosExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.DocumentosExpedientesTramitesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ExpedientesRelacionService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.FirmasTramiteMaestraService;
import es.juntadeandalucia.ctpda.gestionpdt.service.NotificacionesTramiteMaestraService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ObservacionesExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.PersonasExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.PersonasService;
import es.juntadeandalucia.ctpda.gestionpdt.service.PlazosExpdteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ResolucionExpedienteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ResolucionPersonaService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ResolucionService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ResolucionSujetoObligadoService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ResponsablesTramitacionService;
import es.juntadeandalucia.ctpda.gestionpdt.service.SeriesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.SituacionesExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.SujetosObligadosExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.SujetosObligadosService;
import es.juntadeandalucia.ctpda.gestionpdt.service.TareasExpedienteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.TipoTramiteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.TramiteExpedienteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.UsuarioService;
import es.juntadeandalucia.ctpda.gestionpdt.service.UsuariosResponsablesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ValoresDominioService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.ValidacionException;
import es.juntadeandalucia.ctpda.gestionpdt.util.FechaUtils;
import es.juntadeandalucia.ctpda.gestionpdt.util.StringUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.SesionBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.Constantes;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.JsfUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.LazyDataModelByQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.MyFilterMeta;
import es.juntadeandalucia.ctpda.gestionpdt.web.menu.NavegacionBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.menu.NavegacionBean.ListadoNavegaciones;
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
public class DatosTramiteExpedienteBean extends BaseBean implements Serializable {

	private static final String IDEXPSESSION = "idExp";
	private static final String MENSAJESFORMULARIO = "messagesFormTramiteExpediente";
	private static final String CAMPOSOBLIGATORIOS = "campos.obligatorios";
	private static final String NUMRESOLNOEXISTE = "num.resol.no.existe";
	private static final String CAMPOSOBLIGATORIOSGUARDAR = "campos.obligatorios.guardar";
	private static final String TRAMITEMSJ = "Trámite ";
	private static final String SUBTRAMITEMSJ = "Subtrámite ";
	private static final String ELIMINADOCORRECTAMENTE = "eliminado.correctamente";
	private static final String FINALIZADOCORRECTAMENTE = "finalizado.correctamente";
	private static final String ACTUALIZADOCORRECTAMENTE = "actualizado.correctamente";
	private static final String EXPEDIENTEFORMULARIO = "expedienteFormulario";
	private static final String EXPEDIENTEDG = "expedientesDG";
	private static final String EXPEDIENTE = "expediente";
	private static final String FORMFORMULARIOEXPEDIENTES = "formFormularioExpedientes";
	private static final String MENSAJEERROR = "error";
	private static final String TRAMITE = "tramite";
	private static final String RESOLUCIONCREADA = "resolucion.creada.creada";
	private static final String RESOLUCIONASOCIADA = "resolucion.asociada.creada";
	private static final String TRAMITELIBRE = "tramiteLibre";
	private static final String EVENTO = "evento";
	private static final String SINEVENTO = "sinevento";	
	private static final String ACTIVIDAD = "actividad";
	private static final String ELIMINAR = "eliminar";
	private static final String FINALIZAR = "finalizar";
	private static final String INTERESADOFINALIZARTRAMITE = "interesado.finalizar.tramite";
	private static final String FECHAENVIOFINALIZARTRAMITE = "fecha.envio.finalizar.tramite";
	private static final String DATOSNOINFORMADOS = "no.finalizar.sin.datos.informados";
	private static final String MOTIVOINADMISIONOBLIG = "motivo.inadmision.oblig";
	private static final String FECHAFIRMAFINALIZARTRAMITE = "fecha.firmar.finalizar.tramite";
	private static final String FIRMANTEFINALIZARTRAMITE = "firmante.finalizar.tramite";
	private static final String FECHARESPUESTAFINALIZARTRAMITE = "fecha.respuesta.finalizar.tramite";
	private static final String DOCUMENTOACTIVOTRAMITE = "documento.activo.tramite";
	private static final String DOCUMENTOACTIVOSUBTRAMITE = "documento.activo.subtramite";
	private static final String FECHARESOLOBLIG = "fecha.resol.oblig";
	private static final String FECHARESPPOSTERIORFECHAENV = "fecha.respuesta.post.fecha.envio.tramite";
	private static final String FECHANOTIFPOSTERIORFECHAENV = "fecha.notif.post.fecha.envio.notif";
	private static final String FECHAENVIONOANTERIORFECHAENTRADA = "fecha.envio.no.anterior.fecha.entrada";
	private static final String FECHAACREDITNOANTERIORFECHANOTIF = "fecha.acredit.no.anterior.fecha.notif";
	private static final String FECHARESPNOANTERIORFECHANOTIF = "fecha.resp.no.anterior.fecha.notif";
	private static final String FECHANOTIFNOANTERIORFECHAENV = "fecha.notif.no.anterior.fecha.envio";
	private static final String FECHAFIRMANOANTERIORFECHAENV = "fecha.firma.no.anterior.fecha.envio";
	private static final String RESULTNOTIFICACIONNOINFORMADO = "resultado.notificacion.noinformado";
	private static final String GUARDADOCORRECTAMENTE = "anyadido.correctamente";
	private static final String NOEXISTEREGISTROCONFIG = "no.existe.registro.config";
	private static final String FECHARESOLNOSUPFECHAACTUAL = "fecha.resol.no.superior.actual";
	private static final String FECHAACUERDONOSUPFECHAACTUAL = "fecha.acuerdo.no.superior.actual";
	private static final String FECHAACREDNOSUPFECHAACTUAL = "fecha.acred.no.superior.actual";	
	private static final String FECHAENVIONOSUPFECHAACTUAL = "fecha.envio.no.superior.actual";
	private static final String FECHANOTIFNOSUPFECHAACTUAL = "fecha.notif.no.superior.actual";
	private static final String FECHAINFORMENOSUPFECHAACTUAL = "fecha.informe.no.superior.actual";
	private static final String FECHAFIRMANOSUPFECHAACTUAL = "fecha.firma.no.superior.actual";	
	private static final String FECHARESPNOSUPFECHAACTUAL = "fecha.resp.no.superior.actual";
	private static final String FECHAENTRADANOSUPFECHAACTUAL = "fecha.entrada.no.superior.actual";
	private static final String FECHAREGISTRONOSUPFECHAACTUAL = "fecha.registro.no.superior.actual";
	private static final String FECHAENTSUPERIORIGUALFECHAREG = "fecha.entrada.superior.igual.fecha.registro";
	private static final String NUMRESOLOBLIG = "num.resol.oblig";
	private static final String SENTIDORESOLOBLIG = "sentido.resol.oblig";
	private static final String TIPORESOLOBLIG = "tipo.resol.oblig";
	private static final String INDICADOREXTRACTOMARCADO = "indicador.extracto.marcado";
	private static final String INDICADORANTECEDENTESMARCADO = "indicador.antecedentes.marcado";
	private static final String COMANDOSHOW = "').show();";
	
	
	
	private static final String CREARRESOLUCION = "crearResolucion";
	private static final String VINCULARRESOLUCION = "vincularResolucion";
	private static final String FINALIZARRESOLUCION = "finalizarResolucion";

	public static final String ORIGENPESTANYATRAMITES = "pestanyaTramites";
	public static final String ORIGENALTAEXPEDIENTE = "altaExpediente";
	private static final String INICIALIZARCAMPOSPARA = "inicializarCampos para ";
	private static final String FINALIZARCOMPORTAMIENTO14CAMPO = "finalizar.comportamientoc14.elcampo";
	private static final String FINALIZARCOMPORTAMIENTO14VALOR = "finalizar.comportamientoc14.tener.valor";
	private static final String CERRARTAREAS = "cerrar.tareas";
	
	private static final long serialVersionUID = 1L;

	@Autowired
	private VolverBean volverBean;

	@Autowired
	private ComunExpedientesBean comunExpedientesBean;

	@Autowired
	private SeriesService seriesService;
	
	@Autowired
	private CfgAutoSituacionService cfgAutoSituacionService;
	
	@Autowired
	private SituacionesExpedientesService situacionesExpedientesService;
	
	@Getter
	@Setter
	private String mensajeConfirmacionCierreTareas;
	
	@Autowired
	private ObservacionesExpedientesService observacionesExpedientesService;
	
	
	@Getter
	private List<SortMeta> defaultOrdenList;

	@Getter
	@Setter
	private Expedientes expedientes;

	@Getter
	@Setter
	private List<CfgExpedienteTramite> listaTramites;

	@Getter
	@Setter
	private List<CfgExpedienteSubtramite> listaSubTramites;

	@Getter
	@Setter
	private CfgExpedienteTramite cfgExpTramite;

	@Getter
	@Setter
	private CfgExpedienteSubtramite cfgExpSubTramite;
	
	@Getter
	@Setter
	private List<String> listaDescAbrevTramitesExp;

	@Getter
	@Setter
	private List<String> listaCodTipTramTramitesExp;

	@Getter
	@Setter
	private List<TramiteExpediente> listaTramTramitesExp;

	@Getter
	@Setter
	private List<TramiteExpediente> listaSubTramTramitesTrdpd;

	@Getter
	@Setter
	private List<TramiteExpediente> listaSubTramTramitesOtro;

	@Getter
	@Setter
	private Long selectedNuevoTramiteId;

	@Getter
	@Setter
	private Long selectedNuevoSubTramiteId;
	
	@Getter
	@Setter
	private Long selectedNuevoResponsableTramId;
	
	@Getter
	@Setter
	private Long selectedNuevoResponsableSubTramId;
	
	@Getter
	@Setter
	private String numResolVinc;
	
	@Getter
	@Setter
	private Date fechaResolVinc;
	
	@Getter
	@Setter
	private ValoresDominio valorDomSentidoResolVinc;
	
	@Getter
	@Setter
	private ValoresDominio valorDomTipoResolVinc;
	
	@Getter
	@Setter
	private List<ResponsablesTramitacion> listaResponsablesTram;
	@Getter
	@Setter
	private List<ResponsablesTramitacion> listaResponsablesSubTram;

	@Getter
	@Setter
	private TramiteExpediente tramiteExpediente;

	@Getter
	@Setter
	private DetalleExpdteTram detalleExpdteTram;

	@Getter
	@Setter
	private DetalleExpdteTram detalleExpdteSubTram;

	@Autowired
	private CfgExpedienteTramiteService cfgExpedienteTramiteService;

	@Autowired
	private CfgExpedienteSubtramiteService cfgExpedienteSubTramiteService;

	@Autowired
	private TramiteExpedienteService tramiteExpedienteService;
	
	@Autowired
	private NotificacionesTramiteMaestraService notificacionesTramiteMaestraService;
	
	@Autowired
	private FirmasTramiteMaestraService firmasTramiteMaestraService;

	@Autowired
	private SujetosObligadosExpedientesService sujetosObligadosExpedientesService;

	@Autowired
	private PersonasExpedientesService personasExpedientesService;

	@Autowired
	private PersonasService personasService;
	
	@Autowired
	private DocumentosExpedientesService documentosExpedientesService;
	
	@Autowired
	private ResolucionService resolucionService;
	
	@Autowired
	private ResolucionSujetoObligadoService resolucionSujetoObligadoService;
	
	@Autowired
	private ResolucionPersonaService resolucionPersonaService;
	
	@Autowired
	private ResolucionExpedienteService resolucionExpedienteService;
	
	@Autowired
	private ArticuloAfectadoResolucionService articuloAfectadoResolucionService;
	
	@Autowired
	private ArticulosAfectadosExpedientesService articuloAfectadoExpedienteService;
	
	@Autowired
	private DerechoReclamadoResolucionService derechoReclamadoResolucionService;
	
	@Autowired
	private DerechosReclamadosExpedientesService derechosReclamadosExpedientesService;
	
	@Autowired
	private DocumentoResolucionService documentoResolucionService;

	@Autowired
	private CfgPlazosAutService cfgPlazosAutService;
	
	@Autowired
	private TipoTramiteService tipoTramiteService;

	@Getter
	@Setter
	private String tramiteDescAbrev;

	@Getter
	@Setter
	private String codigoTipoInteresado;

	@Getter
	@Setter
	private Boolean permisoNewExpTram;

	@Getter
	@Setter
	private Boolean permisoNewExpTramLib;

	@Getter
	@Setter
	private Boolean permisoNewExpTarea;

	@Getter
	@Setter
	private Boolean permisoNewPlazoExp;

	@Getter
	@Setter
	private Boolean permisoNewExpAct;

	@Getter
	@Setter
	private Boolean permisoNewExpTareaDet;

	@Getter
	@Setter
	private Boolean permisoNewExpPlazoDet;

	@Getter
	@Setter
	private Boolean permisoNewExpFinTram;

	@Getter
	@Setter
	private Boolean permisoDesacExpTram;
	
	@Getter
	@Setter
	private Boolean permisoRehabTramite;

	@Getter
	@Setter
	private Boolean editable;
	


	@Autowired
	private DetalleExpdteTramService detalleExpdteTramService;

	@Autowired
	private AgrupacionesExpedientesService agrupacionesExpedientesService;

	@Getter
	@Setter
	private Long selectedTramite;

	@Autowired
	private ExpedientesService expedientesService;

	@Autowired
	private CfgMetadatosTramService cfgMetadatosTramService;

	@Autowired
	private ValoresDominioService valoresDominioService;

	@Autowired
	private SujetosObligadosService sujetosObligadosService;

	@Autowired
	private DocumentosExpedientesTramitesService documentosExpedientesTramitesService;

	@Getter
	private List<TramiteExpediente> listaTramExpAux;
	
	@Getter
	private List<ValoresDominio> listaValoresDominioTipoAdmision;
	
	@Getter
	private List<ValoresDominio> listaValoresDominioMotivoInadmision;

	@Getter
	private List<ValoresDominio> listaValoresDominioCanalEntrada;

	
	@Getter
	private List<ValoresDominio> listaValoresDominioCanalSalida;
	
	@Getter
	private List<ValoresDominio> listaValoresDominioActoRec;

	@Getter
	private List<ValoresDominio> listaValoresDominioCanalInfEntrada;
	
	@Getter
	private List<ValoresDominio> listaValoresDominioCanalInfSalida;

	@Getter
	private List<ValoresDominio> listaValoresDominioTipoPlazo;
	
	@Getter
	private List<ValoresDominio> listaValoresDominioSentidoResolucion;
	
	@Getter
	private List<ValoresDominio> listaValoresDominioTipoResolucion;
	
	@Getter
	private List<ValoresDominio> listaValoresDominioSerieNumeracion;
    
	@Getter
	private List<ValoresDominio> listaValoresDominioInstructorAPI;
	
	@Getter
	private List<ValoresDominio> listaValoresDominioResultadosNotificacion;
	
	@Getter
	@Setter
	private ValoresDominio valDomCanalSalida;

	@Getter
	@Setter
	private Date fechaInicioTramite;

	@Getter
	@Setter
	private Date fechaInicioSubTramite;

	@Getter
	@Setter
	private Long idExp;

	@Getter
	@Setter
	private Boolean habilitarIdentifInt;

	@Getter
	@Setter
	private Boolean esIdentIntDPD;

	@Getter
	@Setter
	private Boolean esIdentIntPersona;

	@Getter
	@Setter
	private Boolean esIdentIntSujOblig;

	@Getter
	@Setter
	private Boolean esIdentIntAutControl;

	@Getter
	@Setter
	private List<CfgPlazosExpdte> listaTiposPlazosByTipoExpediente;

	@Autowired
	private CfgPlazosExpdteService cfgPlazosExpdteService;

	@Autowired
	private CfgTareasService cfgTareasService;
	
	@Getter
	@Setter
	private Date fechaLimiteAnyadirPlazo;

	@Getter
	@Setter
	private String observacionesAnyadirPlazo;

	@Getter
	@Setter
	private Long selectedTipoPlazoId;

	@Autowired
	private PlazosExpdteService plazosExpdteService;
	
	@Autowired
	private CfgTipoExpedienteService cfgTipoExpedienteService;

	@Autowired
	private ResponsablesTramitacionService responsablesTramitacionService;

	@Getter
	@Setter
	private Boolean permisoSavePlazoExp;

	@Autowired
	private DatosExpedientesBean datosExpedientesBean;

	@Autowired
	private DatosExpedientesDatosGeneralesBean datosExpedientesDatosGeneralesBean;

	@Autowired
	private DatosEvolucionExpedienteBean datosEvolucionExpedienteBean;

	@Autowired
	private UtilsComun utilsComun;
	
	@Autowired
	private UsuarioService usuarioService;
	@Getter @Setter
	private Boolean tieneListaSubTramites=true;
	@Getter
	@Setter
	private String cabeceraDialog;
	
	@Getter @Setter
	private Long selectedNuevoPropuestaApiId;
	
	@Getter
	private List<ValoresDominio> listaValoresDominioPropuestaApi;
	
	@Autowired
	private NavegacionBean navegacionBean;
	
	@Getter
	@Setter
	private List<String> listaCodigoPermisos;
	
	@Autowired
	private ExpedientesRelacionService expedientesRelacionService;
	
	@Autowired
	private TareasExpedienteService tareasExpedienteService;
	
	@Getter
	@Setter
	private String numeroExpedienteFiltroBuscadorExpPsan;
	
	@Getter
	@Setter
	private String nombreExpedienteFiltroBuscadorExpPsan;
	
	@Getter
	@Setter
	private Date fechaEntradaFiltroBuscadorExpPsan;
	
	@Getter
	private LazyDataModelByQueryService<Expedientes> lazyModelExpedientesBuscadorExpPsan;
	@Getter
	private LazyDataModelByQueryService<TramiteExpediente> lazyModelNotificaciones;
	
	@Getter
	private SortMeta defaultOrdenBuscadorExpPsan;

	@Autowired
	private UsuariosResponsablesService usuariosResponsablesService;
	
	@Autowired
	private SesionBean sesionBean;
	
	@Override
	@PostConstruct
	public void init() {
		super.init();

		/**
		 * GESTION DE PERMISOS
		 */
		listaCodigoPermisos = (List<String>) JsfUtils.getSessionAttribute(Constantes.LISTACODIGOPERMISOS);
		permisoNewExpTram = listaCodigoPermisos.contains(Constantes.PERMISO_NEW_EXPTRAM);
		permisoNewExpTramLib = listaCodigoPermisos.contains(Constantes.PERMISO_NEW_EXPTRAMLIB);
		permisoNewExpTarea = listaCodigoPermisos.contains(Constantes.PERMISO_NEW_EXPTAREA);
		permisoNewPlazoExp = listaCodigoPermisos.contains(Constantes.PERMISO_NEW_PLAZOEXP);
		permisoSavePlazoExp = listaCodigoPermisos.contains(Constantes.PERMISO_SAVE_PLAZOEXP);

		permisoNewExpAct = listaCodigoPermisos.contains(Constantes.PERMISO_NEW_EXPACT);
		permisoNewExpTareaDet = listaCodigoPermisos.contains(Constantes.PERMISO_NEW_EXPTAREADET);
		permisoNewExpPlazoDet = listaCodigoPermisos.contains(Constantes.PERMISO_NEW_EXPPLAZODET);
		permisoNewExpFinTram = listaCodigoPermisos.contains(Constantes.PERMISO_NEW_EXPFINTRAM);
		permisoDesacExpTram = listaCodigoPermisos.contains(Constantes.PERMISO_DESAC_EXPTRAM);
		
		permisoRehabTramite = listaCodigoPermisos.contains(Constantes.PERMISO_REHAB_TRAMITE);

		cargarExpediente();
		valDomCanalSalida = new ValoresDominio();
		tramiteExpediente = new TramiteExpediente();
		

		this.listaValoresDominioCanalEntrada = valoresDominioService.findValoresDominioActivosByCodigoDominio(Constantes.COD_DOM_CAN_ENT);
		this.listaValoresDominioCanalSalida = valoresDominioService.findValoresDominioActivosByCodigoDominioOrden(ValoresDominioService.COD_COM);
		this.listaValoresDominioCanalInfEntrada = valoresDominioService.findValoresDominioActivosByCodigoDominio(Constantes.COD_DOM_CAN_COM_INF);
		this.listaValoresDominioCanalInfSalida = valoresDominioService.findValoresDominioActivosByCodigoDominio(Constantes.COD_DOM_CAN_COM_INF);
		
		this.listaValoresDominioActoRec = valoresDominioService.findValoresDominioActivosByCodigoDominioOrden(ValoresDominioService.COD_ACTO_REC);
		
		
		this.listaValoresDominioTipoPlazo = valoresDominioService.findValoresDominioActivosByCodigoDominio(Constantes.COD_TIP_PLA_TEMP);
		
		this.listaValoresDominioSentidoResolucion = valoresDominioService.findValoresDominioActivosByCodigoDominio(Constantes.COD_SENT_RESOL);
		
		this.listaValoresDominioTipoResolucion = valoresDominioService.findValoresDominioActivosByCodigoDominio(Constantes.COD_TIPORESOL);
		
		this.listaValoresDominioSerieNumeracion = valoresDominioService.findValoresDominioActivosByCodigoDominio(Constantes.COD_SERIESRESOL);
		
		this.listaValoresDominioTipoAdmision = valoresDominioService.findValoresDominioActivosByCodigoDominio(Constantes.COD_DOM_TIP_ADM);
		this.listaValoresDominioMotivoInadmision = valoresDominioService.findValoresDominioActivosByCodigoDominio(Constantes.COD_DOM_MOT_INADM);
		this.listaValoresDominioInstructorAPI = valoresDominioService.findValoresInstructoresAPI();

		this.listaValoresDominioResultadosNotificacion = valoresDominioService.findValoresDominioActivosByCodigoDominio(Constantes.COD_DOM_RES_NOTIF);
		
		this.listaValoresDominioPropuestaApi = valoresDominioService.findValoresDominioActivosByCodigoDominio(Constantes.COD_DOM_PROP_API);

		this.fechaInicioTramite = null;
		this.fechaInicioSubTramite = null;

		this.esIdentIntDPD = false;
		this.esIdentIntAutControl = false;
		this.esIdentIntPersona = false;
		this.esIdentIntSujOblig = false;
		
		this.listaResponsablesTram = responsablesTramitacionService.findResponsablesActivos();
		cabeceraDialog="";
		
		this.mensajeConfirmacionCierreTareas = "";
		
		/**Dialogo para la vinculacion de expedientes RCO en PSAN*/
		ValoresDominio valorDominioTipoExpPsan = valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_TIPO_EXPEDIENTE, Constantes.PSAN);
		lazyModelExpedientesBuscadorExpPsan = new LazyDataModelByQueryService<>(Expedientes.class,expedientesService);
		lazyModelExpedientesBuscadorExpPsan.setPreproceso((a, b, c, filters) -> {
			if (numeroExpedienteFiltroBuscadorExpPsan != null && !numeroExpedienteFiltroBuscadorExpPsan.isEmpty()){
				filters.put("numExpediente", new MyFilterMeta(numeroExpedienteFiltroBuscadorExpPsan));				
			}
			if (fechaEntradaFiltroBuscadorExpPsan != null) {
				filters.put("fechaEntrada", new MyFilterMeta(fechaEntradaFiltroBuscadorExpPsan));
			}
			if (nombreExpedienteFiltroBuscadorExpPsan != null && !nombreExpedienteFiltroBuscadorExpPsan.isEmpty()){
				filters.put("nombreExpediente", new MyFilterMeta(nombreExpedienteFiltroBuscadorExpPsan));				
			}
			filters.put("valorTipoExpediente.id", new MyFilterMeta(valorDominioTipoExpPsan.getId()));
		});
		defaultOrdenBuscadorExpPsan = SortMeta.builder().field("numExpediente").order(SortOrder.ASCENDING).priority(1).build();
	}

	private void cargarExpediente() {
		ContextoVolver cv = volverBean.getContexto(); // Sólo get, ya se ha recogido en DatosExpedientesBean
		ContextoVolver cvResol = volverBean.getContexto(Constantes.VOLVERRESOLUCION); // Sólo get, ya se ha recogido en DatosExpedientesBean

		/**
		 * EDICIÓN EXPEDIENTE
		 */
		expedientes = (Expedientes) JsfUtils.getSessionAttribute(EXPEDIENTEFORMULARIO);

		if (expedientes == null) {
			/**
			 * ALTA EXPEDIENTE
			 */
			expedientes = (Expedientes) JsfUtils.getSessionAttribute(EXPEDIENTE);
		}

		this.editable = (Boolean) JsfUtils.getSessionAttribute("editable");
		this.habilitarIdentifInt = true;

		if (cv != null) {
			idExp = (Long) cv.get(IDEXPSESSION);
			// Tenemos que recoger el expediente que ya teníamos en sesión
			expedientes = (Expedientes) JsfUtils.getSessionAttribute(EXPEDIENTEDG);
		} else if (cvResol != null){
			idExp = (Long) cvResol.get(IDEXPSESSION);
			// Tenemos que recoger el expediente que ya teníamos en sesión
			expedientes = (Expedientes) JsfUtils.getSessionAttribute(EXPEDIENTEDG);
		} else {
			// Carga normal
			idExp = (Long) JsfUtils.getFlashAttribute(IDEXPSESSION);

			if (idExp != null) {
				expedientes = expedientesService.obtener(idExp);
			}

		}
		
		/**
		 * Para modificar:
		 * - idExp no es necesario como miembro, solo se usa en este método
		 * - No parece necesario usar volverBean para cargar el expediente, 
		 *   debería bastar con este código.
		 
		expedientes = (Expedientes) JsfUtils.getSessionAttribute(EXPEDIENTEDG);
		if(expedientes == null) {
			Long idExp = (Long) JsfUtils.getFlashAttribute(IDEXPSESSION);

			if (idExp != null) {
				expedientes = expedientesService.obtener(idExp);
			}			
		}
		*/
		
		inicializarCampos();
		
	}

	public void inicializarCampos() {
		listaTramTramitesExp = new ArrayList<>();
		List<TramiteExpediente> listaTramTramitesExpAux;

		this.fechaInicioTramite = null;
		this.fechaInicioSubTramite = null;
		this.selectedNuevoResponsableTramId = null;
		this.selectedNuevoResponsableSubTramId = null;
		
		this.numResolVinc = null;
		this.fechaResolVinc = null;
		this.valorDomSentidoResolVinc = null;
		this.valorDomTipoResolVinc = null;
		
		if (expedientes != null) {

			listaTramTramitesExpAux = tramiteExpedienteService.findTipoTramTramitesExpAbiertos(expedientes.getId());

			/**
			 * RECORREMOS LA LISTA DE TRAMITES ABIERTOS AL EXPEDIENTE PARA CARGAR LOS
			 * VALORES ESPECÍFICOS DE CADA TRAMITE LOS CUALES NO ESTAN ALMACENADOS
			 * DIRECTAMENTE EN LA ENTIDAD QUE REPRESENTA AL TRAMITE EN SÍ. ESTOS SON: -
			 * LISTADO DE SUBTRAMITES - METADATOS ESPECIFICOS PARA EL TRAMITE SEGUN SU
			 * COMPORTAMIENTO - DETALLES DEL TRAMITE
			 */
			for (TramiteExpediente tE : listaTramTramitesExpAux) {

				/**
				 * OBTENEMOS LOS SUBTRAMITES ASOCIADOS AL TRAMITE
				 */
				this.listaTramTramitesExp.remove(tE);
				tE.setListaSubTramites(null);

				List<TramiteExpediente> listaSubTramTramitesExp = tramiteExpedienteService.findSubTramExp(expedientes.getId(), tE.getId());
				
				Boolean noTieneListaSub=noTieneListaSubtramites(tE);
				tE.setTieneListaSubTramites(noTieneListaSub);
				
				actualizarVisibilidadListadoNotificaciones(tE);
				actualizarVisibilidadListadoFirmas(tE);
				
				actualizarVisibilidadInstruccionesTramite(tE);
				actualizarVisibilidadBotonesTareas(tE);

				actualizarVisibilidadBotonFinalizar(tE);
				/**
				 * POR CADA SUBTRAMITE TENEMOS QUE TRATAR LOS DATOS ESPECIFICOS.
				 */

				for (TramiteExpediente subTrExp : listaSubTramTramitesExp) {
					
					subTrExp.setListaResponsables(responsablesTramitacionService.findResponsablesActivos());

								
					if (subTrExp.getResponsable() != null) {
						subTrExp.setSelectedNuevoResponsableId(subTrExp.getResponsable().getId());
					}
					
					actualizarVisibilidadInstruccionesTramite(subTrExp);
					actualizarVisibilidadBotonesTareas(subTrExp);

					actualizarVisibilidadBotonFinalizar(subTrExp);
					/**
					 * OBTENEMOS PARA CADA SUBTRAMITE O ACTIVIDAD LOS METADATOS ASOCIADOS A LA
					 * DESCRIPCION DE CAMPOS ESPECIFICOS DEL MISMO
					 * 
					 */
					metadatosPorSubtramite(subTrExp,tE);
					
				}

				tE.setListaSubTramites(listaSubTramTramitesExp);

				/**
				 * OBTENEMOS PARA CADA TRAMITE LOS METADATOS ASOCIADOS A LA DESCRIPCION DE
				 * CAMPOS ESPECIFICOS DEL MISMO
				 * 
				 * */
				
				String comp = tE.getTipoTramite().getComportamiento();	
				metadatosPorTramite(comp,tE);
				
				tE.setListaResponsables(responsablesTramitacionService.findResponsablesActivos());
				

				if (tE.getResponsable() != null) {
					tE.setSelectedNuevoResponsableId(tE.getResponsable().getId());
				}

				this.listaTramTramitesExp.add(tE);
			}
		}

		PrimeFaces.current().ajax().update(FORMFORMULARIOEXPEDIENTES);
	}
	
	
	
	private void actualizarVisibilidadInstruccionesTramite(TramiteExpediente tramExp)
	{
		TipoTramite tipoTramite = tramExp.getTipoTramite();

		tramExp.setMostrarInstrucciones(false);
		
		if(tipoTramite.getInstrucciones() != null && !tipoTramite.getInstrucciones().isEmpty()) {
			tramExp.setMostrarInstrucciones(true);	
		}
		
	}
	
	private void actualizarVisibilidadListadoNotificaciones(TramiteExpediente tramExp)
	{
		List<TramiteExpediente> listadoNotificaciones = tramiteExpedienteService.findSubTramExpByTramExpActivosCodTram(tramExp.getId(), Constantes.TIP_TRAM_COM, Constantes.TIP_TRAM_NOT); 
		tramExp.setMostrarListadoNotificaciones(false);
		
		if(listadoNotificaciones != null && !listadoNotificaciones.isEmpty())
		{
			tramExp.setMostrarListadoNotificaciones(true);	
		}
	}
	
	private void actualizarVisibilidadListadoFirmas(TramiteExpediente tramExp)
	{
		List<TramiteExpediente> listadoFirmas = tramiteExpedienteService.findSubTramExpByTramExpActivosCodTram(tramExp.getId(), Constantes.TIP_TRAM_FIRM); 
		tramExp.setMostrarListadoFirmas(false);
		
		if(listadoFirmas != null && !listadoFirmas.isEmpty())
		{
			tramExp.setMostrarListadoFirmas(true);	
		}
	}
	
	public Boolean muestraListadoNotificaciones(Long idTramExp)
	{
		Boolean mostrarListadoNotif = false;
		List<TramiteExpediente> listadoNotificaciones = tramiteExpedienteService.findSubTramExpByTramExpActivosCodTram(idTramExp, Constantes.TIP_TRAM_COM, Constantes.TIP_TRAM_NOT); 
				
		if(listadoNotificaciones != null && !listadoNotificaciones.isEmpty())
		{
			mostrarListadoNotif = true;	
		}
		
		return mostrarListadoNotif;
	}
	
	public Boolean muestraListadoFirmas(Long idTramExp)
	{
		Boolean mostrarListadoFirm = false;
		List<TramiteExpediente> listadoFirmas = tramiteExpedienteService.findSubTramExpByTramExpActivosCodTram(idTramExp, Constantes.TIP_TRAM_FIRM); 
				
		if(listadoFirmas != null && !listadoFirmas.isEmpty())
		{
			mostrarListadoFirm = true;	
		}
		
		return mostrarListadoFirm;
	}
	
	private void actualizarVisibilidadBotonesTareas(TramiteExpediente tramExp)
	{	
		
		int tareasActivas = 0;
		tramExp.setMostrarTareas(false);
		tramExp.setMostrarTareaREVT(false);
		tramExp.setMostrarTareaFYN(false);
		
		ValoresDominio tareasREVT = valoresDominioService.findValoresDominioByCodigoDomCodValDom("TAREAS", "REVT");
		
		Long idTipoTramSup = null;
		if (tramExp.getTramiteExpedienteSup() != null) {
			idTipoTramSup = tramExp.getTramiteExpedienteSup().getTipoTramite().getId();
		}
		
		List<CfgTareas> listaREVT = cfgTareasService.findCfgTareas(tramExp.getExpediente().getValorTipoExpediente().getId(), tramExp.getTipoTramite().getId(), idTipoTramSup, tareasREVT.getId(), false);
		
		if(listaREVT != null && !listaREVT.isEmpty()) {
			tramExp.setMostrarTareaREVT(true);
			tareasActivas ++;
		}
		
		ValoresDominio tareasFYN = valoresDominioService.findValoresDominioByCodigoDomCodValDom("TAREAS", Constantes.COD_TIP_TAR_TRAM_FYN);
		List<CfgTareas> listaFYN = cfgTareasService.findCfgTareas(tramExp.getExpediente().getValorTipoExpediente().getId(), tramExp.getTipoTramite().getId(), idTipoTramSup, tareasFYN.getId(), false);
		
		if(listaFYN != null && !listaFYN.isEmpty()) {
			tramExp.setMostrarTareaFYN(true);
			tareasActivas ++;
		}
		
		List<CfgTareas> listaTodas = cfgTareasService.findCfgTareasTodas(tramExp.getExpediente().getValorTipoExpediente().getId(), tramExp.getTipoTramite().getId(), idTipoTramSup, false);
		
		if((listaTodas != null && !listaTodas.isEmpty()) && (listaTodas.size() > tareasActivas)) {
				tramExp.setMostrarTareas(true);
		}
		
	}
	
	private void metadatosPorTramite (String comp,TramiteExpediente tE) {
		if(   (Constantes.C001.equals(comp) && tE.getTramiteExpedienteSup() == null) 
				|| Constantes.C003.equals(comp)
				|| Constantes.C004.equals(comp)
				|| Constantes.C005.equals(comp) 
				|| Constantes.C006.equals(comp)
				|| Constantes.C007.equals(comp)
				|| Constantes.C008.equals(comp)
				|| Constantes.C009.equals(comp)
				|| Constantes.C000.equals(comp)
				|| Constantes.C010.equals(comp)
				|| Constantes.C012.equals(comp)
				|| Constantes.C024.equals(comp)
				|| (Constantes.C011.equals(comp) && tE.getTramiteExpedienteSup() == null) 
				|| (Constantes.C014.equals(comp) && tE.getTramiteExpedienteSup() == null) 
				|| Constantes.C013.equals(comp)
				|| Constantes.C018.equals(comp)
				|| Constantes.C019.equals(comp)
				|| Constantes.C021.equals(comp)
				|| Constantes.C020.equals(comp)
				|| Constantes.C023.equals(comp)
				|| (Constantes.C017.equals(comp) && tE.getTramiteExpedienteSup() == null)  ){			
			// Cargar datos interesado y tipo interesado					
			if(!Constantes.C010.equals(comp) && !Constantes.C012.equals(comp) && !Constantes.C024.equals(comp)){
				tE.setListaValoresDominioTipoInteresado(valoresDominioService.findValoresDominioActivosByCodigoDominio(ValoresDominioService.COD_TIP_INT));
			}	
			
			try {
				tE.setCfgMetadatosTram(this.obtenerMetadatosTramite(expedientes.getValorTipoExpediente(), tE.getTipoTramite(), tE.getTramiteExpedienteSup()));
			} catch (ValidacionException e) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(NOEXISTEREGISTROCONFIG));
				log.warn(INICIALIZARCAMPOSPARA + expedientes.getValorTipoExpediente().getCodigo() + ", " + tE.getTipoTramite().getCodigo() + " - " + message.getDetail());
			}	
			
			/**
			 * CARGAMOS LOS DATOS DEL DETALLE DEL TRAMITE
			 */
			detalleExpdteTram = cargarDatosDetalleTramExp(tE);
			tE.setDetalleExpdteTram(detalleExpdteTram);
		}
	}
	
	private void metadatosPorSubtramite (TramiteExpediente subTrExp,TramiteExpediente tE) {
		if (Constantes.C015.equals(subTrExp.getTipoTramite().getComportamiento())
				|| Constantes.C003.equals(subTrExp.getTipoTramite().getComportamiento())
				|| Constantes.C004.equals(subTrExp.getTipoTramite().getComportamiento())
				|| Constantes.C001.equals(subTrExp.getTipoTramite().getComportamiento())
				|| Constantes.C016.equals(subTrExp.getTipoTramite().getComportamiento())
				|| Constantes.C002.equals(subTrExp.getTipoTramite().getComportamiento())) {
			try {
				subTrExp.setCfgMetadatosTram(this.obtenerMetadatosTramite(expedientes.getValorTipoExpediente(),subTrExp.getTipoTramite(), tE));
			} catch (ValidacionException e) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(NOEXISTEREGISTROCONFIG));
				log.warn(INICIALIZARCAMPOSPARA + expedientes.getValorTipoExpediente().getCodigo() + ", " + subTrExp.getTipoTramite().getCodigo() + " - " + message.getDetail());
			}
			
			if(Constantes.C015.equals(subTrExp.getTipoTramite().getComportamiento())
					|| Constantes.C001.equals(subTrExp.getTipoTramite().getComportamiento())
					|| Constantes.C002.equals(subTrExp.getTipoTramite().getComportamiento())
					|| Constantes.C003.equals(subTrExp.getTipoTramite().getComportamiento())
					|| Constantes.C004.equals(subTrExp.getTipoTramite().getComportamiento()))
			{
				subTrExp.setListaValoresDominioTipoInteresado(valoresDominioService.findValoresDominioActivosByCodigoDominio(ValoresDominioService.COD_TIP_INT));
			}
			
			if(Constantes.C016.equals(subTrExp.getTipoTramite().getComportamiento()))
			{	
				subTrExp.setListaFirmantes(usuarioService.findFirmantesActivos());
				subTrExp.setListaTipoFirmas(valoresDominioService.findValoresDominioActivosByCodigoDominio(Constantes.COD_DOM_TIP_FIRM));	
			}
			
			/**
			 * CARGAMOS LOS DATOS DEL DETALLE DEL TRAMITE
			 */
			detalleExpdteTram = cargarDatosDetalleTramExp(subTrExp);
			subTrExp.setDetalleExpdteTram(detalleExpdteTram);			
		}
	}

	public DetalleExpdteTram cargarDatosDetalleTramExp(TramiteExpediente trExp) {
		/**
		 * OBTENEMOS PARA CADA TRAMITE EL DETALLE DE LOS DATOS DEL DEL MISMO EN FUNCION DE SU COMPORTAMIENTO
		 * */
		
		detalleExpdteTram =detalleExpdteTramService.findDetalleTramiteExp(expedientes.getId(), trExp.getId());		
		
		if(detalleExpdteTram != null){			
			if(detalleExpdteTram.getValorTipoInteresado() != null){
				trExp.setSelectedNuevoTipoInteresadoId(detalleExpdteTram.getValorTipoInteresado().getId());	
			}
			if (detalleExpdteTram.getValorDominioInteresado() != null) {
				trExp.setSelectedNuevaIdentifInteresadoId(detalleExpdteTram.getValorDominioInteresado().getId());
			}							
			if(detalleExpdteTram.getValorResultadoNotificacion() != null) {
				trExp.setSelectedNuevoResulNotificacionId(detalleExpdteTram.getValorResultadoNotificacion().getId());
			}
			
			cargarDatosDetalleTramExp006(trExp);
			
			cargarDatosDetalleTramExp0080(trExp);
			
			cargarDatosDetalleTramExp00123456791112131417182423(trExp);
			
			cargarDatosDetalleTramExp010(trExp);
			
			cargarDatosDetalleTramExp016(trExp);			
		} else {
			this.detalleExpdteTram = new DetalleExpdteTram();
			establecerTipoInteresado(trExp);
			trExp.setSelectedNuevaIdentifInteresadoId(valoresDominioService.findValoresDominioByCodigoDomCodValDom(ValoresDominioService.COD_AUTCOMP,Constantes.COD_VAL_DOM_AEPD).getId());
			trExp.setSelectedNuevoCanalSalidaId(null);
			trExp.setSelectedNuevoCanalEntradaId(null);			
			trExp.setSelectedNuevoResulNotificacionId(null);
			trExp.setSelectedNuevoActoRecId(null);
		}

		return detalleExpdteTram;
	}

	
	private void establecerTipoInteresado(TramiteExpediente tramExp) {
		TipoTramite tipoTramite = null;
		Long idTipoInteresadoPorDefecto = null;
		
		if(tramExp != null)
		{
			tipoTramite = tramExp.getTipoTramite();
			if(tipoTramite.getValorTipoInteresadoDefecto() != null)
			{
				idTipoInteresadoPorDefecto = tipoTramite.getValorTipoInteresadoDefecto().getId();
				tramExp.setSelectedNuevoTipoInteresadoId(idTipoInteresadoPorDefecto);
			}else
			{
				tramExp.setSelectedNuevoTipoInteresadoId(valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.DOMINIO_TIPO_INTERESADO,Constantes.COD_VAL_DOM_AUTCON).getId());
			}
			recargaIdentificacionesInteresado(tramExp);
		}
	}
	
	private void establecerTipoInteresadoDetalleTramExp(TramiteExpediente tramExp, DetalleExpdteTram detTramExp) {
		TipoTramite tipoTramite = null;
		
		if(tramExp != null) {
			tipoTramite = tramExp.getTipoTramite();
			ValoresDominio valorTipoIntDefecto = tipoTramite.getValorTipoInteresadoDefecto();
			
			if(valorTipoIntDefecto != null) {
				detTramExp.setValorTipoInteresado(valorTipoIntDefecto);
				tramExp.setSelectedNuevoTipoInteresadoId(valorTipoIntDefecto.getId());				
			} else {
				detTramExp.setValorTipoInteresado(null);
				tramExp.setSelectedNuevoTipoInteresadoId(null);
			}
			recargaIdentificacionesInteresado(tramExp);
		}
		
	}
	
	private void cargarDatosDetalleTramExp0079131417341112 (TramiteExpediente trExp) {
		if(Constantes.C007.equals(trExp.getTipoTramite().getComportamiento()) 
				|| Constantes.C009.equals(trExp.getTipoTramite().getComportamiento())
				|| Constantes.C013.equals(trExp.getTipoTramite().getComportamiento())
				|| Constantes.C014.equals(trExp.getTipoTramite().getComportamiento())
				|| Constantes.C017.equals(trExp.getTipoTramite().getComportamiento())){
			cargarDatosDetalleTramExp0079131417(trExp);
			
		}else if(Constantes.C003.equals(trExp.getTipoTramite().getComportamiento())) {
			cargarDatosDetalleTramExp003(trExp);
		
		} else if(Constantes.C004.equals(trExp.getTipoTramite().getComportamiento())) {
			aplicarValorCanalSalida(trExp);
		
		} else if(Constantes.C011.equals(trExp.getTipoTramite().getComportamiento())) {
			cargarDatosDetalleTramExp011(trExp);
			
		}else if(Constantes.C012.equals(trExp.getTipoTramite().getComportamiento())
				|| Constantes.C024.equals(trExp.getTipoTramite().getComportamiento())) {
			cargarDatosDetalleTramExp012024(trExp);
		}
	}
	
	private void cargarDatosDetalleTramExp016 (TramiteExpediente trExp) {
		if(Constantes.C016.equals(trExp.getTipoTramite().getComportamiento())) {
			if((detalleExpdteTram.getFirmante() != null)){
				trExp.setSelectedNuevoFirmanteId(detalleExpdteTram.getFirmante().getId());	
			}else {
				trExp.setSelectedNuevoFirmanteId(null);
			}
			if((detalleExpdteTram.getValorDominioTipoFirma() != null)){
				trExp.setSelectedNuevoTipoFirmaId(detalleExpdteTram.getValorDominioTipoFirma().getId());	
			}else {
				trExp.setSelectedNuevoTipoFirmaId(null);
			}
		}
	}
	
	private void cargarDatosDetalleTramExp012024 (TramiteExpediente trExp) {
		if((detalleExpdteTram.getValorSentidoResolucion() != null)){
			trExp.setSelectedNuevoSentidoResolucionId(detalleExpdteTram.getValorSentidoResolucion().getId());	
		}
		
		if((detalleExpdteTram.getValorTipoResolucion() != null)){
			trExp.setSelectedNuevoTipoResolucionId(detalleExpdteTram.getValorTipoResolucion().getId());	
		}
		trExp.setMostrarBotoneraResolucion(detalleExpdteTram.getFechaResolucion() != null && detalleExpdteTram.getValorSentidoResolucion() != null && detalleExpdteTram.getNumResolucion() == null);
		trExp.setHabilitarVerResol(detalleExpdteTram.getNumResolucion() != null && !detalleExpdteTram.getNumResolucion().isEmpty());	
		trExp.setMostrarCamposResol(detalleExpdteTram.getNumResolucion() != null && !detalleExpdteTram.getNumResolucion().isEmpty());
		trExp.setHabilitarAsocResol(detalleExpdteTram.getNumResolucion() == null || detalleExpdteTram.getNumResolucion().isEmpty());
		
		calculaMensajeConfirmacion(trExp);
	}
	
	private void cargarDatosDetalleTramExp011 (TramiteExpediente trExp) {
		if (detalleExpdteTram.getValorCanalInfEntrada() != null) {
			trExp.setSelectedNuevoCanalInfEntradaId(detalleExpdteTram.getValorCanalInfEntrada().getId());
		}					
		if (detalleExpdteTram.getValorCanalInfSalida() != null) {
			trExp.setSelectedNuevoCanalInfSalidaId(detalleExpdteTram.getValorCanalInfSalida().getId());
		}
	}
	
	private void cargarDatosDetalleTramExp003 (TramiteExpediente trExp) {
		if(detalleExpdteTram.getValorTipoPlazo() != null){
			trExp.setSelectedNuevoTipoPlazoId(detalleExpdteTram.getValorTipoPlazo().getId());	
		}
		
		if(detalleExpdteTram.getPlazo() == null){
			ValoresDominio valDom = valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_TIPO_PLAZO, Constantes.COD_VAL_DOM_NOT);
			CfgPlazosAut cfgPlazosAut = cfgPlazosAutService.findCfgPlazosAutByTipExpTipTramTipPla(expedientes.getValorTipoExpediente().getId(), trExp.getTipoTramite().getId(), valDom.getId());
			if(cfgPlazosAut != null){
				detalleExpdteTram.setPlazo(cfgPlazosAut.getPlazo());						
			}else {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(NOEXISTEREGISTROCONFIG));
				log.warn(INICIALIZARCAMPOSPARA + expedientes.getValorTipoExpediente().getCodigo() + ", " + trExp.getTipoTramite().getCodigo() + " - " + message.getDetail());
			}
		}
		
		aplicarValorCanalSalida(trExp);
		
		if(detalleExpdteTram.getAcuseRecibo() == null)
		{
			detalleExpdteTram.setAcuseRecibo(true);	
		}
		
		
	}
	
	private void cargarDatosDetalleTramExp0079131417 (TramiteExpediente trExp) {
		if(detalleExpdteTram.getValorTipoPlazo() != null){
			trExp.setSelectedNuevoTipoPlazoId(detalleExpdteTram.getValorTipoPlazo().getId());	
		}
		
		if(Constantes.C007.equals(trExp.getTipoTramite().getComportamiento()) 
				|| Constantes.C009.equals(trExp.getTipoTramite().getComportamiento())
				|| Constantes.C013.equals(trExp.getTipoTramite().getComportamiento())
				|| Constantes.C014.equals(trExp.getTipoTramite().getComportamiento())
				|| Constantes.C017.equals(trExp.getTipoTramite().getComportamiento())){
			if(detalleExpdteTram.getPlazo() == null){
				ValoresDominio valDom = valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_TIPO_PLAZO, Constantes.COD_VAL_DOM_NOT);			
				CfgPlazosAut cfgPlazosAut = cfgPlazosAutService.findCfgPlazosAutByTipExpTipTramTipPla(expedientes.getValorTipoExpediente().getId(), trExp.getTipoTramite().getId(), valDom.getId());
				if(cfgPlazosAut != null)	{
					detalleExpdteTram.setPlazo(cfgPlazosAut.getPlazo());						
				}else{
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(NOEXISTEREGISTROCONFIG));
					log.warn(INICIALIZARCAMPOSPARA + expedientes.getValorTipoExpediente().getCodigo() + ", " + trExp.getTipoTramite().getCodigo() + " - " + message.getDetail());
				}
			}
			
			aplicarValorCanalSalida(trExp);
			
			if( (Constantes.C014.equals(trExp.getTipoTramite().getComportamiento())) && (detalleExpdteTram.getValorInstructorAPI() != null) ) {
				trExp.setSelectedNuevoInstructorAPIId(detalleExpdteTram.getValorInstructorAPI().getId());
			}
		}
	}
	
	private void cargarDatosDetalleTramExp0080 (TramiteExpediente trExp) {
		if(Constantes.C008.equals(trExp.getTipoTramite().getComportamiento())) {
			if(detalleExpdteTram.getValorTipoAdmision() != null){
				trExp.setSelectedNuevoTipoAdmisionId(detalleExpdteTram.getValorTipoAdmision().getId());	
			}				
			if(detalleExpdteTram.getValorMotivoInadmision() != null){
				trExp.setSelectedNuevoMotivoInadmisionId(detalleExpdteTram.getValorMotivoInadmision().getId());	
			}

			recargaMotivosInadmision(trExp);				
			recargaInstructorApi(trExp, detalleExpdteTram);	
			
			
		}			
	}
	
	private void cargarDatosDetalleTramExp006 (TramiteExpediente trExp) {
		if(Constantes.C006.equals(trExp.getTipoTramite().getComportamiento()) && (detalleExpdteTram.getValorCanalSalida() != null)){
			trExp.setSelectedNuevoCanalSalidaId(detalleExpdteTram.getValorCanalSalida().getId());
		}
	}

	private void aplicarDatosInteresado(TramiteExpediente trExp, DetalleExpdteTram detalleExpdteTram) {
		
		if(detalleExpdteTram.getValorTipoInteresado() == null) {
			trExp.setSelectedNuevaIdentifInteresadoId(null);
			return;
		}
		
		trExp.setSelectedNuevoTipoInteresadoId(detalleExpdteTram.getValorTipoInteresado().getId());	
		
		final String codTipoInt = detalleExpdteTram.getValorTipoInteresado().getCodigo();
		
		if(Constantes.COD_VAL_DOM_SUJOBL.equals(codTipoInt))
		{
			trExp.setEsIdentIntDPD(false);
			trExp.setEsIdentIntAutControl(false);
			trExp.setEsIdentIntPersona(false);
			trExp.setEsIdentIntSujOblig(true);

			if (detalleExpdteTram.getSujetosObligadosInteresado() != null) {
				trExp.setSelectedNuevaIdentifInteresadoId(
						detalleExpdteTram.getSujetosObligadosInteresado().getId());
			}

		} else if (Constantes.COD_VAL_DOM_AUTCON.equals(codTipoInt)) {
			trExp.setEsIdentIntDPD(false);
			trExp.setEsIdentIntAutControl(true);
			trExp.setEsIdentIntPersona(false);
			trExp.setEsIdentIntSujOblig(false);

			if (detalleExpdteTram.getValorDominioInteresado() != null) {
				trExp.setSelectedNuevaIdentifInteresadoId(
						detalleExpdteTram.getValorDominioInteresado().getId());
			}

		} else if (Constantes.COD_VAL_DOM_PERS.equals(codTipoInt)) {
			trExp.setEsIdentIntDPD(false);
			trExp.setEsIdentIntAutControl(false);
			trExp.setEsIdentIntPersona(true);
			trExp.setEsIdentIntSujOblig(false);

			if (detalleExpdteTram.getPersonasInteresado() != null) {
				trExp.setSelectedNuevaIdentifInteresadoId(
						detalleExpdteTram.getPersonasInteresado().getId());
			}
		} else if (Constantes.COD_VAL_DOM_DPD.equals(codTipoInt)) {
			trExp.setEsIdentIntDPD(true);
			trExp.setEsIdentIntAutControl(false);
			trExp.setEsIdentIntPersona(false);
			trExp.setEsIdentIntSujOblig(false);

			if (detalleExpdteTram.getPersonasInteresado() != null) {
				trExp.setSelectedNuevaIdentifInteresadoId(
						detalleExpdteTram.getPersonasInteresado().getId());
			}
		} else {
			trExp.setEsIdentIntDPD(false);
			trExp.setEsIdentIntAutControl(false);
			trExp.setEsIdentIntPersona(false);
			trExp.setEsIdentIntSujOblig(false);

			trExp.setSelectedNuevaIdentifInteresadoId(null);
		}

	}
	
	public void aplicarValorCanalSalida(TramiteExpediente trExp) {
		if (detalleExpdteTram.getValorCanalSalida() != null) {
			trExp.setSelectedNuevoCanalSalidaId(detalleExpdteTram.getValorCanalSalida().getId());
		}
	}
	
	public void cargarDatosDetalleSubTramExp(TramiteExpediente subTrExp) {
		/**
		 * OBTENEMOS PARA CADA TRAMITE EL DETALLE DE LOS DATOS DEL DEL MISMO EN FUNCION DE SU COMPORTAMIENTO
		 * */
		
		detalleExpdteSubTram =detalleExpdteTramService.findDetalleTramiteExp(expedientes.getId(), subTrExp.getId());
		
		
		if(detalleExpdteSubTram != null)
		{
			
			subTrExp.setListaValoresDominioIdentifInteresado( valoresDominioService.findValoresDominioActivosByCodigoDominio(ValoresDominioService.COD_AUTCOMP));
			
			subTrExp.setSelectedNuevaIdentifInteresadoId(valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.DOMINIO_TIPO_INTERESADO,Constantes.COD_VAL_DOM_AEPD).getId());
			
			if(null != detalleExpdteSubTram.getValorResultadoNotificacion()) {
				subTrExp.setSelectedNuevoResulNotificacionId(detalleExpdteSubTram.getValorResultadoNotificacion().getId());
			}

			if((Constantes.C001.equals(subTrExp.getTipoTramite().getComportamiento())
					|| Constantes.C003.equals(subTrExp.getTipoTramite().getComportamiento())) && subTrExp.getTramiteExpedienteSup() != null)
			{
				subTrExp.setListaIdentifIntSujOblig( sujetosObligadosExpedientesService.obtenerSujetosObligadosExpediente(expedientes.getId()));
				List<PersonasExpedientes> listaPersonasPorExpediente = personasExpedientesService.obtenerPersPorExpediente(expedientes.getId());
				List<PersonaDTO> listaIdentifIntPersonas = cargarPersonasYRepresentantes(listaPersonasPorExpediente);
				
				
				subTrExp.setListaIdentifIntPersDTO(listaIdentifIntPersonas);
				
				subTrExp.setListaIdentifIntDpd(sujetosObligadosExpedientesService.obtenerDpdExpediente(expedientes.getId()));
				subTrExp.setListaValoresDominioIdentifInteresado( valoresDominioService.findValoresDominioActivosByCodigoDominio(ValoresDominioService.COD_AUTCOMP));

				aplicarDatosInteresado(subTrExp, detalleExpdteSubTram);

				if (detalleExpdteSubTram.getValorCanalEntrada() != null) {
					subTrExp.setSelectedNuevoCanalEntradaId(detalleExpdteSubTram.getValorCanalEntrada().getId());
				}
			}else if(Constantes.C016.equals(subTrExp.getTipoTramite().getComportamiento())) {
				cargarDatosDetalleTramExp016(subTrExp);
			}
		} else {
			this.detalleExpdteSubTram = new DetalleExpdteTram();
			establecerTipoInteresado(subTrExp);
			subTrExp.setSelectedNuevaIdentifInteresadoId(
					valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.DOMINIO_TIPO_INTERESADO,Constantes.COD_VAL_DOM_AEPD).getId());
			subTrExp.setSelectedNuevoCanalSalidaId(null);
			subTrExp.setSelectedNuevoCanalEntradaId(null);
			
			subTrExp.setSelectedNuevoResulNotificacionId(null);
		}

	}
	
	public boolean hayTramite() {
		expedientes = (Expedientes) JsfUtils.getSessionAttribute(EXPEDIENTEFORMULARIO);
		if (expedientes.getId() == null) {
			expedientes = (Expedientes) JsfUtils.getSessionAttribute(EXPEDIENTE);
		}
		
		return cfgExpedienteTramiteService.existeTramitessinEventosByTipExpSitExp(expedientes.getValorTipoExpediente().getId(),expedientes.getValorSituacionExpediente().getId());
	}
	
	public boolean vistaEspecialEntradas() {
		
		expedientes = (Expedientes) JsfUtils.getSessionAttribute(EXPEDIENTEFORMULARIO);
		
		if (expedientes.getId() == null) {
			expedientes = (Expedientes) JsfUtils.getSessionAttribute(EXPEDIENTE);
		}
		
		boolean mostrarEntrada = true;
		
		if ((expedientes.getValorTipoExpediente().getCodigo().equals(Constantes.XPC))) {
			mostrarEntrada = false;
		}
		
		return mostrarEntrada;
	}
	
	
public String literalBotonFinalizar() {
		
		expedientes = (Expedientes) JsfUtils.getSessionAttribute(EXPEDIENTEFORMULARIO);
		
		if (expedientes.getId() == null) {
			expedientes = (Expedientes) JsfUtils.getSessionAttribute(EXPEDIENTE);
		}
		
		String literalBoton = mensajesProperties.getString("finalizar.tramite");
		
		if ((expedientes.getValorTipoExpediente().getCodigo().equals(Constantes.XPC))) {
			literalBoton = mensajesProperties.getString("finalizar.entrada");
		}
		
		return literalBoton;
	}
	
	
	
	public boolean hayEvento() {
		expedientes = (Expedientes) JsfUtils.getSessionAttribute(EXPEDIENTEFORMULARIO);
		if (expedientes.getId() == null) {
			expedientes = (Expedientes) JsfUtils.getSessionAttribute(EXPEDIENTE);
		}
		
		return cfgExpedienteTramiteService.existeEventosByTipExpSitExp(expedientes.getValorTipoExpediente().getId(),expedientes.getValorSituacionExpediente().getId());
	}

	

	public void iniciarTramite(String tramite) {
		this.selectedNuevoResponsableTramId=null;
		expedientes = (Expedientes) JsfUtils.getSessionAttribute(EXPEDIENTEFORMULARIO);
		if (expedientes.getId() == null) {
			expedientes = (Expedientes) JsfUtils.getSessionAttribute(EXPEDIENTE);
			cabeceraDialog="Listado de Tramites";
		}else {
			if (tramite.equals(EVENTO)) {
				cabeceraDialog="Listado de Trámites Libres para el expediente "+expedientes.getNumExpediente();}
			else {
				cabeceraDialog="Listado de Trámites para el expediente "+expedientes.getNumExpediente();}
				
			
		}

		this.selectedNuevoTramiteId = null;
		try {
			if (tramite.equals(TRAMITELIBRE)) {
					listaTramites = cfgExpedienteTramiteService.findExpTramitesByTipExp(expedientes.getValorTipoExpediente().getId());
					
					/** Se realiza este bucle para eliminar registros duplicados 
					 * para un mismo tipo de expediente, mismo tipo de tramite pero con distintas situaciones para eliminar las descripciones repetidas en el listado.
					 * Da igual obtener un registro u otro de CfgExpedienteTramite ya que todos losdatos son los mismos para lo que se necesita */
					listadoTramitesParaTramiteLibre();
			} else {
				if (tramite.equals(EVENTO)) {
				
				listaTramites = cfgExpedienteTramiteService.findExpEventosByTipExpSitExp(expedientes.getValorTipoExpediente().getId(),expedientes.getValorSituacionExpediente().getId());

				} else {

					if (tramite.equals(SINEVENTO)) {
						
						listaTramites = cfgExpedienteTramiteService.findExpTramitessinEventosByTipExpSitExp(expedientes.getValorTipoExpediente().getId(),expedientes.getValorSituacionExpediente().getId());

						} else {

					
					listaTramites = cfgExpedienteTramiteService.findExpTramitesByTipExpSitExp(expedientes.getValorTipoExpediente().getId(),expedientes.getValorSituacionExpediente().getId());
						}
				}
			}
			PrimeFaces.current().ajax().update(FORMFORMULARIOEXPEDIENTES);
	
			fechaInicioTramite = FechaUtils.hoy();
	
			PrimeFaces.current().executeScript("PF('dialogListadoTramites').show();");
		
		} catch (ValidacionException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(NOEXISTEREGISTROCONFIG));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error("iniciarTramite - " + message.getDetail());
		}
	}

	public void iniciarActividad(TramiteExpediente tramExp) {
		this.selectedNuevoResponsableSubTramId=null;
		this.tramiteExpediente = tramExp;

		this.selectedNuevoSubTramiteId = null;
		try {
			listaSubTramites = cfgExpedienteSubTramiteService.findSubTramites(tramExp.getTipoTramite().getId(),expedientes.getValorTipoExpediente().getId());
			
			PrimeFaces.current().ajax().update(FORMFORMULARIOEXPEDIENTES);

			fechaInicioSubTramite = FechaUtils.hoy();
			
			if(expedientes.getId()!=null) {
				cabeceraDialog="Listado Subtramites Tramites para el expediente "+ expedientes.getNumExpediente();
				}else {
					cabeceraDialog="Listado Subtramites Tramites para el expediente";
			}

			PrimeFaces.current().executeScript("PF('dialogListadoSubTramites').show();");
		} catch (ValidacionException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(NOEXISTEREGISTROCONFIG));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error("iniciarActividad - " + message.getDetail());
		}		
	}

	public String altaTramite(String tramite, String origenAltaTramite, CfgExpedienteTramite cfgExpedienteTramite) {
		expedientes = (Expedientes) JsfUtils.getSessionAttribute(EXPEDIENTEFORMULARIO);
		if (expedientes.getId() == null) {
			expedientes = (Expedientes) JsfUtils.getSessionAttribute(EXPEDIENTE);
		}
		
		if (tramite.equals(TRAMITE)) {

			return altaTramitePrincipal(origenAltaTramite, cfgExpedienteTramite);

		} else if (tramite.equals(ACTIVIDAD)) {

			altaSubTramite();
		}

		return "";
	}

	public String altaSubTramite() {
		TramiteExpediente subTramExp = new TramiteExpediente();
		DetalleExpdteTram detExpTram = new DetalleExpdteTram();
		ObservacionesExpedientes obsExp = null;

		if (selectedNuevoSubTramiteId != null) {

			/**
			 * VALIDAMOS QUE LA FECHA DE INICIO DEL SUBTRAMITE O ACTIVIDAD VENGA
			 * CUMPLIMENTADA
			 */

			if (this.fechaInicioSubTramite == null || selectedNuevoResponsableSubTramId == null) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						mensajesProperties.getString(CAMPOSOBLIGATORIOS));
				PrimeFaces.current().dialog().showMessageDynamic(message);

				return "";
			}

			cfgExpSubTramite = cfgExpedienteSubTramiteService.obtener(selectedNuevoSubTramiteId);

			subTramExp.setExpediente(expedientes);
			subTramExp.setTipoTramite(cfgExpSubTramite.getTipoTramite());
			subTramExp.setNivel(this.tramiteExpediente.getNivel() + 1);

			subTramExp.setTramiteExpedienteSup(this.tramiteExpediente);

			subTramExp.setDescripcion(cfgExpSubTramite.getDescripcion());
			subTramExp.setDescripcionAbrev(cfgExpSubTramite.getDescripcionAbrev());

			subTramExp.setFechaIni(this.fechaInicioSubTramite);
			subTramExp.setResponsable(responsablesTramitacionService.obtener(this.selectedNuevoResponsableSubTramId));
			subTramExp.setFinalizado(false);
			subTramExp.setActivo(true);

			try {
				Usuario usuario = (Usuario)JsfUtils.getSessionAttribute(Constantes.USUARIO);
				
				subTramExp.setUsuarioTramitador(usuario);
				
				
				obsExp = observacionesExpedientesService.guardarObservacionesExpedientes(null,null, Constantes.COD_VAL_DOM_TIPOBS_TRA, expedientes);

				subTramExp.setObservaciones(obsExp);				
				subTramExp = tramiteExpedienteService.altaTramite(usuario, subTramExp);
				obsExp.setTramiteExpdte(subTramExp);
				observacionesExpedientesService.guardar(obsExp);
				
				expedientes = utilsComun.expedienteUltimaModificacion(expedientes, subTramExp.getFechaModificacion(),
						subTramExp.getFechaCreacion(), subTramExp.getUsuModificacion(), subTramExp.getUsuCreacion());

				/**
				 * ALTA DETALLE TRAMITE EXPEDIENTE SEGUN COMPORTAMIENTO
				 */

				detExpTram.setActivo(true);
				detExpTram.setExpediente(expedientes);
				detExpTram.setTramiteExpediente(subTramExp);
				
				detExpTram.setExtractoExpediente(false);
				detExpTram.setAntecedentesExpediente(false);
				
				if (Constantes.C003.equals(subTramExp.getTipoTramite().getComportamiento())) {

					detExpTram.setValorTipoInteresado(null);
					detExpTram.setValorDominioInteresado(null);
					detExpTram.setValorResultadoNotificacion(valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_RES_NOTIF,Constantes.COD_VAL_DOM_PDTE));
					detExpTram.setValorTipoPlazo(valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_TIP_PLA_TEMP,Constantes.COD_VAL_DOM_DN));
				}
				
				if(Constantes.C016.equals(subTramExp.getTipoTramite().getComportamiento())) {
					detExpTram.setFechaEnvio(FechaUtils.hoy());
				}

				detExpTram = detalleExpdteTramService.guardar(detExpTram);
				expedientes = utilsComun.expedienteUltimaModificacion(expedientes, detExpTram.getFechaModificacion(),
						detExpTram.getFechaCreacion(), detExpTram.getUsuModificacion(), detExpTram.getUsuCreacion());

				inicializarCampos();

			} catch (BaseException e) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						mensajesProperties.getString(MENSAJEERROR));
				PrimeFaces.current().dialog().showMessageDynamic(message);
				log.error(e.getMessage());
			}

			FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "",
							SUBTRAMITEMSJ + " " + mensajesProperties.getString(GUARDADOCORRECTAMENTE)));

			PrimeFaces.current().ajax().update(FORMFORMULARIOEXPEDIENTES);

		} else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					mensajesProperties.getString(CAMPOSOBLIGATORIOS));
			PrimeFaces.current().dialog().showMessageDynamic(message);
		}

		aplicarSituacionAdicional(subTramExp, subTramExp.getDetalleExpdteTram());
		datosEvolucionExpedienteBean.onChangeListaTipoTramite();
		datosExpedientesBean.actualizarCabecera(expedientes, null, null, null);

		return "";
	}

	public String altaTramitePrincipal(String origenAltaTramite, CfgExpedienteTramite cfgExpedienteTramite) {
		TramiteExpediente tramExp = new TramiteExpediente();
		DetalleExpdteTram detExpTram = new DetalleExpdteTram();
		ObservacionesExpedientes obsExp = null;
		
		long nivel = 0;
		
		selectedNuevoTramiteIdIfOrigenAltaExpediente(origenAltaTramite, cfgExpedienteTramite);
		
		if (selectedNuevoTramiteId != null) {
			
			/** SI EL ALTA DEL TRAMITE VIENE DE LA PESTAÑA DE TRÁMITES: 
			 * - DEBEMOS VALIDAR QUE SE HAYA CUMPLIMENTADO LA FECHA DE INICIO
			 * - ASIGNAMOS EL FECHA INICIO Y RESPONSABLE QUE SE HA SELECCIONADO EN LA PANTALLA DE ALTA DE TRAMITE*/
			if(ORIGENPESTANYATRAMITES.equals(origenAltaTramite)){
				/**
				 * VALIDAMOS QUE LA FECHA DE INICIO DEL TRAMITE VENGA CUMPLIMENTADA
				 */
				if (this.fechaInicioTramite == null || selectedNuevoResponsableTramId == null) {
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",mensajesProperties.getString(CAMPOSOBLIGATORIOS));
					PrimeFaces.current().dialog().showMessageDynamic(message);
					return "";
				}
				
				tramExp.setFechaIni(this.fechaInicioTramite);
				tramExp.setResponsable(responsablesTramitacionService.obtener(this.selectedNuevoResponsableTramId));
				
			}else {
				/** SI EL ALTA DEL TRAMITE ES UN ALTA AUTOMATICA DEL ALTA DEL EXPEDIENTE: 
				* - ESTABLECEMOS COMO FECHA INICIAL LA FECHA ACTUAL
				* - ASIGNAMOS EL RESPONSABLE POR DEFECTO QUE TENGA EL TRAMITE CONFIGURADO O BIEN ASIGNAMOS EL RESPONSABLE DEL EXPEDIENTE. */
				tramExp.setFechaIni(FechaUtils.hoy());		
				
				siTieneResponsablesTramitacionPorCfg(tramExp,cfgExpedienteTramite);
			}

			cfgExpTramite = cfgExpedienteTramiteService.obtener(selectedNuevoTramiteId);
			tramExp.setExpediente(expedientes);
			tramExp.setTipoTramite(cfgExpTramite.getTipoTramite());
			tramExp.setNivel(nivel);
			tramExp.setDescripcion(cfgExpTramite.getDescripcion());
			tramExp.setDescripcionAbrev(cfgExpTramite.getDescripcionAbrev());			
			tramExp.setFinalizado(false);
			tramExp.setActivo(true);

			try {
				Usuario usuario = (Usuario)JsfUtils.getSessionAttribute(Constantes.USUARIO);				
				tramExp.setUsuarioTramitador(usuario);				
				obsExp = observacionesExpedientesService.guardarObservacionesExpedientes(null,null, Constantes.COD_VAL_DOM_TIPOBS_TRA, expedientes);
				tramExp.setObservaciones(obsExp);				
				tramExp = tramiteExpedienteService.altaTramite(usuario, tramExp);
				obsExp.setTramiteExpdte(tramExp);
				observacionesExpedientesService.guardar(obsExp);				
				expedientes = utilsComun.expedienteUltimaModificacion(expedientes, tramExp.getFechaModificacion(),tramExp.getFechaCreacion(), tramExp.getUsuModificacion(), tramExp.getUsuCreacion());

				/**
				 * ALTA DETALLE TRAMITE EXPEDIENTE SEGUN COMPORTAMIENTO
				 */
				detExpTram.setActivo(true);
				detExpTram.setExpediente(expedientes);
				detExpTram.setTramiteExpediente(tramExp);
				detExpTram.setApi(false);				
				detExpTram.setExtractoExpediente(false);
				detExpTram.setAntecedentesExpediente(false);
				detExpTram.setImposicionMedidas(false);
				this.establecerTipoInteresadoDetalleTramExp(tramExp, detExpTram);
				guardarDetalleTramiteExpInteresado(tramExp, detExpTram);
				
				/**
				 * Modificacion detExpTram según el comportamiento del trámite
				 */
				detExpTramComportamientoC003(detExpTram);
				detExpTramComportamientoC005(detExpTram);
				detExpTramComportamientoC007(detExpTram);
				detExpTramComportamientoC009(detExpTram);
				detExpTramComportamientoC012024(detExpTram,tramExp);
				detExpTramComportamientoC013(detExpTram);
				detExpTramComportamientoC014(detExpTram);
				detExpTramComportamientoC017(detExpTram);
				detExpTramComportamientoC008(detExpTram,tramExp);
				detExpTram = detalleExpdteTramService.guardar(detExpTram);
				expedientes = utilsComun.expedienteUltimaModificacion(expedientes,detExpTram.getFechaModificacion(),detExpTram.getFechaCreacion(),detExpTram.getUsuModificacion(),detExpTram.getUsuCreacion());
								
				inicializarCampos();
			} catch (BaseException e) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",mensajesProperties.getString(MENSAJEERROR));
				PrimeFaces.current().dialog().showMessageDynamic(message);
				log.error(e.getMessage());
			}

			PrimeFaces.current().executeScript("PF('dialogListadoTramites').hide();");
			FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, new FacesMessage(FacesMessage.SEVERITY_INFO, "", mensajesProperties.getString("tramite.iniciado.correcto")));

			this.tramiteDescAbrev = tramExp.getDescripcionAbrev();
			PrimeFaces.current().ajax().update(FORMFORMULARIOEXPEDIENTES);
			
		} else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",mensajesProperties.getString("crear.tipo.tramite"));
			PrimeFaces.current().dialog().showMessageDynamic(message);
		}

		aplicarSituacionAdicional(tramExp, tramExp.getDetalleExpdteTram());
		datosEvolucionExpedienteBean.onChangeListaTipoTramite();
		cambiarSituacionSegunAltaTramite(tramExp);
		datosExpedientesBean.actualizarCabecera(expedientes, null, null, null);

		return "";
	}
	
	public String guardarTramite(TramiteExpediente tramExpediente) {
		boolean guardadoOk = false;
		
		aplicarDetalleTramite(tramExpediente, tramExpediente.getDetalleExpdteTram());
		
		aplicarFechaTramiteInfoRelevante(tramExpediente, tramExpediente.getDetalleExpdteTram());
		aplicarSituacionAdicional(tramExpediente, tramExpediente.getDetalleExpdteTram());
		boolean descCambiada = tramExpediente.cambiaDescripcion();
		
		tramExpediente.setResponsable(responsablesTramitacionService.obtener(tramExpediente.getSelectedNuevoResponsableId()));
		boolean mostrarMsgCambioResp = mostrarMsgCambioResponsable(tramExpediente);
		String msgCambioResp = null;
		
		if(mostrarMsgCambioResp) {
			final String msg = getMessage("aviso.responsable.tramite.cambiado");
			final String respActual = responsablesTramitacionService.obtenerDescripcion(tramExpediente.getResponsableActual().getId());
			final String respNuevo = responsablesTramitacionService.obtenerDescripcion(tramExpediente.getResponsable().getId());
			msgCambioResp = MessageFormat.format(msg, respActual, respNuevo);
		}

		try {
			guardadoOk = guardarTramiteAux(tramExpediente);
		} catch (BaseException e) {
			facesMsgErrorKey(MENSAJEERROR);
			log.error(e.getMessage());
		}

		if(guardadoOk) {
			if(descCambiada) {
				PrimeFaces.current().ajax().addCallbackParam("refrescarDocs", true);
			}
			
			if(mostrarMsgCambioResp) {
				facesMsgInfo(msgCambioResp); //para este mensaje usamos el diálogo deliberadamente
			}				
			
			FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"",
					mensajesProperties.getString(TRAMITE) + " " + mensajesProperties.getString(ACTUALIZADOCORRECTAMENTE)));
	
			PrimeFaces.current().ajax().update(FORMFORMULARIOEXPEDIENTES);
		}
		
		return "";
	}

	/** METODO QUE GUARDA LA FECHA DE ENTRADA DE LOS TRAMIES ASI COMO INFORMACIÓN RELEVANTE DEL PROPIO TRAMITE.
	 * EN FUNCION DEL COMPORTAMIENTO O DEL CODIGO DEL TRÁMITE, GUARDAREMOS UNOS DATOS U OTROS. **/
	public void aplicarFechaTramiteInfoRelevante(TramiteExpediente traExp, DetalleExpdteTram detExpTram)
	{
		String comportamiento = traExp.getTipoTramite().getComportamiento();
		String codTipoTram = traExp.getTipoTramite().getCodigo();
		
		/** COMPORTAMIENTO TRAMITE: 
		 *  - C001: ENTRADA DOCUMENTO, PRESENTACIÓN DE ALEGACIONES y ENTRADA DOCUMENTO NO SOLICITADO.
		 *  - C005: INICIO EXPEDIENTE. **/
		if (Constantes.C001.equals(comportamiento) || Constantes.C005.equals(comportamiento)
				|| Constantes.C023.equals(comportamiento)) {
			traExp.setFechaTramite(detExpTram.getFechaEntrada());
			ValoresDominio valDomCanalEntrada = detExpTram.getValorCanalEntrada();
			
			if(valDomCanalEntrada != null)
			{
				traExp.setInformacionRelevante(valDomCanalEntrada.getAbreviatura());
			}
		}
		
		/** CODIGO TRAMITE: 
		 * - FIRM: FIRMA DOCUMENTO.
		 * - RESOL: RESOLUCIÓN. **/
		if(Constantes.TIP_TRAM_FIRM.equals(codTipoTram))
		{
			traExp.setFechaTramite(detExpTram.getFechaFirma());
		}
		
		if(Constantes.TIP_TRAM_RESOL.equals(codTipoTram))
		{
			traExp.setFechaTramite(detExpTram.getFechaResolucion());		
			traExp.setInformacionRelevante(detExpTram.getNumResolucion());
		}
		
		if (Constantes.C008.equals(comportamiento))
		{
			if (!traExp.getTipoTramite().getCodigo().equals(Constantes.TIP_TRAM_ACADMHE)) {
				
				traExp.setFechaTramite(detExpTram.getFechaInforme());
			}
			String txtInf = null;
			if (detExpTram.getValorTipoAdmision() != null) {
				txtInf = valoresDominioService.findValorDominioById(detExpTram.getValorTipoAdmision().getId()).getDescripcion();
			}
			
			
			if (detExpTram.getValorMotivoInadmision() != null) {
				txtInf =  txtInf + " " +  valoresDominioService.findValorDominioById(detExpTram.getValorMotivoInadmision().getId()).getDescripcion();
			}


						
			traExp.setInformacionRelevante(txtInf);
		}
		
	}
	
	/** METODO QUE GUARDA INFORMACIÓN DE LA SITUACIÓN EN LA QUE ESTÁ CADA TRÁMITE O SUBTRÁMITE.
	 * PARA INTEGRAR A CONTINUACIÓN EN LOS DATOS DEL EXPEDIENTE Y MOSTRARLO EN PANTALLA. **/
	public void aplicarSituacionAdicional(TramiteExpediente traExp, DetalleExpdteTram detExpTram) 
	{
		String codTipoTram = traExp.getTipoTramite().getCodigo();
		
		boolean subSup = traExp.getTipoTramite().getSubsituacionSuperior();
		
		if (subSup) {

			sitAdicionalSubSup(traExp, detExpTram);
			
		}
		
		if(Constantes.TIP_TRAM_NOT.equals(codTipoTram) && !(subSup))
		{			
			sitAdicionalNOT(traExp, detExpTram);
		}
		
		sitAdicionalParam(traExp);
		
		
		
		datosExpedientesBean.actualizarSituacionAdicional(traExp.getExpediente().getId());
	}

	public void sitAdicionalSubSup(TramiteExpediente traExp, DetalleExpdteTram detExpTram)  {
		
		String txtSitAdic = null;
		
		String descAbrev = traExp.getTipoTramite().getDescripcionAbrev();
		
		if (detExpTram.getFechaRespuesta() != null) {
			txtSitAdic =descAbrev + " respondido";						
		}
		else {
			
			if(detExpTram.getValorResultadoNotificacion() != null) {
				txtSitAdic = descAbrev + " notif " + detExpTram.getValorResultadoNotificacion().getDescripcion();
				}
			else {
				if (detExpTram.getFechaNotificacion() != null) {
				txtSitAdic = descAbrev + " notificado";}
				else {
					if (detExpTram.getFechaEnvio() != null) {
						txtSitAdic = descAbrev + " enviado";
					}
					else {txtSitAdic = descAbrev + " sin enviar";}
				}
			}
		}
		
		
		traExp.setSituacionAdicional(txtSitAdic);
		
		try {
			tramiteExpedienteService.guardar(traExp);
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
		
	}
	

	public void sitAdicionalParam(TramiteExpediente traExp)  {
		
		
		
		/** CODIGO TRAMITE: 
		 * - Rutina general. Busca la descripción en el campo de la tabla GE_TIPO_TRAMITE
		 * - **/
		
		String txtSitAdic = null;
		
		String descParam = traExp.getTipoTramite().getDescSubsituacion();	
		
		if (descParam != null) {
			txtSitAdic = descParam;						
				
			traExp.setSituacionAdicional(txtSitAdic);
		
			try {
				tramiteExpedienteService.guardar(traExp);
			} catch (BaseException e) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						mensajesProperties.getString(MENSAJEERROR));
				PrimeFaces.current().dialog().showMessageDynamic(message);
				log.error(e.getMessage());
			}
		
		}
	}
	
	public void sitAdicionalNOT(TramiteExpediente traExp, DetalleExpdteTram detExpTram)  {
		
		String txtSitAdic = null;
		
		String tramiteSuperior = "";
		
		if (traExp.getTramiteExpedienteSup() != null) {
			if(traExp.getTramiteExpedienteSup().getTipoTramite().getDescSubsituacion() == null) {
				tramiteSuperior = traExp.getTramiteExpedienteSup().getTipoTramite().getDescripcionAbrev();
				} else {
				tramiteSuperior = traExp.getTramiteExpedienteSup().getTipoTramite().getDescSubsituacion();
			}
			}
		
		if (Boolean.FALSE.equals(traExp.getTramiteExpedienteSup().getTipoTramite().getSubsituacionSuperior())){
		
			String tipInt = "";
			if (traExp.getDetalleExpdteTram().getValorTipoInteresado() != null) {
				tipInt = traExp.getDetalleExpdteTram().getValorTipoInteresado().getAbreviatura();
				}
		
		/** CODIGO TRAMITE: 
		 * - NOT: Traslado al NOT.
		 * - **/
			
			txtSitAdic = sitAdicNot(detExpTram, tramiteSuperior, tipInt);
		
		}
		
		traExp.setSituacionAdicional(txtSitAdic);
		
		try {
			tramiteExpedienteService.guardar(traExp);
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
		
	}
	
	public String sitAdicNot(DetalleExpdteTram detExpTram, String tramiteSuperior, String tipInt) {
		
		String txtSitAdic = "";
		
		if(detExpTram.getValorResultadoNotificacion() != null) {
			txtSitAdic = tramiteSuperior + " Notif " + detExpTram.getValorResultadoNotificacion().getDescripcion() + ": " + tipInt;
		} else {

			if ( detExpTram.getFechaNotificacion() != null) {				
				txtSitAdic =tramiteSuperior + " Notif realizada: " + tipInt;
			} else {
				if (detExpTram.getFechaEnvio() != null) {
					txtSitAdic =tramiteSuperior + " Notif enviada: " + tipInt;
				} else {
					txtSitAdic =tramiteSuperior + " Notif sin enviar: " + tipInt;
				}
			}

		}
		return txtSitAdic;
	}
	
	
	private boolean mostrarMsgCambioResponsable(TramiteExpediente tramExp) {
		//Mostrar si no es alta, cambia el responsable y hay tareas que reasignar
		return tramExp.getId() != null && tramExp.cambiaResponsable()
				&& tareasExpedienteService.existeTareaPendienteTramiteDeResponsable(
													tramExp.getId(),
													tramExp.getResponsableActual().getId()); //Responsable anterior
		
	}

	//-------------------------
	
	public String crearResolucion(TramiteExpediente tramExpediente, String tipoOperacion) {

		guardarTramite(tramExpediente);
		DetalleExpdteTram detExpTr = detalleExpdteTramService.findDetalleTramiteExp(tramExpediente.getExpediente().getId(), tramExpediente.getId());
		Resolucion resolucion = new Resolucion();	
		String mensaje = "";
		
		try {
			
			if(CREARRESOLUCION.equals(tipoOperacion))
			{
				resolucion.setCodigoResolucion(detExpTr.getNumResolucion());
				resolucion.setFechaResolucion(detExpTr.getFechaResolucion());
				resolucion.setValorSentidoResolucion(detExpTr.getValorSentidoResolucion());
				resolucion.setValorTipoResolucion(detExpTr.getValorTipoResolucion());

				resolucion.setAnonimizada(false);
				resolucion.setNotificadaTotal(false);
				
				resolucionService.guardar(resolucion);
			}
			/**
			 * SI SE TRATA DE UNA VINCULACION DEBEMOS RECUPERAR DICHA RESOLUCION PARA PODER ALMACENAR LA RELACION DE LA MISMA CON ESTE EXPEDIENTE.
			 * */
			else if(VINCULARRESOLUCION.equals(tipoOperacion))
			{
				/**
				 * OBTENEMOS LA RESOLUCION A PARTIR DE SU NUMERO DE RESOLUCION
				 * */
				resolucion = resolucionService.findResolucionByNumeroResolucion(detExpTr.getNumResolucion());
			}
			
			
			/**
			 * UNA VEZ HEMOS GUARDADO LOS DATOS PRINCIPALES DE LA RESOLUCION PASAMOS A GUARDAR LAS RELACIONES DE LA RESOLUCION CON LOS ELEMENTOS DEL EXPEDIENTE.
			 * 1.- SUJETOS OBLIGADOS
			 * 2.- PERSONAS
			 * 3.- EXPEDIENTES
			 * 4.- ARTICULOS AFECTADOS
			 * 5.- DERECHOS RECLAMADOS
			 * 6.- DOCUMENTOS
			 * */
			guardarResolucionSujetosObligados(resolucion, tramExpediente, tipoOperacion);
			
			guardarResolucionPersonas(resolucion, tramExpediente, tipoOperacion);
			
			guardarResolucionExpediente(resolucion, tramExpediente, tipoOperacion);
			
			guardarArticulosAfectadosResolucion(resolucion, tramExpediente, tipoOperacion);
			
			guardarDerechosReclamadosResolucion(resolucion, tramExpediente, tipoOperacion);
			
			if(VINCULARRESOLUCION.equals(tipoOperacion))
			{
				guardarDocumentosResolEnExpedienteTram(resolucion, tramExpediente);
			}
			
			
			tramExpediente.setMostrarBotoneraResolucion(detExpTr.getFechaResolucion() != null && detExpTr.getValorSentidoResolucion() != null && detExpTr.getValorTipoResolucion() != null && 
					tramExpediente.getDetalleExpdteTram().getNumResolucion() == null);
			tramExpediente.setHabilitarVerResol(detExpTr.getNumResolucion() != null && !detExpTr.getNumResolucion().isEmpty());
			tramExpediente.setMostrarCamposResol(detExpTr.getNumResolucion() != null && !detExpTr.getNumResolucion().isEmpty());
			tramExpediente.setHabilitarAsocResol(detExpTr.getNumResolucion() == null || detExpTr.getNumResolucion().isEmpty());
			
		} catch (BaseException e) {
			facesMsgErrorKey(MENSAJEERROR);
			log.error(e.getMessage());
		}

		if(CREARRESOLUCION.equals(tipoOperacion))
		{
			mensaje = mensajesProperties.getString(RESOLUCIONCREADA);
		}else
		{
			mensaje = mensajesProperties.getString(RESOLUCIONASOCIADA);
		}
		FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, new FacesMessage(FacesMessage.SEVERITY_INFO,
				"", mensaje));

		
		inicializarCampos();
		PrimeFaces.current().ajax().update(FORMFORMULARIOEXPEDIENTES);

		return "";
	}
	
	public String guardarResolucionSujetosObligados(Resolucion resolucion, TramiteExpediente tramExp, String tipoOperacion) {
		
		Expedientes exp = tramExp.getExpediente();
		List<SujetosObligadosExpedientes> listaSujOblExp = sujetosObligadosExpedientesService.obtenerSujetosObligadosExpedientePrincipalYNoPrincipalPorExpediente(exp.getId());
		
		for(SujetosObligadosExpedientes sujOblExp: listaSujOblExp)
		{
			
			ResolucionSujetoObligado resolSujObl = new ResolucionSujetoObligado();
			resolSujObl.setResolucion(resolucion);
			resolSujObl.setSujetoObligado(sujOblExp.getSujetosObligados());
			resolSujObl.setExpediente(exp);
			
			if(CREARRESOLUCION.equals(tipoOperacion))
			{
				resolSujObl.setPrincipal(sujOblExp.getPrincipal());	
			}else if(VINCULARRESOLUCION.equals(tipoOperacion))
			{
				resolSujObl.setPrincipal(false);
			}
			
			
			resolSujObl.setFechaNotificacion(null);
			
			try {
				resolucionSujetoObligadoService.guardar(resolSujObl);
			} catch (BaseException e) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						mensajesProperties.getString(MENSAJEERROR));
				PrimeFaces.current().dialog().showMessageDynamic(message);
				log.error(e.getMessage());
			}
		}
		return "";
	
	}
	
	public String guardarResolucionPersonas(Resolucion resolucion, TramiteExpediente tramExp, String tipoOperacion) {
		
		List<PersonasExpedientes> listaPersonasExp = personasExpedientesService.obtenerListaPersonaExpedientePorExpedientePrincipalYNoPrincipal(tramExp.getExpediente().getId());
		
		for(PersonasExpedientes persExp: listaPersonasExp)
		{
			
			ResolucionPersona resolPers = new ResolucionPersona();
			resolPers.setResolucion(resolucion);
			resolPers.setPersona(persExp.getPersonas());
			resolPers.setExpediente(tramExp.getExpediente());			
			if(CREARRESOLUCION.equals(tipoOperacion))
			{
				 resolPers.setPrincipal(persExp.getPrincipal());
			}else if(VINCULARRESOLUCION.equals(tipoOperacion))
			{
				resolPers.setPrincipal(false);		
			}
			
			resolPers.setFechaNotificacion(null);
			
			try {
				resolucionPersonaService.guardar(resolPers);
			} catch (BaseException e) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						mensajesProperties.getString(MENSAJEERROR));
				PrimeFaces.current().dialog().showMessageDynamic(message);
				log.error(e.getMessage());
			}
		}
		return "";
	
	}

	public String guardarResolucionExpediente(Resolucion resolucion, TramiteExpediente tramExp, String tipoOperacion) {
		
		ResolucionExpediente resolExp = new ResolucionExpediente();
		Expedientes exp = tramExp.getExpediente();
		
		resolExp.setResolucion(resolucion);
		resolExp.setExpediente(exp);
		resolExp.setValorSentidoResolucion(resolucion.getValorSentidoResolucion());
		
		
		if(CREARRESOLUCION.equals(tipoOperacion))
		{
			resolExp.setPrincipal(true);	
		}else if(VINCULARRESOLUCION.equals(tipoOperacion))
		{
			resolExp.setPrincipal(false);
		}
		
		
		try {
			resolucionExpedienteService.guardar(resolExp);
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
		
		return "";
	
	}
	
	
public String guardarArticulosAfectadosResolucion(Resolucion resolucion, TramiteExpediente tramExp, String tipoOperacion) {
		
		Expedientes exp = tramExp.getExpediente();
		List<ArticulosAfectadosExpedientes> listaArtAfecExp = articuloAfectadoExpedienteService.obtenerListArticulosAfectadosExpedientePorExpediente(exp.getId());
		
		for(ArticulosAfectadosExpedientes artAfectExp: listaArtAfecExp)
		{
			/**
			 * SI EL TIPO DE OPERACION ES CREAR RESOLUCION => CREAMOS UN REGISTRO POR CADA ELEMENTO DE LA LISTA, ES DECIR, POR CADA ART AFECTADO QUE EXISTA PARA EL EXP.
			 * SI EL TIPO DE OPERACION ES VINCULAR => CREAMOS EL REGISTRO SIEMPRE Y CUANDO NO EXISTA YA.
			 * */

			if(VINCULARRESOLUCION.equals(tipoOperacion) && articuloAfectadoResolucionService.findArtAfecResolucionExpByIdResolIdArtAdfec(resolucion.getId(), artAfectExp.getValoresArtAfectExp().getId()) == null
					|| CREARRESOLUCION.equals(tipoOperacion))
			{
				ArticuloAfectadoResolucion artAfectResol = new ArticuloAfectadoResolucion();

				artAfectResol.setResolucion(resolucion);
				artAfectResol.setValorArticulo(artAfectExp.getValoresArtAfectExp());
				try {
					articuloAfectadoResolucionService.guardar(artAfectResol);
				} catch (BaseException e) {
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
							mensajesProperties.getString(MENSAJEERROR));
					PrimeFaces.current().dialog().showMessageDynamic(message);
					log.error(e.getMessage());
				}

			}
		}
		return "";
	
	}

	public String guardarDerechosReclamadosResolucion(Resolucion resolucion, TramiteExpediente tramExp, String tipoOperacion) {
		
		Expedientes exp = tramExp.getExpediente();
		List<DerechosReclamadosExpedientes> listaDechosReclamExp = derechosReclamadosExpedientesService.obtenerListDerechosReclamadosExpedientePorExpediente(exp.getId());
		
		for(DerechosReclamadosExpedientes derechoReclamExp: listaDechosReclamExp)
		{
			/**
			 * SI EL TIPO DE OPERACION ES CREAR RESOLUCION => CREAMOS UN REGISTRO POR CADA ELEMENTO DE LA LISTA, ES DECIR, POR CADA DCHO RECLAMADO QUE EXISTA PARA EL EXP.
			 * SI EL TIPO DE OPERACION ES VINCULAR => CREAMOS EL REGISTRO SIEMPRE Y CUANDO NO EXISTA YA.
			 * */
			if(VINCULARRESOLUCION.equals(tipoOperacion) && derechoReclamadoResolucionService.findDchoReclamResolucionExpByIdResolIdDchoReclam(resolucion.getId(), derechoReclamExp.getValoresDerReclExp().getId()) == null
					|| CREARRESOLUCION.equals(tipoOperacion))
			{
				DerechoReclamadoResolucion dchoReclamResol = new DerechoReclamadoResolucion();
				
				dchoReclamResol.setResolucion(resolucion);
				dchoReclamResol.setValoresDerReclResol(derechoReclamExp.getValoresDerReclExp());
				
				try {
					derechoReclamadoResolucionService.guardar(dchoReclamResol);
				} catch (BaseException e) {
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
							mensajesProperties.getString(MENSAJEERROR));
					PrimeFaces.current().dialog().showMessageDynamic(message);
					log.error(e.getMessage());
				}
			}

		}
		return "";
	
	}
	
	
	public String guardarDocumentosResolEnExpedienteTram(Resolucion resolucion, TramiteExpediente tramExp) {
		
		List<DocumentoResolucion> listaDocResol = documentoResolucionService.findDocumentosResolucionByIdResol(resolucion.getId());
		
		for(DocumentoResolucion docResol: listaDocResol)
		{
			try {
				
				/**
				 * CREAMOS LA ASOCIACION DE DOCUMENTOS SIEMPRE Y CUANDO NO EXISTA
				 * */
				
				List<DocumentosExpedientes> listaDocsExp = documentosExpedientesService.findDocumentosActivosByExpdteIdTramExpIdDoc(tramExp.getExpediente().getId(), tramExp.getId(), docResol.getDocumentoExpediente().getId());
				if( listaDocsExp ==null || listaDocsExp.isEmpty())
				{
					DocumentosExpedientes docExp = new DocumentosExpedientes();
					DocumentosExpedientes docExpResol = docResol.getDocumentoExpediente();
					
					docExp.setActivo(true);
					docExp.setExpediente(tramExp.getExpediente());
					docExp.setTramiteExpediente(tramExp);
					docExp.setDocumento(docExpResol.getDocumento());
					docExp.setDescripcionDocumento(docExpResol.getDescripcionDocumento());
					docExp.setDescripcionAbrevDocumento(docExpResol.getDescripcionAbrevDocumento());
				
					documentosExpedientesService.guardar(docExp);

					DocumentosExpedientesTramites docExpTram = new DocumentosExpedientesTramites();
					
					docExpTram.setDocumentoExpediente(docExp);
					docExpTram.setTramiteExpediente(tramExp);	
					docExpTram.setOrigen(DocumentosExpedientesTramites.ORIGEN_VINCULADO);
					
					documentosExpedientesTramitesService.guardar(docExpTram);
					
					/** Puede que lo anterior se pueda hacer con: 
						DocumentosExpedientes docExpResol = docResol.getDocumentoExpediente();
						documentosExpedientesService.vincular(docExpResol, docExpResol.getExpediente(), tramExp);
					 */
				}
					
				
			} catch (BaseException e) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						mensajesProperties.getString(MENSAJEERROR));
				PrimeFaces.current().dialog().showMessageDynamic(message);
				log.error(e.getMessage());
			}
		}
		return "";
	
	}


	private boolean guardarTramiteAux(TramiteExpediente tramExpediente) throws BaseException {
		/**
		 * GUARDAR O ACTUALIZAR UN TRÁMITE IMPLICA GUARDAR: - TRAMITE EXPEDIENTE -
		 * SUBTRAMITES EXPEDIENTE -> PENDIENTE DE IMPLEMENTAR MÁS ADELANTE - DETALLE
		 * TRAMITE EXPEDIENTE
		 */

		/**
		 * GUARDAMOS EL TRAMITE EXPEDIENTE
		 */
		if (validacionesGuardarTramExp(tramExpediente)) {
			ObservacionesExpedientes obsExp = tramExpediente.getObservaciones();
			obsExp = observacionesExpedientesService.guardarObservacionesExpedientes(obsExp, obsExp.getTexto(), Constantes.COD_VAL_DOM_TIPOBS_TRA, expedientes);
			tramExpediente.setObservaciones(obsExp);
			tramExpediente.setResponsable(responsablesTramitacionService.obtener(tramExpediente.getSelectedNuevoResponsableId()));
			setMostrarBotonFinalizar(tramExpediente);
			tramExpediente = tramiteExpedienteService.guardar(tramExpediente);
			obsExp.setTramiteExpdte(tramExpediente);
			observacionesExpedientesService.guardar(obsExp);		
			expedientes = utilsComun.expedienteUltimaModificacion(expedientes,
					tramExpediente.getFechaModificacion(), tramExpediente.getFechaCreacion(),
					tramExpediente.getUsuModificacion(), tramExpediente.getUsuCreacion());
		} else {
			return false;
		}

		/**
		 * GUARDARMOS EL DETALLE DEL TRAMITE EN FUNCION DEL COMPORTAMIENTO:
		 */
		if (validacionesGuardarDetalleTramExp(tramExpediente) && tramExpediente.getDetalleExpdteTram() != null) {
			guardarDetalleTramiteExp(tramExpediente);

			/**
			 * AL GUARDAR LOS DATOS DEBEMOS ACTUALIZAR LA FECHA DE REGISTRO, CANAL DE
			 * ENTRADA Y NUM REGISTRO ENTRADA EN EL EXPEDIENTE.
			 */
			if (Constantes.C005.equals(tramExpediente.getTipoTramite().getComportamiento())) {
				
				actualizarDatosEntradaExpediente(tramExpediente);
				
				datosExpedientesBean.limpiarPanelPlazos();

				datosExpedientesBean.actualizarCabecera(expedientes, null, null, null);
			} else if (Constantes.C014.equals(tramExpediente.getTipoTramite().getComportamiento())) {
				
				operacionesAuxComportamientoC014(tramExpediente);
				actualizarDatosEntradaExpediente(tramExpediente);

			}else if (Constantes.C007.equals(tramExpediente.getTipoTramite().getComportamiento()))
			{
				actualizarPlazosExpdte(tramExpediente);

			} else if(Constantes.C016.equals(tramExpediente.getTipoTramite().getComportamiento())) {
				guardarDetalleExdteTramDatosFirma(tramExpediente, detalleExpdteTram);
				inicializarCampos();
				datosExpedientesBean.actualizarCabecera(expedientes, null, null, null);
			} else if(Constantes.C006.equals(tramExpediente.getTipoTramite().getComportamiento())) {
				actualizarAutoridadCompetenteEnExpdte(tramExpediente);
				datosExpedientesBean.actualizarCabecera(expedientes, null, null, null);
			} else if(Constantes.C008.equals(tramExpediente.getTipoTramite().getComportamiento())) {
				actualizarAPIEnExpdte(tramExpediente);
				datosExpedientesBean.actualizarCabecera(expedientes, null, null, null);
			} else if(Constantes.C018.equals(tramExpediente.getTipoTramite().getComportamiento()) || Constantes.C013.equals(tramExpediente.getTipoTramite().getComportamiento())) {
				actualizarMedidasEnExpdte(tramExpediente);
				datosExpedientesBean.actualizarCabecera(expedientes, null, null, null);}
			
			
		} else {
			return false;
		}
		
		return true;
	}
	
	private void setMostrarBotonFinalizar(TramiteExpediente tramExpediente) {
		boolean esResponsableTramiteUsuarioConectado = usuariosResponsablesService.esResponsableDeUsuario(tramExpediente.getResponsable().getId(), sesionBean.getIdUsuarioSesion());
		tramExpediente.setMostrarBotonFinalizar(esResponsableTramiteUsuarioConectado);
	}

	public Integer calcularPlazoAdicional(Expedientes expedientes)
	{
		
		/**
		 * EN EL CALCULO DEL PLAZO ADICIONAL VAMOS A TENER EN CUENTA, DE MOMENTO, LOS TRAMITES DE SUBSANACION Y REQUERIMIENTO DE INFORMACION.
		 * */
		
		List<DetalleExpdteTram> listaDetallesExpdeTramSubsan = detalleExpdteTramService.findDetalleTramExpAfectaPlazosByCodTipoTram(expedientes.getId(), Constantes.TIP_TRAM_SUB);
		List<DetalleExpdteTram> listaDetallesExpdeTramReqInf = detalleExpdteTramService.findDetalleTramExpAfectaPlazosByCodTipoTram(expedientes.getId(), Constantes.TIP_TRAM_REQINF);
		
		List<DetalleExpdteTram> listaDetallesExpdeTram = new ArrayList<>();
		
		if(listaDetallesExpdeTramSubsan != null && !listaDetallesExpdeTramSubsan.isEmpty())
		{
			listaDetallesExpdeTram.addAll(listaDetallesExpdeTramSubsan);
		}
		
		if(listaDetallesExpdeTramReqInf != null && !listaDetallesExpdeTramReqInf.isEmpty())
		{
			listaDetallesExpdeTram.addAll(listaDetallesExpdeTramReqInf);
		}
		
		
		Integer plazoAdicional = 0;
		
		for(DetalleExpdteTram detExpTram: listaDetallesExpdeTram){
			Integer plazoAdicionalTmp = calcularPlazoAdicionalDetalleExpdteTram(detExpTram);
			plazoAdicional = plazoAdicional + plazoAdicionalTmp;
		}
		
		return plazoAdicional;
	}
	
	private Integer calcularPlazoAdicionalDetalleExpdteTram (DetalleExpdteTram detExpTram) {
		Integer plazoAdicionalTmp = 0;	
		TramiteExpediente trExp = null;
		
		if (Boolean.TRUE.equals(detExpTram.getAfectaPlazos())){			
			/**
			 * SI EL TRAMITE ES DE TIPO C007 (SUBSANACION) => TENDREMOS EN CUENTA LA FECHA DE SUBSANACION A LA HORA DE CALCULAR EL PLAZO ADICIONAL.
			 * SI EL TRAMITE ES DE TIPO C009 (REQUERIMIENTO INFO) => TENDREMOS EN CUENTA LA FECHA DE RESPUESTA.
			 * */
			
			Date fechaSubRes = null;			
			trExp = detExpTram.getTramiteExpediente();
			
			if(Constantes.C007.equals(trExp.getTipoTramite().getComportamiento()))	{
				fechaSubRes = detExpTram.getFechaSubsanacion();
				
			}else if(Constantes.C009.equals(trExp.getTipoTramite().getComportamiento())) {
				fechaSubRes = detExpTram.getFechaRespuesta();
			}
			
			if (fechaSubRes == null && (detExpTram.getFechaNotificacion() != null || detExpTram.getFechaEnvio() != null  )) {
				plazoAdicionalTmp = detExpTram.getPlazo();
			} else {
			
				plazoAdicionalTmp = FechaUtils.diasEntre(FechaUtils.max(detExpTram.getFechaNotificacion(), detExpTram.getFechaEnvio()),FechaUtils.min(fechaSubRes, detExpTram.getFechaLimite()));
				plazoAdicionalTmp = Math.min(plazoAdicionalTmp, detExpTram.getPlazo());				
			}
		}
		return plazoAdicionalTmp;
	}

	public void actualizarDatosEntradaExpediente(TramiteExpediente tE) {
		if (expedientes.getFechaRegistro() == null) {
			expedientes.setFechaRegistro(tE.getDetalleExpdteTram().getFechaEntrada());
		}
		if (expedientes.getNumRegistro() == null || expedientes.getNumRegistro().isEmpty()) {
			expedientes.setNumRegistro(tE.getDetalleExpdteTram().getIdentifEntrada());
		}
		if (expedientes.getValorCanalEntrada() == null 
				&& tE.getDetalleExpdteTram().getValorCanalEntrada() != null) {
			expedientes.setValorCanalEntrada(valoresDominioService.obtener(tE.getDetalleExpdteTram().getValorCanalEntrada().getId()));
		}
		
		expedientes.setApi(tE.getDetalleExpdteTram().getApi());
		expedientes.setValorInstructorAPI(tE.getDetalleExpdteTram().getValorInstructorAPI());

		try {
			expedientesService.guardar(expedientes);
			datosExpedientesDatosGeneralesBean.actualizarPestanyaDatosGral(expedientes);
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}

	}

	public void guardarDetalleTramiteExp(TramiteExpediente traExp) {
		DetalleExpdteTram detExpTram = traExp.getDetalleExpdteTram();
		
		try {
			detExpTram.setActivo(true);
			detExpTram = detalleExpdteTramService.guardar(detExpTram);
			expedientes = utilsComun.expedienteUltimaModificacion(expedientes, detExpTram.getFechaModificacion(),
					detExpTram.getFechaCreacion(), detExpTram.getUsuModificacion(), detExpTram.getUsuCreacion());

		} catch (BaseException e) {
			facesMsgErrorKey(MENSAJEERROR);
			log.error(e.getMessage());
		}

		datosExpedientesBean.actualizarCabecera(expedientes, null, null, null);
	}
	
	
	private void aplicarDetalleTramite(TramiteExpediente traExp, DetalleExpdteTram detExpTram) {
		
		selectedNuevoResultNotificacion(traExp, detExpTram);

		guardarDetalleTramiteExpInteresado(traExp, detExpTram);
		
		switch (traExp.getTipoTramite().getComportamiento()) {
		
			case Constantes.C006:
				guardarDetalleTramiteExpDatosSalida(traExp, detExpTram);	
				detExpTram.setFechaEnvio(traExp.getDetalleExpdteTram().getFechaEnvio());
			
			break;
			
			case Constantes.C005:
			
				guardarDetalleTramiteExpDatosEntrada(traExp, detExpTram);
				
			break;
					
			case Constantes.C001:
			
				guardarDetalleTramiteExpDatosEntrada(traExp, detExpTram);
				
			break;

			case Constantes.C018:
			
				guardarDetalleTramiteExpDatosEntrada(traExp, detExpTram);
				
			break;
			
			case Constantes.C023:
				
				guardarDetalleTramiteExpDatosEntrada(traExp, detExpTram);
				
			break;
		

			case Constantes.C015:

				detExpTram.setFechaEnvio(traExp.getDetalleExpdteTram().getFechaEnvio());
				detExpTram.setFechaRespuesta(traExp.getDetalleExpdteTram().getFechaRespuesta());
							
			break;


			case Constantes.C007: case Constantes.C009: case Constantes.C013: case Constantes.C017:

				guardarDetalleTramiteExpDatosEntrada(traExp, detExpTram);
				guardarDetalleTramiteExpC007C009C013C017(traExp, detExpTram);
							
			break;

			case Constantes.C003:

				guardarDetalleTramiteExpC003(traExp, detExpTram);
							
			break;

			case Constantes.C008:

				guardarDetalleTramiteExpC008C019(traExp, detExpTram);	
							
			break;

			case Constantes.C004:

				guardarDetalleTramiteExpC004(traExp, detExpTram);
							
			break;

			case Constantes.C011:

				guardarDetalleTramiteExpC011(traExp, detExpTram);
							
			break;

			case Constantes.C012:

				guardarDetalleTramiteExpC012C024(traExp, detExpTram);
							
			break;

			case Constantes.C024:

				guardarDetalleTramiteExpC012C024(traExp, detExpTram);
							
			break;

			case Constantes.C014:

				guardarDetalleTramiteExpC014(traExp, detExpTram);
							
			break;

			case Constantes.C016:

				guardarDetalleTramiteExpC016(traExp, detExpTram);
							
			break;

			case Constantes.C010:

			guardarDetalleTramiteExpC010(traExp, detExpTram);
							
			break;


			case Constantes.C019:

				guardarDetalleTramiteExpC008C019(traExp, detExpTram);
				
			break;
			
			default:
				
				

		}
	
		
	}
	
	private void operacionesAuxComportamientoC014(TramiteExpediente traExp) throws BaseException {
		if(traExp.getDetalleExpdteTram().getFechaInforme() != null) {
			expedientes.setFechaEntrada(traExp.getDetalleExpdteTram().getFechaInforme());
			expedientes = expedientesService.guardar(expedientes);
		
			utilsComun.generarPlazoExpdte(traExp.getId(), null, traExp.getDetalleExpdteTram().getFechaInforme(), Constantes.COD_VAL_DOM_RES, Constantes.COD_VAL_DOM_DN, 0, "A", expedientes);
		}
	}
	
	public void guardarDetalleExdteTramDatosFirma(TramiteExpediente tramExp, DetalleExpdteTram detTramExp)
	{
		DetalleExpdteTram detExpTramSup = null; 
		
		
		if(tramExp.getTramiteExpedienteSup() != null )
		{
			detExpTramSup = detalleExpdteTramService.findDetalleTramiteExp(tramExp.getExpediente().getId(), tramExp.getTramiteExpedienteSup().getId());
			if(detExpTramSup == null )
			{
				detExpTramSup = new DetalleExpdteTram();
				detExpTramSup.setActivo(true);
				detExpTramSup.setFechaFirma(detTramExp.getFechaFirma());
				detExpTramSup.setTramiteExpediente(tramExp.getTramiteExpedienteSup());
			}else {
				detExpTramSup.setFechaFirma(detTramExp.getFechaFirma());
			}
				
			try {
				
				detalleExpdteTramService.guardar(detExpTramSup);

			} catch (BaseException e) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						mensajesProperties.getString(MENSAJEERROR));
				PrimeFaces.current().dialog().showMessageDynamic(message);
				log.error(e.getMessage());
			}
		}
	}
	
	public void guardarDetalleTramiteExpC007C009C013C017(TramiteExpediente traExp, DetalleExpdteTram detExpTram)
	{
		
		detExpTram.setFechaFirma(traExp.getDetalleExpdteTram().getFechaFirma());
		
		guardarDetalleTramiteExpDatosSalida(traExp, detExpTram);
		
		detExpTram.setFechaEnvio(traExp.getDetalleExpdteTram().getFechaEnvio());
		detExpTram.setFechaNotificacion(traExp.getDetalleExpdteTram().getFechaNotificacion());
		
		if(null != traExp.getSelectedNuevoTipoPlazoId()){
			detExpTram.setValorTipoPlazo(valoresDominioService.obtener(traExp.getSelectedNuevoTipoPlazoId()));
		}
		
		if(traExp.getDetalleExpdteTram().getPlazo() != null)
		{
			detExpTram.setPlazo(traExp.getDetalleExpdteTram().getPlazo());	
		}else
		{
			detExpTram.setPlazo(null);
		}
		
		detExpTram.setFechaLimite(traExp.getDetalleExpdteTram().getFechaLimite());
		detExpTram.setAfectaPlazos(traExp.getDetalleExpdteTram().getAfectaPlazos());
		
		if(Constantes.C007.equals(traExp.getTipoTramite().getComportamiento()))
		{
			detExpTram.setFechaSubsanacion(traExp.getDetalleExpdteTram().getFechaSubsanacion());

			detExpTram.setSubsanaAdecudamente(traExp.getDetalleExpdteTram().getSubsanaAdecudamente());	
		}else if (Constantes.C009.equals(traExp.getTipoTramite().getComportamiento()))
		{
			detExpTram.setFechaRespuesta(traExp.getDetalleExpdteTram().getFechaRespuesta());
			
		}else if (Constantes.C013.equals(traExp.getTipoTramite().getComportamiento()))
		{
			detExpTram.setFechaAcreditacion(traExp.getDetalleExpdteTram().getFechaAcreditacion());
			detExpTram.setAcreditaCumplimiento(traExp.getDetalleExpdteTram().getAcreditaCumplimiento());			
		}
	}
	
	public void guardarDetalleTramiteExpC012C024(TramiteExpediente traExp, DetalleExpdteTram detExpTram){
		if(this.numResolVinc != null){
			detExpTram.setNumResolucion(this.numResolVinc);	
		}else {
			detExpTram.setNumResolucion(traExp.getDetalleExpdteTram().getNumResolucion());
		}
		
		if(this.fechaResolVinc != null){
			detExpTram.setFechaResolucion(this.fechaResolVinc);
		}else {
			detExpTram.setFechaResolucion(traExp.getDetalleExpdteTram().getFechaResolucion());	
		}
		
		if(detExpTram.getFechaResolucion() != null){
			actualizarResolucionConFechaResolucion(detExpTram);
		}
		
		if(this.valorDomSentidoResolVinc != null){
			detExpTram.setValorSentidoResolucion(this.valorDomSentidoResolVinc);
		}else{
			if(traExp.getSelectedNuevoSentidoResolucionId() != null){
				detExpTram.setValorSentidoResolucion(valoresDominioService.obtener(traExp.getSelectedNuevoSentidoResolucionId()));	
			}else{
				detExpTram.setValorSentidoResolucion(null);
			}
		}
		
		if(this.valorDomTipoResolVinc != null){
			detExpTram.setValorTipoResolucion(this.valorDomTipoResolVinc);
		}else {
			if(traExp.getSelectedNuevoTipoResolucionId() != null){
				detExpTram.setValorTipoResolucion(valoresDominioService.obtener(traExp.getSelectedNuevoTipoResolucionId()));	
			}else{
				detExpTram.setValorTipoResolucion(null);
			}
		}		
		
		actualizarExpedienteConImposicionMedidas(detExpTram,traExp);
	}
	
	private void actualizarExpedienteConImposicionMedidas(DetalleExpdteTram detExpTram,TramiteExpediente traExp){
		if(!Constantes.RCO.equals(traExp.getExpediente().getValorTipoExpediente().getCodigo())) {
			Expedientes exp;
			TramiteExpediente tramExp = detExpTram.getTramiteExpediente();
			
			if(tramExp != null)
			{
				exp = tramExp.getExpediente();
				
				if (detExpTram.getImposicionMedidas() == null) {
					exp.setImposicionMedidas(false);
				} else {
				
				exp.setImposicionMedidas(detExpTram.getImposicionMedidas());
				}
				
				try {
					expedientesService.guardar(exp);
					datosExpedientesDatosGeneralesBean.actualizarPestanyaDatosGral(exp);
				} catch (BaseException e) {
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
							mensajesProperties.getString(MENSAJEERROR));
					PrimeFaces.current().dialog().showMessageDynamic(message);
					log.error(e.getMessage());
				}
			}
		}		
	}
	
	public void actualizarResolucionConFechaResolucion(DetalleExpdteTram detExpTram)
	{
		Resolucion resol = resolucionService.findResolucionByNumeroResolucion(detExpTram.getNumResolucion());
		if(resol != null)
		{				
			resol.setFechaResolucion(detExpTram.getFechaResolucion());	
			try {
				resolucionService.guardar(resol);
			} catch (BaseException e) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						mensajesProperties.getString(MENSAJEERROR));
				PrimeFaces.current().dialog().showMessageDynamic(message);
				log.error(e.getMessage());
			}
		}
	}
	
	public void guardarDetalleTramiteExpC008C019(TramiteExpediente traExp, DetalleExpdteTram detExpTram)
	{
		detExpTram.setFechaInforme(traExp.getDetalleExpdteTram().getFechaInforme());
		if(traExp.getSelectedNuevoTipoAdmisionId() != null)
		{
			detExpTram.setValorTipoAdmision(valoresDominioService.obtener(traExp.getSelectedNuevoTipoAdmisionId()));	
		}else
		{
			detExpTram.setValorTipoAdmision(null);
		}
		
		if(traExp.getSelectedNuevoMotivoInadmisionId()!= null)
		{
			detExpTram.setValorMotivoInadmision(valoresDominioService.obtener(traExp.getSelectedNuevoMotivoInadmisionId()));	
		}else
		{
			detExpTram.setValorMotivoInadmision(null);
		}
		
		if((Constantes.RCO.equals(traExp.getExpediente().getValorTipoExpediente().getCodigo())) && (traExp.getSelectedNuevoInstructorApiIdAcAdmis() != null)) {
			
			detExpTram.setValorInstructorAPI(valoresDominioService.obtener(traExp.getSelectedNuevoInstructorApiIdAcAdmis()));			
		}else
		{
			detExpTram.setValorInstructorAPI(null);
		}
		
		actualizarExpedienteConImposicionMedidas(detExpTram,traExp);

	}	
	
	public void guardarDetalleTramiteExpC003(TramiteExpediente traExp, DetalleExpdteTram detExpTram)
	{
		guardarDetalleTramiteExpDatosSalida(traExp, detExpTram);
		
		detExpTram.setFechaEnvio(traExp.getDetalleExpdteTram().getFechaEnvio());
		detExpTram.setValorTipoPlazo(valoresDominioService.obtener(traExp.getSelectedNuevoTipoPlazoId()));
		detExpTram.setPlazo(traExp.getDetalleExpdteTram().getPlazo());	
		
		detExpTram.setFechaLimite(traExp.getDetalleExpdteTram().getFechaLimite());
		detExpTram.setFechaNotificacion(traExp.getDetalleExpdteTram().getFechaNotificacion());
	}

	public void guardarDetalleTramiteExpC004(TramiteExpediente traExp, DetalleExpdteTram detExpTram)
	{
		
		detExpTram.setFechaFirma(traExp.getDetalleExpdteTram().getFechaFirma());
		
		guardarDetalleTramiteExpDatosSalida(traExp, detExpTram);
		
		detExpTram.setFechaEnvio(traExp.getDetalleExpdteTram().getFechaEnvio());
	}
	
	public void guardarDetalleTramiteExpC011(TramiteExpediente traExp, DetalleExpdteTram detExpTram)
	{
		detExpTram.setFechaEntrada(traExp.getDetalleExpdteTram().getFechaEntrada());
		guardarDetalleTramiteExpDatosEntradaInf(traExp, detExpTram);
		
		detExpTram.setFechaEnvio(traExp.getDetalleExpdteTram().getFechaEnvio());
		guardarDetalleTramiteExpDatosSalidaInf(traExp, detExpTram);
		
	}
	
	public void guardarDetalleTramiteExpC014(TramiteExpediente traExp, DetalleExpdteTram detExpTram)
	{
		
		detExpTram.setFechaInforme(traExp.getDetalleExpdteTram().getFechaInforme());
		if(traExp.getSelectedNuevoInstructorAPIId() != null)
		{
			detExpTram.setValorInstructorAPI(valoresDominioService.obtener(traExp.getSelectedNuevoInstructorAPIId()));
		}else {
			detExpTram.setValorInstructorAPI(null);
		}
		
		guardarDetalleTramiteExpDatosSalida(traExp, detExpTram);
		
	}
	
	public void guardarDetalleTramiteExpDatosSalida(TramiteExpediente traExp, DetalleExpdteTram detExpTram)
	{
		if(traExp.getSelectedNuevoCanalSalidaId() != null)
		{
			detExpTram.setValorCanalSalida(valoresDominioService.obtener(traExp.getSelectedNuevoCanalSalidaId()));
		}else {
			detExpTram.setValorCanalSalida(null);
		}
		
		detExpTram.setDatosCanalSalida(traExp.getDetalleExpdteTram().getDatosCanalSalida());
	}
	
	public void guardarDetalleTramiteExpDatosEntrada(TramiteExpediente traExp, DetalleExpdteTram detExpTram)
	{
		if(traExp.getSelectedNuevoCanalEntradaId() != null)
		{
			detExpTram.setValorCanalEntrada(valoresDominioService.obtener(traExp.getSelectedNuevoCanalEntradaId()));	
		}else {
			detExpTram.setValorCanalEntrada(null);
		}
		
		if(Constantes.C023.equals(traExp.getTipoTramite().getComportamiento()))
		{
			if(traExp.getSelectedNuevoActoRecId() != null)
			{
				detExpTram.setValorDominioActoRec(valoresDominioService.obtener(traExp.getSelectedNuevoActoRecId()));	
			}else {
				detExpTram.setValorDominioActoRec(null);
			}
		}
		detExpTram.setFechaEntrada(traExp.getDetalleExpdteTram().getFechaEntrada());
	}	
	
	public void guardarDetalleTramiteExpDatosSalidaInf(TramiteExpediente traExp, DetalleExpdteTram detExpTram)
	{
		if(traExp.getSelectedNuevoCanalInfSalidaId() != null)
		{
			detExpTram.setValorCanalInfSalida(valoresDominioService.obtener(traExp.getSelectedNuevoCanalInfSalidaId()));
		}else {
			detExpTram.setValorCanalInfSalida(null);
		}
		
		detExpTram.setDatosCanalSalida(traExp.getDetalleExpdteTram().getDatosCanalSalida());
	}
	
	public void guardarDetalleTramiteExpDatosEntradaInf(TramiteExpediente traExp, DetalleExpdteTram detExpTram)
	{
		if(traExp.getSelectedNuevoCanalInfEntradaId() != null)
		{
			detExpTram.setValorCanalInfEntrada(valoresDominioService.obtener(traExp.getSelectedNuevoCanalInfEntradaId()));	
		}else {
			detExpTram.setValorCanalInfEntrada(null);
		}
		
		detExpTram.setDatosCanalEntrada(traExp.getDetalleExpdteTram().getDatosCanalEntrada());
	}
	
	public void guardarDetalleTramiteExpInteresado(TramiteExpediente traExp, DetalleExpdteTram detExpTram)
	{
		if(traExp.getSelectedNuevoTipoInteresadoId() != null)
		{
			detExpTram.setValorTipoInteresado(valoresDominioService.obtener(traExp.getSelectedNuevoTipoInteresadoId()));	
		}else
		{
			detExpTram.setValorTipoInteresado(null);
			detExpTram.setPersonasInteresado(null);
			detExpTram.setSujetosObligadosInteresado(null);
			detExpTram.setValorDominioInteresado(null);
		}
		
		
		/**
		 * ALMACENAMOS LA IDENTIFICACION DEL USUARIO EN FUNCION DEL TIPO DE INTERESADO.
		 * */
		
		if(detExpTram.getValorTipoInteresado() != null && traExp.getSelectedNuevaIdentifInteresadoId() != null)
		{
			if(Constantes.COD_VAL_DOM_PERS.equals(detExpTram.getValorTipoInteresado().getCodigo()) || Constantes.COD_VAL_DOM_DPD.equals(detExpTram.getValorTipoInteresado().getCodigo()))
			{				
				detExpTram.setPersonasInteresado(personasService.obtener(traExp.getSelectedNuevaIdentifInteresadoId()));
			}else if (Constantes.COD_VAL_DOM_SUJOBL.equals(detExpTram.getValorTipoInteresado().getCodigo()))
			{
				detExpTram.setSujetosObligadosInteresado(sujetosObligadosService.obtener(traExp.getSelectedNuevaIdentifInteresadoId()));
			}else if (Constantes.COD_VAL_DOM_AUTCON.equals(detExpTram.getValorTipoInteresado().getCodigo()))
			{
				detExpTram.setValorDominioInteresado(valoresDominioService.obtener(traExp.getSelectedNuevaIdentifInteresadoId()));
			}
		}
	}
	
	public boolean validacionesGuardarDetalleTramExp (TramiteExpediente trEx) {
		boolean validoGuardar = true;

		/**
		 * VALIDACION OBLIGATORIEDAD CAMPOS COMPORTAMIENTO C006
		 */

		if (Constantes.C001.equals(trEx.getTipoTramite().getComportamiento()) 
				|| Constantes.C023.equals(trEx.getTipoTramite().getComportamiento()))
		{
			validoGuardar = validacionesGuardarDetalleTramExpC001C023(trEx);		
		} else if (Constantes.C005.equals(trEx.getTipoTramite().getComportamiento()))
		{
			validoGuardar = validacionesGuardarDetalleTramExpC001C023(trEx);		
		} else if (Constantes.C006.equals(trEx.getTipoTramite().getComportamiento()))
		{
			validoGuardar = validacionesGuardarDetalleTramExpC004(trEx);		
		} 
		
		else if(Constantes.C007.equals(trEx.getTipoTramite().getComportamiento()) 
				|| Constantes.C009.equals(trEx.getTipoTramite().getComportamiento()))
		{
			validoGuardar = validacionesGuardarDetalleTramExpC007C009(trEx);
		} else if(Constantes.C004.equals(trEx.getTipoTramite().getComportamiento()))
		{
			validoGuardar = validacionesGuardarDetalleTramExpC004(trEx);
		} else if(Constantes.C010.equals(trEx.getTipoTramite().getComportamiento()))
		{
			validoGuardar = validacionesGuardarDetalleTramExpC010(trEx);
		}else if(Constantes.C011.equals(trEx.getTipoTramite().getComportamiento()))
		{
			validoGuardar = validacionesGuardarDetalleTramExpC011(trEx);
		}else if(Constantes.C012.equals(trEx.getTipoTramite().getComportamiento())
				|| Constantes.C024.equals(trEx.getTipoTramite().getComportamiento()))
		{
			validoGuardar = validacionesFechaGuardarDetalleTramExpC012C024(trEx);
		}
		else if(Constantes.C003.equals(trEx.getTipoTramite().getComportamiento()))
		{
			validoGuardar = validacionesGuardarDetalleTramExpC003(trEx);
		}else if(Constantes.C008.equals(trEx.getTipoTramite().getComportamiento())) {
			validoGuardar = validacionesGuardarDetalleTramExpC019C008(trEx);	
		}else validoGuardar = validacionesGuardarDetalleTramExpAdicionales(trEx, validoGuardar);
		
				
		return validoGuardar;
	}
	

	public boolean validacionesGuardarDetalleTramExpAdicionales (TramiteExpediente trEx, boolean validoActual) {
		
		boolean validoGuardar = validoActual;

		if(Constantes.C013.equals(trEx.getTipoTramite().getComportamiento())) {
			validoGuardar = validacionesGuardarDetalleTramExpC013(trEx);	
		}
		if(Constantes.C014.equals(trEx.getTipoTramite().getComportamiento())) {
			validoGuardar = validacionesGuardarDetalleTramExpC014(trEx);
		}
		
		if(Constantes.C015.equals(trEx.getTipoTramite().getComportamiento())) {
			validoGuardar = validacionesGuardarDetalleTramExpC015(trEx);	
		}
	
		if(Constantes.C016.equals(trEx.getTipoTramite().getComportamiento())) {
			validoGuardar = validacionesGuardarDetalleTramExpC016(trEx);	
		}
		
		if(Constantes.C017.equals(trEx.getTipoTramite().getComportamiento())) {
			validoGuardar = validacionesGuardarDetalleTramExpC017(trEx);	
		}
		
		if(Constantes.C018.equals(trEx.getTipoTramite().getComportamiento())) {
			validoGuardar = validacionesGuardarDetalleTramExpC001C023(trEx);
		}
		
		if(Constantes.C019.equals(trEx.getTipoTramite().getComportamiento())) {
			validoGuardar = validacionesGuardarDetalleTramExpC019C008(trEx);	
		}
		
		if(Constantes.C020.equals(trEx.getTipoTramite().getComportamiento())) {
			validoGuardar = validacionesGuardarDetalleTramExpC020(trEx);	
		}
				
		return validoGuardar;
	}

	

	public boolean validacionesGuardarDetalleTramExpC001C023(TramiteExpediente trEx) {
		
		/**
		 * VALIDACION FECHAS COMPORTAMIENTO C001 Y C023
		 */
		
		if(trEx.getDetalleExpdteTram().getFechaEntrada() != null  &&
				(trEx.getDetalleExpdteTram().getFechaEntrada().after(FechaUtils.hoy())))
		{
			facesMsgErrorKey(FECHAENTRADANOSUPFECHAACTUAL);
			return false;
		}
			
		return validacionExtracto(trEx) && validacionAntecedentes(trEx) && validacionFechaEntradaFechaRegistro(trEx);
	}
	
	public boolean validacionFechaEntradaFechaRegistro(TramiteExpediente trEx) {
		boolean validoGuardar = true;
		DetalleExpdteTram det = trEx.getDetalleExpdteTram();
				
		if(det.getFechaEntrada() != null && det.getFechaRegistro()!= null && det.getFechaRegistro().after(det.getFechaEntrada())){
				facesMsgErrorKey(FECHAENTSUPERIORIGUALFECHAREG);
				validoGuardar = false;
		}
		if(det.getFechaEntrada() != null  &&
				(det.getFechaEntrada().after(FechaUtils.hoy())))
		{
			facesMsgErrorKey(FECHAENTRADANOSUPFECHAACTUAL);
			return false;
		}
		
		if(det.getFechaRegistro() != null  &&
				(det.getFechaRegistro().after(FechaUtils.hoy())))
		{
			facesMsgErrorKey(FECHAREGISTRONOSUPFECHAACTUAL);
			return false;
		}

		
		return validoGuardar;
	}
	
	public boolean validacionExtracto(TramiteExpediente trEx) {
		boolean validoGuardar = true;
		DetalleExpdteTram det = trEx.getDetalleExpdteTram();
		
		//Si el campo Extracto está vacío, el indicador Extracto del expediente no puede estar marcado a Si.		
		if(StringUtils.isBlank(det.getTextoExtractoExpediente()) && Boolean.TRUE.equals(det.getExtractoExpediente())) {
				facesMsgErrorKey(INDICADOREXTRACTOMARCADO);
				validoGuardar = false;
		}
		
		return validoGuardar;
	}
	
	public boolean validacionAntecedentes(TramiteExpediente trEx) {
		boolean validoGuardar = true;
		DetalleExpdteTram det = trEx.getDetalleExpdteTram();
		
		//Si el campo Antecedentes está vacío, el indicador Antecedentes del expediente no puede estar marcado a Si.
		if(StringUtils.isBlank(det.getTextoAntecedentesExpediente()) && Boolean.TRUE.equals(det.getAntecedentesExpediente())) {
				facesMsgErrorKey(INDICADORANTECEDENTESMARCADO);
				validoGuardar = false;
		}
		
		return validoGuardar;
	}
		
	
	
	public boolean validacionesGuardarDetalleTramExpC019C008(TramiteExpediente trEx)
	{
		boolean validoGuardar = true;
	
		/**
		 * VALIDACION FECHAS COMPORTAMIENTO C019 Y C008
		 */
			if(trEx.getDetalleExpdteTram().getFechaInforme() != null  &&
					(trEx.getDetalleExpdteTram().getFechaInforme().after(FechaUtils.hoy())))
			{
				facesMsgErrorKey(FECHAACUERDONOSUPFECHAACTUAL);
				return false;
			}
	
				
		if(trEx.getSelectedNuevoTipoAdmisionId() != null)
		{
			ValoresDominio valDom = valoresDominioService.obtener(trEx.getSelectedNuevoTipoAdmisionId());
			if(valDom.getCodigo().equals(Constantes.COD_VAL_DOM_INA) && (trEx.getSelectedNuevoMotivoInadmisionId() == null) && !trEx.getTipoTramite().getCodigo().equals(Constantes.TIP_TRAM_ACADMHE))
			{
				facesMsgErrorKey(MOTIVOINADMISIONOBLIG);
				validoGuardar = false;
			}
		}
		
		return validoGuardar;
	}
	
	public boolean validacionesGuardarDetalleTramExpC015(TramiteExpediente trEx)
	{
	
		/**
		 * VALIDACION FECHAS COMPORTAMIENTO C015
		 */
		
		if(trEx.getDetalleExpdteTram().getFechaEnvio() != null  &&
				(trEx.getDetalleExpdteTram().getFechaEnvio().after(FechaUtils.hoy())))
		{
			facesMsgErrorKey(FECHAENVIONOSUPFECHAACTUAL);
			return false;
		}
		
		if(trEx.getDetalleExpdteTram().getFechaRespuesta() != null  &&
				(trEx.getDetalleExpdteTram().getFechaRespuesta().after(FechaUtils.hoy())))
		{
			facesMsgErrorKey(FECHARESPNOSUPFECHAACTUAL);
			return false;
		}
	
		if((trEx.getDetalleExpdteTram().getFechaRespuesta() != null && trEx.getDetalleExpdteTram().getFechaEnvio() != null) &&
				(trEx.getDetalleExpdteTram().getFechaRespuesta().before(trEx.getDetalleExpdteTram().getFechaEnvio())))
		{
			facesMsgErrorKey(FECHARESPPOSTERIORFECHAENV);
			return false;
		}

		return true;
	}
	
	public boolean validacionesGuardarDetalleTramExpC016(TramiteExpediente trEx)
	{
	
		/**
		 * VALIDACION FECHAS COMPORTAMIENTO C016
		 */
		
		if(trEx.getDetalleExpdteTram().getFechaEnvio() != null  &&
				(trEx.getDetalleExpdteTram().getFechaEnvio().after(FechaUtils.hoy())))
		{
			facesMsgErrorKey(FECHAENVIONOSUPFECHAACTUAL);
			return false;
		}
		
		if(trEx.getDetalleExpdteTram().getFechaFirma() != null  &&
				(trEx.getDetalleExpdteTram().getFechaFirma().after(FechaUtils.hoy())))
		{
			facesMsgErrorKey(FECHAFIRMANOSUPFECHAACTUAL);
			return false;
		}
	
		if((trEx.getDetalleExpdteTram().getFechaFirma() != null && trEx.getDetalleExpdteTram().getFechaEnvio() != null) &&
				(trEx.getDetalleExpdteTram().getFechaFirma().before(trEx.getDetalleExpdteTram().getFechaEnvio())))
		{
			facesMsgErrorKey(FECHAFIRMANOANTERIORFECHAENV);
			return false;
		}

		return true;
	}
	

	
	public boolean validacionesGuardarDetalleTramExpC020(TramiteExpediente trEx)
	{
	
		/**
		 * VALIDACION FECHAS COMPORTAMIENTO C020
		 */
		
		if(trEx.getDetalleExpdteTram().getFechaInforme() != null  &&
				(trEx.getDetalleExpdteTram().getFechaInforme().after(FechaUtils.hoy())))
		{
			facesMsgErrorKey(FECHAACUERDONOSUPFECHAACTUAL);
			return false;
		}
	
		return true;
	}
	
	
	
	public String abrirAltaResolucion(TramiteExpediente trEx, String tipoOperacion)
	{
		calculaMensajeConfirmacion(trEx);
		
		if(validacionesGuardarDetalleTramExpC012C024(trEx, tipoOperacion))
		{
			limpiarSerie(trEx);

			String nombreDialog = "";
			if(trEx.getTipoTramite().getComportamiento().equals(Constantes.C012))
			{
				nombreDialog = "dialogCrearResolucionC012-";
			}else {
				nombreDialog = "dialogCrearResolucionC024-";	
			}
			
			PrimeFaces.current().executeScript("PF('" + nombreDialog + trEx.getId() + COMANDOSHOW);
		}
		
		return "";
	}
	
	public boolean validacionesFechaGuardarDetalleTramExpC012C024(TramiteExpediente trEx)
	{
	
		/**
		 * VALIDACION FECHAS COMPORTAMIENTO C012
		 */

		if(trEx.getDetalleExpdteTram().getFechaResolucion() != null  &&
				(trEx.getDetalleExpdteTram().getFechaResolucion().after(FechaUtils.hoy())))
		{
			facesMsgErrorKey(FECHARESOLNOSUPFECHAACTUAL);
			return false;
		}
				
		return true;
	}
	
	
	public boolean validacionesGuardarDetalleTramExpC012C024(TramiteExpediente trEx, String tipoOperacion)
	{
		boolean validoGuardar = true;
	
		/**
		 * VALIDACION FECHAS COMPORTAMIENTO C012
		 */

		if(trEx.getDetalleExpdteTram().getFechaResolucion() != null  &&
				(trEx.getDetalleExpdteTram().getFechaResolucion().after(FechaUtils.hoy())))
		{
			facesMsgErrorKey(FECHARESOLNOSUPFECHAACTUAL);
			return false;
		}
		
		if (trEx.getSelectedNuevoSentidoResolucionId() == null ){

			facesMsgErrorKey(SENTIDORESOLOBLIG);
			return false;
		}
		
		if (trEx.getSelectedNuevoTipoResolucionId() == null){

			facesMsgErrorKey(TIPORESOLOBLIG);
			return false;
		}		
		
		if(FINALIZARRESOLUCION.equals(tipoOperacion))
		{
			if (trEx.getDetalleExpdteTram().getFechaResolucion() == null){
				facesMsgErrorKey(FECHARESOLOBLIG);
				return false;
			}

			if (trEx.getDetalleExpdteTram().getValorSentidoResolucion() == null){

				facesMsgErrorKey(SENTIDORESOLOBLIG);
				return false;
			}
			
			if (trEx.getDetalleExpdteTram().getValorTipoResolucion() == null){

				facesMsgErrorKey(TIPORESOLOBLIG);
				return false;
			}
			
			if (trEx.getDetalleExpdteTram().getNumResolucion() == null ||  trEx.getDetalleExpdteTram().getNumResolucion().isEmpty()) {

				facesMsgErrorKey(NUMRESOLOBLIG);
				validoGuardar = false;
			}
			
		}
		
		
		return validoGuardar;
	}
	
	
	public boolean validacionesGuardarDetalleTramExpC010 (TramiteExpediente trEx) {
		
		/**
		 * VALIDACION FECHAS COMPORTAMIENTO C010
		 */
		
		if(trEx.getDetalleExpdteTram().getFechaInforme() != null  &&
				(trEx.getDetalleExpdteTram().getFechaInforme().after(FechaUtils.hoy())))
		{
			facesMsgErrorKey(FECHAINFORMENOSUPFECHAACTUAL);
			return false;
		}
			
		return true ;
	}
	
	public boolean validacionesGuardarDetalleTramExpC013(TramiteExpediente trEx)
	{
		boolean validoGuardar = true;
	
		if (trEx.getDetalleExpdteTram().getValorTipoPlazo() == null){
			facesMsgErrorKey(CAMPOSOBLIGATORIOS);
			validoGuardar = false;
		}

		/**
		 * VALIDACION FECHAS COMPORTAMIENTO C013
		 */
		
		if(trEx.getDetalleExpdteTram().getFechaEnvio() != null  &&
				(trEx.getDetalleExpdteTram().getFechaEnvio().after(FechaUtils.hoy())))
		{
			facesMsgErrorKey(FECHAENVIONOSUPFECHAACTUAL);
			return false;
		}
	
		if(trEx.getDetalleExpdteTram().getFechaNotificacion() != null  &&
				(trEx.getDetalleExpdteTram().getFechaNotificacion().after(FechaUtils.hoy())))
		{
			facesMsgErrorKey(FECHANOTIFNOSUPFECHAACTUAL);
			return false;
		}
		
		if(trEx.getDetalleExpdteTram().getFechaAcreditacion() != null  &&
				(trEx.getDetalleExpdteTram().getFechaAcreditacion().after(FechaUtils.hoy())))
		{
			facesMsgErrorKey(FECHAACREDNOSUPFECHAACTUAL);
			return false;
		}

		
		if((trEx.getDetalleExpdteTram().getFechaEnvio() != null && trEx.getDetalleExpdteTram().getFechaNotificacion() != null) &&
				(trEx.getDetalleExpdteTram().getFechaNotificacion().before(trEx.getDetalleExpdteTram().getFechaEnvio())))
		{
			facesMsgErrorKey(FECHANOTIFNOANTERIORFECHAENV);
			validoGuardar = false;
		}
		
		if((trEx.getDetalleExpdteTram().getFechaAcreditacion() != null && trEx.getDetalleExpdteTram().getFechaNotificacion() != null) &&
				(trEx.getDetalleExpdteTram().getFechaAcreditacion().before(trEx.getDetalleExpdteTram().getFechaNotificacion())))
		{
			facesMsgErrorKey(FECHAACREDITNOANTERIORFECHANOTIF);
			validoGuardar = false;
		}
		
	    return validoGuardar;
	}

	public boolean validacionesFinalizarDetalleTramExpC013(TramiteExpediente trEx)
	{
		boolean validoGuardar = true;
	
		if (trEx.getDetalleExpdteTram().getValorTipoPlazo() == null){
			facesMsgErrorKey(CAMPOSOBLIGATORIOS);
			validoGuardar = false;
		}

		/**
		 * VALIDACION FECHAS COMPORTAMIENTO C013
		 */
		
		if((trEx.getDetalleExpdteTram().getFechaEnvio() != null && trEx.getDetalleExpdteTram().getFechaNotificacion() != null) &&
				(trEx.getDetalleExpdteTram().getFechaNotificacion().before(trEx.getDetalleExpdteTram().getFechaEnvio())))
		{
			facesMsgErrorKey(FECHANOTIFNOANTERIORFECHAENV);
			validoGuardar = false;
		}
		
		if((trEx.getDetalleExpdteTram().getFechaAcreditacion() != null && trEx.getDetalleExpdteTram().getFechaNotificacion() != null) &&
				(trEx.getDetalleExpdteTram().getFechaAcreditacion().before(trEx.getDetalleExpdteTram().getFechaNotificacion())))
		{
			facesMsgErrorKey(FECHAACREDITNOANTERIORFECHANOTIF);
			validoGuardar = false;
		}

		if (!validarResultadoNotificacion(trEx)){
			return false;
		}
		
	    return validoGuardar;
	}


	
	public boolean validacionesGuardarDetalleTramExpC017(TramiteExpediente trEx)
	{
		boolean validoGuardar = true;
	
		if (trEx.getDetalleExpdteTram().getValorTipoPlazo() == null){
			facesMsgErrorKey(CAMPOSOBLIGATORIOS);
			validoGuardar = false;
		}

		/**
		 * VALIDACION FECHAS COMPORTAMIENTO C017
		 */
		
		
		if((trEx.getDetalleExpdteTram().getFechaEntrada() != null && trEx.getDetalleExpdteTram().getFechaEnvio() != null) &&
				(trEx.getDetalleExpdteTram().getFechaEnvio().before(trEx.getDetalleExpdteTram().getFechaEntrada())))
		{
			facesMsgErrorKey(FECHAENTRADANOSUPFECHAACTUAL);
			validoGuardar = false;
		}		
		
		if((trEx.getDetalleExpdteTram().getFechaEnvio() != null && trEx.getDetalleExpdteTram().getFechaNotificacion() != null) &&
				(trEx.getDetalleExpdteTram().getFechaNotificacion().before(trEx.getDetalleExpdteTram().getFechaEnvio())))
		{
			facesMsgErrorKey(FECHANOTIFNOANTERIORFECHAENV);
			validoGuardar = false;
		}
		
		if (!validarResultadoNotificacion(trEx)){
			return false;
		}
				
				
	    return validoGuardar;
	}
	
	public boolean validacionesGuardarDetalleTramExpC003 (TramiteExpediente trEx) {
		
		boolean validoGuardar = true;

		if(trEx.getDetalleExpdteTram().getFechaEnvio() != null  &&
				(trEx.getDetalleExpdteTram().getFechaEnvio().after(FechaUtils.hoy())))
		{
			facesMsgErrorKey(FECHAENVIONOSUPFECHAACTUAL);
			return false;
		}
	
		if(trEx.getDetalleExpdteTram().getFechaNotificacion() != null  &&
				(trEx.getDetalleExpdteTram().getFechaNotificacion().after(FechaUtils.hoy())))
		{
			facesMsgErrorKey(FECHANOTIFNOSUPFECHAACTUAL);
			return false;
		}
		
		
		if((trEx.getDetalleExpdteTram().getFechaEnvio() != null && trEx.getDetalleExpdteTram().getFechaNotificacion() != null) &&
				(trEx.getDetalleExpdteTram().getFechaNotificacion().before(trEx.getDetalleExpdteTram().getFechaEnvio())))
		{
			facesMsgErrorKey(FECHANOTIFNOANTERIORFECHAENV);
			validoGuardar = false;
		}
				
	    return validoGuardar;

	}
	
	private boolean validacionesGuardarDetalleTramExpC007C009 (TramiteExpediente trEx) {
		boolean validoGuardar = true;

		/**
		 * VALIDACION OBLIGATORIEDAD CAMPOS COMPORTAMIENTO C007, C009 Y C013
		 */

		if (trEx.getDetalleExpdteTram().getValorTipoPlazo() == null){
			validoGuardar = false;
			facesMsgErrorKey(CAMPOSOBLIGATORIOS);
		}

		if(trEx.getDetalleExpdteTram().getFechaEnvio() != null  &&
				(trEx.getDetalleExpdteTram().getFechaEnvio().after(FechaUtils.hoy())))
		{
			facesMsgErrorKey(FECHAENVIONOSUPFECHAACTUAL);
			return false;
		}
	
		if(trEx.getDetalleExpdteTram().getFechaNotificacion() != null  &&
				(trEx.getDetalleExpdteTram().getFechaNotificacion().after(FechaUtils.hoy())))
		{
			facesMsgErrorKey(FECHANOTIFNOSUPFECHAACTUAL);
			return false;
		}
		
		if(trEx.getDetalleExpdteTram().getFechaSubsanacion() != null  &&
				(trEx.getDetalleExpdteTram().getFechaSubsanacion().after(FechaUtils.hoy())))
		{
			facesMsgErrorKey(FECHARESPNOSUPFECHAACTUAL);
			return false;
		}

		if(trEx.getDetalleExpdteTram().getFechaRespuesta() != null  &&
				(trEx.getDetalleExpdteTram().getFechaRespuesta().after(FechaUtils.hoy())))
		{
			facesMsgErrorKey(FECHARESPNOSUPFECHAACTUAL);
			return false;
		}

		
		if((trEx.getDetalleExpdteTram().getFechaEnvio() != null && trEx.getDetalleExpdteTram().getFechaNotificacion() != null) &&
				(trEx.getDetalleExpdteTram().getFechaNotificacion().before(trEx.getDetalleExpdteTram().getFechaEnvio())))
		{
			facesMsgErrorKey(FECHANOTIFNOANTERIORFECHAENV);
			validoGuardar = false;
		}
		
		if((trEx.getDetalleExpdteTram().getFechaSubsanacion() != null && trEx.getDetalleExpdteTram().getFechaNotificacion() != null) &&
				(trEx.getDetalleExpdteTram().getFechaSubsanacion().before(trEx.getDetalleExpdteTram().getFechaNotificacion())))
		{
			facesMsgErrorKey(FECHARESPNOANTERIORFECHANOTIF);
			validoGuardar = false;
		}

		if((trEx.getDetalleExpdteTram().getFechaRespuesta() != null && trEx.getDetalleExpdteTram().getFechaNotificacion() != null) &&
				(trEx.getDetalleExpdteTram().getFechaRespuesta().before(trEx.getDetalleExpdteTram().getFechaNotificacion())))
		{
			facesMsgErrorKey(FECHARESPNOANTERIORFECHANOTIF);
			validoGuardar = false;
		}

		return validoGuardar;
	}

	
	public boolean validacionesGuardarDetalleTramExpC004 (TramiteExpediente trEx) {
		
	
		if(trEx.getDetalleExpdteTram().getFechaEnvio() != null  &&
				(trEx.getDetalleExpdteTram().getFechaEnvio().after(FechaUtils.hoy())))
		{
			facesMsgErrorKey(FECHAENVIONOSUPFECHAACTUAL);
			return false;
			
		}
	
		return true;
	}
	
	public boolean validacionesGuardarDetalleTramExpC011 (TramiteExpediente trEx) {
		boolean validoGuardar = true;

		//Fecha s de entrada y envío informadas y en orden (entrada <= envío)
		Date fEntrada = trEx.getDetalleExpdteTram().getFechaEntrada();
		Date fEnvio = trEx.getDetalleExpdteTram().getFechaEnvio();
		
		
		if(trEx.getDetalleExpdteTram().getFechaEntrada() != null  &&
				(trEx.getDetalleExpdteTram().getFechaEntrada().after(FechaUtils.hoy())))
		{
			facesMsgErrorKey(FECHAENTRADANOSUPFECHAACTUAL);
			return false;
			
		}
	
		
		if(trEx.getDetalleExpdteTram().getFechaEnvio() != null  &&
				(trEx.getDetalleExpdteTram().getFechaEnvio().after(FechaUtils.hoy())))
		{
			facesMsgErrorKey(FECHARESPNOSUPFECHAACTUAL);
			return false;
			
		}
		

		if(fEntrada != null && fEnvio != null && FechaUtils.antes(fEnvio, fEntrada)) {
			facesMsgErrorKey(FECHAENVIONOANTERIORFECHAENTRADA);
			validoGuardar = false;
		}

		return validoGuardar;
	}
	
	public boolean validacionesGuardarDetalleTramExpC014(TramiteExpediente trEx) {
		boolean validoGuardar = true;
			
		Date fechaInforme = trEx.getDetalleExpdteTram().getFechaInforme();
		if(fechaInforme != null && fechaInforme.after(FechaUtils.hoy())){
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "La "+trEx.getCfgMetadatosTram().getFechaInforme()+" "+mensajesProperties.getString("fecha.informe.igual.anterior.actual.parte2"));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			validoGuardar = false;
		}			
				
	    return validoGuardar;
	}
	
	public boolean validacionesFinalizarDetalleTramExpC014 (TramiteExpediente trEx) {
		boolean validoGuardar = true;		
		String errorFechaInforme = "";
		String errorNumeroPsan = "";
		String errorInstructor = "";
		String errorFechaInformePsan = "";
		String mensajeFinal = "";
		
		Date fechaInforme = trEx.getDetalleExpdteTram().getFechaInforme();
		if(fechaInforme != null && fechaInforme.after(FechaUtils.hoy())){
			errorFechaInforme = "La "+trEx.getCfgMetadatosTram().getFechaInforme()+" "+mensajesProperties.getString("fecha.informe.igual.anterior.actual.parte2");
			validoGuardar = false;
		}
		
		if(!trEx.getExpediente().getValorTipoExpediente().getCodigo().equals(Constantes.PSAN) && trEx.getDetalleExpdteTram().getNumeroPsan() == null) {
			errorNumeroPsan = mensajesProperties.getString(FINALIZARCOMPORTAMIENTO14CAMPO)+trEx.getCfgMetadatosTram().getNumeroPsan()+" "+mensajesProperties.getString(FINALIZARCOMPORTAMIENTO14VALOR);
			validoGuardar = false;
		}
		
		if(trEx.getDetalleExpdteTram().getValorInstructorAPI() == null) {
			errorInstructor = mensajesProperties.getString(FINALIZARCOMPORTAMIENTO14CAMPO)+trEx.getCfgMetadatosTram().getInstructorAPI()+" "+mensajesProperties.getString(FINALIZARCOMPORTAMIENTO14VALOR);
			validoGuardar = false;
		}
		
		if(trEx.getExpediente().getValorTipoExpediente().getCodigo().equals(Constantes.PSAN) && fechaInforme == null) {
			errorFechaInformePsan = mensajesProperties.getString(FINALIZARCOMPORTAMIENTO14CAMPO)+trEx.getCfgMetadatosTram().getFechaInforme()+" "+mensajesProperties.getString(FINALIZARCOMPORTAMIENTO14VALOR);
			validoGuardar = false;
		}
		
		if(!errorFechaInforme.isBlank()) {
			mensajeFinal = errorFechaInforme;
		} 
		if(!errorNumeroPsan.isBlank()){
			mensajeFinal += "\n"+errorNumeroPsan;
		}
		if(!errorInstructor.isBlank()) {
			mensajeFinal += "\n"+errorInstructor;
		}
		if(!errorFechaInformePsan.isBlank()) {
			mensajeFinal += "\n"+errorFechaInformePsan;
		}
		if(!mensajeFinal.isBlank()) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajeFinal);
			PrimeFaces.current().dialog().showMessageDynamic(message);
		}
		
		return validoGuardar;
	}

	public boolean validacionesGuardarTramExp(TramiteExpediente trEx) {

		/**
		 * VALIDACION OBLIGATORIEDAD CAMPOS COMUNES
		 */

		if (trEx.getDescripcion() == null || trEx.getDescripcion().isEmpty() || trEx.getSelectedNuevoResponsableId() == null) {
			facesMsgErrorKey(CAMPOSOBLIGATORIOS);
			return false;
		}
		
		return true;
	}

	
	public String guardarSubTramite(TramiteExpediente subTramExpediente) {
		boolean guardadoOk = false;
		boolean descCambiada = subTramExpediente.cambiaDescripcion();
		
		subTramExpediente.setResponsable(responsablesTramitacionService.obtener(subTramExpediente.getSelectedNuevoResponsableId()));
		boolean mostrarMsgCambioResp = subTramExpediente.getId() != null && subTramExpediente.cambiaResponsable();
		String msgCambioResp = null;
		
		if(mostrarMsgCambioResp) {
			final String msg = getMessage("aviso.responsable.subtramite.cambiado");
			final String respActual = responsablesTramitacionService.obtenerDescripcion(subTramExpediente.getResponsableActual().getId());
			final String respNuevo = subTramExpediente.getResponsable().getDescripcion();
			msgCambioResp = MessageFormat.format(msg, respActual, respNuevo);
		}

		try {
			guardadoOk = guardarSubTramiteAux(subTramExpediente);
			if(!accionesAntesFinalizarSubTramite(subTramExpediente)) {
				return "";
			}
			
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}

		if(guardadoOk) {
			if(descCambiada) {
				PrimeFaces.current().ajax().addCallbackParam("refrescarDocs", true);
			}
			
			if(mostrarMsgCambioResp) {
				facesMsgInfo(msgCambioResp); //para este mensaje usamos el diálogo deliberadamente
			}				
			
			FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"", SUBTRAMITEMSJ + " " + mensajesProperties.getString(ACTUALIZADOCORRECTAMENTE)));
	
			PrimeFaces.current().ajax().update(FORMFORMULARIOEXPEDIENTES);
			
			inicializarCampos();
			
			datosExpedientesBean.actualizarCabecera(expedientes, null, null, null);
					
		}
		
		return "";
	}

	private boolean guardarSubTramiteAux(TramiteExpediente subTramExpediente) throws BaseException {
			
		aplicarDetalleTramite(subTramExpediente, subTramExpediente.getDetalleExpdteTram());
		aplicarFechaTramiteInfoRelevante(subTramExpediente, subTramExpediente.getDetalleExpdteTram());
		aplicarSituacionAdicional(subTramExpediente, subTramExpediente.getDetalleExpdteTram());
		
		
		/**
		 * GUARDAMOS EL TRAMITE EXPEDIENTE
		 */
		if (validacionesGuardarTramExp(subTramExpediente)) {
			
			ObservacionesExpedientes obsExp = subTramExpediente.getObservaciones();
			obsExp = observacionesExpedientesService.guardarObservacionesExpedientes(obsExp, obsExp.getTexto(), Constantes.COD_VAL_DOM_TIPOBS_TRA, expedientes);
			subTramExpediente.setObservaciones(obsExp);
			subTramExpediente.setResponsable(responsablesTramitacionService.obtener(subTramExpediente.getSelectedNuevoResponsableId()));
			boolean esResponsableTramiteUsuarioConectado = usuariosResponsablesService.esResponsableDeUsuario(subTramExpediente.getResponsable().getId(), sesionBean.getIdUsuarioSesion());
			subTramExpediente.setMostrarBotonFinalizar(esResponsableTramiteUsuarioConectado);
			subTramExpediente = tramiteExpedienteService.guardar(subTramExpediente);
			obsExp.setTramiteExpdte(subTramExpediente);
			observacionesExpedientesService.guardar(obsExp);	
			
			expedientes = utilsComun.expedienteUltimaModificacion(expedientes,
					subTramExpediente.getFechaModificacion(), subTramExpediente.getFechaCreacion(),
					subTramExpediente.getUsuModificacion(), subTramExpediente.getUsuCreacion());

		} else {
			return false;
		}

		/**
		 * GUARDARMOS EL DETALLE DEL TRAMITE EN FUNCION DEL COMPORTAMIENTO:
		 */
		if (subTramExpediente.getDetalleExpdteTram() != null
				&& validacionesGuardarDetalleTramExp(subTramExpediente)) {
			guardarDetalleTramiteExp(subTramExpediente);
		} else { 
			return false;
		}

		return true;
	}
	
	@Transactional(TxType.REQUIRED)
	public String finalizarEliminarTramite(TramiteExpediente tramExp, String tipoOperacion) {

		/**
		 * ELIMINAR TRAMITE: PARA ELIMINAR UN TRAMITE TIENEN QUE ESTAR ELIMINADOS LOS
		 * SUBTRAMITES ASOCIADOS. FINALIZAR TRAMITE: LOS SUBTRAMITES TIENEN QUE ESTAR
		 * FINALIZADOS O ELIMINADOS.
		 */

		List<TramiteExpediente> listaSubTramitesAsociadosActivos = tramiteExpedienteService
				.findSubTramExpByTramExpActivos(tramExp.getId());

		List<TramiteExpediente> listaSubTramitesAsociadosActivosNoFinalizados = new ArrayList<>();

		if (listaSubTramitesAsociadosActivos != null && !listaSubTramitesAsociadosActivos.isEmpty()) {
			for (TramiteExpediente tE : listaSubTramitesAsociadosActivos) {
				if (Boolean.FALSE.equals(tE.getFinalizado())) {
					listaSubTramitesAsociadosActivosNoFinalizados.add(tE);
				}
			}
		}

		List<TramiteExpediente> listaTramitesBD = tramiteExpedienteService
				.findTipoTramTramitesExpAbiertos(expedientes.getId());

		Boolean haySubTramites = tramExp.tieneSubTramites();

		if (tipoOperacion.equals(ELIMINAR)) {
			eliminarTramite(listaSubTramitesAsociadosActivosNoFinalizados, listaTramitesBD,
					haySubTramites, tramExp);
		} else if (tipoOperacion.equals(FINALIZAR)) {
			return finalizarTramite(listaSubTramitesAsociadosActivosNoFinalizados, listaTramitesBD,
					haySubTramites, tramExp);
		}

		return "";
	}

	public void eliminarTramite(List<TramiteExpediente> listaSubTramitesAsociadosActivos,List<TramiteExpediente> listaTramitesBD, Boolean existeSubTramitesAsociadosTemporales,TramiteExpediente tramExp) {

		if (!listaSubTramitesAsociadosActivos.isEmpty() || Boolean.TRUE.equals(existeSubTramitesAsociadosTemporales)) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",mensajesProperties.getString("eliminar.subtramites.activos"));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			PrimeFaces.current().ajax().update(FORMFORMULARIOEXPEDIENTES);
		} else if (0 != this.documentosExpedientesTramitesService.countDocExpTramByIdTramExp(tramExp.getId())){
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",mensajesProperties.getString("eliminar.tiene.docs"));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			PrimeFaces.current().ajax().update(FORMFORMULARIOEXPEDIENTES);
		
		} else
		{
			for (TramiteExpediente te : listaTramitesBD) {
				eliminarTramiteForAux(te,tramExp);
			}

			FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, new FacesMessage(FacesMessage.SEVERITY_INFO, "", TRAMITEMSJ + mensajesProperties.getString(ELIMINADOCORRECTAMENTE)));

			PrimeFaces.current().ajax().update(FORMFORMULARIOEXPEDIENTES);
			inicializarCampos();
			
			datosExpedientesBean.actualizarSituacionAdicional(tramExp.getExpediente().getId());
			
			if(Constantes.TIP_TRAM_SUB.equals(tramExp.getTipoTramite().getCodigo())){
				actualizarPlazosExpdte(tramExp);
			}else{
				datosExpedientesBean.actualizarCabecera(expedientes, null, null, null);
			}
		}

	}
	
	/** LA FUNCION RECALCULA LOS PLAZOS DE ACUERDO Y RESOLUCION ASOCIADOS AL EXPEDIENTE EN FUNCIÓN DE LOS TIPOS DE TRÁMITES ASOCIADOS AL MISMO. */
	public void actualizarPlazosExpdte(TramiteExpediente tramExp)	{
		try {			
			datosExpedientesBean.limpiarPanelPlazos();
			
			if(Constantes.TIP_TRAM_SUB.equals(tramExp.getTipoTramite().getCodigo()) || Constantes.TIP_TRAM_REQINF.equals(tramExp.getTipoTramite().getCodigo()))	{
				
				/**EN PRIMER LUGAR, CALCULAMOS EL PLAZO ADICIONAL EN FUNCIÓN DEL TIPO DE LOS TIPOS DE TRAMITES DEL EXPEDIENTE.*/
				Integer plazoAdicional = calcularPlazoAdicional(expedientes);
								
				//-------------------
				//Generamos plazos para el expediente 
				//Plazo de acuerdo				
				Date fechaLimite = null;
				/** EL PLAZO DE ACUERDO ASOCIADO AL EXPEDIENTE SE CALCULA EN BASE AL PLAZO ADICIONAL. */			
				if ((tramExp.getExpediente().getValorTipoExpediente().getCodigo().equals("RCE")) || (tramExp.getExpediente().getValorTipoExpediente().getCodigo().equals("RCO"))) {
					fechaLimite = utilsComun.generarPlazoExpdte(tramExp.getId(), null,	expedientes.getFechaEntrada(), Constantes.COD_VAL_DOM_ACU, Constantes.COD_VAL_DOM_DN, plazoAdicional, "A", expedientes);
				}
				
				if ((tramExp.getExpediente().getValorTipoExpediente().getCodigo().equals("PSAN"))) {
					fechaLimite = expedientes.getFechaEntrada();
				}
					
				//Plazo de resolución
				/** EL PLAZO DE RESOLUCION ASOCIADO AL EXPEDIENTE SE CALCULA EN BASE AL PLAZO DE ACUERDO. */			
				utilsComun.generarPlazoExpdte(tramExp.getId(), null, fechaLimite,Constantes.COD_VAL_DOM_RES, Constantes.COD_VAL_DOM_DN, 0, "A", expedientes);

				datosExpedientesBean.limpiarPanelPlazos();
				List<PlazosExpdte> plazosExpdte = plazosExpdteService.plazosExpdteActivosNoCumplidosByExpediente(expedientes.getId());
				if(!plazosExpdte.isEmpty()) {
					datosExpedientesBean.actualizarCabecera(expedientes, null, null, plazosExpdte);
				}else {
					datosExpedientesBean.actualizarCabecera(expedientes, null, null, null);
				}			
			}
			if (tramExp.getTipoTramite().getComportamiento().equals(Constantes.C008))
			{
				actualizarPlazosExpdteC008(tramExp);
			}
			actualizarPlazosExpdteC012(tramExp);
			actualizarPlazosExpdteC024(tramExp);
			actualizarPlazosExpdteC014(tramExp);
			actualizarPlazosExpdteC023(tramExp);
						
		} catch (final BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}	
	}
	
	private void actualizarPlazosExpdteC014 (TramiteExpediente tramExp) throws BaseException {
		if(Constantes.C014.equals(tramExp.getTipoTramite().getComportamiento())){
			/** GENERAMOS EL PLAZO ASOCIADO A LA RESOLUCION. */			
			utilsComun.generarPlazoExpdte(tramExp.getId(),null,tramExp.getDetalleExpdteTram().getFechaInforme(),Constantes.COD_VAL_DOM_RES, Constantes.COD_VAL_DOM_DN, 0, "A", expedientes);

			datosExpedientesBean.limpiarPanelPlazos();
			List<PlazosExpdte> plazosExpdte = plazosExpdteService.plazosExpdteActivosNoCumplidosByExpediente(expedientes.getId());
			if(!plazosExpdte.isEmpty()) {
				datosExpedientesBean.actualizarCabecera(expedientes, null, null, plazosExpdte);
			}else {
				datosExpedientesBean.actualizarCabecera(expedientes, null, null, null);
			}				
			datosExpedientesBean.actualizarCabecera(expedientes, null, null, null);
		}
	}
	
	
	private void actualizarPlazosExpdteC012 (TramiteExpediente tramExp) throws BaseException {
		if(Constantes.C012.equals(tramExp.getTipoTramite().getComportamiento())){
			
			PlazosExpdte plazosRES = plazosExpdteService.findPlazosExpdteByExpTipPla(expedientes.getId(),valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_TIPO_PLAZO, Constantes.COD_VAL_DOM_RES).getId());


			if(plazosRES != null) {
			
			/** GENERAMOS PLAZO ASOCIADO LA RESOLUCION. */
			utilsComun.generarPlazoExpdte(tramExp.getId(),"FIN",null,Constantes.COD_VAL_DOM_RES, Constantes.COD_VAL_DOM_DN,	0, "A",expedientes);
						
			datosExpedientesBean.limpiarPanelPlazos();
			List<PlazosExpdte> plazosExpdte = plazosExpdteService.plazosExpdteActivosNoCumplidosByExpediente(expedientes.getId());
			if(!plazosExpdte.isEmpty()) {
				datosExpedientesBean.actualizarCabecera(expedientes, null, null, plazosExpdte);
			}else {
				datosExpedientesBean.actualizarCabecera(expedientes, null, null, null);
			}				
			datosExpedientesBean.actualizarCabecera(expedientes, null, null, null);
			
			}
		}
	}

	
	private void actualizarPlazosExpdteC024 (TramiteExpediente tramExp) throws BaseException {
		if(Constantes.C024.equals(tramExp.getTipoTramite().getComportamiento())){
			
			PlazosExpdte plazosREC = plazosExpdteService.findPlazosExpdteByExpTipPla(expedientes.getId(),valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_TIPO_PLAZO, Constantes.COD_VAL_DOM_REC).getId());


			if(plazosREC != null) {
			
			/** GENERAMOS PLAZO ASOCIADO LA RESOLUCION. */
			utilsComun.generarPlazoExpdte(tramExp.getId(),"FIN",null,Constantes.COD_VAL_DOM_REC, Constantes.COD_VAL_DOM_DN,	0, "A",expedientes);
						
			datosExpedientesBean.limpiarPanelPlazos();
			List<PlazosExpdte> plazosExpdte = plazosExpdteService.plazosExpdteActivosNoCumplidosByExpediente(expedientes.getId());
			if(!plazosExpdte.isEmpty()) {
				datosExpedientesBean.actualizarCabecera(expedientes, null, null, plazosExpdte);
			}else {
				datosExpedientesBean.actualizarCabecera(expedientes, null, null, null);
			}
			
			aplicarSituacionAdicional(tramExp, tramExp.getDetalleExpdteTram());
			
			datosExpedientesBean.actualizarCabecera(expedientes, null, null, null);
			
			}
		}
	}

	
	private void actualizarPlazosExpdteC008 (TramiteExpediente tramExp) throws BaseException {

			PlazosExpdte plazosACU = plazosExpdteService.findPlazosExpdteByExpTipPla(expedientes.getId(),valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_TIPO_PLAZO, Constantes.COD_VAL_DOM_ACU).getId());
			
			PlazosExpdte plazosPRSC = plazosExpdteService.findPlazosExpdteByExpTipPla(expedientes.getId(),valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_TIPO_PLAZO, Constantes.COD_VAL_DOM_PRSC).getId());

			if(plazosACU != null) {
				/** CERRAMOS PLAZO ASOCIADO AL ACUERDO. */
				utilsComun.generarPlazoExpdte(tramExp.getId(),"FIN",null,Constantes.COD_VAL_DOM_ACU, Constantes.COD_VAL_DOM_DN,	0, "A",expedientes);
							
				Date fechaInicioResolucion;
				if (tramExp.getDetalleExpdteTram().getFechaInforme() != null) {
					fechaInicioResolucion = tramExp.getDetalleExpdteTram().getFechaInforme();
				}
				else
				{
					fechaInicioResolucion = plazosACU.getFechaLimite();
				}
				/** GENERAMOS EL PLAZO ASOCIADO A LA RESOLUCION. */			
				utilsComun.generarPlazoExpdte(tramExp.getId(),null,fechaInicioResolucion ,Constantes.COD_VAL_DOM_RES, Constantes.COD_VAL_DOM_DN, 0, "A", expedientes);
			}
			
			if(plazosPRSC != null) {
				/** CERRAMOS PLAZO ASOCIADO A LA PRESCRIPCIÓN. */
				utilsComun.generarPlazoExpdte(tramExp.getId(),"FIN",null,Constantes.COD_VAL_DOM_PRSC, Constantes.COD_VAL_DOM_DN,	0, "A",expedientes);
			}
			
			if(plazosACU != null || plazosPRSC != null)
			{
				datosExpedientesBean.limpiarPanelPlazos();
				List<PlazosExpdte> plazosExpdte = plazosExpdteService.plazosExpdteActivosNoCumplidosByExpediente(expedientes.getId());
				if(!plazosExpdte.isEmpty()) {
					datosExpedientesBean.actualizarCabecera(expedientes, null, null, plazosExpdte);
				}else {
					datosExpedientesBean.actualizarCabecera(expedientes, null, null, null);
				}				
				datosExpedientesBean.actualizarCabecera(expedientes, null, null, null);
			}
		
	}
	
	private void actualizarPlazosExpdteC023 (TramiteExpediente tramExp) throws BaseException {
		if(Constantes.C023.equals(tramExp.getTipoTramite().getComportamiento())){
			
			/** GENERAMOS EL PLAZO ASOCIADO A LA RECURSO. */			
			utilsComun.generarPlazoExpdte(tramExp.getId(),null,tramExp.getDetalleExpdteTram().getFechaRegistro() ,Constantes.COD_VAL_DOM_REC, Constantes.COD_VAL_DOM_DN, 0, "A", expedientes);

			datosExpedientesBean.limpiarPanelPlazos();
			List<PlazosExpdte> plazosExpdte = plazosExpdteService.plazosExpdteActivosNoCumplidosByExpediente(expedientes.getId());
			if(!plazosExpdte.isEmpty()) {
				datosExpedientesBean.actualizarCabecera(expedientes, null, null, plazosExpdte);
			}else {
				datosExpedientesBean.actualizarCabecera(expedientes, null, null, null);
			}
			
			aplicarSituacionAdicional(tramExp, tramExp.getDetalleExpdteTram());

			
			datosExpedientesBean.actualizarCabecera(expedientes, null, null, null);
			
		}
	}

	public void eliminarSubTramite(TramiteExpediente subTramExp) {
		subTramExp.setActivo(false);
		try {
			tramiteExpedienteService.guardar(subTramExp);
			expedientes = utilsComun.expedienteUltimaModificacion(expedientes, subTramExp.getFechaModificacion(),
					subTramExp.getFechaCreacion(), subTramExp.getUsuModificacion(), subTramExp.getUsuCreacion());
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
		
		aplicarSituacionAdicional(subTramExp, subTramExp.getDetalleExpdteTram());
		

		FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, new FacesMessage(FacesMessage.SEVERITY_INFO,
				"", SUBTRAMITEMSJ + mensajesProperties.getString(ELIMINADOCORRECTAMENTE)));

		inicializarCampos();
		
		datosExpedientesBean.actualizarCabecera(expedientes, null, null, null);

		
	}

	@Transactional(TxType.REQUIRED)
	public String finalizarTramite(List<TramiteExpediente> listaSubTramitesAsociados,List<TramiteExpediente> listaTramitesBD, Boolean existeSubTramitesAsociadosTemporales,TramiteExpediente tramExp) {

		aplicarDetalleTramite(tramExp, tramExp.getDetalleExpdteTram());
		aplicarFechaTramiteInfoRelevante(tramExp, tramExp.getDetalleExpdteTram());

		/**
		 * VALIDAMOS QUE NO EXISTAN SUBTRAMITES ACTIVOS
		 */
		boolean ifFinalizarTramiteSubtramitesActivosAux = finalizarTramiteSubtramitesActivos(listaSubTramitesAsociados,existeSubTramitesAsociadosTemporales);
		if (ifFinalizarTramiteSubtramitesActivosAux) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",mensajesProperties.getString("finalizar.subtramites.activos"));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			PrimeFaces.current().ajax().update(FORMFORMULARIOEXPEDIENTES);
			return "";
		}
		

		/**
		 * EN FUNCION DEL COMPORTAMIENTO DEL TRAMITE, TENDREMOS QUE HACER UNA SERIE DE
		 * VALIDACIONES.
		 * 
		 * - C006, C007, C009 Y C013: EL TRAMITE TIENE QUE TENER AL MENOS UN DOCUMENTO ASOCIADO. - C005: LA
		 * FECHA DE ENTRADA DEBE SER SIEMPRE ANTERIOR O IGUAL A LA FECHA DEL SISTEMA. -
		 * - C001: LA FECHA DE ENTRADA DEBE SER SIEMPRE ANTERIOR O IGUAL A LA FECHA DEL
		 * SISTEMA.
		 * - C008: EL TIPO DE ADMISIÓN Y LA FECHA DE INFORME DEBEN ESTAR INFORMADOS.
		 * - C012: FECHA RESOLUCION, SENTIDO RESOLUCION Y TIPO DE RESOLUCION DEBEN ESTAR INFORMADOS. LA FECHA RESOL NO DEBE SER POSTERIOR A LA FECHA ACTUAL.
		 * 			
		 */
		List<DocumentosExpedientesTramites> listaDocExpTram = documentosExpedientesTramitesService.findDocExpTramByIdTramExp(tramExp.getId());
		boolean ifFinalizarTramiteValidacionesComportamientos67910122013Aux = finalizarTramiteValidacionesComportamientos67910122013(tramExp,listaDocExpTram);	
		if (ifFinalizarTramiteValidacionesComportamientos67910122013Aux) {
			facesMsgErrorKey(DOCUMENTOACTIVOTRAMITE);
			return "";
		} 
		
		boolean ifFinalizarTramiteValidacionesComportamientos5118Aux = finalizarTramiteValidacionesComportamientos511823(tramExp);
		if (ifFinalizarTramiteValidacionesComportamientos5118Aux) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",mensajesProperties.getString(FECHAENTRADANOSUPFECHAACTUAL));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			return "";
		}
		
		boolean ifFinalizarTramiteValidacionesComportamientos7913Aux = finalizarTramiteValidacionesComportamientos7913(tramExp);
		if(ifFinalizarTramiteValidacionesComportamientos7913Aux)	{
			facesMsgErrorKey(FECHAENVIOFINALIZARTRAMITE);
			return "";
		}		
		
		boolean ifFinalizarTramiteValidacionesComportamientos8TipoAdmAux = finalizarTramiteValidacionesComportamientos8TipoAdm(tramExp);
		if(ifFinalizarTramiteValidacionesComportamientos8TipoAdmAux)	{
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",mensajesProperties.getString(DATOSNOINFORMADOS));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			return "";
		}
		
		boolean ifFinalizarTramiteValidacionesComportamientos23TipoAdmAux = finalizarTramiteValidacionesComportamientos23TipoAdm(tramExp);
		if(ifFinalizarTramiteValidacionesComportamientos23TipoAdmAux)	{
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",mensajesProperties.getString(DATOSNOINFORMADOS));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			return "";
		}
		
		boolean ifFinalizarTramiteValidacionesComportamientos8ValidacionesAux = finalizarTramiteValidacionesComportamientos8Validaciones(tramExp);
		if(ifFinalizarTramiteValidacionesComportamientos8ValidacionesAux){
			return "";
		}
		
		boolean ifFinalizarTramiteValidacionesComportamientos10PropApiAux = finalizarTramiteValidacionesComportamientos10PropApi(tramExp);
		if(ifFinalizarTramiteValidacionesComportamientos10PropApiAux) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",mensajesProperties.getString("finalizar.sin.propuesta.api"));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			return "";
		}

		boolean ifFinalizarTramiteValidacionesComportamientos10FechaInforAux = finalizarTramiteValidacionesComportamientos10FechaInfor(tramExp);
		if(ifFinalizarTramiteValidacionesComportamientos10FechaInforAux) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",mensajesProperties.getString(DATOSNOINFORMADOS));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			return "";
		}
		
		boolean ifFinalizarTramiteValidacionesComportamientos20Aux = finalizarTramiteValidacionesComportamientos20(tramExp);
		if(ifFinalizarTramiteValidacionesComportamientos20Aux) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",mensajesProperties.getString(DATOSNOINFORMADOS));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			return "";
		}		
		
		boolean ifFinalizarTramiteValidacionesComportamientos12Aux = finalizarTramiteValidacionesComportamientos12(tramExp);
		if(ifFinalizarTramiteValidacionesComportamientos12Aux){
			return "";
		}
		
		boolean ifFinalizarTramiteValidacionesComportamientos24Aux = finalizarTramiteValidacionesComportamientos24(tramExp);
		if(ifFinalizarTramiteValidacionesComportamientos24Aux){
			return "";
		}
		
		boolean ifFinalizarTramiteValidacionesComportamientos13Aux = finalizarTramiteValidacionesComportamientos13(tramExp);
		if(ifFinalizarTramiteValidacionesComportamientos13Aux){
			return "";
		}
		
		boolean ifFinalizarTramiteValidacionesComportamientos14Aux = finalizarTramiteValidacionesComportamientos14(tramExp);
		if(ifFinalizarTramiteValidacionesComportamientos14Aux){
			return "";
		}
			
		boolean ifFinalizarTramiteValidacionesComportamientos17Aux = finalizarTramiteValidacionesComportamientos17(tramExp);
		if(ifFinalizarTramiteValidacionesComportamientos17Aux)		{
			return "";
		}
		
		Usuario usuario = (Usuario)JsfUtils.getSessionAttribute(Constantes.USUARIO);
		Date tramFechaFin=FechaUtils.fechaYHoraActualDate();
		Date tramFechaFinReal=FechaUtils.fechaYHoraActualDate();
		for (TramiteExpediente te : listaTramitesBD) {
			if (Objects.equals(te.getId(), tramExp.getId())) {
				try {
					if(this.guardarTramiteAux(tramExp)) {						
						if (Boolean.FALSE.equals(validacionesObligatoriedadFinalizarTramite(tramExp))) {
							return "";
						}						

						//Tenemos que ver si finalizar el trámite implica pasar a una situación que finaliza el expediente.
						//En este caso no podemos tener trámites aún abiertos.
						boolean puedeCambiarSituacion = puedeCambiarSituacionFinalizarTramite(tramExp);
						if(!puedeCambiarSituacion) {
							facesMsgErrorKey("tramites.abiertos.situ.final");
							return null;			
						}
						
						//ok, seguimos ------------						
						tramExp.setFinalizado(true);
						tramExp.setUsuarioFinalizacion(usuario);
						tramExp.setFechaFin(tramFechaFin);	
						tramExp.setFechaFinReal(tramFechaFinReal);
						tramExp = tramiteExpedienteService.finalizarTramite(usuario, tramExp);
						
						finalizarTramiteGuardarDetalleComportamientoC008(tramExp);
						finalizarTramiteGuardarDetalleComportamientoC012C024(tramExp);
						finalizarTramiteGuardarDetalle(tramExp);
						
						expedientes = utilsComun.expedienteUltimaModificacion(expedientes, tramExp.getFechaModificacion(),tramExp.getFechaCreacion(), tramExp.getUsuModificacion(), tramExp.getUsuCreacion());
						
					} else {
						return "";
					}
			
					FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, new FacesMessage(FacesMessage.SEVERITY_INFO,"", TRAMITEMSJ + mensajesProperties.getString(FINALIZADOCORRECTAMENTE)));
					PrimeFaces.current().ajax().update(FORMFORMULARIOEXPEDIENTES);
				} catch (ValidacionException ve) {
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(NOEXISTEREGISTROCONFIG));
					log.warn("finalizarTramite - " + message.getDetail());
				} catch (BaseException e) {
					facesMsgErrorKey(MENSAJEERROR);
					log.error(e.getMessage());
					return null;
				}
			}
		}

		
		//---------------------------------------------
		//Bloque post-finalización				
		finalizarTramiteComportamientoC008(tramExp);		
		finalizarTramiteComportamientoC010(tramExp);		
		finalizarTramiteComportamientoC012C024(tramExp);		
		finalizarTramiteComportamientoC013(tramExp);		
		finalizarTramiteComportamientoC006(tramExp);		
		finalizarTramiteComportamientoC018(tramExp);		
		
		datosExpedientesBean.actualizarSituacionAdicional(tramExp.getExpediente().getId());

		actualizarInfVariableCabecera(tramExp);
		
		inicializarCampos();		
	
		//La actualización de plazos y cabeceras son operaciones de lectura, para actualizar el formulario.
		//Puedo hacer la evaluación al final, si no he salido de la página por el cambio de situación.
		finalizarTramiteActualizarPlazosYCabecera(tramExp);
		
		String retorno = finalizarTramiteActualizarSituacion(tramExp);
		
		
		
	PrimeFaces.current().executeScript("actualizar_documentos()");

		return retorno;
	}
	
	
	private void actualizarInfVariableCabecera(TramiteExpediente tramExp) {
		
		actualizarCabeceraC008(tramExp);
		
		actualizarCabeceraC012(tramExp);
		
		actualizarCabeceraC014(tramExp);
		
		actualizarCabeceraPRES(tramExp);
		
	}
	
	private void actualizarCabeceraC008 (TramiteExpediente tramExp) {
		

		if(Constantes.C008.equals(tramExp.getTipoTramite().getComportamiento())){

			
			DetalleExpdteTram detallesTramite= tramExp.getDetalleExpdteTram();
			String nuevaCabecera="";
			if(!tramExp.getTipoTramite().getCodigo().equals(Constantes.TIP_TRAM_ACADMHE)) { 
					Date fechaInforme= detallesTramite.getFechaInforme();
			String pattern = Constantes.FECHA_DDMMYYYY;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			String fechaInformeFormateada = simpleDateFormat.format(fechaInforme);
			nuevaCabecera=fechaInformeFormateada+" - "+ detallesTramite.getValorTipoAdmision().getDescripcion();
			
			}
			else
			{
				nuevaCabecera=detallesTramite.getValorTipoAdmision().getDescripcion();				
			}
			
			Expedientes expAct = tramExp.getExpediente();
			
			if (Constantes.TIP_TRAM_ACNOADM.equals(tramExp.getTipoTramite().getCodigo())) {
				
				nuevaCabecera = nuevaCabecera + " (" + detallesTramite.getValorMotivoInadmision().getAbreviatura()+ ")";
				expAct.setDescSeguimiento2(null);			
			}

			expAct.setInfSeguimiento1(nuevaCabecera);

				
		}
		

		
	}
	

	private void actualizarCabeceraC012 (TramiteExpediente tramExp) {

		if(Constantes.C012.equals(tramExp.getTipoTramite().getComportamiento())){

			if (! Constantes.PSAN.equals(tramExp.getExpediente().getValorTipoExpediente().getCodigo())) {

			DetalleExpdteTram detallesTramite= tramExp.getDetalleExpdteTram();
			String nuevaCabecera="";
			Date fecha= detallesTramite.getFechaResolucion();
			String pattern = Constantes.FECHA_DDMMYYYY;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			String fechaFormateada = simpleDateFormat.format(fecha);
			nuevaCabecera=fechaFormateada+" - "+ detallesTramite.getValorSentidoResolucion().getDescripcion() + " - " + detallesTramite.getNumResolucion();
			
			Expedientes expAct = tramExp.getExpediente();
			if (Boolean.TRUE.equals(detallesTramite.getImposicionMedidas())) {
				
			nuevaCabecera = nuevaCabecera + " (MEDIDAS)";
			}
						
			expAct.setInfSeguimiento2(nuevaCabecera);
			
			}
			else {
				DetalleExpdteTram detallesTramite= tramExp.getDetalleExpdteTram();
				String nuevaCabecera="";
				Date fecha= detallesTramite.getFechaResolucion();
				String pattern = Constantes.FECHA_DDMMYYYY;
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
				String fechaFormateada = simpleDateFormat.format(fecha);
				nuevaCabecera=fechaFormateada+" - "+ detallesTramite.getValorSentidoResolucion().getDescripcion() + " - " + detallesTramite.getNumResolucion();
				
				Expedientes expAct = tramExp.getExpediente();
				if (Boolean.TRUE.equals(detallesTramite.getImposicionMedidas())) {
					
				nuevaCabecera = nuevaCabecera + " (MEDIDAS)";
				}
							
				expAct.setInfSeguimiento3(nuevaCabecera);
								
			}
				
				
		}

		
	}
	

	private void actualizarCabeceraC014 (TramiteExpediente tramExp) {

		
		if(Constantes.C014.equals(tramExp.getTipoTramite().getComportamiento())){

			if (Constantes.RCO.equals(tramExp.getExpediente().getValorTipoExpediente().getCodigo())) {
			DetalleExpdteTram detallesTramite= tramExp.getDetalleExpdteTram();
			String nuevaCabecera="";
			Date fecha= FechaUtils.hoy();
			String pattern = Constantes.FECHA_DDMMYYYY;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			String fechaFormateada = simpleDateFormat.format(fecha);
			nuevaCabecera=fechaFormateada+" - Inicio PSAN "+ detallesTramite.getNumeroPsan();
			
			Expedientes expAct = tramExp.getExpediente();
						
			expAct.setInfSeguimiento2(nuevaCabecera);
			
			}
			
			if (Constantes.PSAN.equals(tramExp.getExpediente().getValorTipoExpediente().getCodigo())) {
			DetalleExpdteTram detallesTramite= tramExp.getDetalleExpdteTram();
			String nuevaCabecera="";
			Date fecha= detallesTramite.getFechaInforme();
			String pattern = Constantes.FECHA_DDMMYYYY;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			String fechaFormateada = simpleDateFormat.format(fecha);
			nuevaCabecera=fechaFormateada;
			
			Expedientes expAct = tramExp.getExpediente();
						
			expAct.setInfSeguimiento1(nuevaCabecera);
			
			}
					
		}
		
	}


	private void actualizarCabeceraPRES (TramiteExpediente tramExp) {

		
		if(Constantes.TIP_TRAM_PRES.equals(tramExp.getTipoTramite().getCodigo())) {

			String nuevaCabecera="";
			Date fecha= FechaUtils.hoy();
			String pattern = Constantes.FECHA_DDMMYYYY;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			String fechaFormateada = simpleDateFormat.format(fecha);
			nuevaCabecera=fechaFormateada;
			
			Expedientes expAct = tramExp.getExpediente();
						
			expAct.setInfSeguimiento2(nuevaCabecera);
			
		}
		
	}


	
	private void actualizarTipoAdmisMotivoInadmEnExpdte(TramiteExpediente tramExp)
	{
		Expedientes exp = tramExp.getExpediente();
		DetalleExpdteTram detExp = tramExp.getDetalleExpdteTram();
		ValoresDominio valorTipoAdmision = exp.getValorTipoAdmision();
		
		if(valorTipoAdmision == null)
		{
			exp.setValorTipoAdmision(detExp.getValorTipoAdmision());		
		}
		
		if(detExp.getValorMotivoInadmision()!= null && exp.getValorMotivoInadmision() == null)
		{
			exp.setValorMotivoInadmision(detExp.getValorMotivoInadmision());
		}
		
		if(Boolean.TRUE.equals(detExp.getImposicionMedidas())) {
			
			exp.setImposicionMedidas(true);
			
		}
		
		try {
			expedientesService.guardar(exp);
			datosExpedientesDatosGeneralesBean.actualizarPestanyaDatosGral(exp);
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
		
	}
	
	private void actualizarAutoridadCompetenteEnExpdte(TramiteExpediente tramExp){
		Expedientes exp = tramExp.getExpediente();
		DetalleExpdteTram detExp = tramExp.getDetalleExpdteTram();		
		exp.setValorAutoridadCompetente(detExp.getValorDominioInteresado());			
		exp.setCompetenciaCtpda(false);				
		try {
			expedientesService.guardar(exp);
			datosExpedientesDatosGeneralesBean.actualizarPestanyaDatosGral(exp);
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}		
	}
	
	private void actualizarAPIEnExpdte(TramiteExpediente tramExp)
	{
		Expedientes exp = tramExp.getExpediente();
		DetalleExpdteTram detExp = tramExp.getDetalleExpdteTram();
		
		if (detExp.getApi() != null) {
			exp.setApi(detExp.getApi());
		}
		
		
		
		try {
			expedientesService.guardar(exp);
			datosExpedientesDatosGeneralesBean.actualizarPestanyaDatosGral(exp);
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
		
	}
	
	private void actualizarMedidasEnExpdte(TramiteExpediente tramExp)
	{
		Expedientes exp = tramExp.getExpediente();
		DetalleExpdteTram detExp = tramExp.getDetalleExpdteTram();
		
		if (Boolean.TRUE.equals(detExp.getAcreditaCumplimiento())) {
			exp.setImposicionMedidas(false);
		}
		
		
		
		try {
			expedientesService.guardar(exp);
			datosExpedientesDatosGeneralesBean.actualizarPestanyaDatosGral(exp);
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
		
	}
	

	
	private boolean validarFinalizarTramiteC003(TramiteExpediente tramExp) {
		//La fecha de notificación y el check de infructuosa se validan al guardar
		if(tramExp.getSelectedNuevaIdentifInteresadoId() == null) {
			facesMsgErrorKey(INTERESADOFINALIZARTRAMITE);
			return false;
		}
		
		Date fechaEnvioNotif = tramExp.getDetalleExpdteTram().getFechaEnvio();
		if(fechaEnvioNotif == null) {
			facesMsgErrorKey(FECHAENVIOFINALIZARTRAMITE);
			return false;
		}
		
		if(Boolean.TRUE.equals(tramExp.getDetalleExpdteTram().getAcuseRecibo()))
		{
			Date fechaNotificacion = tramExp.getDetalleExpdteTram().getFechaNotificacion();
			if(FechaUtils.antes(fechaNotificacion, fechaEnvioNotif)){
				facesMsgErrorKey(FECHANOTIFPOSTERIORFECHAENV);
				return false;
			}
			
			if (!validarResultadoNotificacion(tramExp)){
				return false;
			}
		}
		

		//Validar que hay documento
		if(0 == this.documentosExpedientesTramitesService.countDocExpTramByIdTramExp(tramExp.getId())){
			facesMsgErrorKey(DOCUMENTOACTIVOTRAMITE);
			return false;
		}
			
		return true;
	}

	private boolean validarFinalizarTramiteC014(TramiteExpediente tramExp) {
		
		//Validar que hay documento
		if(0 == this.documentosExpedientesTramitesService.countDocExpTramByIdTramExp(tramExp.getId())){
			facesMsgErrorKey(DOCUMENTOACTIVOTRAMITE);
			return false;
		}
			
		return true;
	}
	

	private boolean validarResultadoNotificacion(TramiteExpediente trEx) {
		//Probablemente habrá más reglas
		resultadoNotificacionVacio(trEx);
		
		if (!resultadoNotificacionVacio(trEx)){
			facesMsgErrorKey(RESULTNOTIFICACIONNOINFORMADO);
			return false;
		}

		return true;
	}
	
	private void cargarCondicionCambioSituacion008(TramiteExpediente tramExp) {
		ValoresDominio valorCondCambioSit = detalleExpdteTram.getValorTipoAdmision();
		boolean boolAPI = detalleExpdteTram.getApi();
		
		String stringLAPI = "0";
		
		if (boolAPI) {
			stringLAPI = "1";
		}
		
		if(valorCondCambioSit != null) {
			tramExp.setCondicionCambioSituacion(valorCondCambioSit.getCodigo() + stringLAPI);
		}
	}
	
	private void cargarCondicionCambioSituacion010(TramiteExpediente tramExp) {
			
		ValoresDominio valorPropuestaAPI = detalleExpdteTram.getValorDominioPropuestaApi();
		
		
		if(valorPropuestaAPI != null) {
			tramExp.setCondicionCambioSituacion(valorPropuestaAPI.getCodigo());
		}
		}

	
	private void cargarCondicionCambioSituacion012C024(TramiteExpediente tramExp) {		
		boolean boolValorCondCambioSit = tramExp.getDetalleExpdteTram().getImposicionMedidas();		
		
		String valorCondCambioSit = "0";		
		if (boolValorCondCambioSit) {			
			valorCondCambioSit = "1";			
		}
			
		tramExp.setCondicionCambioSituacion(valorCondCambioSit);
	}

	private void cargarCondicionCambioSituacion013(TramiteExpediente tramExp) {
		
		boolean boolValorCondCambioSit = detalleExpdteTram.getAcreditaCumplimiento();
		
		String valorCondCambioSit = "0";		
		if (boolValorCondCambioSit) {			
			valorCondCambioSit = "1";			
		}
		
		tramExp.setCondicionCambioSituacion(valorCondCambioSit);
	}


	
	private ValoresDominio cambiarSituacionSegunAltaTramite(TramiteExpediente tramExp) {
		try {
			ValoresDominio sitFin = cfgAutoSituacionService.cambiarSituacionSegunAltaTramite(tramExp.getId());
			
			if(sitFin != null) {
				accionesCambioSituacionAuto(sitFin);
			}
			
			return sitFin;
		} catch (BaseException e) {
			facesMsgErrorKey(MENSAJEERROR);
			log.error(e.getMessage());
			return null;
		}
	}
	
	private ValoresDominio cambiarSituacionSegunFinTramite(TramiteExpediente tramExp) {
		try {
			ValoresDominio sitFin = cfgAutoSituacionService.cambiarSituacionSegunFinTramite(tramExp.getId(), tramExp.getCondicionCambioSituacion());
			
			if(sitFin != null) {
				if(this.expedienteEnSituacionFinal(tramExp.getExpediente()) || Constantes.RST.equals(sitFin.getCodigo())){
					comunExpedientesBean.cerrarPlazosAbiertosExpediente(tramExp.getExpediente().getId());
				}
				accionesCambioSituacionAuto(sitFin);
			}
			
			return sitFin;
		} catch (BaseException e) {
			facesMsgErrorKey(MENSAJEERROR);
			log.error(e.getMessage());
			return null;
		}
	}
	
	private void accionesCambioSituacionAuto(ValoresDominio sitFin) {
		//En BD ya ha cambiado. Lo actualizo en el bean
		expedientes.setValorSituacionExpediente(sitFin);
		datosExpedientesDatosGeneralesBean.recargarSituacion();

		//Mensaje de cambio
		String num = expedientes.getNumExpediente();
		String strFin = sitFin.getDescripcion();
		String msg = this.getMessage("mensaje.cambio.situacion");
		facesMsgInfo(StringUtils.resolverStr(msg, num, strFin));			
	}
	
	private boolean expedienteEnSituacionFinal(Expedientes exp){
		Boolean res = null;
		ValoresDominio valorTipoExp = exp.getValorTipoExpediente();
		ValoresDominio valorSituacion = exp.getValorSituacionExpediente();
		try {
			res = situacionesExpedientesService.esSituacionFinal(valorTipoExp.getId(), valorSituacion.getId());
		} catch (ValidacionException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(NOEXISTEREGISTROCONFIG));
			log.warn("expedienteEnSituacionFinal - " + message.getDetail());
		}
		if(res == null) {
			res = false;		
		}
		return res;
	}
	
	private boolean resultadoNotificacionVacio(TramiteExpediente trEx) {
		return null != trEx.getDetalleExpdteTram().getValorResultadoNotificacion();
	}

	public String finalizarSubTramite(TramiteExpediente subTramExp) {

		aplicarDetalleTramite(subTramExp, subTramExp.getDetalleExpdteTram());
		aplicarFechaTramiteInfoRelevante(subTramExp, subTramExp.getDetalleExpdteTram());


		/**
		 * EN FUNCION DEL COMPORTAMIENTO DEL SUBTRAMITE, TENDREMOS QUE HACER UNA SERIE
		 * DE VALIDACIONES.
		 * 
		 * C015:
		 * 
		 * - EL TRAMITE TIENE QUE TENER AL MENOS UN DOCUMENTO ASOCIADO. - LA FECHA DE
		 * RESPUESTA TIENE QUE SER IGUAL O POSTERIOR A LA FECHA DE ENVIO.
		 * 
		 * C016:
		 * - EL TRAMITE TIENE QUE TENER AL MENOS UN DOCUMENTO ASOCIADO.
		 * - LA FECHA DE FIRMA TIENE QUE SER IGUAL O POSTERIOR A LA FECHA DE ENVÍO.
		 */
		boolean ifFinalizarSubTramiteComportamiento15Aux = finalizarSubTramiteComportamiento15(subTramExp);		
		if (ifFinalizarSubTramiteComportamiento15Aux) {		
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",mensajesProperties.getString(FECHARESPPOSTERIORFECHAENV));
				PrimeFaces.current().dialog().showMessageDynamic(message);
				return "";
		}
		
		boolean ifFinalizarSubTramiteComportamiento16Aux = finalizarSubTramiteComportamiento16(subTramExp);		
		if(ifFinalizarSubTramiteComportamiento16Aux) {
			return "";
		}
			
		List<DocumentosExpedientesTramites> listaDocExpSubTram = documentosExpedientesTramitesService.findDocExpTramByIdTramExp(subTramExp.getId());
		if (listaDocExpSubTram == null || listaDocExpSubTram.isEmpty()) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",mensajesProperties.getString(DOCUMENTOACTIVOSUBTRAMITE));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			return "";
		}			


		try {
			if(this.guardarSubTramiteAux(subTramExp)) {					
				if(!accionesAntesFinalizarSubTramite(subTramExp)) {
					return "";
				}

				Usuario usuario = (Usuario)JsfUtils.getSessionAttribute(Constantes.USUARIO);
				Date tramFechaFin=FechaUtils.fechaYHoraActualDate();
				Date tramFechaFinReal=FechaUtils.fechaYHoraActualDate();

				if (Boolean.FALSE.equals(validacionesObligatoriedadFinalizarTramite(subTramExp))) {
					return "";
				}
				
				/** DEBEMOS COMPROBAR SI EL SUBTRAMITE QUE ESTAMOS FINALIZANDO ES EL ÚLMITO PARA EL TRAMITE PADRE. 
				 * EN ESTE CASO, DEBEMOS TRATAR LAS TAREAS RELACIONADAS. **/
				
				cerrarTareasTramSupSiEsUltimoSubtram(subTramExp);
				
				subTramExp.setFinalizado(true);
				subTramExp.setUsuarioFinalizacion(usuario);
				subTramExp.setFechaFin(tramFechaFin);	
				subTramExp.setFechaFinReal(tramFechaFinReal);
				subTramExp = tramiteExpedienteService.finalizarTramite(usuario, subTramExp);
				
				if (subTramExp.getDetalleExpdteTram() != null) {
					detalleExpdteTramService.guardar(subTramExp.getDetalleExpdteTram());
				}
	
				expedientes = utilsComun.expedienteUltimaModificacion(expedientes, subTramExp.getFechaModificacion(),subTramExp.getFechaCreacion(), subTramExp.getUsuModificacion(), subTramExp.getUsuCreacion());
			} else {
				return "";
			}
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}

		aplicarSituacionAdicional(subTramExp, subTramExp.getDetalleExpdteTram());
	
		FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, new FacesMessage(FacesMessage.SEVERITY_INFO,"", SUBTRAMITEMSJ + mensajesProperties.getString(FINALIZADOCORRECTAMENTE)));
		PrimeFaces.current().ajax().update(FORMFORMULARIOEXPEDIENTES);
		
		inicializarCampos();

		datosExpedientesBean.actualizarCabecera(expedientes, null, null, null);

		return "";
	}
	
	public void cerrarTareasTramSupSiEsUltimoSubtram(TramiteExpediente subTramExp) throws BaseException
	{
		TramiteExpediente tramExpSup = subTramExp.getTramiteExpedienteSup();
		
		if(esUltimoSubtramAbierto(subTramExp) && tramExpSup != null)
		{
		
			TipoTramite tipTramExpSup = tramExpSup.getTipoTramite();
			
			/****/
			List<CfgTareas> listaTareas 
				= cfgTareasService.findTareasByTipExpTipTramTipSubTramNull(subTramExp.getExpediente().getValorTipoExpediente().getId(), tipTramExpSup.getId());
			
			/**PARA CADA TIPO DE CONFIGURACION DE TAREAS RECUPERADA DEBEMOS COMPROBAR SI EXISTE UNA TAREA CON ESE TIPO DE CONFIGURACIÓN
			 * PARA EL TRÁMITE SUPERIOR EN CURSO. **/
			
			cerrarTareasTramSupParaConfigTarea(listaTareas, tramExpSup);

		}
	}
	
	public void cerrarTareasTramSupParaConfigTarea(List<CfgTareas> listaTareas, TramiteExpediente tramExpSup) throws BaseException 
	{
		Usuario usuario = (Usuario)JsfUtils.getSessionAttribute(Constantes.USUARIO);
		
		if(listaTareas != null && !listaTareas.isEmpty())
		{
			
			String mensajeConfirmCierreTareas = mensajesProperties.getString(CERRARTAREAS);
			StringBuilder descTareas = new StringBuilder();
			for(CfgTareas cfgTar: listaTareas)
			{
				List<TareasExpediente> listaTareasTramSup = tareasExpedienteService.findTareasExpActivasByTramExpTipTar(tramExpSup.getId(), cfgTar.getValorTipoTarea().getId());
				
				if(listaTareasTramSup != null && !listaTareasTramSup.isEmpty()){
					
					for(TareasExpediente tareaExp: listaTareasTramSup)
					{
						descTareas = descTareas.append("'" + tareaExp.getDescripcion() + "' ");
						
						tareasExpedienteService.cerrarTarea(tareaExp, usuario);
					}
				}
			}
			
			this.mensajeConfirmacionCierreTareas = mensajeConfirmCierreTareas + descTareas;
			
			if(!this.mensajeConfirmacionCierreTareas.equals(mensajesProperties.getString(CERRARTAREAS)))
			{
				PrimeFaces.current().executeScript("PF('dialogConfirmCierreTarea-" + tramExpSup.getId() + COMANDOSHOW);	
			}
		}
	}
	
	/**
	 * METODO QUE VALORA SI DADO UN SUBTRAMITE, ES EL ULTIMO ABIERTO Y ACTIVO PARA SU TRAMITE PADRE. 
	 * **/
	public boolean esUltimoSubtramAbierto(TramiteExpediente subTramExp)
	{
		boolean esUltimo = false;
		
		TramiteExpediente tramExpSup = subTramExp.getTramiteExpedienteSup();
		
		List<TramiteExpediente> listaSubTramActivosNoFinalizados = null;
		
		if(tramExpSup != null)
		{
			/**RECUPERAMOS LOS SUBTRAMITES ACTIVOS Y ABIERTOS PARA ESTE TRAMITE SUPERIOR.**/
			listaSubTramActivosNoFinalizados = tramiteExpedienteService.findSubTramExpByTramExpNoEliminados(tramExpSup.getId());
			
			
			/**SI SOLO HAY UN SUBTRAMITE ACTIVO Y NO FINALIZADO Y COINCIDE CON EL SUBTRAMITE QUE ESTAMOS TRATANDO, ENTONCES SE TRATA DEL 
			 * ULTIMO SUBTRAMITE ABIERTO PARA EL TRAMITE SUPERIOR QUE ESTAMOS VALORANDO. **/
			if(listaSubTramActivosNoFinalizados != null && listaSubTramActivosNoFinalizados.size() == 1 && 
					(Objects.equals(listaSubTramActivosNoFinalizados.get(0).getId(), subTramExp.getId())))
			{
				esUltimo = true;
			}
		}
		return esUltimo;
	}
	
	public boolean validacionesFinalizarDetalleTramExpC016(TramiteExpediente subTramExp)
	{
		if(subTramExp.getDetalleExpdteTram().getFirmante() == null)
		{
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					mensajesProperties.getString(FIRMANTEFINALIZARTRAMITE));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			return false;
		}
		
		if(subTramExp.getDetalleExpdteTram().getFechaFirma() == null)
		{
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					mensajesProperties.getString(FECHAFIRMAFINALIZARTRAMITE));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			return false;
		}
		if(subTramExp.getDetalleExpdteTram().getFechaEnvio() != null && (subTramExp.getDetalleExpdteTram().getFechaFirma()
				.before(subTramExp.getDetalleExpdteTram().getFechaEnvio())))
		{
			
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					mensajesProperties.getString(FECHAFIRMANOANTERIORFECHAENV));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			return false;
		}
		
		
		return true;
	}
	public Boolean validacionesObligatoriedadFinalizarTramite(TramiteExpediente tramExp) {

		if (tramExp.getDescripcion().isEmpty()) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",mensajesProperties.getString(CAMPOSOBLIGATORIOSGUARDAR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			return false;
		}

		DetalleExpdteTram detExpTram = detalleExpdteTramService.findDetalleTramiteExp(expedientes.getId(),tramExp.getId());
		boolean ifValidacionesObligatoriedadFinalizarTramiteComportamiento6ObligatoriosAux = validacionesObligatoriedadFinalizarTramiteComportamiento6Obligatorios(detExpTram,tramExp);		
		if (ifValidacionesObligatoriedadFinalizarTramiteComportamiento6ObligatoriosAux) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",mensajesProperties.getString(CAMPOSOBLIGATORIOSGUARDAR));
				PrimeFaces.current().dialog().showMessageDynamic(message);
				return false;
		}
		
		boolean ifValidacionesObligatoriedadFinalizarTramiteComportamiento6FechaEnvioAux = validacionesObligatoriedadFinalizarTramiteComportamiento6FechaEnvio(detExpTram,tramExp);	
		if (ifValidacionesObligatoriedadFinalizarTramiteComportamiento6FechaEnvioAux) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",mensajesProperties.getString(FECHAENVIOFINALIZARTRAMITE));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			return false;
		}
		
			
		boolean ifValidacionesObligatoriedadFinalizarTramiteComportamiento4ObligatoriosAux = validacionesObligatoriedadFinalizarTramiteComportamiento4Obligatorios(tramExp);	
		if (ifValidacionesObligatoriedadFinalizarTramiteComportamiento4ObligatoriosAux){
			facesMsgErrorKey(CAMPOSOBLIGATORIOS);
			return false;
		}

		boolean ifValidacionesObligatoriedadFinalizarTramiteComportamiento4FechaEnvioAux = validacionesObligatoriedadFinalizarTramiteComportamiento4FechaEnvio(tramExp);	
		if (ifValidacionesObligatoriedadFinalizarTramiteComportamiento4FechaEnvioAux){
			facesMsgErrorKey(FECHAENVIOFINALIZARTRAMITE);
			return false;
		}
		
		boolean ifValidacionesObligatoriedadFinalizarTramiteComportamiento4DocumentosAux = validacionesObligatoriedadFinalizarTramiteComportamiento4Documentos(tramExp);	
		if(ifValidacionesObligatoriedadFinalizarTramiteComportamiento4DocumentosAux){
			facesMsgErrorKey(DOCUMENTOACTIVOTRAMITE);
			return false;
		}
		
		boolean ifValidacionesObligatoriedadFinalizarTramiteComportamiento3Aux = validacionesObligatoriedadFinalizarTramiteComportamiento3(tramExp);	
		if(ifValidacionesObligatoriedadFinalizarTramiteComportamiento3Aux) {
			return false;
		}

		boolean ifValidacionesObligatoriedadFinalizarTramiteComportamiento51Aux = validacionesObligatoriedadFinalizarTramiteComportamiento5123(tramExp);	
		if (ifValidacionesObligatoriedadFinalizarTramiteComportamiento51Aux) {
			facesMsgErrorKey(CAMPOSOBLIGATORIOS);
			return false;
		}

		
		boolean ifValidacionesObligatoriedadFinalizarTramiteComportamiento51818Aux = validacionesObligatoriedadFinalizarTramiteComportamiento5181823(tramExp);	
		if (ifValidacionesObligatoriedadFinalizarTramiteComportamiento51818Aux) {
			facesMsgErrorKey(DOCUMENTOACTIVOTRAMITE);
			return false;
		}

		boolean ifValidacionesObligatoriedadFinalizarTramiteComportamiento14Aux = validacionesObligatoriedadFinalizarTramiteComportamiento14(tramExp);	
		if(ifValidacionesObligatoriedadFinalizarTramiteComportamiento14Aux) {
			return false;
		}
				
		boolean ifValidacionesObligatoriedadFinalizarTramiteComportamiento15FechaRespuestaAux = validacionesObligatoriedadFinalizarTramiteComportamiento15FechaRespuesta(detExpTram,tramExp);	
		if (ifValidacionesObligatoriedadFinalizarTramiteComportamiento15FechaRespuestaAux) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",mensajesProperties.getString(FECHARESPUESTAFINALIZARTRAMITE));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			return false;
		}
		
		boolean ifValidacionesObligatoriedadFinalizarTramiteComportamiento15FechaEnvioAux = validacionesObligatoriedadFinalizarTramiteComportamiento15FechaEnvio(detExpTram,tramExp);	
		if (ifValidacionesObligatoriedadFinalizarTramiteComportamiento15FechaEnvioAux) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",mensajesProperties.getString(FECHAENVIOFINALIZARTRAMITE));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			return false;
		}

		return true;
	}
		
	private boolean validarCanalSalidaObligatorio(TramiteExpediente trEx) {
		return trEx.getSelectedNuevoCanalSalidaId() != null
				&& !(StringUtils.isBlank(trEx.getDetalleExpdteTram().getDatosCanalSalida()));
	}

	private boolean accionesAntesFinalizarSubTramite(TramiteExpediente tramExp) throws BaseException {
		String comp = tramExp.getTipoTramite().getComportamiento();
		boolean accionesOk = true;
		
		if(Constantes.C003.equals(comp)) {
			accionesOk = actualizarTramiteSup(tramExp);
		}
		
		return accionesOk;
	}
	
	private boolean actualizarTramiteSup(TramiteExpediente tramExp) throws BaseException {
		TramiteExpediente tramExpSup = tramExp.getTramiteExpedienteSup();
		boolean accionOk = true;
		if(tramExpSup != null){
			copiarCamposATramiteSup(tramExp, tramExpSup);
			
			if(Constantes.C003.equals(tramExpSup.getTipoTramite().getComportamiento())) {
				actualizarFechaLimiteBasico(tramExpSup);
			} else {
				actualizarFechaLimite(tramExpSup);
			}
			
			accionOk = guardarTramiteAux(tramExpSup);
			DetalleExpdteTram detExpedTramSup = detalleExpdteTramService.obtenerObjeto(tramExpSup.getDetalleExpdteTram().getId());
			aplicarSituacionAdicional(tramExpSup, detExpedTramSup);

		}
		
		return accionOk;
	}
	
	private void copiarCamposATramiteSup(TramiteExpediente tramExp, TramiteExpediente tramExpSup) {
		DetalleExpdteTram detalle = tramExp.getDetalleExpdteTram();
		DetalleExpdteTram detalleSup = tramExpSup.getDetalleExpdteTram();

		copiarValorTipoInteresado(detalle, detalleSup);					
		copiarPersonasInteresado(detalle, detalleSup);					
		copiarSujetosObligadosInteresado(detalle, detalleSup);
		copiarValorDominioInteresado(detalle, detalleSup);
		copiarValorCanalSalida(detalle, detalleSup);					
		copiarDatosVariables(detalle, detalleSup);
		

		
	}
	
	private void copiarValorTipoInteresado(DetalleExpdteTram dtOri, DetalleExpdteTram dtDest) {
		if(null == dtDest.getValorTipoInteresado()) {
			dtDest.setValorTipoInteresado(dtOri.getValorTipoInteresado());
		}
	}
	
	private void copiarPersonasInteresado(DetalleExpdteTram dtOri, DetalleExpdteTram dtDest) {
		if(null == dtDest.getPersonasInteresado() && null != dtOri.getValorTipoInteresado() && (dtDest.getValorTipoInteresado().getId().equals(dtOri.getValorTipoInteresado().getId()))) {
			dtDest.setPersonasInteresado(dtOri.getPersonasInteresado());
		}
	}
	
	private void copiarSujetosObligadosInteresado(DetalleExpdteTram dtOri, DetalleExpdteTram dtDest) {
		if(null != dtOri.getValorTipoInteresado() && null == dtDest.getSujetosObligadosInteresado() && (dtDest.getValorTipoInteresado().getId().equals(dtOri.getValorTipoInteresado().getId()))) {
			dtDest.setSujetosObligadosInteresado(dtOri.getSujetosObligadosInteresado());
		}
	}
	
	private void copiarValorDominioInteresado(DetalleExpdteTram dtOri, DetalleExpdteTram dtDest) {
		if(null != dtOri.getValorTipoInteresado() && null == dtDest.getValorDominioInteresado() && (dtDest.getValorTipoInteresado().getId().equals(dtOri.getValorTipoInteresado().getId()))) {
			dtDest.setValorDominioInteresado(dtOri.getValorDominioInteresado());
		}
	}

	
	private void copiarValorCanalSalida(DetalleExpdteTram dtOri, DetalleExpdteTram dtDest) {
		if(null != dtOri.getValorTipoInteresado() && null == dtDest.getValorCanalSalida()  && null != dtDest.getValorDominioInteresado() && null != dtOri.getValorDominioInteresado() && (dtDest.getValorTipoInteresado().getId().equals(dtOri.getValorTipoInteresado().getId()))  && (dtDest.getValorDominioInteresado().getId().equals(dtOri.getValorDominioInteresado().getId()) )) {
			dtDest.setValorCanalSalida(dtOri.getValorCanalSalida());
		}
		if(null != dtOri.getValorTipoInteresado() && null == dtDest.getValorCanalSalida()  && null != dtDest.getPersonasInteresado() && null != dtOri.getPersonasInteresado() && (dtDest.getValorTipoInteresado().getId().equals(dtOri.getValorTipoInteresado().getId()))  && (dtDest.getPersonasInteresado().getId().equals(dtOri.getPersonasInteresado().getId()) )) {
			dtDest.setValorCanalSalida(dtOri.getValorCanalSalida());
		}
		if(null != dtOri.getValorTipoInteresado() && null == dtDest.getValorCanalSalida()  && null != dtDest.getSujetosObligadosInteresado() && null != dtOri.getSujetosObligadosInteresado() && (dtDest.getValorTipoInteresado().getId().equals(dtOri.getValorTipoInteresado().getId()))  && (dtDest.getSujetosObligadosInteresado().getId().equals(dtOri.getSujetosObligadosInteresado().getId()) )) {
			dtDest.setValorCanalSalida(dtOri.getValorCanalSalida());
		}

	
	
	
	}
	
	private void copiarDatosVariables(DetalleExpdteTram dtOri, DetalleExpdteTram dtDest) {

		if(null != dtOri.getValorTipoInteresado() && null != dtDest.getValorCanalSalida() && null != dtOri.getValorCanalSalida()  && (dtDest.getValorCanalSalida().getId().equals(dtOri.getValorCanalSalida().getId())) && null != dtDest.getSujetosObligadosInteresado() && null != dtOri.getSujetosObligadosInteresado() && (dtDest.getValorTipoInteresado().getId().equals(dtOri.getValorTipoInteresado().getId()))  && (dtDest.getSujetosObligadosInteresado().getId().equals(dtOri.getSujetosObligadosInteresado().getId()) )) {
		
			dtDest.setDatosCanalSalida(dtOri.getDatosCanalSalida());
			dtDest.setFechaEnvio(dtOri.getFechaEnvio());
			dtDest.setFechaNotificacion(dtOri.getFechaNotificacion());
			dtDest.setValorResultadoNotificacion(dtOri.getValorResultadoNotificacion());

		}

		if(null != dtOri.getValorTipoInteresado() && null != dtDest.getValorCanalSalida() && null != dtOri.getValorCanalSalida()  && (dtDest.getValorCanalSalida().getId().equals(dtOri.getValorCanalSalida().getId())) && null != dtDest.getPersonasInteresado() && null != dtOri.getPersonasInteresado() && (dtDest.getValorTipoInteresado().getId().equals(dtOri.getValorTipoInteresado().getId()))  && (dtDest.getPersonasInteresado().getId().equals(dtOri.getPersonasInteresado().getId()) )) {
			
			dtDest.setDatosCanalSalida(dtOri.getDatosCanalSalida());
			dtDest.setFechaEnvio(dtOri.getFechaEnvio());
			dtDest.setFechaNotificacion(dtOri.getFechaNotificacion());
			dtDest.setValorResultadoNotificacion(dtOri.getValorResultadoNotificacion());

		}

		if(null != dtOri.getValorTipoInteresado() && null != dtDest.getValorCanalSalida() && null != dtOri.getValorCanalSalida()  && (dtDest.getValorCanalSalida().getId().equals(dtOri.getValorCanalSalida().getId())) && null != dtDest.getValorDominioInteresado() && null != dtOri.getValorDominioInteresado() && (dtDest.getValorTipoInteresado().getId().equals(dtOri.getValorTipoInteresado().getId()))  && (dtDest.getValorDominioInteresado().getId().equals(dtOri.getValorDominioInteresado().getId()) )) {
			
			dtDest.setDatosCanalSalida(dtOri.getDatosCanalSalida());
			dtDest.setFechaEnvio(dtOri.getFechaEnvio());
			dtDest.setFechaNotificacion(dtOri.getFechaNotificacion());
			dtDest.setValorResultadoNotificacion(dtOri.getValorResultadoNotificacion());

		}
	
	}
	
	
	//Se puede refactorizar más.
	//El mensaje de error viene en la excepción, se puede llevar el facesMsgErrorKey
	//al try/catch correspondiente y usar el mensaje en vez de la constante.
	private CfgMetadatosTram obtenerMetadatosTramite(ValoresDominio valorTipExp, TipoTramite tipTram,
			TramiteExpediente tramExpSup) throws ValidacionException {

		final CfgMetadatosTram cfgMetadatosTram = cfgMetadatosTramService.findCfgMetadatosTram(valorTipExp, tipTram, tramExpSup);
		
		if (cfgMetadatosTram == null) {
			facesMsgErrorKey(MENSAJEERROR);
		}
		
		return cfgMetadatosTram;

	}

	public void abrirModalAltaPlazo() {
		fechaLimiteAnyadirPlazo = null;
		observacionesAnyadirPlazo = "";
		selectedTipoPlazoId = null;
		try {
			listaTiposPlazosByTipoExpediente = cfgPlazosExpdteService.findCfgPlazosByTipoExpediente(expedientes.getValorTipoExpediente().getId());
			
			if(expedientes.getId()!=null) {
				cabeceraDialog=getMessage("anyadir.plazo")+ " para el expediente "+ expedientes.getNumExpediente();
				}else {
					cabeceraDialog=getMessage("anyadir.plazo");
				}
			PrimeFaces.current().ajax().update("formFormularioExpedientes:tabViewPestanasExpediente:tipoPlazoAnyadirPlazo");
			PrimeFaces.current().executeScript("PF('dialogAnyadirPlazo').show();");
		} catch (ValidacionException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(NOEXISTEREGISTROCONFIG));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error("abrirModalAltaPlazo - " + message.getDetail());
		}
		
		
	}

	public void guardarPlazo() throws BaseException {

		if (selectedTipoPlazoId == null || fechaLimiteAnyadirPlazo == null) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					mensajesProperties.getString(CAMPOSOBLIGATORIOS));
			PrimeFaces.current().dialog().showMessageDynamic(message);
		} else {
			PlazosExpdte plazosExpdte = new PlazosExpdte();
			plazosExpdte.setExpediente(expedientes);
			CfgPlazosExpdte cfgPlazoSeleccionada = cfgPlazosExpdteService.obtener(selectedTipoPlazoId);
			plazosExpdte.setValorTipoPlazo(cfgPlazoSeleccionada.getValorTipoPlazo());
			plazosExpdte.setFechaLimite(fechaLimiteAnyadirPlazo);
			plazosExpdte.setOrigenInicial(Origenes.M);
			ObservacionesExpedientes obsExp = observacionesExpedientesService.guardarObservacionesExpedientes(null, observacionesAnyadirPlazo, Constantes.COD_VAL_DOM_TIPOBS_PLA, expedientes);
			plazosExpdte.setObservaciones(obsExp);
			plazosExpdte.setActivo(true);
			plazosExpdte.setCumplido(false);
			try {
				plazosExpdte = plazosExpdteService.guardar(plazosExpdte);
				obsExp.setPlazoExpdte(plazosExpdte);
				observacionesExpedientesService.guardar(obsExp);		
				
				expedientes = utilsComun.expedienteUltimaModificacion(expedientes, plazosExpdte.getFechaModificacion(),
						plazosExpdte.getFechaCreacion(), plazosExpdte.getUsuModificacion(),
						plazosExpdte.getUsuCreacion());

			} catch (BaseException e) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						mensajesProperties.getString(MENSAJEERROR));
				PrimeFaces.current().dialog().showMessageDynamic(message);
				log.error(e.getMessage());
			}

			PrimeFaces.current().executeScript("PF('dialogAnyadirPlazo').hide();");
			FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, new FacesMessage(
					FacesMessage.SEVERITY_INFO, "", "Plazo " + mensajesProperties.getString(GUARDADOCORRECTAMENTE)));

			List<PlazosExpdte> listaPlazosExpdte = 
					plazosExpdteService.plazosExpdteActivosNoCumplidosByExpediente(expedientes.getId());
			datosExpedientesBean.actualizarCabecera(expedientes, null, null, listaPlazosExpdte);
		}
	}
	
	public void actualizarFechaLimite(TramiteExpediente tramExp)
	{
		Date fechaEnvioNotif = tramExp.getDetalleExpdteTram().getFechaEnvio();
		Date fechaNotificacion = tramExp.getDetalleExpdteTram().getFechaNotificacion();
		Integer plazo = tramExp.getDetalleExpdteTram().getPlazo();
		Date fechaLimite = null;
		
		if(fechaNotificacion != null || fechaEnvioNotif != null || plazo != null)
		{
			if(FechaUtils.antes(fechaNotificacion, fechaEnvioNotif))
			{
				facesMsgErrorKey(FECHANOTIFPOSTERIORFECHAENV);
			}

			int diferenciaDias = FechaUtils.diasEntre(fechaEnvioNotif, fechaNotificacion);
			
			if(plazo != null)
			{
				plazo = plazo + diferenciaDias;	
			}else
			{
				plazo = diferenciaDias;
			}
			
			if(fechaEnvioNotif != null)
			{
				fechaLimite = FechaUtils.sumarDiasAFecha(fechaEnvioNotif, plazo);
			}
		}

		tramExp.getDetalleExpdteTram().setFechaLimite(fechaLimite);
	
	}

	public void actualizarFechaLimiteBasico(TramiteExpediente tramExp)
	{
		Date fechaEnvioNotif = tramExp.getDetalleExpdteTram().getFechaEnvio();
		int plazo = toInt(tramExp.getDetalleExpdteTram().getPlazo());
		
		Date fechaLimite = (fechaEnvioNotif == null)? null
				: FechaUtils.sumarDiasAFecha(fechaEnvioNotif, plazo);

		tramExp.getDetalleExpdteTram().setFechaLimite(fechaLimite);
	}
	
	private int toInt(Integer itg) {
		return itg==null? 0 : itg.intValue();
	}

	public void onChangeTipoInteresado(TramiteExpediente tE) {
		recargaIdentificacionesInteresado(tE);
	}
	
	public void onChangeMotivoInadmision(TramiteExpediente tE) {
		recargaMotivosInadmision(tE);
	}
	
	public void onChangeInteresadoPersona(TramiteExpediente tE) {
		tE.setSelectedNuevoCanalSalidaId(null);
		
		String comp = tE.getTipoTramite().getComportamiento();
		if(Constantes.C007.equals(comp)
				|| Constantes.C003.equals(comp)
				|| Constantes.C004.equals(comp)
				|| Constantes.C009.equals(comp)
				|| Constantes.C013.equals(comp)
				|| Constantes.C017.equals(comp))
			{
				cargaDatosSalidaPersona(tE);
			}
	}
	
	public void onChangeInstructorApi(TramiteExpediente tE)
	{
		if(tE.getDetalleExpdteTram() != null)
		{
			if(Boolean.TRUE.equals(tE.getDetalleExpdteTram().getApi())) {
				tE.setHabilitarInstructorApi(true);
				if(tE.getDetalleExpdteTram().getValorInstructorAPI() != null)
				{
					tE.setSelectedNuevoInstructorApiIdAcAdmis(tE.getDetalleExpdteTram().getValorInstructorAPI().getId());	
				}								
			}else
			{
				tE.setHabilitarInstructorApi(false);
				tE.setSelectedNuevoInstructorApiIdAcAdmis(null);
			}
		}else
		{
			tE.setHabilitarInstructorApi(false);
		}
	}
	
	public void onChangeInteresadoDpd(TramiteExpediente tE) {
		onChangeInteresadoPersona(tE);
	}

	public void recargaResponsable(TramiteExpediente t) {
		TramiteExpediente tBd = tramiteExpedienteService.obtener(t.getId());
		ResponsablesTramitacion resp = tBd.getResponsable();
		
		t.setResponsable(resp);
		t.setSelectedNuevoResponsableId(resp.getId());
		setMostrarBotonFinalizar(t);
	}
	
	public void recargaMotivosInadmision(TramiteExpediente tE) {
		if(tE.getSelectedNuevoTipoAdmisionId() != null)
		{
			ValoresDominio valDom = valoresDominioService.obtener(tE.getSelectedNuevoTipoAdmisionId());
			if(valDom.getCodigo().equals(Constantes.COD_VAL_DOM_INA))
			{
				tE.setHabilitarComboMotivoInad(true);	
			}else
			{
				tE.setHabilitarComboMotivoInad(false);
				tE.setSelectedNuevoMotivoInadmisionId(null);
			}
		}else {
			tE.setHabilitarComboMotivoInad(false);
			tE.setSelectedNuevoMotivoInadmisionId(null);
		}
	}
	
	public void recargaInstructorApi(TramiteExpediente tE, DetalleExpdteTram detExpTram) {
		if(detExpTram != null)
		{
			if(Boolean.TRUE.equals(detExpTram.getApi())) {
				tE.setHabilitarInstructorApi(true);
				if(detExpTram.getValorInstructorAPI() != null)
				{
					tE.setSelectedNuevoInstructorApiIdAcAdmis(detExpTram.getValorInstructorAPI().getId());	
				}								
			}else
			{
				tE.setHabilitarInstructorApi(false);
				tE.setSelectedNuevoInstructorApiIdAcAdmis(null);
			}
		}else
		{
			tE.setHabilitarInstructorApi(false);
		}
	}
	
	public void recargaIdentificacionesInteresado(TramiteExpediente tE) {

		if (tE.getSelectedNuevoTipoInteresadoId() != null) {
			ValoresDominio valDom = valoresDominioService.obtener(tE.getSelectedNuevoTipoInteresadoId());
			tE.setSelectedNuevoCanalSalidaId(null);
						
			if (Constantes.COD_VAL_DOM_SUJOBL.equals(valDom.getCodigo())) {
				cargaIdentifInteresadoSujObl(tE);
			} else if (Constantes.COD_VAL_DOM_PERS.equals(valDom.getCodigo())) {
				cargaIdentifInteresadoPers(tE);
				onChangeInteresadoPersona(tE);
			} else if (Constantes.COD_VAL_DOM_DPD.equals(valDom.getCodigo())) {
				cargaIdentifInteresadoDpd(tE);
				onChangeInteresadoDpd(tE);
			} else if (Constantes.COD_VAL_DOM_AUTCON.equals(valDom.getCodigo())) {
				cargaIdentifInteresadoAutCon(tE);
			} else {
				this.codigoTipoInteresado = null;
				this.esIdentIntDPD = false;
				this.esIdentIntAutControl = false;
			}
		} else {
			limpiarDatosValorInteresado(tE);
			tE.setSelectedNuevaIdentifInteresadoId(null);

			tE.limpiarListasInteresados();
		}

		this.habilitarIdentifInt = false;
	}
	
	private void limpiarDatosValorInteresado(TramiteExpediente tE) {
		final DetalleExpdteTram det = tE.getDetalleExpdteTram();
		
		if(det != null) {
			det.setPersonasInteresado(null);
			det.setSujetosObligadosInteresado(null);
			det.setValorDominioInteresado(null);
		}
		
		tE.setEsIdentIntSujOblig(false);
		tE.setEsIdentIntAutControl(false);
		tE.setEsIdentIntDPD(false);
		tE.setEsIdentIntPersona(false);
	}

	public void cargaDatosSalidaPersona(TramiteExpediente tE) {
		final Long identifInteresado = tE.getSelectedNuevaIdentifInteresadoId();
		if(identifInteresado != null) {
			final Personas persona = personasService.obtener(identifInteresado);
			if(persona != null) {
				final ValoresDominio canalSalida = persona.getValorViaComunicacion();
				if(canalSalida != null) {
					tE.setSelectedNuevoCanalSalidaId(canalSalida.getId());
				}
			}
		}
	}

	public void cargaIdentifInteresadoSujObl(TramiteExpediente tE) {
		limpiarDatosValorInteresado(tE);
		tE.setEsIdentIntSujOblig(true);
		
		tE.setListaIdentifIntSujOblig(sujetosObligadosExpedientesService.obtenerSujetosObligadosExpediente(expedientes.getId()));	
		
		SujetosObligadosExpedientes sujOblExp = sujetosObligadosExpedientesService.obtenerSujetosObligadosExpedientePrincipalPorExpediente(expedientes.getId());
		SujetosObligados sujObl = null;
		
		if(sujOblExp != null)
		{
			sujObl = sujOblExp.getSujetosObligados();
			
			if(sujObl != null) {
				tE.setSelectedNuevaIdentifInteresadoId(sujObl.getId());	
			}
			
		} else if(tE.getListaIdentifIntSujOblig().size() == 1)
		{
			sujObl = tE.getListaIdentifIntSujOblig().get(0);
			tE.setSelectedNuevaIdentifInteresadoId(sujObl.getId());
		}
		
		if((Constantes.C007.equals(tE.getTipoTramite().getComportamiento())
				|| Constantes.C004.equals(tE.getTipoTramite().getComportamiento())
				|| Constantes.C009.equals(tE.getTipoTramite().getComportamiento())
				|| Constantes.C013.equals(tE.getTipoTramite().getComportamiento())) 
				&& sujObl != null && sujObl.getValorViaComunicacion() != null)
		{
			tE.setSelectedNuevoCanalSalidaId(sujObl.getValorViaComunicacion().getId());
		}
		
		if(Constantes.C003.equals(tE.getTipoTramite().getComportamiento()) && sujObl != null && sujObl.getValorViaComunicacion() != null)
		{
			tE.setSelectedNuevoCanalSalidaId(sujObl.getValorViaComunicacion().getId());
		}
	}

	public void cargaIdentifInteresadoPers(TramiteExpediente tE) {
		limpiarDatosValorInteresado(tE);
		tE.setEsIdentIntPersona(true);
		
		List<PersonasExpedientes> listaPersonasPorExpediente = personasExpedientesService.obtenerPersPorExpediente(expedientes.getId());
		
		List<PersonaDTO> listaIdentifIntPersonas = cargarPersonasYRepresentantes(listaPersonasPorExpediente);
		
		
		tE.setListaIdentifIntPersDTO(listaIdentifIntPersonas);
		tE.setSelectedNuevaIdentifInteresadoId(null);

		PersonasExpedientes personasExpPpal =personasExpedientesService.obtenerPersonaExpedientePrincipalPorExpediente(expedientes.getId()); 
		
		if (personasExpPpal != null) {
			if (personasExpPpal.getPersonas() != null) {
				tE.setSelectedNuevaIdentifInteresadoId(personasExpPpal.getPersonas().getId());
			}

		} else if (listaIdentifIntPersonas.size() == 1) {
			tE.setSelectedNuevaIdentifInteresadoId(listaIdentifIntPersonas.get(0).getId());
		}

	}

	public void cargaIdentifInteresadoDpd(TramiteExpediente tE) {
		limpiarDatosValorInteresado(tE);
		tE.setEsIdentIntDPD(true);

		tE.setListaIdentifIntDpd(sujetosObligadosExpedientesService.obtenerDpdExpediente(expedientes.getId()));
		tE.setSelectedNuevaIdentifInteresadoId(null);
			
		SujetosObligadosExpedientes sujObExp = sujetosObligadosExpedientesService
				.obtenerSujetosObligadosExpedientePrincipalPorExpediente(expedientes.getId());
		
		if (sujObExp != null) {
			if (sujObExp.getPersonas() != null) {
				tE.setSelectedNuevaIdentifInteresadoId(sujObExp.getPersonas().getId());
			}
		} else if (tE.getListaIdentifIntDpd().size() == 1) {
			tE.setSelectedNuevaIdentifInteresadoId(tE.getListaIdentifIntDpd().get(0).getId());
		}
	}

	public void cargaIdentifInteresadoAutCon(TramiteExpediente tE) {
		limpiarDatosValorInteresado(tE);
		tE.setEsIdentIntAutControl(true);

		tE.setListaValoresDominioIdentifInteresado(
				valoresDominioService.findValoresDominioActivosByCodigoDominio(ValoresDominioService.COD_AUTCOMP));
		tE.setSelectedNuevaIdentifInteresadoId(null);

		if (tE.getListaValoresDominioIdentifInteresado().size() == 1) {
			tE.setSelectedNuevaIdentifInteresadoId(tE.getListaValoresDominioIdentifInteresado().get(0).getId());
		}
	}
	
	public String nombreUsuario(TramiteExpediente tE) {
		String res= "";
		if(tE!=null && tE.getUsuarioFinalizacion()!=null){		
			Long idUsuario= tE.getUsuarioFinalizacion().getId();
			Usuario usuarioTramiteFin= usuarioService.obtener(idUsuario);
			res=usuarioTramiteFin.getNombreAp();
			}
		
		return res;
	}
	
	public void rehabilitarTramite(TramiteExpediente tE) {
		Long idTipoTramite= tE.getTipoTramite().getId();
		TipoTramite tipoTramite= tipoTramiteService.obtener(idTipoTramite);
		CfgMetadatosTram idCfgTe = null;
		try {
			idCfgTe = obtenerMetadatosTramite(expedientes.getValorTipoExpediente(), tipoTramite, tE.getTramiteExpedienteSup());
		} catch (ValidacionException e1) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(NOEXISTEREGISTROCONFIG));
			log.warn("rehabilitarTramite para " + expedientes.getValorTipoExpediente().getCodigo() + ", " + tipoTramite.getCodigo() + " - " + message.getDetail());
		}
		DetalleExpdteTram detExpd= detalleExpdteTramService.findDetalleTramiteExp(expedientes.getId(),tE.getId());
		
		if(Boolean.TRUE.equals(tE.getFinalizado())) {
			tE.setFinalizado(false);
			tE.setCfgMetadatosTram(idCfgTe);
			tE.setDetalleExpdteTram(detExpd);			
			try {
			
				tE = tramiteExpedienteService.guardar(tE);
				this.listaTramTramitesExp.add(tE);
				inicializarCampos();
			} catch (BaseException e) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						mensajesProperties.getString(MENSAJEERROR));
				PrimeFaces.current().dialog().showMessageDynamic(message);
				log.error(e.getMessage());
			}

			FacesContext.getCurrentInstance().addMessage("messagesFormEvolucionExpediente",
					new FacesMessage(FacesMessage.SEVERITY_INFO, "",
							TRAMITE + " " + mensajesProperties.getString(ACTUALIZADOCORRECTAMENTE)));

			PrimeFaces.current().ajax().update(FORMFORMULARIOEXPEDIENTES);	
		}
	}
	
	public void activarExtractos(TramiteExpediente tE) {

		DetalleExpdteTram detExpd= detalleExpdteTramService.findDetalleTramiteExp(expedientes.getId(),tE.getId());
		
		detExpd.setTextoExtractoExpediente(" ");
		
		try {
				detalleExpdteTramService.guardar(detExpd);
			} catch (BaseException e) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						mensajesProperties.getString(MENSAJEERROR));
				PrimeFaces.current().dialog().showMessageDynamic(message);
				log.error(e.getMessage());
			}

			PrimeFaces.current().ajax().update(FORMFORMULARIOEXPEDIENTES);	
		
	}
	
	public void activarAntecedentes(TramiteExpediente tE) {

		DetalleExpdteTram detExpd= detalleExpdteTramService.findDetalleTramiteExp(expedientes.getId(),tE.getId());
		
		detExpd.setTextoAntecedentesExpediente(" ");
		
		try {
				detalleExpdteTramService.guardar(detExpd);
			} catch (BaseException e) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						mensajesProperties.getString(MENSAJEERROR));
				PrimeFaces.current().dialog().showMessageDynamic(message);
				log.error(e.getMessage());
			}

			PrimeFaces.current().ajax().update(FORMFORMULARIOEXPEDIENTES);	
		
	}
	
	
	public Boolean tieneTramSupFinalizado(TramiteExpediente tE) {
		Boolean res= false;
		if(tE!=null && tE.getTramiteExpedienteSup()!=null && tE.getTramiteExpedienteSup().getFinalizado().equals(true)) {			
				res=true;
						
		}
		return res;
	}
	
	public Boolean noTieneListaSubtramites(TramiteExpediente idTe) {
		Boolean res=true;
		try {
			listaSubTramites = cfgExpedienteSubTramiteService.findSubTramites(idTe.getTipoTramite().getId(),expedientes.getValorTipoExpediente().getId());
		} catch (ValidacionException e) {
			listaSubTramites = null;
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(NOEXISTEREGISTROCONFIG));
			log.warn("noTieneListaSubtramites - " + message.getDetail());
		}
		if(listaSubTramites == null || listaSubTramites.isEmpty()) {
			res=false;		
		}
		return res;
		
	}


	
	public  String calculaMensajeConfirmacion(TramiteExpediente tramExp)
	{
		CfgTipoExpediente cfgTipoExp = cfgTipoExpedienteService.obtenerCfgTipoExpedientePorValorTipoExpediente(tramExp.getExpediente().getValorTipoExpediente().getId());
		ValoresDominio valDomSerie = null;
		
		if(Constantes.C012.equals(tramExp.getTipoTramite().getComportamiento())){
			valDomSerie = cfgTipoExp.getValorSerieResolucion();
		}else {
			valDomSerie = cfgTipoExp.getValorSerieResolRecurso();
		}
		

		String descripcionSerie = "";
		
		
		if(valDomSerie != null)
		{
			descripcionSerie = valDomSerie.getDescripcion();
			tramExp.setMensajeConfirmacionSerie(mensajesProperties.getString("confirmar.serie")+ " " + "'" + descripcionSerie + "'. " 
					+ mensajesProperties.getString("proceso.irreversible")); 
		}else {
			tramExp.setMensajeConfirmacionSerie("No existe serie asociada al tipo de expediente.");	
		}

		
		
		return "";
	}	
	

	public String altaResolucion(TramiteExpediente tramExp) {

		/**TOMAMOS LA SERIE EN FUNCION DEL TIPO DE EXPEDIENTE EN GE_CFG_TIPOEXPEDIENTES*/
		
		calculaMensajeConfirmacion(tramExp);
		
		CfgTipoExpediente cfgTipoExpediente = cfgTipoExpedienteService.obtenerCfgTipoExpedientePorValorTipoExpediente(tramExp.getExpediente().getValorTipoExpediente().getId());
		
		if ((cfgTipoExpediente.getValorSerieResolucion().getId() == null && Constantes.C012.equals(tramExp.getTipoTramite().getComportamiento())) 
				|| (cfgTipoExpediente.getValorSerieResolRecurso().getId() == null && Constantes.C024.equals(tramExp.getTipoTramite().getComportamiento())))
		{
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					mensajesProperties.getString("info.seleccione.serie"));
			PrimeFaces.current().dialog().showMessageDynamic(message);
		}else {
			String codSerie = "";
			ValoresDominio valorDominioSerie;
			
			if(Constantes.C012.equals(tramExp.getTipoTramite().getComportamiento()))
			{
				valorDominioSerie = valoresDominioService.obtener(cfgTipoExpediente.getValorSerieResolucion().getId());
			}else {
				valorDominioSerie = valoresDominioService.obtener(cfgTipoExpediente.getValorSerieResolRecurso().getId());
			}
			
			if(valorDominioSerie != null)
			{
				codSerie = valorDominioSerie.getCodigo();
				
				aplicarDetalleAltaResolucion(codSerie, tramExp);
				
				crearResolucion(tramExp, CREARRESOLUCION);
				
				datosExpedientesBean.actualizarSituacionAdicional(tramExp.getExpediente().getId());
				
				datosExpedientesBean.actualizarCabecera(expedientes, null, null, null);
				
				tramExp.setSelectedNuevoSerieNumeracionId(null);
			}
		}
		

		return "";
	}
	
	public void aplicarDetalleAltaResolucion(String codSerie, TramiteExpediente tramExp)
	{
		try {
			DetalleExpdteTram detExpTram;
			String numeroResolucion = seriesService.nextNumeroSerie(codSerie, tramExp.getDetalleExpdteTram().getFechaResolucion());
			
			detExpTram = tramExp.getDetalleExpdteTram();
			
			detExpTram.setNumResolucion(numeroResolucion);
			
			if(tramExp.getSelectedNuevoSentidoResolucionId() != null)
			{
				detExpTram.setValorSentidoResolucion(valoresDominioService.obtener(tramExp.getSelectedNuevoSentidoResolucionId()));
			}
			if(tramExp.getSelectedNuevoTipoResolucionId() != null)
			{
				detExpTram.setValorTipoResolucion(valoresDominioService.obtener(tramExp.getSelectedNuevoTipoResolucionId()));
			}

		} catch (final BaseException e) {
			FacesMessage message;
			if(e.getMessage().contains("serie")) {
				message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(NOEXISTEREGISTROCONFIG));
			}else {
				message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MENSAJEERROR));
			}
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error("aplicarDetalleAltaResolucion - " + e.getMessage());
		}
	}
	
	public String verResolucion(TramiteExpediente tramExp) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, ((Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS))+1);
		final String EDITABLE = "editable";
		
		ContextoVolver v = volverBean.crearContexto(ListadoNavegaciones.FORM_EXPEDIENTES.getRegla(), Constantes.VOLVERRESOLUCION);
		v.put(IDEXPSESSION, expedientes.getId());
		v.put("idTramite", tramExp.getId());
		v.put(EDITABLE, this.getFormEditable());
		
		JsfUtils.setFlashAttribute(EDITABLE, false);
		Resolucion resol = resolucionService.findResolucionByNumeroResolucion(tramExp.getDetalleExpdteTram().getNumResolucion());
		
		String nav = null;
		
		if(resol != null) {
			JsfUtils.setFlashAttribute("idRes", resol.getId());
			nav = ListadoNavegaciones.FORM_RESOLUCION.getRegla();
			navegacionBean.setTxtMigaPan(Constantes.CONSULTA_RESOLUCION+resol.getCodigoResolucion());
		}
		
		return nav;
	}
	
	
	public String vincularResolucion(TramiteExpediente tramExp)
	{
		/**
		 * PARA VINCULAR UNA RESOLUCION A UN EXPEDIENTE DEBEMOS:
		 * 1.- VALIDAR QUE EL NUMERO DE RESOLUCION QUE SE QUIERE VINCULAR EXISTE.
		 * 2.- 
		 * */
		
		Resolucion resolucion = resolucionService.findResolucionByNumeroResolucion(tramExp.getNumResolVinculada());
		
		if(resolucion != null)
		{
			String nombreDialog = "";
			String ruta = "";
			
			
			if(Constantes.C012.equals(tramExp.getTipoTramite().getComportamiento()))
			{
				nombreDialog = "dialogConfirmVincularResolC012-";
				ruta = "formFormularioExpedientes:tabViewPestanasExpediente:numResolVinculadaC012-";
			}else {
				nombreDialog = "dialogConfirmVincularResolC024-";
				ruta = "formFormularioExpedientes:tabViewPestanasExpediente:numResolVinculadaC024-";
			}
			
			String nombreDialogConfirm = nombreDialog + tramExp.getId();
			
			this.numResolVinc = tramExp.getNumResolVinculada();
			this.fechaResolVinc = resolucion.getFechaResolucion();
			/**
			 * COMPROBAMOS SI EL USUARIO HA SELECCIONADO EN EL SENTIDO DE LA RESOLUCION UN VALOR. SI ES ASI, EL SENTIDO DE LA RESOLUCION
			 * QUE ALMACENAMOS EN EL DETALLE DEL TRAMITE ES EL DEL FORMULARIO Y NO EL DE LA RESOLUCION QUE ESTAMOS VINCULANDO.
			 * */
			if(tramExp.getSelectedNuevoSentidoResolucionId() != null)
			{
				this.valorDomSentidoResolVinc = valoresDominioService.obtener(tramExp.getSelectedNuevoSentidoResolucionId());
			}else
			{
				this.valorDomSentidoResolVinc = resolucion.getValorSentidoResolucion();	
			}
			
			this.valorDomTipoResolVinc = resolucion.getValorTipoResolucion();
			
			tramExp.setNumResolVinculada(null);
			
			String rutaActualizar = ruta + tramExp.getId();
			
			PrimeFaces.current().ajax().update(rutaActualizar);

			
			PrimeFaces.current().executeScript("PF('" + nombreDialogConfirm + COMANDOSHOW);

			
		}else
		{
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					mensajesProperties.getString(NUMRESOLNOEXISTE));
			PrimeFaces.current().dialog().showMessageDynamic(message);
		}
		return "";
	}


	
	public  void actualizarBotoneraResolucion(TramiteExpediente tramExp, Long idSentidoResol, Long idTipoResol, String numResol)
	{
		if(Constantes.C024.equals(tramExp.getTipoTramite().getComportamiento()))
		{
			idTipoResol = valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_TIPORESOL, Constantes.COD_VAL_DOM_REC).getId(); 
		}
		Boolean mostrarBotonera = idSentidoResol != null && idTipoResol != null && (numResol == null || numResol.isEmpty());
		
		tramExp.setMostrarCamposResol(numResol != null && !numResol.isEmpty());
		
		tramExp.setMostrarBotoneraResolucion(mostrarBotonera);
		tramExp.setHabilitarVerResol(numResol != null && !numResol.isEmpty());	
		tramExp.setHabilitarAsocResol(numResol == null || numResol.isEmpty());
		
	}	
	
	public void limpiarSerie(TramiteExpediente tramExp)
	{
		tramExp.setSelectedNuevoSerieNumeracionId(null);
	}
	
	public Long obtenerResponsableTramite(Long idTramite){
		Long idResponsableDefecto=null;
		if(idTramite!=null) {
			
		
		CfgExpedienteTramite tramExp= cfgExpedienteTramiteService.obtener(idTramite);
		if(tramExp.getResponsablesTramitacion()!=null) {
			idResponsableDefecto=tramExp.getResponsablesTramitacion().getId();		
			this.selectedNuevoResponsableTramId = idResponsableDefecto;
			}else {
			idResponsableDefecto= expedientes.getResponsable().getId();
			this.selectedNuevoResponsableTramId = idResponsableDefecto;
		}
		}
		return this.selectedNuevoResponsableTramId;
		
	}
	
	public Long obtenerResponsableSubTramite(Long idSubTramite){
		Long idResponsableDefecto=null;
		if(idSubTramite!=null) {
			
		
		CfgExpedienteSubtramite subTramExp= cfgExpedienteSubTramiteService.obtener(idSubTramite);
		/*si no está informado (esta a null) y está marcado el "Id_Resp_tram", 
		 * se preselecciona por defecto el responsable del trámite superior.*/
		
		
		if(subTramExp.getResponsablesTramitacion()==null && subTramExp.getResponsable().equals(true)) {
			idResponsableDefecto=this.tramiteExpediente.getResponsable().getId();	
			this.selectedNuevoResponsableSubTramId=idResponsableDefecto;		
			
			/*si  está informado se preselecciona el responsable por defecto.*/
		}else if(subTramExp.getResponsablesTramitacion()!=null) {
			
			idResponsableDefecto=subTramExp.getResponsablesTramitacion().getId();	
			this.selectedNuevoResponsableSubTramId=idResponsableDefecto;
			
		}else {
			
			//ninguna de las anteriores premisas, se preselecciona el del expediente
			
			idResponsableDefecto= expedientes.getResponsable().getId();
			this.selectedNuevoResponsableSubTramId=idResponsableDefecto;
		}
		}
		return this.selectedNuevoResponsableSubTramId;
		
	}
	
	private void guardarDetalleTramiteExpC016(TramiteExpediente traExp, DetalleExpdteTram detExpTram) {
		
		
		if(traExp.getSelectedNuevoFirmanteId() != null) {
			detExpTram.setFirmante(usuarioService.obtener(traExp.getSelectedNuevoFirmanteId()));
		}else {
			detExpTram.setFirmante(null);
		}
		if(traExp.getSelectedNuevoTipoFirmaId() != null) {
			detExpTram.setValorDominioTipoFirma(valoresDominioService.obtener(traExp.getSelectedNuevoTipoFirmaId()));
		}else {
			detExpTram.setValorDominioTipoFirma(null);
		}
	}
	
	
	private void guardarDetalleTramiteExpC010(TramiteExpediente traExp, DetalleExpdteTram detExpTram) {
		if(traExp.getSelectedNuevoPropuestaApiId() != null) {
			detExpTram.setValorDominioPropuestaApi(valoresDominioService.obtener(traExp.getSelectedNuevoPropuestaApiId()));
		}else {
			detExpTram.setValorDominioPropuestaApi(null);
		}
	}
	
	private void selectedNuevoResultNotificacion(TramiteExpediente traExp, DetalleExpdteTram detExpTram) {
		if(traExp.getSelectedNuevoResulNotificacionId() != null) {
			detExpTram.setValorResultadoNotificacion(valoresDominioService.obtener(traExp.getSelectedNuevoResulNotificacionId()));	
		} else {
			detExpTram.setValorResultadoNotificacion(null);
		}
	}
	
	private void listadoTramitesParaTramiteLibre () {
		List<CfgExpedienteTramite> listaTramitesAux = new ArrayList<>();
		List<String> listaTramitesDescripcionAux = new ArrayList<>();
		for (int i = 0; i < listaTramites.size(); i++){
			if(!listaTramitesDescripcionAux.contains(listaTramites.get(i).getDescripcion())) {
				listaTramitesDescripcionAux.add(listaTramites.get(i).getDescripcion());
				listaTramitesAux.add(listaTramites.get(i));
			}
		}
		listaTramites = listaTramitesAux;
	}
	
	@Transactional(TxType.REQUIRED)
	public void generarExpedientePsan (TramiteExpediente tramiteExpedienteActual) {
		try {
			ValoresDominio valorDominioTipoExp = valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_TIPO_EXPEDIENTE, Constantes.PSAN);
			String numeroExpedientePsan = seriesService.nextNumeroSerie(valorDominioTipoExp.getCodigo(), FechaUtils.ahora());
			ValoresDominio valorDominioSituacion = valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_SIT, Constantes.PIPS);
			ResponsablesTramitacion responsablesTramitacion = responsablesTramitacionService.findResponsableTramitacionByCodResp(tramiteExpedienteActual.getDetalleExpdteTram().getValorInstructorAPI().getCodigo());
			Expedientes expedienteNuevoPsan = new Expedientes();
			expedienteNuevoPsan.setNumExpediente(numeroExpedientePsan);		
			expedienteNuevoPsan.setNombreExpediente(Constantes.PSAN+" "+expedientes.getNumExpediente());
			expedienteNuevoPsan.setFechaEntrada(FechaUtils.ahora());
			expedienteNuevoPsan.setValorTipoExpediente(valorDominioTipoExp);			
			expedienteNuevoPsan.setValorSituacionExpediente(valorDominioSituacion);
			expedienteNuevoPsan.setResponsable(responsablesTramitacion);
			expedienteNuevoPsan.setApi(false);
			expedienteNuevoPsan.setCompetenciaCtpda(true);
			expedienteNuevoPsan.setAepd(false);	
			expedienteNuevoPsan.setImposicionMedidas(false);
			expedienteNuevoPsan.setOposicionPersona(false);
			expedienteNuevoPsan.setOposicionRepresentante(false);
			expedienteNuevoPsan.setAbstencionRecusacion(expedientes.getAbstencionRecusacion());
			expedienteNuevoPsan.setTramitacionAnonima(expedientes.getTramitacionAnonima());
			expedienteNuevoPsan.setValorInstructorAPI(tramiteExpedienteActual.getDetalleExpdteTram().getValorInstructorAPI());
			CfgTipoExpediente cfgTipoExp = cfgTipoExpedienteService.obtenerCfgTipoExpedientePorValorTipoExpediente(expedienteNuevoPsan.getValorTipoExpediente().getId());
			
			if (cfgTipoExp.getTipoTramiteSeg1() != null) {
				expedienteNuevoPsan.setDescSeguimiento1(cfgTipoExp.getTipoTramiteSeg1().getDescripcionAbrev()+":");
			}

			if (cfgTipoExp.getTipoTramiteSeg2() != null) {
				expedienteNuevoPsan.setDescSeguimiento2(cfgTipoExp.getTipoTramiteSeg2().getDescripcionAbrev()+":");
			}

			if (cfgTipoExp.getTipoTramiteSeg3() != null) {
				expedienteNuevoPsan.setDescSeguimiento3(cfgTipoExp.getTipoTramiteSeg3().getDescripcionAbrev()+":");
			}
			
			expedienteNuevoPsan = expedientesService.guardar(expedienteNuevoPsan);	
			
			List<PersonasExpedientes> personasExpedientes = personasExpedientesService.obtenerListaPersonaExpedientePorExpedientePrincipalYNoPrincipal(expedientes.getId());
			CfgTipoExpediente cfgTipoExpediente = cfgTipoExpedienteService.obtenerCfgTipoExpedientePorValorTipoExpediente(valorDominioTipoExp.getId());
			for(int i = 0; i < personasExpedientes.size(); i++) {
				PersonasExpedientes personasExpedientesNueva = new PersonasExpedientes();
				personasExpedientesNueva.setPersonas(personasExpedientes.get(i).getPersonas());
				personasExpedientesNueva.setPersonasRepre(personasExpedientes.get(i).getPersonasRepre());
				personasExpedientesNueva.setPrincipal(personasExpedientes.get(i).getPrincipal());
				personasExpedientesNueva.setExpediente(expedienteNuevoPsan);
				personasExpedientesNueva.setInteresado(personasExpedientes.get(i).getInteresado());
				personasExpedientesNueva.setValoresRelacionExpPer(cfgTipoExpediente.getValorMotivoRelacionPersona());
				personasExpedientesService.guardar(personasExpedientesNueva);
			}
			
			List<SujetosObligadosExpedientes> sujetosObligadosExpedientes = sujetosObligadosExpedientesService.obtenerSujetosObligadosExpedientePrincipalYNoPrincipalPorExpediente(expedientes.getId());
			for(int i = 0; i < sujetosObligadosExpedientes.size(); i++) {
				SujetosObligadosExpedientes sujetosObligadosExpedientesNuevo = new SujetosObligadosExpedientes();
				sujetosObligadosExpedientesNuevo.setPersonas(sujetosObligadosExpedientes.get(i).getPersonas());
				sujetosObligadosExpedientesNuevo.setPrincipal(sujetosObligadosExpedientes.get(i).getPrincipal());
				sujetosObligadosExpedientesNuevo.setSujetosObligados(sujetosObligadosExpedientes.get(i).getSujetosObligados());
				sujetosObligadosExpedientesNuevo.setExpediente(expedienteNuevoPsan);
				sujetosObligadosExpedientesNuevo.setValoresRelacionExpSuj(cfgTipoExpediente.getValorMotivoRelacionSujeto());
				sujetosObligadosExpedientesService.guardar(sujetosObligadosExpedientesNuevo);
			}
			
			ValoresDominio valorDominioMotivoRelacionExpRelacion = valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_MOT_REL, Constantes.PSAN);
			ExpedientesRelacion expedientesRelacionNuevo = new ExpedientesRelacion();
			expedientesRelacionNuevo.setExpedienteOrigen(expedientes);
			expedientesRelacionNuevo.setExpedienteRelacionado(expedienteNuevoPsan);
			expedientesRelacionNuevo.setMotivo(valorDominioMotivoRelacionExpRelacion);
			expedientesRelacionService.guardar(expedientesRelacionNuevo);	
			
			TramiteExpediente tramiteExpedienteAux = tramiteExpedienteService.obtener(tramiteExpedienteActual.getId());
			TramiteExpediente tramiteExpedienteNuevo = new TramiteExpediente();
			tramiteExpedienteNuevo.setExpediente(expedienteNuevoPsan);
			tramiteExpedienteNuevo.setTipoTramite(tramiteExpedienteAux.getTipoTramite());	
			tramiteExpedienteNuevo.setResponsable(tramiteExpedienteAux.getResponsable());
			tramiteExpedienteNuevo.setTramiteExpedienteSup(tramiteExpedienteAux.getTramiteExpedienteSup());	
			tramiteExpedienteNuevo.setUsuarioTramitador(tramiteExpedienteAux.getUsuarioTramitador());	
			tramiteExpedienteNuevo.setNivel(tramiteExpedienteAux.getNivel());	
			tramiteExpedienteNuevo.setDescripcion(tramiteExpedienteAux.getDescripcion());				
			tramiteExpedienteNuevo.setDescripcionAbrev(tramiteExpedienteAux.getDescripcionAbrev());
			tramiteExpedienteNuevo.setFechaIni(tramiteExpedienteAux.getFechaFin());	
			tramiteExpedienteNuevo.setFechaTramite(tramiteExpedienteAux.getFechaTramite());		
			tramiteExpedienteNuevo.setInformacionRelevante(tramiteExpedienteAux.getInformacionRelevante());	
			tramiteExpedienteNuevo.setSituacionAdicional(tramiteExpedienteAux.getSituacionAdicional());	
			ObservacionesExpedientes obsExp = tramiteExpedienteAux.getObservaciones();
			obsExp = observacionesExpedientesService.guardarObservacionesExpedientes(obsExp, obsExp.getTexto(), Constantes.COD_VAL_DOM_TIPOBS_TRA, expedientes);
			tramiteExpedienteNuevo.setObservaciones(obsExp);


			tramiteExpedienteNuevo.setObservaciones(tramiteExpedienteAux.getObservaciones());	
			tramiteExpedienteNuevo.setActivo(tramiteExpedienteAux.getActivo());
			tramiteExpedienteNuevo.setFinalizado(tramiteExpedienteAux.getFinalizado());	
			tramiteExpedienteNuevo.setFechaFinReal(tramiteExpedienteAux.getFechaFinReal());	
			tramiteExpedienteNuevo.setFechaFin(tramiteExpedienteAux.getFechaFin());	
			tramiteExpedienteNuevo.setUsuarioFinalizacion(tramiteExpedienteAux.getUsuarioFinalizacion());			
			tramiteExpedienteNuevo = tramiteExpedienteService.guardar(tramiteExpedienteNuevo);
			TramiteExpediente tramiteExpedienteNuevoAux = tramiteExpedienteService.obtener(tramiteExpedienteNuevo.getId());
			
			obsExp.setTramiteExpdte(tramiteExpedienteNuevo);
			observacionesExpedientesService.guardar(obsExp);	
			
			DetalleExpdteTram detalleExpdteTramAux = detalleExpdteTramService.obtener(detalleExpdteTram.getId());
			DetalleExpdteTram detalleExpdteTramNuevo = new DetalleExpdteTram();
			detalleExpdteTramNuevo.setExpediente(expedienteNuevoPsan);
			detalleExpdteTramNuevo.setTramiteExpediente(tramiteExpedienteNuevo);	
			detalleExpdteTramNuevo.setValorCanalEntrada(detalleExpdteTramAux.getValorCanalEntrada());	
			detalleExpdteTramNuevo.setValorMotivoInadmision(detalleExpdteTramAux.getValorMotivoInadmision());	
			detalleExpdteTramNuevo.setIdentifEntrada(detalleExpdteTramAux.getIdentifEntrada());	
			detalleExpdteTramNuevo.setValorTipoInteresado(detalleExpdteTramAux.getValorTipoInteresado());	
			detalleExpdteTramNuevo.setValorCanalSalida(detalleExpdteTramAux.getValorCanalSalida());	
			detalleExpdteTramNuevo.setDatosCanalEntrada(detalleExpdteTramAux.getDatosCanalEntrada());
			detalleExpdteTramNuevo.setDatosCanalSalida(detalleExpdteTramAux.getDatosCanalSalida());
			detalleExpdteTramNuevo.setValorResultadoNotificacion(detalleExpdteTramAux.getValorResultadoNotificacion());
			detalleExpdteTramNuevo.setSubsanaAdecudamente(detalleExpdteTramAux.getSubsanaAdecudamente());
			detalleExpdteTramNuevo.setValorTipoAdmision(detalleExpdteTramAux.getValorTipoAdmision());
			detalleExpdteTramNuevo.setInfoRemitida(detalleExpdteTramAux.getInfoRemitida());
			detalleExpdteTramNuevo.setValorSentidoResolucion(detalleExpdteTramAux.getValorSentidoResolucion());
			detalleExpdteTramNuevo.setValorTipoResolucion(detalleExpdteTramAux.getValorTipoResolucion());
			detalleExpdteTramNuevo.setNumResolucion(detalleExpdteTramAux.getNumResolucion());
			detalleExpdteTramNuevo.setAcreditaCumplimiento(detalleExpdteTramAux.getAcreditaCumplimiento());
			detalleExpdteTramNuevo.setFechaEntrada(detalleExpdteTramAux.getFechaEntrada());
			detalleExpdteTramNuevo.setFechaEnvio(detalleExpdteTramAux.getFechaEnvio());
			detalleExpdteTramNuevo.setFechaNotificacion(detalleExpdteTramAux.getFechaNotificacion());
			detalleExpdteTramNuevo.setFechaEmision(detalleExpdteTramAux.getFechaEmision());
			detalleExpdteTramNuevo.setFechaFirma(detalleExpdteTramAux.getFechaFirma());
			detalleExpdteTramNuevo.setFechaInforme(detalleExpdteTramAux.getFechaInforme());
			detalleExpdteTramNuevo.setFechaSubsanacion(detalleExpdteTramAux.getFechaSubsanacion());
			detalleExpdteTramNuevo.setFechaRespuesta(detalleExpdteTramAux.getFechaRespuesta());
			detalleExpdteTramNuevo.setFechaAcreditacion(detalleExpdteTramAux.getFechaAcreditacion());
			detalleExpdteTramNuevo.setFechaLimite(detalleExpdteTramAux.getFechaLimite());
			detalleExpdteTramNuevo.setFechaResolucion(detalleExpdteTramAux.getFechaResolucion());
			detalleExpdteTramNuevo.setValorTipoPlazo(detalleExpdteTramAux.getValorTipoPlazo());
			detalleExpdteTramNuevo.setPlazo(detalleExpdteTramAux.getPlazo());
			detalleExpdteTramNuevo.setAfectaPlazos(detalleExpdteTramAux.getAfectaPlazos());
			detalleExpdteTramNuevo.setActivo(detalleExpdteTramAux.getActivo());
			detalleExpdteTramNuevo.setValorDominioInteresado(detalleExpdteTramAux.getValorDominioInteresado());
			detalleExpdteTramNuevo.setPersonasInteresado(detalleExpdteTramAux.getPersonasInteresado());
			detalleExpdteTramNuevo.setSujetosObligadosInteresado(detalleExpdteTramAux.getSujetosObligadosInteresado());
			detalleExpdteTramNuevo.setValorCanalInfEntrada(detalleExpdteTramAux.getValorCanalInfEntrada());
			detalleExpdteTramNuevo.setValorCanalInfSalida(detalleExpdteTramAux.getValorCanalInfSalida());
			detalleExpdteTramNuevo.setValorInstructorAPI(detalleExpdteTramAux.getValorInstructorAPI());
			detalleExpdteTramNuevo.setApi(detalleExpdteTramAux.getApi());
			detalleExpdteTramNuevo.setSeguimientoCabecera(detalleExpdteTramAux.getSeguimientoCabecera());
			detalleExpdteTramNuevo.setImposicionMedidas(detalleExpdteTramAux.getImposicionMedidas());
			detalleExpdteTramNuevo.setValorDominioPropuestaApi(detalleExpdteTramAux.getValorDominioPropuestaApi());
			detalleExpdteTramNuevo.setNumeroPsan(numeroExpedientePsan);
			detalleExpdteTramNuevo.setExtractoExpediente(detalleExpdteTramAux.getExtractoExpediente());
			detalleExpdteTramNuevo.setTextoExtractoExpediente(detalleExpdteTramAux.getTextoExtractoExpediente());
			detalleExpdteTramNuevo.setAntecedentesExpediente(detalleExpdteTramAux.getAntecedentesExpediente());
			detalleExpdteTramNuevo.setTextoAntecedentesExpediente(detalleExpdteTramAux.getTextoAntecedentesExpediente());
			
			detalleExpdteTramService.guardar(detalleExpdteTramNuevo);
			
			List<AgrupacionesExpedientes> agrupExpedientes = agrupacionesExpedientesService.findByTramite(tramiteExpedienteActual.getId());
			for (int j = 0; j < agrupExpedientes.size(); j++) {
				
				AgrupacionesExpedientes agrupExpedientesAux = agrupacionesExpedientesService.obtener(agrupExpedientes.get(j).getId());
				AgrupacionesExpedientes agrupExpedientesNueva = new AgrupacionesExpedientes();
				agrupExpedientesNueva.setExpediente(expedienteNuevoPsan);
				agrupExpedientesNueva.setTramiteExpediente(tramiteExpedienteNuevoAux);
				agrupExpedientesNueva.setFechaCreacion(FechaUtils.hoy());
				Usuario usuario = (Usuario)JsfUtils.getSessionAttribute(Constantes.USUARIO);
				agrupExpedientesNueva.setUsuCreacion(usuario.getLogin());
				agrupExpedientesNueva.setNVersion(0L);
				agrupExpedientesNueva.setDescripcion(agrupExpedientesAux.getDescripcion());
				agrupExpedientesNueva.setDescripcionAbrev(agrupExpedientesAux.getDescripcionAbrev());
				agrupExpedientesNueva.setActivo(agrupExpedientesAux.getActivo());
				agrupExpedientesNueva.setOrden(1L);
				agrupExpedientesNueva.setCategoria(agrupExpedientesAux.getCategoria());
				agrupExpedientesNueva.setVerPestanaDoc(agrupExpedientesAux.getVerPestanaDoc());
				agrupacionesExpedientesService.guardar(agrupExpedientesNueva);
			}
			
			
			
			List<DocumentosExpedientes> documentosExpedientes = documentosExpedientesService.findDocumentosActivosByExpdteIdTramExp(expedientes.getId(),tramiteExpedienteActual.getId());
			for(int i = 0; i < documentosExpedientes.size(); i++) {
				DocumentosExpedientes documentosExpedientesAux = documentosExpedientesService.obtener(documentosExpedientes.get(i).getId());
				DocumentosExpedientes documentosExpedientesNuevo = new DocumentosExpedientes();
				documentosExpedientesNuevo.setDocumento(documentosExpedientes.get(i).getDocumento());
				documentosExpedientesNuevo.setExpediente(expedienteNuevoPsan);
				documentosExpedientesNuevo.setTramiteExpediente(tramiteExpedienteNuevoAux);
				documentosExpedientesNuevo.setDescripcionDocumento(documentosExpedientesAux.getDescripcionDocumento());
				documentosExpedientesNuevo.setDescripcionAbrevDocumento(documentosExpedientesAux.getDescripcionAbrevDocumento());
				documentosExpedientesNuevo.setAgrupacionDocumentos(documentosExpedientesAux.getAgrupacionDocumentos());
				documentosExpedientesNuevo.setActivo(documentosExpedientesAux.getActivo());
				documentosExpedientesNuevo.setCategoria(documentosExpedientesAux.getCategoria());
				documentosExpedientesNuevo = documentosExpedientesService.guardar(documentosExpedientesNuevo);		
				DocumentosExpedientes documentosExpedientesNuevoAux = documentosExpedientesService.obtener(documentosExpedientesNuevo.getId());
				
				DocumentosExpedientesTramites documentosExpedientesTramites = documentosExpedientesTramitesService.findDocExpTramByIdTramExpAndDocExp(tramiteExpedienteActual.getId(),documentosExpedientes.get(i).getId());
				if(documentosExpedientesTramites != null && documentosExpedientesTramites.getId() != null) {
					DocumentosExpedientesTramites documentosExpedientesTramitesNuevo = new DocumentosExpedientesTramites();
					documentosExpedientesTramitesNuevo.setDocumentoExpediente(documentosExpedientesNuevoAux);
					documentosExpedientesTramitesNuevo.setTramiteExpediente(tramiteExpedienteNuevoAux);
					documentosExpedientesTramitesNuevo.setOrigen(documentosExpedientesTramites.getOrigen());
					documentosExpedientesTramitesService.guardar(documentosExpedientesTramitesNuevo);
				}
				
			}
			
			utilsComun.generarPlazoExpdte(tramiteExpedienteNuevo.getId(), null, expedienteNuevoPsan.getFechaEntrada(), Constantes.COD_VAL_DOM_RES, Constantes.COD_VAL_DOM_DN, 0, "A", expedienteNuevoPsan);	
			
			Usuario usuario = (Usuario)JsfUtils.getSessionAttribute(Constantes.USUARIO);
			TareasExpedienteService.AccionTarea accion = tareasExpedienteService.nuevaAccionTareaTramite(TareasExpedienteService.AccionTarea.ACCION_ALTA, usuario, expedienteNuevoPsan.getId(),tramiteExpedienteNuevo.getId());
			tareasExpedienteService.crearTareasAuto(accion);
			
			detalleExpdteTram.setNumeroPsan(numeroExpedientePsan);
			detalleExpdteTramService.guardar(detalleExpdteTram);
			
			FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO,new FacesMessage(FacesMessage.SEVERITY_INFO, "","Expediente Procedimiento sancionador "+numeroExpedientePsan+ " generado correctamente"));
			
			navegacionBean.setTxtMigaPan(Constantes.EDICION_EXPEDIENTE+expedientes.getMigaDePan()+navegacionBean.expedientesRelacionadosMotivoPsan(expedientes.getId()));
			
		} catch (BaseException e) {
			FacesMessage message = null;
			if(e.getMessage().contains(NOEXISTEREGISTROCONFIG)) {
				message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(NOEXISTEREGISTROCONFIG));
			}else {
				message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MENSAJEERROR));
			}
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error("generarExpedientePsan - " + e.getMessage());
		}
	}
	
	public void abrirBuscadorExpedientesPsan () {
		limpiarFiltroBuscadorExpPsan();
			
		PrimeFaces.current().executeScript("PF('dialogBuscadorExpPsan').show();");
	}
	
	public void limpiarFiltroBuscadorExpPsan() {
		numeroExpedienteFiltroBuscadorExpPsan = "";
		nombreExpedienteFiltroBuscadorExpPsan = "";
		fechaEntradaFiltroBuscadorExpPsan = null;
	}
	
	public void vincularExpedientePsan (Expedientes expedientePsanSeleccionado) {
		try {
			ValoresDominio valorDominioMotivoRelacionExpRelacion = valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_MOT_REL, Constantes.PSAN);
			ExpedientesRelacion expedientesRelacionNuevo = new ExpedientesRelacion();
			expedientesRelacionNuevo.setExpedienteOrigen(expedientes);
			expedientesRelacionNuevo.setExpedienteRelacionado(expedientePsanSeleccionado);
			expedientesRelacionNuevo.setMotivo(valorDominioMotivoRelacionExpRelacion);
			expedientesRelacionService.guardar(expedientesRelacionNuevo);	
			
			detalleExpdteTram.setNumeroPsan(expedientePsanSeleccionado.getNumExpediente());
			detalleExpdteTramService.guardar(detalleExpdteTram);
			
			PrimeFaces.current().ajax().update("formFormularioExpedientes:tabViewPestanasExpediente:generarExpPsanC014-#{tramExp.id}");
			PrimeFaces.current().ajax().update("formFormularioExpedientes:tabViewPestanasExpediente:vincularExpPsanC014-#{tramExp.id}");
			
			PrimeFaces.current().executeScript("PF('dialogBuscadorExpPsan').hide();");
						
			FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO,new FacesMessage(FacesMessage.SEVERITY_INFO, "","Expediente Procedimiento sancionador "+expedientePsanSeleccionado.getNumExpediente()+ " vinculado correctamente"));
		
			navegacionBean.setTxtMigaPan(Constantes.EDICION_EXPEDIENTE+expedientes.getMigaDePan()+navegacionBean.expedientesRelacionadosMotivoPsan(expedientes.getId()));
			
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
		
	}
	
	public String getIdTablaListadoNotif(Long sufijoId) {
		return "tablaNotificacionesTramite_" + sufijoId;
	}
	
	public String getIdTablaListadoFirm(Long sufijoId) {
		return "tablaFirmasTramite_" + sufijoId;
	}
	
/**
 * LISTADO SUBTRAMITES NOTIFICACION Y COMUNICACION POR TRAMITE
 * */
	
	private Map<Long, LazyDataModelByQueryService<NotificacionesTramiteMaestra> > mapLazyNotificacionesTramite = new HashMap<>();

	public LazyDataModelByQueryService<NotificacionesTramiteMaestra> getLazyNotificacionesTramite(Long idTramiteExp){
		return getLazyNotificacionesTramiteByMap(idTramiteExp, mapLazyNotificacionesTramite);
	}
	
	private LazyDataModelByQueryService<NotificacionesTramiteMaestra> getLazyNotificacionesTramiteByMap(Long idTramiteExp, Map<Long, LazyDataModelByQueryService<NotificacionesTramiteMaestra> > lazyMap) {
		LazyDataModelByQueryService<NotificacionesTramiteMaestra> lazy = 
				lazyMap.get(idTramiteExp);
		
		if(lazy == null) {
			lazy = crearLazy(idTramiteExp);
			lazyMap.put(idTramiteExp, lazy);
		}
		
		return lazy;
	}
	
	private LazyDataModelByQueryService<NotificacionesTramiteMaestra> crearLazy(Long idTramiteExp) {
		final LazyDataModelByQueryService<NotificacionesTramiteMaestra> lazy
			= new LazyDataModelByQueryService<>(NotificacionesTramiteMaestra.class, notificacionesTramiteMaestraService);
			
		lazy.setPreproceso((a, b, c, filters) ->
			filters.put("tramExpSup.id", new MyFilterMeta(idTramiteExp))
		);

		return lazy;
	}
	
	public String getIdTablaListadoNotifEvol(Long sufijoId) {
		return "tablaNotificacionesTramiteEvol_" + sufijoId;
	}
	
	/**
	 * LISTADO SUBTRAMITES FIRMAS TRAMITE
	 * */
		
		private Map<Long, LazyDataModelByQueryService<FirmasTramiteMaestra> > mapLazyFirmasTramite = new HashMap<>();

		public LazyDataModelByQueryService<FirmasTramiteMaestra> getLazyFirmasTramite(Long idTramiteExp){
			return getLazyFirmasTramiteByMap(idTramiteExp, mapLazyFirmasTramite);
		}
		
		private LazyDataModelByQueryService<FirmasTramiteMaestra> getLazyFirmasTramiteByMap(Long idTramiteExp, Map<Long, LazyDataModelByQueryService<FirmasTramiteMaestra> > lazyMap) {
			LazyDataModelByQueryService<FirmasTramiteMaestra> lazy = 
					lazyMap.get(idTramiteExp);
			
			if(lazy == null) {
				lazy = crearLazyFirmas(idTramiteExp);
				lazyMap.put(idTramiteExp, lazy);
			}
			
			return lazy;
		}
		
		private LazyDataModelByQueryService<FirmasTramiteMaestra> crearLazyFirmas(Long idTramiteExp) {
			final LazyDataModelByQueryService<FirmasTramiteMaestra> lazy
				= new LazyDataModelByQueryService<>(FirmasTramiteMaestra.class, firmasTramiteMaestraService);
				
			lazy.setPreproceso((a, b, c, filters) ->
				filters.put("tramExpSup.id", new MyFilterMeta(idTramiteExp))
			);

			return lazy;
		}
		
		public String getIdTablaListadoFirmEvol(Long sufijoId) {
			return "tablaFirmasTramiteEvol_" + sufijoId;
		}
		
	private void cargarDatosDetalleTramExp00123456791112131417182423 (TramiteExpediente trExp) {
		if(Constantes.C001.equals(trExp.getTipoTramite().getComportamiento())
				|| Constantes.C002.equals(trExp.getTipoTramite().getComportamiento())
				|| Constantes.C003.equals(trExp.getTipoTramite().getComportamiento())
				|| Constantes.C004.equals(trExp.getTipoTramite().getComportamiento())
				|| Constantes.C005.equals(trExp.getTipoTramite().getComportamiento())
				|| Constantes.C006.equals(trExp.getTipoTramite().getComportamiento())
				|| Constantes.C007.equals(trExp.getTipoTramite().getComportamiento())
				|| Constantes.C009.equals(trExp.getTipoTramite().getComportamiento())
				|| Constantes.C011.equals(trExp.getTipoTramite().getComportamiento())
				|| Constantes.C012.equals(trExp.getTipoTramite().getComportamiento())
				|| Constantes.C013.equals(trExp.getTipoTramite().getComportamiento())
				|| Constantes.C014.equals(trExp.getTipoTramite().getComportamiento())
				|| Constantes.C017.equals(trExp.getTipoTramite().getComportamiento())
				|| Constantes.C018.equals(trExp.getTipoTramite().getComportamiento())
				|| Constantes.C024.equals(trExp.getTipoTramite().getComportamiento())
				|| Constantes.C023.equals(trExp.getTipoTramite().getComportamiento())){	
			trExp.setListaIdentifIntSujOblig(sujetosObligadosExpedientesService.obtenerSujetosObligadosExpediente(expedientes.getId()));
			List<PersonasExpedientes> listaPersonasPorExpediente = personasExpedientesService.obtenerPersPorExpediente(expedientes.getId());
			trExp.setListaIdentifIntPersDTO(cargarPersonasYRepresentantes(listaPersonasPorExpediente));
			
			
			trExp.setListaIdentifIntDpd(sujetosObligadosExpedientesService.obtenerDpdExpediente(expedientes.getId()));
			trExp.setListaValoresDominioIdentifInteresado(valoresDominioService.findValoresDominioActivosByCodigoDominio(ValoresDominioService.COD_AUTCOMP));
			
			cargarDatosDetalleTramExp0079131417341112(trExp);
							
			aplicarDatosInteresado(trExp, detalleExpdteTram);
			
			if (detalleExpdteTram.getValorCanalEntrada() != null) {
				trExp.setSelectedNuevoCanalEntradaId(detalleExpdteTram.getValorCanalEntrada().getId());
			}
			
			if(detalleExpdteTram.getValorDominioActoRec() != null){
				trExp.setSelectedNuevoActoRecId(detalleExpdteTram.getValorDominioActoRec().getId());	
			}	
		}
	}
	
	
	private List<PersonaDTO> cargarPersonasYRepresentantes(List<PersonasExpedientes> listaPersExp)
	{
		List<PersonaDTO> listadoIdentifPersonasRepre = new ArrayList<>();
		for(PersonasExpedientes perExp: listaPersExp) {
			Personas perRepre = perExp.getPersonasRepre();
			Personas per = perExp.getPersonas();
			PersonaDTO personaDTO = null;
			if(Boolean.TRUE.equals(perExp.getInteresado()))
			{
				personaDTO = new PersonaDTO(per.getId(), "(I) " + per.getNombreAp());	
			}else {
				personaDTO = new PersonaDTO(per.getId(), per.getNombreAp());
			}
			
			listadoIdentifPersonasRepre.add(personaDTO);
			if(perRepre != null)
			{
				PersonaDTO personaRepreDTO = null;
				if(listaPersExp.size() == 1)
				{
					personaRepreDTO = new PersonaDTO(perRepre.getId(),"(R) " + perRepre.getNombreAp());
				}else
				{
					personaRepreDTO = new PersonaDTO(perRepre.getId(),"(R) " + perRepre.getNombreAp() + " (" + per.getNombreAp() + ")");	
				}
				
				listadoIdentifPersonasRepre.add(personaRepreDTO);
			}
		}
		
		return listadoIdentifPersonasRepre;
	}
	
	private void cargarDatosDetalleTramExp010 (TramiteExpediente trExp) {
		if(Constantes.C010.equals(trExp.getTipoTramite().getComportamiento()) && detalleExpdteTram.getValorDominioPropuestaApi() != null) {
			trExp.setSelectedNuevoPropuestaApiId(detalleExpdteTram.getValorDominioPropuestaApi().getId());				
		}		
	}
	
	private void detExpTramComportamientoC003(DetalleExpdteTram detExpTram) {
		if (Constantes.C003.equals(cfgExpTramite.getTipoTramite().getComportamiento())) {
			detExpTram.setValorResultadoNotificacion(valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_RES_NOTIF,Constantes.COD_VAL_DOM_PDTE));
			detExpTram.setValorTipoPlazo(valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_TIP_PLA_TEMP,Constantes.COD_VAL_DOM_DN));
		}
	}
	
	private void detExpTramComportamientoC005(DetalleExpdteTram detExpTram) {
		if(Constantes.C005.equals(cfgExpTramite.getTipoTramite().getComportamiento())) {
			detExpTram.setFechaEntrada(expedientes.getFechaEntrada());
			detExpTram.setFechaRegistro(expedientes.getFechaEntrada());
		}
	}
	
	
	private void detExpTramComportamientoC007(DetalleExpdteTram detExpTram) {
		if(Constantes.C007.equals(cfgExpTramite.getTipoTramite().getComportamiento())){
			detExpTram.setValorTipoPlazo(valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_TIP_PLA_TEMP,Constantes.COD_VAL_DOM_DN));
			detExpTram.setAfectaPlazos(true);
		}
	}
	
	private void detExpTramComportamientoC009(DetalleExpdteTram detExpTram) {
		if(Constantes.C009.equals(cfgExpTramite.getTipoTramite().getComportamiento())){
			detExpTram.setValorTipoPlazo(valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_TIP_PLA_TEMP,Constantes.COD_VAL_DOM_DN));
			detExpTram.setAfectaPlazos(false);
		}
	}
	
	private void detExpTramComportamientoC012024(DetalleExpdteTram detExpTram,TramiteExpediente tramExp) {
		if(Constantes.C012.equals(cfgExpTramite.getTipoTramite().getComportamiento())
				|| Constantes.C024.equals(cfgExpTramite.getTipoTramite().getComportamiento())){
			if(tramExp.getSelectedNuevoSentidoResolucionId() != null){
				detExpTram.setValorSentidoResolucion(valoresDominioService.obtener(tramExp.getSelectedNuevoSentidoResolucionId()));	
			}	
			
			/** RECUPERAMOS EL TIPO DE RESOLUCION QUE TIENE ASOCIADO EL TIPO DE EXPEDIENTE*/
			Expedientes exp = tramExp.getExpediente();
			CfgTipoExpediente cfgTipoExp = cfgTipoExpedienteService.obtenerCfgTipoExpedientePorValorTipoExpediente(exp.getValorTipoExpediente().getId());
			if(cfgTipoExp != null){
				
				ValoresDominio valorTipoResol = null;
				if(Constantes.C012.equals(tramExp.getTipoTramite().getComportamiento()))
				{
					valorTipoResol = cfgTipoExp.getValorTipoResolucion();
				}else
				{
					valorTipoResol = cfgTipoExp.getValorTipoResolRecurso();
				}
				if(valorTipoResol != null){
					detExpTram.setValorTipoResolucion(valorTipoResol);
					tramExp.setSelectedNuevoTipoResolucionId(valorTipoResol.getId());
				}else{
					detExpTram.setValorTipoResolucion(null);
					tramExp.setSelectedNuevoTipoResolucionId(null);
				}
			}
		}
	}
	
	private void detExpTramComportamientoC013(DetalleExpdteTram detExpTram) {
		if(Constantes.C013.equals(cfgExpTramite.getTipoTramite().getComportamiento())){
			detExpTram.setValorTipoPlazo(valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_TIP_PLA_TEMP,Constantes.COD_VAL_DOM_DN));	
		}
	}
	
	private void detExpTramComportamientoC014(DetalleExpdteTram detExpTram) {
		if(Constantes.C014.equals(cfgExpTramite.getTipoTramite().getComportamiento())){
			detExpTram.setApi(true);
			detExpTram.setValorTipoPlazo(valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_TIP_PLA_TEMP,Constantes.COD_VAL_DOM_DN));	
		}
	}
	
	private void detExpTramComportamientoC017(DetalleExpdteTram detExpTram) {
		if(Constantes.C017.equals(cfgExpTramite.getTipoTramite().getComportamiento())){
			detExpTram.setValorTipoPlazo(valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_TIP_PLA_TEMP,Constantes.COD_VAL_DOM_DN));	
		}
	}
	
	private void detExpTramComportamientoC008(DetalleExpdteTram detExpTram,TramiteExpediente tramExp) {
		if(Constantes.C008.equals(cfgExpTramite.getTipoTramite().getComportamiento())) {
			ValoresDominio tipoAdmision = new ValoresDominio();
			if(cfgExpTramite.getTipoTramite().getCodigo().equals(Constantes.TIP_TRAM_ACADM)) {
				tipoAdmision = valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_DOM_TIP_ADM, Constantes.COD_VAL_DOM_ADM);
			}else if(cfgExpTramite.getTipoTramite().getCodigo().equals(Constantes.TIP_TRAM_ACADMHE)) {
				tipoAdmision = valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_DOM_TIP_ADM, Constantes.COD_VAL_DOM_AHE);
			}else if(cfgExpTramite.getTipoTramite().getCodigo().equals(Constantes.TIP_TRAM_ACNOADM)) {
				tipoAdmision = valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_DOM_TIP_ADM, Constantes.COD_VAL_DOM_INA);
			}
			tramExp.setSelectedNuevoTipoAdmisionId(tipoAdmision.getId());
			detExpTram.setValorTipoAdmision(tipoAdmision);
		}
	}
	
	private void selectedNuevoTramiteIdIfOrigenAltaExpediente (String origenAltaTramite, CfgExpedienteTramite cfgExpedienteTramite) {
		if(ORIGENALTAEXPEDIENTE.equals(origenAltaTramite) && cfgExpedienteTramite != null){
			selectedNuevoTramiteId = cfgExpedienteTramite.getId();
		}
	}
	
	private void siTieneResponsablesTramitacionPorCfg (TramiteExpediente tramExp,CfgExpedienteTramite cfgExpedienteTramite) {
		if(cfgExpedienteTramite != null && cfgExpedienteTramite.getResponsablesTramitacion() != null){
			tramExp.setResponsable(cfgExpedienteTramite.getResponsablesTramitacion());
		}else {
			tramExp.setResponsable(expedientes.getResponsable());
		}
	}
	
	private void eliminarTramiteForAux (TramiteExpediente te, TramiteExpediente tramExp) {
		if (Objects.equals(te.getId(), tramExp.getId())) {
			tramExp.setActivo(false);
			try {
				Usuario usuario = (Usuario)JsfUtils.getSessionAttribute(Constantes.USUARIO);
				tramExp = tramiteExpedienteService.guardar(tramExp);
				tramiteExpedienteService.finalizarTareasTramite(usuario, tramExp, "eliminarTramite");
				expedientes = utilsComun.expedienteUltimaModificacion(expedientes,tramExp.getFechaModificacion(), tramExp.getFechaCreacion(),tramExp.getUsuModificacion(), tramExp.getUsuCreacion());
				
				DetalleExpdteTram detExpTr = detalleExpdteTramService.findDetalleTramiteExp(expedientes.getId(), tramExp.getId());
				if (detExpTr != null) {
					detExpTr.setActivo(false);
					detalleExpdteTramService.guardar(detExpTr);
				}
			} catch (BaseException e) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",mensajesProperties.getString(MENSAJEERROR));
				PrimeFaces.current().dialog().showMessageDynamic(message);
				log.error(e.getMessage());
			}
		}
	}
	
	private boolean puedeCambiarSituacionFinalizarTramite (TramiteExpediente tramExp) throws ValidacionException {
		boolean puedeCambiarSituacion = true;
		CfgAutoSituacion cfgAutoSituacion = cfgAutoSituacionService.obtenerCfgFinalDeTramite(tramExp.getId(), tramExp.getCondicionCambioSituacion());
		if(cfgAutoSituacion != null) {			
			ValoresDominio situFinal = cfgAutoSituacion.getValorSituacionDestino();			
			puedeCambiarSituacion = comunExpedientesBean.puedeCambiarASituacion(expedientes, tramExp, situFinal);
		}
		return puedeCambiarSituacion;
	}
	
	private void finalizarTramiteGuardarDetalleComportamientoC008(TramiteExpediente tramExp) throws BaseException {
		if(Constantes.C008.equals(tramExp.getTipoTramite().getComportamiento()) && tramExp.getDetalleExpdteTram() != null) {
			DetalleExpdteTram detallesTramite= tramExp.getDetalleExpdteTram();
			String nuevaCabecera="";

			if (!tramExp.getTipoTramite().getCodigo().equals(Constantes.TIP_TRAM_ACADMHE)) {
				//formateamos la fecha
			Date fechaInforme= detallesTramite.getFechaInforme();
			String pattern = Constantes.FECHA_DDMMYYYY;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			String fechaInformeFormateada = simpleDateFormat.format(fechaInforme);
			nuevaCabecera=fechaInformeFormateada+" - "+ detallesTramite.getValorTipoAdmision().getDescripcion();
			}
			else
			{
				nuevaCabecera=detallesTramite.getValorTipoAdmision().getDescripcion();
				
			}
			
			detallesTramite.setSeguimientoCabecera(nuevaCabecera);
		detalleExpdteTramService.guardar(detallesTramite);			
		}		
	}
	
	private void finalizarTramiteGuardarDetalleComportamientoC012C024(TramiteExpediente tramExp) throws BaseException {
		if((Constantes.C012.equals(tramExp.getTipoTramite().getComportamiento())
				|| Constantes.C024.equals(tramExp.getTipoTramite().getComportamiento()))  && tramExp.getDetalleExpdteTram() != null) {
			DetalleExpdteTram detallesTramite= tramExp.getDetalleExpdteTram();
			String nuevaCabecera="";
			//formateamos la fecha
			Date fechaInforme= detallesTramite.getFechaResolucion();
			String pattern = Constantes.FECHA_DDMMYYYY;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			String fechaInformeFormateada = simpleDateFormat.format(fechaInforme);
			nuevaCabecera=fechaInformeFormateada+" - "+ detallesTramite.getValorSentidoResolucion().getDescripcion();
			detallesTramite.setSeguimientoCabecera(nuevaCabecera);	
			detalleExpdteTramService.guardar(detallesTramite);
		}
	}
	
	private void finalizarTramiteGuardarDetalle(TramiteExpediente tramExp) throws BaseException {
		if (tramExp.getDetalleExpdteTram() != null) {
			detalleExpdteTramService.guardar(tramExp.getDetalleExpdteTram());
		}
	}
	
	private void finalizarTramiteComportamientoC008(TramiteExpediente tramExp) {
		if(Constantes.C008.equals(tramExp.getTipoTramite().getComportamiento())){
			cargarCondicionCambioSituacion008(tramExp);			
			actualizarTipoAdmisMotivoInadmEnExpdte(tramExp);
			
		
	
			
		}
	}
	
	private void finalizarTramiteComportamientoC010(TramiteExpediente tramExp) {
		if(Constantes.C010.equals(tramExp.getTipoTramite().getComportamiento())){
			cargarCondicionCambioSituacion010(tramExp);			
		}
	}
	
	private void finalizarTramiteComportamientoC012C024(TramiteExpediente tramExp) {
		if(Constantes.C012.equals(tramExp.getTipoTramite().getComportamiento())
				|| Constantes.C024.equals(tramExp.getTipoTramite().getComportamiento())){
			cargarCondicionCambioSituacion012C024(tramExp);			
		}
	}
	
	private void finalizarTramiteComportamientoC013(TramiteExpediente tramExp) {
		if(Constantes.C013.equals(tramExp.getTipoTramite().getComportamiento())){
			cargarCondicionCambioSituacion013(tramExp);			
		}
	}
	
	private void finalizarTramiteComportamientoC006(TramiteExpediente tramExp) {
		if(Constantes.C006.equals(tramExp.getTipoTramite().getComportamiento())){
			actualizarAutoridadCompetenteEnExpdte(tramExp);
		}
	}
	
	private void finalizarTramiteComportamientoC018(TramiteExpediente tramExp) {
		if(Constantes.C018.equals(tramExp.getTipoTramite().getComportamiento())){
			cargarCondicionCambioSituacion013(tramExp);
		}
	}
	

	private void finalizarTramiteActualizarPlazosYCabecera(TramiteExpediente tramExp) {
		if(Constantes.TIP_TRAM_SUB.equals(tramExp.getTipoTramite().getCodigo()) 
			||Constantes.C008.equals(tramExp.getTipoTramite().getComportamiento())
			||Constantes.C023.equals(tramExp.getTipoTramite().getComportamiento())
			||Constantes.C014.equals(tramExp.getTipoTramite().getComportamiento())
			||Constantes.C024.equals(tramExp.getTipoTramite().getComportamiento())
			||Constantes.C012.equals(tramExp.getTipoTramite().getComportamiento()))	{
			actualizarPlazosExpdte(tramExp);
			
		} 
		datosExpedientesBean.actualizarCabecera(expedientes, null, null, null);
		
	}
				
	private String finalizarTramiteActualizarSituacion(TramiteExpediente tramExp){
		String retorno = StringUtils.EMPTY;		
		ValoresDominio nuevoValorSituacion = cambiarSituacionSegunFinTramite(tramExp);		
		if(null != nuevoValorSituacion) { //!= null si la llamada ha cambiado la situación			
			if(this.expedienteEnSituacionFinal(tramExp.getExpediente())){
				retorno =  datosExpedientesDatosGeneralesBean.accionesSituacionFinal();
			} else {
				datosExpedientesBean.actualizarCabecera(expedientes, null, null);				
				PrimeFaces.current().ajax().update(FORMFORMULARIOEXPEDIENTES + ":tabViewPestanasExpediente:comboTipoSit");
			}
		}
		return retorno;
	}
	
	private boolean finalizarTramiteSubtramitesActivos (List<TramiteExpediente> listaSubTramitesAsociados,Boolean existeSubTramitesAsociadosTemporales) {
		return !listaSubTramitesAsociados.isEmpty() || Boolean.TRUE.equals(existeSubTramitesAsociadosTemporales);
	}
	
	private boolean finalizarTramiteValidacionesComportamientos67910122013 (TramiteExpediente tramExp,List<DocumentosExpedientesTramites> listaDocExpTram) {
		return ((Constantes.C006.equals(tramExp.getTipoTramite().getComportamiento()) 
					|| Constantes.C007.equals(tramExp.getTipoTramite().getComportamiento()) 
					|| Constantes.C009.equals(tramExp.getTipoTramite().getComportamiento())
					|| Constantes.C010.equals(tramExp.getTipoTramite().getComportamiento())
					|| Constantes.C012.equals(tramExp.getTipoTramite().getComportamiento())
					|| Constantes.C020.equals(tramExp.getTipoTramite().getComportamiento())
					|| Constantes.C013.equals(tramExp.getTipoTramite().getComportamiento()))
				&& (listaDocExpTram == null || listaDocExpTram.isEmpty()));
	}
	
	private boolean finalizarTramiteValidacionesComportamientos511823 (TramiteExpediente tramExp) {
		return ((Constantes.C005.equals(tramExp.getTipoTramite().getComportamiento())
				&& (tramExp.getDetalleExpdteTram().getFechaEntrada() != null && !tramExp.getDetalleExpdteTram().getFechaEntrada().before(FechaUtils.hoy()))
				&& (tramExp.getDetalleExpdteTram().getFechaEntrada()!= null && !tramExp.getDetalleExpdteTram().getFechaEntrada().equals(FechaUtils.hoy())))
				|| ((Constantes.C001.equals(tramExp.getTipoTramite().getComportamiento()) || Constantes.C023.equals(tramExp.getTipoTramite().getComportamiento()))
						&& (tramExp.getDetalleExpdteTram().getFechaEntrada() != null && !tramExp.getDetalleExpdteTram().getFechaEntrada().before(FechaUtils.hoy()))
						&& (tramExp.getDetalleExpdteTram().getFechaEntrada() != null && !tramExp.getDetalleExpdteTram().getFechaEntrada().equals(FechaUtils.hoy())) ) 
				|| (Constantes.C018.equals(tramExp.getTipoTramite().getComportamiento())
						&& (tramExp.getDetalleExpdteTram().getFechaEntrada() != null && !tramExp.getDetalleExpdteTram().getFechaEntrada().before(FechaUtils.hoy()))
						&& (tramExp.getDetalleExpdteTram().getFechaEntrada() != null && !tramExp.getDetalleExpdteTram().getFechaEntrada().equals(FechaUtils.hoy())) ));
	}
	
	private boolean finalizarTramiteValidacionesComportamientos7913(TramiteExpediente tramExp) {
		return ((Constantes.C007.equals(tramExp.getTipoTramite().getComportamiento()) && tramExp.getDetalleExpdteTram().getFechaEnvio() == null) 
				|| (Constantes.C009.equals(tramExp.getTipoTramite().getComportamiento()) && tramExp.getDetalleExpdteTram().getFechaEnvio() == null) 
				|| (Constantes.C013.equals(tramExp.getTipoTramite().getComportamiento()) && tramExp.getDetalleExpdteTram().getFechaEnvio() == null));
	}
	
	private boolean finalizarTramiteValidacionesComportamientos8TipoAdm(TramiteExpediente tramExp) {
		boolean valido = false;
		if (Constantes.C008.equals(tramExp.getTipoTramite().getComportamiento()) && !Constantes.TIP_TRAM_ACADMHE.equals(tramExp.getTipoTramite().getCodigo()) && tramExp.getDetalleExpdteTram().getFechaInforme() == null) {	
		
				valido = true;
		}
		
		return  valido;
	}
	
	private boolean finalizarTramiteValidacionesComportamientos23TipoAdm(TramiteExpediente tramExp) {
		return Constantes.C023.equals(tramExp.getTipoTramite().getComportamiento()) && (tramExp.getSelectedNuevoTipoAdmisionId() == null || tramExp.getDetalleExpdteTram().getFechaInforme() == null)
				&& (tramExp.getSelectedNuevoActoRecId() == null);
	}
	
	private boolean finalizarTramiteValidacionesComportamientos8Validaciones(TramiteExpediente tramExp) {
		return Constantes.C008.equals(tramExp.getTipoTramite().getComportamiento()) && !validacionesGuardarDetalleTramExpC019C008(tramExp) ;
	}
	
	private boolean finalizarTramiteValidacionesComportamientos10PropApi(TramiteExpediente tramExp) {
		return Constantes.C010.equals(tramExp.getTipoTramite().getComportamiento()) && tramExp.getSelectedNuevoPropuestaApiId() == null ;
	}
	
	private boolean finalizarTramiteValidacionesComportamientos10FechaInfor(TramiteExpediente tramExp) {
		return Constantes.C010.equals(tramExp.getTipoTramite().getComportamiento()) && tramExp.getDetalleExpdteTram().getFechaInforme() == null ;
	}
	
	private boolean finalizarTramiteValidacionesComportamientos20(TramiteExpediente tramExp) {
		return Constantes.C020.equals(tramExp.getTipoTramite().getComportamiento()) && tramExp.getDetalleExpdteTram().getFechaInforme() == null ;
	}
	
	private boolean finalizarTramiteValidacionesComportamientos12(TramiteExpediente tramExp) {
		return Constantes.C012.equals(tramExp.getTipoTramite().getComportamiento()) && !validacionesGuardarDetalleTramExpC012C024(tramExp, FINALIZARRESOLUCION) ;
	}
	
	private boolean finalizarTramiteValidacionesComportamientos24(TramiteExpediente tramExp) {
		return Constantes.C024.equals(tramExp.getTipoTramite().getComportamiento()) && !validacionesGuardarDetalleTramExpC012C024(tramExp, FINALIZARRESOLUCION) ;
	}
	
	private boolean finalizarTramiteValidacionesComportamientos13(TramiteExpediente tramExp) {
		return Constantes.C013.equals(tramExp.getTipoTramite().getComportamiento()) && !validacionesFinalizarDetalleTramExpC013(tramExp) ;
	}
	
	private boolean finalizarTramiteValidacionesComportamientos14(TramiteExpediente tramExp) {
		return Constantes.C014.equals(tramExp.getTipoTramite().getComportamiento()) && !validacionesFinalizarDetalleTramExpC014(tramExp) ;
	}
	
	private boolean finalizarTramiteValidacionesComportamientos17(TramiteExpediente tramExp) {
		return Constantes.C017.equals(tramExp.getTipoTramite().getComportamiento()) && !validacionesGuardarDetalleTramExpC017(tramExp) ;
	}
	
	private boolean finalizarSubTramiteComportamiento15(TramiteExpediente subTramExp) {
		return Constantes.C015.equals(subTramExp.getTipoTramite().getComportamiento()) && 
			(subTramExp.getDetalleExpdteTram().getFechaEnvio() != null 
			&& subTramExp.getDetalleExpdteTram().getFechaRespuesta() != null 
			&& subTramExp.getDetalleExpdteTram().getFechaEnvio().after(subTramExp.getDetalleExpdteTram().getFechaRespuesta()));
	}
	
	private boolean finalizarSubTramiteComportamiento16(TramiteExpediente subTramExp) {
		return Constantes.C016.equals(subTramExp.getTipoTramite().getComportamiento()) && !validacionesFinalizarDetalleTramExpC016(subTramExp);
	}
	
	private boolean validacionesObligatoriedadFinalizarTramiteComportamiento6Obligatorios (DetalleExpdteTram detExpTram,TramiteExpediente tramExp) {
		return Constantes.C006.equals(tramExp.getTipoTramite().getComportamiento()) && 
				(detExpTram.getValorTipoInteresado() == null 
					|| detExpTram.getValorDominioInteresado() == null 
					|| detExpTram.getValorCanalSalida() == null 
					|| detExpTram.getDatosCanalSalida().isEmpty());
	}
	
	private boolean validacionesObligatoriedadFinalizarTramiteComportamiento6FechaEnvio (DetalleExpdteTram detExpTram,TramiteExpediente tramExp) {
		return Constantes.C006.equals(tramExp.getTipoTramite().getComportamiento()) && detExpTram.getFechaEnvio() == null;
	}
	
	private boolean validacionesObligatoriedadFinalizarTramiteComportamiento4Obligatorios (TramiteExpediente tramExp) {
		return Constantes.C004.equals(tramExp.getTipoTramite().getComportamiento()) && !validarCanalSalidaObligatorio(tramExp);
	}
	
	private boolean validacionesObligatoriedadFinalizarTramiteComportamiento4FechaEnvio (TramiteExpediente tramExp) {
		return Constantes.C004.equals(tramExp.getTipoTramite().getComportamiento()) && tramExp.getDetalleExpdteTram().getFechaEnvio() == null;
	}
	
	private boolean validacionesObligatoriedadFinalizarTramiteComportamiento4Documentos (TramiteExpediente tramExp) {
		return Constantes.C004.equals(tramExp.getTipoTramite().getComportamiento()) && 0 == this.documentosExpedientesTramitesService.countDocExpTramByIdTramExp(tramExp.getId());
	}
	
	private boolean validacionesObligatoriedadFinalizarTramiteComportamiento3 (TramiteExpediente tramExp) {
		return (Constantes.C003.equals(tramExp.getTipoTramite().getComportamiento())) && (!validarFinalizarTramiteC003(tramExp));
	}
	
	private boolean validacionesObligatoriedadFinalizarTramiteComportamiento5123 (TramiteExpediente tramExp) {
		return (Constantes.C005.equals(tramExp.getTipoTramite().getComportamiento()) || Constantes.C001.equals(tramExp.getTipoTramite().getComportamiento())
				|| Constantes.C023.equals(tramExp.getTipoTramite().getComportamiento())) 
				&& (tramExp.getDetalleExpdteTram().getFechaEntrada() == null 
					|| tramExp.getDetalleExpdteTram().getIdentifEntrada() == null
					|| tramExp.getDetalleExpdteTram().getValorCanalEntrada() == null
					||tramExp.getDetalleExpdteTram().getValorTipoInteresado() == null
					|| tramExp.getDetalleExpdteTram().getFechaRegistro() == null);
	}
	
	private boolean validacionesObligatoriedadFinalizarTramiteComportamiento5181823 (TramiteExpediente tramExp) {
		return (Constantes.C005.equals(tramExp.getTipoTramite().getComportamiento())
				|| Constantes.C001.equals(tramExp.getTipoTramite().getComportamiento())
				|| Constantes.C008.equals(tramExp.getTipoTramite().getComportamiento())
				|| Constantes.C018.equals(tramExp.getTipoTramite().getComportamiento())
				|| Constantes.C023.equals(tramExp.getTipoTramite().getComportamiento())) && 
				(0 == this.documentosExpedientesTramitesService.countDocExpTramByIdTramExp(tramExp.getId()));
	}
	
	private boolean validacionesObligatoriedadFinalizarTramiteComportamiento14 (TramiteExpediente tramExp) {
		return (Constantes.C014.equals(tramExp.getTipoTramite().getComportamiento())) && (!validarFinalizarTramiteC014(tramExp));
	}
	
	private boolean validacionesObligatoriedadFinalizarTramiteComportamiento15FechaRespuesta (DetalleExpdteTram detExpTram,TramiteExpediente tramExp) {
		return Constantes.C015.equals(tramExp.getTipoTramite().getComportamiento()) && detExpTram.getFechaRespuesta() == null;
	}
	
	private boolean validacionesObligatoriedadFinalizarTramiteComportamiento15FechaEnvio (DetalleExpdteTram detExpTram,TramiteExpediente tramExp) {
		return Constantes.C015.equals(tramExp.getTipoTramite().getComportamiento()) && detExpTram.getFechaEnvio() == null;
	}
	
	private void actualizarVisibilidadBotonFinalizar(TramiteExpediente tramExp) {
		boolean esResponsableTramiteUsuarioConectado = usuariosResponsablesService.esResponsableDeUsuario(tramExp.getResponsable().getId(), sesionBean.getIdUsuarioSesion());
		tramExp.setMostrarBotonFinalizar(esResponsableTramiteUsuarioConectado);
	}
	
	public boolean tieneDocumentos(TramiteExpediente tramExp) {
		var bean = FacesUtils.getVar("datosExpedientesDocumentosBean", DatosExpedientesDocumentosBean.class);
		var lista = bean.getCategoriasTramite(tramExp.getId());
		
		return !(lista == null || lista.isEmpty());
	}
	
}
