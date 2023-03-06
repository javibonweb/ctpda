package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.Personas;
import es.juntadeandalucia.ctpda.gestionpdt.model.PersonasExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;
@Repository
public interface PersonasExpedientesRepository
		extends AbstractCrudRepository<PersonasExpedientes>, JoinedQDSLPredicateExecutorCustom<PersonasExpedientes>,Serializable  {
	
	@Query("SELECT perExp FROM PersonasExpedientes perExp WHERE perExp.principal=1 AND perExp.expediente.id=?1")
	public List<PersonasExpedientes> obtenerListaPersonaExpedientePorExpediente(@Param("id") long id);
	
	@Query("SELECT perExp FROM PersonasExpedientes perExp WHERE perExp.expediente.id=?1")
	public PersonasExpedientes obtenerPersonaExpedientePorExpediente(@Param("id") long id);
	
	@Query("SELECT perExp FROM PersonasExpedientes perExp WHERE perExp.expediente.id=?1 AND perExp.principal=1")
	public PersonasExpedientes obtenerPersonaExpedientePrincipalPorExpediente(@Param("id") long id);
	
	@Query("SELECT perExp FROM PersonasExpedientes perExp WHERE perExp.expediente.id=?1")
	public List<PersonasExpedientes> obtenerListaPersonaExpedientePorExpedientePrincipalYNoPrincipal(@Param("id") long id);
	
	@Query("SELECT perExp FROM PersonasExpedientes perExp WHERE perExp.personasRepre.id=?1")
	public PersonasExpedientes obtenerPersonaRepreExpedientePorExpediente( @Param("personasRepre") long idPersona);
	
	@Query("SELECT per FROM PersonasExpedientes perExp, Personas per WHERE perExp.expediente.id=?1 and (perExp.personas.id = per.id or perExp.personasRepre.id = per.id) order by per.nombreRazonsocial")
	public List<Personas> obtenerPersYPersReprePorExpediente(@Param("idExo") long idExp);
	
	@Query("SELECT perExp FROM PersonasExpedientes perExp, Personas per WHERE perExp.expediente.id=?1 and perExp.personas.id = per.id order by per.nombreRazonsocial")
	public List<PersonasExpedientes> obtenerPersPorExpediente(@Param("idExp") long idExp);

}
