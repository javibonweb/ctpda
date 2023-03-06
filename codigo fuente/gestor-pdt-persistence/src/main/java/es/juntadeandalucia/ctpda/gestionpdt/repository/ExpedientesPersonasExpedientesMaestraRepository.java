package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.ExpedientesPersonasExpedientesMaestra;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface ExpedientesPersonasExpedientesMaestraRepository extends AbstractCrudRepository<ExpedientesPersonasExpedientesMaestra>,JoinedQDSLPredicateExecutorCustom<ExpedientesPersonasExpedientesMaestra>,Serializable  {
}
