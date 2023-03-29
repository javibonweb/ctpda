package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;

import es.juntadeandalucia.ctpda.gestionpdt.model.CfgTareas;
import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentosExpedientesTramites;
import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.ObservacionesExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.PlazosExpdte;
import es.juntadeandalucia.ctpda.gestionpdt.model.QExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.QResponsablesTramitacion;
import es.juntadeandalucia.ctpda.gestionpdt.model.QTareasExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.QValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.model.ResponsablesTramitacion;
import es.juntadeandalucia.ctpda.gestionpdt.model.TareasExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.TramiteExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.Usuario;
import es.juntadeandalucia.ctpda.gestionpdt.model.UsuariosResponsables;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.repository.TareasExpedienteRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.ValidacionException;
import es.juntadeandalucia.ctpda.gestionpdt.util.FechaUtils;
import es.juntadeandalucia.ctpda.gestionpdt.util.ListUtils;
import es.juntadeandalucia.ctpda.gestionpdt.util.StringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TareasExpedienteService extends AbstractCRUDService<TareasExpediente>{
	
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String FILTRO_LISTA_IDS_RESP = "#listaIdsResponsables";
	public static final String COD_VAL_DOM_TIPOBS_TAR = "TAR";

	private static final String COD_TIPO_TAREA_TRAM_EDPRT="EDPRT";
	private static final String COD_TIPO_TAREA_DOC_REV="REV";

	private TareasExpedienteRepository tareasExpedienteRepository;

	@Autowired
	private ValoresDominioService valoresDominioService;
	@Autowired
	private DocumentosExpedientesTramitesService documentosExpedientesTramitesService;
	@Autowired
	private TramiteExpedienteService tramiteExpedienteService;
	@Autowired
	private CfgTareasService cfgTareasService;
	@Autowired
	private PlazosExpdteService plazosExpdteService;
	@Autowired
	private ExpedientesService expedientesService;
	@Autowired
	private ResponsablesTramitacionService responsablesTramitacionService;
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private UsuariosResponsablesService usuariosResponsablesService;
	
	@Autowired
	private ObservacionesExpedientesService observacionesExpedientesService;
	
	/**
	 * Constructor que inyecta bean de calculos matematicos y el resto de beans que hacen falta
	 * */
	public TareasExpedienteService(@Autowired MathsQueryService mathsQueryService, @Autowired TareasExpedienteRepository tareasExpedienteRepository){
		super(mathsQueryService,
				tareasExpedienteRepository,
				QTareasExpediente.tareasExpediente);
		this.tareasExpedienteRepository = tareasExpedienteRepository;
		
		this.joinBuilder = query -> {
			final QTareasExpediente qTarea = QTareasExpediente.tareasExpediente;
			final QExpedientes qExpediente = new QExpedientes("qExpediente");
			final QResponsablesTramitacion qResponsablesTramitacion = new QResponsablesTramitacion("qResponsablesTramitacion");
			final QValoresDominio qSituExp = new QValoresDominio("qSituExp");
						
			query.join(qTarea.expediente, qExpediente).fetchJoin();
			query.join(qExpediente.valorSituacionExpediente, qSituExp).fetchJoin();
			query.leftJoin(qTarea.responsableTramitacion, qResponsablesTramitacion).fetchJoin();
			
			return query;
		};
	}
	

	
	@Override
	public void checkSiPuedoGrabar(TareasExpediente dto) throws BaseException {
		log.debug("Verifico si puedo grabar y elevo excepción si no");
		
		if(null == dto.getValorTipoTarea()) {
			throw new ValidacionException("El tipo de tarea no puede estar vacío.");							
		}
		
		if(null == dto.getResponsableTramitacion()) {
			throw new ValidacionException("No se ha asignado un responsable.");					
		} else if(Boolean.FALSE.equals(dto.getResponsableTramitacion().getActivo())) {
			throw new ValidacionException("Se debe asignar un responsable activo.");			
		}
		
		if(StringUtils.isBlank(dto.getDescripcion())) {
			throw new ValidacionException("La descripción no puede estar vacía.");
		}
		
		final Date fechaInicio = dto.getFechaInicio();
		final Date fechaLimite =dto.getFechaLimite();
		
		if(null == fechaInicio) {
			throw new ValidacionException("La fecha de inicio no puede estar vacía.");
		}
		
		if(null != fechaLimite && FechaUtils.despues(fechaInicio, fechaLimite)) {
			throw new ValidacionException("La fecha límite no puede ser anterior a la de inicio.");
		}
		
		if(null == dto.getUsuarioCreador()) {
			throw new ValidacionException("El usuario de creación no puede estar vacío.");
		}
		
		//Si es una tarea de documento tengo que asegurarme que 
		//  si es una revisión no haya otra ya para el mismo documento.
		if(dto.getId() == null 
				&& null != dto.getDocumentoExpedienteTramite() 
				&& dto.getValorTipoTarea().getCodigo().equals("REV")) {
			Long idDoc = getIdDoc(dto.getDocumentoExpedienteTramite().getId());
			if(this.existeTareaREVPendienteDocumento(idDoc)) {
				throw new ValidacionException("El documento asociado ya tiene creada una tarea de revisión.");	
			}
		}
	}
	
	private Long getIdDoc(Long idDocExpTram) {
		DocumentosExpedientesTramites det = documentosExpedientesTramitesService.obtener(idDocExpTram);
		return det.getDocumentoExpediente().getDocumento().getId();		
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
		BooleanBuilder bb = new BooleanBuilder();
		
		for(FiltroDTO f : filtros) {
			if(f.getCampo().equals(FILTRO_LISTA_IDS_RESP)) {
				@SuppressWarnings("unchecked")
				final List<Long> idsResp = (List<Long>)f.getValue();
				bb = bb.and(QTareasExpediente.tareasExpediente
						.responsableTramitacion.id.in(idsResp));
			}
		}
		
		return bb;
	}
	
	//**********************************************************
	
	public List<TareasExpediente> findTareasPendientesExpediente(Long idExp){
		return this.tareasExpedienteRepository.findByExpedienteIdAndActivaAndSituacion(idExp, Boolean.TRUE, TareasExpediente.SITUACION_PENDIENTE);
	}
	
	public List<TareasExpediente> findTareasPendientesTramite(Long idTramite){
		return this.tareasExpedienteRepository.findByTramiteExpedienteIdAndActivaAndSituacion(idTramite, Boolean.TRUE, TareasExpediente.SITUACION_PENDIENTE);
	}
	
	public List<TareasExpediente> findTareasPendientesExpedienteDeResponsable(Long idExp, Long idResp){
		return this.tareasExpedienteRepository.findByExpedienteIdAndResponsableTramitacionIdAndActivaAndSituacion(idExp, idResp, Boolean.TRUE, TareasExpediente.SITUACION_PENDIENTE);
	}
	
	public List<TareasExpediente> findTareasPendientesTramiteDeResponsable(Long idTramite, Long idResp){
		return this.tareasExpedienteRepository.findByTramiteExpedienteIdAndResponsableTramitacionIdAndActivaAndSituacion(idTramite, idResp, Boolean.TRUE, TareasExpediente.SITUACION_PENDIENTE);
	}
	
	public List<TareasExpediente> findTareasPendientesTramiteDeResponsablesDeUsuario(Long idTramite, Long idUsuario){
		List<UsuariosResponsables> lista = usuariosResponsablesService.findByUsuarioId(idUsuario);		
		return lista.stream()
			.map(UsuariosResponsables::getResponsable)
			.flatMap(r -> findTareasPendientesTramiteDeResponsable(idTramite, r.getId()).stream())
			.collect(Collectors.toList());		
	}
	
	public List<TareasExpediente> findByExpedienteIdAndActiva(Long idExpediente){
		return this.tareasExpedienteRepository.findByExpedienteIdAndActiva(idExpediente);
	}
	
	//--------------
	
	public boolean existeTareaPendienteExpediente(Long idExp) {
		return this.tareasExpedienteRepository.existeTareaPendienteExpediente(idExp);
	}	
	
	public boolean existeTareaPendienteTramite(Long idTram) {
		return this.tareasExpedienteRepository.existeTareaPendienteTramite(idTram);
	}
	
	public boolean existeTareaPendienteExpediente(Long idExp, String codTipoTarea) {
		return this.tareasExpedienteRepository.existeTareaPendienteExpediente(idExp, codTipoTarea);
	}	
	
	public boolean existeTareaPendienteTramite(Long idTram, String codTipoTarea) {
		return this.tareasExpedienteRepository.existeTareaPendienteTramite(idTram, codTipoTarea);
	}
	
	public boolean existeTareaPendienteExpedienteDeResponsable(Long idExp, Long idResp) {
		return this.tareasExpedienteRepository.existeTareaPendienteExpedienteDeResponsable(idExp, idResp);
	}	
	
	public boolean existeTareaPendienteTramiteDeResponsable(Long idTram, Long idResp) {
		return this.tareasExpedienteRepository.existeTareaPendienteTramiteDeResponsable(idTram, idResp);
	}
	
	
	//----------
	
	public boolean existeTareaEDPRTPendienteTramite(Long idTram) {
		return this.existeTareaPendienteTramite(idTram, COD_TIPO_TAREA_TRAM_EDPRT);
	}
	
	public boolean existeTareaFYNPendienteTramite(Long idTram) {
		return this.tareasExpedienteRepository.existeTareaPendienteTramite(idTram, "FYN");
	}
	
	public TareasExpediente getTareaFYNPendienteTramite(Long idTram) {
		return this.tareasExpedienteRepository.getTareaPendienteTramite(idTram, "FYN");
	}
	//-----------
	
	public boolean existeTareaDocumento(Long idDoc, String codTipoTarea) {
		return this.tareasExpedienteRepository.existeTareaDocumento(idDoc, codTipoTarea);
	}
	
	public boolean existeTareaPendienteDocumento(Long idDoc, String codTipoTarea) {
		return this.tareasExpedienteRepository.existeTareaPendienteDocumento(idDoc, codTipoTarea);
	}
	
	public boolean existeTareaREVDocumento(Long idDoc) {
		return this.existeTareaDocumento(idDoc, COD_TIPO_TAREA_DOC_REV);
	}	
	
	public boolean existeTareaREVPendienteDocumento(Long idDoc) {
		return this.existeTareaPendienteDocumento(idDoc, COD_TIPO_TAREA_DOC_REV);
	}
		
	//--------------
	
	public TareasExpediente getTareaPendienteDocumento(Long idDoc, String codTipoTarea) {
		return this.tareasExpedienteRepository.getTareaPendienteDocumento(idDoc, codTipoTarea);
	}
	
	public TareasExpediente getTareaREVPendienteDocumento(Long idDoc) {
		return this.getTareaPendienteDocumento(idDoc, COD_TIPO_TAREA_DOC_REV);
	}
	
	public List<TareasExpediente> findTareasExpActivasByTramExpTipTar(Long idTramExp, Long idTipTar){
		return this.tareasExpedienteRepository.findTareasExpActivasByTramExpTipTar(idTramExp, idTipTar);
	}

	
	//********************************************************************************************
	
	public TareasExpediente nuevaTarea(Expedientes expediente, Usuario usuCreador) {
		final TareasExpediente t = new TareasExpediente(); 
		
		t.setUsuarioCreador(usuCreador);
		t.setExpediente(expediente);
				
		t.setFechaInicio(FechaUtils.hoy());
		
		t.setSituacion(TareasExpediente.SITUACION_PENDIENTE);
		t.setTipoAlta(TareasExpediente.TIPO_ALTA_MANUAL);
		
		return t;
	}
	
	//Ya no se usa, pero puede ser útil tenerla como public
	public ValoresDominio findTipoTarea(String codigo) {
		return valoresDominioService.findValoresDominioByCodigoDomCodValDom(
				"TAREAS", codigo);
	}
	
	//Devuelve la lista de elementos CfgTareas aplicables a la tarea actual
	private List<CfgTareas> findCfgTareas(TareasExpediente tarea) throws BaseException{
		final Expedientes exp = expedientesService.obtener(tarea.getExpediente().getId());
		final Long[] idsTipoTramite = tramiteExpedienteService.getIdsTipoTramiteSubtramite(tarea.getTramiteExpediente());
		final DocumentosExpedientesTramites docExpTr = tarea.getDocumentoExpedienteTramite();
		final Long idDocExpTramite = docExpTr != null? docExpTr.getId() : null;
		
		return this.cfgTareasService.findTiposTareasActivasCfg(
				exp.getValorTipoExpediente().getId(),
				idsTipoTramite[0],idsTipoTramite[1],
				null != idDocExpTramite);
	}
	
	public CfgTareas getCfgTareaActual(TareasExpediente tarea) throws BaseException {
		List<CfgTareas> listaCfg = findCfgTareas(tarea);
		ValoresDominio tipoTarea = tarea.getValorTipoTarea();
		
		//Buscamos el tipo tarea entre los cfg encontrados
		listaCfg = ListUtils.filter(listaCfg, cfg -> cfg.getValorTipoTarea().getId().equals(tipoTarea.getId()));
		
		if(listaCfg.isEmpty()) {
			throw new BaseException("La lista de CfgTareas para el tipo de la tarea " + tarea.getId() + " no devolvió ningún elemento.");		
		}
		
		if(listaCfg.size() > 1) {
			throw new BaseException("La lista de CfgTareas para el tipo de la tarea " + tarea.getId() + " devolvió más de un elemento.");
		}
		
		return listaCfg.get(0);
	}
	
	public void aplicarCfgTareas(TareasExpediente tarea, CfgTareas cfgTarea) {
		tarea.setValorTipoTarea(valoresDominioService.obtener(cfgTarea.getValorTipoTarea().getId()));
		
		tarea.setPlazoReferencia(cfgTarea.getPlazoReferencia());
		
		ResponsablesTramitacion respDefecto = cfgTarea.getResponsableTramitacionDefecto();
		if(null != respDefecto) {
			respDefecto = responsablesTramitacionService.obtener(respDefecto.getId());
		} 
		tarea.setResponsableTramitacion(respDefecto);

		tarea.setAviso(cfgTarea.getAviso());
		tarea.setBloqueoDistintoResponsable(cfgTarea.getBloqueoDistintoResponsable());
		tarea.setBloqueoMismoResponsable(cfgTarea.getBloqueoMismoResponsable());

		refrescarDescripciones(tarea, cfgTarea);
		aplicarFechaLimite(tarea, cfgTarea);
	}

	private void refrescarDescripciones(TareasExpediente tarea, CfgTareas cfgTarea) {		
		tarea.setDescripcion(cfgTarea.getDescripcion());
		tarea.setDescripcionAbrev(cfgTarea.getDescripcionAbrev());
	}
	
	public void aplicarFechaLimite(TareasExpediente tarea, CfgTareas cfgTarea) {
		tarea.setFechaLimite(null);
		
		if(cfgTarea == null) return;	
		
		//Cálculo basado en plazo de referencia
		if(!StringUtils.isBlank(tarea.getPlazoReferencia())) {
			final PlazosExpdte plazoAbiertoExp = findPlazoAbiertoTarea(tarea);
			
			if(plazoAbiertoExp != null) {
				Date fLimPlazo = plazoAbiertoExp.getFechaLimite();
				Integer diasRef = cfgTarea.getDiasPlazoReferencia();
				tarea.setFechaLimite(FechaUtils.sumarDiasAFecha(fLimPlazo, -diasRef)); //OJO: hay que restar
				return;
			}
		}
		
		//Si no hemos calculado la fecha límite según plazo hago el cálculo según la fecha de inicio como siempre
		final Date fIniTarea = tarea.getFechaInicio();
		final Integer plazoCfg = cfgTarea.getPlazo();
		
		if(plazoCfg != null && fIniTarea != null) {
			final LocalDate fechaInicio = FechaUtils.toLocalDate(fIniTarea);
			final LocalDate fechaLimite = fechaInicio.plusDays(plazoCfg);
			tarea.setFechaLimite(FechaUtils.toDate(fechaLimite));		
		}
		
	}
	
	private PlazosExpdte findPlazoAbiertoTarea(TareasExpediente tarea) {
		final ValoresDominio tipo = plazosExpdteService.findTipoPlazo(tarea.getPlazoReferencia());
		return plazosExpdteService.findPlazosExpdteActivosNoCumplidosByExpTipPla(tarea.getExpediente().getId(), tipo.getId());
	}

	public void aplicarDatosTramite(TareasExpediente tarea, Long idTramiteExpediente, Long idDocExpTramite ) {	
		if(null != idDocExpTramite) {
			final DocumentosExpedientesTramites docTramExp = documentosExpedientesTramitesService.obtener(idDocExpTramite);
			tarea.setDocumentoExpedienteTramite(docTramExp);
			tarea.setTramiteExpediente(docTramExp.getTramiteExpediente());
		} else if(null != idTramiteExpediente) {
			final TramiteExpediente tramExp = tramiteExpedienteService.obtener(idTramiteExpediente);
			tarea.setTramiteExpediente(tramExp);
		}
	}

	private boolean existeTareaSiguiente(TareasExpediente tarea){
		Long idTipoExp = tarea.getExpediente().getValorTipoExpediente().getId();
		Long[] ids = this.tramiteExpedienteService.getIdsTipoTramiteSubtramite(tarea.getTramiteExpediente());
		Long idTipoTr = ids[0];
		Long idTipoSubtr = ids[1];
		Long idTipoTarea = tarea.getValorTipoTarea().getId();
		boolean documento = (null != tarea.getDocumentoExpedienteTramite());
		
		return this.cfgTareasService.existeCfgTareaSiguiente(idTipoExp, idTipoTr, idTipoSubtr, idTipoTarea, documento);
	}

	//---------------------------------------------

	public void desactivarTarea(Long id) throws BaseException {
		final TareasExpediente t = this.obtener(id);
		t.setActiva(Boolean.FALSE);
		this.guardarSimple(t);
	}

	public void reactivarTarea(Long id) throws BaseException {
		final TareasExpediente t = this.obtener(id);
		t.setActiva(Boolean.TRUE);
		this.guardarSimple(t);
	}
	
	public void rehabilitarTarea(Long id) throws BaseException {
		final TareasExpediente t = this.obtener(id);
		t.setSituacion(TareasExpediente.SITUACION_PENDIENTE);
		t.setFechaCierre(null);
		t.setUsuarioCierre(null);
		
		this.guardarSimple(t);
	}

	//-------------
	
	@Transactional(value=TxType.REQUIRED, rollbackOn=Exception.class)
	public void cerrarTarea(TareasExpediente tarea, Usuario usuarioCierre, String motivo) throws BaseException {
		aplicarCierre(tarea, usuarioCierre);
		
		if(!StringUtils.isBlank(motivo)) {
			anyadirObservaciones(tarea, motivo);
		}
		
		//El cierre se hace desde el diálogo, puede que se hayan cambiado datos, lo que implica a otras operaciones
		//Debemos usar por tanto la versión no-simple
		tarea = this.guardar(tarea, usuarioCierre);
				
		if(this.hayQueCrearTareaSiguiente(tarea, usuarioCierre)){
			final AccionTarea accion = new AccionTarea(AccionTarea.ACCION_FIN, usuarioCierre);
			accion.aplicarDatosTarea(tarea);
			accion.setCodTipoTarea(tarea.getValorTipoTarea().getCodigo());
			this.crearTareasAuto(accion);
		}
	}
	
	public boolean hayQueCrearTareaSiguiente(TareasExpediente tarea, Usuario usuarioCierre) throws BaseException {
		final Long idTarea = tarea.getId();
		
		if(idTarea == null) {
			return false;
		}		
		
		tarea = this.obtener(idTarea);
		
		CfgTareas cfgTarea = this.getCfgTareaActual(tarea);
		final ResponsablesTramitacion respVuelta = cfgTarea.getResponsableTareaVuelta();
		
		if (respVuelta != null) {		
		return this.existeTareaSiguiente(tarea)
				&& usuariosResponsablesService.esResponsableDeUsuario(respVuelta.getId(), usuarioCierre.getId());
		}
		else {
			return false;
		}
	}
	
	@Transactional(value=TxType.REQUIRED, rollbackOn=Exception.class)
	public void cerrarTarea(TareasExpediente tarea, Usuario usuarioCierre) throws BaseException {
		cerrarTarea(tarea, usuarioCierre, null);
	}

	//Cerrar tarea, obviando si procede crear una tarea siguiente
	@Transactional(value=TxType.SUPPORTS, rollbackOn=Exception.class)
	public void cerrarTareaInmediatamente(TareasExpediente tarea, Usuario usuarioCierre, String motivo) throws BaseException{
		aplicarCierre(tarea, usuarioCierre);
		
		if(!StringUtils.isBlank(motivo)) {
			anyadirObservaciones(tarea, motivo);
		}
		
		//Usamos la versión "simple", que no crea otras tareas
		this.guardarSimple(tarea);
	}

	public void cerrarTareaInmediatamente(TareasExpediente tarea, Usuario usuarioCierre) throws BaseException{
		cerrarTareaInmediatamente(tarea, usuarioCierre, null);
	}
	
	private void aplicarCierre(TareasExpediente tarea, Usuario usuarioCierre){
		tarea.setSituacion(TareasExpediente.SITUACION_CERRADA);
		tarea.setUsuarioCierre(usuarioCierre);
		tarea.setFechaCierre(FechaUtils.ahora());
	}
	
	@Transactional(value=TxType.REQUIRED, rollbackOn=Exception.class)
	public void cerrarTareasPorAltaTramite(Expedientes exp, Usuario usuario) throws BaseException {
		final String motivo = "Tarea finalizada automáticamente por alta de trámite";
		this.cerrarTareasPorAlta(exp, usuario, motivo, CfgTareas::getFinalizarEnAltaTramite);
	}
	
	@Transactional(value=TxType.REQUIRED, rollbackOn=Exception.class)
	public void cerrarTareasPorAltaTarea(Expedientes exp, Usuario usuario) throws BaseException {
		final String motivo = "Tarea finalizada automáticamente por alta de tarea";
		this.cerrarTareasPorAlta(exp, usuario, motivo, CfgTareas::getFinalizarEnAltaTarea);
	}
	
	//Cierra todas las tareas de expediente (sin trámite) cuya configuración especifique L_FINAL_ALTA_TRAM = 1
	private void cerrarTareasPorAlta(Expedientes exp, Usuario usuario, String motivo, Predicate<CfgTareas> condicion) throws BaseException {
		List<TareasExpediente> tareasExp = this.findTareasPendientesExpediente(exp.getId());
		
		for(TareasExpediente tarea : tareasExp) {
			//De las tareas abiertas finalizo aquellas cuya configuración asociada así lo indique
			final CfgTareas cfg = this.getCfgTareaActual(tarea);
			
			if(condicion.test(cfg)) {
				String m = motivo + " (" + usuario.getNombre() + " - " + FechaUtils.stringFechaYHora() + ")";
				this.cerrarTareaInmediatamente(tarea, usuario, m);			
			}
		}
	}
	
	//----------
	
	@Transactional(value=TxType.REQUIRED, rollbackOn=Exception.class)
	public void rechazarTarea(TareasExpediente tarea, Usuario usuarioRechaza, String motivo) throws BaseException {
		//El responsable será el usuario creador
		Long idUsuCreador = tarea.getUsuarioCreador().getId();
		ResponsablesTramitacion respUsuCreador = responsablesTramitacionService.findResponsablePorDefectoUsuario(idUsuCreador);
		tarea.setResponsableTramitacion(respUsuCreador);
		
		//Adjuntamos motivo a las observaciones
		String cabeceraMotivo = "MOTIVO DE RECHAZO (" + usuarioRechaza.getNombre() + " - " + FechaUtils.stringFechaYHora() + "):\n";
		this.anyadirObservaciones(tarea, cabeceraMotivo + motivo);
		
		//Cambiamos la descripción
		this.aplicarPrefijoEnDescripcion(tarea, "RECHAZADA ");
		
		this.guardarSimple(tarea);
	}
	
	//----------
	
	//Para altas o modificaciones que no impliquen cambiar datos.
	private TareasExpediente guardarSimple(TareasExpediente tarea) throws BaseException {
		if(null == tarea.getId()) { //nueva tarea
			tarea.setFechaCreacion(FechaUtils.hoy());
			tarea.setSituacion(TareasExpediente.SITUACION_PENDIENTE);
			tarea.setActiva(Boolean.TRUE);
			
			if(null == tarea.getUrgente()) {
				tarea.setUrgente(Boolean.FALSE);
			}
	
				
				
				
		}
			
		return super.guardar(tarea);
	}
	
	@Override
	public TareasExpediente guardar(TareasExpediente tarea) throws BaseException {
		throw new IllegalArgumentException("Método no válido, usar guardar(TareasExpediente, Usuario)");
	}
	
	@Transactional(value=TxType.REQUIRED, rollbackOn=Exception.class)
	public TareasExpediente guardar(TareasExpediente tarea, Usuario usuario) throws BaseException {
		final boolean esAlta = (null == tarea.getId());
		final CfgTareas cfgTarea = this.getCfgTareaActual(tarea);
		
		if(esAlta) {
			cerrarTareasPorAltaTarea(tarea.getExpediente(), usuario);
			aplicarResponsableCambioAuto(tarea, cfgTarea);
		} else {

			if(tarea.getCambiaResponsable()
					&& Boolean.TRUE.equals(cfgTarea.getInformeCambioResponsable()
					&& null != cfgTarea.getValorTipoTareaSiCambiaResp())
					&& !(tarea.getUsuarioCreador().getId().equals(usuario.getId()))){
				crearTareaCambioResponsable(tarea, cfgTarea, usuario);
			}
		}
		
		cambiarUsuarioCreador(tarea, usuario);
		
		return this.guardarSimple(tarea);
	}
	
	private void crearTareaCambioResponsable(TareasExpediente tarea, CfgTareas cfgTarea, Usuario usuario) throws BaseException {
		Expedientes exp = expedientesService.obtener(tarea.getExpediente().getId());
		exp.getValorSituacionExpediente().getCodigo(); //precarga para el listado de mi mesa
		
		TareasExpediente tarea2 = this.nuevaTarea(exp, usuario);					
		tarea2.setValorTipoTarea(valoresDominioService.obtener(cfgTarea.getValorTipoTareaSiCambiaResp().getId()));
		tarea2.setTramiteExpediente(tarea.getTramiteExpediente());
		tarea2.setDocumentoExpedienteTramite(tarea.getDocumentoExpedienteTramite());
		
		//Con los datos nuevos de la tarea busco qué cfg le correspondería
		this.aplicarCfgTareas(tarea2, this.getCfgTareaActual(tarea2));
		
		//Sobreescribo lo aplicado por el cfg con valores específicos 
		tarea2.setDescripcion(tarea.getDescripcion()); //La descripción es la de la tarea origen
		this.aplicarPrefijoEnDescripcion(tarea2, "CAMBIO RESP. ");
		
		tarea2.setResponsableTramitacion(responsablesTramitacionService.findResponsablePorDefectoUsuario(tarea.getUsuarioCreador().getId()));
		
		this.guardarSimple(tarea2);
	}
	
	private void aplicarPrefijoEnDescripcion(TareasExpediente tarea, String prefijo) throws BaseException {
		int maxLengthDesc = getMaxLengthDescripcion();
		String desc = tarea.getDescripcion();
		//Ya que prefijamos podemos recortar el resultado tranquilamente
		//El resultado incluirá el prefijo sin recortar
		desc = StringUtils.substring(prefijo + desc, 0, maxLengthDesc);
				
		tarea.setDescripcion(desc);
	}
	
	private int getMaxLengthDescripcion() throws BaseException{
		try {
			return TareasExpediente.class.getDeclaredField("descripcion").getAnnotation(Size.class).max();
		} catch (NoSuchFieldException | SecurityException e) {
			throw new BaseException("Campo descripcion no existe"); //No debe ocurrir
		}	
	}
	
	private void aplicarPrefijoEnDescripcionAbrev(TareasExpediente tarea, String prefijo) throws BaseException {
		int maxLengthDesc = getMaxLengthDescripcionAbrev();
		String desc = tarea.getDescripcionAbrev();
		desc = StringUtils.substring(prefijo + desc, 0, maxLengthDesc);
				
		tarea.setDescripcionAbrev(desc);
	}
	
	private int getMaxLengthDescripcionAbrev() throws BaseException{
		try {
			return TareasExpediente.class.getDeclaredField("descripcionAbrev").getAnnotation(Size.class).max();
		} catch (NoSuchFieldException | SecurityException e) {
			throw new BaseException("Campo descripcionAbrev no existe"); //No debe ocurrir
		}	
	}
	
	private void cambiarUsuarioCreador(TareasExpediente tarea, Usuario usuario) {
		//actualmente este es el único sitio donde se aplica el responsable (el combo del diálogo no usa ajax)
		//quiere decir que podemos obtener el responsable anterior y comparar
		
		if(null != tarea.getId()
				&& tarea.getCambiaResponsable()
				&& this.existeTareaSiguiente(tarea)){ 
			tarea.setUsuarioCreador(usuario);
		}
		
	}
	
	// Cambiar responsable del trámite de la tarea
	private void aplicarResponsableCambioAuto(TareasExpediente tarea, CfgTareas cfg) throws BaseException {
		final boolean cambiarRespTram = cfg.getCambioAutomaticoTramite();

		if (cambiarRespTram) {
			ResponsablesTramitacion respAuto = cfg.getResponsableCambioAutomatico();

			if (respAuto == null) {
				//Si no hay responsable predefinido usar el responsable de la tarea
				respAuto = tarea.getResponsableTramitacion();
			}

			TramiteExpediente tram = tarea.getTramiteExpediente();
			tram.setResponsable(respAuto);
			tramiteExpedienteService.guardar(tram);
		}
	}

	//****************************************************
	// FUNCIÓN AUTOMÁTICA
	
	@Data
	public class AccionTarea {
		
		public static final String ACCION_DIRECTA="D";
		public static final String ACCION_ACEPTAR="A";
		public static final String ACCION_ALTA="N";
		public static final String ACCION_FIN="F";

		private String tipoAccion;
		
		private Long idExpediente;
		private Long idTramite;
		private Long idDocExpTram;
		private Long idTareaOrigen;
		
		private Expedientes expediente;
		private TramiteExpediente tramite;
		private DocumentosExpedientesTramites docExpTram;
		private TareasExpediente tareaOrigen;
		private ValoresDominio valorTipoTarea;
		
		private Usuario usuarioAccion;
		private ResponsablesTramitacion responsable;
		private boolean preferenciaResponsableExpediente;
		private boolean preferenciaResponsableTramite;
		private String codTipoTarea;
		private String mensaje;
		private Date fechaInicio;
		
		public AccionTarea(String tipoAccion, Usuario usuarioCreador) {
			this.tipoAccion = tipoAccion;
			this.usuarioAccion = usuarioCreador;
		}
		
		public void aplicarDatosTarea(TareasExpediente tarea) {
			/**Informamos los datos de expediente, trámite, etc. 
			A partir de estos obtendremos los datos de tipo de expediente, 
			tipo de trámite, etc. con los que buscar en la tabla de CfgTareas. */
			
			if(null != tarea.getExpediente()) {
				this.idExpediente = tarea.getExpediente().getId();
			}
			
			if(null != tarea.getTramiteExpediente()) {
				this.idTramite = tarea.getTramiteExpediente().getId();
			}
			
			if(null != tarea.getDocumentoExpedienteTramite()) {
				this.idDocExpTram = tarea.getDocumentoExpedienteTramite().getId();
			}
			
			this.idTareaOrigen = tarea.getId();
		}
		
		public void validar() throws BaseException {
			if(null == this.usuarioAccion) {
				accionInvalida();
			}
		}

		protected void accionInvalida() throws BaseException {
				throw new BaseException("Se produjo un error en la aplicación. Consulte con su administrador.", Collections.emptyList());			
		}
		
	}
	
	private class AccionTareaExpediente extends AccionTarea {
		public AccionTareaExpediente(String tipoAccion, Usuario usuarioCreador, Long idExpediente) {
			super(tipoAccion, usuarioCreador);
			this.setIdExpediente(idExpediente);
		}
		
		@Override
		public void setIdTramite(Long idTramite) {
			throw new UnsupportedOperationException("La accion de expediente no puede informar el id de trámite");
		}
		
		@Override
		public void setIdDocExpTram(Long idDocExpediente) {
			throw new UnsupportedOperationException("La accion de expediente no puede informar el id de documento-exp.");
		}

		@Override
		public void setIdTareaOrigen(Long idTareaOrigen) {
			throw new UnsupportedOperationException("La accion de expediente no puede informar el id de tarea origen.");
		}
		
		@Override
		public void validar() throws BaseException {
			super.validar();
			
			if(null == this.getIdExpediente()) {
				accionInvalida();
			}
		}
		
	}
	
	private class AccionTareaTramite extends AccionTarea {
		public AccionTareaTramite(String tipoAccion, Usuario usuarioCreador, Long idExpediente, Long idTramite) {
			super(tipoAccion, usuarioCreador);
			this.setIdExpediente(idExpediente);
			this.setIdTramite(idTramite);
		}
		
		@Override
		public void setIdDocExpTram(Long idDocExpediente) {
			throw new UnsupportedOperationException("La accion de trámite no puede informar el id de documento-exp.");
		}

		@Override
		public void setIdTareaOrigen(Long idTareaOrigen) {
			throw new UnsupportedOperationException("La accion de trámite no puede informar el id de tarea origen.");
		}
		
		@Override
		public void validar() throws BaseException {
			super.validar();
			
			if(null == this.getIdExpediente() || null == this.getIdTramite()) {
				accionInvalida();
			}
		}

	}
	
	private class AccionTareaDocumento extends AccionTarea {
		public AccionTareaDocumento(String tipoAccion, Usuario usuarioCreador, Long idTramite, Long idDocExpTram) {
			super(tipoAccion, usuarioCreador);
			this.setIdTramite(idTramite);
			this.setIdDocExpTram(idDocExpTram);
		}

		@Override
		public void setIdTareaOrigen(Long idTareaOrigen) {
			throw new UnsupportedOperationException("La accion de documento no puede informar el id de tarea origen.");
		}

		@Override
		public void validar() throws BaseException {
			super.validar();
			
			if(null == this.getIdExpediente() || null == this.getIdTramite() || null == this.getIdDocExpTram()) {
				accionInvalida();
			}
		}

	}
	
	private class AccionTareaTarea extends AccionTarea {
		public AccionTareaTarea(String tipoAccion, Usuario usuarioCreador, Long idTareaOrigen) throws BaseException {
			super(tipoAccion, usuarioCreador);
			this.setIdTareaOrigen(idTareaOrigen);
		}

		@Override
		public void validar() throws BaseException {
			super.validar();
			
			if(null == this.getIdExpediente() || null == this.getIdTareaOrigen()) {
				accionInvalida();
			}
		}

	}
	
	private class AccionTareaDirecta extends AccionTarea {
		public AccionTareaDirecta(String codTipoTarea, Usuario usuarioCreador) {
			super(ACCION_DIRECTA, usuarioCreador);
			this.setCodTipoTarea(codTipoTarea);
		}

		@Override
		public void validar() throws BaseException {
			super.validar();
			
			if(null == this.getCodTipoTarea()) {
				accionInvalida();
			}
		}

	}
	
	public AccionTarea nuevaAccionTareaExpediente(String tipoAccion, Usuario usuarioCreador, Long idExpediente) {
		return new AccionTareaExpediente(tipoAccion, usuarioCreador, idExpediente);
	}
	
	public AccionTarea nuevaAccionTareaTramite(String tipoAccion, Usuario usuarioCreador, Long idExpediente, Long idTramite) {
		return new AccionTareaTramite(tipoAccion, usuarioCreador, idExpediente, idTramite);
	}
	
	public AccionTarea nuevaAccionTareaDocumento(String tipoAccion, Usuario usuarioCreador, Long idTramite, Long idDocExpTram) {
		return new AccionTareaDocumento(tipoAccion, usuarioCreador, idTramite, idDocExpTram);
	}
	
	public AccionTarea nuevaAccionTareaTarea(String tipoAccion, Usuario usuarioCreador, Long idTareaOrigen) throws BaseException {
		return new AccionTareaTarea(tipoAccion, usuarioCreador, idTareaOrigen);
	}
	
	public AccionTarea nuevaAccionTareaDirecta(String codTipoTarea, Usuario usuarioCreador) {
		return new AccionTareaDirecta(codTipoTarea, usuarioCreador);
	}
	
	//---------
	//Recuperar las entidades correspondientes a los IDs almacenados en la propia acción
	private void aplicarDatosAccion(AccionTarea accion) throws BaseException {
		aplicarExpediente(accion);
		aplicarTramite(accion);
		aplicarDocExpTramite(accion);
		aplicarTareaOrigen(accion);
		aplicarTipoTarea(accion);
	}
	
	private void aplicarExpediente(AccionTarea accion) throws BaseException {
		if(null != accion.getExpediente()) {
			return;
		}
		
		if(accion.getIdExpediente() != null) {
			accion.setExpediente(expedientesService.obtener(accion.getIdExpediente()));	
			return;
		}		
		
		throw new BaseException("El expediente de la acción no puede ser nulo. Usar accion.setExpediente o accion.setIdExpediente.");
	}
	
	private void aplicarTramite(AccionTarea accion) {
		if(null != accion.getTramite()) {
			return;
		}
		
		if(null != accion.getIdTramite()) {
			accion.setTramite(tramiteExpedienteService.obtener(accion.getIdTramite()));		
		}
	}
	
	private void aplicarDocExpTramite(AccionTarea accion) {
		if(null != accion.getDocExpTram()) {
			return;
		}
		
		if(null != accion.getIdDocExpTram()) {
			accion.setDocExpTram(documentosExpedientesTramitesService.obtener(accion.getIdDocExpTram()));		
		}
	}
	
	private void aplicarTareaOrigen(AccionTarea accion) {
		if(null != accion.getTareaOrigen()) {
			return;
		}
		
		if(null != accion.getIdTareaOrigen()) {
			accion.setTareaOrigen(this.obtener(accion.getIdTareaOrigen()));		
		}
	}
	
	private void aplicarTipoTarea(AccionTarea accion) {
		if(null != accion.getValorTipoTarea()) {
			return;
		}
		
		if(null != accion.getCodTipoTarea()) {
			accion.setValorTipoTarea(valoresDominioService.findValoresDominioByCodigoDomCodValDom("TAREAS", accion.getCodTipoTarea()));
		}
	}
	
	//-------------------------------------

	@Transactional(value = TxType.MANDATORY, rollbackOn = Exception.class)
	public void crearTareasAuto(AccionTarea accion) throws BaseException {		
		accion.validar();
		aplicarDatosAccion(accion);
		
		//Cierro antes para que las tareas no se cierren a sí mismas despues de guardarse
		cerrarTareasPorAltaTarea(accion.getExpediente(), accion.getUsuarioAccion());
		
		for(CfgTareas cfg : findCfgTareasAccion(accion)) {
			TareasExpediente tarea = this.nuevaTareaAuto(accion, cfg);
			this.guardarSimple(tarea);
		}
	}
	
	private List<CfgTareas> findCfgTareasAccion(AccionTarea accion) {
		return (accion.tipoAccion.equals(AccionTarea.ACCION_DIRECTA))? 
							findCfgTareasDirectas(accion)
							: findCfgTareasSiguientes(accion);
	}
	
	private List<CfgTareas> findCfgTareasDirectas(AccionTarea accion) {
		final Long idTipoExp = accion.getExpediente().getValorTipoExpediente().getId();
		final Long[] idsTipoTr = tramiteExpedienteService.getIdsTipoTramiteSubtramite(accion.getTramite());
		final Long idTipoTarea = accion.getValorTipoTarea().getId();
		final boolean doc = null != accion.getIdDocExpTram();
		
		final List<CfgTareas> cfgTareas = cfgTareasService.findCfgTareas(idTipoExp, idsTipoTr[0], idsTipoTr[1], idTipoTarea, doc);
		return filtrarPorTipoLlamada(cfgTareas, accion);	
	}
	
	private List<CfgTareas> findCfgTareasSiguientes(AccionTarea accion) {
		final Long idTipoExp = accion.getExpediente().getValorTipoExpediente().getId();
		final Long[] idsTipoTr = tramiteExpedienteService.getIdsTipoTramiteSubtramite(accion.getTramite());
		final Long idTipoTareaOrigen = getIdTipoTarea(accion.getTareaOrigen());
		final boolean doc = null != accion.getIdDocExpTram();
		
		final List<CfgTareas> cfgTareas = cfgTareasService.findCfgTareasSiguientes(idTipoExp, idsTipoTr[0], idsTipoTr[1], idTipoTareaOrigen, doc);
		return filtrarPorTipoLlamada(cfgTareas, accion);	
	}
	
	private Long getIdTipoTarea(TareasExpediente tarea) {
		return (null == tarea)? null : tarea.getValorTipoTarea().getId();
	}
	
	private List<CfgTareas> filtrarPorTipoLlamada(List<CfgTareas> cfgTareas, AccionTarea accion){
		final Predicate<CfgTareas> filtro;
		
		switch(accion.getTipoAccion()) {
		case AccionTarea.ACCION_ACEPTAR: filtro = CfgTareas::getAutoAceptar;
			break;
		case AccionTarea.ACCION_ALTA: filtro = CfgTareas::getAutoAlta;
			break;
		case AccionTarea.ACCION_FIN: filtro = CfgTareas::getAutoFinalizar;
			break;
		//AccionTarea.ACCION_DIRECTA
		default: filtro = cfg -> Boolean.TRUE;
		}
		
		return cfgTareas.stream().filter(filtro).collect(Collectors.toList());
	}	
	
	private TareasExpediente nuevaTareaAuto(AccionTarea accion, CfgTareas cfgTarea) throws BaseException {
		TareasExpediente nuevaTarea = new TareasExpediente();


		nuevaTarea.setUsuarioCreador(accion.getUsuarioAccion());
		nuevaTarea.setExpediente(accion.getExpediente());
		
		nuevaTarea.setSituacion(TareasExpediente.SITUACION_PENDIENTE);
		nuevaTarea.setTipoAlta(TareasExpediente.TIPO_ALTA_AUTO);
		final Date fechaIni = accion.getFechaInicio();
		nuevaTarea.setFechaInicio(fechaIni!=null? fechaIni : FechaUtils.ahora());

		//---------
		
		//Algunos campos cargados aquí se sobrescriben más abajo
		aplicarCfgTareas(nuevaTarea, cfgTarea);
		
		nuevaTarea.setResponsableTramitacion(this.seleccionarResponsable(accion, cfgTarea));
		nuevaTarea.setTramiteExpediente(accion.getTramite());
		nuevaTarea.setDocumentoExpedienteTramite(accion.getDocExpTram());
		
		anyadirObservaciones(nuevaTarea, accion.getMensaje());
		
		//Este método modifica la descripción
		completarTareaSegunAccion(nuevaTarea, accion, cfgTarea);
		
		TareasExpediente tareaOrigen = accion.getTareaOrigen();
		if(null != tareaOrigen) {
			//Sobrescribo la descripción que se haya cargado antes
			nuevaTarea.setDescripcion(tareaOrigen.getDescripcion());
			nuevaTarea.setDescripcionAbrev(tareaOrigen.getDescripcionAbrev());
			
			if(accion.tipoAccion.equals(AccionTarea.ACCION_FIN)) {
				aplicarPrefijoEnDescripcion(nuevaTarea, "FINALIZADA: ");
				aplicarPrefijoEnDescripcionAbrev(nuevaTarea, "FIN ");
			}
			
			anyadirObservaciones(nuevaTarea, leerIndicacionesSiguiente(accion));
		}
		
		return nuevaTarea;
	}
	
	//------------------------
	
	private ResponsablesTramitacion seleccionarResponsable(AccionTarea accion, CfgTareas cfgTarea) {
		//Si ya se proporciona un responsable uso ese
		if(null != accion.getResponsable()) {
			return accion.getResponsable();
		}
		
		//Si no, lo busco
		ResponsablesTramitacion resp = cfgTarea.getResponsableTramitacionDefecto();
		
		if(resp == null) {
			resp = seleccionarResponsablePreferido(accion, cfgTarea);	
		}
		
		//Si la selección básica no devuelve nada, probar con la tarea origen de la acción
		if(resp == null && (Boolean.TRUE.equals(cfgTarea.getEsResponsableAnterior())) && (null != accion.getTareaOrigen()) ) {
			Usuario usuario = accion.getTareaOrigen().getUsuarioCreador();
			resp = responsablesTramitacionService.findResponsablePorDefectoUsuario(usuario.getId());
		}
			
		return resp;		 
	}
	
	private ResponsablesTramitacion seleccionarResponsablePreferido(AccionTarea accion, CfgTareas cfgTarea) {
		ResponsablesTramitacion resp = null;		
		ResponsablesTramitacion respExp = seleccionarResponsableExp(cfgTarea, accion.getExpediente());
		ResponsablesTramitacion respTram = seleccionarResponsableTram(cfgTarea, accion.getTramite());
		
		if(respExp != null && respTram != null) {
			//Si he obtenido los dos responsables miro s se ha indicado preferencia
			if(accion.isPreferenciaResponsableExpediente()) {
				resp = respExp;
			} else if(accion.isPreferenciaResponsableTramite()) {
				resp = respTram;
			} else {
				resp = respExp;
			}
		} else {
			if(respExp != null) {
				resp = respExp;				
			}
			if(respTram != null) {
				resp = respTram;
			}				
			//else ERROR: no debe ocurrir
		}
			
		return resp;		
	}
	
	public ResponsablesTramitacion seleccionarResponsable(TareasExpediente tarea, CfgTareas cfgTarea) {
		ResponsablesTramitacion resp = cfgTarea.getResponsableTramitacionDefecto();
		
		if(resp == null) {
			resp = seleccionarResponsableExp(cfgTarea, tarea.getExpediente());
				
			if(resp == null) {
				resp = seleccionarResponsableTram(cfgTarea, tarea.getTramiteExpediente());
			}
		}
			
		return resp;
	}
		
	private ResponsablesTramitacion seleccionarResponsableExp(CfgTareas cfgTarea, Expedientes exp) {
		ResponsablesTramitacion resp = null;
		boolean cfgRespExp = Boolean.TRUE.equals(cfgTarea.getEsResponsableExpediente());

		if(cfgRespExp) {
			ResponsablesTramitacion responsableExp = exp.getResponsable();
			if(null != responsableExp) {
				resp = responsablesTramitacionService.obtener(responsableExp.getId());
            }
		} 
			
		return resp;
	}
			
	private ResponsablesTramitacion seleccionarResponsableTram(CfgTareas cfgTarea, TramiteExpediente tram) {
		ResponsablesTramitacion resp = null;		
		boolean cfgRespTram = Boolean.TRUE.equals(cfgTarea.getEsResponsableTramite());

		if(cfgRespTram && null != tram) {
			ResponsablesTramitacion responsableTram = tram.getResponsable();
			if(null != responsableTram) {
				resp = responsablesTramitacionService.obtener(responsableTram.getId());
            }
		}
			
		return resp;
	}	
	
	//---------------
	
	private String leerIndicacionesSiguiente(AccionTarea accion) {
		String result = StringUtils.EMPTY;
		String indicaciones = accion.getTareaOrigen().getIndicacionesSiguiente();
		
		if(!StringUtils.isBlank(indicaciones)) {
			StringBuilder sb = new StringBuilder();
			sb.append("**** Indicaciones de la tarea previa *****\n")
				.append(indicaciones).append("\n")
				.append("******************************************\n");
			result = sb.toString();
		}
	
		return result;
	}
	
	private void anyadirObservaciones(TareasExpediente tarea, String txt) throws BaseException {
		ObservacionesExpedientes obsExp;
		String obs = "";
		obsExp = tarea.getObservaciones();
		if(!StringUtils.isEmpty(txt)) {
			if(obsExp != null && obsExp.getTexto() != null)
			{
				obs = obsExp.getTexto();
			}else {
				obs = "";
			}
			StringBuilder sb = new StringBuilder(
					StringUtils.isBlank(obs)? StringUtils.EMPTY : obs);
			
			if(sb.length() > 0) {
				sb.append("\n\n");
			}
			
			obsExp = observacionesExpedientesService.guardarObservacionesExpedientes(obsExp, sb.append(txt).toString(), COD_VAL_DOM_TIPOBS_TAR, tarea.getExpediente());
			
		}else
		{
			obsExp = observacionesExpedientesService.guardarObservacionesExpedientes(obsExp, "", COD_VAL_DOM_TIPOBS_TAR, tarea.getExpediente());
		}
		
		tarea.setObservaciones(obsExp);
	
	
	}
	
	private void completarTareaSegunAccion(TareasExpediente nuevaTarea, AccionTarea accion, CfgTareas cfgTarea) {
		if(AccionTarea.ACCION_DIRECTA.equals(accion.getTipoAccion())){
			final ValoresDominio tipoTarea = accion.getValorTipoTarea();
			nuevaTarea.setValorTipoTarea(tipoTarea);
			nuevaTarea.setDescripcion(tipoTarea.getDescripcion());
			nuevaTarea.setDescripcionAbrev(tipoTarea.getAbreviatura());					
		} else {
			nuevaTarea.setValorTipoTarea(cfgTarea.getValorTipoTarea());
			nuevaTarea.setDescripcion(cfgTarea.getDescripcion());
			nuevaTarea.setDescripcionAbrev(cfgTarea.getDescripcionAbrev());		
		}
	}
	
	
	//***************************
	/*
	Se realiza la siguiente comprobación para la tarea:
	- si existe alguna tarea posterior (que tenga el ID de tarea mayor que el id actual), 
	- que esté asignada a un responsable distinto del de la tarea que se está analizando
	- que esté en situación de activa y pendiente
	- que esté asociada al mismo trámite o a algún subtrámite del trámite de la tarea 
		(para esto último, buscar con el id trámite en el campo id tramite superior de los trámites del expediente).	 */
	public boolean esTareaDependiente(Long idTarea) {
		TareasExpediente tarea = this.obtener(idTarea);
		TramiteExpediente tram = tarea.getTramiteExpediente();
		
		if(tram==null) {
			return false;
		}
		
		BooleanExpression expr = criterioTareaDependiente(tarea, tram);
		
		return this.crudRepository.exists(expr);
	}
	
	public List<TareasExpediente> findDependencias(Long idTarea) {
		TareasExpediente tarea = this.obtener(idTarea);
		TramiteExpediente tram = tarea.getTramiteExpediente();
		
		if(tram==null) {
			return Collections.emptyList();
		}
		
		BooleanExpression expr = criterioTareaDependiente(tarea, tram);
		Iterable<TareasExpediente> it = this.crudRepository.findAll(expr, QTareasExpediente.tareasExpediente.fechaLimite.asc());
		
		return ListUtils.toList(it);
	}
	
	private BooleanExpression criterioTareaDependiente(TareasExpediente tarea, TramiteExpediente tram) {
		final Long idTarea = tarea.getId();
		final Long idTramiteTarea = tram.getId();
		final Long idRespTarea = tarea.getResponsableTramitacion().getId(); //siempre !null
		 
		final QTareasExpediente qTarea = QTareasExpediente.tareasExpediente;
         
		final BooleanExpression exprDistResp = qTarea.responsableTramitacion.id.ne(idRespTarea).and(qTarea.bloqueoDistintoResponsable.eq(Boolean.TRUE));
		final BooleanExpression exprMismoResp = qTarea.responsableTramitacion.id.eq(idRespTarea).and(qTarea.bloqueoMismoResponsable.eq(Boolean.TRUE));
		
		BooleanExpression expr;
		expr = qTarea.id.gt(idTarea);
		expr = expr.and(exprDistResp.or(exprMismoResp));	
		expr = expr.and(qTarea.activa.eq(Boolean.TRUE));
		expr = expr.and(qTarea.situacion.eq(TareasExpediente.SITUACION_PENDIENTE));
		expr = expr.and(qTarea.tramiteExpediente.id.eq(idTramiteTarea)
						.or(qTarea.tramiteExpediente.tramiteExpedienteSup.id.eq(idTramiteTarea)));

		return expr;
	}
	
	@Transactional(value = TxType.REQUIRED, rollbackOn = Exception.class)	
	public void crearTareaEDPRT(Long idTram) throws BaseException {
		
		if(existeTareaEDPRTPendienteTramite(idTram)) {
			return;
		}
		
		AccionTarea accion = null;
		TramiteExpediente tram = tramiteExpedienteService.obtener(idTram);
		Expedientes exp = tram.getExpediente();
		
		final String usuName = SecurityContextHolder.getContext().getAuthentication().getName();
		final Usuario usuCreacion = usuarioService.findByLogin(usuName);
		
		ResponsablesTramitacion respTram = tram.getResponsable();
		boolean usrEsDelRespTram = usuariosResponsablesService.esResponsableDeUsuario(respTram.getId(), usuCreacion.getId());
		
		if(!usrEsDelRespTram) {
			//NO es del responsable del trámite: crear tarea EDPRT
			accion = nuevaAccionTareaDirecta(COD_TIPO_TAREA_TRAM_EDPRT, usuCreacion);
			accion.setPreferenciaResponsableTramite(true);
		} else {
			ResponsablesTramitacion respExp = exp.getResponsable();		
			boolean usrEsDelRespExp = usuariosResponsablesService.esResponsableDeUsuario(respExp.getId(), usuCreacion.getId());
			if(!usrEsDelRespExp) {
				//NO es del responsable del expediente: crear tarea EDPRT
				accion = nuevaAccionTareaDirecta(COD_TIPO_TAREA_TRAM_EDPRT, usuCreacion);
				accion.setPreferenciaResponsableExpediente(true);
			}
		}
		
		if(accion != null) {
			accion.setIdExpediente(exp.getId());
			accion.setIdTramite(tram.getId());
			this.crearTareasAuto(accion);
		}
	}
	
	public List<TareasExpediente> findTareasExpActivasByExpedienteYSituacion(Long idExpediente, String codSituacion){
		return this.tareasExpedienteRepository.findTareasExpActivasByExpedienteYSituacion(idExpediente, codSituacion);
	}

}
