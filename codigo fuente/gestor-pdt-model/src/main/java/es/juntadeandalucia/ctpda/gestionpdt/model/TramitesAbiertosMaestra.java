package es.juntadeandalucia.ctpda.gestionpdt.model;


import java.text.SimpleDateFormat;
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
import javax.validation.constraints.Size;


import es.juntadeandalucia.ctpda.gestionpdt.model.core.Auditable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = "GE_TRAMITES_ABIERTOS")
public class TramitesAbiertosMaestra extends Auditable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "TRAM_ID")
	@NotNull
	@Getter
	@Setter
	private Long id;
	
	@Column(name = "F_FECHA_INI")
	@Setter
	private Date fechaIni;
	
	@Column(name = "D_DESC_TRAMITE")
	@Getter
	@Setter
	private String descTramite;

	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "ID_TIPO_TRAMITE", foreignKey =  @ForeignKey(name="GE_TIPO_TRAMITE_FK"))
	private TipoTramite tipoTramite;
	
	@Column(name = "D_DESC_TIPO_TRAMITE")
	@Getter
	@Setter
	private String descTipoTram;
	
	@Column(name = "T_RESP_TRAMITE")
	@Getter
	@Setter
	private String descRespTram;
	
	@Column(name = "T_RESP_ABREV_TRAMITE")
	@Getter
	@Setter
	private String descAbrevRespTram;
	
	@Column(name = "D_DESC_TRAM_SUP")
	@Getter
	@Setter
	private String descTramSup;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "EXP_ID", foreignKey =  @ForeignKey(name="GE_EXP_FK"))
	private Expedientes expediente;

	

	@Column(name = "F_FECHA_REGISTRO")
	@Getter
	@Setter
	private Date fechaRegistroExp;
	
	
	@Column(name = "T_TIP_EXP")
	@Getter
	@Setter
	private String tipoExp;
	
	@Column(name = "T_NUM_EXPEDIENTE")
	@Getter
	@Setter
	private String numExp;
	
	@Column(name = "T_SITUACION")
	@Getter
	@Setter
	private String situacionExp;
	
	@Column(name = "T_FECHA_ENTRADA")
	@Getter
	@Setter
	private Date fechaEntradaExp;
	
	@Column(name = "T_PERSONA")
	@Getter
	@Setter
	private String persona;
	
	@Column(name = "T_IDENT_PERSONA")
	@Getter
	@Setter
	private String identPersona;
	
	@Column(name = "T_SUJETO_OBLIGADO")
	@Getter
	@Setter
	private String sujObligExp;
	
	@Column(name = "T_NOMBRE_EXPEDIENTE")
	@Getter
	@Setter
	private String nombreExp;
	
	@Column(name = "ID_MATERIA")
	@Getter
	@Setter
	private String materiaExp;
	
	@Column(name = "ID_MATERIASUP")
	@Getter
	@Setter
	private String materiaSupExp;
	
	
	@Column(name = "FINALIZADOS")
	@Getter
	@Setter
	private Long finalizados;
	
	@Column(name = "T_RESPONSABLE")
	@Getter
	@Setter
	private String responsableExp;
	
	@Column(name = "T_SITUACION_ADICIONAL")
	@Getter
	@Setter
	private String situacionAdicionalExp;
	
	@Column(name = "D_TIPO_INTERVINIENTE")
	@Size(max = 250)
	@Getter
	@Setter
	private String tipoInterviniente;
	
	@Column(name = "D_CANAL_SALIDA")
	@Size(max = 250)
	@Getter
	@Setter
	private String canalSalida;
	
	@Column(name = "T_AUT_COMP_INTERV")
	@Size(max = 250)
	@Getter
	@Setter
	private String autCompInterv;
	
	
	@Column(name = "T_PERSONA_INTERV")
	@Size(max = 300)
	@Getter
	@Setter
	private String personaInterv;
	
	@Column(name = "T_SUJETO_OBLIGADO_INTERV")
	@Size(max = 100)
	@Getter
	@Setter
	private String sujetoObligadoInterv;
	
	@Column(name = "D_RES_NOTIF")
	@Size(max = 250)
	@Getter
	@Setter
	private String resNotif;
	
	@Column(name = "T_INTERVINIENTE")
	@Size(max = 300)
	@Getter
	@Setter
	private String interviniente;
	
	@Column(name = "L_ACUSE_RECIBO")
	@Getter
	@Setter
	private Boolean acuseRecibo;
	
	@Column(name = "T_FIRMANTE")
	@Size(max = 300)
	@Getter
	@Setter
	private String firmante;
	
	@Column(name = "D_TIPO_FIRMA")
	@Size(max = 250)
	@Getter
	@Setter
	private String tipoFirma;
	
	@Column(name = "F_FECHA_ENVIO")
	@Setter
	@Getter
	private Date fechaEnvio;
	
	@Column(name = "F_FECHA_NOTIFICACION")
	@Setter
	@Getter
	private Date fechaNotificacion;
	
	@Column(name = "F_FECHA_FIRMA")
	@Setter
	@Getter
	private Date fechaFirma;
	
	@Column(name = "F_FECHA_LIMITE")
	@Setter
	@Getter
	private Date fechaLimite;
	
	public String getFechaIni() {
	    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	    return formatter.format(fechaIni);		
	}
	
	
}

