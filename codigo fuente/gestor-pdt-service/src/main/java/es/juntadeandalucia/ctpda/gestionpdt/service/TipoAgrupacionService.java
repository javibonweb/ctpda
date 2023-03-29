package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.QTipoAgrupacion;
import es.juntadeandalucia.ctpda.gestionpdt.model.TipoAgrupacion;
import es.juntadeandalucia.ctpda.gestionpdt.repository.TipoAgrupacionRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TipoAgrupacionService extends AbstractCRUDService<TipoAgrupacion>{
	
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	private TipoAgrupacionRepository tipoAgrupacionRepository;
	
	/**
	 * Constructor que inyecta bean de calculos matematicos y el resto de beans que hacen falta
	 * */
	public TipoAgrupacionService(@Autowired MathsQueryService mathsQueryService, @Autowired TipoAgrupacionRepository tipoAgrupacionRepository){
		super(mathsQueryService,
				tipoAgrupacionRepository,
				QTipoAgrupacion.tipoAgrupacion);
		//también lo guardo para mi por si quiero hacer consultas personalizadas.
		this.tipoAgrupacionRepository=tipoAgrupacionRepository;
	}
	
	public TipoAgrupacion nuevoTipoAgrupacion() {
		TipoAgrupacion ta = new TipoAgrupacion();
		
		ta.setActiva(Boolean.TRUE);
		//No se tienen en cuenta sexo, tipo, etc.
		
		return ta;
	}

	
	@Override
	public void checkSiPuedoGrabar(TipoAgrupacion dto) throws BaseException {
		log.debug("Verifico si puedo grabar y elevo excepción sino");
		
	}

	@Override
	public void checkSiPuedoEliminar(Long id) throws BaseException {
		log.debug("Verifico si puedo eliminar y elevo excepción sino");		
	}
	
	public List<TipoAgrupacion> findTiposAgrupacionActivas() {
		return tipoAgrupacionRepository.findTiposAgrupacionActivas();	
	}

	public List<TipoAgrupacion> findTiposAgrupacionesHijasActivas(Long id) {
		return tipoAgrupacionRepository.findTiposAgrupacionesHijasActivas(id);	
	}
	
	public TipoAgrupacion obtenerTipoAgrupacionConDescripcion(String desc) {
		return tipoAgrupacionRepository.obtenerTipoAgrupacionConDescripcion(desc);	
	}
	public List<TipoAgrupacion> findTiposAgrupacionesActivaSinAnidamiento(){
		return tipoAgrupacionRepository.findTiposAgrupacionesActivaSinAnidamiento();
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
