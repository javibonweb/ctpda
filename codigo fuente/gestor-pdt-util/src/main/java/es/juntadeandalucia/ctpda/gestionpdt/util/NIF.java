package es.juntadeandalucia.ctpda.gestionpdt.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class NIF {

	public static final String FORMATO_NIF="^(\\d{8})[TRWAGMYFPDXBNJZSQVHLCKEtrwagmyfpdxbnjzsqvhlcke]$";
	public static final String FORMATO_NIE="^[a-zA-Z](\\d{7})[TRWAGMYFPDXBNJZSQVHLCKEtrwagmyfpdxbnjzsqvhlcke]$";
	public static final String FORMATO_NIF_NIE_LETRAS="TRWAGMYFPDXBNJZSQVHLCKE";
	public static final String FORMATO_CIF="^[ABCDEFGHJKLMNPQRSUVWabcdefghjklmnpqrsuvw][0-9]{7}[A-Ja-j[0-9]]{1}$";
	
	private static final Pattern cifPattern = Pattern.compile(FORMATO_CIF);
	
	private NIF () {
		throw new IllegalStateException("Excepción en NIF");
	}

	public static String normalizar(String nif) {
		final StringBuilder sb = new StringBuilder();

		if(nif != null) {
			for(int i=0; i < nif.length(); ++i) {
				final char c = nif.charAt(i);
				if(Character.isLetterOrDigit(c)) {
					sb.append(c);
				}
			}
		}

		return sb.toString();
	}


	/**
	 * metodo que comprueba si el parametros intruducido es NIF o
	 * NIE.
	 *
	 * @param nif the nif
	 * @return true, if successful
	 */
	public static boolean esNIFoNIEValido(String nif) {
		if (!StringUtils.isEmpty(nif)) {
			if (nif.toUpperCase().startsWith("X")
					|| nif.toUpperCase().startsWith("Y")
					|| nif.toUpperCase().startsWith("Z")) {
				return (esNIEValido(nif));
			} else {
				return (esNIFValido(nif));
			}
		} else {
			return false;
		}
	}

	/**
	 * metodo que comprueba si el parametro intruducido es NIE.
	 *
	 * @param entrada the entrada
	 * @return true, if successful
	 */
	public static boolean esNIEValido(String entrada) {
		String nie = entrada;

		final Pattern niePattern = Pattern.compile(FORMATO_NIE);
		final Matcher m = niePattern.matcher(nie);
		boolean res = false;
		if (m.matches()) {
			if (nie.toUpperCase().startsWith("X")) {
				nie = nie.replaceFirst("X", "0");
			} else if (nie.toUpperCase().startsWith("Y")) {
				nie = nie.replaceFirst("Y", "1");
			} else if (nie.toUpperCase().startsWith("Z")) {
				nie = nie.replaceFirst("Z", "2");
			}

			final String letra = nie.substring(nie.length() - 1);
			// Extraer letra del NIF
			final String letras = FORMATO_NIF_NIE_LETRAS;
			int dni = Integer.parseInt(nie.substring(0, nie.length() - 1));
			dni = (dni % 23);
			final String reference = letras.substring(dni, dni + 1);

			if (reference.equalsIgnoreCase(letra)) {
				res = true;
			}
		}
		return res;
	}

	/**
	 * Metodo que comprueba si el parametro intruducido es NIF.
	 *
	 * @param entrada the entrada
	 * @return true, if successful
	 */
	public static boolean esNIFValido(String entrada) {
		if (entrada.length() < 9) {
			/*El while tiene una alerta de Sonar que se debería solucionar con 
			 String ceros = StringUtils.repeat('0', 9-entrada.length())
			entrada = ceros + entrada*/
			
			while (entrada.length() < 9) {
				entrada = "0" + entrada;
			}
		}
		
		final String nif = entrada;

		final Pattern nifPattern = Pattern.compile(FORMATO_NIF);
		final Matcher m = nifPattern.matcher(nif);
		boolean res = false;
		if (m.matches()) {
			final String letra = nif.substring(nif.length() - 1);
			// Extraer letra del NIF
			final String letras = FORMATO_NIF_NIE_LETRAS;
			int dni = Integer.parseInt(nif.substring(0, nif.length() - 1));
			dni = dni % 23;
			final String reference = letras.substring(dni, dni + 1);

			if (reference.equalsIgnoreCase(letra)) {
				res = true;
			}
		}
		return res;
	}

	/**
	 * Calcula letra NIF.
	 *
	 * @param dni the dni
	 * @return the string
	 */
	public static final String calculaLetraNIF(String dni){
		// Calculamos la letra de control
		final int posicion_modulo = Integer.parseInt(dni) % 23;
		return FORMATO_NIF_NIE_LETRAS.substring(posicion_modulo,posicion_modulo + 1);

	}

	/**
	 * The Constant CONTROL_SOLO_NUMEROS.
	 */
	private static final String CONTROL_SOLO_NUMEROS = "ABEH"; // S�lo admiten n�meros como caracter de control

	/**
	 * The Constant CONTROL_SOLO_LETRAS.
	 */
	private static final String CONTROL_SOLO_LETRAS = "KPQS"; // S�lo admiten letras como caracter de control

	/**
	 * The Constant CONTROL_NUMERO_A_LETRA.
	 */
	private static final String CONTROL_NUMERO_A_LETRA = "JABCDEFGHI"; // Conversi�n de d�gito a letra de control.

	/**
	 * Es CIF.
	 *
	 * @param cif the cif
	 * @return true, if successful
	 */
	public static boolean esCIFValido(String cif) {
		try {
			if (!cifPattern.matcher(cif).matches()) {
				// No cumple el patrón
				return false;
			}
			int parA = 0;
			for (int i = 2; i < 8; i += 2) {
				final int digito = Character.digit(cif.charAt(i), 10);
				if (digito < 0) {
					return false;
				}
				parA += digito;
			}
			int nonB = 0;
			for (int i = 1; i < 9; i += 2) {
				final int digito = Character.digit(cif.charAt(i), 10);
				if (digito < 0) {
					return false;
				}
				int nn = 2 * digito;
				if (nn > 9) {
					nn = 1 + (nn - 10);
				}
				nonB += nn;
			}
			final int parcialC = parA + nonB;
			final int digitoE = parcialC % 10;
			final int digitoD = (digitoE > 0) ?
					(10 - digitoE) :
						0;
					final char letraIni = Character.toUpperCase(cif.charAt(0));
					final char caracterFin = Character.toUpperCase(cif.charAt(8));
					return 
							// ¿el caracter de control es válido como letra?
							(CONTROL_SOLO_NUMEROS.indexOf(letraIni) < 0
								&& CONTROL_NUMERO_A_LETRA.charAt(digitoD) == caracterFin)
							||
							// ¿el caracter de control es válido como dígito?
							(CONTROL_SOLO_LETRAS.indexOf(letraIni) < 0
								&& digitoD == Character.digit(caracterFin, 10));
		} catch (final Exception e) {
			return false;
		}
	}
	
	public static Boolean esIdentificadorValido(String tipo, String valor) {
		final boolean valido;
		
		if("NIF".equals(tipo)){
		   valido = esNIFValido(valor);
		} else if("CIF".equals(tipo)){
		   valido = esCIFValido(valor);
		} else if("NIE".equals(tipo)){
		   valido = esNIEValido(valor);
		} else {
			valido = true;
		}
				
		return valido;
	}
	
}
