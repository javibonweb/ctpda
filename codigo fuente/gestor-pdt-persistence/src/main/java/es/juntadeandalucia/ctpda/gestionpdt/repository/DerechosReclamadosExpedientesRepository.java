package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.DerechosReclamadosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface DerechosReclamadosExpedientesRepository extends AbstractCrudRepository<DerechosReclamadosExpedientes>,JoinedQDSLPredicateExecutorCustom<DerechosReclamadosExpedientes>,Serializable {

	@Query("SELECT derReclExp FROM DerechosReclamadosExpedientes derReclExp WHERE derReclExp.expediente.id=?1")
	public DerechosReclamadosExpedientes obtenerDerechosReclamadosExpedientePorExpediente(@Param("id") long id);
	
	@Query("SELECT derReclExp FROM DerechosReclamadosExpedientes derReclExp WHERE derReclExp.expediente.id=?1")
	public List<DerechosReclamadosExpedientes> obtenerListDerechosReclamadosExpedientePorExpediente(@Param("id") long id);
}
