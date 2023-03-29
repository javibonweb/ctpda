package es.juntadeandalucia.ctpda.gestionpdt.web.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import es.juntadeandalucia.ctpda.gestionpdt.service.ExpedientesRelacionService;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.Constantes;
import lombok.Getter;


@Component
@SessionScope
public class NavegacionBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final ResourceBundle NAVEGACION_BUNDLE = ResourceBundle.getBundle("es.juntadeandalucia.ctpda.gestionpdt.web.navegacion");
	
	@Autowired
	private ExpedientesRelacionService expedientesRelacionService;

	public enum ListadoNavegaciones {

		// Comunes
		TEMPLATE_LAYOUT_GENERICO,
		TEMPLATE_LAYOUT_CABECERA,
		CABECERA_WEB,
		HOME_PAGE,
		LOGIN_PAGE,
		MENU_PAGE,
		DIALOG_VISOR_PDF,
				
		//Materias
		LISTADO_MATERIAS,
		//Parametros
		LISTADO_PARAMETROS,
		//Trámites abiertos
		LISTADO_TRAMITES_ABIERTOS,
		//Notificaciones abiertas
		LISTADO_NOTIFICACIONES_ABIERTAS,
		//Notificaciones pendientes
		LISTADO_NOTIFICACIONES_PENDIENTES,
		//Firmas abiertas
		LISTADO_FIRMAS_ABIERTAS,
		//Motivos de relacion
		LISTADO_MOTIVOS_RELACION,
		//Dominios
		LISTADO_DOMINIOS,
		LISTADO_VALORES_DOMINIO,
		FORM_VALORES_DOMINIO,
		//Personas
		LISTADO_PERSONAS,
		FORM_PERSONAS,
		//Tipos de agrupación
		LISTADO_TIPOS_AGRUPACION,
		FORM_TIPOS_AGRUPACION,
		//Sujetos obligados
		LISTADO_SUJETOS_OBLIGADOS,
		FORM_SUJETOS_OBLIGADOS,
		//Expedientes
		LISTADO_EXPEDIENTES,
		LISTADO_ENTRADAS,
		FORM_EXPEDIENTES,
		FORM_DATOSGENERALES_EXPEDIENTES,
		//Control plazos
		LISTADO_CONTROL_PLAZOS,
		//Mi mesa de tareas
		LISTADO_TAREAS_MI_MESA,
		LISTADO_TAREAS_ENVIADAS_PENDIENTES,
		//Plantillas,
		LISTADO_PLANTILLAS,
		//Usuarios
		LISTADO_USUARIOS,
		FORM_USUARIOS,
		//Resoluciones
		LISTADO_RESOLUCIONES,
		FORM_RESOLUCION,

		

		FIN //No se usa. Hace de terminador
		;

		public String getRegla() {
			return NAVEGACION_BUNDLE.getString(this.name());
		}

	}

	public String get(String regla){
		return ListadoNavegaciones.valueOf(regla).getRegla();
	}

    public String get(ListadoNavegaciones listadoNavegaciones){
    	return listadoNavegaciones.getRegla();
    }
    
	@Getter
	private String txtMigaPan;

	public void setTxtMigaPan(String txtMigaPan) {
		this.txtMigaPan = txtMigaPan;
	}
	
	public String expedientesRelacionadosMotivoPsan (Long idExp) {
		StringBuilder expedientesRelacionados = new StringBuilder();
		String expedientesRelacionadosRes = "";
		List<String> expedientesRelacionadosOrigenList = expedientesRelacionService.findExpRelacionadosOrigenByExpAndMotivo(idExp, Constantes.PSAN);
		List<String> expedientesRelacionadosRelacionadosList = expedientesRelacionService.findExpRelacionadosRelacionadoByExpAndMotivo(idExp, Constantes.PSAN);
		List<String> expedientesRelacionadosOrigenYRelacionados = new ArrayList<>();
		
		if(!expedientesRelacionadosOrigenList.isEmpty()) {
			for(int i = 0; i<expedientesRelacionadosOrigenList.size(); i++) {
				expedientesRelacionadosOrigenYRelacionados.add(expedientesRelacionadosOrigenList.get(i));
			}
		}
		if(!expedientesRelacionadosRelacionadosList.isEmpty()) {
			for(int i = 0; i<expedientesRelacionadosRelacionadosList.size(); i++) {
				expedientesRelacionadosOrigenYRelacionados.add(expedientesRelacionadosRelacionadosList.get(i));
			}
		}
		
		if(!expedientesRelacionadosOrigenYRelacionados.isEmpty()) {
			expedientesRelacionados.append(" (");
			for(int i = 0; i < expedientesRelacionadosOrigenYRelacionados.size(); i++) {
				expedientesRelacionados.append(expedientesRelacionadosOrigenYRelacionados.get(i)+", ");
			}
			
			expedientesRelacionadosRes = StringUtils.removeEnd(expedientesRelacionados.toString(), ", ");
			expedientesRelacionadosRes += ")";
		}
		
		
		return expedientesRelacionadosRes;
	}

}
