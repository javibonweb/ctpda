package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.ResolucionSujetoObligado;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;
@Repository
public interface ResolucionSujetoObligadoRepository extends AbstractCrudRepository<ResolucionSujetoObligado>,JoinedQDSLPredicateExecutorCustom<ResolucionSujetoObligado>,Serializable {
	
	public List<ResolucionSujetoObligado> findByResolucionId(Long idResol);

	public ResolucionSujetoObligado getByResolucionIdAndPrincipalTrue(Long idResol);

}