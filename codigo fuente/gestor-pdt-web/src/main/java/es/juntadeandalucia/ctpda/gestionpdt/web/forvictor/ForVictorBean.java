package es.juntadeandalucia.ctpda.gestionpdt.web.forvictor;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import es.juntadeandalucia.ctpda.gestionpdt.model.Usuario;
import es.juntadeandalucia.ctpda.gestionpdt.service.UsuarioService;
import es.juntadeandalucia.ctpda.gestionpdt.web.menu.NavegacionBean.ListadoNavegaciones;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.BaseBean;
import lombok.Getter;
import lombok.Setter;

public class ForVictorBean extends BaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String codigoFiltro;

	@Getter
	@Setter
	private String descripcionFiltro;

	@Getter
	@Setter
	private Boolean activoFiltro;

	@Getter
	@Setter
	private Long versionFiltro;

	@Getter
	@Setter
	private Date fechaCreacionFiltro;

	@Getter
	@Setter
	private Long selectedUsuarioFiltro;

	@Getter
	@Setter
	private List<Usuario> listaUsuarios;

	@Autowired
	private UsuarioService usuarioService;

	@PostConstruct
	@Override
	public void init() {
		super.init();
		activoFiltro = true;
		listaUsuarios = usuarioService.findUsuariosActivos();
	}

	public String redireccionMenu() {
		init();
		return ListadoNavegaciones.LISTADO_FORVICTOR.getRegla();
	}

	public String onCrear() {
		return "";
	}

	public void limpiarFiltro() {
		codigoFiltro = "";
		descripcionFiltro = "";
		activoFiltro = true;
		versionFiltro = null;
		fechaCreacionFiltro = null;
		selectedUsuarioFiltro = null;
	}
}
