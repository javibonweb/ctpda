package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import es.juntadeandalucia.ctpda.gestionpdt.model.ResponsablesTramitacion;


public interface ResponsablesTramitacionRepositoryCustom extends Serializable {

	List<Long> findIdsEquipos(List<Long> idResp);

	List<ResponsablesTramitacion> findEquipoResponsable(Long idResponsable);
	
}
