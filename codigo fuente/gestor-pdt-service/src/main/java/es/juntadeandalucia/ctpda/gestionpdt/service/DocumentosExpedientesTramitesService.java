package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.AgrupacionesDocumentos;
import es.juntadeandalucia.ctpda.gestionpdt.model.AgrupacionesExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentoDTO;
import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentosExpedientesTramites;
import es.juntadeandalucia.ctpda.gestionpdt.model.QDocumentosExpedientesTramites;
import es.juntadeandalucia.ctpda.gestionpdt.model.TramiteExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.repository.DocumentosExpedientesTramitesRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.ValidacionException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DocumentosExpedientesTramitesService extends AbstractCRUDService<DocumentosExpedientesTramites> {

	/**
	 * Serial id
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private AgrupacionesExpedientesService agrupacionesExpedientesService;
	@Autowired
	private AgrupacionesDocumentosService agrupacionesDocumentosService;
	@Autowired
	private TareasExpedienteService tareasExpedienteService;
	
	
	DocumentosExpedientesTramitesRepository documentosExpedientesTramitesRepository;
	
	
	protected DocumentosExpedientesTramitesService (@Autowired MathsQueryService mathsQueryService,
			@Autowired DocumentosExpedientesTramitesRepository documentosExpedientesTramitesRepository) {
		super(mathsQueryService, documentosExpedientesTramitesRepository, QDocumentosExpedientesTramites.documentosExpedientesTramites);
		// Se guarda para mi por si quiero hacer consultas personalizadas.
		this.documentosExpedientesTramitesRepository = documentosExpedientesTramitesRepository;
	}

	@Override
	public void checkSiPuedoGrabar(DocumentosExpedientesTramites dto) throws BaseException {
		log.debug("Verifico si puedo grabar y elevo excepción sino");
		
	}
	
	@Override
	public void checkSiPuedoEliminar(Long id) throws BaseException {
		log.debug("Verifico si puedo eliminar y elevo excepción sino");
		
		DocumentosExpedientesTramites docTr = this.obtener(id);
		Long idDoc = docTr.getDocumentoExpediente().getDocumento().getId();
		boolean existeTarea = tareasExpedienteService.existeTareaREVDocumento(idDoc);
		
		if(existeTarea) {
			throw new ValidacionException("El documento tiene tareas asociadas en BD. No se puede eliminar");
		}
	}
	
	public boolean existsByDocExpIdAndTramExpId(Long idDocExp, Long idTramExp) {
		return documentosExpedientesTramitesRepository.existsByDocumentoExpedienteIdAndTramiteExpedienteId(idDocExp, idTramExp);		
	}
	
	public boolean existsByTramExpId(Long idTramExp) {
		return documentosExpedientesTramitesRepository.existsByTramiteExpedienteId(idTramExp);		
	}
	
	public List<DocumentosExpedientesTramites> findDocExpTramByIdTramExp(@Param("idTramExp")Long idTramExp)
	{
		return documentosExpedientesTramitesRepository.findDocExpTramByIdTramExp(idTramExp);
	}
	
	public List<DocumentosExpedientesTramites> findDocExpTramByDocExpId(Long idDocExp) {
		return documentosExpedientesTramitesRepository.findByDocumentoExpedienteId(idDocExp);	
	}
	
	public List<TramiteExpediente> findTramitesExpByDocExpId(Long idDocExp) {
		return documentosExpedientesTramitesRepository.findTramitesExpByDocumentoExpedienteId(idDocExp);	
	}
	
	public List<TramiteExpediente> findSubTramitesExpByDocExpIdAndTramExpId(Long idDocExp, Long idTramExp) {
		return documentosExpedientesTramitesRepository.findSubTramitesExpByDocumentoExpedienteIdAndTramiteExpedienteId(idDocExp, idTramExp);	
	}
	
	public int countDocExpTramByIdTramExp(Long idTramExp){
		return documentosExpedientesTramitesRepository.countDocExpTramByIdTramExp(idTramExp);
	}
	
	public DocumentosExpedientesTramites findDocExpTramByIdTramExpAndDocExp(Long idTramExp, Long idDocExp) {
		return documentosExpedientesTramitesRepository.findDocExpTramByIdTramExpAndDocExp(idTramExp,idDocExp);	
	}

	public DocumentosExpedientesTramites findDocExpTram(DocumentoDTO dto) {
		return this.findDocExpTramByIdTramExpAndDocExp(dto.getTramiteExpedienteId(), dto.getId());
	}
	
	@Override
	protected BooleanBuilder aniadirFiltrosCustom(List<FiltroDTO> filtros) {
		return new BooleanBuilder();		
	}
		
	//*************************************************************
	
	@Override
	@Transactional(value=TxType.REQUIRED, rollbackOn = Exception.class)
	public DocumentosExpedientesTramites guardar(DocumentosExpedientesTramites det) throws BaseException {
		boolean esAlta = det.getId() == null;

		det = this.guardarBasico(det);
				
		//Vinculamos el doc-exp-tramite superior si procede
		if(esAlta) {
			vincularDocumentoEnTramiteSup(det);
		}

		return det;
	}
	
	private DocumentosExpedientesTramites guardarBasico(DocumentosExpedientesTramites det) throws BaseException {
		det = super.guardar(det);
		guardarAgrupacionDocumento(det);

		return det;
	}
		
	private void vincularDocumentoEnTramiteSup(DocumentosExpedientesTramites docExpTram) throws BaseException {		
		final DocumentosExpedientes docExp = docExpTram.getDocumentoExpediente();
		final TramiteExpediente tramExp = docExpTram.getTramiteExpediente();
		
		if(!tramExp.getEsSubtramite()) {
			return;
		}
		
		final TramiteExpediente tramExpSup = tramExp.getTramiteExpedienteSup();

		//Si el documento no existe en el trámite superior vinculamos
		if(!this.existsByDocExpIdAndTramExpId(docExp.getId(), tramExpSup.getId())) {
			this.guardarBasico(nuevoDocumentosExpTram(docExp, tramExpSup, DocumentosExpedientesTramites.ORIGEN_VINCULADO)); //Origen Vinculación
		}
	}
	
	public DocumentosExpedientesTramites nuevoDocumentosExpTram(DocumentosExpedientes docExp, TramiteExpediente tramExp, String ori ) {
		final DocumentosExpedientesTramites docExpTram = new DocumentosExpedientesTramites();
		docExpTram.setDocumentoExpediente(docExp);
		docExpTram.setTramiteExpediente(tramExp);
		docExpTram.setOrigen(ori);
		
		return docExpTram;
	}
	
	public DocumentosExpedientesTramites nuevoDocumentoExpTramCopia(DocumentosExpedientesTramites docExpTram) {
		DocumentosExpedientesTramites copia = new DocumentosExpedientesTramites();
		
		BeanUtils.copyProperties(docExpTram, copia);
		
		copia.setId(null);
		copia.resetAuditables();
		return copia;
	}
	
	private AgrupacionesDocumentos guardarAgrupacionDocumento(DocumentosExpedientesTramites det) throws BaseException {
		return agrupacionesDocumentosService.guardar(obtenerAgrupacionDocumento(det));
	}
 
	private AgrupacionesDocumentos obtenerAgrupacionDocumento(DocumentosExpedientesTramites det) {
		AgrupacionesDocumentos agrDoc = null;
		
		AgrupacionesExpedientes agrExp = agrupacionesExpedientesService.findAgrupacion(det);
		
		if(agrExp != null) {
			agrDoc = agrupacionesDocumentosService.findAgrupacion(det.getId());
		}
		
		if(agrDoc == null) {
			agrDoc = agrupacionesDocumentosService.nuevaAgrupacion(det);
		}
		
		return agrDoc;
	}
	
	@Override
	@Transactional(value=TxType.MANDATORY, rollbackOn = Exception.class)
	public void delete(Long id) throws BaseException {
		AgrupacionesDocumentos agrDoc = this.agrupacionesDocumentosService.findAgrupacion(id);
		this.agrupacionesDocumentosService.delete(agrDoc.getId());
		
		super.delete(id);
	}

}
