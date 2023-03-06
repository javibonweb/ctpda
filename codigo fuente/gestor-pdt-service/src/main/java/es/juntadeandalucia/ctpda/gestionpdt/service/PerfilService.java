package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.Perfil;
import es.juntadeandalucia.ctpda.gestionpdt.model.QPerfil;
import es.juntadeandalucia.ctpda.gestionpdt.repository.PerfilRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PerfilService extends AbstractCRUDService<Perfil> {

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	private PerfilRepository perfilRepository;

	protected PerfilService(@Autowired MathsQueryService mathsQueryService,
			@Autowired PerfilRepository perfilRepository) {
		super(mathsQueryService, perfilRepository, QPerfil.perfil);
		// también lo guardo para mi por si quiero hacer consultas personalizadas.
		this.perfilRepository = perfilRepository;
	}

	@Override
	public void checkSiPuedoGrabar(Perfil dto) throws BaseException {
		log.debug("Verifico si puedo grabar y elevo excepción sino");

	}

	@Override
	public void checkSiPuedoEliminar(Long id) throws BaseException {
		log.debug("Verifico si puedo eliminar y elevo excepción sino");

	}
	
	public List<Perfil> findPerfilesActivosAsociadosUsuario(@Param("idUsuario") Long idUsuario){
		return perfilRepository.findPerfilesActivosAsociadosUsuario(idUsuario);
	}	

	
	@Override
	protected BooleanBuilder aniadirFiltrosCustom(List<FiltroDTO> filtros) {
		final BooleanBuilder bb = new BooleanBuilder();
		if(filtros != null) {
			filtros.stream().filter(x -> x.getCampo().equals("#notID")).forEach(f -> {
				if(f.getValue() != null) {
					bb.and(QPerfil.perfil.id.ne((Long.valueOf(f.getValue().toString()))));
				}				
			});
			
			filtros.stream().filter(fx-> fx.getCampo().equals("#notIDPerfilesRelacionados")).forEach(
				fx -> {
					if (fx.getValue()!=null) {
						bb.and(QPerfil.perfil.id.notIn((List)fx.getValue()));
					}
				}
			);
		}
		return bb;
	}

}
