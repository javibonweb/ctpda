package es.juntadeandalucia.ctpda.gestionpdt.web.usuarios;

import java.io.Serializable;

import javax.faces.view.ViewScoped;

import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.Usuario;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
@Component
@Slf4j
@ViewScoped
public class ContextoUsuariosBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Getter
	@Setter
	private Usuario usuario;

}
