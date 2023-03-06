package es.juntadeandalucia.ctpda.gestionpdt.web.expedientes;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.Vector;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.apache.commons.lang3.math.NumberUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.model.file.UploadedFiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.CfgDocExpedienteDescripcion;
import es.juntadeandalucia.ctpda.gestionpdt.model.CfgDocExpedienteTramitacion;
import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentoDTO;
import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.TipoTramite;
import es.juntadeandalucia.ctpda.gestionpdt.model.TramiteExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.service.CfgDocExpedienteDescripcionService;
import es.juntadeandalucia.ctpda.gestionpdt.service.CfgDocExpedienteTramitacionService;
import es.juntadeandalucia.ctpda.gestionpdt.service.DocumentosExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.DocumentosService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.TipoTramiteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.TramiteExpedienteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ValoresDominioService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.ValidacionException;
import es.juntadeandalucia.ctpda.gestionpdt.util.FileUtils;
import es.juntadeandalucia.ctpda.gestionpdt.util.StringUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.Constantes;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.JsfUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.BaseBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@ViewScoped
@Slf4j
public class IncorporarDocumentosBean extends BaseBean implements Serializable {
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	private static final String MENSAJESFORMULARIO = "messagesFormulario";
	@Autowired
	private DatosExpedientesDocumentosBean datosExpedientesDocumentosBean;	
	
	@Autowired
	private DocumentosService documentosService;
	@Autowired
	private ValoresDominioService valoresDominioService;
	@Autowired
	private DocumentosExpedientesService documentosExpedientesService;
	@Autowired
	private CfgDocExpedienteTramitacionService cfgDocExpedienteTramitacionService;
	@Autowired
	private CfgDocExpedienteDescripcionService cfgDocExpedienteDescripcionService;
	@Autowired
	private TramiteExpedienteService tramiteExpedienteService;

	@Getter @Setter
	private String extensionesPermitidas;
	@Getter @Setter
	private String strExtensionesPermitidas;
	@Getter @Setter
	private List<CfgDocExpedienteTramitacion> listaTiposDocumentos;
	@Getter @Setter
	private List<ValoresDominio> listaCategoriasDocumentos;
	
	private CfgDocExpedienteTramitacion defaultCfgTipoDoc;	
	
	//@Getter @Setter
	private List<DocumentoDTO> listaDocumentosIncorporar;
	@Getter @Setter
	private DocumentoDTO documento; //documento actual
	@Getter @Setter
	private Expedientes expedientes;
	@Getter @Setter
	private TramiteExpediente tramiteExpediente;

	@Getter @Setter
	private UploadedFiles ficheros;
	
	@Getter @Setter
	private boolean permisoIncorporar;
	@Getter @Setter
	private boolean permisoSubir;

	@Getter @Setter
	private List<String> listaCodigoPermisos;
	
	@Autowired
	TipoTramiteService tipoTramiteService;
	@Autowired
	private ExpedientesService expedientesService;
	
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
		
		documento = new DocumentoDTO();
		
		final String[] ss = documentosService.arrayDeExtensionesPermitidas();
		strExtensionesPermitidas = StringUtils.join(ss, ", ");
		extensionesPermitidas = StringUtils.join(ss, "|");
		
		cargarPermisos();

