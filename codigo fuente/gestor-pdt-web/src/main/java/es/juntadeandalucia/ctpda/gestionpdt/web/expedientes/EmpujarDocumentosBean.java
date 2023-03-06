package es.juntadeandalucia.ctpda.gestionpdt.web.expedientes;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;

import org.primefaces.PrimeFaces;
import org.primefaces.component.dialog.Dialog;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.CfgDocExpedienteDescripcion;
import es.juntadeandalucia.ctpda.gestionpdt.model.CfgDocExpedienteTramitacion;
import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentoDTO;
import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentosTramiteMaestra;
import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.TipoTramite;
import es.juntadeandalucia.ctpda.gestionpdt.model.TramiteExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.service.CfgDocExpedienteDescripcionService;
import es.juntadeandalucia.ctpda.gestionpdt.service.CfgDocExpedienteTramitacionService;
import es.juntadeandalucia.ctpda.gestionpdt.service.DocumentosExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.DocumentosTramiteMaestraService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.TramiteExpedienteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ValoresDominioService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.ValidacionException;
import es.juntadeandalucia.ctpda.gestionpdt.util.ListUtils;
import es.juntadeandalucia.ctpda.gestionpdt.util.StringUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.Constantes;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.JsfUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.LazyDataModelByQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.MyFilterMeta;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.BaseBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@ViewScoped
@Slf4j
public class EmpujarDocumentosBean extends BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String MENSAJESFORMULARIO = "messagesFormulario";
	private static final String DIALOG_EMPUJAR = "dialogEmpujarDocs";

	@Autowired
	private DatosExpedientesDocumentosBean datosExpedientesDocumentosBean;	
	
	@Autowired
	private ExpedientesService expedientesService;
	@Autowired
	private ValoresDominioService valoresDominioService;
	@Autowired
	private DocumentosTramiteMaestraService documentosTramiteMaestraService;
	@Autowired
	private DocumentosExpedientesService documentosExpedientesService;
	@Autowired
	private CfgDocExpedienteTramitacionService cfgDocExpedienteTramitacionService;
	@Autowired
	private CfgDocExpedienteDescripcionService cfgDocExpedienteDescripcionService;
	@Autowired
	private TramiteExpedienteService tramiteExpedienteService;

	@Getter	@Setter
	private Expedientes expedienteDestino;
	@Getter
	private TramiteExpediente tramiteOrigen;
	@Getter
	private TramiteExpediente tramiteDestino;
	@Getter
	private List<TramiteExpediente> listaTramitesExpediente;
	@Getter	@Setter
	private Long selectedTramiteExpedienteId;
	
	//@Getter
	//private Long idTramiteExpedienteSup;	
	@Getter
	private LazyDataModelByQueryService<DocumentosTramiteMaestra> lazyModelDocumentos;

	@Getter	@Setter
	private String filtroNumExpediente;
	@Getter	@Setter
	private List<DocumentosTramiteMaestra> documentosSeleccionados;

	@Getter
	private List<DocumentoDTO> listaDocumentosEmpujar;
	@Getter @Setter
	private DocumentoDTO documento; //documento actual
	
	
	@Getter
	private boolean deshabilitarBusqueda;
	
	@Getter	@Setter
	private List<CfgDocExpedienteTramitacion> listaTiposDocumentos;
	@Getter @Setter
	private List<ValoresDominio> listaCategoriasDocumentos;
	@Getter	@Setter
	private Long cfgDocExpDescripcionId;

	@Getter @Setter
	private List<String> listaCodigoPermisos;
	@Getter
	private boolean permisoEmpujar;
	@Getter
	private boolean hayTramiteSup;

	
	@Autowired
	private DatosExpedientesBean datosExpedientesBean;
	@Autowired
	private ComunDocumentosBean comunDocumentosBean;
	
	@Getter @Setter
	private String cabeceraDialog;


	@Override
	@PostConstruct
	public void init() {
		super.init();
		
		expedienteDestino = null;

		documento = new DocumentoDTO();
		
		initLazyDocumentos();
		
		recogerPermisosUsuario();
		permisoEmpujar=permisoConcedido(Constantes.PERMISO_NEW_EXPEMPDOC);
		
		listaCategoriasDocumentos = valoresDominioService.findValoresCategoriasDocumento();
	}
	
	private void initLazyDocumentos() {
		lazyModelDocumentos = new LazyDataModelByQueryService<>(DocumentosTramiteMaestra.class, documentosTramiteMaestraService);

		final SortMeta docIdenSort = SortMeta.builder().field("identificadorDoc").order(SortOrder.DESCENDING).build();

		lazyModelDocumentos.setPreproceso((a, b, c, filters) -> {
			/** añadamos siempre un orden por id en los casos de vistas */
			if (c!=null) {
				final SortMeta ordenacionById = SortMeta.builder().field("id").order(SortOrder.ASCENDING).priority(c.size()+1).build();
				c.put("id", ordenacionById);
			}
			
			//tengo asegurado que expediente!=null siempre
			filters.put("idTramiteExpediente", new MyFilterMeta(tramiteOrigen.getId()));
			filters.put("#documentosDelTramite", new MyFilterMeta(tramiteOrigen.getId()));

			if(tramiteDestino != null) {
				filters.put("#documentosNoEnTramite", new MyFilterMeta(tramiteDestino.getId()));
			}
			
			c.put("identificadorDoc", docIdenSort);
		});
	
	}
	
	@SuppressWarnings("unchecked")
	private void recogerPermisosUsuario() {
		listaCodigoPermisos = (List<String>) JsfUtils.getSessionAttribute(Constantes.LISTACODIGOPERMISOS);	
	}
	
	private boolean permisoConcedido(String codPermiso) {
		return listaCodigoPermisos.contains(codPermiso);
	}
	
	//-------------------------------
	
	public Expedientes getExpedienteFormulario() {
		return tramiteOrigen.getExpediente();
	}
	
	public void abrirDialogoEmpujarDocumentos(Long idTramExp) {
		tramiteOrigen = tramiteExpedienteService.obtener(idTramExp);
		var expediente = tramiteOrigen.getExpediente();
		expedienteDestino = expediente; //Por defecto comenzamos en el mismo expediente
		filtroNumExpediente = expediente.getNumExpediente();
		
		documento = new DocumentoDTO();
		deshabilitarBusqueda = false;	

		listaDocumentosEmpujar = null;
		documentosSeleccionados = null;
		
		cargarListaTramites();		
		setTextoCabecera();
	}
	
	private void setTextoCabecera() {
		final StringBuilder sb = new StringBuilder();
		
		sb.append(getMessage("vincular.documentos"))
			.append(StringUtils.SPACE)
			.append(getMessage("desde.tramite"))
			.append(StringUtils.SPACE)
			.append("'")
			.append(tramiteOrigen.getDescripcion())
			.append("'");
		
		cabeceraDialog = StringUtils.ptoSusp(sb.toString(), 100);
	}
	
	public void aplicarNumExpActual() {
		//El número de expediente actual ya está aplicado con setPropertyActionListener
	}
	
	public boolean getEsFiltroExpedienteActual() {
		final Expedientes exp = getExpedienteFormulario();
		if(exp == null) return false;
		
		final String num = exp.getNumExpediente();
		if(num == null) return false;

		return num.equals(filtroNumExpediente);
	}
	
	public void consultarNumExpediente() {
		expedienteDestino = expedientesService.findByNumeroExpediente(filtroNumExpediente).orElse(expedienteVacio());
		cargarListaTramites();
	}
	
	private void cargarListaTramites() {
		if(expedienteDestino.getId().equals(0L)) {
			listaTramitesExpediente = null;
		} else { //Leo todos los trámites activos, estén abiertos o finalizados.
			listaTramitesExpediente = tramiteExpedienteService.findTramitesSupExp(expedienteDestino.getId());		
			//Elimino el trámite acual de la lista, si está
			listaTramitesExpediente = ListUtils.filter(listaTramitesExpediente, 
											t -> !t.getId().equals(tramiteOrigen.getId()));
		}

		selectedTramiteExpedienteId = null;
		tramiteDestino = null;
		deshabilitarBusqueda = false;
	}
	
	private Expedientes expedienteVacio() {
		final Expedientes e = new Expedientes();
		e.setId(0L);
		
		return e;
	}
	
	//----------------
	
	public void consultarDocumentos() {
		tramiteDestino = null;
		deshabilitarBusqueda = false;
		
		if(selectedTramiteExpedienteId == null) {
			facesMsgErrorKey("tramite.no.seleccionado");
			return;
		}
		
		//boolean hayDocs = documentosTramiteMaestraService.hayDocumentosVinculables(tramiteOrigen.getId(), selectedTramiteExpedienteId);
		/*if(!hayDocs) {
			//facesMsgErrorKey("documentos.vinculables.no.encontrados");
			return;
		}*/
		
		tramiteDestino = tramiteExpedienteService.obtener(selectedTramiteExpedienteId);
		deshabilitarBusqueda = true;
		
		PrimeFaces.current().ajax().update(Constantes.JSFID_PESTANAS_EXP + ":" + DIALOG_EMPUJAR);
		PrimeFaces.current().executeScript("PF('" + DIALOG_EMPUJAR + "').show()"); //update() me oculta el diálogo
	}
	
	//-----------------------------------------------
	
	public void recogerDocumentosSeleccionados(ActionEvent ev)  {
		if(documentosSeleccionados.isEmpty()) {
			facesMsgErrorKey("documentos.no.seleccionados");
			return;
		}
		
		listaDocumentosEmpujar = documentosTramiteMaestraService.convertirADocumentoDTO(documentosSeleccionados);

		try {
			cargarTiposDocumentos();
			
			for(DocumentoDTO dto : listaDocumentosEmpujar) {
				setIdCfgTramitacion(dto);
				cargarDescripcionesTipoDoc(dto);
			}
			
			//Si los doc. a empujar son de otro expediente se guardarán como nuevos objetos 
			//documento-expediente y por tanto tendrán su propia información de trabajo
			if(!this.expedienteDestino.getId().equals(this.getExpedienteFormulario().getId())) {
				for(DocumentoDTO dto : listaDocumentosEmpujar) {
					dto.setDeTrabajo(Boolean.TRUE);
				}
			}
			
			PrimeFaces.current().ajax().update(getDialog(ev.getComponent()));
			PrimeFaces.current().ajax().addCallbackParam("valid", true);
		} catch (ValidacionException e) {
			facesMsgErrorGenerico();
			e.printStackTrace();
		}
		
	}
	
	//Asumimos que comp es siempre un botón dentro de un diálogo
	private Dialog getDialog(UIComponent comp) {
		UIComponent cmpDlg;
		
		do {
			cmpDlg = comp.getParent();
		} while(cmpDlg != null && !(cmpDlg instanceof Dialog));
		
		return (Dialog) cmpDlg;
	}
	
	public void retirarDocumento(int index) {
		this.getListaDocumentosEmpujar().remove(index);	
	}
	
	private void cargarTiposDocumentos() throws ValidacionException {
		final Expedientes exp = getExpedienteFormulario();
		final ValoresDominio tipoExp = exp.getValorTipoExpediente();
		final ValoresDominio sit = exp.getValorSituacionExpediente();
		final TipoTramite tipoTr = tramiteOrigen.getTipoTramite();
		
		TramiteExpediente trSup = tramiteOrigen.getTramiteExpedienteSup();
		TipoTramite tipoTrSup = null;
		
		if(trSup != null) {
			trSup = tramiteExpedienteService.obtener(trSup.getId());
			tipoTrSup = trSup.getTipoTramite();
		}
		
		this.listaTiposDocumentos = cfgDocExpedienteTramitacionService.findTiposDocumentosCfg(tipoExp, sit, tipoTr, tipoTrSup, null);
	}

	private void setIdCfgTramitacion(DocumentoDTO dto) {
		Optional<CfgDocExpedienteTramitacion> op = this.listaTiposDocumentos.stream()
										.filter(cfg -> cfg.getTipoDocumento().getId().equals(dto.getTipoDocumentoId()))
										.findFirst();
		if(op.isPresent()) {
			dto.setCfgDocExpTramitacionId(op.get().getId());					
		}	
	}
	
	private void cargarDescripcionesTipoDoc(DocumentoDTO dto) {
		List<CfgDocExpedienteDescripcion> listaCfg = cfgDocExpedienteDescripcionService.findDescripcionesTipoDocCfg(dto.getCfgDocExpTramitacionId());		
		dto.setListaCfgDescripcionesTipoDoc(listaCfg);
	}
	
	public void onSelectDescripcion(DocumentoDTO dto) {
		this.documento = dto;
		Optional<CfgDocExpedienteDescripcion> op = dto.getListaCfgDescripcionesTipoDoc().stream()
									.filter(cfg -> cfg.getId().equals(dto.getCfgDocExpDescripcionId()))
									.findFirst();
		if(op.isPresent()) {
			cfgDocExpedienteDescripcionService.aplicarCfg(op.get(), dto);
		}
	}
	
	public void onAbrirDialogoDescripcion(DocumentoDTO dto) {
		this.documento = dto;
	}
		
	public void vistaPreviaFichero(DocumentoDTO dto) {
		comunDocumentosBean.vistaPreviaFichero(dto);
	}

	//********************************************************
	
	private boolean validarEmpuje() {
		
		for(DocumentoDTO dto : this.getListaDocumentosEmpujar()) {
			if(StringUtils.isBlank(dto.getDescripcion())) {
				facesMsgErrorKey(CLAVE_CAMPOS_OBLIGATORIOS);
				return false;
			}
			
			//Descripción no es null aquí
			if(dto.getDescripcion().contains(".")){
				facesMsgErrorKey("descripcion.puntos.no.permitidos");
				return false;
			}
		}
		
		return true;
	}
	
	public void finalizarEmpuje() {
			
			if(!validarEmpuje()) {
				return;
			}
						
			try {			
				//Al final empujar es vincular
				documentosExpedientesService.vincular(this.getListaDocumentosEmpujar(), tramiteDestino);
				PrimeFaces.current().ajax().addCallbackParam("saved", true);
				datosExpedientesDocumentosBean.cargarFiltroTiposDoc();
				
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "", mensajesProperties.getString("exito.empujados"));
				FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, message);
				
				Expedientes expedienteActualizar = expedientesService.obtener(getExpedienteFormulario().getId());
				datosExpedientesBean.actualizarCabecera(expedienteActualizar,null,null,null);
			} catch (ValidacionException e) {
				for(String msg : e.getErrors()) {
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", msg);
					PrimeFaces.current().dialog().showMessageDynamic(message);
				}
			} catch (BaseException e) {
				facesMsgErrorKey(CLAVE_ERROR_GENERICO);
				e.printStackTrace();
			}
	}

}
