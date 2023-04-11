package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;

import es.juntadeandalucia.ctpda.gestionpdt.model.FormacionPruebasAntonioF;
import es.juntadeandalucia.ctpda.gestionpdt.model.QFormacionPruebasAntonioF;
import es.juntadeandalucia.ctpda.gestionpdt.model.QLocalidades;
import es.juntadeandalucia.ctpda.gestionpdt.repository.FormacionPruebasAntonioFRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FormacionPruebasAntonioFService extends AbstractCRUDService<FormacionPruebasAntonioF>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private FormacionPruebasAntonioFRepository formacionPruebasAntonioFRepository;
	
	/**
	 * Constructor que inyecta bean de calculos matematicos y el resto de beans que hacen falta
	 * */
	public FormacionPruebasAntonioFService(@Autowired MathsQueryService mathsQueryService, @Autowired FormacionPruebasAntonioFRepository formacionPruebasAntonioFRepository){
		super(mathsQueryService, 
				formacionPruebasAntonioFRepository,
				QFormacionPruebasAntonioF.formacionPruebasAntonioF); 
		this.formacionPruebasAntonioFRepository = formacionPruebasAntonioFRepository;
	}

	
	@Override
	public void checkSiPuedoGrabar(FormacionPruebasAntonioF dto) throws BaseException {
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
