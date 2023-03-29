package es.juntadeandalucia.ctpda.gestionpdt.web.expedientes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.primefaces.PrimeFaces;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.CfgMetadatosTram;
import es.juntadeandalucia.ctpda.gestionpdt.model.DetalleExpdteTram;
import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.NotificacionesPendientesMaestra;
import es.juntadeandalucia.ctpda.gestionpdt.model.ObservacionesExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.PersonaDTO;
import es.juntadeandalucia.ctpda.gestionpdt.model.Personas;
import es.juntadeandalucia.ctpda.gestionpdt.model.PersonasExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.ResponsablesTramitacion;
import es.juntadeandalucia.ctpda.gestionpdt.model.SujetosObligados;
import es.juntadeandalucia.ctpda.gestionpdt.model.SujetosObligadosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.TipoTramite;
import es.juntadeandalucia.ctpda.gestionpdt.model.TramiteExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.Usuario;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.service.CfgMetadatosTramService;
import es.juntadeandalucia.ctpda.gestionpdt.service.DetalleExpdteTramService;
import es.juntadeandalucia.ctpda.gestionpdt.service.NotificacionesPendientesMaestraService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ObservacionesExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.PersonasExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.PersonasService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ResponsablesTramitacionService;
import es.juntadeandalucia.ctpda.gestionpdt.service.SujetosObligadosExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.SujetosObligadosService;
import es.juntadeandalucia.ctpda.gestionpdt.service.TipoTramiteService;
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
import es.juntadeandalucia.ctpda.gestionpdt.web.util.BaseBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.ContextoVolver;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.FacesUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.VolverBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@ViewScoped
@Slf4j
public class NotificacionesPendientesBean extends BaseBean implements Serializable {
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	private static final String EDITABLE = "editable";
	private static final String FORMNOTIFICACIONESPENDIENTES = "formListadoNotificacionesPendientes";
	public static final String VOLVERLISTADONOTIFICACIONESPENDIENTES = "_volverNotificacionesPendientes_";
	private static final String MENSAJEERROR = "error";
	private static final String MENSAJESFORMULARIO = "messagesListadoNotificacionesPendientes";
	private static final String TRAMNOTIFMSJ = "Notificación ";
	private static final String ACTUALIZADACORRECTAMENTE = "actualizada.correctamente";
	private static final String FINALIZADACORRECTAMENTE = "finalizada.correctamente";
	private static final String FORMFORMULARIOLISTADONOTIFICACIONESPENDIENTES = "formListadoNotificacionesPendientes";
	private static final String FECHAENVIONOSUPFECHAACTUAL = "fecha.envio.no.superior.actual";
	private static final String FECHANOTIFNOSUPFECHAACTUAL = "fecha.notif.no.superior.actual";
	private static final String FECHANOTIFNOANTERIORFECHAENV = "fecha.notif.no.anterior.fecha.envio";
	private static final String RESULTNOTIFICACIONNOINFORMADO = "resultado.notificacion.noinformado";
	private static final String INTERESADOFINALIZARTRAMITE = "interesado.finalizar.tramite";
	private static final String FECHAENVIOFINALIZARTRAMITE = "fecha.envio.finalizar.tramite";
	private static final String FECHANOTIFPOSTERIORFECHAENV = "fecha.notif.post.fecha.envio.notif";
	
	@Autowired
	private TipoTramiteService tipoTramiteService;
	
	@Autowired
	private ValoresDominioService valoresDominioService;
	@Getter
	@Setter
	private Boolean habilitarIdentifInt;
	@Autowired
	private NotificacionesPendientesMaestraService notificacionesPendientesMaestraService;
	
	@Autowired
	private TramiteExpedienteService tramiteExpedienteService;

	
	@Autowired
	private ResponsablesTramitacionService responsablesTramitacionService;

	@Getter
	private List<ValoresDominio> listaValoresDominioTipoPlazo;
	
	
	
	@Autowired
	private PersonasService personasService;
	
	@Getter
	@Setter
	private String codigoTipoInteresado;
	
	@Getter
	@Setter
	private DetalleExpdteTram detalleExpdteTram;

	
	@Autowired
	private SujetosObligadosService sujetosObligadosService;
	
	@Autowired
	private SujetosObligadosExpedientesService sujetosObligadosExpedientesService;
	
	@Autowired
	private ObservacionesExpedientesService observacionesExpedientesService;
	
	@Autowired
	private DetalleExpdteTramService detalleExpdteTramService;
	
	@Autowired
	private CfgMetadatosTramService cfgMetadatosTramService;
	
	@Autowired
	private PersonasExpedientesService personasExpedientesService;

	@Getter
	private LazyDataModelByQueryService<NotificacionesPendientesMaestra> lazyModel;
	
	@Getter
	@Setter
	private List<ValoresDominio> listaValoresDominioTipoInteresado;
	
