package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.querydsl.jpa.impl.JPAQuery;

import es.juntadeandalucia.ctpda.gestionpdt.model.QUsuariosResponsables;
import es.juntadeandalucia.ctpda.gestionpdt.model.UsuariosResponsables;


public class UsuariosResponsablesRepositoryCustomImpl implements UsuariosResponsablesRepositoryCustom, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext
	private transient EntityManager entityManager;

	//****************************************************************
	//Todos los responsables de los que forma parte el usuario individual o colectivamente
	@Override
	@SuppressWarnings("unchecked")
	public List<UsuariosResponsables> findByUsuarioId(Long idUsuario){
		final JPAQuery<UsuariosResponsables> jpa = new JPAQuery<>(this.entityManager);
		final QUsuariosResponsables qUsuResp = QUsuariosResponsables.usuariosResponsables;
		
		final Query q = jpa.select(qUsuResp).from(qUsuResp)
					.where(qUsuResp.usuarioResponsable.id.eq(idUsuario))
					.orderBy(qUsuResp.responsable.descripcion.asc())
					.createQuery();
		
		final EntityGraph<?> graph = this.entityManager.getEntityGraph("usuariosResponsables.listaResponsables");
		q.setHint("javax.persistence.loadgraph", graph);

		return q.getResultList();
	}
	
}
