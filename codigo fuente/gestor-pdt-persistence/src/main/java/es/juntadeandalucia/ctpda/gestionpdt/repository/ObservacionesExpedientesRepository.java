package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.ObservacionesExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface ObservacionesExpedientesRepository extends AbstractCrudRepository<ObservacionesExpedientes>,JoinedQDSLPredicateExecutorCustom<ObservacionesExpedientes>,Serializable {

	@Query("SELECT obsExp FROM ObservacionesExpedientes obsExp WHERE obsExp.expediente.id=?1")
	public List<ObservacionesExpedientes> obtenerObservacionesExpedientesPorExpediente(@Param("id") long id);
}
