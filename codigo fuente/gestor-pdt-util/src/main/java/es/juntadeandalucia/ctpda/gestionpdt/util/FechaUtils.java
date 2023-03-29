package es.juntadeandalucia.ctpda.gestionpdt.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FechaUtils {

	private static final String DATE_FORMAT = "dd/MM/yyyy HH:mm";
	
	private static final LocalDateTime FECHAZERO = Instant.ofEpochMilli( 0L )
													.atOffset(ZoneOffset.UTC)
													.toLocalDate().atStartOfDay();
	
	private static final ZoneId zoneIdMadrid =  ZoneId.of( "Europe/Madrid" ) ;
	
	private FechaUtils () {
		throw new IllegalStateException("Excepción en FechaUtils");
	}


	public static LocalDate toLocalDate(Date fecha) {
		return (fecha instanceof java.sql.Date)?
				((java.sql.Date)fecha).toLocalDate()
				: fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static Date hora0(Date d) {
		return toDate(toLocalDate(d).atStartOfDay()); 
	}

	public static Date toDate(LocalDateTime ld) {
	    return java.util.Date.from(ld 
	    	      .atZone(ZoneId.systemDefault())
	    	      .toInstant()
	    	      );
	}

	public static Date toDate(LocalDate ld) {
	    return toDate(ld.atStartOfDay());
	}
	
	
	public static Date fechaYHoraActualDate() {
		Date dateReturn = new Date();
		try {
			Calendar cal = Calendar.getInstance();
	        Date date = cal.getTime();
	        
			TimeZone timeZone = TimeZone.getTimeZone("Europe/Madrid");
	        SimpleDateFormat formatterWithTimeZone = new SimpleDateFormat(DATE_FORMAT);
	        formatterWithTimeZone.setTimeZone(timeZone);
	
	        String sDate = formatterWithTimeZone.format(date);
	
	        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
	        
	        dateReturn = formatter.parse(sDate);
		} catch (ParseException e) {
			log.error(e.getMessage());
		}
		return dateReturn;
	}

	//Fecha de hoy, hora actual
	public static Date ahora() {
		// Or get the JVM’s current default time zone: ZoneId.systemDefault() 
	    return toDate(ZonedDateTime.now(zoneIdMadrid).toLocalDateTime());
	}

	//Fecha de hoy, a las 00:00
	public static Date hoy() {
	    return toDate(ZonedDateTime.now(zoneIdMadrid).toLocalDate().atStartOfDay());
	}

	public static boolean antes(Date d1, Date d2) {
		if(d1 == null || d2 == null) {
			return false;
		}
		
		final LocalDate ld1 = toLocalDate(d1);
		final LocalDate ld2 = toLocalDate(d2);

		return ld1.isBefore(ld2);
	}

	public static boolean despues(Date d1, Date d2) {
		if(d1 == null || d2 == null) {
			return false;
		}

		final LocalDate ld1 = toLocalDate(d1);
		final LocalDate ld2 = toLocalDate(d2);

		return ld1.isAfter(ld2);
	}

	public static Date diaDespues(Date d) {
		return toDate(toLocalDate(d).plusDays(1));
	}

	public static boolean entre(Date d, Date d1, Date d2) {
		final LocalDate ld = toLocalDate(d);
		final LocalDate ld1 = toLocalDate(d1);
		final LocalDate ld2 = toLocalDate(d2);

		return !ld.isBefore(ld1) && !ld.isAfter(ld2);
	}

	/**
	 * Número de días transcurridos desde la fecha 0 (típicamente 1/1/1970) hasta [fecha]
	 */
	public static int dias(Date fecha) {
		if(fecha==null) {
			return 0;
		}

		final LocalDateTime fFin = toLocalDate(fecha).atStartOfDay();
		return (int)Duration.between(FECHAZERO, fFin).toDays();
	}

	public static int diasEntre(Date fecha1, Date fecha2) {
		if(fecha1 == null || fecha2 == null) {
			return 0;
		}

		final LocalDateTime f1 = toLocalDate(fecha1).atStartOfDay();
		final LocalDateTime f2 = toLocalDate(fecha2).atStartOfDay();
		return (int)Duration.between(f1, f2).toDays();
	}

	public static Date diastoDate(Long dias) {
		final LocalDate res = (dias==null || dias == 0)? FECHAZERO.toLocalDate() : FECHAZERO.toLocalDate().plusDays(dias);

		return toDate(res);
	}

	public static Date min(Date d1, Date d2) {
		if(d1 == null) {
			return d2;
		}
		if(d2 == null) {
			return d1;
		}
		return d1.before(d2)? d1 : d2;
	}

	public static Date max(Date d1, Date d2) {
		if(d1 == null) {
			return d2;
		}
		if(d2 == null) {
			return d1;
		}
		return d1.after(d2)? d1 : d2;
	}
	
	public static Date sumarDiasAFecha(Date fecha, Integer dias)
	{
		Date fechaResult = null;
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha); // Configuramos la fecha que se recibe
		calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de días a añadir, o restar en caso de días<0
		fechaResult = calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos
		
		
		return fechaResult;		
	}

	/**
	 * Transformamos una fecha a String en formato corto.
	 *
	 * @param fecha
	 * @return
	 */
	public static String dateToStringFecha(final Date fecha) {
		return dateToString(fecha, "dd/MM/YYYY");
	}
	
	public static String dateToStringFechaYHora(final Date fecha) {
		return dateToString(fecha, "dd/MM/yyyy  HH:mm");
	}
	
	public static String stringFechaYHora() {
		return dateToStringFechaYHora(ahora());
	}

	public static String dateToString(final Date fecha, String formato) {
		String res = "";
		try {
			if (fecha != null) {
				final SimpleDateFormat sdf = new SimpleDateFormat(formato);
				res = sdf.format(fecha);
			}
		} catch (final Exception e) {
			res = "### Error al convertir la fecha " + fecha;
		}
		return res;
	}
	
	public static String dateToStringFechaYHoraCompleta(final Date fecha) {
		return dateToString(fecha, "ddMMyyyyHHmmss");
	}	
	
	public static String stringFechaYHoraCompleta() {
		return dateToStringFechaYHoraCompleta(ahora());
	}

}
