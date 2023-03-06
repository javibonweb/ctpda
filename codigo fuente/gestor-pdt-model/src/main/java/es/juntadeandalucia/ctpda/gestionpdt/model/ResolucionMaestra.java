package es.juntadeandalucia.ctpda.gestionpdt.model;

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
@Table(name = "GE_LISTADO_RESOLUCIONES")
public class ResolucionMaestra  extends Auditable{

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "RES_ID")
	@NotNull
	@Getter
	@Setter
	private Long id;

	@Column(name = "T_CODIGO_RESOL")
	@Getter
	@Setter
	private String codigoResolucion;
	
	@Column(name = "F_FECHA_RESOLUCION")
	@Getter
	@Setter
	private Date fechaResolucion;
	
	
	@Column(name = "F_FECHA_PUBLI_WEB")
	@Getter
	@Setter
	private Date fechaPublicacionWeb;
	
	@Column(name = "L_ANONIMIZADA")
	@Getter
	@Setter
	private Boolean anonimizada;
	
	@Column(name = "L_NOTIFICADA_TOTAL")
	@Getter
	@Setter
	private Boolean notifTotal;
	
	
	@Column(name = "TIP_RESOL_ID")
	@Getter
	@Setter
	private Long idTipoResolucion;

	@Column(name = "TIPO_RESOL")
	@Getter
	@Setter
	private String tipoResolucion;
	
	@Column(name = "SENTIDO_ID")
	@Getter
	@Setter
	private Long idSentidoResolucion;

	@Column(name = "SENTIDO_RESOL")
	@Getter
	@Setter
	private String sentidoResolucion;
	
	
	@Column(name = "PER_ID")
	@Getter
	@Setter
	private Long idPersona;

	@Column(name = "NOMBRE_PERSONA")
	@Getter
	@Setter
	private String nombrePersona;
	
	@Column(name = "NIF_PERSONA")
	@Getter
	@Setter
	private String nifPersona;

	@Column(name = "COD_TIPO_PERSONA")
	@Getter
	@Setter
	private String codTipoPersona;


	@Column(name = "SUJ_ID")
	@Getter
	@Setter
	private Long idSujetoObligado;

	@Column(name = "NOMBRE_SUJ_OBLIGADO")
	@Getter
	@Setter
	private String nombreSujetoObligado;

	@Column(name = "NIF_SUJ_OBLIGADO")
	@Getter
	@Setter
	private String nifSujetoObligado;
		
}
