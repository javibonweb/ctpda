package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;
@Repository
public interface ExpedientesRepository extends AbstractCrudRepository<Expedientes>,JoinedQDSLPredicateExecutorCustom<Expedientes>,Serializable {

	@Query("SELECT exp FROM Expedientes exp WHERE UPPER(exp.valorSituacionExpediente.codigo) = UPPER(?1)")
	List<Expedientes> findExpedientesBySituacion(@Param("codSituacion") String codSituacion);
	
	@Query("select exp from Expedientes exp where "
			+ "exp.valorTipoExpediente.id in (select sitExpTipExp.valorTipoExpediente.id from SituacionesExpedientes sitExpTipExp where sitExpTipExp.noSupervisada = 0) "
			+ "and exp.valorSituacionExpediente.id in (select sitExpSit.valorSituacion.id from SituacionesExpedientes sitExpSit where sitExpSit.noSupervisada = 0)")
	List<Expedientes> findExpedientesBySituacionExpNoSupervisada();
	
	@Query("SELECT exp FROM Expedientes exp WHERE UPPER(exp.valorTipoExpediente.codigo) = UPPER(?1) and UPPER(exp.valorSituacionExpediente.codigo) = UPPER(?2)")
	List<Expedientes> findExpedientesByTipExpSituacion(@Param("codTipExpediente") String codTipExpediente,@Param("codSituacion") String codSituacion);
	
}
