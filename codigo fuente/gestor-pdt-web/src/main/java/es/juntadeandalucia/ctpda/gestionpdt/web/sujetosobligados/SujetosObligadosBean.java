package es.juntadeandalucia.ctpda.gestionpdt.web.sujetosobligados;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.SujetosObligados;
import es.juntadeandalucia.ctpda.gestionpdt.model.TipoAgrupacion;
import es.juntadeandalucia.ctpda.gestionpdt.service.SujetosObligadosService;
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
public class SujetosObligadosBean extends BaseBean implements Serializable {

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	private static final String MENSAJESLISTADO = "messagesListado";
	private static final String MENSAJESUJETOSOBLIGADOS = "Organismos/Sujetos obligados ";
	private static final String MENSAJEERROR = "error";
	private static final String EDITABLE = "editable";
	private static final String ACTUALIZADO = "actualizado.correctamente";

	@Autowired
	private SujetosObligadosService sujetosObligadosService;

	@Getter
	private LazyDataModelByQueryService<SujetosObligados> lazyModel;
	@Getter
	@Setter
	private String descripcionFiltro;
	@Getter
	@Setter
	private Long selectedSujetosObligadosSupIdFiltro;

	@Getter
	@Setter
	private List<SujetosObligados> listaSujetosObligadosSup;
	
	@Autowired
	private TipoAgrupacionService tipoAgrupacionService;

	@Getter
	@Setter
	private TipoAgrupacion tipoAgrupacion;
	@Getter
	@Setter

	private List<TipoAgrupacion> listaTipoAgrupaciones;

	@Getter
	@Setter
	private Long selectedTipoAgrupacionIdFiltro;

	@Getter
	@Setter
	private Boolean activoFiltro;

	@Getter
	@Setter
	private SujetosObligados selectedSujetosObligados;

	@Getter
	@Setter
	private SujetosObligados selectedNuevoSujetoObligadoId;

	@Getter
	@Setter
	private SujetosObligados sujetosObligados;
	
	
	@Getter
	@Setter
	private List<String> listaCodigoPermisos;
	
	@Getter	@Setter
	private Boolean permisoNewSujObl;
	
	@Getter	@Setter
	private Boolean permisoConsSujObl;
	
	@Getter	@Setter
	private Boolean permisoEditSujObl;
	
	@Getter	@Setter
	private Boolean permisoDesacSujObl;
	
	@Getter	@Setter
	private Boolean permisoActSujObl;
	
	@Getter	@Setter
	private Boolean permisoSaveSujObl;
	
	@Getter	@Setter
	private Boolean permisoErroneoSujObl;
	
	@Autowired
	private NavegacionBean navegacionBean;


	/**
	 * Initialize default attributes.
	 * @throws BaseException
	 * @throws BaseFrontException
	 */

	@PostConstruct
	@Override
	public void init() {
		super.init();
		
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, 0);
		
		navegacionBean.setTxtMigaPan(Constantes.LISTADO_SUJETOSOBLIGADOS);
		
		/**
		 * GESTION DE PERMISOS
		 * */
		listaCodigoPermisos = (List<String>) JsfUtils.getSessionAttribute(Constantes.LISTACODIGOPERMISOS);
		
		permisoConsSujObl = listaCodigoPermisos.contains(Constantes.PERMISO_CONS_SUJOBL);
		
		permisoEditSujObl = listaCodigoPermisos.contains(Constantes.PERMISO_EDIT_SUJOBL);
		
		permisoDesacSujObl = listaCodigoPermisos.contains(Constantes.PERMISO_DESAC_SUJOBL);
		
		permisoNewSujObl = listaCodigoPermisos.contains(Constantes.PERMISO_NEW_SUJOBL);
		
		permisoActSujObl =  listaCodigoPermisos.contains(Constantes.PERMISO_ACT_SUJOBL);
		
		permisoErroneoSujObl= listaCodigoPermisos.contains(Constantes.PERMISO_ERRO_SUJOBL);

		lazyModel = new LazyDataModelByQueryService<>(SujetosObligados.class, sujetosObligadosService);

		lazyModel.setPreproceso((a, b, c, filters) -> {

			if (selectedSujetosObligadosSupIdFiltro != null) {
				filters.put("sujetosObligadosPadre",
						new MyFilterMeta(sujetosObligadosService.obtener(selectedSujetosObligadosSupIdFiltro)));
			}
			if (selectedTipoAgrupacionIdFiltro != null) {
				filters.put("tipoAgrupacion",
						new MyFilterMeta(tipoAgrupacionService.obtener(selectedTipoAgrupacionIdFiltro)));
			}

			if (descripcionFiltro != null && !descripcionFiltro.isEmpty()) {
				filters.put("descripcion", new MyFilterMeta(descripcionFiltro));
			}

			if (Boolean.TRUE.equals(this.activoFiltro)) {
				filters.put("activa", new MyFilterMeta(this.activoFiltro));
			}
		});
		

		listaSujetosObligadosSup = sujetosObligadosService.findSujetosObligadosActivos();
		
		
		listaTipoAgrupaciones =  tipoAgrupacionService.findTiposAgrupacionActivas();

