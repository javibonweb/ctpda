package es.juntadeandalucia.ctpda.gestionpdt.model;
import java.text.SimpleDateFormat;
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
@AuditTable(value = "GE_USUARIOS_H")
@Table(name = "GE_USUARIOS")
public class Usuario extends Auditable {
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "USU_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_USUARIOS")
	@SequenceGenerator(name = "S_USUARIOS", sequenceName = "S_USUARIOS", allocationSize = 1)
	@Getter
	@Setter
	private Long id;

	@Column(name = "T_NOMBRE")
	@Size(max = 100)
	@NotNull
	@Getter
	@Setter
	private String nombre;
	
	@Column(name = "T_PRIMER_APELLIDO")
	@Size(max = 100)
	@NotNull
	@Getter
	@Setter
	private String primerApellido;
	
	@Column(name = "T_SEGUNDO_APELLIDO")
	@Size(max = 100)
	@Getter
	@Setter
	private String segundoApellido;
	
	@Column(name = "T_IDENTIFICADOR")
	@Size(max = 9)
	@NotNull
	@Getter
	@Setter
	private String identificador;


	@Column(name = "L_ACTIVA")
	@ColumnDefault("1")
	@NotNull
	@Getter
	@Setter
	private Boolean activa;
	
	@Column(name = "L_FIRMANTE")
	@ColumnDefault("1")
	@Getter
	@Setter
	private Boolean firmante;
	
	@Column(name = "T_EMAIL")
	@Size(max = 100)
	@Getter
	@Setter
	private String email;
	
	@Column(name = "N_TELEFONO")
	@Getter
	@Setter
	private Long telefono;
	
	@Column(name = "T_LOGIN")
	@Size(max = 50)
	@NotNull
	@Getter
	@Setter
	private String login;
	
	@Column(name = "T_CONTRASENYA")
	@Size(max = 255)
	@Getter
	@Setter
	private String contrasenya;	
	
	@Setter
	@Column(name = "F_FECHA_ULTIMO_ACCESO")
	private Date fechaUltimoAcceso;
	
	@OneToOne (fetch=FetchType.EAGER)
	@JoinColumn(name= "USU_VALDOM_TIPIDE_ID", foreignKey =  @ForeignKey(name="GE_USUARIO_VALDOM_TIPIDE_FK"))
	private ValoresDominio valorTipoIdentificador;		

	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;	

	public String getFechaUltimoAcceso() {
		String fecha = "";
		if(fechaUltimoAcceso != null)
		{
			SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy  HH:mm");
			fecha = formateador.format(fechaUltimoAcceso);
		}
		
		return fecha;
	}
	
	//*********************************************************

    @Transient
    public String getNombreAp() {
    	return StringUtils.getNombreAp(this.nombre, this.primerApellido, this.segundoApellido);
    }

}