	@Getter
	@Setter
	private Long selectedTipoExpedienteIdFiltro;
	
	@Getter
	@Setter
	private Long selectedTipoTramiteIdFiltro;
	
	@Getter
	@Setter
	private Long selectedResponsableTramIdFiltro;
	
	@Getter
	@Setter
	private Boolean esIdentIntDPD;
	
	
	@Getter
	@Setter
	private Long selectedNuevoTipoExpedienteId;

	@Getter
	@Setter
	private Long selectedSituacionIdFiltro;
	
	@Getter
	@Setter
	private Long selectedCanalSalidaIdFiltro;
	
	@Getter
	@Setter
	private  Long selectedResponsableExpIdFiltro;

	@Getter
	@Setter
	private String numeroExpedienteFiltro;
	
	@Getter
	@Setter
	private String responsableFiltro;

	@Getter
	@Setter
	private Date fechaEntradaInicialFiltro;
	
	@Getter
	@Setter
	private Date fechaEntradaFinalFiltro;

	@Getter
	@Setter
	private NotificacionesPendientesMaestra selectedNotificacionesPendientes;

	@Getter
	@Setter
	private List<ValoresDominio> listaValoresDominioTipoExp;
	
	@Getter
	@Setter
	private Boolean esIdentIntPersona;


	@Getter
	@Setter
	private Boolean esIdentIntAutControl;
	

	@Getter
	@Setter
	private List<TipoTramite> listaTipoTramites;
	
	
	@Getter
	@Setter
	private List<ValoresDominio> listaValoresDominioSituacion;
	
	@Getter
	@Setter
	private List<ValoresDominio> listaValoresDominioCanalSalida;
	
	@Getter
	@Setter
	private List<ResponsablesTramitacion> listaResponsablesTramitacion;
	
	@Getter
	@Setter
	private List<ResponsablesTramitacion> listaResponsablesExpediente;
	
	@Getter
	@Setter
	private List<String> listaCodigoPermisos;
	
	@Autowired
	private VolverBean volverBean;

	@Getter
	@Setter
	private Expedientes expedientes;
	
	@Getter
	@Setter
	private TramiteExpediente tramiteExpediente;
	
	@Getter
	private SortMeta defaultOrden;
	
	@Getter	@Setter
	private Boolean permisoModDatNot;
	
	@Autowired
	private NavegacionBean navegacionBean;
	
	@Getter
	private List<ValoresDominio> listaValoresDominioResultadosNotificacion;

	@PostConstruct
	@Override
	public void init() {
		super.init();
		
		FacesUtils.setAttribute(EDITABLE, JsfUtils.getFlashAttribute(EDITABLE));
		
		/**
		 * GESTION DE PERMISOS
		 * */
		listaCodigoPermisos = (List<String>) JsfUtils.getSessionAttribute(Constantes.LISTACODIGOPERMISOS);
		
		this.tramiteExpediente = new TramiteExpediente();
		this.tramiteExpediente.setDetalleExpdteTram(new DetalleExpdteTram());
		this.tramiteExpediente.setObservaciones(new ObservacionesExpedientes());

		
		permisoModDatNot = listaCodigoPermisos.contains(Constantes.PERMISO_MOD_DAT_NOT);
		

		listaValoresDominioTipoExp = valoresDominioService.findValoresTipoExpediente();
		
		listaTipoTramites = tipoTramiteService.findTipoTramitesActivosOrdenAlfab();
		
		selectedTipoTramiteIdFiltro = tipoTramiteService.findTipoTramiteActivoNotif().getId();

		listaValoresDominioSituacion = notificacionesPendientesMaestraService.findSituacionesExpedienteByTramiteAbierto();
		
		listaValoresDominioCanalSalida = valoresDominioService.findValoresDominioActivosByCodigoDominioOrden(ValoresDominioService.COD_COM);
		
		listaValoresDominioTipoInteresado = valoresDominioService.findValoresDominioActivosByCodigoDominio(ValoresDominioService.COD_TIP_INT);
		
		this.listaValoresDominioTipoPlazo = valoresDominioService.findValoresDominioActivosByCodigoDominio(Constantes.COD_TIP_PLA_TEMP);
		
		
		listaResponsablesTramitacion = responsablesTramitacionService.findResponsablesActivos();
		
		listaResponsablesExpediente = responsablesTramitacionService.findResponsablesActivos();
		
		this.listaValoresDominioResultadosNotificacion = valoresDominioService.findValoresDominioActivosByCodigoDominio(Constantes.COD_DOM_RES_NOTIF);
		
		
		defaultOrden = SortMeta.builder().field("fechaIni").order(SortOrder.DESCENDING).priority(1).build();
		defaultOrden = SortMeta.builder().field("id").order(SortOrder.DESCENDING).priority(2).build();
		
		cargarFiltros();
		
		buscar();
		
		navegacionBean.setTxtMigaPan(Constantes.LISTADO_NOTIFICACIONES_PENDIENTES);
	}
	
