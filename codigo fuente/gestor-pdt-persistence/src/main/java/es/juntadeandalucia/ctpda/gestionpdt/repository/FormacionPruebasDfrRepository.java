package es.juntadeandalucia.ctpda.gestionpdt.repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.FormacionPruebasDfr;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface FormacionPruebasDfrRepository extends AbstractCrudRepository<FormacionPruebasDfr>, JoinedQDSLPredicateExecutorCustom<FormacionPruebasDfr>, Serializable {
}
