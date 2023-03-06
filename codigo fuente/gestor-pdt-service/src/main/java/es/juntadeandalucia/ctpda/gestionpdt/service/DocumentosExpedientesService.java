package es.juntadeandalucia.ctpda.gestionpdt.service;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.AgrupacionesDocumentos;
import es.juntadeandalucia.ctpda.gestionpdt.model.CfgDocExpedienteTramitacion;
import es.juntadeandalucia.ctpda.gestionpdt.model.DatosTmpUsuario;
import es.juntadeandalucia.ctpda.gestionpdt.model.DetalleExpdteTram;
import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentoDTO;
import es.juntadeandalucia.ctpda.gestionpdt.model.Documentos;
import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentosExpedientesTramites;
import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentosTramiteMaestra;
import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.Personas;
import es.juntadeandalucia.ctpda.gestionpdt.model.PersonasExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.Plantillas;
import es.juntadeandalucia.ctpda.gestionpdt.model.PlantillasDocumentos;
import es.juntadeandalucia.ctpda.gestionpdt.model.Provincias;
import es.juntadeandalucia.ctpda.gestionpdt.model.QDocumentosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.SujetosObligados;
import es.juntadeandalucia.ctpda.gestionpdt.model.SujetosObligadosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.TramiteExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.core.Auditable;
import es.juntadeandalucia.ctpda.gestionpdt.repository.DocumentosExpedientesRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.ValidacionException;
import es.juntadeandalucia.ctpda.gestionpdt.util.FechaUtils;
import es.juntadeandalucia.ctpda.gestionpdt.util.FileUtils;
import es.juntadeandalucia.ctpda.gestionpdt.util.StringUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jooreports.templates.DocumentTemplate;
import net.sf.jooreports.templates.DocumentTemplateException;
import net.sf.jooreports.templates.DocumentTemplateFactory;

@Service
@Slf4j
public class DocumentosExpedientesService extends AbstractCRUDService<DocumentosExpedientes> {
	
	
	public static final String TRAM_REQUERIM_INFO_ADICIONAL = "REQINF";
	public static final String TRAM_REQUERIM_SUBSANACION = "SUB";
	public static final String TRAM_TRASLADO_DPD = "TRDPD";
	public static final String TRAM_DECISION_ADMISION = "ACADM";
	

	/**
	 * Serial id
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private DocumentosService documentosService;
	@Autowired
	private TipoDocumentoService tipoDocumentoService;
	@Autowired
	private ValoresDominioService valoresDominioService;
	@Autowired
	private PersonasService personasService;
	@Autowired
	private TramiteExpedienteService tramiteExpedienteService;
	@Autowired
	private TareasExpedienteService tareasExpedienteService;
	@Autowired
	private DocumentosExpedientesTramitesService documentosExpedientesTramitesService;
	@Autowired
	private AgrupacionesDocumentosService agrupacionesDocumentosService;
	@Autowired
	private PlantillasService plantillaService;
	@Autowired
	private PlantillasDocumentosService plantillasDocumentosService;
	@Autowired
	private PersonasExpedientesService personasExpedientesService;
	@Autowired
	private SujetosObligadosExpedientesService sujetosObligadosExpedientesService;
	@Autowired
	private DetalleExpdteTramService detalleExpdteTramService;
	@Autowired
	private DatosTmpUsuarioService datosTmpUsuarioService;
	

	
	DocumentosExpedientesRepository documentosExpedientesRepository;
	
	@Autowired
	private ExpedientesService expedientesService;
	
	
	protected DocumentosExpedientesService (@Autowired MathsQueryService mathsQueryService,
			@Autowired DocumentosExpedientesRepository documentosExpedientesRepository) {
		super(mathsQueryService, documentosExpedientesRepository, QDocumentosExpedientes.documentosExpedientes);
		// Se guarda para mi por si quiero hacer consultas personalizadas.
		this.documentosExpedientesRepository = documentosExpedientesRepository;
	}

	@Override
	public void checkSiPuedoGrabar(DocumentosExpedientes dto) throws BaseException {
		log.debug("Verifico si puedo grabar y elevo excepción sino");

		final List<String> errores = new ArrayList<>();

		if(null == dto.getDocumento().getTipoDocumento() 
					|| StringUtils.isBlank(dto.getDescripcionDocumento())
					|| null == dto.getCategoria()) {
			errores.add("Debe rellenar los campos obligatorios");
		} else if(dto.getDescripcionDocumento().contains(".")){	//Descripción no es null aquí
			errores.add("No es posible incluir caracteres punto en la descripción de un documento.");
		}
		
		if(!errores.isEmpty()) {
			throw new ValidacionException("Formulario incompleto", errores);
		}
		
	}

	@Override
	public void checkSiPuedoEliminar(Long id) throws BaseException {
		log.debug("Verifico si puedo eliminar y elevo excepción sino");
		
	}
	
	@Override
	protected BooleanBuilder aniadirFiltrosCustom(List<FiltroDTO> filtros) {
		return new BooleanBuilder();
	}
	
	//*****************************************************************************
	
	public DocumentosExpedientes getDocumentoBasico(Long idDocExp) {
		return this.documentosExpedientesRepository.getDocumentoBasico(idDocExp);
	}

	public List<DocumentosExpedientes> findDocumentosByDocumentoId(Long idDoc){
		return this.documentosExpedientesRepository.findByDocumentoIdAndActivoTrue(idDoc);
	}
	
	public List<DocumentosExpedientes> findDocumentosActivosByExpdteIdTramExpActivos(Long idExp, Long idTramExp){
		return this.documentosExpedientesRepository.findDocumentosActivosByExpdteIdTramExpActivos(idExp, idTramExp);
	}
	
	public List<DocumentosExpedientes> findDocumentosActivosByExpdteIdTramExpIdDoc(Long idExp, Long idTramExp, Long idDoc)
	{
		return documentosExpedientesRepository.findDocumentosActivosByExpdteIdTramExpIdDoc(idExp, idTramExp, idDoc);
	}
	
	public List<DocumentosExpedientes> findDocumentosActivosByExpdteIdTramExp(Long idExp, Long idTramExp){
		return this.documentosExpedientesRepository.findDocumentosActivosByExpdteIdTramExp(idExp, idTramExp);
	}
	
	public byte[] getBytesDocumento(Long idDocumento) {
		return this.documentosService.getBytesDocumento(idDocumento);
	}
	
	//******************************************************************************
	
	@Transactional(value=TxType.REQUIRED, rollbackOn = Exception.class)
	public void incorporar(List<DocumentoDTO> listaDocumentosIncorporar, Expedientes exp, TramiteExpediente trExp) throws BaseException {
		validarIncorporacion(listaDocumentosIncorporar);
		
		final TramiteExpediente tramiteExp = tramiteExpedienteService.obtener(trExp.getId());
		final Expedientes expediente = tramiteExp.getExpediente();

		final List<DocumentosExpedientes> listaDocExp = nuevosDocumentosExp(listaDocumentosIncorporar, expediente, tramiteExp);
		this.guardarDocumentosIncorporados(listaDocExp, tramiteExp);
		
		this.tareasExpedienteService.crearTareaEDPRT(tramiteExp.getId());
}
	
	private void validarIncorporacion(List<DocumentoDTO> listaDocumentosIncorporar) throws ValidacionException {
		final List<String> errores = new ArrayList<>();
		
		for(DocumentoDTO dto : listaDocumentosIncorporar) {
			if(null == dto.getTipoDocumentoId() 
					|| StringUtils.isBlank(dto.getDescripcion())
					|| null == dto.getCategoriaId()) {
				errores.add("Debe rellenar los campos obligatorios");
			} else if(dto.getDescripcion().contains(".")){	//Descripción no es null aquí
				errores.add("No es posible incluir caracteres punto en la descripción de un documento.");
			}
			
			if(!errores.isEmpty()) {
				break;
			}
		}
		
		if(!errores.isEmpty()) {
			throw new ValidacionException("Formulario incompleto", errores);
		}
	}
	
	private List<DocumentosExpedientes> nuevosDocumentosExp(List<DocumentoDTO> listaDocumentosIncorporar, Expedientes exp, TramiteExpediente trExp) {
		final List<DocumentosExpedientes> listaDocExp = new ArrayList<>();
		
		for(DocumentoDTO dto : listaDocumentosIncorporar) {
			final Documentos doc = nuevoDocumento(dto);
			listaDocExp.add(nuevoDocumentoExp(dto, exp, doc, trExp));
		}
		
		return listaDocExp;
	}
	
	private void guardarDocumentosIncorporados(List<DocumentosExpedientes> listaDocExp, TramiteExpediente tramExp) throws BaseException {
		for(DocumentosExpedientes docExp : listaDocExp) {
			//Guardar documento
			final Documentos doc = docExp.getDocumento();
			doc.setOrigen(Documentos.ORIGEN_INCORPORADO);
			documentosService.guardar(doc);
			
			//Guardar documento-expediente
			this.guardarDocExp(docExp);
			
			//Guardar doc-exp-tramite
			DocumentosExpedientesTramites documentosExpedientesTramites = 
					documentosExpedientesTramitesService.nuevoDocumentosExpTram(docExp, tramExp, DocumentosExpedientesTramites.ORIGEN_INCORPORADO); //Origen Incorporación
			documentosExpedientesTramites = documentosExpedientesTramitesService.guardar(documentosExpedientesTramites);
			
			expedienteUltimaModificacion(documentosExpedientesTramites.getTramiteExpediente().getExpediente(),documentosExpedientesTramites.getFechaModificacion(),documentosExpedientesTramites.getFechaCreacion(),documentosExpedientesTramites.getUsuModificacion(),documentosExpedientesTramites.getUsuCreacion());
		}	
	}
	
	//Fin incorporar
	//***********************************************************************************
	//Generar documento
	
    @Value("${files.docservice.url.site}")
    private String docserviceSite;
    
    @Value("${server.servlet.context-path}")
    private String contextPath;
   
    @Value("${server.port}")
    private String serverPort;
    
    @Value("${server.address}")
    private String serverAddress;
    
 
	/** Devuelve la URL de OnlyOffice donde consultar el documento convertido
	 * 
	 * @param tmp Objeto DatosUsuarioTmp
	 * @param serverAddress address del host de la aplicación gestor-pdt
	 * @param serverPort puerto del host de la aplicación gestor-pdt
	 * @return
	 * @throws BaseException
	 */
	public String convertirOdtAPdf(DatosTmpUsuario tmp) throws BaseException {
		return convertirOdtAPdf(tmp, serverAddress, serverPort);
	}
	
