package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.QExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.QResolucionSujetoObligado;
import es.juntadeandalucia.ctpda.gestionpdt.model.QSujetosObligados;
import es.juntadeandalucia.ctpda.gestionpdt.model.ResolucionSujetoObligado;
import es.juntadeandalucia.ctpda.gestionpdt.repository.ResolucionSujetoObligadoRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ResolucionSujetoObligadoService extends AbstractCRUDService<ResolucionSujetoObligado> {

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	private ResolucionSujetoObligadoRepository resolucionSujetoObligadoRepository;

	protected ResolucionSujetoObligadoService(@Autowired MathsQueryService mathsQueryService,
			@Autowired ResolucionSujetoObligadoRepository resolucionSujetoObligadoRepository) {
		super(mathsQueryService, resolucionSujetoObligadoRepository, QResolucionSujetoObligado.resolucionSujetoObligado);
		// también lo guardo para mi por si quiero hacer consultas personalizadas.
		this.resolucionSujetoObligadoRepository = resolucionSujetoObligadoRepository;

		this.joinBuilder = query -> {
			final QResolucionSujetoObligado qresolsujobl = QResolucionSujetoObligado.resolucionSujetoObligado;
			
			final QSujetosObligados qSujeto = new QSujetosObligados("qSujeto");
			final QExpedientes qExpediente = new QExpedientes("qExpediente");
			
			query.innerJoin(qresolsujobl.sujetoObligado, qSujeto).fetchJoin();
			query.innerJoin(qresolsujobl.expediente, qExpediente).fetchJoin();

			return query;
		};

	}

	@Override
	public void checkSiPuedoGrabar(ResolucionSujetoObligado dto) throws BaseException {
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
	
	public List<ResolucionSujetoObligado> findByResolucionId(Long idResol){
		return this.resolucionSujetoObligadoRepository.findByResolucionId(idResol);
	}
	
	public ResolucionSujetoObligado findSujetoPrincipal(Long idResolucion) {
		return this.resolucionSujetoObligadoRepository.getByResolucionIdAndPrincipalTrue(idResolucion);
	}
	
	@Transactional(TxType.REQUIRED)
	public void setPrincipal(Long idRelacion) throws BaseException {
		ResolucionSujetoObligado nuevoPrinc = this.obtener(idRelacion);
		
		Long idResol = nuevoPrinc.getResolucion().getId();
		ResolucionSujetoObligado sujPrincipal = this.findSujetoPrincipal(idResol);
		if(null != sujPrincipal) {
			sujPrincipal.setPrincipal(false);
			this.guardar(sujPrincipal);
		}
		
		//Esto hay que hacerlo después, para que Hibernate 
		//no lo incluya en la query de findExpedientePrincipal.
		nuevoPrinc.setPrincipal(true);		
		this.guardar(nuevoPrinc);
	}

}
