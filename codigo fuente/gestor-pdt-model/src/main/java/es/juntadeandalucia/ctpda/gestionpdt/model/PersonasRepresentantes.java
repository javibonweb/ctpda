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
@AuditTable(value = "GE_PERSONAS_REPRESENTANTES_H")
@Table(name = "GE_PERSONAS_REPRESENTANTES")
@NamedEntityGraph(name="persona.representante",
attributeNodes = {
	@NamedAttributeNode("id"),
	@NamedAttributeNode(value = "representante",  subgraph = "persona.basico"),
	@NamedAttributeNode(value = "persona",       subgraph = "persona.basico"),
	@NamedAttributeNode(value = "principal")
	})		    
public class PersonasRepresentantes extends Auditable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PRP_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_PERSONAS_REPRESENTANTES")
    @SequenceGenerator(name = "S_PERSONAS_REPRESENTANTES", sequenceName = "S_PERSONAS_REPRESENTANTES", allocationSize = 1)
	@Getter
	@Setter
	private Long id;

    @Version
    @Column(name = "N_VERSION")
    private Long version;
	
	@Column(name = "L_PRINCIPAL")
	@NotNull
	@Getter
	@Setter
	private Boolean principal;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PRP_PER_ID", referencedColumnName = "PER_ID", foreignKey = @ForeignKey(name = "GE_PRP_PERSONA_FK"))
    private Personas persona;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PRP_PER_REP_ID", referencedColumnName = "PER_ID", foreignKey = @ForeignKey(name = "GE_PRP_PERSONA_REP_FK"))
    private Personas representante;

    //---------------------
    
	public PersonasRepresentantes() {
		super();
	}

}
