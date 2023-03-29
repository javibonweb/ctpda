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

import org.hibernate.annotations.ColumnDefault;
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
@AuditTable(value = "GE_CFG_PLAZOS_EXPDTE_H")
@Table(name = "GE_CFG_PLAZOS_EXPDTE")
public class CfgPlazosExpdte extends Auditable{
	
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CFG_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_CFG_PLAZOS_EXPDTE")
	@SequenceGenerator(name = "S_CFG_PLAZOS_EXPDTE", sequenceName = "S_CFG_PLAZOS_EXPDTE", allocationSize = 1)
	@Getter
	@Setter
	private Long id;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "PLAEXP_VALDOM_TIPEXP_ID", foreignKey = @ForeignKey(name = "GE_PLAEXP_VALDOM_TIPEXP_FK"))
	private ValoresDominio valorTipoExpediente;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "PLAEXP_VALDOM_TIPPLA_ID", foreignKey = @ForeignKey(name = "GE_PLAEXP_VALDOM_TIPPLA_FK"))
	private ValoresDominio valorTipoPlazo;
	
	@Column(name = "D_DESC_TIPO_PLAZO")
	@Getter
	@Setter
	private String descripcionTipPlazo;
	
	@Column(name = "D_DESC_ABREV_TIPO_PLAZO")
	@Getter
	@Setter
	private String descAbrevTipPlazo;
	
	@Column(name = "N_ORDEN")
	@Getter
	@Setter
	private Long orden;
	
	
	@Column(name = "L_BLOQUEADO")
	@ColumnDefault("1")
	@NotNull
	@Getter
	@Setter
	private Boolean bloqueado;
	
	@Column(name = "L_ACTIVO")
	@ColumnDefault("1")
	@NotNull
	@Getter
	@Setter
	private Boolean activo;
	
	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;	

}
