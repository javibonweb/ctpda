package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.QTipoTramite_DMS;
import es.juntadeandalucia.ctpda.gestionpdt.model.TipoTramite_DMS;
import es.juntadeandalucia.ctpda.gestionpdt.repository.TipoTramiteRepository_DMS;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TipoTramiteService_DMS extends AbstractCRUDService< TipoTramite_DMS>{
	

	private static final long serialVersionUID = 1L;
	
	private TipoTramiteRepository_DMS tipoTramiteRepository_DMS;
	

	public TipoTramiteService_DMS(@Autowired MathsQueryService mathsQueryService, @Autowired TipoTramiteRepository_DMS tipoTramiteRepository_DMS){
		super(mathsQueryService,
				tipoTramiteRepository_DMS,
				QTipoTramite_DMS.tipoTramite_DMS);

		this.tipoTramiteRepository_DMS=tipoTramiteRepository_DMS;
	}
	
	public TipoTramite_DMS findTipoTramiteActivoNotif(){
		return tipoTramiteRepository_DMS.findTipoTramiteActivoNotif();
	}
	
	public TipoTramite_DMS findTipoTramiteActivoFirm(){
		return tipoTramiteRepository_DMS.findTipoTramiteActivoFirm();
	}

	public List<TipoTramite_DMS> findByComportamiento(String comp){
		return tipoTramiteRepository_DMS.findByComportamiento(comp);
	}
	
	
	public List<TipoTramite_DMS> findTipoTramitesActivos(){
		return tipoTramiteRepository_DMS.findTipoTramitesActivos();
	}
	
	public List<TipoTramite_DMS> findTipoTramitesActivosOrdenAlfab(){
		return tipoTramiteRepository_DMS.findTipoTramitesActivosOrdenAlfab();
	}
	
	@Override
	public void checkSiPuedoGrabar(TipoTramite_DMS dto) throws BaseException {
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

	public TipoTramite_DMS getByCodigo(String codigo) {
		return this.tipoTramiteRepository_DMS.getByCodigo(codigo);
	}
}