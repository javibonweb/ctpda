package es.juntadeandalucia.ctpda.gestionpdt.model.core;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class AuditorAwareImpl implements AuditorAware<String> {
@Override
  public Optional<String> getCurrentAuditor() {
    return this.getCurrentUser();
  }
  /**
   * Gets the current user.
   *
   * @return the current user
   */
  private Optional<String> getCurrentUser() {
      String userName=null;
	
	   if (SecurityContextHolder.getContext()!=null&&SecurityContextHolder.getContext().getAuthentication()!=null&&SecurityContextHolder.getContext().getAuthentication().getPrincipal()!=null) 
	   {
	       userName= SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
	   }
	   return userName!=null?Optional.of(userName):Optional.empty();
  }



}