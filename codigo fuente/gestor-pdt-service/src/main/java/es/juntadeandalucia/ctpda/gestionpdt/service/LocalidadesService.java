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
import es.juntadeandalucia.ctpda.gestionpdt.repository.LocalidadesRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LocalidadesService extends AbstractCRUDService<Localidades>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Constructor que inyecta bean de calculos matematicos y el resto de beans que hacen falta
	 * */
	public LocalidadesService(@Autowired MathsQueryService mathsQueryService, @Autowired LocalidadesRepository localidadesRepository){
		super(mathsQueryService, 
				localidadesRepository,
				QLocalidades.localidades);
	}

	public Iterable<Localidades> findAllByIdProvincia(Long idProv){
		if(idProv==null) {
			return Collections.emptyList();
		}
		
		BooleanExpression b = QLocalidades.localidades.provincia.id.eq(idProv);
		final Sort sort = Sort.by(new Sort.Order(Direction.ASC, "descripcion")); 
		return this.crudRepository.findAll(b, sort);
	}
	
	@Override
	public void checkSiPuedoGrabar(Localidades dto) throws BaseException {
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
