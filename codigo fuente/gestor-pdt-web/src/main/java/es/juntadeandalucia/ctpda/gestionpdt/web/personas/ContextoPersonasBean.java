package es.juntadeandalucia.ctpda.gestionpdt.web.personas;

import java.io.Serializable;

import javax.faces.view.ViewScoped;

import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.Personas;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@ViewScoped
public class ContextoPersonasBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Getter
	@Setter
	private Personas personas;
	
	@Getter
	@Setter
	private String cifNifRepresentante;


}
