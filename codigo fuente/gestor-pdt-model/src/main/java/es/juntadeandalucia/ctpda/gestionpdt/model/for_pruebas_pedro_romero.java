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
import javax.persistence.ManyToOne;
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
@AuditTable(value = "FOR_PRUEBAS_PEDRO_ROMERO_H")
@Table(name = "FOR_PRUEBAS_PEDRO_ROMERO")
		
		
public class for_pruebas_pedro_romero extends Auditable{

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "PRUD_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_FOR_PRUEBAS_PEDRO_ROMERO")
	@SequenceGenerator(name = "S_FOR_PRUEBAS_PEDRO_ROMERO", sequenceName = "S_FOR_PRUEBAS_PEDRO_ROMERO", allocationSize = 1)
	@Getter
	@Setter
	private Long id;
	
	@Column(name = "L_ACTIVA")
	@NotNull
	@ColumnDefault("1")
	@Getter
	@Setter
	private Boolean activa;
	
	@Column(name = "D_DESCRIPCION")
	@NotNull
	@Size(max = 225)
	@Getter
	@Setter
	private String descripcion;
	
	@Column(name = "C_CODIGO")
	@NotNull
	@Size(max = 2)
	@Getter
	@Setter
	private String codigo;
	

	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;	


}
