package es.juntadeandalucia.ctpda.gestionpdt.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ColumnDefault;

import es.juntadeandalucia.ctpda.gestionpdt.model.core.Auditable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = "GE_LISTADO_MAT_TIPEXP")
public class MateriasTipoExpedienteMaestra extends Auditable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "MAT_TIPEXP_ID")
	@NotNull
	@Getter
	@Setter
	private Long id;
	
	@Column(name = "C_CODIGO")
	@Size(max = 15)
	@NotNull
	@Getter
	@Setter
	private String codigo;
	
	@Column(name = "D_DESCRIPCION")
	@Size(max = 250)
	@NotNull
	@Getter
	@Setter
	private String descripcion;
	
	@Column(name = "L_BLOQUEADO")
	@ColumnDefault("0")
	@NotNull
	@Getter
	@Setter
	private Boolean bloqueado;
	
	@Column(name = "N_NIVEL_ANIDAMIENTO")
	@Max(3)
	@NotNull
	@Getter
	@Setter
	private Long nivelAnidamiento;
	
	@Column(name = "L_ACTIVO")
	@ColumnDefault("1")
	@NotNull
	@Getter
	@Setter
	private Boolean activo;
	
	@Column(name = "N_ORDEN")
	@Max(3)
	@NotNull
	@Getter
	@Setter
	private Long orden;
	
	@Column(name = "T_ABREVIATURA")
	@Size(max = 20)
	@NotNull
	@Getter
	@Setter
	private String abreviatura;
	
	
	@OneToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "VALDOM_VALDOM_ID", foreignKey = @ForeignKey(name = "GE_VALDOM_VALDOM_FK"))
	@NotNull
	@Getter
	@Setter
	private ValoresDominio valorDominioPadre;
	
	
	@Setter
	@Column(name = "TIP_EXPE")
	private String tiposexpedientes;
	
	@Setter
	@Column(name = "TIP_EXPE_ABREV")
	private String tipExpAbrev;
	
	@Getter
	@Setter
	@Column(name = "ID_EXPE")
	private String idexpedientes;
	
	public String gettipExpAbrev() {
	    
		String formateo = tipExpAbrev; 
		if(tipExpAbrev.equals("; "))
			formateo = "";
	    
		return formateo;		
	}
	
	public String gettiposexpedientes() {
	    
		String formateo = tiposexpedientes; 
		if(tiposexpedientes.equals("; "))
			formateo = "";
	    
		return formateo;		
	}


}
