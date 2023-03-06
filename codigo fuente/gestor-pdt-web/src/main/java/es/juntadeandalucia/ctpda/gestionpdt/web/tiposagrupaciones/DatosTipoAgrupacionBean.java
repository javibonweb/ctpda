package es.juntadeandalucia.ctpda.gestionpdt.web.tiposagrupaciones;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.QTipoAgrupacion;
import es.juntadeandalucia.ctpda.gestionpdt.model.TipoAgrupacion;
import es.juntadeandalucia.ctpda.gestionpdt.service.TipoAgrupacionService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.Constantes;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.JsfUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.menu.NavegacionBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.menu.NavegacionBean.ListadoNavegaciones;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.BaseBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.FacesUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@ViewScoped
@Slf4j
public class DatosTipoAgrupacionBean extends BaseBean implements Serializable{

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String MENSAJESFORMULARIO = "messagesFormulario";
	private static final String MENSAJETIPOAGRUPACION= "Tipo de agrupación ";
	private static final String MENSAJEERROR= "error";
	private static final String ACTUALIZADOCORRECTAMENTE = "actualizado.correctamente";
	private static final String GUARDADOCORRECTAMENTE = "guardado.correctamente";
	private static final String EDITABLE= "editable";
		
	@Autowired
	private TipoAgrupacionService tipoAgrupacionService;
		
	@Getter @Setter
	private List<TipoAgrupacion> listaTipoAgrupacionesSup;
	
	@Getter @Setter
	private List<TipoAgrupacion> listaTipoAgrupacionesAscendentes;

	@Getter
	@Setter
	private Long selectedNuevoTipoAgrupacionId;
		
	@Getter
	@Setter
	private TipoAgrupacion tipoAgrupacion;
	
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
	private List<String> listaCodigoPermisos;
	
	@Getter	@Setter
	private Boolean permisoSaveTipAgrup;
	
	@Getter	@Setter
	private Boolean permisoEditTipAgrup;

	@Autowired
	private NavegacionBean navegacionBean;
	
	@Getter @Setter
	private Integer numerosSaltos;
	
	/**
	* Initialize default attributes.
	 * @throws BaseException 
	 * @throws BaseFrontException 
	*/
	@PostConstruct
	@Override
	public void init(){
		super.init();
		
		numerosSaltos = (Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS);
		
		/**
		 * GESTION DE PERMISOS
		 * */
		listaCodigoPermisos = (List<String>) JsfUtils.getSessionAttribute(Constantes.LISTACODIGOPERMISOS);
		
		permisoSaveTipAgrup = listaCodigoPermisos.contains(Constantes.PERMISO_SAVE_TIPAGRUP);
		permisoEditTipAgrup = listaCodigoPermisos.contains(Constantes.PERMISO_EDIT_TIPAGRUP);
		
		Long idTipAgrup = (Long) JsfUtils.getFlashAttribute("idTipAgrup");
		
		FacesUtils.setAttribute(EDITABLE, JsfUtils.getFlashAttribute(EDITABLE));
		
		TipoAgrupacion tipAgrupPadre = null; 
		
		if (idTipAgrup != null)
		{
			tipoAgrupacion = tipoAgrupacionService.obtener(idTipAgrup);
			tipAgrupPadre = tipoAgrupacion.getTipoAgrupacionPadre();
			
			if(tipAgrupPadre != null)
				selectedNuevoTipoAgrupacionId = tipAgrupPadre.getId();

			listaTipoAgrupacionesAscendentes = obtenerTipoAgrupacionesAscendentes(tipoAgrupacion);
		}
		else
		{
			listaTipoAgrupacionesAscendentes = null;
			tipoAgrupacion = new TipoAgrupacion();
		}
		
		modoAccesoConsulta = false;		
		
		listaTipoAgrupacionesSup = tipoAgrupacionService.findTiposAgrupacionActivas();
		
	}
	

	public boolean validacionesGuardar () {
		boolean validoGuardar = true;
		String descripcion = tipoAgrupacion.getDescripcion();
		
		//VALIDACION OBLIGATORIEDAD
		if((descripcion == null || descripcion.isEmpty())){
			validoGuardar = false;
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", MENSAJETIPOAGRUPACION + mensajesProperties.getString("campos.obligatorios"));
			PrimeFaces.current().dialog().showMessageDynamic(message);
		}
		
		//OTRAS Consultar si existe un tipo de agrupación con la descripción que intentamos guardar o editar
		if(tipoAgrupacion.getDescripcion() != null || !tipoAgrupacion.getDescripcion().isEmpty()) 
		{
			BooleanBuilder bb= new BooleanBuilder();
			bb.and(QTipoAgrupacion.tipoAgrupacion.descripcion.eq(tipoAgrupacion.getDescripcion()));
			List<TipoAgrupacion> agrupacionesByDescripcion = (List<TipoAgrupacion>) tipoAgrupacionService.findAllRepository(bb,Sort.unsorted());
			if(!agrupacionesByDescripcion.isEmpty() && (
					(tipoAgrupacion.getId() == null) ||
					(tipoAgrupacion.getId() != null && !agrupacionesByDescripcion.get(0).getId().equals(tipoAgrupacion.getId()))
					)) { //esto va a traer siempre un sólo elemento porque descripcion es unica
				validoGuardar = false;
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString("el.campo") + " "  +mensajesProperties.getString("descripcion")+ " "  +mensajesProperties.getString("existe.sistema"));
				PrimeFaces.current().dialog().showMessageDynamic(message);
			}			
		}
		

