package es.juntadeandalucia.ctpda.gestionpdt.web.usuarios;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.primefaces.PrimeFaces;
import org.primefaces.model.FilterMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.Perfil;
import es.juntadeandalucia.ctpda.gestionpdt.model.QUsuario;
import es.juntadeandalucia.ctpda.gestionpdt.model.Usuario;
import es.juntadeandalucia.ctpda.gestionpdt.model.UsuarioPerfil;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.service.PerfilService;
import es.juntadeandalucia.ctpda.gestionpdt.service.UsuarioPerfilService;
import es.juntadeandalucia.ctpda.gestionpdt.service.UsuarioService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ValoresDominioService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.util.NIF;
import es.juntadeandalucia.ctpda.gestionpdt.util.StringUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.Constantes;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.JsfUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.LazyDataModelByQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.MyFilterMeta;
import es.juntadeandalucia.ctpda.gestionpdt.web.menu.NavegacionBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.menu.NavegacionBean.ListadoNavegaciones;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.BaseBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.ContextoVolver;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.FacesUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.VolverBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@ViewScoped
public class DatosUsuariosBean extends BaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String IDUSUARIOTXT = "idUsuario";
	private static final String EDITABLE = "editable";
	public static final String VOLVERUSUARIOS = "_volverUsuarios_";
	private static final String ELCAMPO = "el.campo";
	private static final String IDENTIFICADOR = "identificador";
	private static final String MENSAJEERROR = "error";
	private static final String GUARDADOCORRECTAMENTE = "guardado.correctamente";
	private static final String ACTUALIZADOCORRECTAMENTE = "actualizado.correctamente";
	private static final String MENSAJEUSUARIO = "Usuario ";
	private static final String MENSAJESFORMULARIO = "messagesFormulario";

	@Getter
	private LazyDataModelByQueryService<Perfil> lazyModelPerfil;

	@Getter
	private LazyDataModelByQueryService<UsuarioPerfil> lazyModelUsuarioPerfilRelacion;

	@Autowired
	private UsuarioService usuarioService;

	@Getter
	@Setter
	private Usuario usuario;

	@Autowired
	private VolverBean volverBean;

	@Getter
	@Setter
	private Long idUsuario;

	@Getter
	@Setter
	private Long idTipoIdentificadorSeleccionado;

	@Autowired
	ContextoUsuariosBean contextoUsuariosBean;

	@Autowired
	UsuarioPerfilService usuarioPerfilService;

	@Getter
	@Setter
	private List<ValoresDominio> listaValoresDominioTipoIdentificador;

	@Autowired
	private ValoresDominioService valoresDominioService;

	@Getter
	@Setter
	private String errorIdentificadorNoValido;
	@Getter
	@Setter
	private List<String> listaCodigoPermisos;
	@Getter
	@Setter
	private Boolean permisoSaveUsu;
	@Getter
	@Setter
	private Boolean permisoConsUsu;
	@Getter
	@Setter
	private Boolean permisoEditUsu;
	@Getter
	@Setter
	private UsuarioPerfil usuarioPerfil;
	@Getter
	@Setter
	private List<Long> listaPerfilesRelacionados;

	@Autowired
	private PerfilService perfilService;
	@Getter
	@Setter
	private String descripcionPerfil;
	@Getter
	@Setter
	private String descripcionCortaPerfil;
	@Getter
	@Setter
	private String descripcionPerfilFiltro;
	@Getter
	@Setter
	private String descripcionCortaPerfilFiltro;

	@Getter
	@Setter
	private String cabeceraDialog;

	@Getter
	@Setter
	private UsuarioPerfil selectedUsuarioPerfil;

	@Getter
	@Setter
	private Boolean usuarioPerfilActivo;

	@Getter
	@Setter
	private Boolean permisoBusUsuPer;
	
	@Autowired
	private NavegacionBean navegacionBean;
	
	@Getter @Setter
	private Integer numerosSaltos;

	@PostConstruct
	@Override
	public void init() {
		super.init();
		
		numerosSaltos = (Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS);

		/**
		 * CONSULTA DE PERMISOS DEL USUARIO
		 */
		listaCodigoPermisos = (List<String>) JsfUtils.getSessionAttribute(Constantes.LISTACODIGOPERMISOS);

		permisoSaveUsu = listaCodigoPermisos.contains(Constantes.PERMISO_SAVE_USUARIOS);
		permisoConsUsu = listaCodigoPermisos.contains(Constantes.PERMISO_CONS_USUARIOS);
		permisoEditUsu = listaCodigoPermisos.contains(Constantes.PERMISO_EDIT_USUARIOS);
		permisoBusUsuPer = listaCodigoPermisos.contains(Constantes.PERMISO_BUS_USUPER);

		listaValoresDominioTipoIdentificador = valoresDominioService.findValoresTipoIdentificador();

		listaPerfilesRelacionados = new ArrayList<>();

		cargarUsuario();

		ContextoVolver cp = volverBean.recogerContexto(VOLVERUSUARIOS);
		if (cp != null) {
			idUsuario = (Long) cp.get(IDUSUARIOTXT);
			this.setFormEditable((boolean) cp.get(EDITABLE));
		}

		usuario = Optional.ofNullable(idUsuario).map(s -> usuarioService.obtener(s))
				.orElse(usuarioService.nuevoUsuario());

		if (usuario.getId() != null) {
			this.comprobacionesAtributosUsuarios();
		}

		// Comparto el objeto
		contextoUsuariosBean.setUsuario(usuario);

		ContextoVolver cv = volverBean.getContexto();

		if (cv != null) {
			if (idUsuario != null) {
				FacesUtils.setAttribute(EDITABLE, Boolean.FALSE);
			} else {
				FacesUtils.setAttribute(EDITABLE, Boolean.TRUE);
			}
		} else {
			FacesUtils.setAttribute(EDITABLE, JsfUtils.getFlashAttribute(EDITABLE));
		}

		if (idUsuario == null) {
			setFormEditable(Boolean.TRUE.equals(JsfUtils.getFlashAttribute(EDITABLE)));
		}

		if (idUsuario != null) {
			listaPerfilesRelacionados = usuarioPerfilService.findPerfilesUsuarioActivosPorUsuario(idUsuario);
			usuarioPerfilActivo = true;
			lazyModelUsuarioPerfilRelacion = new LazyDataModelByQueryService<>(UsuarioPerfil.class, usuarioPerfilService);
			lazyModelUsuarioPerfilRelacion.setPreproceso((a, b, c, filters) -> {
				filters.put("usuario.id", new MyFilterMeta(usuario.getId()));
				filters.put("activa", new MyFilterMeta(usuarioPerfilActivo));

			});
		}

		inicializaLazyModelPerfiles();

	}

	private void inicializaLazyModelPerfiles() {

		lazyModelPerfil = new LazyDataModelByQueryService<>(Perfil.class, perfilService);
		lazyModelPerfil.setPreproceso((a, b, c, filters) -> 
			filtrosLazyModel(filters)
		);

	}

	private void filtrosLazyModel(Map<String, FilterMeta> filters) {
		if (descripcionPerfilFiltro != null && !descripcionPerfilFiltro.isEmpty()) {
			filters.put("descripcion", new MyFilterMeta(descripcionPerfilFiltro));
		}
		if (descripcionCortaPerfilFiltro != null && !descripcionCortaPerfilFiltro.isEmpty()) {
			filters.put("descripcionCorta", new MyFilterMeta(descripcionCortaPerfilFiltro));
		}

		if (!listaPerfilesRelacionados.isEmpty()) {
			filters.put("#notIDPerfilesRelacionados", new MyFilterMeta(listaPerfilesRelacionados));
		}
		filters.put("#notID", new MyFilterMeta(usuario.getId()));

	}

	public void limpiarFiltroPerfiles() {
		descripcionPerfilFiltro = "";
		descripcionCortaPerfilFiltro = "";
	}

	public String onEditarByForm(Long idUsuario) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, numerosSaltos+1);
		
		JsfUtils.setFlashAttribute(IDUSUARIOTXT, idUsuario);
		JsfUtils.setFlashAttribute(EDITABLE, true);
		Usuario usuarioEdit = usuarioService.obtener(idUsuario);
		navegacionBean.setTxtMigaPan(Constantes.EDICION_USUARIO+usuarioEdit.getLogin());
		return ListadoNavegaciones.FORM_USUARIOS.getRegla();
	}

	private void comprobacionesAtributosUsuarios() {

		if (null != usuario.getValorTipoIdentificador()) {
			idTipoIdentificadorSeleccionado = usuario.getValorTipoIdentificador().getId();
		}

	}

	private void cargarUsuario() {
		ContextoVolver cv = volverBean.recogerContexto();
		idUsuario = (Long) JsfUtils.getFlashAttribute(IDUSUARIOTXT);

		if (cv != null) {
			idUsuario = (Long) cv.get(IDUSUARIOTXT);
			setFormEditable(true);
			usuario = (Usuario) cv.get("usuario");

		} else {
			idUsuario = (Long) JsfUtils.getFlashAttribute(IDUSUARIOTXT);
			setFormEditable(Boolean.TRUE.equals(JsfUtils.getFlashAttribute(EDITABLE)));
			cargarDatosPanelUsuario();
			if (idUsuario == null) {
				setFormEditable(Boolean.FALSE.equals(JsfUtils.getFlashAttribute(EDITABLE)));
			}
		}

		if (idUsuario == null) {
			// Se crea un nuevo usuario.
			usuario = new Usuario();
		} else {
			usuario = usuarioService.obtener(idUsuario);
		}

	}

	private void cargarDatosPanelUsuario() {
		if (idUsuario != null) {
			// Consulta para obtener de la BBDD el usuario
			usuario = usuarioService.obtener(idUsuario);
		}
	}

	public boolean validacionesGuardar() throws BaseException {
		boolean validoGuardar = true;
		boolean obligatoriosOk = true;
		String mensajeFinal = "";
		String errorObligatorios = "";
		boolean identificadorValido = true;
		errorIdentificadorNoValido = "";
		String errorIdentificadorRepetido = "";
		String errorIdentificadorRepetidoInactivo = "";
		String errorEmailNoValido = "";

		obligatoriosOk = validacionesObligatoriosUsuarios(obligatoriosOk);

		if (!obligatoriosOk) {
			errorObligatorios = getMessage("campos.obligatorios");
			validoGuardar = false;
		}
		/** identificador no valido **/
		identificadorValido = errorIdentificadorNoValido(obligatoriosOk);

		if (obligatoriosOk && identificadorValido && existeIdentificadorUsuarios(usuario)) {
			validoGuardar = false;
			errorIdentificadorRepetido = "El identificador (" + usuario.getIdentificador() + ") "
					+ getMessage("existe.sistema");
		} else if (obligatoriosOk && identificadorValido && existeIdentificadorUsuariosInactivos(usuario)) {
			validoGuardar = false;
			errorIdentificadorRepetidoInactivo = "El identificador (" + usuario.getIdentificador() + ") "
					+ getMessage("existe.sistema.persona.inactiva");
		}

		if (obligatoriosOk && usuario.getEmail() != null && !usuario.getEmail().isBlank()
				&& !StringUtils.esEmailValido(usuario.getEmail())) {
			validoGuardar = false;
			errorEmailNoValido = getMessages(ELCAMPO, "email", "no.valido");
		}

		mensajeFinalGuardar(mensajeFinal, errorObligatorios, errorIdentificadorNoValido, errorIdentificadorRepetido,
				errorIdentificadorRepetidoInactivo, errorEmailNoValido);

		return validoGuardar;
	}

	private void mensajeFinalGuardar(String mensajeFinal, String errorObligatorios, String errorIdentificadorNoValido,
			String errorIdentificadorRepetido, String errorIdentificadorRepetidoInactivo, String errorEmailNoValido) {
		if (!errorObligatorios.isBlank()) {
			mensajeFinal = errorObligatorios;
		}
		if (!errorEmailNoValido.isBlank()) {
			mensajeFinal += errorEmailNoValido;
		}
		if (!errorIdentificadorNoValido.isBlank()) {
			mensajeFinal += "\n" + errorIdentificadorNoValido;
		} else if (!errorIdentificadorRepetido.isBlank()) {
			mensajeFinal += "\n" + errorIdentificadorRepetido;
		} else if (!errorIdentificadorRepetidoInactivo.isBlank()) {
			mensajeFinal += "\n" + errorIdentificadorRepetidoInactivo;
		}

		if (!mensajeFinal.isBlank()) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajeFinal);
			PrimeFaces.current().dialog().showMessageDynamic(message);
		}
	}

	private boolean validacionesObligatoriosUsuarios(boolean obligatoriosOk) {

		if (StringUtils.isBlank(usuario.getNombre()) || StringUtils.isBlank(usuario.getPrimerApellido())
				|| idTipoIdentificadorSeleccionado == null || StringUtils.isBlank(usuario.getIdentificador())) {
			obligatoriosOk = false;
		}

		return obligatoriosOk;
	}

	private boolean errorIdentificadorNoValido(boolean obligatoriosOk) throws BaseException {
		boolean validacionComun = true;
		if (obligatoriosOk && null != idTipoIdentificadorSeleccionado
				&& !StringUtils.isBlank(usuario.getIdentificador())) {
			final String tipoIden = valoresDominioService
					.getCodigoTipoIdentificador(listaValoresDominioTipoIdentificador, idTipoIdentificadorSeleccionado);

			// Tengo asegurado que no es null
			usuario.setIdentificador(usuario.getIdentificador().toUpperCase());

			if (tipoIden != null && !NIF.esIdentificadorValido(tipoIden, usuario.getIdentificador())) {
				validacionComun = false;
				errorIdentificadorNoValido = getMessages(ELCAMPO, IDENTIFICADOR, "no.valido");
			}
		}
		return validacionComun;
	}

	private boolean existeIdentificadorUsuarios(Usuario u) {
		boolean returnUsuariosRepetidos = false;
		BooleanBuilder bb = new BooleanBuilder();
		if (u.getId() != null) {
			bb.and(QUsuario.usuario.id.ne(u.getId()));
		}
		bb.and(QUsuario.usuario.identificador.eq(u.getIdentificador()));
		bb.and(QUsuario.usuario.activa.eq(true));// Activos
		List<Usuario> usuariosRepetidas = (List<Usuario>) usuarioService.findAllRepository(bb, Sort.unsorted());
		if (!usuariosRepetidas.isEmpty()) {
			returnUsuariosRepetidos = true;
		}

		return returnUsuariosRepetidos;
	}

	private boolean existeIdentificadorUsuariosInactivos(Usuario u) {
		boolean returnUsuariosRepetidos = false;
		BooleanBuilder bb = new BooleanBuilder();
		if (u.getId() != null) {
			bb.and(QUsuario.usuario.id.ne(u.getId()));
		}
		bb.and(QUsuario.usuario.identificador.eq(u.getIdentificador()));
		bb.and(QUsuario.usuario.activa.eq(false));// Inactivos
		List<Usuario> usuariosRepetidas = (List<Usuario>) usuarioService.findAllRepository(bb, Sort.unsorted());
		if (!usuariosRepetidas.isEmpty()) {
			returnUsuariosRepetidos = true;
		}

		return returnUsuariosRepetidos;
	}

	public void saveUsuarios() {

		try {
			if (validacionesGuardar()) {

				String msgAccion = usuario.getId() == null ? GUARDADOCORRECTAMENTE : ACTUALIZADOCORRECTAMENTE;

				if (idTipoIdentificadorSeleccionado != null) {
					usuario.setValorTipoIdentificador(valoresDominioService.obtener(idTipoIdentificadorSeleccionado));
				} else {
					usuario.setValorTipoIdentificador(null);
				}
				
				String contrasenya = convertirSHA256(usuario.getLogin());
				usuario.setContrasenya(contrasenya);

				this.usuarioService.guardar(this.usuario);
				FacesMessage msgExito = new FacesMessage(FacesMessage.SEVERITY_INFO, "",
						MENSAJEUSUARIO + " " + usuario.getIdentificador() + " " + getMessage(msgAccion));

				contextoVolver(msgExito);
				
				navegacionBean.setTxtMigaPan(Constantes.EDICION_USUARIO+usuario.getLogin());
				PrimeFaces.current().ajax().update("textoMigaPan");
			}

		} catch (final BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
	}

	private void contextoVolver(FacesMessage msgExito) {
		ContextoVolver cv = volverBean.getContexto();
		if (cv != null) {
			// Tal como guardamos volvemos al expediente
			cv.put("identificador_usuario", usuario.getIdentificador());
			cv.put("mensaje_ok", msgExito);
			volverBean.pasarContexto(cv);
			PrimeFaces.current().executeScript("$(\"[id$='volver']\").click()");
		} else {
			FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, msgExito);
			PrimeFaces.current().ajax().update("formFormularioUsuarios");
		}
	}

	public void abrirBusquedaPerfiles() {

		if (usuario.getId() != null) {
			cabeceraDialog = "Buscar perfil relacionado para el usuario " + usuario.getIdentificador();
		} else {
			cabeceraDialog = "Buscar perfil relacionado para el usuario";
		}
		PrimeFaces.current().ajax().update("dialogBuscarPerfiles");
		PrimeFaces.current().executeScript("PF('dialogBuscarPerfiles').show();");
	}

	public void asignarPerfil(Perfil perfilSeleccionado) throws BaseException {
		UsuarioPerfil nuevoUsuarioPerfil = new UsuarioPerfil();
		if (idUsuario != null) {

			nuevoUsuarioPerfil = usuarioPerfilService.findPerfil(idUsuario, perfilSeleccionado.getId());
			if(nuevoUsuarioPerfil==null) { //no esta en BD anteriormente, se crea nuevo
				UsuarioPerfil usuPerfilIdNull = new UsuarioPerfil();
				usuPerfilIdNull.setPerfil(perfilSeleccionado);
				usuPerfilIdNull.setUsuario(usuario);
				usuPerfilIdNull.setActiva(true);
				usuarioPerfilService.guardar(usuPerfilIdNull);
			}else { //si esta en BD, solo le cambiamos el activo a true
				nuevoUsuarioPerfil.setActiva(true);
				usuarioPerfilService.guardar(nuevoUsuarioPerfil);
			}
			
		} else {
			nuevoUsuarioPerfil.setPerfil(perfilSeleccionado);
			nuevoUsuarioPerfil.setUsuario(usuario);
			nuevoUsuarioPerfil.setActiva(true);

			usuarioPerfilService.guardar(nuevoUsuarioPerfil);

		}

		FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "", MENSAJEUSUARIO + " " + usuario.getIdentificador() + " "
						+ mensajesProperties.getString(ACTUALIZADOCORRECTAMENTE)));
		listaPerfilesRelacionados = usuarioPerfilService.findPerfilesUsuarioActivosPorUsuario(usuario.getId());
	}

	public void eliminarRelPerfilUsuario(UsuarioPerfil usuarioPerfilSeleccionado) {

		try {
			usuarioPerfilSeleccionado.setActiva(false);
			usuarioPerfilService.guardar(usuarioPerfilSeleccionado);
			FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "", MENSAJEUSUARIO + " " + usuario.getIdentificador()
							+ " " + mensajesProperties.getString(ACTUALIZADOCORRECTAMENTE)));
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
		listaPerfilesRelacionados = usuarioPerfilService
				.findPerfilesUsuarioActivosPorUsuario(usuarioPerfilSeleccionado.getUsuario().getId());

	}
	
	public Boolean ocultaPerfilesAsignados() {
		Boolean res=null;
		if(idUsuario==null) {
			res=true;
		}else {
			res=false;
		}
		return res;
				
	}
	
	/**
	 * Cifrado a SHA256
	 */
	private String convertirSHA256(String password) {
		MessageDigest md = null;
		StringBuilder sb = new StringBuilder();
		try {
			md = MessageDigest.getInstance("SHA-256");
			byte[] hash = md.digest(password.getBytes());
			for (byte b : hash) {
				sb.append(String.format("%02x", b));
			}
		} catch (NoSuchAlgorithmException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
			return null;
		}
		return sb.toString();
	}

}
