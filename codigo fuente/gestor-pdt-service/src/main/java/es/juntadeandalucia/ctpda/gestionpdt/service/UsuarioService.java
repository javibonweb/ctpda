package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.QUsuario;
import es.juntadeandalucia.ctpda.gestionpdt.model.Usuario;
import es.juntadeandalucia.ctpda.gestionpdt.repository.UsuarioRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UsuarioService extends AbstractCRUDService<Usuario> {

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	private UsuarioRepository usuarioRepository;

	protected UsuarioService(@Autowired MathsQueryService mathsQueryService,
			@Autowired UsuarioRepository usuarioRepository) {
		super(mathsQueryService, usuarioRepository, QUsuario.usuario);
		// también lo guardo para mi por si quiero hacer consultas personalizadas.
		this.usuarioRepository = usuarioRepository;
	}
	
	public Usuario nuevoUsuario() {
		Usuario u = new Usuario();
		
		u.setActiva(Boolean.TRUE);
		//No se tienen en cuenta sexo, tipo, etc.
		
		return u;
	}

	@Override
	public void checkSiPuedoGrabar(Usuario dto) throws BaseException {
		log.debug("Verifico si puedo grabar y elevo excepción sino");

	}

	@Override
	public void checkSiPuedoEliminar(Long id) throws BaseException {
		log.debug("Verifico si puedo eliminar y elevo excepción sino");

	}
	
	public Usuario findUsuarioActivo(String usuario, String clave){
		return usuarioRepository.findUsuarioActivo(usuario, clave);
	}
	
	public List<Usuario> findFirmantesActivos(){
		return usuarioRepository.findFirmantesActivos();
	}
	
	
	public Usuario findByLogin(String usuario){
		return usuarioRepository.findByLogin(usuario);
	}
	
	public Usuario findUsuarioLogado(){
		return this.findByLogin(loginUsuarioLogado());
	}
	
	public String loginUsuarioLogado(){
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
		
	
	@Override
	protected BooleanBuilder aniadirFiltrosCustom(List<FiltroDTO> filtros) {
		return new BooleanBuilder();
	}

}
