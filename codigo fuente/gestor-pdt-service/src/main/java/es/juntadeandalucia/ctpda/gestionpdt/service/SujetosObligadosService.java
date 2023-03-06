package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;

import es.juntadeandalucia.ctpda.gestionpdt.model.QSujetosObligados;
import es.juntadeandalucia.ctpda.gestionpdt.model.SujetosObligados;
import es.juntadeandalucia.ctpda.gestionpdt.repository.SujetosObligadosRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SujetosObligadosService extends AbstractCRUDService<SujetosObligados> {

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	private SujetosObligadosRepository sujetosObligadosRepository;

	/**
	 * Constructor que inyecta bean de calculos matematicos y el resto de beans que
	 * hacen falta
	 */

	public SujetosObligadosService(@Autowired MathsQueryService mathsQueryService,
			@Autowired SujetosObligadosRepository sujetosObligadosRepository) {
		super(mathsQueryService, sujetosObligadosRepository, QSujetosObligados.sujetosObligados);
		// también lo guardo para mi por si quiero hacer consultas personalizadas.
		this.sujetosObligadosRepository = sujetosObligadosRepository;
	}

	@Override
	public void checkSiPuedoGrabar(SujetosObligados dto) throws BaseException {
		log.debug("Verifico si puedo grabar y elevo excepción sino");

	}

	@Override
	public void checkSiPuedoEliminar(Long id) throws BaseException {
		log.debug("Verifico si puedo eliminar y elevo excepción sino");

	}

	public List<SujetosObligados> findSujetosObligadosActivos() {
		return sujetosObligadosRepository.findSujetosObligadosActivos();
	}

	public List<SujetosObligados> findSujetosObligadosHijosActivos(Long id) {
		return sujetosObligadosRepository.findSujetosObligadosHijosActivos(id);
	}

	public SujetosObligados obtenerSujetosObligadosConDescripcion(String desc) {
		return sujetosObligadosRepository.obtenerSujetosObligadosConDescripcion(desc);
	}

	public SujetosObligados obtenerSujetoConMismoOrdenVisualizacion(String orden, long padreId, long tipAgrupId) {
		return sujetosObligadosRepository.obtenerSujetoConMismoOrdenVisualizacion(orden, padreId,tipAgrupId);
	}
	public SujetosObligados obtenerSujetoNullConMismoOrdenVisualizacion(String orden,long tipAgrupId) {
		return sujetosObligadosRepository.obtenerSujetoNullConMismoOrdenVisualizacion(orden, tipAgrupId);
	}
	
	public List<Long> findSujetosObligadosActivosConPadre() {
		return sujetosObligadosRepository.findSujetosObligadosActivosConPadre();
	}
	
	public List<SujetosObligados> findSujetosObligadosByTipIdentif(long idTipIdentif)
	{
		return sujetosObligadosRepository.findSujetosObligadosByTipIdentif(idTipIdentif);
	}
	
	public boolean existeNIFSujeto(SujetosObligados s) {
		NumberPath<Long> qId = QSujetosObligados.sujetosObligados.id;
			
		BooleanExpression idCheck = s.getId() == null? 
				qId.isNotNull() : qId.ne(s.getId());
		
		return this.crudRepository.exists(idCheck
				.and(QSujetosObligados.sujetosObligados.nif.eq(s.getNif()))
				.and(QSujetosObligados.sujetosObligados.activa.eq(true)));
	}

	@Override
	protected BooleanBuilder aniadirFiltrosCustom(List<FiltroDTO> filtros) {
		return new BooleanBuilder();
	}

	public SujetosObligados nuevoSujetoObligado() {
		SujetosObligados s = new SujetosObligados();
		s.setActiva(Boolean.TRUE);
		return null;
	}

	

}
