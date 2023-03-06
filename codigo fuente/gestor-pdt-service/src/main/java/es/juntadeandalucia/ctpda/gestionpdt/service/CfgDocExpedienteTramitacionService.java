package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;

import es.juntadeandalucia.ctpda.gestionpdt.model.CfgDocExpedienteTramitacion;
import es.juntadeandalucia.ctpda.gestionpdt.model.QCfgDocExpedienteTramitacion;
import es.juntadeandalucia.ctpda.gestionpdt.model.TipoDocumento;
import es.juntadeandalucia.ctpda.gestionpdt.model.TipoTramite;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.repository.CfgDocExpedienteTramitacionRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.ValidacionException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CfgDocExpedienteTramitacionService extends AbstractCRUDService<CfgDocExpedienteTramitacion> {

	/**
	 * Serial id
	 */
	private static final long serialVersionUID = 1L;
	
	private CfgDocExpedienteTramitacionRepository cfgDocExpedienteTramitacionRepository;
	
	protected CfgDocExpedienteTramitacionService (@Autowired MathsQueryService mathsQueryService,
			@Autowired CfgDocExpedienteTramitacionRepository cfgDocExpedienteTramitacionRepository) {
		super(mathsQueryService, cfgDocExpedienteTramitacionRepository, QCfgDocExpedienteTramitacion.cfgDocExpedienteTramitacion);
		// Se guarda para mi por si quiero hacer consultas personalizadas.
		this.cfgDocExpedienteTramitacionRepository = cfgDocExpedienteTramitacionRepository;
	}

	public List<TipoDocumento> findTiposDocumentos(ValoresDominio tipoExp, ValoresDominio situacion, TipoTramite tipoTramite, TipoTramite tipoTramiteSup) throws ValidacionException{
		List<TipoDocumento> res = cfgDocExpedienteTramitacionRepository.findTiposDocumentos(tipoExp, situacion, tipoTramite, tipoTramiteSup);
		if(res.isEmpty()) {
			throw new ValidacionException("no.existe.registro.config");
		}
		return res;
	}
	
	public List<CfgDocExpedienteTramitacion> findTiposDocumentosCfg(ValoresDominio tipoExp, ValoresDominio situacion, TipoTramite tipoTramite, TipoTramite tipoTramiteSup, String codOrigen) throws ValidacionException{
		List<CfgDocExpedienteTramitacion> res = cfgDocExpedienteTramitacionRepository.findTiposDocumentosCfg(tipoExp, situacion, tipoTramite, tipoTramiteSup, codOrigen);
		if(res.isEmpty()) {
			throw new ValidacionException("no.existe.registro.config");
		}
		return res;
	}
	
	public CfgDocExpedienteTramitacion findOne(CfgDocExpedienteTramitacion cfgDocDTO) throws BaseException{
		return this.findOne(
				cfgDocDTO.getValorTipoExpediente(),
				cfgDocDTO.getSituacion(),
				cfgDocDTO.getTipoTramite(),
				cfgDocDTO.getTipoTramiteSup(),
				cfgDocDTO.getTipoDocumento());
	}

	public CfgDocExpedienteTramitacion findOne(ValoresDominio tipoExp, ValoresDominio situacion, TipoTramite tipoTramite, TipoTramite tipoTramiteSup, TipoDocumento tipoDocumento) throws BaseException{
		QCfgDocExpedienteTramitacion qCfg = QCfgDocExpedienteTramitacion.cfgDocExpedienteTramitacion;
		
		BooleanExpression b;
		b = qCfg.valorTipoExpediente.id.eq(tipoExp.getId());
		b = b.and(qCfg.situacion.id.eq(situacion.getId()).or(qCfg.situacion.id.isNull()));
		b = b.and(qCfg.tipoTramite.id.eq(tipoTramite.getId()));
		
		if(null != tipoTramiteSup && null != tipoTramiteSup.getId()) {
			b = b.and(qCfg.tipoTramiteSup.id.eq(tipoTramiteSup.getId()));
		} else {
			b = b.and(qCfg.tipoTramiteSup.id.isNull());
		}
		
		b = b.and(qCfg.tipoDocumento.id.eq(tipoDocumento.getId()));

		return this.crudRepository.findOne(b).orElseThrow(() -> new BaseException("No se encuentra el elemento CfgDocExpedienteTramitacion.", new java.util.ArrayList<>()));
	}
	
	@Override
	public void checkSiPuedoGrabar(CfgDocExpedienteTramitacion dto) throws BaseException {
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
