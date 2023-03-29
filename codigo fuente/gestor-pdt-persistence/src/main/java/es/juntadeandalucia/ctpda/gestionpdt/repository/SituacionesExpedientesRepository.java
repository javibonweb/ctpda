package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.SituacionesExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface  SituacionesExpedientesRepository extends AbstractCrudRepository<SituacionesExpedientes>, JoinedQDSLPredicateExecutorCustom<SituacionesExpedientes>,Serializable  {

	@Query("SELECT sitExp FROM SituacionesExpedientes sitExp WHERE sitExp.activo = 1")
	public List<SituacionesExpedientes> findSituacionesExpedientesActivos();
	
	@Query("SELECT sitExp FROM SituacionesExpedientes sitExp WHERE sitExp.situacionRelSuperior.id = ?1 and sitExp.activo = 1")
	public List<SituacionesExpedientes> findSituacionesExpedientesHijosActivos(@Param("id") long id);
	
	@Query("SELECT sitExp FROM SituacionesExpedientes sitExp WHERE sitExp.valorTipoExpediente.id = ?1 and sitExp.situacionInicial = 1 and sitExp.activo = 1 ORDER BY sitExp.orden")
	public List<SituacionesExpedientes> findSituacionesExpedientesTipoExped(@Param("id") long id);
	
	@Query("SELECT sitExp FROM SituacionesExpedientes sitExp WHERE sitExp.valorTipoExpediente.id = ?1 and sitExp.situacionRelSuperior.id = ?2 and sitExp.activo = 1 ORDER BY sitExp.orden")
	public List<SituacionesExpedientes> findSituacionesExpedientesRelSuperior(@Param("id") long id,@Param("idCodRel") long codigo);
	
	@Query("SELECT sitExp FROM SituacionesExpedientes sitExp WHERE sitExp.valorTipoExpediente.id = ?1 and sitExp.activo = 1 ORDER BY sitExp.descripcion")
	public List<SituacionesExpedientes> findSituacionesExpedientesTipExpAll(@Param("id") long id);
	
	@Query("SELECT sitExp FROM SituacionesExpedientes sitExp WHERE sitExp.valorTipoExpediente.id = ?1 and sitExp.valorSituacion.id = ?2 AND ROWNUM <= 1")
	public SituacionesExpedientes findSituacionesExpedientesTipExpValorSit(@Param("idExp") long idExp,@Param("idValorSit") long idValorSit);
	
	@Query("SELECT sitExp FROM SituacionesExpedientes sitExp WHERE sitExp.valorTipoExpediente.id = ?1 and sitExp.valorSituacion.id = ?2 AND sitExp.situacionFinal = TRUE AND ROWNUM <= 1 ")
	public SituacionesExpedientes findSituacionesExpedientesTipExpValorSitFinal(@Param("idExp") long idExp,@Param("idValorSit") long idValorSit);
	
	@Query("SELECT sitExp FROM SituacionesExpedientes sitExp WHERE sitExp.situacionFinal = 1 and sitExp.activo = 1")
	public List<SituacionesExpedientes> findSituacionesExpedientesFinalizados();
	
}
