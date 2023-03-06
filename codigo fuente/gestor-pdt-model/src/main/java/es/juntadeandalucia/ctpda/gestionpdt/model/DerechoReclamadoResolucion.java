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
@AuditTable(value = "GE_DERECH_RECLAM_RESOL_H")
@Table(name = "GE_DERECH_RECLAM_RESOL")
public class DerechoReclamadoResolucion extends Auditable {
	
	/**
	 * Serial id
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "DER_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_DERECH_RECLAM_RESOL")
	@SequenceGenerator(name = "S_DERECH_RECLAM_RESOL", sequenceName = "S_DERECH_RECLAM_RESOL", allocationSize = 1)
	private Long id;
	

	
	@OneToOne(fetch = FetchType.LAZY)
	@NotNull
	@JoinColumn(name = "DER_RES_ID", foreignKey = @ForeignKey(name = "GE_DER_RES_FK"))
	private Resolucion resolucion;
	
	@OneToOne(fetch = FetchType.EAGER)
	@NotNull
	@JoinColumn(name = "DER_VALDOM_DERRECL_ID", foreignKey = @ForeignKey(name = "GE_DER_VALDOM_DERRECL_FK"))
	private ValoresDominio valoresDerReclResol;

	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;
	
}
