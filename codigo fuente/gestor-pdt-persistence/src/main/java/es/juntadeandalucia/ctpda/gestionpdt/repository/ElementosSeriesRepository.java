package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;

import es.juntadeandalucia.ctpda.gestionpdt.model.ElementosSeries;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

public interface ElementosSeriesRepository extends AbstractCrudRepository<ElementosSeries>,JoinedQDSLPredicateExecutorCustom<ElementosSeries>,Serializable  {

}
