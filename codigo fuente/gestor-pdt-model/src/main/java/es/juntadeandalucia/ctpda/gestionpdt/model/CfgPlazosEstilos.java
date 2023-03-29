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
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@EntityListeners(AuditorAwareImpl.class)
@Audited
@AuditTable(value = "GE_CFG_PLAZOS_ESTILOS_H")
@Table(name = "GE_CFG_PLAZOS_ESTILOS")
public class CfgPlazosEstilos extends Auditable{
	
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CFG_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_CFG_PLAZOS_ESTILOS")
	@SequenceGenerator(name = "S_CFG_PLAZOS_ESTILOS", sequenceName = "S_CFG_PLAZOS_ESTILOS", allocationSize = 1)
	@Getter
	@Setter
	private Long id;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "PLAEST_VALDOM_TIPEXP_ID", foreignKey = @ForeignKey(name = "GE_PLAEST_VALDOM_TIPEXP_FK"))
	private ValoresDominio valorTipoExpediente;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "PLAEST_VALDOM_TIPPLA_ID", foreignKey = @ForeignKey(name = "GE_PLAEST_VALDOM_TIPPLA_FK"))
	private ValoresDominio valorTipoPlazo;
	
    @NotNull
    @Getter
	@Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "V_TIPO_CONTROL")
    private TiposControl tipoControl;
    
	@Column(name = "N_LIMITE_INFERIOR")
	@Getter
	@Setter
	private Long limiteInferior;
	
	@Column(name = "N_LIMITE_SUPERIOR")
	@Getter
	@Setter
	private Long limiteSuperior;
	
	@Column(name = "D_ESTILO")
	@Getter
	@Setter
	private String estilo;

	
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
