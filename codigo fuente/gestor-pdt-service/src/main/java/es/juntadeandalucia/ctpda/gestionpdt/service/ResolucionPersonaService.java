package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.QExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.QPersonas;
import es.juntadeandalucia.ctpda.gestionpdt.model.QResolucionPersona;
import es.juntadeandalucia.ctpda.gestionpdt.model.ResolucionPersona;
import es.juntadeandalucia.ctpda.gestionpdt.repository.ResolucionPersonaRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ResolucionPersonaService extends AbstractCRUDService<ResolucionPersona> {

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	private ResolucionPersonaRepository resolucionPersonaRepository;

	protected ResolucionPersonaService(@Autowired MathsQueryService mathsQueryService,
			@Autowired ResolucionPersonaRepository resolucionPersonaRepository) {
		super(mathsQueryService, resolucionPersonaRepository, QResolucionPersona.resolucionPersona);
		// también lo guardo para mi por si quiero hacer consultas personalizadas.
		this.resolucionPersonaRepository = resolucionPersonaRepository;

		this.joinBuilder = query -> {
			final QResolucionPersona qresolpersona = QResolucionPersona.resolucionPersona;
			
			final QPersonas qPersonas = new QPersonas("qPersonas");
			final QExpedientes qExpediente = new QExpedientes("qExpediente");
			
			query.innerJoin(qresolpersona.persona, qPersonas).fetchJoin();
			query.innerJoin(qresolpersona.expediente, qExpediente).fetchJoin();

			return query;
		};

	}

	@Override
	public void checkSiPuedoGrabar(ResolucionPersona dto) throws BaseException {
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

	//****************************
	
	public List<ResolucionPersona> findByResolucionId(Long idResol){
		return this.resolucionPersonaRepository.findByResolucionId(idResol);
	}
	
	public ResolucionPersona findExpedientePrincipal(Long idResolucion) {
		return this.resolucionPersonaRepository.getByResolucionIdAndPrincipalTrue(idResolucion);
	}
	
	@Transactional(TxType.REQUIRED)
	public void setPrincipal(Long idRelacion) throws BaseException {
		ResolucionPersona nuevoPrinc = this.obtener(idRelacion);
		
		Long idResol = nuevoPrinc.getResolucion().getId();
		ResolucionPersona persPrincipal = this.findExpedientePrincipal(idResol);
		if(null != persPrincipal) {
			persPrincipal.setPrincipal(false);
			this.guardar(persPrincipal);
		}
		
		//Esto hay que hacerlo después, para que Hibernate 
		//no lo incluya en la query de findExpedientePrincipal.
		nuevoPrinc.setPrincipal(true);		
		this.guardar(nuevoPrinc);
	}

}
