package es.juntadeandalucia.ctpda.gestionpdt.web.menu;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;

import org.primefaces.PrimeFaces;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.Dominio;
import es.juntadeandalucia.ctpda.gestionpdt.model.Perfil;
import es.juntadeandalucia.ctpda.gestionpdt.model.Usuario;
import es.juntadeandalucia.ctpda.gestionpdt.service.DominioService;
import es.juntadeandalucia.ctpda.gestionpdt.service.PerfilService;
import es.juntadeandalucia.ctpda.gestionpdt.service.UsuarioPerfilService;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.Constantes;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.JsfUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.menu.NavegacionBean.ListadoNavegaciones;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.BaseBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@ViewScoped
@Slf4j
public class MenuBean extends BaseBean implements Serializable{
	
	private static final String DATAFORM = ":dataForm:data";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@Autowired
	private PerfilService perfilService;
	
	@Autowired
	UsuarioPerfilService usuarioPerfilService;
	
	@Getter
	@Setter
	private transient MenuModel model;
	
	@Getter
	@Setter
	private List<Dominio> listaDominios;
	
	@Getter
	@Setter
	private Long selectedPerfilAsociado;	
	
	@Getter
	@Setter
	private List<Perfil> listaPerfilesAsociados;
	
	@Getter
	@Setter
	private  String nombreCompletoUsuario;
	@Getter
	@Setter
	private String descripcionPerfil; 
	
	@Getter
	@Setter
	private List<String> listaCodigoPermisos;
	
	@Autowired
	private DominioService dominioService;
	
	@Autowired
	private NavegacionBean navegacionBean;



	private enum Items {
		NULO,
		MATERIAS, PARAMETROS, MOTIVOSRELACION, DOMINIOS,	
		PERSONAS,
		TIPOSAGRUPACIONES,
		SUJETOSOBLIGADOS,
		EXPEDIENTES
		;
	}

    private Items itemActivo;

    @Override
    @PostConstruct
    public void init() {
        this.itemActivo = Items.NULO;
    
        
        /**
         * CERRAMOS EL DIALOG CON LA SELECCION DE PERFIL DE ACCESO AL SISTEMA
         * */
        PrimeFaces.current().executeScript("PF('dialogPerfiles').hide();");
        
        
        mensajesProperties = ResourceBundle.getBundle("es.juntadeandalucia.ctpda.gestionpdt.web.Messages_es");
        
        model = new DefaultMenuModel();
          
        /**
         * MONTAMOS EL MENU EN FUNCION DE LOS PERMISOS ASOCIADOS AL PERFIL DEL USUARIO LOGADO EN EL SISTEMA
         * 
         * */
        
        listaCodigoPermisos = (List<String>) JsfUtils.getSessionAttribute(Constantes.LISTACODIGOPERMISOS);
        /**
         * 	SUBMENU ADMINISTRACION FUNCIONAL
         **/
        
        //LISTA DINAMICA A PARTIR DE DOMINIOS CON PUNTO DE MENU ACTIVOS.
        listaDominios = dominioService.findDominiosPuntoMenu();
        

        /*
         * 	SUBMENU MI MESA
         * */    	
    	DefaultSubMenu submenuMiMesa = new DefaultSubMenu();
    	submenuMiMesa.setLabel(mensajesProperties.getString("mi.mesa"));
    	menuMiMesa(submenuMiMesa);

        /*
         * 	SUBMENU EXPEDIENTES
         * */    	
    	
    	DefaultSubMenu submenuExpedientes = new DefaultSubMenu();
    	submenuExpedientes.setLabel(mensajesProperties.getString("expedientes"));    	
    	menuExpedientes(submenuExpedientes);

        /*
         * 	SUBMENU PERSONAS
         * */    	
    	DefaultSubMenu submenuPersonas = new DefaultSubMenu();
    	submenuPersonas.setLabel(mensajesProperties.getString("personas"));         
    	menuPersonas(submenuPersonas);
            	
    	
        /*
         * 	SUBMENU ADMINISTRACION FUNCIONAL
         * */    	
        DefaultSubMenu submenuAdmonFuncional = new DefaultSubMenu();
        submenuAdmonFuncional.setLabel(mensajesProperties.getString("administracion.funcional"));
        menuAdmonFuncional(submenuAdmonFuncional);
        
        /*
         * 	SUBMENU ADMINISTRACION
         * */    	
    	DefaultSubMenu submenuAdmon = new DefaultSubMenu();
    	submenuAdmon.setLabel(mensajesProperties.getString("administracion"));
    	menuAdmon(submenuAdmon);
    	    	

    	    	
    	PrimeFaces.current().ajax().update("menuForm");
    	
    }
    
