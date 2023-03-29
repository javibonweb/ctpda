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
@AuditTable(value = "GE_CFG_METADATOS_TRAM_H")
@Table(name = "GE_CFG_METADATOS_TRAM")
public class CfgMetadatosTram extends Auditable{
	
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CFG_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_CFG_METADATOS_TRAM")
	@SequenceGenerator(name = "S_CFG_METADATOS_TRAM", sequenceName = "S_CFG_METADATOS_TRAM", allocationSize = 1)
	@Getter
	@Setter
	private Long id;
	
	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;

	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "METTRAM_VALDOM_TIPEXP_ID", foreignKey = @ForeignKey(name = "GE_METTRAM_VALDOM_TIPEXP_FK"))
	private ValoresDominio valorTipoExpediente;
	
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "METTRAM_TIPTRAM_ID", foreignKey =  @ForeignKey(name="GE_METTRAM_TIPTRAM_FK"))
	private TipoTramite tipoTramite;	
	
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "METTRAM_TIPTRAM_SUP_ID", foreignKey =  @ForeignKey(name="GE_METTRAM_TIPTRAM_SUP_FK"))
	private TipoTramite tipoTramiteSuperior;	
	
	@Column(name = "D_BT_INCORPORAR_DOC")
	@Size(max = 50)
	@Getter
	@Setter
	private String btIncorporarDoc;
	
	@Column(name = "D_CANAL_ENTRADA")
	@Size(max = 50)
	@Getter
	@Setter
	private String canalEntrada;
	
	
	@Column(name = "D_IDENTIFICACION_ENTRADA")
	@Size(max = 50)
	@Getter
	@Setter
	private String identificacionEntrada;
	
	@Column(name = "D_BT_GENERAR_DOC")
	@Size(max = 50)
	@Getter
	@Setter
	private String btGenerarDoc;
	
	@Column(name = "D_TIPO_INTERESADO")
	@Size(max = 50)
	@Getter
	@Setter
	private String tipoInteresado;
	
	
	@Column(name = "D_IDENTIF_INTERESADO")
	@Size(max = 50)
	@Getter
	@Setter
	private String identifInteresado;
	
	
	@Column(name = "D_CANAL_SALIDA")
	@Size(max = 50)
	@Getter
	@Setter
	private String canalSalida;
	
	@Column(name = "D_DATOS_CANAL_SALIDA")
	@Size(max = 50)
	@Getter
	@Setter
	private String datosCanalSalida;
	
	
	@Column(name = "D_NOTIF_INFRUCTUOSA")
	@Size(max = 50)
	@Getter
	@Setter
	private String notifInfructuosa;
	
	@Column(name = "D_SUBSANA_ADECUADAMENTE")
	@Size(max = 50)
	@Getter
	@Setter
	private String subsanaAdecuadamente;
	
	@Column(name = "D_TIPO_ADMISION")
	@Size(max = 50)
	@Getter
	@Setter
	private String tipoAdmision;
	
	@Column(name = "D_MOTIVO_INADMISION")
	@Size(max = 50)
	@Getter
	@Setter
	private String motivoInadmision;
	
	
	@Column(name = "D_INFORM_REMITIDA")
	@Size(max = 50)
	@Getter
	@Setter
	private String informRemitida;
	
	@Column(name = "D_SENTIDO_RESOLUCION")
	@Size(max = 50)
	@Getter
	@Setter
	private String sentidoResolucion;
	
	@Column(name = "D_TIPO_RESOLUCION")
	@Size(max = 50)
	@Getter
	@Setter
	private String tipoResolucion;
	
	@Column(name = "D_NUM_RESOLUCION")
	@Size(max = 50)
	@Getter
	@Setter
	private String numResolucion;
	
	@Column(name = "D_ACREDITA_CUMPLIMIENTO")
	@Size(max = 50)
	@Getter
	@Setter
	private String acreditaCumplimiento;
	
	@Column(name = "D_FECHA_ENTRADA")
	@Size(max = 50)
	@Getter
	@Setter
	private String fechaEntrada;
	
	@Column(name = "D_FECHA_ENVIO")
	@Size(max = 50)
	@Getter
	@Setter
	private String fechaEnvio;
	
	@Column(name = "D_FECHA_NOTIFICACION")
	@Size(max = 50)
	@Getter
	@Setter
	private String fechaNotificacion;
	
	@Column(name = "D_FECHA_EMISION")
	@Size(max = 50)
	@Getter
	@Setter
	private String fechaEmision;
	
	@Column(name = "D_FECHA_FIRMA")
	@Size(max = 50)
	@Getter
	@Setter
	private String fechaFirma;
	
	@Column(name = "D_FECHA_INFORME")
	@Size(max = 50)
	@Getter
	@Setter
	private String fechaInforme;
	
	@Column(name = "D_FECHA_SUBSANACION")
	@Size(max = 50)
	@Getter
	@Setter
	private String fechaSubsanacion;
	
	@Column(name = "D_FECHA_RESPUESTA")
	@Size(max = 50)
	@Getter
	@Setter
	private String fechaRespuesta;
	
	@Column(name = "D_FECHA_REGISTRO")
	@Size(max = 50)
	@Getter
	@Setter
	private String fechaRegistro;
	
	@Column(name = "D_FECHA_ACREDITACION")
	@Size(max = 50)
	@Getter
	@Setter
	private String fechaAcreditacion;
	
	@Column(name = "D_FECHA_LIMITE")
	@Size(max = 50)
	@Getter
	@Setter
	private String fechaLimite;
	
	@Column(name = "D_FECHA_RESOLUCION")
	@Size(max = 50)
	@Getter
	@Setter
	private String fechaResolucion;
	
	@Column(name = "D_TIPO_PLAZO")
	@Size(max = 50)
	@Getter
	@Setter
	private String tipoPlazo;
	
	@Column(name = "D_PLAZO")
	@Size(max = 50)
	@Getter
	@Setter
	private String plazo;
	
	
	@Column(name = "D_AFECTA_PLAZOS")
	@Size(max = 50)
	@Getter
	@Setter
	private String afectaPlazos;
	
	
	@Column(name = "L_ACTIVO")
	@ColumnDefault("1")
	@NotNull
	@Getter
	@Setter
	private Boolean activo;

	
	@Column(name = "D_CANAL_INF_ENTRADA")
	@Size(max = 50)
	@Getter
	@Setter
	private String datosCanalInfEntrada;
	
	@Column(name = "D_CANAL_INF_SALIDA")
	@Size(max = 50)
	@Getter
	@Setter
	private String datosCanalInfSalida;
	
	@Column(name = "D_API")
	@Size(max = 50)
	@Getter
	@Setter
	private String api;
	
	@Column(name = "D_INSTRUCTOR_API")
	@Size(max = 50)
	@Getter
	@Setter
	private String instructorAPI;
	
	@Column(name = "D_PROPUESTA_API")
	@Size(max = 50)
	@Getter
	@Setter
	private String propuestaAPI;
	
	@Column(name = "D_NUMERO_PSAN")
	@Size(max = 50)
	@Getter
	@Setter
	private String numeroPsan;
	
	@Column(name = "D_BT_GENERAR_EXP")
	@Size(max = 50)
	@Getter
	@Setter
	private String btGenerarExp;
	
	@Column(name = "D_BT_VINCULAR_EXP")
	@Size(max = 50)
	@Getter
	@Setter
	private String btVincularExp;
	
	
	@Column(name = "D_EXTRACTO")
	@Size(max = 50)
	@Getter
	@Setter
	private String extracto;
	
	@Column(name = "D_EXTRACTO_EXPDTE")
	@Size(max = 50)
	@Getter
	@Setter
	private String extractoExpediente;
	
	@Column(name = "D_ANTECEDENTES")
	@Size(max = 50)
	@Getter
	@Setter
	private String antecedentes;
	
	@Column(name = "D_ANTEC_EXPDTE")
	@Size(max = 50)
	@Getter
	@Setter
	private String antecedentesExpediente;
	
	@Column(name = "D_ACUSE_RECIBO")
	@Size(max = 50)
	@Getter
	@Setter
	private String acuseRecibo;
	
	@Column(name = "D_FIRMANTE")
	@Size(max = 50)
	@Getter
	@Setter
	private String firmante;
	
	@Column(name = "D_TIPO_FIRMA")
	@Size(max = 50)
	@Getter
	@Setter
	private String tipoFirma;
	
	
	@Column(name = "D_BT_VINCULAR_DOC")
	@Size(max = 50)
	@Getter
	@Setter
	private String btVincularDoc;
	
	@Column(name = "D_ACTO_REC")
	@Size(max = 50)
	@Getter
	@Setter
	private String actoRecurrido;
	
	@Column(name = "D_BT_EMPUJAR_DOC")
	@Size(max = 50)
	@Getter
	@Setter
	private String btEmpujarDoc;
	
}
