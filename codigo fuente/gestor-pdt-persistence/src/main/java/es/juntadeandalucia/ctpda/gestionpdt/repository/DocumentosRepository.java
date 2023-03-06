package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.Documentos;
import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface DocumentosRepository extends AbstractCrudRepository<Documentos>,JoinedQDSLPredicateExecutorCustom<Documentos>,Serializable {
	
	Documentos getByIdentificadorDoc(String iden);

	@EntityGraph(value="documento.basico", type = EntityGraphType.FETCH)
	@Query("SELECT d FROM Documentos d WHERE d.id = :idDoc") 
	DocumentosExpedientes getDocumentoBasico(@Param("idDoc")Long idDoc);
	
	@Query("SELECT doc FROM Documentos doc WHERE doc.id=?1")
	public List<Documentos> obtenerListaDocById(@Param("idExp") long idExp);

	@Query("SELECT d.bytes FROM Documentos d WHERE d.id =?1")
	byte[] getBytesDocumento(@Param("idDoc")Long idDoc);
	
}
