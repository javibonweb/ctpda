package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.Personas;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface PersonasRepository
		extends AbstractCrudRepository<Personas>, JoinedQDSLPredicateExecutorCustom<Personas>,Serializable  {

	@Query("SELECT per FROM Personas per WHERE per.valorTipoIdentificador.id = ?1 and per.activa = 1")
	public List<Personas> findPersonasByTipIdentif(@Param("idTipIdentif") long idTipIdentif);

}
