package es.juntadeandalucia.ctpda.gestionpdt.web.controlplazos;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.CfgPlazosEstilos;
import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.ObservacionesExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.PlazosExpdte;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.model.enums.Origenes;
import es.juntadeandalucia.ctpda.gestionpdt.service.CfgPlazosEstilosService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ObservacionesExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.PlazosExpdteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ValoresDominioService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.Constantes;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.JsfUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.LazyDataModelByQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.MyFilterMeta;
import es.juntadeandalucia.ctpda.gestionpdt.web.expedientes.DatosExpedientesBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.menu.NavegacionBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.menu.NavegacionBean.ListadoNavegaciones;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.BaseBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.ContextoVolver;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.FacesUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.UtilsComun;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.VolverBean;
import es.juntadeandalucia.ctpda.gestionpdt.service.ResponsablesTramitacionService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@ViewScoped
@Slf4j
public class ControlPlazosBean extends BaseBean implements Serializable {
	
	
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String EDITABLE = "editable";
	public static final String VUELTACONTROLPLAZOS = "_volverControlPlazos_";
	private static final String CAMPOSOBLIGATORIOS = "campos.obligatorios";
	private static final String ACTUALIZADOCORRECTAMENTE = "actualizado.correctamente";
	private static final String MENSAJEERROR = "error";
	private static final String MENSAJESLISTADOPLAZO = "messagesListado";
	private static final String PLAZO = "Plazo ";
	private static final String PARAELEXPEDIENTE = " para el expediente ";
	private static final String NUMEROEXPEDIENTE = "numero.expediente";
	
	
	@Getter @Setter
	private List<String> listaCodigoPermisos;
	
	@Getter	@Setter
	private Boolean permisoConsPlazoExp;
	
	@Getter	@Setter
	private Boolean permisoEditPlazoExp;
	
	@Getter @Setter
	private Long selectedTipoExpedienteIdFiltro;
	
	@Getter @Setter
	private List<ValoresDominio> listaValoresDominioTipoExp;
	
	@Autowired
	private ValoresDominioService valoresDominioService;
	
	@Autowired
	private ObservacionesExpedientesService observacionesExpedientesService;
	
	@Getter @Setter
	private Long selectedTipoPlazoIdFiltro;
	
	@Getter @Setter
	private Long selectedResponsableIdFiltro;
	
	@Getter @Setter
	private List<ValoresDominio> listaValoresDominioTipoPlazo;
	
	@Getter @Setter
	private Date fechaLimiteFiltro;
	
	@Autowired
	private ResponsablesTramitacionService responsablesTramitacionService;
	
	@Getter
	private LazyDataModelByQueryService<PlazosExpdte> lazyModelPlazosExpediente;
	
	@Autowired
	private PlazosExpdteService plazosExpdteService;
	
	@Getter
	private SortMeta defaultOrden;
	
	@Getter @Setter
	private PlazosExpdte selectedPlazosExpedientes;
	
	@Getter	@Setter
	private Boolean permisoConsultaExpediente;
	
	@Autowired
	private VolverBean volverBean;
	
	@Autowired
	private CfgPlazosEstilosService cfgPlazosEstilosService;
	
	@Getter @Setter
	private Boolean modoAccesoEditar;
	
	@Getter @Setter
	private Boolean modoAccesoConsulta;
	
	@Getter @Setter
	private String cabeceraDialogo;
	
	@Getter @Setter
	private PlazosExpdte plazoExpDialogo;
	
	@Getter @Setter
	private Boolean permisoSavePlazoExp;
	
	@Getter @Setter
	private Boolean permisoDarCumplidoPlazoExp;
	
	@Getter @Setter
	private Boolean permisoActivarPlazoExp;
	
	@Getter @Setter
	private Boolean permisoDesacPlazoExp;
	
	@Getter @Setter
	private Boolean activoFiltro;
	
	@Getter @Setter
	private Boolean plazoExpDialogoBloqueado;
	
	@Getter @Setter
	private PlazosExpdte selectedPlazoExpdte;
	
	@Getter @Setter
	private Date fechaLimitePlazoExpDialogo;
	
	@Getter @Setter
	private Boolean pendienteFiltro;
	
	@Getter @Setter
	private Boolean permisoRehabilitarPlazoExp;
	
	@Autowired
	private ExpedientesService expedientesService;
	
	@Autowired
	private DatosExpedientesBean datosExpedientesBean;
	
