package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;

import es.juntadeandalucia.ctpda.gestionpdt.model.AgrupacionesExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentosExpedientesTramites;
import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.QAgrupacionesExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.QValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.model.TipoTramite;
import es.juntadeandalucia.ctpda.gestionpdt.model.TramiteExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.repository.AgrupacionesExpedientesRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.util.FechaUtils;
import es.juntadeandalucia.ctpda.gestionpdt.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AgrupacionesExpedientesService extends AbstractCRUDService<AgrupacionesExpedientes> {

	private static final long serialVersionUID = 1L;
	

	@Autowired
	private transient ApplicationContext appContext;

	@Autowired
	private TramiteExpedienteService tramiteExpedienteService;
	
	@Autowired
	private ValoresDominioService valoresDominioService;

	
	AgrupacionesExpedientesRepository agrupacionesExpedientesRepository;
	
	
	protected AgrupacionesExpedientesService (@Autowired MathsQueryService mathsQueryService,
			@Autowired AgrupacionesExpedientesRepository agrupacionesExpedientesRepository) {
		super(mathsQueryService, agrupacionesExpedientesRepository, QAgrupacionesExpedientes.agrupacionesExpedientes);
		// Se guarda para mi por si quiero hacer consultas personalizadas.
		this.agrupacionesExpedientesRepository = agrupacionesExpedientesRepository;
		
		this.joinBuilder = query -> {
			final QAgrupacionesExpedientes qAgr = QAgrupacionesExpedientes.agrupacionesExpedientes;
			final QValoresDominio qCat = new QValoresDominio("qCat");

			query.innerJoin(qAgr.categoria, qCat).fetchJoin();
			
			return query;
		};

	}

	@Override
	public void checkSiPuedoGrabar(AgrupacionesExpedientes dto) throws BaseException {
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
	
	private BooleanExpression criterioAgrupacionExpTramCat(
			Long idTram, Long idCat) {
		final QAgrupacionesExpedientes qAgr = QAgrupacionesExpedientes.agrupacionesExpedientes;
		final BooleanExpression bCat = qAgr.categoria.id.eq(idCat);
		final BooleanExpression bTram = qAgr.tramiteExpediente.id.eq(idTram);		
		final BooleanExpression bActiva = qAgr.activo.isTrue();
		
		return //bExp.and
				(bTram).and(bCat).and(bActiva);
	}
	
	//***********************************************
		
	public List<AgrupacionesExpedientes> findByExpedienteYCategoria(Long idExp, Long idCat){
		return this.agrupacionesExpedientesRepository.findByExpedienteIdAndCategoriaId(idExp, idCat);
	}
	
	public List<AgrupacionesExpedientes> findByExpediente(Long idExp){
		return this.agrupacionesExpedientesRepository.findByExpedienteId(idExp);
	}
	
	public List<AgrupacionesExpedientes> findByExpedienteDeExpediente(Long idExp){
		return this.agrupacionesExpedientesRepository.findByExpedienteIdDeExpediente(idExp);
	}

	
	public AgrupacionesExpedientes findAgrupacion(Long idTram, Long idCat) {
		//Por problemas de versiones de QueryDsl fetch/fecthOne da error.
		//Obtenemos el resultado único de forma alternativa.
		//¡OJO! obviamos lanzar excepión si hay más de un resultado.
//		final QAgrupacionesExpedientes qAgr = QAgrupacionesExpedientes.agrupacionesExpedientes
//		return JPAExpressions
//					.select(qAgr)
//					.from(qAgr)
//					.where(criterioAgrupacionExpTramCat(idExp, idTram, idCat))
//				.fetchOne()
		return this.crudRepository.findOne(criterioAgrupacionExpTramCat(idTram, idCat)).orElse(null);
	}
	
	public AgrupacionesExpedientes findAgrupacion(DocumentosExpedientesTramites det) {		
		return this.findAgrupacion(
						det.getTramiteExpediente().getId(), 
						det.getDocumentoExpediente().getCategoria().getId());
	}
	
	//Buscar las categorías asociadas a agrupaciones que pertenezcan al trámite
	public List<ValoresDominio> findCategoriasConAgrupacionDeTramite(Long idTramite){
		final QAgrupacionesExpedientes qAgr = QAgrupacionesExpedientes.agrupacionesExpedientes;
		final QValoresDominio qCat = QValoresDominio.valoresDominio;
		
		final BooleanBuilder bb = new BooleanBuilder();

		bb.and( qCat.id.in(JPAExpressions
								.select(qAgr.categoria.id).distinct()
								.from(qAgr)
					            .where(	qAgr.tramiteExpediente.id.eq(idTramite)	)	));
		bb.and(qCat.activo.isTrue());
		
		return (List<ValoresDominio>)this.valoresDominioService.findAllRepository(bb, Sort.by(qCat.orden.getMetadata().getName()));		
	}
	
	//***************************************************
	
	public AgrupacionesExpedientes nuevaAgrupacion(DocumentosExpedientesTramites det) {
		TramiteExpediente tram = det.getTramiteExpediente();
		ValoresDominio cat = det.getDocumentoExpediente().getCategoria();

		return this.nuevaAgrupacion(tram, cat);
	}
	
	private AgrupacionesExpedientes nuevaAgrupacion(TramiteExpediente tram, ValoresDominio categoria) {
		AgrupacionesExpedientes agr = new AgrupacionesExpedientes();
	
		agr.setActivo(true);
		agr.setExpediente(tram.getExpediente());
		agr.setTramiteExpediente(tram);
		agr.setCategoria(categoria);
		
		agr.setDescripcion(obtenerDescripcion(tram));
		agr.setDescripcionAbrev(tram.getDescripcionAbrev());

		agr.setVerPestanaDoc(visibilidadAgrupacion(tram));
		
		//El orden lo aplicamos al final, en el guardado	
		return agr;
	}
	
	private String obtenerDescripcion(TramiteExpediente tram) {
		final boolean finalizado = Boolean.TRUE.equals(tram.getFinalizado());
		
		final String descTramite = tram.getDescripcion();
		final String fechaIni = FechaUtils.dateToStringFecha(tram.getFechaIni());
		final String fechaFin = finalizado? " - " + FechaUtils.dateToStringFecha(tram.getFechaFin())
									: StringUtils.EMPTY;
		
		return descTramite + " (" + fechaIni + fechaFin + ")";
	}
	
	private boolean visibilidadAgrupacion(TramiteExpediente tram) {
		TipoTramite tipoTr = tram.getTipoTramite();
		boolean visible = false;
		
		if(tram.getEsSubtramite()) {
			TramiteExpediente sup = tram.getTramiteExpedienteSup();
			visible = visibilidadAgrupacion(sup);
			
			if(visible) { //si el trámite superior no me limita uso el indicador propio
				visible = tipoTr.getVerDocumentosSubtramite(); 
			}
		} else {
			visible = tipoTr.getVerDocumentosTramite();
		}

		return visible;
	}
		
	private AgrupacionesExpedientes nuevaAgrupacionTramiteSup(AgrupacionesExpedientes agr) {		
		TramiteExpediente tram = agr.getTramiteExpediente().getTramiteExpedienteSup();
		ValoresDominio categoria = agr.getCategoria();
		
		return nuevaAgrupacion(tram, categoria);
	}
		
	@Override	
	@Transactional(value=TxType.REQUIRED, rollbackOn = Exception.class)
	public AgrupacionesExpedientes guardar(AgrupacionesExpedientes agr) throws BaseException {
		AgrupacionesExpedientes agrTrSup = guardarAgrupacionTramiteSup(agr);
		aplicarOrden(agr, agrTrSup);
		
		return super.guardar(agr);
	}
	
	private AgrupacionesExpedientes guardarAgrupacionTramiteSup(AgrupacionesExpedientes agr) throws BaseException {
		boolean esAlta = null == agr.getId();
		AgrupacionesExpedientes agrTrSup = findAgrupacionDeTramiteSup(agr);
		
		if(esAlta) {
			TramiteExpediente tram = agr.getTramiteExpediente();
			//Hay que acomodar la agrupación dentro de los existentes
			if(tram.getEsSubtramite() && agrTrSup == null) {
				agrTrSup = this.guardar(nuevaAgrupacionTramiteSup(agr));
			}
		}
		
		return agrTrSup;
	}

	private void aplicarOrden(AgrupacionesExpedientes agr, AgrupacionesExpedientes agrTrSup) throws BaseException {
		if(!agr.cambiaCategoria()) return; //si es alta cambiaCategoria() debe dar true --> no return
		
		boolean esAlta = null == agr.getId();
		Expedientes exp = agr.getExpediente();
		TramiteExpediente tram = agr.getTramiteExpediente();

		//Aplicar el orden en función de la categoría
		//Ya sea alta o edición en este punto la agrupación de trámite superior existe
		//La comprobación de agrTrSup es redundante, está para que no salte Sonar	
		if(tram.getEsSubtramite() && agrTrSup != null) {
			Long ordenTram = agrTrSup.getOrden();
			Long ordenSubTr = this.getUltimoOrdenSubtramite(tram.getId(), agr.getCategoria().getId());
			
			if(ordenSubTr == 0) {
				ordenSubTr = ordenTram;
			}
								
			List<AgrupacionesExpedientes> list = findDesdeOrden(agrTrSup.getExpediente().getId(), agrTrSup.getCategoria().getId(), ordenSubTr);
			
			//La lista empieza por el primer orden > ordenSubTr
			for(AgrupacionesExpedientes a : list) {
				a.setOrden(1 + a.getOrden());
				this.guardar(a);
			}
			
			agr.setOrden(1 + ordenSubTr);
		} else {
			agr.setOrden(1+this.getUltimoOrden(exp.getId(), agr.getCategoria().getId()));
			
			if(!esAlta) {
				//La agrupación ya tenía una categoría
				//¿resetear el hueco de la categoría anterior?
			}
		}
	}
	
	public List<AgrupacionesExpedientes> findDesdeOrden(Long idExp, Long idCat, Long orden) {
		return this.agrupacionesExpedientesRepository.findDesdeOrden(idExp, idCat, orden);
	}

	//--------------------------------------------
	
	public Long getUltimoOrden(Long idExp, Long idCat) {
		return this.agrupacionesExpedientesRepository.getUltimoOrden(idExp, idCat);
	}
	
	private Long getUltimoOrdenSubtramite(Long idSubtram, Long idCat) {
		TramiteExpediente subTram = tramiteExpedienteService.obtener(idSubtram);
		return this.agrupacionesExpedientesRepository.getUltimoOrdenTramiteSup(subTram.getTramiteExpedienteSup().getId(), idCat);
	}
	
	private AgrupacionesExpedientes findAgrupacionDeTramiteSup(AgrupacionesExpedientes agr) {
		TramiteExpediente tram = agr.getTramiteExpediente();
		return !tram.getEsSubtramite()? null : this.findAgrupacion(tram.getTramiteExpedienteSup().getId(), agr.getCategoria().getId());
	}
	
	public List<AgrupacionesExpedientes> findByTramite(Long idTramite) {
		return this.agrupacionesExpedientesRepository.findByTramiteExpedienteId(idTramite);
	}
		
	public AgrupacionesExpedientes findByTramiteyCat(Long idTramite, Long idCat) {
		return this.agrupacionesExpedientesRepository.findByTramiteExpedienteIdyCat(idTramite, idCat);
	}

	
	//**************************************************************************************
	
	@Transactional(value=TxType.REQUIRED, rollbackOn = Exception.class)
	boolean cambioDeCategoria(Long idAgrExp, Long idCategoria) throws BaseException {
		boolean cambio = false;
		final AgrupacionesExpedientes agrExp = this.obtener(idAgrExp);
		
		agrExp.setCategoria(valoresDominioService.obtener(idCategoria));
		
		if(agrExp.cambiaCategoria()) {
			cambio = true;
			this.guardar(agrExp);
		}
		
		return cambio;
	}

	@Transactional(value=TxType.REQUIRED, rollbackOn = Exception.class)
	public void cambiarDescripciones(TramiteExpediente tram) throws BaseException {
		final List<AgrupacionesExpedientes> lista = this.findByTramite(tram.getId());
		
		for(AgrupacionesExpedientes agr : lista) {
			agr.setDescripcion(obtenerDescripcion(tram));
			this.guardar(agr);
		}
	}
	
	//Borra la agrupación exp. si no tiene docs (mirando la tabla de AgrupacionesDocumentos). 
	//  Si no, no hace nada.
	public void eliminarAgrupacionSinDocumentos(Long idAgr) throws BaseException {
		final AgrupacionesDocumentosService agrDocSvc = (AgrupacionesDocumentosService) appContext.getBean("agrupacionesDocumentosService");

		//¿Existe algún agrupacionDocumentos relacionado con idAgr
		if(!agrDocSvc.existsByAgrupacionExp(idAgr)) {
			this.delete(idAgr);
		}		
	}
}

