package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.CfgExpedienteTramite;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface CfgExpedienteTramiteRepository extends AbstractCrudRepository<CfgExpedienteTramite>,JoinedQDSLPredicateExecutorCustom<CfgExpedienteTramite>,Serializable  {
	
	@Query("SELECT distinct expTram FROM CfgExpedienteTramite expTram WHERE expTram.valorTipoExpediente.id =?1 and expTram.tipoTramite.activo=1 ORDER BY expTram.descripcion ASC")
	public List<CfgExpedienteTramite> findExpTramitesByTipExp(@Param("idTipExp") Long idTipExp);
	
	@Query("SELECT distinct expTram FROM CfgExpedienteTramite expTram WHERE expTram.valorTipoExpediente.id =?1 and (expTram.valorSituacion.id is null or expTram.valorSituacion.id = ?2) and expTram.tipoTramite.activo=1 ORDER BY expTram.descripcion ASC")
	public List<CfgExpedienteTramite> findExpTramitesByTipExpSitExp(@Param("idTipExp") Long idTipExp, @Param("idSitExp") Long idSitExp);
	
	@Query("SELECT distinct expTram FROM CfgExpedienteTramite expTram WHERE expTram.valorTipoExpediente.id =?1 and (expTram.valorSituacion.id is null) and expTram.tipoTramite.activo=1 ORDER BY expTram.descripcion ASC")
	public List<CfgExpedienteTramite> findExpEventosByTipExpSitExp(@Param("idTipExp") Long idTipExp, @Param("idSitExp") Long idSitExp);
	
	@Query("SELECT distinct expTram FROM CfgExpedienteTramite expTram WHERE expTram.valorTipoExpediente.id =?1 and (expTram.valorSituacion.id = ?2) and expTram.tipoTramite.activo=1 ORDER BY expTram.descripcion ASC")
	public List<CfgExpedienteTramite> findExpTramitessinEventosByTipExpSitExp(@Param("idTipExp") Long idTipExp, @Param("idSitExp") Long idSitExp);
	
	@Query("SELECT expTram.tipoTramite.id FROM CfgExpedienteTramite expTram WHERE expTram.id =?1")
	public Long findCfgExpTramiteByTipTramite(@Param("idExpTram") Long idExpTram);
	
	@Query("SELECT cfgExpTram FROM CfgExpedienteTramite cfgExpTram WHERE cfgExpTram.valorTipoExpediente.id =?1 and cfgExpTram.valorSituacion.id = ?2 and cfgExpTram.auto = 1")
	public List<CfgExpedienteTramite> findExpTramitesByTipExpSitExpAut(@Param("idTipExp") Long idTipExp, @Param("idSitExp") Long idSitExp);	
	
	@Query("SELECT cfgExpTram FROM CfgExpedienteTramite cfgExpTram WHERE cfgExpTram.valorTipoExpediente.codigo =?1 and cfgExpTram.tipoTramite.codigo = ?2")
	public CfgExpedienteTramite findCfgExpTramiteByTipExpYTipTram(@Param("codTipoExpediente") String codTipoExpediente, @Param("codTipoTramite") String codTipoTramite);	
	
}
