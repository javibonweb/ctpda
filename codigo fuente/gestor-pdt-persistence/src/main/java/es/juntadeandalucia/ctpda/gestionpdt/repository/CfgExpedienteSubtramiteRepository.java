package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.CfgExpedienteSubtramite;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface CfgExpedienteSubtramiteRepository extends AbstractCrudRepository<CfgExpedienteSubtramite>,JoinedQDSLPredicateExecutorCustom<CfgExpedienteSubtramite>,Serializable  {
	
	@Query("SELECT expSubTram FROM CfgExpedienteSubtramite expSubTram WHERE expSubTram.tipoSubtramite.id =?1 AND expSubTram.valorTipoExpediente.id =?2 ORDER BY expSubTram.descripcion")
	List<CfgExpedienteSubtramite> findSubTramites(@Param("idTipTram") Long idTipTram,@Param("idTipExp") Long idTipExp);


}
