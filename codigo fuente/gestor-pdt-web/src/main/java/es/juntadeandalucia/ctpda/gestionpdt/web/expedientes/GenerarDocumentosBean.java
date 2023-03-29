package es.juntadeandalucia.ctpda.gestionpdt.web.expedientes;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.CfgDocExpedienteDescripcion;
import es.juntadeandalucia.ctpda.gestionpdt.model.CfgDocExpedienteTramitacion;
import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentoDTO;
import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.PlantillasDocumentos;
import es.juntadeandalucia.ctpda.gestionpdt.model.TipoTramite;
import es.juntadeandalucia.ctpda.gestionpdt.model.TramiteExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.service.CfgDocExpedienteDescripcionService;
import es.juntadeandalucia.ctpda.gestionpdt.service.CfgDocExpedienteTramitacionService;
import es.juntadeandalucia.ctpda.gestionpdt.service.DocumentosExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.PlantillasDocumentosService;
import es.juntadeandalucia.ctpda.gestionpdt.service.TramiteExpedienteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ValoresDominioService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.ValidacionException;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.Constantes;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.JsfUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.BaseBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@ViewScoped
@Slf4j
public class GenerarDocumentosBean extends BaseBean implements Serializable {
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	private static final String MENSAJESFORMULARIO = "messagesFormulario";
	private static final String MENSAJE_GENERADO_OK = "documento.generado.correcto";
		
	@Autowired
	private DatosExpedientesBean datosExpedientesBean;
	@Autowired
	private ComunDocumentosBean comunDocumentosBean;
	
	@Autowired
	private ExpedientesService expedientesService;	
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
	@Autowired
	private PlantillasDocumentosService plantillasDocumentosService;	
	
	@Getter	@Setter
	private Long tipoDocumentoCfgId;
	@Getter	@Setter
	private List<CfgDocExpedienteTramitacion> listaTiposDocumentos;
	@Getter @Setter
	private List<ValoresDominio> listaCategoriasDocumentos;
	
	@Getter	@Setter
	private Long plantillaDocId;
	@Getter	@Setter
	private List<PlantillasDocumentos> listaPlantillas;
	
	@Getter	@Setter
	private DocumentoDTO documento;
	@Getter	@Setter
	private TramiteExpediente tramiteExpediente;
	
	@Getter	@Setter
	private Boolean documentoGenerado;

	@Getter	@Setter
	private UploadedFiles ficheros;
	
	@Getter	@Setter
	private boolean permisoGenerar;

	@Getter	@Setter
	private String cabeceraDialog;


	
	@Override
	@PostConstruct
	public void init() {
		super.init();
		
		documento = new DocumentoDTO();

		List<String> listaCodigoPermisos = getPermisosUsuario();		
		permisoGenerar=listaCodigoPermisos.contains(Constantes.PERMISO_NEW_EXPDOCGEN);

		listaCategoriasDocumentos = valoresDominioService.findValoresCategoriasDocumento();
	}
	
	private Expedientes getExpedienteFormulario() {
		return (Expedientes)JsfUtils.getSessionAttribute("expedienteFormulario");
	}
	
	@SuppressWarnings("unchecked")
	private List<String> getPermisosUsuario() {
		return (List<String>) JsfUtils.getSessionAttribute(Constantes.LISTACODIGOPERMISOS);
	}
	
