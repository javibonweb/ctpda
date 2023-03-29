package es.juntadeandalucia.ctpda.gestionpdt.model;

import java.util.Date;

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

@Data
@Entity
@EntityListeners(AuditorAwareImpl.class)
@Audited
@AuditTable(value = "GE_OBSERVACIONES_EXPEDIENTES_H")
@Table(name = "GE_OBSERVACIONES_EXPEDIENTES")
public class ObservacionesExpedientes extends Auditable{

	/**
	 * Serial id
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "OBS_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_OBSERVACIONES_EXPEDIENTES")
	@SequenceGenerator(name = "S_OBSERVACIONES_EXPEDIENTES", sequenceName = "S_OBSERVACIONES_EXPEDIENTES", allocationSize = 1)
	private Long id;
	
	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;	
	
	@OneToOne(fetch = FetchType.LAZY)
	@NotNull
	@JoinColumn(name = "OBS_EXP_ID", foreignKey = @ForeignKey(name = "GE_OBS_EXP_FK"))
	private Expedientes expediente;
	
	@OneToOne (fetch=FetchType.LAZY)
	@NotNull
	@JoinColumn(name= "OBS_VALDOM_TIPOBS_ID", foreignKey =  @ForeignKey(name="GE_OBS_VALDOM_TIPOBS_FK"))
	private ValoresDominio valorTipoObservacion;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "OBSEXP_TRAM_ID", foreignKey =  @ForeignKey(name="GE_OBSEXP_TRAM_FK"))
	private TramiteExpediente tramiteExpdte;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "OBSEXP_TAR_ID", foreignKey =  @ForeignKey(name="GE_OBSEXP_TAR_FK"))
	private TareasExpediente tareaExpdte;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "OBSEXP_PLA_ID", foreignKey =  @ForeignKey(name="GE_OBSEXP_PLA_FK"))
	private PlazosExpdte plazoExpdte;
	
	@Column(name = "F_ENTRADA")
	private Date fechaEntrada;
	
	@Column(name = "T_TEXTO")
	@Size(max = 4000)
	private String texto;
	
	

	
}
