package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;

import es.juntadeandalucia.ctpda.gestionpdt.model.Localidades;
import es.juntadeandalucia.ctpda.gestionpdt.model.QLocalidades;
import es.juntadeandalucia.ctpda.gestionpdt.model.Qfor_pruebas_pedro_romero;
import es.juntadeandalucia.ctpda.gestionpdt.model.for_pruebas_pedro_romero;
import es.juntadeandalucia.ctpda.gestionpdt.repository.LocalidadesRepository;
import es.juntadeandalucia.ctpda.gestionpdt.repository.for_pruebas_pedro_romeroRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class for_pruebas_pedro_romeroService extends AbstractCRUDService<for_pruebas_pedro_romero>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private for_pruebas_pedro_romeroRepository for_pruebas_pedro_romeroRepository;
	/**
	 * Constructor que inyecta bean de calculos matematicos y el resto de beans que hacen falta
	 * */
	public for_pruebas_pedro_romeroService(@Autowired MathsQueryService mathsQueryService, @Autowired for_pruebas_pedro_romeroRepository for_pruebas_pedro_romero){
		super(mathsQueryService, 
				for_pruebas_pedro_romero,
				Qfor_pruebas_pedro_romero.for_pruebas_pedro_romero);
		this.for_pruebas_pedro_romeroRepository = for_pruebas_pedro_romeroRepository;
	}


	
	@Override
	public void checkSiPuedoGrabar(for_pruebas_pedro_romero dto) throws BaseException {
		log.debug("Verifico si puedo grabar y elevo excepción sino");
		
	}

	@Override
	public void checkSiPuedoEliminar(Long id) throws BaseException {
		log.debug("Verifico si puedo eliminar y elevo excepción sino");		
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
