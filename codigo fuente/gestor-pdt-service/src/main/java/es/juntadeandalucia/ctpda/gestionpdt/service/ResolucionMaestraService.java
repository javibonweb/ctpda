package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.DateTimePath;

import es.juntadeandalucia.ctpda.gestionpdt.model.QResolucionMaestra;
import es.juntadeandalucia.ctpda.gestionpdt.model.ResolucionMaestra;
import es.juntadeandalucia.ctpda.gestionpdt.repository.ResolucionMaestraRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ResolucionMaestraService extends AbstractCRUDService<ResolucionMaestra> {

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	protected ResolucionMaestraService(@Autowired MathsQueryService mathsQueryService,
			@Autowired ResolucionMaestraRepository resolucionMaestraRepository) {
		super(mathsQueryService, resolucionMaestraRepository, QResolucionMaestra.resolucionMaestra);
		this.joinBuilder = query ->
			query
		;

	}

	@Override
	public void checkSiPuedoGrabar(ResolucionMaestra dto) throws BaseException {
		log.debug("Verifico si puedo grabar y elevo excepción sino");

	}

	@Override
	public void checkSiPuedoEliminar(Long id) throws BaseException {
		log.debug("Verifico si puedo eliminar y elevo excepción sino");

	}
	
	
	@Override
	protected BooleanBuilder aniadirFiltrosCustom(List<FiltroDTO> filtros) {
		final BooleanBuilder bb = new BooleanBuilder();
		
		if(filtros != null) {
			filtros.stream().forEach(f -> {
				if((f.getCampo().equals("#fechaInicial"))){
					if(f.getValue() != null) {
						DateTimePath<Date> fechaInicial = QResolucionMaestra.resolucionMaestra.fechaResolucion;
						bb.and(fechaInicial.eq((Date)f.getValue()).or(fechaInicial.after((Date)f.getValue())));
					}
					return;
				}
				if((f.getCampo().equals("#fechaFinal"))){
					if(f.getValue() != null) {
						DateTimePath<Date> fechaFinal = QResolucionMaestra.resolucionMaestra.fechaResolucion;
						bb.and(fechaFinal.eq((Date)f.getValue()).or(fechaFinal.before((Date)f.getValue())));
					}
					return;
				}
			});
		}
	
		
		
		return bb;
	}

}
