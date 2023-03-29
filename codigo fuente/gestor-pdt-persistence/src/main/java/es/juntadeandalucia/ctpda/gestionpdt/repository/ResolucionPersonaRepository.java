package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.ResolucionPersona;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;
@Repository
public interface ResolucionPersonaRepository extends AbstractCrudRepository<ResolucionPersona>,JoinedQDSLPredicateExecutorCustom<ResolucionPersona>,Serializable {
	
	public List<ResolucionPersona> findByResolucionId(Long idResol);
	
	public ResolucionPersona getByResolucionIdAndPrincipalTrue(Long idResol);

}