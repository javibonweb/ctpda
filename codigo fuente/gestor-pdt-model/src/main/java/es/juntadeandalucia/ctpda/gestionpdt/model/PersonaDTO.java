package es.juntadeandalucia.ctpda.gestionpdt.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class PersonaDTO implements Serializable{

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private Long id;

	@Getter
	@Setter
	private String nombreRazonSocial;

}