	public void buscar()
	{
		lazyModel = new LazyDataModelByQueryService<>(NotificacionesPendientesMaestra.class, notificacionesPendientesMaestraService);

		lazyModel.setPreproceso((a, b, c, filters) -> 

			filtrosLazyModel(filters)
		);
		
		PrimeFaces.current().ajax().update(FORMNOTIFICACIONESPENDIENTES);
	}
	
	private void filtrosLazyModel (Map<String, FilterMeta> filters) {

		
		if (selectedTipoTramiteIdFiltro != null) {
			filters.put("tipoTramite.id", new MyFilterMeta(selectedTipoTramiteIdFiltro));
		}
		
		if (selectedResponsableTramIdFiltro != null) {
			String descripcionRespTram = responsablesTramitacionService.obtener(selectedResponsableTramIdFiltro).getDescripcion();
			filters.put("descResponsableTram", new MyFilterMeta(descripcionRespTram));
		}


		if (selectedTipoExpedienteIdFiltro != null) {
			String abreviatura = valoresDominioService.obtener(selectedTipoExpedienteIdFiltro).getAbreviatura();
			filters.put("tipoExpediente", new MyFilterMeta(abreviatura));
		}
		
		
		if (numeroExpedienteFiltro != null && !numeroExpedienteFiltro.isEmpty()) {
			filters.put("numExpediente", new MyFilterMeta(numeroExpedienteFiltro));
		}
		if (selectedSituacionIdFiltro != null) {
			String descripcion = valoresDominioService.obtener(selectedSituacionIdFiltro).getDescripcion();
			filters.put("situacion", new MyFilterMeta(descripcion));
		}
		
		if (selectedCanalSalidaIdFiltro != null) {
			String descripcion = valoresDominioService.obtener(selectedCanalSalidaIdFiltro).getDescripcion();
			filters.put("descCanalSalida", new MyFilterMeta(descripcion));
		}
		
		
		
		if (fechaEntradaInicialFiltro != null) {
		filters.put("#fechaInicialInicio", new MyFilterMeta(fechaEntradaInicialFiltro));	
		} 
		if (fechaEntradaFinalFiltro != null) {
			filters.put("#fechaFinalInicio", new MyFilterMeta(fechaEntradaFinalFiltro));	
		} 
		
		validaRangoFechas(fechaEntradaInicialFiltro, fechaEntradaFinalFiltro);
		
		
		if (selectedResponsableExpIdFiltro != null) {
			String descripcionRespExp = responsablesTramitacionService.obtener(selectedResponsableExpIdFiltro).getDescripcion();
			filters.put("responsable", new MyFilterMeta(descripcionRespExp));
		}

		
	}
	
	private void cargarDatosDetalleTramExp003 (DetalleExpdteTram detalleExpdteTram, TramiteExpediente trExp) { 
		if(detalleExpdteTram.getValorTipoPlazo() != null){
			trExp.setSelectedNuevoTipoPlazoId(detalleExpdteTram.getValorTipoPlazo().getId());	
		}
		
		aplicarValorCanalSalida(detalleExpdteTram, trExp);
		
		if(detalleExpdteTram.getAcuseRecibo() == null)
		{
			detalleExpdteTram.setAcuseRecibo(true);	
		}
		
		
	}
	
	public void aplicarValorCanalSalida(DetalleExpdteTram detalleExpdteTram, TramiteExpediente trExp) {
		if (detalleExpdteTram.getValorCanalSalida() != null) {
			trExp.setSelectedNuevoCanalSalidaId(detalleExpdteTram.getValorCanalSalida().getId());
		}
	}
	
