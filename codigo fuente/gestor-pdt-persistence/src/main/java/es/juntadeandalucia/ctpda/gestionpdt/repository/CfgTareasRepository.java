package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.CfgTareas;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface CfgTareasRepository extends AbstractCrudRepository<CfgTareas>,JoinedQDSLPredicateExecutorCustom<CfgTareas>, CfgTareasRepositoryCustom,Serializable  {

	@Query("SELECT cfgTar FROM CfgTareas cfgTar WHERE cfgTar.valorTipoExpediente.id =?1 and cfgTar.tipoTramite.id =?2 and cfgTar.activo=1 "
			+ "and cfgTar.cierreSubTra = 1 and cfgTar.tipoSubtramite IS NULL")
	public List<CfgTareas> findTareasByTipExpTipTramTipSubTramNull(@Param("idTipoExp") Long idTipoExp, @Param("idTipoTram") Long idTipoTram);
	
}
