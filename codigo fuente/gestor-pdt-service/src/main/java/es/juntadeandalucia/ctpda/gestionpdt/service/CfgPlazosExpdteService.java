package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.CfgPlazosExpdte;
import es.juntadeandalucia.ctpda.gestionpdt.model.QCfgPlazosExpdte;
import es.juntadeandalucia.ctpda.gestionpdt.repository.CfgPlazosExpdteRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.ValidacionException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CfgPlazosExpdteService extends AbstractCRUDService<CfgPlazosExpdte>{
	
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	private CfgPlazosExpdteRepository cfgPlazosExpdteRepository;
	
	/**
	 * Constructor que inyecta bean de calculos matematicos y el resto de beans que hacen falta
	 * */
	public CfgPlazosExpdteService(@Autowired MathsQueryService mathsQueryService, @Autowired CfgPlazosExpdteRepository cfgPlazosExpdteRepository){
		super(mathsQueryService,
				cfgPlazosExpdteRepository,
				QCfgPlazosExpdte.cfgPlazosExpdte);
		//también lo guardo para mi por si quiero hacer consultas personalizadas.
		this.cfgPlazosExpdteRepository=cfgPlazosExpdteRepository;
	}
	
	public List<CfgPlazosExpdte> findCfgPlazosByTipoExpediente(Long idTipoExp) throws ValidacionException{
		List<CfgPlazosExpdte> res = cfgPlazosExpdteRepository.findCfgPlazosByTipoExpediente(idTipoExp);
		if(res.isEmpty()) {
			throw new ValidacionException("no.existe.registro.config");
		}
		return res;
	}
	
	
	public CfgPlazosExpdte findCfgPlazosByTipExpTipPlazo(long idTipoExp, long idTipoPlazo) throws ValidacionException {
		CfgPlazosExpdte res = cfgPlazosExpdteRepository.findCfgPlazosByTipExpTipPlazo(idTipoExp, idTipoPlazo);
		if(res == null) {
			throw new ValidacionException("no.existe.registro.config");
		}
		return res;
	}
	
	@Override
	public void checkSiPuedoGrabar(CfgPlazosExpdte dto) throws BaseException {
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
