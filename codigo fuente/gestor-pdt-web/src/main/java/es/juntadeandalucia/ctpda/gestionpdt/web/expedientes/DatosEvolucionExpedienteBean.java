package es.juntadeandalucia.ctpda.gestionpdt.web.expedientes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;

import org.primefaces.PrimeFaces;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.CfgMetadatosTram;
import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.TramiteExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.service.CfgMetadatosTramService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.TramiteExpedienteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.ValidacionException;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.Constantes;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.JsfUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.LazyDataModelByQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.MyFilterMeta;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.BaseBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.ContextoVolver;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.VolverBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@ViewScoped
public class DatosEvolucionExpedienteBean extends BaseBean implements Serializable {


	private static final long serialVersionUID = 1L;	
	
	private static final String FORMAL = "formal";
	
	@Getter @Setter
	private Date fechaInicialFiltro;
	
	@Getter @Setter
	private Date fechaTramiteFiltro;
	
	@Getter @Setter
	private Date fechaFinalFiltro;
	
	@Getter @Setter
	private String selectedTipoTramiteIdFiltro;
	
	@Getter @Setter
	private String informacionRelevanteFiltro;
	
	@Getter @Setter
	private List<String> listaTiposTramites;
	
	@Getter @Setter
	private Boolean nivelSuperiorFiltro;
	
	@Getter @Setter
	private Integer selectedEstadoIdFiltro;
	
	@Autowired
	private TramiteExpedienteService tramiteExpedienteService;

	@Getter @Setter
	private Expedientes expediente;
	
	@Getter @Setter
	private Long idExp;
	
	@Autowired
	private ComunTramitesBean comunTramitesBean;
	
	@Autowired
	private ExpedientesService expedientesService;
	
	@Getter
	private LazyDataModelByQueryService<TramiteExpediente> lazyTramitesExpediente;

	@Getter
	private List<SortMeta> defaultOrdenListLazyTramitesExpediente;
	
	@Autowired
	private VolverBean volverBean;
	
	@Autowired
	private CfgMetadatosTramService cfgMetadatosTramService;
	
	@Getter @Setter
	private String etiquetaMetadato;
	
	@Getter @Setter
	private String tipoTramiteFiltro;
	
	@Getter
	@Setter
	private List<String> listaCodigoPermisos;
	
	@Getter	@Setter
	private Boolean permisoConsTramiteEvol;
	
	

	@PostConstruct
	@Override
	public void init() {
		super.init();
		
		/**
		 * GESTION DE PERMISOS
		 * */
		listaCodigoPermisos = (List<String>) JsfUtils.getSessionAttribute(Constantes.LISTACODIGOPERMISOS);
		permisoConsTramiteEvol = listaCodigoPermisos.contains(Constantes.PERMISO_CONS_TRAMITE_EVOL);
				
		cargarExpediente();
		
		if (expediente.getId() != null) {		
			tipoTramiteFiltro = FORMAL;
			
			listaTiposTramites = tramiteExpedienteService.findDescripcionTipoTramitesInformalesByExp(expediente.getId(),false);
			Collections.sort(listaTiposTramites);
			
			nivelSuperiorFiltro = true;
			
			lazyTramitesExpediente = new LazyDataModelByQueryService<>(TramiteExpediente.class, tramiteExpedienteService);
			lazyTramitesExpediente.setPreproceso((a, b, c, filters) -> {
				filters.put("expediente.id", new MyFilterMeta(expediente.getId()));
				filters.put("activo", new MyFilterMeta(true));
				
				filtrosLazyTramitesExpediente(filters);				
			});
			
			defaultOrdenListLazyTramitesExpediente = new ArrayList<>();
			defaultOrdenListLazyTramitesExpediente.add(SortMeta.builder().field("fechaIni").order(SortOrder.DESCENDING).priority(1).build());
			defaultOrdenListLazyTramitesExpediente.add(SortMeta.builder().field("id").order(SortOrder.DESCENDING).priority(2).build());		
		}
		
	}
	
