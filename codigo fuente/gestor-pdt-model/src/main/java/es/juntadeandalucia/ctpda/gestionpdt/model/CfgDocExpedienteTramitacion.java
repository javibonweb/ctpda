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
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
@AuditTable(value = "GE_CFG_DOC_EXPED_TRAM_H")
@Table(name = "GE_CFG_DOC_EXPED_TRAM")
@NamedEntityGraph(name = "cfgDocExpedienteTramitacion.listaTiposDocumento", 
attributeNodes = {
		@NamedAttributeNode("id"),
		@NamedAttributeNode("descripcionTipoDoc")
		//,@NamedAttributeNode("descripcionAbrevTipoDoc")
		})
public class CfgDocExpedienteTramitacion extends Auditable {

	public static final String ORIGEN_ADMINISTRACION = "1";
	public static final String ORIGEN_CIUDADANO = "0";

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@NotNull
	@Column(name = "CFG_DOC_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_CFG_DOC_EXPDTE_TRAM")
	@SequenceGenerator(name = "S_CFG_DOC_EXPDTE_TRAM", sequenceName = "S_CFG_DOC_EXPDTE_TRAM", allocationSize = 1)
	@Getter
	@Setter
	private Long id;
	
	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;
	
	
	@Column(name = "T_COD_REL_DET")
	@NotNull
	@Size(max = 10)
	@Getter
	@Setter
	private String codigoRelacion;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "CFGDOC_VALDOM_TIPEXP_ID", foreignKey =  @ForeignKey(name="CFGDOC_VALDOM_TIPEXP_FK"))
	private ValoresDominio valorTipoExpediente;	
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "CFGDOC_VALDOM_SIT_ID", foreignKey =  @ForeignKey(name="CFGDOC_VALDOM_SIT_FK"))
	private ValoresDominio situacion;	

	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "CFGDOC_TIP_TRA_ID", foreignKey =  @ForeignKey(name="CFGDOC_TIP_TRA_FK"))
	private TipoTramite tipoTramite;	
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "CFGDOC_TIP_TRA_SUP_ID", foreignKey =  @ForeignKey(name="CFGDOC_TIP_TRA_SUP_FK"))
	private TipoTramite tipoTramiteSup;	

	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "CFGDOC_TIP_DOC_ID", foreignKey =  @ForeignKey(name="CFGDOC_TIP_DOC_FK"))
	private TipoDocumento tipoDocumento;	
	
	@Column(name = "D_DESC_TIP_DOC")
	@Size(max = 255)
	@Getter
	@Setter
	private String descripcionTipoDoc;

	@Column(name = "D_DESC_ABREV_TIP_DOC")
	@Size(max = 100)
	@Getter
	@Setter
	private String descripcionAbrevTipoDoc;
	
	@Column(name = "T_CODIGO_TIPO_ENI")
	@NotNull
	@Size(max = 10)
	@Getter
	@Setter
	private String codigoTipoENI;
	
	@Column(name = "T_CODIGO_ORIGEN_ENI")
	@ColumnDefault("0")
	@NotNull
	@Size(max = 1)
	@Getter
	@Setter
	private String codigoOrigen;
	
	@Column(name = "L_OBLIGATORIO")
	@ColumnDefault("1")
	@NotNull
	@Getter
	@Setter
	private Boolean obligatorio;
	
	@Column(name = "L_ACTIVO")
	@ColumnDefault("1")
	@NotNull
	@Getter
	@Setter
	private Boolean activo;

}
