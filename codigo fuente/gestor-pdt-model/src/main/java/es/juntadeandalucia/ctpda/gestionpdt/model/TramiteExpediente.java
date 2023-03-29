package es.juntadeandalucia.ctpda.gestionpdt.model;

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
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Max;
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
@AuditTable(value = "GE_TRAMITE_EXPED_H")
@Table(name = "GE_TRAMITE_EXPED")
@NamedEntityGraph(name = "tramiteExp.basico", 
	attributeNodes = {
			@NamedAttributeNode("id"),
			@NamedAttributeNode("fechaIni"),
			@NamedAttributeNode("descripcion"),
			@NamedAttributeNode("finalizado"),
})
public class TramiteExpediente extends Auditable{

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	public static final String ABIERTO = "Abierto";
	public static final String CERRADA = "Cerrada";
	
	@Id
	@NotNull
	@Column(name = "TRAM_EXP_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_TRAMITE_EXPED")
	@SequenceGenerator(name = "S_TRAMITE_EXPED", sequenceName = "S_TRAMITE_EXPED", allocationSize = 1)
	@Getter
	@Setter
	private Long id;

	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;

	
	@OneToOne (fetch=FetchType.EAGER)
	@NotNull
	@JoinColumn(name= "TRAM_EXP_EXP_ID", foreignKey =  @ForeignKey(name="GE_TRAM_EXP_EXP_FK"))
	private Expedientes expediente;

