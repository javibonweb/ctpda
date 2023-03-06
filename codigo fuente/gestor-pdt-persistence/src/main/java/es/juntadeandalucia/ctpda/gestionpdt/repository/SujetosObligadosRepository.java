package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.SujetosObligados;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface SujetosObligadosRepository
		extends AbstractCrudRepository<SujetosObligados>, JoinedQDSLPredicateExecutorCustom<SujetosObligados>,Serializable  {

	@Query("SELECT sujObli FROM SujetosObligados sujObli WHERE sujObli.activa = 1 ORDER BY sujObli.descripcion")
	public List<SujetosObligados> findSujetosObligadosActivos();

	@Query("SELECT sujObli FROM SujetosObligados sujObli WHERE sujObli.sujetosObligadosPadre.id = ?1 and sujObli.activa = 1")
	public List<SujetosObligados> findSujetosObligadosHijosActivos(@Param("id") long id);
	
	@Query("SELECT sujObli FROM SujetosObligados sujObli WHERE sujObli.valorTipoIdentificador.id = ?1 and sujObli.activa = 1")
	public List<SujetosObligados> findSujetosObligadosByTipIdentif(@Param("idTipIdentif") long idTipIdentif);

	@Query("SELECT sujObli FROM SujetosObligados sujObli WHERE sujObli.descripcion = ?1")
	public SujetosObligados obtenerSujetosObligadosConDescripcion(@Param("desc") String descripcion);

	@Query("SELECT sujObli FROM SujetosObligados sujObli WHERE sujObli.ordenVisualizacion = ?1 and sujObli.sujetosObligadosPadre.id=?2 and sujObli.tipoAgrupacion.id =?3")
	public SujetosObligados obtenerSujetoConMismoOrdenVisualizacion(@Param("orden") String ordenVisualizacion,
			@Param("padreId") long padreId, @Param("tipAgrupId") long tipAgrupId);

	@Query("SELECT sujObli FROM SujetosObligados sujObli WHERE sujObli.ordenVisualizacion = ?1  and sujObli.sujetosObligadosPadre.id=null and sujObli.tipoAgrupacion.id =?2")
	public SujetosObligados obtenerSujetoNullConMismoOrdenVisualizacion(@Param("orden") String ordenVisualizacion, @Param("tipAgrupId") long tipAgrupId);
	
	@Query("SELECT sujObli.sujetosObligadosPadre.id FROM SujetosObligados sujObli WHERE sujObli.sujetosObligadosPadre.id is not null and sujObli.activa = 1")
	public List<Long> findSujetosObligadosActivosConPadre();

}
