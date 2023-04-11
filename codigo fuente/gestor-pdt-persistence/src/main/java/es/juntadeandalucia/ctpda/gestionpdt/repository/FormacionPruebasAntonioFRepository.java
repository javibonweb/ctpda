package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.FormacionPruebasAntonioF;
import es.juntadeandalucia.ctpda.gestionpdt.model.Localidades;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface FormacionPruebasAntonioFRepository extends AbstractCrudRepository<FormacionPruebasAntonioF>,JoinedQDSLPredicateExecutorCustom<FormacionPruebasAntonioF>,Serializable  {

}
