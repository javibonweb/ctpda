package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.TramiteExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface TramiteExpedienteRepository extends AbstractCrudRepository<TramiteExpediente>,JoinedQDSLPredicateExecutorCustom<TramiteExpediente>,Serializable  {
	
	@Query("SELECT tramExp FROM TramiteExpediente tramExp WHERE tramExp.expediente.id =?1")
	public List<TramiteExpediente> findTramitesExp(@Param("idTipExp") Long idTipExp);
	
	@Query("SELECT tramExp.id FROM TramiteExpediente tramExp WHERE tramExp.expediente.id =?1 and tramExp.id != ?2")
	public List<Long> findIdsTramitesExpByExp(@Param("idExp") Long idExp, @Param("idTramite") Long idTramite);
	
	@Query("SELECT tramExp.tipoTramite.codigo FROM TramiteExpediente tramExp WHERE tramExp.expediente.id =?1 and tramExp.finalizado = FALSE and tramExp.nivel = 0 and tramExp.activo = 1 "
			+ "ORDER BY tramExp.fechaIni ASC, tramExp.id ASC")
	public List<String> findCodigoTipoTramTramitesExpAbiertos(@Param("idTipExp") Long idTipExp);
	
	@Query("SELECT tramExp FROM TramiteExpediente tramExp WHERE tramExp.expediente.id =?1 and tramExp.finalizado = 0 and tramExp.nivel = 0 and tramExp.activo = 1 "
			+ "ORDER BY tramExp.fechaIni ASC, tramExp.id ASC")
	public List<TramiteExpediente> findTipoTramTramitesExpAbiertos(@Param("idTipExp") Long idTipExp);
	
	@Query("SELECT tramExp FROM TramiteExpediente tramExp WHERE tramExp.expediente.id =?1 and tramExp.finalizado = 0 and tramExp.activo = 1 "
			+ "ORDER BY tramExp.fechaIni ASC, tramExp.id ASC")
	public List<TramiteExpediente> findTramitesExpAbiertos(@Param("idTipExp") Long idTipExp);
	
	@Query("SELECT tramExp FROM TramiteExpediente tramExp "
			+ "WHERE tramExp.expediente.id =?1 and tramExp.finalizado = 0 and tramExp.activo = 1 and tramExp.nivel = 0"
			+ "ORDER BY tramExp.fechaIni ASC, tramExp.id ASC")
	public List<TramiteExpediente> findTramitesSupExpAbiertos(@Param("idExp") Long idExp);
	
	@Query("SELECT tramExp FROM TramiteExpediente tramExp "
			+ "WHERE tramExp.expediente.id =?1 and tramExp.activo = 1 and tramExp.nivel = 0"
			+ "ORDER BY tramExp.fechaIni ASC, tramExp.id ASC")
	public List<TramiteExpediente> findTramitesSupExp(@Param("idExp") Long idExp);
	
	@Query("SELECT tramExp FROM TramiteExpediente tramExp WHERE tramExp.expediente.id =?1 and tramExp.finalizado = FALSE and tramExp.nivel > 0 and tramExp.activo = 1 "
			+ "ORDER BY tramExp.fechaIni ASC, tramExp.id ASC")
	public List<TramiteExpediente> findTipoTramSubTramitesExpAbiertos(@Param("idTipExp") Long idTipExp);
	
		@Query("SELECT tramExp.tipoTramite.codigo FROM TramiteExpediente tramExp WHERE tramExp.expediente.id =?1")
	public List<String> findCodigoTipoTramTramitesExp(@Param("idTipExp") Long idTipExp);
	
	@Query("SELECT tramExp.descripcionAbrev FROM TramiteExpediente tramExp WHERE tramExp.expediente.id =?1")
	public List<String> findDescAbrevTramitesExp(@Param("idTipExp") Long idTipExp);


	@Query("SELECT tramExp.descripcionAbrev FROM TramiteExpediente tramExp WHERE tramExp.expediente.id =?1")
	public List<String> findCogidosTramitesExp(@Param("idTipExp") Long idTipExp);
	
	@Query("SELECT tramExp FROM TramiteExpediente tramExp WHERE tramExp.tramiteExpedienteSup.tipoTramite.codigo =?1 and  tramExp.expediente.id =?2 and tramExp.finalizado = FALSE and tramExp.nivel > 0 and tramExp.activo = 1 "
			+ "ORDER BY tramExp.fechaIni ASC, tramExp.id ASC")
	public List<TramiteExpediente> findTramSubTramExpByCod(@Param("codTipTramite") String codTipTramite, @Param("idTipExp") Long idTipExp);
	
	@Query("SELECT tramExp FROM TramiteExpediente tramExp WHERE tramExp.expediente.id=?1 AND tramExp.id = (SELECT MAX(tE.id) FROM TramiteExpediente tE WHERE tE.tipoTramite.id = (SELECT tipTram.id FROM TipoTramite tipTram WHERE tipTram.codigo = ?2) AND tE.expediente.valorTipoExpediente.id = ?3) AND tramExp.activo = 1 ")
	public TramiteExpediente findUltimoTramActivoByExpTipoTramite(@Param("idExp") Long idExp, @Param("codTipTramite") String codTipTramite, @Param("idTipExp") Long idTipExp);
	
	@Query("SELECT tramExp FROM TramiteExpediente tramExp WHERE tramExp.tramiteExpedienteSup.id =?1 and (tramExp.tipoTramite.codigo =?2 or tramExp.tipoTramite.codigo =?3) and tramExp.activo = 1")
	public List<TramiteExpediente> findSubTramExpByTramExpActivosCodTram(@Param("idTramExp") Long idTramExp, @Param("codTipoTram1") String codTipoTram1, @Param("codTipoTram2") String codTipoTram2);

	@Query("SELECT tramExp FROM TramiteExpediente tramExp WHERE tramExp.tramiteExpedienteSup.id =?1 and tramExp.tipoTramite.codigo =?2 and tramExp.activo = 1")
	public List<TramiteExpediente> findSubTramExpByTramExpActivosCodTram(@Param("idTramExp") Long idTramExp, @Param("codTipoTram") String codTipoTram);
	
	@Query("SELECT tramExp FROM TramiteExpediente tramExp WHERE tramExp.tramiteExpedienteSup.id =?1 and tramExp.finalizado = 0")
	public List<TramiteExpediente> findSubTramExpByTramExp(@Param("idTramExp") Long idTramExp);
	
	@Query("SELECT tramExp FROM TramiteExpediente tramExp WHERE tramExp.tramiteExpedienteSup.id =?1 and tramExp.finalizado = 0 and tramExp.activo=1")
	public List<TramiteExpediente> findSubTramExpByTramExpNoEliminados(@Param("idTramExp") Long idTramExp);
	
	@Query("SELECT tramExp FROM TramiteExpediente tramExp WHERE tramExp.tramiteExpedienteSup.id =?1 and tramExp.activo = 1")
	public List<TramiteExpediente> findSubTramExpByTramExpActivos(@Param("idTramExp") Long idTramExp);
	
	@Query("SELECT tramExp FROM TramiteExpediente tramExp WHERE tramExp.tramiteExpedienteSup.id =?1 and tramExp.activo = 1 and tramExp.finalizado = 0")
	public List<TramiteExpediente> findSubTramExpByTramExpActivosNoFinalizados(@Param("idTramExp") Long idTramExp);
	
	@Query("SELECT tramExp FROM TramiteExpediente tramExp WHERE tramExp.expediente.id =?1 and tramExp.tipoTramite.id =?2 and tramExp.finalizado = 0 and tramExp.activo = 1")
	public List<TramiteExpediente> findTramExpAsociadoExp(@Param("idExp") Long idExp, @Param("idTipTram") Long idTipTram);
	
	@Query("SELECT tramExp FROM TramiteExpediente tramExp WHERE tramExp.expediente.id =?1 and tramExp.tramiteExpedienteSup.id =?2 and tramExp.finalizado = 0 and tramExp.activo = 1 "
			+ "ORDER BY tramExp.fechaIni ASC, tramExp.id ASC")

	public List<TramiteExpediente> findSubTramExp(@Param("idExp") Long idExp, @Param("idTipTramSup") Long idTipTramSup);

	@Query("SELECT DISTINCT tipoTramite.descripcion FROM TramiteExpediente tramExp WHERE tramExp.expediente.id =?1 and tramExp.activo = 1")
	public List<String> findDescripcionTipoTramitesByExp(@Param("idExp") Long idExp);
	
	@Query("SELECT tramExp FROM TramiteExpediente tramExp WHERE tramExp.expediente.id =?1 and tramExp.activo = 1 ORDER BY tramExp.fechaIni ASC, tramExp.id ASC")
	public List<TramiteExpediente> findTramitesExpedientes(@Param("idExp") Long idExp);
	
	@Query("SELECT tramExp FROM TramiteExpediente tramExp WHERE tramExp.expediente.id =?1 and tramExp.tramiteExpedienteSup.id =?2 and tramExp.activo = 1")
	public List<TramiteExpediente> findTodosSubTramExp(@Param("idExp") Long idExp, @Param("idTipTramSup") Long idTipTramSup);
	
	@Query("SELECT DISTINCT tipoTramite.descripcion FROM TramiteExpediente tramExp WHERE tramExp.expediente.id =?1 and tramExp.activo = 1 and tramExp.tipoTramite.informal = ?2")
	public List<String> findDescripcionTipoTramitesInformalesByExp(@Param("idExp") Long idExp, @Param("informal") boolean informal); 

	@Query("SELECT tramExp FROM TramiteExpediente tramExp WHERE tramExp.expediente.id =?1")
	public List<TramiteExpediente> findAllTramitesExpedientesByExp(@Param("idExp") Long idExp);
	
	public boolean existsByActivoTrueAndFinalizadoFalseAndExpedienteIdAndIdNot(Long idExp, Long idTram);
	
	@Query("SELECT tramExp FROM TramiteExpediente tramExp WHERE tramExp.expediente.id =?1 and tramExp.finalizado = 0 and tramExp.activo = 1")
	public List<TramiteExpediente> findTramitesExpAbiertosYActivosByExpediente(@Param("idExpediente") Long idExpediente);
	
	@Query("SELECT tramExp FROM TramiteExpediente tramExp WHERE tramExp.expediente.id =?1 and tramExp.tipoTramite.codigo =?2 and tramExp.finalizado = 1 and tramExp.activo = 1")
	public List<TramiteExpediente> findTramiteFinalizadoByExpYTipoTramite(@Param("idExpediente") Long idExpediente, @Param("codTipTramite") String codTipTramite);
}
