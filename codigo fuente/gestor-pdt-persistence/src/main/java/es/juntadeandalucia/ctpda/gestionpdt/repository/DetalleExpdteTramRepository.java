package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.DetalleExpdteTram;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface DetalleExpdteTramRepository extends AbstractCrudRepository<DetalleExpdteTram>,JoinedQDSLPredicateExecutorCustom<DetalleExpdteTram>,Serializable  {
	
	@EntityGraph("detalleTram.completo")
	@Query("SELECT detTramExp FROM DetalleExpdteTram detTramExp WHERE detTramExp.id =?1")
	public DetalleExpdteTram obtenerObjeto(@Param("idDetExpTr") Long idDetExpTr);
	
	@Query("SELECT detTramExp FROM DetalleExpdteTram detTramExp WHERE detTramExp.activo=1 AND detTramExp.expediente.id =?1 AND detTramExp.tramiteExpediente.id=?2")
	public DetalleExpdteTram findDetalleTramiteExp(@Param("idExp") Long idExp, @Param("idTramExp") Long idTramExp);
	
	@Query("SELECT detTramExp FROM DetalleExpdteTram detTramExp WHERE detTramExp.activo=1 AND detTramExp.expediente.id =?1 AND detTramExp.tramiteExpediente.id=?2 AND detTramExp.afectaPlazos = 1")
	public List<DetalleExpdteTram> findDetalleTramiteExpAfectaPlazos(@Param("idExp") Long idExp, @Param("idTramExp") Long idTramExp);
	
	@Query("SELECT detTramExp FROM DetalleExpdteTram detTramExp WHERE detTramExp.activo=1 AND detTramExp.expediente.id =?1 AND detTramExp.tramiteExpediente.tipoTramite.codigo=?2 AND detTramExp.afectaPlazos = 1")
	public List<DetalleExpdteTram> findDetalleTramExpSubsanaAfectaPlazos(@Param("idExp") Long idExp, @Param("codTipoTram") String codTipoTram);
	
	@Query("SELECT detTramExp FROM DetalleExpdteTram detTramExp WHERE detTramExp.activo=1 AND detTramExp.expediente.id =?1 AND detTramExp.tramiteExpediente.tipoTramite.codigo=?2 AND detTramExp.afectaPlazos = 1")
	public List<DetalleExpdteTram> findDetalleTramExpAfectaPlazosByCodTipoTram(@Param("idExp") Long idExp, @Param("codTipoTram") String codTipoTram);
	
	@Query("SELECT detTramExp FROM DetalleExpdteTram detTramExp WHERE detTramExp.expediente.id =?1 and detTramExp.activo=1")
	public List<DetalleExpdteTram> findAllDetalleTramExpByExp(@Param("idExp") Long idExp);

	@Query("SELECT detTramExp FROM DetalleExpdteTram detTramExp WHERE detTramExp.expediente.id =?1 and detTramExp.extractoExpediente=1 order by detTramExp.id")
	public List<DetalleExpdteTram> findDetalleTramExpByExpConExtractos(@Param("idExp") Long idExp);
	
	@Query("SELECT detTramExp FROM DetalleExpdteTram detTramExp WHERE detTramExp.expediente.id =?1 and detTramExp.antecedentesExpediente=1  order by detTramExp.id")
	public List<DetalleExpdteTram> findDetalleTramExpByExpConAntec(@Param("idExp") Long idExp);
	
	@Query("SELECT detTramExp FROM DetalleExpdteTram detTramExp WHERE detTramExp.activo = 1 and detTramExp.expediente.id =?1 and detTramExp.tramiteExpediente.tipoTramite.codigo =?2 "
			+ "and (detTramExp.valorResultadoNotificacion.codigo =?3 or detTramExp.valorResultadoNotificacion.codigo =?4)")
	public List<DetalleExpdteTram> findDetalleTramExpActivoByExpedienteYTramiteNotifYResultNotif(@Param("idExpediente") Long idExpediente, @Param("codTipoTramite") String codTipoTramite,
			@Param("codResultadoNotif1") String codResultadoNotif1, @Param("codResultadoNotif2") String codResultadoNotif2);
	
	@Query("SELECT detTramExp FROM DetalleExpdteTram detTramExp WHERE detTramExp.activo=1 and detTramExp.expediente.id =?1 and detTramExp.tramiteExpediente.tipoTramite.codigo =?2 and detTramExp.tramiteExpediente.tramiteExpedienteSup.tipoTramite.codigo =?3")
	public List<DetalleExpdteTram> findSubtramitesByExpYTipSubtramiteYTipTramiteSup(@Param("idExpediente") Long idExpediente, @Param("codTipoSubTramite") String codTipoSubTramite, @Param("codTipoTramiteSup") String codTipoTramiteSup);
		
	@Query("SELECT MAX(detTramExp.fechaNotificacion) FROM DetalleExpdteTram detTramExp WHERE detTramExp.activo=1 and detTramExp.expediente.id =?1 and detTramExp.tramiteExpediente.tipoTramite.codigo =?2 and detTramExp.tramiteExpediente.tramiteExpedienteSup.tipoTramite.codigo =?3")
	public Date findFechaMayorNotifDetalleExpActivoByExpedienteAndTipoTramiteSup(@Param("idExpediente") Long idExpediente, @Param("codTipoSubTramite") String codTipoSubTramite, @Param("codTipoTramiteSup") String codTipoTramiteSup);

	@Query("SELECT MAX(detTramExp.fechaNotificacion) FROM DetalleExpdteTram detTramExp WHERE detTramExp.expediente.id =?1 and detTramExp.activo =1 and (detTramExp.tramiteExpediente.tramiteExpedienteSup.tipoTramite.codigo =?2 or detTramExp.tramiteExpediente.tramiteExpedienteSup.tipoTramite.codigo =?3)")
	public Date findFechaMayorNotifDetalleExpActivoByExpediente(@Param("idExpediente") Long idExpediente, @Param("codTipoTramite1") String codTipoTramite1, @Param("codTipoTramite2") String codTipoTramite2);

	@Query("SELECT MAX(detTramExp.fechaEnvio) FROM DetalleExpdteTram detTramExp WHERE detTramExp.activo=1 and detTramExp.expediente.id =?1 and detTramExp.tramiteExpediente.tipoTramite.codigo =?2 and detTramExp.tramiteExpediente.tramiteExpedienteSup.tipoTramite.codigo =?3")
	public Date findFechaMayorEnvioDetalleExpActivoByExpedienteAndTipoTramiteSup(@Param("idExpediente") Long idExpediente, @Param("codTipoSubTramite") String codTipoSubTramite, @Param("codTipoTramiteSup") String codTipoTramiteSup);
}
