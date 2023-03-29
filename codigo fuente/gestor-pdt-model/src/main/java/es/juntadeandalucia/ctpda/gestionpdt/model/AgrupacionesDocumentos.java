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
@AuditTable(value = "GE_AGRUP_DOCUMENTOS_H")
@Table(name = "GE_AGRUP_DOCUMENTOS")
public class AgrupacionesDocumentos extends Auditable implements Ordenable {

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@NotNull
	@Column(name = "AGRDOC_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_AGRUP_DOCUMENTOS")
	@SequenceGenerator(name = "S_AGRUP_DOCUMENTOS", sequenceName = "S_AGRUP_DOCUMENTOS", allocationSize = 1)
	@Getter
	@Setter
	private Long id;
	
	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;
	
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "AGRDOC_AGREXP_ID", foreignKey =  @ForeignKey(name="AGRDOC_AGREXP_FK"))
	private AgrupacionesExpedientes agrupacionExpedientes;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "AGRDOC_DOC_EXP_TRAM_ID", foreignKey =  @ForeignKey(name="AGRDOC_DOC_EXP_TRAM_FK"))
	private DocumentosExpedientesTramites documentosExpedientesTramites;
	
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
	
	@Column(name = "L_VINCULADO")
	@ColumnDefault("0")
	@NotNull
	@Getter
	@Setter
	private Boolean vinculado;
	
}
