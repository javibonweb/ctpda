package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.CfgPlazosAut;
import es.juntadeandalucia.ctpda.gestionpdt.model.enums.OrigenAut;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface CfgPlazosAutRepository extends AbstractCrudRepository<CfgPlazosAut>,JoinedQDSLPredicateExecutorCustom<CfgPlazosAut>,Serializable  {
	
	@Query("SELECT plazoAut FROM CfgPlazosAut plazoAut WHERE plazoAut.activo = 1 and plazoAut.valorTipoExpediente.id =?1 and plazoAut.tipoTramite.id =?2 and plazoAut.tipoTramiteSuperior.id is null"
			+ " and plazoAut.origen = ?3 and plazoAut.valorPlazo.id = ?4 and plazoAut.accionEspecial = ?5")
	public List<CfgPlazosAut> findCfgPlazosAutSinTramSup(	@Param("idValDomTipoExp") Long idValDomTipoExp, @Param("idTipoTram") Long idTipoTram,
												@Param("origen") OrigenAut origen, @Param("idPlazo") Long idPlazo, @Param("accionEspecial") String accionEspecial);
	
	@Query("SELECT plazoAut FROM CfgPlazosAut plazoAut WHERE plazoAut.activo = 1 and plazoAut.valorTipoExpediente.id =?1 and plazoAut.tipoTramite.id =?2 and plazoAut.tipoTramiteSuperior.id is null"
			+ " and plazoAut.origen = ?3 and plazoAut.valorPlazo.id = ?4 and plazoAut.accionEspecial is null")
	public List<CfgPlazosAut> findCfgPlazosAutSinTramSup(	@Param("idValDomTipoExp") Long idValDomTipoExp, @Param("idTipoTram") Long idTipoTram,
												@Param("origen") OrigenAut origen, @Param("idPlazo") Long idPlazo);	
	
	@Query("SELECT plazoAut FROM CfgPlazosAut plazoAut WHERE plazoAut.activo = 1 and plazoAut.valorTipoExpediente.id =?1 and plazoAut.tipoTramite.id =?2 and plazoAut.tipoTramiteSuperior.id = ?3"
			+ " and plazoAut.origen = ?4 and plazoAut.valorPlazo.id = ?5 and plazoAut.tipoAccion = ?6")
	public List<CfgPlazosAut> findCfgPlazosAutTramSup(	@Param("idValDomTipoExp") Long idValDomTipoExp, @Param("idTipoTram") Long idTipoTram, @Param("idTipoTramSup") Long idTipoTramSup,
													@Param("origen") OrigenAut origen, @Param("idPlazo") Long idPlazo, @Param("accionEspecial") String accionEspecial);
	
	@Query("SELECT plazoAut FROM CfgPlazosAut plazoAut WHERE plazoAut.activo = 1 and plazoAut.valorTipoExpediente.id =?1 and plazoAut.tipoTramite.id =?2 and plazoAut.valorPlazo.id = ?3")
	public CfgPlazosAut findCfgPlazosAutByTipExpTipTramTipPla(@Param("idValDomTipoExp") Long idValDomTipoExp, @Param("idTipoTram") Long idTipoTram,
															@Param("idValDomTipPlazo") Long idValDomTipPlazo);	
	
	@Query("SELECT plazoAut FROM CfgPlazosAut plazoAut WHERE plazoAut.activo = 1 and plazoAut.valorTipoExpediente.id =?1 and plazoAut.tipoTramite.id is null and plazoAut.tipoTramiteSuperior.id is null"
			+ " and plazoAut.origen = ?2 and plazoAut.valorPlazo.id = ?3 and plazoAut.accionEspecial is null")
	public List<CfgPlazosAut> findCfgPlazosAutSinTramite(	@Param("idValDomTipoExp") Long idValDomTipoExp, 
												@Param("origen") OrigenAut origen, @Param("idPlazo") Long idPlazo);
	
	@Query("SELECT plazoAut FROM CfgPlazosAut plazoAut WHERE plazoAut.activo = 1 and plazoAut.valorTipoExpediente.id =?1 and plazoAut.tipoTramite.id is null and plazoAut.tipoTramiteSuperior.id is null"
			+ " and plazoAut.origen = ?2 and plazoAut.valorPlazo.id = ?3 and plazoAut.accionEspecial = ?4")
	public List<CfgPlazosAut> findCfgPlazosAutSinTramite(	@Param("idValDomTipoExp") Long idValDomTipoExp,
												@Param("origen") OrigenAut origen, @Param("idPlazo") Long idPlazo, @Param("accionEspecial") String accionEspecial);

}
