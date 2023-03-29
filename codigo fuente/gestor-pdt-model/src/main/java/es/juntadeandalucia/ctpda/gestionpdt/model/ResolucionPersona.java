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
@AuditTable(value = "GE_RESOL_PERSONA_H")
@Table(name = "GE_RESOL_PERSONA")
public class ResolucionPersona  extends Auditable{

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "RESPER_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_RESOL_PERS")
	@SequenceGenerator(name = "S_RESOL_PERS", sequenceName = "S_RESOL_PERS", allocationSize = 1)
	@Getter
	@Setter
	private Long id;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "RESPER_RES_ID", foreignKey = @ForeignKey(name = "GE_RESPER_RES_ID_FK"))
	private Resolucion resolucion;

	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "RESPER_PER_ID", foreignKey = @ForeignKey(name = "GE_RESPER_PER_ID_FK"))
	private Personas persona;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "RESPER_EXP_ID", foreignKey =  @ForeignKey(name="RESPER_EXP_FK"))
	private Expedientes expediente;	
	
	@Column(name = "N_PRINCIPAL")
	@NotNull
	@Getter
	@Setter
	private Boolean principal;
	
	@Column(name = "F_FECHA_NOTIFICACION")
	@Getter
	@Setter
	private Date fechaNotificacion;
	
	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;	
	
}
