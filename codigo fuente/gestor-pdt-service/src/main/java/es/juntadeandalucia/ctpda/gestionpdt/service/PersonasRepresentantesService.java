package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.Personas;
import es.juntadeandalucia.ctpda.gestionpdt.model.PersonasRepresentantes;
import es.juntadeandalucia.ctpda.gestionpdt.model.QPersonasRepresentantes;
import es.juntadeandalucia.ctpda.gestionpdt.repository.PersonasRepresentantesRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class PersonasRepresentantesService extends AbstractCRUDService<PersonasRepresentantes> {

	private static final long serialVersionUID = 1L;

	private PersonasRepresentantesRepository personasRepresentantesRepository;

	protected PersonasRepresentantesService(@Autowired MathsQueryService mathsQueryService,
			@Autowired PersonasRepresentantesRepository personasRepresentantesRepository) {
		super(mathsQueryService, personasRepresentantesRepository, QPersonasRepresentantes.personasRepresentantes);
		// también lo guardo para mi por si quiero hacer consultas personalizadas.
		this.personasRepresentantesRepository = personasRepresentantesRepository;
	}

	@Override
	public void checkSiPuedoGrabar(PersonasRepresentantes dto) throws BaseException {
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

	public List<PersonasRepresentantes> findRepresentantes(Long idExp){
		return this.personasRepresentantesRepository.findRepresentantes(idExp);
	}
	
	public List<PersonasRepresentantes> findPersonas(Long idExp){
		return this.personasRepresentantesRepository.findPersonas(idExp);
	}

	public List<Personas> findNuevosRepresentantes(Long idExp, List<Long> idsPersonasRepresentantes, String nombreRazonSocialFiltro, String nifCifFiltro){
		return this.personasRepresentantesRepository.findNuevosRepresentantes(idExp, idsPersonasRepresentantes, nombreRazonSocialFiltro, nifCifFiltro);
	}

	
	@Override
	public PersonasRepresentantes guardar(PersonasRepresentantes pr) throws BaseException {
		//Si este es el primer representante se marca como principal 
		if(0 == this.personasRepresentantesRepository.countByPersonaId(pr.getPersona().getId())) {
			pr.setPrincipal(true);
		} else if(null == pr.getPrincipal()) {
			pr.setPrincipal(false);
		}
		
		return super.guardar(pr);
	}
	
	@Transactional(TxType.REQUIRED)
	public void setRepresentantePrincipal(PersonasRepresentantes personaRepresentante) throws BaseException {
		PersonasRepresentantes perRepAux = this.obtenerRepresentantePrincipal(personaRepresentante.getPersona().getId());
		
		if(perRepAux != null) {
			perRepAux.setPrincipal(false);
			this.guardar(perRepAux);
		}
		
		personaRepresentante.setPrincipal(true);
		this.guardar(personaRepresentante);		
	}
	
	private PersonasRepresentantes obtenerRepresentantePrincipal(Long idPer) {
		return this.personasRepresentantesRepository.obtenerRepresentantePrincipal(idPer);
	}

}
