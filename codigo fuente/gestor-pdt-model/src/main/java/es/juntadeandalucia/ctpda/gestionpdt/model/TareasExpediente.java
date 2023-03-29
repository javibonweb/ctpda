package es.juntadeandalucia.ctpda.gestionpdt.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
import javax.persistence.PostLoad;
import javax.persistence.PostUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import es.juntadeandalucia.ctpda.gestionpdt.model.core.Auditable;
import es.juntadeandalucia.ctpda.gestionpdt.model.core.AuditorAwareImpl;
import es.juntadeandalucia.ctpda.gestionpdt.model.core.EntidadBasica;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@EntityListeners(AuditorAwareImpl.class)
@Audited
@AuditTable(value = "GE_TAREAS_EXPEDIENTE_H")
@Table(name = "GE_TAREAS_EXPEDIENTE")
public class TareasExpediente extends Auditable{

	private static final long serialVersionUID = 1L;
	
	public static final String SITUACION_PENDIENTE = "P";
	public static final String SITUACION_CERRADA = "C";
	public static final String TIPO_ALTA_MANUAL = "M";
	public static final String TIPO_ALTA_AUTO = "A";

	@Id
	@Column(name = "TAREXP_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_TAREAS_EXPEDIENTE")
	@SequenceGenerator(name = "S_TAREAS_EXPEDIENTE", sequenceName = "S_TAREAS_EXPEDIENTE", allocationSize = 1)
	@Getter @Setter
	private Long id;
	
	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;

	@OneToOne (fetch=FetchType.EAGER)
	@JoinColumn(name= "TAREXP_EXP_ID", foreignKey =  @ForeignKey(name="GE_TAREAS_EXP_EXP_FK"))
	private Expedientes expediente;	

	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "TAREXP_TRA_ID", foreignKey =  @ForeignKey(name="GE_TAREAS_EXP_TRA_FK"))
	private TramiteExpediente tramiteExpediente;

	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "TAREXP_DOCEXP_TRA_ID", foreignKey =  @ForeignKey(name="GE_TAREAS_EXP_DOCEXP_TRA_FK"))
	private DocumentosExpedientesTramites documentoExpedienteTramite;	
	
	@OneToOne (fetch=FetchType.EAGER)
	@JoinColumn(name= "TAREXP_VALDOM_TAR_ID", foreignKey =  @ForeignKey(name="GE_TAREAS_EXP_TAR_FK"))
	private ValoresDominio valorTipoTarea;
	
	@OneToOne (fetch=FetchType.EAGER)
	@JoinColumn(name= "TAREXP_RESP_ID", foreignKey =  @ForeignKey(name="GE_TAREAS_EXP_RESP_FK"))
	private ResponsablesTramitacion responsableTramitacion;	
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "TAREXP_USU_ID_CREA", foreignKey =  @ForeignKey(name="GE_TAREAS_EXP_USU_CREA_FK"))
	private Usuario usuarioCreador;	

	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "TAREXP_USU_ID_CIERRA", foreignKey =  @ForeignKey(name="GE_TAREAS_EXP_USU_CIERRA_FK"))
	private Usuario usuarioCierre;	
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "TAR_EXP_OBSEXP_ID", foreignKey =  @ForeignKey(name="GE_TAR_EXP_OBSEXP_FK"))
	private ObservacionesExpedientes observaciones;
	
	@Column(name = "D_DESCRIPCION")
	@Size(max = 50)
	@Getter @Setter
	private String descripcion;

	@Column(name = "D_DESC_ABREV")
	@Size(max = 15)
	@Getter @Setter
	private String descripcionAbrev;

	@Column(name = "F_FECHA_INICIO")
	@Getter @Setter
	private Date fechaInicio;

	@Column(name = "F_FECHA_LIMITE")
	@Getter @Setter
	private Date fechaLimite;
	
	@Column(name = "F_FECHA_CIERRE")
	@Getter @Setter
	private Date fechaCierre;

	
	@Column(name = "T_INDICACIONES_SIG")
	@Size(max = 4000)
	@Getter @Setter
	private String indicacionesSiguiente;
	
	@Column(name = "T_SITUACION")
	@Size(max = 1)
	@Getter @Setter
	private String situacion;
	
	@Column(name = "T_TIPO_ALTA")
	@Size(max = 1)
	@Getter @Setter
	private String tipoAlta;

	@Column(name = "L_ACTIVA")
	@ColumnDefault("1")
	@NotNull
	@Getter @Setter
	private Boolean activa;

	@Column(name = "L_URGENTE")
	@ColumnDefault("0")
	@NotNull
	@Getter @Setter
	private Boolean urgente;

	
	@Column(name = "L_AVISO")
	@ColumnDefault("0")
	@NotNull
	@Getter @Setter
	private Boolean aviso;
	
	@Column(name = "L_BLOQUEO_DISTINTO_RESP")
	@ColumnDefault("0")
	@NotNull
	@Getter @Setter
	private Boolean bloqueoDistintoResponsable;
	
	@Column(name = "L_BLOQUEO_MISMO_RESP")
	@ColumnDefault("0")
	@NotNull
	@Getter @Setter
	private Boolean bloqueoMismoResponsable;

	@Column(name = "D_PLAZO_REFERENCIA")
	@Size(max = 15)
	@Getter @Setter
	private String plazoReferencia;

	//-------------------------------------------------
	
	@Transient
	private ResponsablesTramitacion responsableOriginal;
	
	
	@PostLoad
	@PostUpdate
	private void cargarValoresOriginales() {
		this.responsableOriginal = this.responsableTramitacion;
	}
	
	public boolean getCambiaResponsable() {
		return !mismaEntidad(this.responsableOriginal, this.responsableTramitacion);
	}

	private <E extends EntidadBasica> boolean mismaEntidad(E e1, E e2) {
		final Long id1 = EntidadBasica.getIdEntidad(e1);
		final Long id2 = EntidadBasica.getIdEntidad(e2);
		
		return Objects.equals(id1, id2);	
	}

	
	
	//-------------------------------------------------
	
    @Transient
	@Getter	@Setter
	private Boolean esTareaDependiente;
    
    @Transient
	@Getter	@Setter
	private List<TareasExpediente> dependencias;
	
    @Transient
	public boolean getEsTareaCerrada() {
		return SITUACION_CERRADA.equals(this.situacion);
	}
    
    @Transient
	public boolean getEsTareaTipo(String codTipo) {
		return this.getValorTipoTarea().getCodigo().equals(codTipo);
	}
    
    public String getFechaLimiteFormato() {
	    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	    return formatter.format(fechaLimite);		
	}
    
}
