package es.juntadeandalucia.ctpda.gestionpdt.web.usuarios;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.Usuario;
import es.juntadeandalucia.ctpda.gestionpdt.service.UsuarioService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
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
public class UsuariosBean extends BaseBean implements Serializable {

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	private static final String MENSAJESLISTADO = "messagesListado";
	private static final String MENSAJEUSUARIO = "Usuario ";
	private static final String MENSAJEERROR = "error";
	private static final String ACTUALIZADOCORRECTAMENTE = "actualizado.correctamente";
	private static final String EDITABLE = "editable";

	@Getter
	private LazyDataModelByQueryService<Usuario> lazyModel;

	@Autowired
	private UsuarioService usuarioService;

	@Getter
	@Setter
	private Usuario selectedUsuario;

	@Getter
	@Setter
	Usuario usuario;

	@Getter
	@Setter
	private String nombreFiltro;

	@Getter
	@Setter
	private String primerApellidoFiltro;

	@Getter
	@Setter
	private String segundoApellidoFiltro;

	@Getter
	@Setter
	private String identificadorFiltro;
	@Getter
	@Setter
	private String loginFiltro;
	@Getter
	@Setter
	private Boolean activoFiltro;

	@Getter
	@Setter
	private Boolean modoAccesoEditar;

	@Getter
	@Setter
	private Boolean modoAccesoConsulta;

	@Getter
	@Setter
	private String cabeceraDialogo;

	@Getter
	private SortMeta defaultOrden;

	@Getter
	@Setter
	private List<String> listaCodigoPermisos;

	@Getter
	@Setter
	private Boolean permisoNewUsu;

	@Getter
	@Setter
	private Boolean permisoConsUsu;

	@Getter
	@Setter
	private Boolean permisoDesacUsu;

	@Getter
	@Setter
	private Boolean permisoEditUsu;

	@Getter
	@Setter
	private Boolean permisoActUsu;
	
	@Autowired
	private NavegacionBean navegacionBean;

	@Override
	@PostConstruct
	@SuppressWarnings("unchecked")
	public void init() {
		super.init();
		
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, 0);
		
		navegacionBean.setTxtMigaPan(Constantes.LISTADO_USUARIOS);
		
		listaCodigoPermisos = (List<String>) JsfUtils.getSessionAttribute(Constantes.LISTACODIGOPERMISOS);

		permisoNewUsu = listaCodigoPermisos.contains(Constantes.PERMISO_NEW_USUARIOS);

		permisoConsUsu = listaCodigoPermisos.contains(Constantes.PERMISO_CONS_USUARIOS);

		permisoDesacUsu = listaCodigoPermisos.contains(Constantes.PERMISO_DESAC_USUARIOS);

		permisoEditUsu = listaCodigoPermisos.contains(Constantes.PERMISO_EDIT_USUARIOS);
		
		permisoActUsu = listaCodigoPermisos.contains(Constantes.PERMISO_ACT_USUARIOS);

		inicializaLazyModel();

