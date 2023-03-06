package es.juntadeandalucia.ctpda.gestionpdt.web.parametros;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.FilterMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.Parametro;
import es.juntadeandalucia.ctpda.gestionpdt.model.QParametro;
import es.juntadeandalucia.ctpda.gestionpdt.service.ParametroService;
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
public class ParametroBean extends BaseBean implements Serializable{

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String MENSAJESLISTADO = "messagesListado";
	private static final String MENSAJEPARAMETRO= "Parámetro ";
	private static final String MENSAJEERROR= "error";
	private static final String ACTUALIZADOCORRECTAMENTE = "actualizado.correctamente";
		
	@Autowired
	private ParametroService parametroService;
	
	@Getter
	private LazyDataModelByQueryService<Parametro> lazyModel;
	
	@Getter
	@Setter
	private Parametro selectedParametro;
		
	@Getter
	@Setter
	private Parametro parametro;
	
	@Getter
	@Setter
	private Boolean modoAccesoEditar;
	
	@Getter
	@Setter
	private Boolean modoAccesoConsulta;
	
	@Getter
	@Setter
	private String cabeceraDialogo;
	
	@Getter
	@Setter
	private String campoFiltro;
	
	@Getter
	@Setter
	private String valorFiltro;
	
	@Getter
	@Setter
	private String descripcionFiltro;
	
	@Getter
	@Setter
	private Boolean activoFiltro;
	
	@Getter
	@Setter
	private List<String> listaCodigoPermisos;
	
	@Getter	@Setter
	private Boolean permisoNewParam;
	
	@Getter	@Setter
	private Boolean permisoConsParam;
	
	@Getter	@Setter
	private Boolean permisoEditParam;
	
	@Getter	@Setter
	private Boolean permisoDesacParam;
	
	@Getter	@Setter
	private Boolean permisoActParam;
	
	@Getter	@Setter
	private Boolean permisoSaveParam;
	
	@Autowired
	private NavegacionBean navegacionBean;
	
	/**
	* Initialize default attributes.
	 * @throws BaseFrontException 
	*/
	@PostConstruct
	@Override
	public void init() {
		super.init();
		
		navegacionBean.setTxtMigaPan(Constantes.LISTADO_PARAMETROS);
		
		listaCodigoPermisos = (List<String>) JsfUtils.getSessionAttribute(Constantes.LISTACODIGOPERMISOS);
		
		permisoNewParam = listaCodigoPermisos.contains(Constantes.PERMISO_NEW_PARAM);
		
		permisoConsParam = listaCodigoPermisos.contains(Constantes.PERMISO_CONS_PARAM);
		
		permisoEditParam = listaCodigoPermisos.contains(Constantes.PERMISO_EDIT_PARAM);
		
		permisoDesacParam = listaCodigoPermisos.contains(Constantes.PERMISO_DESAC_PARAM);
				
		permisoActParam = listaCodigoPermisos.contains(Constantes.PERMISO_ACT_PARAM);
		
		permisoSaveParam = listaCodigoPermisos.contains(Constantes.PERMISO_SAVE_PARAM);
		
		
		lazyModel= new LazyDataModelByQueryService<>(Parametro.class,parametroService);
		lazyModel.setPreproceso((a,b,c,filters)->
			filtrosLazyModel(filters)
		);
				
		modoAccesoConsulta = false;
		modoAccesoEditar = false;
		
		reiniciarFormulario();

		activoFiltro = Boolean.TRUE;
		
		cabeceraDialogo = "";
		
	}
	
	private void filtrosLazyModel (Map<String, FilterMeta> filters ) {
		if (campoFiltro != null && !campoFiltro.isEmpty()){
			filters.put("campo", new MyFilterMeta(campoFiltro));				
		}
		if (valorFiltro != null && !valorFiltro.isEmpty()){
			filters.put("valor", new MyFilterMeta(valorFiltro));				
		}  
		if (descripcionFiltro != null && !descripcionFiltro.isEmpty()){
			filters.put("descripcion", new MyFilterMeta(descripcionFiltro));				
		} 
		if (Boolean.TRUE.equals(this.activoFiltro)) {
			filters.put("activa", new MyFilterMeta(this.activoFiltro));
		}
	}
	
	public String redireccionMenu() {
		init();
		return ListadoNavegaciones.LISTADO_PARAMETROS.getRegla();
	}
	
	public void reiniciarFormulario () {
		parametro = new Parametro();
		parametro.setCampo("");
		parametro.setValor("");
		parametro.setDescripcion("");
	}
	
	public void accesoFormularioAlta () {
		cabeceraDialogo = "Alta de parámetro";
		reiniciarFormulario();
		PrimeFaces.current().executeScript("PF('dialogParametro').show();");
	}

