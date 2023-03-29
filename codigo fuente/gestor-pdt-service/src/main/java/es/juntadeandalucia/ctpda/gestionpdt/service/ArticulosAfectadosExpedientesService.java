package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.ArticulosAfectadosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.QArticulosAfectadosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.repository.ArticulosAfectadosExpedientesRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ArticulosAfectadosExpedientesService extends AbstractCRUDService<ArticulosAfectadosExpedientes> {

	/**
	 * Serial id
	 */
	private static final long serialVersionUID = 1L;
	
	ArticulosAfectadosExpedientesRepository articulosAfectadosExpedientesRepository;
	
	protected ArticulosAfectadosExpedientesService (@Autowired MathsQueryService mathsQueryService,
			@Autowired ArticulosAfectadosExpedientesRepository articulosAfectadosExpedientesRepository) {
		super(mathsQueryService, articulosAfectadosExpedientesRepository, QArticulosAfectadosExpedientes.articulosAfectadosExpedientes);
		// Se guarda para mi por si quiero hacer consultas personalizadas.
		this.articulosAfectadosExpedientesRepository = articulosAfectadosExpedientesRepository;
	}

	@Override
	public void checkSiPuedoGrabar(ArticulosAfectadosExpedientes dto) throws BaseException {
		log.debug("Verifico si puedo grabar y elevo excepción sino");
		
	}

	@Override
	public void checkSiPuedoEliminar(Long id) throws BaseException {
		log.debug("Verifico si puedo eliminar y elevo excepción sino");
		
	}
	
	ArticulosAfectadosExpedientes obtenerArticulosAfectadosExpedientePorExpediente(Long id) {
		return articulosAfectadosExpedientesRepository.obtenerArticulosAfectadosExpedientePorExpediente(id);
	}
	
	public List<ArticulosAfectadosExpedientes> obtenerListArticulosAfectadosExpedientePorExpediente(Long id) {
		return articulosAfectadosExpedientesRepository.obtenerListArticulosAfectadosExpedientePorExpediente(id);
	}

	@Override
	protected BooleanBuilder aniadirFiltrosCustom(List<FiltroDTO> filtros) {
		return new BooleanBuilder();
	}
}
