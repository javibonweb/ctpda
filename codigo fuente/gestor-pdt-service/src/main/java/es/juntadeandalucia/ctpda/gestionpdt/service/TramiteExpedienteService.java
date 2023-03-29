package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.StringPath;

import es.juntadeandalucia.ctpda.gestionpdt.model.QDetalleExpdteTram;
import es.juntadeandalucia.ctpda.gestionpdt.model.QExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.QPersonas;
import es.juntadeandalucia.ctpda.gestionpdt.model.QResponsablesTramitacion;
import es.juntadeandalucia.ctpda.gestionpdt.model.QSujetosObligados;
import es.juntadeandalucia.ctpda.gestionpdt.model.QTramiteExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.QUsuario;
import es.juntadeandalucia.ctpda.gestionpdt.model.QValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.model.TareasExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.TramiteExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.Usuario;
import es.juntadeandalucia.ctpda.gestionpdt.model.core.EntidadBasica;
import es.juntadeandalucia.ctpda.gestionpdt.repository.TramiteExpedienteRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TramiteExpedienteService extends AbstractCRUDService<TramiteExpediente>{

	private static final long serialVersionUID = 1L;
	

	@Autowired
	private transient ApplicationContext appContext;
	
	@Autowired
	private TareasExpedienteService tareasExpedienteService;
	
	
	private TramiteExpedienteRepository tramiteExpedienteRepository;

	
	/**
	 * Constructor que inyecta bean de calculos matematicos y el resto de beans que hacen falta
	 * */
	public TramiteExpedienteService(@Autowired MathsQueryService mathsQueryService, @Autowired TramiteExpedienteRepository tramiteExpedienteRepository){
		super(mathsQueryService,
				tramiteExpedienteRepository,
				QTramiteExpediente.tramiteExpediente);
		//también lo guardo para mi por si quiero hacer consultas personalizadas.
		this.tramiteExpedienteRepository=tramiteExpedienteRepository;
		this.joinBuilder = query -> {
			final QTramiteExpediente qTramExp = QTramiteExpediente.tramiteExpediente;
			final QDetalleExpdteTram qDetalleExpdteTram = QDetalleExpdteTram.detalleExpdteTram;
			final QValoresDominio qDominioTipoInteresado = new QValoresDominio("qDominioTipoInteresado");
			final QValoresDominio qValoresDominioActoRec = new QValoresDominio("qValoresDominioActoRec");

			final QValoresDominio qDominioTipoPlazo = new QValoresDominio("qDominioTipoPlazo");
			final QValoresDominio qDominioTipoAdmision = new QValoresDominio("qDominioTipoAdmision");
			final QUsuario qUsuario = new QUsuario("qUsuario");
			final QValoresDominio qDominioTipoFirma = new QValoresDominio("qDominioTipoFirma");
			final QValoresDominio qDominioCanalSalida = new QValoresDominio("qDominioCanalSalida");
			final QValoresDominio qDominioCanalEntrada = new QValoresDominio("qDominioCanalEntrada");
			final QValoresDominio qDominioCanalInfSalida = new QValoresDominio("qDominioCanalInfSalida");
			final QValoresDominio qDominioCanalInfEntrada = new QValoresDominio("qDominioCanalInfEntrada");
			final QValoresDominio qvaloresDominioInteresado = new QValoresDominio("qvaloresDominioInteresado");
			final QValoresDominio qvaloresDominioResultNotif = new QValoresDominio("qvaloresDominioResultNotif");
			final QPersonas qPersonasInteresado = new QPersonas("qPersonasInteresado");
			final QExpedientes qExpediente = new QExpedientes("qExpediente");
			final QSujetosObligados qSujetosObligadosInteresado = new QSujetosObligados("qSujetosObligadosInteresado");
			final QResponsablesTramitacion qResponsablesTramitacion = new QResponsablesTramitacion("qResponsablesTramitacion");
			final QValoresDominio qValorInstructorAPI = new QValoresDominio("qValorInstructorAPI");
			final QValoresDominio qvalorSentidoResolucion = new QValoresDominio("qvalorSentidoResolucion");
			final QValoresDominio qvalorTipoResolucion = new QValoresDominio("qvalorTipoResolucion");
			final QValoresDominio qvalorMotivoInadmision = new QValoresDominio("qvalorMotivoInadmision");
			final QValoresDominio qvalorTipoExpediente = new QValoresDominio("qvalorTipoExpediente");
			final QValoresDominio qPropuestaAPI = new QValoresDominio("qPropuestaAPI");
						
			query.leftJoin(qTramExp.detalleExpdteTramMappedBy, qDetalleExpdteTram).fetchJoin();
			query.leftJoin(qTramExp.responsable, qResponsablesTramitacion).fetchJoin();
			query.leftJoin(qDetalleExpdteTram.valorTipoInteresado, qDominioTipoInteresado).fetchJoin();
			query.leftJoin(qDetalleExpdteTram.valorDominioActoRec, qValoresDominioActoRec).fetchJoin();
			query.leftJoin(qDetalleExpdteTram.valorTipoPlazo, qDominioTipoPlazo).fetchJoin();
			query.leftJoin(qDetalleExpdteTram.valorTipoAdmision, qDominioTipoAdmision).fetchJoin();
			query.leftJoin(qDetalleExpdteTram.valorDominioTipoFirma, qDominioTipoFirma).fetchJoin();
			query.leftJoin(qDetalleExpdteTram.firmante, qUsuario).fetchJoin();
			query.leftJoin(qDetalleExpdteTram.valorCanalSalida, qDominioCanalSalida).fetchJoin();
			query.leftJoin(qDetalleExpdteTram.valorCanalEntrada, qDominioCanalEntrada).fetchJoin();
			query.leftJoin(qDetalleExpdteTram.valorCanalInfSalida, qDominioCanalInfSalida).fetchJoin();
			query.leftJoin(qDetalleExpdteTram.valorCanalInfEntrada, qDominioCanalInfEntrada).fetchJoin();
			query.leftJoin(qDetalleExpdteTram.valorDominioInteresado, qvaloresDominioInteresado).fetchJoin();
			query.leftJoin(qDetalleExpdteTram.valorResultadoNotificacion, qvaloresDominioResultNotif).fetchJoin();
			query.leftJoin(qDetalleExpdteTram.personasInteresado, qPersonasInteresado).fetchJoin();
			query.leftJoin(qDetalleExpdteTram.sujetosObligadosInteresado, qSujetosObligadosInteresado).fetchJoin();
			query.leftJoin(qDetalleExpdteTram.valorInstructorAPI, qValorInstructorAPI).fetchJoin();
			query.leftJoin(qDetalleExpdteTram.valorSentidoResolucion, qvalorSentidoResolucion).fetchJoin();
			query.leftJoin(qDetalleExpdteTram.valorTipoResolucion, qvalorTipoResolucion).fetchJoin();
			query.leftJoin(qDetalleExpdteTram.valorMotivoInadmision, qvalorMotivoInadmision).fetchJoin();
			query.leftJoin(qDetalleExpdteTram.expediente, qExpediente).fetchJoin();
			query.leftJoin(qExpediente.valorTipoExpediente, qvalorTipoExpediente).fetchJoin();
			query.leftJoin(qDetalleExpdteTram.valorDominioPropuestaApi, qPropuestaAPI).fetchJoin();
			
			return query;
		};
	}
	

	
	
	@Override
	public void checkSiPuedoGrabar(TramiteExpediente dto) throws BaseException {
		log.debug("Verifico si puedo grabar y elevo excepción sino");
		
	}

	@Override
	public void checkSiPuedoEliminar(Long id) throws BaseException {
		log.debug("Verifico si puedo eliminar y elevo excepción sino");		
	}
	
	public List<TramiteExpediente> findTramitesExp(Long idTipExp){
		return tramiteExpedienteRepository.findTramitesExp(idTipExp);
	}
	
	public List<Long> findIdsTramitesExpByExp(Long idExp, Long idTramite){
		return tramiteExpedienteRepository.findIdsTramitesExpByExp(idExp, idTramite);
	}
		
	public List<String> findDescAbrevTramitesExp(@Param("idTipExp") Long idTipExp){
		return tramiteExpedienteRepository.findDescAbrevTramitesExp(idTipExp);
	}
	
	public List<String> findCodigoTipoTramTramitesExp(@Param("idTipExp") Long idTipExp){
		return tramiteExpedienteRepository.findCodigoTipoTramTramitesExp(idTipExp);
	}
	
	public List<String> findCodigoTipoTramTramitesExpAbiertos(@Param("idTipExp") Long idTipExp){
		return tramiteExpedienteRepository.findCodigoTipoTramTramitesExpAbiertos(idTipExp);
	}
	
	public List<TramiteExpediente> findTipoTramTramitesExpAbiertos(@Param("idTipExp") Long idTipExp){
		return tramiteExpedienteRepository.findTipoTramTramitesExpAbiertos(idTipExp);
	}
	
	public List<TramiteExpediente> findTipoTramSubTramitesExpAbiertos(@Param("idTipExp") Long idTipExp){
		return tramiteExpedienteRepository.findTipoTramSubTramitesExpAbiertos(idTipExp);
	}
	
	
	public List<TramiteExpediente> findTramSubTramExpByCod(String codTipTram, Long idTipExp){
		return tramiteExpedienteRepository.findTramSubTramExpByCod(codTipTram, idTipExp);
	}
	
	public List<TramiteExpediente> findSubTramExpByTramExp(Long idTramExp){
		return tramiteExpedienteRepository.findSubTramExpByTramExp(idTramExp);
	}
	
	public List<TramiteExpediente> findSubTramExpByTramExpNoEliminados(Long idTramExp){
		return tramiteExpedienteRepository.findSubTramExpByTramExpNoEliminados(idTramExp);
	}
	
	public List<TramiteExpediente> findSubTramExpByTramExpActivosCodTram(Long idTramExp, String codTipoTram1, String codTipoTram2)
	{
		return tramiteExpedienteRepository.findSubTramExpByTramExpActivosCodTram(idTramExp, codTipoTram1, codTipoTram2);
	}
	
	public List<TramiteExpediente> findSubTramExpByTramExpActivosCodTram(Long idTramExp, String codTipoTram)
	{
		return tramiteExpedienteRepository.findSubTramExpByTramExpActivosCodTram(idTramExp, codTipoTram);
	}
	
	public List<TramiteExpediente> findSubTramExpByTramExpActivos(Long idTramExp){
		return tramiteExpedienteRepository.findSubTramExpByTramExpActivos(idTramExp);
	}
	
	public List<TramiteExpediente> findSubTramExpByTramExpActivosNoFinalizados(Long idTramExp){
		return tramiteExpedienteRepository.findSubTramExpByTramExpActivosNoFinalizados(idTramExp);
	}
	
	
	public List<TramiteExpediente> findTramExpAsociadoExp(Long idExp, Long idTipTram){
		return tramiteExpedienteRepository.findTramExpAsociadoExp(idExp, idTipTram);
	}
	
	public TramiteExpediente obtenerTramiteSup(Long idTramExp) {
		TramiteExpediente tram = obtener(idTramExp);
		return tram.getTramiteExpedienteSup();
	}
	
	public List<TramiteExpediente> findSubTramExp(Long idExp, Long idTipTramSup){
		return tramiteExpedienteRepository.findSubTramExp(idExp, idTipTramSup);
	}
	
	public List<String> findDescripcionTipoTramitesByExp (Long idExp){
		return tramiteExpedienteRepository.findDescripcionTipoTramitesByExp(idExp);
	}	
	
	public List<TramiteExpediente> findTramitesExpedientes(@Param("idExp") Long idExp){
		return tramiteExpedienteRepository.findTramitesExpedientes(idExp);
	}
	
	public List<TramiteExpediente> findTramitesExpAbiertos(@Param("idExp") Long idExp){
		return tramiteExpedienteRepository.findTramitesExpAbiertos(idExp);
	}
	
	/**
	 * Sólo trámites superiores
	 * @param idExp
	 * @return
	 */
	public List<TramiteExpediente> findTramitesSupExpAbiertos(@Param("idExp") Long idExp){
		return tramiteExpedienteRepository.findTramitesSupExpAbiertos(idExp);
	}
	
	/**
	 * Sólo trámites superiores
	 * @param idExp
	 * @return
	 */
	public List<TramiteExpediente> findTramitesSupExp(@Param("idExp") Long idExp){
		return tramiteExpedienteRepository.findTramitesSupExp(idExp);
	}
	
	public List<TramiteExpediente> findTodosSubTramExp(Long idExp, Long idTipTramSup){
		return tramiteExpedienteRepository.findTodosSubTramExp(idExp, idTipTramSup);
	}
	
	public List<String> findDescripcionTipoTramitesInformalesByExp (Long idExp, boolean informal){
		return tramiteExpedienteRepository.findDescripcionTipoTramitesInformalesByExp(idExp,informal);
	}
	
	public List<TramiteExpediente> findAllTramitesExpedientesByExp(Long idExp){
		return tramiteExpedienteRepository.findAllTramitesExpedientesByExp(idExp);
	}
	
	public boolean existeTramiteActivoNoFinalizadoDistinto(Long idExp, Long idTra){
		return tramiteExpedienteRepository.existsByActivoTrueAndFinalizadoFalseAndExpedienteIdAndIdNot(idExp, idTra);
	}
	
	public TramiteExpediente findUltimoTramActivoByExpTipoTramite(Long idExp, String codTipTramite, Long idTipExp)
	{
		return tramiteExpedienteRepository.findUltimoTramActivoByExpTipoTramite(idExp, codTipTramite, idTipExp);
	}


	
	

/**
 * Filtros custom.
 * Se pueden añadir predicados querydsl a manini, o dejar a nulo.
 * Se añade el filtro 
 * */
	@Override
	protected BooleanBuilder aniadirFiltrosCustom(List<FiltroDTO> filtros) {
		final BooleanBuilder bb = new BooleanBuilder();
		
		if(filtros != null) {
			filtros.stream().forEach(f -> {
				if((f.getCampo().equals("#fechaInicialTramite"))){
					DateTimePath<Date> fechaInicial = QTramiteExpediente.tramiteExpediente.fechaIni;
					bb.and(fechaInicial.eq((Date)f.getValue()).or(fechaInicial.after((Date)f.getValue())));
				}
				if((f.getCampo().equals("#fechaTramite"))){
					DateTimePath<Date> fechaTramite = QTramiteExpediente.tramiteExpediente.fechaTramite;
					bb.and(fechaTramite.eq((Date)f.getValue()).or(fechaTramite.after((Date)f.getValue())));
				}				
				if((f.getCampo().equals("#fechaFinalTramite"))){
					DateTimePath<Date> fechaFinal = QTramiteExpediente.tramiteExpediente.fechaIni;
					bb.and(fechaFinal.eq((Date)f.getValue()).or(fechaFinal.before((Date)f.getValue())));
				}
				if((f.getCampo().equals("#codigoTipoTramite"))){
					StringPath codigoTipoTramite = QTramiteExpediente.tramiteExpediente.tipoTramite.codigo;
					bb.and(codigoTipoTramite.eq("NOT").or(codigoTipoTramite.eq((String)f.getValue())));
				}
			});
		}

		return bb;
	}

	//---------------------------------------------
	
	// A partir de un trámite devuelve los ids de tipo de trámite correspondientes a su naturaleza como trámite o subtrámite
	// - Si es un trámite, el array devolvera [id_tipo_tramite, null]
	// - Si es un subtrámite, el array será   [id_tipo_tramite_superior, id_tipo_tramite]
	public Long[] getIdsTipoTramiteSubtramite(TramiteExpediente tram) {
		final Long[] idsTipoTramite = new Long[2];
		
		if(null != tram) {
			final Long[] idsTramite = tram.getIdsTramiteSubtramite();
			idsTipoTramite[0] = getIdTipoTramite(idsTramite[0]);
			idsTipoTramite[1] = getIdTipoTramite(idsTramite[1]);
		}
		
		return idsTipoTramite;
	}
	
	private Long getIdTipoTramite(Long idTr) {
		if(idTr == null) return null;
		
		final TramiteExpediente tramExp = this.obtener(idTr);
		return tramExp.getTipoTramite().getId();
	}
	
	//-----------------------------

	@Override
	@Transactional(value=TxType.REQUIRED, rollbackOn = Exception.class)
	public TramiteExpediente guardar(TramiteExpediente tramExp) throws BaseException {
		boolean finalizado = Boolean.TRUE.equals(tramExp.getFinalizado());
		
		if(tramExp.cambiaDescripcion() || finalizado) {
			this.cambiarDescripcionesAgrupacionesExpediente(tramExp);
		}
		
		if(!finalizado && tramExp.getId() != null && tramExp.cambiaResponsable()) {
			cambioResponsableTram(tramExp);
		} 
		
		return super.guardar(tramExp);
	}
	
	@Autowired
	private transient ApplicationContext appCtx;

	private void cambioResponsableTram(TramiteExpediente tramExp) throws BaseException {		
		UsuarioService usuarioService = (UsuarioService)appCtx.getBean("usuarioService");
		
		final Usuario usr = usuarioService.findUsuarioLogado();
		final Long idRespActual = tramExp.getResponsableActual().getId();
		final List<TareasExpediente> tareasExp = tareasExpedienteService.findTareasPendientesTramiteDeResponsable(tramExp.getId(), idRespActual);
		
		for(TareasExpediente tarea : tareasExp) {
			tarea.setResponsableTramitacion(tramExp.getResponsable());				
			//Para anular la detección de cambio de responsable y así evitar generar tareas derivadas de dicho cambio.
			tarea.setResponsableOriginal(tramExp.getResponsable());
			
			tareasExpedienteService.guardar(tarea, usr);
		}
	}

	@Transactional(value=TxType.REQUIRED, rollbackOn = Exception.class)
	public TramiteExpediente altaTramite(Usuario usuario, TramiteExpediente tram) throws BaseException {
		tram = this.guardar(tram);
		
		//Todos estos podrían ir al service de tareas
		tareasExpedienteService.cerrarTareasPorAltaTramite(tram.getExpediente(), usuario);
		
		//Por crear estas tareas se ejecutaría el cierre automático por alta igual que en la linea anterior, pero serían cierres por alta de tarea.
		//Con la linea anterior las tareas ya estarán cerradas al crear las nuevas. Se trata de que así identificamos mejor la causa del cierre (alta por trámite).
		TareasExpedienteService.AccionTarea accion = 
				tareasExpedienteService.nuevaAccionTareaTramite(TareasExpedienteService.AccionTarea.ACCION_ALTA, usuario,
						tram.getExpediente().getId(), tram.getId());
		tareasExpedienteService.crearTareasAuto(accion);
		
		return tram;
	}
	
	@Transactional(value=TxType.REQUIRED, rollbackOn = Exception.class)
	public TramiteExpediente finalizarTramite(Usuario usuario, TramiteExpediente tram) throws BaseException {
		tram = this.guardar(tram);
		
		finalizarTareasTramite(usuario, tram, "finalizarTramite");
		
		//Todos estos podrían ir al service de tareas
		TareasExpedienteService.AccionTarea accion = 
				tareasExpedienteService.nuevaAccionTareaTramite(TareasExpedienteService.AccionTarea.ACCION_FIN, usuario,
						tram.getExpediente().getId(), tram.getId());
		tareasExpedienteService.crearTareasAuto(accion);
		
		return tram;
	}
	
	//Cambia las descripciones de las agrupaciones de expediente asociadas a los documentos
	private void cambiarDescripcionesAgrupacionesExpediente(TramiteExpediente tram) throws BaseException {
		AgrupacionesExpedientesService agrSvc = (AgrupacionesExpedientesService)appContext.getBean("agrupacionesExpedientesService");
		agrSvc.cambiarDescripciones(tram);
	}
	
	public void finalizarTareasTramite(Usuario usuario, TramiteExpediente tram, String motivoFinalizacionTareas) throws BaseException{
		String motivo = "";
		
		if(motivoFinalizacionTareas.equals("finalizarTramite"))
		{
			motivo = "Tarea finalizada automáticamente al finalizar el trámite asociado.";
		}else
		{
			motivo = "Tarea finalizada automáticamente al eliminar el trámite asociado.";
		}
		
		for(TareasExpediente tarea : tareasExpedienteService.findTareasPendientesTramite(tram.getId())){
			tareasExpedienteService.cerrarTarea(tarea, usuario, motivo);
		}
	
	}
	
	public List<TramiteExpediente> findTramitesExpAbiertosYActivosByExpediente(Long idExpediente){
		return tramiteExpedienteRepository.findTramitesExpAbiertosYActivosByExpediente(idExpediente);
	}
	
	public List<TramiteExpediente> findTramiteFinalizadoByExpYTipoTramite(Long idExpediente, String codTipTramite){
		return tramiteExpedienteRepository.findTramiteFinalizadoByExpYTipoTramite(idExpediente, codTipTramite);
	}
	
	public boolean mismoExpediente(Long idTram1, Long idTram2) {
		var t1 = this.obtener(idTram1);
		var t2 = this.obtener(idTram2);
		
		if(t1 == null || t2 == null) {
			return false;
		}
		
		Long id1 = EntidadBasica.getIdEntidad(t1.getExpediente());
		Long id2 = EntidadBasica.getIdEntidad(t2.getExpediente());
		
		return Objects.equals(id1, id2);
	}
	
	public Long getIdExpedienteTramite(Long idTramite) {
		var tr = this.obtener(idTramite);
		return tr.getExpediente().getId();
	}

}
