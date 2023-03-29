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
@AuditTable(value = "GE_RESP_TRAMITACION_H")
@Table(name = "GE_RESP_TRAMITACION")
public class ResponsablesTramitacion extends Auditable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "RESTRA_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_RESP_TRAMITACION")
	@SequenceGenerator(name = "S_RESP_TRAMITACION", sequenceName = "S_RESP_TRAMITACION", allocationSize = 1)
	@Getter
	@Setter
	private Long id;
	
	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;	

	@Column(name = "L_ACTIVO")
	@ColumnDefault("1")
	@NotNull
	@Getter
	@Setter
	private Boolean activo;

	@Column(name = "T_COD_RESP")
	@NotNull
	@Size(max = 20)
	@Getter
	@Setter
	private String codResponsable;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "RESTRA_ID_RESP_SUPERIOR", foreignKey =  @ForeignKey(name="GE_RESP_TRAMITACION_SUP_FK"))
	private ResponsablesTramitacion responsableSuperior;	
	
	@Column(name = "D_DESCRIPCION")
	@Size(max = 50)
	@Getter
	@Setter
	private String descripcion;

	@Column(name = "D_DESC_ABREV")
	@Size(max = 15)
	@Getter
	@Setter
	private String descripcionAbrev;

}
