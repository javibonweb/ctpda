package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentosExpedientesTramites;
import es.juntadeandalucia.ctpda.gestionpdt.model.TramiteExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface DocumentosExpedientesTramitesRepository extends AbstractCrudRepository<DocumentosExpedientesTramites>,JoinedQDSLPredicateExecutorCustom<DocumentosExpedientesTramites>,Serializable  {	
		
	boolean existsByDocumentoExpedienteIdAndTramiteExpedienteId(Long idDocExp, Long idTramExp);
	
	@Query("SELECT docExpTram FROM DocumentosExpedientesTramites docExpTram WHERE docExpTram.tramiteExpediente.id =?1 ")
	List<DocumentosExpedientesTramites> findDocExpTramByIdTramExp(@Param("idTramExp")Long idTramExp);
	boolean existsByTramiteExpedienteId(Long idTramExp);
	
	List<DocumentosExpedientesTramites> findByDocumentoExpedienteId(Long idDocExp);
	
	@Query("SELECT docExpTram.tramiteExpediente FROM DocumentosExpedientesTramites docExpTram WHERE docExpTram.documentoExpediente.id =?1")
	List<TramiteExpediente> findTramitesExpByDocumentoExpedienteId(@Param("idDocExp")Long idDocExp);

	@Query("SELECT docExpTram.tramiteExpediente FROM DocumentosExpedientesTramites docExpTram WHERE docExpTram.documentoExpediente.id =?1 and docExpTram.tramiteExpediente.tramiteExpedienteSup.id = ?2 ")
	List<TramiteExpediente> findSubTramitesExpByDocumentoExpedienteIdAndTramiteExpedienteId(@Param("idDocExp")Long idDocExp, @Param("idTramite")Long idTramite);

	@Query("SELECT count(docExpTram.id) FROM DocumentosExpedientesTramites docExpTram WHERE docExpTram.tramiteExpediente.id =?1 ")
	int countDocExpTramByIdTramExp(@Param("idTramExp")Long idTramExp);
	
	@Query("SELECT docExpTram FROM DocumentosExpedientesTramites docExpTram WHERE docExpTram.tramiteExpediente.id =?1 and docExpTram.documentoExpediente.id =?2")
	DocumentosExpedientesTramites findDocExpTramByIdTramExpAndDocExp(@Param("idTramExp")Long idTramExp, @Param("idDocExp")Long idDocExp);
	
}