	public void inicializarCampos( DetalleExpdteTram detalleExpdteTram) {

		if(detalleExpdteTram.getValorResultadoNotificacion() != null) {
			this.tramiteExpediente.setSelectedNuevoResulNotificacionId(detalleExpdteTram.getValorResultadoNotificacion().getId());
		}
				
		this.listaValoresDominioTipoInteresado = valoresDominioService.findValoresDominioActivosByCodigoDominio(ValoresDominioService.COD_TIP_INT);
		
		if(detalleExpdteTram.getValorTipoInteresado() != null){
			this.tramiteExpediente.setSelectedNuevoTipoInteresadoId(detalleExpdteTram.getValorTipoInteresado().getId());	
		}
		if (detalleExpdteTram.getValorDominioInteresado() != null) {
			this.tramiteExpediente.setSelectedNuevaIdentifInteresadoId(detalleExpdteTram.getValorDominioInteresado().getId());
		}							
		if(detalleExpdteTram.getValorResultadoNotificacion() != null) {
			this.tramiteExpediente.setSelectedNuevoResulNotificacionId(detalleExpdteTram.getValorResultadoNotificacion().getId());
		}
		
		this.tramiteExpediente.setListaIdentifIntSujOblig(sujetosObligadosExpedientesService.obtenerSujetosObligadosExpediente(detalleExpdteTram.getExpediente().getId()));
		List<PersonasExpedientes> listaPersonasPorExpediente = personasExpedientesService.obtenerPersPorExpediente(detalleExpdteTram.getExpediente().getId());
		List<PersonaDTO> listaIdentifIntPersonas = cargarPersonasYRepresentantes(listaPersonasPorExpediente);
		this.tramiteExpediente.setListaIdentifIntPersDTO(listaIdentifIntPersonas);
		this.tramiteExpediente.setListaIdentifIntDpd(sujetosObligadosExpedientesService.obtenerDpdExpediente(detalleExpdteTram.getExpediente().getId()));
		this.tramiteExpediente.setListaValoresDominioIdentifInteresado(valoresDominioService.findValoresDominioActivosByCodigoDominio(ValoresDominioService.COD_AUTCOMP));
		
		aplicarDatosInteresado(this.tramiteExpediente, detalleExpdteTram);
		
		cargarDatosDetalleTramExp003(detalleExpdteTram, this.tramiteExpediente);

		
	

	PrimeFaces.current().ajax().update(FORMFORMULARIOLISTADONOTIFICACIONESPENDIENTES);
	}
	
	private List<PersonaDTO> cargarPersonasYRepresentantes(List<PersonasExpedientes> listaPersExp)
	{
		List<PersonaDTO> listadoIdentifPersonasRepre = new ArrayList<>();
		for(PersonasExpedientes perExp: listaPersExp) {
			Personas perRepre = perExp.getPersonasRepre();
			Personas per = perExp.getPersonas();
			PersonaDTO personaDTO = new PersonaDTO(per.getId(), per.getNombreAp());
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


	private void validaRangoFechas(Date fechaInicio, Date fechaFin)
	{
		if((fechaInicio != null && fechaFin == null) || (fechaInicio == null && fechaFin != null))
		{
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					mensajesProperties.getString("rango.fechas.incompleto"));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			PrimeFaces.current().ajax().update(FORMNOTIFICACIONESPENDIENTES);
		}
		if(fechaInicio != null &&  fechaFin != null &&  fechaInicio.after(fechaFin))
		{
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					mensajesProperties.getString("fecha.rango.ini.posterior"));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			PrimeFaces.current().ajax().update(FORMNOTIFICACIONESPENDIENTES);
		}
	}
	

	public String redireccionMenu() {
		return ListadoNavegaciones.LISTADO_NOTIFICACIONES_PENDIENTES.getRegla();
	}

	public void limpiarFiltro() {
		selectedTipoExpedienteIdFiltro = null;
		selectedTipoTramiteIdFiltro = tipoTramiteService.findTipoTramiteActivoNotif().getId();
		selectedResponsableTramIdFiltro = null;
		selectedResponsableExpIdFiltro = null;
		numeroExpedienteFiltro = "";
		selectedSituacionIdFiltro = null;
		selectedCanalSalidaIdFiltro = null;
		fechaEntradaInicialFiltro = null;
		fechaEntradaFinalFiltro = null;
		lazyModel = null;
		
		buscar();
		
		PrimeFaces.current().ajax().update(FORMNOTIFICACIONESPENDIENTES);		
	}

	public String onEditar(Long idTram) {
		TramiteExpediente tramExp = tramiteExpedienteService.obtener(idTram);
		Expedientes expediente = tramExp.getExpediente();
		final ContextoVolver contexto = volverBean.crearContexto(ListadoNavegaciones.LISTADO_NOTIFICACIONES_PENDIENTES.getRegla(), Constantes.VOLVERNOTIFICACIONESPENDIENTES);
		cargarFiltrosEnContexto(contexto);
		
		JsfUtils.setSessionAttribute(Constantes.VUELTA_NOTIFICACIONES_PENDIENTES, true);
		JsfUtils.setFlashAttribute(EDITABLE, true);
		JsfUtils.setFlashAttribute("idExp", expediente.getId());
		JsfUtils.setSessionAttribute(EDITABLE, true);
		if (expediente.getSituacionAdicional() != null) {
		navegacionBean.setTxtMigaPan(Constantes.EDICION_EXPEDIENTE +" - " + expediente.getNumExpediente() + navegacionBean.expedientesRelacionadosMotivoPsan(expediente.getId()) +  " - " + expediente.getValorSituacionExpediente().getDescripcion()+ " - " + expediente.getSituacionAdicional());
		} else {
		navegacionBean.setTxtMigaPan(Constantes.EDICION_EXPEDIENTE + " - " + expediente.getNumExpediente() + navegacionBean.expedientesRelacionadosMotivoPsan(expediente.getId()) + " - " + expediente.getValorSituacionExpediente().getDescripcion());	
		}
	return ListadoNavegaciones.FORM_EXPEDIENTES.getRegla();
	}

