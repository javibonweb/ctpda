package es.juntadeandalucia.ctpda.gestionpdt.web.expedientes;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentoDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.DocumentosExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.BaseBean;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@ViewScoped
public class ComunDocumentosBean extends BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private DocumentosExpedientesService documentosExpedientesService;

	@Autowired
	private transient HttpServletRequest request;

	@Override
	@PostConstruct
	public void init() {
		super.init();		
	}
	
	public void vistaPreviaFichero(DocumentoDTO dto) {
		try {
			documentosExpedientesService.generarVistaPreviaOdt(dto, request.getServerName(), request.getServerPort());			
			PrimeFaces.current().ajax().addCallbackParam("ok", true);
		} catch (IOException e) { //IOException incluye BaseException
			facesMsgErrorKey(CLAVE_ERROR_GENERICO);
			e.printStackTrace();
		}
	}
	
}
