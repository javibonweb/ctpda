package es.juntadeandalucia.ctpda.gestionpdt.model;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToOne;
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
import es.juntadeandalucia.ctpda.gestionpdt.util.FileUtils;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@EntityListeners(AuditorAwareImpl.class)
@Audited
@AuditTable(value = "GE_DOCUMENTOS_H")
@Table(name = "GE_DOCUMENTOS")
@NamedEntityGraph(name = "documento.basico", 
attributeNodes = {
		@NamedAttributeNode("id"),
		@NamedAttributeNode("descripcion"),
		@NamedAttributeNode("extensionFichero"),
		@NamedAttributeNode("editable")
})
@NamedEntityGraph(name = "documento.listado", 
attributeNodes = {
		@NamedAttributeNode("id"),
		@NamedAttributeNode("descripcion"),
		@NamedAttributeNode("extensionFichero"),
		@NamedAttributeNode("fechaCreacion"),
		@NamedAttributeNode("fechaModificacion"),
		@NamedAttributeNode(value = "tipoDocumento", subgraph = "tipoDocumento.basico"),
		@NamedAttributeNode("editable"),
		@NamedAttributeNode("firmado"),
		@NamedAttributeNode("sellado"),
		@NamedAttributeNode("sellado"),
		@NamedAttributeNode("anonimizado"),
		@NamedAttributeNode("anonimizadoParcial")
})
public class Documentos extends Auditable{

	public static final String ORIGEN_GENERADO = "G";
	public static final String ORIGEN_INCORPORADO = "I";

	private static final long serialVersionUID = 1L;
	
	@Id
	@NotNull
	@Column(name = "DOC_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_DOCUMENTOS")
	@SequenceGenerator(name = "S_DOCUMENTOS", sequenceName = "S_DOCUMENTOS", allocationSize = 1)
	@Getter
	@Setter
	private Long id;
	
	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;	


	@Column(name = "T_IDEN_DOC")
	@NotNull
	@Size(max = 255)
	@Getter
	@Setter
	private String identificadorDoc;

	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "DOC_TIPDOC_ID", foreignKey =  @ForeignKey(name="DOC_TIPDOC_FK"))
	private TipoDocumento tipoDocumento;	

	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "DOC_PLA_ID", foreignKey =  @ForeignKey(name="DOC_PLA_FK"))
	private Plantillas plantilla;	
	
	@Column(name = "D_DESCRIPCION")
	@Size(max = 255)
	@Getter
	@Setter
	private String descripcion;

	@Column(name = "D_DESC_ABREV")
	@Size(max = 100)
	@Getter
	@Setter
	private String descripcionAbrev;
	
	
	@Column(name = "T_NOMBRE_FICHERO")
	@NotNull
	@Size(max = 100)
	@Getter
	@Setter
	private String nombreFichero;
	
	@Column(name = "T_EXTENSION_FICHERO")
	@NotNull
	@Size(max = 10)
	@Getter
	@Setter
	private String extensionFichero;
	
	
	@Column(name = "B_BYTES")
	@Lob
	@Getter
	@Setter
	private byte[] bytes;
	
	@Column(name = "F_ULT_EDICION", columnDefinition = "TIMESTAMP")
	@Getter
	@Setter
	private LocalDateTime fechaUltimaEdicion;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "DOC_USU_EDITA_ID", foreignKey =  @ForeignKey(name="DOC_USU_EDITA_FK"))
	private Usuario usuarioUltimaEdicion;	
	
	
	@Column(name = "T_ORIGEN")
	@ColumnDefault("0")
	@NotNull
	@Size(max = 1)
	@Getter
	@Setter
	private String origen;
	
	@Column(name = "F_FECHA_CAPTURA")
	@Getter
	@Setter
	private Date fechaCaptura;

	@Column(name = "N_VERSION_DOC")
	@NotNull
	@Getter
	@Setter
	private Long versionDocumento;	

	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "DOC_ORIGINAL_ID", foreignKey =  @ForeignKey(name="DOC_ORIGINAL_FK"))
	private Documentos documentoOriginal;

	@Column(name = "L_ULTIMA_VERSION")
	@ColumnDefault("1")
	@NotNull
	@Getter
	@Setter
	private Boolean ultimaVersion;

	@Column(name = "L_DOC_VERSIONADO")
	@ColumnDefault("0")
	@NotNull
	@Getter
	@Setter
	private Boolean documentoVersionado;
	
	@Column(name = "L_FIRMADO")
	@ColumnDefault("0")
	@NotNull
	@Getter
	@Setter
	private Boolean firmado;
	
	@Column(name = "L_SELLADO")
	@ColumnDefault("0")
	@NotNull
	@Getter
	@Setter
	private Boolean sellado;
	
	@Column(name = "L_EDITABLE")
	@ColumnDefault("0")
	@NotNull
	@Getter
	@Setter
	private Boolean editable;
	
	@Column(name = "L_ANONIMIZADO")
	@ColumnDefault("0")
	@NotNull
	@Getter
	@Setter
	private Boolean anonimizado;
	
	@Column(name = "L_ANONIMIZADO_PARCIAL")
	@ColumnDefault("0")
	@NotNull
	@Getter
	@Setter
	private Boolean anonimizadoParcial;

	@Column(name = "L_ACTIVO")
	@ColumnDefault("1")
	@NotNull
	@Getter
	@Setter
	private Boolean activo;
	
	//***********************************************
	
	@Transient
	public String getNombreExtFichero() {
		return FileUtils.getNombreExt(descripcion, extensionFichero);
	}
	
	@Transient
	public boolean getOrigenIncorporado() {
		return ORIGEN_INCORPORADO.equals(this.origen);
	}
	
	public void copiarIndicadores(Documentos destino) {
		destino.setAnonimizado(this.getAnonimizado());
		destino.setAnonimizadoParcial(this.getAnonimizadoParcial());
		destino.setEditable(this.getEditable());
		destino.setSellado(this.getSellado());
		destino.setFirmado(this.getFirmado());
		destino.setDocumentoVersionado(this.getDocumentoVersionado());
	}
	
}
