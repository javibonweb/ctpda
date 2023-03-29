package es.juntadeandalucia.ctpda.gestionpdt.repository;


import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.Personas;
import es.juntadeandalucia.ctpda.gestionpdt.model.SujetosObligados;
import es.juntadeandalucia.ctpda.gestionpdt.model.SujetosObligadosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;
@Repository
public interface SujetosObligadosExpedientesRepository extends AbstractCrudRepository<SujetosObligadosExpedientes>,JoinedQDSLPredicateExecutorCustom<SujetosObligadosExpedientes>,Serializable {

	
	@Query("SELECT sujObliExp FROM SujetosObligadosExpedientes sujObliExp WHERE sujObliExp.expediente.id=?1")
	public SujetosObligadosExpedientes obtenerSujetosObligadosExpedientePorExpediente(@Param("id") long id);
	
	@Query("SELECT MAX(sujObliExp.expediente) FROM SujetosObligadosExpedientes sujObliExp WHERE sujObliExp.sujetosObligados.id=?1 "
			+ "and (sujObliExp.nombreRazonsocial is not null OR sujObliExp.telefono IS NOT NULL OR sujObliExp.apellidos IS NOT NULL OR sujObliExp.email IS NOT NULL)")
	public Expedientes obtenerExpMaxPorSujetoOblig(@Param("idSujOblig") long idSujOblig);
	
	@Query("SELECT sujObliExp FROM SujetosObligadosExpedientes sujObliExp WHERE sujObliExp.expediente.id=?1 and sujObliExp.sujetosObligados.id=?2")
	public SujetosObligadosExpedientes obtenerSujetosObligadosExpedientePorSujOblig(@Param("idExp") long idExp, @Param("idSujOblig") long idSujOblig);
	
	@Query("SELECT sujObliExp FROM SujetosObligadosExpedientes sujObliExp WHERE sujObliExp.expediente.id=?1 AND sujObliExp.principal=1")
	public SujetosObligadosExpedientes obtenerSujetosObligadosExpedientePrincipalPorExpediente(@Param("id") long id);
	
	@Query("SELECT sujObliExp FROM SujetosObligadosExpedientes sujObliExp WHERE sujObliExp.expediente.id=?1")
	public List<SujetosObligadosExpedientes> obtenerSujetosObligadosExpedientePrincipalYNoPrincipalPorExpediente(@Param("id") long id);
	
	@Query("SELECT sujObl FROM SujetosObligadosExpedientes sujObliExp, SujetosObligados sujObl WHERE sujObliExp.expediente.id=?1 and sujObliExp.sujetosObligados.id = sujObl.id order by sujObl.descripcion")
	public List<SujetosObligados> obtenerSujetosObligadosExpediente(@Param("id") long id);
	
	@Query("SELECT per FROM SujetosObligadosExpedientes sujObliExp, Personas per WHERE sujObliExp.expediente.id=?1 and sujObliExp.personas.id = per.id order by per.nombreRazonsocial")
	public List<Personas> obtenerDpdExpediente(@Param("idExp") long idExp);
	
	@Query("SELECT per FROM SujetosObligadosExpedientes sujObliExp, Personas per WHERE sujObliExp.expediente.id=?1 and sujObliExp.principal = 1 and sujObliExp.personas.id = per.id order by per.nombreRazonsocial")
	public Personas obtenerDpdSujetoPpalExpediente(@Param("idExp") long idExp);
	
	@Query("SELECT sujObliExp FROM SujetosObligadosExpedientes sujObliExp WHERE sujObliExp.expediente.id=?1 and sujObliExp.principal = 1")
	public SujetosObligadosExpedientes obtenerSujObligExpConDpdSinPersona(@Param("idExp") long idExp);
	
	
}
