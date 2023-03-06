package es.juntadeandalucia.ctpda.gestionpdt.repository;


import java.io.Serializable;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.CfgMetadatosTram;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface CfgMetadatosTramRepository extends AbstractCrudRepository<CfgMetadatosTram>,JoinedQDSLPredicateExecutorCustom<CfgMetadatosTram>,Serializable  {
	
	@Query("SELECT metadatosTram FROM CfgMetadatosTram metadatosTram WHERE metadatosTram.activo = 1 "
			+ "and metadatosTram.valorTipoExpediente.id =?1 "
			+ "and metadatosTram.tipoTramite.id=?2 "
			+ "and metadatosTram.tipoTramiteSuperior.id=?3")
	public CfgMetadatosTram findCfgMetadatosTram(@Param("idTipExp") Long idTipExp, @Param("idTipTram") Long idTipTram, @Param("idTipTramSup") Long idTipTramSup);
	
	@Query("SELECT metadatosTram FROM CfgMetadatosTram metadatosTram WHERE metadatosTram.activo = 1 "
			+ "and metadatosTram.valorTipoExpediente.id =?1 "
			+ "and metadatosTram.tipoTramite.id=?2 "
			+ "and metadatosTram.tipoTramiteSuperior.id is null")
	public CfgMetadatosTram findCfgMetadatosTram(@Param("idTipExp") Long idTipExp, @Param("idTipTram") Long idTipTram);
	
	@Query("SELECT metadatosTram FROM CfgMetadatosTram metadatosTram WHERE metadatosTram.activo = 1 "
			+ "and metadatosTram.valorTipoExpediente.id =?1 "
			+ "and metadatosTram.tipoTramite.id is null "
			+ "and metadatosTram.tipoTramiteSuperior.id is null")
	public CfgMetadatosTram findCfgMetadatosTram(@Param("idTipExp") Long idTipExp);
}
