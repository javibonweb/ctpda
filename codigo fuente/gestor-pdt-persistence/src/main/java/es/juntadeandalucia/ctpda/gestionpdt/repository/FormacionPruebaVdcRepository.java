package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.FormacionPruebaVdc;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface FormacionPruebaVdcRepository extends AbstractCrudRepository<FormacionPruebaVdc>,
		JoinedQDSLPredicateExecutorCustom<FormacionPruebaVdc>, Serializable {

}
