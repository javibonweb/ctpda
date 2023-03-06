package es.juntadeandalucia.ctpda.gestionpdt.web.dominios;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.Dominio;
import es.juntadeandalucia.ctpda.gestionpdt.service.DominioService;
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
public class DominioBean  extends BaseBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private static final String DOMINIO = "dominio";
	
	@Autowired
	private DominioService dominioService;
	
	@Getter
	private LazyDataModelByQueryService<Dominio> lazyModel;
	
	@Getter
	@Setter
	private Dominio selectedDominio;	

	
	
	//CAMPOS FILTROS
	@Getter @Setter
	private String descripcionFiltro;
	
	@Getter @Setter
	private Long nivelAnidamientoMaxFiltro;
	
	@Getter @Setter
	private Boolean bloqueadoFiltro;
	
	@Getter @Setter
	private Boolean puntoMenuFiltro;

	@Getter
	@Setter
	private List<String> listaCodigoPermisos;
	
	@Getter	@Setter
	private Boolean permisoListValDom;
		
	@Autowired
	private NavegacionBean navegacionBean;
	
	/**
	* Initialize default attributes.
	 * @throws BaseFrontException 
	*/
	@PostConstruct
	@Override
	public void init(){
		
		super.init();
		
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, 0);
		
		navegacionBean.setTxtMigaPan(Constantes.LISTADO_DOMINIOS);
		
		/**
		 * GESTION DE PERMISOS
		 * */
		listaCodigoPermisos = (List<String>) JsfUtils.getSessionAttribute(Constantes.LISTACODIGOPERMISOS);
		
		permisoListValDom = listaCodigoPermisos.contains(Constantes.PERMISO_LIST_VALDOM);
		
		lazyModel= new LazyDataModelByQueryService<>(Dominio.class,dominioService);
		lazyModel.setPreproceso((a,b,c,filters)->{
			if (descripcionFiltro != null && !descripcionFiltro.isEmpty()){
				filters.put("descripcion", new MyFilterMeta(descripcionFiltro));				
			}
			
			if (nivelAnidamientoMaxFiltro != null){

				filters.put("nivelAnidamientoMaximo", new MyFilterMeta(nivelAnidamientoMaxFiltro));
			}
			
			if (bloqueadoFiltro != null && (bloqueadoFiltro)) {
				filters.put("bloqueado", new MyFilterMeta("true"));
			} 
			
			if (puntoMenuFiltro != null && (puntoMenuFiltro)){
				filters.put("puntoMenu", new MyFilterMeta("true"));
			}
			
			
			filters.put("visible", new MyFilterMeta("true"));
			
		});
		
		/** se elimina el dominio almacenado en sesion */
		JsfUtils.removeSessionAttribute(DOMINIO);
		JsfUtils.removeSessionAttribute("dominioPuntoMenu");
	}
	

	public String redireccionMenu() {
		init();
		JsfUtils.setSessionAttribute("vieneMenuDominio", true);
		return ListadoNavegaciones.LISTADO_DOMINIOS.getRegla();
	}
	
	public void limpiarFiltro () {
		descripcionFiltro = "";
		nivelAnidamientoMaxFiltro = null;
		bloqueadoFiltro = null;
		puntoMenuFiltro = null;
	}
	
	public void onDomRowDblClkSelect(final SelectEvent<Dominio> event) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, ((Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS))+1);
		Dominio p = event.getObject();
		PrimeFaces.current().ajax().addCallbackParam("id", p.getId());
		navegacionBean.setTxtMigaPan(Constantes.LISTADO_DOMINIO+p.getDescripcion());
	}
	
	public String onConsultarPtoMenu(String codDom) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, ((Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS))+1);
		JsfUtils.setSessionAttribute("vienePuntoMenu", true);
		
		Dominio dominioPtoMenu = null; 
		
		dominioPtoMenu = dominioService.findDominioByCodigo(codDom);

		JsfUtils.setSessionAttribute(DOMINIO, dominioPtoMenu);	

		JsfUtils.setSessionAttribute("dominioPuntoMenu", true);
		navegacionBean.setTxtMigaPan(Constantes.LISTADO_DOMINIO+dominioPtoMenu.getDescripcion());
		return ListadoNavegaciones.LISTADO_VALORES_DOMINIO.getRegla();
	}	
	
	public String onConsultar(Dominio dominio) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, ((Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS))+1);
		JsfUtils.setSessionAttribute(DOMINIO, dominio);
		navegacionBean.setTxtMigaPan(Constantes.LISTADO_DOMINIO+dominio.getDescripcion());
		return ListadoNavegaciones.LISTADO_VALORES_DOMINIO.getRegla();
	}
	
	
}
