package es.juntadeandalucia.ctpda.gestionpdt.model;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import es.juntadeandalucia.ctpda.gestionpdt.model.core.EntidadBasica;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Entity
@Table(name = "GE_CONEXIONES_USUARIOS")
public class ConexionUsuario implements  EntidadBasica,Serializable{
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CON_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_CONEXIONES_USUARIOS")
	@SequenceGenerator(name = "S_CONEXIONES_USUARIOS", sequenceName = "S_CONEXIONES_USUARIOS", allocationSize = 1)
	@Getter
	@Setter
	private Long id;

	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "CON_USU_ID", foreignKey =  @ForeignKey(name="GE_CON_USU_FK"))
	private Usuario usuarioLogado;		
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "CON_PER_ID", foreignKey =  @ForeignKey(name="GE_CON_PER_FK"))
	private Perfil perfilAcceso;		
	
	@Getter
	@Setter
	@Column(name = "F_FECHA_ACCESO")
	private Date fechaAcceso;
	
	@Setter
	@Getter
	@Column(name = "T_DESLOGADO")
	private String modoDeslogado;
	
	
	@Getter
	@Setter
	@Column(name = "F_FECHA_DESLOGADO")
	private Date fechaDeslogado;
	
	@Getter
	@Setter
	@Column(name = "T_IP_CONEXION")
	private String ipConexion;
	
	@Getter
	@Setter
	@Column(name = "T_EQUIPO_CONEXION")
	private String equipoConexion;
	

}
