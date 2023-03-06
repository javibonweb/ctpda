package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.Parametro;
import es.juntadeandalucia.ctpda.gestionpdt.model.Plantillas;
import es.juntadeandalucia.ctpda.gestionpdt.model.QPlantillas;
import es.juntadeandalucia.ctpda.gestionpdt.repository.PlantillasRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.ValidacionException;
import es.juntadeandalucia.ctpda.gestionpdt.util.FileUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PlantillasService extends AbstractCRUDService<Plantillas> {

	/**
	 * Serial id
	 */
	private static final long serialVersionUID = 1L;

	private static final String PARAM_EXTENSIONES_PERMITIDAS_PLANTILLAS = "extensionesPlantillas";

	
	@Autowired
	private ParametroService parametroService;

	
	PlantillasRepository plantillasRepository;
	
	
	protected PlantillasService (@Autowired MathsQueryService mathsQueryService,
			@Autowired PlantillasRepository plantillasRepository) {
		super(mathsQueryService, plantillasRepository, QPlantillas.plantillas);
		// Se guarda para mi por si quiero hacer consultas personalizadas.
		this.plantillasRepository = plantillasRepository;
	}

	@Override
	public void checkSiPuedoGrabar(Plantillas dto) throws BaseException {
		log.debug("Verifico si puedo grabar y elevo excepción sino");
		
		if(existeCodigo(dto.getCodigo(), dto.getId())) {
			throw new ValidacionException("Ya existe una plantilla con el mismo código.");
		}
		
	}

	@Override
	public void checkSiPuedoEliminar(Long id) throws BaseException {
		log.debug("Verifico si puedo eliminar y elevo excepción sino");
		
	}
	
	@Override
	protected BooleanBuilder aniadirFiltrosCustom(List<FiltroDTO> filtros) {
		return new BooleanBuilder();
	}
	
	//*******************************************************
	
	private boolean existeCodigo(String cod, Long id) {
		id = id == null? 0L : id;
		return this.plantillasRepository.existsByCodigoAndIdNot(cod, id);
	}

	//*******************************************************
	
	@Override
	public Plantillas guardar(Plantillas body) throws BaseException {
		if(body.getId() == null) {
			body.setActivo(true);
		}
		
		return super.guardar(body);
	}
	
	
	public void desactivarPlantilla(Long idPlantilla) throws BaseException {
		Plantillas p = this.obtener(idPlantilla);
		p.setActivo(false);
		this.guardar(p);
	}
	
	public void activarPlantilla(Long idPlantilla) throws BaseException {
		Plantillas p = this.obtener(idPlantilla);
		p.setActivo(true);
		this.guardar(p);
	}

	
	public String[] obtenerExtensionesPermitidas() {
		final Parametro p = parametroService.getByCampo(PARAM_EXTENSIONES_PERMITIDAS_PLANTILLAS);
		return FileUtils.arrayDeExtensiones(p.getValor());
	}
		
}
