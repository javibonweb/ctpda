package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.DerechoReclamadoResolucion;
import es.juntadeandalucia.ctpda.gestionpdt.model.QDerechoReclamadoResolucion;
import es.juntadeandalucia.ctpda.gestionpdt.model.QValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.repository.DerechoReclamadoResolucionRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DerechoReclamadoResolucionService extends AbstractCRUDService<DerechoReclamadoResolucion> {

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	private DerechoReclamadoResolucionRepository derechoReclamadoResolucionRepository;

	protected DerechoReclamadoResolucionService(@Autowired MathsQueryService mathsQueryService,
			@Autowired DerechoReclamadoResolucionRepository derechoReclamadoResolucionRepository) {
		super(mathsQueryService, derechoReclamadoResolucionRepository, QDerechoReclamadoResolucion.derechoReclamadoResolucion);
		// también lo guardo para mi por si quiero hacer consultas personalizadas.
		this.derechoReclamadoResolucionRepository = derechoReclamadoResolucionRepository;

		this.joinBuilder = query -> {
			final QDerechoReclamadoResolucion qDerechReclamResol = QDerechoReclamadoResolucion.derechoReclamadoResolucion;

			final QValoresDominio qValDomderecho = new QValoresDominio("qValDomderecho");
			query.innerJoin(qDerechReclamResol.valoresDerReclResol, qValDomderecho).fetchJoin();

			return query;
		};

	}

	public DerechoReclamadoResolucion findDchoReclamResolucionExpByIdResolIdDchoReclam(Long idResol, Long idDchoRecl)
	{
		return derechoReclamadoResolucionRepository.findDchoReclamResolucionExpByIdResolIdDchoReclam(idResol, idDchoRecl);
	}

	
	@Override
	public void checkSiPuedoGrabar(DerechoReclamadoResolucion dto) throws BaseException {
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

	//*********************
	
	public List<DerechoReclamadoResolucion> findByResolucionId(Long idResol){
		return this.derechoReclamadoResolucionRepository.findByResolucionId(idResol);
	}

}
