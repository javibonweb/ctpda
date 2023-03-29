package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.CfgPlazosExpdte;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface CfgPlazosExpdteRepository extends AbstractCrudRepository<CfgPlazosExpdte>,JoinedQDSLPredicateExecutorCustom<CfgPlazosExpdte>,Serializable  {
	
	@Query("SELECT cfgPlazos FROM CfgPlazosExpdte cfgPlazos WHERE cfgPlazos.activo = 1 and cfgPlazos.valorTipoExpediente.id=?1  ORDER BY cfgPlazos.descripcionTipPlazo")
	List<CfgPlazosExpdte> findCfgPlazosByTipoExpediente(@Param("idTipoExp") long idTipoExp);
	
	@Query("SELECT cfgPlazos FROM CfgPlazosExpdte cfgPlazos WHERE cfgPlazos.activo = 1 and cfgPlazos.valorTipoExpediente.id=?1 and valorTipoPlazo.id =?2")
	CfgPlazosExpdte findCfgPlazosByTipExpTipPlazo(@Param("idTipoExp") long idTipoExp, @Param("idTipoPlazo") long idTipoPlazo);
}
