package es.juntadeandalucia.ctpda.gestionpdt.persitence.core;

import java.util.List;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;

import es.juntadeandalucia.ctpda.gestionpdt.model.core.EntidadBasica;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.utils.PersistenceCoreUtils;


public class JoinedQDSLPredicateExecutorCustomImpl<T extends EntidadBasica> implements JoinedQDSLPredicateExecutorCustom<T> {

	//entitymanager, donde se disparan las queries
	@PersistenceContext
	private EntityManager em;
		
	
	@Override
	public List<T> consultarDatosJoined(Pageable page, Predicate predicate,EntityPathBase<T> qFrom,
			Function<JPQLQuery<T>, JPQLQuery<T>> joinBuilder) {
		JPQLQuery<T> query = new JPAQuery<T>(this.em)
				.from(qFrom);
		
			//procesamos los joins
				if (joinBuilder!=null) {
					query=joinBuilder.apply(query);
				}
		
				if (predicate !=null) {
					query = query.where(predicate);
				}
				if (page!=null) {
					query.offset(page.getOffset());
					query.limit(page.getPageSize());
					Sort s = page.getSort();
					for (Order orden : s) {
						query.orderBy(PersistenceCoreUtils.obtenerOrdenacion(qFrom, orden.getProperty(), orden.getDirection()));
					}
				}
				return query.fetch();				
	}

	@Override
	public Iterable<T> consultarDatosJoined(Sort sort, Predicate predicate,EntityPathBase<T> qFrom,
			Function<JPQLQuery<T>, JPQLQuery<T>> joinBuilder) {
		JPQLQuery<T> query = new JPAQuery<T>(this.em)
				.from(qFrom);
				//procesamos los joins
				if (joinBuilder!=null) {
					query=joinBuilder.apply(query);
				}
				
				if (predicate !=null) {
					query = query.where(predicate);
				}
				if (sort!=null && !sort.isEmpty()) {
						for (Order orden : sort) {
							query.orderBy(PersistenceCoreUtils.obtenerOrdenacion(qFrom, orden.getProperty(), orden.getDirection()));
						}
				}
				return query.fetch();
	}
	
}
