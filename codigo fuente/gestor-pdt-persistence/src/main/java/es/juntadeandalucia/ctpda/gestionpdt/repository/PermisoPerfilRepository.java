package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.Permiso;
import es.juntadeandalucia.ctpda.gestionpdt.model.PermisoPerfil;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;
@Repository
public interface PermisoPerfilRepository extends AbstractCrudRepository<PermisoPerfil>, JoinedQDSLPredicateExecutorCustom<PermisoPerfil>,Serializable  {
	
	@Query("SELECT permPerf.permiso FROM PermisoPerfil permPerf, Permiso perm WHERE perm.id = permPerf.permiso and permPerf.perfil.id = ?1 and permPerf.activa = 1 and perm.activa = 1")
	public List<Permiso> findPermisosActivosAsociadosPerfil(@Param("idPerfil") Long idPerfil);
	
	@Query("SELECT permPerf.permiso.codigo FROM PermisoPerfil permPerf, Permiso perm  WHERE perm.id = permPerf.permiso and perm.id = permPerf.permiso and permPerf.perfil.id = ?1 and permPerf.activa = 1 and perm.activa = 1")
	public List<String> findCodPermisosActivosAsociadosPerfil(@Param("idPerfil") Long idPerfil);


}
