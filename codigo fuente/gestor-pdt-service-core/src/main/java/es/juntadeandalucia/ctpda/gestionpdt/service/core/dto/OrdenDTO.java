package es.juntadeandalucia.ctpda.gestionpdt.service.core.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor

@NoArgsConstructor
public class OrdenDTO implements Serializable {

	/** serial id. */
	private static final long serialVersionUID = 1L;

	/** The campo. */
	private String campo;

	/** The orden. */
	private String orden;
}
