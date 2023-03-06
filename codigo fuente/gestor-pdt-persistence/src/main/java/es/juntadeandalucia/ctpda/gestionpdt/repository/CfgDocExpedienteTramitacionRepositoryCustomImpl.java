package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;

import es.juntadeandalucia.ctpda.gestionpdt.model.CfgDocExpedienteTramitacion;
import es.juntadeandalucia.ctpda.gestionpdt.model.QCfgDocExpedienteTramitacion;
import es.juntadeandalucia.ctpda.gestionpdt.model.TipoDocumento;
import es.juntadeandalucia.ctpda.gestionpdt.model.TipoTramite;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;


public class CfgDocExpedienteTramitacionRepositoryCustomImpl implements CfgDocExpedienteTramitacionRepositoryCustom,Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext
	private transient EntityManager entityManager;

	@Override
	public List<TipoDocumento> findTiposDocumentos(ValoresDominio tipoExp, ValoresDominio situacion, TipoTramite tipoTramite, TipoTramite tipoTramiteSup) {
		JPAQuery<TipoDocumento> q = new JPAQuery<>(this.entityManager);
		QCfgDocExpedienteTramitacion qCfg = QCfgDocExpedienteTramitacion.cfgDocExpedienteTramitacion;
		
		q = q.select(qCfg.tipoDocumento).from(qCfg).where(getWhereTiposDocumentos(tipoExp, situacion, tipoTramite, tipoTramiteSup, null));
		
		return this.getResultList(q);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<CfgDocExpedienteTramitacion> findTiposDocumentosCfg(ValoresDominio tipoExp, ValoresDominio situacion, TipoTramite tipoTramite, TipoTramite tipoTramiteSup, String codOrigen) {
		JPAQuery<CfgDocExpedienteTramitacion> jpa = new JPAQuery<>(this.entityManager);
		QCfgDocExpedienteTramitacion qCfg = QCfgDocExpedienteTramitacion.cfgDocExpedienteTramitacion;
		
		jpa = jpa.select(qCfg).from(qCfg)
				.where(getWhereTiposDocumentos(tipoExp, situacion, tipoTramite, tipoTramiteSup, codOrigen))
				.orderBy(qCfg.descripcionTipoDoc.asc());
		
		Query q = jpa.createQuery();
		final EntityGraph<?> graph = this.entityManager.getEntityGraph("cfgDocExpedienteTramitacion.listaTiposDocumento");
		q.setHint("javax.persistence.loadgraph", graph);

		return q.getResultList();
	}
	
	private BooleanExpression getWhereTiposDocumentos(ValoresDominio tipoExp, ValoresDominio situacion, TipoTramite tipoTramite, TipoTramite tipoTramiteSup, String codOrigen) {
		QCfgDocExpedienteTramitacion qCfg = QCfgDocExpedienteTramitacion.cfgDocExpedienteTramitacion;
		
		BooleanExpression where = 
				qCfg.tipoTramite.id.eq(tipoTramite.getId())
				.and(qCfg.valorTipoExpediente.id.eq(tipoExp.getId()));
		
		if(situacion != null && null != situacion.getId()) {
			where = where.and(
					qCfg.situacion.id.eq(situacion.getId())
					.or(qCfg.situacion.id.isNull()));
		}
		
		if(tipoTramiteSup != null && null != tipoTramiteSup.getId()) {
			where = where.and(
					qCfg.tipoTramiteSup.id.eq(tipoTramiteSup.getId())
					.or(qCfg.tipoTramiteSup.id.isNull()));
		} else {
			where = where.and(qCfg.tipoTramiteSup.id.isNull());
		}
		
		if(StringUtils.isNotBlank(codOrigen)) {
			where = where.and(qCfg.codigoOrigen.eq(codOrigen));
		}
		
		where = where.and(qCfg.activo.eq(Boolean.TRUE));
		
		return where;
	}
	
	@SuppressWarnings("unchecked")
	private List<TipoDocumento> getResultList(JPAQuery<TipoDocumento> q) {
		List<TipoDocumento> tmpList = q.createQuery().getResultList();
		Map<String, TipoDocumento> mapTipos = new HashMap<>();
		//stream.collect da error cuando hay claves repetidas, que es justo lo que buscamos
		tmpList.stream().forEach(t -> mapTipos.put(t.getCodigo(), t));
		
		tmpList.clear();
		tmpList.addAll(mapTipos.values());
		tmpList.sort((t1,t2) -> StringUtils.compare(t1.getDescripcion(), t2.getDescripcion()));
		
		return tmpList;
	}
	
}
