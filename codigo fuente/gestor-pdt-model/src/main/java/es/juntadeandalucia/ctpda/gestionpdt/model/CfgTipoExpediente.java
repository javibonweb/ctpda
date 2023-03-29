	package es.juntadeandalucia.ctpda.gestionpdt.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import es.juntadeandalucia.ctpda.gestionpdt.model.core.Auditable;
import es.juntadeandalucia.ctpda.gestionpdt.model.core.AuditorAwareImpl;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Entity
@EntityListeners(AuditorAwareImpl.class)
@Audited
@AuditTable(value = "GE_CFG_TIPOEXPEDIENTE_H")
@Table(name = "GE_CFG_TIPOEXPEDIENTE")
public class CfgTipoExpediente extends Auditable{
	
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "CFG_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_CFG_TIPOEXPEDIENTE")
	@SequenceGenerator(name = "S_CFG_TIPOEXPEDIENTE", sequenceName = "S_CFG_TIPOEXPEDIENTE", allocationSize = 1)
	@Getter
	@Setter
	private Long id;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "CFG_VALDOM_TIP_EXP_ID", foreignKey = @ForeignKey(name = "GE_CFG_VALDOM_TIP_EXP_FK"))
	private ValoresDominio valorTipoExpediente;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "CFG_VALDOM_TIP_AREA_CTPDA_ID", foreignKey = @ForeignKey(name = "GE_CFG_VALDOM_TIP_AREA_CTPDA_FK"))
	private ValoresDominio valorTipoCtpda;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "CFG_VALDOM_SERIERESOL_ID", foreignKey = @ForeignKey(name = "GE_CFG_VALDOM_SERIERESOL_FK"))
	private ValoresDominio valorSerieResolucion;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "CFG_VALDOM_SERIERESREC_ID", foreignKey = @ForeignKey(name = "GE_VALDOM_SERIERESREC_ID_FK"))
	private ValoresDominio valorSerieResolRecurso;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "CFG_VALDOM_TIPORESOL_ID", foreignKey = @ForeignKey(name = "GE_CFG_VALDOM_TIPORESOL_FK"))
	private ValoresDominio valorTipoResolucion;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "CFG_VALDOM_TIPRESREC_ID", foreignKey = @ForeignKey(name = "GE_CFG_VALDOM_TIPRESREC_ID_FK"))
	private ValoresDominio valorTipoResolRecurso;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "CFG_TIP_TRA_SEG_1_ID", foreignKey = @ForeignKey(name = "GE_CFG_TIP_TRA_SEG_1_FK"))
	private TipoTramite tipoTramiteSeg1;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "CFG_TIP_TRA_SEG_2_ID", foreignKey = @ForeignKey(name = "GE_CFG_TIP_TRA_SEG_2_FK"))
	private TipoTramite tipoTramiteSeg2;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "CFG_TIP_TRA_SEG_3_ID", foreignKey = @ForeignKey(name = "GE_CFG_TIP_TRA_SEG_3_FK"))
	private TipoTramite tipoTramiteSeg3;
	
	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "CFG_VALDOM_MOTRELPER_ID", foreignKey = @ForeignKey(name = "GE_CFG_VALDOM_MOTRELPER_ID_FK"))
	private ValoresDominio valorMotivoRelacionPersona;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "CFG_VALDOM_MOTRELSUJ_ID", foreignKey = @ForeignKey(name = "GE_CFG_VALDOM_MOTRELSUJ_ID_FK"))
	private ValoresDominio valorMotivoRelacionSujeto;
	
	@Column(name = "N_DIASFINALIZACION")
	private Long diasFinalizacion;
	
	@Column(name = "N_DIASALEGACIONES")
	private Long diasAlegaciones;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "CFGTIPOEXP_RESPTRAMITACION_ID", foreignKey = @ForeignKey(name = "GE_CFGTIPOEXP_RESPTRAMITACION_ID_FK"))
	private ResponsablesTramitacion responsablePorDefecto;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "CFGTIPOEXP_PERSONAS_ID", foreignKey = @ForeignKey(name = "GE_CFGTIPOEXP_PERSONAS_ID_FK"))
	private Personas personaPorDefecto;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "CFGTIPOEXP_SUJETOSOBLIGADOS_ID", foreignKey = @ForeignKey(name = "GE_CFGTIPOEXP_SUJETOSOBLIGADOS_ID_FK"))
	private SujetosObligados sujetoPorDefecto;

}
