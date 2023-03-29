
package es.juntadeandalucia.ctpda.gestionpdt.repository;


import java.io.Serializable;

import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.NotificacionesTramiteMaestra;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;
@Repository
public interface NotificacionesTramiteMaestraRepository extends AbstractCrudRepository<NotificacionesTramiteMaestra>,JoinedQDSLPredicateExecutorCustom<NotificacionesTramiteMaestra>,Serializable  {

	
	
}