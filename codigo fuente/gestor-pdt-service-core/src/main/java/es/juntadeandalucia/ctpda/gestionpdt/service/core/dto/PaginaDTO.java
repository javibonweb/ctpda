package es.juntadeandalucia.ctpda.gestionpdt.service.core.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PaginaDTO<T extends Serializable> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The total pages. */
	private int totalPages;

	/** The total elements. */
	private long totalElements;

	/** The number of elements. */
	private int numberOfElements;

	/** The size. */
	private int size;

	/** The number. */
	private int number;

	/** The sort property. */
	private String sortProperty;

	/** The sort ascending. */
	private boolean sortAscending;

	/** The content. */
	private List<T> content;

	/**
	 * Instantiates a new Pagina dto.
	 */
	public PaginaDTO() {
		content = new ArrayList<>();
	}
}