	public String convertirOdtAPdf(DatosTmpUsuario tmp, String serverAddress, String serverPort) throws BaseException {

		try {
			String postEndpoint = docserviceSite + "ConvertService.ashx";
			String inputJson = componerJsonOdtAPdf(tmp,serverAddress,serverPort);
			
			var request = HttpRequest.newBuilder().uri(URI.create(postEndpoint))
					.header("Content-Type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(inputJson))
					.build();
	
			var client = HttpClient.newBuilder()
					.version(HttpClient.Version.HTTP_1_1)
					.build();

			var response = client.send(request, HttpResponse.BodyHandlers.ofString());
			if(response.statusCode() == 200) {
				return extraerURLRespuesta(response);
			}
			
			throw new BaseException("Se produjo un eror en la operación. Consulte con su administrador", Collections.emptyList());
		} catch (IOException | SAXException | ParserConfigurationException | InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new BaseException(e);
		}
	}
	
	private String componerJsonOdtAPdf(DatosTmpUsuario tmp, String serverAddress, String serverPort) throws NumberFormatException, MalformedURLException {
		final URL urlDescargaODT = new URL("http", serverAddress, Integer.parseInt(serverPort), 
				contextPath + "/ViewDocTmp?id=" + tmp.getId());
		
		return "{ \"key\":\"" + tmp.getNombre() + "\", \"filetype\":\"odt\", \"outputtype\":\"pdf\", "
				+ "\"url\":\"" + urlDescargaODT.toString() + "\"}";		
	}
	
	private String extraerURLRespuesta(HttpResponse<String> response) throws SAXException, IOException, ParserConfigurationException {
		final StringBuilder urlSb = new StringBuilder();
		
		DefaultHandler handler = new DefaultHandler() {
			StringBuilder sb = null;
			
	        @Override
	        public void startElement(String uri, String localName, String qName, Attributes attributes)
	                throws SAXException {
	            sb = new StringBuilder() ;
	        }
	        
	        @Override
	        public void characters(char[] ch, int start, int length) throws SAXException {
	        	sb.append(ch, start, length);
	        }
	        
	        @Override
	        public void endElement(String uri, String localName, String qName) throws SAXException {
	            if (qName.equals("FileUrl")) {
	                urlSb.append(sb.toString());
	            }
	        }

	    };

		final SAXParser parser = SAXParserFactory.newDefaultInstance().newSAXParser();
		parser.parse(new InputSource(new StringReader(response.body())), handler);

		return urlSb.toString();
	}
	
	
	/** serverName y servetPort indican donde está el servicio /ViewDocTmp desde donde OnlyOffice podrá obtener el ODT a convertir 
	 * @throws IOException */
	
	@Transactional(value=TxType.REQUIRED, rollbackOn = Exception.class)
	public void generarVistaPreviaOdt(DocumentoDTO documento, String serverName, int serverPort) throws IOException {
		String claveVP = documento.getClaveVistaPrevia();
		
		if(null == claveVP) {
			claveVP = "" + System.currentTimeMillis();
		}
		
		byte[] bytes = documento.getBytes();
		
		if(null == bytes) {
			bytes = this.getBytesDocumento(documento.getDocumentoId());
		}
		
		//Convertimos el documento a pdf:
		//Guardamos el fichero en el almacen temporal para hacerlo disponible al conversor de OnlyOffice
		DatosTmpUsuario tmp = datosTmpUsuarioService.guardarBytesOdtEnTmp(bytes, claveVP);
		
		//Una vez guardado está disponible para el servicio de conversión
	    final String url = this.convertirOdtAPdf(tmp, serverName, String.valueOf(serverPort));		

	    //Si no hay errores marco el documento con el código de VP.
		documento.setClaveVistaPrevia(claveVP); 
		documento.setUrlVistaPrevia(url);
	}

	@Transactional(value=TxType.REQUIRED, rollbackOn = Exception.class)
	public DocumentosExpedientesTramites generarDocumentoPdfDesdeOdt(Long idDocExpTram) throws BaseException {
		DocumentosExpedientesTramites docExpTram = documentosExpedientesTramitesService.obtener(idDocExpTram);
		DocumentosExpedientes docExp = docExpTram.getDocumentoExpediente();
		Documentos doc = docExp.getDocumento();
		
		DatosTmpUsuario datosTmp = datosTmpUsuarioService.guardarBytesOdtEnTmp(
																doc.getBytes(),
																"" + FechaUtils.ahora().toInstant().toEpochMilli());
		
		String url = this.convertirOdtAPdf(datosTmp);	
		byte[] bytesPDF;
		
		try {
			bytesPDF = FileUtils.leerBytesURLRemota(url);
		} catch (IOException e) {
			throw new BaseException("No se pudo leer el documento convertido.");
		}
		
		//Hacemos no editable el documento actual
		doc.setEditable(false);
		doc = documentosService.guardar(doc);
		
		//Creamos el documento pdf con los bytes del odt convertido
		Documentos docPdf = documentosService.nuevoDocumento();
		doc.copiarIndicadores(docPdf); //Aquí se ha copiado el  editable=false anterior
		docPdf.setBytes(bytesPDF);
		docPdf.setTipoDocumento(doc.getTipoDocumento());
		docPdf.setNombreFichero(doc.getNombreFichero());
		docPdf.setExtensionFichero("pdf");
		docPdf.setOrigen(Documentos.ORIGEN_GENERADO);
		docPdf.setDescripcion(doc.getDescripcion());
		docPdf.setDescripcionAbrev(doc.getDescripcionAbrev());
		docPdf = documentosService.guardar(docPdf);
		
		//Creamos el DocumentoExpediente
		DocumentosExpedientes docExpPdf = new DocumentosExpedientes();
		docExpPdf.setDescripcionDocumento(docExp.getDescripcionDocumento());
		docExpPdf.setDescripcionAbrevDocumento(docExp.getDescripcionAbrevDocumento());
		docExpPdf.setDocumento(docPdf);
		docExpPdf.setCategoria(docExp.getCategoria());
		docExpPdf.setExpediente(docExp.getExpediente());
		docExpPdf.setTramiteExpediente(docExp.getTramiteExpediente());
		docExpPdf.setActivo(true);
		docExpPdf = this.guardar(docExpPdf);
		
		//Creamos el Doc-Exp-tramite
		DocumentosExpedientesTramites docExpTramPdf = new DocumentosExpedientesTramites();
		docExpTramPdf.setDocumentoExpediente(docExpPdf);
		docExpTramPdf.setTramiteExpediente(docExpTram.getTramiteExpediente());
		docExpTramPdf.setOrigen(DocumentosExpedientesTramites.ORIGEN_GENERADO);
		docExpTramPdf = documentosExpedientesTramitesService.guardar(docExpTramPdf);

		//No hace falta crear el objeto AgrDoc. El DocExpTramite ya se ubica solo al guardar.
		
		return docExpTramPdf;
	}
	
	//--------------------------------------------------
	
	//Clase de soporte a los datos de la plantilla
	private class DatosPlantilla {
		@Getter
		private Map<String, Object> data = new HashMap<>();
		@Getter
		@Setter
		private byte[] bytesPlantilla;

		@SuppressWarnings("unused")
		public void forEach(BiConsumer<? super String, ? super Object> arg0) {
			this.data.forEach(arg0);
		}

		@SuppressWarnings("unused")
		public Object get(Object k) { return this.data.get(k); }
		public Object put(String k, Object v) { return this.data.put(k, dato(v)); } 
		
		private Object dato(Object o) { return o == null? StringUtils.EMPTY : o ; }
	}
		
	//------------------------------------
	
	@Transactional(value=TxType.REQUIRED, rollbackOn = Exception.class)
	public DocumentoDTO generarDocumentoPlantilla(Expedientes exp, TramiteExpediente tramExp,PlantillasDocumentos planDocDTO) throws BaseException {		
		PlantillasDocumentos plantillaDoc = plantillasDocumentosService.obtener(planDocDTO.getId());
		DocumentoDTO docDTO = nuevoDocumentoDTO(plantillaDoc);
				
		//VALIDAR SI EL DOCUMENTO A GENERAR YA EXISTE
		//...
		
		final DatosPlantilla data = nuevoDatosPlantilla(exp, tramExp,plantillaDoc.getPlantilla());
		data.put("titulo", "Titulillo");
				
		docDTO.setBytes(this.generarDocumento(data));
		docDTO.setEditable(Boolean.TRUE);
		docDTO.setDeTrabajo(Boolean.TRUE);
		
		return docDTO;
	}
	
	private DocumentoDTO nuevoDocumentoDTO(PlantillasDocumentos plantillasDoc) {
		CfgDocExpedienteTramitacion cfgDoc = plantillasDoc.getCfgDocExpedienteTramitacion();
		Plantillas plantilla = plantillasDoc.getPlantilla();
		DocumentoDTO docDTO = new DocumentoDTO();
		
		docDTO.setTipoDocumentoId(cfgDoc.getTipoDocumento().getId());
		docDTO.setPlantillaId(plantilla.getId());
		docDTO.setNombreFichero(plantilla.getFichero());
		docDTO.setDescripcion(cfgDoc.getDescripcionTipoDoc());
		docDTO.setDescripcionAbrev(cfgDoc.getDescripcionAbrevTipoDoc());

		return docDTO;
	}


	/** RECOPILA LOS DATOS TIPICOS DE UNA PLANTILLA SE USEN O NO. **/
	private DatosPlantilla nuevoDatosPlantilla(Expedientes exp,TramiteExpediente tramExp, Plantillas plantilla) {
		final DatosPlantilla data = new DatosPlantilla();
		
		data.setBytesPlantilla(plantilla.getBytes());
		
		/**DATOS COMUNES DEL EXPEDIENTE*/
		cargarDatosGeneralesExpediente(exp, data);
		
		/**DATOS PERSONAS Y SUJETOS OBLIGADOS*/
		cargarDatosPersonas(exp, data);
		
		/***DATOS SUJETO **/
		cargarSujetosObligados(exp, data);
		
		/** CARGAR DPD **/
		cargarDatosDPD(exp, data);
		
		/** TRAMITE REQUERIMIENTO INFORMACION ADICIONAL **/
		cargarDatosTramRequerimInfoAdicional(exp, data);
		
		/** TRAMITE REQUERIMIENTO SUBSANACION **/
		cargarDatosTramRequerimSubsanacion(exp, data);
		
		
		/** TRAMITE TRASLADO AL DPD **/
		cargarDatosTramTrasladoDPD(exp, data);	
		
		/** TRAMITE ACUERDO DE ADMISIÓN **/
		cargarDatosTramDecisionAdmision(exp, data);
		
		/** EXTRACTOS Y ANTECEDENTES DEL EXPEDIENTE **/
		cargarDatosExtractosAntec(exp, data);
	
		/** TRAMITE ACTUAL; DESDE EL QUE ESTAMOS GENERANDO EL DOCUMENTO **/
		cargarDatosTramActual(tramExp, data);
		
		return data;
	}
	
	/** RECOPILA Y CARGA LOS VALORES DE VARIABLES DE PLANTILLA CORRESPONDIENTES AL INTERESADO DEL EXPEDIENTE **/
	private void cargarDatosInteresadoTramite(TramiteExpediente tramExp, DatosPlantilla data) {
		Personas personaInteresado = tramExp.getDetalleExpdteTramMappedBy().getPersonasInteresado();
		long idPersonaInteresado;
		
		if(personaInteresado!=null)
		{
			idPersonaInteresado = personaInteresado.getId();
			
			Personas personaInt = personasService.obtener(idPersonaInteresado);
			
			if(personaInt != null)
			{
				data.put("interesado_tramite", personaInt.getNombreAp());
				data.put("interesado_tramite_direccion",personaInt.getDireccionCompleta());
			}
		}else
		{
			data.put("interesado_tramite", "");
			data.put("interesado_tramite_direccion","");
		}
	}
	
	/** RECOPILA Y CARGA LOS VALORES DE VARIABLES DE PLANTILLA CORRESPONDIENTES A LA RESOLUCION  **/
	private void cargarDatosTramResolucion(TramiteExpediente tramExp, DatosPlantilla data)
	{
		Date fechaResolucion = tramExp.getDetalleExpdteTramMappedBy().getFechaResolucion();
		String numResol = tramExp.getDetalleExpdteTramMappedBy().getNumResolucion();
		
		data.put("fecha_resolucion_tramite", fechaResolucion == null?  StringUtils.EMPTY : FechaUtils.dateToStringFecha(fechaResolucion));
		
		data.put("numero_resolucion_tramite_resolucion", numResol == null?  StringUtils.EMPTY : numResol);
	}
	
	/** RECOPILA Y CARGA LOS VALORES DE VARIABLES DE PLANTILLA CORRESPONDIENTES A LAS FECHAS DE ENVIO DEL TRAMITE DESDE EL CUAL SE GENERA EL DOCUMENTO  **/
	private void cargarFechasEnvioTramite(TramiteExpediente tramExp, DatosPlantilla data) {
		Date fechaEnvio = tramExp.getDetalleExpdteTramMappedBy().getFechaEnvio();
		
		data.put("fecha_envio_tramite", fechaEnvio == null?  StringUtils.EMPTY : FechaUtils.dateToStringFecha(fechaEnvio));
		data.put("fecha_envio_acuerdo_tramite", fechaEnvio == null?  StringUtils.EMPTY : FechaUtils.dateToStringFecha(fechaEnvio));
		data.put("fecha_envio_comunicacion_tramite", fechaEnvio == null?  StringUtils.EMPTY : FechaUtils.dateToStringFecha(fechaEnvio));
		data.put("fecha_envio_notificacion_tramite", fechaEnvio == null?  StringUtils.EMPTY : FechaUtils.dateToStringFecha(fechaEnvio));
		data.put("fecha_envio_resolucion_tramite", fechaEnvio == null?  StringUtils.EMPTY : FechaUtils.dateToStringFecha(fechaEnvio));
	}
	
	
	/** RECOPILA Y CARGA LOS VALORES DE VARIABLES DE PLANTILLA CORRESPONDIENTES AL TRAMITE DESDE EL CUAL SE GENERA EL DOCUMENTO  **/
	private void cargarDatosTramActual(TramiteExpediente tramExp, DatosPlantilla data)
	{
		Date fechaInicioTramite = tramExp.getFechaIni();
		Date fechaAcreditacionTram = tramExp.getDetalleExpdteTramMappedBy().getFechaAcreditacion();
		Date fechaEmision = tramExp.getDetalleExpdteTramMappedBy().getFechaEmision();
		Date fechaEntradaTram = tramExp.getDetalleExpdteTramMappedBy().getFechaEntrada();
		Date fechaFirma = tramExp.getDetalleExpdteTramMappedBy().getFechaFirma();
		
		Date fechaInforme = tramExp.getDetalleExpdteTramMappedBy().getFechaInforme();
		Date fechaNotificacion = tramExp.getDetalleExpdteTramMappedBy().getFechaNotificacion();
		Date fechaRespuesta = tramExp.getDetalleExpdteTramMappedBy().getFechaRespuesta();
		Date fechaSubsanacion = tramExp.getDetalleExpdteTramMappedBy().getFechaSubsanacion();
		Integer plazo = tramExp.getDetalleExpdteTramMappedBy().getPlazo();

		data.put("fecha_inicio_tramite", fechaInicioTramite == null? StringUtils.EMPTY : FechaUtils.dateToStringFecha(fechaInicioTramite));
		data.put("fecha_acreditacion_tramite", fechaAcreditacionTram == null? StringUtils.EMPTY : FechaUtils.dateToStringFecha(fechaAcreditacionTram));
		data.put("fecha_emision_tramite", fechaEmision == null? StringUtils.EMPTY : FechaUtils.dateToStringFecha(fechaEmision));
		data.put("fecha_entrada_tramite",fechaEntradaTram == null?  StringUtils.EMPTY : FechaUtils.dateToStringFecha(fechaEntradaTram));
		
		/** CARGA LAS FECHAS DE ENVIO DEL TRAMITE **/
		cargarFechasEnvioTramite(tramExp, data);
		
		data.put("fecha_firma_tramite", fechaFirma == null?  StringUtils.EMPTY : FechaUtils.dateToStringFecha(fechaFirma));
		data.put("fecha_firma_acuerdo_tramite", fechaFirma == null?  StringUtils.EMPTY : FechaUtils.dateToStringFecha(fechaFirma));
		
		data.put("fecha_acuerdo_tramite", fechaInforme == null?  StringUtils.EMPTY : FechaUtils.dateToStringFecha(fechaInforme));
		data.put("fecha_informe_tramite", fechaInforme == null?  StringUtils.EMPTY : FechaUtils.dateToStringFecha(fechaInforme));
		data.put("fecha_informe_api_tramite", fechaInforme == null?  StringUtils.EMPTY : FechaUtils.dateToStringFecha(fechaInforme));
		
		data.put("fecha_notificacion_tramite", fechaNotificacion == null?  StringUtils.EMPTY : FechaUtils.dateToStringFecha(fechaNotificacion));
		data.put("fecha_notificacion_acuerdo_tramite", fechaNotificacion == null?  StringUtils.EMPTY : FechaUtils.dateToStringFecha(fechaNotificacion));
		data.put("fecha_notificacion_resolucion_tramite", fechaNotificacion == null?  StringUtils.EMPTY : FechaUtils.dateToStringFecha(fechaNotificacion));
		
		/** CARGA LOS DATOS DE RESOLUCION **/
		cargarDatosTramResolucion(tramExp, data);
		
		data.put("fecha_respuesta_tramite", fechaRespuesta == null?  StringUtils.EMPTY : FechaUtils.dateToStringFecha(fechaRespuesta));
		
		data.put("fecha_subsanacion_tramite", fechaSubsanacion == null?  StringUtils.EMPTY : FechaUtils.dateToStringFecha(fechaSubsanacion));
		
		/**CARGA LOS DATOS DEL INTERESADO**/
		cargarDatosInteresadoTramite(tramExp, data);
		
		data.put("plazo_tramite", plazo == null?  StringUtils.EMPTY : plazo.toString());
	}

	/** RECOPILA Y CARGA LOS VALORES DE VARIABLES DE PLANTILLA CORRESPONDIENTES AL TRAMITE 'DECISIÓN DE ADMISIÓN'  **/
	private void cargarDatosTramDecisionAdmision(Expedientes exp, DatosPlantilla data)
	{
		TramiteExpediente tramExpDecisionAdmision = null;
		Date fechaAcuerdo = null;
		
		tramExpDecisionAdmision = tramiteExpedienteService.findUltimoTramActivoByExpTipoTramite(exp.getId(),TRAM_DECISION_ADMISION, exp.getValorTipoExpediente().getId());
		
		if(tramExpDecisionAdmision != null)
		{
			fechaAcuerdo = tramExpDecisionAdmision.getDetalleExpdteTramMappedBy().getFechaInforme();
		}
		
		data.put("fecha_acuerdo_tramite_acadmision", fechaAcuerdo == null? StringUtils.EMPTY : FechaUtils.dateToStringFecha(fechaAcuerdo));

	}
	

	/** RECOPILA Y CARGA LOS EXTRACTOS Y ANTECEDENTES DEL EXPEDIENTE  **/
	private void cargarDatosExtractosAntec(Expedientes exp, DatosPlantilla data)
	{
		
		final StringBuilder sbe = new StringBuilder();
		final StringBuilder sba = new StringBuilder();

		String extractos = null;
		String antecedentes = null;

		
		final List<DetalleExpdteTram> listaDetExt = detalleExpdteTramService.findDetalleTramExpByExpConExtractos(exp.getId());
		
		
		for(DetalleExpdteTram detExt : listaDetExt) {

			if (detExt.getTextoExtractoExpediente() != null) {
				
				sbe.append(detExt.getTextoExtractoExpediente()).append("\n\n");
				}		
		}
		
		final List<DetalleExpdteTram> listaDet = detalleExpdteTramService.findDetalleTramExpByExpConAntec(exp.getId());
				
		for(DetalleExpdteTram det : listaDet) {
			
			if (det.getTextoAntecedentesExpediente() != null) {
				
				sba.append(det.getTextoAntecedentesExpediente()).append("\n\n");
				}			
		}
		
		extractos = sbe.toString();
		antecedentes = sba.toString();
		
		data.put("extractos", extractos == null? StringUtils.EMPTY : extractos);
		data.put("antecedentes", extractos == null? StringUtils.EMPTY : antecedentes);

	}

	
	/** RECOPILA Y CARGA LOS VALORES DE VARIABLES DE PLANTILLA CORRESPONDIENTES AL TRAMITE 'TRASLADO AL DPD'  **/
	private void cargarDatosTramTrasladoDPD(Expedientes exp, DatosPlantilla data)
	{
		TramiteExpediente tramExpTrasladoDPD = null;
		Date fechaEnvioTrasladoDPD = null;
		Date fechaInicioTrasladoDPD = null;
		
		tramExpTrasladoDPD = tramiteExpedienteService.findUltimoTramActivoByExpTipoTramite(exp.getId(),TRAM_TRASLADO_DPD, exp.getValorTipoExpediente().getId());
		
		if(tramExpTrasladoDPD != null)
		{
			fechaEnvioTrasladoDPD = tramExpTrasladoDPD.getDetalleExpdteTramMappedBy().getFechaEnvio();
			fechaInicioTrasladoDPD = tramExpTrasladoDPD.getFechaIni();
		}
		
		data.put("fecha_envio_notificacion_trasladodpd1", fechaEnvioTrasladoDPD == null? StringUtils.EMPTY : FechaUtils.dateToStringFecha(fechaEnvioTrasladoDPD));
		data.put("fecha_inicio_trasladodpd2", fechaInicioTrasladoDPD == null? StringUtils.EMPTY : FechaUtils.dateToStringFecha(fechaInicioTrasladoDPD));
	}

	/** RECOPILA Y CARGA LOS VALORES DE VARIABLES DE PLANTILLA CORRESPONDIENTES AL TRAMITE 'REQUERIMIENTO DE SUBSANACIÓN'  **/
	private void cargarDatosTramRequerimSubsanacion(Expedientes exp, DatosPlantilla data)
	{
		TramiteExpediente tramExpReqSubsanacion = null;
		Date fechaSubsanacion = null;
		Date fechaRespuestaSubsanacion = null;
		
		tramExpReqSubsanacion = tramiteExpedienteService.findUltimoTramActivoByExpTipoTramite(exp.getId(),TRAM_REQUERIM_SUBSANACION, exp.getValorTipoExpediente().getId());
		
		if(tramExpReqSubsanacion != null)
		{
			fechaSubsanacion = tramExpReqSubsanacion.getDetalleExpdteTramMappedBy().getFechaNotificacion();
			fechaRespuestaSubsanacion = tramExpReqSubsanacion.getDetalleExpdteTramMappedBy().getFechaSubsanacion();
		}
		
		data.put("fecha_notificacion_tramite_reqsubsan", fechaSubsanacion == null? StringUtils.EMPTY : FechaUtils.dateToStringFecha(fechaSubsanacion));		
		data.put("fecha_subsanacion_tramite_reqsubsan", fechaRespuestaSubsanacion == null? StringUtils.EMPTY : FechaUtils.dateToStringFecha(fechaRespuestaSubsanacion));
	}
	
	
	/** RECOPILA Y CARGA LOS VALORES DE VARIABLES DE PLANTILLA CORRESPONDIENTES AL TRAMITE 'REQUERIMIENTO DE INFORMACIÓN ADICIONAL'  **/
	private void cargarDatosTramRequerimInfoAdicional(Expedientes exp, DatosPlantilla data)
	{
		TramiteExpediente tramExpRequerimInfoAdicional = null;
		Date fechaNotifRequInfAdicional = null;
		Date fechaEntradaNotifRequInfAdicional = null;
		
		tramExpRequerimInfoAdicional = tramiteExpedienteService.findUltimoTramActivoByExpTipoTramite(exp.getId(),TRAM_REQUERIM_INFO_ADICIONAL, exp.getValorTipoExpediente().getId());
		
		if(tramExpRequerimInfoAdicional != null)
		{
			fechaNotifRequInfAdicional = tramExpRequerimInfoAdicional.getDetalleExpdteTramMappedBy().getFechaNotificacion();
			fechaEntradaNotifRequInfAdicional = tramExpRequerimInfoAdicional.getFechaIni();
		}
		
		data.put("fecha_envio_notificacion_tramite_reqinforadic", fechaNotifRequInfAdicional == null? StringUtils.EMPTY : FechaUtils.dateToStringFecha(fechaNotifRequInfAdicional));
		data.put("fecha_inicio_tramite_reqinforadic", fechaEntradaNotifRequInfAdicional == null? StringUtils.EMPTY : FechaUtils.dateToStringFecha(fechaEntradaNotifRequInfAdicional));
	}
	
	/** RECOPILA Y CARGA LOS VALORES DE VARIABLES DE PLANTILLA CORRESPONDIENTES AL EXPEDIENTE  **/
	private void cargarDatosGeneralesExpediente(Expedientes exp, DatosPlantilla data)
	{
		data.put("numero_expediente", exp.getNumExpediente() == null? StringUtils.EMPTY : exp.getNumExpediente());
		data.put("fecha_entrada", exp.getFechaEntrada() == null? StringUtils.EMPTY : FechaUtils.dateToStringFecha(exp.getFechaEntrada()));
		data.put("fecha_registro", exp.getFechaRegistro() == null? StringUtils.EMPTY : FechaUtils.dateToStringFecha(exp.getFechaRegistro()));
		data.put("fecha_solicitud_ejercicio", exp.getFechaSolicitudEjercicio() == null? StringUtils.EMPTY : FechaUtils.dateToStringFecha(exp.getFechaSolicitudEjercicio()));		
		data.put("numero_registro", exp.getNumRegistro() == null? StringUtils.EMPTY : exp.getNumRegistro());
		data.put("motivo", exp.getMotivo() == null? StringUtils.EMPTY : exp.getMotivo());
		
		data.put("instructor", exp.getValorInstructorAPI() == null? StringUtils.EMPTY : exp.getValorInstructorAPI().getDescripcion());
		
		boolean vieneAEPD = false;
		if (exp.getValorDominioOrigen() != null) {
				vieneAEPD = exp.getValorDominioOrigen().getCodigo().equals("AEPD");
		}
		data.put("viene_AEPD", vieneAEPD);
		boolean faltaRespuesta = false;
		if (exp.getRespuestaSolicitudEjercicio() != null) {
			faltaRespuesta = exp.getRespuestaSolicitudEjercicio();
			}
		data.put("es_falta_respuesta", faltaRespuesta);
		

	}
	
	/** RECOPILA Y CARGA LOS VALORES DE VARIABLES DE PLANTILLA CORRESPONDIENTES A LAS PERSONAS IMPLICADAS EN EL EXPEDIENTE  **/
	private void cargarDatosPersonas(Expedientes exp, DatosPlantilla data)
	{
		/** PERSONA **/
		final PersonasExpedientes personaExp = 
				personasExpedientesService.obtenerPersonaExpedientePrincipalPorExpediente(exp.getId());
		
		data.put("interesado_principal", 
				personaExp == null? StringUtils.EMPTY
						: personaExp.getPersonas().getNombreAp());
		
		data.put("interesado_principal_direccion", 
				personaExp == null? StringUtils.EMPTY
						: personaExp.getPersonas().getDireccionCompleta());
		
		boolean obligadoElectronico = false;


		/** REPRESENTANTE **/
		if(personaExp != null) {
			obligadoElectronico = !personaExp.getPersonas().getValorTipoPersona().getCodigo().equals("FIS");
			Personas repre = personaExp.getPersonasRepre();
			boolean tieneRepre = repre != null;
			data.put("personaprincipal_expediente_tienerepresentante", tieneRepre);
			data.put("interesado_principal_representante", 
					tieneRepre? repre.getNombreAp() : StringUtils.EMPTY);
			
			data.put("interesado_principal_representante_direccion", 
					repre == null? StringUtils.EMPTY
							: repre.getDireccionCompleta());
			
			if (tieneRepre) {
				if (!obligadoElectronico) {
					obligadoElectronico = !repre.getValorTipoPersona().getCodigo().equals("FIS");
				}
				data.put("inter_o_repres", repre.getNombreAp());
				data.put("inter_o_repres_dir",repre.getDireccionCompleta());
			}
			else {

				data.put("inter_o_repres", personaExp.getPersonas().getNombreAp());
				data.put("inter_o_repres_dir",personaExp.getPersonas().getDireccionCompleta());

			}
		}
		
		data.put("obligado_electronico", obligadoElectronico);
		
		
	}
	
	/** RECOPILA Y CARGA LOS VALORES DE VARIABLES DE PLANTILLA CORRESPONDIENTES AL SUJETO OBLIGADO DEL EXPEDIENTE  **/
	private void cargarSujetosObligados(Expedientes exp, DatosPlantilla data)
	{
		final SujetosObligadosExpedientes sujetoExp = 
				sujetosObligadosExpedientesService.obtenerSujetosObligadosExpedientePrincipalPorExpediente(exp.getId());

		data.put("sujeto_principal", 
				sujetoExp == null? StringUtils.EMPTY
						: sujetoExp.getSujetosObligados().getDescripcion());
		
		if(sujetoExp != null)
		{
			SujetosObligados sujOblig = sujetoExp.getSujetosObligados();
			data.put("sujeto_principal_direccion", 
					sujOblig == null? StringUtils.EMPTY
							: sujOblig.getDireccionCompleta());
		}
	}
	
	/** RECOPILA Y CARGA LOS VALORES DE VARIABLES DE PLANTILLA CORRESPONDIENTES AL DPD ASOCIADO AL SUJETO PRINCIPAL DEL EXPEDIENTE **/
	private void cargarDatosDPD(Expedientes exp, DatosPlantilla data)
	{
		Personas  personaDpd = sujetosObligadosExpedientesService.obtenerDpdSujetoPpalExpediente(exp.getId());
		Provincias provinciaDpd = null;
		
		data.put("sujeto_principal_dpd", personaDpd == null? StringUtils.EMPTY
				: personaDpd.getNombreAp());
		data.put("sujeto_principal_dpd_direccion", personaDpd == null? StringUtils.EMPTY
				: personaDpd.getDireccionCompleta());
		if(personaDpd != null)
		{
			provinciaDpd = personaDpd.getProvincia();
		}

		data.put("sujeto_principal_dpd_provincia", provinciaDpd == null? StringUtils.EMPTY
				: provinciaDpd.getDescripcion());	
	}
		
	//JODReports
	private byte[] generarDocumento(DatosPlantilla datos) throws BaseException {
		final DocumentTemplateFactory documentTemplateFactory = new DocumentTemplateFactory();
		try {
			final DocumentTemplate template = documentTemplateFactory.getTemplate(
					new ByteArrayInputStream(datos.getBytesPlantilla()));

			final ByteArrayOutputStream bos = new ByteArrayOutputStream();
			template.createDocument(datos.getData(), bos);

			return bos.toByteArray();
		} catch (final IOException | DocumentTemplateException e) {
			throw new BaseException(e);
		}
	}
	
	@Transactional(value=TxType.REQUIRED, rollbackOn = Exception.class)
	public void guardarDocumentoGenerado(DocumentoDTO docDTO, Long idTram) throws BaseException {
		final Documentos doc = nuevoDocumento(docDTO);
		doc.setOrigen(Documentos.ORIGEN_GENERADO);
		doc.setActivo(true);
		
		TramiteExpediente tramExp = tramiteExpedienteService.obtener(idTram);
		Expedientes exp = tramExp.getExpediente();
		
		final DocumentosExpedientes docExp = nuevoDocumentoExp(docDTO, exp, doc, tramExp);		
		documentosService.guardar(doc);
		
		this.guardarDocExp(docExp);
		
		this.tareasExpedienteService.crearTareaEDPRT(tramExp.getId());

		//Guardar documento-expediente-tramite
		DocumentosExpedientesTramites documentosExpedientesTramites = 
				documentosExpedientesTramitesService.nuevoDocumentosExpTram(docExp, tramExp, DocumentosExpedientesTramites.ORIGEN_GENERADO); //Origen Generación
		documentosExpedientesTramites = documentosExpedientesTramitesService.guardar(documentosExpedientesTramites);

		expedienteUltimaModificacion(documentosExpedientesTramites.getTramiteExpediente().getExpediente(),documentosExpedientesTramites.getFechaModificacion(),documentosExpedientesTramites.getFechaCreacion(),documentosExpedientesTramites.getUsuModificacion(),documentosExpedientesTramites.getUsuCreacion());
	}
	
	//Fin generar
	//***********************************************************************************
	//Vincular

	@Transactional(value=TxType.REQUIRED, rollbackOn = Exception.class)
	public void vincular(DocumentosExpedientes documentoExp, TramiteExpediente trExp) throws BaseException {
		List<DocumentoDTO> listaDocs = List.of(cargarDocumentoDTO(documentoExp));
		this.vincular(listaDocs, trExp);
	}
	
	@Transactional(value=TxType.REQUIRED, rollbackOn = Exception.class)
	public void vincular(List<DocumentoDTO> listaDocumentosVincular, TramiteExpediente trExp) throws BaseException {
		validarVinculacion(listaDocumentosVincular);
		
		final TramiteExpediente tramiteExp = tramiteExpedienteService.obtener(trExp.getId());		
		final List<DocumentosExpedientes> listaDocExp = documentosExpAVincular(listaDocumentosVincular, tramiteExp);
		
		this.marcarVinculacion(listaDocumentosVincular, tramiteExp);
		this.guardarDocumentosVinculados(listaDocExp, tramiteExp);
		
		this.tareasExpedienteService.crearTareaEDPRT(tramiteExp.getId());
	}
		
	//------------	
	
	private void validarVinculacion(List<DocumentoDTO> listaDocumentosVincular) throws ValidacionException {
		if(listaDocumentosVincular == null || listaDocumentosVincular.isEmpty()) {
			throw new ValidacionException("No hay ningún documento en la selección. Debe iniciar de nuevo la operación desde el principio.");
		}
		
		validarIncorporacion(listaDocumentosVincular);
	}
	
	private List<DocumentosExpedientes> documentosExpAVincular(List<DocumentoDTO> listaDocumentosVincular, TramiteExpediente tramDestino) {
		final List<DocumentosExpedientes> listaDocExp = new ArrayList<>();
		final Expedientes expDestino = tramDestino.getExpediente();	
		
		for(DocumentoDTO dto : listaDocumentosVincular) {
			final DocumentosExpedientes docExp;
			final Long idExpOrigen = dto.getExpedienteId();
			
			//si el expediente es distinto al actual debemos crear objetos documento-expediente nuevos
			//si no, debemos recuperar los actuales
			if(!idExpOrigen.equals(expDestino.getId())) {
				final Documentos doc = documentosService.obtener(dto.getDocumentoId());
				docExp = nuevoDocumentoExp(dto, expDestino, doc, tramDestino);
			} else {
				docExp = this.obtener(dto.getId());
				this.aplicarDocumentoDTO(dto, docExp);
			}

			listaDocExp.add(docExp);
		}
		
		return listaDocExp;
	}
	
	private void guardarDocumentosVinculados(List<DocumentosExpedientes> listaDocExp, TramiteExpediente tramExp) throws BaseException {
		for(DocumentosExpedientes docExp : listaDocExp) {
			guardarDocumentoVinculado(docExp, tramExp);
		}	
	}
	
	private DocumentosExpedientesTramites guardarDocumentoVinculado(DocumentosExpedientes docExp, TramiteExpediente tramExp) throws BaseException {
		//Guardar documento. Puede que se haya cambiado alguno de sus checks.
		//NO se modifica su campo origen.
		documentosService.guardar(docExp.getDocumento());
		
		//Tanto si es nuevo como si ya existe guardo el DocExp. Puede haber cambiado la descripción al vincular, por ejemplo.
		this.guardarDocExp(docExp);
		
		//Guardar documento-expediente-tramite
		DocumentosExpedientesTramites documentosExpedientesTramites = 
				documentosExpedientesTramitesService.nuevoDocumentosExpTram(docExp, tramExp, DocumentosExpedientesTramites.ORIGEN_VINCULADO); //Origen Vinculación
		documentosExpedientesTramites = documentosExpedientesTramitesService.guardar(documentosExpedientesTramites);

		expedienteUltimaModificacion(documentosExpedientesTramites.getTramiteExpediente().getExpediente(),documentosExpedientesTramites.getFechaModificacion(),documentosExpedientesTramites.getFechaCreacion(),documentosExpedientesTramites.getUsuModificacion(),documentosExpedientesTramites.getUsuCreacion());	
	
		return documentosExpedientesTramites;
	}

	private void marcarVinculacion(List<DocumentoDTO> listaDocumentosVincular, TramiteExpediente tramExp) throws BaseException {		
		/*Si el documento seleccionado para vincular está en el mismo expediente 
		y está en un trámite que tiene el indicador “L_TRAT_VINCULADOS” marcado a verdadero (1),
		una vez confirmada la vinculación en el trámite nuevo, actualizar la tabla GE_AGRUP_DOCUMENTOS para el trámite origen 
		con el indicador nuevo L_VINCULADO = 1. El nuevo registro para el nuevo trámite debe quedar con el L_VINCULADO = 0.*/
		for(DocumentoDTO dto : listaDocumentosVincular) {
			//Buscamos en los trámites a los que esté asociado el documento (antes de finalizar la vinculación, que añadiría otro elemento a la lista)
			final List<DocumentosExpedientesTramites> listaDocExpTram = documentosExpedientesTramitesService.findDocExpTramByDocExpId(dto.getId());
			final boolean mismoExp = dto.getExpedienteId().equals(tramExp.getExpediente().getId());
			
			for(DocumentosExpedientesTramites docExpTram : listaDocExpTram) {
				final Boolean tratVinculados = docExpTram.getTramiteExpediente().getTipoTramite().getTratarVinculados();
				
				if(mismoExp && Boolean.TRUE.equals(tratVinculados)) {
					final AgrupacionesDocumentos agrDoc = agrupacionesDocumentosService.findAgrupacion(docExpTram.getId());
					
					if (!Boolean.TRUE.equals(agrDoc.getVinculado())) {					
						agrDoc.setVinculado(true);
						agrupacionesDocumentosService.guardar(agrDoc);
					}
				}
				
			}
		}
	}
	
	
	
	//-----------------------------
	/** true si el documento está vinculado al trámite superior del asociado al idDocExpTram */
	public boolean documentoVinculadoSup(Long idDocExpTram) {		
		DocumentosExpedientesTramites docExpTram = documentosExpedientesTramitesService.obtener(idDocExpTram);
		DocumentosExpedientes docExp = docExpTram.getDocumentoExpediente();
		
		TramiteExpediente tramExp = docExpTram.getTramiteExpediente();
		TramiteExpediente tramExpSup = tramExp.getTramiteExpedienteSup();

		return tramExpSup != null && documentosExpedientesTramitesService.existsByDocExpIdAndTramExpId(docExp.getId(), tramExpSup.getId());
	}
	
	@Transactional(value=TxType.REQUIRED, rollbackOn = Exception.class)
	public DocumentosExpedientesTramites vincularDocumentoEnTramiteSup(Long idDocExpTram) throws BaseException {		
		DocumentosExpedientesTramites docExpTram = documentosExpedientesTramitesService.obtener(idDocExpTram);
		DocumentosExpedientes docExp = docExpTram.getDocumentoExpediente();
		TramiteExpediente tramExp = docExpTram.getTramiteExpediente();
		TramiteExpediente tramExpSup = tramExp.getTramiteExpedienteSup();
		
		//Anticipamos la comprobación para avisar al usuario
		if(documentosExpedientesTramitesService
				.existsByDocExpIdAndTramExpId(idDocExpTram, tramExpSup.getId())) {
			String msg = "El documento ya está vinculado al trámite superior.";
			throw new BaseException(msg, List.of(msg));
		}

		return guardarDocumentoVinculado(docExp, tramExpSup);
	}
		
	
	//Fin vincular
	//***********************************************************************************

	//Copiamos los datos parciales a un documento "de verdad"
	private Documentos nuevoDocumento(DocumentoDTO dto) {
		final Documentos d = documentosService.nuevoDocumento();
		
		final String[] ss = FileUtils.split(dto.getNombreFichero());
		d.setNombreFichero(ss[0]);
		d.setExtensionFichero(ss[1]);
		
		d.setDescripcion(dto.getDescripcion());
		d.setDescripcionAbrev(dto.getDescripcionAbrev());
		
		aplicarDocumentoDTO(dto, d);
		
		d.setTipoDocumento(tipoDocumentoService.obtener(dto.getTipoDocumentoId()));
		
		if(null != dto.getPlantillaId()) {
			d.setPlantilla(plantillaService.obtener(dto.getPlantillaId()));
		}
		
		//agrupación: null
		
		d.setBytes(dto.getBytes());
		
		return d;
	}

	public DocumentoDTO cargarDocumentoDTO(DocumentosExpedientes docExp) {
		DocumentoDTO dto = new DocumentoDTO();
		Documentos doc = docExp.getDocumento();
		
		dto.setId(docExp.getId());
		dto.setDocumentoId(doc.getId());
		dto.setTipoDocumentoId(doc.getTipoDocumento().getId());
		dto.setNombreFichero(doc.getNombreExtFichero());
		dto.setOrigenIncorporado(doc.getOrigenIncorporado());
		
		dto.setDescripcion(docExp.getDescripcionDocumento());
		dto.setDescripcionAbrev(docExp.getDescripcionAbrevDocumento());
		
		dto.setFirmado(doc.getFirmado());
		dto.setSellado(doc.getSellado());
		dto.setEditable(doc.getEditable());
		dto.setAnonimizado(doc.getAnonimizado());
		dto.setAnonimizadoParcial(doc.getAnonimizadoParcial());
		
		if(null != docExp.getCategoria()) {
			dto.setCategoriaId(docExp.getCategoria().getId());
		}

		return dto;
	}
	
	
	private DocumentosExpedientes nuevoDocumentoExp(DocumentoDTO dto, Expedientes exp, Documentos doc, TramiteExpediente trExp)  {		
		final DocumentosExpedientes docExp = new DocumentosExpedientes();
		docExp.setDocumento(doc);
		docExp.setExpediente(exp);
		docExp.setTramiteExpediente(trExp);
		docExp.setActivo(true);

		this.aplicarDocumentoDTO(dto, docExp);

		return docExp;
	}
	
	//Aplica un DocumentoDTO a un DocumentosExpedientes
	private void aplicarDocumentoDTO(final DocumentoDTO dto, final DocumentosExpedientes docExp) {
		//Aplicamos las descripciones en el DocExp
		docExp.setDescripcionDocumento(dto.getDescripcion());
		docExp.setDescripcionAbrevDocumento(dto.getDescripcionAbrev());

		//quitar este if cuando el campo esté generalizado y no haya doc. antiguos de prueba (con valores null)
		//dejar solo el else
		if(null == dto.getCategoriaId()) {
			docExp.setCategoria(null);
		} else { 
			docExp.setCategoria(valoresDominioService.obtener(dto.getCategoriaId()));
		}
		
		aplicarDocumentoDTO(dto, docExp.getDocumento());
	}
		
	//Aplica un DocumentoDTO a un Documentos
	private void aplicarDocumentoDTO(final DocumentoDTO dto, final Documentos d) {
		// == para tratar los casos null
		d.setEditable(Boolean.TRUE == dto.getEditable());
		d.setFirmado(Boolean.TRUE == dto.getFirmado());
		d.setSellado(Boolean.TRUE == dto.getSellado());
		d.setAnonimizado(Boolean.TRUE == dto.getAnonimizado());
		d.setAnonimizadoParcial(Boolean.TRUE == dto.getAnonimizadoParcial());
	}
	
	//*******************************************************
	
	
	private DocumentosExpedientes guardarDocExp(DocumentosExpedientes docExp) throws BaseException {
		docExp = super.guardar(docExp);
		
		expedienteUltimaModificacion(docExp.getExpediente(), docExp);
		
		return docExp;
	}
	
	@Transactional(value=TxType.REQUIRED, rollbackOn = Exception.class)
	public DocumentosExpedientes guardar(DocumentoDTO dto) throws BaseException {
		final Long idDocExp = dto.getId();
		
		DocumentosExpedientes docExpObtenido = obtener(idDocExp);
		
		if(esCambioDeTipoDoc(docExpObtenido, dto)) {
			docExpObtenido = cambiarTipoDeDocumento(docExpObtenido, dto);
		}

		//Guardado normal
		if(docExpObtenido != null) {
			aplicarDocumentoDTO(dto, docExpObtenido);
			docExpObtenido = guardarDocExp(docExpObtenido);	
		}				
		
		guardarAgrupacionesDoc(dto);
		
		return docExpObtenido;
	}
	
	private boolean esCambioDeTipoDoc(DocumentosExpedientes docExpObtenido, DocumentoDTO dto) {
		Long idTipoDocActual = docExpObtenido.getDocumento().getTipoDocumento().getId();
		Long idTipoDocNuevo = dto.getTipoDocumentoId();
		return ! java.util.Objects.equals(idTipoDocNuevo, idTipoDocActual);
	}
		
	private DocumentosExpedientes cambiarTipoDeDocumento(DocumentosExpedientes docExpObtenido, DocumentoDTO dto) throws BaseException {
		DocumentosExpedientes docExpObtenidoTrasClonado = null;
		Documentos docActual = docExpObtenido.getDocumento();
		
		//Copiamos el documento
		Documentos docCopia = nuevoDocumentoCopia(docActual);
		//Aplicamos AQUÍ el tipo de documento
		docCopia.setTipoDocumento(tipoDocumentoService.obtener(dto.getTipoDocumentoId()));
		docCopia = documentosService.guardar(docCopia);
		
		//Copiamos los DocExp que referencian al Doc actual
		List<DocumentosExpedientes> docExpDeDoc = this.findDocumentosByDocumentoId(docActual.getId());
		for(DocumentosExpedientes docExpActual : docExpDeDoc) {
			
			//Clonar DocExp
			DocumentosExpedientes docExpCopia = nuevoDocumentoExpCopia(docExpActual);
			docExpCopia.setDocumento(docCopia);
			docExpCopia = this.guardarDocExp(docExpCopia);
			
			//Debo ver si el documento expediente de la lista es el docExp que se iba a guardar inicialmente.
			//Lo identifico para que sea la copia la que reciba la actualización prevista.
			if(docExpActual.getId().equals(docExpObtenido.getId())) {
				docExpObtenidoTrasClonado = docExpCopia;
			}
			
			//Copiamos los DocExpTram que referencian al DocExp actual
			List<DocumentosExpedientesTramites> docExpTramDeDocExp = documentosExpedientesTramitesService.findDocExpTramByDocExpId(docExpActual.getId());
			for(DocumentosExpedientesTramites docExpTramActual : docExpTramDeDocExp) {
				//Clonar DocExpTram
				DocumentosExpedientesTramites docExpTramCopia = documentosExpedientesTramitesService.nuevoDocumentoExpTramCopia(docExpTramActual);
				docExpTramCopia.setDocumentoExpediente(docExpCopia);
				documentosExpedientesTramitesService.guardar(docExpTramCopia);
			}
			
			//Damos de baja el DocExp actual
			docExpActual.setActivo(false);
			this.guardarDocExp(docExpActual);
		}
		
		//Damos de baja el Doc actual
		docActual.setActivo(false);
		documentosService.guardar(docActual);
				
		return docExpObtenidoTrasClonado;
	}
	
	private AgrupacionesDocumentos guardarAgrupacionesDoc(DocumentoDTO dto) throws BaseException {
		DocumentosExpedientesTramites det = documentosExpedientesTramitesService.findDocExpTram(dto);
		return agrupacionesDocumentosService.guardar(obtenerAgrupacionDoc(det));
	}

	//Obtener el AgrupacionExpediente existente, si no, crear uno nuevo
	private AgrupacionesDocumentos obtenerAgrupacionDoc(DocumentosExpedientesTramites det) {
		AgrupacionesDocumentos agrDoc = agrupacionesDocumentosService.findAgrupacion(det.getId());
		
		if(agrDoc == null) {
			agrDoc = agrupacionesDocumentosService.nuevaAgrupacion(det);
		}
		
		return agrDoc;
	}
	
	
	/**
	 * Este método no funciona porque EntidadBasica no tiene setId, solo getId
	 * Si funcionara se podría usar la implementación de los tres métodos siguientes
	@SuppressWarnings("unchecked")
	private <T extends Auditable> T clonarEntidad(T entidad, Class<T> clase) throws BaseException {
		T copia;
		try {
			copia = clase.getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new BaseException(e);
		}
		
		BeanUtils.copyProperties(entidad, copia);
		
		copia.setId(null);
		copia.resetAuditables();
		return copia;
	}
	
	private Documentos nuevoDocumentoCopia(Documentos doc) {
		return clonarEntidad(doc, Documentos.class);
	}
	
	private DocumentosExpedientes nuevoDocumentoExpCopia(DocumentosExpedientes docExp) {
		return clonarEntidad(docExp, DocumentosExpedientes.class);
	}
	
	private DocumentosExpedientesTramites nuevoDocumentoExpTramCopia(DocumentosExpedientesTramites docExpTram) {
		return clonarEntidad(docExpTram, DocumentosExpedientesTramites.class);
	}

	*/
	
	private Documentos nuevoDocumentoCopia(Documentos doc) {
		Documentos copia = new Documentos();
		
		BeanUtils.copyProperties(doc, copia);

		copia.setId(null);
		copia.resetAuditables();
		return copia;
	}
	
	private DocumentosExpedientes nuevoDocumentoExpCopia(DocumentosExpedientes docExp) {
		DocumentosExpedientes copia = new DocumentosExpedientes();
		
		BeanUtils.copyProperties(docExp, copia);
		
		copia.setId(null);
		copia.resetAuditables();
		return copia;
	}

	//*******************************************************
		
	//Restablece el campo orden de los elementos de una página de listado
	//La lista pagina ya trae los elementos reordenados, falta restablecer el valor del campo orden.
	@Transactional(value=TxType.REQUIRED, rollbackOn = Exception.class)
	public void reordenar(List<DocumentosTramiteMaestra> lista, long orden0) throws BaseException {
		
		if(!lista.isEmpty()) {
			//Objeto vacío, para no poner null y que Sonar se queje
			DocumentosExpedientes docE = new DocumentosExpedientes(); 
			
			for(int i=0; i < lista.size(); i++) {
				final Long orden = orden0 + i;
				DocumentosTramiteMaestra docM = lista.get(i);				
				docM.setOrden(orden);
				
				//si se quita el campo orden estas tres lineas sobran
				docE = this.obtener(docM.getId()); //borrar 
				docE = super.guardar(docE);//borrar	
				
				AgrupacionesDocumentos agrDoc = agrupacionesDocumentosService.findAgrupacion(docM.getIdDocExpTramite());
				agrDoc.setOrden(orden);
				agrupacionesDocumentosService.guardar(agrDoc);				
			}
			
			expedienteUltimaModificacion(docE.getTramiteExpediente().getExpediente(), docE.getFechaModificacion(), docE.getFechaCreacion(), docE.getUsuModificacion(), docE.getUsuCreacion());
		}

	}

	//**************************************
	
	//Ya que hemos centralizado toda la gestión de documentos aquí, añadimos también un atajo para este método.
	
	public void eliminarDocumentoTramite(Long idDocExpTram) throws BaseException {
		this.documentosExpedientesTramitesService.delete(idDocExpTram);
	}
	
	
	@Transactional(value=TxType.REQUIRED, rollbackOn = Exception.class)
	public void eliminarDocumentoTramiteYExpediente(Long idDocExpTram) throws BaseException {
		final DocumentosExpedientesTramites det = this.documentosExpedientesTramitesService.obtener(idDocExpTram);
		
		this.documentosExpedientesTramitesService.delete(idDocExpTram);
		this.delete(det.getDocumentoExpediente().getId());
	}	
	
	//***********************************************************
	
	/**
	 * Metodo para guardar la ultima fecha y el ultimo usuario de persistencia sobre cualquier dato del expediente
	 * @param expedientesAModificar
	 * @param fechaModificacion
	 * @param fechaCreacion
	 * @param usuModificacion
	 * @param usuCreacion
	 * @return
	 * @throws BaseException
	 */
	private Expedientes expedienteUltimaModificacion(Expedientes expedientesAModificar, Date fechaModificacion, Date fechaCreacion, String usuModificacion, String usuCreacion) throws BaseException {
		Expedientes expedienteAux = expedientesAModificar;
		expedienteAux.setFechaUltimaPersistencia(fechaModificacion!=null ? fechaModificacion : fechaCreacion);
		expedienteAux.setUsuUltimaPersistencia(usuModificacion!=null ? usuModificacion : usuCreacion);
		expedienteAux = expedientesService.guardar(expedienteAux);
		return expedienteAux;
	}
	
	private Expedientes expedienteUltimaModificacion(Expedientes expedientesAModificar, Auditable auditable) throws BaseException {
		Expedientes expedienteAux = expedientesAModificar;
		Date fechaModificacion = auditable.getFechaModificacion();
		Date fechaCreacion = auditable.getFechaCreacion();
		String usuModificacion = auditable.getUsuModificacion();
		String usuCreacion = auditable.getUsuCreacion();
		
		return expedienteUltimaModificacion(expedienteAux ,fechaModificacion, fechaCreacion, usuModificacion, usuCreacion);
	}
	
	public List<DocumentosExpedientes> findDocumentosByExpedienteId(long idExp){
		return documentosExpedientesRepository.findDocumentosByExpedienteId(idExp);
	}
	
	public List<DocumentosExpedientes> findDocumentosByDocumentoId(long idExp){
		return documentosExpedientesRepository.findDocumentosByDocumentoId(idExp);
	}
}
