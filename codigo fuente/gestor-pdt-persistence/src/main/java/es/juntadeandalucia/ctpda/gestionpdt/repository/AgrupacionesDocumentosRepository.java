package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.AgrupacionesDocumentos;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface AgrupacionesDocumentosRepository extends AbstractCrudRepository<AgrupacionesDocumentos>,JoinedQDSLPredicateExecutorCustom<AgrupacionesDocumentos>,Serializable {
	
	public boolean existsByAgrupacionExpedientesId(Long id);
	
	@Query("SELECT  COUNT(doc) FROM AgrupacionesDocumentos doc WHERE doc.agrupacionExpedientes.id=?1 and doc.activo=1")
	public Long obtenerNumDocumentosAgrupExpediente(@Param("idExp") long idExp);
	
	@Query("SELECT doc FROM AgrupacionesDocumentos doc WHERE doc.agrupacionExpedientes.id=?1")
	public List<AgrupacionesDocumentos> obtenerListaDocAgrupExpediente(@Param("idExp") long idExp);
	
	@Query("SELECT doc FROM AgrupacionesDocumentos doc WHERE doc.agrupacionExpedientes.id=?1")
	public AgrupacionesDocumentos obtenerDocAgrupExpediente(@Param("idExp") long idExp);
	

	@Query("SELECT a FROM AgrupacionesDocumentos a WHERE a.documentosExpedientesTramites.id=?1 AND a.activo = 1")
	public AgrupacionesDocumentos getByDocExpTram(@Param("idDocExpTr")Long idDocExpTr);
	
	@Query("SELECT COALESCE(max(a.orden), 0) FROM AgrupacionesDocumentos a WHERE a.agrupacionExpedientes.id = :idAgrExp AND a.activo = 1")
	public Long getUltimoOrden(@Param("idAgrExp")Long idAgrExp);	
	@Query("SELECT COALESCE(max(a.orden), 0) FROM AgrupacionesDocumentos a WHERE a.id != :id AND a.agrupacionExpedientes.id = :idAgrExp AND a.activo = 1")
	public Long getUltimoOrden(@Param("id")Long id, @Param("idAgrExp")Long idAgrExp);
	
	
	
}
