package es.juntadeandalucia.ctpda.gestionpdt.web.tareasprogramadas;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.CfgExpedienteTramite;
import es.juntadeandalucia.ctpda.gestionpdt.model.CfgTipoExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.DetalleExpdteTram;
import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.LogCron;
import es.juntadeandalucia.ctpda.gestionpdt.model.ObservacionesExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.PlazosExpdte;
import es.juntadeandalucia.ctpda.gestionpdt.model.ResolucionExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.TareasExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.TramiteExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.Usuario;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.service.CfgExpedienteTramiteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.CfgTipoExpedienteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.DetalleExpdteTramService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.LogCronService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ObservacionesExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.PlazosExpdteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ResolucionExpedienteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.TareasExpedienteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.TareasExpedienteService.AccionTarea;
import es.juntadeandalucia.ctpda.gestionpdt.service.TramiteExpedienteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.UsuarioService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ValoresDominioService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.util.FechaUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.Constantes;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.BaseBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TareasProgramadas extends BaseBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String TIME_ZONE = "Europe/Madrid";
	private static final String PROCESOFINAL = "Finalización";
	private static final String PROCESOSINACTIVIDAD = "SINACTIVIDAD";
	private static final String ERROR = "error";
	private static final String PRESENTACIONALEGACIONES = "PRESENTACION_ALEGACIONES";
	private static final String INICIOPROCESO = "Inicio proceso";
	private static final String FINPROCESO = "Fin proceso: Expedientes procesados: ";
	private static final String EXPEDIENTESFINALIZADOS = " Expedientes finalizados: ";
	private static final String EXPEDIENTESEVOLUCION = " Expedientes avanzados: ";	
	private static final String ELEXPEDIENTE = " el expediente ";
	private static final String PORPROCESOAUTOMATICO = " por proceso automático.";

	
	
	@Autowired
	private ExpedientesService expedientesService;
	
	@Autowired
	private TramiteExpedienteService tramiteExpedienteService;
	
	@Autowired
	private TareasExpedienteService tareasExpedienteService;
	
	@Autowired
	private PlazosExpdteService plazosExpdteService;
	
	@Autowired
	private DetalleExpdteTramService detalleExpdteTramService;
	
	@Autowired 
	private CfgTipoExpedienteService cfgTipoExpedienteService;
	
	@Autowired
	private ValoresDominioService valoresDominioService;
	
	@Autowired
	private ObservacionesExpedientesService observacionesExpedientesService;
	
	@Autowired
	private LogCronService logCronService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private CfgExpedienteTramiteService cfgExpedienteTramiteService;
	
	@Autowired
	private ResolucionExpedienteService resolucionExpedienteService;
	
	@Getter
	@Setter
	private int registrosTotalesPresentacionAlegacionesPsan;
	
	@Getter
	@Setter
	private int registrosModificadosPresentacionAlegacionesPsan;	
	
	
	@Scheduled (cron = "${cron.resueltos.a.finalizados}", zone = TIME_ZONE)
	@Transactional(TxType.REQUIRED)
	public void pasarExpedienteResueltoAFinalizado() {
		

		escribeLog(PROCESOFINAL,INICIOPROCESO);
		
		int registrosTotales = 0;
		int registrosModificados = 0;
		
		List<Expedientes> expedientesResueltos = expedientesService.findExpedientesBySituacion(Constantes.RST);
		
		for(int i = 0; i < expedientesResueltos.size(); i++) {
			registrosTotales ++;
			List<TramiteExpediente> tramitesExpAbiertosYActivos = tramiteExpedienteService.findTramitesExpAbiertosYActivosByExpediente(expedientesResueltos.get(i).getId());
			List<TareasExpediente> tareasExpActivasYPendientes = tareasExpedienteService.findTareasExpActivasByExpedienteYSituacion(expedientesResueltos.get(i).getId(), Constantes.SITUACIONTAREA_PENDIENTE);
			List<PlazosExpdte> plazosExpdteActivosYCumplidos = plazosExpdteService.findPlazosExpdteActivosYCumplidosByExpediente(expedientesResueltos.get(i).getId());
			List<DetalleExpdteTram> detalleTramExpNotifYResultNotifProvOPdte = detalleExpdteTramService.findDetalleTramExpActivoByExpedienteYTramiteNotifYResultNotif
					(expedientesResueltos.get(i).getId(),Constantes.COD_VAL_DOM_NOT,Constantes.RESULTNOTIF_PROV,Constantes.RESULTNOTIF_PDTE);
			Date fechaMayorNotifDetalleExpActivo = detalleExpdteTramService.findFechaMayorNotifDetalleExpActivoByExpediente(expedientesResueltos.get(i).getId(), Constantes.TIP_TRAM_RESOL, Constantes.TIP_TRAM_ACNOADM);
			int diasEntreFechaActualYFechaMayorNotif = FechaUtils.diasEntre(fechaMayorNotifDetalleExpActivo, FechaUtils.hoy());
			CfgTipoExpediente cfgTipoExpediente = cfgTipoExpedienteService.obtenerCfgTipoExpedientePorValorTipoExpediente(expedientesResueltos.get(i).getValorTipoExpediente().getId());
			
			if(tramitesExpAbiertosYActivos.isEmpty() && tareasExpActivasYPendientes.isEmpty() && plazosExpdteActivosYCumplidos.isEmpty() && detalleTramExpNotifYResultNotifProvOPdte.isEmpty()
					&& diasEntreFechaActualYFechaMayorNotif > cfgTipoExpediente.getDiasFinalizacion()) {
				try {
					registrosModificados ++;
					ValoresDominio valoresDominioSituacionFinalizado = valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_SIT, Constantes.FNZ);
					expedientesResueltos.get(i).setValorSituacionExpediente(valoresDominioSituacionFinalizado);				
					expedientesService.guardar(expedientesResueltos.get(i));
					
					ObservacionesExpedientes observacionesExpedientes = new ObservacionesExpedientes();
					observacionesExpedientes.setExpediente(expedientesResueltos.get(i));
					ValoresDominio valoresDominioObservacionExpediente = valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_DOM_TIPOBS, Constantes.COD_VAL_DOM_TIPOBS_EXP);
					observacionesExpedientes.setValorTipoObservacion(valoresDominioObservacionExpediente);
					observacionesExpedientes.setFechaEntrada(FechaUtils.ahora());
					observacionesExpedientes.setTexto("Expediente Finalizado por proceso automático");
					observacionesExpedientesService.guardar(observacionesExpedientes);
					
					escribeLog(PROCESOFINAL,"Expediente "+expedientesResueltos.get(i).getNumExpediente()+" Finalizado por proceso automático");
				
				} catch (BaseException e) {
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(ERROR));
					PrimeFaces.current().dialog().showMessageDynamic(message);
					log.error(e.getMessage());
				}
			}else {
				log.error("Durante el proceso automático de finalización a fecha "+FechaUtils.hoy()+ELEXPEDIENTE+expedientesResueltos.get(i).getNumExpediente()+" no cumple todas las condiciones para pasar de situación Resuelta a Finalizada.");
				if(diasEntreFechaActualYFechaMayorNotif > cfgTipoExpediente.getDiasFinalizacion()) {
					try {
						Usuario usuarioLogado = usuarioService.findByLogin(Constantes.USUARIO_SISTEMA);					
						AccionTarea accion = tareasExpedienteService.nuevaAccionTareaDirecta(Constantes.TAREA_AFNP, usuarioLogado);
			            accion.setExpediente(expedientesResueltos.get(i));	
			            accion.setIdExpediente(expedientesResueltos.get(i).getId());					
						tareasExpedienteService.crearTareasAuto(accion);
					} catch (BaseException e) {
						FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(ERROR));
						PrimeFaces.current().dialog().showMessageDynamic(message);
						log.error(e.getMessage());
					}
				}
			}
		}
		escribeLog(PROCESOFINAL,FINPROCESO + registrosTotales + EXPEDIENTESFINALIZADOS+ registrosModificados);

	}
	
	private void escribeLog(String proceso, String texto) {
		
		LogCron logCron = new LogCron();
		logCron.setFecha(FechaUtils.ahora());
		logCron.setProceso(proceso);
		logCron.setLog(texto);
		try {
			logCronService.guardar(logCron);
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(ERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
				
	}
	
	@Scheduled (cron = "${cron.expedientes.sin.actividad}", zone = TIME_ZONE)
	@Transactional(TxType.REQUIRED)
	public void expedientesSinActivdad() {
		escribeLog(PROCESOSINACTIVIDAD,INICIOPROCESO);
		
		int registrosTotales = 0;
		int registrosModificados = 0;
		
		List<Expedientes> expedientesNoSupervisados = expedientesService.findExpedientesBySituacionExpNoSupervisada();
		for(int i = 0; i < expedientesNoSupervisados.size(); i++) {
			registrosTotales ++;
			List<TramiteExpediente> tramitesExpAbiertosYActivos = tramiteExpedienteService.findTramitesExpAbiertosYActivosByExpediente(expedientesNoSupervisados.get(i).getId());
			List<TareasExpediente> tareasExpActivasYPendientes = tareasExpedienteService.findTareasExpActivasByExpedienteYSituacion(expedientesNoSupervisados.get(i).getId(), Constantes.SITUACIONTAREA_PENDIENTE);
			if(tramitesExpAbiertosYActivos.isEmpty() && tareasExpActivasYPendientes.isEmpty()) {
				try {
					registrosModificados ++;
					Usuario usuarioLogado = usuarioService.findByLogin(Constantes.USUARIO_SISTEMA);					
					AccionTarea accion = tareasExpedienteService.nuevaAccionTareaDirecta(Constantes.TAREA_ESA, usuarioLogado);
		            accion.setExpediente(expedientesNoSupervisados.get(i));	
		            accion.setIdExpediente(expedientesNoSupervisados.get(i).getId());
					tareasExpedienteService.crearTareasAuto(accion);
					
					escribeLog(PROCESOSINACTIVIDAD,"Tarea creada para el expediente "+expedientesNoSupervisados.get(i).getNumExpediente()+PORPROCESOAUTOMATICO);
				} catch (BaseException e) {
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(ERROR));
					PrimeFaces.current().dialog().showMessageDynamic(message);
					log.error(e.getMessage());
				}
			}else {
				log.error("Durante el proceso automático de sin actividad a fecha "+FechaUtils.hoy()+ELEXPEDIENTE+expedientesNoSupervisados.get(i).getNumExpediente()+" no cumple todas las condiciones para crear la tarea automática 'Expediente sin actividad'.");
			}
		}
		
		escribeLog(PROCESOSINACTIVIDAD,FINPROCESO + registrosTotales + EXPEDIENTESFINALIZADOS+ registrosModificados);
	}
	
	@Scheduled (cron = "${cron.presentacion.alegaciones.psan}", zone = TIME_ZONE)
	@Transactional(TxType.REQUIRED)
	public void presentacionAlegacionesPSAN () {
		escribeLog(PRESENTACIONALEGACIONES,INICIOPROCESO);		
		registrosTotalesPresentacionAlegacionesPsan = 0;
		registrosModificadosPresentacionAlegacionesPsan = 0;		
		
		List<Expedientes> expedientesPsanPdteRecepcionAlegaciones = expedientesService.findExpedientesByTipExpSituacion(Constantes.PSAN,Constantes.PRAI);
		presentacionAlegacionesPSANExpedientesPsanPdteRecepcionAlegacionesAux(expedientesPsanPdteRecepcionAlegaciones);
				
		List<Expedientes> expedientesPsanPdteRecepcionAlegacionesPR = expedientesService.findExpedientesByTipExpSituacion(Constantes.PSAN,Constantes.PRAP);
		presentacionAlegacionesPSANExpedientesPsanPdteRecepcionAlegacionesPRAux(expedientesPsanPdteRecepcionAlegacionesPR);
		
		escribeLog(PRESENTACIONALEGACIONES,FINPROCESO + registrosTotalesPresentacionAlegacionesPsan + EXPEDIENTESEVOLUCION+ registrosModificadosPresentacionAlegacionesPsan);
	}
	
	private void presentacionAlegacionesPSANExpedientesPsanPdteRecepcionAlegacionesAux (List<Expedientes> expedientesPsanPdteRecepcionAlegaciones) {
		for(int i = 0; i < expedientesPsanPdteRecepcionAlegaciones.size(); i++) {
			
			registrosTotalesPresentacionAlegacionesPsan ++;			
			List<DetalleExpdteTram> subtramNotificacionTramIniPsan = detalleExpdteTramService.findSubtramitesByExpYTipSubtramiteYTipTramiteSup(expedientesPsanPdteRecepcionAlegaciones.get(i).getId(), Constantes.COD_VAL_DOM_NOT, Constantes.TIP_TRAM_INIPSAN);
			Date fechaMayorNotifTramIniPsan = detalleExpdteTramService.findFechaMayorNotifDetalleExpActivoByExpedienteAndTipoTramiteSup(expedientesPsanPdteRecepcionAlegaciones.get(i).getId(), Constantes.COD_VAL_DOM_NOT, Constantes.TIP_TRAM_INIPSAN); 
			Date fechaMayorEnvioTramIniPsan = detalleExpdteTramService.findFechaMayorEnvioDetalleExpActivoByExpedienteAndTipoTramiteSup(expedientesPsanPdteRecepcionAlegaciones.get(i).getId(), Constantes.COD_VAL_DOM_NOT, Constantes.TIP_TRAM_INIPSAN); 
			
			try {				
				if(subtramNotificacionTramIniPsan.isEmpty() || (!subtramNotificacionTramIniPsan.isEmpty() && fechaMayorNotifTramIniPsan == null && fechaMayorEnvioTramIniPsan == null)) {
					log.error("Durante el proceso automático de presentación de alegaciones a fecha "+FechaUtils.hoy()+ELEXPEDIENTE+expedientesPsanPdteRecepcionAlegaciones.get(i).getNumExpediente()+" ("+expedientesPsanPdteRecepcionAlegaciones.get(i).getValorSituacionExpediente().getDescripcion()+")"+" no cumple todas las condiciones para gestionar la espera de los tiempos de alegaciones'.");
				}else {
					registrosModificadosPresentacionAlegacionesPsan = registrosModificadosPresentacionAlegacionesPsan +	presentacionAlegacionesPSANExpedientesPsanPdteRecepcionAlegacionesAux2(fechaMayorNotifTramIniPsan,fechaMayorEnvioTramIniPsan,expedientesPsanPdteRecepcionAlegaciones.get(i));
				}
			} catch (BaseException e) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(ERROR));
				PrimeFaces.current().dialog().showMessageDynamic(message);
				log.error(e.getMessage());
			}
		}
	}
	
	private void presentacionAlegacionesPSANExpedientesPsanPdteRecepcionAlegacionesPRAux (List<Expedientes> expedientesPsanPdteRecepcionAlegacionesPR) {
		for(int i = 0; i < expedientesPsanPdteRecepcionAlegacionesPR.size(); i++) {
			
			registrosTotalesPresentacionAlegacionesPsan ++;	
			List<DetalleExpdteTram> subtramNotificacionTramPres = detalleExpdteTramService.findSubtramitesByExpYTipSubtramiteYTipTramiteSup(expedientesPsanPdteRecepcionAlegacionesPR.get(i).getId(), Constantes.COD_VAL_DOM_NOT, Constantes.TIP_TRAM_PRES);
			Date fechaMayorNotifTramPres = detalleExpdteTramService.findFechaMayorNotifDetalleExpActivoByExpedienteAndTipoTramiteSup(expedientesPsanPdteRecepcionAlegacionesPR.get(i).getId(), Constantes.COD_VAL_DOM_NOT, Constantes.TIP_TRAM_PRES); 
			Date fechaMayorEnvioTramPres = detalleExpdteTramService.findFechaMayorEnvioDetalleExpActivoByExpedienteAndTipoTramiteSup(expedientesPsanPdteRecepcionAlegacionesPR.get(i).getId(), Constantes.COD_VAL_DOM_NOT, Constantes.TIP_TRAM_PRES); 
			
			try {
				if(subtramNotificacionTramPres.isEmpty() || (!subtramNotificacionTramPres.isEmpty() && fechaMayorNotifTramPres == null && fechaMayorEnvioTramPres == null)) {
					log.error("Durante el proceso automático de presentación de alegaciones a fecha "+FechaUtils.hoy()+ELEXPEDIENTE+expedientesPsanPdteRecepcionAlegacionesPR.get(i).getNumExpediente()+" ("+expedientesPsanPdteRecepcionAlegacionesPR.get(i).getValorSituacionExpediente().getDescripcion()+")"+" no cumple todas las condiciones para gestionar la espera de los tiempos de alegaciones'.");
				}else {
					registrosModificadosPresentacionAlegacionesPsan = registrosModificadosPresentacionAlegacionesPsan + presentacionAlegacionesPSANExpedientesPsanPdteRecepcionAlegacionesPRAux(fechaMayorNotifTramPres, fechaMayorEnvioTramPres, expedientesPsanPdteRecepcionAlegacionesPR.get(i));
				}	
			} catch (BaseException e) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(ERROR));
				PrimeFaces.current().dialog().showMessageDynamic(message);
				log.error(e.getMessage());
			}
		}
	}
	
	private int presentacionAlegacionesPSANExpedientesPsanPdteRecepcionAlegacionesAux2 (Date fechaMayorNotifTramIniPsan, Date fechaMayorEnvioTramIniPsan, Expedientes expedientesPsanPdteRecepcionAlegacionesFor) throws BaseException {
		int cambiosHechos = 0;
		Date fechaMayorTramIniPsan = (fechaMayorNotifTramIniPsan != null)?fechaMayorNotifTramIniPsan:fechaMayorEnvioTramIniPsan;
		CfgTipoExpediente cfgTipoExpediente = cfgTipoExpedienteService.obtenerCfgTipoExpedientePorValorTipoExpediente(expedientesPsanPdteRecepcionAlegacionesFor.getValorTipoExpediente().getId());
		Date fechaMayorMasDiasAlegaciones = FechaUtils.sumarDiasAFecha(fechaMayorTramIniPsan, (cfgTipoExpediente.getDiasAlegaciones()).intValue());
		List<TramiteExpediente> tramiteAlegacFinalizado = tramiteExpedienteService.findTramiteFinalizadoByExpYTipoTramite(expedientesPsanPdteRecepcionAlegacionesFor.getId(), Constantes.TIP_TRAM_ALEG);
		
		if(((FechaUtils.hoy()).after(fechaMayorMasDiasAlegaciones)) || (!(FechaUtils.hoy()).after(fechaMayorMasDiasAlegaciones) && !tramiteAlegacFinalizado.isEmpty())){
			ValoresDominio valoresDominioSituacionPdteProResol = valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_SIT, Constantes.PPR);
			expedientesPsanPdteRecepcionAlegacionesFor.setValorSituacionExpediente(valoresDominioSituacionPdteProResol);
			expedientesService.guardar(expedientesPsanPdteRecepcionAlegacionesFor);
			
			CfgExpedienteTramite cfgExpTramitePsanPres = cfgExpedienteTramiteService.findCfgExpTramiteByTipExpYTipTram(Constantes.PSAN, Constantes.TIP_TRAM_PRES);
			altaTramite(cfgExpTramitePsanPres,expedientesPsanPdteRecepcionAlegacionesFor);
			
			escribeLog(PRESENTACIONALEGACIONES, "Situacion modificada a Pdte. propuesta resol. y alta del trámite Propuesta de resol. para el expediente "+expedientesPsanPdteRecepcionAlegacionesFor.getNumExpediente()+PORPROCESOAUTOMATICO);
			cambiosHechos ++;
		}
		
		return cambiosHechos;
	}

	private int presentacionAlegacionesPSANExpedientesPsanPdteRecepcionAlegacionesPRAux (Date fechaMayorNotifTramPres, Date fechaMayorEnvioTramPres, Expedientes expedientesPsanPdteRecepcionAlegacionesPRFor) throws BaseException {
		int cambiosHechos = 0;
		Date fechaMayorTramPres = (fechaMayorNotifTramPres != null)?fechaMayorNotifTramPres:fechaMayorEnvioTramPres;
		CfgTipoExpediente cfgTipoExpediente = cfgTipoExpedienteService.obtenerCfgTipoExpedientePorValorTipoExpediente(expedientesPsanPdteRecepcionAlegacionesPRFor.getValorTipoExpediente().getId());
		Date fechaMayorMasDiasAlegaciones = FechaUtils.sumarDiasAFecha(fechaMayorTramPres, (cfgTipoExpediente.getDiasAlegaciones()).intValue());
		List<TramiteExpediente> tramitePresentacionAlegacFinalizado = tramiteExpedienteService.findTramiteFinalizadoByExpYTipoTramite(expedientesPsanPdteRecepcionAlegacionesPRFor.getId(), Constantes.TP_TRAM_ALEGPR);
	
		if(((FechaUtils.hoy()).after(fechaMayorMasDiasAlegaciones)) || (!(FechaUtils.hoy()).after(fechaMayorMasDiasAlegaciones) && !tramitePresentacionAlegacFinalizado.isEmpty())){
			ValoresDominio valoresDominioSituacionPdteResolProcSanc = valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_SIT, Constantes.PRPS);
			expedientesPsanPdteRecepcionAlegacionesPRFor.setValorSituacionExpediente(valoresDominioSituacionPdteResolProcSanc);
			expedientesService.guardar(expedientesPsanPdteRecepcionAlegacionesPRFor);
			
			CfgExpedienteTramite cfgExpTramiteResol = cfgExpedienteTramiteService.findCfgExpTramiteByTipExpYTipTram(Constantes.PSAN, Constantes.TIP_TRAM_RESOL);
			altaTramite(cfgExpTramiteResol,expedientesPsanPdteRecepcionAlegacionesPRFor);
			
			escribeLog(PRESENTACIONALEGACIONES, "Situacion modificada a Pdte. resol. procedimiento sancionador y alta del trámite Resolución para el expediente "+expedientesPsanPdteRecepcionAlegacionesPRFor.getNumExpediente()+PORPROCESOAUTOMATICO);
			cambiosHechos ++;
		}
		
		return cambiosHechos;
	}
	
	private void altaTramite (CfgExpedienteTramite cfgExpedienteTramite, Expedientes expedienteActual) throws BaseException{
		ObservacionesExpedientes newObservacionExp = observacionesExpedientesService.guardarObservacionesExpedientes(null,null, Constantes.COD_VAL_DOM_TIPOBS_TRA, expedienteActual);
				
		TramiteExpediente newTramiteExp = new TramiteExpediente();			
		newTramiteExp.setFechaIni(FechaUtils.hoy());		
		newTramiteExp.setFechaFinReal(FechaUtils.fechaYHoraActualDate());
		newTramiteExp.setFechaFin(FechaUtils.fechaYHoraActualDate());
		newTramiteExp.setExpediente(expedienteActual);
		newTramiteExp.setTipoTramite(cfgExpedienteTramite.getTipoTramite());
		newTramiteExp.setNivel((long) 0);
		newTramiteExp.setDescripcion(cfgExpedienteTramite.getDescripcion());
		newTramiteExp.setDescripcionAbrev(cfgExpedienteTramite.getDescripcionAbrev());			
		newTramiteExp.setFinalizado(false);
		newTramiteExp.setActivo(true);
		Usuario usuarioLogado = usuarioService.findByLogin(Constantes.USUARIO_SISTEMA);
		newTramiteExp.setUsuarioTramitador(usuarioLogado);				
		newTramiteExp.setObservaciones(newObservacionExp);				
		if(cfgExpedienteTramite.getResponsablesTramitacion() != null){
			newTramiteExp.setResponsable(cfgExpedienteTramite.getResponsablesTramitacion());
		}else {
			newTramiteExp.setResponsable(expedienteActual.getResponsable());
		}		
		newTramiteExp = tramiteExpedienteService.altaTramite(usuarioLogado, newTramiteExp);
				
		newObservacionExp.setTramiteExpdte(newTramiteExp);
		observacionesExpedientesService.guardar(newObservacionExp);		
		
		expedienteActual.setFechaUltimaPersistencia(newTramiteExp.getFechaModificacion()!=null ? newTramiteExp.getFechaModificacion() : newTramiteExp.getFechaCreacion());
		expedienteActual.setUsuUltimaPersistencia(newTramiteExp.getUsuModificacion()!=null ? newTramiteExp.getUsuModificacion() : newTramiteExp.getUsuCreacion());
		expedienteActual = expedientesService.guardar(expedienteActual);
				
		DetalleExpdteTram newDetalleExpTramite = new DetalleExpdteTram();
		newDetalleExpTramite.setActivo(true);
		newDetalleExpTramite.setExpediente(expedienteActual);
		newDetalleExpTramite.setTramiteExpediente(newTramiteExp);
		newDetalleExpTramite.setApi(false);				
		newDetalleExpTramite.setExtractoExpediente(false);
		newDetalleExpTramite.setAntecedentesExpediente(false);
		newDetalleExpTramite.setImposicionMedidas(false);		
		newDetalleExpTramite.setValorTipoInteresado(null);
		newDetalleExpTramite.setPersonasInteresado(null);
		newDetalleExpTramite.setSujetosObligadosInteresado(null);
		newDetalleExpTramite.setValorDominioInteresado(null);
		if(Constantes.C012.equals(cfgExpedienteTramite.getTipoTramite().getComportamiento())){
			CfgTipoExpediente cfgTipoExpediente = cfgTipoExpedienteService.obtenerCfgTipoExpedientePorValorTipoExpediente(expedienteActual.getValorTipoExpediente().getId());
			if(cfgTipoExpediente != null){				
				ValoresDominio valorTipoResol = cfgTipoExpediente.getValorTipoResolucion();		
					newDetalleExpTramite.setValorTipoResolucion(valorTipoResol);
			}
		}			
		newDetalleExpTramite = detalleExpdteTramService.guardar(newDetalleExpTramite);
				
		expedienteActual.setFechaUltimaPersistencia(newDetalleExpTramite.getFechaModificacion()!=null ? newDetalleExpTramite.getFechaModificacion() : newDetalleExpTramite.getFechaCreacion());
		expedienteActual.setUsuUltimaPersistencia(newDetalleExpTramite.getUsuModificacion()!=null ? newDetalleExpTramite.getUsuModificacion() : newDetalleExpTramite.getUsuCreacion());
		expedientesService.guardar(expedienteActual);
		
		situacionesAdicionalesExpedienteAux(expedienteActual);		
	}
	
	private void situacionesAdicionalesExpedienteAux(Expedientes expedienteActual) throws BaseException {
		String situacionesAdiocionalesExp = calculaSituacionesAdicionalesExp(expedienteActual.getId(),expedienteActual);	    
		List<TramiteExpediente> tramitesExpAbiertos = tramiteExpedienteService.findTramitesExpAbiertos(expedienteActual.getId());
		final StringBuilder txt = new StringBuilder();		
		if (situacionesAdiocionalesExp != null) {
			txt.append(situacionesAdiocionalesExp);
		}		
		for(TramiteExpediente trExp:tramitesExpAbiertos) {			
			if (trExp.getSituacionAdicional() != null) {
				if (txt.length() > 0) {
					txt.append("; "); 
				}			
				txt.append(trExp.getSituacionAdicional()); 
			}			
		}			
		String txtSitAdicional = "";
		if (txt.length() > 0) {
			txtSitAdicional = txt.toString();
		}		
		expedienteActual.setSituacionAdicional(txtSitAdicional);
		expedientesService.guardar(expedienteActual);
	}
		
	private String calculaSituacionesAdicionalesExp(Long idExpedienteActual, Expedientes expedienteActual)  {		
		final StringBuilder txt = new StringBuilder();	
		
		String situacionPdtePublicarWeb = situacionAdicionalPdtePublicarWeb(idExpedienteActual);		
		if (situacionPdtePublicarWeb != null) {
			txt.append(situacionPdtePublicarWeb);
		}
		
		String situacionPdteResolucionRecurso = situacionAdicionalPdteResolRecurso(idExpedienteActual);		
		if (situacionPdteResolucionRecurso != null) {
			if (txt.length() > 0) {
				txt.append("; "); 
			}								
			txt.append(situacionPdteResolucionRecurso);
		}

		String situacionMedidasPdteAcreditar = situacionAdicionalMedidasPdteAcreditar(expedienteActual);		
		if (situacionMedidasPdteAcreditar != null) {
			if (txt.length() > 0) {
				txt.append("; "); 
			}								
			txt.append(situacionMedidasPdteAcreditar);
		}

		String txtSituacionAdicional = "";
		if (txt.length() > 0) {
			txtSituacionAdicional = txt.toString();
		}

		return txtSituacionAdicional;		
	}
	
	private String situacionAdicionalPdtePublicarWeb(Long idExpedienteActual)  {		
		String txtAux = null;		
		List<ResolucionExpediente> resolucionesExpediente = resolucionExpedienteService.findListResolucionExpByIdExpediente(idExpedienteActual);		
		for(ResolucionExpediente resolucionExp : resolucionesExpediente){			
			if(resolucionExp.getResolucion().getFechaPublicacionWeb() == null) {
				txtAux ="Pendiente publicación WEB";
			}		
		}		
		return txtAux;	
	}
	

	private String situacionAdicionalPdteResolRecurso(Long idExpedienteActual)  {		
		String txtAux = null;		
		ValoresDominio valorDominioPlazoRecurso =  valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_TIPO_PLAZO, Constantes.COD_VAL_DOM_REC);		
		PlazosExpdte plazosExpediente = plazosExpdteService.findPlazosExpdteByExpTipPla(idExpedienteActual, valorDominioPlazoRecurso.getId());		
		if (plazosExpediente != null) {		
				txtAux ="Pendiente Res. Recurso";
			}		
		return txtAux;	
	}
	
	private String situacionAdicionalMedidasPdteAcreditar(Expedientes expedienteActual)  {		
		String txtAux = null;
		if (Boolean.TRUE.equals(expedienteActual.getImposicionMedidas())) {
			txtAux ="Medidas pdte. acreditar";
		}		
		return txtAux;		
	}
	
}
