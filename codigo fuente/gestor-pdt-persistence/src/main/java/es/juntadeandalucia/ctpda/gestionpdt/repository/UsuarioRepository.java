package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.Usuario;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;
@Repository
public interface UsuarioRepository extends AbstractCrudRepository<Usuario>, JoinedQDSLPredicateExecutorCustom<Usuario>,Serializable  {
	
	@Query("SELECT usu FROM Usuario usu WHERE usu.login = ?1 and UPPER(usu.contrasenya) =UPPER(?2) and usu.activa = 1")
	public Usuario findUsuarioActivo(String usuario, String clave);
	
	@Query("SELECT usu FROM Usuario usu WHERE usu.activa = 1 and usu.firmante = 1")
	public List<Usuario> findFirmantesActivos();
	
	public Usuario findByLogin(String usuario);

}
