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
@AuditTable(value = "GE_RESOL_EXPEDIENTES_H")
@Table(name = "GE_RESOL_EXPEDIENTES")
public class ResolucionExpediente  extends Auditable{

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "RESEXP_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_RESOL_EXPEDIENTES")
	@SequenceGenerator(name = "S_RESOL_EXPEDIENTES", sequenceName = "S_RESOL_EXPEDIENTES", allocationSize = 1)
	@Getter
	@Setter
	private Long id;

	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "RESEXP_RES_ID", foreignKey = @ForeignKey(name = "GE_RESEXP_RES_ID_FK"))
	private Resolucion resolucion;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "RESEXP_EXP_ID", foreignKey = @ForeignKey(name = "GE_RESEXP_EXP_ID_FK"))
	private Expedientes expediente;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "RESEXP_VALDOMSENRES_ID", foreignKey = @ForeignKey(name = "GE_RESEXP_VALDOMSENRES_FK"))
	private ValoresDominio valorSentidoResolucion;
	
	@Column(name = "N_PRINCIPAL")
	@NotNull
	@Getter
	@Setter
	private Boolean principal;
	
	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;	
	
}
