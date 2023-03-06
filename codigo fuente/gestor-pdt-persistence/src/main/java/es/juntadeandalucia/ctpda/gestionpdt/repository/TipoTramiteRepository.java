package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.TipoTramite;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface TipoTramiteRepository extends AbstractCrudRepository<TipoTramite>,JoinedQDSLPredicateExecutorCustom<TipoTramite>,Serializable  {
	
	TipoTramite getByCodigo(String cod);
	
	List<TipoTramite> findByComportamiento(String comp);
	
	@Query("SELECT tipTram FROM TipoTramite tipTram WHERE tipTram.activo = 1 and tipTram.codigo = 'NOT'")
	public TipoTramite findTipoTramiteActivoNotif();
	
	@Query("SELECT tipTram FROM TipoTramite tipTram WHERE tipTram.activo = 1 and tipTram.codigo = 'FIRM'")
	public TipoTramite findTipoTramiteActivoFirm();
	
	@Query("SELECT tipTram FROM TipoTramite tipTram WHERE tipTram.activo = 1")
	public List<TipoTramite> findTipoTramitesActivos();
	
	@Query("SELECT tipTram FROM TipoTramite tipTram WHERE tipTram.activo = 1 order by tipTram.descripcion asc")
	public List<TipoTramite> findTipoTramitesActivosOrdenAlfab();
	
}
