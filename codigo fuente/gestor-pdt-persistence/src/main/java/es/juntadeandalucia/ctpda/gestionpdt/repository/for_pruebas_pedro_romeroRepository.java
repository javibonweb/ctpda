package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.Localidades;
import es.juntadeandalucia.ctpda.gestionpdt.model.for_pruebas_pedro_romero;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface for_pruebas_pedro_romeroRepository extends AbstractCrudRepository<for_pruebas_pedro_romero>,JoinedQDSLPredicateExecutorCustom<for_pruebas_pedro_romero>,Serializable  {

}
