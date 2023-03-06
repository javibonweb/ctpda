package es.juntadeandalucia.ctpda.gestionpdt.persitence.core;

import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.JPQLQuery;

import es.juntadeandalucia.ctpda.gestionpdt.model.core.EntidadBasica;


/**
 * Interfaz para añadir joins personalizados a las consultas de abstractcrud
 * De esta forma los repositorios podran añadir un joinBuilder para confeccionar los joins y fetch con
 * solo una variable lambda. En un caso extremo se le pueden añadir mas cosas a la query, que no  tengamos comtempladas.
 * ej: this.joinBuilder = (query) -> query.leftJoin(QMunicipio.municipio.provincia, QProvincia.provincia).fetchJoin();
 *
 * @param <T> the type parameter
 */

public interface JoinedQDSLPredicateExecutorCustom<T extends EntidadBasica> {
	
	default Iterable<T> findAllJoined(Predicate predicate, Sort sort, EntityPathBase<T> qFrom,Function<JPQLQuery<T>, JPQLQuery<T>> joinBuilder){
		return consultarDatosJoined(sort, predicate,qFrom,joinBuilder);
	}
	
	default Iterable<T> findAllJoined(Sort sort, EntityPathBase<T> qFrom,Function<JPQLQuery<T>,  JPQLQuery<T>> joinBuilder){
		return findAllJoined(null,sort,qFrom,joinBuilder);
	}
	
	default Iterable<T> findAllJoined(EntityPathBase<T> qFrom,Function<JPQLQuery<T>,  JPQLQuery<T>> joinBuilder){
		return findAllJoined(null,Sort.unsorted(),qFrom,joinBuilder);
	}
	
	default Page<T> findAllJoined(Predicate predicate, Pageable pageable,EntityPathBase<T> qFrom,  Function<JPQLQuery<T>, JPQLQuery<T>> joinBuilder){
		List<T> contenido = consultarDatosJoined(pageable,predicate,qFrom,joinBuilder);
		return new PageImpl<>(contenido);
	}
	
	default Page<T> findAllJoined(Pageable pageable,EntityPathBase<T> qFrom,  Function<JPQLQuery<T>, JPQLQuery<T>> joinBuilder){
		return findAllJoined(null,pageable,qFrom,joinBuilder);
	}
	
	
	/** METODO A COMPLETAR SI QUEREMOS JOINS PERSONALIZADOS EN NUESTRO ABSTRACTCRUD **/
	List<T> consultarDatosJoined(Pageable page,Predicate predicate,EntityPathBase<T> qFrom, Function<JPQLQuery<T>, JPQLQuery<T>> joinBuilder);
	
	/** METODO A COMPLETAR SI QUEREMOS JOINS PERSONALIZADOS EN NUESTRO ABSTRACTCRUD **/
	Iterable<T> consultarDatosJoined(Sort sort,Predicate predicate, EntityPathBase<T> qFrom,Function<JPQLQuery<T>, JPQLQuery<T>> joinBuilder);
	
		
}