		return validoGuardar;
	}
		
	public String redireccionMenu() {
		init();
		return ListadoNavegaciones.LISTADO_TIPOS_AGRUPACION.getRegla();
	}		
	
	public void guardarTipoAgrupacion() {
		boolean puedoGuardar = validacionesGuardar();
		TipoAgrupacion tipAgrupPadre = null;
		try {
			if(puedoGuardar) {
				//ALTA DE TIPO DE AGRUPACION
				if(tipoAgrupacion.getId() == null) { 					
					tipoAgrupacion.setActiva(true);

					FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, new FacesMessage(FacesMessage.SEVERITY_INFO, "", MENSAJETIPOAGRUPACION+" "+tipoAgrupacion.getDescripcion()+" "+mensajesProperties.getString(GUARDADOCORRECTAMENTE)));
				
				//MODIFICACION DEL TIPO DE AGRUPACION
				}else if(tipoAgrupacion.getId() != null) {
					
					FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, new FacesMessage(FacesMessage.SEVERITY_INFO, "", MENSAJETIPOAGRUPACION+" "+tipoAgrupacion.getDescripcion()+" "+mensajesProperties.getString(ACTUALIZADOCORRECTAMENTE)));
				}
				
				if(selectedNuevoTipoAgrupacionId != null){
					tipAgrupPadre = tipoAgrupacionService.obtener(selectedNuevoTipoAgrupacionId);
				}else {
					tipoAgrupacion.setNivelAnidamiento(0L);
				}
				
				tipoAgrupacion.setTipoAgrupacionPadre(tipAgrupPadre);
				tipoAgrupacionService.guardar(tipoAgrupacion);
				
				listaTipoAgrupacionesAscendentes = obtenerTipoAgrupacionesAscendentes(tipoAgrupacion);
				listaTipoAgrupacionesSup = tipoAgrupacionService.findTiposAgrupacionActivas();
				modoAccesoConsulta = true;
				PrimeFaces.current().ajax().update("formListadoTiposAgrupaciones");
				
				navegacionBean.setTxtMigaPan(Constantes.EDICION_TIPOAGRUPACION+tipoAgrupacion.getDescripcion());
				PrimeFaces.current().ajax().update("textoMigaPan");
			}
		}catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}	
	}	
	
	public void modoAccesoFormulario (TipoAgrupacion tipoAgrupacionSeleccionado, String modoAcceso) {
		if(modoAcceso.contains("consulta")) {
			modoAccesoConsulta = true;
			modoAccesoEditar = false;
			cabeceraDialogo = "Consulta tipo de agrupación";
		}else if(modoAcceso.contains("editar")) {
			modoAccesoEditar = true;
			modoAccesoConsulta = false;
			cabeceraDialogo = "Edición tipo de agrupación";
		}
		
		listaTipoAgrupacionesAscendentes = obtenerTipoAgrupacionesAscendentes(tipoAgrupacionSeleccionado);
		
		
		if(tipoAgrupacionSeleccionado != null && tipoAgrupacionSeleccionado.getId() != null) {
			tipoAgrupacion = tipoAgrupacionSeleccionado;
			
			if(tipoAgrupacionSeleccionado.getTipoAgrupacionPadre() != null)
				this.selectedNuevoTipoAgrupacionId = tipoAgrupacionSeleccionado.getTipoAgrupacionPadre().getId();
			else
				this.selectedNuevoTipoAgrupacionId = null;
		}
				
		PrimeFaces.current().executeScript("PF('dialogTipoAgrupacion').show();");
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
		
		while(tipoAgrupacionPadre != null)
		{
			listaTipAgrupAscendentes.add(tipoAgrupacionPadre);
			tipoAgrupacionPadre = tipoAgrupacionPadre.getTipoAgrupacionPadre();
		}
		
		return listaTipAgrupAscendentes;
	}
	
	public  void calcularNivelAnidamiento()
	{
		TipoAgrupacion tipAgrupPadre = null;
		
		if(selectedNuevoTipoAgrupacionId != null)
		{
			tipAgrupPadre = tipoAgrupacionService.obtener(selectedNuevoTipoAgrupacionId);
			tipoAgrupacion.setNivelAnidamiento(tipAgrupPadre.getNivelAnidamiento() + 1);
		} 
		
		PrimeFaces.current().ajax().update("formFormularioTipoAgrupacion:nivelAnidamiento");

	}
	
	public String onEditarByForm(Long idTipAgrup) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, numerosSaltos+1);
		
		JsfUtils.setFlashAttribute("idTipAgrup", idTipAgrup);
		JsfUtils.setFlashAttribute(EDITABLE, true);
		TipoAgrupacion tipoAgrupacionEdit = tipoAgrupacionService.obtener(idTipAgrup);
		navegacionBean.setTxtMigaPan(Constantes.EDICION_TIPOAGRUPACION+tipoAgrupacionEdit.getDescripcion());
		return ListadoNavegaciones.FORM_TIPOS_AGRUPACION.getRegla();
	}
		
}
	


