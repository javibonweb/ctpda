package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.jpa.JPAExpressions;

import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentoDTO;
import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentosMaestra;
import es.juntadeandalucia.ctpda.gestionpdt.model.QDocumentosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.QDocumentosExpedientesTramites;
import es.juntadeandalucia.ctpda.gestionpdt.model.QDocumentosMaestra;
import es.juntadeandalucia.ctpda.gestionpdt.model.TramiteExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.repository.DocumentosMaestraRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.util.FechaUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DocumentosMaestraService extends AbstractCRUDService<DocumentosMaestra> {

	/**
	 * Serial id
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private DocumentosExpedientesTramitesService documentosExpedientesTramitesService;
	@Autowired
	private TramiteExpedienteService tramiteExpedienteService;

	DocumentosMaestraRepository documentosMaestraRepository;
	
	
	
	protected DocumentosMaestraService (@Autowired MathsQueryService mathsQueryService,
			@Autowired DocumentosMaestraRepository documentosMaestraRepository) {
		super(mathsQueryService, documentosMaestraRepository, QDocumentosMaestra.documentosMaestra);
		// Se guarda para mi por si quiero hacer consultas personalizadas.
		this.documentosMaestraRepository = documentosMaestraRepository;
	}

	@Override
	public void checkSiPuedoGrabar(DocumentosMaestra dto) throws BaseException {
		log.debug("Verifico si puedo grabar y elevo excepción sino");
		
	}

	@Override
	public void checkSiPuedoEliminar(Long id) throws BaseException {
		log.debug("Verifico si puedo eliminar y elevo excepción sino");
		
	}
	
	@Override
	protected BooleanBuilder aniadirFiltrosCustom(List<FiltroDTO> filtros) {
		final BooleanBuilder bb = new BooleanBuilder();
		
		filtros.stream().filter(fx-> fx.getCampo().equals("#fechaIniCreacion")).forEach(
				fx -> {
					if(fx.getValue() != null) {
						DateTimePath<Date> fcrea = QDocumentosMaestra.documentosMaestra.fechaCreacion;
						bb.and(fcrea.eq((Date)fx.getValue()).or(fcrea.after((Date)fx.getValue())));
					}
				}
		);
		
		filtros.stream().filter(fx-> fx.getCampo().equals("#fechaFinCreacion")).forEach(
				fx -> {
					if(fx.getValue() != null) {
						final DateTimePath<Date> fcrea = QDocumentosMaestra.documentosMaestra.fechaCreacion;
						final Date d = FechaUtils.diaDespues((Date)fx.getValue()); //la fecha del doc. lleva incluida la hora, avanzamos un día en el filtro
						bb.and(fcrea.before(d));
					}
				}
		);
		
		filtros.stream().filter(fx-> fx.getCampo().equals("#fechaIniModificacion")).forEach(
				fx -> {
					if(fx.getValue() != null) {
						final DateTimePath<Date> fmod = QDocumentosMaestra.documentosMaestra.fechaModificacion;
						bb.and(fmod.eq((Date)fx.getValue()).or(fmod.after((Date)fx.getValue())));
					}
				}
		);
		
		filtros.stream().filter(fx-> fx.getCampo().equals("#fechaFinModificacion")).forEach(
				fx -> {
					if(fx.getValue() != null) {
						final DateTimePath<Date> fmod = QDocumentosMaestra.documentosMaestra.fechaModificacion;
						final Date d = FechaUtils.diaDespues((Date)fx.getValue()); //la fecha del doc. lleva incluida la hora, avanzamos un día en el filtro
						bb.and(fmod.before(d));
					}
				}
		);
		
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
		
		filtros.stream().filter(fx-> fx.getCampo().equals("#documentosExpDelTramite")).forEach(
				fx -> {
					if(fx.getValue() != null) {
						bb.and(filtroDocumentosExpTramite((Long)fx.getValue()));
					}
				}
		);
		
		filtros.stream().filter(fx-> fx.getCampo().equals("#documentosExpNoEnTramite")).forEach(
				fx -> {
					if(fx.getValue() != null) {
						bb.and(filtroDocumentosExpNoEnTramite((Long)fx.getValue()));
					}
				}
		);
		
		return bb;
	}
	
	private BooleanExpression filtroDocumentosNoEnTramite(Long idTramExp) {
		return QDocumentosMaestra.documentosMaestra.idDocumento.notIn(subQueryDocumentosDeTramite(idTramExp));
	}
	private BooleanExpression filtroDocumentosExpNoEnTramite(Long idTramExp) {
		return QDocumentosMaestra.documentosMaestra.id.notIn(subQueryDocumentosExpDeTramite(idTramExp));
	}
	
	private BooleanExpression filtroDocumentosTramite(Long idTramExp) {
		return QDocumentosMaestra.documentosMaestra.idDocumento.in(subQueryDocumentosDeTramite(idTramExp));
	}
	
	private BooleanExpression filtroDocumentosExpTramite(Long idTramExp) {
		return QDocumentosMaestra.documentosMaestra.id.in(subQueryDocumentosExpDeTramite(idTramExp));
	}	
	
	//Devuelve una lista de ids de Documento (no DocumentoExpediente)
	private SubQueryExpression<Long> subQueryDocumentosDeTramite(Long idTramExp){
		final QDocumentosExpedientes qDocExp = QDocumentosExpedientes.documentosExpedientes;
		final QDocumentosExpedientesTramites qDocExpTram = QDocumentosExpedientesTramites.documentosExpedientesTramites;
		
		/*return JPAExpressions
					.select(qDocExp.documento.id).distinct()
					.from(qDocExp)
					.join(qDocExpTram)
						.on(qDocExp.id.eq(qDocExpTram.documentoExpediente.id))
		            .where(qDocExpTram.tramiteExpediente.id.eq(idTramExp));*/
		return JPAExpressions
				.select(qDocExpTram.documentoExpediente.documento.id).distinct()
				.from(qDocExpTram)
	            .where(qDocExpTram.tramiteExpediente.id.eq(idTramExp));		
	}
	
	//Devuelve una lista de ids de Documento (no DocumentoExpediente)
	private SubQueryExpression<Long> subQueryDocumentosExpDeTramite(Long idTramExp){
		final QDocumentosExpedientes qDocExp = QDocumentosExpedientes.documentosExpedientes;
		final QDocumentosExpedientesTramites qDocExpTram = QDocumentosExpedientesTramites.documentosExpedientesTramites;
		
		/*return JPAExpressions
				.select(qDocExp.id).distinct()
				.from(qDocExp)
				.join(qDocExpTram)
					.on(qDocExp.id.eq(qDocExpTram.documentoExpediente.id))
	            .where(qDocExpTram.tramiteExpediente.id.eq(idTramExp));*/
		return JPAExpressions
				.select(qDocExpTram.documentoExpediente.id).distinct()
				.from(qDocExpTram)
	            .where(qDocExpTram.tramiteExpediente.id.eq(idTramExp));
	}
	
	//----------------
	
	public boolean hayDocumentosVinculables(Long idTramExp) {
		var tramite = tramiteExpedienteService.obtener(idTramExp);
		return this.crudRepository.exists(
				QDocumentosMaestra.documentosMaestra.idExpediente.eq(tramite.getExpediente().getId())
				.and(filtroDocumentosNoEnTramite(idTramExp)));
	}
	
	public boolean hayDocumentosVinculables(Long idTramOrigen, Long idTramDestino) {		
		return this.crudRepository
				.exists(filtroDocumentosTramite(idTramOrigen)
				.and(filtroDocumentosNoEnTramite(idTramDestino)));
	}
		
	public boolean hayDocumentosVinculablesSoloTramiteSup(Long idTramExp) {
		TramiteExpediente trSup = tramiteExpedienteService.obtenerTramiteSup(idTramExp);
		return hayDocumentosVinculables(trSup.getId(), idTramExp);
	}
		
	//************************************************
	
	public List<DocumentoDTO> convertirADocumentoDTO(List<DocumentosMaestra> docsMaestra){
		return docsMaestra.stream().map(this::nuevoDocumentoDTO).collect(Collectors.toList());
	}
	
	private DocumentoDTO nuevoDocumentoDTO(DocumentosMaestra docMaestra) {
		final DocumentoDTO docDTO = new DocumentoDTO();
		
		docDTO.setId(docMaestra.getId());
		docDTO.setDocumentoId(docMaestra.getIdDocumento());
		docDTO.setTipoDocumentoId(docMaestra.getIdTipo());
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
