package es.juntadeandalucia.ctpda.gestionpdt.web.resoluciones;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.ArticuloAfectadoResolucion;
import es.juntadeandalucia.ctpda.gestionpdt.model.CfgTipoExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.DerechoReclamadoResolucion;
import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentoResolucion;
import es.juntadeandalucia.ctpda.gestionpdt.model.Documentos;
import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.Resolucion;
import es.juntadeandalucia.ctpda.gestionpdt.model.ResolucionExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.ResolucionPersona;
import es.juntadeandalucia.ctpda.gestionpdt.model.ResolucionSujetoObligado;
import es.juntadeandalucia.ctpda.gestionpdt.model.Usuario;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.service.AccesosDocumentosService;
import es.juntadeandalucia.ctpda.gestionpdt.service.CfgTipoExpedienteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ArticuloAfectadoResolucionService;
import es.juntadeandalucia.ctpda.gestionpdt.service.DerechoReclamadoResolucionService;
import es.juntadeandalucia.ctpda.gestionpdt.service.DocumentoResolucionService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ResolucionExpedienteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ResolucionPersonaService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ResolucionService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ResolucionSujetoObligadoService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ValoresDominioService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.ValidacionException;
import es.juntadeandalucia.ctpda.gestionpdt.util.FechaUtils;
import es.juntadeandalucia.ctpda.gestionpdt.util.ListUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.SesionBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.Constantes;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.JsfUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.LazyDataModelByQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.MyFilterMeta;
import es.juntadeandalucia.ctpda.gestionpdt.web.expedientes.DatosExpedientesBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.menu.NavegacionBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.menu.NavegacionBean.ListadoNavegaciones;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.BaseBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.ContextoVolver;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.VolverBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@ViewScoped
public class DatosResolucionBean extends BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String MENSAJESFORMULARIO = "messagesFormulario";
	private static final String CLAVE_RESOLUCION = "resolucion";
	
	private static final String IDRES = "idRes";
	private static final String EDITABLE = "editable";
	private static final String ACTUALIZADACORRECTAMENTE = "actualizada.correctamente";
	
	private static final String CLAVE_FECHA_NOTI_POSTERIOR = "fecha.notificacion.posterior";
	private static final String CLAVE_FECHA_PUBLI_POSTERIOR = "fecha.publicacion.web.posterior";

	private static final String CALLBACK_PARAM_SAVED = "saved";


	@Autowired
	private VolverBean volverBean;

	@Autowired
	private SesionBean sesionBean;

	
	@Autowired
	private ResolucionService resolucionService;
	@Autowired
	private ExpedientesService expedientesService;
	@Autowired
	private ValoresDominioService valoresDominioService;
	@Autowired
	private DerechoReclamadoResolucionService derechoReclamadoResolucionService;
	@Autowired
	private ArticuloAfectadoResolucionService articuloAfectadoResolucionService;
	@Autowired
	private ResolucionExpedienteService resolucionExpedienteService;
	@Autowired
	private ResolucionPersonaService resolucionPersonaService;	
	@Autowired
	private ResolucionSujetoObligadoService resolucionSujetoObligadoService;
	@Autowired
	private DocumentoResolucionService documentoResolucionService;
	@Autowired
	private AccesosDocumentosService accesosDocumentosService;
	@Autowired
	private DatosExpedientesBean datosExpedientesBean;
	@Autowired
	private CfgTipoExpedienteService cfgTipoExpedienteService;
	
	@Getter
	private LazyDataModelByQueryService<ResolucionExpediente> lazyModelExpedientesResolucion;
	@Getter
	private LazyDataModelByQueryService<ResolucionPersona> lazyModelPersonasResolucion;
	@Getter
	private LazyDataModelByQueryService<ResolucionSujetoObligado> lazyModelSujetosObligadosResolucion;
	@Getter
	private LazyDataModelByQueryService<DocumentoResolucion> lazyModelDocumentosResolucion;
	
	@Getter @Setter
	private Resolucion resolucion;
	
	@Getter @Setter
	private Long selectedNuevoTipoResolId;
	
	@Getter @Setter
	private Long selectedNuevoSentidoResolId;
	
	@Getter
	private List<ValoresDominio> listaTiposResolucion;

	@Getter
	private List<ValoresDominio> listaSentidosResolucion;
	
	@Getter
	private List<ValoresDominio> listaDerechosReclamadosResolucion;
	@Getter @Setter
	private List<ValoresDominio> selectedDerechosReclamadosResolucion;
	
	@Getter
	private List<ValoresDominio> listaArticulosAfectadosResolucion;
	@Getter @Setter
	private List<ValoresDominio> selectedArticulosAfectadosResolucion;
		
	@Getter @Setter
	private List<ResolucionExpediente> listaExpedientesResolucion;
	@Getter @Setter
	private List<ResolucionPersona> listaPersonasResolucion;
	@Getter @Setter
	private List<ResolucionSujetoObligado> listaSujetosResolucion;
	@Getter @Setter
	private List<DocumentoResolucion> listaDocumentosResolucion;
	
	
	@Getter @Setter
	private ResolucionExpediente expedienteResolucion;
	@Getter @Setter
	private ResolucionPersona personaResolucion;
	@Getter @Setter
	private ResolucionSujetoObligado sujetoResolucion;
	@Getter @Setter
	private DocumentoResolucion documentoResolucion;
	
	@Getter @Setter
	private Long selectedNuevoSentidoResolExpedienteId;	
	
	
	@Getter @Setter
	private boolean permisoEditarResol;
	
	@Getter @Setter
	private boolean permisoEditarResolExp;
	@Getter @Setter
	private boolean permisoBorrarResolExp;
	
	@Getter @Setter
	private boolean permisoEditarResolPers;
	@Getter @Setter
	private boolean permisoBorrarResolPers;
	
	@Getter @Setter
	private boolean permisoEditarResolSuj;
	@Getter @Setter
	private boolean permisoBorrarResolSuj;
	
	@Getter @Setter
	private boolean permisoEditarResolDoc;
	@Getter @Setter
	private boolean permisoBorrarResolDoc;

	//--------------
	
	@Getter @Setter
	private String cabeceraDialog;
	
	@Getter @Setter
	private String numeroExpedienteFiltro;

	@Getter @Setter
	private List<Long> listIdsExpedientesRelacionados;
	
	@Getter
	private LazyDataModelByQueryService<ResolucionExpediente> lazyModelResolucionExpediente;
	@Getter
	private LazyDataModelByQueryService<Expedientes> lazyModelExpedientesRelacionables;
	@Getter
	private LazyDataModelByQueryService<ResolucionExpediente> lazyModelResolucionExpedienteOrigen;
	
	@Autowired
	private NavegacionBean navegacionBean;

	@Getter
	@Setter
	private List<String> listaCodigoPermisos;
	
	@Getter @Setter
	private Integer numerosSaltos;
	
	//--------------
	
	@Override
	@PostConstruct
	public void init() {
		super.init();
		
		numerosSaltos = (Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS);
		
		/**
		 * GESTION DE PERMISOS
		 * */
		listaCodigoPermisos = (List<String>) JsfUtils.getSessionAttribute(Constantes.LISTACODIGOPERMISOS);
		permisoEditarResol = listaCodigoPermisos.contains(Constantes.PERMISO_EDIT_RESOL);
		
		permisoEditarResolExp = listaCodigoPermisos.contains(Constantes.PERMISO_EDIT_RESOL_EXP);
		permisoBorrarResolExp = listaCodigoPermisos.contains(Constantes.PERMISO_DEL_RESOL_EXP);
		
		permisoEditarResolPers = listaCodigoPermisos.contains(Constantes.PERMISO_EDIT_RESOL_PERS);
		permisoBorrarResolPers = listaCodigoPermisos.contains(Constantes.PERMISO_DEL_RESOL_PERS);
		
		permisoEditarResolSuj = listaCodigoPermisos.contains(Constantes.PERMISO_EDIT_RESOL_SUJ);
		permisoBorrarResolSuj = listaCodigoPermisos.contains(Constantes.PERMISO_DEL_RESOL_SUJ);
		
		permisoEditarResolDoc = listaCodigoPermisos.contains(Constantes.PERMISO_EDIT_RESOL_DOC);
		permisoBorrarResolDoc = listaCodigoPermisos.contains(Constantes.PERMISO_EDIT_RESOL_DOC);
		
		cargarResolucion();
		cargarFormulario();

		volverBean.recogerContexto(Constantes.VOLVERRESOLUCION);
	}
	
	private void cargarResolucion() {
		Long idRes = (Long) JsfUtils.getFlashAttribute(IDRES);
		setFormEditable(Boolean.TRUE.equals(JsfUtils.getFlashAttribute(EDITABLE)));
		this.resolucion = resolucionService.obtener(idRes);
	}
	
	private void cargarFormulario() {
		listaTiposResolucion = valoresDominioService.findValoresTipoResolucion();
		listaSentidosResolucion = valoresDominioService.findValoresSentidoResolucion();
		
		ValoresDominio valorTipoRes = resolucion.getValorTipoResolucion();
		if(valorTipoRes != null) {
			selectedNuevoTipoResolId = valorTipoRes.getId();
		}
		
		ValoresDominio valorSentidoRes = resolucion.getValorSentidoResolucion();
		if(valorSentidoRes != null) {			
			selectedNuevoSentidoResolId = valorSentidoRes.getId();
		}

		//Derechos
		listaDerechosReclamadosResolucion = valoresDominioService.findValoresDerechosReclamados();
		List<DerechoReclamadoResolucion> derechosByResolucion = derechoReclamadoResolucionService.findByResolucionId(resolucion.getId());
		selectedDerechosReclamadosResolucion = ListUtils.collect(derechosByResolucion, DerechoReclamadoResolucion::getValoresDerReclResol);
		
		//Artículos
		listaArticulosAfectadosResolucion = valoresDominioService.findValoresArticulosAfectados();
		List<ArticuloAfectadoResolucion> articulosByResolucion = articuloAfectadoResolucionService.findByResolucionId(resolucion.getId());
		selectedArticulosAfectadosResolucion = ListUtils.collect(articulosByResolucion, ArticuloAfectadoResolucion::getValorArticulo);
		
	    //-------
		
		expedienteResolucion = new ResolucionExpediente();
		personaResolucion = new ResolucionPersona();
	    sujetoResolucion = new ResolucionSujetoObligado();
	    documentoResolucion = new DocumentoResolucion();
	    
		cargarLazyDataTables();
		
	}
	
	private void cargarLazyDataTables() {
		final String CAMPO_RESOL_ID = "resolucion.id";
		
		lazyModelExpedientesResolucion = new LazyDataModelByQueryService<>(ResolucionExpediente.class, resolucionExpedienteService);
		lazyModelExpedientesResolucion.setPreproceso((a, b, c, filters) ->
			filters.put(CAMPO_RESOL_ID, new MyFilterMeta(resolucion.getId()))
		);
		
		lazyModelPersonasResolucion = new LazyDataModelByQueryService<>(ResolucionPersona.class, resolucionPersonaService);
		lazyModelPersonasResolucion.setPreproceso((a, b, c, filters) ->
			filters.put(CAMPO_RESOL_ID, new MyFilterMeta(resolucion.getId()))
		);
		
		lazyModelSujetosObligadosResolucion = new LazyDataModelByQueryService<>(ResolucionSujetoObligado.class, resolucionSujetoObligadoService);
		lazyModelSujetosObligadosResolucion.setPreproceso((a, b, c, filters) ->
			filters.put(CAMPO_RESOL_ID, new MyFilterMeta(resolucion.getId()))
		);
		
		lazyModelDocumentosResolucion = new LazyDataModelByQueryService<>(DocumentoResolucion.class, documentoResolucionService);
		lazyModelDocumentosResolucion.setPreproceso((a, b, c, filters) ->
			filters.put(CAMPO_RESOL_ID, new MyFilterMeta(resolucion.getId()))
		);
	}

	public String onEditarByForm(Long idResolucion) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, numerosSaltos+1);
		JsfUtils.setFlashAttribute(EDITABLE, true);
		JsfUtils.setFlashAttribute(IDRES, idResolucion);
		Resolucion resolucionEdit = resolucionService.obtener(idResolucion);
		navegacionBean.setTxtMigaPan(Constantes.EDICION_RESOLUCION+resolucionEdit.getCodigoResolucion());
		return ListadoNavegaciones.FORM_RESOLUCION.getRegla();
	}

	//*********************************************************
	
	public void abrirBusquedaRelacionables() {
		limpiarFiltroBusquedaRelacionables();
		
		initDialogBusquedaRelacionables();
		
		PrimeFaces.current().ajax().update("formFormularioResoluciones:dialogBuscarExpRelacionadosResolucion");
		PrimeFaces.current().executeScript("PF('dialogBuscarExpRelacionadosResolucion').show();");
	}
	
	public void limpiarFiltroBusquedaRelacionables() {
		numeroExpedienteFiltro="";
	}
	
	private void initDialogBusquedaRelacionables() {
		cabeceraDialog= "Buscar expediente relacionado para la resolución "+ resolucion.getCodigoResolucion();
		
		listIdsExpedientesRelacionados = resolucionExpedienteService.findIdsExpRelacionadosByResolId(resolucion.getId());
		
		lazyModelExpedientesRelacionables = new LazyDataModelByQueryService<>(Expedientes.class,expedientesService);
		lazyModelExpedientesRelacionables.setPreproceso((a, b, c, filters) -> {
			if (!StringUtils.isBlank(numeroExpedienteFiltro)){
				filters.put("numExpediente", new MyFilterMeta(numeroExpedienteFiltro));				
			}
			
			if(!listIdsExpedientesRelacionados.isEmpty()) {
				filters.put("#notIDExpRelacionados", new MyFilterMeta(listIdsExpedientesRelacionados));
			}	
		});
		
	}
	
	public void agregarRelacionExpediente(Long idExpediente) {
		try {
			resolucionExpedienteService.agregarExpediente(resolucion.getId(), idExpediente);

			PrimeFaces.current().ajax().addCallbackParam(CALLBACK_PARAM_SAVED, true);
			PrimeFaces.current().ajax().update("formFormularioResoluciones:panelRelacionesResolucion");
			
			FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, 
					new FacesMessage(FacesMessage.SEVERITY_INFO, "", 
								getMessages(CLAVE_RESOLUCION, ACTUALIZADACORRECTAMENTE)));		
		} catch (ValidacionException e) {
			facesMsgError(e.getMessage());
			log.error(e.getMessage());
		} catch (BaseException e) {
			facesMsgErrorGenerico();
			log.error(e.getMessage());
		}
	}
	
	
	//*********************************************************
	
	private void aplicarFormulario() {
		resolucion.setValorTipoResolucion(valoresDominioService.obtener(selectedNuevoTipoResolId));
		
		if(selectedNuevoSentidoResolId != null) {
			resolucion.setValorSentidoResolucion(valoresDominioService.obtener(selectedNuevoSentidoResolId));
		} else {
			resolucion.setValorSentidoResolucion(null);
		}
		
		aplicarDerechosFormulario();
		aplicarArticulosFormulario();
		
	}
	
	private void aplicarDerechosFormulario() {
		List<ValoresDominio> derechosResol = new ArrayList<>();
		for(ValoresDominio valorDerecho : selectedDerechosReclamadosResolucion) {
			derechosResol.add(valorDerecho);
		}
		
		resolucion.setDerechosReclamados(derechosResol);
	}
	
	private void aplicarArticulosFormulario() {
		List<ValoresDominio> articulosResol = new ArrayList<>();
		for(ValoresDominio valorArticulo : selectedArticulosAfectadosResolucion) {
			articulosResol.add(valorArticulo);
		}
		
		resolucion.setArticulosAfectados(articulosResol);
	}
		
	//****************************************************************
	
	public boolean validacionesGuardar() {
		boolean valido = true;

		if(selectedNuevoTipoResolId == null) {
			facesMsgErrorKey(CLAVE_CAMPOS_OBLIGATORIOS);
			valido = false;
		}
		
		if(selectedNuevoSentidoResolId == null) {
			facesMsgErrorKey(CLAVE_CAMPOS_OBLIGATORIOS);
			valido = false;
		}
		
		return valido;
	}

	public String saveResolucion() {
		String vueltaListado = "";
		
		try {
			if (validacionesGuardar()) {				
				aplicarFormulario();
				
				resolucionService.guardar(resolucion);
				
				List<ResolucionExpediente> resolExpedientes = resolucionExpedienteService.findListResolucionExpByIdResolucion(resolucion.getId());
				
				for(ResolucionExpediente resolExp : resolExpedientes){
								
					datosExpedientesBean.actualizarSituacionAdicional(resolExp.getExpediente().getId());
				
				}
				
				FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, 
						new FacesMessage(FacesMessage.SEVERITY_INFO, "", 
									getMessages(CLAVE_RESOLUCION, ACTUALIZADACORRECTAMENTE)));
			}			
		} catch (final BaseException e) {
			facesMsgErrorGenerico();
			log.error(e.getMessage());
		}
		
		return vueltaListado;

	}
	
	//****************************************************************
	
	public void editarRelacionExpediente(Long idRelacion) {
		expedienteResolucion = resolucionExpedienteService.obtener(idRelacion);
		
		ValoresDominio sentido = expedienteResolucion.getValorSentidoResolucion();
		selectedNuevoSentidoResolExpedienteId =
				(sentido != null)? sentido.getId() : null;
	}
	
	public void cambiarExpedientePrincipal(Long idRelacion) {
		try {
			resolucionExpedienteService.setPrincipal(idRelacion);
		} catch (BaseException e) {
			facesMsgErrorGenerico();
			log.error(e.getMessage());
		}
	}
	
	public void guardarRelacionExpediente() {
		try {			
			if(selectedNuevoSentidoResolExpedienteId != null) {
				expedienteResolucion.setValorSentidoResolucion(
						valoresDominioService.obtener(selectedNuevoSentidoResolExpedienteId));
			} else {
				facesMsgErrorKey(CLAVE_CAMPOS_OBLIGATORIOS);
				return;
			}
			
			ResolucionExpediente expeResol = resolucionExpedienteService.obtener(expedienteResolucion.getId());
			expeResol.setValorSentidoResolucion(expedienteResolucion.getValorSentidoResolucion());
			resolucionExpedienteService.guardar(expeResol);
			
			FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, 
					new FacesMessage(FacesMessage.SEVERITY_INFO, "", 
								getMessages(CLAVE_RESOLUCION, ACTUALIZADACORRECTAMENTE)));
			PrimeFaces.current().ajax().addCallbackParam(CALLBACK_PARAM_SAVED, true);
		} catch (final BaseException e) {
			facesMsgErrorGenerico();
			log.error(e.getMessage());
		}

	}
	
	public void eliminarRelacionExpediente(Long idRelacion) {
		try {			
			this.resolucionExpedienteService.delete(idRelacion);
		} catch (final BaseException e) {
			facesMsgErrorGenerico();
			log.error(e.getMessage());
		}
	}
	
	//---------------
	
	public void editarRelacionPersona(Long idRelacion) {
		personaResolucion = resolucionPersonaService.obtener(idRelacion);
	}
	
	public void cambiarPersonaPrincipal(Long idRelacion) {
		try {
			resolucionPersonaService.setPrincipal(idRelacion);
		} catch (BaseException e) {
			facesMsgErrorGenerico();
			log.error(e.getMessage());
		}
	}

	public void guardarRelacionPersona() {
		try {			
			Date fechaNoti = personaResolucion.getFechaNotificacion();
			if(null != fechaNoti && FechaUtils.despues(fechaNoti, FechaUtils.hoy())) {
				facesMsgErrorKey(CLAVE_FECHA_NOTI_POSTERIOR);
				return;					
			}
			
			ResolucionPersona persResol = resolucionPersonaService.obtener(personaResolucion.getId());
			persResol.setFechaNotificacion(fechaNoti);
			resolucionPersonaService.guardar(persResol);
			
			FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, 
					new FacesMessage(FacesMessage.SEVERITY_INFO, "", 
								getMessages(CLAVE_RESOLUCION, ACTUALIZADACORRECTAMENTE)));
			PrimeFaces.current().ajax().addCallbackParam(CALLBACK_PARAM_SAVED, true);
		} catch (final BaseException e) {
			facesMsgErrorGenerico();
			log.error(e.getMessage());
		}

	}
	
	public void eliminarRelacionPersona(Long idRelacion) {
		try {			
			this.resolucionPersonaService.delete(idRelacion);
		} catch (final BaseException e) {
			facesMsgErrorGenerico();
			log.error(e.getMessage());
		}
	}	
	
	//---------------
	
	public void editarRelacionSujeto(Long idRelacion) {
		sujetoResolucion = resolucionSujetoObligadoService.obtener(idRelacion);
	}
	
	public void cambiarSujetoPrincipal(Long idRelacion) {
		try {
			resolucionSujetoObligadoService.setPrincipal(idRelacion);
		} catch (BaseException e) {
			facesMsgErrorGenerico();
			log.error(e.getMessage());
		}
	}

	public void guardarRelacionSujeto() {
		try {
			Date fechaNoti = sujetoResolucion.getFechaNotificacion();
			if(null != fechaNoti && FechaUtils.despues(fechaNoti, FechaUtils.hoy())) {
				facesMsgErrorKey(CLAVE_FECHA_NOTI_POSTERIOR);
				return;					
							
			}
			
			ResolucionSujetoObligado sujResol = resolucionSujetoObligadoService.obtener(sujetoResolucion.getId());
			sujResol.setFechaNotificacion(fechaNoti);
			resolucionSujetoObligadoService.guardar(sujResol);
			
			FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, 
					new FacesMessage(FacesMessage.SEVERITY_INFO, "", 
								getMessages(CLAVE_RESOLUCION, ACTUALIZADACORRECTAMENTE)));
			PrimeFaces.current().ajax().addCallbackParam(CALLBACK_PARAM_SAVED, true);
		} catch (final BaseException e) {
			facesMsgErrorGenerico();
			log.error(e.getMessage());
		}

	}
	
	public void eliminarRelacionSujeto(Long idRelacion) {
		try {			
			this.resolucionSujetoObligadoService.delete(idRelacion);
		} catch (final BaseException e) {
			facesMsgErrorGenerico();
			log.error(e.getMessage());
		}
	}		
	
	//---------------
	
	public void editarRelacionDocumento(Long idRelacion) {
		documentoResolucion = documentoResolucionService.obtener(idRelacion);
	}
	
	public void guardarRelacionDocumento() {
		try {	
			Date fechaPubli = documentoResolucion.getFechaPublicacionWeb();
			if(null != fechaPubli && FechaUtils.despues(fechaPubli, FechaUtils.hoy())) {
				facesMsgErrorKey(CLAVE_FECHA_PUBLI_POSTERIOR);
				return;		
			}
			
			DocumentoResolucion docResol = documentoResolucionService.obtener(documentoResolucion.getId());
			docResol.setFechaPublicacionWeb(fechaPubli);
			docResol.setAnonimizado(documentoResolucion.getAnonimizado());
			documentoResolucionService.guardar(docResol);
			
			FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, 
					new FacesMessage(FacesMessage.SEVERITY_INFO, "", 
								getMessages(CLAVE_RESOLUCION, ACTUALIZADACORRECTAMENTE)));
			PrimeFaces.current().ajax().addCallbackParam(CALLBACK_PARAM_SAVED, true);
		} catch (final BaseException e) {
			facesMsgErrorGenerico();
			log.error(e.getMessage());
		}

	}
	
	public void eliminarRelacionDocumento(Long idRelacion) {
		try {			
			this.documentoResolucionService.delete(idRelacion);
		} catch (final BaseException e) {
			facesMsgErrorGenerico();
			log.error(e.getMessage());
		}
	}	

	public void registrarAccesoDocumento(DocumentoResolucion docRes) {
		Documentos doc = docRes.getDocumentoExpediente().getDocumento();
		Expedientes exp = docRes.getDocumentoExpediente().getExpediente();
		Usuario usu = sesionBean.getUsuarioSesion();
				
		try {
			accesosDocumentosService.registrarAccesoDocumentoResolucion(usu, doc, exp);
		} catch (BaseException e) {
			facesMsgError("Se produjo un error en el registro del acceso al documento. Consulte con su administrador.");
		}

	}

	
	//******************************************
	
	public String ejecutarVolver() {
		 ContextoVolver ctx = volverBean.getContexto(Constantes.VOLVERRESOLUCION);
		 if(ctx==null) {
			 volverBean.migaPanVolver(Constantes.LISTADO_RESOLUCIONES);
		 }else if(ctx.getVista().contains("formFormularioExpedientes")) {
			 Expedientes expedientes = expedientesService.obtener((Long) ctx.get("idExp"));
			 if((boolean) ctx.get(EDITABLE)) {
				 volverBean.migaPanVolver(Constantes.EDICION_EXPEDIENTE+expedientes.getMigaDePan()+navegacionBean.expedientesRelacionadosMotivoPsan(expedientes.getId()));
			 }else {
				 volverBean.migaPanVolver(Constantes.CONSULTA_EXPEDIENTE+expedientes.getMigaDePan()+navegacionBean.expedientesRelacionadosMotivoPsan(expedientes.getId()));
			 }			 
		 }
		 
		return volverBean.onVolver(Constantes.VOLVERRESOLUCION, ListadoNavegaciones.LISTADO_RESOLUCIONES.getRegla());
	}
	


}
