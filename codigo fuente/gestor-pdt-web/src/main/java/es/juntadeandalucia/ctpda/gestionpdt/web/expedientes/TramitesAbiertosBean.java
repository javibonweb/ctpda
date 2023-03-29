package es.juntadeandalucia.ctpda.gestionpdt.web.expedientes;

import java.io.Serializable;
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

import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.ResponsablesTramitacion;
import es.juntadeandalucia.ctpda.gestionpdt.model.TipoTramite;
import es.juntadeandalucia.ctpda.gestionpdt.model.TramiteExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.TramitesAbiertosMaestra;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.service.ResponsablesTramitacionService;
import es.juntadeandalucia.ctpda.gestionpdt.service.TramiteExpedienteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.TramitesAbiertosMaestraService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ValoresDominioService;
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
public class TramitesAbiertosBean extends BaseBean implements Serializable {
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	private static final String EDITABLE = "editable";
	private static final String FORMTRAMITESABIERTOS = "formListadoTramitesAbiertos";
	public static final String VOLVERLISTADOTRAMITESABIERTOS = "_volverTramitesAbiertos_";

	
	@Autowired
	private ValoresDominioService valoresDominioService;


	@Autowired
	private TramitesAbiertosMaestraService tramitesAbiertosMaestraService;
	
	@Autowired
	private TramiteExpedienteService tramiteExpedienteService;

	
	@Autowired
	private ResponsablesTramitacionService responsablesTramitacionService;

	@Getter
	private LazyDataModelByQueryService<TramitesAbiertosMaestra> lazyModel;
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
	private Long selectedNuevoTipoExpedienteId;

	@Getter
	@Setter
	private Long selectedSituacionIdFiltro;
	
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
	private String personaFiltro;
	@Getter
	@Setter
	private String identPersonaFiltro;

	@Getter
	@Setter
	private String sujetoObligadoFiltro;
	@Getter
	@Setter
	private TramitesAbiertosMaestra selectedTramitesAbiertos;

	@Getter
	@Setter
	private List<ValoresDominio> listaValoresDominioTipoExp;
	

	@Getter
	@Setter
	private List<TipoTramite> listaTipoTramites;
	
	
	@Getter
	@Setter
	private List<ValoresDominio> listaValoresDominioSituacion;
	
	
	@Getter
	@Setter
	private List<ResponsablesTramitacion> listaResponsablesTramitacion;
	
	@Getter
	@Setter
	private List<ResponsablesTramitacion> listaResponsablesExpediente;
	
	@Autowired
	private VolverBean volverBean;

	@Getter
	@Setter
	private Expedientes expedientes;
	
	@Getter
	private SortMeta defaultOrden;
	
	
	@Autowired
	private NavegacionBean navegacionBean;

	@PostConstruct
	@Override
	public void init() {
		super.init();
		
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, 0);
		
		FacesUtils.setAttribute(EDITABLE, JsfUtils.getFlashAttribute(EDITABLE));
		

		listaValoresDominioTipoExp = valoresDominioService.findValoresTipoExpediente();
		
		listaTipoTramites = tramitesAbiertosMaestraService.findTiposTramitesByTramiteAbierto();

		listaValoresDominioSituacion = tramitesAbiertosMaestraService.findSituacionesExpedienteByTramiteAbierto();
		
		
		listaResponsablesTramitacion = responsablesTramitacionService.findResponsablesActivos();
		
		listaResponsablesExpediente = responsablesTramitacionService.findResponsablesActivos();
		

		expedientes = new Expedientes();
		
		defaultOrden = SortMeta.builder().field("fechaIni").order(SortOrder.DESCENDING).priority(1).build();
		defaultOrden = SortMeta.builder().field("id").order(SortOrder.DESCENDING).priority(2).build();
		
		cargarFiltros();
		
