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

import es.juntadeandalucia.ctpda.gestionpdt.model.Usuario;
import es.juntadeandalucia.ctpda.gestionpdt.model.core.Auditable;
import es.juntadeandalucia.ctpda.gestionpdt.model.core.AuditorAwareImpl;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Entity
@EntityListeners(AuditorAwareImpl.class)
@Audited
@AuditTable(value = "FOR_PRUEBAS_BLH_H")
@Table(name = "FOR_PRUEBAS_BLH")
public class FormacionPruebasPrb extends Auditable{
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "PRU_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_FOR_PRUEBAS_BLH")
	@SequenceGenerator(name = "S_FOR_PRUEBAS_BLH", sequenceName = "S_FOR_PRUEBAS_BLH", allocationSize = 1)
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

	@Version
	@Column(name = "N_VERSION")
	@Getter
	@Setter
	private Long nVersion;	

	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "FORPRU_BLH_USU", foreignKey = @ForeignKey(name = "FORPRU_BLH_USU_FK"))
	private Usuario usuario;
	
}