		activoFiltro = Boolean.TRUE;

	}

	private void inicializaLazyModel() {
		lazyModel = new LazyDataModelByQueryService<>(Usuario.class, usuarioService);
		lazyModel.setPreproceso((a, b, c, filters) ->
			filtrosLazyModel(filters)
		);

		modoAccesoConsulta = false;
		modoAccesoEditar = false;

		reiniciarFormulario();

		defaultOrden = SortMeta.builder().field("nombre").order(SortOrder.ASCENDING).priority(1).build();
	}

	private void filtrosLazyModel(Map<String, FilterMeta> filters) {
		if (nombreFiltro != null && !nombreFiltro.isEmpty()) {
			filters.put("nombre", new MyFilterMeta(nombreFiltro));
		}
		if (primerApellidoFiltro != null && !primerApellidoFiltro.isEmpty()) {
			filters.put("primerApellido", new MyFilterMeta(primerApellidoFiltro));
		}
		if (segundoApellidoFiltro != null && !segundoApellidoFiltro.isEmpty()) {
			filters.put("segundoApellido", new MyFilterMeta(segundoApellidoFiltro));
		}
		if (identificadorFiltro != null && !identificadorFiltro.isEmpty()) {
			filters.put("identificador", new MyFilterMeta(identificadorFiltro));
		}
		if (loginFiltro != null && !loginFiltro.isEmpty()) {
			filters.put("login", new MyFilterMeta(loginFiltro));
		}
		if (Boolean.TRUE.equals(this.activoFiltro)) {
			filters.put("activa", new MyFilterMeta(this.activoFiltro));
		}
	}

	public void reiniciarFormulario() {
		usuario = new Usuario();
		usuario.setNombre("");
		usuario.setPrimerApellido("");
		usuario.setSegundoApellido("");
		usuario.setIdentificador("");
		usuario.setLogin("");
	}

	public void limpiarFiltro() {
		nombreFiltro = "";
		primerApellidoFiltro = "";
		segundoApellidoFiltro = "";
		identificadorFiltro = "";
		loginFiltro = "";
		activoFiltro = true;
	}

	public String redireccionMenu() {
		init();
		return ListadoNavegaciones.LISTADO_USUARIOS.getRegla();
	}

	public void accesoRapido(final SelectEvent<Usuario> event) {
		modoAccesoConsulta = true;
		modoAccesoEditar = false;
		cabeceraDialogo = "Consulta de usuario";

		selectedUsuario = event.getObject();
		selectedUsuario = usuarioService.obtener(selectedUsuario.getId());
		usuario = selectedUsuario;
	}

	public void eliminarUsuario(Usuario usuarioSeleccionado) {
		try {
			usuarioSeleccionado.setActiva(false);
			usuarioService.guardar(usuarioSeleccionado);
			FacesContext.getCurrentInstance().addMessage(MENSAJESLISTADO,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "",
							MENSAJEUSUARIO + " " + usuarioSeleccionado.getIdentificador() + " "
									+ mensajesProperties.getString(ACTUALIZADOCORRECTAMENTE)));
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
	}

	public void activarUsuario(Usuario usuarioSeleccionado) {
		try {
			usuarioSeleccionado.setActiva(true);
			usuarioService.guardar(usuarioSeleccionado);
			FacesContext.getCurrentInstance().addMessage(MENSAJESLISTADO,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "",
							MENSAJEUSUARIO + " " + usuarioSeleccionado.getIdentificador() + " "
									+ mensajesProperties.getString(ACTUALIZADOCORRECTAMENTE)));
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
	}

	public String onCrear() {
		// No se ha enviado id, la persona viene inicializada por defecto.
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, ((Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS))+1);
		JsfUtils.setFlashAttribute(EDITABLE, true);
		navegacionBean.setTxtMigaPan(Constantes.ALTA_USUARIO);
		return ListadoNavegaciones.FORM_USUARIOS.getRegla();
	}

	public String onEdita(Long idUsuario) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, ((Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS))+1);
		
		// Se ha enviado id, la persona viene de BD.
		JsfUtils.setFlashAttribute(EDITABLE, true);
		JsfUtils.setFlashAttribute("idUsuario", idUsuario);
		Usuario usuarioEdit = usuarioService.obtener(idUsuario);
		navegacionBean.setTxtMigaPan(Constantes.EDICION_USUARIO+usuarioEdit.getLogin());
		return ListadoNavegaciones.FORM_USUARIOS.getRegla();
	}

	public String onConsulta(Long idUsuario) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, ((Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS))+1);
		
		JsfUtils.setFlashAttribute(EDITABLE, false);
		JsfUtils.setFlashAttribute("idUsuario", idUsuario);
		Usuario usuarioCons = usuarioService.obtener(idUsuario);
		navegacionBean.setTxtMigaPan(Constantes.CONSULTA_USUARIO+usuarioCons.getLogin());
		return ListadoNavegaciones.FORM_USUARIOS.getRegla();
	}

	public void onPersRowDblClkSelect(final SelectEvent<Usuario> event) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, ((Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS))+1);
		
		JsfUtils.setFlashAttribute(EDITABLE, false);
		Usuario p = event.getObject();
		PrimeFaces.current().ajax().addCallbackParam("id", p.getId());
		navegacionBean.setTxtMigaPan(Constantes.CONSULTA_USUARIO+p.getLogin());
	}
	
	public void onUserRowDblClkSelect(final SelectEvent<Usuario> event) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, ((Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS))+1);
		
		JsfUtils.setFlashAttribute(EDITABLE, false);
		Usuario user = event.getObject();
		PrimeFaces.current().ajax().addCallbackParam("id", user.getId());
		navegacionBean.setTxtMigaPan(Constantes.CONSULTA_USUARIO+user.getLogin());
	}

}
