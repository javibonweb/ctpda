package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.PlantillasDocumentos;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface PlantillasDocumentosRepository extends AbstractCrudRepository<PlantillasDocumentos>,JoinedQDSLPredicateExecutorCustom<PlantillasDocumentos>,Serializable  {
	
	@EntityGraph("plantillaDoc.listado")
	@Query("SELECT pd FROM PlantillasDocumentos pd "
			+ "WHERE pd.cfgDocExpedienteTramitacion.id = :idTipoDocCfg "
			+ "AND pd.activo = TRUE "
			+ "ORDER BY pd.plantilla.descripcion")
	List<PlantillasDocumentos> findByCfgId(@Param("idTipoDocCfg") Long idTipoDocCfg);
	
}