	public boolean validacionesGuardar () {
		boolean validoGuardar = true;
		
		if((parametro.getCampo() == null || parametro.getCampo().isEmpty())
				|| (parametro.getValor() == null || parametro.getValor().isEmpty())
				|| (parametro.getDescripcion() == null || parametro.getDescripcion().isEmpty())){
			validoGuardar = false;
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString("campos.obligatorios"));
			PrimeFaces.current().dialog().showMessageDynamic(message);
		}else if(parametro.getCampo() != null || !parametro.getCampo().isEmpty()) {
			BooleanBuilder bb= new BooleanBuilder();
			bb.and(QParametro.parametro.campo.eq(parametro.getCampo()));
			List<Parametro> parametrosByCampo = (List<Parametro>) parametroService.findAllRepository(bb,Sort.unsorted());
			if(!parametrosByCampo.isEmpty() && (
				(parametro.getId() == null) || 
				(parametro.getId() != null && !parametrosByCampo.get(0).getId().equals(parametro.getId()))
			)) { //esto va a traer siempre un sólo elemento porque campo es unico
				validoGuardar = false;
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString("el.campo")+" Campo "+mensajesProperties.getString("existe.sistema"));
				PrimeFaces.current().dialog().showMessageDynamic(message);
			}
		}
		return validoGuardar;
	}
	
	public void guardarParametro() {
		boolean puedoGuardar = validacionesGuardar();
		try {
			if(puedoGuardar) {
				//guardado del parametro
				if(parametro.getId() == null) { 						
						parametro.setActiva(true);
						parametroService.guardar(parametro);
						FacesContext.getCurrentInstance().addMessage(MENSAJESLISTADO, new FacesMessage(FacesMessage.SEVERITY_INFO, "", MENSAJEPARAMETRO+" "+parametro.getCampo()+" "+mensajesProperties.getString("guardado.correctamente")));
						reiniciarFormulario();
						PrimeFaces.current().executeScript("PF('dialogParametro').hide()");											
				}else if(parametro.getId() != null) {					
					parametroService.guardar(parametro);
					FacesContext.getCurrentInstance().addMessage(MENSAJESLISTADO, new FacesMessage(FacesMessage.SEVERITY_INFO, "", MENSAJEPARAMETRO+" "+parametro.getCampo()+" "+mensajesProperties.getString(ACTUALIZADOCORRECTAMENTE)));
					reiniciarFormulario();
					PrimeFaces.current().executeScript("PF('dialogParametro').hide()");
					
				}
			}
		}catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}	
	}	
	
	public void modoAccesoFormulario (Parametro parametroSeleccionado, String modoAcceso) {
		if(modoAcceso.contains("consulta")) {
			modoAccesoConsulta = true;
			modoAccesoEditar = false;
			cabeceraDialogo = "Consulta de parámetro";
		}else if(modoAcceso.contains("editar")) {
			modoAccesoEditar = true;
			modoAccesoConsulta = false;
			cabeceraDialogo = "Edición de parámetro";
		}
		
		if(parametroSeleccionado != null && parametroSeleccionado.getId() != null) {
			parametro = parametroSeleccionado;
			parametro.setCampo(parametroSeleccionado.getCampo());
			parametro.setValor(parametroSeleccionado.getValor());
			parametro.setDescripcion(parametroSeleccionado.getDescripcion());
		}
				
		PrimeFaces.current().executeScript("PF('dialogParametro').show();");
	}
	
	public void eliminarParametro(Parametro parametroSeleccionado) {
		try {			
			parametroSeleccionado.setActiva(false);
			parametroService.guardar(parametroSeleccionado);
			FacesContext.getCurrentInstance().addMessage(MENSAJESLISTADO, new FacesMessage(FacesMessage.SEVERITY_INFO, "", MENSAJEPARAMETRO+" "+parametroSeleccionado.getCampo()+" "+mensajesProperties.getString(ACTUALIZADOCORRECTAMENTE)));
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
	}
	
	public void activarParametro(Parametro parametroSeleccionado) {
		try {			
			parametroSeleccionado.setActiva(true);
			parametroService.guardar(parametroSeleccionado);	
			FacesContext.getCurrentInstance().addMessage(MENSAJESLISTADO, new FacesMessage(FacesMessage.SEVERITY_INFO, "", MENSAJEPARAMETRO+" "+parametroSeleccionado.getCampo()+" "+mensajesProperties.getString(ACTUALIZADOCORRECTAMENTE)));
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
	}
	
	public void limpiarFiltro() {
		campoFiltro = "";
		valorFiltro = "";
		descripcionFiltro = "";
		activoFiltro = true;
	}

	public void accesoRapido(final SelectEvent<Parametro> event) {
		modoAccesoConsulta = true;
		modoAccesoEditar = false;
		cabeceraDialogo = "Consulta de parámetro";
		
		selectedParametro = event.getObject();
		selectedParametro = parametroService.obtener(selectedParametro.getId());
		parametro = selectedParametro;
	}
	
}
