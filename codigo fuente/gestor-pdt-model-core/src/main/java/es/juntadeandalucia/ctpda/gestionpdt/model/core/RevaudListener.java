package es.juntadeandalucia.ctpda.gestionpdt.model.core;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.context.SecurityContextHolder;

import es.juntadeandalucia.ctpda.gestionpdt.util.FechaUtils;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RevaudListener implements RevisionListener{
 
	
	/** The no user. */
	private static String noUser = "inicio-usuarioSinLogar";
	private static String anonymousUser = "anonymousUser";

	
	@Override
	public void newRevision(Object revisionEntity) {
		String name = getCurrentUser();
		Revaud revEntity = (Revaud) revisionEntity;
		String usuarioModificacion = !Objects.equals(name, anonymousUser) ? name : noUser;
		
		revEntity.setFechaModificacion(FechaUtils.fechaYHoraActualDate());
		revEntity.setUsuModificacion(usuarioModificacion);
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
