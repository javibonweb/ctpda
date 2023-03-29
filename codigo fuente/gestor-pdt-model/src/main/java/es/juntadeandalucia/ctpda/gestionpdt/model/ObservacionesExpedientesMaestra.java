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
import javax.validation.constraints.Size;


import es.juntadeandalucia.ctpda.gestionpdt.model.core.Auditable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = "GE_LISTADO_OBSERVACIONES_EXP")
public class ObservacionesExpedientesMaestra extends Auditable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "OBS_ID")
	@NotNull
	@Getter
	@Setter
	private Long id;
	
	@Column(name = "F_ENTRADA")
	@Getter
	@Setter
	private Date fechaEntrada;

	
	@OneToOne (fetch=FetchType.EAGER)
	@JoinColumn(name= "VALDOM_TIPOBS", foreignKey =  @ForeignKey(name="GE_VALDOM_TIPOBS_FK"))
	private ValoresDominio valorTipoObservacion;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "TRAMITE", foreignKey =  @ForeignKey(name="GE_TRAM_FK"))
	private TramiteExpediente tramiteExpdte;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "TAREA", foreignKey =  @ForeignKey(name="GE_TAR_FK"))
	private TareasExpediente tareaExpdte;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "PLAZO", foreignKey =  @ForeignKey(name="GE_PLA_FK"))
	private PlazosExpdte plazoExpdte;
	
	@OneToOne(fetch = FetchType.LAZY)
	@NotNull
	@JoinColumn(name = "EXPEDIENTE", foreignKey = @ForeignKey(name = "GE_EXP_FK"))
	private Expedientes expediente;
	
	@Column(name = "TEXTO")
	@Size(max = 4000)
	private String texto;
	
	
	public String getOrigen() {
	   String origen = "";
	   
	   if(this.tramiteExpdte != null)
	   {
		   origen = this.tramiteExpdte.getDescripcion();
	   }else if(this.plazoExpdte != null)
	   {
		   origen = this.plazoExpdte.getValorTipoPlazo().getDescripcion();
	   }else if(this.tareaExpdte != null)
	   {
		   origen = this.tareaExpdte.getDescripcion();
	   }
	   
	   return origen;
	}
}

