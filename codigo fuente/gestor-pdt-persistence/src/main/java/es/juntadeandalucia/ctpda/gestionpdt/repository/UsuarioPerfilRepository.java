package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.UsuarioPerfil;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;
@Repository

public interface UsuarioPerfilRepository extends AbstractCrudRepository<UsuarioPerfil>, JoinedQDSLPredicateExecutorCustom<UsuarioPerfil>,Serializable  {
	@Query("SELECT usuPerf FROM UsuarioPerfil usuPerf WHERE usuPerf.id =?1 and usuPerf.activa = 1")
	public List<UsuarioPerfil> findPerfilesUsuarioActivos(@Param("idUsuario") Long idUsuario);
	
	
	/**
	 * SELECT perm.* FROM ge_usuarios_perfiles usuPerf inner join ge_permisos_perfiles permperf on usuperf.usuper_per_id = permperf.permperf_perf_id
INNER JOIN GE_PERMISOS perm on permperf.permperf_perm_id = perm.per_id
where usuperf.usuper_usu_id = 1 and usuperf.usuper_per_id = (select perf.per_id from ge_perfiles perf where perf.c_codigo = 'ADMIN') 
and perm.c_codigo = 'FORM_EXP';

	 * */
	
	@Query("SELECT perm.codigo FROM UsuarioPerfil usuPerf INNER JOIN PermisoPerfil permPerf ON usuPerf.perfil.id = permPerf.perfil.id"
			+ " INNER JOIN Permiso perm ON permPerf.permiso.id = perm.id"
			
			+ " WHERE usuPerf.usuario.id =?1 and usuPerf.perfil = (SELECT perf.id FROM Perfil perf WHERE perf.codigo = ?2)"
			+ " AND perm.codigo = ?3")
	public String findPermisoRequerido(@Param("idUsuario") Long idUsuario, @Param("codigoPerfil") String codigoPerfil, @Param("codigoPermiso") String codigoPermiso);
	
	@Query("SELECT usuPerf.perfil.id FROM UsuarioPerfil usuPerf WHERE usuPerf.usuario.id =?1 and usuPerf.activa = 1")
	public List<Long> findPerfilesUsuarioActivosPorUsuario(@Param("idUsuario") Long idUsuario);
	
	@Query("SELECT usuPerf FROM UsuarioPerfil usuPerf WHERE usuPerf.usuario.id =?1 and usuPerf.activa = 1")
	public List<UsuarioPerfil> findListaPerfilesUsuarioActivosPorUsuario(@Param("idUsuario") Long idUsuario);
	
	@Query("SELECT usuPerf FROM UsuarioPerfil usuPerf WHERE usuPerf.usuario.id =?1 and usuPerf.perfil.id = ?2")
	public UsuarioPerfil findPerfil(@Param("idUsuario") Long idUsuario,@Param("idPerfil") Long idPerfil);
}
