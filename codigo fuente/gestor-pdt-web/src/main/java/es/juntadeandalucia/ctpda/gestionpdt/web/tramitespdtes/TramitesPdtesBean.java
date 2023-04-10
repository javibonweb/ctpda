package es.juntadeandalucia.ctpda.gestionpdt.web.tramitespdtes;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;

import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.web.forvictor.ForVictorBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.menu.NavegacionBean.ListadoNavegaciones;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.BaseBean;
import lombok.extern.slf4j.Slf4j;

@Component
@ViewScoped
@Slf4j
public class TramitesPdtesBean extends BaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String MODOACCESO = "modoAcceso";
	private static final String EDITABLE = "editable";

	@PostConstruct
	@Override
	public void init() {
		super.init();
	}

	public String redireccionMenu() {
		init();
		return ListadoNavegaciones.LISTADO_TRAMITES_PDTES.getRegla();
	}
}
