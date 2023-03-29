package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.MateriasExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface MateriasExpedientesRepository extends AbstractCrudRepository<MateriasExpedientes>,JoinedQDSLPredicateExecutorCustom<MateriasExpedientes>,Serializable {

	@Query("SELECT matExp FROM MateriasExpedientes matExp WHERE matExp.expediente.id=?1")
	public List<MateriasExpedientes> obtenerListMateriasExpedientesPorExpediente(@Param("id") long id);
	
}
