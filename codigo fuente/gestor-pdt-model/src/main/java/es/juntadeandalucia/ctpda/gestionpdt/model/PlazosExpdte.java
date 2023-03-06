package es.juntadeandalucia.ctpda.gestionpdt.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import es.juntadeandalucia.ctpda.gestionpdt.model.core.Auditable;
import es.juntadeandalucia.ctpda.gestionpdt.model.core.AuditorAwareImpl;
import es.juntadeandalucia.ctpda.gestionpdt.model.enums.Origenes;
import es.juntadeandalucia.ctpda.gestionpdt.util.FechaUtils;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@EntityListeners(AuditorAwareImpl.class)
@Audited
@AuditTable(value = "GE_PLAZOS_EXPDTE_H")
@Table(name = "GE_PLAZOS_EXPDTE")
public class PlazosExpdte extends Auditable{
	
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PLA_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_PLAZOS_EXPDTE")
	@SequenceGenerator(name = "S_PLAZOS_EXPDTE", sequenceName = "S_PLAZOS_EXPDTE", allocationSize = 1)
	@Getter
	@Setter
	private Long id;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "PLAEXP_EXP_ID", foreignKey = @ForeignKey(name = "GE_PLAEXP_EXP_FK"))
	private Expedientes expediente;
	
	@OneToOne( fetch = FetchType.EAGER)
	@JoinColumn(name = "PLAEXP_VALDOM_TIPPLA_ID", foreignKey = @ForeignKey(name = "GE_PLAEXP_VALDOMTIPPLA_FK"))
	private ValoresDominio valorTipoPlazo;
	
	@Column(name = "F_FECHA_LIMITE")
	@Getter
	@Setter
	@NotNull
	private Date fechaLimite;
	
    @Getter
	@Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "V_ORIGEN_INICIAL")
    private Origenes origenInicial;
    
    @Getter
	@Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "V_ORIGEN_FINAL")
    private Origenes origenFinal;

	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "PLA_EXP_OBSEXP_ID", foreignKey =  @ForeignKey(name="GE_PLA_EXP_OBSEXP_FK"))
	private ObservacionesExpedientes observaciones;

    
	@Column(name = "L_CUMPLIDO")
	@Getter
	@Setter
	private Boolean cumplido;
	
	@Column(name = "L_ACTIVO")
	@ColumnDefault("1")
	@NotNull
	@Getter
	@Setter
	private Boolean activo;
	
	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;	
	
    @Transient
    public String getDiasRestantes() {
    	int diasEntre = FechaUtils.diasEntre(FechaUtils.hoy(), fechaLimite);
    	return String.valueOf(diasEntre);
    }

}