	@Autowired
	private UtilsComun utilsComun;
	
	@Autowired
	private NavegacionBean navegacionBean;
	
	@PostConstruct
	@Override
	public void init() {
		super.init();
		
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, 0);
		
		navegacionBean.setTxtMigaPan(Constantes.LISTADO_CONTROLPLAZOS);
		
		/** GESTION DE PERMISOS */
		listaCodigoPermisos = (List<String>) JsfUtils.getSessionAttribute(Constantes.LISTACODIGOPERMISOS);		
		permisoConsPlazoExp = listaCodigoPermisos.contains(Constantes.PERMISO_CONS_PLAZOEXP);		
		permisoEditPlazoExp = listaCodigoPermisos.contains(Constantes.PERMISO_EDIT_PLAZOEXP);
		permisoConsultaExpediente = listaCodigoPermisos.contains(Constantes.PERMISO_CONS_EXP);
		permisoSavePlazoExp = listaCodigoPermisos.contains(Constantes.PERMISO_SAVE_PLAZOEXP);
		permisoDarCumplidoPlazoExp = listaCodigoPermisos.contains(Constantes.PERMISO_DARCUMP_PLAZOEXP);
		permisoActivarPlazoExp = listaCodigoPermisos.contains(Constantes.PERMISO_ACT_PLAZOEXP);
		permisoDesacPlazoExp = listaCodigoPermisos.contains(Constantes.PERMISO_DESAC_PLAZOEXP);
		permisoRehabilitarPlazoExp = listaCodigoPermisos.contains(Constantes.PERMISO_REHAB_PLAZOEXP);
		
		FacesUtils.setAttribute(EDITABLE, JsfUtils.getFlashAttribute(EDITABLE));
		
		listaValoresDominioTipoExp = valoresDominioService.findValoresDominioByCodigoDominio(Constantes.COD_TIPO_EXPEDIENTE);
		listaValoresDominioTipoPlazo = valoresDominioService.findValoresDominioByCodigoDominio(Constantes.COD_TIPO_PLAZO);
		
		lazyModelPlazosExpediente = new LazyDataModelByQueryService<>(PlazosExpdte.class, plazosExpdteService);
		lazyModelPlazosExpediente.setPreproceso((a, b, c, filters) -> {
			if (selectedTipoExpedienteIdFiltro != null) {
				filters.put("expediente.valorTipoExpediente.id", new MyFilterMeta(selectedTipoExpedienteIdFiltro));
			}
			if (selectedTipoPlazoIdFiltro != null) {
				filters.put("valorTipoPlazo.id", new MyFilterMeta(selectedTipoPlazoIdFiltro));
			}
			
			if (selectedResponsableIdFiltro != null) {
				String descripcion = responsablesTramitacionService.obtener(selectedResponsableIdFiltro).getDescripcion();
				filters.put("expediente.responsable.descripcion", new MyFilterMeta(descripcion));
			}
			
				if (fechaLimiteFiltro != null) {
				filters.put("#fecha_limite", new MyFilterMeta(fechaLimiteFiltro));	
			}
			if (activoFiltro != null && activoFiltro) {
				filters.put("activo", new MyFilterMeta("true"));
			} 	
			if (pendienteFiltro != null && pendienteFiltro) {
				filters.put("cumplido", new MyFilterMeta("false"));
			} 		
			
		});
		
		activoFiltro = true;
		pendienteFiltro = true;
		
		defaultOrden = SortMeta.builder().field("fechaLimite").order(SortOrder.ASCENDING).priority(1).build();
		