		navegacionBean.setTxtMigaPan(Constantes.LISTADO_TRAMITES_ABIERTOS);
	}
	
	public void buscar()
	{
		lazyModel = new LazyDataModelByQueryService<>(TramitesAbiertosMaestra.class, tramitesAbiertosMaestraService);

		lazyModel.setPreproceso((a, b, c, filters) ->
			filtrosLazyModel(filters)
		);
		
		PrimeFaces.current().ajax().update(FORMTRAMITESABIERTOS);
	}
	
	private void filtrosLazyModel (Map<String, FilterMeta> filters) {
		
		if (selectedTipoTramiteIdFiltro != null) {
			filters.put("tipoTramite.id", new MyFilterMeta(selectedTipoTramiteIdFiltro));
		}
		
		if (selectedResponsableTramIdFiltro != null) {
			String descripcionRespTram = responsablesTramitacionService.obtener(selectedResponsableTramIdFiltro).getDescripcion();
			filters.put("descRespTram", new MyFilterMeta(descripcionRespTram));
		}


		if (selectedTipoExpedienteIdFiltro != null) {
			String abreviatura = valoresDominioService.obtener(selectedTipoExpedienteIdFiltro).getAbreviatura();
			filters.put("tipoExp", new MyFilterMeta(abreviatura));
		}
		
		
		if (numeroExpedienteFiltro != null && !numeroExpedienteFiltro.isEmpty()) {
			filters.put("numExp", new MyFilterMeta(numeroExpedienteFiltro));
		}
		if (selectedSituacionIdFiltro != null) {
			String descripcion = valoresDominioService.obtener(selectedSituacionIdFiltro).getDescripcion();
			filters.put("situacionExp", new MyFilterMeta(descripcion));
		}
		if (fechaEntradaInicialFiltro != null) {
		filters.put("#fechaInicialInicio", new MyFilterMeta(fechaEntradaInicialFiltro));	
		} 
		if (fechaEntradaFinalFiltro != null) {
			filters.put("#fechaFinalInicio", new MyFilterMeta(fechaEntradaFinalFiltro));	
		} 
		
		validaRangoFechas(fechaEntradaInicialFiltro, fechaEntradaFinalFiltro);
		
		if (personaFiltro != null && !personaFiltro.isEmpty()) {
			filters.put("persona", new MyFilterMeta(personaFiltro));
		}	
		if (identPersonaFiltro != null && !identPersonaFiltro.isEmpty()) {
			filters.put("identPersona", new MyFilterMeta(identPersonaFiltro));
		}	
		if (sujetoObligadoFiltro != null && !sujetoObligadoFiltro.isEmpty()) {
			filters.put("sujObligExp", new MyFilterMeta(sujetoObligadoFiltro));
		}	
		
		if (selectedResponsableExpIdFiltro != null) {
			String descripcionRespExp = responsablesTramitacionService.obtener(selectedResponsableExpIdFiltro).getDescripcion();
			filters.put("responsableExp", new MyFilterMeta(descripcionRespExp));
		}

		
	}
	
	private void validaRangoFechas(Date fechaInicio, Date fechaFin)
	{
		if((fechaInicio != null && fechaFin == null) || (fechaInicio == null && fechaFin != null))
		{
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					mensajesProperties.getString("rango.fechas.incompleto"));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			PrimeFaces.current().ajax().update(FORMTRAMITESABIERTOS);
		}
		if(fechaInicio != null &&  fechaFin != null &&  fechaInicio.after(fechaFin))
		{
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					mensajesProperties.getString("fecha.rango.ini.posterior"));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			PrimeFaces.current().ajax().update(FORMTRAMITESABIERTOS);
		}
	}
	

	public String redireccionMenu() {
		return ListadoNavegaciones.LISTADO_TRAMITES_ABIERTOS.getRegla();
	}

	public void limpiarFiltro() {
		selectedTipoExpedienteIdFiltro = null;
		selectedTipoTramiteIdFiltro = null;
		selectedResponsableTramIdFiltro = null;
		selectedResponsableExpIdFiltro = null;
		numeroExpedienteFiltro = "";
		selectedSituacionIdFiltro = null;
		fechaEntradaInicialFiltro = null;
		fechaEntradaFinalFiltro = null;
		personaFiltro = "";
		identPersonaFiltro = "";
		sujetoObligadoFiltro = "";
		lazyModel = null;
		
		PrimeFaces.current().ajax().update(FORMTRAMITESABIERTOS);		
	}

	public String onEditar(Long idTram) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, ((Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS))+1);
		TramiteExpediente tramExp = tramiteExpedienteService.obtener(idTram);
		Expedientes expediente = tramExp.getExpediente();
		final ContextoVolver contexto = volverBean.crearContexto(ListadoNavegaciones.LISTADO_TRAMITES_ABIERTOS.getRegla(), Constantes.VOLVERTRAMITESABIERTOS);
		cargarFiltrosEnContexto(contexto);
		
		JsfUtils.setSessionAttribute(Constantes.VUELTA_TRAMITES_ABIERTOS, true);
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
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, ((Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS))+1);
		TramiteExpediente tramExp = tramiteExpedienteService.obtener(idTram);
		Expedientes expediente = tramExp.getExpediente();
		
		final ContextoVolver contexto = volverBean.crearContexto(ListadoNavegaciones.LISTADO_TRAMITES_ABIERTOS.getRegla(), Constantes.VOLVERTRAMITESABIERTOS);
		cargarFiltrosEnContexto(contexto);
		
		JsfUtils.setSessionAttribute(Constantes.VUELTA_TRAMITES_ABIERTOS, true);
		
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
		contexto.put("selectedTipoTramiteIdFiltro", this.selectedTipoTramiteIdFiltro);
		contexto.put("selectedResponsableTramIdFiltro", this.selectedResponsableTramIdFiltro);
		contexto.put("selectedTipoExpedienteIdFiltro", this.selectedTipoExpedienteIdFiltro);
		
		contexto.put("numeroExpedienteFiltro", this.numeroExpedienteFiltro);
		contexto.put("selectedSituacionIdFiltro", this.selectedSituacionIdFiltro);
		
		contexto.put("fechaEntradaInicialFiltro", this.fechaEntradaInicialFiltro);
		contexto.put("fechaEntradaFinalFiltro", this.fechaEntradaFinalFiltro);
		contexto.put("personaFiltro", this.personaFiltro);
		contexto.put("identPersonaFiltro", this.identPersonaFiltro);
		contexto.put("sujetoObligadoFiltro", this.sujetoObligadoFiltro);
		contexto.put("selectedResponsableExpIdFiltro", this.selectedResponsableExpIdFiltro);
	}
	
	private void cargarFiltros() {
		final ContextoVolver cv = volverBean.recogerContexto(Constantes.VOLVERTRAMITESABIERTOS);
		
		if(cv != null) {
			this.selectedTipoTramiteIdFiltro = (Long) cv.get("selectedTipoTramiteIdFiltro");
			this.selectedResponsableTramIdFiltro = (Long) cv.get("selectedResponsableTramIdFiltro");
			this.selectedTipoExpedienteIdFiltro = (Long) cv.get("selectedTipoExpedienteIdFiltro");			
			
			this.numeroExpedienteFiltro = (String) cv.get("numeroExpedienteFiltro");
			this.selectedSituacionIdFiltro = (Long) cv.get("selectedSituacionIdFiltro");
			
			this.fechaEntradaInicialFiltro = (Date) cv.get("fechaEntradaInicialFiltro");
			this.fechaEntradaFinalFiltro = (Date) cv.get("fechaEntradaFinalFiltro");
			this.personaFiltro = (String) cv.get("personaFiltro");
			this.identPersonaFiltro = (String) cv.get("identPersonaFiltro");
			this.sujetoObligadoFiltro = (String) cv.get("sujetoObligadoFiltro");
			this.selectedResponsableExpIdFiltro = (Long) cv.get("selectedResponsableExpIdFiltro");
			buscar();
		} else {
			limpiarFiltro();
		}
	}

}
