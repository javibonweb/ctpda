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
import javax.persistence.Lob;
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
@AuditTable(value = "GE_DETALLE_EXPDTE_TRAM_H")
@Table(name = "GE_DETALLE_EXPDTE_TRAM")

@NamedEntityGraph(name = "detalleTram.completo", includeAllAttributes = true,
attributeNodes = {
		@NamedAttributeNode(value = "expediente", subgraph = "expediente.basico"),
		@NamedAttributeNode(value = "tramiteExpediente", subgraph = "tramiteExp.basico"),
		@NamedAttributeNode(value = "valorCanalEntrada", subgraph = "valoresDominio.basico"),
		@NamedAttributeNode(value = "valorCanalSalida", subgraph = "valoresDominio.basico"),
		@NamedAttributeNode(value = "valorMotivoInadmision", subgraph = "valoresDominio.basico"),
		@NamedAttributeNode(value = "valorTipoInteresado", subgraph = "valoresDominio.basico"),
		@NamedAttributeNode(value = "valorResultadoNotificacion", subgraph = "valoresDominio.basico"),
		@NamedAttributeNode(value = "valorTipoAdmision", subgraph = "valoresDominio.basico"),
		@NamedAttributeNode(value = "valorSentidoResolucion", subgraph = "valoresDominio.basico"),
		@NamedAttributeNode(value = "valorTipoResolucion", subgraph = "valoresDominio.basico"),
		@NamedAttributeNode(value = "valorTipoPlazo", subgraph = "valoresDominio.basico"),
		@NamedAttributeNode(value = "valorDominioInteresado", subgraph = "valoresDominio.basico"),
		@NamedAttributeNode(value = "valorCanalInfEntrada", subgraph = "valoresDominio.basico"),
		@NamedAttributeNode(value = "valorCanalInfSalida", subgraph = "valoresDominio.basico"),
		@NamedAttributeNode(value = "valorInstructorAPI", subgraph = "valoresDominio.basico"),
		@NamedAttributeNode(value = "valorDominioPropuestaApi", subgraph = "valoresDominio.basico")
})

public class DetalleExpdteTram extends Auditable{
	
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	public static final String CODIGO_VALDOM_PERS = "PERS";
	public static final String CODIGO_VALDOM_DPD = "DPD";
	public static final String CODIGO_VALDOM_AUTCON = "AUTCON";
	public static final String CODIGO_VALDOM_SUJOBL = "SUJOBL";
	