		plazoExpDialogo = new PlazosExpdte();
		plazoExpDialogo.setExpediente(new Expedientes());
		plazoExpDialogo.setValorTipoPlazo(new ValoresDominio());
		fechaLimitePlazoExpDialogo = null;
	}
	
	public String redireccionMenu() {
		return ListadoNavegaciones.LISTADO_CONTROL_PLAZOS.getRegla();
	}
	
	public void limpiarFiltro() {
		selectedTipoExpedienteIdFiltro = null;
		selectedTipoPlazoIdFiltro = null;
		fechaLimiteFiltro = null;
		selectedResponsableIdFiltro = null;
		activoFiltro = true;
		pendienteFiltro = true;
	}
	
	public String onConsultarExpediente(Long idExpediente, Long idControlPlazosSeleccionado) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, ((Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS))+1);
		
		JsfUtils.setFlashAttribute(EDITABLE, false);
		JsfUtils.setFlashAttribute("idExp", idExpediente);
		JsfUtils.setSessionAttribute(Constantes.VUELTAS_CONTROLPLAZOS, true);
		JsfUtils.setFlashAttribute(Constantes.IDPLAZOSELECCIONADO, idControlPlazosSeleccionado);
		ContextoVolver v = volverBean.crearContexto(ListadoNavegaciones.LISTADO_CONTROL_PLAZOS.getRegla(), VUELTACONTROLPLAZOS);
		v.put("idControlPlazos", idControlPlazosSeleccionado);
		v.put(EDITABLE, this.getFormEditable());
		Expedientes expediente = expedientesService.obtener(idExpediente);
		navegacionBean.setTxtMigaPan(Constantes.CONSULTA_EXPEDIENTE+expediente.getMigaDePan()+navegacionBean.expedientesRelacionadosMotivoPsan(expediente.getId()));
		return ListadoNavegaciones.FORM_EXPEDIENTES.getRegla();
	}
	
	public String configEstiloTabla (PlazosExpdte plazosExpdteSelect, String diasRestantesSelect) {
		String estiloAPintar = "";
		List<CfgPlazosEstilos> estiloByTipExpTipPlaDiasRest = cfgPlazosEstilosService.findEstiloByTipoExpTipoPlazoDiasRestantes(plazosExpdteSelect.getExpediente().getValorTipoExpediente().getId(), plazosExpdteSelect.getValorTipoPlazo().getId(), Long.parseLong(diasRestantesSelect));
				
		if(estiloByTipExpTipPlaDiasRest != null && estiloByTipExpTipPlaDiasRest.size() == 1) {
			estiloAPintar = estiloByTipExpTipPlaDiasRest.get(0).getEstilo().toLowerCase();
		}else {
			estiloAPintar = null;
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString("no.existe.registro.config"));
			log.warn("configEstiloTabla " + plazosExpdteSelect.getExpediente().getValorTipoExpediente().getCodigo() + ", " + plazosExpdteSelect.getValorTipoPlazo().getCodigo() + ", " + Long.parseLong(diasRestantesSelect) + " - " + message.getDetail());
		}
		PrimeFaces.current().ajax().update("formListadoControlPlazos:listadoPlazos");
		
		return estiloAPintar;
	}
	
	public void accesoRapido(final SelectEvent<PlazosExpdte> event) {
		modoAccesoConsulta = true;
		modoAccesoEditar = false;
		cabeceraDialogo = "Consulta de plazo";
		
		selectedPlazoExpdte = event.getObject();
		selectedPlazoExpdte = plazosExpdteService.obtener(selectedPlazoExpdte.getId());
		plazoExpDialogo = selectedPlazoExpdte;
	}
	
	public void modoAccesoFormulario (PlazosExpdte plazoExpSeleccionado, String modoAcceso) {
		if(modoAcceso.contains("consulta")) {
			modoAccesoConsulta = true;
			modoAccesoEditar = false;
			cabeceraDialogo = "Consulta de plazo";
		}else if(modoAcceso.contains("editar")) {
			modoAccesoEditar = true;
			modoAccesoConsulta = false;
			cabeceraDialogo = "Edición de plazo";
		}
		
		if(plazoExpSeleccionado != null && plazoExpSeleccionado.getId() != null) {
			plazoExpDialogo = plazoExpSeleccionado;
			plazoExpDialogo.setExpediente(plazoExpSeleccionado.getExpediente());
			plazoExpDialogo.setValorTipoPlazo(plazoExpSeleccionado.getValorTipoPlazo());
			plazoExpDialogo.setFechaLimite(plazoExpSeleccionado.getFechaLimite());
			ObservacionesExpedientes obsExp = observacionesExpedientesService.obtener(plazoExpSeleccionado.getObservaciones().getId());
			
			plazoExpDialogo.setObservaciones(obsExp);
			plazoExpDialogoBloqueado = plazoExpDialogo.getValorTipoPlazo().getBloqueado();
			fechaLimitePlazoExpDialogo = plazoExpSeleccionado.getFechaLimite();
		}
				
		PrimeFaces.current().executeScript("PF('dialogFormPlazo').show();");
	}
	
	public void guardarPlazo () {
		boolean puedoGuardar = validacionGuardarPlazo();
		try {
			if(puedoGuardar) {
				plazoExpDialogo.setFechaLimite(fechaLimitePlazoExpDialogo);
				plazoExpDialogo.setOrigenFinal(Origenes.M);			
				ObservacionesExpedientes obsExp = observacionesExpedientesService.guardarObservacionesExpedientes(plazoExpDialogo.getObservaciones(), plazoExpDialogo.getObservaciones().getTexto(), Constantes.COD_VAL_DOM_TIPOBS_TRA, plazoExpDialogo.getExpediente());
				plazoExpDialogo.setObservaciones(obsExp);
				plazoExpDialogo = plazosExpdteService.guardar(plazoExpDialogo);
				obsExp.setPlazoExpdte(plazoExpDialogo);
				observacionesExpedientesService.guardar(obsExp);		

				
				utilsComun.expedienteUltimaModificacion(plazoExpDialogo.getExpediente(),plazoExpDialogo.getFechaModificacion(),plazoExpDialogo.getFechaCreacion(),plazoExpDialogo.getUsuModificacion(),plazoExpDialogo.getUsuCreacion());
					
				PrimeFaces.current().executeScript("PF('dialogFormPlazo').hide();");
				if(plazoExpDialogo.getExpediente().getId()!=null) {
					FacesContext.getCurrentInstance().addMessage(MENSAJESLISTADOPLAZO,new FacesMessage(FacesMessage.SEVERITY_INFO, "",PLAZO+" "+plazoExpDialogo.getValorTipoPlazo().getDescripcion() +PARAELEXPEDIENTE +mensajesProperties.getString(NUMEROEXPEDIENTE)+": "+plazoExpDialogo.getExpediente().getNumExpediente()+" " +  mensajesProperties.getString(ACTUALIZADOCORRECTAMENTE)));
				}else {
					FacesContext.getCurrentInstance().addMessage(MENSAJESLISTADOPLAZO,new FacesMessage(FacesMessage.SEVERITY_INFO, "",PLAZO+" "+ mensajesProperties.getString(ACTUALIZADOCORRECTAMENTE)));
				}
				
			}
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
	}
	
	private boolean validacionGuardarPlazo () {
		boolean validoGuardar = true;
		
		if(fechaLimitePlazoExpDialogo == null){
			validoGuardar = false;
			PrimeFaces.current().executeScript("PF('dialogFormPlazo').show();");
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(CAMPOSOBLIGATORIOS));
			PrimeFaces.current().dialog().showMessageDynamic(message);
		}
		return validoGuardar;
	}
	
	public void darCumplidoPlazo () {
		try {
			plazoExpDialogo.setCumplido(true);
			plazoExpDialogo = plazosExpdteService.guardar(plazoExpDialogo);
			
			Expedientes expedienteModificado = plazoExpDialogo.getExpediente();
			expedienteModificado.setFechaUltimaPersistencia(plazoExpDialogo.getFechaModificacion()!=null ? plazoExpDialogo.getFechaModificacion() : plazoExpDialogo.getFechaCreacion());
			expedienteModificado.setUsuUltimaPersistencia(plazoExpDialogo.getUsuModificacion()!=null ? plazoExpDialogo.getUsuModificacion() : plazoExpDialogo.getUsuCreacion());
			expedientesService.guardar(expedienteModificado);
			
			PrimeFaces.current().executeScript("PF('dialogFormPlazo').hide();");
			if(plazoExpDialogo.getExpediente().getId()!=null) {
				FacesContext.getCurrentInstance().addMessage(MENSAJESLISTADOPLAZO, new FacesMessage(FacesMessage.SEVERITY_INFO, "", PLAZO+" "+plazoExpDialogo.getValorTipoPlazo().getDescripcion() +PARAELEXPEDIENTE +mensajesProperties.getString(NUMEROEXPEDIENTE)+": "+plazoExpDialogo.getExpediente().getNumExpediente()+" " +  " cumplido correctamente"));
			}else {
				FacesContext.getCurrentInstance().addMessage(MENSAJESLISTADOPLAZO, new FacesMessage(FacesMessage.SEVERITY_INFO, "", PLAZO+" cumplido correctamente"));
			}
			
			
			datosExpedientesBean.actualizarCabecera(expedienteModificado,null,null,null);
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
	}
	
	public void activarPlazo(PlazosExpdte plazoExpSeleccionado) {
		try {
			plazoExpSeleccionado.setActivo(true);
			plazoExpSeleccionado = plazosExpdteService.guardar(plazoExpSeleccionado);
			
			Expedientes expedienteModificado = plazoExpSeleccionado.getExpediente();
			expedienteModificado.setFechaUltimaPersistencia(plazoExpSeleccionado.getFechaModificacion()!=null ? plazoExpSeleccionado.getFechaModificacion() : plazoExpSeleccionado.getFechaCreacion());
			expedienteModificado.setUsuUltimaPersistencia(plazoExpSeleccionado.getUsuModificacion()!=null ? plazoExpSeleccionado.getUsuModificacion() : plazoExpSeleccionado.getUsuCreacion());
			expedientesService.guardar(expedienteModificado);
			if(plazoExpDialogo.getExpediente().getId()!=null) {
				FacesContext.getCurrentInstance().addMessage(MENSAJESLISTADOPLAZO, new FacesMessage(FacesMessage.SEVERITY_INFO, "", PLAZO+" "+plazoExpDialogo.getValorTipoPlazo().getDescripcion() +PARAELEXPEDIENTE +mensajesProperties.getString(NUMEROEXPEDIENTE)+": "+plazoExpDialogo.getExpediente().getNumExpediente()+" " +  mensajesProperties.getString(ACTUALIZADOCORRECTAMENTE)));
			}else {
				FacesContext.getCurrentInstance().addMessage(MENSAJESLISTADOPLAZO, new FacesMessage(FacesMessage.SEVERITY_INFO, "", PLAZO+" "+ mensajesProperties.getString(ACTUALIZADOCORRECTAMENTE)));
			}
			
			
			datosExpedientesBean.actualizarCabecera(expedienteModificado,null,null,null);
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
	}
	
	public void eliminarPlazo(PlazosExpdte plazoExpSeleccionado) {
		try {
			plazoExpSeleccionado.setActivo(false);
			plazoExpSeleccionado = plazosExpdteService.guardar(plazoExpSeleccionado);
			
			utilsComun.expedienteUltimaModificacion(plazoExpSeleccionado.getExpediente(),plazoExpSeleccionado.getFechaModificacion(),plazoExpSeleccionado.getFechaCreacion(),plazoExpSeleccionado.getUsuModificacion(),plazoExpSeleccionado.getUsuCreacion());
			if(plazoExpSeleccionado.getExpediente().getId()!=null) {
				FacesContext.getCurrentInstance().addMessage(MENSAJESLISTADOPLAZO, new FacesMessage(FacesMessage.SEVERITY_INFO, "", PLAZO+" "+plazoExpSeleccionado.getValorTipoPlazo().getDescripcion() +PARAELEXPEDIENTE +mensajesProperties.getString(NUMEROEXPEDIENTE)+": "+plazoExpSeleccionado.getExpediente().getNumExpediente()+" " +    mensajesProperties.getString(ACTUALIZADOCORRECTAMENTE)));
			}else {
				FacesContext.getCurrentInstance().addMessage(MENSAJESLISTADOPLAZO, new FacesMessage(FacesMessage.SEVERITY_INFO, "", PLAZO+" "+mensajesProperties.getString(ACTUALIZADOCORRECTAMENTE)));
			}
			
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
	}
	
	public void rehabilitarPlazo(PlazosExpdte plazoExpSeleccionado) {
		try {
			plazoExpSeleccionado.setCumplido(false);
			plazoExpSeleccionado = plazosExpdteService.guardar(plazoExpSeleccionado);
			
			utilsComun.expedienteUltimaModificacion(plazoExpSeleccionado.getExpediente(),plazoExpSeleccionado.getFechaModificacion(),plazoExpSeleccionado.getFechaCreacion(),plazoExpSeleccionado.getUsuModificacion(),plazoExpSeleccionado.getUsuCreacion());
			if(plazoExpSeleccionado.getExpediente().getId()!=null) {
				FacesContext.getCurrentInstance().addMessage(MENSAJESLISTADOPLAZO, new FacesMessage(FacesMessage.SEVERITY_INFO, "", PLAZO+" "+plazoExpSeleccionado.getValorTipoPlazo().getDescripcion() +PARAELEXPEDIENTE +mensajesProperties.getString(NUMEROEXPEDIENTE)+": "+plazoExpSeleccionado.getExpediente().getNumExpediente()+" " + "rehabilitado correctamente"));
			}else {
				FacesContext.getCurrentInstance().addMessage(MENSAJESLISTADOPLAZO, new FacesMessage(FacesMessage.SEVERITY_INFO, "", PLAZO+" "+"rehabilitado correctamente"));
			}
			
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
	}
	
}
