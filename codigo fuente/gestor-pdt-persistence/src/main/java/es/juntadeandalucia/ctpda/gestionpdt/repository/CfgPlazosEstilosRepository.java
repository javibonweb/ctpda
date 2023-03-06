package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.CfgPlazosEstilos;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface CfgPlazosEstilosRepository extends AbstractCrudRepository<CfgPlazosEstilos>,JoinedQDSLPredicateExecutorCustom<CfgPlazosEstilos>,Serializable  {
	
	@Query("SELECT cfg FROM CfgPlazosEstilos cfg WHERE cfg.valorTipoExpediente.id=?1 and cfg.valorTipoPlazo.id=?2 and (?3 between cfg.limiteInferior and cfg.limiteSuperior) and cfg.activo = 1")
	List<CfgPlazosEstilos> findEstiloByTipoExpTipoPlazoDiasRestantes(@Param("idTipoExp") Long idTipoExp, @Param("idTipoPlazo") Long idTipoPlazo, @Param("diasRestantes") Long diasRestantes);

}