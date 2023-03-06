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
@AuditTable(value = "GE_TIPO_TRAMITE_H")
@Table(name = "GE_TIPO_TRAMITE")
public class TipoTramite extends Auditable{

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@NotNull
	@Column(name = "TIP_TRA_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_TIPO_TRAMITE")
	@SequenceGenerator(name = "S_TIPO_TRAMITE", sequenceName = "S_TIPO_TRAMITE", allocationSize = 1)
	@Getter
	@Setter
	private Long id;

	@Version
	@Column(name = "N_VERSION")
	private Long nVersion;


	@Column(name = "T_CODIGO")
	@NotNull
	@Size(max = 10)
	@Getter
	@Setter
	private String codigo;
	
	@Column(name = "D_DESCRIPCION")
	@Size(max = 255)
	@Getter
	@Setter
	private String descripcion;
	
	@Column(name = "C_COLOR_TRAMITE")
	@Size(max = 255)
	@Getter
	@Setter
	private String colorTramite;
	
	@Column(name = "C_COLOR_SUB_TRAMITE")
	@Size(max = 255)
	@Getter
	@Setter
	private String colorSubTramite;
	
	@Column(name = "T_INSTRUCCIONES")
	@Size(max = 4000)
	@Getter
	@Setter
	private String instrucciones;
	
	@Column(name = "D_DESC_ABREV")
	@Size(max = 100)
	@Getter
	@Setter
	private String descripcionAbrev;

	@Column(name = "L_ACTIVO")
	@ColumnDefault("1")
	@NotNull
	@Getter
	@Setter
	private Boolean activo;
	
	@Column(name = "T_COMPORTAMIENTO")
	@NotNull
	@Size(max = 50)
	@Getter
	@Setter
	private String comportamiento;

	@Column(name = "L_INFORMAL")
	@ColumnDefault("0")
	@Getter
	@Setter
	private Boolean informal;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TIPTRA_VALDOM_TIPINT_ID", foreignKey = @ForeignKey(name = "GE_TIPTRA_VALDOM_TIPINT_ID_FK"))
	private ValoresDominio valorTipoInteresadoDefecto;
	
	@Column(name = "L_TIPO_INTERESADO_BLOQUEADO")
	@Getter
	@Setter
	private Boolean tipoInteresadoBloqueado;
	
	@Column(name = "L_MODIF_DESCRIPCION")
	@ColumnDefault("0")
	@Getter
	@Setter
	private Boolean modifDescripcion;

	
	@Column(name = "L_VER_DOC_TRAM")
	@ColumnDefault("1")
	@Getter
	@Setter
	private Boolean verDocumentosTramite;
	
	@Column(name = "L_VER_DOC_SUBTRAM")
	@ColumnDefault("0")
	@Getter
	@Setter
	private Boolean verDocumentosSubtramite;
	
	@Column(name = "D_SUBSITUACION")
	@Size(max = 50)
	@Getter
	@Setter
	private String descSubsituacion;
	
	@Column(name = "L_SUBSITUACION_SUP")
	@ColumnDefault("0")
	@Getter
	@Setter
	private Boolean subsituacionSuperior;
	
	@Column(name = "L_TRAT_VINCULADOS")
	@ColumnDefault("0")
	@NotNull
	@Getter
	@Setter
	private Boolean tratarVinculados;
		
}
