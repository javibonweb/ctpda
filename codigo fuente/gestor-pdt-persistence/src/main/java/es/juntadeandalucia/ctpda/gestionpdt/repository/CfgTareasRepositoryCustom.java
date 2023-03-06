package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.CfgTareas;

@Repository
public interface CfgTareasRepositoryCustom extends Serializable{

	List<CfgTareas> findTiposTareasActivasCfg(Long idTipoExp, Long idTipoTr, Long idTipoSubtr, boolean documento);
	
	List<CfgTareas> findTiposTareasManualesActivasCfg(Long idTipoExp, Long idTipoTr, Long idTipoSubtr, boolean documento);
	
	List<CfgTareas> findCfgTareasSiguientes(Long idTipoExp, Long idTipoTr, Long idTipoSubtr, Long idTipoTareaOrigen,
			boolean documento);
	
	List<CfgTareas> findCfgTareas(Long idTipoExp, Long idTipoTr, Long idTipoSubtr, Long idTipoTarea,
			boolean documento);
	
	boolean existeCfgTareaSiguiente(Long idTipoExp, Long idTipoTr, Long idTipoSubtr, Long idTipoTareaOrigen,
			boolean documento);

}