	@Id
	@Column(name = "DET_EXP_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_DETALLE_EXPDTE_TRAM")
	@SequenceGenerator(name = "S_DETALLE_EXPDTE_TRAM", sequenceName = "S_DETALLE_EXPDTE_TRAM", allocationSize = 1)
	@Getter
	@Setter
	private Long id;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "DETEXPTRAM_EXP_ID", foreignKey = @ForeignKey(name = "GE_DETEXPTRAM_EXP_FK"))
	private Expedientes expediente;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "DETEXPTRAM_TRAMEXP_ID", foreignKey = @ForeignKey(name = "GE_DETEXPTRAM_TRAMEXP_FK"))
	private TramiteExpediente tramiteExpediente;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "DETEXPTRAM_VALDOMCANENT_ID", foreignKey = @ForeignKey(name = "GE_DETEXPTRAM_VALDOMCANENT_FK"))
	private ValoresDominio valorCanalEntrada;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "DETEXTRAM_VALDOMMOTINAD_ID", foreignKey = @ForeignKey(name = "GE_DETEXTRAM_VALDOMMOTINAD_FK"))
	private ValoresDominio valorMotivoInadmision;
	
	@Column(name = "D_IDENTIF_ENTRADA")
	@Getter
	@Setter
	private String identifEntrada;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "DETEXPTRAM_VALDOMTIPINT_ID", foreignKey = @ForeignKey(name = "GE_DETEXPTRAM_VALDOMTIPINT_FK"))
	private ValoresDominio valorTipoInteresado;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "DETEXPTRAM_VALDOMCANSAL_ID", foreignKey = @ForeignKey(name = "GE_DETEXPTRAM_VALDOMCANSAL_FK"))
	private ValoresDominio valorCanalSalida;
	
	
	@Column(name = "D_DATOS_CANAL_ENTRADA")
	@Getter
	@Setter
	private String datosCanalEntrada;	
	
	@Column(name = "D_DATOS_CANAL_SALIDA")
	@Getter
	@Setter
	private String datosCanalSalida;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "DETEXPTRAM_VALDOMRESNOTIF_ID", foreignKey = @ForeignKey(name = "GE_DETEXPTRAM_VALDOMRESNOTIF_FK"))
	private ValoresDominio valorResultadoNotificacion;
	
	
	@Column(name = "L_SUBSANA_ADECUADAMENTE")
	@ColumnDefault("0")
	@Getter
	@Setter
	private Boolean subsanaAdecudamente;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "DETEXPTRAM_VALDOMTIPADM_ID", foreignKey = @ForeignKey(name = "GE_DETEXPTRAM_VALDOMTIPADM_FK"))
	private ValoresDominio valorTipoAdmision; 
	
	@Column(name = "L_INFO_REMITIDA")
	@ColumnDefault("0")
	@Getter
	@Setter
	private Boolean infoRemitida;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "DETEXPTRAM_VALDOMSENRES_ID", foreignKey = @ForeignKey(name = "GE_DETEXPTRAM_VALDOMSENRES_FK"))
	private ValoresDominio valorSentidoResolucion;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "DETEXPTRAM_VALDOMTIPRES_ID", foreignKey = @ForeignKey(name = "GE_DETEXPTRAM_VALDOMTIPRES_FK"))
	private ValoresDominio valorTipoResolucion;
	
	@Column(name = "D_NUM_RESOLUCION")
	@Getter
	@Setter
	private String numResolucion;
	
	@Column(name = "L_ACREDITA_CUMPLIMIENTO")
	@ColumnDefault("0")
	@Getter
	@Setter
	private Boolean acreditaCumplimiento;
	
	@Column(name = "F_FECHA_ENTRADA")
	@Getter
	@Setter
	private Date fechaEntrada;
	
	@Column(name = "F_FECHA_ENVIO")
	@Getter
	@Setter
	private Date fechaEnvio;
	
	@Column(name = "F_FECHA_NOTIFICACION")
	@Getter
	@Setter
	private Date fechaNotificacion;
	
	@Column(name = "F_FECHA_EMISION")
	@Getter
	@Setter
	private Date fechaEmision;
	
	@Column(name = "F_FECHA_FIRMA")
	@Getter
	@Setter
	private Date fechaFirma;
	
	@Column(name = "F_FECHA_INFORME")
	@Getter
	@Setter
	private Date fechaInforme;
	
	@Column(name = "F_FECHA_SUBSANACION")
	@Getter
	@Setter
	private Date fechaSubsanacion;
	
	
	@Column(name = "F_FECHA_RESPUESTA")
	@Getter
	@Setter
	private Date fechaRespuesta;
	
	@Column(name = "F_FECHA_ACREDITACION")
	@Getter
	@Setter
	private Date fechaAcreditacion;
	
	@Column(name = "F_FECHA_LIMITE")
	@Getter
	@Setter
	private Date fechaLimite;
	
	@Column(name = "F_FECHA_RESOLUCION")
	@Getter
	@Setter
	private Date fechaResolucion;
	
	@Column(name = "F_FECHA_REGISTRO")
	@Getter
	@Setter
	private Date fechaRegistro;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "DETEXPTRAM_VALDOMTIPPLA_ID", foreignKey = @ForeignKey(name = "GE_DETEXPTRAM_VALDOMTIPPLA_FK"))
	private ValoresDominio valorTipoPlazo;
	
	@Column(name = "N_PLAZO")
	@Getter
	@Setter
	private Integer plazo;
	
	@Column(name = "L_AFECTA_PLAZOS")
	@ColumnDefault("0")
	@Getter
	@Setter
	private Boolean afectaPlazos;
	
	@Column(name = "L_ACTIVO")
	@ColumnDefault("1")
	@NotNull
	@Getter
	@Setter
	private Boolean activo;
	
	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;	
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "DETEXPTRAM_VALDOM_INT_ID", foreignKey = @ForeignKey(name = "GE_DETEXPTRAM_VALDOM_INT_FK"))
	private ValoresDominio valorDominioInteresado;
	
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "DETEXPTRAM_PER_INT_ID", foreignKey = @ForeignKey(name = "GE_DETEXPTRAM_PER_INT_FK"))
	private Personas personasInteresado;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "DETEXPTRAM_SUJOBL_INT_ID", foreignKey = @ForeignKey(name = "GE_DETEXPTRAM_SUJOBL_INT_FK"))
	private SujetosObligados sujetosObligadosInteresado;

	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "VALDOM_CANAL_INF_ENT_ID", foreignKey = @ForeignKey(name = "GE_VALDOM_CANAL_INF_ENT_ID_FK"))
	private ValoresDominio valorCanalInfEntrada;

	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "VALDOM_CANAL_INF_SAL_ID", foreignKey = @ForeignKey(name = "GE_VALDOM_CANAL_INF_SAL_ID_FK"))
	private ValoresDominio valorCanalInfSalida;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "DET_VALDOM_INSTAPI_ID", foreignKey =  @ForeignKey(name="GE_DET_VALDOM_INSTAPI_ID_FK"))
	private ValoresDominio valorInstructorAPI;
	
	@Column(name = "L_API")
	@ColumnDefault("0")
	@Getter
	@Setter
	private Boolean api;
	
	@Column(name = "D_SEGUIMIENTO_CABECERA")
	@Getter
	@Setter
	private String seguimientoCabecera;
	
	@Column(name = "L_IMPOSICION_MEDIDAS")
	@ColumnDefault("0")
	private Boolean imposicionMedidas;

	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "DETEXPTRAM_VALDOM_PROPAPI_ID", foreignKey = @ForeignKey(name = "GE_DETEXPTRAM_VALDOM_PROP_API_FK"))
	private ValoresDominio valorDominioPropuestaApi;
	
	@Column(name = "D_NUMERO_PSAN")
	@Size(max = 50)
	@Getter
	@Setter
	private String numeroPsan;
		
		
	@Column(name = "T_EXTRACTO")
	@Lob
	private String textoExtractoExpediente;
	
	@Column(name = "L_EXTRACTO_EXPDTE")
	@ColumnDefault("0")
	private Boolean extractoExpediente;
	
	@Column(name = "T_ANTECEDENTES")
	@Lob
	private String textoAntecedentesExpediente;
	
	@Column(name = "L_ANTEC_EXPDTE")
	@ColumnDefault("0")
	private Boolean antecedentesExpediente;
	
	@Column(name = "L_ACUSE_RECIBO")
	@ColumnDefault("1")
	@Getter
	@Setter
	private Boolean acuseRecibo;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "DETEXPTRAM_FIRMANTE_ID", foreignKey = @ForeignKey(name = "GE_DETEXPTRAM_FIRMANTE_FK"))
	private Usuario firmante;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "DETEXPTRAM_VDOM_TIPFIRM_ID", foreignKey = @ForeignKey(name = "GE_DETEXPTRAM_VDOM_TIPFIRM_FK"))
	private ValoresDominio valorDominioTipoFirma;

	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "DETEXPTRAM_VDOM_ACTREC_ID", foreignKey = @ForeignKey(name = "GE_DETEXPTRAM_VDOM_ACTREC_FK"))
	private ValoresDominio valorDominioActoRec;

	public String getInterviniente()
	{
		String interviniente = "";
		String codigoTipoInteresado = "";
		if(this.valorTipoInteresado!= null)
		{
			codigoTipoInteresado = this.valorTipoInteresado.getCodigo();
			if((CODIGO_VALDOM_PERS.equals(codigoTipoInteresado) || CODIGO_VALDOM_DPD.equals(codigoTipoInteresado)) && this.personasInteresado != null)
			{
				interviniente = this.personasInteresado.getNombreAp();
			}else if(CODIGO_VALDOM_SUJOBL.equals(codigoTipoInteresado) && this.sujetosObligadosInteresado != null)
			{
				interviniente = this.sujetosObligadosInteresado.getDescripcion();
			}else if(CODIGO_VALDOM_AUTCON.equals(codigoTipoInteresado) && this.valorDominioInteresado != null) {
				interviniente = this.valorDominioInteresado.getDescripcion();
			}
		}
		return interviniente;
	}
}

