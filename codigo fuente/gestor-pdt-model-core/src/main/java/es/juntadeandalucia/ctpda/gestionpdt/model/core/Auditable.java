package es.juntadeandalucia.ctpda.gestionpdt.model.core;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.Size;

import org.springframework.security.core.context.SecurityContextHolder;

import es.juntadeandalucia.ctpda.gestionpdt.util.FechaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


/**
 * Modelo abstracto para aglutinar los elementos de filas y columnas de campos auditables. 
 * */

/**
 * Instantiates a new auditable.
 */
@Data

/**
 * Hash code.
 *
 * @return the int
 */
@MappedSuperclass
@Slf4j
public abstract class Auditable implements  EntidadBasica,Serializable{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The no user. */
	private static String noUser = "-";
	  
	/** The fecha creacion. */
	@Column (name="F_CREACION")
	private Date fechaCreacion;
	
	/** The fecha modificacion. */
	@Column (name="F_MODIFICACION")
	private Date fechaModificacion;
	
	/** The usu creacion. */
	@Column (name="USU_CREACION")
	@Size(max=25)
	private String usuCreacion;
	
	/** The usu modificacion. */
	@Column (name="USU_MODIFICACION")
	@Size(max=25)
	private String usuModificacion;
	
	
	  /**
	   * Pre persist.
	   */
	  @PrePersist
	  public void prePersist() {
	    fechaCreacion = FechaUtils.fechaYHoraActualDate();
	    String name = getCurrentUser();
	    usuCreacion = name != null ? name : noUser;
	  }

	  /**
	   * Pre update.
	   */
	  @PreUpdate
	  public void preUpdate() {
	    fechaModificacion = FechaUtils.fechaYHoraActualDate();
	    String name = getCurrentUser();
	    usuModificacion = name != null ? name : noUser;

	  }
	  
	  public void resetAuditables() {
		  fechaCreacion = fechaModificacion = null;
		  usuCreacion = usuModificacion = null;
	  }

	  /**
	   * Gets the current user.
	   * Se va a utilizar la credencial principal, que será seteada con el codigo del sistema desde los filtros de spring security.
	   * @return the current user
	   */
	  private String getCurrentUser() {
		  try {
			  return SecurityContextHolder.getContext().getAuthentication().getName();
		  }catch (NullPointerException e){
			  log.warn("Fallo detectando sistema activo "+e.getLocalizedMessage());
			  return "-";
			  //asumimos que si no hay usuario ha sido un cron o similar, y dejamos un guion
		  }
		 
	  }
	
}
