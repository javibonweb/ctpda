package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.ElementosSeries;
import es.juntadeandalucia.ctpda.gestionpdt.model.QElementosSeries;
import es.juntadeandalucia.ctpda.gestionpdt.repository.ElementosSeriesRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.ValidacionException;
import es.juntadeandalucia.ctpda.gestionpdt.util.FechaUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ElementosSeriesService extends AbstractCRUDService<ElementosSeries>{

	public ElementosSeriesService(@Autowired MathsQueryService mathsQueryService, @Autowired ElementosSeriesRepository elementosSeriesRepository){
		super(mathsQueryService, 
				elementosSeriesRepository,
				QElementosSeries.elementosSeries);
	}
	
	@Override
	public void checkSiPuedoGrabar(ElementosSeries dto) throws BaseException {
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
	
	//************************************
	
	public ElementosSeries obtenerElementoSerie(String tipo, Date fecha) throws ValidacionException{
		final Optional<ElementosSeries> op = this.crudRepository.findOne(
				QElementosSeries.elementosSeries.tipo.eq(tipo)
					.and(QElementosSeries.elementosSeries.fechaInicial.loe(fecha))
					.and(QElementosSeries.elementosSeries.fechaFinal.goe(fecha)
							.or(QElementosSeries.elementosSeries.fechaFinal.isNull()))
					);
		
		if(op.isEmpty()) {
			final String msg = "No se encuentra el elemento para la serie con el código de tipo y fecha de referencia indicados (" + tipo + ", " + FechaUtils.dateToStringFecha(fecha) + ").";
			log.error(msg);
			throw new ValidacionException(msg + "Consulte con su administrador.", Collections.emptyList());
		}

		return op.get();
	}
	
}

