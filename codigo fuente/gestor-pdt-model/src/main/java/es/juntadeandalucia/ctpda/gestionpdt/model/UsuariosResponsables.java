package es.juntadeandalucia.ctpda.gestionpdt.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

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
@AuditTable(value = "GE_USUARIO_RESP_H")
@Table(name = "GE_USUARIO_RESP")
@NamedEntityGraph(name = "usuariosResponsables.listaResponsables", 
	attributeNodes = {
			@NamedAttributeNode("id"),
			@NamedAttributeNode(value = "responsable", subgraph = "usuResp.responsable" )
	}, 
	subgraphs = { 
		@NamedSubgraph(name="usuResp.responsable", 
			attributeNodes = { 
					@NamedAttributeNode("id"),
					@NamedAttributeNode("descripcion")
			})
	}
)
public class UsuariosResponsables extends Auditable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "USRES_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_USUARIO_RESP")
	@SequenceGenerator(name = "S_USUARIO_RESP", sequenceName = "S_USUARIO_RESP", allocationSize = 1)
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

	@Column(name = "L_POR_DEFECTO")
	@ColumnDefault("1")
	@NotNull
	@Getter
	@Setter
	private Boolean porDefecto;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "USRRES_USU_ID", foreignKey =  @ForeignKey(name="GE_USUARIO_RESP_USU_FK"))
	private Usuario usuarioResponsable;	
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "USRRES_RESP_TR_ID", foreignKey =  @ForeignKey(name="GE_USUARIO_RESP_RESP_TR_FK"))
	private ResponsablesTramitacion responsable;	

	@Column(name = "F_FECHA_INICIO")
	@Getter
	@Setter
	private Date fechaInicio;
	
	@Column(name = "F_FECHA_FIN")
	@Getter
	@Setter
	private Date fechaFin;
	
}

