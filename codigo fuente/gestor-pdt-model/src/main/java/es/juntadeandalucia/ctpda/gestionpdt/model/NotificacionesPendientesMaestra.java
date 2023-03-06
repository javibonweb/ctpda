package es.juntadeandalucia.ctpda.gestionpdt.model;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


import es.juntadeandalucia.ctpda.gestionpdt.model.core.Auditable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = "GE_NOTIF_PENDIENTES")
public class NotificacionesPendientesMaestra extends Auditable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String CODIGO_VALDOM_PERS = "PERS";
	public static final String CODIGO_VALDOM_DPD = "DPD";
	public static final String CODIGO_VALDOM_AUTCON = "AUTCON";
	public static final String CODIGO_VALDOM_SUJOBL = "SUJOBL";
	public static final String ABIERTO = "Abierto";
	public static final String CERRADA = "Cerrada";

	
	
	@Id
	@Column(name = "TRAM_ID")
	@NotNull
	@Getter
	@Setter
	private Long id;
	
	
	@Column(name = "F_FECHA_INI")
	@Getter
	@Setter
	private Date fechaIni;
	
	@Column(name = "D_DESC_TRAMITE")
	@Getter
	@Setter
	private String descTramExp;
	
	@OneToOne (fetch=FetchType.EAGER)
	@JoinColumn(name= "ID_TIPO_TRAMITE", foreignKey =  @ForeignKey(name="GE_TIPO_TRAMITE_FK"))
	private TipoTramite tipoTramite;
	
	@Column(name = "D_DESC_TIPO_TRAMITE")
	@Getter
	@Setter
	private String descTipoTram;
	
	@Column(name = "T_RESP_TRAMITE")
	@Getter
	@Setter
	private String descResponsableTram;
	
	
	@Column(name = "T_RESP_ABREV_TRAMITE")
	@Getter
	@Setter
	private String descRespAbrev;
	
	@Column(name = "D_DESC_TRAM_SUP")
	@Getter
	@Setter
	private String descTramSup;
	
	@OneToOne (fetch=FetchType.EAGER)
	@JoinColumn(name= "EXP_ID", foreignKey =  @ForeignKey(name="GE_EXP_FK"))
	private Expedientes expediente;
	
	

	@Column(name = "F_FECHA_REGISTRO")
	@Getter
	@Setter
	private Date fechaRegistro;
	
	@Getter
	@Setter
	@NotNull
	@Column(name = "T_TIP_EXP")
	private String tipoExpediente;
	
	
	@Getter
	@Setter
	@NotNull
	@Column(name = "T_NUM_EXPEDIENTE")
	private String numExpediente;
	
	@Getter
	@Setter
	@NotNull
	@Column(name = "T_SITUACION")
	private String situacion;
	
	@Setter
	@Getter
	@NotNull
	@Column(name = "T_FECHA_ENTRADA")
	private Date fechaEntrada;
	
	@Setter
	@Getter
	@Column(name = "F_FECHA_ENVIO")
	private Date fechaEnvio;
	
	@Setter
	@Getter
	@Column(name = "F_FECHA_NOTIFICACION")
	private Date fechaNotificacion;
	
	@Setter
	@Getter
	@Column(name = "F_FECHA_FIRMA")
	private Date fechaFirma;
	
	@Setter
	@Getter
	@Column(name = "F_FECHA_LIMITE")
	private Date fechaLimite;
	
	@Getter
	@Setter
	@Column(name = "D_TIPO_INTERVINIENTE")
	private String descTipoInterviniente;
	
	@Getter
	@Setter
	@Column(name = "D_CANAL_SALIDA")
	private String descCanalSalida;
	
	@Getter
	@Setter
	@Column(name = "T_AUT_COMP_INTERV")
	private String descAutCompInterv;
	
	@Getter
	@Setter
	@Column(name = "T_PERSONA_INTERV")
	private String descPersonaInterv;
	
	@Getter
	@Setter
	@Column(name = "T_SUJETO_OBLIGADO_INTERV")
	private String descSujObligInterv;
	
	@Getter
	@Setter
	@Column(name = "D_RES_NOTIF")
	private String descResNotif;
	
	@Getter
	@Setter
	@Column(name = "T_INTERVINIENTE")
	private String interviniente;
	
	@Getter
	@Setter
	@Column(name = "T_FIRMANTE")
	private String firmante;
	
	@Getter
	@Setter
	@Column(name = "D_TIPO_FIRMA")
	private String descTipoFirma;

	@Getter
	@Setter
	@NotNull
	@Column(name = "T_PERSONA")
	private String persona;

	@Getter
	@Setter
	@NotNull
	@Column(name = "T_SUJETO_OBLIGADO")
	private String sujetoObligado;
	
	@Column(name = "T_NOMBRE_EXPEDIENTE")
	@NotNull
	@Getter
	@Setter
	private String nombreExpediente;	
	
	@Getter
	@Setter
	@Column(name = "ID_MATERIA")
	private String idMateria;
	
	@Getter
	@Setter
	@Column(name = "ID_MATERIASUP")
	private String idMateriaSup;
	
	@Getter
	@Setter
	@Column(name = "FINALIZADOS")
	private Boolean finalizados;
	
	@Getter
	@Setter
	@Column(name = "T_RESPONSABLE")
	private String responsable;
	
	@Getter
	@Setter
	@Column(name = "T_SITUACION_ADICIONAL")
	private String situacionAdicional;
	
	
	@Column(name = "L_ACUSE_RECIBO")
	@Getter
	@Setter
	private Boolean acuseRecibo;

}

