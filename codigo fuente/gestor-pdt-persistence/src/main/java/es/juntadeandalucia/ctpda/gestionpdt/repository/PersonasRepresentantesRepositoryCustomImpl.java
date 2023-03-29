package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;

import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.ExpedientesRelacion;
import es.juntadeandalucia.ctpda.gestionpdt.model.Personas;
import es.juntadeandalucia.ctpda.gestionpdt.model.PersonasRepresentantes;
import es.juntadeandalucia.ctpda.gestionpdt.model.QPersonas;
import es.juntadeandalucia.ctpda.gestionpdt.model.QPersonasRepresentantes;
import es.juntadeandalucia.ctpda.gestionpdt.util.StringUtils;

public class PersonasRepresentantesRepositoryCustomImpl implements PersonasRepresentantesRepositoryCustom,Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext
	private transient EntityManager entityManager;
	
	@Override
	public List<PersonasRepresentantes> findRepresentantes(Long id) {
		return findPersonasBySentidoRelacion(id, true);
	}
	
	@Override
	public List<PersonasRepresentantes> findPersonas(Long id) {
		return findPersonasBySentidoRelacion(id, false);
	}
	
	@SuppressWarnings("unchecked")
	public List<PersonasRepresentantes> findPersonasBySentidoRelacion(Long id, boolean representante){
		JPAQuery<ExpedientesRelacion> q = new JPAQuery<>(this.entityManager);
		final EntityGraph<?> graph = this.entityManager.getEntityGraph("persona.representante");
		q.setHint("javax.persistence.loadgraph", graph);
		
		BooleanExpression whereRepre = representante? 
				//origen = id; ¿relacionados?
				QPersonasRepresentantes.personasRepresentantes.persona.id.eq(id) :
				//relacionado = id; ¿orígenes?
				QPersonasRepresentantes.personasRepresentantes.representante.id.eq(id);
		
		q = q.from(QPersonasRepresentantes.personasRepresentantes)
		.where(whereRepre);
		
		return q.createQuery().getResultList();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Personas> findNuevosRepresentantes(Long idPer, List<Long> idsPersonasRepresentantes, String nombreRazonSocialFiltro, String nifCifFiltro) {
		JPAQuery<Expedientes> q = new JPAQuery<>(this.entityManager);
		final EntityGraph<?> graph = this.entityManager.getEntityGraph("persona.basico");
		q.setHint("javax.persistence.loadgraph", graph);

		//Filtro para no recoger los expedientes 
		final List<Long> lista = new ArrayList<>(idsPersonasRepresentantes);
		
		//es alta?
		if (idPer != null) {
			lista.add(idPer);
		}
		
		BooleanExpression where = QPersonas.personas.id.notIn(lista);
		
		//Filtros por nombre y nif/cif
		if (!StringUtils.isBlank(nombreRazonSocialFiltro)) {
			String filtro = nombreRazonSocialFiltro.trim().replaceAll("\\s+", " ").toLowerCase();
			
			where = where.and(QPersonas.personas.nombreRazonsocial
			.concat(" ")
			.concat(QPersonas.personas.primerApellido)
			.concat(" ")
			.concat(QPersonas.personas.segundoApellido)
			.toLowerCase()
			.like(Expressions.stringTemplate("'%'")
	               .concat(Expressions.stringTemplate("{0}", filtro)
	            		   .concat(Expressions.stringTemplate("'%'")))));
		}
		
		if (!StringUtils.isBlank(nifCifFiltro)) {
			String filtro = nifCifFiltro.trim().replaceAll("\\s|-", "").toLowerCase();
			
			where = where.and(QPersonas.personas.nifCif
			.toLowerCase()
			.like(Expressions.stringTemplate("'%'")
	               .concat(Expressions.stringTemplate("{0}", filtro)
	            		   .concat(Expressions.stringTemplate("'%'")))));
		}

		q = q.from(QPersonas.personas)
				.where(where)
				.orderBy(QPersonas.personas.segundoApellido.asc(), 
						QPersonas.personas.primerApellido.asc(),
						QPersonas.personas.nombreRazonsocial.asc());

		return q.createQuery().getResultList();
	}
}
