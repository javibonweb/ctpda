package es.juntadeandalucia.ctpda.gestionpdt.web.plantillas;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.Plantillas;
import es.juntadeandalucia.ctpda.gestionpdt.model.PlantillasDocumentos;
import es.juntadeandalucia.ctpda.gestionpdt.service.PlantillasService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.ValidacionException;
import es.juntadeandalucia.ctpda.gestionpdt.util.FileUtils;
import es.juntadeandalucia.ctpda.gestionpdt.util.StringUtils;
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
@Slf4j
@ViewScoped
public class PlantillasBean extends BaseBean {

	private static final long serialVersionUID = 1L;

	private static final String MENSAJESLISTADO = "messagesListado";

	
	private static final String TXTPLANTILLA = "Plantilla";
	private static final String CAMPOSOBLIGATORIOS = "campos.obligatorios";
	private static final String GUARDADACORRECTAMENTE = "guardada.correctamente";
	private static final String ACTUALIZADACORRECTAMENTE = "actualizada.correctamente";
	private static final String DESACTIVADACORRECTAMENTE = "desactivada.correctamente";
	private static final String REACTIVADACORRECTAMENTE = "reactivada.correctamente";

	private static final String CALLBACK_SAVED = "saved";


	@Autowired
	private PlantillasService plantillasService;
	
	
	@Getter @Setter
	private Plantillas selectedPlantilla;
	@Getter @Setter
	private Plantillas plantilla;
	@Getter @Setter
    private transient UploadedFile file;
	@Getter
	private String nombreFicheroActual;

	@Getter
	private String tituloDialog;
	@Getter
	private String strExtensionesPermitidas;
	@Getter
	private String extensionesPermitidas;
	
	@Getter @Setter
	private Long selectedTipoExpIdFiltro;
	@Getter @Setter
	private Long selectedTipoTareaIdFiltro;
	@Getter @Setter
	private Long selectedResponsableIdFiltro;
	@Getter @Setter
	private boolean activasFiltro;
	@Getter @Setter
	private String codigoFiltro;
	@Getter @Setter
	private String descripcionFiltro;
	
	@Getter @Setter
	private boolean permisoListaPlantillas;
	@Getter @Setter
	private boolean permisoActPlantillas;
	@Getter @Setter
	private boolean permisoConsPlantillas;
	@Getter @Setter
	private boolean permisoDesacPlantillas;
	@Getter @Setter
	private boolean permisoEditPlantillas;
	@Getter @Setter
	private boolean permisoGuardarPlantillas;
	@Getter @Setter
	private boolean permisoNuevaPlantilla;
	
	
	@Getter
	private LazyDataModelByQueryService<Plantillas> lazyModelPlantillas;
	
	@Getter
	private LazyDataModelByQueryService<PlantillasDocumentos> lazyModelPlantillasDoc;
	
	@Getter
	private SortMeta defaultOrden;
	
	@Autowired
	private NavegacionBean navegacionBean;
	
	@Getter
	@Setter
	private List<String> listaCodigoPermisos;
	
	public String redireccionMenu() {
		return ListadoNavegaciones.LISTADO_PLANTILLAS.getRegla();
	}
	
	
	@Override
	@PostConstruct
	public void init() {
		super.init();
		
		navegacionBean.setTxtMigaPan(Constantes.LISTADO_PLANTILLASDOCUMENTOS);
		
		cargarPermisosUsuario();

		plantilla = new Plantillas();
		
		final String[] ss = plantillasService.obtenerExtensionesPermitidas();
		strExtensionesPermitidas = StringUtils.join(ss, ", ");
		extensionesPermitidas = StringUtils.join(ss, "|");
		
		inicializaLazyModel();

		limpiarFiltros();
		
	}
	
	private void cargarPermisosUsuario() {
		listaCodigoPermisos = (List<String>) JsfUtils.getSessionAttribute(Constantes.LISTACODIGOPERMISOS);
		permisoListaPlantillas = listaCodigoPermisos.contains(Constantes.PERMISO_LIST_PLANTILLASDOC);
		permisoActPlantillas = listaCodigoPermisos.contains(Constantes.PERMISO_ACT_PLANTILLASDOC);
		permisoConsPlantillas = listaCodigoPermisos.contains(Constantes.PERMISO_CONS_PLANTILLASDOC);
		permisoDesacPlantillas = listaCodigoPermisos.contains(Constantes.PERMISO_DESAC_PLANTILLASDOC);
		permisoEditPlantillas = listaCodigoPermisos.contains(Constantes.PERMISO_EDIT_PLANTILLASDOC);
		permisoGuardarPlantillas = listaCodigoPermisos.contains(Constantes.PERMISO_SAVE_PLANTILLASDOC);
		permisoNuevaPlantilla = listaCodigoPermisos.contains(Constantes.PERMISO_NEW_PLANTILLASDOC);
	}
	
	private void inicializaLazyModel() {
		lazyModelPlantillas= new LazyDataModelByQueryService<>(Plantillas.class, plantillasService);
		lazyModelPlantillas.setPreproceso((a,b,c,filters)->{
			
			if(!StringUtils.isBlank(this.codigoFiltro)) {
				filters.put("codigo", new MyFilterMeta(this.codigoFiltro)); 			
			}
			if(!StringUtils.isBlank(this.descripcionFiltro)) {
				filters.put("descripcion", new MyFilterMeta(this.descripcionFiltro)); 			
			}
			if(this.activasFiltro) {
				filters.put("activo", new MyFilterMeta(this.activasFiltro)); 			
			}
					
		});
		
		defaultOrden = SortMeta.builder().field("codigo").order(SortOrder.ASCENDING).priority(1).build();
	}
	
