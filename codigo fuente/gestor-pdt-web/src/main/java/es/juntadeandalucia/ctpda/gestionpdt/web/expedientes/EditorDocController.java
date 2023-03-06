package es.juntadeandalucia.ctpda.gestionpdt.web.expedientes;

import java.io.ByteArrayInputStream;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.onlyoffice.integration.DocumentServiceSupport;
import com.onlyoffice.integration.documentserver.callbacks.CallbackHandler;
import com.onlyoffice.integration.documentserver.managers.jwt.JwtManager;
import com.onlyoffice.integration.documentserver.models.enums.Action;
import com.onlyoffice.integration.documentserver.models.enums.Language;
import com.onlyoffice.integration.documentserver.models.enums.Type;
import com.onlyoffice.integration.documentserver.models.filemodel.FileModel;
import com.onlyoffice.integration.dto.Track;
import com.onlyoffice.integration.entities.User;
import com.onlyoffice.integration.services.configurers.FileConfigurer;
import com.onlyoffice.integration.services.configurers.wrappers.DefaultFileWrapper;

import es.juntadeandalucia.ctpda.gestionpdt.model.DatosTmpUsuario;
import es.juntadeandalucia.ctpda.gestionpdt.model.Documentos;
import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.service.DatosTmpUsuarioService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.Constantes;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.BaseBean;
import lombok.Getter;
import lombok.Setter;

