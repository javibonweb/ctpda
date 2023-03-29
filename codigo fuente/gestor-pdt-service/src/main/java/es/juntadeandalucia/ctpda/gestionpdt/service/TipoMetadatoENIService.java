package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.TipoMetadatoENI;
import es.juntadeandalucia.ctpda.gestionpdt.model.QTipoMetadatoENI;
import es.juntadeandalucia.ctpda.gestionpdt.repository.TipoMetadatoENIRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TipoMetadatoENIService extends AbstractCRUDService<TipoMetadatoENI> {

	/**
	 * Serial id
	 */
	private static final long serialVersionUID = 1L;
	
	TipoMetadatoENIRepository tipoMetadatoENIRepository;
	
	protected TipoMetadatoENIService (@Autowired MathsQueryService mathsQueryService,
			@Autowired TipoMetadatoENIRepository tipoMetadatoENIRepository) {
		super(mathsQueryService, tipoMetadatoENIRepository, QTipoMetadatoENI.tipoMetadatoENI);
		// Se guarda para mi por si quiero hacer consultas personalizadas.
		this.tipoMetadatoENIRepository = tipoMetadatoENIRepository;
	}

	@Override
	public void checkSiPuedoGrabar(TipoMetadatoENI dto) throws BaseException {
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
