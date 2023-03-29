package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import es.juntadeandalucia.ctpda.gestionpdt.model.Personas;
import es.juntadeandalucia.ctpda.gestionpdt.model.PersonasRepresentantes;

public interface PersonasRepresentantesRepositoryCustom extends Serializable {
	
	List<PersonasRepresentantes> findRepresentantes(Long id);

	List<PersonasRepresentantes> findPersonas(Long id);
	
	List<Personas> findNuevosRepresentantes(Long idExp, List<Long> idsPersonasRepresentantes, String nombreRazonSocialFiltro, String nifCifFiltro);


}
