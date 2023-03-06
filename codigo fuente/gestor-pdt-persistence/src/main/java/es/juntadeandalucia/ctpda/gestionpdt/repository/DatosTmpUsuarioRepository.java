package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.DatosTmpUsuario;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface DatosTmpUsuarioRepository extends AbstractCrudRepository<DatosTmpUsuario>,JoinedQDSLPredicateExecutorCustom<DatosTmpUsuario>,Serializable  {

	DatosTmpUsuario getByUsuarioId(Long idUsuario);
	
}
