package es.juntadeandalucia.ctpda.gestionpdt.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.*;
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
@AuditTable(value = "FOR_PRUEBAS_GRS_H")
@Table(name = "FOR_PRUEBAS_GRS")
public class FormacionPruebasGRS extends Auditable{

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "PRU_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FOR_PRUEBAS_GRS")
	@SequenceGenerator(name = "S_FOR_PRUEBAS_GRS", sequenceName = "S_FOR_PRUEBAS_GRS", allocationSize = 1)
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
	@Size(max = 255)
	@Getter
	@Setter
	private String descripcion;
	
	@Column(name = "C_CODIGO")
	@NotNull
	@Size(max = 50)
	@Getter
	@Setter
	private String codigo;
	

	@Column(name = "N_VERSION")
	@Version
	@Getter
	@Setter
	private Long nVersion;
	
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FORPRU_GRS_USU", foreignKey = @ForeignKey(name = "FOR_PRUEBAS_GRS_USU"))
	private Usuario usuario;


	public String getIdentificador() {
		return this.id.toString();
	}	


}
