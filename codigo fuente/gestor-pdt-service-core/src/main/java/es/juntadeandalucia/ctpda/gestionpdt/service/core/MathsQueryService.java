package es.juntadeandalucia.ctpda.gestionpdt.service.core;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQuery;

import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.enums.OperadorEnum;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.utils.FiltroQDSLUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase para ejecutar pequeñas operaciones matematicas.
 */
@Service
@Transactional

/** The Constant log. */
@Slf4j
public class MathsQueryService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	/** The entity manager. */
	@PersistenceContext

	/**
	 * Sets the entity manager.
	 *
	 * @param entityManager the new entity manager
	 */
	@Setter
	private transient EntityManager entityManager;

	/**
	 * Calculadora custom.
	 *
	 * @param filtros  the filtros
	 * @param campo    the campo
	 * @param operador the operador
	 * @param base     the base
	 * @return Double
	 */
	public Object calculadoraCustom(List<FiltroDTO> filtros, String campo, String operador, EntityPathBase<?> base) {

		Object resultado = null;
		Object finall = null;
		if (!ObjectUtils.isEmpty(campo) && !ObjectUtils.isEmpty(operador) && base != null) {
			String[] ruta = campo.split("\\.");
			Object invocado = base;
			for (String rutaSegmentada : ruta) {
				invocado = obtenerObjetoPorCampo(invocado, rutaSegmentada);
			}
			finall = construccionExpresion(finall, invocado, operador);

			if (finall != null) {
				JPAQuery<Object> query = construccionConsulta(base, finall);

				// Filtrado
				if (query != null) {
					resultado = filtradoYConsulta(filtros, base, query);
					resultado = transformarResultado(resultado);
				}
			}
		} else {
			log.error("Parámetros incorrectos.");
		}

		return resultado;
	}

	/**
	 * Obtener objeto por campo.
	 *
	 * @param invocado       the invocado
	 * @param rutaSegmentada the ruta segmentada
	 * @return the object
	 */
	private Object obtenerObjetoPorCampo(Object invocado, String rutaSegmentada) {
		try {
			invocado = obtenerCampo(invocado, rutaSegmentada);
		} catch (NoSuchFieldException e) {
			log.error(e.getMessage(), e);
		} catch (SecurityException e) {
			log.error(e.getMessage(), e);
		}
		return invocado;
	}

	/**
	 * Obtener campo.
	 *
	 * @param invocado       the invocado
	 * @param rutaSegmentada the ruta segmentada
	 * @return the object
	 * @throws NoSuchFieldException the no such field exception
	 */
	private Object obtenerCampo(Object invocado, String rutaSegmentada) throws NoSuchFieldException {
		Field field;
		field = invocado.getClass().getField(rutaSegmentada);
		try {
			invocado = field.get(invocado);
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			log.error(e.getMessage(), e);
		}
		return invocado;
	}

	/**
	 * Construccion expresion.
	 *
	 * @param finall   the finall
	 * @param invocado the invocado
	 * @param operador the operador
	 * @return the object
	 */
	private Object construccionExpresion(Object finall, Object invocado, String operador) {
		Method ejecutor;
		try {
			if (OperadorEnum.existeOperador(operador)) {
				String resOperador = OperadorEnum.valueOf(operador.toUpperCase()).getOperador();
				ejecutor = invocado.getClass().getMethod(resOperador);
				finall = invocarMetodo(finall, invocado, ejecutor);
			} else {
				log.error("El operador introducido no es válido");
			}
		} catch (NoSuchMethodException e) {
			log.error(e.getMessage(), e);
		} catch (SecurityException e) {
			log.error(e.getMessage(), e);
		}
		return finall;
	}

	/**
	 * Invocar metodo.
	 *
	 * @param finall   the finall
	 * @param invocado the invocado
	 * @param ejecutor the ejecutor
	 * @return the object
	 */
	private Object invocarMetodo(Object finall, Object invocado, Method ejecutor) {
		try {
			finall = ejecutor.invoke(invocado);
		} catch (IllegalAccessException e) {
			log.error(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			log.error(e.getMessage(), e);
		}
		return finall;
	}

	/**
	 * Construccion consulta.
	 *
	 * @param base   the base
	 * @param finall the finall
	 * @return the JPA query
	 */
	private JPAQuery<Object> construccionConsulta(EntityPathBase<?> base, Object finall) {
		Expression<?> expr = (Expression<?>) finall;

		@SuppressWarnings("unchecked")
		JPAQuery<Object> query = (JPAQuery<Object>) new JPAQuery<Object>(this.entityManager).select(expr).from(base);
		return query;
	}

	/**
	 * Filtrado Y consulta.
	 *
	 * @param filtros the filtros
	 * @param base    the base
	 * @param query   the query
	 * @return the object
	 */
	private Object filtradoYConsulta(List<FiltroDTO> filtros, EntityPathBase<?> base, JPAQuery<Object> query) {
		BooleanBuilder predi = null;
		if (filtros != null) {
			predi = FiltroQDSLUtils.obtenerFiltro(base, filtros);
		}
		if (predi != null && predi.hasValue()) {
			query = query.where(predi);
		}

		return query.createQuery().getResultList();
	}

	/**
	 * Transformar resultado.
	 *
	 * @param resultado the resultado
	 * @return the object
	 */
	private Object transformarResultado(Object resultado) {
		if (resultado != null) {
			List<?> lista = (List<?>) resultado;
			if (!lista.isEmpty() && lista.size() == 1) {
				resultado = lista.get(0);
			}
		}
		return resultado;
	}
}
