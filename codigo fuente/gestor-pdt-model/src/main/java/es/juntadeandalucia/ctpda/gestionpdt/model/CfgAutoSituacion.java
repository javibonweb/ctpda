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
import javax.validation.constraints.Size;

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
@AuditTable(value = "GE_CFG_AUTO_SITUACION_H")
@Table(name = "GE_CFG_AUTO_SITUACION")
public class CfgAutoSituacion extends Auditable{
	
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String OP_ALTA = "A";
	public static final String OP_FIN = "F";

	@Id
	@Column(name = "AUS_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_CFG_AUTO_SITUACION")
	@SequenceGenerator(name = "S_CFG_AUTO_SITUACION", sequenceName = "S_CFG_AUTO_SITUACION", allocationSize = 1)
	@Getter
	@Setter
	private Long id;
	
	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;	
	
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "AUS_VALDOM_TIP_EXP_ID", foreignKey = @ForeignKey(name = "GE_AUTO_SIT_VALDOM_TIP_EXP_FK"))
	private ValoresDominio valorTipoExpediente;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "AUS_TIP_TRA_ID", foreignKey =  @ForeignKey(name="GE_AUTO_SIT_TIP_TRA_FK"))
	private TipoTramite tipoTramite;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "AUS_VALDOM_SIT_ORI_ID", foreignKey = @ForeignKey(name = "GE_AUTO_SIT_VALDOM_SIT_ORI_FK"))
	private ValoresDominio valorSituacionOrigen;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "AUS_VALDOM_SIT_DEST_ID", foreignKey = @ForeignKey(name = "GE_AUTO_SIT_VALDOM_SIT_DEST_FK"))
	private ValoresDominio valorSituacionDestino;
	
	@Column(name = "T_CONDICION")
	@Size(max = 50)
	@Getter
	@Setter
	private String condicion;
	
	@Column(name = "L_OPERACION")
	@Size(max=1)
	@NotNull
	@Getter
	@Setter
	private String operacion;

}
