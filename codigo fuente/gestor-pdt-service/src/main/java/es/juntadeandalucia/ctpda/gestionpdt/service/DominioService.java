package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.Dominio;
import es.juntadeandalucia.ctpda.gestionpdt.model.QDominio;
import es.juntadeandalucia.ctpda.gestionpdt.repository.DominioRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DominioService extends AbstractCRUDService<Dominio> {

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	private DominioRepository dominioRepository;

	protected DominioService(@Autowired MathsQueryService mathsQueryService,
			@Autowired DominioRepository dominioRepository) {
		super(mathsQueryService, dominioRepository, QDominio.dominio);
		// también lo guardo para mi por si quiero hacer consultas personalizadas.
		this.dominioRepository = dominioRepository;
	}

	@Override
	public void checkSiPuedoGrabar(Dominio dto) throws BaseException {
		log.debug("Verifico si puedo grabar y elevo excepción sino");

	}

	@Override
	public void checkSiPuedoEliminar(Long id) throws BaseException {
		log.debug("Verifico si puedo eliminar y elevo excepción sino");

	}
	

	public List<String> findDescripcionDominiosPuntoMenu(){
		return dominioRepository.findDescripcionDominiosPuntoMenu();
	}
	
	public List<Dominio> findDominiosPuntoMenu(){
		return dominioRepository.findDominiosPuntoMenu();
	}	
	
	public Dominio findDominioByCodigo(String codigoDominio){
		return dominioRepository.findDominioByCodigo(codigoDominio);
	}	
	
	
	@Override
	protected BooleanBuilder aniadirFiltrosCustom(List<FiltroDTO> filtros) {
		return new BooleanBuilder();
	}

}
