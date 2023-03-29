package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.DateTimePath;

import es.juntadeandalucia.ctpda.gestionpdt.model.NotificacionesPendientesMaestra;
import es.juntadeandalucia.ctpda.gestionpdt.model.QNotificacionesPendientesMaestra;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.repository.NotificacionesPendientesMaestraRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificacionesPendientesMaestraService extends AbstractCRUDService<NotificacionesPendientesMaestra>{
	
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	NotificacionesPendientesMaestraRepository notificacionesPendientesMaestraRepository;
	
	protected NotificacionesPendientesMaestraService (@Autowired MathsQueryService mathsQueryService,
			@Autowired NotificacionesPendientesMaestraRepository notificacionesPendientesMaestraRepository) {
		super(mathsQueryService, notificacionesPendientesMaestraRepository, QNotificacionesPendientesMaestra.notificacionesPendientesMaestra);
		// Se guarda para mi por si quiero hacer consultas personalizadas.
		this.notificacionesPendientesMaestraRepository = notificacionesPendientesMaestraRepository;
	}
	
	public List<ValoresDominio> findSituacionesExpedienteByTramiteAbierto(){
		return notificacionesPendientesMaestraRepository.findSituacionesExpedienteByTramiteAbierto();
	}


	
	@Override
	public void checkSiPuedoGrabar(NotificacionesPendientesMaestra dto) throws BaseException {
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
		final BooleanBuilder bb = new BooleanBuilder();
		if(filtros != null) {
			filtros.stream().forEach(f -> {
				if((f.getCampo().equals("#fechaInicialInicio"))){
					if(f.getValue() != null) {
						DateTimePath<Date> fechaInicial = QNotificacionesPendientesMaestra.notificacionesPendientesMaestra.fechaIni;
						bb.and(fechaInicial.eq((Date)f.getValue()).or(fechaInicial.after((Date)f.getValue())));
					}
					return;
				}
				if((f.getCampo().equals("#fechaFinalInicio"))){
					if(f.getValue() != null) {
						DateTimePath<Date> fechaFinal = QNotificacionesPendientesMaestra.notificacionesPendientesMaestra.fechaIni;
						bb.and(fechaFinal.eq((Date)f.getValue()).or(fechaFinal.before((Date)f.getValue())));
					}
					return;
				}
			});
		}
		
		return bb;
	}

}
