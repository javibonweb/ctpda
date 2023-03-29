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
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import es.juntadeandalucia.ctpda.gestionpdt.model.core.Auditable;
import es.juntadeandalucia.ctpda.gestionpdt.model.core.AuditorAwareImpl;
import es.juntadeandalucia.ctpda.gestionpdt.util.StringUtils;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Entity
@EntityListeners(AuditorAwareImpl.class)
@Audited
@AuditTable(value = "GE_PERSONAS_H")
@Table(name = "GE_PERSONAS")
@NamedEntityGraph(name="persona.basico", attributeNodes= {
		@NamedAttributeNode("id"),
	    @NamedAttributeNode("valorTipoPersona"),
	    @NamedAttributeNode("nombreRazonsocial"),
	    @NamedAttributeNode("primerApellido"),
		@NamedAttributeNode("segundoApellido"),
		@NamedAttributeNode("nifCif")
})
public class Personas extends Auditable{

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PER_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_PERSONAS")
	@SequenceGenerator(name = "S_PERSONAS", sequenceName = "S_PERSONAS", allocationSize = 1)
	@Getter
	@Setter
	private Long id;
	
	@Column(name = "L_ACTIVA")
	@ColumnDefault("1")
	@NotNull
	@Getter
	@Setter
	private Boolean activa;

	@Column(name = "T_PRIMER_APELLIDO")
	@Size(max = 100)
	@Getter
	@Setter
	private String primerApellido;
	
	@Column(name = "T_SEGUNDO_APELLIDO")
	@Size(max = 100)
	@Getter
	@Setter
	private String segundoApellido;
	
	@Column(name = "C_CODIGO_POSTAL")
	@Size(max = 5)
	@Getter
	@Setter
	private String codigoPostal;
	
	@Column(name = "T_DIRECCION")
	@Size(max = 120)
	@Getter
	@Setter
	private String direccion;
	
	@Column(name = "T_EMAIL")
	@Size(max = 50)
	@Getter
	@Setter
	private String email;
	
	@Column(name = "T_INICIALES")
	@Size(max = 5)
	@Getter
	@Setter
	private String iniciales;
	
	@Column(name = "T_NIF_CIF")
	@Size(max = 9)
	@Getter
	@Setter
	private String nifCif;
	
	@Column(name = "T_NOMBRE_RAZONSOCIAL")
	@Size(max = 100)
	@Getter
	@Setter
	private String nombreRazonsocial;
	
	@Column(name = "N_TELEFONO_FIJO")
	@Getter
	@Setter
	private Long telefonoFijo;
	
	@Column(name = "N_TELEFONO_MOVIL")
	@Getter
	@Setter
	private Long telefonoMovil;

	@ManyToOne( fetch = FetchType.EAGER)
	@JoinColumn(name = "PER_PAI_ID", foreignKey = @ForeignKey(name = "GE_PERSONA_PAIS_FK"))
	private Paises pais;
	
	@ManyToOne( fetch = FetchType.EAGER)
	@JoinColumn(name = "PER_PRO_ID", foreignKey = @ForeignKey(name = "GE_PERSONA_PROVINCIA_FK"))
	private Provincias provincia;
	
	@ManyToOne( fetch = FetchType.EAGER)
	@JoinColumn(name = "PER_LOC_ID", foreignKey = @ForeignKey(name = "GE_PERSONA_LOCALIDAD_FK"))
	private Localidades localidad;
	
	@Column(name = "D_DESCRIPCION_MUNICIPIO")
	@Size(max = 100)
	@Getter
	@Setter
	private String descripcionMunicipio;
	
	@OneToOne (fetch=FetchType.EAGER)
	@JoinColumn(name= "PER_VALDOM_TIPPER_ID", foreignKey =  @ForeignKey(name="GE_PERSONA_VALDOM_TIPPER_FK"))
	private ValoresDominio valorTipoPersona;
	
	@OneToOne (fetch=FetchType.EAGER)
	@JoinColumn(name= "PER_VALDOM_SEX_ID", foreignKey =  @ForeignKey(name="GE_PERSONA_VALDOM_SEX_FK"))
	private ValoresDominio valorSexo;
	
	@OneToOne (fetch=FetchType.EAGER)
	@JoinColumn(name= "PER_VALDOM_VIACOM_ID", foreignKey =  @ForeignKey(name="GE_PERSONA_VALDOM_VIACOM_FK"))
	private ValoresDominio valorViaComunicacion;	
	
	@OneToOne (fetch=FetchType.EAGER)
	@JoinColumn(name= "PER_VALDOM_TIPIDE_ID", foreignKey =  @ForeignKey(name="GE_PERSONA_VALDOM_TIPIDE_FK"))
	private ValoresDominio valorTipoIdentificador;	
	
	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;	

		
	//*********************************************************
	
    @Transient
    public boolean getEsPF() {
    	return this.valorTipoPersona != null && (this.valorTipoPersona.getCodigo().equals("FIS"));
    }

    @Transient
    public String getNombreAp() {
    	String nombre = this.nombreRazonsocial;
    	
    	if(this.getEsPF()) {
	    	nombre = StringUtils.getNombreAp(nombre, this.primerApellido, this.segundoApellido);
	    }

    	return nombre;
    }
    
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
