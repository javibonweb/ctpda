package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.QResponsablesTramitacion;
import es.juntadeandalucia.ctpda.gestionpdt.model.ResponsablesTramitacion;
import es.juntadeandalucia.ctpda.gestionpdt.repository.ResponsablesTramitacionRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.ValidacionException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ResponsablesTramitacionService extends AbstractCRUDService<ResponsablesTramitacion>{
	
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String NOEXISTEREGISTROCONFIG = "no.existe.registro.config";
	
	private ResponsablesTramitacionRepository responsablesTramitacionRepository;
	
	/**
	 * Constructor que inyecta bean de calculos matematicos y el resto de beans que hacen falta
	 * */
	public ResponsablesTramitacionService(@Autowired MathsQueryService mathsQueryService, @Autowired ResponsablesTramitacionRepository responsablesTramitacionRepository){
		super(mathsQueryService,
				responsablesTramitacionRepository,
				QResponsablesTramitacion.responsablesTramitacion);
		//también lo guardo para mi por si quiero hacer consultas personalizadas.
		this.responsablesTramitacionRepository=responsablesTramitacionRepository;
	}
	

	
	@Override
	public void checkSiPuedoGrabar(ResponsablesTramitacion dto) throws BaseException {
		log.debug("Verifico si puedo grabar y elevo excepción sino");
		
	}

	@Override
	public void checkSiPuedoEliminar(Long id) throws BaseException {
		log.debug("Verifico si puedo eliminar y elevo excepción sino");		
	}
	
/**
 * Filtros custom.
 * Se pueden añadir predicados querydsl a manini, o dejar a nulo.
 * Se añade el filtro 
 * */
	@Override
	protected BooleanBuilder aniadirFiltrosCustom(List<FiltroDTO> filtros) {
		return new BooleanBuilder();
	}
	
	/**
	 * Obtiene el objeto completo (sin referencias lazy), no solo la referencia.
	 * @param id
	 * @return instancia de la clase entidad
	 */
	public ResponsablesTramitacion obtenerObjeto(Long id) {
		return this.crudRepository.findById(id).orElseThrow(() -> new javax.persistence.EntityNotFoundException(this.getClass().getName() + ".obtenerObjeto: id " + id + " no encontrado."));
	}
	
	//Solo queremos el nombre
	public String obtenerDescripcion(Long id) {
		return this.obtener(id).getDescripcion();
	}
	
	public List<ResponsablesTramitacion> findResponsablesActivos(){
		return this.responsablesTramitacionRepository.findByActivo();
	}
	
	public List<Long> findIdsEquipos(List<Long> idsResp){
		return this.responsablesTramitacionRepository.findIdsEquipos(idsResp);
	}

	public List<ResponsablesTramitacion> findEquipoResponsable(Long idResp){
		return this.responsablesTramitacionRepository.findEquipoResponsable(idResp);
	}
	
	public boolean esResponsableSuperiorDe(ResponsablesTramitacion respSup, ResponsablesTramitacion respSub) {		
		return respSup.getId().equals(respSub.getId()) 
				|| this.findIdsEquipos(List.of(respSup.getId()))
							.contains(respSub.getId());
	}

	
	public ResponsablesTramitacion findResponsablePorDefectoUsuario(Long idUsuario) {
		return this.responsablesTramitacionRepository.findResponsablePorDefectoUsuario(idUsuario);		
	}
	
	public ResponsablesTramitacion findResponsableTramitacionByCodResp(String codResp) throws ValidacionException {
		ResponsablesTramitacion responsablesTramitacion = responsablesTramitacionRepository.findResponsableTramitacionByCodResp(codResp);
		if(responsablesTramitacion == null) {
			throw new ValidacionException(NOEXISTEREGISTROCONFIG); 
		}
		return responsablesTramitacion;
	}

}
