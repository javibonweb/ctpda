package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.Perfil;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;
@Repository
public interface PerfilRepository extends AbstractCrudRepository<Perfil>, JoinedQDSLPredicateExecutorCustom<Perfil>,Serializable  {
	
	@Query("SELECT perf FROM Perfil perf WHERE perf.id in (SELECT usuPerf.perfil FROM UsuarioPerfil usuPerf where usuPerf.usuario.id = ?1 and usuPerf.activa = 1) and perf.activa =1 order by perf.descripcion")
	public List<Perfil> findPerfilesActivosAsociadosUsuario(@Param("idUsuario") Long idUsuario);

}
