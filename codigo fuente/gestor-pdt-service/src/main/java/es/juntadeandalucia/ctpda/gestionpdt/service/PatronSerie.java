package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.ValidacionException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
class PatronSerie {
	private static final int DIGITOSTAM=2; /** El tamaño tendrá 2 dígitos máximo (números de 0 a 99 caracteres) */
	private static final int CARACTERESMAX = 15; /** Máximo de caracteres definidos por los dígitos (no pueden ser 99); */ 
	/**Por ejemplo:
		RCE-{4}-2021: {4}  -> 1 dígito,  números de 0000 a 9999
		RCE-{99}-2021: {99} -> 2 dígitos, números de 00....0 a 99...9 (99 caracteres).
		RCE-{120}-2021: {120} -> 3 dígitos. No permitido por ser 3 > DIGITOS_TAM
	Limitamos a 15 (o el valor establecido en CARACTERES_MAX)
		RCE-{15}-2021: {15} -> 2 dígitos, números de 000000000000000 a 999999999999999
		RCE-{99}-2021: {99} -> 2 dígitos, números de 99 caracteres. No permitido por ser 99 > CARACTERES_MAX.
	*/ 
	
	//Asumimos una única variable por patrón, por lo que omitimos el nombre de esta y usamos solo el tamaño.
	private static Pattern p = Pattern.compile("^([^{]*)(\\{(\\d{1," + DIGITOSTAM + "})\\})(.*)$");
	/**
		Ejemplos de match groups:
		RCE-{4}-2021
			1.	RCE-
			2.	{4}
			3.	4
			4.	-2021
			
		{4}-2021
			1.	<vacío>
			2.	{4}
			3.	4
			4.	-2021
	 */
	private static final String ERRORSERIE = "Se produjo un error en la generación del número de serie. Consulte con su administrador.";
		
	private String prefijo;
	private String sufijo;
	private Integer tamanyo;
	
	PatronSerie(String s) throws BaseException{
		final Matcher m = p.matcher(s); //Ver excepciones
		
		if(!m.matches()) { //Llamando a matches() también se crean los grupos de m.
			log.error("El patrón de la serie no tiene un formato correcto.");
			throw new BaseException(ERRORSERIE, Collections.emptyList());
		}
		
		build(m);
	}
	
	private void build(Matcher m) throws BaseException{
		//Asumimos que el patrón proporciona 5 grupos
		prefijo = m.group(1);
		tamanyo = Integer.parseInt(m.group(3));
		
		if(tamanyo == 0){
			String msg = "El número de digitos del patrón debe estar entre 1 y " + "9".repeat(DIGITOSTAM);
			log.error(msg);
			throw new BaseException(msg, Collections.emptyList());
		} else if(tamanyo > CARACTERESMAX) {
			String msg = "Los " + DIGITOSTAM + " dígitos del patrón deben especificar un número de caracteres entre 1 y " + CARACTERESMAX;
			log.error(msg);
			throw new ValidacionException(msg, Collections.emptyList());
		}
		
		sufijo = m.group(4);
	}				

	//-------------------------------------
	
	String get(Long valor) throws BaseException{
		StringBuilder b = new StringBuilder();
		
		b.append(prefijo).append(valorToNum(valor)).append(sufijo);
		
		return b.toString();
	}
	
	private String valorToNum(Long valor) throws BaseException{	
		if(valor == null) { //Variable no puede ser null
			log.error("El valor de variable para resolver el patrón no puede ser nulo");
			throw new BaseException(ERRORSERIE, Collections.emptyList());
		}
	
		String tmp = "0".repeat(tamanyo) + valor.toString();
		return tmp.substring(tmp.length() - tamanyo);
	}	

}