    private void menuMiMesa(DefaultSubMenu submenuMiMesa) {
        if((listaCodigoPermisos!= null) && listaCodigoPermisos.contains(Constantes.PERMISO_MI_MESA)) {
        	DefaultMenuItem itemTareas = new DefaultMenuItem();
        	itemTareas.setAjax(false);
        	itemTareas.setAsync(false);
        	itemTareas.setUpdate(DATAFORM);
        	itemTareas.setValue(mensajesProperties.getString("mi.mesa"));
        	itemTareas.setCommand("#{tareasBean.redireccionMenu}");
        	submenuMiMesa.getElements().add(itemTareas);
        }
        
        if((listaCodigoPermisos!= null) && listaCodigoPermisos.contains(Constantes.PERMISO_TAREAS_ENVIADAS_PDTES)) {
        	DefaultMenuItem itemTareas = new DefaultMenuItem();
        	itemTareas.setAjax(false);
        	itemTareas.setAsync(false);
        	itemTareas.setUpdate(DATAFORM);
        	itemTareas.setValue(mensajesProperties.getString("tareas.enviadas.pendientes"));
        	itemTareas.setCommand("#{tareasEnviadasPendientesBean.redireccionMenu}");
        	submenuMiMesa.getElements().add(itemTareas);
        }
        
        
        if(!submenuMiMesa.getElements().isEmpty())
        {
        	model.getElements().add(submenuMiMesa);
        }

    }
    
