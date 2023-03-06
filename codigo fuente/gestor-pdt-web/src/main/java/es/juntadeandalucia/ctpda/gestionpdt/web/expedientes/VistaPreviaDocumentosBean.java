package es.juntadeandalucia.ctpda.gestionpdt.web.expedientes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.util.EscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentoDTO;
import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.service.DocumentosExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.util.FileUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.Constantes;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.BaseBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.FacesUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@SessionScope
public class VistaPreviaDocumentosBean extends BaseBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Autowired
	private DocumentosExpedientesService documentosExpedientesService;
	
	@Getter
	private transient StreamedContent vistaPrevia;
	@Getter
	private String nombreDoc;

	//--------------------------

    private void crearStreamedContent(String nombre, byte[] bytesPdf) {
		this.vistaPrevia = DefaultStreamedContent.builder()
	                .name(nombre)
	                .contentLength(bytesPdf.length)
	                .contentType(Constantes.MIME_PDF)
	                .stream(() -> new ByteArrayInputStream(bytesPdf))
	                .build();
    }

    //****************************
    /**
     * Para ser usado desde una petición GET (ver PF showcase)
     * https://www.primefaces.org/showcase-ext/sections/documentviewer/basic.jsf
     */
    public void onPrerender() {
		cargarUrl();	
    }
    
    /** Sin parámetros: Se buscan en el objeto request (getRequestParameter "nombre" y "url") */
    public void cargarUrl() {
		String nombre = FacesUtils.getRequestParameter("nombre");
		String url = FacesUtils.getRequestParameter("url");

		this.cargarUrl(nombre, url);
	}

    //*****************************
    
    public void cargarUrl(DocumentoDTO doc) {
		String nombre = doc.getNombreFicheroVistaPrevia();
		String url = doc.getUrlVistaPrevia();
		
		this.cargarUrl(nombre, url);
	}
    
    public void cargarUrl(String nombre, String url) {
		final byte[] bytesPdf;
		
		try {
			bytesPdf = (url == null)? new byte[0] : FileUtils.leerBytesURLRemota(url);
		} catch (IOException e) {
			facesMsgErrorKey(CLAVE_ERROR_GENERICO);
			return;
		}
		
		nombreDoc = nombre;
	    crearStreamedContent(nombre, bytesPdf);
	    abrirDialogoVisor();
    }        
    
    public void cargarDocExp(String nombre, String strId) {
		final byte[] bytesPdf;
		
		if(strId != null) {
			DocumentosExpedientes docExp = documentosExpedientesService.obtener(Long.parseLong(strId));
			bytesPdf =  docExp.getDocumento().getBytes();
		} else {
			bytesPdf = new byte[0];
		}
		
		nombreDoc = nombre;
	    crearStreamedContent(nombre, bytesPdf);
 	    abrirDialogoVisor();
   }
    
   private void abrirDialogoVisor() {
    	final Map<String, Object> options = new HashMap<>();
    	options.put("modal", false);
    	//options.put("minimizable", true)
    	//options.put("maximizable", true)
    	options.put("resizable", true);
    	options.put("responsive", true);
    	//options.put("showEffect", "fade")
    	//options.put("hideEffect", "fade")
    	options.put("contentWidth", "100%");
    	options.put("contentHeight", "100%");
    	options.put("width", 900);
    	options.put("height", 700);
    	
     	final String rutaDialog = "/aplicacion/comun/dialogVisorPdf2";
    	
    	PrimeFaces.current().dialog().openDynamic(rutaDialog, options, null);
    	//PrimeFaces.current().executeScript("traerVisorAlFrente('" + getWidgetVar() + "')")
   }
   
   //De momento no sirve
   public String getWidgetVar() {
	   //String id = nombre.replaceAll("\\'|\\\"", "-")
	   return EscapeUtils.forJavaScript(this.nombreDoc);
   }
    
}
