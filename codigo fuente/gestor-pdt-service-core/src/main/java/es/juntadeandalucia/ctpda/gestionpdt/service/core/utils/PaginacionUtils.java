package es.juntadeandalucia.ctpda.gestionpdt.service.core.utils;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.PaginaDTO;

/**
 * The Class PaginacionUtils.
 */
public class PaginacionUtils {
	
	
	private PaginacionUtils () {
		throw new IllegalStateException("Excepción en PaginacionUtils");
	}

	/**
	 * As pagina dto pagina dto.
	 *
	 * @param <D>    the generic type
	 * @param pagina the pagina
	 * @param dtos   the dtos
	 * @return the pagina dto
	 */
	public static <D extends Serializable> PaginaDTO<D> asPaginaDTO(Page<D> pagina, List<D> dtos) {
		final PaginaDTO<D> dto = new PaginaDTO<>();
		setContenidoPaginaDTO(pagina, dto, dtos);
		dto.setNumber(pagina.getNumber());
		dto.setNumberOfElements(pagina.getNumberOfElements());
		dto.setSize(pagina.getSize());
		setOrdenPaginaDTO(pagina, dto);
		dto.setTotalElements(pagina.getTotalElements());
		dto.setTotalPages(pagina.getTotalPages());
		return dto;
	}

	/**
	 * Set orden pagina DTO.
	 *
	 * @param <D>       the generic type
	 * @param page      the page
	 * @param paginaDto the pagina dto
	 */
	private static <D extends Serializable> void setOrdenPaginaDTO(Page<D> page, PaginaDTO<D> paginaDto) {
		final Sort ordenPagina = page.getSort();
		final Iterator<Order> orders = ordenPagina.iterator();
		if (orders.hasNext()) {
			final Order orden = orders.next();
			paginaDto.setSortProperty(orden.getProperty());
			paginaDto.setSortAscending(orden.isAscending());
		}
	}

	/**
	 * Set contenido pagina DTO.
	 *
	 * @param <D>       the generic type
	 * @param page      the page
	 * @param paginaDto the pagina dto
	 * @param dtos      the dtos
	 */
	private static <D extends Serializable> void setContenidoPaginaDTO(Page<D> page, PaginaDTO<D> paginaDto,
			List<D> dtos) {
		for (int i = 0; i < page.getContent().size(); i++) {
			paginaDto.getContent().add(dtos.get(i));
		}
	}

	/**
	 * To list.
	 *
	 * @param <D>  the generic type
	 * @param data the data
	 * @return the list
	 */
	public static <D extends Serializable> List<D> toList(Iterable<D> data) {
		final List<D> returno = new LinkedList<>();
		data.forEach(returno::add);
		return returno;
	}

}