    private void menuExpedientes(DefaultSubMenu submenuExpedientes) {
    	if((listaCodigoPermisos!= null) && listaCodigoPermisos.contains(Constantes.PERMISO_LIST_EXP)){
        	DefaultMenuItem itemExpedientes = new DefaultMenuItem();
        	itemExpedientes.setAjax(false);
        	itemExpedientes.setAsync(false);
        	itemExpedientes.setUpdate(DATAFORM);
        	itemExpedientes.setValue(mensajesProperties.getString("gestion.expedientes"));
        	itemExpedientes.setCommand("#{expedientesBean.redireccionMenu}");
        	
        	submenuExpedientes.getElements().add(itemExpedientes);
        }
    	
    	if((listaCodigoPermisos!= null) && listaCodigoPermisos.contains(Constantes.PERMISO_VIEW_XPC)){
        	DefaultMenuItem itemEntradaNoCalificada = new DefaultMenuItem();
        	itemEntradaNoCalificada.setAjax(false);
        	itemEntradaNoCalificada.setAsync(false);
        	itemEntradaNoCalificada.setUpdate(DATAFORM);
        	itemEntradaNoCalificada.setValue(mensajesProperties.getString("entrada.no.calificada"));
        	itemEntradaNoCalificada.setCommand("#{expedientesBean.redireccionMenuListadoEntradas}");
        	
        	submenuExpedientes.getElements().add(itemEntradaNoCalificada);
        }
    	
    	if((listaCodigoPermisos!= null) && listaCodigoPermisos.contains(Constantes.PERMISO_VIEW_TRAM_AB)){
        	DefaultMenuItem itemTramitesAbiertos = new DefaultMenuItem();
        	itemTramitesAbiertos.setAjax(false);
        	itemTramitesAbiertos.setAsync(false);
        	itemTramitesAbiertos.setUpdate(DATAFORM);
        	itemTramitesAbiertos.setValue(mensajesProperties.getString("tramites.abiertos"));
        	itemTramitesAbiertos.setCommand("#{tramitesAbiertosBean.redireccionMenu}");
        	
        	submenuExpedientes.getElements().add(itemTramitesAbiertos);
       }
    	
    	if((listaCodigoPermisos!= null) && listaCodigoPermisos.contains(Constantes.PERMISO_VIEW_NOTIF_AB)){
        	DefaultMenuItem itemTramitesAbiertos = new DefaultMenuItem();
        	itemTramitesAbiertos.setAjax(false);
        	itemTramitesAbiertos.setAsync(false);
        	itemTramitesAbiertos.setUpdate(DATAFORM);
        	itemTramitesAbiertos.setValue(mensajesProperties.getString("notificaciones.en.curso"));
        	itemTramitesAbiertos.setCommand("#{notificacionesAbiertasBean.redireccionMenu}");
        	
        	submenuExpedientes.getElements().add(itemTramitesAbiertos);
       }
    	
    	if((listaCodigoPermisos!= null) && listaCodigoPermisos.contains(Constantes.PERMISO_VIEW_NOT_PDTE)){
        	DefaultMenuItem itemTramitesAbiertos = new DefaultMenuItem();
        	itemTramitesAbiertos.setAjax(false);
        	itemTramitesAbiertos.setAsync(false);
        	itemTramitesAbiertos.setUpdate(DATAFORM);
        	itemTramitesAbiertos.setValue(mensajesProperties.getString("notificaciones.pendientes.acuse"));
        	itemTramitesAbiertos.setCommand("#{notificacionesPendientesBean.redireccionMenu}");
        	
        	submenuExpedientes.getElements().add(itemTramitesAbiertos);
       }
    	
    	if((listaCodigoPermisos!= null) && listaCodigoPermisos.contains(Constantes.PERMISO_VIEW_FIRMAS_AB)){
        	DefaultMenuItem itemTramitesAbiertos = new DefaultMenuItem();
        	itemTramitesAbiertos.setAjax(false);
        	itemTramitesAbiertos.setAsync(false);
        	itemTramitesAbiertos.setUpdate(DATAFORM);
        	itemTramitesAbiertos.setValue(mensajesProperties.getString("firmas.en.curso"));
        	itemTramitesAbiertos.setCommand("#{firmasAbiertasBean.redireccionMenu}");
        	
        	submenuExpedientes.getElements().add(itemTramitesAbiertos);
       }
        
        if((listaCodigoPermisos!= null) && listaCodigoPermisos.contains(Constantes.PERMISO_LIST_PLAZOEXP)) {
        	DefaultMenuItem itemControlPlazos = new DefaultMenuItem();
        	itemControlPlazos.setAjax(false);
        	itemControlPlazos.setAsync(false);
        	itemControlPlazos.setUpdate(DATAFORM);
        	itemControlPlazos.setValue(mensajesProperties.getString("control.plazos"));
        	itemControlPlazos.setCommand("#{controlPlazosBean.redireccionMenu}");
        	submenuExpedientes.getElements().add(itemControlPlazos);
        }    	
    	
        if(!submenuExpedientes.getElements().isEmpty())
        {
        	model.getElements().add(submenuExpedientes);
        }
        
        menuResoluciones(submenuExpedientes);
    }
    
    private void menuPersonas(DefaultSubMenu submenuPersonas) {
    	if((listaCodigoPermisos!= null) && listaCodigoPermisos.contains(Constantes.PERMISO_LIST_PERS))
        	
        {
        	DefaultMenuItem itemPersonas = new DefaultMenuItem();
        	itemPersonas.setAjax(false);
        	itemPersonas.setAsync(false);
        	itemPersonas.setUpdate(DATAFORM);
        	itemPersonas.setValue(mensajesProperties.getString("personas"));
        	itemPersonas.setCommand("#{personasBean.redireccionMenu}");
        	
        	submenuPersonas.getElements().add(itemPersonas);
        }

        if(!submenuPersonas.getElements().isEmpty())
        {
        	model.getElements().add(submenuPersonas);
        }
    }
    
