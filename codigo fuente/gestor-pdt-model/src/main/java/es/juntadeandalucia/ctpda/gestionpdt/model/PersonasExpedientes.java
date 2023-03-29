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
@AuditTable(value = "GE_PERSONAS_EXPEDIENTES_H")
@Table(name = "GE_PERSONAS_EXPEDIENTES")
public class PersonasExpedientes extends Auditable{
	
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PER_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_PERSONAS_EXPEDIENTES")
	@SequenceGenerator(name = "S_PERSONAS_EXPEDIENTES", sequenceName = "S_PERSONAS_EXPEDIENTES", allocationSize = 1)
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
	@JoinColumn(name = "PER_EXP_ID", foreignKey = @ForeignKey(name = "GE_PER_EXP_FK"))
	
	private Expedientes expediente;
	
	@OneToOne(fetch = FetchType.LAZY)
	@NotNull
	@JoinColumn(name = "PER_PER_ID", foreignKey = @ForeignKey(name = "GE_PER_PER_FK"))
	private Personas personas;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PER_PER_REPRE_ID", foreignKey = @ForeignKey(name = "GE_PER_PER_REPRE_FK"))
	private Personas personasRepre;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PER_VALDOM_MOTRELEXPPER_ID", foreignKey = @ForeignKey(name = "GE_PER_VALDOM_MOTRELEXPPER_FK"))
	private ValoresDominio valoresRelacionExpPer;
	
	@Column(name = "L_INTERESADO")
	@ColumnDefault("0")
	@NotNull
	@Getter
	@Setter
	private Boolean interesado;

	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;

}
