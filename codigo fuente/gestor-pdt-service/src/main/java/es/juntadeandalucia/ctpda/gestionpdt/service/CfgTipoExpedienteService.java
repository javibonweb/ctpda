package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.CfgTipoExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.QCfgTipoExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.repository.CfgTipoExpedienteRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CfgTipoExpedienteService extends AbstractCRUDService<CfgTipoExpediente> {
	
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	private CfgTipoExpedienteRepository cfgTipoExpedienteRepository;
	
	/**
	 * Constructor que inyecta bean de calculos matematicos y el resto de beans que hacen falta
	 * */
	public CfgTipoExpedienteService(@Autowired MathsQueryService mathsQueryService, @Autowired CfgTipoExpedienteRepository cfgTipoExpedienteRepository){
		super(mathsQueryService,
				cfgTipoExpedienteRepository,
				QCfgTipoExpediente.cfgTipoExpediente);
		//también lo guardo para mi por si quiero hacer consultas personalizadas.
		this.cfgTipoExpedienteRepository=cfgTipoExpedienteRepository;
	}

	@Override
	public void checkSiPuedoGrabar(CfgTipoExpediente dto) throws BaseException {
		/**
		 * PENDIENTE DESARROLLO
		 */
 }

	@Override
	public void checkSiPuedoEliminar(Long id) throws BaseException {
		/**
		 * PENDIENTE DESARROLLO
		 */
 }

	@Override
	protected BooleanBuilder aniadirFiltrosCustom(List<FiltroDTO> filtros) {
		return null;
	}
	
	public CfgTipoExpediente obtenerCfgTipoExpedientePorValorTipoExpediente(Long id) {
		return cfgTipoExpedienteRepository.obtenerCfgTipoExpedientePorValorTipoExpediente(id);
	}
	
	public CfgTipoExpediente obtenerCfgTipoExpedienteByCodigoTipoExp(String codigoTipoExp) {
		return cfgTipoExpedienteRepository.obtenerCfgTipoExpedienteByCodigoTipoExp(codigoTipoExp);
	}

}
