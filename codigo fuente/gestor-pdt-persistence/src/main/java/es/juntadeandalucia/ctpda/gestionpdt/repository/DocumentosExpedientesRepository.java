package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface DocumentosExpedientesRepository extends AbstractCrudRepository<DocumentosExpedientes>,JoinedQDSLPredicateExecutorCustom<DocumentosExpedientes>,Serializable  {
	
	@EntityGraph(value="documentoExp.basico", type = EntityGraphType.FETCH)
	@Query("SELECT de FROM DocumentosExpedientes de WHERE de.id = :idDocExp") 
	DocumentosExpedientes getDocumentoBasico(@Param("idDocExp")Long idDocExp);
	
	List<DocumentosExpedientes> findByDocumentoIdAndActivoTrue(@Param("idExp")Long idDoc);

	@Query("SELECT de FROM DocumentosExpedientes de WHERE de.activo = 1 AND de.expediente.id =?1 AND de.tramiteExpediente.id =?2")
	List<DocumentosExpedientes> findDocumentosActivosByExpdteIdTramExpActivos(@Param("idExp")Long idExp, @Param("idTramExp")Long idTramExp);
	
	@Query("SELECT de FROM DocumentosExpedientes de WHERE de.activo = 1 AND de.expediente.id =?1 AND de.tramiteExpediente.id =?2 AND de.documento.id =?3")
	List<DocumentosExpedientes> findDocumentosActivosByExpdteIdTramExpIdDoc(@Param("idExp")Long idExp, @Param("idTramExp")Long idTramExp, @Param("idDoc")Long idDoc);
	
	@Query("SELECT de FROM DocumentosExpedientes de WHERE de.expediente.id = :id and de.activo=1")
	List<DocumentosExpedientes> findDocumentosByExpedienteId(@Param("id") Long idExp);
	
	@Query("SELECT de FROM DocumentosExpedientes de WHERE de.documento.id = :id")
	List<DocumentosExpedientes> findDocumentosByDocumentoId(@Param("id") Long idExp);
		
	@Query("SELECT de FROM DocumentosExpedientes de WHERE de.expediente.id =?1 AND de.tramiteExpediente.id =?2")
	List<DocumentosExpedientes> findDocumentosActivosByExpdteIdTramExp(@Param("idExp")Long idExp, @Param("idTramExp")Long idTramExp);
	
}