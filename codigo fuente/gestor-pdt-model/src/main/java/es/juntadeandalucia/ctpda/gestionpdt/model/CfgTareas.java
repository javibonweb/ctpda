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
@AuditTable(value = "GE_CFG_TAREAS_H")
@Table(name = "GE_CFG_TAREAS")
@NamedEntityGraph(name = "cfgTareas.listaTiposTarea", 
attributeNodes = {
		@NamedAttributeNode("id"),
		@NamedAttributeNode("descripcion")
		})		    
public class CfgTareas extends Auditable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "CFGTAR_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_CFG_TAREAS")
	@SequenceGenerator(name = "S_CFG_TAREAS", sequenceName = "S_CFG_TAREAS", allocationSize = 1)
	@Getter @Setter
	private Long id;
	
	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;	

	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "CFGTAR_VALDOM_TIP_EXP_ID", foreignKey =  @ForeignKey(name="GE_CFG_TAREAS_TIP_EXP_FK"))
	private ValoresDominio valorTipoExpediente;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "CFGTAR_TIP_TRA_ID", foreignKey =  @ForeignKey(name="GE_CFG_TAREAS_TIP_TRA_FK"))
	private TipoTramite tipoTramite;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "CFGTAR_TIP_SUBTRA_ID", foreignKey =  @ForeignKey(name="GE_CFG_TAREAS_TIP_SUBTRA_FK"))
	private TipoTramite tipoSubtramite;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "CFGTAR_VALDOM_TIP_TAR_ID", foreignKey =  @ForeignKey(name="GE_CFG_TAREAS_TIP_TAR_FK"))
	private ValoresDominio valorTipoTarea;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "CFGTAR_VALDOM_TIP_TAR_ORIG_ID", foreignKey =  @ForeignKey(name="GE_CFG_TAREAS_TIP_TAR_ORIG_FK"))
	private ValoresDominio valorTipoTareaOrigen;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "CFGTAR_RESP_ID_DEFECTO", foreignKey =  @ForeignKey(name="GE_CFG_TAREAS_RESP_DEFECTO_FK"))
	private ResponsablesTramitacion responsableTramitacionDefecto;	
		
	@Column(name = "D_DESCRIPCION")
	@NotNull
	@Size(max = 50)
	@Getter @Setter
	private String descripcion;

	@Column(name = "D_DESC_ABREV")
	@Size(max = 15)
	@Getter	@Setter
	private String descripcionAbrev;

	@Column(name = "L_DOCUMENTO")
	@ColumnDefault("0")
	@NotNull
	@Getter @Setter
	private Boolean documento;

	@Column(name = "L_RESP_EXPED")
	@ColumnDefault("0")
	@NotNull
	@Getter @Setter
	private Boolean esResponsableExpediente;
	
	@Column(name = "L_RESP_TRAM")
	@ColumnDefault("0")
	@NotNull
	@Getter @Setter
	private Boolean esResponsableTramite;	
	
	@Column(name = "L_RESP_ANTERIOR")
	@ColumnDefault("0")
	@NotNull
	@Getter @Setter
	private Boolean esResponsableAnterior;	
	
	@Column(name = "N_PLAZO")
	@Getter @Setter
	private Integer plazo;

	@Column(name = "D_PLAZO_REFERENCIA")
	@Size(max = 15)
	@Getter	@Setter
	private String plazoReferencia;
	
	@Column(name = "N_DIAS_PLAZO_REFERENCIA")
	@Getter @Setter
	private Integer diasPlazoReferencia;
	
	
	@Column(name = "L_AUTO_ALTA")
	@ColumnDefault("0")
	@NotNull
	@Getter @Setter
	private Boolean autoAlta;
	
	@Column(name = "L_AUTO_ACEPTAR")
	@ColumnDefault("0")
	@NotNull
	@Getter @Setter
	private Boolean autoAceptar;
	
	@Column(name = "L_AUTO_FINALIZAR")
	@ColumnDefault("0")
	@NotNull
	@Getter @Setter
	private Boolean autoFinalizar;
	
	@Column(name = "L_ACTIVO")
	@ColumnDefault("1")
	@NotNull
	@Getter @Setter
	private Boolean activo;

	
	@Column(name = "L_TAREA_MANUAL")
	@ColumnDefault("0")
	@NotNull
	@Getter @Setter
	private Boolean tareamanual;
	
	@Column(name = "L_AVISO")
	@ColumnDefault("0")
	@NotNull
	@Getter @Setter
	private Boolean aviso;
	
	@Column(name = "L_BLOQUEO_DISTINTO_RESP")
	@ColumnDefault("0")
	@NotNull
	@Getter @Setter
	private Boolean bloqueoDistintoResponsable;
	
	@Column(name = "L_BLOQUEO_MISMO_RESP")
	@ColumnDefault("0")
	@NotNull
	@Getter @Setter
	private Boolean bloqueoMismoResponsable;
	
	@Column(name = "L_INFORME_CAMBIO_RESP")
	@ColumnDefault("0")
	@NotNull
	@Getter @Setter
	private Boolean informeCambioResponsable;
	
	@Column(name = "L_MODIF_DESCRIPCION")
	@ColumnDefault("1")
	@NotNull
	@Getter @Setter
	private Boolean descripcionModificable;
	
	@Column(name = "L_FINAL_ALTA_TRAM")
	@ColumnDefault("0")
	@NotNull
	@Getter @Setter
	private Boolean finalizarEnAltaTramite;
	
	@Column(name = "L_FINAL_ALTA_TAREA")
	@ColumnDefault("0")
	@NotNull
	@Getter @Setter
	private Boolean finalizarEnAltaTarea;
	
	@Column(name = "L_CIERRE_SUBTRA")
	@ColumnDefault("0")
	@NotNull
	@Getter @Setter
	private Boolean cierreSubTra;
	
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "CFGTAR_VALDOM_TIP_TAR_CAMB_ID", foreignKey =  @ForeignKey(name="GE_CFG_TAR_VDOM_TP_TAR_CM_ID_FK"))
	private ValoresDominio valorTipoTareaSiCambiaResp;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "CFGTAR_RESP_TAREA_VUELTA_ID", foreignKey =  @ForeignKey(name="GE_CFG_TAR_RESP_TAR_VTA_ID_FK"))
	private ResponsablesTramitacion responsableTareaVuelta;

	
	@Column(name = "L_CAMBIO_AUT_TRAMITE")
	@ColumnDefault("0")
	@NotNull
	@Getter @Setter
	private Boolean cambioAutomaticoTramite;
	
	@Column(name = "D_CIERRE_AUTO_TRAMITE")
	@NotNull
	@Size(max = 50)
	@Getter @Setter
	private String cierreAutomaticoTramite;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "CFGTAR_RESP_CAMBIO_AUT_ID", foreignKey =  @ForeignKey(name="CFGTAR_RESP_CAMBIO_AUT_FK"))
	private ResponsablesTramitacion responsableCambioAutomatico;	

}
