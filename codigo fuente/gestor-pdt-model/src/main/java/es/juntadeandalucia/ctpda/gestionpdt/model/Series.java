package es.juntadeandalucia.ctpda.gestionpdt.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@AuditTable(value = "GE_SERIES_H")
@Table(name = "GE_SERIES")
public class Series extends Auditable{

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "SER_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_SERIES")
	@SequenceGenerator(name = "S_SERIES", sequenceName = "S_SERIES", allocationSize = 1)
	@Getter
	@Setter
	private Long id;

	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;	
	
	@Column(name = "T_CODIGO_SERIE")
	@Size(max = 25)
	@Getter
	@Setter
	private String codigoSerie;
	
	
	//---------------
	
	@Column(name = "F_FECHA_INICIAL")
	@Getter
	@Setter
	private Date fechaInicial;
	
	@Column(name = "F_FECHA_FINAL")
	@Getter
	@Setter
	private Date fechaFinal;
	
	@Column(name = "N_NUMERO_INICIAL")
	@Getter
	@Setter
	private Long numeroInicial;
	
	@Column(name = "N_NUMERO_FINAL")
	@Getter
	@Setter
	private Long numeroFinal;
	
	@Column(name = "T_PATRON")
	@Size(max = 100)
	@Getter
	@Setter
	private String patron;
	
	@Column(name = "N_MARGEN_AVISO")
	@Getter
	@Setter
	private Long margenAviso;
	
	@Column(name = "N_INCREMENTO")
	@Getter
	@Setter
	private Integer incremento;
	
	//�ltimo n�mero generado
	@Column(name = "N_ULTIMO_NUMERO")
	@Getter
	@Setter
	private Long ultimoNumero;
	
	@Column(name = "F_ULTIMA_FECHA")
	@Getter
	@Setter
	private Date ultimaFecha;	
	
	@Column(name = "L_SECUENCIA_FECHA")
	@ColumnDefault("1")
	@NotNull
	@Getter
	@Setter
	private Boolean secuenciaFecha;


}
