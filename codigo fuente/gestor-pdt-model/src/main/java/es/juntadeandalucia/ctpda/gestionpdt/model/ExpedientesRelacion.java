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
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

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
@AuditTable(value = "GE_EXPEDIENTES_RELACIONADOS_H")
@Table(name = "GE_EXPEDIENTES_RELACIONADOS")
@NamedEntityGraph(name="expediente.relacion",
attributeNodes = {
	@NamedAttributeNode("id"),
	@NamedAttributeNode(value = "expedienteRelacionado",  subgraph = "expediente.basico"),
	@NamedAttributeNode(value = "expedienteOrigen",       subgraph = "expediente.basico"),
	@NamedAttributeNode(value = "motivo")
	})		    
public class ExpedientesRelacion extends Auditable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "EXR_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_EXPEDIENTES_RELACIONADOS")
    @SequenceGenerator(name = "S_EXPEDIENTES_RELACIONADOS", sequenceName = "S_EXPEDIENTES_RELACIONADOS", allocationSize = 1)
	@Getter
	@Setter
	private Long id;

    @Version
    @Column(name = "N_VERSION")
    private Long version;

    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false) //, cascade = CascadeType.DETACH)
    @JoinColumn(name = "EXR_VALDOM_MOT_ID", referencedColumnName = "VAL_DOM_ID", foreignKey = @ForeignKey(name = "GE_EXR_VALDOM_MOT_FK"))
    private ValoresDominio motivo;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "EXR_EXPRELAC_ID", referencedColumnName = "EXP_ID", foreignKey = @ForeignKey(name = "GE_EXR_EXPRELAC_FK"))
    private Expedientes expedienteRelacionado;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "EXR_EXPORIGEN_ID", referencedColumnName = "EXP_ID", foreignKey = @ForeignKey(name = "GE_EXR_EXPORIGEN_FK"))
    private Expedientes expedienteOrigen;

    //---------------------
    
	public ExpedientesRelacion() {
		super();
	}

}