		activoFiltro= Boolean.TRUE;


	}


	public String redireccionMenu() {
		init();
		return ListadoNavegaciones.LISTADO_SUJETOS_OBLIGADOS.getRegla();
	}


	public void eliminarSujetosObligados(SujetosObligados sujetosObligadosSeleccionado) {
		try {

			boolean esPadreConSujetosObligadosActivo = esPadreSujetosObligadosActivo(
					sujetosObligadosSeleccionado.getId());

			if (!esPadreConSujetosObligadosActivo) {
				sujetosObligadosSeleccionado.setActiva(false);
				sujetosObligadosService.guardar(sujetosObligadosSeleccionado);
				listaSujetosObligadosSup = sujetosObligadosService.findSujetosObligadosActivos();
				FacesContext.getCurrentInstance().addMessage(MENSAJESLISTADO,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "",
								MENSAJESUJETOSOBLIGADOS +" "+sujetosObligadosSeleccionado.getDescripcion()+" "+ mensajesProperties.getString(ACTUALIZADO)));
			} else {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "El organismo/sujeto obligado " + mensajesProperties.getString("sujetos.obligados.asociados.activos"));
				PrimeFaces.current().dialog().showMessageDynamic(message);
			}

		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
	}

	public void activarSujetosObligados(SujetosObligados sujetosObligadosSeleccionado) {
		try {
			sujetosObligadosSeleccionado.setActiva(true);
			sujetosObligadosService.guardar(sujetosObligadosSeleccionado);
			listaSujetosObligadosSup = sujetosObligadosService.findSujetosObligadosActivos();
			FacesContext.getCurrentInstance().addMessage(MENSAJESLISTADO, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"", MENSAJESUJETOSOBLIGADOS +" "+sujetosObligadosSeleccionado.getDescripcion()+" "+ mensajesProperties.getString(ACTUALIZADO)));
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
	}

	public void limpiarFiltro() {
		selectedSujetosObligadosSupIdFiltro = null;
		descripcionFiltro = "";
		selectedTipoAgrupacionIdFiltro=null;
		activoFiltro= true;
		
	}

	public boolean esPadreSujetosObligadosActivo(Long id) {
		boolean esPadreConSujetosObligadosActivo = false;
		List<SujetosObligados> sujetosObligadosHijos;
		sujetosObligadosHijos = sujetosObligadosService.findSujetosObligadosHijosActivos(id);
		if (!sujetosObligadosHijos.isEmpty())
			esPadreConSujetosObligadosActivo = true;

		return esPadreConSujetosObligadosActivo;
	}
	public String onCrear() {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, ((Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS))+1);
		//No se ha enviado id, la persona viene inicializada por defecto.
		JsfUtils.setFlashAttribute(EDITABLE, true);
		navegacionBean.setTxtMigaPan(Constantes.ALTA_SUJETOOBLIGADO);
		return ListadoNavegaciones.FORM_SUJETOS_OBLIGADOS.getRegla();
	}
	public String onEditar(Long idSujOblig) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, ((Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS))+1);
		JsfUtils.setFlashAttribute(EDITABLE, true);
		JsfUtils.setFlashAttribute("idSujOblig", idSujOblig);
		SujetosObligados sujetosObligadosEdit = sujetosObligadosService.obtener(idSujOblig);
		navegacionBean.setTxtMigaPan(Constantes.EDICION_SUJETOOBLIGADO+sujetosObligadosEdit.getDescripcion());
		return ListadoNavegaciones.FORM_SUJETOS_OBLIGADOS.getRegla();
	}
	
	public String onConsultar(Long idSujOblig) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, ((Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS))+1);
		JsfUtils.setFlashAttribute(EDITABLE, false);
		JsfUtils.setFlashAttribute("idSujOblig", idSujOblig);
		SujetosObligados sujetosObligadosCons = sujetosObligadosService.obtener(idSujOblig);
		navegacionBean.setTxtMigaPan(Constantes.CONSULTA_SUJETOOBLIGADO+sujetosObligadosCons.getDescripcion());
		return ListadoNavegaciones.FORM_SUJETOS_OBLIGADOS.getRegla();
	}
	
	public void onTipAgrupRowDblClkSelect(final SelectEvent<SujetosObligados> event) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, ((Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS))+1);
		JsfUtils.setFlashAttribute(EDITABLE, false);
		SujetosObligados so = event.getObject();
		PrimeFaces.current().ajax().addCallbackParam("id", so.getId());
		navegacionBean.setTxtMigaPan(Constantes.CONSULTA_SUJETOOBLIGADO+so.getDescripcion());
	}
	
	public void marcarErroneoSujetosObligados(SujetosObligados sujetosObligadosSeleccionado) {
		try {						
			sujetosObligadosSeleccionado.setActiva(false);
			sujetosObligadosSeleccionado.setErroneo(true);
			sujetosObligadosService.guardar(sujetosObligadosSeleccionado);
			listaSujetosObligadosSup = sujetosObligadosService.findSujetosObligadosActivos();
			FacesContext.getCurrentInstance().addMessage(MENSAJESLISTADO, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"", MENSAJESUJETOSOBLIGADOS +" "+sujetosObligadosSeleccionado.getDescripcion()+" "+ mensajesProperties.getString(ACTUALIZADO)));			
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
	}

}