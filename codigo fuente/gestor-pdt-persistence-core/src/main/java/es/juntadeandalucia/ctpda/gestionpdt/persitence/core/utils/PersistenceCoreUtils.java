package es.juntadeandalucia.ctpda.gestionpdt.persitence.core.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.data.domain.Sort.Direction;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.EntityPathBase;

import lombok.extern.slf4j.Slf4j;

/** The Constant log. */
@Slf4j
public class PersistenceCoreUtils {
	
	private PersistenceCoreUtils () {
		throw new IllegalStateException("Excepción en PersistenceCoreUtils");
	}


	/**
	 * Obtener ordenacion.
	 *
	 * @param base the base
	 * @param namedato the namedato
	 * @param direccion the direccion
	 * @return the order specifier
	 */
	public static OrderSpecifier<?> obtenerOrdenacion(EntityPathBase<?> base,String namedato,Direction direccion) {
		String[] ruta = namedato.split("\\.");
		
		Object invocado = base;
		try {
		for (String s : ruta) {
				Field field= invocado.getClass().getField(s);
				invocado = field.get(invocado);
		}
		Method ejecutor = invocado.getClass().getMethod(direccion.equals(Direction.DESC)?"desc":"asc");
		invocado = ejecutor.invoke(invocado);
		
		} catch (NoSuchFieldException | SecurityException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.error(e.getMessage());
		}
		return (OrderSpecifier) invocado;
	}
	
}
