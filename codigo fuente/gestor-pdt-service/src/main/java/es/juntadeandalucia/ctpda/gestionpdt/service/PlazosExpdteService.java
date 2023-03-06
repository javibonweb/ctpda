package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import com.querydsl.core.types.dsl.BooleanExpression;

import es.juntadeandalucia.ctpda.gestionpdt.model.PlazosExpdte;
import es.juntadeandalucia.ctpda.gestionpdt.model.QPlazosExpdte;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.repository.PlazosExpdteRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PlazosExpdteService extends AbstractCRUDService<PlazosExpdte> {

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	private PlazosExpdteRepository plazosExpdteRepository;
	
	@Autowired
	private ValoresDominioService valoresDominioService;


	protected PlazosExpdteService(@Autowired MathsQueryService mathsQueryService,
			@Autowired PlazosExpdteRepository plazosExpdteRepository) {
		super(mathsQueryService, plazosExpdteRepository, QPlazosExpdte.plazosExpdte);
		// también lo guardo para mi por si quiero hacer consultas personalizadas.
		this.plazosExpdteRepository = plazosExpdteRepository;
	}

	@Override
	public void checkSiPuedoGrabar(PlazosExpdte dto) throws BaseException {
		log.debug("Verifico si puedo grabar y elevo excepción sino");

	}

	@Override
	public void checkSiPuedoEliminar(Long id) throws BaseException {
		log.debug("Verifico si puedo eliminar y elevo excepción sino");

	}
	
	public ValoresDominio findTipoPlazo(String codigo) {
		return valoresDominioService.findValoresDominioByCodigoDomCodValDom(
				"TIP_PLA", codigo);
	}
	
	public PlazosExpdte findPlazosExpdteByExp(Long idExp){
		
		return plazosExpdteRepository.findPlazosExpdteByExp(idExp);
	}	
	
	public PlazosExpdte findPlazosExpdteByExpTipPla(Long idExp, Long idTipoPlazo){
		
		return plazosExpdteRepository.findPlazosExpdteByExpTipPla(idExp, idTipoPlazo);
	}	
	
	public List<PlazosExpdte> plazosExpdteActivosNoCumplidosByExpediente(Long idExp) {
		return plazosExpdteRepository.plazosExpdteActivosNoCumplidosByExpediente(idExp);
	}
	
	public PlazosExpdte findPlazosExpdteActivosNoCumplidosByExpTipPla(Long idExp, Long idTipoPlazo) {
		return plazosExpdteRepository.findPlazosExpdteActivosNoCumplidosByExpTipPla(idExp, idTipoPlazo);
	}
	
	@Override
	protected BooleanBuilder aniadirFiltrosCustom(List<FiltroDTO> filtros) {
		final BooleanBuilder bb = new BooleanBuilder();
		
		filtros.stream().filter(fx-> fx.getCampo().equals("#fecha_limite")).forEach(
			fx -> {
				if (fx.getValue()!=null) {
					BooleanExpression fechaEq = QPlazosExpdte.plazosExpdte.fechaLimite.eq((Date) fx.getValue());
					BooleanExpression fechaBef = QPlazosExpdte.plazosExpdte.fechaLimite.before((Date) fx.getValue());
					
					bb.or(fechaEq);
					bb.or(fechaBef);
				}
			}
		);
		
		return bb;
	}
	
	public List<PlazosExpdte> findPlazosExpdteActivosYCumplidosByExpediente(Long idExpediente) {
		return plazosExpdteRepository.findPlazosExpdteActivosYCumplidosByExpediente(idExpediente);
	}

}
