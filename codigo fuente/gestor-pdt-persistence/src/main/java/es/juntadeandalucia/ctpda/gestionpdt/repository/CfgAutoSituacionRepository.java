package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.CfgAutoSituacion;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface CfgAutoSituacionRepository extends AbstractCrudRepository<CfgAutoSituacion>,JoinedQDSLPredicateExecutorCustom<CfgAutoSituacion>,Serializable  {

	List<CfgAutoSituacion> findByValorTipoExpedienteIdAndTipoTramiteIdAndValorSituacionOrigenIdAndCondicionAndOperacion(Long valorTipoExpedienteId, Long tipoTramiteId, Long valorSituacionOrigen, String condicion, String operacion);
	
	List<CfgAutoSituacion> findByValorTipoExpedienteIdAndTipoTramiteIdAndValorSituacionOrigenIdAndCondicionNullAndOperacion(Long valorTipoExpedienteId, Long tipoTramiteId, Long valorSituacionOrigen, String operacion);
	
}
