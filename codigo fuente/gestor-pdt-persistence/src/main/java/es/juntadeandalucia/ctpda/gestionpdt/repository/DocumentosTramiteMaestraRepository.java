package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentosTramiteMaestra;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface DocumentosTramiteMaestraRepository extends AbstractCrudRepository<DocumentosTramiteMaestra>,JoinedQDSLPredicateExecutorCustom<DocumentosTramiteMaestra>,Serializable  {

	List<DocumentosTramiteMaestra> findByIdTramiteExpedienteAndIdAgrupacionExpedienteOrderByOrdenAgrupacionExp(Long idTram, Long idCat);
	List<DocumentosTramiteMaestra> findByIdTramiteExpedienteOrderByOrdenAgrupacionExp(Long idTram);
	
}