    private void menuAdmon (DefaultSubMenu submenuAdmon) {
    	if((listaCodigoPermisos!= null) && listaCodigoPermisos.contains(Constantes.PERMISO_LIST_PARAM))
        	
        {
        	//AÑADIMOS PARAMETROS Y DOMINIOS
        	DefaultMenuItem itemParametros = new DefaultMenuItem();
        	itemParametros.setAjax(false);
        	itemParametros.setAsync(false);
        	itemParametros.setUpdate(DATAFORM);
        	itemParametros.setValue(mensajesProperties.getString("parametros"));
        	itemParametros.setCommand("#{parametroBean.redireccionMenu}");
        	
        	
        	submenuAdmon.getElements().add(itemParametros);
        	
        }
    	
      
        
        if((listaCodigoPermisos!= null) && listaCodigoPermisos.contains(Constantes.PERMISO_LIST_DOM))
        	
        {
        	DefaultMenuItem itemDominios = new DefaultMenuItem();
        	itemDominios.setAjax(false);
        	itemDominios.setAsync(false);
        	itemDominios.setUpdate(DATAFORM);
        	itemDominios.setValue(mensajesProperties.getString("dominios"));
        	itemDominios.setCommand("#{dominioBean.redireccionMenu}");
        	
        	submenuAdmon.getElements().add(itemDominios);
        	
        }
        
        if((listaCodigoPermisos!= null) && listaCodigoPermisos.contains(Constantes.PERMISO_LIST_USUARIOS))
        	
        {
        	DefaultMenuItem itemUsuarios = new DefaultMenuItem();
        	itemUsuarios.setAjax(false);
        	itemUsuarios.setAsync(false);
        	itemUsuarios.setUpdate(DATAFORM);
        	itemUsuarios.setValue(mensajesProperties.getString("usuarios"));
        	itemUsuarios.setCommand("#{usuariosBean.redireccionMenu}");
        	
        	submenuAdmon.getElements().add(itemUsuarios);
        	
        }
        
	    if((listaCodigoPermisos!= null) && listaCodigoPermisos.contains(Constantes.PERMISO_MENU)) {       	

		DefaultMenuItem itemMenu = new DefaultMenuItem();
		itemMenu.setAjax(false);
		itemMenu.setAsync(false);
		itemMenu.setUpdate(DATAFORM);
		itemMenu.setValue(mensajesProperties.getString("menu.aplicacion"));
		itemMenu.setCommand("#{dominioBean.onConsultarPtoMenu('MENU')}");

		submenuAdmon.getElements().add(itemMenu);
		}
        
    
        if(!submenuAdmon.getElements().isEmpty())
        {
            model.getElements().add(submenuAdmon);
        }
    }

