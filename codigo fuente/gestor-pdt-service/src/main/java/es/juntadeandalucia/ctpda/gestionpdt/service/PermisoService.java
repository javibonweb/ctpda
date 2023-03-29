package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;
import es.juntadeandalucia.ctpda.gestionpdt.model.Permiso;
import es.juntadeandalucia.ctpda.gestionpdt.model.QPermiso;
import es.juntadeandalucia.ctpda.gestionpdt.repository.PermisoRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PermisoService extends AbstractCRUDService<Permiso> {

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	protected PermisoService(@Autowired MathsQueryService mathsQueryService,
			@Autowired PermisoRepository permisoRepository) {
		super(mathsQueryService, permisoRepository, QPermiso.permiso);
	}

	@Override
	public void checkSiPuedoGrabar(Permiso dto) throws BaseException {
		log.debug("Verifico si puedo grabar y elevo excepción sino");

	}

	@Override
	public void checkSiPuedoEliminar(Long id) throws BaseException {
		log.debug("Verifico si puedo eliminar y elevo excepción sino");

	}
	

	
	@Override
	protected BooleanBuilder aniadirFiltrosCustom(List<FiltroDTO> filtros) {
		return new BooleanBuilder();
	}

}
