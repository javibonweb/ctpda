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
@AuditTable(value = "GE_PERFILES_H")
@Table(name = "GE_PERFILES")
public class Perfil extends Auditable {
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PER_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_PERFILES")
	@SequenceGenerator(name = "S_PERFILES", sequenceName = "S_PERFILES", allocationSize = 1)
	@Getter
	@Setter
	private Long id;
	
	@Column(name = "C_CODIGO")
	@Size(max = 10)
	@NotNull
	@Getter
	@Setter
	private String codigo;
	
	
	@Column(name = "D_DESCRIPCION")
	@Size(max = 50)
	@NotNull
	@Getter
	@Setter
	private String descripcion;
	
	@Column(name = "L_ACTIVA")
	@ColumnDefault("1")
	@NotNull
	@Getter
	@Setter
	private Boolean activa;
		
	

	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;	
	
	@Column(name = "D_DESCRIPCION_CORTA")
	@Size(max = 25)
	@Getter
	@Setter
	private String descripcionCorta;

}