package es.juntadeandalucia.ctpda.gestionpdt.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import es.juntadeandalucia.ctpda.gestionpdt.model.core.Auditable;
import lombok.Data;

@Data
@Entity
@Table(name = "GE_LIST_EXP_PEREXP")
public class ExpedientesPersonasExpedientesMaestra extends Auditable {
	/**
	 * Serial id
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "EXP_ID")
	@NotNull
	private Long id;

	@NotNull
	@Column(name = "T_NUM_EXPEDIENTE")
	private String numExpediente;

	@NotNull
	@Column(name = "T_NOMBRE_EXPEDIENTE")
	private String nombreExpediente;

	@NotNull
	@Column(name = "T_MOTREL")
	private String motivoRelacion;
	
	@NotNull
	@Column(name = "N_PRINCIPAL")
	private Boolean persPrincipal;
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "PER_PER_ID", foreignKey = @ForeignKey(name = "GE_PER_PER_FK"))
	@NotNull
	private Personas persona;

}