		listaCategoriasDocumentos = valoresDominioService.findValoresCategoriasDocumento();
	}
	
	private Expedientes getExpediente() {
		return (Expedientes)JsfUtils.getSessionAttribute("expedienteFormulario");
	}
	
	@SuppressWarnings("unchecked")
	private void cargarPermisos() {
		listaCodigoPermisos = (List<String>) JsfUtils.getSessionAttribute(Constantes.LISTACODIGOPERMISOS);
		permisoIncorporar=listaCodigoPermisos.contains(Constantes.PERMISO_NEW_EXPINCDOC);
		permisoSubir=listaCodigoPermisos.contains(Constantes.PERMISO_NEW_EXPSUBDOC);	
	}
	
	public void abrirDialogoIncorporarDocumentos(Long idTramExp) {
		documento = new DocumentoDTO();
		expedientes = getExpediente();

		crearListaDocumentosIncorporar();
		
		if(expedientes.getId()!=null) {
			cabeceraDialog=getMessage("incorporar.documentos")+" para el expediente "+ expedientes.getNumExpediente();
		}else {
			cabeceraDialog=getMessage("incorporar.documentos");
		}
		
		this.tramiteExpediente = tramiteExpedienteService.obtener(idTramExp);
		try {
			cargarTiposDocumentos();
			
			PrimeFaces.current().executeScript("PF('dialogSubirDocumentos').show();");
		} catch (ValidacionException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString("no.existe.registro.config"));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error("abrirDialogoIncorporarDocumentos - " + message.getDetail());
		}
	}

	public void subirDocumentos() {
		//nada
	}
	
	public void retirarDocumento(int index) {
		this.getListaDocumentosIncorporar().remove(index);	
	}
	
	
	private void cargarTiposDocumentos() throws ValidacionException {
		final ValoresDominio tipoExp = expedientes.getValorTipoExpediente();
		final ValoresDominio sit = expedientes.getValorSituacionExpediente();
		final TipoTramite tipoTr = tramiteExpediente.getTipoTramite();
		
		TramiteExpediente trSup = tramiteExpediente.getTramiteExpedienteSup();
		final TipoTramite tipoTrSup = (trSup == null)? null : trSup.getTipoTramite();
		
		this.listaTiposDocumentos = cfgDocExpedienteTramitacionService.findTiposDocumentosCfg(tipoExp, sit, tipoTr, tipoTrSup, null);
		
		if(NumberUtils.INTEGER_ONE == listaTiposDocumentos.size()) {
			this.defaultCfgTipoDoc = listaTiposDocumentos.get(0);
		} else {
			this.defaultCfgTipoDoc = this.listaTiposDocumentos.stream()
					.filter(t -> t.getTipoDocumento().getCodigo().equals(Constantes.TIP_DOC_OTRO))
					.findFirst().orElse(null);
		}
	}	
	
	public void onSelectTipoDoc(DocumentoDTO dto) {
		this.documento = dto;
		
		CfgDocExpedienteTramitacion cfg = cfgDocExpedienteTramitacionService.obtener(dto.getCfgDocExpTramitacionId());
		dto.setTipoDocumentoId(cfg.getTipoDocumento().getId());
		
		cargarDescripcionesTipoDoc(dto);
	}
	
	private void cargarDescripcionesTipoDoc(DocumentoDTO dto) {
		List<CfgDocExpedienteDescripcion> listaCfg = cfgDocExpedienteDescripcionService.findDescripcionesTipoDocCfg(dto.getCfgDocExpTramitacionId());
		
		if(!listaCfg.isEmpty()) {
			cfgDocExpedienteDescripcionService.aplicarCfgIndicadores(listaCfg.get(0), dto);
		} else {
			dto.setCategoriaId(null);
		}
		
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
	
	private boolean validarIncorporacion() {
		
		for(DocumentoDTO dto : this.getListaDocumentosIncorporar()) {
			if(null == dto.getCfgDocExpTramitacionId()
					|| StringUtils.isBlank(dto.getDescripcion())
					|| null == dto.getCategoriaId()) {
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
	
	public void finalizarIncorporacion() {
		
			if(!validarIncorporacion()) {
				return;
			}
			
			try {
				documentosExpedientesService.incorporar(this.getListaDocumentosIncorporar(), expedientes, tramiteExpediente);
				PrimeFaces.current().ajax().addCallbackParam("saved", true);
				datosExpedientesDocumentosBean.cargarFiltroTiposDoc();
				
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "", mensajesProperties.getString("exito.incorporados"));
				FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, message);
				
				expedientes = expedientesService.obtener(expedientes.getId());
				datosExpedientesBean.actualizarCabecera(expedientes,null,null,null);
				
			} catch (ValidacionException e) {
				for(String msg : e.getErrors()) {
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", msg);
					PrimeFaces.current().dialog().showMessageDynamic(message);
				}
			} catch (BaseException e) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(CLAVE_ERROR_GENERICO));
				PrimeFaces.current().dialog().showMessageDynamic(message);
				e.printStackTrace();
			}
	}	
	
	//---------------------------------
	// Manejo de FileUlpload
	    
    public void handleFileUpload(FileUploadEvent event) {
		addUploadedFile(event.getFile());        
    }
    
    //------
    
    public void addUploadedFile(UploadedFile f) {
		addBloqueDoc(f);        
    }
	
	private void addBloqueDoc(UploadedFile upfile) {
		DocumentoDTO dto = new DocumentoDTO();
		dto.setNombreFichero(upfile.getFileName());
		dto.setDescripcion(FileUtils.getNombreSinExt(upfile.getFileName()));
		dto.setBytes(upfile.getContent());
		dto.setDeTrabajo(true);
		
		if(this.defaultCfgTipoDoc != null) {			
			dto.setTipoDocumentoId(this.defaultCfgTipoDoc.getTipoDocumento().getId());
			dto.setCfgDocExpTramitacionId(this.defaultCfgTipoDoc.getId());
		}

		cargarDescripcionesTipoDoc(dto);

		this.getListaDocumentosIncorporar().add(dto);
	}
	
	private void crearListaDocumentosIncorporar() {
		//Parece que FileUpload múltiple no lleva bien la sesión + ViewScope
		//https://stackoverflow.com/questions/8875818/how-to-use-primefaces-pfileupload-listener-method-is-never-invoked-or-uploaded/8880083#8880083	
		//Al subir múltiples ficheros ficheros en distintas peticiones el acceso a 
		//la lista es potencialmente concurrente. No podemos usar ArrayList.		
		listaDocumentosIncorporar = new Vector<>(10);
	}
	
	public List<DocumentoDTO> getListaDocumentosIncorporar() {
		return listaDocumentosIncorporar; 
	}
		
	// Fin FileUpoad
	//---------------------------------------
		
}
