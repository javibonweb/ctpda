package es.juntadeandalucia.ctpda.gestionpdt.util;

import java.text.MessageFormat;
import java.util.HashMap;

public final class StringUtils {

	private static final String EXPRESION = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" ;
	
	private static final HashMap<Character, Character> sinAcento = new HashMap<>();
	
	private StringUtils () {
		throw new IllegalStateException("Excepción en StringUtils");
	}

	//Utilidad para eliminar los acentos de una cadena
	static {
		final char[] cars = "áéíóúÁÉÍÓÚàèìòùÀÈÌÒÙäëïöüÄËÏÖÜâêîôûÂÊÎÔÛ".toCharArray();
		final char[] carsSin = "aeiouAEIOU".toCharArray();

		final int tamSin = carsSin.length;
		for (int i = 0; i < cars.length; ++i) {
			final char c = cars[i];
			final char c_sin = carsSin[i % tamSin];
			sinAcento.put(c, c_sin); //Autoboxing
		}
	}

	public static char quitarAcento(char c) {
		final Character chsin = sinAcento.get(c);
		return (chsin == null) ? c : chsin.charValue();
	}

	public static String quitarAcentos(String str) {
		if(str==null) {
			return null;
		}

		final StringBuilder sb = new StringBuilder();

		for (int i = 0; i < str.length(); ++i) {
			sb.append(quitarAcento(str.charAt(i)));
		}

		return sb.toString();
	}

	//*******************************************************************
	// Métodos fachada

	public static final String EMPTY = org.apache.commons.lang3.StringUtils.EMPTY;
	public static final String SPACE = org.apache.commons.lang3.StringUtils.SPACE;
	public static final String COMA = ",";

	public static boolean isEmpty(String s) {
		return org.apache.commons.lang3.StringUtils.isEmpty(s);
	}

	public static boolean isBlank(String s) {
		return org.apache.commons.lang3.StringUtils.isBlank(s);
	}

	public static boolean isAllBlank(String... s) {
		return org.apache.commons.lang3.StringUtils.isAllBlank(s);
	}

	public static boolean isAnyBlank(String... s) {
		return org.apache.commons.lang3.StringUtils.isAnyBlank(s);
	}

	public static String deleteWhitespace(String s) {
		return org.apache.commons.lang3.StringUtils.deleteWhitespace(s);
	}

	public static String join(String[] ss) {
		return org.apache.commons.lang3.StringUtils.join(ss);
	}

	public static String join(String[] ss, String delim) {
		return org.apache.commons.lang3.StringUtils.join(ss, delim);
	}

	public static String left(String s, int n) {
		return org.apache.commons.lang3.StringUtils.left(s, n);
	}
	
	public static String defaultIfBlank(String s, String d) {
		return org.apache.commons.lang3.StringUtils.defaultIfBlank(s, d);
	}
	
	public static String substring(String str, int start, int end) {
		return org.apache.commons.lang3.StringUtils.substring(str, start, end);
	}
	
	public static String repeat(char c, int rep) {
		return org.apache.commons.lang3.StringUtils.repeat(c, rep);
	}
	
	public static String repeat(String str, int rep) {
		return org.apache.commons.lang3.StringUtils.repeat(str, rep);
	}
	
	public static boolean equalsIgnoreCase(String str1, String str2) {
		return org.apache.commons.lang3.StringUtils.equalsIgnoreCase(str1, str2);
	}
	
	//*******************************************************************
	//Otros métodos
	
	private static final Integer DEFAULT_PTO_SUSP_LENGTH=50;
	
	/** Aplica recorte por defecto a 50 caracteres (incluido "...") */
	public static String ptoSusp(String s) {
		return ptoSusp(s, DEFAULT_PTO_SUSP_LENGTH);
	}
	
	public static String ptoSusp(String str, Integer length) {
		final int MIN_LEN=5;
		
		if(length < MIN_LEN) {
			throw new IllegalArgumentException("El valor de longitud es demasiado pequeño (< " + MIN_LEN + ").");
		}
		
		final String PTO_SUSP="...";
		return (str.length() <= length)? str :  
			str.substring(0, length - PTO_SUSP.length()) + PTO_SUSP;
	}
	
	/** Alias de ptoSusp(String, Integer) para usarse como función JSF */
	public static String ptoSuspCustom(String s, Integer length) {
		return ptoSusp(s, length);
	}

	//*******************************************************************
	//Específicos de GEPD

	private static final int TAM_INIC_NULO=-1;

	public static String inic(String s) {
		return inic(s, TAM_INIC_NULO);
	}

	public static String inic(String s, int tam) {
		final String[] ss = s.split("\\s+|-");

		for(int i=0;i<ss.length;++i) {
			if(!ss[i].isEmpty()) {
				ss[i] = ss[i].substring(0,1);
			}
		}

		final String res = join(ss);
		return (tam >= 0)? left(res, tam) : res;
	}

    public static String getNombreAp(String nombre, String ap1, String ap2) {
    	final StringBuilder sb = new StringBuilder(ap1);
    	final String strComa = ","; 

    	if(!StringUtils.isEmpty(ap2)) {
    		sb.append(StringUtils.SPACE).append(ap2);
    	}

    	if(!StringUtils.isEmpty(nombre)) {
    		sb.append(strComa).append(StringUtils.SPACE).append(nombre);
    	}

    	return sb.toString();
    }
    
    //true si el email es vacío
    public static boolean esEmailValido(String email) {
    	boolean b = true;
    	
    	if(!isBlank(email)) {
	    	String regexPattern = 
	    			EXPRESION
	    	        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
	    	b = email.matches(regexPattern);   
    	}
    	
    	return b;
    }
    
	public static String resolverStr(String message, Object... params) {

        if (params == null || message == null) {
            return message;
        }

        MessageFormat messageFormat = new MessageFormat(message);
        return messageFormat.format(params);
    }

}
