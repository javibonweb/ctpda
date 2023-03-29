package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.QTipoTramite;
import es.juntadeandalucia.ctpda.gestionpdt.model.TipoTramite;
import es.juntadeandalucia.ctpda.gestionpdt.repository.TipoTramiteRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TipoTramiteService extends AbstractCRUDService< TipoTramite>{
	
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	private TipoTramiteRepository tipoTramiteRepository;
	
	/**
	 * Constructor que inyecta bean de calculos matematicos y el resto de beans que hacen falta
	 * */
	public TipoTramiteService(@Autowired MathsQueryService mathsQueryService, @Autowired TipoTramiteRepository tipoTramiteRepository){
		super(mathsQueryService,
				tipoTramiteRepository,
				QTipoTramite.tipoTramite);
		//también lo guardo para mi por si quiero hacer consultas personalizadas.
		this.tipoTramiteRepository=tipoTramiteRepository;
	}
	
	public TipoTramite findTipoTramiteActivoNotif(){
		return tipoTramiteRepository.findTipoTramiteActivoNotif();
	}
	
	public TipoTramite findTipoTramiteActivoFirm(){
		return tipoTramiteRepository.findTipoTramiteActivoFirm();
	}

	public List<TipoTramite> findByComportamiento(String comp){
		return tipoTramiteRepository.findByComportamiento(comp);
	}
	
	
	public List<TipoTramite> findTipoTramitesActivos(){
		return tipoTramiteRepository.findTipoTramitesActivos();
	}
	
	public List<TipoTramite> findTipoTramitesActivosOrdenAlfab(){
		return tipoTramiteRepository.findTipoTramitesActivosOrdenAlfab();
	}
	
	@Override
	public void checkSiPuedoGrabar(TipoTramite dto) throws BaseException {
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

	public TipoTramite getByCodigo(String codigo) {
		return this.tipoTramiteRepository.getByCodigo(codigo);
	}
}
