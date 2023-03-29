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

@Data
@Entity
@EntityListeners(AuditorAwareImpl.class)
@Audited
@AuditTable(value = "GE_ARTICULOS_AFECTAD_RESOL_H")
@Table(name = "GE_ARTICULOS_AFECTAD_RESOL")
public class ArticuloAfectadoResolucion extends Auditable {
	
	/**
	 * Serial id
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ART_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_ARTICULOS_AFECTAD_RESOL")
	@SequenceGenerator(name = "S_ARTICULOS_AFECTAD_RESOL", sequenceName = "S_ARTICULOS_AFECTAD_RESOL", allocationSize = 1)
	private Long id;
	
	@OneToOne(fetch = FetchType.LAZY)
	@NotNull
	@JoinColumn(name = "ART_RES_ID", foreignKey = @ForeignKey(name = "GE_ART_RES_FK"))
	private Resolucion resolucion;
	
	@OneToOne(fetch = FetchType.LAZY)
	@NotNull
	@JoinColumn(name = "ART_VALDOM_ART_ID", foreignKey = @ForeignKey(name = "GE_ART_VALDOM_ART_FK"))
	private ValoresDominio valorArticulo;
	
	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;

}
