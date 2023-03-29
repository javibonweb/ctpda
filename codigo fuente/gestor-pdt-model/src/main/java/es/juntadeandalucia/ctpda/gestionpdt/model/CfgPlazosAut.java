package es.juntadeandalucia.ctpda.gestionpdt.model;

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
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import es.juntadeandalucia.ctpda.gestionpdt.model.core.Auditable;
import es.juntadeandalucia.ctpda.gestionpdt.model.core.AuditorAwareImpl;
import es.juntadeandalucia.ctpda.gestionpdt.model.enums.OrigenAut;
import es.juntadeandalucia.ctpda.gestionpdt.model.enums.TipoAccion;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@EntityListeners(AuditorAwareImpl.class)
@Audited
@AuditTable(value = "GE_CFG_PLAZOS_AUT_H")
@Table(name = "GE_CFG_PLAZOS_AUT")
public class CfgPlazosAut extends Auditable{
	
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CFG_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_CFG_PLAZOS_AUT")
	@SequenceGenerator(name = "S_CFG_PLAZOS_AUT", sequenceName = "S_CFG_PLAZOS_AUT", allocationSize = 1)
	@Getter
	@Setter
	private Long id;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "PLAAUT_VALDOM_TIPEXP_ID", foreignKey = @ForeignKey(name = "GE_PLAAUT_VALDOM_TIPEXP_FK"))
	private ValoresDominio valorTipoExpediente;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "PLAAUT_TIPTRAM_ID", foreignKey = @ForeignKey(name = "GE_PLAAUT_TIPTRAM_FK"))
	private TipoTramite tipoTramite;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "PLAAUT_TIPTRAMSUP_ID", foreignKey = @ForeignKey(name = "GE_PLAAUT_TIPTRAMSUP_FK"))
	private TipoTramite tipoTramiteSuperior;
	
    @Getter
	@Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "V_ORIGEN")
    private OrigenAut origen;
    
    
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "PLAAUT_VALDOM_PLAZO_ID", foreignKey = @ForeignKey(name = "GE_PLAAUT_VALDOM_PLAZO_FK"))
	private ValoresDominio valorPlazo;
    
	
	@Column(name = "D_ACCION_ESPECIAL")
	@Getter
	@Setter
	private String accionEspecial;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "PLAAUT_VALDOM_TIPPLA_ID", foreignKey = @ForeignKey(name = "GE_PLAAUT_VALDOM_TIPPLA_FK"))
	private ValoresDominio valorTipoPlazo;
	
	@Column(name = "N_PLAZO")
	@Getter
	@Setter
	private Integer plazo;
	
    
    @Getter
	@Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "V_TIPO_ACCION")
    private TipoAccion tipoAccion;
    
	@Column(name = "L_AVISO")
	@Getter
	@Setter
	private Boolean aviso;
	
	@Column(name = "L_ACTIVO")
	@ColumnDefault("1")
	@NotNull
	@Getter
	@Setter
	private Boolean activo;
	
	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;	

}
