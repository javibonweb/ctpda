package es.juntadeandalucia.ctpda.gestionpdt.service.core.enums;

import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

/**
 * The enum Filtro condicion enum.
 */

/** The Constant log. */
@Slf4j
public enum OperadorEnum {

	/** Máximo. max */
	MAXIMO("max"),

	/** Mínimo. min */
	MINIMO("min"),

	/** Valor absoluto. abs */
	ABSOLUTO("abs"),

	/** Media. avg */
	MEDIA("avg"),

	/** Suma. sum */
	SUMA("sum"),

	/** Raiz cuadrada. sqrt */
	RAIZCUADRADA("sqrt"),;

	/** The operador. */
	private final String operador;

	/**
	 * Instantiates a new OperadorEnum operador.
	 *
	 * @param operador the operador
	 */
	OperadorEnum(String operador) {
		this.operador = operador;
	}

	/**
	 * Gets the operador.
	 *
	 * @return the operador
	 */
	public String getOperador() {
		return operador;
	}

	/**
	 * Existe operador.
	 *
	 * @param operador the operador
	 * @return true, if successful
	 */
	public static boolean existeOperador(String operador) {
		Stream<OperadorEnum> lista = Stream.of(OperadorEnum.values());
		return lista.anyMatch(operator -> {
			log.debug(operator.toString());
			return operador.equalsIgnoreCase(operator.toString());
		});
	}
}
