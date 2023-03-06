package es.juntadeandalucia.ctpda.gestionpdt.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ColumnDefault;

import es.juntadeandalucia.ctpda.gestionpdt.model.core.Auditable;
import es.juntadeandalucia.ctpda.gestionpdt.model.core.Ordenable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = "GE_LISTADO_DOCUMENTOS_EXP_TRAM")
public class DocumentosTramiteMaestra extends Auditable implements Ordenable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "DOC_EXP_TRAM_ID")
	@Getter
	@Setter
	private Long idDocExpTramite;

	@Column(name = "DOCEXP_ID")
	@Getter
	@Setter
	private Long id;

	@Column(name = "DOC_ID")
	@Getter
	@Setter
	private Long idDocumento;
	
	@Column(name = "EXP_ID")
	@Getter
	@Setter
	private Long idExpediente;

	@Column(name = "TRAM_EXPE_ID")
	@Getter
	@Setter
	private Long idTramiteExpediente;

	@Column(name = "TRAM_EXPE_SUP_ID")
	@Getter
	@Setter
	private Long idTramiteExpedienteSup;

	@Column(name = "T_IDEN_DOC")
	@NotNull
	@Size(max = 255)
	@Getter
	@Setter
	private String identificadorDoc;

	@Column(name = "D_DESCRIPCION")
	@Size(max = 255)
	@Getter
	@Setter
	private String descripcion;
	
	@Column(name = "D_DESC_ABREV")
	@Size(max = 255)
	@Getter
	@Setter
	private String descripcionAbrev;
	
	@Column(name = "T_NOMBRE_FICHERO")
	@NotNull
	@Size(max = 100)
	@Getter
	@Setter
	private String nombreFichero;
	
	@Column(name = "T_EXTENSION_FICHERO")
	@NotNull
	@Size(max = 10)
	@Getter
	@Setter
	private String extensionFichero;
		
	@Column(name = "TIP_DOC_ID")
	@Getter
	@Setter
	private Long idTipo;
	
	@Column(name = "D_DESCRIPCION_TIPO")
	@Size(max = 255)
	@Getter
	@Setter
	private String descripcionTipo;

	@Column(name = "N_ORDEN")
	//@NotNull
	@Getter
	@Setter
	private Long orden;
	
	@Column(name = "TAREXP_ID")
	@Getter
	@Setter
	private Long idTarea;
	
	@Column(name = "ORIGEN_DOCEXPTR")
	@NotNull
	@Size(max = 1)
	@Getter
	@Setter
	private String origen;
	
	@Column(name = "L_FIRMADO")
	@ColumnDefault("0")
	@NotNull
	@Getter
	@Setter
	private Boolean firmado;
	
	@Column(name = "L_SELLADO")
	@ColumnDefault("0")
	@NotNull
	@Getter
	@Setter
	private Boolean sellado;
	
	@Column(name = "L_EDITABLE")
	@ColumnDefault("0")
	@NotNull
	@Getter
	@Setter
	private Boolean editable;
	
	@Column(name = "L_ANONIMIZADO")
	@ColumnDefault("0")
	@NotNull
	@Getter
	@Setter
	private Boolean anonimizado;
	
	@Column(name = "L_ANONIMIZADO_PARCIAL")
	@ColumnDefault("0")
	@NotNull
	@Getter
	@Setter
	private Boolean anonimizadoParcial;

	@Column(name = "L_ACTIVO")
	@ColumnDefault("1")
	@NotNull
	@Getter
	@Setter
	private Boolean activo;	
	
	@Column(name = "T_USUARIO_ACCESO")
	@Size(max = 255)
	@Getter
	@Setter
	private String usuarioAcceso;
	
	@Column(name = "F_FECHA_HORA_ACCESO")
	@Getter
	@Setter
	private Date fechaHoraAcceso;

	
	@Column(name = "T_CODIGO")
	@Size(max = 10)
	@Getter
	@Setter
	private String tipoTramite;

	@Column(name = "L_VER_PEST_DOC")
	@NotNull
	@Getter
	@Setter
	private Boolean verEnPestDocumentos;
	
	@Column(name = "NUM_RESOL")
	@Size(max = 255)
	@Getter
	@Setter
	private String numResolucion;
	
	//-----------
	
	@Column(name = "AGR_EXP_ID")
	@Getter
	@Setter
	private Long idAgrupacionExpediente;
	
	@Column(name = "AGRUPACION_EXP")
	@Getter
	@Setter
	private String agrupacionExpediente;
	
	@Column(name = "N_ORDEN_AGR_EXP")
	@Getter
	@Setter
	private Long ordenAgrupacionExp;
	
	@Column(name = "L_VINCULADO")
	@Getter
	@Setter
	private Boolean vinculado;
	
	//-----------
	
	@Column(name = "CAT_ID")
	@Getter
	@Setter
	private Long idCategoria;	
	
	@Column(name = "CATEGORIA")
	@Getter
	@Setter
	private String categoria;
	
	@Column(name = "N_ORDEN_CAT")
	@Getter
	@Setter
	private Long ordenCategoria;	
	

	//------------------------------------

	/** true si el documento tiene tareas pendientes */
	@Transient
	public boolean getTieneTareaPendiente() {
		return null != idTarea;
	}

	public String descOrigen()	{

		String descO = "";
		if (this.origen.equals("I")) {
			descO = "Incorporado";
		}

		if (this.origen.equals("G")) {
			descO = "Generado";
		}

		if (this.origen.equals("V")) {
			descO = "Vinculado";
		}
		
		return descO;
	}
	
}
