package es.juntadeandalucia.ctpda.gestionpdt.model;

import java.util.Objects;

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
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import es.juntadeandalucia.ctpda.gestionpdt.model.core.Auditable;
import es.juntadeandalucia.ctpda.gestionpdt.model.core.AuditorAwareImpl;
import es.juntadeandalucia.ctpda.gestionpdt.model.core.Ordenable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@EntityListeners(AuditorAwareImpl.class)
@Audited
@AuditTable(value = "GE_AGRUP_EXPEDIENTES_H")
@Table(name = "GE_AGRUP_EXPEDIENTES")
public class AgrupacionesExpedientes extends Auditable implements Ordenable {

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@NotNull
	@Column(name = "AGREXP_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_AGRUP_EXPEDIENTES")
	@SequenceGenerator(name = "S_AGRUP_EXPEDIENTES", sequenceName = "S_AGRUP_EXPEDIENTES", allocationSize = 1)
	@Getter
	@Setter
	private Long id;
	
	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;	

	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "AGREXP_EXP_ID", foreignKey =  @ForeignKey(name="AGREXP_EXP_FK"))
	private Expedientes expediente;	
	
	@Column(name = "D_DESCRIPCION_DOC")
	@Size(max = 255)
	@Getter
	@Setter
	private String descripcion;

	@Column(name = "D_DESC_ABREV_DOC")
	@Size(max = 100)
	@Getter
	@Setter
	private String descripcionAbrev;
	
	@Column(name = "L_ACTIVO")
	@ColumnDefault("1")
	@NotNull
	@Getter
	@Setter
	private Boolean activo;
	
	
	@Column(name = "N_ORDEN")
	@NotNull
	@Getter @Setter
	private Long orden;

	@NotNull
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "AGREXP_VALDOM_CATEG_ID", foreignKey =  @ForeignKey(name="GE_AGREXP_VALDOM_CATEG_ID_FK"))
	private ValoresDominio categoria;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "AGREXP_TRAM_ID", foreignKey =  @ForeignKey(name="GE_AGREXP_TRAM_ID_FK"))
	private TramiteExpediente tramiteExpediente;
	
	@Column(name = "L_VER_PEST_DOC")
	@ColumnDefault("1")
	@NotNull
	@Getter
	@Setter
	private Boolean verPestanaDoc;
	
	//-------------------------------------------
	
	@Transient
	private ValoresDominio categoriaActual;
	
	@PostLoad
	private void init() {
		this.categoriaActual = this.categoria;
	}
	
	@PostPersist @PostUpdate
	private void saved() {
		this.categoriaActual = this.categoria;
	}
	
	public boolean cambiaCategoria() {
		return !Objects.equals(categoria, categoriaActual);
	}
	
}
