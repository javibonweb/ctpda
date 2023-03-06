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
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
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
@AuditTable(value = "GE_PLANTILLA_DOC_H")
@Table(name = "GE_PLANTILLA_DOC")
@NamedEntityGraph(name = "plantillaDoc.listado", 
attributeNodes = {
		@NamedAttributeNode("id"),
		@NamedAttributeNode(value = "plantilla", subgraph = "plantilla.listado")
		})
public class PlantillasDocumentos extends Auditable{

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@NotNull
	@Column(name = "PLDOC_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_PLANTILLA_DOC")
	@SequenceGenerator(name = "S_PLANTILLA_DOC", sequenceName = "S_PLANTILLA_DOC", allocationSize = 1)
	@Getter
	@Setter
	private Long id;
	
	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;	

	
	@NotNull
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "PLDOC_CFGDOC_ID", foreignKey =  @ForeignKey(name="PLDOC_CFGDOC_FK"))
	private CfgDocExpedienteTramitacion cfgDocExpedienteTramitacion;
	
	@NotNull
	@OneToOne (fetch=FetchType.LAZY)
	@NotNull
	@JoinColumn(name= "PLDOC_PLA_ID", foreignKey =  @ForeignKey(name="PLDOC_PLA_FK"))
	private Plantillas plantilla;	

	@Column(name = "L_ACTIVO")
	@ColumnDefault("1")
	@NotNull
	@Getter
	@Setter
	private Boolean activo;

	//-------------------
	
	@Transient
	public String getDescripcion() {
		return this.getPlantilla().getDescripcion();
	}
	
	@Transient
	public String getDescripcionAbrev() {
		return this.getPlantilla().getDescripcionAbrev();
	}
	
}
