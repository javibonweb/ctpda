package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.Plantillas;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface PlantillasRepository extends AbstractCrudRepository<Plantillas>,JoinedQDSLPredicateExecutorCustom<Plantillas>,Serializable {
	
	public boolean existsByCodigoAndIdNot(String cod, Long id);
	
}
