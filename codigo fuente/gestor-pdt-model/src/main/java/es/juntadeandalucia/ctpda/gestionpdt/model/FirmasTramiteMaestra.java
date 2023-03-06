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
@Table(name = "GE_LISTADO_FIRMAS_TRAM")
public class FirmasTramiteMaestra extends Auditable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String ABIERTO = "Abierto";
	public static final String CERRADA = "Cerrada";

	
	
	@Id
	@Column(name = "TRAM_EXP_ID")
	@NotNull
	@Getter
	@Setter
	private Long id;
	
	@OneToOne (fetch=FetchType.EAGER)
	@JoinColumn(name= "TRAM_EXP_SUP_ID", foreignKey =  @ForeignKey(name="GE_TRAM_EXP_SUP_FK"))
	private TramiteExpediente tramExpSup;
	
	@Column(name = "F_FECHA_INI")
	@Getter
	@Setter
	private Date fechaIni;
	
	@Column(name = "DESC_ABREV")
	@Getter
	@Setter
	private String descAbrev;
	
	
	@OneToOne (fetch=FetchType.EAGER)
	@JoinColumn(name= "VALDOM_TIPO_FIRMA", foreignKey =  @ForeignKey(name="GE_VALDOM_TIPO_FIRMA_FK"))
	private ValoresDominio valorTipoFirma;
	
	@OneToOne (fetch=FetchType.EAGER)
	@JoinColumn(name= "FIRMANTE", foreignKey =  @ForeignKey(name="GE_FIRMANTE_FK"))
	private Usuario firmante;
	
	@Column(name = "F_FECHA_ENVIO")
	@Getter
	@Setter
	private Date fechaEnvio;
	
	@Column(name = "F_FECHA_FIRMA")
	@Getter
	@Setter
	private Date fechaFirma;
	
	@Column(name = "L_FINALIZADO")
	@Getter
	@Setter
	private Boolean finalizado;
	
    public String getSituacionFirm()
    {
    	String situacionFirm = "";
    	
    	if(Boolean.TRUE.equals(this.finalizado))
    	{
    		situacionFirm = CERRADA;
    	}else {
    		situacionFirm = ABIERTO;
    	}
    	
    	return situacionFirm;
    }
    
}

