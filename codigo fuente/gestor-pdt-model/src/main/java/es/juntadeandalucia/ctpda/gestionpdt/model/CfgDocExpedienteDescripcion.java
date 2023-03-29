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
@AuditTable(value = "GE_CFG_DOC_EXPED_DESC_H")
@Table(name = "GE_CFG_DOC_EXPED_DESC")
public class CfgDocExpedienteDescripcion extends Auditable{

	private static final long serialVersionUID = 1L;
	
	
	@Id
	@NotNull
	@Column(name = "CFGDE_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_CFG_DOC_EXPED_DESC")
	@SequenceGenerator(name = "S_CFG_DOC_EXPED_DESC", sequenceName = "S_CFG_DOC_EXPED_DESC", allocationSize = 1)
	@Getter
	@Setter
	private Long id;
	
	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;
	
	
	@Column(name = "N_ORDEN")
	@Getter
	@Setter
	private Long orden;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "CFGDE_DOC_ID", foreignKey =  @ForeignKey(name="GE_CFG_DOC_EXPED_DES_FK"))
	private CfgDocExpedienteTramitacion cfgDocExpedienteTramitacion;
	
	@Column(name = "D_DESCRIPCION")
	@Size(max = 255)
	@Getter
	@Setter
	private String descripcion;

	@Column(name = "D_DESC_ABREV")
	@Size(max = 100)
	@Getter
	@Setter
	private String descripcionAbrev;
	

	@Column(name = "L_EDITABLE")
	@ColumnDefault("0")
	@NotNull
	@Getter
	@Setter
	private Boolean editable;
	
	@Column(name = "L_ANONIMIZADO")
	@ColumnDefault("0")
	@NotNull
	@Getter
	@Setter
	private Boolean anonimizado;
	
	@Column(name = "L_ANON_PARCIAL")
	@ColumnDefault("0")
	@NotNull
	@Getter
	@Setter
	private Boolean anonimizadoParcial;
	
	@Column(name = "L_FIRMADO")
	@ColumnDefault("0")
	@NotNull
	@Getter
	@Setter
	private Boolean firmado;
	
	@Column(name = "L_SELLADO")
	@ColumnDefault("0")
	@NotNull
	@Getter
	@Setter
	private Boolean sellado;

	@NotNull
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "CFG_VALDOM_CATEG_ID", foreignKey =  @ForeignKey(name="GE_CFG_VALDOM_CATEG_ID_FK"))
	private ValoresDominio categoria;
	
}