	//----------------------------
			
	public void limpiarFiltros() {
		this.codigoFiltro = null;
		this.descripcionFiltro = null;
		this.activasFiltro = true;
	}

	//*************************************************************

	public void onAlta() {
		this.plantilla = new Plantillas();
		this.nombreFicheroActual = null;
		this.tituloDialog = getMessage("alta.plantilla");
		setFormEditable(true);
	}
	
	public void onEditar(Long idPlantilla) {
		this.plantilla = plantillasService.obtener(idPlantilla);
		this.nombreFicheroActual = plantilla.getFichero();
		this.tituloDialog = getMessage("edicion.plantilla");
		setFormEditable(true);
	}
	
	public void aplicarNombreFichero() {
		if(StringUtils.isBlank(this.plantilla.getDescripcion())) {
			String fName = this.file.getFileName();
			if(fName != null) {
				String desc = FileUtils.getNombreSinExt(fName);
				this.plantilla.setDescripcion(desc);
			}
		}
	}

	public void onConsultar(Long idPlantilla) {
		this.plantilla = plantillasService.obtener(idPlantilla);
		this.nombreFicheroActual = plantilla.getFichero();
		this.tituloDialog = getMessage("consulta.plantilla");
		setFormEditable(false);
	}

	public void onPlantillaRowDblClkSelect(final SelectEvent<Plantillas> event) {
		Plantillas p = event.getObject();
		PrimeFaces.current().ajax().addCallbackParam("id", p.getId());
	}
	
	public StreamedContent descargarFichero() {
		Plantillas plan = plantillasService.obtener(plantilla.getId());
		final String fName= plan.getFichero();
		final byte[] bytes = plan.getBytes();
		
		return DefaultStreamedContent.builder()
                .name(fName)
                .contentLength(bytes.length)
                .stream(() -> new ByteArrayInputStream(bytes))
                .build();
	}

	//*************************************************************

	private boolean validarPlantilla() {
		if(StringUtils.isBlank(this.plantilla.getCodigo()) || StringUtils.isBlank(this.plantilla.getDescripcion()) || (null == this.plantilla.getId() && ( file == null || file.getFileName() == null))) {
			facesMsgErrorKey(CAMPOSOBLIGATORIOS);
			return false;
		}
		
		return true;
	}
	
	public void guardarPlantilla(){
		try {
			if(validarPlantilla()) {
				aplicarFichero();				
				this.plantillasService.guardar(plantilla);
				
				String msg = TXTPLANTILLA + StringUtils.SPACE + 
							getMessage(null == plantilla.getId()? GUARDADACORRECTAMENTE : ACTUALIZADACORRECTAMENTE);
				
				FacesContext.getCurrentInstance().addMessage(MENSAJESLISTADO, new FacesMessage(FacesMessage.SEVERITY_INFO, "", msg));
				PrimeFaces.current().ajax().addCallbackParam(CALLBACK_SAVED , true);				
			}
		} catch (ValidacionException e) {
			facesMsgError(e.getMessage());
			e.printStackTrace();
		} catch (BaseException e) {
			facesMsgErrorKey(CLAVE_ERROR_GENERICO);
			e.printStackTrace();
		}
	}
	
	private void aplicarFichero() {
		if(file != null && null != file.getFileName()) {
			plantilla.setFichero(file.getFileName());
			plantilla.setBytes(file.getContent());
		}
	}
	
	public void eliminarPlantilla(Plantillas plantilla){
		try {
			this.plantillasService.desactivarPlantilla(plantilla.getId());
			FacesContext.getCurrentInstance().addMessage(MENSAJESLISTADO, 
					new FacesMessage(FacesMessage.SEVERITY_INFO, "", TXTPLANTILLA + StringUtils.SPACE + getMessage(DESACTIVADACORRECTAMENTE)));
		} catch (BaseException e) {
			facesMsgErrorKey(CLAVE_ERROR_GENERICO);
			e.printStackTrace();
		}
	}
	
	public void activarPlantilla(Plantillas plantilla){
		try {
			this.plantillasService.activarPlantilla(plantilla.getId());
			FacesContext.getCurrentInstance().addMessage(MENSAJESLISTADO, 
					new FacesMessage(FacesMessage.SEVERITY_INFO, "", TXTPLANTILLA + StringUtils.SPACE + getMessage(REACTIVADACORRECTAMENTE)));
		} catch (BaseException e) {
			facesMsgErrorKey(CLAVE_ERROR_GENERICO);
			e.printStackTrace();
		}
	}
	
	public StreamedContent descargarDocumentoInfo () {
		StreamedContent res = null;
		String fName = Constantes.NOMBRE_DOCINFORMATIVO_VARIABLES;
		Path path = Paths.get("src/"+fName); 
		
		 try {
			 byte[] fileContent = Files.readAllBytes(path);     
			res = DefaultStreamedContent.builder().name(fName).contentLength(fileContent.length).stream(() -> new ByteArrayInputStream(fileContent)).build();
		} catch (IOException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",mensajesProperties.getString("error"));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
		
		return res;
	}

}