	@OneToOne (fetch=FetchType.EAGER)
	@NotNull
	@JoinColumn(name= "TRAM_EXP_TIP_TRA_ID", foreignKey =  @ForeignKey(name="GE_TRAM_EXP_TIP_TRA_FK"))
	private TipoTramite tipoTramite;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "TRAM_EXP_RESTRA_ID", foreignKey =  @ForeignKey(name="GE_TRAMITE_EXPED_RESP_FK"))
	private ResponsablesTramitacion responsable;	

	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "TRAM_EXP_TRAMEXPSUP_ID", foreignKey =  @ForeignKey(name="GE_TRAM_EXP_TRAMEXPSUP_FK"))
	private TramiteExpediente tramiteExpedienteSup;
	
	@OneToOne (fetch=FetchType.EAGER)
	@JoinColumn(name= "TRAM_EXP_USU_ALTA_ID", foreignKey =  @ForeignKey(name="GE_TRAM_EXP_USU_ALTA_FK"))
	private Usuario usuarioTramitador;
	
	
	@OneToOne (fetch=FetchType.EAGER)
	@JoinColumn(name= "TRAM_EXP_OBSEXP_ID", foreignKey =  @ForeignKey(name="GE_TRAM_EXP_OBSEXP_FK"))
	private ObservacionesExpedientes observaciones;
	
	@Column(name = "N_NIVEL")
	@Max(3)
	@NotNull
	@Getter
	@Setter
	private Long nivel;
	
	@Column(name = "D_DESCRIPCION")
	@Size(max = 255)
	@Getter
	@Setter
	private String descripcion;
	
	@Column(name = "D_DESC_ABREV")
	@Size(max = 100)
	@Getter
	@Setter
	private String descripcionAbrev;

	@Column(name = "F_FECHA_INI")
	@NotNull
	@Getter
	@Setter
	private Date fechaIni;
	
	@Column(name = "F_FECHA_TRAMITE")
	@Getter
	@Setter
	private Date fechaTramite;
	
	@Column(name = "D_INF_RELEVANTE")
	@Size(max = 255)
	@Getter
	@Setter
	private String informacionRelevante;
	
	@Column(name = "D_SIT_ADICIONAL")
	@Size(max = 255)
	@Getter
	@Setter
	private String situacionAdicional;
	
	@Column(name = "L_ACTIVO")
	@ColumnDefault("1")
	@NotNull
	@Getter
	@Setter
	private Boolean activo;

	@Column(name = "L_FINALIZADO")
	@ColumnDefault("1")
	@NotNull
	@Getter
	@Setter
	private Boolean finalizado;
	
	@Column(name = "F_FECHA_FIN_REAL")
	@Getter
	@Setter
	private Date fechaFinReal;
	
	@Column(name = "F_FECHA_FIN")
	@Getter
	@Setter
	private Date fechaFin;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name= "TRAM_EXP_USU_FIN_ID", foreignKey =  @ForeignKey(name="GE_TRAM_EXP_USU_FIN_FK"))
	private Usuario usuarioFinalizacion;	
	
    @Transient
	@Getter
	@Setter
    private String numResolVinculada;
    
    @Transient
	@Getter
	@Setter
    private String numResolVinculadaConfirm;
    
    @Transient
	@Getter
	@Setter
    private List<TramiteExpediente> listaSubTramites;
    
    @Transient
	@Getter
	@Setter
    private CfgMetadatosTram cfgMetadatosTram;
    
    @Transient
	@Getter
	@Setter
    private DetalleExpdteTram detalleExpdteTram;
    
    @Transient
	@Getter
	@Setter
	private Date fechaLimite;
    
    @Transient
	@Getter
	@Setter
	private Long selectedNuevoTipoPlazoId;
    
    @Transient
	@Getter
	@Setter
	private Long selectedNuevoInstructorApiIdAcAdmis;

    @Transient
	@Getter
	@Setter
	private Long selectedNuevoSerieNumeracionId;
    
    @Transient
	@Getter
	@Setter
	private Long selectedNuevoSentidoResolucionId;
    
    @Transient
	@Getter
	@Setter
	private Long selectedNuevoTipoResolucionId;
    
    @Transient
	@Getter
	@Setter
	private Long selectedNuevoResponsableId;

    @Transient
	@Getter
	@Setter
	private Long selectedNuevoTipoAdmisionId;
    
    @Transient
	@Getter
	@Setter
	private Long selectedNuevoMotivoInadmisionId;
    
    @Transient
	@Getter
	@Setter
	private Long selectedNuevoTipoInteresadoId;
    
    @Transient
	@Getter
	@Setter
	private Long selectedNuevaIdentifInteresadoId;
    
    @Transient
	@Getter
	@Setter
	private Long selectedNuevoCanalSalidaId;
    
    @Transient
	@Getter
	@Setter
	private Long selectedNuevoActoRecId;
    
    @Transient
	@Getter
	@Setter
	private Long selectedNuevoCanalEntradaId;
    
    
    @Transient
	@Getter
	@Setter
	private Long selectedNuevoFirmanteId;
    
    @Transient
	@Getter
	@Setter
	private Long selectedNuevoTipoFirmaId;
     
    @Transient
	@Getter
	@Setter
	private Long selectedNuevoCanalInfSalidaId;
    
    @Transient
	@Getter
	@Setter
	private Long selectedNuevoCanalInfEntradaId;
    
    @Transient
	@Getter
	@Setter
	private Long selectedNuevoInstructorAPIId;
     
    @Transient
	@Getter
	@Setter
	private Long selectedNuevoResulNotificacionId;
    
    @Transient
	@Getter
	@Setter
	private List<ResponsablesTramitacion> listaResponsables;
      
    @Transient
	@Getter
	@Setter
	private List<ValoresDominio> listaValoresDominioTipoInteresado;
    
    @Transient
	@Getter
	@Setter
	private List<Usuario> listaFirmantes;
    
    @Transient
	@Getter
	@Setter
    private List<ValoresDominio> listaTipoFirmas;
    
    @Transient
	@Getter
	@Setter
	private Boolean mostrarCamposResol;
    
    @Transient
	@Getter
	@Setter
	private Boolean mostrarInstrucciones;
    
    @Transient
	@Getter
	@Setter
	private Boolean mostrarTareas;
    
    @Transient
	@Getter
	@Setter
	private Boolean mostrarTareaREVT;
    
    @Transient
	@Getter
	@Setter
	private Boolean mostrarTareaFYN;
    
    @Transient
	@Getter
	@Setter
	private Boolean mostrarListadoNotificaciones;
    
    @Transient
	@Getter
	@Setter
	private Boolean mostrarListadoFirmas;
    
    
    @Transient
	@Getter
	@Setter
	private Boolean mostrarBotoneraResolucion;
    
    @Transient
	@Getter
	@Setter
	private Boolean habilitarVerResol;
    
    @Transient
	@Getter
	@Setter
	private Boolean habilitarComboMotivoInad;
    
    @Transient
	@Getter
	@Setter
	private Boolean habilitarAsocResol;
    
    @Transient
	@Getter
	@Setter
	private Boolean habilitarInstructorApi;
     
    @Transient
	@Getter
	@Setter
	private String mensajeConfirmacionSerie;
    
    @Transient
	@Getter
	@Setter
	private Boolean esIdentIntDPD;
    
    @Transient
	@Getter
	@Setter
	private Boolean esIdentIntPersona;
    
    @Transient
	@Getter
	@Setter
	private Boolean esIdentIntSujOblig;
    
    
    @Transient
	@Getter
	@Setter
	private Boolean esIdentIntAutControl;
	
    @Transient
    @Getter
	@Setter
	private List<ValoresDominio> listaValoresDominioIdentifInteresado;
	
    @Transient
	@Getter
	@Setter
	private List<SujetosObligados> listaIdentifIntSujOblig;

    
    @Transient
	@Getter
	@Setter
	private List<PersonaDTO> listaIdentifIntPersDTO;
    
	
    @Transient
	@Getter
	@Setter
	private List<Personas> listaIdentifIntDpd;

    @Transient
	@Getter
	@Setter
	private Long selectedNuevoPropuestaApiId;
    
	@Getter
    @Setter
    @OneToOne(mappedBy = "tramiteExpediente")
    private DetalleExpdteTram detalleExpdteTramMappedBy;
	
    @Transient
	@Getter
	@Setter
	private Boolean mostrarBotonFinalizar;
    
	//************************************************************
	
    public void limpiarListasInteresados() {
    	this.listaValoresDominioIdentifInteresado = null;
		this.listaIdentifIntSujOblig = null;
		this.listaIdentifIntPersDTO = null;
		this.listaIdentifIntDpd = null;			
    }
    
    //------------------
    
    @Transient
   	private String descripcionActual;
    @Transient
    private ResponsablesTramitacion responsableActual;
    
    @PostLoad
    private void init() {
    	descripcionActual = descripcion;
    	responsableActual = this.responsable;
    }
    
    @PostPersist @PostUpdate
    private void saved() {
    	init();
    } 
    
    public boolean cambiaDescripcion() {
		return !Objects.equals(descripcion, descripcionActual);
    }

	public boolean cambiaResponsable() {
		return !mismaEntidad(responsable, responsableActual);
	}
	
	private <E extends EntidadBasica> boolean mismaEntidad(E e1, E e2) {
		final Long id1 = EntidadBasica.getIdEntidad(e1);
		final Long id2 = EntidadBasica.getIdEntidad(e2);
		
		return Objects.equals(id1, id2);	
	}

	//------------------
    
    public boolean getEsSubtramite() {
    	return null != this.getTramiteExpedienteSup();
    }
    
	@Transient
	public Boolean tieneSubTramites() {
		return this.listaSubTramites != null && !this.listaSubTramites.isEmpty();
	}
	
    @Transient
    public Long[] getIdsTramiteSubtramite() {
    	final Long[] ids = new Long[2];
    	
    	if(null != this.getTramiteExpedienteSup()) {
    		//Este trámite es un subtrámite
    		ids[0] = this.getTramiteExpedienteSup().getId();
    		ids[1] = this.getId();
    	} else {
    		ids[0] = this.getId();  		
    	}
    	
    	return ids;
    }
    
    @Transient
   	@Getter	@Setter
   	private Boolean tieneListaSubTramites;
    
    //-------------------
    
    @Transient
   	@Getter	@Setter
   	private String condicionCambioSituacion;
    
    public String cssSemaforoFinalizado(boolean finalizado) {
        final String css;
        
        if(Boolean.TRUE.equals(finalizado)) {
            css = "marca-roja";
        } else {
            css = "marca-verde";
        }

        return css;
    }	
    
    public String getSituacionNotCom()
    {
    	String situacionNotCom = "";
    	
    	if(Boolean.TRUE.equals(this.finalizado))
    	{
    		situacionNotCom = CERRADA;
    	}else {
    		situacionNotCom = ABIERTO;
    	}
    	
    	return situacionNotCom;
    }

}
