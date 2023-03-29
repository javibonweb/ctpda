package es.juntadeandalucia.ctpda.gestionpdt.web.core;

import java.io.Serializable;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

@Component
@ApplicationScope
public class Constantes  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	/**
	 * PERMISOS DEFINIDOS EN EL SISTEMA
	 * */
	
	public static final String PERMISO_LIST_DOM = "LIST_DOM";
	public static final String PERMISO_LIST_VALDOM = "LIST_VALDOM";
	public static final String PERMISO_LIST_TIPAGRUP = "LIST_TIPAGRUP";
	public static final String PERMISO_LIST_SUJOBL = "LIST_SUJOBL";
	public static final String PERMISO_NEW_SUJOBL = "NEW_SUJOBL";
	public static final String PERMISO_SAVE_SUJOBL = "SAVE_SUJOBL";
	public static final String PERMISO_LIST_PERS = "LIST_PERS";
	public static final String PERMISO_NEW_PERS = "NEW_PERS";
	public static final String PERMISO_SAVE_PERS = "SAVE_PERS";
	public static final String PERMISO_MENU = "VALDOM_MENU";

	
	
	public static final String PERMISO_NEW_VALORDOM = "NEW_VALORDOM";
	public static final String PERMISO_CONS_VALORDOM = "CONS_VALORDOM";
	public static final String PERMISO_EDIT_VALORDOM = "EDIT_VALORDOM";
	public static final String PERMISO_DESAC_VALORDOM = "DESAC_VALORDOM";
	public static final String PERMISO_ACT_VALORDOM  = "ACT_VALORDOM";
	public static final String PERMISO_SAVE_VALORDOM = "SAVE_VALORDOM";

	public static final String PERMISO_NEW_TIPAGRUP  = "NEW_TIPAGRUP";
	public static final String PERMISO_CONS_TIPAGRUP  = "CONS_TIPAGRUP";
	public static final String PERMISO_EDIT_TIPAGRUP  = "EDIT_TIPAGRUP";
	public static final String PERMISO_DESAC_TIPAGRUP  = "DESAC_TIPAGRUP";
	public static final String PERMISO_ACT_TIPAGRUP  = "ACT_TIPAGRUP"; 
	public static final String PERMISO_SAVE_TIPAGRUP  = "SAVE_TIPAGRUP";

	public static final String PERMISO_CONS_SUJOBL  = "CONS_SUJOBL";
	public static final String PERMISO_EDIT_SUJOBL  = "EDIT_SUJOBL";
	public static final String PERMISO_DESAC_SUJOBL   = "DESAC_SUJOBL";
	public static final String PERMISO_ACT_SUJOBL  = "ACT_SUJOBL";
	public static final String PERMISO_ERRO_SUJOBL  = "ERRO_SUJOBL";
	

	public static final String PERMISO_LIST_PARAM  = "LIST_PARAM";
	public static final String PERMISO_NEW_PARAM  = "NEW_PARAM";
	public static final String PERMISO_CONS_PARAM  = "CONS_PARAM";
	public static final String PERMISO_EDIT_PARAM  = "EDIT_PARAM";
	public static final String PERMISO_DESAC_PARAM  = "DESAC_PARAM";
	public static final String PERMISO_ACT_PARAM  = "ACT_PARAM";
	public static final String PERMISO_SAVE_PARAM  = "SAVE_PARAM";

	public static final String PERMISO_CONS_PERS  = "CONS_PERS";
	public static final String PERMISO_EDIT_PERS  = "EDIT_PERS";
	public static final String PERMISO_DESAC_PERS  = "DESAC_PERS";

	public static final String PERMISO_DEL_PERSREP  = "DEL_PERSREP";
	public static final String PERMISO_BUS_PERSREP  = "BUS_PERSREP";

	public static final String PERMISO_CONS_EXP  = "CONS_EXP";
	public static final String PERMISO_EDIT_EXP  = "EDIT_EXP";
	public static final String PERMISO_LIST_EXP = "LIST_EXP";
	public static final String PERMISO_NEW_EXP = "NEW_EXP";
	public static final String PERMISO_SAVE_EXP = "SAVE_EXP";
	
	public static final String PERMISO_VIEW_XPC = "VIEW_XPC";

	public static final String PERMISO_BUS_EXPREL  = "BUS_EXPREL";
	public static final String PERMISO_DEL_EXPREL  = "DEL_EXPREL";
	public static final String PERMISO_EDIT_EXPREL  = "EDIT_EXPREL";

	public static final String PERMISO_NEW_EXPOBS  = "NEW_EXPOBS";

	public static final String PERMISO_BUS_EXPSUJOBL  = "BUS_EXPSUJOBL";
	public static final String PERMISO_DEL_EXPSUJOBL  = "DEL_EXPSUJOBL";
	public static final String PERMISO_EDIT_EXPSUJOBL  = "EDIT_EXPSUJOBL";
	public static final String PERMISO_BUS_EXPSUJOBLDPD  = "BUS_EXPSUJOBLDPD";
	public static final String PERMISO_DEL_EXPSUJOBLDPD  = "DEL_EXPSUJOBLDPD";

	public static final String PERMISO_BUS_EXPPER  = "BUS_EXPPER";
	public static final String PERMISO_NEW_EXPPER  = "NEW_EXPPER";
	public static final String PERMISO_DEL_EXPPER  = "DEL_EXPPER";
	public static final String PERMISO_EDIT_EXPPER  = "EDIT_EXPPER";
	public static final String PERMISO_BUS_EXPPERREP  = "BUS_EXPPERREP";
	public static final String PERMISO_DEL_EXPPERREP  = "DEL_EXPPERREP";
	
	public static final String PERMISO_SAVE_EXPSITLIB  = "SAVE_EXPSITLIB";
	public static final String PERMISO_CREA_EXPEPC = "CREA_EXP_EPC";
	public static final String PERMISO_DOCS_EXPEPC = "DOCS_EXP_EPC";	
	public static final String PERMISO_EDIT_MOTIVOEXP  = "EDIT_MOTIVO";

	public static final String PERMISO_CONS_PEREXP  = "CONS_PEREXP";
	
	public static final String PERMISO_CONS_TRAMITE_EVOL = "CONS_TRAM_EVOL";
	
	public static final String PERMISO_VIEW_TRAM_AB = "VIEW_TRAM_AB";
	
	public static final String PERMISO_VIEW_NOTIF_AB = "VIEW_NOTIF_AB";
	public static final String PERMISO_VIEW_NOT_PDTE = "VIEW_NOT_PDTE";
	public static final String PERMISO_MOD_DAT_NOT = "MOD_DAT_NOT";																															
	public static final String PERMISO_VIEW_FIRMAS_AB = "VIEW_FIRMAS_AB";
	
	public static final String PERMISO_EDIT_DESCIPCIONEXP = "EDIT_DESCIPCIONEXP";
	public static final String PERMISO_RCO_VIEW_CAMPOSESP = "RCO_VIEW_CAMPOSESP";
	public static final String PERMISO_RCE_VIEW_CAMPOSESP = "RCE_VIEW_CAMPOSESP";
	public static final String PERMISO_PSAN_VIEW_CAMPOSESP = "PSAN_VIEW_CAMPOSESP";
	public static final String PERMISO_XPC_VIEW_CAMPOSESP = "XPC_VIEW_CAMPOSESP";
	public static final String PERMISO_RCO_EDIT_RESPEXP = "RCO_EDIT_RESPEXP";
	public static final String PERMISO_RCE_EDIT_RESPEXP = "RCE_EDIT_RESPEXP";
	public static final String PERMISO_PSAN_EDIT_RESPEXP = "PSAN_EDIT_RESPEXP";
	public static final String PERMISO_XPC_EDIT_RESPEXP = "XPC_EDIT_RESPEXP";
	
	/**
	 * PERMISOS TRAMITACION EXPEDIENTE
	 * */
	public static final String PERMISO_NEW_EXPTRAM  = "NEW_EXPTRAM";
	public static final String PERMISO_NEW_EXPTRAMLIB  = "NEW_EXPTRAMLIB";
	public static final String PERMISO_NEW_EXPTAREA  = "NEW_EXPTAREA";
	public static final String PERMISO_NEW_PLAZOEXP  = "NEW_PLAZOEXP";
	public static final String PERMISO_SAVE_PLAZOEXP = "SAVE_PLAZOEXP";
	public static final String PERMISO_DARCUMP_PLAZOEXP = "DARCUMP_PLAZOEXP";
	public static final String PERMISO_ACT_PLAZOEXP = "ACT_PLAZOEXP";
	public static final String PERMISO_DESAC_PLAZOEXP = "DESAC_PLAZOEXP";
	public static final String PERMISO_REHAB_PLAZOEXP = "REHAB_PLAZOEXP";
	public static final String PERMISO_EDIT_EXPFIN = "MODSITFINAL";
	public static final String PERMISO_REHAB_TRAMITE = "REHABTRAM";
	public static final String PERMISO_EDIT_EXTRACTO  = "EDIT_EXTRACTO";
	public static final String PERMISO_EDIT_ANTECEDENTES  = "EDIT_ANTECEDENTES";
	
	//Resolución
	public static final String PERMISO_MENU_RESOL = "MENU_RESOL";
	public static final String PERMISO_EDIT_RESOL = "EDIT_RESOL";
	public static final String PERMISO_VER_RESOL = "VER_RESOL";
	
	public static final String PERMISO_EDIT_RESOL_EXP  = "EDIT_RESOL_EXP";  
	public static final String PERMISO_DEL_RESOL_EXP   = "DEL_RESOL_EXP";   
	public static final String PERMISO_EDIT_RESOL_PERS = "EDIT_RESOL_PERS"; 
	public static final String PERMISO_DEL_RESOL_PERS  = "DEL_RESOL_PERS";  
	public static final String PERMISO_EDIT_RESOL_SUJ  = "EDIT_RESOL_SUJ";  
	public static final String PERMISO_DEL_RESOL_SUJ   = "DEL_RESOL_SUJ";   
	public static final String PERMISO_EDIT_RESOL_DOC  = "EDIT_RESOL_DOC";  
	public static final String PERMISO_DEL_RESOL_DOC   = "DEL_RESOL_DOC";   
	
	
	//Tareas
	public static final String PERMISO_MI_MESA = "LIST_MIMESA";
	
	public static final String PERMISO_CREAR_TAREADOCEXP  = "CREAR_TAREADOCEXP";
	public static final String PERMISO_VER_TAREASOTROS  = "VER_TAREASOTROS";
	public static final String PERMISO_VER_TAREASEQUIPO  = "VER_TAREASEQUIPO";
	public static final String PERMISO_VER_TAREASNOACTIVAS = "VER_TAREASNOACTIVAS";
	public static final String PERMISO_VER_TAREASNOPENDIENTES = "VER_TAREASNOPENDIENTES";
	
	public static final String PERMISO_TRAMITAR_TAREAEXP = "TRAM_TAREAEXP";
	public static final String PERMISO_EDIT_TAREADOC = "EDIT_TAREADOC";
	public static final String PERMISO_EDIT_TAREA = "EDIT_TAREA";
	public static final String PERMISO_ELIM_TAREA = "ELIM_TAREA";
	public static final String PERMISO_REACT_TAREA = "REACT_TAREA";
	public static final String PERMISO_REHAB_TAREA = "REHAB_TAREA";
	public static final String PERMISO_RECHAZA_TAREA = "RECHAZA_TAREA";
	public static final String PERMISO_ACCEDER_PESTA_TAREA = "LIST_TAREASEXP";
	
	public static final String COD_TIP_TAR_TRAM_FYN = "FYN";
	
	public static final String PERMISO_TAREAS_ENVIADAS_PDTES = "LIST_TAREAS_ENVIADAS_PDTES";

	
	/**
	 * PERMISOS DOCUMENTOS EXPEDIENTE
	 * */	
	public static final String PERMISO_NEW_EXPINCDOC = "NEW_EXPINCDOC";
	public static final String PERMISO_NEW_EXPSUBDOC = "NEW_EXPSUBDOC";
	public static final String PERMISO_NEW_EXPVINCDOC = "NEW_EXPVINCDOC";
	public static final String PERMISO_NEW_EXPEMPDOC = "NEW_EXPEMPDOC";
	
	public static final String PERMISO_NEW_EXPACT = "NEW_EXPACT";
	public static final String PERMISO_NEW_EXPTAREADET = "NEW_EXPTAREADET";
	public static final String PERMISO_NEW_EXPPLAZODET = "NEW_EXPPLAZODET";
	public static final String PERMISO_NEW_EXPFINTRAM = "NEW_EXPFINTRAM";
	public static final String PERMISO_DESAC_EXPTRAM = "DESAC_EXPTRAM";

	public static final String PERMISO_CONS_EXPDOC = "CONS_EXPDOC";
	public static final String PERMISO_EDIT_EXPDOC = "EDIT_EXPDOC";
	public static final String PERMISO_DESAC_EXPDOC = "DESAC_EXPDOC";
	public static final String PERMISO_NEW_EXPDOCTRAB = "NEW_EXPDOCTRAB";
	public static final String PERMISO_EDIT_EXPDOCCONF = "EDIT_EXPDOCCONF";
	public static final String PERMISO_EDIT_EXPTIPODOC = "EDIT_EXPTIPODOC";

	public static final String PERMISO_NEW_EXPDOCGEN = "NEW_EXPDOCGEN";
	
	public static final String PERMISO_VER_DOCSOCULTOS = "VER_DOCS_OCULTOS";
	
	
	/** * PERMISOS PARA PLAZOS DEL EXPEDIENTE */
	public static final String PERMISO_LIST_PLAZOEXP = "LIST_PLAZOEXP";
	public static final String PERMISO_EDIT_PLAZOEXP = "EDIT_PLAZOEXP";
	public static final String PERMISO_CONS_PLAZOEXP = "CONS_PLAZOEXP";
	
	
	
	/**
	 * COOKIES DE SESION
	 */
	public static final String COOKIE_UID = "uid";
	public static final String COOKIE_CODPERFIL = "codPerfil";
	
	
	/**
	 * Prefijos JSF de formulario, pestañas y otros elementos comunes 
	 */
	public static final String JSFID_FORMEXP = "formFormularioExpedientes";
	public static final String JSFID_PESTANAS_EXP = "formFormularioExpedientes:tabViewPestanasExpediente";
	public static final String JSFID_MSG_DIALOG = "primefacesmessagedlg";
	
	
	/**
	 * ATRIBUTOS A USAR PARA LOS VALORES ALMACENADOS EN SESSION
	 * */
	public static final String NUMERO_SALTOS = "numeroSaltos";
	public static final String USUARIO = "usuario";
	public static final String CONEXION_USUARIO = "conexionUsuario";
	public static final String USUARIO_LOGADO = "usuarioLogado";
	public static final String COD_PERFIL_USUARIO = "codPerfilUsuario";
	public static final String LISTACODIGOPERMISOS = "listaCodigoPermisos";
	public static final String LISTA_TRAM_EXP = "listaTramExp";	
	public static final String LISTA_SUB_TRAM_EXP = "listaSubTramExp";
	public static final String LISTA_SUB_TRAM_EXP_TRDPD = "listaSubTramExpTrdpd";
	public static final String LISTA_SUB_TRAM_EXP_OTRO = "listaSubTramExpOtro";
	public static final String ULTIMO_EXPEDIENTE_CREADO = "ultimoExpedienteCreado";
	public static final String ULTIMO_TIPO_EXPEDIENTE_CREADO = "ultimoTipoExpedienteCreado";	
	public static final String VUELTA_PERSONAS = "vueltaPersonas";
	public static final String VUELTA_LISTADO_ENTRADAS = "vueltaListadoEntradas";
	public static final String VUELTA_TRAMITES_ABIERTOS = "vueltaTramitesAbiertos";
	public static final String VUELTA_NOTIFICACIONES_ABIERTAS = "vueltaNotificacionesAbiertas";
	public static final String VUELTA_NOTIFICACIONES_PENDIENTES = "vueltaNotificacionesPendientes";																						
	public static final String VUELTA_FIRMAS_ABIERTAS = "vueltaFirmasAbiertas";
	public static final String IDPERSONASELECCIONADA = "idPersonaSeleccionada";
	public static final String VUELTAS_CONTROLPLAZOS = "vueltaControlPlazos";
	public static final String IDPLAZOSELECCIONADO = "idControlPlazosSeleccionado";
	public static final String ULTIMO_EXPEDIENTE_FINALIZADO = "ultimoExpedienteFinalizado";
	public static final String VUELTA_EXPRELACIONADOS = "vueltaExpRelacionados";
	public static final String IDEXPEDIENTERELACIONADOSELECCIONADO = "idExpedienteRelacionadoSeleccionado";
	public static final String VUELTA_LISTADO_TAREAS_ENVIADAS_PDTES = "vueltaListadoTareasEnviadasPdtes";
	
	
	/**
	 * ABREVIATURAS TIPOS DE TRAMITES 
	 **/
	
	public static final String TIP_TRAM_OTRO = "OTRO";
	public static final String TIP_TRAM_SUB = "SUB";
	public static final String TIP_TRAM_EDOC = "EDOC";
	public static final String TIP_TRAM_GDOC = "GDOC";
	public static final String TIP_TRAM_REV = "REV";
	public static final String TIP_TRAM_FIRM = "FIRM";
	public static final String TIP_TRAM_NOT = "NOT";
	public static final String TIP_TRAM_COM = "COM";
	public static final String TIP_TRAM_INIP = "INIP";
	public static final String TIP_TRAM_TRA = "TRA";
	public static final String TIP_TRAM_TRDPD = "TRDPD";
	public static final String TIP_TRAM_TRREC = "TRREC";
	public static final String TIP_TRAM_ACADM = "ACADM";
	public static final String TIP_TRAM_REQINF = "REQINF";
	public static final String TIP_TRAM_ENESP = "ENESP";
	public static final String TIP_TRAM_RESP = "RESP";
	public static final String TIP_TRAM_RESOL = "RESOL";
	public static final String TIP_TRAM_REQCUMPL = "REQCUMPL";
	public static final String TIP_TRAM_INFAPI = "INFAPI";
	public static final String TIP_TRAM_REQAVIS = "REQAVIS";
	public static final String TIP_TRAM_ACIAPI = "ACIAPI";
	public static final String TIP_TRAM_ACADMHE = "ACADMHE";
	public static final String TIP_TRAM_ACNOADM = "ACNOADM";
	public static final String TIP_TRAM_PRES = "PRES";
	public static final String TIP_TRAM_PWEB = "PWEB";
	public static final String TIP_TRAM_ALEG = "ALEG";
	public static final String TIP_TRAM_INIPSAN = "INIPSAN";
	public static final String TP_TRAM_ALEGPR = "ALEGPR";
 
	
	/**
	 * CODIGOS TIPOS DE DOCUMENTOS 
	 **/
	
	public static final String TIP_DOC_OTRO = "OTR";
	public static final String TIP_DOC_E = "E";
	public static final String TIP_DOC_T = "T";
	
	
	public static final String EXTENSION_PDF = "pdf";
	public static final String EXTENSION_ODT= "odt";
	
	public static final String MIME_PDF = "application/pdf";
	public static final String MIME_ODT = "application/vnd.oasis.opendocument.text"; 													 
																				  
  
	
	/**
	 * OTROS 
	 **/
	
	public static final String EDITABLE = "editable";
	
	/**
	 * COMPORTAMIENTO DE TRAMITES
	 */
	public static final String C000 = "C000";
	public static final String C002 = "C002";
	public static final String C001 = "C001";
	public static final String C003 = "C003";
	public static final String C004 = "C004";
	public static final String C005 = "C005";
	public static final String C006 = "C006";
	public static final String C007 = "C007";
	public static final String C008 = "C008";
	public static final String C009 = "C009";
	public static final String C010 = "C010";
	public static final String C012 = "C012";
	public static final String C011 = "C011";
	public static final String C013 = "C013";
	public static final String C014 = "C014";
	public static final String C017 = "C017";
	public static final String C018 = "C018";
	public static final String C019 = "C019";
	public static final String C020 = "C020";	
	public static final String C021 = "C021";
	public static final String C023 = "C023";
	public static final String C024 = "C024";

	

	/**
	 * TIPOS DE OBSERVACIONES
	 * */
	
	public static final String COD_VAL_DOM_TIPOBS_EXP = "EXP";
	public static final String COD_VAL_DOM_TIPOBS_TRA = "TRA";
	public static final String COD_VAL_DOM_TIPOBS_TAR = "TAR";
	public static final String COD_VAL_DOM_TIPOBS_PLA = "PLA";
	
	public static final String COD_DOM_TIPOBS = "TIP_OBS";

	
	
	/**
	 * COMPORTAMIENTOS TRAMITES
	 * */
	
	public static final String C015 = "C015";
	public static final String C016 = "C016";
	
	public static final String COD_VAL_DOM_AUTCON = "AUTCON";
	public static final String COD_VAL_DOM_DPD = "DPD";
	public static final String COD_VAL_DOM_PERS = "PERS";
	public static final String COD_VAL_DOM_SUJOBL = "SUJOBL";
	public static final String COD_VAL_DOM_AEPD = "AEPD";
	public static final String COD_VAL_DOM_NOT = "NOT";
	
	public static final String COD_DOM_TIP_INT = "TIP_INT";													
	public static final String COD_DOM_TIP_IDE = "TIP_IDE";
	public static final String COD_DOM_TIP_FIRM = "TIP_FIRM";
	public static final String COD_DOM_CAN_ENT = "CAN_ENT";
	public static final String COD_DOM_CAN_SAL = "CAN_SAL";
	public static final String COD_DOM_CAN_COM_INF = "CANCOMINF";
	public static final String COD_DOM_TIP_ADM = "TIP_ADM";
	public static final String COD_DOM_MOT_INADM = "MOT_INADM";

	public static final String COD_VAL_DOM_ACU = "ACU";
	public static final String COD_VAL_DOM_RES = "RES";
	public static final String COD_VAL_DOM_PRES = "PRES";
	public static final String COD_VAL_DOM_PRSC = "PRSC";
	
	/** CODIGO TIPO ADMINISION: INADMISION **/
	public static final String COD_VAL_DOM_INA = "INA";
	public static final String COD_VAL_DOM_ADM = "ADM";
	public static final String COD_VAL_DOM_AHE = "AHE";	
	
	
	public static final String COD_VAL_DOM_REC = "REC";
	
	
	public static final String COD_DOM_RES_NOTIF = "RES_NOTIF";
	
	/**
	 * CODIGOS DE DOMINIOS USADOS EN EL SISTEMA
	 */
	public static final String COD_TIPO_EXPEDIENTE = "TIP_EXP";
	public static final String COD_TIPO_PLAZO = "TIP_PLA";
	public static final String COD_TIP_PLA_TEMP = "TIP_PLA_TEMP";
	public static final String COD_SENT_RESOL = "SENT_RESOL";
	public static final String COD_SERIESRESOL = "SERIESRESOL";
	public static final String COD_TIPORESOL = "TIPORESOL";
	public static final String COD_SIT = "SIT";	
	public static final String COD_MOT_REL = "MOT_REL";
	public static final String COD_DOM_PROP_API = "PROP_API";
	public static final String COD_DOM_TIP_ACC = "TIP_ACC";
	public static final String DOMINIO_TIPO_INTERESADO = "TIP_INT";
	public static final String COD_RES_NOTIF = "RES_NOTIF";
	public static final String COD_DOM_GRAV = "GRAV";
	public static final String COD_DOM_ORIGREC = "ORIGREC";
	
	
	/**
	 * TIPOS PLAZOS TEMPORALES
	 * */
	
	public static final String COD_VAL_DOM_DN = "DN";
	
	/**
	 * RESULTADOS RESOLUCION
	 * */
	public static final String COD_VAL_DOM_PDTE = "PDTE";

	
	/**
						 
	  
 
													  

 
	
	 * TIPOS LLAMADAS - CALCULO FECHA LIMITE
	 * */
	
	public static final String TIPO_LLAMADA_CONSULTA =  "C";
	public static final String TIPO_LLAMADA_ACTUALIZACION =  "A";
	
	
	/**
	 * CONTEXTOS VOLVER
	 */
	public static final String VOLVERMESATAREAS = "_volverMesaTareas_";
	public static final String VOLVERTRAMITESABIERTOS = "_volverTramitesAbiertos_";
	public static final String VOLVERNOTIFICACIONESABIERTAS = "_volverNotificacionesAbiertas_";
	public static final String VOLVERNOTIFICACIONESPENDIENTES = "_volverNotificacionesPendientes_";																							
	public static final String VOLVERFIRMASABIERTAS = "_volverFirmasAbiertas_";
	public static final String VOLVERRESOLUCION = "_volverResolucion_";
	public static final String VOLVERLISTADOENTRADAS = "_volverListadoEntradas_";
	public static final String VOLVERLISTADOTAREASENVIADAS = "_volverListadoTareasEnviadas_";


	public static final String PERMISO_LIST_USUARIOS ="LIST_USUARIOS";
	public static final String PERMISO_ACT_USUARIOS ="ACT_USUARIOS";
	public static final String PERMISO_CONS_USUARIOS ="CONS_USUARIOS";
	public static final String PERMISO_DESAC_USUARIOS ="DESAC_USUARIOS";
	public static final String PERMISO_EDIT_USUARIOS ="EDIT_USUARIOS";
	public static final String PERMISO_NEW_USUARIOS ="NEW_USUARIOS";
	public static final String PERMISO_SAVE_USUARIOS ="SAVE_USUARIOS";
	public static final String PERMISO_BUS_USUPER ="BUS_USUPER";

	
	/**
	 * TIPOS DE EXPEDIENTES
	 */
	
	public static final String RCO = "RCO";
	public static final String RCE = "RCE";
	public static final String PSAN = "PSAN";
	public static final String XPC = "XPC";
	
	/**
	 * SITUACIONES
	 */
	public static final String IE = "IE";
	public static final String PAP = "PAP";
	public static final String CA = "CA";
	public static final String RST = "RST";
	public static final String PIPS = "PIPS";
	public static final String GE = "GE";
	public static final String FNZ = "FNZ";
	public static final String PRAI = "PRAI";
	public static final String PPR = "PPR";
	public static final String PRAP = "PRAP";
	public static final String PRPS = "PRPS";
	

											

	/**
	 * RESOLUCIONES
	 */
	public static final String COD_TIPO_RESOLUCION = "TIPORESOL";
	public static final String COD_SENTIDO_RESOLUCION = "SENT_RESOL";
	public static final String COD_DERECHOS_RECLAM = "DER_RECL";
	public static final String COD_ARTICULOS_AFEC = "ARTICULO";
	
	
	/**
	 * PLANTILLAS
	 */
	public static final String PERMISO_LIST_PLANTILLASDOC ="LIST_PLANTILLASDOC";
	public static final String PERMISO_ACT_PLANTILLASDOC ="ACT_PLANTILLASDOC";
	public static final String PERMISO_CONS_PLANTILLASDOC ="CONS_PLANTILLASDOC";
	public static final String PERMISO_DESAC_PLANTILLASDOC ="DESAC_PLANTILLASDOC";
	public static final String PERMISO_EDIT_PLANTILLASDOC ="EDIT_PLANTILLASDOC";
	public static final String PERMISO_SAVE_PLANTILLASDOC ="SAVE_PLANTILLASDOC";
	public static final String PERMISO_NEW_PLANTILLASDOC ="NEW_PLANTILLASDOC";

	
	/**
	 * MOTIVOS DE RELACION
	 */
	public static final String C_EDOC = "EDOC";
	
	/**
	 * MOTIVOS DESLOGADO SISTEMA
	 */
	public static final String LOGOUT_MANUAL = "MANUAL";
	public static final String LOGOUT_INACTIVIDAD = "INACTIVIDAD";
	public static final String LOGOUT_SIN_PERFIL = "SIN_PERFIL";
	public static final String LOGOUT_CAMBIO_PERFIL = "CAMBIO_PERFIL";
	
	
	/**
	 * DOCUMENTO INFORMATIVO VARIABLES
	 */
	public static final String NOMBRE_DOCINFORMATIVO_VARIABLES = "documento_informativo_variables.pdf";


	/**
	 * TEXTO MIGA DE PAN
	 */
	public static final String LISTADO_MIMESA = "Listado de mi mesa";
	public static final String CONSULTA_TAREA = "Consulta de tarea ";
	public static final String EDICION_TAREA = " Edición de tarea ";
	public static final String LISTADO_EXPEDIENTES = "Listado de expedientes";
	public static final String LISTADO_ENTRADAS = "Listado de entradas";
	public static final String CONSULTA_EXPEDIENTE = "Consulta de expediente ";
	public static final String EDICION_EXPEDIENTE = "Edición de expediente ";
	public static final String CONSULTA_ENTRADA = "Consulta de entrada ";
	public static final String EDICION_ENTRADA = "Edición de entrada ";
	public static final String ALTA_EXPEDIENTE = "Alta de expediente ";
	public static final String LISTADO_CONTROLPLAZOS = "Listado de control de plazos";
	public static final String LISTADO_RESOLUCIONES = "Listado de resoluciones";
	public static final String CONSULTA_RESOLUCION = "Consulta de resolución ";
	public static final String EDICION_RESOLUCION = "Edición de resolución ";
	public static final String LISTADO_PERSONAS = "Listado de personas";
	public static final String CONSULTA_PERSONA = "Consulta de persona ";
	public static final String EDICION_PERSONA = "Edición de persona ";
	public static final String ALTA_PERSONA = "Alta de persona ";
	public static final String LISTADO_DOMINIO = "Listado de ";
	public static final String CONSULTA_DOMINIO = "Consulta de ";
	public static final String EDICION_DOMINIO = "Edición de ";
	public static final String ALTA_DOMINIO = "Alta de ";
	public static final String LISTADO_TIPOSAGRUPACIONES = "Listado de tipos de agrupaciones";
	public static final String CONSULTA_TIPOAGRUPACION = "Consulta de tipo de agrupación ";
	public static final String EDICION_TIPOAGRUPACION = "Edición de tipo de agrupación ";
	public static final String ALTA_TIPOAGRUPACION = "Alta de tipo de agrupación ";
	public static final String LISTADO_SUJETOSOBLIGADOS = "Listado de organismos/sujetos obligados";
	public static final String CONSULTA_SUJETOOBLIGADO = "Consulta de organismo/sujeto obligado ";
	public static final String EDICION_SUJETOOBLIGADO = "Edición de organismo/sujeto obligado ";
	public static final String ALTA_SUJETOOBLIGADO = "Alta de organismo/sujeto obligado ";
	public static final String LISTADO_PARAMETROS = "Listado de parámetros";
	public static final String LISTADO_TRAMITES_ABIERTOS = "Listado de trámites en curso";
	public static final String LISTADO_NOTIFICACIONES_ABIERTAS = "Listado de notificaciones en curso";
	public static final String LISTADO_NOTIFICACIONES_PENDIENTES = "Listado de notificaciones pendientes de acuse";																											
	public static final String LISTADO_FIRMAS_ABIERTAS = "Listado de firmas en curso";
	public static final String LISTADO_DOMINIOS = "Listado de dominios";
	public static final String LISTADO_USUARIOS = "Listado de usuarios";
	public static final String CONSULTA_USUARIO = "Consulta de usuario ";
	public static final String EDICION_USUARIO = "Edición de usuario ";
	public static final String ALTA_USUARIO = "Alta de usuario ";
	public static final String LISTADO_PLANTILLASDOCUMENTOS = "Listado de plantillas de documentos";
	public static final String LISTADO_TAREAS_ENVIADAS_PENDIENTES = "Listado de tareas enviadas pendientes";
	
	
	/**
	 * VALORES DE DOMINIO GRAVEDAD
	 */
	public static final String GRAV_LEVE = "L";
	public static final String GRAV_GRAVE = "G";
	public static final String GRAV_MUYGRAVE = "MG";
	
	/**
	 * FORMATOS VARIOS
	 */
	public static final String FECHA_DDMMYYYY = "dd/MM/yyyy";
	
	/**
	 * SITUACIONES TAREAS
	 */
	public static final String SITUACIONTAREA_PENDIENTE = "P";
	public static final String SITUACIONTAREA_CERRADA = "C";
	
	/**
	 * VALORES DOMINIO RESULTADO NOTIFICACION
	 */
	public static final String RESULTNOTIF_PROV = "PROV";
	public static final String RESULTNOTIF_PDTE = "PDTE";
	
	
	/**
	 * TIPOS DE TAREAS
	 */
	public static final String TAREA_ESA = "ESA";
	public static final String TAREA_AFNP = "AFNP";
	
	
	/**
	 * USUARIO DEL SISTEMA
	 */
	public static final String USUARIO_SISTEMA = "SISTEMA";
	
}