@CrossOrigin("*")
@Controller
public class EditorDocController  extends BaseBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private Expedientes expedientes;
	
	@Autowired
	private DocumentServiceSupport documentServiceSupport;	
	

    @Value("${files.docservice.url.site}")
    private String docserviceSite;

    @Value("${files.docservice.url.api}")
    private String docserviceApiUrl;
    
    @Autowired
    private JwtManager jwtManager;
    
    @Value("${files.docservice.header}")
    private String documentJwtHeader;

    @Value("${filesize-max}")
    private String filesizeMax;
    
    @Autowired
    private CallbackHandler callbackHandler;
     
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FileConfigurer<DefaultFileWrapper> fileConfigurer;

	@Autowired
	private DatosTmpUsuarioService datosTmpUsuarioService;

	
    @PostMapping(path = "${url.editor}")
    // process request to open the editor page
    public String index(@RequestParam("fileName") String fileName,
                        @RequestParam(value = "action", required = false) String actionParam,
                        @RequestParam(value = "type", required = false) String typeParam,
                        @RequestParam(value = "actionLink", required = false) String actionLink,
                        @CookieValue(value = Constantes.COOKIE_UID) String uid,
                        @CookieValue(value = Constantes.COOKIE_CODPERFIL) String codPerfil,
                        @CookieValue(value = "ulang") String lang,
                        Model model) {
        Action action = Action.EDIT;
        Type type = Type.DESKTOP;
        Language language = Language.ES;
 
        if(actionParam != null) action = Action.valueOf(actionParam.toUpperCase());
        if(typeParam != null) type = Type.valueOf(typeParam.toUpperCase());
        if(lang != null) language = Language.valueOf(lang.toUpperCase());

        //Cargar usuario editor
        final User onlyUser = documentServiceSupport.getUser(uid, codPerfil);
        //fileName contiene un id de documento-expediente
        final DocumentosExpedientes docExp = documentServiceSupport.getDocumentoBasico(fileName);
        final Documentos doc = docExp.getDocumento();

        //Independientemente de que el usuario tenga permiso para editar, 
        // si el documento no es editable debe forzarse el modo consulta
        if(!Boolean.TRUE.equals(doc.getEditable())) {
        	action=Action.VIEW;
        }
        
        // get file model with the default file parameters
        FileModel fileModel = fileConfigurer.getFileModel(
                DefaultFileWrapper
                        .builder()
                //aunque filename sea el id de docExp hay que adjuntar la extensión para que funcione
                        .fileName(fileName + "." + doc.getExtensionFichero())
                        .type(type)
                        .lang(language)
                        .action(action)
                        .user(onlyUser)
                        .actionData(actionLink)
                        .build()
        );
        
        fileModel.getDocument().setTitle(docExp.getDescripcionDocumento());

        // add attributes to the specified model
        //model.addAttribute("model", fileModel);  // add file model with the default parameters to the original model
        Gson gson = new Gson();
        model.addAttribute("modelJS", gson.toJson(fileModel));  // add file model with the default parameters to the original model

        //model.addAttribute("fileHistory", historyManager.getHistory(fileModel.getDocument()));  // get file history and add it to the model
        model.addAttribute("docserviceApiUrl",docserviceSite + docserviceApiUrl);  // create the document service api URL and add it to the model
        //model.addAttribute("dataInsertImage",  getInsertImage());  // get an image and add it to the model
        //model.addAttribute("dataCompareFile",  getCompareFile());  // get a document for comparison and add it to the model
        //model.addAttribute("dataMailMergeRecipients", getMailMerge());  // get recipients data for mail merging and add it to the model
        //model.addAttribute("usersForMentions", getUserMentions(uid));  // get user data for mentions and add it to the model
        return "/aplicacion/expedientes/expedientes/editorDoc.jsp";
    }    

    // download data from the specified file
    private ResponseEntity<Resource> downloadFile(String fileName){
        //Resource resource = storageMutator.loadFileAsResource(fileName);  // load the specified file as a resource
        Resource resource = documentServiceSupport.loadDocAsResource(fileName);
        String contentType = "application/octet-stream";

        // create a response with the content type, header and body with the file data
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    

    @GetMapping(path = "${url.download}")
    public ResponseEntity<Resource> download(HttpServletRequest request,  // download a file
                                             @RequestParam("fileName") String fileName){
        try{
            // check if a token is enabled or not
            if(jwtManager.tokenEnabled()){
                String header = request.getHeader(documentJwtHeader == null  // get the document JWT header
                        || documentJwtHeader.isEmpty() ? "Authorization" : documentJwtHeader);
                if(header != null && !header.isEmpty()){
                    String token = header.replace("Bearer ", "");  // token is the header without the Bearer prefix
                    jwtManager.readToken(token);  // read the token
                }
            }
            return downloadFile(fileName);  // download data from the specified file
        } catch(Exception e){
            return null;
        }
    }
    
    @PostMapping(path = "${url.track}")
    @ResponseBody
    public String track(HttpServletRequest request,  // track file changes
                        @RequestParam("fileName") String fileName,
                        @RequestParam("userAddress") String userAddress,
                        @RequestBody Track body){
        try {
            String bodyString = objectMapper.writeValueAsString(body);  // write the request body to the object mapper as a string
            String header = request.getHeader(documentJwtHeader == null  // get the request header
                    || documentJwtHeader.isEmpty() ? "Authorization" : documentJwtHeader);

            if (bodyString.isEmpty()) {  // if the request body is empty, an error occurs
                throw new BaseException("{\"error\":1,\"message\":\"Request payload is empty\"}");
            }

            JSONObject bodyCheck = jwtManager.parseBody(bodyString, header);  // parse the request body
            body = objectMapper.readValue(bodyCheck.toJSONString(), Track.class);  // read the request body
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }

        int error = callbackHandler.handle(body, fileName);

        return"{\"error\":" + error + "}";
    }

    //Para el visor JS
    @GetMapping(path = "/ViewDocTmp")
    public ResponseEntity<Resource> verDocTmpGenerado(HttpServletRequest request){
    	
    	final Long idTmp = Long.parseLong(request.getParameter("id"));
    	String nombreFichero = "fichero";
    	String tipoContenido = "application/vnd.oasis.opendocument.text";
    	byte[] bb = new byte[0];
    	
    	if(idTmp != null) {
    		try {
    			final DatosTmpUsuario tmp = datosTmpUsuarioService.obtener(idTmp);
    			bb = tmp.getBytes();
    			nombreFichero = tmp.getNombre();
    			tipoContenido = tmp.getTipoContenido();
    		} catch(Exception e) {
    			e.printStackTrace();
    		}
    	}
		
		final Resource resource = new InputStreamResource(new ByteArrayInputStream(bb));

		return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(tipoContenido))
                .contentLength(bb.length)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nombreFichero + "\"")
                .body(resource);
     }
   
}
