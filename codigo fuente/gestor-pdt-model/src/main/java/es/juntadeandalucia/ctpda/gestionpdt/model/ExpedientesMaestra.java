package es.juntadeandalucia.ctpda.gestionpdt.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import es.juntadeandalucia.ctpda.gestionpdt.model.core.Auditable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = "GE_EXP_PEREXP_SUJEXP")
public class ExpedientesMaestra extends Auditable {
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "EXP_ID")
	@NotNull
	@Getter
	@Setter
	private Long id;

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
	@NotNull
	@Column(name = "T_FECHA_ENTRADA")
	private Date fechaEntrada;

	@Getter
	@Setter
	@NotNull
	@Column(name = "T_PERSONA")
	private String persona;
	
	@Getter
	@Setter
	@NotNull
	@Column(name = "T_IDENT_PERSONA")
	private String identPersona;

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

	
	public String getFechaEntrada() {
	    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	    return formatter.format(fechaEntrada);		
	}
	
	public String getMigaDePan() {
		String txtMiga = this.numExpediente;
		txtMiga = this.nombreExpediente == null? txtMiga: txtMiga + " - " + this.nombreExpediente;
		txtMiga = this.situacion == null? txtMiga: txtMiga + " - " + this.situacion;
		txtMiga = this.situacionAdicional == null? txtMiga: txtMiga + " - " + this.situacionAdicional;
	
		return txtMiga; 
	}

}
