package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.ObservacionesExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.QObservacionesExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.repository.ObservacionesExpedientesRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.util.FechaUtils;
import es.juntadeandalucia.ctpda.gestionpdt.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ObservacionesExpedientesService extends AbstractCRUDService<ObservacionesExpedientes> {
	
	/**
	 * Serial id
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String COD_DOM_TIPOBS = "TIP_OBS";
	

	private ObservacionesExpedientesRepository observacionesExpedientesRepository;
	
	@Autowired
	private ValoresDominioService valoresDominioService;
	
	protected ObservacionesExpedientesService(@Autowired MathsQueryService mathsQueryService,
			@Autowired ObservacionesExpedientesRepository observacionesExpedientesRepository) {
		super(mathsQueryService, observacionesExpedientesRepository, QObservacionesExpedientes.observacionesExpedientes);
		// Se guarda para mi por si quiero hacer consultas personalizadas.
		this.observacionesExpedientesRepository = observacionesExpedientesRepository;
	}
	
	public ObservacionesExpedientes guardarObservacionesExpedientes(ObservacionesExpedientes observacionesExp, String observaciones, String tipoObservacion, Expedientes exp) throws BaseException {

		ObservacionesExpedientes obsExp = null;
		/**MODIFICACION**/
		if(observacionesExp != null && observacionesExp.getId() != null)
		{
			if(observacionesExp.getFechaEntrada() == null && (observacionesExp.getTexto() != null && !StringUtils.isBlank(observacionesExp.getTexto())))
			{
				observacionesExp.setFechaEntrada(FechaUtils.ahora());
			}
			obsExp = observacionesExp;
			obsExp.setTexto(observaciones);
		}
		/**ALTA**/
		else
		{
			obsExp = new ObservacionesExpedientes();
			obsExp.setExpediente(exp);
			obsExp.setValorTipoObservacion(valoresDominioService.findValoresDominioByCodigoDomCodValDom(COD_DOM_TIPOBS, tipoObservacion));
			if(observaciones != null && !StringUtils.isBlank(observaciones))
			{
				obsExp.setFechaEntrada(FechaUtils.ahora());		
				obsExp.setTexto(observaciones);
			}else {
				obsExp.setTexto("");
			}
		}
		
		return super.guardar(obsExp);
	}
	
	public ObservacionesExpedientes guardarObservacionesExpedientesSinFecha(ObservacionesExpedientes observacionesExp, String observaciones, String tipoObservacion, Expedientes exp) throws BaseException {

		ObservacionesExpedientes obsExp = null;
		/**MODIFICACION**/
		if(observacionesExp != null && observacionesExp.getId() != null)
		{
			obsExp = observacionesExp;
			obsExp.setTexto(observaciones);
		}
		/**ALTA**/
		else
		{
			obsExp = new ObservacionesExpedientes();
			obsExp.setExpediente(exp);
			obsExp.setValorTipoObservacion(valoresDominioService.findValoresDominioByCodigoDomCodValDom(COD_DOM_TIPOBS, tipoObservacion));
			if(observaciones != null && !StringUtils.isBlank(observaciones))
			{
				obsExp.setTexto(observaciones);
			}else {
				obsExp.setTexto("");
			}
		}
		
		return super.guardar(obsExp);
	}
	
	
	public ObservacionesExpedientes actualizarObservacionesExpedientes(String observaciones, ObservacionesExpedientes obsExp) throws BaseException {
		obsExp.setTexto(observaciones);
		return super.guardar(obsExp);
	}


	@Override
	public void checkSiPuedoGrabar(ObservacionesExpedientes dto) throws BaseException {
		log.debug("Verifico si puedo grabar y elevo excepción si no puedo.");
		
	}

	@Override
	public void checkSiPuedoEliminar(Long id) throws BaseException {
		log.debug("Verifico si puedo eliminar y elevo excepción si no puedo.");
		
	}
	
	public List<ObservacionesExpedientes> obtenerObservacionesExpedientesPorExpediente(Long id){
		return observacionesExpedientesRepository.obtenerObservacionesExpedientesPorExpediente(id);
		
	}

	@Override
	protected BooleanBuilder aniadirFiltrosCustom(List<FiltroDTO> filtros) {
		return new BooleanBuilder();
	}

}
