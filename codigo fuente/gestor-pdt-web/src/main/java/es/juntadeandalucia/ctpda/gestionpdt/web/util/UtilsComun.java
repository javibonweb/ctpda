package es.juntadeandalucia.ctpda.gestionpdt.web.util;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.CfgPlazosAut;
import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.ObservacionesExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.PlazosExpdte;
import es.juntadeandalucia.ctpda.gestionpdt.model.TipoTramite;
import es.juntadeandalucia.ctpda.gestionpdt.model.TramiteExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.Usuario;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.model.enums.OrigenAut;
import es.juntadeandalucia.ctpda.gestionpdt.model.enums.Origenes;
import es.juntadeandalucia.ctpda.gestionpdt.model.enums.TipoAccion;
import es.juntadeandalucia.ctpda.gestionpdt.service.CfgPlazosAutService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ObservacionesExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.PlazosExpdteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.TramiteExpedienteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ValoresDominioService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.util.FechaUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.Constantes;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.JsfUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase de utilidad para el uso comun de metodos en distintos Beans
 *
 */

@Component
@ViewScoped
@Slf4j
public class UtilsComun implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private static final String NOEXISTEREGISTROCONFIGAUT = "no.existe.registro.config.aut";
	private static final String MASDEUNREGISTROCONFIGAUT = "mas.de.un.registro.config.aut";
	private static final String SOLCREACIONPLAZOEXISTE = "solicitud.creacion.plazo.existe";
	private static final String SOLMODIFPLAZONOEXISTE = "solicitud.modif.plazo.no.existe";
	private static final String SOLFINALPLAZONOEXISTE = "solicitud.final.plazo.no.existe";
	private static final String PLAZOCREADOAUTTRAM = "plazo.creado.aut.tram";
	private static final String PLAZOCERRADOAUTTRAM = "plazo.cerrado.aut.tram";
	private static final String PLAZOMODIFICADOAUTTRAM = "plazo.modificado.aut.tram";
	
	private transient ResourceBundle mensajesProperties;	
	
	@Autowired
	private ExpedientesService expedientesService;
	
	@Autowired
	private ValoresDominioService valoresDominioService;
	
	@Autowired
	private TramiteExpedienteService tramiteExpedienteService;
	
	@Autowired
	private PlazosExpdteService plazosExpdteService;
	
	@Autowired
	private CfgPlazosAutService cfgPlazosAutService;
	
	@Autowired
	private ObservacionesExpedientesService observacionesExpedientesService;
	
	@Setter
	private String nombreCsvExportar;
	
	
	/**
	 * Metodo para guardar la ultima fecha y el ultimo usuario de persistencia sobre cualquier dato del expediente
	 * @param expedientesAModificar
	 * @param fechaModificacion
	 * @param fechaCreacion
	 * @param usuModificacion
	 * @param usuCreacion
	 * @return
	 * @throws BaseException
	 */
	public Expedientes expedienteUltimaModificacion(Expedientes expedientesAModificar, Date fechaModificacion, Date fechaCreacion, String usuModificacion, String usuCreacion) throws BaseException {
		Expedientes expedienteAux = expedientesAModificar;
		expedienteAux.setFechaUltimaPersistencia(fechaModificacion!=null ? fechaModificacion : fechaCreacion);
		expedienteAux.setUsuUltimaPersistencia(usuModificacion!=null ? usuModificacion : usuCreacion);
		expedienteAux = expedientesService.guardar(expedienteAux);
		return expedienteAux;
	}
	
	
	/**
	 * FUNCION QUE GENERA DE FORMA AUTOMATICA LOS PLAZOS EN LA TABLA GE_PLAZOS_EXPDTE.
	 * @param idExp              IDE DEL EXPEDIENTE ACTUAL.
	 * @param idTramite          ID TRAMITE ACTUAL.
	 * @param origen             INDICA DESDE QUE PUNTO DEL TRAMITE SE LLAMA A LA FUNCION (E: EDICION; F: FINALIZAR)
	 * @param accionExpecial     INDICA SI SE TIENE QUE REALIZAR UNA FUNCION ESPECIAL.
	 * @param fechaInicioCalculo INDICA LA FECHA A PARTIR DE LA CUAL SE DEBE REALIZAR EL CALCULO DE LA FECHA LIMITE DEL PLAZO.
	 * @param tipoPlazo          INDICA EL TIPO DE PLAZO SOBRE EL QUE SE QUIERE ACTUAR (ACU: ACUERDO; RES: RESOLUCION; API: IINFORME API/ARCHIVO; PRES:PROPUESTA DE RESOLUCION)
	 * @param tipoPlazoTemp      INDICA EL TIPO DE AMBITO TEMPORAL A UTILIZAR PARA EL INCREMENTO DE PLAZO QUE SE INFORMA EN EL PARAMETRO SIGUIENTE (DN: DIAS NATURALES;DH: DIAS HABILES;S: SEMANAS; M: MESES)
	 * @param plazoAdicional     INDICA EL INCREMENTO DE PLAZO A SUMAR A LA FECHA LIMITE CALCULADA
	 * @param tipoLlamada        A: ACTUALIZACION; C: CONSULTA
	 * @return fechaLimite FECHA LIMITE CALCULADA O NULO SI SE PRODUCE ALGUN ERROR
	 * @throws BaseException 
	 */
	public Date generarPlazoExpdte(Long idTramite, String accionEspecial,Date fechaInicioCalculo, String tipoPlazo, 
									String tipoPlazoTemp, Integer plazoAdicional,String tipoLlamada, Expedientes expediente) throws BaseException {
		
		mensajesProperties = ResourceBundle.getBundle("es.juntadeandalucia.ctpda.gestionpdt.web.Messages_es");
		
		Date fechaLimite = null;
		ValoresDominio valorTipoExpdte = null;
		TipoTramite tipoTram = null;
		TipoTramite tipoTramSup = null;
		CfgPlazosAut cfgPlazoAut = null;
		PlazosExpdte plazosExpdte = null;
		TipoAccion tipoAccion = null;
		Boolean aviso = null;

		Long idPlazo = valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_TIPO_PLAZO, tipoPlazo).getId();

		if (expedientesService.obtener(expediente.getId()) != null && Constantes.COD_VAL_DOM_DN.equals(tipoPlazoTemp)) {				
				valorTipoExpdte = expedientesService.obtener(expediente.getId()).getValorTipoExpediente();	
				
				tipoTram = generarPlazoExpdteObtenerTipoTramite(idTramite);
				tipoTramSup = generarPlazoExpdteObtenerTipoTramiteSup(idTramite);
					
				cfgPlazoAut = obtenerRegistroCfgPlazoAut(idTramite, accionEspecial, valorTipoExpdte, idPlazo);
				if (cfgPlazoAut != null) {
					tipoAccion = cfgPlazoAut.getTipoAccion();
					aviso = cfgPlazoAut.getAviso();
	
					plazosExpdte = plazosExpdteService.findPlazosExpdteByExpTipPla(expediente.getId(),idPlazo);
	
					tipoAccion = gestionarTipoAccionSegunPlazo(tipoAccion, aviso, plazosExpdte);
					
					fechaLimite = calcularFechaLimite(fechaInicioCalculo, cfgPlazoAut.getPlazo(), plazoAdicional,Constantes.COD_VAL_DOM_DN);
	
					/** SI EL TIPO DE LLAMADA ES 'C' (CONSULTA) RETORNAMOS LA FECHA LIMITE SINREALIZAR NINGUNA ACCION ADICIONAL AL CÁLCULO DE LA FEHCA LIMITE. */
	
					if (Constantes.TIPO_LLAMADA_CONSULTA.equals(tipoLlamada)) {
						return fechaLimite;
					}
					/** SI EL TIPO DE LLAMADA ES 'A' (ACTUALIZACIÓN) Y EL TIPO DE ACCION ES 'C' (CREACIÓN)
					 */
					else if (Constantes.TIPO_LLAMADA_ACTUALIZACION.equals(tipoLlamada)) {
						
						if (TipoAccion.C.equals(tipoAccion)) {
							crearPlazoExpdte(fechaLimite, tipoTramSup, tipoTram, idPlazo, expediente);
						} else if (TipoAccion.M.equals(tipoAccion)) {
							modificarPlazoExpdte(plazosExpdte, fechaLimite, tipoTramSup, tipoTram);
						} else if (TipoAccion.F.equals(tipoAccion)) {
							finalizarPlazoExpdte(plazosExpdte, tipoTramSup, tipoTram);
							return null;
						}							
					}						
				}
			}
		return fechaLimite;
	}
	
	private TipoTramite generarPlazoExpdteObtenerTipoTramiteSup (Long idTramite) {
		TipoTramite tipoTramSup = null;
		if(idTramite != null) {
			TramiteExpediente tramiteExpedienteAux = tramiteExpedienteService.obtener(idTramite);
			if(tramiteExpedienteAux.getId() != null && tramiteExpedienteAux.getTramiteExpedienteSup() != null) {
				tipoTramSup = tramiteExpedienteAux.getTramiteExpedienteSup().getTipoTramite();
			}					
		}
		return tipoTramSup;
	}
	
	
	private TipoTramite generarPlazoExpdteObtenerTipoTramite (Long idTramite) {
		TipoTramite tipoTram = null;
		if(idTramite != null) {
			tipoTram = tramiteExpedienteService.obtener(idTramite).getTipoTramite();					
		}
		return tipoTram;
	}
	
	
	public CfgPlazosAut obtenerRegistroCfgPlazoAut(Long idTramite, String accionEspecial,ValoresDominio valorTipoExpdte, Long idPlazo) {
		List<CfgPlazosAut> listaCfgPlazosAut = null;
		CfgPlazosAut cfgPlazoAut = null;
		
		listaCfgPlazosAut = obtenerListaCfgPlazosAut(idTramite, accionEspecial, valorTipoExpdte, idPlazo);				

		/** TRAS REALIZAR LA BUSQUEDA DE DATOS PARA EL CALCULO DE PLAZOS EN LA TABLA GE_CFG_PLAZOS_AUT, VALORAMOS QUÉ HACER: 
		 * SI NO ENCONTRAMOS NINGUN REGISTRO CON LOS CRITERIOS INDICADOS: GENERAMOS
		 * ERROR INDICANDO QUE NO EXISTE CONFIGURACION DE PLAZO AUTOMÁTICO.
		 */
		if (listaCfgPlazosAut == null || listaCfgPlazosAut.isEmpty()) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(NOEXISTEREGISTROCONFIGAUT));
			PrimeFaces.current().dialog().showMessageDynamic(message);
		}
		
		/**
		 * SI ENCONTRAMOS MAS DE UN REGISTRO CON LOS CRITERIOS INDICADOS: GENERAMOS
		 * ERROR INDICANDO QUE EXISTE MAS DE UNA CONFIGURACION.
		 */
		else if (listaCfgPlazosAut.size() > 1) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MASDEUNREGISTROCONFIGAUT));
			PrimeFaces.current().dialog().showMessageDynamic(message);
		}
		
		/**
		 * SI ENCONTRAMOS UN SOLO REGISTRO DE CONFIGURACION: DEBEMOS ACCEDER A LA TABLA
		 * GE_PLAZOS_EXPDTE
		 */
		else {
			cfgPlazoAut = listaCfgPlazosAut.get(0);
		}
		return cfgPlazoAut;
	}
	
	
	public List<CfgPlazosAut> obtenerListaCfgPlazosAut(Long idTramite, String accionEspecial, ValoresDominio valorTipoExpdte, Long idPlazo) {
		List<CfgPlazosAut> listaCfgPlazosAut = null;
		TipoTramite tipoTram = null;
		TipoTramite tipoTramSup = null;

		if(idTramite != null) {
			tipoTram = tramiteExpedienteService.obtener(idTramite).getTipoTramite();
			TramiteExpediente tramiteExpedienteAux = tramiteExpedienteService.obtener(idTramite);
			if(tramiteExpedienteAux.getId() != null && tramiteExpedienteAux.getTramiteExpedienteSup() != null) {
				tipoTramSup = tramiteExpedienteAux.getTramiteExpedienteSup().getTipoTramite();
			}	
				if (tipoTramSup != null) {
					listaCfgPlazosAut = cfgPlazosAutService.findCfgPlazosAutTramSup(valorTipoExpdte.getId(),tipoTram.getId(), tipoTramSup.getId(), OrigenAut.E, idPlazo, accionEspecial);
				}else{
					if (accionEspecial == null) {
						listaCfgPlazosAut = cfgPlazosAutService.findCfgPlazosAutSinTramSup(valorTipoExpdte.getId(),	tipoTram.getId(), OrigenAut.E, idPlazo);
					} else {
						listaCfgPlazosAut = cfgPlazosAutService.findCfgPlazosAutSinTramSup(valorTipoExpdte.getId(),	tipoTram.getId(), OrigenAut.E, idPlazo, accionEspecial);
					}	
				}
				
		}else {
			listaCfgPlazosAut = obtenerListaCfgPlazosAutAux(accionEspecial,valorTipoExpdte,idPlazo);
		}
		
		if(listaCfgPlazosAut.isEmpty()) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString("no.existe.registro.config"));
			String codigoTramite = (tipoTram!=null)?tipoTram.getCodigo():"noExisteTipoTramiteAsociado";
			log.warn("obtenerListaCfgPlazosAut " + codigoTramite + ", " + accionEspecial + ", " + valorTipoExpdte.getCodigo() + ", " + idPlazo + " - " + message.getDetail());
		}

		return listaCfgPlazosAut;
	}
	
	private List<CfgPlazosAut> obtenerListaCfgPlazosAutAux (String accionEspecial,ValoresDominio valorTipoExpdte, Long idPlazo) {
		List<CfgPlazosAut> listaCfgPlazosAut = null;
		if (accionEspecial == null) {
			listaCfgPlazosAut = cfgPlazosAutService.findCfgPlazosAutSinTramite(valorTipoExpdte.getId(), OrigenAut.E, idPlazo);
		} else {
			listaCfgPlazosAut = cfgPlazosAutService.findCfgPlazosAutSinTramite(valorTipoExpdte.getId(), OrigenAut.E, idPlazo, accionEspecial);
		}
		
		return listaCfgPlazosAut;
	}
	
	
	public TipoAccion gestionarTipoAccionSegunPlazo(TipoAccion tipoAccion, Boolean aviso, PlazosExpdte plazosExpdte) {
		/** SI EL TIPO DE ACCION ES 'C' (CREACIÓN) Y EL INDICADOR DE AVISO ES 'SI'. */
		if (tipoAccion.equals(TipoAccion.C)) {
			/** SI YA EXISTE UN REGISTRO EN LA TABLA DE PLAZOS => RECALCULAMOS FECHAS Y GENERAMOS UN AVISO DE ERROR.
			 */
			if (plazosExpdte != null) {
				tipoAccion = TipoAccion.M;
			}
			if (plazosExpdte != null && Boolean.TRUE.equals(aviso)) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(SOLCREACIONPLAZOEXISTE));
				PrimeFaces.current().dialog().showMessageDynamic(message);
			}
		}
		/** SI EL TIPO DE ACCION ES 'M' (MODIFICACIÓN) Y EL INDICADOR DE AVISO ES 'SI'. */
		else if (tipoAccion.equals(TipoAccion.M)) {
			/** SI NO EXISTE UN REGISTRO EN LA TABLA DE PLAZOS => GENERAMOS UN AVISO DE ERROR.
			 */
			if (plazosExpdte == null) {
				tipoAccion = TipoAccion.C;
			}
			if (plazosExpdte == null && Boolean.TRUE.equals(aviso)) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(SOLMODIFPLAZONOEXISTE));
				PrimeFaces.current().dialog().showMessageDynamic(message);
			}
		}
		/** SI EL TIPO DE ACCION ES 'F' (FINALIZACIÓN) Y EL INDICADOR DE AVISO ES 'SI' Y SI NO EXISTE UN REGISTRO EN LA TABLA DE PLAZOS => GENERAMOS UN AVISO DE ERROR. */
		else if (tipoAccion.equals(TipoAccion.F) && Boolean.TRUE.equals(aviso) && (plazosExpdte == null)) {
			tipoAccion = TipoAccion.C;
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(SOLFINALPLAZONOEXISTE));
			PrimeFaces.current().dialog().showMessageDynamic(message);
		}

		return tipoAccion;
	}
	
	
	public Date calcularFechaLimite(Date fechaInicioCalculo, Integer numDias, Integer plazoAdicional,String tipoPlazoTemp) {
		Date fechaLimite = null;
		
		if (Constantes.COD_VAL_DOM_DN.equals(tipoPlazoTemp) && fechaInicioCalculo != null){
				fechaLimite = FechaUtils.sumarDiasAFecha(fechaInicioCalculo, numDias);				
				if (plazoAdicional != null && plazoAdicional > 0) {
					fechaLimite = FechaUtils.sumarDiasAFecha(fechaLimite, plazoAdicional);
				}
			}	
		return fechaLimite;
	}
	
	
	public void crearPlazoExpdte(Date fechaLimite, TipoTramite tipoTramSup, TipoTramite tipoTram, long idPlazo, Expedientes expediente) throws BaseException {
		PlazosExpdte plazosExpdteNuevo = null;
		plazosExpdteNuevo = new PlazosExpdte();
		plazosExpdteNuevo.setExpediente(expedientesService.obtener(expediente.getId()));

		plazosExpdteNuevo.setValorTipoPlazo(valoresDominioService.obtener(idPlazo));
		plazosExpdteNuevo.setFechaLimite(fechaLimite);
		plazosExpdteNuevo.setCumplido(false);
		plazosExpdteNuevo.setOrigenInicial(Origenes.A);
		plazosExpdteNuevo.setOrigenFinal(Origenes.A);
		
		String observaciones = generaObservaciones(tipoTram, tipoTramSup, TipoAccion.C,null);
		
		if(observaciones != null)
		{
			ObservacionesExpedientes obsExp = observacionesExpedientesService.guardarObservacionesExpedientesSinFecha(null, observaciones, Constantes.COD_VAL_DOM_TIPOBS_PLA, expediente);

			plazosExpdteNuevo.setObservaciones(obsExp);
			plazosExpdteNuevo.setActivo(true);
			plazosExpdteService.guardar(plazosExpdteNuevo);	
			
			obsExp.setPlazoExpdte(plazosExpdteNuevo);
			observacionesExpedientesService.guardar(obsExp);
		}else
		{
			plazosExpdteNuevo.setActivo(true);
			plazosExpdteService.guardar(plazosExpdteNuevo);
		}
	}

	public void modificarPlazoExpdte(PlazosExpdte plazosExpdte, Date fechaLimite, TipoTramite tipoTramSup,TipoTramite tipoTram) throws BaseException {
		String observaciones = generaObservaciones(tipoTram, tipoTramSup, TipoAccion.M, plazosExpdte.getFechaLimite());

		ObservacionesExpedientes obsExp = plazosExpdte.getObservaciones();
		if (obsExp.getTexto() != null) {
		observaciones = obsExp.getTexto() + "\n\n" + observaciones;}
		observacionesExpedientesService.actualizarObservacionesExpedientes(observaciones, obsExp);

		plazosExpdte.setFechaLimite(fechaLimite);
		plazosExpdte.setOrigenFinal(Origenes.A);
		plazosExpdteService.guardar(plazosExpdte);
	}
	
	
	public String generaObservaciones(TipoTramite tipoTram, TipoTramite tipoTramSup, TipoAccion tipoAccion,Date fechaAnterior) {
		String observaciones = "";
		String mensaje = "";

		if (TipoAccion.C.equals(tipoAccion)) {
			mensaje = mensajesProperties.getString(PLAZOCREADOAUTTRAM);
		} else if (TipoAccion.M.equals(tipoAccion)) {
			mensaje = mensajesProperties.getString(PLAZOMODIFICADOAUTTRAM);
		} else if (TipoAccion.F.equals(tipoAccion)) {
			mensaje = mensajesProperties.getString(PLAZOCERRADOAUTTRAM);
		}

		/** SI EL TRAMITE TIENE TRAMITE SUPERIOR */
		if(tipoTram != null) {
			if (tipoTramSup != null) {
				observaciones = mensaje + " " + tipoTramSup.getDescripcion() + " y subtrámite " + tipoTram.getDescripcion() + ".";
			} else {
				observaciones = mensaje + " " + tipoTram.getDescripcion() + ".";
			}
		}else {
			observaciones = mensajesProperties.getString("plazo.creado.alta.expediente");
		}
		
		Usuario usuario = (Usuario) JsfUtils.getSessionAttribute(Constantes.USUARIO);
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		SimpleDateFormat formatoCorto = new SimpleDateFormat("dd/MM/yyyy");
		String formattedDate = formatter.format(FechaUtils.fechaYHoraActualDate());

		observaciones = observaciones + " Usuario: " + usuario.getNombreAp() + " y fecha: " + formattedDate + ".";
		if (TipoAccion.M.equals(tipoAccion) && fechaAnterior != null) {
			observaciones = observaciones + " Fecha límite anterior: " + formatoCorto.format(fechaAnterior) + ".";
		}

		return observaciones;
	}
	
	
	public void finalizarPlazoExpdte(PlazosExpdte plazosExpdte, TipoTramite tipoTramSup,TipoTramite tipoTram) throws BaseException {
		String observaciones = generaObservaciones(tipoTram, tipoTramSup, TipoAccion.F, null);
		this.finalizarPlazoExpdte(plazosExpdte, observaciones);
	}
	
	public void finalizarPlazoExpdte(PlazosExpdte plazosExpdte, String observaciones) throws BaseException {
		plazosExpdte.setCumplido(true);
		plazosExpdte.setOrigenFinal(Origenes.A);
		ObservacionesExpedientes obsExp = plazosExpdte.getObservaciones();
		if (obsExp.getTexto() != null) {
		observaciones = obsExp.getTexto() + "\n\n" + observaciones;}
		obsExp = observacionesExpedientesService.guardarObservacionesExpedientesSinFecha(obsExp, observaciones, Constantes.COD_VAL_DOM_TIPOBS_PLA, plazosExpdte.getExpediente());
		plazosExpdte.setObservaciones(obsExp);

		plazosExpdteService.guardar(plazosExpdte);
		obsExp.setPlazoExpdte(plazosExpdte);
		observacionesExpedientesService.guardar(obsExp);		
	}
	
	public String getNombreCsvExportar(String listado) {
		Usuario usuario = (Usuario)JsfUtils.getSessionAttribute(Constantes.USUARIO);
		nombreCsvExportar = listado+"_"+usuario.getLogin()+"_"+FechaUtils.stringFechaYHoraCompleta();
		return nombreCsvExportar;
	}

	public void postProcessXLS(Object document) {
		SXSSFWorkbook wb = (SXSSFWorkbook) document;
        SXSSFSheet sheet = wb.getSheetAt(0);
        SXSSFRow header = sheet.getRow(0);

        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
            SXSSFCell cell = header.getCell(i);

            cell.setCellStyle(cellStyle);
        }
    }
	
}
