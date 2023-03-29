package es.juntadeandalucia.ctpda.gestionpdt.persitence.core;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import es.juntadeandalucia.ctpda.gestionpdt.model.core.EntidadBasica;


/**
 * The interface Abstract crud repository.
 *
 * @param <T> the type parameter
 */
@NoRepositoryBean
public interface AbstractCrudRepository<T extends EntidadBasica> extends JpaRepository<T, Long>, QuerydslPredicateExecutor<T> ,JoinedQDSLPredicateExecutorCustom<T>, Serializable{

}
