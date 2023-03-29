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
@Table(name = "GE_LISTADO_NOTIFICACIONES_TRAM")
public class NotificacionesTramiteMaestra extends Auditable{

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
	@JoinColumn(name= "VALDOM_RESNOTIF", foreignKey =  @ForeignKey(name="GE_VALDOM_RESNOTIF_FK"))
	private ValoresDominio valorResNotif;
	

	@OneToOne (fetch=FetchType.EAGER)
	@JoinColumn(name= "VALDOM_TIPINT", foreignKey =  @ForeignKey(name="GE_VALDOM_TIPINT_FK"))
	private ValoresDominio valorTipInt;
	
	@Column(name = "D_DESCRIPCION_TRAMEXP")
	@Getter
	@Setter
	private String descTramExp;
	
	@OneToOne (fetch=FetchType.EAGER)
	@JoinColumn(name= "DETALLE_EXP_TRAM", foreignKey =  @ForeignKey(name="GE_DETALLE_EXP_TRAM_FK"))
	private DetalleExpdteTram detExpTram;

	@Column(name = "L_ACUSE_RECIBO")
	@Getter
	@Setter
	private Boolean acuseRecibo;
	
	@Column(name = "L_FINALIZADO")
	@Getter
	@Setter
	private Boolean finalizado;
	
    public String getSituacionNotCom()
    {
    	String situacionNotCom = "";
    	
    	if(Boolean.TRUE.equals(this.finalizado))
    	{
    		situacionNotCom = CERRADA;
    	}else {
    		situacionNotCom = ABIERTO;
    	}
    	
    	return situacionNotCom;
    }
    
	public String getInterviniente()
	{
		String interviniente = "";
		String codigoTipoInteresado = "";
		if(this.valorTipInt!= null)
		{
			codigoTipoInteresado = this.valorTipInt.getCodigo();
			if((CODIGO_VALDOM_PERS.equals(codigoTipoInteresado) || CODIGO_VALDOM_DPD.equals(codigoTipoInteresado)) && this.detExpTram.getPersonasInteresado() != null)
			{
				interviniente = this.detExpTram.getPersonasInteresado().getNombreAp();
			}else if(CODIGO_VALDOM_SUJOBL.equals(codigoTipoInteresado) && this.detExpTram.getSujetosObligadosInteresado() != null)
			{
				interviniente = this.detExpTram.getSujetosObligadosInteresado().getDescripcion();
			}else if(CODIGO_VALDOM_AUTCON.equals(codigoTipoInteresado) && this.detExpTram.getValorDominioInteresado() != null) {
				interviniente = this.detExpTram.getValorDominioInteresado().getDescripcion();
			}
		}
		return interviniente;
	}


}

