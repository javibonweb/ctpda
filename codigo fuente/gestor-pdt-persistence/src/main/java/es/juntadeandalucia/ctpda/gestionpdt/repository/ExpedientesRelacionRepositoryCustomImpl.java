package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;

import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.ExpedientesRelacion;
import es.juntadeandalucia.ctpda.gestionpdt.model.QExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.QExpedientesRelacion;
import es.juntadeandalucia.ctpda.gestionpdt.util.StringUtils;

public class ExpedientesRelacionRepositoryCustomImpl implements ExpedientesRelacionRepositoryCustom,Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext
	private transient EntityManager entityManager;
	
	@Override
	public List<ExpedientesRelacion> findExpedientesRelacionados(Long id) {
		return findExpedientesBySentido(id, true);
	}	
	
	@Override
	public List<ExpedientesRelacion> findExpedientesOrigen(Long id) {
		return findExpedientesBySentido(id, false);
	}
	
	@SuppressWarnings("unchecked")
	public List<ExpedientesRelacion> findExpedientesBySentido(Long id, boolean relacionado){
		JPAQuery<ExpedientesRelacion> q = new JPAQuery<>(this.entityManager);
		final EntityGraph<?> graph = this.entityManager.getEntityGraph("expediente.relacion");
		q.setHint("javax.persistence.loadgraph", graph);
		
		BooleanExpression whereRelacion = relacionado? 
				//origen = id; ¿relacionados?
				QExpedientesRelacion.expedientesRelacion.expedienteOrigen.id.eq(id) :
				//relacionado = id; ¿orígenes?
				QExpedientesRelacion.expedientesRelacion.expedienteRelacionado.id.eq(id);
		
		q = q.from(QExpedientesRelacion.expedientesRelacion)
		.where(whereRelacion);
		
		return q.createQuery().getResultList();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Expedientes> findExpedientesRelacionables(Long idExp, List<Long> idsExpedientesRelacionados, String filtroNum, Date filtroFecha) {
		JPAQuery<Expedientes> q = new JPAQuery<>(this.entityManager);
		final EntityGraph<?> graph = this.entityManager.getEntityGraph("expediente.basico");
		q.setHint("javax.persistence.loadgraph", graph);

		//Filtro para no recoger los expedientes 
		final List<Long> lista = new ArrayList<>(idsExpedientesRelacionados);
		
		BooleanExpression where = QExpedientes.expedientes.id.notIn(lista);

		//es alta?
		if (idExp != null) {
			Expedientes e = this.obtener(idExp);
			where = where.and(QExpedientes.expedientes.fechaEntrada.loe(e.getFechaEntrada()));
			lista.add(idExp);
		}
		
		//Filtro por número/nombre y fecha de entrada
		if (!StringUtils.isBlank(filtroNum)) {
			final String t = "%" + filtroNum.toLowerCase() + "%";
			where = where.and(QExpedientes.expedientes.numExpediente.like(t)
					.or(QExpedientes.expedientes.nombreExpediente.lower().like(t)));
		}
		
		if(filtroFecha != null) {
			where = where.and(QExpedientes.expedientes.fechaEntrada.eq(filtroFecha));
		}

		q = q.from(QExpedientes.expedientes)
				.where(where)
				.orderBy(QExpedientes.expedientes.fechaEntrada.desc());

		return q.createQuery().getResultList();
	}

	private Expedientes obtener(Long idExp) {
		return new JPAQuery<Expedientes>(this.entityManager)
				.from(QExpedientes.expedientes)
				.where(QExpedientes.expedientes.id.eq(idExp))
				.fetchFirst();		
	}
}
