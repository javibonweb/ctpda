package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.ArticuloAfectadoResolucion;
import es.juntadeandalucia.ctpda.gestionpdt.model.DerechoReclamadoResolucion;
import es.juntadeandalucia.ctpda.gestionpdt.model.QResolucion;
import es.juntadeandalucia.ctpda.gestionpdt.model.Resolucion;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.repository.ResolucionRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ResolucionService extends AbstractCRUDService<Resolucion> {

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private DerechoReclamadoResolucionService derechoReclamadoResolucionService;
	@Autowired
	private ArticuloAfectadoResolucionService articuloAfectadoResolucionService;

	private ResolucionRepository resolucionRepository;
	

	protected ResolucionService(@Autowired MathsQueryService mathsQueryService,
			@Autowired ResolucionRepository resolucionRepository) {
		super(mathsQueryService, resolucionRepository, QResolucion.resolucion);
		// también lo guardo para mi por si quiero hacer consultas personalizadas.
		this.resolucionRepository = resolucionRepository;

		this.joinBuilder = query -> 
			query
		;

	}
	
	public Resolucion findResolucionByNumeroResolucion(String numeroResolucion){
		return resolucionRepository.findResolucionByNumeroResolucion(numeroResolucion);
	}

	@Override
	public void checkSiPuedoGrabar(Resolucion dto) throws BaseException {
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
	
	//***************************************
		
	@Override
	@Transactional(TxType.REQUIRED)
	public Resolucion guardar(Resolucion resol) throws BaseException  {
		guardarDerechosResol(resol);
		guardarArticulosResol(resol);
		
		return super.guardar(resol);
	}
	
	private void guardarDerechosResol(Resolucion resol) throws BaseException {
		List<DerechoReclamadoResolucion> actuales = derechoReclamadoResolucionService.findByResolucionId(resol.getId());
		
		for(DerechoReclamadoResolucion derechoResol : actuales) {
			derechoReclamadoResolucionService.delete(derechoResol.getId());
		}	

		List<ValoresDominio> derechos = resol.getDerechosReclamados();
		if(derechos != null) {
			for(ValoresDominio valorDerecho : derechos) {
				DerechoReclamadoResolucion derechoResol = new DerechoReclamadoResolucion();
				derechoResol.setResolucion(resol);
				derechoResol.setValoresDerReclResol(valorDerecho);
				derechoReclamadoResolucionService.guardar(derechoResol);
			}
		}
	}
	
	private void guardarArticulosResol(Resolucion resol) throws BaseException {
		List<ArticuloAfectadoResolucion> actuales = articuloAfectadoResolucionService.findByResolucionId(resol.getId());
		
		for(ArticuloAfectadoResolucion derechoResol : actuales) {
			articuloAfectadoResolucionService.delete(derechoResol.getId());
		}

		List<ValoresDominio> articulos = resol.getArticulosAfectados();
		if(articulos != null) {
			for(ValoresDominio valorArticulo : articulos) {
				ArticuloAfectadoResolucion articuloResol = new ArticuloAfectadoResolucion();
				articuloResol.setResolucion(resol);
				articuloResol.setValorArticulo(valorArticulo);
				articuloAfectadoResolucionService.guardar(articuloResol);
			}
		}
	}
	
}
