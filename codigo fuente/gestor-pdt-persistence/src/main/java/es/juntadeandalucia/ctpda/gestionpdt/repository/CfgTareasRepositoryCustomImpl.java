package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;

import es.juntadeandalucia.ctpda.gestionpdt.model.CfgTareas;
import es.juntadeandalucia.ctpda.gestionpdt.model.QCfgTareas;


public class CfgTareasRepositoryCustomImpl implements CfgTareasRepositoryCustom, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext
	private transient EntityManager entityManager;

	@Override
	@SuppressWarnings("unchecked")
	public List<CfgTareas> findTiposTareasActivasCfg(Long idTipoExp, Long idTipoTr, Long idTipoSubtr, boolean documento){
		BooleanExpression b = buildFiltroCfgTareas(idTipoExp, idTipoTr, idTipoSubtr, documento);
			
		return buildQuery(b).getResultList();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<CfgTareas> findTiposTareasManualesActivasCfg(Long idTipoExp, Long idTipoTr, Long idTipoSubtr, boolean documento){
		BooleanExpression b = buildFiltroCfgTareas(idTipoExp, idTipoTr, idTipoSubtr, documento);
	
		b = b.and(QCfgTareas.cfgTareas.tareamanual.eq(Boolean.TRUE));
		
		return buildQuery(b).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CfgTareas> findCfgTareasSiguientes(Long idTipoExp, Long idTipoTr, Long idTipoSubtr, Long idTipoTareaOrigen,
			boolean documento) {
		BooleanExpression b = buildFiltroCfgTareas(idTipoExp, idTipoTr, idTipoSubtr, documento);	

		b = b.and(buildFiltroTareaOrigen(idTipoTareaOrigen));
		
		return buildQuery(b).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CfgTareas> findCfgTareas(Long idTipoExp, Long idTipoTr, Long idTipoSubtr, Long idTipoTarea,
			boolean documento) {
		BooleanExpression b = buildFiltroCfgTareas(idTipoExp, idTipoTr, idTipoSubtr, documento);	

		b = b.and(buildFiltroTipoTarea(idTipoTarea));
		
		return buildQuery(b).getResultList();
	}	
	
	@Override
	public boolean existeCfgTareaSiguiente(Long idTipoExp, Long idTipoTr, Long idTipoSubtr, Long idTipoTareaOrigen,
			boolean documento) {
		BooleanExpression b = buildFiltroCfgTareas(idTipoExp, idTipoTr, idTipoSubtr, documento);	

		b = b.and(buildFiltroTareaOrigen(idTipoTareaOrigen));
		
		final QCfgTareas qCfg = QCfgTareas.cfgTareas;
		JPAQuery<Long> jpa = new JPAQuery<>(this.entityManager);
		
		jpa = jpa.select(qCfg.id.count()).from(qCfg).where(b);

		return 0 < ((Long)jpa.createQuery().getSingleResult());
	}
	
	private BooleanExpression buildFiltroCfgTareas(Long idTipoExp, Long idTipoTr, Long idTipoSubtr, boolean documento) {
		final QCfgTareas qCfg = QCfgTareas.cfgTareas;
		
		BooleanExpression b = qCfg.activo.eq(true);

		b = b.and(qCfg.valorTipoExpediente.id.eq(idTipoExp));
		
		if(null != idTipoTr) {
			b = b.and(qCfg.tipoTramite.id.eq(idTipoTr));
			
			if(null != idTipoSubtr) {
				b = b.and(qCfg.tipoSubtramite.id.eq(idTipoSubtr));
			} else {	
				b = b.and(qCfg.tipoSubtramite.id.isNull());
			}
		} else {		
			b = b.and(qCfg.tipoTramite.id.isNull());
		}
		
		return b.and(qCfg.documento.eq(documento));

	}
	
	private BooleanExpression buildFiltroTareaOrigen(Long idTipoTareaOrigen) {
		return (null != idTipoTareaOrigen)?
				QCfgTareas.cfgTareas.valorTipoTareaOrigen.id.eq(idTipoTareaOrigen)
				:QCfgTareas.cfgTareas.valorTipoTareaOrigen.id.isNull();
	}
	
	private BooleanExpression buildFiltroTipoTarea(Long idTipoTarea) {
		return (null != idTipoTarea)?
				QCfgTareas.cfgTareas.valorTipoTarea.id.eq(idTipoTarea)
				:QCfgTareas.cfgTareas.valorTipoTarea.id.isNull();
	}

	
	private Query buildQuery(BooleanExpression bExp) {
		final QCfgTareas qCfg = QCfgTareas.cfgTareas;
		JPAQuery<CfgTareas> jpa = new JPAQuery<>(this.entityManager);
		
		jpa = jpa.select(qCfg).from(qCfg).where(bExp)
				.orderBy(qCfg.valorTipoTarea.descripcion.asc());
		
		final Query q = jpa.createQuery();
		final EntityGraph<?> graph = this.entityManager.getEntityGraph("cfgTareas.listaTiposTarea");
		q.setHint("javax.persistence.loadgraph", graph);
		
		return q;
	}
}
