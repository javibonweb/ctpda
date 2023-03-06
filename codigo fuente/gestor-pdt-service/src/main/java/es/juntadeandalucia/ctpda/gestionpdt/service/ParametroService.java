package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.Parametro;
import es.juntadeandalucia.ctpda.gestionpdt.model.QParametro;
import es.juntadeandalucia.ctpda.gestionpdt.repository.ParametroRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ParametroService extends AbstractCRUDService< Parametro>{
	
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	private ParametroRepository parametroRepository;
	
	/**
	 * Constructor que inyecta bean de calculos matematicos y el resto de beans que hacen falta
	 * */
	public ParametroService(@Autowired MathsQueryService mathsQueryService, @Autowired ParametroRepository parametroRepository){
		super(mathsQueryService,
				parametroRepository,
				QParametro.parametro);
		//también lo guardo para mi por si quiero hacer consultas personalizadas.
		this.parametroRepository=parametroRepository;
	}

	
	@Override
	public void checkSiPuedoGrabar(Parametro dto) throws BaseException {
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

	//********************************************************
	
	public Parametro getByCampo(String campo) {
		return parametroRepository.getByCampo(campo);
	}
	
	//Si solo queremos el valor
	public String getValorByCampo(String campo) {
		final Parametro p = parametroRepository.getByCampo(campo);
		return p==null? null : p.getValor();
	}
	
}
