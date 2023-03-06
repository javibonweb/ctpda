package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;
@Repository
public interface ValoresDominioRepository
		extends AbstractCrudRepository<ValoresDominio>, JoinedQDSLPredicateExecutorCustom<ValoresDominio>,Serializable  {
		
	@Query("SELECT valDom FROM ValoresDominio valDom WHERE valDom.activo=1 and valDom.dominio.id= (SELECT dominio FROM Dominio dominio WHERE dominio.codigo='TIP_PER')"
			+ "and (valDom.codigo='FIS' or valDom.codigo='JUR') ORDER BY valDom.descripcion")
	public List<ValoresDominio> findValoresDominioTipoPersonaFisicaYJuridica();
	
	@Query("SELECT valDom FROM ValoresDominio valDom WHERE valDom.activo=1 and valDom.dominio.id= (SELECT dominio FROM Dominio dominio WHERE dominio.codigo='SEX') ORDER BY valDom.descripcion")
	public List<ValoresDominio> findValoresSexo();
	
	@Query("SELECT valDom FROM ValoresDominio valDom WHERE valDom.activo=1 and valDom.dominio.id= (SELECT dominio FROM Dominio dominio WHERE dominio.codigo='TIP_EXP') ORDER BY valDom.descripcion")
	public List<ValoresDominio> findValoresTipoExpediente();
	
	@Query("SELECT valDom FROM ValoresDominio valDom WHERE valDom.activo=1 and valDom.dominio.id= (SELECT dominio FROM Dominio dominio WHERE dominio.codigo='TIP_EXP') and valDom.codigo != 'XPC' ORDER BY valDom.descripcion")
	public List<ValoresDominio> findValoresTipoExpedienteSinXPC();
	
	@Query("SELECT valDom FROM ValoresDominio valDom WHERE valDom.activo=1 and valDom.dominio.id= (SELECT dominio FROM Dominio dominio WHERE dominio.codigo='SIT') ORDER BY valDom.descripcion")
	public List<ValoresDominio> findValoresSituacion();

	@Query("SELECT DISTINCT(valDom) FROM ValoresDominio valDom, ValoresDominio valDomExp, SituacionesExpedientes sitExp WHERE valDom.activo=1 and (valDomExp.dominio.id= (SELECT dominio FROM Dominio dominio WHERE dominio.codigo='TIP_EXP')) and (valDom.dominio.id= (SELECT dominio FROM Dominio dominio WHERE dominio.codigo='SIT')) and (valDomExp.codigo = 'XPC') and valDom.id = sitExp.valorSituacion.id and valDomExp.id = sitExp.valorTipoExpediente.id ORDER BY valDom.descripcion")
	public List<ValoresDominio> findValoresSituacionXPC();
		
	@Query("SELECT valDom FROM ValoresDominio valDom WHERE valDom.id != ?1 and valDom.dominio.id=?2 and valDom.activo = 1 and UPPER(valDom.descripcion) = UPPER(?3)")
	List<ValoresDominio> findValoresDominioByIdDescripcion(@Param("idValDom") Long idValDom, @Param("idDom") Long idDom, @Param("desc") String desc);
	
	@Query("SELECT valDom FROM ValoresDominio valDom WHERE valDom.id != ?1 and valDom.dominio.id=?2 and valDom.activo = 1 and UPPER(valDom.codigo) = UPPER(?3)")
	List<ValoresDominio> findValoresDominioByIdCodigo(@Param("idValDom") Long idValDom, @Param("idDom") Long idDom, @Param("cod") String cod);
	
	@Query("SELECT valDom FROM ValoresDominio valDom WHERE valDom.activo = 1 and valDom.dominio.id=?1 and UPPER(valDom.descripcion) = UPPER(?2)")
	List<ValoresDominio> findValoresDominioByDescripcion(@Param("idDom") Long idDom, @Param("desc") String desc);
	
	@Query("SELECT valDom FROM ValoresDominio valDom WHERE valDom.dominio.id=?1 and valDom.activo = 1 and UPPER(valDom.codigo) = UPPER(?2)")
	List<ValoresDominio> findValoresDominioByCodigo(@Param("idDom") Long idDom, @Param("cod") String cod);
	
	@Query("SELECT valDom FROM ValoresDominio valDom WHERE valDom.id=?1")
	ValoresDominio findValorDominioById(@Param("idDom") Long idDom);
	
	
	@Query("SELECT valDomi FROM ValoresDominio valDomi WHERE valDomi.activo = 1 and valDomi.dominio.id=?1 ORDER BY valDomi.descripcion")
	List<ValoresDominio> findValoresDominioActivosByDominio(Long idDominio);
	
	@Query("SELECT valDom FROM ValoresDominio valDom WHERE valDom.activo=1 and valDom.dominio.id= (SELECT dominio FROM Dominio dominio WHERE dominio.codigo='TIP_INF') ORDER BY valDom.descripcion")
	public List<ValoresDominio> findValoresInfraccion();
	
	@Query("SELECT valDom FROM ValoresDominio valDom WHERE valDom.activo=1 and valDom.dominio.id= (SELECT dominio FROM Dominio dominio WHERE dominio.codigo='MAT') ORDER BY valDom.descripcion")
	public List<ValoresDominio> findValoresMaterias();
  
	@Query("SELECT valDom FROM ValoresDominio valDom WHERE valDom.activo=1 and valDom.dominio.id= (SELECT dominio FROM Dominio dominio WHERE dominio.codigo='CAN_ENT') ORDER BY valDom.descripcion")
	public List<ValoresDominio> findValoresCanalEntrada();

	@Query("SELECT valDom FROM ValoresDominio valDom WHERE valDom.activo=1 and valDom.dominio.id= (SELECT dominio FROM Dominio dominio WHERE dominio.codigo='MOT_REL_EXP_PER')  ORDER BY valDom.orden")
	public List<ValoresDominio> findValoresMotivosRelacionPersonaExpediente();
	
	@Query("SELECT valDom FROM ValoresDominio valDom WHERE valDom.activo=1 and valDom.dominio.id= (SELECT dominio FROM Dominio dominio WHERE dominio.codigo='MOT_REL_EXP_SUJ') ORDER BY valDom.orden")
	public List<ValoresDominio> findValoresMotivosRelacionSujetosExpediente();
		
	@Query("SELECT valDomi FROM ValoresDominio valDomi WHERE valDomi.activo = 1 and valDomi.dominio = (select dom from Dominio dom where dom.codigo =?1) ORDER BY valDomi.descripcion")
	List<ValoresDominio> findValoresDominioActivosByCodigoDominio(@Param("cod") String codigoDominio);
	
	@Query("SELECT valDomi FROM ValoresDominio valDomi WHERE valDomi.activo = 1 and valDomi.dominio = (select dom from Dominio dom where dom.codigo =?1) ORDER BY valDomi.orden")
	List<ValoresDominio> findValoresDominioActivosByCodigoDominioOrden(@Param("cod") String codigoDominio);
	
	@Query("SELECT valDomi.descripcion FROM ValoresDominio valDomi WHERE valDomi.activo = 1 and valDomi.dominio = (select dom from Dominio dom where dom.codigo =?1)")
	List<String> findValoresDominioDescActivosByCodigoDominio(@Param("cod") String codigoDominio);
	
	@Query("SELECT valDom FROM ValoresDominio valDom WHERE valDom.dominio.id = ?1 and valDom.valorDominioPadre.id = ?2 and valDom.activo = 1")
	public List<ValoresDominio> findValoresDominioHijosActivos(@Param("idDominio") long idDominio,@Param("idValorDominio") long idValorDominio);
	
	@Query("SELECT valDom FROM ValoresDominio valDom WHERE valDom.orden = ?1  and valDom.valorDominioPadre.id=null and valDom.dominio.id =?2")
	public List<ValoresDominio> obtenerValoresDominioNullConMismoOrdenVisualizacion(@Param("orden") long ordenVisualizacion, @Param("dominioId") long dominioId);
	
	@Query("SELECT valDom FROM ValoresDominio valDom WHERE valDom.orden = ?1  and valDom.valorDominioPadre.id=null and valDom.dominio.id =?2 and valDom.id != ?3")
	public List<ValoresDominio> obtenerValoresDominioModNullConMismoOrdenVisualizacion(@Param("orden") long ordenVisualizacion, @Param("dominioId") long dominioId, @Param("valDomId") long valDomId);
	
	@Query("SELECT valDom FROM ValoresDominio valDom WHERE valDom.orden = ?1  and valDom.dominio.id =?2")
	public List<ValoresDominio> obtenerValorDominioConMismoOrdenVisualizacionSinAnidamiento(@Param("orden") long ordenVisualizacion, @Param("dominioId") long dominioId);
	
	@Query("SELECT valDom FROM ValoresDominio valDom WHERE valDom.orden = ?1  and valDom.dominio.id =?2 and valDom.id !=?3 ")
	public List<ValoresDominio> obtenerValorDominioModConMismoOrdenVisualizacionSinAnidamiento(@Param("orden") long ordenVisualizacion, @Param("dominioId") long dominioId, @Param("valDomId") long valDomId);
	
	@Query("SELECT valDom FROM ValoresDominio valDom WHERE valDom.orden = ?1 and valDom.valorDominioPadre.id=?2 and valDom.dominio.id !=?3")
	public List<ValoresDominio> obtenerValorDominioConMismoOrdenVisualizacion(@Param("orden") long ordenVisualizacion,
			@Param("valDomPadreId") long valDomPadreId, @Param("dominioId") long dominioId);
	
	@Query("SELECT valDom FROM ValoresDominio valDom WHERE valDom.orden = ?1 and valDom.valorDominioPadre.id=?2 and valDom.dominio.id =?3 and valDom.id != ?4")
	public List<ValoresDominio> obtenerValorDominioModConMismoOrdenVisualizacion(@Param("orden") long ordenVisualizacion,
			@Param("valDomPadreId") long valDomPadreId, @Param("dominioId") long dominioId,@Param("valDomId")  long valDomId);
	
	@Query("SELECT valDom FROM ValoresDominio valDom WHERE valDom.activo=1 and valDom.dominio.id= (SELECT dominio FROM Dominio dominio WHERE dominio.codigo = UPPER(?1)) ORDER BY valDom.descripcion")
	public List<ValoresDominio> findValoresDominioByCodigoDominio(@Param("codDom") String codDom);
	
	@Query("SELECT valDom FROM ValoresDominio valDom WHERE valDom.activo=1 and valDom.dominio.id= (SELECT dominio FROM Dominio dominio WHERE dominio.codigo = UPPER(?1)) "
			+ "and valDom.codigo =?2")
	public ValoresDominio findValoresDominioByCodigoDomCodValDom(@Param("codDom") String codDom, @Param("codValDom") String codValDom);
	
	@Query("SELECT valDom FROM ValoresDominio valDom WHERE valDom.activo=1 and valDom.id != ?1 and valDom.id != ?2 and valDom.dominio.id= (SELECT dominio FROM Dominio dominio WHERE dominio.codigo='TIP_EXP') ORDER BY valDom.descripcion")
	public List<ValoresDominio> findValoresTipoExpedienteNoActualNoParam(@Param("idValDomTipExpActual") Long idValDomTipExpActual, @Param("idNoTipExp") Long idNoTipExp);
	
	@Query("SELECT valDomi.id FROM ValoresDominio valDomi WHERE valDomi.activo = 1 and valDomi.dominio = (select dom from Dominio dom where dom.codigo =?1) AND valDomi.codigo IN ('EXP', 'TRA') ORDER BY valDomi.descripcion")
	List<Long> findIdsValDomActivosTramExp(@Param("cod") String codigoDominio);

	
	
}
