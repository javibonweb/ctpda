package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentoResolucion;
import es.juntadeandalucia.ctpda.gestionpdt.model.QDocumentoResolucion;
import es.juntadeandalucia.ctpda.gestionpdt.model.QDocumentos;
import es.juntadeandalucia.ctpda.gestionpdt.model.QDocumentosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.QTipoDocumento;
import es.juntadeandalucia.ctpda.gestionpdt.repository.DocumentoResolucionRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DocumentoResolucionService extends AbstractCRUDService<DocumentoResolucion> {

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	private DocumentoResolucionRepository documentoResolucionRepository;

	protected DocumentoResolucionService(@Autowired MathsQueryService mathsQueryService,
			@Autowired DocumentoResolucionRepository documentoResolucionRepository) {
		super(mathsQueryService, documentoResolucionRepository, QDocumentoResolucion.documentoResolucion);
		// también lo guardo para mi por si quiero hacer consultas personalizadas.
		this.documentoResolucionRepository = documentoResolucionRepository;

		this.joinBuilder = query -> {
			final QDocumentoResolucion qdocumentoresol = QDocumentoResolucion.documentoResolucion;
			
			final QDocumentosExpedientes qDocExp = new QDocumentosExpedientes("qDocExp");
			final QDocumentos qDoc = new QDocumentos("qDoc");
			final QTipoDocumento qTipoDoc = new QTipoDocumento("qTipoDoc");
			
			query.innerJoin(qdocumentoresol.documentoExpediente, qDocExp);
			query.innerJoin(qDocExp.documento, qDoc);
			query.innerJoin(qDoc.tipoDocumento, qTipoDoc);
			
			return query;
		};

	}
	
	public List<DocumentoResolucion> findDocumentosResolucionByIdResol(Long idResol)
	{
		return documentoResolucionRepository.findDocumentosResolucionByIdResol(idResol);
	}
	
	public List<DocumentoResolucion> findDocumentosResolucionByIdResolIdDoc(Long idResol, Long idDoc)
	{
		return documentoResolucionRepository.findDocumentosResolucionByIdResolIdDoc(idResol, idDoc);
	}
	

	@Override
	public void checkSiPuedoGrabar(DocumentoResolucion dto) throws BaseException {
		log.debug("Verifico si puedo grabar y elevo excepción sino");

	}

	@Override
	public void checkSiPuedoEliminar(Long id) throws BaseException {
		log.debug("Verifico si puedo eliminar y elevo excepción sino");

	}
	
	
	@Override
	protected BooleanBuilder aniadirFiltrosCustom(List<FiltroDTO> filtros) {
		return new BooleanBuilder();
	}

	//****************************
	
	public List<DocumentoResolucion> findByResolucionId(Long idResol){
		return this.documentoResolucionRepository.findByResolucionId(idResol);
	}

}
