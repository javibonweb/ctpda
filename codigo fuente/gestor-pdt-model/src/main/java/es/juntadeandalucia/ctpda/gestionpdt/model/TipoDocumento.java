package es.juntadeandalucia.ctpda.gestionpdt.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
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
@AuditTable(value = "GE_TIPOS_DOCUMENTOS_H")
@Table(name = "GE_TIPOS_DOCUMENTOS")
@NamedEntityGraph(name = "tipoDocumento.basico", 
attributeNodes = {
		@NamedAttributeNode("id"),
		@NamedAttributeNode("descripcion")
		})		    
public class TipoDocumento extends Auditable{

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@NotNull
	@Column(name = "TIP_DOC_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_TIPOS_DOCUMENTOS")
	@SequenceGenerator(name = "S_TIPOS_DOCUMENTOS", sequenceName = "S_TIPOS_DOCUMENTOS", allocationSize = 1)
	@Getter
	@Setter
	private Long id;
	
	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;	

	
	@Column(name = "T_CODIGO")
	@NotNull
	@Size(max = 10)
	@Getter
	@Setter
	private String codigo;

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
	
	@Column(name = "T_CODIGO_TIPO_ENI")
	@NotNull
	@Size(max = 10)
	@Getter
	@Setter
	private String codigoTipoENI;
	
	@Column(name = "T_CODIGO_ORIGEN")
	@ColumnDefault("0")
	@NotNull
	@Size(max = 1)
	@Getter
	@Setter
	private String codigoOrigen;
	
	@Column(name = "L_ACTIVO")
	@ColumnDefault("1")
	@NotNull
	@Getter
	@Setter
	private Boolean activo;

}
