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
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
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
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@EntityListeners(AuditorAwareImpl.class)
@Audited
@AuditTable(value = "GE_DOCUMENTOS_EXPEDIENTES_H")
@Table(name = "GE_DOCUMENTOS_EXPEDIENTES")
@NamedEntityGraph(name = "documentoExp.basico", 
attributeNodes = {
		@NamedAttributeNode("id"),
		@NamedAttributeNode("descripcionDocumento"),
		@NamedAttributeNode(value = "documento", subgraph = "documento.basico")
})   
@NamedEntityGraph(name = "documentoExp.listado", 
attributeNodes = {
		@NamedAttributeNode("id"),
		@NamedAttributeNode(value = "documento", subgraph = "documento.listado"),
})
public class DocumentosExpedientes extends Auditable {
	
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@NotNull
	@Column(name = "DOCEXP_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_DOCUMENTOS_EXPEDIENTES")
	@SequenceGenerator(name = "S_DOCUMENTOS_EXPEDIENTES", sequenceName = "S_DOCUMENTOS_EXPEDIENTES", allocationSize = 1)
	@Getter
	@Setter
	private Long id;
	
	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;	


	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "DOCEXP_DOC_ID", foreignKey =  @ForeignKey(name="DOCEXP_DOC_FK"))
	private Documentos documento;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "DOCEXP_EXP_ID", foreignKey =  @ForeignKey(name="DOCEXP_EXP_FK"))
	private Expedientes expediente;	

	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "DOCEXP_TRAM_EXP_ID", foreignKey =  @ForeignKey(name="DOCEXP_TRAM_EXP_FK"))
	private TramiteExpediente tramiteExpediente;	
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "DOCEXP_AGRDOC_ID", foreignKey =  @ForeignKey(name="DOCEXP_AGRDOC_FK"))
	private AgrupacionesDocumentos agrupacionDocumentos;
	
	@Column(name = "D_DESCRIPCION_DOC")
	@Size(max = 255)
	@Getter
	@Setter
	private String descripcionDocumento;

	@Column(name = "D_DESC_ABREV_DOC")
	@Size(max = 100)
	@Getter
	@Setter
	private String descripcionAbrevDocumento;

	@Column(name = "L_ACTIVO")
	@ColumnDefault("1")
	@NotNull
	@Getter
	@Setter
	private Boolean activo;

	@NotNull
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "DOCEXP_VALDOM_CATEG_ID", foreignKey =  @ForeignKey(name="GE_DOCEXP_VALDOM_CATEG_ID_FK"))
	private ValoresDominio categoria;

	//------------------------------------------
    
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
