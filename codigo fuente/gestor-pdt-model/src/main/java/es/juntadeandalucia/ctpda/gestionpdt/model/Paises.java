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
@AuditTable(value = "GE_PAISES_H")
@Table(name = "GE_PAISES")
public class Paises extends Auditable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "PAI_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_PAISES")
	@SequenceGenerator(name = "S_PAISES", sequenceName = "S_PAISES", allocationSize = 1)
	@Getter
	@Setter
	private Long id;
	
	@Column(name = "L_ACTIVA")
	@NotNull
	@ColumnDefault("1")
	@Getter
	@Setter
	private Boolean activa;
	
	@Column(name = "C_CODIGO")
	@Size(max = 2)
	@NotNull
	@Getter
	@Setter
	private String codigo;

	@Column(name = "D_DESCRIPCION")
	@NotNull
	@Size(max = 100)
	@Getter
	@Setter
	private String descripcion;
	
	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;	

}
