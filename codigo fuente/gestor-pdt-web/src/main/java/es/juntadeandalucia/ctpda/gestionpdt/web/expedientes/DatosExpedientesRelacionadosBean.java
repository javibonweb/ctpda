package es.juntadeandalucia.ctpda.gestionpdt.web.expedientes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.apache.commons.lang3.BooleanUtils;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.ExpedientesRelacion;
import es.juntadeandalucia.ctpda.gestionpdt.model.Usuario;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.service.ExpedientesRelacionService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ValoresDominioService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.util.FechaUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.Constantes;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.JsfUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.LazyDataModelByQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.MyFilterMeta;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.BaseBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.FacesUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.UtilsComun;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@ViewScoped
public class DatosExpedientesRelacionadosBean extends BaseBean implements Serializable {

	private static final String MENSAJESFORMULARIO = "messagesFormulario";
	private static final String MENSAJEEXPEDIENTE = "Expediente ";
	private static final String ACTUALIZADOCORRECTAMENTE = "actualizada.correctamente";
	private static final String EDITABLE = "editable";
	public static final String VOLVERFORMEXPRELACIONADOS = "_volverExpRelacionados_";

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private Expedientes expedientes;
	@Getter
	@Setter
	private ExpedientesRelacion expedientesRelacion;
	@Getter
	@Setter
	private ExpedientesRelacion expedientesOrigen;

	@Getter
	@Setter
	private Expedientes expedienteRelacionado;
	@Getter
	@Setter
	private Expedientes nuevoExpedienteRelacionable;

	@Autowired
	private ExpedientesRelacionService expedientesRelacionService;
	@Autowired
	private ValoresDominioService valoresDominioService;

	@Getter
	@Setter
	private Long selectedMotivoRelacionId;

	@Getter
	@Setter
	boolean soloLectura;

	@Getter
	@Setter
	private String numeroExpedienteFiltro;
	
	@Getter
	@Setter
	private String nombreExpedienteFiltro;

	@Getter
	@Setter
	private Date fechaEntradaFiltro;

	@Getter
	@Setter
	private List<ValoresDominio> listaValoresMotivosRelacionExpediente;
	
	@Getter
	@Setter
	private String cabeceraDialogoMotivoRelacion;
	
	@Getter
	private LazyDataModelByQueryService<ExpedientesRelacion> lazyModelExpedientesRelacion;
	
	@Getter
	private LazyDataModelByQueryService<Expedientes> lazyModelExpedientesRelacionables;
	
	@Autowired
	private ExpedientesService expedientesService;
	
	@Getter
	private LazyDataModelByQueryService<ExpedientesRelacion> lazyModelExpedientesOrigen;
	
	@Getter
	@Setter
	private List<String> listaCodigoPermisos;
	
	@Getter	@Setter
	private Boolean permisoDelExpRel;
	
	@Getter	@Setter
	private Boolean permisoEditExpRel;
	
	@Getter	@Setter
	private Boolean permisoBusExpRel;
	
	@Autowired
	private DatosExpedientesBean datosExpedientesBean;
	
	@Autowired
	private UtilsComun utilsComun;
	@Getter @Setter
	private String cabeceraDialog;
	
	@Getter @Setter
	private List<Long> listIdsExpedientesRelacionados;
	
	@Getter	@Setter
	private Boolean permisoConsExp;
	
	@PostConstruct
	@Override
	public void init() {
		super.init();
		
		/**
		 * GESTION DE PERMISOS
		 * */
		listaCodigoPermisos = (List<String>) JsfUtils.getSessionAttribute(Constantes.LISTACODIGOPERMISOS);
		
		permisoDelExpRel = listaCodigoPermisos.contains(Constantes.PERMISO_DEL_EXPREL);
		
		permisoEditExpRel = listaCodigoPermisos.contains(Constantes.PERMISO_EDIT_EXPREL);
		
		permisoBusExpRel = listaCodigoPermisos.contains(Constantes.PERMISO_BUS_EXPREL);
		
		permisoConsExp = listaCodigoPermisos.contains(Constantes.PERMISO_CONS_EXP);
		
		expedientes = (Expedientes) JsfUtils.getSessionAttribute("expedienteFormulario");
		
		listIdsExpedientesRelacionados = new ArrayList<>();
		listIdsExpedientesRelacionados = expedientesRelacionService.findIdExpRelacionadosByExpOrigen(expedientes.getId());
		
		lazyModelExpedientesRelacionables = new LazyDataModelByQueryService<>(Expedientes.class,expedientesService);
		lazyModelExpedientesRelacionables.setPreproceso((a, b, c, filters) -> {
			if (numeroExpedienteFiltro != null && !numeroExpedienteFiltro.isEmpty()){
				filters.put("numExpediente", new MyFilterMeta(numeroExpedienteFiltro));				
			}
			if (fechaEntradaFiltro != null) {
				filters.put("fechaEntrada", new MyFilterMeta(fechaEntradaFiltro));
			}
			if (nombreExpedienteFiltro != null && !nombreExpedienteFiltro.isEmpty()){
				filters.put("nombreExpediente", new MyFilterMeta(nombreExpedienteFiltro));				
			}
			
			if(!listIdsExpedientesRelacionados.isEmpty()) {
				filters.put("#notIDExpRelacionados", new MyFilterMeta(listIdsExpedientesRelacionados));
			}	
			filters.put("#notID", new MyFilterMeta(expedientes.getId()));
		});
		
		if(expedientes.getId() != null) {
			
			Boolean situacionEditable = Boolean.TRUE;
			FacesUtils.setAttribute(EDITABLE, BooleanUtils.toBoolean((Boolean)JsfUtils.getFlashAttribute(EDITABLE)) && situacionEditable );

			lazyModelExpedientesRelacion = new LazyDataModelByQueryService<>(ExpedientesRelacion.class,expedientesRelacionService);
			lazyModelExpedientesRelacion.setPreproceso((a, b, c, filters) ->
				filters.put("expedienteOrigen.id", new MyFilterMeta(expedientes.getId()))				
			);
			
			lazyModelExpedientesOrigen = new LazyDataModelByQueryService<>(ExpedientesRelacion.class,expedientesRelacionService);
			lazyModelExpedientesOrigen.setPreproceso((a, b, c, filters) ->
				filters.put("expedienteRelacionado.id", new MyFilterMeta(expedientes.getId()))
			);

			listaValoresMotivosRelacionExpediente = valoresDominioService.findValoresMotivosRelacionExpediente();
			
			cabeceraDialogoMotivoRelacion = "";
			cabeceraDialog="";
		}		
	}
		
