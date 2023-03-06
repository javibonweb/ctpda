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
@AuditTable(value = "GE_VALORES_META_ENI_H")
@Table(name = "GE_VALORES_META_ENI")
public class ValoresMetadatoENI extends Auditable{

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@NotNull
	@Column(name = "VMETENI_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_VALORES_META_ENI")
	@SequenceGenerator(name = "S_VALORES_META_ENI", sequenceName = "S_VALORES_META_ENI", allocationSize = 1)
	@Getter
	@Setter
	private Long id;
	
	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;	


	@NotNull
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "VMETENI_TMETENI_ID", foreignKey =  @ForeignKey(name="VMETENI_TMETENI_FK"))
	private TipoMetadatoENI tipoMetadatoENI;	

	@Column(name = "T_CODIGO_ENI")
	@NotNull
	@Size(max = 255)
	@Getter
	@Setter
	private String codigoENI;
	
	@Column(name = "T_VALOR")
	@NotNull
	@Size(max = 255)
	@Getter
	@Setter
	private String valor;
	
	@Column(name = "N_ORDEN")
	//@NotNull
	@Getter
	@Setter
	private Long orden;
	
	@Column(name = "L_ACTIVO")
	@ColumnDefault("1")
	@NotNull
	@Getter
	@Setter
	private Boolean activo;
	
}
