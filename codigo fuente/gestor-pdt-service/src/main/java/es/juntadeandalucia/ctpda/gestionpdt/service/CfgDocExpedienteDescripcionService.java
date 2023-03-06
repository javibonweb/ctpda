package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.CfgDocExpedienteDescripcion;
import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentoDTO;
import es.juntadeandalucia.ctpda.gestionpdt.model.QCfgDocExpedienteDescripcion;
import es.juntadeandalucia.ctpda.gestionpdt.repository.CfgDocExpedienteDescripcionRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CfgDocExpedienteDescripcionService extends AbstractCRUDService<CfgDocExpedienteDescripcion> {

	/**
	 * Serial id
	 */
	private static final long serialVersionUID = 1L;
	
	private CfgDocExpedienteDescripcionRepository cfgDocExpedienteDescripcionRepository;
	
	protected CfgDocExpedienteDescripcionService (@Autowired MathsQueryService mathsQueryService,
			@Autowired CfgDocExpedienteDescripcionRepository cfgDocExpedienteDescripcionRepository) {
		super(mathsQueryService, cfgDocExpedienteDescripcionRepository, QCfgDocExpedienteDescripcion.cfgDocExpedienteDescripcion);
		// Se guarda para mi por si quiero hacer consultas personalizadas.
		this.cfgDocExpedienteDescripcionRepository = cfgDocExpedienteDescripcionRepository;
	}
	
	@Override
	public void checkSiPuedoGrabar(CfgDocExpedienteDescripcion dto) throws BaseException {
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
	
	
	public List<CfgDocExpedienteDescripcion> findDescripcionesTipoDocCfg(Long idCfgTr) {
		return cfgDocExpedienteDescripcionRepository.findByCfgDocExpedienteTramitacionIdOrderByOrden(idCfgTr);
	}
	
	public void aplicarCfg(CfgDocExpedienteDescripcion cfg, DocumentoDTO dto) {
		dto.setDescripcion(cfg.getDescripcion());
		dto.setDescripcionAbrev(cfg.getDescripcionAbrev());

		this.aplicarCfgIndicadores(cfg, dto);
	}
	
	public void aplicarCfgIndicadores(CfgDocExpedienteDescripcion cfg, DocumentoDTO dto) {		
		dto.setEditable(cfg.getEditable());
		dto.setAnonimizado(cfg.getAnonimizado());
		dto.setAnonimizadoParcial(cfg.getAnonimizadoParcial());
		dto.setFirmado(cfg.getFirmado());
		dto.setSellado(cfg.getSellado());
		
		dto.setCategoriaId(null != cfg.getCategoria()? 
				cfg.getCategoria().getId()
				: null);
	}
	
}
