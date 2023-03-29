package es.juntadeandalucia.ctpda.gestionpdt.service.core;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Pageable;
import es.juntadeandalucia.ctpda.gestionpdt.model.core.EntidadBasica;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.OrdenacionDTO;

/**
 * The Interface CrudQueryService.
 *
 * @param <E> the element type
 */
public interface CrudQueryService<E extends EntidadBasica> extends Serializable {

	/**
	 * Find all list.
	 *
	 * @param pageable   the pageable
	 * @param multiOrder the multi order
	 * @param filtros    the filtros
	 * @return the list
	 */
	List<E> findAll(Pageable pageable, OrdenacionDTO multiOrder, List<FiltroDTO> filtros);

	/**
	 * Obtener elemento por ID.
	 *
	 * @param idL the id l
	 * @return the d
	 */
	E obtener(Long idL);

	/**
	 * Obtener numero de elementos.
	 *
	 * @param multiOrder ordenación
	 * @param filtros    the filtros
	 * @return the long
	 */
	Long count(OrdenacionDTO multiOrder, List<FiltroDTO> filtros);

	/**
	 * Servicio para pequeños calculos matematicos, ideal para agrupaciones y
	 * footers de tablas.
	 *
	 * @param filtros  the filtros
	 * @param campo    the campo
	 * @param operador the operador
	 * @return Double
	 */
	Object calculadora(List<FiltroDTO> filtros, String campo, String operador);

}