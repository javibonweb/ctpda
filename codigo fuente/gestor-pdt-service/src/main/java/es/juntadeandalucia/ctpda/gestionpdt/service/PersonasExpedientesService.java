package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.Personas;
import es.juntadeandalucia.ctpda.gestionpdt.model.PersonasExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.QPersonasExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.repository.PersonasExpedientesRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PersonasExpedientesService extends AbstractCRUDService<PersonasExpedientes> {
	
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	private PersonasExpedientesRepository personasExpedientesRepository;
	
	protected PersonasExpedientesService(@Autowired MathsQueryService mathsQueryService,
			@Autowired PersonasExpedientesRepository personasExpedientesRepository) {
		super(mathsQueryService, personasExpedientesRepository, QPersonasExpedientes.personasExpedientes);
		// también lo guardo para mi por si quiero hacer consultas personalizadas.
		this.personasExpedientesRepository = personasExpedientesRepository;
	}

	@Override
	public void checkSiPuedoGrabar(PersonasExpedientes dto) throws BaseException {
		log.debug("Verifico si puedo grabar y elevo excepción sino");

	}

	@Override
	public void checkSiPuedoEliminar(Long id) throws BaseException {
		log.debug("Verifico si puedo eliminar y elevo excepción sino");

	}
	
	public List<PersonasExpedientes> obtenerListaPersonaExpedientePorExpediente(Long id) {
		return personasExpedientesRepository.obtenerListaPersonaExpedientePorExpediente(id);
	}
	public PersonasExpedientes obtenerPersonaExpedientePorExpediente(Long id) {
		return personasExpedientesRepository.obtenerPersonaExpedientePorExpediente(id);
	}
	
	public PersonasExpedientes obtenerPersonaExpedientePrincipalPorExpediente(Long id) {
		return personasExpedientesRepository.obtenerPersonaExpedientePrincipalPorExpediente(id);
	}
	public List<PersonasExpedientes> obtenerListaPersonaExpedientePorExpedientePrincipalYNoPrincipal(Long id){
		return personasExpedientesRepository.obtenerListaPersonaExpedientePorExpedientePrincipalYNoPrincipal(id);
	}
	public PersonasExpedientes obtenerPersonaRepreExpedientePorExpediente(Long idPersona) {
		return personasExpedientesRepository.obtenerPersonaRepreExpedientePorExpediente(idPersona);
	}

	
	public List<Personas> obtenerPersYPersReprePorExpediente(long idExp){
		return personasExpedientesRepository.obtenerPersYPersReprePorExpediente(idExp);
	}
	
	public List<PersonasExpedientes> obtenerPersPorExpediente(long idExp){
		return personasExpedientesRepository.obtenerPersPorExpediente(idExp);
	}
	

	@Override
	protected BooleanBuilder aniadirFiltrosCustom(List<FiltroDTO> filtros) {
		return new BooleanBuilder();
	}

}
