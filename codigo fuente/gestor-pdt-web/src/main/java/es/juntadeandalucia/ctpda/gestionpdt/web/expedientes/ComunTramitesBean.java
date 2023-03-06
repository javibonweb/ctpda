package es.juntadeandalucia.ctpda.gestionpdt.web.expedientes;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;

import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.TramiteExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.service.TramiteExpedienteService;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.BaseBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@ViewScoped
public class ComunTramitesBean extends BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String TITULOTRAMITE = "tituloTramite";
	private static final String IDEXECUTESCRIPT = "$(\"[id$='";
	private static final String PARENTEXECUTESCRIPT = "']\").parent().click();";
	
	
	@Autowired
	private TramiteExpedienteService tramiteExpedienteService;

	
	@Getter @Setter
	private String idTituloTramiteConsultaTramite;
	@Getter @Setter
	private Boolean idTituloTramiteConsultaTramitePrimerAcceso;
	@Getter @Setter
	private Boolean idTituloTramiteConsultaTramiteSegundoAcceso;

	
	@Override
	@PostConstruct
	public void init() {
		super.init();
		
		idTituloTramiteConsultaTramite = "";
		idTituloTramiteConsultaTramitePrimerAcceso = false;
		idTituloTramiteConsultaTramiteSegundoAcceso = false;

	}
	
	public void onConsultarTramite(Long idTramite) {
		TramiteExpediente tramiteExpediente = tramiteExpedienteService.obtener(idTramite);
		
		PrimeFaces.current().executeScript("$(\"[id$='tituloTabTramitacion']\").parent().click();");
		
		//primer acceso
		if(Boolean.TRUE.equals(idTituloTramiteConsultaTramite.isBlank() && !idTituloTramiteConsultaTramitePrimerAcceso) && Boolean.TRUE.equals(!idTituloTramiteConsultaTramiteSegundoAcceso)) {
			idTituloTramiteConsultaTramite = TITULOTRAMITE+idTramite.toString();
			idTituloTramiteConsultaTramitePrimerAcceso = true;
		}else	
		//segundo acceso
		if(Boolean.TRUE.equals(!idTituloTramiteConsultaTramite.isBlank() && idTituloTramiteConsultaTramitePrimerAcceso) && Boolean.TRUE.equals(!idTituloTramiteConsultaTramiteSegundoAcceso)) {
			segundoAcceso(tramiteExpediente,idTramite);
		}else
		//tercero acceso
		if(Boolean.TRUE.equals(!idTituloTramiteConsultaTramite.isBlank() && idTituloTramiteConsultaTramitePrimerAcceso) && Boolean.TRUE.equals(idTituloTramiteConsultaTramiteSegundoAcceso)) {
			tercerAcceso(tramiteExpediente,idTramite);
		}	
	}
	
	private void tercerAcceso (TramiteExpediente tramiteExpediente, Long idTramite) {
		if(idTituloTramiteConsultaTramite.equals(TITULOTRAMITE+idTramite.toString())) {
			log.info("no hace nada");
		}else {
			verificoSuperiorYDespliego();
			PrimeFaces.current().executeScript(IDEXECUTESCRIPT + idTituloTramiteConsultaTramite + PARENTEXECUTESCRIPT);
			
			boolean tieneSuperior = despliegoSuperior(tramiteExpediente);
			if(!tieneSuperior) {
				idTituloTramiteConsultaTramite = TITULOTRAMITE+idTramite.toString();
				PrimeFaces.current().executeScript(IDEXECUTESCRIPT + idTituloTramiteConsultaTramite + PARENTEXECUTESCRIPT);
			}		
		}
	}
	
	private void segundoAcceso (TramiteExpediente tramiteExpediente, Long idTramite) {
		boolean tieneSuperior = despliegoSuperior(tramiteExpediente);		
		if(!tieneSuperior) {
			idTituloTramiteConsultaTramite = TITULOTRAMITE+idTramite.toString();
			PrimeFaces.current().executeScript(IDEXECUTESCRIPT + idTituloTramiteConsultaTramite + PARENTEXECUTESCRIPT);
		}				
		idTituloTramiteConsultaTramiteSegundoAcceso = true;
	}
	
	private boolean despliegoSuperior(TramiteExpediente tramiteExpediente) {
		boolean tieneSuperior = false;
		if(tramiteExpediente.getTramiteExpedienteSup()!=null) {
			tieneSuperior = true;
			String idTituloTramiteConsultaTramiteSuperior = TITULOTRAMITE+tramiteExpediente.getTramiteExpedienteSup().getId().toString();
			PrimeFaces.current().executeScript(IDEXECUTESCRIPT + idTituloTramiteConsultaTramiteSuperior + PARENTEXECUTESCRIPT);	
			String idTituloTramiteConsultaTramiteHijo = "tituloSubTramite"+tramiteExpediente.getId().toString();
			PrimeFaces.current().executeScript(IDEXECUTESCRIPT + idTituloTramiteConsultaTramiteHijo + PARENTEXECUTESCRIPT);	
		}
		return tieneSuperior;
	}
	
	private void verificoSuperiorYDespliego () {
		String[] parts = idTituloTramiteConsultaTramite.split(TITULOTRAMITE);
		String idAnteriorAux = parts[1];
		TramiteExpediente tramiteExpediente = tramiteExpedienteService.obtener(Long.parseLong(idAnteriorAux));		
		despliegoSuperior(tramiteExpediente);
	}	
	
}
