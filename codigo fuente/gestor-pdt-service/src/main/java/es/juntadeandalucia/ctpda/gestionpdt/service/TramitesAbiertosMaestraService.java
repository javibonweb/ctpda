package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.DateTimePath;

import es.juntadeandalucia.ctpda.gestionpdt.model.QTramitesAbiertosMaestra;
import es.juntadeandalucia.ctpda.gestionpdt.model.TipoTramite;
import es.juntadeandalucia.ctpda.gestionpdt.model.TramitesAbiertosMaestra;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.repository.TramitesAbiertosMaestraRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TramitesAbiertosMaestraService extends AbstractCRUDService<TramitesAbiertosMaestra> {

	/**
	 * Serial id
	 */
	private static final long serialVersionUID = 1L;
	
	TramitesAbiertosMaestraRepository tramitesAbiertosMaestraRepository;
	
	protected TramitesAbiertosMaestraService (@Autowired MathsQueryService mathsQueryService,
			@Autowired TramitesAbiertosMaestraRepository tramitesAbiertosMaestraRepository) {
		super(mathsQueryService, tramitesAbiertosMaestraRepository, QTramitesAbiertosMaestra.tramitesAbiertosMaestra);
		// Se guarda para mi por si quiero hacer consultas personalizadas.
		this.tramitesAbiertosMaestraRepository = tramitesAbiertosMaestraRepository;
	}
	
	
	public List<TipoTramite> findTiposTramitesByTramiteAbierto(){
		return tramitesAbiertosMaestraRepository.findTiposTramitesByTramiteAbierto();
	}
	
	public List<ValoresDominio> findSituacionesExpedienteByTramiteAbierto(){
		return tramitesAbiertosMaestraRepository.findSituacionesExpedienteByTramiteAbierto();
	}
	
	public TipoTramite findTiposTramitesNotif() {
		return tramitesAbiertosMaestraRepository.findTiposTramitesNotif();
	}

	@Override
	public void checkSiPuedoGrabar(TramitesAbiertosMaestra dto) throws BaseException {
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
				
				if((f.getCampo().equals("#fechaInicialInicio"))){
					if(f.getValue() != null) {
						DateTimePath<Date> fechaInicial = QTramitesAbiertosMaestra.tramitesAbiertosMaestra.fechaIni;
						bb.and(fechaInicial.eq((Date)f.getValue()).or(fechaInicial.after((Date)f.getValue())));
					}
					return;
				}
				if((f.getCampo().equals("#fechaFinalInicio"))){
					if(f.getValue() != null) {
						DateTimePath<Date> fechaFinal = QTramitesAbiertosMaestra.tramitesAbiertosMaestra.fechaIni;
						bb.and(fechaFinal.eq((Date)f.getValue()).or(fechaFinal.before((Date)f.getValue())));
					}
					return;
				}
			});			
		}
		return bb;
	}
	
}
