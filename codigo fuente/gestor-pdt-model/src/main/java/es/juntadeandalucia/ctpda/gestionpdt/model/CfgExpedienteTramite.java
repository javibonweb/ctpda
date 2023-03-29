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
@AuditTable(value = "GE_CFG_EXPED_TRAMITE_H")
@Table(name = "GE_CFG_EXPED_TRAMITE")
public class CfgExpedienteTramite extends Auditable{

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@NotNull
	@Column(name = "CFG_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_CFG_EXPED_TRAMITE")
	@SequenceGenerator(name = "S_CFG_EXPED_TRAMITE", sequenceName = "S_CFG_EXPED_TRAMITE", allocationSize = 1)
	@Getter
	@Setter
	private Long id;

	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;

	
	@Column(name = "T_CODIGO")
	@NotNull
	@Size(max = 15)
	@Getter
	@Setter
	private String codigo;


	@OneToOne (fetch=FetchType.EAGER)
	@NotNull
	@JoinColumn(name= "CFG_VALDOM_TIPEXP_ID", foreignKey =  @ForeignKey(name="GE_CFG_VALDOM_TIPEXP_FK"))
	private ValoresDominio valorTipoExpediente;	

	@OneToOne (fetch=FetchType.EAGER)
	@JoinColumn(name= "CFG_VALDOM_SIT_ID", foreignKey =  @ForeignKey(name="GE_CFG_VALDOM_SIT_FK"))
	private ValoresDominio valorSituacion;	

	@OneToOne (fetch=FetchType.EAGER)
	@NotNull
	@JoinColumn(name= "CFG_TIP_TRA_ID", foreignKey =  @ForeignKey(name="GE_CFG_TIP_TRA_FK"))
	private TipoTramite tipoTramite;	
	
	@Column(name = "D_DESCRIPCION")
	@NotNull
	@Size(max = 255)
	@Getter
	@Setter
	private String descripcion;
	
	@Column(name = "D_DESC_ABREV")
	@NotNull
	@Size(max = 100)
	@Getter
	@Setter
	private String descripcionAbrev;

	@Column(name = "L_AUTO")
	@ColumnDefault("0")
	@NotNull
	@Getter
	@Setter
	private Boolean auto;
	
	@OneToOne (fetch=FetchType.EAGER)
	@JoinColumn(name= "CFGEXPTR_RESP_ID_DEFECTO", foreignKey =  @ForeignKey(name="GE_CFGEXPTR_RESP_FK"))
	private ResponsablesTramitacion responsablesTramitacion;	

}
