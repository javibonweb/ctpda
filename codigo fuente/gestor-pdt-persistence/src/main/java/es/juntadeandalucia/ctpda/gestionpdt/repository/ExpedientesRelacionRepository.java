package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.ExpedientesRelacion;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;
@Repository
public interface ExpedientesRelacionRepository extends AbstractCrudRepository<ExpedientesRelacion>,JoinedQDSLPredicateExecutorCustom<ExpedientesRelacion>, ExpedientesRelacionRepositoryCustom,Serializable  {

	
	@Query("SELECT expRela.expedienteRelacionado.id FROM ExpedientesRelacion expRela WHERE expRela.expedienteOrigen.id =?1")
	public List<Long> findIdExpRelacionadosByExpOrigen(@Param("idExpOrigen") long idExpOrigen);
	
	@Query("SELECT expRela.expedienteRelacionado.numExpediente FROM ExpedientesRelacion expRela WHERE expRela.expedienteOrigen.id =?1 and expRela.motivo.codigo =?2")
	public List<String> findExpRelacionadosOrigenByExpAndMotivo(@Param("idExp") long idExp, @Param("motivo") String motivo);
	
	@Query("SELECT expRela.expedienteOrigen.numExpediente FROM ExpedientesRelacion expRela WHERE expRela.expedienteRelacionado.id =?1 and expRela.motivo.codigo =?2")
	public List<String> findExpRelacionadosRelacionadoByExpAndMotivo(@Param("idExp") long idExp, @Param("motivo") String motivo);
	
}
