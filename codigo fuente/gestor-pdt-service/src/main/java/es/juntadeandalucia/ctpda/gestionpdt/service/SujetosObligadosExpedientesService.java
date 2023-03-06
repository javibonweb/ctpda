package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.Personas;
import es.juntadeandalucia.ctpda.gestionpdt.model.QSujetosObligadosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.SujetosObligados;
import es.juntadeandalucia.ctpda.gestionpdt.model.SujetosObligadosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.repository.SujetosObligadosExpedientesRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class SujetosObligadosExpedientesService extends AbstractCRUDService<SujetosObligadosExpedientes> {

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	private SujetosObligadosExpedientesRepository sujetosObligadosExpedientesRepository;
	
	protected SujetosObligadosExpedientesService(@Autowired MathsQueryService mathsQueryService,
			@Autowired SujetosObligadosExpedientesRepository sujetosObligadosExpedientesRepository) {
		super(mathsQueryService, sujetosObligadosExpedientesRepository, QSujetosObligadosExpedientes.sujetosObligadosExpedientes);
		// también lo guardo para mi por si quiero hacer consultas personalizadas.
		this.sujetosObligadosExpedientesRepository = sujetosObligadosExpedientesRepository;
	}

	@Override
	public void checkSiPuedoGrabar(SujetosObligadosExpedientes dto) throws BaseException {
		log.debug("Verifico si puedo grabar y elevo excepción sino");

	}

	@Override
	public void checkSiPuedoEliminar(Long id) throws BaseException {
		log.debug("Verifico si puedo eliminar y elevo excepción sino");

	}
	
	public SujetosObligadosExpedientes obtenerSujetosObligadosExpedientePorExpediente(Long id) {
		return sujetosObligadosExpedientesRepository.obtenerSujetosObligadosExpedientePorExpediente(id);
	}
	
	public Expedientes obtenerExpMaxPorSujetoOblig(Long idSujOblig) {
		return sujetosObligadosExpedientesRepository.obtenerExpMaxPorSujetoOblig(idSujOblig);
	}
	
	public SujetosObligadosExpedientes obtenerSujetosObligadosExpedientePorSujOblig(Long idExp, Long idSujOblig) {
		return sujetosObligadosExpedientesRepository.obtenerSujetosObligadosExpedientePorSujOblig(idExp, idSujOblig);
	}
	
	public SujetosObligadosExpedientes obtenerSujetosObligadosExpedientePrincipalPorExpediente(Long id) {
		return sujetosObligadosExpedientesRepository.obtenerSujetosObligadosExpedientePrincipalPorExpediente(id);
	}
	public List<SujetosObligadosExpedientes> obtenerSujetosObligadosExpedientePrincipalYNoPrincipalPorExpediente(Long id) {
		return sujetosObligadosExpedientesRepository.obtenerSujetosObligadosExpedientePrincipalYNoPrincipalPorExpediente(id);
	}
	
	public List<SujetosObligados> obtenerSujetosObligadosExpediente(Long id) {
		return sujetosObligadosExpedientesRepository.obtenerSujetosObligadosExpediente(id);
	}
	
	public List<Personas> obtenerDpdExpediente(long idExp){
		return sujetosObligadosExpedientesRepository.obtenerDpdExpediente(idExp);
	}
	
	public Personas obtenerDpdSujetoPpalExpediente(long idExp) {
		return sujetosObligadosExpedientesRepository.obtenerDpdSujetoPpalExpediente(idExp);
	}
	
	public SujetosObligadosExpedientes obtenerSujObligExpConDpdSinPersona(long idExp)
	{
		return sujetosObligadosExpedientesRepository.obtenerSujObligExpConDpdSinPersona(idExp);
	}

	@Override
	protected BooleanBuilder aniadirFiltrosCustom(List<FiltroDTO> filtros) {
		return new BooleanBuilder();
	}
}
