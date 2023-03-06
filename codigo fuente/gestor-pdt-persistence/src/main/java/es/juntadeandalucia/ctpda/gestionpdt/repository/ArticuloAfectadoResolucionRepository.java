package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.ArticuloAfectadoResolucion;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;
@Repository
public interface ArticuloAfectadoResolucionRepository extends AbstractCrudRepository<ArticuloAfectadoResolucion>,JoinedQDSLPredicateExecutorCustom<ArticuloAfectadoResolucion>,Serializable {
	
	public List<ArticuloAfectadoResolucion> findByResolucionId(Long idResol);

	@Query("SELECT artAfecResol FROM ArticuloAfectadoResolucion artAfecResol WHERE artAfecResol.resolucion.id =?1 and artAfecResol.valorArticulo.id=?2")
	public ArticuloAfectadoResolucion findArtAfecResolucionExpByIdResolIdArtAdfec(@Param("idResol") Long idResol, @Param("idArtAfec") Long idArtAfec);
}