package es.juntadeandalucia.ctpda.gestionpdt.web.expedientes;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.apache.commons.io.FilenameUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.ReorderEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.AccesosDocumentos;
import es.juntadeandalucia.ctpda.gestionpdt.model.AgrupacionesExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentoDTO;
import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentoResolucion;
import es.juntadeandalucia.ctpda.gestionpdt.model.Documentos;
import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentosExpedientesTramites;
import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentosMaestra;
import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentosTramiteMaestra;
import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.Resolucion;
import es.juntadeandalucia.ctpda.gestionpdt.model.ResolucionExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.TipoDocumento;
import es.juntadeandalucia.ctpda.gestionpdt.model.TramiteExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.Usuario;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.service.AccesosDocumentosService;
import es.juntadeandalucia.ctpda.gestionpdt.service.DocumentoResolucionService;
import es.juntadeandalucia.ctpda.gestionpdt.service.DocumentosExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.DocumentosExpedientesTramitesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.DocumentosService;
import es.juntadeandalucia.ctpda.gestionpdt.service.DocumentosTramiteMaestraService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ResolucionService;
import es.juntadeandalucia.ctpda.gestionpdt.service.TipoDocumentoService;
import es.juntadeandalucia.ctpda.gestionpdt.service.TramiteExpedienteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ValoresDominioService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.ValidacionException;
import es.juntadeandalucia.ctpda.gestionpdt.service.ResolucionExpedienteService;
import es.juntadeandalucia.ctpda.gestionpdt.util.FechaUtils;
import es.juntadeandalucia.ctpda.gestionpdt.util.StringUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.SesionBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.Constantes;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.JsfUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.LazyDataModelByQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.MyFilterMeta;
import es.juntadeandalucia.ctpda.gestionpdt.web.documentos.AgrupacionDTO;
import es.juntadeandalucia.ctpda.gestionpdt.web.documentos.CategoriaDocDTO;
import es.juntadeandalucia.ctpda.gestionpdt.web.documentos.TramiteDocDTO;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.BaseBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.ControlAcordeon;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.FacesUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.Reordenacion;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.UtilsComun;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@ViewScoped
public class DatosExpedientesDocumentosBean extends BaseBean implements Serializable {
	
	private static final String MENSAJESFORMULARIO = "messagesFormulario";
	private static final String MENSAJEREORDENADOSOK = "exito.reordenacion";
	private static final String ACTUALIZADOCORRECTAMENTE = "actualizado.correctamente";
	private static final String ACTUALIZADACORRECTAMENTE = "actualizada.correctamente";
	private static final String ELIMINADACORRECTAMENTE = "eliminada.correctamente";
	
	private static final String MENSAJEFALTAORDEN = "campo.orden.falta";
	private static final String MENSAJEREORDENACIONDESC = "reordenacion.no.permitida.ordendesc";
	private static final String EDITABLE = "editable";
	private static final String MENSAJEERROR = "error";
	private static final String VINCULADOCORRECTO = "vinculado.correcto";
	
	
	private static final String ORDEN = "orden";
	private static final String EDITARPROPIEDADESDOC = "editar.propiedades.doc";

	private static final long serialVersionUID = 1L;
	
	@Getter
	private LazyDataModelByQueryService<DocumentosMaestra> lazyModelDocumentosExpediente;
	@Getter
	private LazyDataModelByQueryService<DocumentosMaestra> lazyModelDocumentosTrabajo;
	
	@Getter
	private LazyDataModelByQueryService<AccesosDocumentos> lazyModelAccesosDocumento;
	@Getter
	private LazyDataModelByQueryService<AgrupacionesExpedientes> lazyModelAgrupacionesExpedientes;
	
	@Autowired
	private SesionBean sesionBean;
	@Autowired
	private ControlAcordeon controlAcordeon;
	
	@Autowired
	private DocumentosExpedientesService documentosExpedientesService;
	@Autowired
	private ValoresDominioService valoresDominioService;
	@Autowired
	private DocumentosExpedientesTramitesService documentosExpedientesTramitesService;
	@Autowired
	private DocumentosTramiteMaestraService documentosTramiteMaestraService;
	
	@Autowired
	private ResolucionExpedienteService resolucionExpedienteService;
	
