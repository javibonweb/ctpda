package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.ArticuloAfectadoResolucion;
import es.juntadeandalucia.ctpda.gestionpdt.model.QArticuloAfectadoResolucion;
import es.juntadeandalucia.ctpda.gestionpdt.model.QValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.repository.ArticuloAfectadoResolucionRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ArticuloAfectadoResolucionService extends AbstractCRUDService<ArticuloAfectadoResolucion> {

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	private ArticuloAfectadoResolucionRepository articuloAfectadoResolucionRepository;

	protected ArticuloAfectadoResolucionService(@Autowired MathsQueryService mathsQueryService,
			@Autowired ArticuloAfectadoResolucionRepository articuloAfectadoResolucionRepository) {
		super(mathsQueryService, articuloAfectadoResolucionRepository, QArticuloAfectadoResolucion.articuloAfectadoResolucion);
		// también lo guardo para mi por si quiero hacer consultas personalizadas.
		this.articuloAfectadoResolucionRepository = articuloAfectadoResolucionRepository;

		this.joinBuilder = query -> {
			final QArticuloAfectadoResolucion qArtAfecResol = QArticuloAfectadoResolucion.articuloAfectadoResolucion;

			final QValoresDominio qValDomArticulo = new QValoresDominio("qValDomArticulo");
			query.innerJoin(qArtAfecResol.valorArticulo, qValDomArticulo).fetchJoin();
			
			return query;
		};

	}
	
	public ArticuloAfectadoResolucion findArtAfecResolucionExpByIdResolIdArtAdfec(Long idResol, Long idArtAfec)
	{
		return articuloAfectadoResolucionRepository.findArtAfecResolucionExpByIdResolIdArtAdfec(idResol, idArtAfec);
	}

	@Override
	public void checkSiPuedoGrabar(ArticuloAfectadoResolucion dto) throws BaseException {
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

	//****************************
	
	public List<ArticuloAfectadoResolucion> findByResolucionId(Long idResol){
		return this.articuloAfectadoResolucionRepository.findByResolucionId(idResol);
	}
	
}
