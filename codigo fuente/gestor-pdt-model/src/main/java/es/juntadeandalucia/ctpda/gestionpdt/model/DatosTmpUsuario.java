package es.juntadeandalucia.ctpda.gestionpdt.model;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import es.juntadeandalucia.ctpda.gestionpdt.model.core.EntidadBasica;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Entity
@Table(name = "GE_DATOS_TMP_USUARIO")
public class DatosTmpUsuario implements EntidadBasica, Serializable {
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "TMP_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_DATOS_TMP_USUARIO")
	@SequenceGenerator(name = "S_DATOS_TMP_USUARIO", sequenceName = "S_DATOS_TMP_USUARIO", allocationSize = 1)
	@Getter
	@Setter
	private Long id;

	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;	
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "TMP_USU_ID") //, foreignKey = @ForeignKey(name = "GE_TMP_USU_FK"))
	@NotNull
	private Usuario usuario;

	@Column(name = "B_BYTES")
	@Lob
	@Getter
	@Setter
	private byte[] bytes;

	@Column(name = "T_NOMBRE")
	@Size(max = 150)
	@NotNull
	@Getter
	@Setter
	private String nombre;
	
	@Column(name = "T_TIPO_MIME")
	@Size(max = 50)
	@NotNull
	@Getter
	@Setter
	private String tipoContenido;

	public void limpiar() {
		this.bytes = null;
		this.nombre = "";
		this.tipoContenido = "";
	}
}