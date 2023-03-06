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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
@AuditTable(value = "GE_ELEMENTOS_SERIES_H")
@Table(name = "GE_ELEMENTOS_SERIES")
public class ElementosSeries extends Auditable {

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ELEM_SER_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_ELEMENTOS_SERIES")
	@SequenceGenerator(name = "S_ELEMENTOS_SERIES", sequenceName = "S_ELEMENTOS_SERIES", allocationSize = 1)
	@Getter
	@Setter
	private Long id;

	//--------------

	@Column(name = "T_TIPO")
	@Size(max = 10)
	@Getter
	@Setter
	private String tipo;
	
	@Column(name = "T_DESCRIPCION")
	@Size(max = 100)
	@Getter
	@Setter
	private String descripcion;

	@Column(name = "T_CODIGO_SERIE")
	@Size(max = 25)
	@Getter
	@Setter
	private String codigoSerie;

	//---------------------
	
	@Column(name = "F_FECHA_INICIAL")
	@Getter
	@Setter
	private Date fechaInicial;
	
	@Column(name = "F_FECHA_FINAL")
	@Getter
	@Setter
	private Date fechaFinal;
	
}
