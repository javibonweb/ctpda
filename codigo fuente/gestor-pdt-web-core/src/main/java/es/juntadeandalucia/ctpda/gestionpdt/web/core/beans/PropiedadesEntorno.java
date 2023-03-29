	package es.juntadeandalucia.ctpda.gestionpdt.web.core.beans;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@ApplicationScope
@Slf4j
/**
 * Clase para modificar el entorno.
 * */
public class PropiedadesEntorno implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PropiedadesEntorno() {
		log.debug("Iniciando nuevo PropiedadesEntorno");
	}
	
	@Getter
	@Setter
	@Value("${ctpda.gestorpdt.entorno}")
	private String entorno;
	
	@Getter
	@Setter
	@Value("${ctpda.gestorpdt.version}")
	private String version;
	
	@Getter
	@Setter
	@Value("${ctpda.gestorpdt.tiempo.inactividad}")
	private String tiempoInactividad;
	


}