	public String onConsultar(Long idTram) {
		
		TramiteExpediente tramExp = tramiteExpedienteService.obtener(idTram);
		Expedientes expediente = tramExp.getExpediente();
		
		final ContextoVolver contexto = volverBean.crearContexto(ListadoNavegaciones.LISTADO_NOTIFICACIONES_PENDIENTES.getRegla(), Constantes.VOLVERNOTIFICACIONESPENDIENTES);
		cargarFiltrosEnContexto(contexto);
		
		JsfUtils.setSessionAttribute(Constantes.VUELTA_NOTIFICACIONES_PENDIENTES, true);
		
		JsfUtils.setFlashAttribute(EDITABLE, false);
		JsfUtils.setSessionAttribute(EDITABLE, false);
		JsfUtils.setFlashAttribute("idExp", expediente.getId());
		if (expediente.getSituacionAdicional() != null) {
		navegacionBean.setTxtMigaPan(Constantes.CONSULTA_EXPEDIENTE+ " - " + expediente.getNumExpediente() + navegacionBean.expedientesRelacionadosMotivoPsan(expediente.getId()) + " - " + expediente.getValorSituacionExpediente().getDescripcion()+ " - " + expediente.getSituacionAdicional());
		} else {
		navegacionBean.setTxtMigaPan(Constantes.CONSULTA_EXPEDIENTE +" - " + expediente.getNumExpediente() + navegacionBean.expedientesRelacionadosMotivoPsan(expediente.getId()) + " - " + expediente.getValorSituacionExpediente().getDescripcion());	
		}
		
		return ListadoNavegaciones.FORM_EXPEDIENTES.getRegla();
	}
	
	
	private void cargarFiltrosEnContexto(ContextoVolver contexto)
	{
		contexto.put("selectedTipoTramiteIdFiltro", tipoTramiteService.findTipoTramiteActivoNotif().getId());
		contexto.put("selectedResponsableTramIdFiltro", this.selectedResponsableTramIdFiltro);
		contexto.put("selectedTipoExpedienteIdFiltro", this.selectedTipoExpedienteIdFiltro);
		
		contexto.put("numeroExpedienteFiltro", this.numeroExpedienteFiltro);
		contexto.put("selectedSituacionIdFiltro", this.selectedSituacionIdFiltro);
		contexto.put("selectedCanalSalidaIdFiltro", this.selectedCanalSalidaIdFiltro);
		
		contexto.put("fechaEntradaInicialFiltro", this.fechaEntradaInicialFiltro);
		contexto.put("fechaEntradaFinalFiltro", this.fechaEntradaFinalFiltro);
		contexto.put("selectedResponsableExpIdFiltro", this.selectedResponsableExpIdFiltro);
	}
	
	private void cargarFiltros() {
		final ContextoVolver cv = volverBean.recogerContexto(Constantes.VOLVERNOTIFICACIONESPENDIENTES);
		
		if(cv != null) {
			this.selectedTipoTramiteIdFiltro = (Long) cv.get("selectedTipoTramiteIdFiltro");
			this.selectedResponsableTramIdFiltro = (Long) cv.get("selectedResponsableTramIdFiltro");
			this.selectedTipoExpedienteIdFiltro = (Long) cv.get("selectedTipoExpedienteIdFiltro");			
			
			this.numeroExpedienteFiltro = (String) cv.get("numeroExpedienteFiltro");
			this.selectedSituacionIdFiltro = (Long) cv.get("selectedSituacionIdFiltro");
			this.selectedCanalSalidaIdFiltro = (Long) cv.get("selectedCanalSalidaIdFiltro");
			
			this.fechaEntradaInicialFiltro = (Date) cv.get("fechaEntradaInicialFiltro");
			this.fechaEntradaFinalFiltro = (Date) cv.get("fechaEntradaFinalFiltro");
			this.selectedResponsableExpIdFiltro = (Long) cv.get("selectedResponsableExpIdFiltro");
			buscar();
		} else {
			limpiarFiltro();
		}
	}
	
	public void editarNotificacion(Long idTramExp) {
		Expedientes exp;
		this.tramiteExpediente = tramiteExpedienteService.obtener(idTramExp);
		exp = this.tramiteExpediente.getExpediente();
		detalleExpdteTram =detalleExpdteTramService.findDetalleTramiteExp(exp.getId(), idTramExp);	
		
		if(detalleExpdteTram == null)
		{
			detalleExpdteTram = new DetalleExpdteTram();
		}
		
		inicializarCampos(detalleExpdteTram);
		
		setCfgMetadatosTramite();

		this.tramiteExpediente.setDetalleExpdteTram(detalleExpdteTram);
		
		expedientes = this.tramiteExpediente.getExpediente();
		JsfUtils.setSessionAttribute("expedienteFormulario", expedientes);
		PrimeFaces.current().ajax().update("dialogNotificacion");
		PrimeFaces.current().executeScript("PF('dialogNotificacion').show();");
	}
	