    private void menuAdmonFuncional(DefaultSubMenu submenuAdmonFuncional) {
    	
       if((listaCodigoPermisos!= null) && listaCodigoPermisos.contains(Constantes.PERMISO_LIST_SUJOBL))
        	
        {
        	DefaultMenuItem itemSujObligados = new DefaultMenuItem();
        	itemSujObligados.setAjax(false);
        	itemSujObligados.setAsync(false);
        	itemSujObligados.setUpdate(DATAFORM);
    		itemSujObligados.setStyle("efecto-menu-lateral");
        	itemSujObligados.setValue(mensajesProperties.getString("sujetos.obligados"));
        	itemSujObligados.setCommand("#{sujetosObligadosBean.redireccionMenu}");
        	
        	submenuAdmonFuncional.getElements().add(itemSujObligados);
        }
       
       if((listaCodigoPermisos!= null) && listaCodigoPermisos.contains(Constantes.PERMISO_LIST_PLANTILLASDOC))
       	
       {
       	DefaultMenuItem itemPlantillas = new DefaultMenuItem();
       	itemPlantillas.setAjax(false);
       	itemPlantillas.setAsync(false);
       	itemPlantillas.setUpdate(DATAFORM);
       	itemPlantillas.setValue(mensajesProperties.getString("plantillas.documentos"));
       	itemPlantillas.setCommand("#{plantillasBean.redireccionMenu}");
       	
       	submenuAdmonFuncional.getElements().add(itemPlantillas);
       	
       }
   
    	
       if((listaCodigoPermisos!= null) && listaCodigoPermisos.contains(Constantes.PERMISO_LIST_TIPAGRUP))
       	
       {
       	DefaultMenuItem itemTipoAgrupaciones = new DefaultMenuItem();
       	itemTipoAgrupaciones.setAjax(false);
       	itemTipoAgrupaciones.setAsync(false);
       	itemTipoAgrupaciones.setUpdate(DATAFORM);
       	itemTipoAgrupaciones.setValue(mensajesProperties.getString("tipos.agrupaciones"));
       	itemTipoAgrupaciones.setCommand("#{tipoAgrupacionBean.redireccionMenu}");
       	
       	submenuAdmonFuncional.getElements().add(itemTipoAgrupaciones);
       }
       
       if((listaCodigoPermisos!= null) && listaCodigoPermisos.contains(Constantes.PERMISO_LIST_VALDOM))        	
        {
        	for(Dominio dom: listaDominios)
        	{
        	    if(listaCodigoPermisos.contains("VALDOM_"+dom.getCodigo())) {       	

        		DefaultMenuItem item = new DefaultMenuItem();
    			item.setAjax(false);
    			item.setAsync(false);
    			item.setUpdate(DATAFORM);
    			item.setValue(dom.getDescripcion());
    			item.setCommand("#{dominioBean.onConsultarPtoMenu('"+dom.getCodigo()+"')}");

    			submenuAdmonFuncional.getElements().add(item);
        		}
        	}
        }

        if(!submenuAdmonFuncional.getElements().isEmpty())
        {
        	model.getElements().add(submenuAdmonFuncional);
        }
    }

	public String getCssItemActivo(String sitem) {
		final Items item = Items.valueOf(sitem);
		String css=null;

			if(item == this.itemActivo) {
				css = "menu-item-activo";
			}

		return css;
	}


	public void setItemActivo(Items itemActivo) {
		this.itemActivo = itemActivo;
	}
	

    public String redireccionInicio() {
    	this.itemActivo = Items.NULO;
    	setItemActivo(Items.NULO);
    	navegacionBean.setTxtMigaPan("");
    	return ListadoNavegaciones.HOME_PAGE.getRegla();
    }
    
    public String redireccionInicioCambioPerfil() {
    	MenuBean.this.init();
		navegacionBean.setTxtMigaPan("");
    	return ListadoNavegaciones.HOME_PAGE.getRegla();
    }
    
    
	public void cambiarPerfil () {
		this.selectedPerfilAsociado = null;

		this.listaPerfilesAsociados = perfilService.findPerfilesActivosAsociadosUsuario( ((Usuario) JsfUtils.getSessionAttribute(Constantes.USUARIO)).getId());
		
		PrimeFaces.current().executeScript("PF('dialogPerfiles').show();");
		
	}
	
	private void menuResoluciones (DefaultSubMenu submenuExpedientes) {
		if((listaCodigoPermisos!= null) && listaCodigoPermisos.contains(Constantes.PERMISO_MENU_RESOL)){
        	DefaultMenuItem itemResoluciones = new DefaultMenuItem();
        	itemResoluciones.setAjax(false);
        	itemResoluciones.setAsync(false);
        	itemResoluciones.setUpdate(DATAFORM);
        	itemResoluciones.setValue(mensajesProperties.getString("resoluciones"));
        	itemResoluciones.setCommand("#{resolucionesBean.redireccionMenu}");
        	
        	submenuExpedientes.getElements().add(itemResoluciones);
        }
	}


}