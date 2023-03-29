package es.juntadeandalucia.ctpda.gestionpdt.web.valoresdominio;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.Dominio;
import es.juntadeandalucia.ctpda.gestionpdt.model.MateriasTipoExpedienteMaestra;
import es.juntadeandalucia.ctpda.gestionpdt.model.TipoAgrupacion;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.service.MateriasTipoExpedienteMaestraService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ValoresDominioService;
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
public class ValoresDominioBean extends BaseBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private static final String EDITABLE= "editable";
	private static final String MENSAJESLISTADO = "messagesListado";
	private static final String MENSAJEVALORDOMINIO= "Valor de dominio ";
	private static final String MENSAJEERROR= "error";
	private static final String ACTUALIZADOCORRECTAMENTE = "actualizado.correctamente";
	private static final String TIPOOPERACION = "tipoOperacion";

	@Autowired
	private ValoresDominioService valoresDominioService;
	
	@Autowired
	private MateriasTipoExpedienteMaestraService materiasTipoExpedienteMaestraService;
	
	@Getter
	private LazyDataModelByQueryService<ValoresDominio> lazyModel;

	@Getter
	private LazyDataModelByQueryService<MateriasTipoExpedienteMaestra> lazyModelMatExpMaestra;
	
	@Getter
	private List<MateriasTipoExpedienteMaestra> listValDomMaestra;
	
	@Getter
	@Setter
	private ValoresDominio selectedValorDominio;
	
	@Getter
	@Setter
	private MateriasTipoExpedienteMaestra selectedMateriaTipoExpedienteMaestra;
	
	//CAMPOS FILTROS
	@Getter @Setter
	private String descripcionFiltro;
	
	@Getter @Setter
	private String abreviaturaFiltro;
	
	@Getter @Setter
	private String codigoFiltro;
	
	@Getter	@Setter
	private Long nivelAnidamientoFiltro;
	
	@Getter @Setter
	private Boolean bloqueadoFiltro;
	
	@Getter @Setter
	private Boolean activoFiltro;
	
	@Getter @Setter
	private Long ordenFiltro; 

	
	@Getter	@Setter
	private String descripValorDom;
	
	@Getter @Setter
	private Long idSuperiorSeleccionado;
	
	@Getter @Setter
	private Long idExpedienteSeleccionado;
	
	@Getter @Setter
	Dominio dominio; 
	
	@Getter
	@Setter
	private List<ValoresDominio> listaSuperiores;
	
	@Getter
	@Setter
	private List<ValoresDominio> listaTiposExpediente;
	
	
	@Getter
	@Setter
	private List<MateriasTipoExpedienteMaestra> lista;

	
	
	@Getter	@Setter
	private String descripSuperior;
	
	@Getter	@Setter
	private String descripValorDominio;
	
	@Getter	@Setter
	private Boolean bloqueado;
	
	@Getter	@Setter
	private Boolean puntoMenu;
	
	
	@Getter
	@Setter
	private List<String> listaCodigoPermisos;
	
	@Getter	@Setter
	private Boolean permisoNewValorDom;
	
	@Getter	@Setter
	private Boolean permisoConsValorDom;
	
	@Getter	@Setter
	private Boolean permisoEditValorDom;
	
	@Getter	@Setter
	private Boolean permisoDesacValorDom;
	
	@Getter	@Setter
	private Boolean permisoActValorDom;
	
	@Getter	@Setter
	private Boolean permisoSaveValorDom;	
	
	@Autowired
	private NavegacionBean navegacionBean;
	
	@Getter @Setter
	private Integer numerosSaltos;
	
	/**
	* Initialize default attributes.
	 * @throws BaseFrontException 
	*/
	@PostConstruct
	@Override
	public void init(){
		super.init();
		
		if(JsfUtils.getSessionAttribute("vieneMenuDominio") != null) {
			JsfUtils.removeSessionAttribute("vieneMenuDominio");
		}else if(JsfUtils.getSessionAttribute("vienePuntoMenu") != null) {
			JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, 0);
			JsfUtils.removeSessionAttribute("vienePuntoMenu");
		}	
		numerosSaltos = (Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS);
		
		/**
		 * GESTION DE PERMISOS
		 * */
		listaCodigoPermisos = (List<String>) JsfUtils.getSessionAttribute(Constantes.LISTACODIGOPERMISOS);
		
		permisoConsValorDom = listaCodigoPermisos.contains(Constantes.PERMISO_CONS_VALORDOM);
		
		permisoEditValorDom = listaCodigoPermisos.contains(Constantes.PERMISO_EDIT_VALORDOM);
		
		permisoDesacValorDom = listaCodigoPermisos.contains(Constantes.PERMISO_DESAC_VALORDOM);
		
		permisoNewValorDom = listaCodigoPermisos.contains(Constantes.PERMISO_NEW_VALORDOM);
		
		permisoActValorDom =  listaCodigoPermisos.contains(Constantes.PERMISO_ACT_VALORDOM);
		
		mensajesProperties = ResourceBundle.getBundle("es.juntadeandalucia.ctpda.gestionpdt.web.Messages_es");
		
		dominio = (Dominio) JsfUtils.getSessionAttribute("dominio");

		
		/*
		 * A VALORES DE DOMINIO PODEMOS LLEGAR DESDE EL LISTADO DE DOMINIOS O BIEN DESDE EL MENU PRINCIPAL. 
		 * PUNTO MENU NOS SIRVE PARA DIFERENCIAR DE DONDE VENIMOS PARA EN LA PANTALLA VALORAR A DONDE 'VOLVER'.
		 * */
		puntoMenu = (Boolean) JsfUtils.getSessionAttribute("dominioPuntoMenu");
		
		if(puntoMenu == null)
			puntoMenu = false;
		
		/*
		 * EN ESTE BEAN HACEMOS UNA DIFERENCIACION ENTRE LOS VALORES DE DOMINIO ASOCIADOS AL DOMINO 'MATERIA' Y EL RESTO DE VALORES DE DOMINIO
		 * */
		
		if(dominio != null)
		{
			descripValorDom = mensajesProperties.getString("gestion.de") + dominio.getDescripcion();
			descripSuperior = dominio.getDescripcion() + " superior";
			descripValorDominio = mensajesProperties.getString("crear.registro");
			
			/*
			 * INICIALIZACION Y FILTROS PARA LOS VALORES DE DOMINIO ASOCIADOS AL DOMINIO MATERIA.
			 * EN ESTE CASO, HACEMOS UNA CONSULTA DE LA VISTA 'MateriasTipoExpedienteMaestra' PARA PODER OBTENER LOS VALORES DE DOMINIO DEL DOMINIO 'MATERIA' Y 
			 * PODER OBTENER LOS VALORES DE DOMINIO DEL DOMINIO 'TIPO DE EXPEDIENTE' ASOCIADOS A CADA VALOR DE DOMINIO DE DOMINIO TIPO 'MATERIA'
			 */
			
			
			if(dominio.getCodigo().equals("MAT"))
			{
				initDominioMateria();
			}
			
			/*
			 * INICIALIZACION Y FILTROS PARA LOS VALORES DE DOMINIO QUE NO SE CORRESPONDEN CON EL DOMINIO 'MATERIA'.
			 * EN ESTE CASO, HACEMOS UNA CONSULTA DE LA TABLA 'ValoresDominio'.
			 */
			else {
				initRestoDominios();				
			}
			
			activoFiltro = true;
			
			listaSuperiores =valoresDominioService.findValoresDominioActivosByDominio(dominio.getId());
			
			navegacionBean.setTxtMigaPan(Constantes.LISTADO_DOMINIO+dominio.getDescripcion());
		}
		else
		{
			lazyModelMatExpMaestra = null;
			lazyModel = null;
		}
			
	}
	
	private void initRestoDominios () {
		lazyModel= new LazyDataModelByQueryService<>(ValoresDominio.class,valoresDominioService);
		
		lazyModel.setPreproceso((a,b,c,filters)->
			filtrosLazyModelRestoDominios(filters)					
		);
	}
	
	private void filtrosLazyModelRestoDominios(Map<String, FilterMeta> filters ) {
		if(dominio != null)
		{
			filters.put("dominio.id", new MyFilterMeta(dominio.getId()));	
		}
		if (nivelAnidamientoFiltro != null){
			filters.put("nivelAnidamiento", new MyFilterMeta(nivelAnidamientoFiltro));				
		}
		if (idSuperiorSeleccionado != null){
			filters.put("valorDominioPadre", new MyFilterMeta(valoresDominioService.obtener(idSuperiorSeleccionado)));				
		}	

		
		if (descripcionFiltro != null && !descripcionFiltro.isEmpty()){
			filters.put("descripcion", new MyFilterMeta(descripcionFiltro));				
		}
		if (abreviaturaFiltro != null && !abreviaturaFiltro.isEmpty()){
			filters.put("abreviatura", new MyFilterMeta(abreviaturaFiltro));				
		}
		if (codigoFiltro != null && !codigoFiltro.isEmpty()){
			filters.put("codigo", new MyFilterMeta(codigoFiltro));				
		}
		if (ordenFiltro != null){
			filters.put("orden", new MyFilterMeta(ordenFiltro));				
		}
		if (bloqueadoFiltro != null && bloqueadoFiltro) {
			filters.put("bloqueado", new MyFilterMeta("true"));
		} 
		
		if (activoFiltro != null && activoFiltro) {
			filters.put("activo", new MyFilterMeta("true"));
		} 
	}
	
	private void initDominioMateria () {
		lazyModelMatExpMaestra= new LazyDataModelByQueryService<>(MateriasTipoExpedienteMaestra.class,materiasTipoExpedienteMaestraService);
		
		
		lazyModelMatExpMaestra.setPreproceso((a,b,c,filters)->{
			/** añadamos siempre un orden por id en los casos de vistas */
			if (c!=null) {
				final SortMeta ordenacionById = SortMeta.builder().field("id").order(SortOrder.ASCENDING).priority(c.size()+1).build();
				c.put("id", ordenacionById);
			}
			
			filtrosLazyModelMateria(filters);						
		});
		
		listaTiposExpediente = valoresDominioService.findValoresDominioActivosByCodigoDominio(Constantes.COD_TIPO_EXPEDIENTE);
	}
	
	private void filtrosLazyModelMateria (Map<String, FilterMeta> filters ) {
		if(dominio != null)
		{ 
			filters.put("dominio.id", new MyFilterMeta(dominio.getId()));	
		}
		if (nivelAnidamientoFiltro != null){
			filters.put("nivelAnidamiento", new MyFilterMeta(nivelAnidamientoFiltro));				
		}
		if (idSuperiorSeleccionado != null){
			filters.put("valorDominioPadre", new MyFilterMeta(valoresDominioService.obtener(idSuperiorSeleccionado)));				
		}	

		
		if (descripcionFiltro != null && !descripcionFiltro.isEmpty()){
			filters.put("descripcion", new MyFilterMeta(descripcionFiltro));				
		}
		
		if (abreviaturaFiltro != null && !abreviaturaFiltro.isEmpty()){
			filters.put("abreviatura", new MyFilterMeta(abreviaturaFiltro));				
		}
		
		if (codigoFiltro != null && !codigoFiltro.isEmpty()){
			filters.put("codigo", new MyFilterMeta(codigoFiltro));				
		}
		if (ordenFiltro != null){
			filters.put("orden", new MyFilterMeta(ordenFiltro));				
		}
		if (bloqueadoFiltro != null && bloqueadoFiltro){
			filters.put("bloqueado", new MyFilterMeta("true"));
		} 
		
		if (activoFiltro != null && activoFiltro) {
			filters.put("activo", new MyFilterMeta("true"));
		} 
		
		/*
		 * HACEMOS ESTE FILTRO TENIENDO EN CUENTA LOS '-' YA QUE LA VISTA OBTIENE LOS IDS DE LOS EXPEDIENTES SEPARADOS POR '-'
		 * */
		if (idExpedienteSeleccionado != null){
			filters.put("idexpedientes", new MyFilterMeta("-" + idExpedienteSeleccionado.toString() + "-"));				
		}
	}

	public void limpiarFiltro () {
		descripcionFiltro = "";
		abreviaturaFiltro = "";
		ordenFiltro = null;
		bloqueadoFiltro = null;
		activoFiltro = true;
		codigoFiltro = "";
		nivelAnidamientoFiltro = null;
		idSuperiorSeleccionado = null;
		idExpedienteSeleccionado = null;
	}
	
	public void eliminarValorDominio(ValoresDominio valorDominioSeleccionado) {
		try {
			
			boolean esPadreConValorDominioActivo = esPadreValoresDominioActivo(valorDominioSeleccionado.getDominio().getId(),valorDominioSeleccionado.getId());
				
			if(!esPadreConValorDominioActivo)
			{
				valorDominioSeleccionado.setActivo(false);
				valoresDominioService.guardar(valorDominioSeleccionado);
				listaSuperiores = valoresDominioService.findValoresDominioActivosByDominio(dominio.getId());
				FacesContext.getCurrentInstance().addMessage(MENSAJESLISTADO, new FacesMessage(FacesMessage.SEVERITY_INFO, "", MENSAJEVALORDOMINIO +" "+valorDominioSeleccionado.getDescripcion()+" "+ mensajesProperties.getString(ACTUALIZADOCORRECTAMENTE)));
			}
			else
			{
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString("valor.dominio.asociados.activos"));
				PrimeFaces.current().dialog().showMessageDynamic(message);
			}
	
			
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}			
		
	}
	
	public void eliminarValorDominioMateria(MateriasTipoExpedienteMaestra valorDominioMateriaSeleccionado) {
			ValoresDominio valorDominioMateria = valoresDominioService.obtener(valorDominioMateriaSeleccionado.getId());
			
			try {
				
				boolean esPadreConValorDominioActivo = esPadreValoresDominioActivo(valorDominioMateria.getDominio().getId(),valorDominioMateria.getId());
					
				if(!esPadreConValorDominioActivo)
				{
					valorDominioMateria.setActivo(false);
					valoresDominioService.guardar(valorDominioMateria);
					listaSuperiores = valoresDominioService.findValoresDominioActivosByDominio(dominio.getId());
					FacesContext.getCurrentInstance().addMessage(MENSAJESLISTADO, new FacesMessage(FacesMessage.SEVERITY_INFO, "", MENSAJEVALORDOMINIO +" "+valorDominioMateria.getDescripcion()+" "+ mensajesProperties.getString(ACTUALIZADOCORRECTAMENTE)));
				}
				else
				{
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString("valor.dominio.asociados.activos"));
					PrimeFaces.current().dialog().showMessageDynamic(message);
				}
		
				
			} catch (BaseException e) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MENSAJEERROR));
				PrimeFaces.current().dialog().showMessageDynamic(message);
				log.error(e.getMessage());
			}						

	}
	
	
	public void activarValorDominio(ValoresDominio valorDominioSeleccionado) {
		try {
			valorDominioSeleccionado.setActivo(true);
			valoresDominioService.guardar(valorDominioSeleccionado);
			FacesContext.getCurrentInstance().addMessage(MENSAJESLISTADO, new FacesMessage(FacesMessage.SEVERITY_INFO, "", MENSAJEVALORDOMINIO +" "+valorDominioSeleccionado.getDescripcion()+" "+ mensajesProperties.getString(ACTUALIZADOCORRECTAMENTE)));
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
	}	
	
	public void activarValorDominioMateria(MateriasTipoExpedienteMaestra valorDominioMateriaSeleccionado) {
		try {
			ValoresDominio valorDominioMateria = valoresDominioService.obtener(valorDominioMateriaSeleccionado.getId());
			valorDominioMateria.setActivo(true);
			valoresDominioService.guardar(valorDominioMateria);
			FacesContext.getCurrentInstance().addMessage(MENSAJESLISTADO, new FacesMessage(FacesMessage.SEVERITY_INFO, "", MENSAJEVALORDOMINIO +" "+valorDominioMateria.getDescripcion()+" "+ mensajesProperties.getString(ACTUALIZADOCORRECTAMENTE)));
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
	}
	
	public boolean esPadreValoresDominioActivo(Long idDominio,Long idValorDominio)
	{
		boolean esPadreConValDomActivo = false;
		List<ValoresDominio> valoresDominioHijos;
		valoresDominioHijos = valoresDominioService.findValoresDominioHijosActivos(idDominio,idValorDominio);
		if(!valoresDominioHijos.isEmpty())
			esPadreConValDomActivo = true;

		return esPadreConValDomActivo;
	}
	

	public String onConsultar(Long idValDom) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, numerosSaltos+1);
		
		JsfUtils.setFlashAttribute(EDITABLE, false);
		JsfUtils.setFlashAttribute("idValDom", idValDom);	
		JsfUtils.setFlashAttribute(TIPOOPERACION, "consultar");
		ValoresDominio valoresDominio = valoresDominioService.obtener(idValDom);
		navegacionBean.setTxtMigaPan(Constantes.CONSULTA_DOMINIO+dominio.getDescripcion()+" "+valoresDominio.getDescripcion());
		return ListadoNavegaciones.FORM_VALORES_DOMINIO.getRegla();
	}	
	
	public String onCrear() {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, numerosSaltos+1);
		
		JsfUtils.setFlashAttribute(EDITABLE, true);
		JsfUtils.setFlashAttribute(TIPOOPERACION, "crear");
		navegacionBean.setTxtMigaPan(Constantes.ALTA_DOMINIO+dominio.getDescripcion());
		return ListadoNavegaciones.FORM_VALORES_DOMINIO.getRegla();
	}

	
	public String onEditar(Long idValDom) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, numerosSaltos+1);
		
		JsfUtils.setFlashAttribute("idValDom", idValDom);
		JsfUtils.setFlashAttribute(EDITABLE, true);
		JsfUtils.setFlashAttribute(TIPOOPERACION, "editar");
		ValoresDominio valoresDominio = valoresDominioService.obtener(idValDom);
		navegacionBean.setTxtMigaPan(Constantes.EDICION_DOMINIO+dominio.getDescripcion()+" "+valoresDominio.getDescripcion());
		return ListadoNavegaciones.FORM_VALORES_DOMINIO.getRegla();
	}
	
	
	public void onTipAgrupRowDblClkSelect(final SelectEvent<TipoAgrupacion> event) {
		TipoAgrupacion ta = event.getObject();
		JsfUtils.setFlashAttribute(EDITABLE, false);
		PrimeFaces.current().ajax().addCallbackParam("id", ta.getId());
	}
		
	
	public void onValDomRowDblClkSelect(final SelectEvent<ValoresDominio> event) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, numerosSaltos+1);
		
		ValoresDominio valDom = event.getObject();
		PrimeFaces.current().ajax().addCallbackParam("id", valDom.getId());
		navegacionBean.setTxtMigaPan(Constantes.CONSULTA_DOMINIO+dominio.getDescripcion()+" "+valDom.getDescripcion());
	}
	
	public void onValDomMatRowDblClkSelect(final SelectEvent<MateriasTipoExpedienteMaestra> event) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, numerosSaltos+1);
		
		MateriasTipoExpedienteMaestra valDomMat = event.getObject();
		PrimeFaces.current().ajax().addCallbackParam("id", valDomMat.getId());
		navegacionBean.setTxtMigaPan(Constantes.CONSULTA_DOMINIO+dominio.getDescripcion()+" "+valDomMat.getDescripcion());
	}
	
	
}