	private void filtrosLazyTramitesExpediente (Map<String, FilterMeta> filters ) {
		if (fechaInicialFiltro != null) {
			filters.put("#fechaInicialTramite", new MyFilterMeta(fechaInicialFiltro));	
		}
		if (fechaTramiteFiltro != null) {
			filters.put("#fechaTramite", new MyFilterMeta(fechaTramiteFiltro));	
		} 
		if (fechaFinalFiltro != null) {
			filters.put("#fechaFinalTramite", new MyFilterMeta(fechaFinalFiltro));	
		}
		if (informacionRelevanteFiltro != null) {
			filters.put("informacionRelevante", new MyFilterMeta(informacionRelevanteFiltro));
		}
		if (selectedTipoTramiteIdFiltro != null) {
			filters.put("tipoTramite.descripcion", new MyFilterMeta(selectedTipoTramiteIdFiltro));
		}
		if (nivelSuperiorFiltro != null && nivelSuperiorFiltro) {
			filters.put("nivel", new MyFilterMeta("0"));
		}
		if (selectedEstadoIdFiltro != null && selectedEstadoIdFiltro.equals(0)) {
			filters.put("finalizado", new MyFilterMeta(false));
		}else if (selectedEstadoIdFiltro != null && selectedEstadoIdFiltro.equals(1)) {
			filters.put("finalizado", new MyFilterMeta(true));
		}
		if(tipoTramiteFiltro.equals(FORMAL)) {
			filters.put("tipoTramite.informal", new MyFilterMeta("false"));
		}else if(tipoTramiteFiltro.equals("informal")) {
			filters.put("tipoTramite.informal", new MyFilterMeta("true"));
		}
	}
	
	private void cargarExpediente() {
		ContextoVolver cv = volverBean.getContexto(); 

		expediente = (Expedientes) JsfUtils.getSessionAttribute("expedienteFormulario");
		if (expediente == null) {
			expediente = (Expedientes) JsfUtils.getSessionAttribute("expediente");
		}
		if (cv != null) {
			idExp = (Long) cv.get("idExp");
			expediente = (Expedientes) JsfUtils.getSessionAttribute("expedientesDG");
		} else {
			idExp = (Long) JsfUtils.getFlashAttribute("idExp");
			if (idExp != null) {
				expediente = expedientesService.obtener(idExp);
			}
		}
	}
	
	public void limpiarFiltro() {
		fechaInicialFiltro = null;
		fechaFinalFiltro = null;
		fechaTramiteFiltro = null;
		informacionRelevanteFiltro = null;
		selectedTipoTramiteIdFiltro = null;
		nivelSuperiorFiltro = true;
		selectedEstadoIdFiltro = null;
		tipoTramiteFiltro = FORMAL;
		listaTiposTramites = tramiteExpedienteService.findDescripcionTipoTramitesInformalesByExp(expediente.getId(),false);
		Collections.sort(listaTiposTramites);
	}
	
	public String metadatosExpediente (String etiqueta, TramiteExpediente tramiteExpediente) {
		etiquetaMetadato = "Etiqueta pendiente definir"; 
		
		CfgMetadatosTram cfgMetadatosTram = null;
		try {
			cfgMetadatosTram = cargaMetadatosTramiteByTramiteExpediente(tramiteExpediente);
		} catch (ValidacionException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString("no.existe.registro.config"));
			log.warn("metadatosExpediente - " + message.getDetail());
		}
		
		/** Propuesta de mejora. Haría innecesario mantener el bloque de ifs y sus subllamadas 
		String getter = "get" + org.apache.commons.lang3.StringUtils.capitalize(etiqueta);
		Method m = BeanUtils.findMethod(CfgMetadatosTram.class, getter);
		
		if (m != null) {
			try {
				etiquetaMetadato = (String) m.invoke(cfgMetadatosTram);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		
		if(true) return etiquetaMetadato;
		
		//*/
		
		if(cfgMetadatosTram != null) {
			if(etiqueta.equals("tipoInteresado")) {
				etiquetaMetadato = cfgMetadatosTram.getTipoInteresado();
			}else if(etiqueta.equals("identifInteresado")) {
				etiquetaMetadato = cfgMetadatosTram.getIdentifInteresado();
			}else if(etiqueta.equals("acuseRecibo")) {
				etiquetaMetadato = cfgMetadatosTram.getAcuseRecibo();
			}else if(etiqueta.equals("firmante")) {
				etiquetaMetadato = cfgMetadatosTram.getFirmante();
			}else if(etiqueta.equals("tipoFirma")) {
				etiquetaMetadato = cfgMetadatosTram.getTipoFirma();
			}else if(etiqueta.equals("canalSalida")) {
				etiquetaMetadato = cfgMetadatosTram.getCanalSalida();
			}else if(etiqueta.equals("fechaEntrada")) {
				etiquetaMetadato = cfgMetadatosTram.getFechaEntrada();
			}else if(etiqueta.equals("identifEntrada")) {
				etiquetaMetadato = cfgMetadatosTram.getIdentificacionEntrada();
			}else if(etiqueta.equals("datosCanalSalida")) {
				etiquetaMetadato = cfgMetadatosTram.getDatosCanalSalida();	
			}else if(etiqueta.equals("datosCanalInfEntrada")) {
				etiquetaMetadato = cfgMetadatosTram.getDatosCanalInfEntrada();
			}else if(etiqueta.equals("datosCanalInfSalida")) {
				etiquetaMetadato = cfgMetadatosTram.getDatosCanalInfSalida();	
			}else {
				etiquetaMetadato = etiquetaMetadatoByEtiqueta(etiqueta,cfgMetadatosTram);
			}
		}
		
