package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.CfgDocExpedienteDescripcion;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface CfgDocExpedienteDescripcionRepository extends AbstractCrudRepository<CfgDocExpedienteDescripcion>,JoinedQDSLPredicateExecutorCustom<CfgDocExpedienteDescripcion>,Serializable  {

	List<CfgDocExpedienteDescripcion> findByCfgDocExpedienteTramitacionIdOrderByOrden(Long idCfgTr);
	
}
