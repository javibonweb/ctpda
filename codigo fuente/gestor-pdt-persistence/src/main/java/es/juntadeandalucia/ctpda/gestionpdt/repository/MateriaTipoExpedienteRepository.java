package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.MateriaTipoExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface MateriaTipoExpedienteRepository extends AbstractCrudRepository<MateriaTipoExpediente>,JoinedQDSLPredicateExecutorCustom<MateriaTipoExpediente>,Serializable  {
	
	@Query("select matTipExp.valorDominioTipoExpediente.id from MateriaTipoExpediente matTipExp where matTipExp.valorDominioMateria.id = ?1")
	String[] findValoresDominioTiposExpedientes(@Param("idValorDominio") Long idValorDominio);
	
	@Query("select matTipExp.valorDominioTipoExpediente.descripcion from MateriaTipoExpediente matTipExp where matTipExp.valorDominioMateria.id = ?1")
	String[] findValoresDominioTiposExpedientesDesc(@Param("idValorDominio") Long idValorDominio);
	
	@Query("select matTipExp.valorDominioTipoExpediente from MateriaTipoExpediente matTipExp where matTipExp.valorDominioMateria.id = ?1")
	List<ValoresDominio> findValoresDominioTipExp(@Param("idValorDominio") Long idValorDominio);
	
	@Query("select matTipExp from MateriaTipoExpediente matTipExp where matTipExp.valorDominioMateria.id = ?1")
	List<MateriaTipoExpediente> findMateriasTipoExpedienteIdMateria(@Param("idValorDominio") Long idValorDominio);
	
	@Query("select matTipExp from MateriaTipoExpediente matTipExp where matTipExp.valorDominioMateria.id = ?1 and matTipExp.valorDominioTipoExpediente.id= ?2")
	MateriaTipoExpediente findMateriasTipoExpedienteIdMateria(@Param("idValDomMateria") Long idValDomMateria, @Param("idValDomTipExp") Long idValDomTipExp);
	
	@Query("select matTipExp from MateriaTipoExpediente matTipExp where matTipExp.valorDominioTipoExpediente.id = ?1 ORDER BY matTipExp.valorDominioMateria.descripcion")
	List<MateriaTipoExpediente> findValoresDominioMateriaByValorDominioTipoExpediente(@Param("idValorDominioTipoExpediente") Long idValorDominioTipoExpediente);
}
