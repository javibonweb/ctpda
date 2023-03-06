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
@AuditTable(value = "GE_USUARIOS_PERFILES_H")
@Table(name = "GE_USUARIOS_PERFILES")
public class UsuarioPerfil extends Auditable{
	
	
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "USU_PER_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_USUARIOS_PERFILES")
	@SequenceGenerator(name = "S_USUARIOS_PERFILES", sequenceName = "S_USUARIOS_PERFILES", allocationSize = 1)
	@Getter
	@Setter
	private Long id;

	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "USUPER_PER_ID", foreignKey = @ForeignKey(name = "GE_USUPER_PER_FK"))
	@NotNull
	private Perfil perfil;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "USUPER_USU_ID", foreignKey = @ForeignKey(name = "GE_USUPER_USU_FK"))
	@NotNull
	private Usuario usuario;
	
	@Column(name = "L_ACTIVA")
	@ColumnDefault("1")
	@NotNull
	@Getter
	@Setter
	private Boolean activa;	
	
	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;	
	
}