	@Autowired
	private DocumentosService documentosService;
	@Autowired
	private AccesosDocumentosService accesosDocumentosService;
	@Autowired
	private TramiteExpedienteService tramiteExpedienteService;
	@Autowired
	private TipoDocumentoService tipoDocumentoService;
	@Autowired
	private ResolucionService resolucionService;
	@Autowired
	private DocumentoResolucionService documentoResolucionService;
	@Autowired
	private DatosExpedientesBean datosExpedientesBean;
	@Autowired
	private UtilsComun utilsComun;
	@Getter	@Setter
	private Date fechaIniCreaFiltro;
	@Getter	@Setter
	private Date fechaFinCreaFiltro;
	@Getter	@Setter
	private Date fechaIniModificaFiltro;
	@Getter	@Setter
	private Date fechaFinModificaFiltro;
	@Getter	@Setter
	private Long tipoDocumentoIdFiltro;
	@Getter	@Setter
	private Boolean verVinculadosFiltro;
	
	@Getter	@Setter
	private transient Iterable<TipoDocumento> listaTiposDocumentosFiltro;
	@Getter	@Setter
	private List<TipoDocumento> listaTiposDocumentos;
	@Getter
	private List<CategoriaDocDTO> listaCategoriasDocumentosExpediente;
	@Getter
	private List<ValoresDominio> listaCategoriasDocumentos; //Todas las categorias del sistema
	
	@Getter	@Setter
	private Expedientes expedientes;
	@Getter	@Setter
	private List<DocumentosExpedientes> listaDocumentosTrabajoExpedientes;
	
	@Getter	@Setter
	boolean soloLectura;
	@Getter	@Setter
	private DocumentosMaestra selectedDocumento;
	@Getter	@Setter
	private DocumentosTramiteMaestra selectedDocumentoTramite;

	@Getter	@Setter
	private DocumentoDTO documento;
	

	@Getter
	private List<SortMeta> defaultOrdenList;

	
	@Getter	@Setter
	private List<String> listaCodigoPermisos;	
	
	@Getter	@Setter
	private Boolean permisoDescargarDoc;
	
	@Getter	@Setter
	private Boolean permisoEditarDoc;
	
	@Getter	@Setter
	private Boolean permisoPasarTrDoc;
	
	@Getter	@Setter
	private Boolean permisoEditarPropsDoc;
	
	@Getter	@Setter
	private Boolean permisoCambiarTipoDoc;
	
	@Getter	@Setter
	private Boolean permisoVincularDoc;
	
	@Getter	@Setter
	private Boolean permisoVerDocsOcultos;

	
	@Getter @Setter
	private String cabeceraDialog;
	
	
	
	@Override
	@PostConstruct
	public void init() {
		super.init();
		
		cargarExpediente();
		cargarPermisos();
		
		defaultOrdenList = List.of(SortMeta.builder().field(ORDEN).order(SortOrder.ASCENDING).build());

		cargarFiltroTiposDoc();
		
		documento = new DocumentoDTO();
		
		documentosPorTramite = new DocumentosPorTramite();
		
		listaCategoriasDocumentos = valoresDominioService.findValoresCategoriasDocumento();

		if(null != expedientes.getId()) {
			actualizarDocumentos();
		}
	}
	
	private void cargarExpediente() {
		FacesUtils.setAttribute(EDITABLE, JsfUtils.getFlashAttribute(EDITABLE));
		expedientes = (Expedientes) JsfUtils.getSessionAttribute("expedienteFormulario");
	}
	
	@SuppressWarnings("unchecked")
	private void cargarPermisos() {		
		listaCodigoPermisos = (List<String>) JsfUtils.getSessionAttribute(Constantes.LISTACODIGOPERMISOS);
		permisoDescargarDoc = listaCodigoPermisos.contains(Constantes.PERMISO_CONS_EXPDOC);
		permisoEditarDoc = listaCodigoPermisos.contains(Constantes.PERMISO_EDIT_EXPDOC);
		permisoPasarTrDoc = listaCodigoPermisos.contains(Constantes.PERMISO_NEW_EXPDOCTRAB);
		permisoEditarPropsDoc = listaCodigoPermisos.contains(Constantes.PERMISO_EDIT_EXPDOCCONF);
		permisoCambiarTipoDoc = listaCodigoPermisos.contains(Constantes.PERMISO_EDIT_EXPTIPODOC);
		permisoVincularDoc = listaCodigoPermisos.contains(Constantes.PERMISO_NEW_EXPVINCDOC);
		permisoVerDocsOcultos = listaCodigoPermisos.contains(Constantes.PERMISO_VER_DOCSOCULTOS);
	}
		
