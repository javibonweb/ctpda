package es.juntadeandalucia.ctpda.gestionpdt.web.tiposagrupaciones;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.TipoAgrupacion;
import es.juntadeandalucia.ctpda.gestionpdt.service.TipoAgrupacionService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.Constantes;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.JsfUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.LazyDataModelByQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.MyFilterMeta;
import es.juntadeandalucia.ctpda.gestionpdt.web.menu.NavegacionBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.menu.NavegacionBean.ListadoNavegaciones;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.BaseBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@ViewScoped
@Slf4j
public class TipoAgrupacionBean extends BaseBean implements Serializable{

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String MENSAJESLISTADO = "messagesListado";
	private static final String MENSAJETIPOAGRUPACION= "Tipo de agrupación ";
	private static final String MENSAJEELTIPOAGRUPACION= "El tipo de agrupación ";
	private static final String MENSAJEERROR= "error";
	private static final String EDITABLE= "editable";
		
	@Autowired
	private TipoAgrupacionService tipoAgrupacionService;
	
	@Getter
	private LazyDataModelByQueryService<TipoAgrupacion> lazyModel;
	
	@Getter
	@Setter
	private String descripcionFiltro;
	
	@Getter @Setter
	private Long selectedTipoAgrupacionSupIdFiltro;
	
	@Getter @Setter
	private List<TipoAgrupacion> listaTipoAgrupacionesSup;

	@Getter
	@Setter
	private String nivelAnidamientoFiltro;
	
	@Getter
	@Setter
	private Boolean activoFiltro;
	
	@Getter
	@Setter
	private TipoAgrupacion selectedTipoAgrupacion;
		
	@Getter
	@Setter
	private TipoAgrupacion tipoAgrupacion;
	
	
	@Getter
	@Setter
	private List<String> listaCodigoPermisos;
	
	@Getter	@Setter
	private Boolean permisoNewTipAgrup;
	
	@Getter	@Setter
	private Boolean permisoConsTipAgrup;
	
	@Getter	@Setter
	private Boolean permisoEditTipAgrup;
	
	@Getter	@Setter
	private Boolean permisoDesacTipAgrup;
	
	@Getter	@Setter
	private Boolean permisoActTipAgrup;
	
	@Autowired
	private NavegacionBean navegacionBean;
	
	/**
	* Initialize default attributes.
	 * @throws BaseException 
	 * @throws BaseFrontException 
	*/
	@PostConstruct
	@Override
	public void init(){
		super.init();
		
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, 0);
		
		navegacionBean.setTxtMigaPan(Constantes.LISTADO_TIPOSAGRUPACIONES);
		
		/**
		 * GESTION DE PERMISOS
		 * */
		listaCodigoPermisos = (List<String>) JsfUtils.getSessionAttribute(Constantes.LISTACODIGOPERMISOS);
		
		permisoNewTipAgrup = listaCodigoPermisos.contains(Constantes.PERMISO_NEW_TIPAGRUP);
		
		permisoConsTipAgrup = listaCodigoPermisos.contains(Constantes.PERMISO_CONS_TIPAGRUP);
		
		permisoEditTipAgrup = listaCodigoPermisos.contains(Constantes.PERMISO_EDIT_TIPAGRUP);
		
		permisoDesacTipAgrup = listaCodigoPermisos.contains(Constantes.PERMISO_DESAC_TIPAGRUP);
		
		permisoActTipAgrup =  listaCodigoPermisos.contains(Constantes.PERMISO_ACT_TIPAGRUP);
		
		lazyModel= new LazyDataModelByQueryService<>(TipoAgrupacion.class,tipoAgrupacionService);
		
		lazyModel.setPreproceso((a,b,c,filters)->{
			if (descripcionFiltro != null && !descripcionFiltro.isEmpty()){
				filters.put("descripcion", new MyFilterMeta(descripcionFiltro));				
			}
			if (selectedTipoAgrupacionSupIdFiltro != null){
				filters.put("tipoAgrupacionPadre", new MyFilterMeta(tipoAgrupacionService.obtener(selectedTipoAgrupacionSupIdFiltro)));				
			}
			if (nivelAnidamientoFiltro != null && !nivelAnidamientoFiltro.isEmpty()){
				filters.put("nivelAnidamiento", new MyFilterMeta(nivelAnidamientoFiltro));				
			}  
			if (Boolean.TRUE.equals(this.activoFiltro)) {
				filters.put("activa", new MyFilterMeta(this.activoFiltro));
			}
		});

		
		reiniciarFormulario();
		activoFiltro = Boolean.TRUE;

