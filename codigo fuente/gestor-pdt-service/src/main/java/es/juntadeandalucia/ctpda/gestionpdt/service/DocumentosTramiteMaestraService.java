package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;

import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentoDTO;
import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentosTramiteMaestra;
import es.juntadeandalucia.ctpda.gestionpdt.model.QDocumentosExpedientesTramites;
import es.juntadeandalucia.ctpda.gestionpdt.model.QDocumentosTramiteMaestra;
import es.juntadeandalucia.ctpda.gestionpdt.repository.DocumentosTramiteMaestraRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.util.ListUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DocumentosTramiteMaestraService extends AbstractCRUDService<DocumentosTramiteMaestra> {

	/**
	 * Serial id
	 */
	private static final long serialVersionUID = 1L;

	
	DocumentosTramiteMaestraRepository documentosTramiteMaestraRepository;
	
	
	
	protected DocumentosTramiteMaestraService (@Autowired MathsQueryService mathsQueryService,
			@Autowired DocumentosTramiteMaestraRepository documentosTramiteMaestraRepository) {
		super(mathsQueryService, documentosTramiteMaestraRepository, QDocumentosTramiteMaestra.documentosTramiteMaestra);
		// Se guarda para mi por si quiero hacer consultas personalizadas.
		this.documentosTramiteMaestraRepository = documentosTramiteMaestraRepository;
	}

	@Override
	public void checkSiPuedoGrabar(DocumentosTramiteMaestra dto) throws BaseException {
		log.debug("Verifico si puedo grabar y elevo excepción sino");
		
	}

	@Override
	public void checkSiPuedoEliminar(Long id) throws BaseException {
		log.debug("Verifico si puedo eliminar y elevo excepción sino");
		
	}
	
	@Override
	protected BooleanBuilder aniadirFiltrosCustom(List<FiltroDTO> filtros) {
		final BooleanBuilder bb = new BooleanBuilder();
		
		filtros.stream().filter(fx-> fx.getCampo().equals("#documentosDelTramite")).forEach(
				fx -> {
					if(fx.getValue() != null) {
						bb.and(filtroDocumentosTramite((Long)fx.getValue()));
					}
				}
		);
		
		filtros.stream().filter(fx-> fx.getCampo().equals("#documentosNoEnTramite")).forEach(
				fx -> {
					if(fx.getValue() != null) {
						bb.and(filtroDocumentosNoEnTramite((Long)fx.getValue()));
					}
				}
		);
				
		return bb;
	}
	
	public List<DocumentosTramiteMaestra> findDocumentosExpediente(Long idExp) {		
		return findDocumentosExpediente(idExp, null, true);
	}
	
	public List<DocumentosTramiteMaestra> findDocumentosExpediente(Long idExp, Long idTipo, Boolean verVinculados) {
		QDocumentosTramiteMaestra qDoc = QDocumentosTramiteMaestra.documentosTramiteMaestra;
		BooleanExpression be = qDoc.idExpediente.eq(idExp);
		
		if(null != idTipo) {
			be = be.and(qDoc.idTipo.eq(idTipo));
		}
		
		if(!Boolean.TRUE.equals(verVinculados)) {
			be = be.and(qDoc.vinculado.eq(false));
		}
		
		final Iterable<DocumentosTramiteMaestra> it = this.crudRepository.findAll(be);
		
		return ListUtils.toList(it);
	}
	
	public List<DocumentosTramiteMaestra> findDocumentosTramite(Long idTram) {
		return this.documentosTramiteMaestraRepository.findByIdTramiteExpedienteOrderByOrdenAgrupacionExp(idTram);
	}
	
	public List<DocumentosTramiteMaestra> findByTramiteYAgrupacionExp(Long idTram, Long idCat) {
		return this.documentosTramiteMaestraRepository.findByIdTramiteExpedienteAndIdAgrupacionExpedienteOrderByOrdenAgrupacionExp(idTram, idCat);
	}
	
	//----------------------------------
	
	private BooleanExpression filtroDocumentosNoEnTramite(Long idTramExp) {	
		return QDocumentosTramiteMaestra.documentosTramiteMaestra.idDocumento.notIn(subQueryDocumentosDeTramite(idTramExp));
	}
	
	private BooleanExpression filtroDocumentosTramite(Long idTramExp) {
		return QDocumentosTramiteMaestra.documentosTramiteMaestra.idDocumento.in(subQueryDocumentosDeTramite(idTramExp));
	}		
		
	//Devuelve una lista de ids de Documento (no DocumentoExpediente)
	private SubQueryExpression<Long> subQueryDocumentosDeTramite(Long idTramExp){
		final QDocumentosExpedientesTramites qDocExpTram = QDocumentosExpedientesTramites.documentosExpedientesTramites;
		
		return JPAExpressions
				.select(qDocExpTram.documentoExpediente.documento.id).distinct()
				.from(qDocExpTram)
	            .where(qDocExpTram.tramiteExpediente.id.eq(idTramExp));		
	}

	public boolean hayDocumentosVinculables(Long idTramOrigen, Long idTramDestino) {		
		return this.crudRepository
				.exists(QDocumentosTramiteMaestra.documentosTramiteMaestra.idTramiteExpediente.eq(idTramOrigen)
				.and(filtroDocumentosTramite(idTramOrigen))
				.and(filtroDocumentosNoEnTramite(idTramDestino)));
	}

	//************************************************
	
	public List<DocumentoDTO> convertirADocumentoDTO(List<DocumentosTramiteMaestra> docsTramiteMaestra){
		return docsTramiteMaestra.stream().map(this::nuevoDocumentoDTO).collect(Collectors.toList());
	}
	
	private DocumentoDTO nuevoDocumentoDTO(DocumentosTramiteMaestra docMaestra) {
		final DocumentoDTO docDTO = new DocumentoDTO();
		
		docDTO.setId(docMaestra.getId());
		docDTO.setDocumentoId(docMaestra.getIdDocumento());
		docDTO.setTipoDocumentoId(docMaestra.getIdTipo());
		docDTO.setTramiteExpedienteId(docMaestra.getIdTramiteExpediente());
		docDTO.setExpedienteId(docMaestra.getIdExpediente());
		docDTO.setNombreFichero(docMaestra.getNombreFichero());
		docDTO.setDescripcion(docMaestra.getDescripcion());
		docDTO.setDescripcionAbrev(docMaestra.getDescripcionAbrev());
		docDTO.setCategoriaId(docMaestra.getIdCategoria());
		
		docDTO.setFirmado(docMaestra.getFirmado());
		docDTO.setSellado(docMaestra.getSellado());
		docDTO.setAnonimizado(docMaestra.getAnonimizado());
		docDTO.setAnonimizadoParcial(docMaestra.getAnonimizadoParcial());
		docDTO.setEditable(docMaestra.getEditable());

		return docDTO;
	}
	

}
