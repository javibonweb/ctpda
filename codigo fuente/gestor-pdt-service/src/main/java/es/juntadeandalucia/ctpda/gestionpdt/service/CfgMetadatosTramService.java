package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.CfgMetadatosTram;
import es.juntadeandalucia.ctpda.gestionpdt.model.QCfgMetadatosTram;
import es.juntadeandalucia.ctpda.gestionpdt.model.TipoTramite;
import es.juntadeandalucia.ctpda.gestionpdt.model.TramiteExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.repository.CfgMetadatosTramRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.ValidacionException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CfgMetadatosTramService extends AbstractCRUDService<CfgMetadatosTram>{
	
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String NOEXISTEREGISTROCONFIG = "no.existe.registro.config";
	
	private CfgMetadatosTramRepository cfgMetadatosTramRepository;
	
	/**
	 * Constructor que inyecta bean de calculos matematicos y el resto de beans que hacen falta
	 * */
	public CfgMetadatosTramService(@Autowired MathsQueryService mathsQueryService, @Autowired CfgMetadatosTramRepository cfgMetadatosTramRepository){
		super(mathsQueryService,
				cfgMetadatosTramRepository,
				QCfgMetadatosTram.cfgMetadatosTram);
		//también lo guardo para mi por si quiero hacer consultas personalizadas.
		this.cfgMetadatosTramRepository=cfgMetadatosTramRepository;
	}
	

	
	
	@Override
	public void checkSiPuedoGrabar(CfgMetadatosTram dto) throws BaseException {
		log.debug("Verifico si puedo grabar y elevo excepción sino");
		
	}

	@Override
	public void checkSiPuedoEliminar(Long id) throws BaseException {
		log.debug("Verifico si puedo eliminar y elevo excepción sino");		
	}
	
	
	//----------------------------------
	// Buscamos "hacia arriba".
	// Si con idTipTramSup devuelve null, buscamos por idTipTramSup = null
	// Si con idTipTram devuelve null, buscamos por idTipTram = null y idTipTramSup = null
	
	public CfgMetadatosTram findCfgMetadatosTram(Long idTipExp, Long idTipTram, Long idTipTramSup) throws ValidacionException{
		CfgMetadatosTram cfg = cfgMetadatosTramRepository.findCfgMetadatosTram(idTipExp, idTipTram, idTipTramSup);
		CfgMetadatosTram res = cfg != null? cfg : this.findCfgMetadatosTram(idTipExp, idTipTram);
		if(res == null) {
			throw new ValidacionException(NOEXISTEREGISTROCONFIG); 
		}		
		return res;
	}
	
	public CfgMetadatosTram findCfgMetadatosTram(Long idTipExp, Long idTipTram) throws ValidacionException{
		CfgMetadatosTram cfg = cfgMetadatosTramRepository.findCfgMetadatosTram(idTipExp, idTipTram);
		CfgMetadatosTram res = cfg != null? cfg : this.findCfgMetadatosTram(idTipExp);
		if(res == null) {
			throw new ValidacionException(NOEXISTEREGISTROCONFIG); 
		}		
		return res;
	}
	
	public CfgMetadatosTram findCfgMetadatosTram(Long idTipExp) throws ValidacionException{
		CfgMetadatosTram res = cfgMetadatosTramRepository.findCfgMetadatosTram(idTipExp);
		
		if(res == null) {
			throw new ValidacionException(NOEXISTEREGISTROCONFIG); 
		}
		
		return res;
	}
	
	public CfgMetadatosTram findCfgMetadatosTram(TramiteExpediente tramExp) throws ValidacionException {
		final ValoresDominio valorTipExp = tramExp.getExpediente().getValorTipoExpediente();
		final TipoTramite tipTram = tramExp.getTipoTramite();
		final TramiteExpediente tramExpSup = tramExp.getTramiteExpedienteSup();
		
		return this.findCfgMetadatosTram(valorTipExp, tipTram, tramExpSup);
	}

	public CfgMetadatosTram findCfgMetadatosTram(ValoresDominio valorTipExp, TipoTramite tipTram,
				TramiteExpediente tramExpSup) throws ValidacionException {

		final CfgMetadatosTram cfgMetadatosTram;

		/**
		 * SI EL TRAMITE EN CURSO TIENE TRAMITE SUPERIOR => BUSCAMOS POR ID DEL TIPO DE
		 * EXPEDIENTE, ID DEL TIPO DE TRAMITE E ID DEL TIPO DE TRAMITE SUPERIOR
		 */
		
		if (tramExpSup != null && tramExpSup.getTipoTramite() != null) {
			Long idTipoSup = tramExpSup.getTipoTramite().getId();
			cfgMetadatosTram = this.findCfgMetadatosTram(valorTipExp.getId(), tipTram.getId(), idTipoSup);
		} else {
			cfgMetadatosTram = this.findCfgMetadatosTram(valorTipExp.getId(), tipTram.getId());
		}
		
		return cfgMetadatosTram;
	}

	// Fin ----------------------------

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