		listaTipoAgrupacionesSup = tipoAgrupacionService.findTiposAgrupacionActivas();
	}
	
	public void reiniciarFormulario () {
		tipoAgrupacion = new TipoAgrupacion();
		tipoAgrupacion.setDescripcion("");
		tipoAgrupacion.setTipoAgrupacionPadre(null);
		tipoAgrupacion.setNivelAnidamiento(null);
		tipoAgrupacion.setActiva(true);
		listaTipoAgrupacionesSup = tipoAgrupacionService.findTiposAgrupacionActivas();
		PrimeFaces.current().ajax().update("formListadoTiposAgrupaciones");
		
	}

		
	public String redireccionMenu() {
		init();
		return ListadoNavegaciones.LISTADO_TIPOS_AGRUPACION.getRegla();
	}		
	
	
	public void eliminarTipoAgrupacion(TipoAgrupacion tipoAgrupacionSeleccionado) {
		try {
			
			boolean esPadreConTipAgrupActiva = esPadreTiposAgrupacionActiva(tipoAgrupacionSeleccionado.getId());
				
			if(!esPadreConTipAgrupActiva)
			{
				tipoAgrupacionSeleccionado.setActiva(false);
				tipoAgrupacionService.guardar(tipoAgrupacionSeleccionado);
				listaTipoAgrupacionesSup = tipoAgrupacionService.findTiposAgrupacionActivas();
				FacesContext.getCurrentInstance().addMessage(MENSAJESLISTADO, new FacesMessage(FacesMessage.SEVERITY_INFO, "", MENSAJETIPOAGRUPACION+" "+tipoAgrupacionSeleccionado.getDescripcion()+" "+mensajesProperties.getString("actualizado.correctamente")));
			}
			else
			{
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", MENSAJEELTIPOAGRUPACION+mensajesProperties.getString("tipos.agrupacion.asociados.activos"));
				PrimeFaces.current().dialog().showMessageDynamic(message);
			}
	
			
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
	}
	
	public void activarTipoAgrupacion(TipoAgrupacion tipoAgrupacionSeleccionado) {
		try {
			tipoAgrupacionSeleccionado.setActiva(true);
			tipoAgrupacionService.guardar(tipoAgrupacionSeleccionado);
			listaTipoAgrupacionesSup = tipoAgrupacionService.findTiposAgrupacionActivas();
			FacesContext.getCurrentInstance().addMessage(MENSAJESLISTADO, new FacesMessage(FacesMessage.SEVERITY_INFO, "", MENSAJETIPOAGRUPACION+" "+tipoAgrupacionSeleccionado.getDescripcion()+" "+mensajesProperties.getString("actualizado.correctamente")));
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
	}	
	
	public void limpiarFiltro() {
		selectedTipoAgrupacionSupIdFiltro =null;
		descripcionFiltro = "";
		nivelAnidamientoFiltro ="";
		activoFiltro = true;
	}
	
	public boolean esPadreTiposAgrupacionActiva(Long id)
	{
		boolean esPadreConTipAgrupActiva = false;
		List<TipoAgrupacion> tiposAgrupacionesHijas;
		tiposAgrupacionesHijas = tipoAgrupacionService.findTiposAgrupacionesHijasActivas(id);
		if(!tiposAgrupacionesHijas.isEmpty())
			esPadreConTipAgrupActiva = true;

		return esPadreConTipAgrupActiva;
	}
	

	
	public List<TipoAgrupacion> obtenerTipoAgrupacionesAscendentes(TipoAgrupacion tipoAgrupacionSeleccionado)
	{
		List<TipoAgrupacion> listaTipAgrupAscendentes = new ArrayList<>();
		TipoAgrupacion tipoAgrupacionPadre = null;
		TipoAgrupacion tipAgrupacionHija = tipoAgrupacionSeleccionado;
		
		tipoAgrupacionPadre = tipAgrupacionHija.getTipoAgrupacionPadre();
		
		while(tipoAgrupacionPadre != null && tipoAgrupacionPadre.getActiva())
		{
			listaTipAgrupAscendentes.add(tipoAgrupacionPadre);
			tipoAgrupacionPadre = tipoAgrupacionPadre.getTipoAgrupacionPadre();
		}
		
		return listaTipAgrupAscendentes;
	}

	
	
	public String onCrear() {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, ((Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS))+1);
		JsfUtils.setFlashAttribute(EDITABLE, true);
		navegacionBean.setTxtMigaPan(Constantes.ALTA_TIPOAGRUPACION);
		return ListadoNavegaciones.FORM_TIPOS_AGRUPACION.getRegla();
	}

	
	public String onEditar(Long idTipAgrup) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, ((Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS))+1);
		
		JsfUtils.setFlashAttribute("idTipAgrup", idTipAgrup);
		JsfUtils.setFlashAttribute(EDITABLE, true);
		TipoAgrupacion tipoAgrupacionEdit = tipoAgrupacionService.obtener(idTipAgrup);
		navegacionBean.setTxtMigaPan(Constantes.EDICION_TIPOAGRUPACION+tipoAgrupacionEdit.getDescripcion());
		return ListadoNavegaciones.FORM_TIPOS_AGRUPACION.getRegla();
	}
	
	public String onConsultar(Long idTipAgrup) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, ((Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS))+1);
		
		JsfUtils.setFlashAttribute("idTipAgrup", idTipAgrup);
		JsfUtils.setFlashAttribute(EDITABLE, false);
		TipoAgrupacion tipoAgrupacionCons = tipoAgrupacionService.obtener(idTipAgrup);
		navegacionBean.setTxtMigaPan(Constantes.CONSULTA_TIPOAGRUPACION+tipoAgrupacionCons.getDescripcion());
		return ListadoNavegaciones.FORM_TIPOS_AGRUPACION.getRegla();
	}
	
	public void onTipAgrupRowDblClkSelect(final SelectEvent<TipoAgrupacion> event) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, ((Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS))+1);
		
		TipoAgrupacion ta = event.getObject();
		JsfUtils.setFlashAttribute(EDITABLE, false);
		PrimeFaces.current().ajax().addCallbackParam("id", ta.getId());
		navegacionBean.setTxtMigaPan(Constantes.CONSULTA_TIPOAGRUPACION+ta.getDescripcion());
	}
	
}
	


