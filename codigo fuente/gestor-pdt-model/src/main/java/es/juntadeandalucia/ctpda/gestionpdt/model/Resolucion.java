package es.juntadeandalucia.ctpda.gestionpdt.model;

import java.util.Date;
import java.util.List;

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
@AuditTable(value = "GE_RESOLUCIONES_H")
@Table(name = "GE_RESOLUCIONES")
public class Resolucion  extends Auditable{

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "RES_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_RESOLUCION")
	@SequenceGenerator(name = "S_RESOLUCION", sequenceName = "S_RESOLUCION", allocationSize = 1)
	@Getter
	@Setter
	private Long id;
	
	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;
	
	
	@Column(name = "T_CODIGO_RESOL")
	@Size(max = 50)
	@Getter
	@Setter
	private String codigoResolucion;
	
	@Column(name = "F_FECHA_RESOLUCION")
	@Getter
	@Setter
	private Date fechaResolucion;
	
	@Column(name = "F_FECHA_PUBLI_WEB")
	@Getter
	@Setter
	private Date fechaPublicacionWeb;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "RESOL_VALDOMSENTRESOL_ID", foreignKey = @ForeignKey(name = "GE_RESOL_VALDOMSENTRESOL_FK"))
	private ValoresDominio valorSentidoResolucion;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "RESOL_VALDOMTIPORESOL_ID", foreignKey = @ForeignKey(name = "GE_RESOL_VALDOMTIPORESOL_FK"))
	private ValoresDominio valorTipoResolucion;
	
	@Column(name = "D_RESUMEN")
	@Size(max = 400)
	private String resumen;
	
	@Column(name = "L_ANONIMIZADA")
	@ColumnDefault("0")
	@Getter
	@Setter
	private Boolean anonimizada;
	
	@Column(name = "L_NOTIFICADA_TOTAL")
	@ColumnDefault("0")
	@Getter
	@Setter
	private Boolean notificadaTotal;
	
	//**************************
	@Transient
	@Getter
	@Setter
	private List<ValoresDominio> derechosReclamados;
	@Transient
	@Getter
	@Setter
	private List<ValoresDominio> articulosAfectados;
	
}
