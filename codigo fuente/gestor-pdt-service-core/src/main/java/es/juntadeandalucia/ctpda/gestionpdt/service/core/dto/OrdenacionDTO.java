package es.juntadeandalucia.ctpda.gestionpdt.service.core.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Ordenacion dto.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdenacionDTO implements Serializable {

	/** serial id. */
	private static final long serialVersionUID = 1L;

	/** The lista orden. */
	private List<OrdenDTO> listaOrden;

}
