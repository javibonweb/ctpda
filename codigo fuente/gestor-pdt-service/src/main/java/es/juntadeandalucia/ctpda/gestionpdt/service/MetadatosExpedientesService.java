package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.MetadatosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.QMetadatosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.repository.MetadatosExpedientesRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MetadatosExpedientesService extends AbstractCRUDService<MetadatosExpedientes> {

	/**
	 * Serial id
	 */
	private static final long serialVersionUID = 1L;
	
	MetadatosExpedientesRepository metadatosExpedientesRepository;
	
	protected MetadatosExpedientesService (@Autowired MathsQueryService mathsQueryService,
			@Autowired MetadatosExpedientesRepository metadatosExpedientesRepository) {
		super(mathsQueryService, metadatosExpedientesRepository, QMetadatosExpedientes.metadatosExpedientes);
		// Se guarda para mi por si quiero hacer consultas personalizadas.
		this.metadatosExpedientesRepository = metadatosExpedientesRepository;
	}

	@Override
	public void checkSiPuedoGrabar(MetadatosExpedientes dto) throws BaseException {
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
