package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.TareasExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface TareasExpedienteRepository extends AbstractCrudRepository<TareasExpediente>,JoinedQDSLPredicateExecutorCustom<TareasExpediente>,Serializable  {

	public List<TareasExpediente> findByExpedienteIdAndActivaAndSituacion(Long idExp, Boolean activa, String situacion);
	public List<TareasExpediente> findByTramiteExpedienteIdAndActivaAndSituacion(Long idTramite, Boolean activa, String situacion);
	
	public List<TareasExpediente> findByExpedienteIdAndResponsableTramitacionIdAndActivaAndSituacion(Long idExp, Long idResp, Boolean activa, String situacion);
	public List<TareasExpediente> findByTramiteExpedienteIdAndResponsableTramitacionIdAndActivaAndSituacion(Long idTramite, Long idResp, Boolean activa, String situacion);
	
	@Query("SELECT tar FROM TareasExpediente tar WHERE tar.expediente.id = ?1 and tar.activa = 1")
	public List<TareasExpediente> findByExpedienteIdAndActiva(Long idExpediente);
	
	//------------------------------------
	
	//Todas las tareas con idExp
	@Query("SELECT CASE WHEN count(t)> 0 THEN true ELSE false END "
			+ "FROM TareasExpediente t "
			+ "WHERE t.expediente.id = ?1 "
			+ "and t.activa = TRUE and t.situacion = '" + TareasExpediente.SITUACION_PENDIENTE + "'")
	public boolean existeTareaPendienteExpediente(Long idExp);
		
	//---------------
	
	//Todas las tareas con idTram
	@Query("SELECT CASE WHEN count(t)> 0 THEN true ELSE false END "
			+ "FROM TareasExpediente t "
			+ "WHERE t.tramiteExpediente.id = ?1 "
			+ "and t.activa = TRUE and t.situacion = '" + TareasExpediente.SITUACION_PENDIENTE + "'")
	public boolean existeTareaPendienteTramite(Long idTram);
	
	//---------------
	
	//Todas las tareas con idExp
	//Que tengan código de tipo = codTipoTarea
	@Query("SELECT CASE WHEN count(t)> 0 THEN true ELSE false END "
			+ "FROM TareasExpediente t "
			+ "WHERE t.expediente.id = ?1 "
			+ "and t.valorTipoTarea.codigo = ?2 "
			+ "and t.activa = TRUE and t.situacion = '" + TareasExpediente.SITUACION_PENDIENTE + "'")
	public boolean existeTareaPendienteExpediente(Long idExp, String codTipoTarea);
		
	//---------------
	
	//Todas las tareas con idTram
	//Que tengan código de tipo = codTipoTarea
	@Query("SELECT CASE WHEN count(t)> 0 THEN true ELSE false END "
			+ "FROM TareasExpediente t "
			+ "WHERE t.tramiteExpediente.id = ?1 "
			+ "and t.valorTipoTarea.codigo = ?2 "
			+ "and t.activa = TRUE and t.situacion = '" + TareasExpediente.SITUACION_PENDIENTE + "'")
	public boolean existeTareaPendienteTramite(Long idTram, String codTipoTarea);
	
	//---------------
	
	//Todas las tareas con idExp
	//Que tengan responsable con idResp
	@Query("SELECT CASE WHEN count(t)> 0 THEN true ELSE false END "
			+ "FROM TareasExpediente t "
			+ "WHERE t.expediente.id = ?1 "
			+ "and t.responsableTramitacion.id = ?2 "
			+ "and t.activa = TRUE and t.situacion = '" + TareasExpediente.SITUACION_PENDIENTE + "'")
	public boolean existeTareaPendienteExpedienteDeResponsable(Long idExp, Long idResp);
		
	//---------------
	
	//Todas las tareas con idTram
	//Que tengan responsable con idResp
	@Query("SELECT CASE WHEN count(t)> 0 THEN true ELSE false END "
			+ "FROM TareasExpediente t "
			+ "WHERE t.tramiteExpediente.id = ?1 "
			+ "and t.responsableTramitacion.id = ?2 "
			+ "and t.activa = TRUE and t.situacion = '" + TareasExpediente.SITUACION_PENDIENTE + "'")
	public boolean existeTareaPendienteTramiteDeResponsable(Long idTram, Long idResp);
	
	//---------------
	
	//Todas las tareas con doc-exp-tram que tengan el documento idDoc
	//Tarea -> d-e-t -> doc-exp -> doc		
	@Query("SELECT CASE WHEN count(t)> 0 THEN true ELSE false END "
			+ "FROM TareasExpediente t "
			+ "WHERE t.documentoExpedienteTramite.documentoExpediente.documento.id = ?1 "
			+ "and t.valorTipoTarea.codigo = ?2 "
			+ "and t.activa = TRUE and t.situacion = '" + TareasExpediente.SITUACION_PENDIENTE + "'")
	public boolean existeTareaPendienteDocumento(Long idDoc, String codTipoTarea);
	
	//Igual que lo anterior, pero sin condición de activa y situación
	//Devuelve true si tiene una tarea registrada, esté como esté.
	@Query("SELECT CASE WHEN count(t)> 0 THEN true ELSE false END "
			+ "FROM TareasExpediente t "
			+ "WHERE t.documentoExpedienteTramite.documentoExpediente.documento.id = ?1 "
			+ "and t.valorTipoTarea.codigo = ?2 ")
	public boolean existeTareaDocumento(Long idDoc, String codTipoTarea);
		
	//-------------------------------------------
	
	@Query("SELECT t "
			+ "FROM TareasExpediente t "
			+ "WHERE t.documentoExpedienteTramite.documentoExpediente.documento.id = ?1 "
			+ "and t.valorTipoTarea.codigo = ?2 "
			+ "and t.activa = TRUE and t.situacion = '" + TareasExpediente.SITUACION_PENDIENTE + "'")
	public TareasExpediente getTareaPendienteDocumento(Long idDoc, String codTipoTarea);
			
	@Query("SELECT t "
			+ "FROM TareasExpediente t "
			+ "WHERE t.tramiteExpediente.id = ?1 "
			+ "and t.valorTipoTarea.codigo = ?2 "
			+ "and t.activa = TRUE and t.situacion = '" + TareasExpediente.SITUACION_PENDIENTE + "'")
	public TareasExpediente getTareaPendienteTramite(Long idDoc, String codTipoTarea);
	
	@Query("SELECT tar FROM TareasExpediente tar WHERE tar.expediente.id =?1 and tar.activa = 1 and tar.situacion =?2")
	public List<TareasExpediente> findTareasExpActivasByExpedienteYSituacion(@Param("idExpediente") Long idExpediente, @Param("codSituacion") String codSituacion);
	
	@Query("SELECT tarExp FROM TareasExpediente tarExp WHERE tarExp.tramiteExpediente.id =?1 and tarExp.valorTipoTarea.id =?2 and tarExp.situacion = '"  + TareasExpediente.SITUACION_PENDIENTE + "'")
	public List<TareasExpediente> findTareasExpActivasByTramExpTipTar(@Param("idTramExp") Long idTramExp, @Param("idTipTar") Long idTipTar);
	
}
