package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.PlantillasDocumentos;
import es.juntadeandalucia.ctpda.gestionpdt.model.QPlantillasDocumentos;
import es.juntadeandalucia.ctpda.gestionpdt.repository.PlantillasDocumentosRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PlantillasDocumentosService extends AbstractCRUDService<PlantillasDocumentos> {

	/**
	 * Serial id
	 */
	private static final long serialVersionUID = 1L;
	
	PlantillasDocumentosRepository plantillasDocumentosRepository;
	
	protected PlantillasDocumentosService (@Autowired MathsQueryService mathsQueryService,
			@Autowired PlantillasDocumentosRepository plantillasDocumentosRepository) {
		super(mathsQueryService, plantillasDocumentosRepository, QPlantillasDocumentos.plantillasDocumentos);
		// Se guarda para mi por si quiero hacer consultas personalizadas.
		this.plantillasDocumentosRepository = plantillasDocumentosRepository;
	}

	@Override
	public void checkSiPuedoGrabar(PlantillasDocumentos dto) throws BaseException {
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
	
	//**********************************************************************
	
	public List<PlantillasDocumentos> findByCfgId(Long idTipoDocCfg) {
		return plantillasDocumentosRepository.findByCfgId(idTipoDocCfg);
	}
	
}
