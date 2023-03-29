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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
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
@AuditTable(value = "GE_MATERIAS_TIPEXPEDIENTES_H")
@Table(name = "GE_MATERIAS_TIPEXPEDIENTES")
public class MateriaTipoExpediente extends Auditable{
	
	
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "MAT_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_MATERIAS_TIPEXPEDIENTES")
	@SequenceGenerator(name = "S_MATERIAS_TIPEXPEDIENTES", sequenceName = "S_MATERIAS_TIPEXPEDIENTES", allocationSize = 1)
	@Getter
	@Setter
	private Long id;

	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "MATTIPEXP_VALDOMMAT_ID", foreignKey = @ForeignKey(name = "GE_MATTIPEXP_VALDOMMAT_FK"))
	@NotNull
	private ValoresDominio valorDominioMateria;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "MATTIPEXP_VALDOMTIPEXP_ID", foreignKey = @ForeignKey(name = "GE_MATTIPEXP_VALDOMTIPEXP_FK"))
	@NotNull
	private ValoresDominio valorDominioTipoExpediente;
	
	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;	
	
}
