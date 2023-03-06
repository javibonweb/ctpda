package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.DerechosReclamadosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.QDerechosReclamadosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.repository.DerechosReclamadosExpedientesRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DerechosReclamadosExpedientesService extends AbstractCRUDService<DerechosReclamadosExpedientes> {
	
	/**
	 * Serial id
	 */
	private static final long serialVersionUID = 1L;
	
	DerechosReclamadosExpedientesRepository derechosReclamadosExpedientesRepository;
	
	protected DerechosReclamadosExpedientesService (@Autowired MathsQueryService mathsQueryService,
			@Autowired DerechosReclamadosExpedientesRepository derechosReclamadosExpedientesRepository) {
		super(mathsQueryService, derechosReclamadosExpedientesRepository, QDerechosReclamadosExpedientes.derechosReclamadosExpedientes);
		// Se guarda para mi por si quiero hacer consultas personalizadas.
		this.derechosReclamadosExpedientesRepository = derechosReclamadosExpedientesRepository;
	}

	@Override
	public void checkSiPuedoGrabar(DerechosReclamadosExpedientes dto) throws BaseException {
		log.debug("Verifico si puedo grabar y elevo excepción sino");
		
	}

	@Override
	public void checkSiPuedoEliminar(Long id) throws BaseException {
		log.debug("Verifico si puedo eliminar y elevo excepción sino");
		
	}
	
	DerechosReclamadosExpedientes obtenerDerechosReclamadosExpedientePorExpediente(Long id) {
		return derechosReclamadosExpedientesRepository.obtenerDerechosReclamadosExpedientePorExpediente(id);
	}
	
	public List<DerechosReclamadosExpedientes> obtenerListDerechosReclamadosExpedientePorExpediente(Long id) {
		return derechosReclamadosExpedientesRepository.obtenerListDerechosReclamadosExpedientePorExpediente(id);
	}

	@Override
	protected BooleanBuilder aniadirFiltrosCustom(List<FiltroDTO> filtros) {
		return new BooleanBuilder();
	}

}
