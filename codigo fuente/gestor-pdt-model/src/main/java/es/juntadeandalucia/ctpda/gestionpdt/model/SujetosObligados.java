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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@EntityListeners(AuditorAwareImpl.class)
@Audited
@AuditTable(value = "GE_SUJETOS_OBLIGADOS_H")
@Table(name = "GE_SUJETOS_OBLIGADOS")
public class SujetosObligados extends Auditable {
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "SUJ_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_SUJETOS_OBLIGADOS")
	@SequenceGenerator(name = "S_SUJETOS_OBLIGADOS", sequenceName = "S_SUJETOS_OBLIGADOS", allocationSize = 1)
	@Getter
	@Setter
	private Long id;

	@Column(name = "L_ACTIVA")
	@ColumnDefault("1")
	@NotNull
	@Getter
	@Setter
	private Boolean activa;

	@Column(name = "D_DESCRIPCION")
	@Size(max = 100)
	@NotNull
	@Getter
	@Setter
	private String descripcion;

	@Column(name = "N_NIVEL_ANIDAMIENTO")
	@Max(5)
	@NotNull
	@Getter
	@Setter
	private Long nivelAnidamiento;
	

	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "SUJ_SUJ_ID", foreignKey = @ForeignKey(name = "GE_SUJOBLI_SUJOBLI_FK"))
	private SujetosObligados sujetosObligadosPadre;

	@Column(name = "T_ORDEN_VISUALIZACION")
	@Size(max = 3)
	@NotNull
	@Getter
	@Setter
	private String ordenVisualizacion;

	
	@OneToOne( fetch = FetchType.LAZY)
	@NotNull
	@JoinColumn(name = "SUJ_TIP_ID", foreignKey = @ForeignKey(name = "GE_SUJOBLI_TIPAGRUP_FK"))
	private TipoAgrupacion tipoAgrupacion;

	@Column(name = "T_ABREVIATURA_1")
	@Size(max = 20)
	@Getter
	@Setter
	private String abreviatura1;

	@Column(name = "T_ABREVIATURA_2")
	@Size(max = 12)
	@Getter
	@Setter
	private String abreviatura2;

	@Column(name = "T_ABREVIATURA_3")
	@Size(max = 5)
	@Getter
	@Setter
	private String abreviatura3;

	@Column(name = "T_DIRECCION")
	@Size(max = 255)
	@Getter
	@Setter
	private String direccion;

	@Column(name = "C_CODIGO_POSTAL")
	@Size(max = 5)
	@Getter
	@Setter
	private String codigoPostal;

	@ManyToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "SUJ_PAI_ID", foreignKey = @ForeignKey(name = "GE_SUJOBLI_PAIS_FK"))
	private Paises pais;
		
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "SUJ_PRO_ID", foreignKey = @ForeignKey(name = "GE_SUJOBLI_PROVINCIA_FK"))
	private Provincias provincia;
	
	@ManyToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "SUJ_LOC_ID", foreignKey = @ForeignKey(name = "GE_SUJOBLI_LOCALIDAD_FK"))
	private Localidades localidad;
	
	@Column(name = "D_DESCRIPCION_MUNICIPIO")
	@Size(max = 100)
	@Getter
	@Setter
	private String descripcionMunicipio;
	
	@OneToOne (fetch=FetchType.EAGER)
	@JoinColumn(name= "SUJ_VALDOM_TIPIDE_ID", foreignKey =  @ForeignKey(name="GE_SUJOBLI_VALDOM_TIPIDE_FK"))
	private ValoresDominio valorTipoIdentificador;	
	
	@OneToOne (fetch=FetchType.EAGER)
	@JoinColumn(name= "SUJ_VALDOM_VIACOM_ID", foreignKey =  @ForeignKey(name="GE_SUJOBLI_VALDOM_VIACOM_FK"))
	private ValoresDominio valorViaComunicacion;	
	
	@OneToOne (fetch=FetchType.EAGER)
	@JoinColumn(name= "SUJ_VALDOM_TIPOLO_ID", foreignKey =  @ForeignKey(name="GE_SUJOBLI_VALDOM_TIPOLO_FK"))
	private ValoresDominio valorTipologia;	
	
	@Column(name = "T_NIF")
	@Size(max = 9)
	@Getter
	@Setter
	private String nif;
	
	@Column(name = "T_EMAIL_CONTACTO")
	@Size(max = 100)
	@Getter
	@Setter
	private String emailContacto;
	
	@Column(name = "C_CODIGO_DIR3")
	@Size(max = 50)
	@Getter
	@Setter
	private String codigoDir3;
	
	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;	
	
	@Column(name = "T_PUESTO_TITULAR")
	@Getter
	@Setter
	private String puestoTitular;
	
	@Column(name = "L_UNIDAD_TRANSPARENCIA")
	@ColumnDefault("0")
	@NotNull
	@Getter
	@Setter
	private Boolean unidadTransparencia;
	
	@Column(name = "T_CONTACT_UNIDAD_TRANSPARENCIA")
	@Getter
	@Setter
	private String contactoUnidadTransparencia;
	
	@Column(name = "L_DPD")
	@ColumnDefault("0")
	@NotNull
	@Getter
	@Setter
	private Boolean dpd;
	
	@Column(name = "T_CONTACTO_DPD")
	@Getter
	@Setter
	private String contactoDpd;
	
	@Column(name = "L_ERRONEO")
	@ColumnDefault("0")
	@NotNull
	@Getter
	@Setter
	private Boolean erroneo;
	
    @Transient
    public String getDireccionCompleta() {
    	String descripProv = "";
    	String direccionCompleta = "";
    	
    	if(this.direccion != null && !this.direccion.isEmpty())
    	{
    		direccion = direccion.trim();
    		direccionCompleta = direccion;
    	}
    	
    	if(this.codigoPostal != null && !this.codigoPostal.isEmpty())
    	{
    		this.codigoPostal = this.codigoPostal.trim();
    		direccionCompleta = direccionCompleta + ", " + this.codigoPostal;
    	}

    	if(this.provincia != null)
    	{
    		descripProv = this.provincia.getDescripcion();
    		descripProv = descripProv.trim();
    		direccionCompleta = direccionCompleta + ", " +descripProv;
    	}
    	
    	if(this.descripcionMunicipio != null && !this.descripcionMunicipio.isEmpty())
    	{
    		this.descripcionMunicipio = this.descripcionMunicipio.trim();
    		direccionCompleta = direccionCompleta + ", " + this.descripcionMunicipio;
    	}
	    
    	return direccionCompleta;
    }

	


}