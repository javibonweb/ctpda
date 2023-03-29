package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.ElementosSeries;
import es.juntadeandalucia.ctpda.gestionpdt.model.QSeries;
import es.juntadeandalucia.ctpda.gestionpdt.model.Series;
import es.juntadeandalucia.ctpda.gestionpdt.repository.SeriesRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.ValidacionException;
import es.juntadeandalucia.ctpda.gestionpdt.util.FechaUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SeriesService extends AbstractCRUDService<Series>{

	private static final long serialVersionUID = 1L;
	
	private static final String CONSULTEADMINISTRADOR = " Consulte con su administrador.";

	@Autowired
	private ElementosSeriesService elemSeriesService;
	
	public SeriesService(@Autowired MathsQueryService mathsQueryService, @Autowired SeriesRepository seriesRepository){
		super(mathsQueryService, 
				seriesRepository,
				QSeries.series);
	}
	
	@Override
	public void checkSiPuedoGrabar(Series dto) throws BaseException {
		log.debug("Verifico si puedo grabar y elevo excepción si no");
	}

	@Override
	public void checkSiPuedoEliminar(Long id) throws BaseException {
		log.debug("Verifico si puedo eliminar y elevo excepción si no");		
	}

	@Override
	protected BooleanBuilder aniadirFiltrosCustom(List<FiltroDTO> filtros) {
		return new BooleanBuilder();
	}
	
	//--------------------------------------
	
	private Series obtenerSerie(String tipo, Date fecha) throws ValidacionException{
		//Localizo el elemento y busco su serie vigente
		final ElementosSeries elemento = elemSeriesService.obtenerElementoSerie(tipo, fecha);
		
		if(elemento == null) {
			final String msg = "No se encuentra el elemento de serie con el código de tipo y fecha de referencia indicados (" + tipo + ", " + FechaUtils.dateToStringFecha(fecha) + ").";
			log.error(msg);
			throw new ValidacionException(msg + CONSULTEADMINISTRADOR, Collections.emptyList());
		}
		
		final String codigoSerie = elemento.getCodigoSerie();
		
		//Si no encuentra la serie debe dar excepción
		final Optional<Series> opS; 
		
		try {
			opS = this.crudRepository.findOne(
				QSeries.series.codigoSerie.eq(codigoSerie)
					.and(QSeries.series.fechaInicial.loe(fecha))
					.and(QSeries.series.fechaFinal.goe(fecha))
					);
		} catch(IncorrectResultSizeDataAccessException irsEx) {
			final String msg = "La consulta para obtener la serie obtuvo más de un resultado.";
			log.error(msg + " Comprobar en la serie el código de elemento y las fechas de inicio/fin almacenadas.");
			throw new ValidacionException(msg + CONSULTEADMINISTRADOR, Collections.emptyList());
		}
	
		if(opS.isEmpty()) {
			final String msg = "No se encuentra la serie con el código de serie y fecha de referencia indicados (" + codigoSerie + ", " + FechaUtils.dateToStringFecha(fecha) + ").";
			log.error(msg);
			throw new ValidacionException(msg + CONSULTEADMINISTRADOR, Collections.emptyList());
		}
		
		return opS.get();
	}
	
	//--------------------------------------
	//Propagation Mandatory: La operación siempre debe formar parte de otra. 
	//La transacción debe venir ya creada desde esta última.
	
	public String nextNumeroSerie(String tipo) throws BaseException {
		return nextNumeroSerie(tipo, null);
	}
	
	public String nextNumeroSerie(String tipo, Date fecha) throws BaseException {
		final Date f = (fecha!=null)? fecha : FechaUtils.hoy();
		//obtener serie a partir de tipo y fecha
		final Series s = obtenerSerie(tipo, f);
		final String num = incrementar(s, f);
		this.guardar(s);

		return num;
	}
	
	//--------------------------------------
	
	private String incrementar(Series s, Date fechaRef) throws BaseException {
		
		Long numIni = s.getNumeroInicial();
		Long ultimo = s.getUltimoNumero();
		
		//Con esta linea se permite setear el último número a 0 (el número inicial debe ser al menos 1) 
		//Ponerlo a 0 usará el número inicial como primer valor de la secuencia. En otro caso se incrementa el último. 
		final Long n = (numIni > ultimo)? numIni :
				 ultimo + s.getIncremento();
		
		if(n > s.getNumeroFinal()) {
			final String msg = "La secuencia de la serie con id " + s.getId() + " ha llegado a su valor máximo estipulado: " + 
							n + " > " + s.getNumeroFinal() + " (siguiente número > número final). ";
			log.error(msg);
			throw new ValidacionException(msg + CONSULTEADMINISTRADOR, Collections.emptyList());
		}
		
		final Date ultFecha = s.getUltimaFecha();
		if(Boolean.TRUE.equals(s.getSecuenciaFecha()) && ultFecha!= null && fechaRef.before(ultFecha)) {				
			final String msg = "La secuencia para incrementar la serie no permite continuar usando una fecha anterior a la última vez que se incrementó (" 
								+ FechaUtils.dateToStringFecha(fechaRef) + " < "
								+ FechaUtils.dateToStringFecha(ultFecha) + ").";
			log.error(msg);
			throw new ValidacionException(msg + CONSULTEADMINISTRADOR, Collections.emptyList());
		}
		
		//La fecha está normalizada a las 00:00
		s.setUltimaFecha(FechaUtils.hora0(fechaRef));
		final String strNum = aplicarPatron(s, n);
		
		//ok, actualizo
		s.setUltimoNumero(n);
		
		return strNum;
	}
	
	private String aplicarPatron(Series s, Long numero) throws BaseException {
		final PatronSerie p = new PatronSerie(s.getPatron());
		return p.get(numero);
	}

	//--------------------------------------
	
	public boolean avisarFinNumero(String tipo, Date fecha) throws BaseException {
		return avisarFinNumero(obtenerSerie(tipo, fecha));
	}	
	
	public boolean avisarFinNumero(Series s) {
		return s.getUltimoNumero() + s.getMargenAviso() >= s.getNumeroFinal();
	}
	
}
