package es.juntadeandalucia.ctpda.gestionpdt.web.login;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import es.juntadeandalucia.ctpda.gestionpdt.model.ConexionUsuario;
import es.juntadeandalucia.ctpda.gestionpdt.model.Dominio;
import es.juntadeandalucia.ctpda.gestionpdt.model.Perfil;
import es.juntadeandalucia.ctpda.gestionpdt.model.Usuario;
import es.juntadeandalucia.ctpda.gestionpdt.service.ConexionUsuarioService;
import es.juntadeandalucia.ctpda.gestionpdt.service.PerfilService;
import es.juntadeandalucia.ctpda.gestionpdt.service.PermisoPerfilService;
import es.juntadeandalucia.ctpda.gestionpdt.service.UsuarioService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.service.ParametroService;
import es.juntadeandalucia.ctpda.gestionpdt.util.FechaUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.SesionBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.Constantes;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.JsfUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.menu.NavegacionBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.menu.NavegacionBean.ListadoNavegaciones;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.BaseBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@SessionScope
@Slf4j
public class LoginBean extends BaseBean implements Serializable {

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	private static final String MENSAJESFORMULARIO = "messagesFormulario";
	private static final String MENSAJESDIALOGPERFILES = "messagesDialogPerfiles";
	private static final String MENSAJEERROR = "error";

	@Autowired
	private ConexionUsuarioService conexionUsuarioService;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private PerfilService perfilService;

	@Autowired
	private ParametroService parametroService;

	
	@Autowired
	private PermisoPerfilService permisoPerfilService;

	@Getter
	@Setter
	private String ultimoLogin;

	@Getter
	@Setter
	private String loginUsuario;

	@Getter
	@Setter
	private String contrasenyaUsuario;

	@Getter
	@Setter
	public String descripcionPerfil;

	@Getter
	@Setter
	public String nombreCompletoUsuario;

	@Getter
	@Setter
	private Usuario usuario;
	
	@Getter
	@Setter
	private ConexionUsuario conexionUsuario;

	@Getter
	@Setter
	private Boolean mostrarPerfiles;

	@Getter
	@Setter
	private Boolean usuarioLogado;

	@Getter
	@Setter
	private Boolean esCambioDePerfil;

	@Getter
	@Setter
	private Long selectedPerfilAsociado;

	@Getter
	@Setter
	private List<Perfil> listaPerfilesAsociados;

	@Getter
	@Setter
	private List<Dominio> listaDominios;

	@Getter
	@Setter
	private List<String> listaCodigoPermisos;

	@Autowired
	private NavegacionBean navegacionBean;
	@Autowired
	private SesionBean sesionBean;

	/**
	 * Initialize default attributes.
	 * 
	 * @throws BaseFrontException
	 */
	@PostConstruct
	@Override
	public void init() {
		super.init();

		mensajesProperties = ResourceBundle.getBundle("es.juntadeandalucia.ctpda.gestionpdt.web.Messages_es");
		/**
		 * COMPROBAMOS SI EXISTE UN USUARIO LOGADO CORRECTAMENTE. USUARIO LOGADO
		 * SIGNIFICA QUE USUARIO Y LOGIN EXISTEN EN BD Y SON CORRECTOS PERO NO TIENE POR
		 * QUÉ HABERSE SELECCIONADO AUN UN PERFIL DE ACCESO.
		 */
		if ((Boolean) JsfUtils.getSessionAttribute(Constantes.USUARIO_LOGADO) == null) {
			usuarioLogado = false;
			JsfUtils.setSessionAttribute(Constantes.USUARIO_LOGADO, usuarioLogado);
		} else {
			mostrarPerfiles = false;
		}

		listaPerfilesAsociados = null;

		descripcionPerfil = "";

		nombreCompletoUsuario = "";

		PrimeFaces.current().ajax().update("formLogin");
		PrimeFaces.current().ajax().update("formDialogPerfiles");

	}

