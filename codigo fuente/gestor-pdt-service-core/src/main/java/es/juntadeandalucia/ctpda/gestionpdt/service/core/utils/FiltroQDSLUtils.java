package es.juntadeandalucia.ctpda.gestionpdt.service.core.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.EntityPathBase;

import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.OrdenDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.OrdenacionDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.enums.DireccionEnum;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.enums.FiltroCondicionEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * The type Filtro qdsl utils.
 */

/** The Constant log. */
@Slf4j
public class FiltroQDSLUtils {

	/**
	 * The constant log.
	 */

	/** The Constant YEAR_METHOD. */
	public static final String YEAR_METHOD = ".year()";

	/** The Constant ENCONTRADA_LA_FECHA. */
	public static final String ENCONTRADA_LA_FECHA = "Encontrada la fecha!! --> ";

	/** The Constant MONTH_METHOD. */
	public static final String MONTH_METHOD = ".month()";

	/** The Constant DAY_OF_MONTH_METHOD. */
	public static final String DAY_OF_MONTH_METHOD = ".dayOfMonth()";
	
	private static final String CADENACARACTERES = "([0-9]{2}(/|-|.)[0-9]{2}(/|-|.)[0-9]{4})";
	
	
	private FiltroQDSLUtils () {
		throw new IllegalStateException("Excepción en FiltroQDSLUtils");
	}


