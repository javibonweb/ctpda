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

@Data
@Entity
@EntityListeners(AuditorAwareImpl.class)
@Audited
@AuditTable(value = "GE_DERECHOS_RECLAMADOS_EXPDT_H")
@Table(name = "GE_DERECHOS_RECLAMADOS_EXPDT")
public class DerechosReclamadosExpedientes extends Auditable {
	
	/**
	 * Serial id
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "DER_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_DERECHOS_RECLAMADOS_EXPDT")
	@SequenceGenerator(name = "S_DERECHOS_RECLAMADOS_EXPDT", sequenceName = "S_DERECHOS_RECLAMADOS_EXPDT", allocationSize = 1)
	private Long id;
	
	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;
	
	@OneToOne(fetch = FetchType.LAZY)
	@NotNull
	@JoinColumn(name = "DER_EXP_ID", foreignKey = @ForeignKey(name = "GE_DER_EXP_FK"))
	private Expedientes expediente;
	
	@OneToOne(fetch = FetchType.LAZY)
	@NotNull
	@JoinColumn(name = "DER_VALDOM_DERRECL_ID", foreignKey = @ForeignKey(name = "GE_DER_VALDOM_DERRECL_FK"))
	private ValoresDominio valoresDerReclExp;

}
