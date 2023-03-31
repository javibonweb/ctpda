package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.FormacionPruebasGRS;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface FormacionPruebasGRSRepository extends AbstractCrudRepository<FormacionPruebasGRS>,JoinedQDSLPredicateExecutorCustom<FormacionPruebasGRS>,Serializable {
	

	
}
