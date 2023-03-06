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
@AuditTable(value = "GE_DOCUMENTOS_EXPED_TRAMITES_H")
@Table(name = "GE_DOCUMENTOS_EXPED_TRAMITES")
public class DocumentosExpedientesTramites extends Auditable {	

	public static final String ORIGEN_INCORPORADO = "I";
	public static final String ORIGEN_GENERADO = "G";
	public static final String ORIGEN_VINCULADO = "V";

	private static final long serialVersionUID = 1L;
	
	@Id
	@NotNull
	@Column(name = "DETR_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_DOCUMENTOS_EXPED_TRAMITES")
	@SequenceGenerator(name = "S_DOCUMENTOS_EXPED_TRAMITES", sequenceName = "S_DOCUMENTOS_EXPED_TRAMITES", allocationSize = 1)
	@Getter
	@Setter
	private Long id;
	
	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;	

	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "DETR_DOCEXP_ID", foreignKey =  @ForeignKey(name="GE_DOCEXPTRAM_DOCEXP_FK"))
	private DocumentosExpedientes documentoExpediente;	

	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "DETR_TRAMEXP_ID", foreignKey =  @ForeignKey(name="GE_DOCEXPTRAM_TRAMEXP_FK"))
	private TramiteExpediente tramiteExpediente;

	@Column(name = "T_ORIGEN")
	@NotNull
	@Size(max = 1)
	@Getter
	@Setter
	private String origen;

}