	private void setCfgMetadatosTramite(){
		try {
			CfgMetadatosTram cfg = cfgMetadatosTramService.findCfgMetadatosTram(tramiteExpediente);
			this.tramiteExpediente.setCfgMetadatosTram(cfg);
		} catch (ValidacionException e1) {
			facesMsgErrorKey(e1.getMessage());
		}		
	}
	
	public void guardarNotificacion(TramiteExpediente tramExp)
	{	
		DetalleExpdteTram detTramExp = null;
		ValoresDominio valDomResultadoNotif = null;
		
		if(tramExp != null)
		{
			detTramExp = tramExp.getDetalleExpdteTram();
			if(this.tramiteExpediente.getSelectedNuevoResulNotificacionId() != null)
			{
				valDomResultadoNotif = valoresDominioService.obtener(this.tramiteExpediente.getSelectedNuevoResulNotificacionId());	
			}
			
			detTramExp.setValorResultadoNotificacion(valDomResultadoNotif);
			
			try {
				if(validacionGuardar(detTramExp))
				{
					guardarDetalleTramiteExpInteresado(tramExp, detTramExp);
					guardarDetalleNotificacion(tramExp, detTramExp);
					detalleExpdteTramService.guardar(detTramExp);
					
					ObservacionesExpedientes obsExp = tramExp.getObservaciones();
					obsExp = observacionesExpedientesService.guardarObservacionesExpedientes(obsExp, obsExp.getTexto(), Constantes.COD_VAL_DOM_TIPOBS_TRA, expedientes);
					tramExp.setObservaciones(obsExp);
					tramExp = tramiteExpedienteService.guardar(tramExp);
					obsExp.setTramiteExpdte(tramExp);
					observacionesExpedientesService.guardar(obsExp);
					
					FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "",
									TRAMNOTIFMSJ + " " + mensajesProperties.getString(ACTUALIZADACORRECTAMENTE)));
					PrimeFaces.current().ajax().update(FORMFORMULARIOLISTADONOTIFICACIONESPENDIENTES);
				}
				
			} catch (BaseException e) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						mensajesProperties.getString(MENSAJEERROR));
				PrimeFaces.current().dialog().showMessageDynamic(message);
				log.error(e.getMessage());
			}

		}
	}
	
	public boolean validacionGuardar(DetalleExpdteTram detTramExp)
	{
		boolean validoGuardar = true;


		if(detTramExp.getFechaEnvio() != null  &&
				(detTramExp.getFechaEnvio().after(FechaUtils.hoy())))
		{
			facesMsgErrorKey(FECHAENVIONOSUPFECHAACTUAL);
			return false;
		}
		
		if(detTramExp.getFechaNotificacion() != null  &&
				(detTramExp.getFechaNotificacion().after(FechaUtils.hoy())))
		{
			facesMsgErrorKey(FECHANOTIFNOSUPFECHAACTUAL);
			return false;
		}
		
		if((detTramExp.getFechaEnvio() != null && detTramExp.getFechaNotificacion() != null) &&
				(detTramExp.getFechaNotificacion().before(detTramExp.getFechaEnvio())))
		{
			facesMsgErrorKey(FECHANOTIFNOANTERIORFECHAENV);
			validoGuardar = false;
		}
			
	    return validoGuardar;
	}
	
	public boolean validacionesFinalizar(TramiteExpediente tramExp)
	{
		boolean validoFinalizar = true; 
		
		DetalleExpdteTram detExpTram = tramExp.getDetalleExpdteTram();
		
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
		

		if(detExpTram != null && validacionGuardar(detExpTram) && (!validarResultadoNotificacion(tramExp)))
		{
			return false;
		}
		
		return validoFinalizar;
	}
	

	private boolean validarResultadoNotificacion(TramiteExpediente trEx) {
		
		if (trEx.getDetalleExpdteTram().getValorResultadoNotificacion()== null){
			facesMsgErrorKey(RESULTNOTIFICACIONNOINFORMADO);
			return false;
		}

		return true;
	}
	
	
	public String finalizarNotificacion(TramiteExpediente tramExp) {
		
		DetalleExpdteTram detTramExp = null;
		Usuario usuario = (Usuario)JsfUtils.getSessionAttribute(Constantes.USUARIO);
		Date tramFechaFin=FechaUtils.fechaYHoraActualDate();
		Date tramFechaFinReal=FechaUtils.fechaYHoraActualDate();
		
		if(tramExp != null)
		{
			detTramExp = tramExp.getDetalleExpdteTram();
			
			if(validacionesFinalizar(tramExp))
			{
				tramExp.setFinalizado(true);
				tramExp.setUsuarioFinalizacion(usuario);
				tramExp.setFechaFin(tramFechaFin);	
				tramExp.setFechaFinReal(tramFechaFinReal);
				
				tramExp.setSituacionAdicional(null);
				
				actualizarTramiteSuperior(tramExp);
				
				
				try {
					tramExp = tramiteExpedienteService.finalizarTramite(usuario, tramExp);
					
					detalleExpdteTramService.guardar(detTramExp);
					tramiteExpedienteService.guardar(tramExp);
					
				} catch (BaseException e) {
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
							mensajesProperties.getString(MENSAJEERROR));
					PrimeFaces.current().dialog().showMessageDynamic(message);
					log.error(e.getMessage());
					return null;
				}

			FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "",
							TRAMNOTIFMSJ + " " + mensajesProperties.getString(FINALIZADACORRECTAMENTE)));

			PrimeFaces.current().ajax().update(FORMFORMULARIOLISTADONOTIFICACIONESPENDIENTES);
			
			}else
			{
				return "";
			}
		}
		
		return "";
		
	}
	
	public void actualizarTramiteSuperior(TramiteExpediente tramExp)
	{
		TramiteExpediente tramExpSup = null;
		DetalleExpdteTram detExpTramSup = null;
		DetalleExpdteTram detExpTram;
		
		if(tramExp != null && tramExp.getTramiteExpedienteSup() != null)
		{
				detExpTram = tramExp.getDetalleExpdteTram();
				tramExpSup = tramExp.getTramiteExpedienteSup();
				
				if(tramExpSup.getId() != null)
				{
					tramExpSup = tramiteExpedienteService.obtener(tramExpSup.getId());
					detExpTramSup = tramExpSup.getDetalleExpdteTram();
					
					if(detExpTramSup != null)
					{
						actualizarDatosTramSupNotif(detExpTramSup, detExpTram);
						
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
		}
	}
	
	public void actualizarDatosTramSupNotif(DetalleExpdteTram detExpTramSup, DetalleExpdteTram detExpTram)
	{
		if(detExpTramSup != null)
		{
			detExpTramSup.setFechaEnvio(detExpTramSup.getFechaEnvio()==null ? detExpTram.getFechaEnvio() : detExpTramSup.getFechaEnvio());
			detExpTramSup.setFechaNotificacion(detExpTramSup.getFechaNotificacion()==null ? detExpTram.getFechaNotificacion() : detExpTramSup.getFechaNotificacion());
			detExpTramSup.setValorResultadoNotificacion(detExpTramSup.getValorResultadoNotificacion()==null ? detExpTram.getValorResultadoNotificacion() : detExpTramSup.getValorResultadoNotificacion());
		}
	}
	
	public void onChangeTipoInteresado(TramiteExpediente tE) {
		recargaIdentificacionesInteresado(tE);
	}
	
	public void recargaIdentificacionesInteresado(TramiteExpediente tE) {
		

		if (tE.getSelectedNuevoTipoInteresadoId() != null) {
			ValoresDominio valDom = valoresDominioService.obtener(tE.getSelectedNuevoTipoInteresadoId());
			tE.setSelectedNuevoCanalSalidaId(null);
						
			if (Constantes.COD_VAL_DOM_SUJOBL.equals(valDom.getCodigo())) {
				cargaIdentifInteresadoSujObl(tE);
			} else if (Constantes.COD_VAL_DOM_PERS.equals(valDom.getCodigo())) {
				cargaIdentifInteresadoPers(tE);
			} else if (Constantes.COD_VAL_DOM_DPD.equals(valDom.getCodigo())) {
				cargaIdentifInteresadoDpd(tE);
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
	
	public void cargaIdentifInteresadoSujObl(TramiteExpediente tE) {
		limpiarDatosValorInteresado(tE);
		tE.setEsIdentIntSujOblig(true);
		
		tE.setListaIdentifIntSujOblig(sujetosObligadosExpedientesService.obtenerSujetosObligadosExpediente(tE.getExpediente().getId()));	
		
		SujetosObligadosExpedientes sujOblExp = sujetosObligadosExpedientesService.obtenerSujetosObligadosExpedientePrincipalPorExpediente(tE.getExpediente().getId());
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
		
		if(Constantes.C003.equals(tE.getTipoTramite().getComportamiento()) && sujObl != null && sujObl.getValorViaComunicacion() != null)
		{
			tE.setSelectedNuevoCanalSalidaId(sujObl.getValorViaComunicacion().getId());
		}
	}

	public void cargaIdentifInteresadoPers(TramiteExpediente tE) {
		limpiarDatosValorInteresado(tE);
		tE.setEsIdentIntPersona(true);
		
		List<PersonasExpedientes> listaPersonasPorExpediente = personasExpedientesService.obtenerPersPorExpediente(tE.getExpediente().getId());
		List<PersonaDTO> listaIdentifIntPersonas = cargarPersonasYRepresentantes(listaPersonasPorExpediente);
		
		tE.setListaIdentifIntPersDTO(listaIdentifIntPersonas);
		tE.setSelectedNuevaIdentifInteresadoId(null);

		PersonasExpedientes personasExpPpal =personasExpedientesService.obtenerPersonaExpedientePrincipalPorExpediente(tE.getExpediente().getId()); 
		
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

		tE.setListaIdentifIntDpd(sujetosObligadosExpedientesService.obtenerDpdExpediente(tE.getExpediente().getId()));
		tE.setSelectedNuevaIdentifInteresadoId(null);
			
		SujetosObligadosExpedientes sujObExp = sujetosObligadosExpedientesService
				.obtenerSujetosObligadosExpedientePrincipalPorExpediente(tE.getExpediente().getId());
		
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
	
	public void limpiarDatosValorInteresado(TramiteExpediente tE) {
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
	
	public void guardarDetalleNotificacion(TramiteExpediente traExp, DetalleExpdteTram detExpTram)
	{
		guardarDetalleNotificacionDatosSalida(traExp, detExpTram);
		
		detExpTram.setFechaEnvio(traExp.getDetalleExpdteTram().getFechaEnvio());
		detExpTram.setPlazo(traExp.getDetalleExpdteTram().getPlazo());	
		
		detExpTram.setFechaLimite(traExp.getDetalleExpdteTram().getFechaLimite());
		detExpTram.setFechaNotificacion(traExp.getDetalleExpdteTram().getFechaNotificacion());
	}
	
	public void guardarDetalleNotificacionDatosSalida(TramiteExpediente traExp, DetalleExpdteTram detExpTram)
	{
		if(traExp.getSelectedNuevoCanalSalidaId() != null)
		{
			detExpTram.setValorCanalSalida(valoresDominioService.obtener(traExp.getSelectedNuevoCanalSalidaId()));
		}else {
			detExpTram.setValorCanalSalida(null);
		}
		
		detExpTram.setDatosCanalSalida(traExp.getDetalleExpdteTram().getDatosCanalSalida());
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
		guardarIdentifPorTipoInteresado(traExp, detExpTram);

	}
	
	private void guardarIdentifPorTipoInteresado(TramiteExpediente traExp, DetalleExpdteTram detExpTram)
	{
		if(detExpTram.getValorTipoInteresado() != null)
		{
			if(Constantes.COD_VAL_DOM_PERS.equals(detExpTram.getValorTipoInteresado().getCodigo()) || Constantes.COD_VAL_DOM_DPD.equals(detExpTram.getValorTipoInteresado().getCodigo()))
			{	
				guardaIdentifPersona(traExp, detExpTram);
			}else if (Constantes.COD_VAL_DOM_SUJOBL.equals(detExpTram.getValorTipoInteresado().getCodigo()))
			{
				guardaIdentifSujOblig(traExp, detExpTram);
				
			}else if (Constantes.COD_VAL_DOM_AUTCON.equals(detExpTram.getValorTipoInteresado().getCodigo()))
			{
				guardaIdentifAutCon(traExp, detExpTram);
			}
		}else {
			traExp.setSelectedNuevaIdentifInteresadoId(null);		
		}
	}
	
	private void guardaIdentifPersona(TramiteExpediente traExp, DetalleExpdteTram detExpTram)
	{
		if(traExp.getSelectedNuevaIdentifInteresadoId() != null)
		{
			detExpTram.setPersonasInteresado(personasService.obtener(traExp.getSelectedNuevaIdentifInteresadoId()));	
		}else {
			detExpTram.setPersonasInteresado(null);
		}
	}
	
	private void guardaIdentifSujOblig(TramiteExpediente traExp, DetalleExpdteTram detExpTram)
	{
		if(traExp.getSelectedNuevaIdentifInteresadoId() != null) {
			detExpTram.setSujetosObligadosInteresado(sujetosObligadosService.obtener(traExp.getSelectedNuevaIdentifInteresadoId()));	
		}else {
			detExpTram.setSujetosObligadosInteresado(null);
		}
	}

	private void guardaIdentifAutCon(TramiteExpediente traExp, DetalleExpdteTram detExpTram)
	{
		if(traExp.getSelectedNuevaIdentifInteresadoId() != null) {
			detExpTram.setValorDominioInteresado(valoresDominioService.obtener(traExp.getSelectedNuevaIdentifInteresadoId()));	
		}else{
			detExpTram.setValorDominioInteresado(null);
		}
	}
}
