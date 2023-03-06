package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.TipoAgrupacion;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface TipoAgrupacionRepository extends AbstractCrudRepository<TipoAgrupacion>,JoinedQDSLPredicateExecutorCustom<TipoAgrupacion>,Serializable  {

	@Query("SELECT tipAgrup FROM TipoAgrupacion tipAgrup WHERE tipAgrup.activa = 1 ORDER BY tipAgrup.descripcion")
	public List<TipoAgrupacion> findTiposAgrupacionActivas();
	
	@Query("SELECT tipAgrup FROM TipoAgrupacion tipAgrup WHERE tipAgrup.tipoAgrupacionPadre.id = ?1 and tipAgrup.activa = 1")
	public List<TipoAgrupacion> findTiposAgrupacionesHijasActivas(@Param("id") long id);
	@Query("SELECT tipAgrup FROM TipoAgrupacion tipAgrup WHERE tipAgrup.nivelAnidamiento = 0 AND tipAgrup.activa = 1")
	public List<TipoAgrupacion> findTiposAgrupacionesActivaSinAnidamiento();
	
	@Query("SELECT tipAgrup FROM TipoAgrupacion tipAgrup WHERE tipAgrup.descripcion = ?1")
	public TipoAgrupacion obtenerTipoAgrupacionConDescripcion(@Param("desc") String descripcion);
}
