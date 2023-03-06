package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.ResponsablesTramitacion;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;

@Repository
public interface ResponsablesTramitacionRepository extends AbstractCrudRepository<ResponsablesTramitacion>, JoinedQDSLPredicateExecutorCustom<ResponsablesTramitacion>, ResponsablesTramitacionRepositoryCustom,Serializable  {
		
	@Query("SELECT respTram FROM ResponsablesTramitacion respTram WHERE respTram.activo = 1 ORDER BY respTram.descripcion")
	List<ResponsablesTramitacion> findByActivo();
	
	@Query("SELECT ur.responsable FROM UsuariosResponsables ur WHERE ur.usuarioResponsable.id = :id and ur.porDefecto = TRUE") //and ur.responsable.activo")
	ResponsablesTramitacion findResponsablePorDefectoUsuario(@Param("id") Long idUsuario);
	
	@Query("SELECT respTram FROM ResponsablesTramitacion respTram WHERE respTram.codResponsable = :codResp") 
	ResponsablesTramitacion findResponsableTramitacionByCodResp(@Param("codResp") String codResp);
	
}
