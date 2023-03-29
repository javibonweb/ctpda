package es.juntadeandalucia.ctpda.gestionpdt.model;

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
@AuditTable(value = "GE_PARAMETROS_H")
@Table(name = "GE_PARAMETROS")
public class Parametro  extends Auditable{

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "PAR_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_PARAMETRO")
	@SequenceGenerator(name = "S_PARAMETRO", sequenceName = "S_PARAMETRO", allocationSize = 1)
	@Getter
	@Setter
	private Long id;
	
	@Column(name = "T_CAMPO", unique = true)
	@Size(max = 50)
	@NotNull
	@Getter
	@Setter
	private String campo;
	
	
	@Column(name = "D_DESCRIPCION")
	@Size(max = 250)
	private String descripcion;

	@Column(name = "T_VALOR")
	@Size(max = 250)
	@Getter
	@Setter
	private String valor;
	
	
	@Column(name = "L_ACTIVA")
	@ColumnDefault("1")
	@NotNull
	@Getter
	@Setter
	private Boolean activa;
	
	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;	
	

}
