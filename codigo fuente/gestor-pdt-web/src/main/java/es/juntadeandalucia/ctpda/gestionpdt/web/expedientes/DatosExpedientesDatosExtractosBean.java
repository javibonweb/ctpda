package es.juntadeandalucia.ctpda.gestionpdt.web.expedientes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.primefaces.PrimeFaces;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.DetalleExpdteTram;
import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.TramiteExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.service.DetalleExpdteTramService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
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
public class DatosExpedientesDatosExtractosBean extends BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static final String MENSAJESFORMULARIO = "messagesFormulario";

		
	@Autowired
	private DetalleExpdteTramService detalleExpdteTramService;
	
	@Getter
	private LazyDataModelByQueryService<DetalleExpdteTram> lazyModel;
	@Getter
	private List<SortMeta> defaultOrdenList;

	@Getter @Setter
	private DetalleExpdteTram detalle;
	
	@Getter @Setter
	private List<String> listaCodigoPermisos;
	@Getter @Setter
	private Boolean permisoEditarExtracto;
	

	
	@Override
	@PostConstruct
	public void init() {
		super.init();
		
		detalle = new DetalleExpdteTram();
		detalle.setTramiteExpediente(new TramiteExpediente());		
		
		cargarPermisosUsuario();		
		inicializaLazyModel();
	}
	
	private void cargarPermisosUsuario() {
		listaCodigoPermisos = (List<String>) JsfUtils.getSessionAttribute(Constantes.LISTACODIGOPERMISOS);
		permisoEditarExtracto = listaCodigoPermisos.contains(Constantes.PERMISO_EDIT_EXTRACTO);
	}
	
	private void inicializaLazyModel() {
		lazyModel= new LazyDataModelByQueryService<>(DetalleExpdteTram.class, detalleExpdteTramService);
		lazyModel.setPreproceso((a,b,c,filters)->{				
			filters.put("expediente.id", new MyFilterMeta(this.getExpedienteFormulario().getId()));
			filters.put("tramiteExpediente.activo", new MyFilterMeta(true));
			filters.put("#extractoNotNull", new MyFilterMeta(null));
		});
		
		defaultOrdenList = new ArrayList<>();
		defaultOrdenList.add(SortMeta.builder().field("tramiteExpediente.fechaIni").order(SortOrder.DESCENDING).priority(1).build());
		defaultOrdenList.add(SortMeta.builder().field("tramiteExpediente.id").order(SortOrder.DESCENDING).priority(2).build());
	}
	
	private Expedientes getExpedienteFormulario() {
		return (Expedientes)JsfUtils.getSessionAttribute("expedienteFormulario");
	}
	
	//---------------
	
	public void editar(Long idDetExpTram) {
		//Necesitamos el objeto (no la referencia) para que no de error en el listado del bean 
		//y en otras pestañas que cargan los datos del detalle.
		try {
			detalle = detalleExpdteTramService.obtenerObjeto(idDetExpTram);
			PrimeFaces.current().ajax().addCallbackParam("ok", true);
		} catch (Exception e) {
			facesMsgError(e.getMessage());
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void onChangeIncluir(DetalleExpdteTram detalleRow) {
		//Igual que antes, obtenemos el objeto completo aunque solo vayamos a actualizar una propiedad
		try {
			detalle = detalleExpdteTramService.obtenerObjeto(detalleRow.getId());
			detalle.setExtractoExpediente(detalleRow.getExtractoExpediente());
		} catch (Exception e) {
			facesMsgError(e.getMessage());
			log.error(e.getMessage());
			e.printStackTrace();
			return;
		}
			
		this.guardar();
	}
	
	public void guardar() {
		try {
			detalle = detalleExpdteTramService.guardar(detalle);
			PrimeFaces.current().ajax().addCallbackParam("saved", true);
		
			final String msg = getMessages("extracto", "guardado.correctamente");
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "", msg);
			FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, message);
		} catch (BaseException e) {
			facesMsgError(e.getMessage());
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

}
