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
@AuditTable(value = "GE_TIPOS_AGRUPACIONES_H")
@Table(name = "GE_TIPOS_AGRUPACIONES")
public class TipoAgrupacion extends Auditable{

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@NotNull
	@Column(name = "TIP_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_TIPOAGRUPACION")
	@SequenceGenerator(name = "S_TIPOAGRUPACION", sequenceName = "S_TIPOAGRUPACION", allocationSize = 1)
	@Getter
	@Setter
	private Long id;

	
	@Column(name = "D_DESCRIPCION")
	@NotNull
	@Size(max = 100)
	@Getter
	@Setter
	private String descripcion;
	
	@Column(name = "N_NIVEL_ANIDAMIENTO")
	@NotNull
	@Getter
	@Setter
	private Long nivelAnidamiento;


	@Column(name = "L_ACTIVA")
	@ColumnDefault("1")
	@NotNull
	@Getter
	@Setter
	private Boolean activa;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "TIP_TIP_ID", foreignKey = @ForeignKey(name = "GE_TIPAGRUP_TIPAGRUP_FK"))
	private TipoAgrupacion tipoAgrupacionPadre;
	
	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;	
}
