
package es.juntadeandalucia.ctpda.gestionpdt.repository;


import java.io.Serializable;

import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.FirmasTramiteMaestra;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;
@Repository
public interface FirmasTramiteMaestraRepository extends AbstractCrudRepository<FirmasTramiteMaestra>,JoinedQDSLPredicateExecutorCustom<FirmasTramiteMaestra>,Serializable  {

	
	
}