	public void abrirBusquedaRelacionables() {
		limpiarFiltro();
		if(expedientes!=null) {
			cabeceraDialog= "Buscar expediente relacionado para el expediente "+ expedientes.getNumExpediente();
		}else {
			cabeceraDialog= "Buscar expediente relacionado para el expediente";
		}
		
		PrimeFaces.current().ajax().update("dialogBuscarRelacionados");
		PrimeFaces.current().executeScript("PF('dialogBuscarRelacionados').show();");
	}
    
	public void abrirAsignarMotRela(ExpedientesRelacion expedientesRelacionSeleccionado, Expedientes expedienteRelacionable, String accion) {

		soloLectura = false;

		if ("asignarMotRela".equals(accion)) {
			cabeceraDialogoMotivoRelacion = "Seleccionar motivo de relación";

		} else if ("editarExpRel".equals(accion)) {
			cabeceraDialogoMotivoRelacion = "Editar motivo de relación";
		}
		
		if (expedientesRelacionSeleccionado != null) {			
			expedientesRelacion = expedientesRelacionSeleccionado;	
			this.selectedMotivoRelacionId = expedientesRelacionSeleccionado.getMotivo().getId();
		}else {
			this.selectedMotivoRelacionId = null;
			expedientesRelacion = new ExpedientesRelacion();
		}
		
		nuevoExpedienteRelacionable = expedienteRelacionable;

		PrimeFaces.current().executeScript("PF('dialogAsignarMotRelaExp').show();");
	}

	public void limpiarFiltro() {
		numeroExpedienteFiltro="";
		nombreExpedienteFiltro = "";
		fechaEntradaFiltro=null;
	}

	public boolean validacionesGuardar() {
		boolean valido = true;

		if (this.selectedMotivoRelacionId == null) {
			valido = false;
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString("campos.obligatorios"));
			PrimeFaces.current().dialog().showMessageDynamic(message);
		}
		return valido;
	}

	public void asignarMotRelaExpedienteRelacion() throws BaseException {
		boolean puedoGuardar = validacionesGuardar();
		if (puedoGuardar) {
			ValoresDominio motivo = valoresDominioService.obtener(this.selectedMotivoRelacionId);
			if(cabeceraDialogoMotivoRelacion.contains("Editar") && selectedMotivoRelacionId != null) {
				// opcion editar expediente relacion
				expedientesRelacion.setMotivo(motivo);
				expedientesRelacion = expedientesRelacionService.guardar(expedientesRelacion);
			}else {
				//opcion guardar nuevo expediente relacion
				expedientesRelacion = new ExpedientesRelacion();
				expedientesRelacion.setExpedienteOrigen(expedientes);
				expedientesRelacion.setExpedienteRelacionado(nuevoExpedienteRelacionable);
				expedientesRelacion.setMotivo(motivo);
				expedientesRelacion = expedientesRelacionService.guardar(expedientesRelacion);				
			}		
			
			expedientes = utilsComun.expedienteUltimaModificacion(expedientes,expedientesRelacion.getFechaModificacion(),expedientesRelacion.getFechaCreacion(),expedientesRelacion.getUsuModificacion(),expedientesRelacion.getUsuCreacion());
			
			FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, new FacesMessage(FacesMessage.SEVERITY_INFO, "",MENSAJEEXPEDIENTE + mensajesProperties.getString(ACTUALIZADOCORRECTAMENTE)));

			limpiarAsignarMotRelaExpediente();
			
			datosExpedientesBean.actualizarCabecera(expedientes,null,null,null);
			
			listIdsExpedientesRelacionados = expedientesRelacionService.findIdExpRelacionadosByExpOrigen(expedientes.getId());
		}
	}
	
	public void eliminarRelacion(ExpedientesRelacion rel) throws BaseException {
		this.expedientesRelacionService.delete(rel.getId());
		FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, new FacesMessage(FacesMessage.SEVERITY_INFO, "",MENSAJEEXPEDIENTE + mensajesProperties.getString(ACTUALIZADOCORRECTAMENTE)));
		
		Usuario usuarioLogado = (Usuario) JsfUtils.getSessionAttribute(Constantes.USUARIO);
		expedientes = utilsComun.expedienteUltimaModificacion(expedientes,FechaUtils.fechaYHoraActualDate(),FechaUtils.fechaYHoraActualDate(),usuarioLogado.getLogin(),usuarioLogado.getLogin());

		datosExpedientesBean.actualizarCabecera(expedientes,null,null,null);
		
		listIdsExpedientesRelacionados = expedientesRelacionService.findIdExpRelacionadosByExpOrigen(expedientes.getId());
	}

	private void limpiarAsignarMotRelaExpediente() {
		this.selectedMotivoRelacionId = null;
		PrimeFaces.current().executeScript("PF('dialogAsignarMotRelaExp').hide();");
		PrimeFaces.current().executeScript("PF('dialogBuscarRelacionados').hide();");
	}

}
