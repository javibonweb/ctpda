
package es.juntadeandalucia.ctpda.gestionpdt.repository;


import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.TipoTramite;
import es.juntadeandalucia.ctpda.gestionpdt.model.TramitesAbiertosMaestra;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;
@Repository
public interface TramitesAbiertosMaestraRepository extends AbstractCrudRepository<TramitesAbiertosMaestra>,JoinedQDSLPredicateExecutorCustom<TramitesAbiertosMaestra>,Serializable  {

	
	@Query("SELECT distinct tramAbierto.tipoTramite FROM TramitesAbiertosMaestra tramAbierto WHERE tramAbierto.tipoTramite.activo = 1 order by tramAbierto.tipoTramite.descripcion asc")
	public List<TipoTramite> findTiposTramitesByTramiteAbierto();
	
	@Query("SELECT distinct tramAbierto.expediente.valorSituacionExpediente FROM TramitesAbiertosMaestra tramAbierto WHERE tramAbierto.expediente.valorSituacionExpediente.activo = 1")
	public List<ValoresDominio> findSituacionesExpedienteByTramiteAbierto();
	
	
	@Query("SELECT tramAbierto.tipoTramite FROM TramitesAbiertosMaestra tramAbierto WHERE tramAbierto.tipoTramite.activo = 1 and tramAbierto.tipoTramite.codigo = 'NOT'")
	public TipoTramite findTiposTramitesNotif();
	
}