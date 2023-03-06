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
import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentosMaestra;
import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.TipoTramite;
import es.juntadeandalucia.ctpda.gestionpdt.model.TramiteExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.service.CfgDocExpedienteDescripcionService;
import es.juntadeandalucia.ctpda.gestionpdt.service.CfgDocExpedienteTramitacionService;
import es.juntadeandalucia.ctpda.gestionpdt.service.DocumentosExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.DocumentosMaestraService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.TramiteExpedienteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ValoresDominioService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.ValidacionException;
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
public class VincularDocumentosBean extends BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String MENSAJESFORMULARIO = "messagesFormulario";

	@Autowired
	private DatosExpedientesDocumentosBean datosExpedientesDocumentosBean;	
	
	@Autowired
	private ExpedientesService expedientesService;
	@Autowired
	private ValoresDominioService valoresDominioService;
	@Autowired
	private DocumentosMaestraService documentosMaestraService;
	@Autowired
	private DocumentosExpedientesService documentosExpedientesService;
	@Autowired
	private CfgDocExpedienteTramitacionService cfgDocExpedienteTramitacionService;
	@Autowired
	private CfgDocExpedienteDescripcionService cfgDocExpedienteDescripcionService;
	@Autowired
	private TramiteExpedienteService tramiteExpedienteService;

	@Getter	@Setter
	private Expedientes expedienteOrigen;
	@Getter
	private TramiteExpediente tramiteExpediente;	
	@Getter
	private Long idTramiteExpedienteSup;	
	@Getter
	private LazyDataModelByQueryService<DocumentosMaestra> lazyModelDocumentos;

	@Getter	@Setter
	private String filtroNumExpediente;
	@Getter	@Setter
	private List<DocumentosMaestra> documentosSeleccionados;

	@Getter
	private List<DocumentoDTO> listaDocumentosVincular;
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
	private boolean permisoVincular;
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
		
		expedienteOrigen = null;

		documento = new DocumentoDTO();
		
		initLazyDocumentos();
		
		recogerPermisosUsuario();
		permisoVincular=permisoConcedido(Constantes.PERMISO_NEW_EXPVINCDOC);
		
		listaCategoriasDocumentos = valoresDominioService.findValoresCategoriasDocumento();
	}
	
	private void initLazyDocumentos() {
		lazyModelDocumentos = new LazyDataModelByQueryService<>(DocumentosMaestra.class, documentosMaestraService);

		final SortMeta docIdenSort = SortMeta.builder().field("identificadorDoc").order(SortOrder.DESCENDING).build();

		lazyModelDocumentos.setPreproceso((a, b, c, filters) -> {
			/** añadamos siempre un orden por id en los casos de vistas */
			if (c!=null) {
				final SortMeta ordenacionById = SortMeta.builder().field("id").order(SortOrder.ASCENDING).priority(c.size()+1).build();
				c.put("id", ordenacionById);
			}
			
			//tengo asegurado que expediente!=null siempre
			filters.put("idExpediente", new MyFilterMeta(expedienteOrigen.getId()));
			if(hayFiltroSoloTramiteSup()) {
				filters.put("#documentosExpDelTramite", new MyFilterMeta(idTramiteExpedienteSup));
			}
			filters.put("#documentosExpNoEnTramite", new MyFilterMeta(tramiteExpediente.getId()));
			
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
		return (Expedientes)JsfUtils.getSessionAttribute("expedienteFormulario");
	}
	
	public void abrirDialogoVincularDocumentos(Long idTramExp) {
		documento = new DocumentoDTO();
		filtroNumExpediente = getExpedienteFormulario().getNumExpediente();
		expedienteOrigen = null;
		deshabilitarBusqueda = false;	

		listaDocumentosVincular = null;
		documentosSeleccionados = null;
		
		tramiteExpediente = tramiteExpedienteService.obtener(idTramExp);
		idTramiteExpedienteSup = null;
		hayTramiteSup = tramiteExpediente.getEsSubtramite();
		
		if(hayTramiteSup) {
			consultarNumExpediente();
			idTramiteExpedienteSup = tramiteExpediente.getTramiteExpedienteSup().getId();
		}
		
		cabeceraDialog = getMessage("relacionar.documentos") + " para el expediente " + filtroNumExpediente;
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
		expedienteOrigen = expedientesService.findByNumeroExpediente(filtroNumExpediente).orElse(expedienteVacio());
		listaDocumentosVincular = null;
		
		deshabilitarBusqueda = hayFiltroSoloTramiteSup()
				|| documentosMaestraService.hayDocumentosVinculables(tramiteExpediente.getId());
	}
	
	private boolean hayFiltroSoloTramiteSup() {
		return getEsFiltroExpedienteActual() && hayTramiteSup;
	}
	
	private Expedientes expedienteVacio() {
		final Expedientes e = new Expedientes();
		e.setId(0L);
		
		return e;
	}
	
	//-----------------------------------------------
	
	public void recogerDocumentosSeleccionados(ActionEvent ev)  {
		if(documentosSeleccionados.isEmpty()) {
			facesMsgErrorKey("documentos.no.seleccionados");
			return;
		}
		
		listaDocumentosVincular = documentosMaestraService.convertirADocumentoDTO(documentosSeleccionados);

		try {
			cargarTiposDocumentos();
			
			for(DocumentoDTO dto : listaDocumentosVincular) {
				setIdCfgTramitacion(dto);
				cargarDescripcionesTipoDoc(dto);
			}
			
			//Si los doc. a vincular son de otro expediente se guardarán como nuevos objetos 
			//documento-expediente y por tanto tendrán su propia información de trabajo
			if(!this.expedienteOrigen.getId().equals(this.getExpedienteFormulario().getId())) {
				for(DocumentoDTO dto : listaDocumentosVincular) {
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
		this.getListaDocumentosVincular().remove(index);	
	}
	
	private void cargarTiposDocumentos() throws ValidacionException {
		final Expedientes exp = getExpedienteFormulario();
		final ValoresDominio tipoExp = exp.getValorTipoExpediente();
		final ValoresDominio sit = exp.getValorSituacionExpediente();
		final TipoTramite tipoTr = tramiteExpediente.getTipoTramite();
		
		TramiteExpediente trSup = tramiteExpediente.getTramiteExpedienteSup();
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
	
	//********************************************************
	
	public void vistaPreviaFichero(DocumentoDTO dto) {
		comunDocumentosBean.vistaPreviaFichero(dto);
	}

	//********************************************************
	
	private boolean validarVinculacion() {
		
		for(DocumentoDTO dto : this.getListaDocumentosVincular()) {
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
	
	public void finalizarVinculacion() {
		
			if(!validarVinculacion()) {
				return;
			}
			
			try {
				documentosExpedientesService.vincular(this.getListaDocumentosVincular(), tramiteExpediente);
				PrimeFaces.current().ajax().addCallbackParam("saved", true);
				datosExpedientesDocumentosBean.cargarFiltroTiposDoc();
				
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "", mensajesProperties.getString("exito.vinculados"));
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
