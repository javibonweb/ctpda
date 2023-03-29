package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.ResolucionExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;
@Repository
public interface ResolucionExpedienteRepository extends AbstractCrudRepository<ResolucionExpediente>,JoinedQDSLPredicateExecutorCustom<ResolucionExpediente>,Serializable {

	public List<ResolucionExpediente> findByResolucionId(Long idResol);

	public ResolucionExpediente getByResolucionIdAndPrincipalTrue(Long idResol);
	
	@Query("SELECT resolExp FROM ResolucionExpediente resolExp WHERE resolExp.resolucion.id =?1")
	public ResolucionExpediente findResolucionExpByIdResolucion(@Param("idResol") Long idResol);
	
	@Query("SELECT resolExp FROM ResolucionExpediente resolExp WHERE resolExp.resolucion.id =?1")
	public List<ResolucionExpediente> findListResolucionExpByIdResolucion(@Param("idResol") Long idResol);
	
	@Query("SELECT resolExp FROM ResolucionExpediente resolExp WHERE resolExp.expediente.id =?1")
	public List<ResolucionExpediente> findListResolucionExpByIdExpediente(@Param("idExp") Long idExp);
	
	@Query("SELECT resolExp FROM ResolucionExpediente resolExp WHERE resolExp.expediente.id =?1")
	public ResolucionExpediente findResolucionExpByIdExpediente(@Param("idExp") Long idExp);
	
	
	@Query("SELECT re.expediente.id FROM ResolucionExpediente re WHERE re.resolucion.id =?1")
	public List<Long> findIdsExpRelacionadosByResolId(@Param("idResol") long idResol);
	
}