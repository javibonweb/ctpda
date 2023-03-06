package es.juntadeandalucia.ctpda.gestionpdt.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
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
@AuditTable(value = "GE_TIPOS_METADATOS_ENI_H")
@Table(name = "GE_TIPOS_METADATOS_ENI")
public class TipoMetadatoENI extends Auditable{

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@NotNull
	@Column(name = "TMETENI_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_TIPOS_METADATOS_ENI")
	@SequenceGenerator(name = "S_TIPOS_METADATOS_ENI", sequenceName = "S_TIPOS_METADATOS_ENI", allocationSize = 1)
	@Getter
	@Setter
	private Long id;
	
	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;	

	
	@Column(name = "T_CODIGO")
	@NotNull
	@Size(max = 10)
	@Getter
	@Setter
	private String codigo;

	@Column(name = "D_DESCRIPCION")
	@Size(max = 255)
	@Getter
	@Setter
	private String descripcion;

	@Column(name = "D_DESC_ABREV")
	@Size(max = 100)
	@Getter
	@Setter
	private String descripcionAbrev;

	@Column(name = "T_OBJETO")
	@Size(max = 3)
	@NotNull
	@Getter
	@Setter
	private String objeto;
	
	@Column(name = "T_TIPO_DATO")
	@Size(max = 25)
	@NotNull
	@Getter
	@Setter
	private String tipoDato;
	
	@Column(name = "N_ORDEN")
	//@NotNull
	@Getter
	@Setter
	private Long orden;

	@Column(name = "L_OBLIGATORIO")
	@ColumnDefault("0")
	@NotNull
	@Getter
	@Setter
	private Boolean obligatorio;
	
	@Column(name = "L_ACTIVO")
	@ColumnDefault("1")
	@NotNull
	@Getter
	@Setter
	private Boolean activo;
	
	@Column(name = "L_CAPTURA_USUARIO")
	@ColumnDefault("0")
	@NotNull
	@Getter
	@Setter
	private Boolean capturaUsuario;

}
