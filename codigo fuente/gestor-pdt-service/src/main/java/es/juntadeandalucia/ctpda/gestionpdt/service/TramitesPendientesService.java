package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.DateTimePath;

import es.juntadeandalucia.ctpda.gestionpdt.model.DetalleExpdteTram;
import es.juntadeandalucia.ctpda.gestionpdt.model.QDetalleExpdteTram;
import es.juntadeandalucia.ctpda.gestionpdt.model.QTramitesAbiertosMaestra;
import es.juntadeandalucia.ctpda.gestionpdt.model.TipoTramite;
import es.juntadeandalucia.ctpda.gestionpdt.model.TramitesAbiertosMaestra;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.repository.TramitesAbiertosMaestraRepository;
import es.juntadeandalucia.ctpda.gestionpdt.repository.TramitesPendientesRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TramitesPendientesService extends AbstractCRUDService<DetalleExpdteTram> {

	/**
	 * Serial id
	 */
	private static final long serialVersionUID = 1L;
	
	TramitesPendientesRepository tramitesPendientesRepository;
	
	protected TramitesPendientesService (@Autowired MathsQueryService mathsQueryService,
			@Autowired TramitesPendientesRepository tramitesPendientesRepository) {
		super(mathsQueryService, tramitesPendientesRepository, QDetalleExpdteTram.detalleExpdteTram);
		// Se guarda para mi por si quiero hacer consultas personalizadas.
		this.tramitesPendientesRepository = tramitesPendientesRepository;
	}
	
	
	
	@Override
	public void checkSiPuedoGrabar(DetalleExpdteTram dto) throws BaseException {
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
				
				if((f.getCampo().equals("#fechaLimite"))){
					if(f.getValue() != null) {
						DateTimePath<Date> fechaInicial = QDetalleExpdteTram.detalleExpdteTram.fechaLimite;
						bb.and(fechaInicial.eq((Date)f.getValue()).or(fechaInicial.after((Date)f.getValue())));
					}
					return;
				}
				if((f.getCampo().equals("#fechaLimite"))){
					if(f.getValue() != null) {
						DateTimePath<Date> fechaFinal = QDetalleExpdteTram.detalleExpdteTram.fechaLimite;
						bb.and(fechaFinal.eq((Date)f.getValue()).or(fechaFinal.before((Date)f.getValue())));
					}
					return;
				}
			});			
		}
		return bb;
	}
	
}
