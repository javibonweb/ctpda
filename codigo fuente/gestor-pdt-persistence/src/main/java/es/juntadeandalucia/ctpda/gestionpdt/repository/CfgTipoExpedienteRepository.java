package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.CfgTipoExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface CfgTipoExpedienteRepository extends AbstractCrudRepository<CfgTipoExpediente>,JoinedQDSLPredicateExecutorCustom<CfgTipoExpediente>,Serializable {
	
	@Query("SELECT cfgTe FROM CfgTipoExpediente cfgTe WHERE cfgTe.valorTipoExpediente.id=?1")
	public CfgTipoExpediente obtenerCfgTipoExpedientePorValorTipoExpediente(@Param("id") long id);
	
	@Query("SELECT cfgTe FROM CfgTipoExpediente cfgTe WHERE cfgTe.valorTipoExpediente.codigo=?1")
	public CfgTipoExpediente obtenerCfgTipoExpedienteByCodigoTipoExp(@Param("codigoTipoExp") String codigoTipoExp);

}
