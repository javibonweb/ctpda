package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.QUsuarioPerfil;
import es.juntadeandalucia.ctpda.gestionpdt.model.UsuarioPerfil;
import es.juntadeandalucia.ctpda.gestionpdt.repository.UsuarioPerfilRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UsuarioPerfilService extends AbstractCRUDService<UsuarioPerfil> {

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	private UsuarioPerfilRepository usuarioPerfilRepository;

	protected UsuarioPerfilService(@Autowired MathsQueryService mathsQueryService,
			@Autowired UsuarioPerfilRepository usuarioPerfilRepository) {
		super(mathsQueryService, usuarioPerfilRepository, QUsuarioPerfil.usuarioPerfil);
		// también lo guardo para mi por si quiero hacer consultas personalizadas.
		this.usuarioPerfilRepository = usuarioPerfilRepository;
	}

	@Override
	public void checkSiPuedoGrabar(UsuarioPerfil dto) throws BaseException {
		log.debug("Verifico si puedo grabar y elevo excepción sino");

	}

	@Override
	public void checkSiPuedoEliminar(Long id) throws BaseException {
		log.debug("Verifico si puedo eliminar y elevo excepción sino");

	}
	
	public List<UsuarioPerfil> findPerfilesUsuarioActivos(Long idUsuario){
		return usuarioPerfilRepository.findPerfilesUsuarioActivos(idUsuario);
	}		
	
	
//	@Cacheable("permiso")
	public String findPermisoRequerido(@Param("idUsuario") Long idUsuario, @Param("codigoPerfil") String codigoPerfil, @Param("codigoPermiso") String codigoPermiso) {
		return usuarioPerfilRepository.findPermisoRequerido(idUsuario, codigoPerfil, codigoPermiso);
	}
	
	@Override
	protected BooleanBuilder aniadirFiltrosCustom(List<FiltroDTO> filtros) {
		return new BooleanBuilder();
	}
	
	public List<Long> findPerfilesUsuarioActivosPorUsuario(Long idUsuario){
		return usuarioPerfilRepository.findPerfilesUsuarioActivosPorUsuario(idUsuario);
		
	}
	
	public List<UsuarioPerfil> findListaPerfilesUsuarioActivosPorUsuario(Long idUsuario){
		return usuarioPerfilRepository.findListaPerfilesUsuarioActivosPorUsuario(idUsuario);
	}
	
	public UsuarioPerfil findPerfil(Long idUsuario, Long idPerfil) {
		return usuarioPerfilRepository.findPerfil(idUsuario, idPerfil);
	}

}
