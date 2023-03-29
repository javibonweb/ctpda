package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentoResolucion;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;
@Repository
public interface DocumentoResolucionRepository extends AbstractCrudRepository<DocumentoResolucion>,JoinedQDSLPredicateExecutorCustom<DocumentoResolucion>,Serializable {

	List<DocumentoResolucion> findByResolucionId(Long idResol);

	@Query("SELECT docResol FROM DocumentoResolucion docResol WHERE docResol.resolucion.id=?1")
	List<DocumentoResolucion> findDocumentosResolucionByIdResol(@Param("idResol")Long idResol);
	
	@Query("SELECT docResol FROM DocumentoResolucion docResol WHERE docResol.resolucion.id=?1 AND docResol.documentoExpediente.id=?2")
	List<DocumentoResolucion> findDocumentosResolucionByIdResolIdDoc(@Param("idResol")Long idResol, @Param("idDoc")Long idDoc);
	
}