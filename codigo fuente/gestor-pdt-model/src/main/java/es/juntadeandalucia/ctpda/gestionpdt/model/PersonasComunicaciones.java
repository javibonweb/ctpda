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
@AuditTable(value = "GE_PERSONASCOMUNICACIONES_H")
@Table(name = "GE_PERSONASCOMUNICACIONES")
public class PersonasComunicaciones extends Auditable{

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PER_VIACOM_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_PERSONASCOMUNICACIONES")
	@SequenceGenerator(name = "S_PERSONASCOMUNICACIONES", sequenceName = "S_PERSONASCOMUNICACIONES", allocationSize = 1)
	@Getter
	@Setter
	private Long id;

	@Column(name = "L_ACTIVA")
	@ColumnDefault("1")
	@NotNull
	@Getter
	@Setter
	private Boolean activa;
	
	@ManyToOne( fetch = FetchType.LAZY)
	@NotNull
	@JoinColumn(name = "PERVIA_PER_ID", foreignKey = @ForeignKey(name = "GE_PERSONACOMUNICAC_PERSONA_FK"))
	private Personas persona;
	
	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;	


}
