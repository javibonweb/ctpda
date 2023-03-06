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
@AuditTable(value = "GE_METADATOS_DOCUMENTOS_H")
@Table(name = "GE_METADATOS_DOCUMENTOS")
public class MetadatosDocumentos extends Auditable{

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@NotNull
	@Column(name = "MET_DOC_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_METADATOS_DOCUMENTOS")
	@SequenceGenerator(name = "S_METADATOS_DOCUMENTOS", sequenceName = "S_METADATOS_DOCUMENTOS", allocationSize = 1)
	@Getter
	@Setter
	private Long id;
	
	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;	
			                           
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "METDOC_DOC_ID", foreignKey =  @ForeignKey(name="METDOC_DOC_FK"))
	private Documentos documento;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "METDOC_TMETENI_ID", foreignKey =  @ForeignKey(name="METDOC_TMETENI_FK"))
	private TipoMetadatoENI tipoMetadatoENI;
	
	@Column(name = "N_ORDEN")
	//@NotNull
	@Getter
	@Setter
	private Long orden;
	
	@Column(name = "T_VALOR")
	@Size(max = 255)
	@Getter
	@Setter
	private String valor;
	
	@Column(name = "L_ACTIVO")
	@ColumnDefault("1")
	@NotNull
	@Getter
	@Setter
	private Boolean activo;
	
}
