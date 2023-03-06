package es.juntadeandalucia.ctpda.gestionpdt.model;

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
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@EntityListeners(AuditorAwareImpl.class)
@Audited
@AuditTable(value = "GE_DOCUMENTOS_RESOLUCIONES_H")
@Table(name = "GE_DOCUMENTOS_RESOLUCIONES")
public class DocumentoResolucion extends Auditable{

	
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@NotNull
	@Column(name = "DOCRES_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_DOCUMENTOS_RESOLUCIONES")
	@SequenceGenerator(name = "S_DOCUMENTOS_RESOLUCIONES", sequenceName = "S_DOCUMENTOS_RESOLUCIONES", allocationSize = 1)
	@Getter
	@Setter
	private Long id;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "DOCRES_DOCEXP_ID", foreignKey =  @ForeignKey(name="DOCRES_DOCEXP_FK"))
	private DocumentosExpedientes documentoExpediente;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "DOCRES_RES_ID", foreignKey =  @ForeignKey(name="DOCRES_RES_FK"))
	private Resolucion resolucion;	

	@Column(name = "L_ANONIMIZADO")
	@ColumnDefault("0")
	@Getter
	@Setter
	private Boolean anonimizado;
	
	@Column(name = "F_FECHA_PUBLI_WEB")
	@Getter
	@Setter
	private Date fechaPublicacionWeb;

	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;	

}
