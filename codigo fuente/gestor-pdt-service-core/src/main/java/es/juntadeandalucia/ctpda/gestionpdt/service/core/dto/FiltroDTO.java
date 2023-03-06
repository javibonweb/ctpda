package es.juntadeandalucia.ctpda.gestionpdt.service.core.dto;

import java.io.Serializable;

import es.juntadeandalucia.ctpda.gestionpdt.service.core.enums.FiltroCondicionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FiltroDTO implements Serializable {

	/** serial id. */
	private static final long serialVersionUID = 1L;

	/** The campo. */
	private String campo;

	/** The value. */
	private transient Object value;

	/** The condicion. */
	private FiltroCondicionEnum condicion;

}
