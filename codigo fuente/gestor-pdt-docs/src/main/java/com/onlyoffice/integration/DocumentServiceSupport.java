package com.onlyoffice.integration;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.onlyoffice.integration.documentserver.util.file.FileUtility;
import com.onlyoffice.integration.entities.Permission;
import com.onlyoffice.integration.entities.User;

import es.juntadeandalucia.ctpda.gestionpdt.model.Documentos;
import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.Usuario;
import es.juntadeandalucia.ctpda.gestionpdt.service.DocumentosExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.DocumentosService;
import es.juntadeandalucia.ctpda.gestionpdt.service.UsuarioPerfilService;
import es.juntadeandalucia.ctpda.gestionpdt.service.UsuarioService;
import es.juntadeandalucia.ctpda.gestionpdt.util.StringUtils;
import lombok.Getter;
import lombok.SneakyThrows;


@Component
public class DocumentServiceSupport implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//URL de la aplicación que gestiona los documentos
	//(esta misma)
    @Getter
    private String storageAddress;

	@Autowired
	private transient HttpServletRequest request;

	@Autowired
	private DocumentosExpedientesService documentosExpedientesService;
	  
	@Autowired
	private DocumentosService documentosService;

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private UsuarioPerfilService usuarioPerfilService;
	
	@Autowired
	private FileUtility fileUtility;

	// Host alternativo --> VER WIKI.
	@Value("${ip.despliegue.alt:#{null}}")
    private String hostAlternativo;

 	@Value("${server.address}")
    private String serverAddress;
   
	/*
	 * This Storage configuration method should be called whenever a new storage
	 * folder is required
	 */
	public void configure(String address) {
		this.storageAddress = address;
		if (this.storageAddress == null) {
			this.storageAddress = serverAddress;
		}
	}

    public void updateFile(String idDoc, String uid, byte[] bytes) {
		this.guardar(idDoc, uid, bytes);
    }

    public Resource loadDocAsResource(String idDoc){
        final DocumentosExpedientes docExp = this.getDocumento(idDoc);
        final Documentos doc = docExp.getDocumento();
	    byte[] bytes = doc.getBytes();
	    return new InputStreamResource(new ByteArrayInputStream(bytes));
    }
    
	private DocumentosExpedientes getDocumento(String idDoc) {
		//Por si acaso elimino la extensión, si la tiene
		if(idDoc.contains(".")) {
			idDoc = fileUtility.getFileNameWithoutExtension(idDoc);
		}
		return documentosExpedientesService.obtener(Long.parseLong(idDoc));
	}
	
	//Documento sin bytes, solo datos de descripción y  nombre de fichero
	public DocumentosExpedientes getDocumentoBasico(String idDoc) {
		//Por si acaso elimino la extensión, si la tiene
		if(idDoc.contains(".")) {
			idDoc = fileUtility.getFileNameWithoutExtension(idDoc);
		}
		return documentosExpedientesService.getDocumentoBasico(Long.parseLong(idDoc));
	}
    
	@SneakyThrows
	public void guardar(String idDoc, String uid, byte[] bytes) {
		final DocumentosExpedientes docExp = this.getDocumento(idDoc);
		final Documentos d = docExp.getDocumento();
		Long idUsrEdita;
		
		try {	
			idUsrEdita = Long.parseLong(uid);
		}catch(NumberFormatException nfe) {
			throw new IllegalArgumentException("Id de usuario no numérico. (uid: " + uid + ")");
		}
		
		if(!Boolean.TRUE.equals(d.getEditable())) {
			throw new IllegalArgumentException("El documento no es editable. (idDoc: " + idDoc + ", idUsr: " + idUsrEdita + ")");
		}
		
		d.setBytes(bytes);
		d.setUsuarioUltimaEdicion(usuarioService.obtener(idUsrEdita));
		d.setFechaUltimaEdicion(LocalDateTime.now());
		
		documentosService.guardar(d);
	}


    public String getServerUrl() {
        final String host = StringUtils.defaultIfBlank(hostAlternativo, 
        			request.getServerName() + ":" + request.getServerPort());
        return request.getScheme() + "://" + host + request.getContextPath();
    }

    //-----------------------------
    
	public boolean usuarioPuedeEditar(Long idUsr, String codPerfil) {
    	return null != usuarioPerfilService.findPermisoRequerido(idUsr, codPerfil, "EDIT_EXPDOC"); //Constantes.PERMISO_EDIT_EXPDOC
    }
	
	public User getUser(String uid, String codPerfil) {
        //Cargar usuario editor
        final Usuario usuario = usuarioService.obtener(Long.parseLong(uid));
        final User user = new User();
        user.setId(Integer.parseInt(uid));
        user.setName(usuario.getNombreAp());
        
        Permission perm = new Permission();
        perm.setCommentsRemoveGroups(Collections.emptyList());
        perm.setCommentsEditGroups(Collections.emptyList());
        perm.setCommentsViewGroups(Collections.emptyList());
        perm.setComment(false);
        perm.setCopy(false);
        perm.setDownload(true);
        perm.setEdit(this.usuarioPuedeEditar(Long.parseLong(uid), codPerfil)); //¿Puede editar?
        perm.setFillForms(false);
        perm.setModifyContentControl(false);
        perm.setModifyFilter(false);
        perm.setPrint(true);
        perm.setReview(false);
        perm.setReviewGroups(Collections.emptyList());
        perm.setTemplates(false);
        user.setPermissions(perm);
        
        return user;
	}
    
}
