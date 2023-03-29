package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.QSituacionesExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.SituacionesExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.repository.SituacionesExpedientesRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.ValidacionException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SituacionesExpedientesService extends AbstractCRUDService<SituacionesExpedientes> {
	/**
	 * Serial id
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String NOEXISTEREGISTROCONFIG = "no.existe.registro.config";
	
	private SituacionesExpedientesRepository situacionesExpedientesRepository;
	
	protected SituacionesExpedientesService(@Autowired MathsQueryService mathsQueryService,
			@Autowired SituacionesExpedientesRepository situacionesExpedientesRepository) {
		super(mathsQueryService, situacionesExpedientesRepository, QSituacionesExpedientes.situacionesExpedientes);
		// Se guarda para mi por si quiero hacer consultas personalizadas.
		this.situacionesExpedientesRepository = situacionesExpedientesRepository;
	}

	@Override
	public void checkSiPuedoGrabar(SituacionesExpedientes dto) throws BaseException {
		log.debug("Verifico si puedo grabar y elevo excepción sino");
		
	}

	@Override
	public void checkSiPuedoEliminar(Long id) throws BaseException {
		log.debug("Verifico si puedo eliminar y elevo excepción sino");
		
	}
	
	public List<SituacionesExpedientes> findSituacionesExpedientesActivos() throws ValidacionException{
																			  
  
 
		List<SituacionesExpedientes> res = situacionesExpedientesRepository.findSituacionesExpedientesActivos();
		if(res.isEmpty()) {
			throw new ValidacionException(NOEXISTEREGISTROCONFIG); 
		}		
		return res;
	}
	
	public List<SituacionesExpedientes> findSituacionesExpedientesHijosActivos(Long id) throws ValidacionException{
		List<SituacionesExpedientes> res = situacionesExpedientesRepository.findSituacionesExpedientesHijosActivos(id);
		if(res.isEmpty()) {
			throw new ValidacionException(NOEXISTEREGISTROCONFIG); 
		}		
		return res;
	}
	
	public List<SituacionesExpedientes> findSituacionesExpedientesTipoExped(Long id) throws ValidacionException{
		List<SituacionesExpedientes> res = situacionesExpedientesRepository.findSituacionesExpedientesTipoExped(id);
		if(res.isEmpty()) {
			throw new ValidacionException(NOEXISTEREGISTROCONFIG); 
		}		
		return res;
	}
	
	public List<SituacionesExpedientes> findSituacionesExpedientesRelSuperior(Long id, Long idCodRel) throws ValidacionException{
		List<SituacionesExpedientes> res = situacionesExpedientesRepository.findSituacionesExpedientesRelSuperior(id, idCodRel);
		if(res.isEmpty()) {
			throw new ValidacionException(NOEXISTEREGISTROCONFIG); 
		}		
		return res;
	}
	
	public List<SituacionesExpedientes> findSituacionesExpedientesTipExpAll(Long id) throws ValidacionException{
		List<SituacionesExpedientes> res = situacionesExpedientesRepository.findSituacionesExpedientesTipExpAll(id);
		if(res.isEmpty()) {
			throw new ValidacionException(NOEXISTEREGISTROCONFIG); 
		}		
		return res;
	}
	
	public SituacionesExpedientes findSituacionesExpedientesTipExpValorSit(Long idExp, Long idValorSit) throws ValidacionException{
		SituacionesExpedientes res = situacionesExpedientesRepository.findSituacionesExpedientesTipExpValorSit(idExp, idValorSit);
		if(res == null) {
			throw new ValidacionException(NOEXISTEREGISTROCONFIG); 
		}		
		return res;
	}
	
	public List<SituacionesExpedientes> findSituacionesExpedientesFinalizados() throws ValidacionException{
		List<SituacionesExpedientes> res = situacionesExpedientesRepository.findSituacionesExpedientesFinalizados();
		if(res.isEmpty()) {
			throw new ValidacionException(NOEXISTEREGISTROCONFIG); 
		}		
		return res;
	}
	
	public SituacionesExpedientes findSituacionesExpedientesTipExpValorSitFinal(Long idTipoExp, Long idValorSit){
		return situacionesExpedientesRepository.findSituacionesExpedientesTipExpValorSitFinal(idTipoExp, idValorSit);
	}

	/**
	 * Comprueba si la situación según el tipo de expediente es final
	 * @throws ValidacionException 
	 */
	public boolean esSituacionFinal(Long idTipoExp, Long idSitExp) throws ValidacionException {
		final SituacionesExpedientes sitExp = this.findSituacionesExpedientesTipExpValorSit(idTipoExp, idSitExp);
		return sitExp != null && Boolean.TRUE.equals(sitExp.getSituacionFinal());
	}
	
	/**
	 * Comprueba si el objeto SituacionExpediente del id es final
	 */
	public boolean esSituacionExpedienteFinal(Long idSitExp) {
		return Boolean.TRUE.equals(this.obtener(idSitExp).getSituacionFinal());
	}
	
	//-------------------
	
 
	@Override
	protected BooleanBuilder aniadirFiltrosCustom(List<FiltroDTO> filtros) {
		return new BooleanBuilder();
	}
}
