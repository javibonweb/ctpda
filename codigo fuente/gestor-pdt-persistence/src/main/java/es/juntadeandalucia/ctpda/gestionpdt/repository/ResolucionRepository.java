package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.Resolucion;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;
@Repository
public interface ResolucionRepository extends AbstractCrudRepository<Resolucion>,JoinedQDSLPredicateExecutorCustom<Resolucion>,Serializable {

	@Query("SELECT resolucion FROM Resolucion resolucion WHERE resolucion.codigoResolucion =?1")
	public Resolucion findResolucionByNumeroResolucion(@Param("numResol") String numResol);
	
}