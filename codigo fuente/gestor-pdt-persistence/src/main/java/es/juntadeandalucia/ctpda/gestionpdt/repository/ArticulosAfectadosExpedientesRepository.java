package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.ArticulosAfectadosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface ArticulosAfectadosExpedientesRepository extends AbstractCrudRepository<ArticulosAfectadosExpedientes>,JoinedQDSLPredicateExecutorCustom<ArticulosAfectadosExpedientes>,Serializable {

	@Query("SELECT artAfectExp FROM ArticulosAfectadosExpedientes artAfectExp WHERE artAfectExp.expediente.id=?1")
	public ArticulosAfectadosExpedientes obtenerArticulosAfectadosExpedientePorExpediente(@Param("id") long id);
	
	@Query("SELECT artAfectExp FROM ArticulosAfectadosExpedientes artAfectExp WHERE artAfectExp.expediente.id=?1")
	public List<ArticulosAfectadosExpedientes> obtenerListArticulosAfectadosExpedientePorExpediente(@Param("id") long id);
	
}