	public void cargarFiltroTiposDoc() {
		listaTiposDocumentosFiltro =  tipoDocumentoService.findTiposDocumentoUsados(expedientes.getId());
	}
	
	//**************************************************
	//Los métodos valen para todos los acordeones.
	//No podemos aplicar por tanto el tipo genérico (que corresponde a la colección usada en los <p:tab>.

	
	public void onChangeTabAcordeon(@SuppressWarnings("rawtypes") TabChangeEvent event) {
		controlAcordeon.mantenerIndices(event);
	}
	
	public void onCloseTabAcordeon(@SuppressWarnings("rawtypes") TabCloseEvent event) {
		controlAcordeon.mantenerIndices(event);
	}
		
	
	//*********************************************************************************
	
	public void limpiarFiltro() {
		fechaIniCreaFiltro = null;
		fechaFinCreaFiltro = null;
		fechaIniModificaFiltro = null;
		fechaFinModificaFiltro = null;
		tipoDocumentoIdFiltro = null;
		verVinculadosFiltro = false;
	}
	
	
	//*********************************************************************************

	public void onDocsTramRowReorder(ReorderEvent event) {
		try {
			
			this.reordenacionDocsTram.onRowReorder(event);
			actualizarDocumentos();
			
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "", getMessage(MENSAJEREORDENADOSOK));
			FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, message);
			
		} catch (ValidacionException e) {
			facesMsgWarn(e.getMessage());
		} catch (BaseException e) {
			e.printStackTrace();
			facesMsgErrorGenerico();
		}
	}
	
	private Reordenacion<DocumentosTramiteMaestra> reordenacionDocsTram =  new Reordenacion<>() {

		private static final long serialVersionUID = 1L;

		@Override @SuppressWarnings("unchecked")
		public List<DocumentosTramiteMaestra> cargarDatos(DataTable dt) {
			return (List<DocumentosTramiteMaestra>)dt.getValue();
		}
		
		@Override
		public String getMensajeFaltaOrden() {
			return mensajesProperties.getString(MENSAJEFALTAORDEN);
		}
		
		@Override
		public String getMensajeOrdenDesc() {
			return mensajesProperties.getString(MENSAJEREORDENACIONDESC);
		}
		
		@Override
		public void reordenar(List<DocumentosTramiteMaestra> lista, Long orden0) throws BaseException {
			documentosExpedientesService.reordenar(lista, orden0);			
		}

		@Override
		public SortMeta getSortMeta(DataTable dt) {
			return dt.getActiveSortMeta().get(ORDEN);
		}
		
	};
			
	//********************************************
	
	public StreamedContent descargar(Long idDocExp) {
		final DocumentosExpedientes docExp = documentosExpedientesService.obtener(idDocExp);
		final Documentos d = docExp.getDocumento();

		final String fName = FilenameUtils.normalize(docExp.getDescripcionDocumento());
		final byte[] bytes = d.getBytes();
		
		return DefaultStreamedContent.builder()
                .name(fName + "." + d.getExtensionFichero())
                .contentLength(bytes.length)
                .stream(() -> new ByteArrayInputStream(bytes))
                .build();
	}
	
	//-------------------------------
	
	public boolean esExtensionEditor(DocumentosMaestra docM) {
		return documentosService.esExtensionEditor(docM.getExtensionFichero());
	}
	
	public boolean esExtensionEditor(DocumentosTramiteMaestra docM) {
		return documentosService.esExtensionEditor(docM.getExtensionFichero());
	}
	
	public boolean esDocumentoEditable(DocumentosMaestra docM) {
		return documentosService.esDocumentoEditable(docM.getEditable(), docM.getExtensionFichero());
	}
	
	public boolean esDocumentoEditable(DocumentosTramiteMaestra docM) {
		return documentosService.esDocumentoEditable(docM.getEditable(), docM.getExtensionFichero());
	}
		
	public boolean esDocumentoODT(DocumentosTramiteMaestra docM) {
		boolean esConvertible = StringUtils.equalsIgnoreCase(docM.getExtensionFichero(), Constantes.EXTENSION_ODT);
		
		if (docM.getEditable().equals(false)) {
			esConvertible = false;
		} 
		
		return esConvertible;
	}
	
	public void convertirOdtAPdf(DocumentosTramiteMaestra docM) {
		try {
			documentosExpedientesService.generarDocumentoPdfDesdeOdt(docM.getIdDocExpTramite());
			PrimeFaces.current().ajax().addCallbackParam("ok", true);
		} catch (Exception e) {
			facesMsgErrorKey(CLAVE_ERROR_GENERICO);
			e.printStackTrace();
		}
	}

	//----------------------------------------
		
	public void iniciarEliminarDocumentoTramite(DocumentosTramiteMaestra docExpTram) {
		this.selectedDocumentoTramite = docExpTram;
		boolean eliminarSimple = true;
		
		//Debería dar siempre true, ya se comprueba desde jsf
		if(!checkPuedeEliminarse(docExpTram)) {
			PrimeFaces.current().ajax().addCallbackParam("cancel", true );
			return;
		}
		
		//Si sólo queda el doc-trámite que quiero borrar, puedo borrar el doc-expediente si el expediente es el actual.
		//De no ser así estaría intentando borrar un documento que pertenece a otro expediente.
		//De todas formas, es algo que no se puede dar. Siempre se genera un doc-exp para el propio expediente, da igual que el documento sea I, G o V.
		if(expedientes.getId().equals(docExpTram.getIdExpediente())) {
			List<?> listaTramitesDoc = documentosExpedientesTramitesService.findDocExpTramByDocExpId(docExpTram.getId());
			eliminarSimple = 1 != listaTramitesDoc.size();
		}
		
		PrimeFaces.current().ajax().addCallbackParam("cancel", false );
		PrimeFaces.current().ajax().addCallbackParam("eliminarSimple", eliminarSimple  );
	}
	
	private boolean checkPuedeEliminarse(DocumentosTramiteMaestra docExpTram) {
		boolean b = true;

		try {
			documentosExpedientesTramitesService.checkSiPuedoEliminar(docExpTram.getIdDocExpTramite());
		} catch (BaseException e) {
			facesMsgError(e.getMessage());
			b = false;			
		}
		
		return b;
	}
		
	public void eliminarDocumentoTramite() {
		Long idDocExpTram = this.selectedDocumentoTramite.getIdDocExpTramite();
		
		try {
			documentosExpedientesService.eliminarDocumentoTramite(idDocExpTram);
			
			String txt = mensajesProperties.getString("asociacion.tramite") + " " + mensajesProperties.getString(ELIMINADACORRECTAMENTE);
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "", txt);
			PrimeFaces.current().dialog().showMessageDynamic(message);
			
			actualizarDespuesDeEliminarDocExpTram();
			PrimeFaces.current().ajax().addCallbackParam("ok", true);
		} catch (BaseException e) {
			facesMsgErrorGenerico();
			e.printStackTrace();
		}
	}
	
	public void eliminarDocumentoTramiteCompleto() {
		Long idDocExpTram = this.selectedDocumentoTramite.getIdDocExpTramite();
		
		try {
			documentosExpedientesService.eliminarDocumentoTramiteYExpediente(idDocExpTram);
			
			String txt = getMessages("asociacion.tramite.expediente", "eliminadas.correctamente");
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "", txt);
			PrimeFaces.current().dialog().showMessageDynamic(message);
			
			actualizarDespuesDeEliminarDocExpTram();
			PrimeFaces.current().ajax().addCallbackParam("ok", true);
		} catch (BaseException e) {
			facesMsgErrorGenerico();
			e.printStackTrace();
		}
	}
	
	private void actualizarDespuesDeEliminarDocExpTram() throws BaseException {
		cargarFiltroTiposDoc(); 
		
		//----
		
		Usuario usuarioLogado = (Usuario) JsfUtils.getSessionAttribute(Constantes.USUARIO);
		String login = usuarioLogado.getLogin();
		Date ahora = FechaUtils.fechaYHoraActualDate(); //Usar FechaUtils, método ahora()
		expedientes = utilsComun.expedienteUltimaModificacion(expedientes, ahora, ahora, login, login);
		
		//----
		
		datosExpedientesBean.actualizarCabecera(expedientes,null,null,null);
	}
		
	//------------------------------
	
	public void registrarAcceso(DocumentosMaestra docM, boolean evol) {
		Documentos doc = documentosService.obtener(docM.getIdDocumento());
				
		String codTipoAcceso = esAccesoEdicion(doc, evol)? 
				AccesosDocumentos.TIPO_ACCESO_EDICION : AccesosDocumentos.TIPO_ACCESO_CONSULTA;
		
		//evol no se usa, está para no tener que duplicar el botón de editar en el listado
		registrarAcceso(doc, null, false, codTipoAcceso); 
	}
		
	public void registrarAcceso(DocumentosTramiteMaestra docM, boolean evol) {
		Documentos doc = documentosService.obtener(docM.getIdDocumento());
		TramiteExpediente tram = tramiteExpedienteService.obtener(docM.getIdTramiteExpediente());
		String codTipoAcceso = esAccesoEdicion(doc, evol)? 
				AccesosDocumentos.TIPO_ACCESO_EDICION : AccesosDocumentos.TIPO_ACCESO_CONSULTA;
		
		registrarAcceso(doc, tram, evol, codTipoAcceso);
	}
	
	private boolean esAccesoEdicion(Documentos doc, boolean evol) {
		//El acceso finalmente será de tipo edición si:
		//El expediente es editable, no accccedemos desde evolución, el documento tiene atributo editable y además es un formato editable
		return this.getFormEditable() && !evol && accesosDocumentosService.esAccesoEdicion(doc);
	}
	
	//--------

	public void registrarAccesoDescarga(DocumentosMaestra docM) {
		Documentos doc = documentosService.obtener(docM.getIdDocumento());
		
		registrarAcceso(doc, null, false, AccesosDocumentos.TIPO_ACCESO_DESCARGA); 
	}
	
	public void registrarAccesoDescarga(DocumentosTramiteMaestra docM) {
		Documentos doc = documentosService.obtener(docM.getIdDocumento());
		TramiteExpediente tram = tramiteExpedienteService.obtener(docM.getIdTramiteExpediente());
		
		registrarAcceso(doc, tram, false, AccesosDocumentos.TIPO_ACCESO_DESCARGA);
	}
	
	//--------
	
	private void registrarAcceso(Documentos doc, TramiteExpediente tramExp, boolean evol, String codTipoAcceso) {
		try {
			Usuario usu = sesionBean.getUsuarioSesion();
			
			if(tramExp == null) {
				accesosDocumentosService.registrarAccesoDocumentos(usu, doc, expedientes, codTipoAcceso);
			} else {
				Expedientes exp = tramExp.getExpediente();
				if(evol) {
					accesosDocumentosService.registrarAccesoEvolucion(usu, doc, exp, tramExp);
				} else {	
					accesosDocumentosService.registrarAccesoTramitacion(usu, doc, exp, tramExp, codTipoAcceso);
				}
			}
		} catch (BaseException e) {
			facesMsgError("Se produjo un error en el registro del acceso al documento. Consulte con su administrador.");
		}
	}
	
	//------------------------------
	//Dependiendo del tipo de listado de documentos (de expediente o de trámite) 
	//entraremos por uno u otro método editarPropiedades
	public void editarPropiedades(DocumentosTramiteMaestra docm) {
		if(expedientes.getId()!=null) {
			cabeceraDialog=getMessage(EDITARPROPIEDADESDOC) 
					+ " \"" + docm.getDescripcion() + "\" "
					+ " del expediente "+ expedientes.getNumExpediente();
			}else {
				cabeceraDialog=getMessage(EDITARPROPIEDADESDOC);
		}
		final DocumentosExpedientesTramites docExpTram = documentosExpedientesTramitesService.obtener(docm.getIdDocExpTramite());
		cargarDocumentoDTO(docExpTram);
		cargarTiposDocumentos();
	}
	
	public void editarPropiedades(DocumentosMaestra docm) {
		if(expedientes.getId()!=null) {
			cabeceraDialog=getMessage(EDITARPROPIEDADESDOC)+ " del expediente "+ expedientes.getNumExpediente();
			}else {
				cabeceraDialog=getMessage(EDITARPROPIEDADESDOC);
		}
		final DocumentosExpedientes docExp = documentosExpedientesService.obtener(docm.getId());
		cargarDocumentoDTO(docExp);
		cargarTiposDocumentos();
	}
	
	private void cargarDocumentoDTO(DocumentosExpedientesTramites docExpTr) {
		documento = documentosExpedientesService.cargarDocumentoDTO(docExpTr.getDocumentoExpediente());
		documento.setTramiteExpedienteId(docExpTr.getTramiteExpediente().getId());
		documento.setTipoTramite(docExpTr.getTramiteExpediente().getTipoTramite().getCodigo());
	}
	
	private void cargarDocumentoDTO(DocumentosExpedientes docExp) {
		documento = documentosExpedientesService.cargarDocumentoDTO(docExp);
	}
	
	private void cargarTiposDocumentos() {
		if(Boolean.TRUE.equals(permisoCambiarTipoDoc)) {
			this.listaTiposDocumentos = tipoDocumentoService.findTodos();
		}
	}
	
	//-------------
	
	public void verRegistroAccesosDocumento() {
		verRegistroAccesosDocumento(documento.getDocumentoId());
	}
	
	public void verRegistroAccesosDocumento(DocumentosMaestra doc) {
		DocumentosExpedientes docExp = documentosExpedientesService.obtener(doc.getId());
		documento = documentosExpedientesService.cargarDocumentoDTO(docExp);
		verRegistroAccesosDocumento(doc.getIdDocumento());
	}
	
	public void verRegistroAccesosDocumento(DocumentosTramiteMaestra docTr) {
		DocumentosExpedientes docExp = documentosExpedientesService.obtener(docTr.getId());
		documento = documentosExpedientesService.cargarDocumentoDTO(docExp);
		verRegistroAccesosDocumento(docTr.getIdDocumento());
	}
	
	//----
	
	private void verRegistroAccesosDocumento(Long idDocumento) {
		inicializaLazyModelAccesosDocumento(idDocumento);
	}
		
	private void inicializaLazyModelAccesosDocumento(Long idDocumento) {
		lazyModelAccesosDocumento = new LazyDataModelByQueryService<>(AccesosDocumentos.class, 
				accesosDocumentosService);
		lazyModelAccesosDocumento.setPreproceso((a, b, c, filters) -> {
			if (c!=null) { //c: multiSortMeta
				final SortMeta byFechaDesc = SortMeta.builder().field("fechaHoraAcceso").order(SortOrder.DESCENDING).priority(c.size()+1).build();
				c.put("fechaHoraAcceso", byFechaDesc);
			}
			
			filters.put("documento.id", new MyFilterMeta(idDocumento));
		});
	}
	
	//----------
	
	public void guardarPropiedades() {
		
		try {
			if(validarGuardado()) {	
				documentosExpedientesService.guardar(documento);
				
				PrimeFaces.current().ajax().addCallbackParam("saved", true);
				msgDocActualizado();
						
				DocumentosExpedientes documentoExpedienteAux = documentosExpedientesService.obtener(documento.getId());
				expedientes = utilsComun.expedienteUltimaModificacion(expedientes,documentoExpedienteAux.getFechaModificacion(),documentoExpedienteAux.getFechaCreacion(),documentoExpedienteAux.getUsuModificacion(),documentoExpedienteAux.getUsuCreacion());
				datosExpedientesBean.actualizarCabecera(expedientes,null,null,null);
				
				cargarFiltroTiposDoc();
				actualizarDocumentos();
			}
		} catch (BaseException e) {
			e.printStackTrace();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(CLAVE_ERROR_GENERICO));
			PrimeFaces.current().dialog().showMessageDynamic(message);
		}
	}
	
	private boolean validarGuardado() {
		if(StringUtils.isBlank(documento.getDescripcion())){
			facesMsgErrorKey(CLAVE_CAMPOS_OBLIGATORIOS);
			return false;
		}
		
		//Descripción no es null aquí
		if(documento.getDescripcion().contains(".")){
			facesMsgErrorKey("descripcion.puntos.no.permitidos");
			return false;
		}
		
		return true;
	}
	
	private void msgDocActualizado() {
		String txt = getMessages("documento", ACTUALIZADOCORRECTAMENTE);
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "", txt);
		FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, message);
	}
	
	//-----------------------------------------------
		
	public boolean documentoEnTramiteSup(DocumentosTramiteMaestra docm) {
		return this.documentosExpedientesService.documentoVinculadoSup(docm.getIdDocExpTramite());
	}
	
	public boolean documentoEnResolucion(DocumentosTramiteMaestra docm, String numResolucion) {

		Resolucion resolucion = this.resolucionService.findResolucionByNumeroResolucion(numResolucion);
		
		List<DocumentoResolucion> listDocRes = this.documentoResolucionService.findDocumentosResolucionByIdResolIdDoc(resolucion.getId(),docm.getId());

		boolean existeDoc = true;
		
		if (listDocRes.isEmpty()) {
			existeDoc = false;
		}
		
		return existeDoc;
	}
	
	public boolean documentoEnResolucionWeb(DocumentosTramiteMaestra docm) {

		List<ResolucionExpediente> resolExpedientes = resolucionExpedienteService.findListResolucionExpByIdExpediente(docm.getIdExpediente());

		boolean existeDoc = true;

		for(ResolucionExpediente resolExp : resolExpedientes){
		
			Resolucion resolucion = this.resolucionService.findResolucionByNumeroResolucion(resolExp.getResolucion().getCodigoResolucion());
			
			if (resolucion.getFechaPublicacionWeb() == null) {
			
				List<DocumentoResolucion> listDocRes = this.documentoResolucionService.findDocumentosResolucionByIdResolIdDoc(resolucion.getId(),docm.getId());
				
				if (listDocRes.isEmpty()) {
					existeDoc = false;
				}
			}
		}
		return existeDoc;
	}
	
	public void vincularDocumentoEnTramiteSup(DocumentosTramiteMaestra docm) {
		
		try {
			this.documentosExpedientesService.vincularDocumentoEnTramiteSup(docm.getIdDocExpTramite());
			
			//Actualizamos la propia tabla y la del trámite superior
			actualizarDocumentos();
			
			String txt = mensajesProperties.getString("asociacion.tramite") + " " + mensajesProperties.getString(ACTUALIZADACORRECTAMENTE);
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "", txt);
			FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, message);
			
		} catch (BaseException e) {
			facesMsgErrorGenerico();
			e.printStackTrace();
		}
		
	}
	
	public void vincularDocumentoResolucion(String numResolucion, DocumentosTramiteMaestra docm) {
			
		Resolucion resolucion = this.resolucionService.findResolucionByNumeroResolucion(numResolucion);
		
		DocumentoResolucion docResol = new DocumentoResolucion();
		final DocumentosExpedientesTramites docExpTram = documentosExpedientesTramitesService.obtener(docm.getIdDocExpTramite());
		
		docResol.setResolucion(resolucion);
		docResol.setDocumentoExpediente(docExpTram.getDocumentoExpediente());
		docResol.setAnonimizado(docExpTram.getDocumentoExpediente().getDocumento().getAnonimizado());
		docResol.setFechaPublicacionWeb(null);
		
		try {
			documentoResolucionService.guardar(docResol);
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
					mensajesProperties.getString(VINCULADOCORRECTO));
			PrimeFaces.current().dialog().showMessageDynamic(message);

		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
			
	}
		
	public void vincularDocumentoResolucionWeb(DocumentosTramiteMaestra docm) {
		
		List<ResolucionExpediente> resolExpedientes = resolucionExpedienteService.findListResolucionExpByIdExpediente(docm.getIdExpediente());
		
		for(ResolucionExpediente resolExp : resolExpedientes){
		
		Resolucion resolucion = this.resolucionService.findResolucionByNumeroResolucion(resolExp.getResolucion().getCodigoResolucion());
		
		if(resolucion.getFechaPublicacionWeb() == null) {
		
		DocumentoResolucion docResol = new DocumentoResolucion();
		final DocumentosExpedientesTramites docExpTram = documentosExpedientesTramitesService.obtener(docm.getIdDocExpTramite());
		
		docResol.setResolucion(resolucion);
		docResol.setDocumentoExpediente(docExpTram.getDocumentoExpediente());
		docResol.setAnonimizado(docExpTram.getDocumentoExpediente().getDocumento().getAnonimizado());
		docResol.setFechaPublicacionWeb(null);
		
		try {
			documentoResolucionService.guardar(docResol);

			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
					mensajesProperties.getString(VINCULADOCORRECTO));
			PrimeFaces.current().dialog().showMessageDynamic(message);

		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
		}
		}
	}
	
	//**********************************************
	// Listados de documentos por trámite
	
	private void cargarPanelDocumentos() {
		listaCategoriasDocumentosExpediente = new CargaArbolDocumentos().cargarDocumentos();
	}

	
	private class CargaArbolDocumentos {
		
		Map<Long, CategoriaDocDTO> mapCategorias;
		
		List<CategoriaDocDTO> cargarDocumentos() {
			mapCategorias = new HashMap<>();
			List<DocumentosTramiteMaestra> docs = documentosTramiteMaestraService.findDocumentosExpediente(getExpedientes().getId(), tipoDocumentoIdFiltro, verVinculadosFiltro);
			
			for(DocumentosTramiteMaestra docTr : docs) {
				if(Boolean.TRUE.equals(docTr.getVerEnPestDocumentos())) {
					getCategoria(docTr).addDocumento(docTr);
				}
			}
			
			var lista = mapCategorias.values().stream()
					.map(c-> { c.finCargaDocumentos(); return c; })
					.collect(Collectors.toList());
			Collections.sort(lista);
			
			return lista;
		}		
		
		private CategoriaDocDTO getCategoria(DocumentosTramiteMaestra docTr) {
			final Long key = docTr.getIdCategoria();
			
			if(!mapCategorias.containsKey(key)) {
				mapCategorias.put(key, this.crearCategoria(docTr));
			}
			
			return mapCategorias.get(key);
		}
		
		private CategoriaDocDTO crearCategoria(DocumentosTramiteMaestra docTr) {
			CategoriaDocDTO dto = new CategoriaDocDTO();
			dto.setId(docTr.getIdCategoria());
			dto.setDescripcion(docTr.getCategoria());
			dto.setOrden(docTr.getOrdenCategoria());
			
			return dto;
		}

	}
	
	//***************************************
	
	private DocumentosPorTramite documentosPorTramite;
	
	private class DocumentosPorTramite implements Serializable {
		
		private static final long serialVersionUID = 1L;
		
		/** En trámites para las categorías usamos objetos AgrupacionDTO mapeados por id de trámite */ 
		
		private transient Map<Long, TramiteDocDTO> mapTramitesDoc;
		
		void cargarDocumentos(Long idTramite) {
			List<DocumentosTramiteMaestra> docs = documentosTramiteMaestraService.findDocumentosTramite(idTramite);
			mapTramitesDoc = new HashMap<>();
			
			for(DocumentosTramiteMaestra docTr : docs) {
				getTramiteDoc(docTr).addDocumento(docTr);
			}
		}
		
		void cargarDocumentos() {
			List<DocumentosTramiteMaestra> docs = documentosTramiteMaestraService.findDocumentosExpediente(getExpedientes().getId());
			mapTramitesDoc = new HashMap<>();
			
			for(DocumentosTramiteMaestra docTr : docs) {
				getTramiteDoc(docTr).addDocumento(docTr);
			}
		}		
		
		private TramiteDocDTO getTramiteDoc(DocumentosTramiteMaestra docTr) {
			Long key = docTr.getIdTramiteExpediente();
			
			if(!mapTramitesDoc.containsKey(key)) {
				mapTramitesDoc.put(key, crearTramiteDoc(docTr));				
			}
			
			return mapTramitesDoc.get(key);
		}
		
		private TramiteDocDTO crearTramiteDoc(DocumentosTramiteMaestra docTr) {
			TramiteDocDTO dto = new TramiteDocDTO();
			dto.setId(docTr.getIdTramiteExpediente());
			return dto;
		}
			
		//Obtenemos los documentos consultando todos los del expediente sin no están cargados
		List<AgrupacionDTO> getCategorias(Long idTramite) {
			if(null == mapTramitesDoc) {
				this.cargarDocumentos();
			}
			
			var tramDoc = this.mapTramitesDoc.get(idTramite);
			
			return (tramDoc != null)?
						tramDoc.getAgrupaciones() :
							Collections.emptyList();
		}
		
		//Obtenemos los documentos pero consultando solo el trámite
		List<AgrupacionDTO> getCategoriasSoloTramite(Long idTramite) {
			this.cargarDocumentos(idTramite);
			
			//Hemos precargado mapTramitesDoc solo para el trámite, podemos hacer la llamada normal
			return getCategorias(idTramite);
		}

	}
	
	public List<AgrupacionDTO> getCategoriasTramite(Long idTramite) {
		return idTramite == null? Collections.emptyList() :
				this.documentosPorTramite.getCategorias(idTramite);
	}
	
	public List<AgrupacionDTO> getCategoriasSoloTramite(Long idTramite) {
		return idTramite == null? Collections.emptyList() :
				this.documentosPorTramite.getCategoriasSoloTramite(idTramite);
	}
	
	public void actualizarDocumentos() {
		cargarPanelDocumentos();
		this.documentosPorTramite.cargarDocumentos();			
	}
	
	//Para un trámite específico
	public void actualizarDocumentosTramite(Long idTramite) {
		this.documentosPorTramite.cargarDocumentos(idTramite);			
	}	
	
}
