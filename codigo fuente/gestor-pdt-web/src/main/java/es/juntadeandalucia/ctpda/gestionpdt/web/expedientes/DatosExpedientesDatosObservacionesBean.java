package es.juntadeandalucia.ctpda.gestionpdt.web.expedientes;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.ObservacionesExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.ObservacionesExpedientesMaestra;
import es.juntadeandalucia.ctpda.gestionpdt.model.TareasExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.service.ExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ObservacionesExpedientesMaestraService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ObservacionesExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ValoresDominioService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.util.FechaUtils;
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
public class DatosExpedientesDatosObservacionesBean extends BaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String MENSAJESFORMULARIO = "messagesListadoObservaciones";
	private static final String MENSAJEERROR = "error";
	private static final String ACTUALIZADASCORRECTAMENTE = "actualizadas.correctamente";
	private static final String GUARDADASCORRECTAMENTE = "guardadas.correctamente";
	private static final String MENSAJEOBSERVACIONES = "Observaciones del expediente";
	private static final String EDICIONOBSERVACIONEXPEXP = "edicion.observacion.exp.exp";
	private static final String EDICIONOBSERVACIONEXP = "edicion.observacion.exp";
	private static final String CONSULTAOBSERVACIONEXPEXP = "consulta.observacion.exp.exp";
	private static final String CONSULTAOBSERVACIONEXP = "consulta.observacion.exp";
	private static final String CONSULTAOBSEXP = "consulta.obsexp";
	private static final String ALTAOBSERVACIONEXPEXP = "alta.observacion.exp.exp";
	private static final String ALTAOBSERVACIONEXP = "alta.observacion.exp";
	private static final String VERDE = "verde";
	private static final String ROJA = "roja";
	private static final String MARCA = "marca-";
		
	@Autowired
	private ExpedientesService expedientesService;
	
	@Autowired
	private ObservacionesExpedientesMaestraService observacionesExpedientesMaestraService;


	@Getter
	@Setter
	private Expedientes expedientes;
	
	@Getter
	@Setter
	String cabeceraDialogo;
	
	@Getter
	@Setter
	private ObservacionesExpedientesMaestra observacionesExpedientesMaestra;
	
	
	@Getter
	@Setter
	private List<ValoresDominio> listaValoresTiposObservaciones;
	
	
	@Getter
	@Setter
	private ObservacionesExpedientes observacionesExpedientes;

	
	@Getter
	@Setter
	private Boolean modoAccesoEditar;
	
	@Getter
	@Setter
	private Boolean modoAccesoConsulta;
	@Getter
	@Setter
	private List<TareasExpediente> listaTareasExpedientes;

	@Getter
	@Setter
	boolean soloLectura;

	@Getter
	@Setter
	private String origenObsExp;
	
	@Getter
	@Setter
	private List<Long> selectedTipoObservaciones;


	@Getter
	private LazyDataModelByQueryService<ObservacionesExpedientesMaestra> lazyModelObservacionesExpedientesMaestra;


	@Getter
	@Setter
	private Long selectedTipoObservacioIdFiltro;

	@Getter
	@Setter
	private ObservacionesExpedientesMaestra selectedObservacionesExpedientesMaestra;
	
	@Getter
	private List<ValoresDominio> listadoTiposObservacionFiltro;

	@Autowired
	private ValoresDominioService valoresDominioService;
	
	@Autowired
	private ObservacionesExpedientesService observacionesExpedientesService;
	
	@Getter
	private SortMeta sortDefaultLazyObsExp;
	
	@Getter	@Setter
	private Boolean permisoNewExpObs;
	
	@Getter
	@Setter
	private List<String> listaCodigoPermisos;
	


	@Override
	@PostConstruct
	public void init() {
		super.init();		
		
		/**
		 * GESTION DE PERMISOS
		 * */
		listaCodigoPermisos = (List<String>) JsfUtils.getSessionAttribute(Constantes.LISTACODIGOPERMISOS);
		
		permisoNewExpObs = listaCodigoPermisos.contains(Constantes.PERMISO_NEW_EXPOBS);


		listadoTiposObservacionFiltro = valoresDominioService.findValoresDominioActivosByCodigoDominioOrden(Constantes.COD_DOM_TIPOBS);
		
		FacesUtils.setAttribute("editable", JsfUtils.getFlashAttribute("editable"));

		Expedientes expedienteFormulario = (Expedientes) JsfUtils.getSessionAttribute("expedienteFormulario");
		if (expedienteFormulario.getId() != null) {
			expedientes = expedientesService.obtener(expedienteFormulario.getId());
		} else {
			expedientes = new Expedientes();
		}
		
		selectedTipoObservaciones = valoresDominioService.findIdsValDomActivosTramExp(Constantes.COD_DOM_TIPOBS);
		
		
		lazyModelObservacionesExpedientesMaestra = new LazyDataModelByQueryService<>(ObservacionesExpedientesMaestra.class, observacionesExpedientesMaestraService);
		lazyModelObservacionesExpedientesMaestra.setPreproceso((a, b, c, filters) -> {
			
			filters.put("expediente.id", new MyFilterMeta(expedientes.getId()));
			if (selectedTipoObservaciones != null && !selectedTipoObservaciones.isEmpty()) {
				filters.put("#listaIdsTiposObservaciones", new MyFilterMeta(selectedTipoObservaciones));
			}
		});
		
		sortDefaultLazyObsExp = SortMeta.builder().field("fechaEntrada").order(SortOrder.DESCENDING).priority(1).build();
		
		listaValoresTiposObservaciones = valoresDominioService.findValoresTipoObservacionExpediente();
		
		observacionesExpedientes = new ObservacionesExpedientes();
		observacionesExpedientes.setValorTipoObservacion(new ValoresDominio());

	}
	
	

	
	public void limpiarFiltro() {
		selectedTipoObservaciones = valoresDominioService.findIdsValDomActivosTramExp(Constantes.COD_DOM_TIPOBS);
	}
	
	public void accesoRapido(final SelectEvent<ObservacionesExpedientesMaestra> event) {
		modoAccesoConsulta = true;
		modoAccesoEditar = false;
		cabeceraDialogo = mensajesProperties.getString(CONSULTAOBSEXP);
		
		selectedObservacionesExpedientesMaestra = event.getObject();
		observacionesExpedientes = observacionesExpedientesService.obtener(selectedObservacionesExpedientesMaestra.getId());
	}
	
	public void modoAccesoFormulario (ObservacionesExpedientesMaestra observacionesExpedientesSeleccionado, String modoAcceso) {
		if(observacionesExpedientesSeleccionado != null && observacionesExpedientesSeleccionado.getId() != null) {
			observacionesExpedientes = observacionesExpedientesService.obtener(observacionesExpedientesSeleccionado.getId());
			   
		   if(observacionesExpedientes.getTramiteExpdte() != null)
		   {
			   origenObsExp = observacionesExpedientes.getTramiteExpdte().getDescripcion();
		   }else if(observacionesExpedientes.getPlazoExpdte() != null)
		   {
			   origenObsExp = observacionesExpedientes.getPlazoExpdte().getValorTipoPlazo().getDescripcion();
		   }else if(observacionesExpedientes.getTareaExpdte() != null)
		   {
			   origenObsExp = observacionesExpedientes.getTareaExpdte().getDescripcion();
		   }else {
			   origenObsExp = "";
		   }
			   
			}
		
		
		if(modoAcceso.contains("consulta")) {
			modoAccesoConsulta = true;
			if(expedientes.getId()!=null) {
				expedientes = expedientesService.obtener(expedientes.getId());
				cabeceraDialogo = mensajesProperties.getString(CONSULTAOBSERVACIONEXPEXP) + " "+ expedientes.getNumExpediente();
			}else {
				cabeceraDialogo = mensajesProperties.getString(CONSULTAOBSERVACIONEXP);
			}
			
		}else if(modoAcceso.contains("editar")) {
			modoAccesoConsulta = false;
			if(expedientes.getId()!=null) {
				expedientes = expedientesService.obtener(expedientes.getId());
				cabeceraDialogo = mensajesProperties.getString(EDICIONOBSERVACIONEXPEXP) + " "+ expedientes.getNumExpediente();
			}else {
				cabeceraDialogo = mensajesProperties.getString(EDICIONOBSERVACIONEXP);
			}
		}
		
		
				
		PrimeFaces.current().executeScript("PF('dialogObservacionExp').show();");
	}
	
	public void onItemUnselect(UnselectEvent<?> event) {
        /** Método de selección de elementos. **/
    }
	
	
	public void guardarObservacionesExp(String texto, ObservacionesExpedientes obsExp)
	{
		try {
			if(obsExp.getId()!= null)
			{
				observacionesExpedientesService.actualizarObservacionesExpedientes(texto, obsExp);	
				FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "",
								MENSAJEOBSERVACIONES +" "+obsExp.getExpediente().getNumExpediente()+" "+ mensajesProperties.getString(ACTUALIZADASCORRECTAMENTE)));
			}else {
				ObservacionesExpedientes nuevoObsExp = observacionesExpedientesService.guardarObservacionesExpedientes(null, texto, Constantes.COD_VAL_DOM_TIPOBS_EXP, expedientes);
				FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "",
								MENSAJEOBSERVACIONES +" "+nuevoObsExp.getExpediente().getNumExpediente()+" "+ mensajesProperties.getString(GUARDADASCORRECTAMENTE)));
			}
			
			PrimeFaces.current().executeScript("PF('dialogObservacionExp').hide();");
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());		}
	}

	
	public void accesoFormularioAlta () {
		if(expedientes!=null && expedientes.getId() != null) {
			expedientes = expedientesService.obtener(expedientes.getId());
			cabeceraDialogo = mensajesProperties.getString(ALTAOBSERVACIONEXPEXP)+" "+ expedientes.getNumExpediente();
		}else {
			cabeceraDialogo = mensajesProperties.getString(ALTAOBSERVACIONEXP);
		}
		
		reiniciarFormulario();
		observacionesExpedientes.setFechaEntrada(FechaUtils.fechaYHoraActualDate());
		observacionesExpedientes.setValorTipoObservacion(valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_DOM_TIPOBS, Constantes.COD_VAL_DOM_TIPOBS_EXP));
		modoAccesoConsulta = false;
		PrimeFaces.current().executeScript("PF('dialogObservacionExp').show();");
	}
	
	public void reiniciarFormulario () {
		observacionesExpedientes = new ObservacionesExpedientes(); 
		expedientes = (Expedientes) JsfUtils.getSessionAttribute("expedienteFormulario");
		observacionesExpedientes.setExpediente(expedientes);
		ValoresDominio valor = new ValoresDominio();
		observacionesExpedientes.setValorTipoObservacion(valor);
		observacionesExpedientes.setFechaEntrada(null);
		observacionesExpedientes.setTexto("");
	}
	
	public String cssSemaforoSituacion (ObservacionesExpedientesMaestra observacionesExpedientesMaestra) {
		String estiloCss = "";
		
		if(observacionesExpedientesMaestra.getTramiteExpdte() != null) {
			estiloCss = MARCA + (Boolean.TRUE.equals(observacionesExpedientesMaestra.getTramiteExpdte().getFinalizado())? ROJA : VERDE);
		}else if(observacionesExpedientesMaestra.getPlazoExpdte() != null) {
			estiloCss = MARCA + (Boolean.TRUE.equals(observacionesExpedientesMaestra.getPlazoExpdte().getCumplido())? ROJA : VERDE);
		}else if (observacionesExpedientesMaestra.getTareaExpdte() != null) {
			estiloCss = MARCA + (observacionesExpedientesMaestra.getTareaExpdte().getSituacion().equals(TareasExpediente.SITUACION_CERRADA)? ROJA : VERDE);
		}
		
		return estiloCss;
		
	}
	
	public Date fechaFinalizacionSegunOrigen(ObservacionesExpedientesMaestra observacionesExpedientesMaestra) {
		Date fechaFinalizacion = null;		
		if(observacionesExpedientesMaestra.getTramiteExpdte() != null) {
			fechaFinalizacion = observacionesExpedientesMaestra.getTramiteExpdte().getFechaFin();
		}else if(observacionesExpedientesMaestra.getPlazoExpdte() != null) {
			fechaFinalizacion = observacionesExpedientesMaestra.getPlazoExpdte().getFechaLimite();
		}else if (observacionesExpedientesMaestra.getTareaExpdte() != null) {
			fechaFinalizacion = observacionesExpedientesMaestra.getTareaExpdte().getFechaCierre();
		}		
		return fechaFinalizacion;
	}
	
}
