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
@AuditTable(value = "GE_DOMINIOS_H")
@Table(name = "GE_DOMINIOS")
public class Dominio extends Auditable {
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "DOM_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_DOMINIOS")
	@SequenceGenerator(name = "S_DOMINIOS", sequenceName = "S_DOMINIOS", allocationSize = 1)
	@Getter
	@Setter
	private Long id;

	@Column(name = "C_CODIGO")
	@Size(max = 15)
	@NotNull
	@Getter
	@Setter
	private String codigo;

	@Column(name = "D_DESCRIPCION")
	@Size(max = 250)
	@NotNull
	@Getter
	@Setter
	private String descripcion;

	@Column(name = "N_NIVEL_ANIDAMIENTO_MAX")
	@Max(3)
	@NotNull
	@Getter
	@Setter
	private Long nivelAnidamientoMaximo;

	@Column(name = "L_BLOQUEADO")
	@ColumnDefault("0")
	@NotNull
	@Getter
	@Setter
	private Boolean bloqueado;

	@Column(name = "L_VISIBLE")
	@ColumnDefault("1")
	@NotNull
	@Getter
	@Setter
	private Boolean visible;

	@Column(name = "L_PUNTO_MENU")
	@ColumnDefault("0")
	@NotNull
	@Getter
	@Setter
	private Boolean puntoMenu;
	
	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;	

}