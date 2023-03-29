package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.Documentos;
import es.juntadeandalucia.ctpda.gestionpdt.model.Parametro;
import es.juntadeandalucia.ctpda.gestionpdt.model.QDocumentos;
import es.juntadeandalucia.ctpda.gestionpdt.repository.DocumentosRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.util.FechaUtils;
import es.juntadeandalucia.ctpda.gestionpdt.util.FileUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DocumentosService extends AbstractCRUDService<Documentos> {

	/**
	 * Serial id
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String PARAM_EXTENSIONES_PERMITIDAS_DOCUMENTOS = "extensionesDoc";
	private static final String PARAM_EXTENSIONES_PERMITIDAS_ONLYOFFICE = "extensionesOnlyOffice";

	@Autowired
	private SeriesService seriesService;
	
	@Autowired
	private ParametroService parametroService;
	
	private DocumentosRepository documentosRepository;
	
	private List<String> extensionesOnlyOffice;
	
	protected DocumentosService (@Autowired MathsQueryService mathsQueryService,
			@Autowired DocumentosRepository documentosRepository,
			@Autowired ParametroService parametroService) {
		super(mathsQueryService, documentosRepository, QDocumentos.documentos);
		// Se guarda para mi por si quiero hacer consultas personalizadas.
		this.documentosRepository = documentosRepository;
		this.parametroService = parametroService;
		
		//Las extensiones de OnlyOffice se espera que sean acccedidas
		//con mucha frecuencia: Las cacheo.
		//No voy a hacer escrituras, pero por si acaso cargo la lista como thread-safe
		extensionesOnlyOffice = Collections.synchronizedList(this.listaDeExtensionesOnlyOffice());
	}
	
	@Override
	public void checkSiPuedoGrabar(Documentos dto) throws BaseException {
		log.debug("Verifico si puedo grabar y elevo excepción sino");
		
	}

	@Override
	public void checkSiPuedoEliminar(Long id) throws BaseException {
		log.debug("Verifico si puedo eliminar y elevo excepción sino");
		
	}
	
	public List<Documentos> obtenerListaDocById(Long idExp){
		return documentosRepository.obtenerListaDocById(idExp);
	}
	
	@Override
	protected BooleanBuilder aniadirFiltrosCustom(List<FiltroDTO> filtros) {
		return new BooleanBuilder();
	}
	
	@Override
	@Transactional(TxType.REQUIRED)
	public Documentos guardar(Documentos doc) throws BaseException {

		if(doc.getId() == null) { //alta?
			doc.setIdentificadorDoc(seriesService.nextNumeroSerie(doc.getTipoDocumento().getCodigo()));
		}
		
		return super.guardar(doc);
	}

	public Documentos getByIdentificador(String identificador) {
		return documentosRepository.getByIdentificadorDoc(identificador);	
	}
	
	public byte[] getBytesDocumento(Long idDocumento) {
		return this.documentosRepository.getBytesDocumento(idDocumento);
	}
	
	public Documentos nuevoDocumento() {
		Documentos d = new Documentos();
		
		d.setEditable(Boolean.FALSE);
		d.setFirmado(Boolean.FALSE);
		d.setSellado(Boolean.FALSE);
		d.setAnonimizado(Boolean.FALSE);
		d.setAnonimizadoParcial(Boolean.FALSE);
		
		d.setDocumentoVersionado(false);
		d.setVersionDocumento(0L);
		d.setUltimaVersion(true);

		d.setFechaCaptura(FechaUtils.ahora());
		d.setActivo(true);
		
		return d;
	}
	
	//-----------------------------------------------------------------
	//OBTENER DE PARÁMETROS
	private static final String[] EXTENSIONES_NO_EDITABLES = { "PDF" };
		
	public String[] arrayDeExtensionesPermitidas() {
		final Parametro p = parametroService.getByCampo(PARAM_EXTENSIONES_PERMITIDAS_DOCUMENTOS);		
		return FileUtils.arrayDeExtensiones(p.getValor());
	}
	
	private List<String> listaDeExtensionesOnlyOffice() {
		final Parametro p = parametroService.getByCampo(PARAM_EXTENSIONES_PERMITIDAS_ONLYOFFICE);		
		return FileUtils.listaDeExtensiones(p.getValor());
	}

	public boolean esDocumentoEditable(Documentos doc) {
		return esDocumentoEditable(doc.getEditable(), doc.getExtensionFichero());
	}
	
	public boolean esDocumentoEditable(boolean atributoEditable, String extensionFichero) {
		return atributoEditable
				&& !ArrayUtils.contains(EXTENSIONES_NO_EDITABLES, extensionFichero.toUpperCase())
				&& this.esExtensionEditor(extensionFichero);
	}
	
	public boolean esExtensionEditor(String ext) {
		return !StringUtils.isBlank(ext) 
				&& extensionesOnlyOffice.contains(ext.toLowerCase());
	}
	
	//------------------------------------------
	
}
