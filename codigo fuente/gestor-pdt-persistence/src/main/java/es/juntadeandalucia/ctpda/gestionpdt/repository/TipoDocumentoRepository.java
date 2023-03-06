package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.TipoDocumento;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface TipoDocumentoRepository extends AbstractCrudRepository<TipoDocumento>,JoinedQDSLPredicateExecutorCustom<TipoDocumento>,Serializable {
		
	@Query("SELECT DISTINCT td FROM TipoDocumento td "
			+ "WHERE EXISTS (SELECT de FROM DocumentosExpedientes de "
			+ 				"WHERE de.documento.tipoDocumento.id = td.id AND "
			+  					"de.expediente.id = :idExp AND de.activo = TRUE) "
			+ "ORDER BY td.descripcion")
	List<TipoDocumento> findTiposDocumentoUsados(@Param("idExp") Long idExp);
	
	@Query("SELECT td FROM TipoDocumento td WHERE td.activo = TRUE ORDER BY td.descripcion")
	List<TipoDocumento> findTodos();

}
