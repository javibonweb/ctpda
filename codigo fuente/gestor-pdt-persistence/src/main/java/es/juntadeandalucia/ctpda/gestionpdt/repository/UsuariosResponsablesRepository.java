package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.UsuariosResponsables;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface UsuariosResponsablesRepository extends AbstractCrudRepository<UsuariosResponsables>, JoinedQDSLPredicateExecutorCustom<UsuariosResponsables>, UsuariosResponsablesRepositoryCustom,Serializable  {
	
}
