package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.ConexionUsuario;
import es.juntadeandalucia.ctpda.gestionpdt.model.QConexionUsuario;
import es.juntadeandalucia.ctpda.gestionpdt.repository.ConexionUsuarioRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ConexionUsuarioService extends AbstractCRUDService<ConexionUsuario> {

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	protected ConexionUsuarioService(@Autowired MathsQueryService mathsQueryService,
			@Autowired ConexionUsuarioRepository conexionUsuarioRepository) {
		super(mathsQueryService, conexionUsuarioRepository, QConexionUsuario.conexionUsuario);
	}

	@Override
	public void checkSiPuedoGrabar(ConexionUsuario dto) throws BaseException {
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
