package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;

import es.juntadeandalucia.ctpda.gestionpdt.model.QResponsablesTramitacion;
import es.juntadeandalucia.ctpda.gestionpdt.model.ResponsablesTramitacion;
import es.juntadeandalucia.ctpda.gestionpdt.util.ListUtils;


public class ResponsablesTramitacionRepositoryCustomImpl implements ResponsablesTramitacionRepositoryCustom,Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext
	private transient EntityManager entityManager;
	
	@Override
	/**
	 * Todos los miembros del equipo de los responsables, excepto ellos mismos.
	 */
	public List<Long> findIdsEquipos(List<Long> idsResp){
		List<Long> ids = new ArrayList<>();
		List<Long> tmp = findIdsByResponsableSuperior(idsResp);
		
		while(!tmp.isEmpty()) {
			ids.addAll(tmp);
			tmp = findIdsByResponsableSuperior(tmp);
		}
		
		return ids;
	}
	
	//------------------
	
	@Override
	/**
	 * Todos los miembros del equipo del responsable, excepto él mismo.
	 */
	public List<ResponsablesTramitacion> findEquipoResponsable(Long idResponsable){
		List<ResponsablesTramitacion> listaResp = new ArrayList<>();
		List<ResponsablesTramitacion> tmp = findResponsablesSubordinados(List.of(idResponsable));
		
		while(!tmp.isEmpty()) {
			listaResp.addAll(tmp);
			List<Long> ids = ListUtils.collect(tmp, ResponsablesTramitacion::getId);
			tmp = findResponsablesSubordinados(ids);
		}
		
		return listaResp;
	}
	
	private List<ResponsablesTramitacion> convertir(List<Tuple> oo){
		return oo.stream().map(this::convertir).collect(Collectors.toList());
	}
	
	private ResponsablesTramitacion convertir(Tuple obj) {
		int i=0;
		final ResponsablesTramitacion resp = new ResponsablesTramitacion();
		
		resp.setId(obj.get(i++, Long.class));
		resp.setDescripcion(obj.get(i, String.class));
		
		return resp;
	}
	
	//---------------------------

	@SuppressWarnings("unchecked")
	private List<Long> findIdsByResponsableSuperior(List<Long> idsResp){
		QResponsablesTramitacion qResp = QResponsablesTramitacion.responsablesTramitacion;
		JPAQuery<Long> q = new JPAQuery<>(this.entityManager);
		
		q = q.select(qResp.id).from(qResp).where(qResp.responsableSuperior.id.in(idsResp));
		
		return q.createQuery().getResultList();
	}

	@SuppressWarnings("unchecked")
	private List<ResponsablesTramitacion> findResponsablesSubordinados(List<Long> idsResp){
		QResponsablesTramitacion qResp = QResponsablesTramitacion.responsablesTramitacion;
		JPAQuery<Tuple> q = new JPAQuery<>(this.entityManager);
		
		//Los propios ids de responsable se excluyen
		q = q.select(qResp.id, qResp.descripcion).from(qResp).where(qResp.id.notIn(idsResp).and(qResp.responsableSuperior.id.in(idsResp)));
		
		return this.convertir(q.createQuery().getResultList());
	}
	
}

