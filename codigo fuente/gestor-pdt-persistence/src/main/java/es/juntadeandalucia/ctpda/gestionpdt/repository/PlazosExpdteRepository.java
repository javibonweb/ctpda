package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.PlazosExpdte;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface PlazosExpdteRepository
		extends AbstractCrudRepository<PlazosExpdte>, JoinedQDSLPredicateExecutorCustom<PlazosExpdte>,Serializable  {
	
	@Query("SELECT plazosExp FROM PlazosExpdte plazosExp "
			+ "WHERE plazosExp.activo = 1 and plazosExp.cumplido = 0 and plazosExp.expediente.id=?1 "
			+ "ORDER BY plazosExp.fechaLimite ASC")
	public List<PlazosExpdte> plazosExpdteActivosNoCumplidosByExpediente(@Param("idExp") long idExp);	
	
	@Query("SELECT plazosExp FROM PlazosExpdte plazosExp "
			+ "WHERE plazosExp.activo = 1 and plazosExp.cumplido = 0 and plazosExp.expediente.id=?1 and plazosExp.valorTipoPlazo.id =?2 "
			+ "ORDER BY plazosExp.fechaLimite ASC")
	public PlazosExpdte findPlazosExpdteActivosNoCumplidosByExpTipPla(@Param("idExp") long idExp, @Param("idTipoPlazo") Long idTipoPlazo);	
	
	@Query("SELECT plazoExp FROM PlazosExpdte plazoExp WHERE plazoExp.activo = 1 and plazoExp.expediente.id =?1")
	public PlazosExpdte findPlazosExpdteByExp(@Param("idExp") Long idExp);
	
	@Query("SELECT plazoExp FROM PlazosExpdte plazoExp "
			+ "WHERE plazoExp.activo = 1 and plazoExp.cumplido = 0 and plazoExp.expediente.id =?1 and plazoExp.valorTipoPlazo.id =?2 ")
	public PlazosExpdte findPlazosExpdteByExpTipPla(@Param("idExp") Long idExp, @Param("idTipoPlazo") Long idTipoPlazo);
	
	@Query("SELECT plazoExp FROM PlazosExpdte plazoExp WHERE plazoExp.activo = 1 and plazoExp.cumplido = 0 and plazoExp.expediente.id =?1")
	public List<PlazosExpdte> findPlazosExpdteActivosYCumplidosByExpediente(@Param("idExpediente") Long idExpediente);
}
