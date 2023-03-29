package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.DerechoReclamadoResolucion;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;
@Repository
public interface DerechoReclamadoResolucionRepository extends AbstractCrudRepository<DerechoReclamadoResolucion>,JoinedQDSLPredicateExecutorCustom<DerechoReclamadoResolucion>,Serializable {

	public List<DerechoReclamadoResolucion> findByResolucionId(Long idResol);
	
	@Query("SELECT dchoReclamResol FROM DerechoReclamadoResolucion dchoReclamResol WHERE dchoReclamResol.resolucion.id =?1 and dchoReclamResol.valoresDerReclResol.id=?2")
	public DerechoReclamadoResolucion findDchoReclamResolucionExpByIdResolIdDchoReclam(@Param("idResol") Long idResol, @Param("idDchoRecl") Long idDchoRecl);

}