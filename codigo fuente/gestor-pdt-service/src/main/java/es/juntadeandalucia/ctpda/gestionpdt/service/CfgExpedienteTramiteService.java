package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.CfgExpedienteTramite;
import es.juntadeandalucia.ctpda.gestionpdt.model.QCfgExpedienteTramite;
import es.juntadeandalucia.ctpda.gestionpdt.repository.CfgExpedienteTramiteRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.ValidacionException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CfgExpedienteTramiteService extends AbstractCRUDService<CfgExpedienteTramite>{
	
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String NOEXISTEREGISTROCONFIG = "no.existe.registro.config";
	
	private CfgExpedienteTramiteRepository cfgExpedienteTramiteRepository;
	
	/**
	 * Constructor que inyecta bean de calculos matematicos y el resto de beans que hacen falta
	 * */
	public CfgExpedienteTramiteService(@Autowired MathsQueryService mathsQueryService, @Autowired CfgExpedienteTramiteRepository cfgExpedienteTramiteRepository){
		super(mathsQueryService,
				cfgExpedienteTramiteRepository,
				QCfgExpedienteTramite.cfgExpedienteTramite);
		//también lo guardo para mi por si quiero hacer consultas personalizadas.
		this.cfgExpedienteTramiteRepository=cfgExpedienteTramiteRepository;
	}
	

	
	
	@Override
	public void checkSiPuedoGrabar(CfgExpedienteTramite dto) throws BaseException {
		log.debug("Verifico si puedo grabar y elevo excepción sino");
		
	}

	@Override
	public void checkSiPuedoEliminar(Long id) throws BaseException {
		log.debug("Verifico si puedo eliminar y elevo excepción sino");		
	}
	
	
	public List<CfgExpedienteTramite> findExpTramitesByTipExp(Long idTipExp) throws ValidacionException{
		List<CfgExpedienteTramite> res = cfgExpedienteTramiteRepository.findExpTramitesByTipExp(idTipExp);
		if(res.isEmpty()) {
			throw new ValidacionException(NOEXISTEREGISTROCONFIG); 
		}		
		return res;
	}
	
	public List<CfgExpedienteTramite> findExpTramitesByTipExpSitExp(Long idTipExp, Long idSitExp) throws ValidacionException{
		List<CfgExpedienteTramite> res = cfgExpedienteTramiteRepository.findExpTramitesByTipExpSitExp(idTipExp, idSitExp);
		if(res.isEmpty()) {
			throw new ValidacionException(NOEXISTEREGISTROCONFIG); 
		}		
		return res;
	}
	
	public List<CfgExpedienteTramite> findExpEventosByTipExpSitExp(Long idTipExp, Long idSitExp) throws ValidacionException{
		List<CfgExpedienteTramite> res = cfgExpedienteTramiteRepository.findExpEventosByTipExpSitExp(idTipExp, idSitExp);
		if(res.isEmpty()) {
			throw new ValidacionException(NOEXISTEREGISTROCONFIG); 
		}		
		return res;
	}
	
	public List<CfgExpedienteTramite> findExpTramitessinEventosByTipExpSitExp(Long idTipExp, Long idSitExp) throws ValidacionException{
		List<CfgExpedienteTramite> res = cfgExpedienteTramiteRepository.findExpTramitessinEventosByTipExpSitExp(idTipExp, idSitExp);
		if(res.isEmpty()) {
			throw new ValidacionException(NOEXISTEREGISTROCONFIG); 
		}		
		return res;
	}
	
	public boolean existeTramitessinEventosByTipExpSitExp(Long idTipExp, Long idSitExp) {
		List<CfgExpedienteTramite> res = cfgExpedienteTramiteRepository.findExpTramitessinEventosByTipExpSitExp(idTipExp, idSitExp);
		boolean hayTramites = true;
		if(res.isEmpty()) {
			hayTramites = false; 
		}		
		return hayTramites;
	}
	
	public boolean existeEventosByTipExpSitExp(Long idTipExp, Long idSitExp) {
		List<CfgExpedienteTramite> res = cfgExpedienteTramiteRepository.findExpEventosByTipExpSitExp(idTipExp, idSitExp);
		boolean hayEventos = true;
		if(res.isEmpty()) {
			hayEventos = false; 
		}		
		return hayEventos;
	}
	
	public Long findCfgExpTramiteByTipTramite(Long idExpTram) throws ValidacionException{
		Long res = cfgExpedienteTramiteRepository.findCfgExpTramiteByTipTramite(idExpTram); 
		
		if(res == null) {
			throw new ValidacionException(NOEXISTEREGISTROCONFIG); 
		}		
		return res;
	}
	
	public List<CfgExpedienteTramite> findExpTramitesByTipExpSitExpAut(Long idTipExp, Long idSitExp){
		return cfgExpedienteTramiteRepository.findExpTramitesByTipExpSitExpAut(idTipExp, idSitExp);
	}
	
	public CfgExpedienteTramite findCfgExpTramiteByTipExpYTipTram(String codTipoExpediente, String codTipoTramite) {
		return cfgExpedienteTramiteRepository.findCfgExpTramiteByTipExpYTipTram(codTipoExpediente, codTipoTramite);
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
