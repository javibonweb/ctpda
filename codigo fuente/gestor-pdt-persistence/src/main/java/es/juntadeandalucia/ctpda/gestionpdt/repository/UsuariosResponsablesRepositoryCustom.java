package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import es.juntadeandalucia.ctpda.gestionpdt.model.UsuariosResponsables;

public interface UsuariosResponsablesRepositoryCustom extends Serializable  {

	List<UsuariosResponsables> findByUsuarioId(Long idUsuario);
	
}
