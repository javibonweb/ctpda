package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.TipoTramite_DMS;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface TipoTramiteRepository_DMS extends AbstractCrudRepository<TipoTramite_DMS>,JoinedQDSLPredicateExecutorCustom<TipoTramite_DMS>,Serializable  {
	
	TipoTramite_DMS getByCodigo(String cod);
	
	List<TipoTramite_DMS> findByComportamiento(String comp);
	
	@Query("SELECT tipTram FROM TipoTramite tipTram WHERE tipTram.activo = 1 and tipTram.codigo = 'NOT'")
	public TipoTramite_DMS findTipoTramiteActivoNotif();
	
	@Query("SELECT tipTram FROM TipoTramite tipTram WHERE tipTram.activo = 1 and tipTram.codigo = 'FIRM'")
	public TipoTramite_DMS findTipoTramiteActivoFirm();
	
	@Query("SELECT tipTram FROM TipoTramite tipTram WHERE tipTram.activo = 1")
	public List<TipoTramite_DMS> findTipoTramitesActivos();
	
	@Query("SELECT tipTram FROM TipoTramite tipTram WHERE tipTram.activo = 1 order by tipTram.descripcion asc")
	public List<TipoTramite_DMS> findTipoTramitesActivosOrdenAlfab();
	
}