	public void abrirDialogoGenerarDocumentos(Long idTramExp) {
		this.documento = new DocumentoDTO();
		this.tipoDocumentoCfgId = null;
		this.plantillaDocId = null;
		
		cabeceraDialog=getMessage("generar.documento")+" para el expediente "+ getExpedienteFormulario().getNumExpediente();
		
		try {
			cargarTiposDocumentos(idTramExp);
			
			PrimeFaces.current().executeScript("PF('dialogGenerarDocs').show();");
		} catch (ValidacionException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString("no.existe.registro.config"));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error("abrirDialogoGenerarDocumentos - " + message.getDetail());
		}		
	}
	
	private void cargarTiposDocumentos(Long idTramExp) throws ValidacionException {
		tramiteExpediente = tramiteExpedienteService.obtener(idTramExp);				
		final Expedientes exp = getExpedienteFormulario();
		
		listaTiposDocumentos = cfgDocExpedienteTramitacionService.findTiposDocumentosCfg(
				exp.getValorTipoExpediente(), 
				exp.getValorSituacionExpediente(), 
				tramiteExpediente.getTipoTramite(), 
				this.getTipoTramiteSup(tramiteExpediente), 
				CfgDocExpedienteTramitacion.ORIGEN_ADMINISTRACION);	
		
		if(NumberUtils.INTEGER_ONE == listaTiposDocumentos.size()) {
			this.tipoDocumentoCfgId = listaTiposDocumentos.get(0).getId();
			cargarPlantillas();
		}
	}
	
	private TipoTramite getTipoTramiteSup(TramiteExpediente tramiteExp) {		
		final TramiteExpediente tramiteExpSup = tramiteExp.getTramiteExpedienteSup();
		return (tramiteExpSup == null)? null : tramiteExpSup.getTipoTramite();
	}
	
	public void onSelectTipoDoc() {
		cargarPlantillas();
		cargarDescripcionesTipoDoc();
	}
	
	private void cargarPlantillas() {
		listaPlantillas = (tipoDocumentoCfgId==null)? Collections.emptyList()
				: plantillasDocumentosService.findByCfgId(tipoDocumentoCfgId);	
		
		if(NumberUtils.INTEGER_ONE == listaPlantillas.size()) {
			this.plantillaDocId = listaPlantillas.get(0).getId();
		}
	}
	
	private void cargarDescripcionesTipoDoc() {
		if(this.hayDocumentoGenerado()) { //ya tenemos un documento generado
			List<CfgDocExpedienteDescripcion> listaCfg = cfgDocExpedienteDescripcionService.findDescripcionesTipoDocCfg(tipoDocumentoCfgId);
			
			if(!listaCfg.isEmpty()) {
				cfgDocExpedienteDescripcionService.aplicarCfg(listaCfg.get(0), this.documento);
			} else {
				this.documento.setCategoriaId(null);
			}
			
			this.documento.setListaCfgDescripcionesTipoDoc(listaCfg);
		}
	}
	
	public void onSelectDescripcion(DocumentoDTO dto) {
		Optional<CfgDocExpedienteDescripcion> op = dto.getListaCfgDescripcionesTipoDoc().stream().filter(cfg -> cfg.getId().equals(dto.getCfgDocExpDescripcionId())).findFirst();
		if(op.isPresent()) {
			cfgDocExpedienteDescripcionService.aplicarCfg(op.get(), dto);
		}
	}
	
	public Boolean getCamposDeshabilitados() {
		return !this.hayDocumentoGenerado();
	}
	
	private boolean hayDocumentoGenerado() {
		return null != this.documento.getPlantillaId();
	}
	
	//*************************************************************************
	
	public void generar(TramiteExpediente tramExp) {
		if(tipoDocumentoCfgId==null || plantillaDocId == null) {
			facesMsgErrorKey(CLAVE_CAMPOS_OBLIGATORIOS);
			return;
		}
		
		try {
			final PlantillasDocumentos plantillaDoc = new PlantillasDocumentos();
			plantillaDoc.setId(plantillaDocId);
			
			//Carga el resto de campos de documento
			documento = documentosExpedientesService.generarDocumentoPlantilla(getExpedienteFormulario(), tramExp, plantillaDoc);
			//Ahora que tenemos documento, aplico los datos de descripción por 
			//defecto, si los hay
			cargarDescripcionesTipoDoc();
		} catch (BaseException e) {
			facesMsgErrorKey(CLAVE_ERROR_GENERICO);
			e.printStackTrace();
		}
		
	}
		
	public StreamedContent getFicheroGenerado() {
		final byte[] bb = documento.getBytes();
		return DefaultStreamedContent.builder()
	                .name(documento.getNombreFichero())
	                .contentLength(bb.length)
	                .stream(() -> new ByteArrayInputStream(bb))
	                .build();
	}
	
	public void vistaPreviaFichero() {
		comunDocumentosBean.vistaPreviaFichero(documento);
	}
	
	private boolean validarGenerado() {
		if(StringUtils.isBlank(this.documento.getDescripcion())
				|| null == this.documento.getCategoriaId()) {
			facesMsgErrorKey(CLAVE_CAMPOS_OBLIGATORIOS);
			return false;
		}
		
		//Descripción no es null aquí
		if(this.documento.getDescripcion().contains(".")){
			facesMsgErrorKey("descripcion.puntos.no.permitidos");
			return false;
		}

		return true;
	}
	
	public void guardarGenerado() {
		
		if(validarGenerado()) {
			try {
				documentosExpedientesService.guardarDocumentoGenerado(documento, tramiteExpediente.getId());
				FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, 
						new FacesMessage(FacesMessage.SEVERITY_INFO, "", this.getMessage(MENSAJE_GENERADO_OK)));			
				PrimeFaces.current().ajax().addCallbackParam("saved", true);
				
				Expedientes expedienteActualizar = expedientesService.obtener(getExpedienteFormulario().getId());
				datosExpedientesBean.actualizarCabecera(expedienteActualizar,null,null,null);
			} catch (BaseException e) {
				facesMsgErrorKey(CLAVE_ERROR_GENERICO);
				e.printStackTrace();
			}
		}
		
	}
	
}
