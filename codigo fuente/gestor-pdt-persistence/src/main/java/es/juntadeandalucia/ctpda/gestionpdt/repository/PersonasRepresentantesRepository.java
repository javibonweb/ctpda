package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.PersonasRepresentantes;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;
@Repository
public interface PersonasRepresentantesRepository extends AbstractCrudRepository<PersonasRepresentantes>,JoinedQDSLPredicateExecutorCustom<PersonasRepresentantes>, PersonasRepresentantesRepositoryCustom,Serializable  {
	
	@EntityGraph(value="persona.representante", type = EntityGraphType.LOAD)
	@Query("SELECT pr FROM PersonasRepresentantes pr WHERE pr.persona.id = :id AND pr.principal = TRUE")
	PersonasRepresentantes obtenerRepresentantePrincipal(@Param("id") Long idPer);

	Long countByPersonaId(Long idPersona);
}
