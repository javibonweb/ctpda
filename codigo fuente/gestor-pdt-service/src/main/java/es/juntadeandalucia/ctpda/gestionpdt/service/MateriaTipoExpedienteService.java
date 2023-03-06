package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.MateriaTipoExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.QMateriaTipoExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.repository.MateriaTipoExpedienteRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MateriaTipoExpedienteService extends AbstractCRUDService< MateriaTipoExpediente>{
	
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	private MateriaTipoExpedienteRepository materiaTipoExpedienteRepository;
	
	/**
	 * Constructor que inyecta bean de calculos matematicos y el resto de beans que hacen falta
	 * */
	public MateriaTipoExpedienteService(@Autowired MathsQueryService mathsQueryService, @Autowired MateriaTipoExpedienteRepository materiaTipoExpedienteRepository){
		super(mathsQueryService,
				materiaTipoExpedienteRepository,
				QMateriaTipoExpediente.materiaTipoExpediente);
		//también lo guardo para mi por si quiero hacer consultas personalizadas.
		this.materiaTipoExpedienteRepository=materiaTipoExpedienteRepository;
	}
	
	public String[] findValoresDominioTiposExpedientes(@Param("idValorDominio") Long idValorDominio) {
		return materiaTipoExpedienteRepository.findValoresDominioTiposExpedientes(idValorDominio);
	}
	
	public String[] findValoresDominioTiposExpedientesDesc(@Param("idValorDominio") Long idValorDominio) {
		return materiaTipoExpedienteRepository.findValoresDominioTiposExpedientesDesc(idValorDominio);
	}
	
	public List<ValoresDominio> findValoresDominioTipExp(@Param("idValorDominio") Long idValorDominio) {
		return materiaTipoExpedienteRepository.findValoresDominioTipExp(idValorDominio);
	}
	
	public List<MateriaTipoExpediente> findMateriasTipoExpedienteIdMateria(@Param("idValorDominio") Long idValorDominio) {
		return materiaTipoExpedienteRepository.findMateriasTipoExpedienteIdMateria(idValorDominio);
	}
	
	public MateriaTipoExpediente findMateriasTipoExpedienteIdMateria(@Param("idValDomMateria") Long idValDomMateria, @Param("idValDomTipExp") Long idValDomTipExp) {
		return materiaTipoExpedienteRepository.findMateriasTipoExpedienteIdMateria(idValDomMateria, idValDomTipExp);
	}
	
	public List<MateriaTipoExpediente> findValoresDominioMateriaByValorDominioTipoExpediente(@Param("idValorDominioTipoExpediente") Long idValorDominioTipoExpediente) {
		return materiaTipoExpedienteRepository.findValoresDominioMateriaByValorDominioTipoExpediente(idValorDominioTipoExpediente);
	}
	
	
	@Override
	public void checkSiPuedoGrabar(MateriaTipoExpediente dto) throws BaseException {
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

}
