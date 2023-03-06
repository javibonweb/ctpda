package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.QTipoDocumento;
import es.juntadeandalucia.ctpda.gestionpdt.model.TipoDocumento;
import es.juntadeandalucia.ctpda.gestionpdt.repository.TipoDocumentoRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TipoDocumentoService extends AbstractCRUDService<TipoDocumento> {

	/**
	 * Serial id
	 */
	private static final long serialVersionUID = 1L;
	
	TipoDocumentoRepository tipoDocumentoRepository;
	
	protected TipoDocumentoService (@Autowired MathsQueryService mathsQueryService,
			@Autowired TipoDocumentoRepository tipoDocumentoRepository) {
		super(mathsQueryService, tipoDocumentoRepository, QTipoDocumento.tipoDocumento);
		// Se guarda para mi por si quiero hacer consultas personalizadas.
		this.tipoDocumentoRepository = tipoDocumentoRepository;
	}

	@Override
	public void checkSiPuedoGrabar(TipoDocumento dto) throws BaseException {
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
	
	//**********************************************
	
	public List<TipoDocumento> findTiposDocumentoUsados(Long idExp) {
		return tipoDocumentoRepository.findTiposDocumentoUsados(idExp);
	}
	
	public List<TipoDocumento> findTodos() {
		return tipoDocumentoRepository.findTodos();
	}
	
}
