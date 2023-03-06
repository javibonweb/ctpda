
package es.juntadeandalucia.ctpda.gestionpdt.repository;


import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.NotificacionesPendientesMaestra;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;
@Repository
public interface NotificacionesPendientesMaestraRepository extends AbstractCrudRepository<NotificacionesPendientesMaestra>,JoinedQDSLPredicateExecutorCustom<NotificacionesPendientesMaestra>,Serializable  {

	@Query("SELECT distinct notifPendiente.expediente.valorSituacionExpediente FROM NotificacionesPendientesMaestra notifPendiente WHERE notifPendiente.expediente.valorSituacionExpediente.activo = 1")
	public List<ValoresDominio> findSituacionesExpedienteByTramiteAbierto();	
	
}