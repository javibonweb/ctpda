package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.MateriasExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.QMateriasExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.repository.MateriasExpedientesRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MateriasExpedientesService extends AbstractCRUDService<MateriasExpedientes> {

	/**
	 * Serial id
	 */
	private static final long serialVersionUID = 1L;
	
	private MateriasExpedientesRepository materiasExpedientesRepository;
	
	protected MateriasExpedientesService (@Autowired MathsQueryService mathsQueryService,
			@Autowired MateriasExpedientesRepository materiasExpedientesRepository) {
		super(mathsQueryService, materiasExpedientesRepository, QMateriasExpedientes.materiasExpedientes);
		// Se guarda para mi por si quiero hacer consultas personalizadas.
		this.materiasExpedientesRepository = materiasExpedientesRepository;
	}

	@Override
	public void checkSiPuedoGrabar(MateriasExpedientes dto) throws BaseException {
		log.debug("Verifico si puedo grabar y elevo excepción sino");
		
	}

	@Override
	public void checkSiPuedoEliminar(Long id) throws BaseException {
		log.debug("Verifico si puedo eliminar y elevo excepción sino");
		
	}
	
	public List<MateriasExpedientes> obtenerListMateriasExpedientesPorExpediente(Long id) {
		return materiasExpedientesRepository.obtenerListMateriasExpedientesPorExpediente(id);
	}
	
	@Override
	protected BooleanBuilder aniadirFiltrosCustom(List<FiltroDTO> filtros) {
		return new BooleanBuilder();
	}
}
