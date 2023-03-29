package es.juntadeandalucia.ctpda.gestionpdt.model;

import java.util.Date;
import java.util.Objects;

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
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import es.juntadeandalucia.ctpda.gestionpdt.model.core.Auditable;
import es.juntadeandalucia.ctpda.gestionpdt.model.core.AuditorAwareImpl;
import es.juntadeandalucia.ctpda.gestionpdt.model.core.EntidadBasica;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@EntityListeners(AuditorAwareImpl.class)
@Audited
@AuditTable(value = "GE_EXPEDIENTES_H")
@Table(name = "GE_EXPEDIENTES")
@NamedEntityGraph(name="expediente.basico", attributeNodes= {
		@NamedAttributeNode("id"),
	    //@NamedAttributeNode("version"),
	    @NamedAttributeNode("nombreExpediente"),
	    @NamedAttributeNode("numExpediente"),
		@NamedAttributeNode("fechaEntrada")
})
@NamedEntityGraph(name="expediente.resumen", attributeNodes= {
		@NamedAttributeNode("id"),
	    //@NamedAttributeNode("version"),
	    @NamedAttributeNode("nombreExpediente"),
	    @NamedAttributeNode("numExpediente"),
		@NamedAttributeNode("fechaEntrada"),
		@NamedAttributeNode(value = "valorTipoExpediente", subgraph = "valorDominio.basico"),
		@NamedAttributeNode(value = "valorSituacionExpediente", subgraph = "valorDominio.basico")
})
public class Expedientes extends Auditable {
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "EXP_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_EXPEDIENTES")
	@SequenceGenerator(name = "S_EXPEDIENTES", sequenceName = "S_EXPEDIENTES", allocationSize = 1)
	@Getter
	@Setter
	private Long id;

	@Column(name = "T_NUMERO_EXPEDIENTE")
	@Size(max = 255)
	@NotNull
	@Getter
	@Setter
	private String numExpediente;

	@Column(name = "N_RESOLUCION_WEB")
	@Max(1)
	@Getter
	@Setter
	private Long resolucionWeb;

	@Column(name = "F_FECHA_ENTRADA")
	@Getter
	@Setter
	
	private Date fechaEntrada;
	

	@Column(name = "T_NOMBRE_EXPEDIENTE")
	@Size(max = 255)
	@Getter
	@Setter
	private String nombreExpediente;

	@Column(name = "T_MOTIVO")
	@Size(max = 500)
	@Getter
	@Setter
	private String motivo;

	@Column(name = "F_FECHA_REGISTRO")
	@Getter
	@Setter
	private Date fechaRegistro;

	@Column(name = "T_NUMERO_REGISTRO")
	@Size(max = 50)
	@Getter
	@Setter
	private String numRegistro;

	@Column(name = "T_ETIQUETA1")
	@Size(max = 100)
	@Getter
	@Setter
	
	private String etiqueta1;
	@Column(name = "T_ETIQUETA2")
	@Size(max = 100)
	@Getter
	@Setter
	
	private String etiqueta2;
	@Column(name = "T_ETIQUETA3")
	@Size(max = 100)
	@Getter
	@Setter
	private String etiqueta3;
	
	@OneToOne (fetch=FetchType.LAZY)
	@NotNull
	@JoinColumn(name= "EXP_VALDOM_TIPEXP_ID", foreignKey =  @ForeignKey(name="GE_EXP_VALDOM_TIPEXP_FK"))
	private ValoresDominio valorTipoExpediente;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "EXP_VALDOM_TIPINF_ID", foreignKey =  @ForeignKey(name="GE_EXP_VALDOM_TIPINF_FK"))
	private ValoresDominio valorTipoInfraccion;
	
	@OneToOne (fetch=FetchType.LAZY)
	@NotNull
	@JoinColumn(name= "EXP_VALDOM_SIT_ID", foreignKey =  @ForeignKey(name="GE_EXP_VALDOM_SIT_FK"))
	private ValoresDominio valorSituacionExpediente;	
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "EXP_RESTRA_ID", foreignKey =  @ForeignKey(name="GE_EXPEDIENTES_RESP_FK"))
	private ResponsablesTramitacion responsable;	
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "EXP_USR_MOD_MOTIVO", foreignKey =  @ForeignKey(name="GE_EXP_USR_MOD_MOTIVO_FK"))
	private Usuario usuarioModifMotivo;	
	
	@Column (name="F_FECHA_HORA_MOD_MOTIVO")
	@Getter @Setter
	private Date fechaHoraModifMotivo;

	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "EXP_VALDOM_CANENT_ID", foreignKey =  @ForeignKey(name="GE_EXP_VALDOM_CANENT_FK"))
	private ValoresDominio valorCanalEntrada;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "EXP_VALDOM_MOTINADM_ID", foreignKey =  @ForeignKey(name="GE_EXP_VALDOM_MOTINADM_FK"))
	private ValoresDominio valorMotivoInadmision;
	
	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;	
	
	@Column(name = "L_COMPETENCIA_CTPDA")
	private Boolean competenciaCtpda;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "EXP_VALDOM_AUTCOMP_ID", foreignKey =  @ForeignKey(name="GE_EXP_VALDOM_AUTCOMP_FK"))
	private ValoresDominio valorAutoridadCompetente;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "EXP_VALDOM_INSTAPI_ID", foreignKey =  @ForeignKey(name="GE_EXP_VALDOM_INSTAPI_ID_FK"))
	private ValoresDominio valorInstructorAPI;
	
	/**
	 * en el SP15 se deja de usar este campo, pero se deja en BD y mapeo por posible uso de nuevo a futuro
	 */
	@Column(name = "L_AEPD")
	private Boolean aepd;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "EXP_VALDOM_TIPADM_ID", foreignKey =  @ForeignKey(name="GE_EXP_VALDOM_TIPADM_FK"))
	private ValoresDominio valorTipoAdmision;
	
	@Column(name = "L_IMPOSICION_MEDIDAS")
	private Boolean imposicionMedidas;
	
	@Column(name = "L_API")
	private Boolean api;
	
	@Column(name = "L_OPOSICION_PERSONA")
	@ColumnDefault("0")
	private Boolean oposicionPersona;
	
	@Column(name = "L_OPOSICION_REPRESENTANTE")
	@ColumnDefault("0")
	private Boolean oposicionRepresentante;
	
	@Column(name = "L_TRAMITACION_ANONIMA")
	@NotNull
	@ColumnDefault("0")
	private Boolean tramitacionAnonima;
	
	@Column(name = "L_ABSTENCION_RECUSACION")
	@NotNull
	@ColumnDefault("0")
	private Boolean abstencionRecusacion;
	
	@Column (name="F_ULTIMA_PERSISTENCIA")
	@Getter @Setter
	private Date fechaUltimaPersistencia;
	
	@Column (name="USU_ULTIMA_PERSISTENCIA")
	@Size(max=25)
	@Getter @Setter
	private String usuUltimaPersistencia;
	
	@Column(name = "T_EXPEDIENTE_IDENTIFICADO")
	@Size(max = 255)
	@Getter
	@Setter
	private String expedienteIdentificado;
	
	@Column(name = "D_SIT_ADICIONAL")
	@Size(max = 255)
	@Getter
	@Setter
	private String situacionAdicional;
	
	@Column(name = "D_DESC_SEG1")
	@Size(max = 20)
	@Getter
	@Setter
	private String descSeguimiento1;

	@Column(name = "D_DESC_SEG2")
	@Size(max = 20)									   
	@Getter
	@Setter
	private String descSeguimiento2;
	
	@Column(name = "D_DESC_SEG3")
	@Size(max = 20)
	@Getter
	@Setter
	private String descSeguimiento3;

	@Column(name = "D_INF_SEG1")
	@Size(max = 50)
	@Getter
	@Setter
	private String infSeguimiento1;

	@Column(name = "D_INF_SEG2")
	@Size(max = 50)
	@Getter
	@Setter
	private String infSeguimiento2;

	@Column(name = "D_INF_SEG3")
	@Size(max = 50)
	@Getter
	@Setter
	private String infSeguimiento3;
	

	@Column(name = "T_DESCRIPCION")
	@Size(max = 4000)
	@Getter
	@Setter
	private String descripcion;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "EXP_USR_MOD_DESC", foreignKey =  @ForeignKey(name="GE_EXP_USR_MOD_DESC_FK"))
	private Usuario usuarioModifDescripcion;	
	
	@Column (name="F_FECHA_HORA_MOD_DESC")
	@Getter 
	@Setter
	private Date fechaHoraModifDescr;
	
	@Column (name="F_FECHA_INFRACCION")
	@Getter 
		
	@Setter
	private Date fechaInfraccion;
	
	@Column(name = "L_INFRACCION_CONT")
	@ColumnDefault("0")
	@Getter 
	@Setter
	private Boolean infraccionContinuada;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "EXP_VALDOM_GRAV", foreignKey =  @ForeignKey(name="GE_EXP_VALDOM_GRAV_FK"))
	@Getter 
	@Setter
	private ValoresDominio valorDominioGravedad;
	
	@Column (name="F_FECHA_PRESCRIPCIOM")
	@Getter 
		
	@Setter
	private Date fechaPrescripcion;
	
	@Column (name="F_FECHA_SOLIC_EJ")
	@Getter 
	@Setter
	private Date fechaSolicitudEjercicio;
	
	@Column(name = "L_RESPUESTA_SOLIC")
	@ColumnDefault("0")
	@Getter 
	@Setter
	private Boolean respuestaSolicitudEjercicio;
	
	@Column(name = "L_RESPUESTA_INSAT")
	@ColumnDefault("0")
	@Getter 
	@Setter
	private Boolean respuestaInsatisfactoria;
	
	@Column (name="F_FECHA_RESP_SOLIC_EJ")
	@Getter 
	@Setter
	private Date fechaRespuestaSolicitudEjercicio;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "EXP_VALDOM_ORIGREC", foreignKey =  @ForeignKey(name="GE_EXP_VALDOM_ORIGREC_FK"))
	@Getter 
	@Setter
	private ValoresDominio valorDominioOrigen;
	
	//************************************************************************
	
	public String getMigaDePan() {
		String txtMiga = "";
	
		txtMiga = this.numExpediente;
		txtMiga = Boolean.FALSE.equals(this.tramitacionAnonima)? txtMiga: txtMiga + " - <span style=\"color:red; \">" + "TRAMITACIÓN ANÓNIMA" + " </span>";
		txtMiga = Boolean.FALSE.equals(this.abstencionRecusacion)? txtMiga: txtMiga + " - <span style=\"color:red; \">" + "ABSTENCIÓN/RECUSACIÓN" + " </span>";
		txtMiga = this.nombreExpediente == null? txtMiga: txtMiga + " - " + this.nombreExpediente;
		txtMiga = this.valorSituacionExpediente == null? txtMiga: txtMiga + " - " + this.valorSituacionExpediente.getDescripcion();
		txtMiga = this.situacionAdicional == null? txtMiga: txtMiga + " - " + this.situacionAdicional;

		return txtMiga; 
	} 
	
	
	//------------------------------------------
    
	@Transient
	private ResponsablesTramitacion responsableActual;
	
	@PostLoad
	private void init() {
		this.responsableActual = this.responsable;
	}
	
	@PostPersist @PostUpdate
	private void saved() {
		this.responsableActual = this.responsable;
	}
	
	public boolean cambiaResponsable() {
		return !mismaEntidad(responsable, responsableActual);
	}
	
	private <E extends EntidadBasica> boolean mismaEntidad(E e1, E e2) {
		final Long id1 = EntidadBasica.getIdEntidad(e1);
		final Long id2 = EntidadBasica.getIdEntidad(e2);
		
		return Objects.equals(id1, id2);	
	}
	
}