	/**
	 * Obtener ordenacion pageable.
	 *
	 * @param pageable   the pageable
	 * @param multiOrder the multi order
	 * @return the pageable
	 */
	public static Pageable obtenerOrdenacion(Pageable pageable, OrdenacionDTO multiOrder) {
		List<Order> orders = new ArrayList<>();
		Pageable pageableResult;
		pageableResult = pageable;
		if (multiOrder != null && multiOrder.getListaOrden() != null && !multiOrder.getListaOrden().isEmpty()) {
			mapMultiOrderToOrders(multiOrder, orders);
			pageableResult = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(orders));
		}
		return pageableResult;
	}

	/**
	 * Obtener ordenacion sort.
	 *
	 * @param multiOrder the multi order
	 * @return the sort
	 */
	public static Sort obtenerOrdenacion(OrdenacionDTO multiOrder) {
		List<Order> orders = new ArrayList<>();
		if (multiOrder != null && multiOrder.getListaOrden() != null && !multiOrder.getListaOrden().isEmpty()) {
			mapMultiOrderToOrders(multiOrder, orders);
		}
		return Sort.by(orders);
	}

	/**
	 * Map multi order to orders.
	 *
	 * @param multiOrder the multi order
	 * @param orders     the orders
	 */
	private static void mapMultiOrderToOrders(OrdenacionDTO multiOrder, List<Order> orders) {
		for (OrdenDTO e : multiOrder.getListaOrden()) {
			Direction direccion;
			if (e.getOrden().equals(DireccionEnum.DESC.toString())) {
				direccion = Direction.DESC;
			} else {
				direccion = Direction.ASC;
			}
			String[] campos = e.getCampo().split(" ");
			for (String campo : campos) {
				orders.add(new Order(direccion, campo));
			}

		}
	}

	/**
	 * Obtener filtro boolean builder de una lista de filtros.
	 *
	 * @param base         the base
	 * @param listaFiltros the lista filtros
	 * @return the boolean builder
	 */
	public static BooleanBuilder obtenerFiltro(EntityPathBase<?> base, List<FiltroDTO> listaFiltros) {
		BooleanBuilder predi = new BooleanBuilder();
		if (listaFiltros != null && !listaFiltros.isEmpty()) {
			predi = obtenerFiltroAux(base,listaFiltros);
		}
		if (!predi.hasValue()) {
			predi = null;
		}

		return predi;
	}
	
	private static BooleanBuilder obtenerFiltroAux(EntityPathBase<?> base, List<FiltroDTO> listaFiltros) {
		BooleanBuilder predi = new BooleanBuilder();
		for (FiltroDTO filtro : listaFiltros) {
			try {
				// Input de texto libre de busqueda por varios campos
				if (!filtro.getCampo().trim().isEmpty()) {
					addExpresionElemento(predi, base, filtro, true);
				} else if (!filtro.getValue().toString().trim().isEmpty()) {
					BooleanBuilder prediGlobal = new BooleanBuilder();
					predi.andAnyOf(prediGlobal);
				}
			} catch (NoSuchFieldException | NoSuchMethodException | IllegalAccessException
					| InvocationTargetException e1) {
					String cond = filtro.getCondicion() == null ? "EQ" : filtro.getCondicion().toString();
					log.info("LA OPERACION " + cond + " NO ESTA SOPORTADA PARA EL TIPO DEL CAMPO "
							+ filtro.getCampo()); 
			}
		}
		return predi;
	}

	/**
	 * Obtener filtros or boolean builder.
	 *
	 * @param base        the base
	 * @param predicate   the predicate
	 * @param listFiltros the list filtros
	 * @return the boolean builder
	 */
	public static BooleanBuilder obtenerFiltrosOr(EntityPathBase<?> base, Predicate predicate,
			List<FiltroDTO> listFiltros) {
		BooleanBuilder predicateOr = new BooleanBuilder();
		BooleanBuilder result = new BooleanBuilder(predicate);
		for (FiltroDTO fil : listFiltros) {
			try {
				addExpresionElemento(predicateOr, base, fil, false);
			} catch (NoSuchFieldException | NoSuchMethodException | IllegalAccessException
					| InvocationTargetException e1) {
				log.debug(e1.getMessage());
				}
		}
		result.andAnyOf(predicateOr);
		return result;
	}

	/**
	 * Add expresion elemento.
	 *
	 * @param predicado the predicado
	 * @param base      the base
	 * @param filtro    the filtro
	 * @param and       the and
	 * @throws NoSuchFieldException      the no such field exception
	 * @throws NoSuchMethodException     the no such method exception
	 * @throws IllegalAccessException    the illegal access exception
	 * @throws InvocationTargetException the invocation target exception
	 */
	private static boolean multiples;
	private static BooleanBuilder mypredicado;
	private static boolean myand;
	
	private static void addExpresionElemento(BooleanBuilder predicado, EntityPathBase<?> base, FiltroDTO filtro,
			boolean and)
			throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		// Puede que un campo sea filtrado por varios distintos y estos vienen separados
		// por espacio
		List<String> filtrosMultiples = Arrays.asList(filtro.getCampo().split(" "));
		mypredicado = null;
		multiples = false;
		myand = and;
		if (filtrosMultiples.size() > 1) {
			myand = false;
			multiples = true;
			mypredicado = new BooleanBuilder();
		}

		addExpresionElementoAux(filtrosMultiples,base,filtro,predicado);

		if (multiples && mypredicado != null && mypredicado.hasValue()) {
			addToPredicate(predicado, mypredicado, and);
		}
	}
	
	private static void addExpresionElementoAux(List<String> filtrosMultiples,EntityPathBase<?> base,FiltroDTO filtro,BooleanBuilder predicado) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		for (String campo : filtrosMultiples) {
			try {
				// Prueba para comparar caracteres
				if (campo.contains("substring")) { // Incluir condicion nulo
					addExpresionElementoAux2(campo,base,filtro,predicado);
				} else {
					addExpresionElementoAux3(campo,base,filtro,predicado);
				}
			} catch (NumberFormatException | ParseException ex) {
				// ignoramos problemas de conversion, si has puesto algo q no es se obvia el
				// filtro.
				if (log.isDebugEnabled()) {
					log.debug(
							"Se ignora este filtro {" + filtro.getCampo() + "}  por problemas de conversion de campos");
					log.debug(String.valueOf(ex));
				}

			}
		}
	}
	
	private static void addExpresionElementoAux2(String campo,EntityPathBase<?> base,FiltroDTO filtro,BooleanBuilder predicado) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		String[] ruta = campo.split("\\.");
		Object invocado = base;
		for (String s : ruta) {
			if (s.contains("(")) {
				// metodo, se invoca
				Method mm = invocado.getClass().getMethod("substring", int.class, int.class);

				// Cogemos el indice donde estara la posicion deseada
				String numeroString = s.substring(10, 11);
				int numero = Integer.parseInt(numeroString);

				invocado = mm.invoke(invocado, numero, numero + 1);
			} else {
				Field field = invocado.getClass().getField(s);
				invocado = field.get(invocado);
			}
		}
		Method ejecutor = invocado.getClass().getMethod("equalsIgnoreCase", String.class);
		Predicate finall = (Predicate) ejecutor.invoke(invocado, filtro.getValue().toString());
		addToPredicate(multiples ? mypredicado : predicado, finall, myand);
	}
	
	private static void addExpresionElementoAux3(String campo,EntityPathBase<?> base,FiltroDTO filtro,BooleanBuilder predicado) throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, ParseException {
		Field fTipo = obtenerTipoDato(base, campo);//
		Object value = filtro.getValue();
		FiltroCondicionEnum condicion = filtro.getCondicion();
		// en funciopn del tipo , cargamos untipode filtro u otro.
		if (fTipo.getType().equals(Date.class) || fTipo.getType().equals(LocalDateTime.class)) {
			// por ahora nada. vamos a reconocer patrones
			addDateToPredicate(multiples ? mypredicado : predicado, base, filtro, myand);
		} else if (fTipo.getType().equals(Double.class)) {
			String modificado = filtro.getValue().toString().replace(",", ".");
			filtro.setValue(modificado);
			Predicate prx = obtenerPorReflexion(base, campo, value, condicion);
			addToPredicate(multiples ? mypredicado : predicado, prx, myand);
		} else {
			Predicate prx = obtenerPorReflexion(base, campo, value, condicion);
			addToPredicate(multiples ? mypredicado : predicado, prx, myand);
		}
	}

	/**
	 * Transformar dato.
	 *
	 * @param fTipo the f tipo
	 * @param campo the campo
	 * @return the object
	 */
	// forzamos el tipo de dato para evitar problemas de mapeo
	private static Object transformarDato(Field fTipo, Object campo) {
		if (campo == null) {
			return null;
		} else if (campo instanceof String) {
			if (fTipo.getType().equals(String.class)) {
				return campo.toString();
			} else if (fTipo.getType().equals(Long.class)) {
				return Long.parseLong(campo.toString());
			} else if (fTipo.getType().equals(Integer.class)) {
				return Integer.parseInt(campo.toString());
			} else if (fTipo.getType().equals(Double.class)) {
				return Double.parseDouble(campo.toString());
			} else if (fTipo.getType().equals(Float.class)) {
				return Float.parseFloat(campo.toString());
			} else {
				return campo;
			}
		} else {
			return campo;
		}
	}

	/**
	 * Obtener por reflexion predicate.
	 *
	 * @param base      the base
	 * @param campo     the campo
	 * @param valor     the valor
	 * @param condicion the condicion
	 * @return the predicate
	 * @throws NoSuchFieldException      the no such field exception
	 * @throws NoSuchMethodException     the no such method exception
	 * @throws IllegalAccessException    the illegal access exception
	 * @throws InvocationTargetException the invocation target exception
	 */
	@SuppressWarnings("rawtypes")
	public static Predicate obtenerPorReflexion(final EntityPathBase<?> base, final String campo, final Object valor,
			final FiltroCondicionEnum condicion)
			throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Field fTipo = obtenerTipoDato(base, campo);
		Object finall = null;
		if (valor instanceof Iterable) {
			// debemos iterar amigos!!!
			// usaremos recursividad.
			BooleanBuilder bb = new BooleanBuilder();
			for (Object simplevalor : (Iterable) valor) {
				bb.or(obtenerPorReflexion(base, campo, simplevalor, condicion));
			}
			finall = bb;
		} else {
			Object xvalor = transformarDato(fTipo, valor);
			String[] ruta = campo.split("\\.");
			Object invocado = base;
			for (String s : ruta) {
				if (s.contains("()")) {
					// metodo, se invoca
					Method mm = invocado.getClass().getMethod(s.replace("()", ""));
					invocado = mm.invoke(invocado);
				} else {
					Field field = invocado.getClass().getField(s);
					invocado = field.get(invocado);
				}
			}
			// En este punto deberemos tener el objeto hasta la condicion.
			// vamos a probar primero con los stringpath y numberpath

			// cuando terminamos, en funcion del tipo de campo, por reflexion tb sacamos si
			// es un eq o lo que sea.
			final Class<?> fTipoType = fTipo.getType();
			finall = obtenerPorReflexionAux(fTipoType,invocado,xvalor,condicion);
		}
		return (Predicate) finall;
	}
	
	private static Object obtenerPorReflexionAux (final Class<?> fTipoType,Object invocado,Object xvalor,final FiltroCondicionEnum condicion) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Object finall = null;
		if (fTipoType.equals(Boolean.class)) {
			finall = getBooleanPredicate((BooleanPath) invocado, xvalor);
		} else {
			if (xvalor == null) {
				Method ejecutor = invocado.getClass().getMethod("isNull");
				finall = ejecutor.invoke(invocado);
			} else {// stringValue()
				// En caso de Enum le añadimos stringValue para comparar por su valor String
				if (fTipoType.isEnum()) {
					Method ejecutor = invocado.getClass().getMethod("stringValue");
					invocado = ejecutor.invoke(invocado);
				}
				MethodInputType methodInputType = getMethodByCondicion(fTipoType, condicion);
				Class<? extends Object> clase = methodInputType.getMethodName().equals("eq")
						|| methodInputType.getMethodName().equals("like") ? Object.class
								: methodInputType.getInputType();
				Method ejecutor = invocado.getClass().getMethod(methodInputType.getMethodName(), clase);
				finall = ejecutor.invoke(invocado, xvalor);
			}

		}
		return finall;
	}

	/**
	 * Devuelve el método QueryDSL que equivale al parametro 'condicion' y su tipo
	 * Java.
	 *
	 * @param inputClass Clase del campo a filtrar
	 * @param condicion  tipo de condicion a aplicar en el filtro
	 * @return nombre del metodo y el tipo de parametro
	 */
	private static MethodInputType getMethodByCondicion(final Class<?> inputClass,
			final FiltroCondicionEnum condicion) {
		// Por defecto tipo Number
		MethodInputType methodInputType = MethodInputType.builder().inputType(Number.class).build();
		// Tratamos de forma especial las cadenas
		if (String.class.equals(inputClass)) {
			getStringMethodByCondicion(condicion, methodInputType);
			return methodInputType;
		} else if (LocalDateTime.class.equals(inputClass) || Date.class.equals(inputClass)) {
			if (condicion == null || FiltroCondicionEnum.EQ.equals(condicion)) {
				methodInputType.setInputType(Object.class);
			} else {
				methodInputType.setInputType(Number.class);
			}
		} else if (FiltroCondicionEnum.EQ.equals(condicion)) {
			methodInputType.setInputType(Object.class);
		}
		// Si no se especifica condicion --> 'eq' (equals)
		String methodName = condicion != null ? condicion.toString().toLowerCase()
				: FiltroCondicionEnum.EQ.toString().toLowerCase();
		methodInputType.setMethodName(methodName);
		return methodInputType;
	}

	/**
	 * Devuelve el método QueryDSL para tipo String que equivale al parametro
	 * 'condicion'.
	 *
	 * @param condicion       tipo de condicion a aplicar en el filtro
	 * @param methodInputType nombre del metodo y el tipo de parametro
	 * @return the string method by condicion
	 */
	private static void getStringMethodByCondicion(final FiltroCondicionEnum condicion,
			MethodInputType methodInputType) {
		methodInputType.setInputType(String.class);
		switch (condicion != null ? condicion : FiltroCondicionEnum.LIKE) {
		case EQ:
			methodInputType.setMethodName("equalsIgnoreCase");
			break;
		case LIKE:
		default:
			methodInputType.setMethodName("containsIgnoreCase");
			break;
		}
	}

	/**
	 * Gets the boolean predicate.
	 *
	 * @param base  the base
	 * @param campo the campo
	 * @param valor the valor
	 * @return the boolean predicate
	 * @throws NoSuchMethodException     the no such method exception
	 * @throws IllegalAccessException    the illegal access exception
	 * @throws InvocationTargetException the invocation target exception
	 */
	private static Predicate getBooleanPredicate(BooleanPath base, Object valor)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Object finall = null;
		if (valor instanceof Boolean) {
			Method ejecutor = base.getClass().getMethod("eq", Object.class);
			finall = ejecutor.invoke(base, valor);
		} else if (valor != null) { // optamos por el toString
			if (Boolean.TRUE.toString().equalsIgnoreCase(valor.toString().trim())) {
				Method ejecutor = base.getClass().getMethod("isTrue");
				finall = ejecutor.invoke(base);
			} else if (Boolean.FALSE.toString().equalsIgnoreCase(valor.toString().trim())) {
				Method ejecutor = base.getClass().getMethod("isFalse");
				finall = ejecutor.invoke(base);
			}
		}
		return (Predicate) finall;

	}

	/**
	 * Obtener tipo dato.
	 *
	 * @param base     the base
	 * @param namedato the namedato
	 * @return the field
	 * @throws NoSuchFieldException the no such field exception
	 */
	private static Field obtenerTipoDato(final EntityPathBase<?> base, String namedato) throws NoSuchFieldException {
		String[] ruta = namedato.split("\\.");
		EntityPathBase<?> deep = base;
		Field campo;
		try {
			campo = deep.getType().getDeclaredField(ruta[0]);
		} catch (NoSuchFieldException e) {
			log.debug(String.valueOf(e));
			campo = deep.getType().getSuperclass().getDeclaredField(ruta[0]);
		}
		for (int i = 1; i < ruta.length; i++) {
			if (ruta[i].contains("()")) {
				if (log.isDebugEnabled()) {
					log.debug("localizado funccion " + ruta[i] + " e ignorado");
				}
			} else if (campo.getType() == List.class) {
				ParameterizedType listType = (ParameterizedType) campo.getGenericType();
				Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0];
				try {
					campo = listClass.getDeclaredField(ruta[i]);
				} catch (NoSuchFieldException e) {
					log.debug(String.valueOf(e));
					campo = listClass.getSuperclass().getDeclaredField(ruta[i]);
				}
			} else {
				try {
					campo = campo.getType().getDeclaredField(ruta[i]);
				} catch (NoSuchFieldException e) {
					log.debug(String.valueOf(e));
					campo = campo.getType().getSuperclass().getDeclaredField(ruta[i]);
				}
			}
		}
		return campo;
	}

	/**
	 * Add to predicate.
	 *
	 * @param predicado the predicado
	 * @param pr        the pr
	 * @param and       the and
	 */
	private static void addToPredicate(BooleanBuilder predicado, Predicate pr, boolean and) {
		if (and) {
			predicado.and(pr);
		} else {
			predicado.or(pr);
		}
	}

	/**
	 * Add date to predicate.
	 *
	 * @param predicado the predicado
	 * @param base      the base
	 * @param filtro    the filtro
	 * @param and       the and
	 * @throws ParseException            the parse exception
	 * @throws NoSuchFieldException      the no such field exception
	 * @throws NoSuchMethodException     the no such method exception
	 * @throws IllegalAccessException    the illegal access exception
	 * @throws InvocationTargetException the invocation target exception
	 */
	private static void addDateToPredicate(BooleanBuilder predicado, EntityPathBase<?> base, FiltroDTO filtro,
			boolean and) throws ParseException, NoSuchFieldException, NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		// primero, ver si hay posibles fechas.
		final String campo = filtro.getCampo();
		final FiltroCondicionEnum condicion = filtro.getCondicion();
		if (filtro.getValue() instanceof Date) {
			addFechaExacta(base, (Date) filtro.getValue(), campo, predicado, and, condicion);

		} else {
			Date fecha = obtenerFecha(filtro.getValue().toString());
			if (fecha != null) {
				// fecha exacta, parseamos
				addFechaExacta(base, fecha, campo, predicado, and, condicion);
			} else {
				// buscamos un mes / anyo
				Matcher m = Pattern.compile("([0-9]{2}(/|-|//.)[0-9]{4})|([0-9]{1}(/|-|//.)[0-9]{4})")
						.matcher(filtro.getValue().toString());
				// DatePath<Date> path = Expressions.datePath(Date.class, base.getRoot(),
				BooleanBuilder bb = new BooleanBuilder();
				addDateToPredicateAux(bb,m,base,campo,condicion,predicado,and,filtro);
			}
		}
	}
	
	private static void addDateToPredicateAux (BooleanBuilder bb,Matcher m,EntityPathBase<?> base,final String campo,
				final FiltroCondicionEnum condicion,BooleanBuilder predicado,boolean and,FiltroDTO filtro) throws NumberFormatException, NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		if (m.find()) {
			if (log.isDebugEnabled()) {
				log.debug(ENCONTRADA_LA_FECHA + m.group(0) + "\n");
			}

			String[] fraccion = m.group(0).split("/");
			bb = builderFecha(base, campo, Integer.parseInt(fraccion[1]), Integer.parseInt(fraccion[0]), null,
					condicion);

			addToPredicate(predicado, bb, and);
		} else {
			// buscamos solo un anio
			Matcher mx = Pattern.compile("([0-9]{4})").matcher(filtro.getValue().toString());
			if (mx.find()) {
				if (log.isDebugEnabled()) {
					log.debug(ENCONTRADA_LA_FECHA + mx.group(0) + "\n");
				}
				Predicate prm = obtenerPorReflexion(base, campo + YEAR_METHOD, Integer.parseInt(mx.group(0)),
						condicion);
				bb.and(prm);
				addToPredicate(predicado, bb, and);
			}

		}
	}

	/**
	 * Obtener fecha.
	 *
	 * @param palabra the palabra
	 * @return the date
	 * @throws ParseException the parse exception
	 */
	private static Date obtenerFecha(String palabra) throws ParseException {
		// Se fija si hace match con algun formato de fecha conocido
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyy");
		Matcher m = Pattern.compile(CADENACARACTERES).matcher(palabra);
		if (m.find()) {
			if (log.isDebugEnabled()) {
				log.debug(ENCONTRADA_LA_FECHA + m.group(0) + "\n");
			}

			return formato.parse(m.group(0));
		}
		return null;
	}

	/**
	 * Add fecha exacta.
	 *
	 * @param base      the base
	 * @param fecha     the fecha
	 * @param campo     the campo
	 * @param predicado the predicado
	 * @param and       the and
	 * @param condicion the condicion
	 * @throws NoSuchFieldException      the no such field exception
	 * @throws NoSuchMethodException     the no such method exception
	 * @throws IllegalAccessException    the illegal access exception
	 * @throws InvocationTargetException the invocation target exception
	 */
	private static void addFechaExacta(EntityPathBase<?> base, Date fecha, final String campo, BooleanBuilder predicado,
			boolean and, FiltroCondicionEnum condicion)
			throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(fecha);

		BooleanBuilder bb = builderFecha(base, campo, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
				cal.get(Calendar.DAY_OF_MONTH), condicion);
		addToPredicate(predicado, bb, and);
	}

	/**
	 * Builder fecha.
	 *
	 * @param base      the base
	 * @param campo     the campo
	 * @param year      the year
	 * @param month     the month
	 * @param day       the day
	 * @param condicion the condicion
	 * @return the boolean builder
	 * @throws NoSuchFieldException      the no such field exception
	 * @throws NoSuchMethodException     the no such method exception
	 * @throws IllegalAccessException    the illegal access exception
	 * @throws InvocationTargetException the invocation target exception
	 */
	private static BooleanBuilder builderFecha(EntityPathBase<?> base, String campo, Integer year, Integer month,
			Integer day, FiltroCondicionEnum condicion)
			throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		BooleanBuilder bb = new BooleanBuilder();
		if (condicion == null || FiltroCondicionEnum.LIKE.equals(condicion)) {
			condicion = FiltroCondicionEnum.EQ;
		}
		if (FiltroCondicionEnum.EQ.equals(condicion)) {
			Predicate prm = obtenerPorReflexion(base, campo + MONTH_METHOD, month, condicion);
			Predicate pry = obtenerPorReflexion(base, campo + YEAR_METHOD, year, condicion);
			bb.and(prm);
			bb.and(pry);
			if (day != null) {
				Predicate prd = obtenerPorReflexion(base, campo + DAY_OF_MONTH_METHOD, day, condicion);
				bb.and(prd);
			}
		} else {
			FiltroCondicionEnum est;
			if (FiltroCondicionEnum.LOE.equals(condicion) || FiltroCondicionEnum.LT.equals(condicion)) {
				est = FiltroCondicionEnum.LT;
			} else {
				est = FiltroCondicionEnum.GT;
			}
			Predicate pry = obtenerPorReflexion(base, campo + YEAR_METHOD, year, est);
			Predicate eqy = obtenerPorReflexion(base, campo + YEAR_METHOD, year, FiltroCondicionEnum.EQ);
			Predicate prm;
			BooleanBuilder bb2 = new BooleanBuilder();
			if (day == null) {
				prm = obtenerPorReflexion(base, campo + MONTH_METHOD, month, condicion);
			} else {
				BooleanBuilder bb3 = new BooleanBuilder();
				Predicate prd = obtenerPorReflexion(base, campo + DAY_OF_MONTH_METHOD, day, condicion);
				prm = obtenerPorReflexion(base, campo + MONTH_METHOD, month, est);
				Predicate eqm = obtenerPorReflexion(base, campo + MONTH_METHOD, month, FiltroCondicionEnum.EQ);
				bb3.and(prd);
				bb3.and(eqm);
				bb3.and(eqy);
				bb.or(bb3);
			}
			bb.or(pry);
			bb2.and(prm);
			bb2.and(eqy);
			bb.or(bb2);
		}
		return bb;
	}

}
