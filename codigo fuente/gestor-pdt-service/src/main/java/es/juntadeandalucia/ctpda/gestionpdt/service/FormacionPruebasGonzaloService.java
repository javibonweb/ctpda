package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.FormacionPruebasGonzalo;
import es.juntadeandalucia.ctpda.gestionpdt.model.QFormacionPruebasGonzalo;
import es.juntadeandalucia.ctpda.gestionpdt.repository.FormacionPruebasGonzaloRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FormacionPruebasGonzaloService extends AbstractCRUDService<FormacionPruebasGonzalo>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private FormacionPruebasGonzaloRepository formacionPruebasGonzaloRepository;
	
	/**
	 * Constructor que inyecta bean de calculos matematicos y el resto de beans que hacen falta
	 * */
	public FormacionPruebasGonzaloService(@Autowired MathsQueryService mathsQueryService, @Autowired FormacionPruebasGonzaloRepository formacionPruebasGonzaloRepository){
		super(mathsQueryService, 
				formacionPruebasGonzaloRepository,
				QFormacionPruebasGonzalo.formacionPruebasGonzalo);
		this.formacionPruebasGonzaloRepository = formacionPruebasGonzaloRepository;
	}
	
	@Override
	public void checkSiPuedoGrabar(FormacionPruebasGonzalo dto) throws BaseException {
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

