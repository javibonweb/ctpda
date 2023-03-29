package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.CfgPlazosAut;
import es.juntadeandalucia.ctpda.gestionpdt.model.QCfgPlazosAut;
import es.juntadeandalucia.ctpda.gestionpdt.model.enums.OrigenAut;
import es.juntadeandalucia.ctpda.gestionpdt.repository.CfgPlazosAutRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CfgPlazosAutService extends AbstractCRUDService<CfgPlazosAut>{
	
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	private CfgPlazosAutRepository cfgPlazosAutRepository;
	
	/**
	 * Constructor que inyecta bean de calculos matematicos y el resto de beans que hacen falta
	 * */
	public CfgPlazosAutService(@Autowired MathsQueryService mathsQueryService, @Autowired CfgPlazosAutRepository cfgPlazosAutRepository){
		super(mathsQueryService,
				cfgPlazosAutRepository,
				QCfgPlazosAut.cfgPlazosAut);
		//también lo guardo para mi por si quiero hacer consultas personalizadas.
		this.cfgPlazosAutRepository=cfgPlazosAutRepository;
	}
	

	
	
	@Override
	public void checkSiPuedoGrabar(CfgPlazosAut dto) throws BaseException {
		log.debug("Verifico si puedo grabar y elevo excepción sino");
		
	}

	@Override
	public void checkSiPuedoEliminar(Long id) throws BaseException {
		log.debug("Verifico si puedo eliminar y elevo excepción sino");		
	}
	
	public List<CfgPlazosAut> findCfgPlazosAutSinTramSup(Long idValDomTipoExp, Long idTipoTram, OrigenAut origen, Long idPlazo, String accionEspecial)
	{
		return cfgPlazosAutRepository.findCfgPlazosAutSinTramSup(idValDomTipoExp, idTipoTram, origen, idPlazo, accionEspecial);
	}
	
	public List<CfgPlazosAut> findCfgPlazosAutSinTramSup(Long idValDomTipoExp, Long idTipoTram, OrigenAut origen, Long idPlazo)
	{
		return cfgPlazosAutRepository.findCfgPlazosAutSinTramSup(idValDomTipoExp, idTipoTram, origen, idPlazo);
	}
	
	public List<CfgPlazosAut> findCfgPlazosAutTramSup(Long idValDomTipoExp, Long idTipoTram, Long idTipoTramSup, OrigenAut origen, Long idPlazo, String accionEspecial)
	{
		return cfgPlazosAutRepository.findCfgPlazosAutTramSup(idValDomTipoExp, idTipoTram, idTipoTramSup, origen, idPlazo, accionEspecial);
	}
	
	public CfgPlazosAut findCfgPlazosAutByTipExpTipTramTipPla(Long idValDomTipoExp, Long idTipoTram, Long idValDomTipPlazo) {
		return cfgPlazosAutRepository.findCfgPlazosAutByTipExpTipTramTipPla(idValDomTipoExp, idTipoTram, idValDomTipPlazo);
	}
	
	public List<CfgPlazosAut> findCfgPlazosAutSinTramite(Long idValDomTipoExp, OrigenAut origen, Long idPlazo)	{
		return cfgPlazosAutRepository.findCfgPlazosAutSinTramite(idValDomTipoExp, origen, idPlazo);
	}
	
	public List<CfgPlazosAut> findCfgPlazosAutSinTramite(Long idValDomTipoExp, OrigenAut origen, Long idPlazo, String accionEspecial)	{
		return cfgPlazosAutRepository.findCfgPlazosAutSinTramite(idValDomTipoExp, origen, idPlazo, accionEspecial);
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
