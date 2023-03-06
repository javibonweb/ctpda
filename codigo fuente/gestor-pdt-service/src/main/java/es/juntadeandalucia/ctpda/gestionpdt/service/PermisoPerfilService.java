package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.Permiso;
import es.juntadeandalucia.ctpda.gestionpdt.model.PermisoPerfil;
import es.juntadeandalucia.ctpda.gestionpdt.model.QPermisoPerfil;
import es.juntadeandalucia.ctpda.gestionpdt.repository.PermisoPerfilRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PermisoPerfilService extends AbstractCRUDService<PermisoPerfil> {

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	private PermisoPerfilRepository permisoPerfilRepository;

	protected PermisoPerfilService(@Autowired MathsQueryService mathsQueryService,
			@Autowired PermisoPerfilRepository permisoPerfilRepository) {
		super(mathsQueryService, permisoPerfilRepository, QPermisoPerfil.permisoPerfil);
		// también lo guardo para mi por si quiero hacer consultas personalizadas.
		this.permisoPerfilRepository = permisoPerfilRepository;
	}

	@Override
	public void checkSiPuedoGrabar(PermisoPerfil dto) throws BaseException {
		log.debug("Verifico si puedo grabar y elevo excepción sino");

	}

	@Override
	public void checkSiPuedoEliminar(Long id) throws BaseException {
		log.debug("Verifico si puedo eliminar y elevo excepción sino");

	}
	
//	@Cacheable("permisos")
	public List<Permiso> findPermisosActivosAsociadosPerfil(@Param("idPerfil") Long idPerfil){
		return permisoPerfilRepository.findPermisosActivosAsociadosPerfil(idPerfil);
	}	
	
//	@Cacheable("codigoPermisos")
	public List<String> findCodPermisosActivosAsociadosPerfil(@Param("idPerfil") Long idPerfil){
		return permisoPerfilRepository.findCodPermisosActivosAsociadosPerfil(idPerfil);
	}	
	
	@Override
	protected BooleanBuilder aniadirFiltrosCustom(List<FiltroDTO> filtros) {
		return new BooleanBuilder();
	}

}