		return etiquetaMetadato;
	}
	
	private String etiquetaMetadatoByEtiqueta (String etiqueta, CfgMetadatosTram cfgMetadatosTram) {
		etiquetaMetadato = etiqueta; 
		
		if(etiqueta.equals("canalEntrada")) {
			etiquetaMetadato = cfgMetadatosTram.getCanalEntrada();
		}else if(etiqueta.equals("identificacionEntrada")) {
			etiquetaMetadato = cfgMetadatosTram.getIdentificacionEntrada();
		}else if(etiqueta.equals("sentidoResolucion")) {
			etiquetaMetadato = cfgMetadatosTram.getSentidoResolucion();
		}else if(etiqueta.equals("tipoResolucion")) {
			etiquetaMetadato = cfgMetadatosTram.getTipoResolucion();
		}else if(etiqueta.equals("fechaResolucion")) {
			etiquetaMetadato = cfgMetadatosTram.getFechaResolucion();
		}else if(etiqueta.equals("acreditaCumplimiento")) {
			etiquetaMetadato = cfgMetadatosTram.getAcreditaCumplimiento();
		}else if(etiqueta.equals("fechaEntrada")) {
			etiquetaMetadato = cfgMetadatosTram.getFechaEntrada();
		}else if(etiqueta.equals("fechaEnvio")) {
			etiquetaMetadato = cfgMetadatosTram.getFechaEnvio();
		}else if(etiqueta.equals("notifInfructuosa")) {
			etiquetaMetadato = cfgMetadatosTram.getNotifInfructuosa();
		}else if(etiqueta.equals("fechaNotificacion")) {
			etiquetaMetadato = cfgMetadatosTram.getFechaNotificacion();
		}else if(etiqueta.equals("fechaEmision")) {
			etiquetaMetadato = cfgMetadatosTram.getFechaEmision();
		}else if(etiqueta.equals("fechaFirma")) {
			etiquetaMetadato = cfgMetadatosTram.getFechaFirma();
		}else if(etiqueta.equals("fechaInforme")) {
			etiquetaMetadato = cfgMetadatosTram.getFechaInforme();
		}else if(etiqueta.equals("fechaSubsanacion")) {
			etiquetaMetadato = cfgMetadatosTram.getFechaSubsanacion();
		}else {
			etiquetaMetadato = etiquetaMetadatoByEtiqueta2(etiqueta,cfgMetadatosTram);
		}
		
		return etiquetaMetadato;
	}
	
	private String etiquetaMetadatoByEtiqueta2(String etiqueta, CfgMetadatosTram cfgMetadatosTram) {
		etiquetaMetadato = etiqueta; 
		
		if(etiqueta.equals("fechaRespuesta")) {
			etiquetaMetadato = cfgMetadatosTram.getFechaRespuesta();
		}else if(etiqueta.equals("fechaAcreditacion")) {
			etiquetaMetadato = cfgMetadatosTram.getFechaAcreditacion();
		}else if(etiqueta.equals("plazo")) {
			etiquetaMetadato = cfgMetadatosTram.getPlazo();
		}else if(etiqueta.equals("afectaPlazos")) {
			etiquetaMetadato = cfgMetadatosTram.getAfectaPlazos();
		}else if(etiqueta.equals("numResolucion")) {
			etiquetaMetadato = cfgMetadatosTram.getNumResolucion();
		}else if(etiqueta.equals("propuestaApi")) {
			etiquetaMetadato = cfgMetadatosTram.getPropuestaAPI();
		}else if(etiqueta.equals("subsanaAdecuadamente")) {
			etiquetaMetadato = cfgMetadatosTram.getSubsanaAdecuadamente();
		}else if(etiqueta.equals("tipoAdmision")) {
			etiquetaMetadato = cfgMetadatosTram.getTipoAdmision();
		}else if(etiqueta.equals("motivoInadmision")){
			etiquetaMetadato = cfgMetadatosTram.getMotivoInadmision();
		}else if(etiqueta.equals("informRemitida")) {
			etiquetaMetadato = cfgMetadatosTram.getInformRemitida();
		}else if(etiqueta.equals("fechaLimite")) {
			etiquetaMetadato = cfgMetadatosTram.getFechaLimite();
		}else if(etiqueta.equals("tipoPlazo")) {
			etiquetaMetadato = cfgMetadatosTram.getTipoPlazo();
		}else if(etiqueta.equals("instructorAPI")) {
			etiquetaMetadato = cfgMetadatosTram.getInstructorAPI();
		}else if(etiqueta.equals("api")) {
			etiquetaMetadato = cfgMetadatosTram.getApi();
		}else {
			etiquetaMetadato = etiquetaMetadatoByEtiqueta3(etiqueta,cfgMetadatosTram);
		}		
		return etiquetaMetadato;
	}

	private String etiquetaMetadatoByEtiqueta3(String etiqueta, CfgMetadatosTram cfgMetadatosTram) {
		etiquetaMetadato = etiqueta; 
		
		if(etiqueta.equals("numeroPsan")) {
			etiquetaMetadato = cfgMetadatosTram.getNumeroPsan();
		}else if(etiqueta.equals("actoRec")) {
			etiquetaMetadato = cfgMetadatosTram.getActoRecurrido();
		}else if(etiqueta.equals("fechaRegistro")) {
			etiquetaMetadato = cfgMetadatosTram.getFechaRegistro();
		}
		
		return etiquetaMetadato;
	}
	
	private CfgMetadatosTram cargaMetadatosTramiteByTramiteExpediente (TramiteExpediente tramiteExpediente) throws ValidacionException {
		CfgMetadatosTram metadatosTramite = null;
		
		if(tramiteExpediente.getTramiteExpedienteSup() != null) {
			metadatosTramite = cfgMetadatosTramService.findCfgMetadatosTram(tramiteExpediente.getExpediente().getValorTipoExpediente().getId(), tramiteExpediente.getTipoTramite().getId(), tramiteExpediente.getTramiteExpedienteSup().getTipoTramite().getId());
		
			if(metadatosTramite == null) {
				metadatosTramite = cfgMetadatosTramService.findCfgMetadatosTram(tramiteExpediente.getExpediente().getValorTipoExpediente().getId(), tramiteExpediente.getTipoTramite().getId());
				
				if(metadatosTramite == null) {
					metadatosTramite = cfgMetadatosTramService.findCfgMetadatosTram(tramiteExpediente.getExpediente().getValorTipoExpediente().getId(), null);
				}				
			}
			
		}else{		
			metadatosTramite = cfgMetadatosTramService.findCfgMetadatosTram(tramiteExpediente.getExpediente().getValorTipoExpediente().getId(), tramiteExpediente.getTipoTramite().getId());
			
			if(metadatosTramite == null) {
				metadatosTramite = cfgMetadatosTramService.findCfgMetadatosTram(tramiteExpediente.getExpediente().getValorTipoExpediente().getId(), null);
			}
		}
		
		if(metadatosTramite == null){
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString("error"));
			PrimeFaces.current().dialog().showMessageDynamic(message);	
		}
		
		return metadatosTramite;
	}
	
	public void onChangeListaTipoTramite () {
		if(tipoTramiteFiltro != null)
		{
			if(tipoTramiteFiltro.equals(FORMAL)) {
				listaTiposTramites = tramiteExpedienteService.findDescripcionTipoTramitesInformalesByExp(expediente.getId(),false);
				Collections.sort(listaTiposTramites);
			}else if(tipoTramiteFiltro.equals("informal")) {
				listaTiposTramites = tramiteExpedienteService.findDescripcionTipoTramitesInformalesByExp(expediente.getId(),true);
				Collections.sort(listaTiposTramites);
			}else {
				listaTiposTramites = tramiteExpedienteService.findDescripcionTipoTramitesByExp(expediente.getId());
				Collections.sort(listaTiposTramites);
			}
			PrimeFaces.current().ajax().update("formFormularioExpedientes:tabViewPestanasExpediente:tipoTramite_filtro");
			PrimeFaces.current().ajax().update("formFormularioExpedientes:tabViewPestanasExpediente:listatipoTramite_filtro");
			
		}
	}
	
	public void onConsultarTramite(Long idTramite) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, ((Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS))+1);
		comunTramitesBean.onConsultarTramite(idTramite);
	}
	
}

		
		
		
			
		