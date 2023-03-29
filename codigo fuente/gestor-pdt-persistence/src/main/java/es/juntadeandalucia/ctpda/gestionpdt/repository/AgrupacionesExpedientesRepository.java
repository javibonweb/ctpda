package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.AgrupacionesExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface AgrupacionesExpedientesRepository extends AbstractCrudRepository<AgrupacionesExpedientes>,JoinedQDSLPredicateExecutorCustom<AgrupacionesExpedientes>,Serializable {
	
	List<AgrupacionesExpedientes> findByExpedienteIdAndCategoriaId(Long idExp, Long idCat);
	List<AgrupacionesExpedientes> findByTramiteExpedienteId(Long idTram);
	
	@Query("SELECT a FROM AgrupacionesExpedientes a WHERE a.tramiteExpediente.expediente.id = :idExp AND a.activo = 1")
	List<AgrupacionesExpedientes> findByExpedienteId(@Param("idExp")Long idExp);
	
	@Query("SELECT a FROM AgrupacionesExpedientes a WHERE a.expediente.id = :idExp AND a.activo = 1")
	List<AgrupacionesExpedientes> findByExpedienteIdDeExpediente(@Param("idExp")Long idExp);

	@Query("SELECT a FROM AgrupacionesExpedientes a WHERE a.tramiteExpediente.id = :idTramExp AND a.categoria.id = :idCat AND a.activo = 1")
	AgrupacionesExpedientes findByTramiteExpedienteIdyCat(@Param("idTramExp")Long idExp,  @Param("idCat")Long idCat);
	
	@Query("SELECT COALESCE(max(a.orden), 0) FROM AgrupacionesExpedientes a WHERE a.expediente.id = :idExp AND a.categoria.id = :idCat AND a.activo = 1")
	Long getUltimoOrden(@Param("idExp")Long idExp, @Param("idCat")Long idCat);
	
	@Query("SELECT COALESCE(max(a.orden), 0) FROM AgrupacionesExpedientes a WHERE a.tramiteExpediente.tramiteExpedienteSup.id = :idTramSup AND a.categoria.id = :idCat AND a.activo = 1")
	Long getUltimoOrdenTramiteSup(@Param("idTramSup")Long idTramSup, @Param("idCat")Long idCat);
	
	//El orden irá asociado con un idTramite concreto
	@Query("SELECT a FROM AgrupacionesExpedientes a WHERE a.tramiteExpediente.id = :idTram AND a.categoria.id = :idCat AND a.activo = 1")
	AgrupacionesExpedientes getByTramiteCategoria(@Param("idTram")Long idTram, @Param("idCat")Long idCat);
	
	//El orden irá asociado con un idTramite concreto
	@Query("SELECT a FROM AgrupacionesExpedientes a WHERE a.expediente.id = :idExp AND a.categoria.id = :idCat AND a.orden > :orden AND a.activo = 1")
	List<AgrupacionesExpedientes> findDesdeOrden(@Param("idExp")Long idExp, @Param("idCat")Long idCat, @Param("orden")Long orden);

}
