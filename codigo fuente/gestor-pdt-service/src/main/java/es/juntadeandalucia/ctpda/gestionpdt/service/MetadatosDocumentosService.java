package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.MetadatosDocumentos;
import es.juntadeandalucia.ctpda.gestionpdt.model.QMetadatosDocumentos;
import es.juntadeandalucia.ctpda.gestionpdt.repository.MetadatosDocumentosRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MetadatosDocumentosService extends AbstractCRUDService<MetadatosDocumentos> {

	/**
	 * Serial id
	 */
	private static final long serialVersionUID = 1L;
	
	MetadatosDocumentosRepository metadatosDocumentosRepository;
	
	protected MetadatosDocumentosService (@Autowired MathsQueryService mathsQueryService,
			@Autowired MetadatosDocumentosRepository metadatosDocumentosRepository) {
		super(mathsQueryService, metadatosDocumentosRepository, QMetadatosDocumentos.metadatosDocumentos);
		// Se guarda para mi por si quiero hacer consultas personalizadas.
		this.metadatosDocumentosRepository = metadatosDocumentosRepository;
	}

	@Override
	public void checkSiPuedoGrabar(MetadatosDocumentos dto) throws BaseException {
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
}
