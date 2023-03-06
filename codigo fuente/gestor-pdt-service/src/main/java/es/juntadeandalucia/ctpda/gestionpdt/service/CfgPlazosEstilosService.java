package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.CfgPlazosEstilos;
import es.juntadeandalucia.ctpda.gestionpdt.model.QCfgPlazosEstilos;
import es.juntadeandalucia.ctpda.gestionpdt.repository.CfgPlazosEstilosRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CfgPlazosEstilosService extends AbstractCRUDService<CfgPlazosEstilos>{
	
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	private CfgPlazosEstilosRepository cfgPlazosEstilosRepository;
	
	/**
	 * Constructor que inyecta bean de calculos matematicos y el resto de beans que hacen falta
	 * */
	public CfgPlazosEstilosService(@Autowired MathsQueryService mathsQueryService, @Autowired CfgPlazosEstilosRepository cfgPlazosEstilosRepository){
		super(mathsQueryService,
				cfgPlazosEstilosRepository,
				QCfgPlazosEstilos.cfgPlazosEstilos);
		//también lo guardo para mi por si quiero hacer consultas personalizadas.
		this.cfgPlazosEstilosRepository=cfgPlazosEstilosRepository;
	}
	
	public List<CfgPlazosEstilos> findEstiloByTipoExpTipoPlazoDiasRestantes(Long idTipoExp, Long idTipoPlazo, Long diasRestantes){
		return cfgPlazosEstilosRepository.findEstiloByTipoExpTipoPlazoDiasRestantes(idTipoExp,idTipoPlazo,diasRestantes);
	}
	
	
	@Override
	public void checkSiPuedoGrabar(CfgPlazosEstilos dto) throws BaseException {
		log.debug("Verifico si puedo grabar y elevo excepción sino");
		
	}

	@Override
	public void checkSiPuedoEliminar(Long id) throws BaseException {
		log.debug("Verifico si puedo eliminar y elevo excepción sino");		
	}
	

/**
 * Filtros custom.
 * Se pueden añadir predicados querydsl a manini, o dejar a nulo.
 * Se añade el filtro 
 * */
	@Override
	protected BooleanBuilder aniadirFiltrosCustom(List<FiltroDTO> filtros) {
		return new BooleanBuilder();
	}

}
