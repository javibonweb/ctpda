package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.Dominio;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;
@Repository
public interface DominioRepository extends AbstractCrudRepository<Dominio>, JoinedQDSLPredicateExecutorCustom<Dominio>,Serializable  {

	@Query("SELECT dom.descripcion FROM Dominio dom WHERE dom.puntoMenu = 1")
	public List<String> findDescripcionDominiosPuntoMenu();
	
	@Query("SELECT dom FROM Dominio dom, ValoresDominio valDom WHERE dom.puntoMenu = 1 AND valDom.dominio = (SELECT id FROM Dominio WHERE codigo = 'MENU') AND valDom.codigo = dom.codigo ORDER BY valDom.orden, dom.descripcion")
	public List<Dominio> findDominiosPuntoMenu();

	@Query("SELECT dom FROM Dominio dom WHERE dom.codigo = ?1")
	public Dominio findDominioByCodigo(String codigoDominio);
}
