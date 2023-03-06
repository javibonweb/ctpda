package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;

import es.juntadeandalucia.ctpda.gestionpdt.model.Provincias;
import es.juntadeandalucia.ctpda.gestionpdt.model.QProvincias;
import es.juntadeandalucia.ctpda.gestionpdt.repository.ProvinciasRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProvinciasService extends AbstractCRUDService<Provincias>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Constructor que inyecta bean de calculos matematicos y el resto de beans que hacen falta
	 * */
	public ProvinciasService(@Autowired MathsQueryService mathsQueryService, @Autowired ProvinciasRepository provinciasRepository){
		super(mathsQueryService, 
				provinciasRepository,
				QProvincias.provincias);
	}
	
	@Override
	public List<Provincias> findAll(){
		final Sort sort = Sort.by(new Sort.Order(Direction.ASC, "descripcion")); 
		return this.crudRepository.findAll(sort);
	}

	public Iterable<Provincias> findAllByCPostal(String cp){
		if(cp == null) {
			return this.findAll();
		}
				
		if(cp.length() < 2) {
			return this.findAll();
		}

		final BooleanExpression b = QProvincias.provincias.codigo.eq(cp.substring(0,2));
		final Sort sort = Sort.by(new Sort.Order(Direction.ASC, "descripcion")); 
		return this.crudRepository.findAll(b, sort);
	}
	
	@Override
	public void checkSiPuedoGrabar(Provincias dto) throws BaseException {
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
