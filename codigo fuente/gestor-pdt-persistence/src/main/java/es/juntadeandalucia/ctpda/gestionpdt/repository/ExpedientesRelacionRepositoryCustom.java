package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.ExpedientesRelacion;

public interface ExpedientesRelacionRepositoryCustom extends Serializable{
	
	List<ExpedientesRelacion> findExpedientesRelacionados(Long id);

	List<ExpedientesRelacion> findExpedientesOrigen(Long id);
	
	List<Expedientes> findExpedientesRelacionables(Long idExp, List<Long> idsExpedientesRelacionados, String filtroNum,
			Date filtroFecha);

}
