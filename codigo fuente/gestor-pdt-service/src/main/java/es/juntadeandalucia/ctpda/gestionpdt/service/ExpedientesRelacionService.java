package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.ExpedientesRelacion;
import es.juntadeandalucia.ctpda.gestionpdt.model.QExpedientesRelacion;
import es.juntadeandalucia.ctpda.gestionpdt.repository.ExpedientesRelacionRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class ExpedientesRelacionService extends AbstractCRUDService<ExpedientesRelacion> {

	private static final long serialVersionUID = 1L;

	private ExpedientesRelacionRepository expedientesRelacionRepository;

	protected ExpedientesRelacionService(@Autowired MathsQueryService mathsQueryService,
			@Autowired ExpedientesRelacionRepository expedientesRelacionRepository) {
		super(mathsQueryService, expedientesRelacionRepository, QExpedientesRelacion.expedientesRelacion);
		// también lo guardo para mi por si quiero hacer consultas personalizadas.
		this.expedientesRelacionRepository = expedientesRelacionRepository;
	}

	@Override
	public void checkSiPuedoGrabar(ExpedientesRelacion dto) throws BaseException {
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

	public List<ExpedientesRelacion> findExpedientesRelacionados(Long idExp){
		return this.expedientesRelacionRepository.findExpedientesRelacionados(idExp);
	}
	
	public List<ExpedientesRelacion> findExpedientesOrigen(Long idExp){
		return this.expedientesRelacionRepository.findExpedientesOrigen(idExp);
	}

	public List<Expedientes> findExpedientesRelacionables(Long idExp, List<Long> idsExpedientesRelacionados,
			String filtroNum, Date filtroFecha){
		return this.expedientesRelacionRepository.findExpedientesRelacionables(idExp, idsExpedientesRelacionados, filtroNum, filtroFecha);
	}
	
	public List<Long> findIdExpRelacionadosByExpOrigen(Long idExpOrigen){
		return this.expedientesRelacionRepository.findIdExpRelacionadosByExpOrigen(idExpOrigen);
	}
	
	public List<String> findExpRelacionadosOrigenByExpAndMotivo(long idExp, String motivo){
		return this.expedientesRelacionRepository.findExpRelacionadosOrigenByExpAndMotivo(idExp, motivo);
	}
	
	public List<String> findExpRelacionadosRelacionadoByExpAndMotivo(long idExp, String motivo){
		return this.expedientesRelacionRepository.findExpRelacionadosRelacionadoByExpAndMotivo(idExp, motivo);
	}
	

}
