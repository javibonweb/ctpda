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
@AuditTable(value = "GE_ACCESOS_DOCS_H")
@Table(name = "GE_ACCESOS_DOCS")
public class AccesosDocumentos extends Auditable{

	private static final long serialVersionUID = 1L;
	
	public static final String TIPO_ACCESO_CONSULTA = "CON";
	public static final String TIPO_ACCESO_EDICION = "EDI";
	public static final String TIPO_ACCESO_DESCARGA = "DESC";

	@Id
	@Column(name = "ACD_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_ACCESOS_DOCS")
	@SequenceGenerator(name = "S_ACCESOS_DOCS", sequenceName = "S_ACCESOS_DOCS", allocationSize = 1)
	@Getter
	@Setter
	private Long id;
	
	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;	
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "ACD_DOC_ID", foreignKey =  @ForeignKey(name="GE_ACD_DOC_ID_FK"))
	private Documentos documento;
	
	@NotNull
	@Column(name = "N_VERSION_DOC")
	private Long nVersionDoc;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "ACD_USU_ID", foreignKey =  @ForeignKey(name="GE_ACD_USU_FK"))
	private Usuario usuario;	
	
	@Column(name = "F_FECHA_HORA_ACCESO")
	@Getter
	@Setter
	private Date fechaHoraAcceso;
		
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "ACD_VALDOMTIPACC_ID", foreignKey =  @ForeignKey(name="GE_ACD_VALDOMTIPACC_FK"))
	private ValoresDominio valorTipoAcceso;

	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "ACD_EXP_ID", foreignKey =  @ForeignKey(name="GE_ACD_EXP_ID_FK"))
	private Expedientes expediente;	

	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "ACD_TRAM_ID", foreignKey =  @ForeignKey(name="GE_ACD_TRAM_ID_FK"))
	private TramiteExpediente tramite;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "ACD_TAREA_ID", foreignKey =  @ForeignKey(name="GE_ACD_TAREA_ID_FK"))
	private TareasExpediente tarea;	

	@Column(name = "L_EVOLUCION")
	@ColumnDefault("0")
	@NotNull
	@Getter
	@Setter
	private Boolean evolucion;

	@Column(name = "L_RESOLUCION")
	@ColumnDefault("0")
	@NotNull
	@Getter
	@Setter
	private Boolean resolucion;

}
