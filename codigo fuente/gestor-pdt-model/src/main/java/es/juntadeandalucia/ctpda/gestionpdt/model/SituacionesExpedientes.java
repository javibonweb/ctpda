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
@AuditTable(value = "GE_SITUACIONES_EXPEDIENTES_H")
@Table(name = "GE_SITUACIONES_EXPEDIENTES")
public class SituacionesExpedientes extends Auditable{
	/**
	 * Serial id
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "SIT_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_SITUACIONES_EXPEDIENTES")
	@SequenceGenerator(name = "S_SITUACIONES_EXPEDIENTES", sequenceName = "S_SITUACIONES_EXPEDIENTES", allocationSize = 1)
	private Long id;
	
	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;
	
	@Column(name = "C_CODREL")
	@Size(max = 100)
	private String codigoRelacion;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "SIT_VALDOM_TIPEXP_ID", foreignKey =  @ForeignKey(name="GE_SIT_VALDOM_TIPEXP_FK"))
	private ValoresDominio valorTipoExpediente;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "SIT_VALDOM_SIT_ID", foreignKey =  @ForeignKey(name="GE_SIT_VALDOM_SIT_FK"))
	private ValoresDominio valorSituacion;
	
	@Column(name = "D_DESCRIPCION")
	@Size(max = 255)
	private String descripcion;
	
	@Column(name = "D_DESC_ABREV")
	@Size(max = 100)
	private String descripcionAbreviada;
	
	@Column(name = "N_NIVEL_ANIDAMIENTO")
	private Long nivelAnidamiento;
	
	@Column(name = "N_ORDEN")
	private Long orden;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "SIT_SIT_SUPER_ID", foreignKey =  @ForeignKey(name="GE_SIT_SIT_SUPER_FK"))
	private SituacionesExpedientes situacionRelSuperior;
	
	@Column(name = "L_ACTIVO")
	private Boolean activo;
	
	@Column(name = "L_SIT_INICIAL")
	private Boolean situacionInicial;
	
	@Column(name = "L_SIT_FINAL")
	private Boolean situacionFinal;
	
	@Column(name = "L_NO_SUPERVISADA")
	@ColumnDefault("0")
	@NotNull
	@Getter
	@Setter
	private Boolean noSupervisada;
}
