package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.CfgExpedienteSubtramite;
import es.juntadeandalucia.ctpda.gestionpdt.model.QCfgExpedienteSubtramite;
import es.juntadeandalucia.ctpda.gestionpdt.repository.CfgExpedienteSubtramiteRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.ValidacionException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CfgExpedienteSubtramiteService extends AbstractCRUDService<CfgExpedienteSubtramite>{
	
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	private CfgExpedienteSubtramiteRepository cfgExpedienteSubtramiteRepository;
	
	/**
	 * Constructor que inyecta bean de calculos matematicos y el resto de beans que hacen falta
	 * */
	public CfgExpedienteSubtramiteService(@Autowired MathsQueryService mathsQueryService, @Autowired CfgExpedienteSubtramiteRepository cfgExpedienteSubtramiteRepository){
		super(mathsQueryService,
				cfgExpedienteSubtramiteRepository,
				QCfgExpedienteSubtramite.cfgExpedienteSubtramite);
		//también lo guardo para mi por si quiero hacer consultas personalizadas.
		this.cfgExpedienteSubtramiteRepository=cfgExpedienteSubtramiteRepository;
	}
	

	
	
	@Override
	public void checkSiPuedoGrabar(CfgExpedienteSubtramite dto) throws BaseException {
		log.debug("Verifico si puedo grabar y elevo excepción sino");
		
	}

	@Override
	public void checkSiPuedoEliminar(Long id) throws BaseException {
		log.debug("Verifico si puedo eliminar y elevo excepción sino");		
	}
	

	public List<CfgExpedienteSubtramite> findSubTramites(Long idTipTram, Long idTipExp) throws ValidacionException{
		List<CfgExpedienteSubtramite> res = cfgExpedienteSubtramiteRepository.findSubTramites(idTipTram, idTipExp);
		if(res.isEmpty()) {
			throw new ValidacionException("no.existe.registro.config"); 
		}		
		return res;
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
