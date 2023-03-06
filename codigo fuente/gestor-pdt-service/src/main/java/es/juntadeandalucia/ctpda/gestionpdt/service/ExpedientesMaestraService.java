package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.DateTimePath;

import es.juntadeandalucia.ctpda.gestionpdt.model.ExpedientesMaestra;
import es.juntadeandalucia.ctpda.gestionpdt.model.QExpedientesMaestra;
import es.juntadeandalucia.ctpda.gestionpdt.repository.ExpedientesMaestraRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class ExpedientesMaestraService extends AbstractCRUDService<ExpedientesMaestra> {
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	protected ExpedientesMaestraService(@Autowired MathsQueryService mathsQueryService,
			@Autowired ExpedientesMaestraRepository expedientesMaestraRepository) {
		super(mathsQueryService, expedientesMaestraRepository, QExpedientesMaestra.expedientesMaestra);
	}

	@Override
	public void checkSiPuedoGrabar(ExpedientesMaestra dto) throws BaseException {
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
			/**
			 * Actualmente no se usa este filtro, porque es necesario tener un estudio previo a su uso
			 * para que el resultado de la consulta devuelva además todos los expedientes que tengan 
			 * materias hijas de las padres según los anidamientos 
			 * Es decir, expedientes con materias incluidas en los anidamientos del formulario de la materia
			 
			if((f.getCampo().equals("#materiasuphijas"))){
				if (f.getValue()!=null) {
					BooleanExpression materiaSup = QExpedientesMaestra.expedientesMaestra.idMateria.contains((String) f.getValue());
					BooleanExpression materiaHija = QExpedientesMaestra.expedientesMaestra.idMateriaSup.contains((String) f.getValue());
					
					bb.or(materiaSup);
					bb.or(materiaHija);
				}
				return;
			}
			*/
			
			filtros.stream().filter(fx-> fx.getCampo().equals("#fechaInicialEntrada")).forEach(
				fx -> {
					if (fx.getValue()!=null) {
						DateTimePath<Date> fechaInicial = QExpedientesMaestra.expedientesMaestra.fechaEntrada;
						bb.and(fechaInicial.eq((Date)fx.getValue()).or(fechaInicial.after((Date)fx.getValue())));
					}
				}
			);
			
			filtros.stream().filter(fx-> fx.getCampo().equals("#fechaFinalEntrada")).forEach(
					fx -> {
						if (fx.getValue()!=null) {
							DateTimePath<Date> fechaFinal = QExpedientesMaestra.expedientesMaestra.fechaEntrada;
							bb.and(fechaFinal.eq((Date)fx.getValue()).or(fechaFinal.before((Date)fx.getValue())));
						}
					}
				);
		
			filtros.stream().filter(fx-> fx.getCampo().equals("#tipoExpedienteExcluido")).forEach(
				fx -> {
					if (fx.getValue()!=null) {
						bb.and(QExpedientesMaestra.expedientesMaestra.tipoExpediente.ne(fx.getValue().toString()));
					}
				}
			);			
		}
				
		return bb;
	}

}
