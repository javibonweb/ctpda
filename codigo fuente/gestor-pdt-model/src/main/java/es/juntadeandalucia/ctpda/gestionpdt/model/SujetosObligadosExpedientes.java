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
@AuditTable(value = "GE_SUJETOS_OBLIGADOS_EXPDT_H")
@Table(name = "GE_SUJETOS_OBLIGADOS_EXPDT")

public class SujetosObligadosExpedientes extends Auditable {

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "SUJ_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_SUJETOS_OBLIGADOS_EXPDT")
	@SequenceGenerator(name = "S_SUJETOS_OBLIGADOS_EXPDT", sequenceName = "S_SUJETOS_OBLIGADOS_EXPDT", allocationSize = 1)
	@Getter
	@Setter
	private Long id;

	@Column(name = "N_PRINCIPAL")
	@NotNull
	@Getter
	@Setter
	private Boolean principal;

	@OneToOne(fetch = FetchType.LAZY)
	@NotNull
	@JoinColumn(name = "SUJ_EXP_ID", foreignKey = @ForeignKey(name = "GE_SUJ_EXP_FK"))
	private Expedientes expediente;

	@OneToOne(fetch = FetchType.LAZY)
	@NotNull
	@JoinColumn(name = "SUJ_SUJ_ID", foreignKey = @ForeignKey(name = "GE_SUJ_SUJ_FK"))
	private SujetosObligados sujetosObligados;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUJ_VALDOM_MOTRELEXPSUJ_ID", foreignKey = @ForeignKey(name = "GE_SUJ_VALDOM_MOTRELEXPSUJ_FK"))
	private ValoresDominio valoresRelacionExpSuj;

	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUJ_PER_ID", foreignKey = @ForeignKey(name = "GE_SUJ_PER_FK"))
	private Personas personas;
	
	@Column(name = "T_NOMBRE_RAZONSOCIAL")
	@Size(max = 100)
	@Getter
	@Setter
	private String nombreRazonsocial;
	
	@Column(name = "N_TELEFONO")
	@Getter
	@Setter
	@Size(max = 50)
	private String telefono;
	
	@Column(name = "T_APELLIDOS")
	@Size(max = 200)
	@Getter
	@Setter
	private String apellidos;
	
	@Column(name = "L_DPD")
	@Getter
	@Setter
	private Boolean dpd;
	
	@Column(name = "T_EMAIL")
	@Size(max = 50)
	@Getter
	@Setter
	private String email;

}