	/**
	 * EN ESTA FUNCION SE GESTIONA EL LOGADO EN EL SISTEMA PARA REDIRIGIR A UNA
	 * PAGINA U OTRA EN FUNCIÓN DE LOS DATOS DE USUARIO Y PERFILES.
	 * 
	 * @throws ParseException
	 */
	public String login() {
		String acceso = "";
		init();

		/**
		 * SI NO EXISTE UN USUARIO LOGADO EN EL SISTEMA:
		 */
		if (Boolean.FALSE.equals(JsfUtils.getSessionAttribute(Constantes.USUARIO_LOGADO))) {
			/**
			 * VALIDAMOS EL LOGIN EN PRIMER LUGAR, ES DECIR, QUE USUARIO Y LOGIN EXISTAN EN
			 * LA BD.
			 */
			if (validarLogin()) {
				/**
				 * GUARDAMOS LA FECHA Y HORA ACTUAL PARA PODER MOSTRARLA EN EL SIGUIENTE ACCESO.
				 * GUARDAMOS TAMBIÉN QUE EL USUARIO TIENE UN LOGIN VALIDO Y LOS DATOS DEL
				 * USUARIO.
				 */
				guardarFechaUltimoAcceso();

				JsfUtils.setSessionAttribute(Constantes.USUARIO_LOGADO, usuarioLogado);
				JsfUtils.setSessionAttribute(Constantes.USUARIO, usuario);
				Authentication auth = new Authentication() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public String getName() {
						return usuario.getLogin();
					}

					@Override
					public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
						/** metodo no usado */
					}

					@Override
					public boolean isAuthenticated() {
						return true;
					}

					@Override
					public Object getPrincipal() {
						return true;
					}

					@Override
					public Object getDetails() {
						return null;
					}

					@Override
					public Collection<? extends GrantedAuthority> getAuthorities() {
						return Collections.emptyList();
					}

					@Override
					public Object getCredentials() {
						return null;
					}
				};
				SecurityContextHolder.getContext().setAuthentication(auth);

				/**
				 * GESTIONAR PERFILES: RECUPERAMOS LOS PERFILES ASOCIADOS AL USUARIO LOGADO Y EN
				 * FUNCION DEL PERFIL O PERFILES ASOCIDOS AL MISMO EL SISTEMA TENDRÁ UN
				 * COMPORTAMIENTO DISTINTO.
				 */

				listaPerfilesAsociados = perfilService.findPerfilesActivosAsociadosUsuario(usuario.getId());

				acceso = gestionarPerfiles();
			}

		}
		/**
		 * SI EXISTE UN USUARIO YA LOGADO CORRECTAMENTE, ES DECIR, USUARIO Y LOGIN
		 * VALIDADOS:
		 */
		else {
			/**
			 * RECUPERAMOS LOS DATOS DEL USUARIO
			 */
			usuario = (Usuario) JsfUtils.getSessionAttribute(Constantes.USUARIO);
			/**
			 * VALIDAMOS SI EL USUARIO HA SELECCIONADO UN PERFIL O NO.
			 */
			if (validarPerfil()) {
				/**
				 * SI EL USUARIO TIENE PERFIL VALIDO SELECCIONADO PROCEDEMOS A CARGAR LOS DATOS
				 * DE USUARIO DE LA CABECERA Y MOSTRAMOS LA PAGINA DE BIENVENIDA.
				 */
				cargarDatosUsuario(perfilService.obtener(selectedPerfilAsociado));

				acceso = ListadoNavegaciones.MENU_PAGE.getRegla();
			} else {
				/**
				 * SI EL USUARIO NO TIENE UN PERFIL VALIDO REDIGIMOS A LA PANTALLA DE LOGIN DE
				 * NUEVO
				 */
				acceso = "";
				this.loginUsuario = usuario.getLogin();
			}

		}
		return acceso;

	}

	public String redireccionMenu() {
		init();
		return ListadoNavegaciones.MENU_PAGE.getRegla();
	}

	public boolean validarPerfil() {
		boolean perfilValido = false;
		if (this.selectedPerfilAsociado == null) {
			perfilValido = false;
			this.mostrarPerfiles = true;
			this.listaPerfilesAsociados = perfilService.findPerfilesActivosAsociadosUsuario(usuario.getId());
			FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Por favor, seleccione un perfil"));
		} else {
			perfilValido = true;
		}

		return perfilValido;
	}

	public String gestionarPerfiles() {
		String acceso = "";

		if (listaPerfilesAsociados != null && !listaPerfilesAsociados.isEmpty()) {
			if (listaPerfilesAsociados.size() == 1) {
				cargarDatosUsuario(listaPerfilesAsociados.get(0));

				acceso = ListadoNavegaciones.HOME_PAGE.getRegla();
			} else {

				if (selectedPerfilAsociado == null) {
					acceso = "";
					mostrarPerfiles = true;
					PrimeFaces.current().ajax().update("formLogin:panelLogin");
				} else {
					cargarDatosUsuario(perfilService.obtener(selectedPerfilAsociado));

					acceso = ListadoNavegaciones.MENU_PAGE.getRegla();
				}
			}

		} else {
			acceso = "";
			mostrarPerfiles = false;
			/**REGISTRO INTENTO CONEXION SIN PERFIL**/
			registrarAccesoUsuario(null);
			FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "El usuario no tiene perfiles asociados."));

		}

		return acceso;

	}

	public void cargarDatosUsuario(Perfil perfil) {

		this.descripcionPerfil = perfil.getDescripcion();
		this.nombreCompletoUsuario = usuario.getNombre() + " " + usuario.getPrimerApellido() + " ";
		if (usuario.getSegundoApellido() != null) {
			this.nombreCompletoUsuario = this.nombreCompletoUsuario + usuario.getSegundoApellido();
		}

		/**
		 * RECUPERAMOS LOS PERMISOS ASOCIADOS AL PERFIL
		 */
		listaCodigoPermisos = permisoPerfilService.findCodPermisosActivosAsociadosPerfil(perfil.getId());
		JsfUtils.setSessionAttribute(Constantes.LISTACODIGOPERMISOS, listaCodigoPermisos);
		JsfUtils.setSessionAttribute(Constantes.COD_PERFIL_USUARIO, perfil.getCodigo());

		registrarAccesoUsuario(perfil);

		sesionBean.setCookie(Constantes.COOKIE_UID, usuario.getId().toString());
		sesionBean.setCookie(Constantes.COOKIE_CODPERFIL, perfil.getCodigo());
	}

	public void registrarAccesoUsuario(Perfil perfil) {
		conexionUsuario = new ConexionUsuario();
		conexionUsuario.setUsuarioLogado(usuario);
		if(perfil == null)
		{
			conexionUsuario.setFechaDeslogado(FechaUtils.ahora());
			conexionUsuario.setModoDeslogado(Constantes.LOGOUT_SIN_PERFIL);
		}else
		{
			conexionUsuario.setFechaAcceso(FechaUtils.ahora());
			conexionUsuario.setPerfilAcceso(perfil);
						
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
			        .getRequest();
			String ipLocalString = request.getRemoteAddr();
			String equipo = parametroService.getValorByCampo(ipLocalString);
			if (equipo == null) { 
				equipo = request.getRemoteHost();
			} 
			conexionUsuario.setIpConexion(ipLocalString);
			conexionUsuario.setEquipoConexion(equipo);
			
		}
			

		try {
			conexionUsuarioService.guardar(conexionUsuario);
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
	}

	public void guardarFechaUltimoAcceso() {
		this.ultimoLogin = usuario.getFechaUltimoAcceso();
		usuario.setFechaUltimoAcceso(FechaUtils.fechaYHoraActualDate());
		try {
			usuarioService.guardar(usuario);
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
	}

	public boolean validarLogin() {
		boolean loginValido = false;
		String contrasenyaUsuarioCifrado = convertirSHA256(contrasenyaUsuario);

		// VALIDACION OBLIGATORIEDAD
		if ((loginUsuario == null || loginUsuario.isEmpty())
				|| (!(boolean) JsfUtils.getSessionAttribute(Constantes.USUARIO_LOGADO)
						&& (contrasenyaUsuarioCifrado == null || contrasenyaUsuarioCifrado.isEmpty()))) {
			FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
							mensajesProperties.getString("login.usuario.campos.obligatorios")));
			//
		}

		if (loginUsuario != null && !loginUsuario.isEmpty()
				&& (!(boolean) JsfUtils.getSessionAttribute(Constantes.USUARIO_LOGADO)
						&& (contrasenyaUsuarioCifrado != null && !contrasenyaUsuarioCifrado.isEmpty()))
				&& (this.selectedPerfilAsociado == null)) {
			usuario = usuarioService.findUsuarioActivo(loginUsuario, contrasenyaUsuarioCifrado);

			if (usuario == null) {
				loginValido = false;
				usuarioLogado = false;
				FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "", getMessage("login.usuario.no.registrado")));
			} else {
				usuarioLogado = true;
				loginValido = true;
			}

		} else if ((boolean) JsfUtils.getSessionAttribute(Constantes.USUARIO_LOGADO)) {

			if (this.selectedPerfilAsociado == null) {
				loginValido = false;
				FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "", getMessage("login.usuario.no.registrado")));
			} else {
				loginValido = true;
			}

		}

		return loginValido;
	}


	public String logOut(String tipoDeslogado) {
		JsfUtils.removeSessionAttribute(Constantes.USUARIO_LOGADO);
		JsfUtils.removeSessionAttribute(Constantes.USUARIO);
		JsfUtils.removeSessionAttribute(Constantes.LISTACODIGOPERMISOS);
		JsfUtils.removeSessionAttribute(Constantes.COD_PERFIL_USUARIO);
		
		sesionBean.borrarCookies();
		
		if(Constantes.LOGOUT_MANUAL.equals(tipoDeslogado)) {
			registrarLogOutUsuario(Constantes.LOGOUT_MANUAL);	
		}else if(Constantes.LOGOUT_INACTIVIDAD.equals(tipoDeslogado))
		{
			registrarLogOutUsuario(Constantes.LOGOUT_INACTIVIDAD);	
		}

		this.limpiarFormulario();

		return ListadoNavegaciones.LOGIN_PAGE.getRegla();
	}
	
	private void registrarLogOutUsuario(String tipoDeslogado)
	{
		conexionUsuario.setFechaDeslogado(FechaUtils.ahora());
		if(Constantes.LOGOUT_MANUAL.equals(tipoDeslogado))
		{
			conexionUsuario.setModoDeslogado(Constantes.LOGOUT_MANUAL);	
		}else if(Constantes.LOGOUT_INACTIVIDAD.equals(tipoDeslogado)) {
			conexionUsuario.setModoDeslogado(Constantes.LOGOUT_INACTIVIDAD);
		}else if(Constantes.LOGOUT_CAMBIO_PERFIL.equals(tipoDeslogado)){
			conexionUsuario.setModoDeslogado(Constantes.LOGOUT_CAMBIO_PERFIL);
		}

		try {
			conexionUsuarioService.guardar(conexionUsuario);
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
	}

	private void limpiarFormulario() {
		this.selectedPerfilAsociado = null;
		this.contrasenyaUsuario = "";
		this.loginUsuario = "";
		this.mostrarPerfiles = false;
		this.listaPerfilesAsociados = null;
		PrimeFaces.current().ajax().update("formLogin");
	}

	public void cambiarPerfil() {
		this.selectedPerfilAsociado = null;

		usuario = (Usuario) JsfUtils.getSessionAttribute(Constantes.USUARIO);
		this.listaPerfilesAsociados = perfilService.findPerfilesActivosAsociadosUsuario(usuario.getId());

		PrimeFaces.current().ajax().update("formDialogPerfiles");

		PrimeFaces.current().executeScript("PF('dialogPerfiles').show();");

	}

	public String cambiarPerfilLogin() {
		this.selectedPerfilAsociado = null;
		this.mostrarPerfiles = true;
		this.esCambioDePerfil = true;

		usuario = (Usuario) JsfUtils.getSessionAttribute(Constantes.USUARIO);
		this.listaPerfilesAsociados = perfilService.findPerfilesActivosAsociadosUsuario(usuario.getId());

		return ListadoNavegaciones.LOGIN_PAGE.getRegla();
	}

	public String redireccionInicioCambioPerfil() {

		Usuario usuarioActual = (Usuario) JsfUtils.getSessionAttribute(Constantes.USUARIO);
		Perfil perfilSeleccionado;

		if (this.selectedPerfilAsociado != null) {
			perfilSeleccionado = perfilService.obtener(this.selectedPerfilAsociado);
			this.descripcionPerfil = perfilSeleccionado.getDescripcion();
			this.nombreCompletoUsuario = usuarioActual.getNombre() + " " + usuarioActual.getPrimerApellido() + " ";
			if (usuarioActual.getSegundoApellido() != null) {
				this.nombreCompletoUsuario = this.nombreCompletoUsuario + usuarioActual.getSegundoApellido();
			}

			this.listaPerfilesAsociados = perfilService.findPerfilesActivosAsociadosUsuario(usuarioActual.getId());

			this.listaCodigoPermisos = permisoPerfilService
					.findCodPermisosActivosAsociadosPerfil(perfilSeleccionado.getId());
			
			registrarLogOutUsuario(Constantes.LOGOUT_CAMBIO_PERFIL);
			usuario = usuarioActual;			
			registrarAccesoUsuario(perfilSeleccionado);

			JsfUtils.setSessionAttribute(Constantes.LISTACODIGOPERMISOS, listaCodigoPermisos);
			JsfUtils.setSessionAttribute(Constantes.COD_PERFIL_USUARIO, perfilSeleccionado.getCodigo());

			sesionBean.setCookie(Constantes.COOKIE_CODPERFIL, perfilSeleccionado.getCodigo());

		} else {
			this.mostrarPerfiles = true;
			this.listaPerfilesAsociados = perfilService.findPerfilesActivosAsociadosUsuario(usuario.getId());
			FacesContext.getCurrentInstance().addMessage(MENSAJESDIALOGPERFILES,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Por favor, seleccione un perfil"));
			return "";
		}

		navegacionBean.setTxtMigaPan("");
		return ListadoNavegaciones.HOME_PAGE.getRegla();
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

	public void logOutExpirado(String tipoDeslogado) {
		this.logOut(tipoDeslogado);
	}

}
