package es.juntadeandalucia.ctpda.gestionpdt.web.expedientes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;

import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.ArticulosAfectadosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.CfgExpedienteTramite;
import es.juntadeandalucia.ctpda.gestionpdt.model.CfgTipoExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.DerechosReclamadosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.MateriasExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.Personas;
import es.juntadeandalucia.ctpda.gestionpdt.model.PersonasExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.PlazosExpdte;
import es.juntadeandalucia.ctpda.gestionpdt.model.ResolucionExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.SujetosObligados;
import es.juntadeandalucia.ctpda.gestionpdt.model.SujetosObligadosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.TramiteExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.service.ArticulosAfectadosExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.CfgExpedienteTramiteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.CfgTipoExpedienteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.DerechosReclamadosExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.DetalleExpdteTramService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.MateriasExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.PersonasExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.PersonasService;
import es.juntadeandalucia.ctpda.gestionpdt.service.PlazosExpdteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ResolucionExpedienteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.SeriesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.SujetosObligadosExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.SujetosObligadosService;
import es.juntadeandalucia.ctpda.gestionpdt.service.TramiteExpedienteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ValoresDominioService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.util.FechaUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.controlplazos.ControlPlazosBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.Constantes;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.JsfUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.LazyDataModelByQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.web.menu.NavegacionBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.menu.NavegacionBean.ListadoNavegaciones;
import es.juntadeandalucia.ctpda.gestionpdt.web.personas.DatosPersonasBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.sujetosobligados.DatosSujetosObligadosBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.tareas.TareasEnviadasPendientesBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.BaseBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.ContextoVolver;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.FacesUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.UtilsComun;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.VolverBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@ViewScoped
public class DatosExpedientesBean extends BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String MENSAJEERROR = "error";
	private static final String IDEXPSESSION = "idExp";
	private static final String EDITABLE = "editable";
	public static final String VUELTACONTROLPLAZOS = "_volverControlPlazos_";
	private static final String IDPERSONA = "idPersona";
	private static final String IDPERSONAEXP = "idPersonaExp";
	private static final String ESPERSONADPD = "esPersonaDpd";
	private static final String PERSONASEXPEDIENTESTXT = "personasExpedientes";
	private static final String SUJETOSOBLIGADOSEXPEDIENTESTXT = "sujetosObligadosExpedientes";
	public static final String IDPERSONADPD = "idPersonaDpd";
	public static final String IDPERSONAREPRE = "idPersonaRepre";
	public static final String ORIGENPESTANYATRAMITES = "pestanyaTramites";
	public static final String ORIGENALTAEXPEDIENTE = "altaExpediente";
	private static final String TRAMITE = "tramite";
	private static final String IDEXPEDIENTEVUELTAEXPRELACIONADOS = "idExpedienteVueltaExpRelacionados";
	private static final String IDEXPRELACIONADOACTUAL = "idExpRelacionadoActual";
	private static final String IDEXPRELACIONADOCONSULTA = "idExpRelacionadoConsulta";
		
	
	@Getter
	private LazyDataModelByQueryService<PersonasExpedientes> lazyModelPersonasExpedientes;

	@Getter
	@Setter
	private Expedientes expedientes;

	@Getter
	@Setter
	private PersonasExpedientes personasExpedientes;

	@Autowired
	private ExpedientesService expedientesService;
	
	@Autowired
	private CfgExpedienteTramiteService cfgExpedienteTramiteService;
	
	@Autowired
	private PersonasExpedientesService personasExpedientesService;
	
	@Autowired
	private ValoresDominioService valoresDominioService;
	
	@Getter
	@Setter
	private Long selectedNuevoTipoExpedienteId;

	@Getter
	@Setter
	private Long selectedNuevoTipoInfraccionId;

	@Getter
	@Setter
	private Long selectedNuevaSituacionId;

	@Getter
	@Setter
	private Long selectedNuevaMateriaId;

	@Getter
	@Setter
	private Long selectedNuevoCanalEntId;

	@Getter
	@Setter
	private Personas nuevaPersona;

	@Getter
	@Setter
	private SujetosObligadosExpedientes sujetosObligadosExpedientes;

	@Autowired
	private SujetosObligadosExpedientesService sujetosObligadosExpedientesService;
	
	@Autowired
	private ArticulosAfectadosExpedientesService articulosAfectadosExpedientesService;
	
	@Autowired
	private ResolucionExpedienteService resolucionExpedienteService;
	
	
	@Autowired
	private DerechosReclamadosExpedientesService derechosReclamadosExpedientesService;

	@Getter
	@Setter
	private Long idTipoExpediente;

	@Getter
	@Setter
	private Long idExp;
	
	@Getter
	@Setter
	private String tituloPestSuj;
	@Getter
	@Setter
	private String tituloPestPers;
	@Getter
	@Setter
	private String etiquetaSuj;
	@Getter
	@Setter
	private String tituloPestTareas;
	@Getter
	@Setter
	private String tituloPestExtractos;
	@Getter
	@Setter
	private String tituloPestAntec;
	@Getter
	@Setter
	private String tituloPestTramita;
	
	@Autowired
	private SeriesService seriesService;
	
	@Autowired
	private VolverBean volverBean;
	
	@Autowired
	private ComunExpedientesBean comunExpedientesBean;
	
	@Autowired
	private ComunTramitesBean comunTramitesBean;
	
	
	@Getter
	@Setter
	private List<String> listaCodigoPermisos;
	
	@Getter	@Setter
	private Boolean permisoSaveExp;
	

	@Autowired
	private PlazosExpdteService plazosExpdteService;
	
	//----------------------------------------------

	@Getter	@Setter
	private Boolean permisoEditExp;
	
	@Getter	@Setter
	private Boolean permisoEditExpFin;
	
	@Autowired
	private MateriasExpedientesService materiasExpedientesService;
	
	@Getter
	@Setter
	private String etiquetaPers;
	
	@Autowired
	private UtilsComun utilsComun;
	
	@Autowired
	private SujetosObligadosService sujetosObligadosService;
	
	@Autowired
	private CfgTipoExpedienteService cfgTipoExpedienteService;
	@Getter
	@Setter

	@Autowired
	private DetalleExpdteTramService detalleExpdteTramService;
	
	@Autowired
	private TramiteExpedienteService tramiteExpedienteService;
	
	@Autowired
	private NavegacionBean navegacionBean;
	
	@Autowired
	private PersonasService personasService;
	
	@Getter @Setter
	private Integer numerosSaltos;
	
	@Override
	@PostConstruct
	public void init() {
		super.init();

		numerosSaltos = (Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS);

		/**
		 * GESTION DE PERMISOS
		 * */
		listaCodigoPermisos = (List<String>) JsfUtils.getSessionAttribute(Constantes.LISTACODIGOPERMISOS);
		
		permisoSaveExp = listaCodigoPermisos.contains(Constantes.PERMISO_SAVE_EXP);
		permisoEditExp = listaCodigoPermisos.contains(Constantes.PERMISO_EDIT_EXP);
		permisoEditExpFin= listaCodigoPermisos.contains(Constantes.PERMISO_EDIT_EXPFIN);
		personasExpedientes = new PersonasExpedientes();
		Long idPersExp = (Long) JsfUtils.getFlashAttribute(IDPERSONAEXP);
		if(idPersExp!=null) {
			personasExpedientes= personasExpedientesService.obtener(idPersExp);
		}
		

		cargarExpediente();
		
		// Rellenar los campos principales.
		rellenadoCamposPrincipales();
		
		ContextoVolver contexto= volverBean.getContexto();
		ContextoVolver contextoResol= volverBean.getContexto(Constantes.VOLVERRESOLUCION);
		//Hago esto lo último, para asegurarme de que lo tengo cargado antes
		if(null != contexto && null != contexto.get(IDPERSONADPD) && contexto.get(IDPERSONADPD).equals(true)){					
			//Mostrar pestaña personas si vengo de una vuelta atrás
			PrimeFaces.current().executeScript("$(\"[id$='tituloTabSujetos']\").parent().click();");			
		}else if(null != contexto && null != contexto.get(IDPERSONAREPRE) && contexto.get(IDPERSONAREPRE).equals(true)){
			//Mostrar pestaña personas si vengo de una vuelta atrás
			PrimeFaces.current().executeScript("$(\"[id$='tituloTabPersonas']\").parent().click();");			
		}else if(contextoResol != null) {
			comunTramitesBean.onConsultarTramite((Long)contextoResol.get("idTramite"));
		}
		
		// Titulo de las pestañas
		this.rellenarTitulos();
		
		//otros contextos
		volverBean.recogerContexto();
		volverBean.recogerContexto(DatosPersonasBean.VOLVERPERSONAS);
		volverBean.recogerContexto(DatosSujetosObligadosBean.VOLVERSUJETOS);
		volverBean.recogerContexto(ControlPlazosBean.VUELTACONTROLPLAZOS);
		volverBean.recogerContexto(DatosExpedientesRelacionadosBean.VOLVERFORMEXPRELACIONADOS);
		
		ContextoVolver contextoMiMesa = volverBean.recogerContexto(Constantes.VOLVERMESATAREAS);
		ContextoVolver contextoTramitesAbiertos = volverBean.recogerContexto(Constantes.VOLVERTRAMITESABIERTOS);
		ContextoVolver contextoNotificacionesAbiertas = volverBean.recogerContexto(Constantes.VOLVERNOTIFICACIONESABIERTAS);
		ContextoVolver contextoNotificacionesPendientes = volverBean.recogerContexto(Constantes.VOLVERNOTIFICACIONESPENDIENTES);
		ContextoVolver contextoFirmasAbiertas = volverBean.recogerContexto(Constantes.VOLVERFIRMASABIERTAS);
		ContextoVolver contextoTareasEnviadasPdtes = volverBean.recogerContexto(Constantes.VOLVERLISTADOTAREASENVIADAS);
		
		//Incluir este if en el if-else anterior
		if((contextoMiMesa != null && !Constantes.XPC.equals(getExpedientes().getValorTipoExpediente().getCodigo())) 
				|| contextoTramitesAbiertos != null || contextoNotificacionesAbiertas != null || contextoNotificacionesPendientes != null
				|| contextoFirmasAbiertas != null || contextoTareasEnviadasPdtes != null) {
			//Mostrar pestaña tramitación si vengo desde Mi Mesa
			PrimeFaces.current().executeScript("$(\"[id$='tituloTabTramitacion']\").parent().click();");	
		}
		


	}
	
	private void rellenadoCamposPrincipales() {
		if (personasExpedientes.getId() != null) {
			if(Boolean.TRUE.equals(personasExpedientes.getInteresado()))
			{
				this.setPersona(personasExpedientes.getPersonas().getNombreAp() + " (INTERESADO)");
			}else {
				this.setPersona(personasExpedientes.getPersonas().getNombreAp());	
			}
			
			
			if(personasExpedientes.getPersonasRepre() != null) {
				this.setRepresentante(personasExpedientes.getPersonasRepre().getNombreAp());
			}	
			
			JsfUtils.setSessionAttribute("expedientesPersPrinc", personasExpedientes);
		}
		if (sujetosObligadosExpedientes != null) {
			this.setSujetoObligado(sujetosObligadosExpedientes.getSujetosObligados().getDescripcion());
			this.setListaAnidamientosSujetoObligado(obtenerSujetosObligadosAscendentes(sujetosObligadosExpedientes.getSujetosObligados()));
		}
		// Rellenar los campos del panel lateral derecho de información.
		if(expedientes.getFechaUltimaPersistencia() != null) {
			this.setPanelActualizado(FechaUtils.dateToStringFechaYHora(expedientes.getFechaUltimaPersistencia()) + " ("+ expedientes.getUsuUltimaPersistencia() + ")");
			this.setTramitacionAnonima(expedientes.getTramitacionAnonima());
			this.setAbstencionRecusacion(expedientes.getAbstencionRecusacion());

		}else {
			this.setPanelActualizado("");
		}
		
		seguimCabecera(expedientes);
		
		StringBuilder panelPlazos = new StringBuilder();
		panelPlazos.append("");
		if(expedientes.getId() != null) {
			List<PlazosExpdte> plazosExpdtePanelPlazos = plazosExpdteService.plazosExpdteActivosNoCumplidosByExpediente(expedientes.getId());
			this.setPlazosExpdtePanelPlazos(plazosExpdtePanelPlazos);
			if(!plazosExpdtePanelPlazos.isEmpty()) {
				for(PlazosExpdte plazosExpdte : plazosExpdtePanelPlazos) {
					panelPlazos.append(plazosExpdte.getValorTipoPlazo().getAbreviatura());
					panelPlazos.append(" - ");
					panelPlazos.append(FechaUtils.dateToStringFecha(plazosExpdte.getFechaLimite()));
					panelPlazos.append(";");
					panelPlazos.append("\n");
				} 
			}
		}
		
		this.setPanelPlazos(panelPlazos);
	}
	
	
	private void seguimCabecera (Expedientes expedientes) {
		
		if(null != expedientes.getValorSituacionExpediente()) {
			this.setPanelSituacion(expedientes.getValorSituacionExpediente().getDescripcion());
		}
		if (expedientes.getSituacionAdicional() != null) {
			this.setPanelSituacionAdicional(expedientes.getSituacionAdicional());
		}
		
		if (expedientes.getDescSeguimiento1() != null) {
			this.setPanelDescSeguimiento1(expedientes.getDescSeguimiento1());
		}

		if (expedientes.getInfSeguimiento1() != null) {
			this.setPanelInfSeguimiento1(expedientes.getInfSeguimiento1());
		}

		if (expedientes.getDescSeguimiento2() != null) {
			this.setPanelDescSeguimiento2(expedientes.getDescSeguimiento2());
		}

		if (expedientes.getInfSeguimiento2() != null) {
			this.setPanelInfSeguimiento2(expedientes.getInfSeguimiento2());
		}

		if (expedientes.getDescSeguimiento3() != null) {
			this.setPanelDescSeguimiento3(expedientes.getDescSeguimiento3());
		}

		if (expedientes.getInfSeguimiento3() != null) {
			this.setPanelInfSeguimiento3(expedientes.getInfSeguimiento3());
		}


		
	}
	
	
	
	//------------------------------------------------------------
	//Datos de cabecera
		
	public String getPersona() {
		return comunExpedientesBean.getPersona();
	}

	public void setPersona(String persona) {
		comunExpedientesBean.setPersona(persona);
	}

	public String getRepresentante() {
		return comunExpedientesBean.getRepresentante();
	}

	public void setRepresentante(String representante) {
		comunExpedientesBean.setRepresentante(representante);
	}

	public String getSujetoObligado() {
		return comunExpedientesBean.getSujetoObligado();
	}

	public void setSujetoObligado(String sujetoObligado) {
		comunExpedientesBean.setSujetoObligado(sujetoObligado);
	}

	public String getPanelActualizado() {
		return comunExpedientesBean.getPanelActualizado();
	}

	public void setPanelActualizado(String panelActualizado) {
		comunExpedientesBean.setPanelActualizado(panelActualizado);
	}
	

	public Boolean getTieneDpd() {
		return comunExpedientesBean.getTieneDpd();
	}

	public void setTieneDpd(Boolean tieneDpd) {
		comunExpedientesBean.setTieneDpd(tieneDpd);
	}
	
	public Boolean getTramitacionAnonima() {
		return comunExpedientesBean.getTramitacionAnonima();
	}

	public void setTramitacionAnonima(Boolean tramitacionAnonima) {
		comunExpedientesBean.setTramitacionAnonima(tramitacionAnonima);
	}
	
	public Boolean getAbstencionRecusacion() {
		return comunExpedientesBean.getAbstencionRecusacion();
	}

	public void setAbstencionRecusacion(Boolean abstencionRecusacion) {
		comunExpedientesBean.setAbstencionRecusacion(abstencionRecusacion);
	}

	public String getPanelSituacion() {
		return comunExpedientesBean.getPanelSituacion();
	}

	public void setPanelSituacionAdicional(String panelSituacionAdicional) {
		comunExpedientesBean.setPanelSituacionAdicional(panelSituacionAdicional);
	}
	
	

	public String getPanelSituacionAdicional() {
		return comunExpedientesBean.getPanelSituacionAdicional();
	}

	public void setPanelDescSeguimiento1(String panelDescSeguimiento1) {
		comunExpedientesBean.setPanelDescSeguimiento1(panelDescSeguimiento1);
	}

	public String getPanelDescSeguimiento1() {
		return comunExpedientesBean.getPanelDescSeguimiento1();
	}
	
	public void setPanelInfSeguimiento1(String panelInfSeguimiento1) {
		comunExpedientesBean.setPanelInfSeguimiento1(panelInfSeguimiento1);
	}

	public String getPanelInfSeguimiento1() {
		return comunExpedientesBean.getPanelInfSeguimiento1();
	}
	
	
	public void setPanelDescSeguimiento2(String panelDescSeguimiento2) {
		comunExpedientesBean.setPanelDescSeguimiento2(panelDescSeguimiento2);
	}

	public String getPanelDescSeguimiento2() {
		return comunExpedientesBean.getPanelDescSeguimiento2();
	}
	
	public void setPanelInfSeguimiento2(String panelInfSeguimiento2) {
		comunExpedientesBean.setPanelInfSeguimiento2(panelInfSeguimiento2);
	}

	public String getPanelInfSeguimiento2() {
		return comunExpedientesBean.getPanelInfSeguimiento2();
	}

	public void setPanelDescSeguimiento3(String panelDescSeguimiento3) {
		comunExpedientesBean.setPanelDescSeguimiento3(panelDescSeguimiento3);
	}

	public String getPanelDescSeguimiento3() {
		return comunExpedientesBean.getPanelDescSeguimiento3();
	}
	
	public void setPanelInfSeguimiento3(String panelInfSeguimiento3) {
		comunExpedientesBean.setPanelInfSeguimiento3(panelInfSeguimiento3);
	}

	public String getPanelInfSeguimiento3() {
		return comunExpedientesBean.getPanelInfSeguimiento3();
	}

	
	public void setPanelSituacion(String panelSituacion) {
		comunExpedientesBean.setPanelSituacion(panelSituacion);
	}

	public boolean isMostrarAvisos() {
		return Boolean.TRUE.equals(this.getTramitacionAnonima())
				|| Boolean.TRUE.equals(this.getAbstencionRecusacion());
	}
	
	public StringBuilder getPanelPlazos() {
		return comunExpedientesBean.getPanelPlazos();
	}

	public void setPanelPlazos(StringBuilder panelPlazos) {
		comunExpedientesBean.setPanelPlazos(panelPlazos);
	}

	public List<PlazosExpdte> getPlazosExpdtePanelPlazos() {
		return comunExpedientesBean.getPlazosExpdtePanelPlazos();
	}

	public void setPlazosExpdtePanelPlazos(List<PlazosExpdte> plazosExpdtePanelPlazos) {
		comunExpedientesBean.setPlazosExpdtePanelPlazos(plazosExpdtePanelPlazos);
	}

	public List<SujetosObligados> getListaAnidamientosSujetoObligado() {
		return comunExpedientesBean.getListaAnidamientosSujetoObligado();
	}

	public void setListaAnidamientosSujetoObligado(List<SujetosObligados> listaAnidamientosSujetoObligado) {
		comunExpedientesBean.setListaAnidamientosSujetoObligado(listaAnidamientosSujetoObligado);
	}

	
	//------------------------------------------------------------
	
	private void cargarExpediente() {
		ContextoVolver cv = volverBean.recogerContexto();
		ContextoVolver cvResol = volverBean.recogerContexto(Constantes.VOLVERRESOLUCION);
		ContextoVolver cvExpRelacionados = volverBean.recogerContexto(DatosExpedientesRelacionadosBean.VOLVERFORMEXPRELACIONADOS);

		if(cv != null) {
			idExp = (Long) cv.get(IDEXPSESSION);
			setFormEditable(true);
			personasExpedientes = (PersonasExpedientes) cv.get(PERSONASEXPEDIENTESTXT);
			sujetosObligadosExpedientes = (SujetosObligadosExpedientes) cv.get(SUJETOSOBLIGADOSEXPEDIENTESTXT);
		} else if(cvResol != null){
			idExp = (Long) cvResol.get(IDEXPSESSION);
			setFormEditable((Boolean)cvResol.get(EDITABLE));
			cargarDatosPanelExpediente();
		} else if(cvExpRelacionados != null){
			Long idExpRelacionadoActual = (Long) cvExpRelacionados.get(IDEXPRELACIONADOACTUAL);
			Long idExpRelacionadoConsulta = (Long) cvExpRelacionados.get(IDEXPRELACIONADOCONSULTA);
			Long idExpedienteVueltaExpRelacionadosCtx = (Long) cvExpRelacionados.get(IDEXPEDIENTEVUELTAEXPRELACIONADOS);
			if(idExpRelacionadoActual != null && idExpedienteVueltaExpRelacionadosCtx != null && idExpRelacionadoActual.equals(idExpedienteVueltaExpRelacionadosCtx)) {
				idExp = (Long) cvExpRelacionados.get(IDEXPRELACIONADOACTUAL);
				setFormEditable((Boolean)cvExpRelacionados.get(EDITABLE));
			}else if(idExpRelacionadoConsulta != null && idExpRelacionadoConsulta.equals(JsfUtils.getFlashAttribute(IDEXPSESSION))) {
				idExp = (Long) cvExpRelacionados.get(IDEXPRELACIONADOCONSULTA);
				cvExpRelacionados.put(IDEXPEDIENTEVUELTAEXPRELACIONADOS, idExpRelacionadoActual);
				setFormEditable(false);
			}else if(idExp == null){
				idExp = (Long) JsfUtils.getFlashAttribute(IDEXPSESSION);
				setFormEditable(Boolean.TRUE.equals(JsfUtils.getFlashAttribute(EDITABLE)));
			}
			cargarDatosPanelExpediente();		
		} else {
			idExp = (Long) JsfUtils.getFlashAttribute(IDEXPSESSION);
			setFormEditable(Boolean.TRUE.equals(JsfUtils.getFlashAttribute(EDITABLE)));
			cargarDatosPanelExpediente();
		}
		
		if(idExp == null) {
			// Se crea un nuevo expediente.
			expedientes = new Expedientes();
			// Se obtiene el tipo de expediente y si no es nulo se obtiene los valores del dominio.
			this.idTipoExpediente = (Long) JsfUtils.getSessionAttribute("idTipoExpediente");
			if (idTipoExpediente != null) {
				expedientes.setValorTipoExpediente(valoresDominioService.obtener(idTipoExpediente));
				// Se limpia los campos principales y los del panel.
				this.limpiarCamposPrincipales();
				this.limpiarPanel();	
			}
		} else {
			expedientes = expedientesService.obtener(idExp);
		}
			
		JsfUtils.setSessionAttribute("expedienteFormulario", expedientes);
		
	}
	
	private void cargarDatosPanelExpediente() {
		if(idExp != null) {
			// Consulta para obtener de la BBDD la persona y el representante principales
			personasExpedientes = personasExpedientesService.obtenerPersonaExpedientePrincipalPorExpediente(idExp);
			// Consulta para obtener de la BBDD sujeto obligado principal
			sujetosObligadosExpedientes = sujetosObligadosExpedientesService
					.obtenerSujetosObligadosExpedientePrincipalPorExpediente(idExp);
		}
	}
	
	private void rellenarTitulos() {
		
		
		this.tituloPestPers = mensajesProperties.getString("datos.persona");
		this.tituloPestSuj = mensajesProperties.getString("datos.sujeto.obligado");
		this.etiquetaPers = mensajesProperties.getString("persona");
		this.etiquetaSuj = mensajesProperties.getString("sujeto.obligado");
		this.tituloPestTareas = mensajesProperties.getString("tareas");
		this.tituloPestExtractos = mensajesProperties.getString("extractos");
		this.tituloPestAntec = mensajesProperties.getString("antecedentes");
		this.tituloPestTramita = mensajesProperties.getString("tramitacion");

		if(this.expedientes.getValorTipoExpediente()!= null && (this.expedientes.getValorTipoExpediente().getCodigo().equals("RCE") || this.expedientes.getValorTipoExpediente().getCodigo().equals("RCO"))){
			this.tituloPestPers = mensajesProperties.getString("reclamante");
			this.tituloPestSuj = mensajesProperties.getString("reclamado");
			this.etiquetaPers = mensajesProperties.getString("reclamante");
			this.etiquetaSuj = mensajesProperties.getString("reclamado");
		}
		

		if(this.expedientes.getValorTipoExpediente()!= null && this.expedientes.getValorTipoExpediente().getCodigo().equals("PSAN")){
			this.tituloPestPers = mensajesProperties.getString("involucrado");
			this.tituloPestSuj = mensajesProperties.getString("expedientado");
			this.etiquetaPers = mensajesProperties.getString("involucrado");
			this.etiquetaSuj = mensajesProperties.getString("expedientado");
		}

		
		if(this.expedientes.getValorTipoExpediente()!= null && (this.expedientes.getValorTipoExpediente().getCodigo().equals("XPC"))){
			this.tituloPestPers = mensajesProperties.getString("datos.interesado");
			this.etiquetaPers = mensajesProperties.getString("datos.interesado");
			this.tituloPestTramita = mensajesProperties.getString("entrada.documentacion");
			
		}
		
	}


	/** Método que limpia los campos principales. */
	private void limpiarCamposPrincipales() {
		this.setPersona(null);
		this.setRepresentante(null);
		this.setSujetoObligado(null);
		this.setListaAnidamientosSujetoObligado(null);
	}

	/** Método que limpia los campos del panel. */
	private void limpiarPanel() {
		this.setPanelActualizado(null);
		this.setPanelSituacion(null);
		this.setPanelSituacionAdicional(null);
		this.setPanelDescSeguimiento1(null);
		this.setPanelInfSeguimiento1(null);
		this.setPanelDescSeguimiento2(null);
		this.setPanelInfSeguimiento2(null);
		this.setPanelDescSeguimiento3(null);
		this.setPanelInfSeguimiento3(null);
		
		StringBuilder panelPlazos = new StringBuilder();
		panelPlazos.append("");
		this.setPanelPlazos(panelPlazos);
	}
	
	/** Método que limpia los campos plazos del panel. */
	public void limpiarPanelPlazos() {
		StringBuilder panelPlazos = new StringBuilder();
		panelPlazos.append("");
		this.setPanelPlazos(panelPlazos);

	}

	/** Método que devuelve los tipo de Expedientes. */
	public List<ValoresDominio> getListaValoresDominioTipoExp() {
		return valoresDominioService.findValoresTipoExpediente();
	}

	public boolean validacionesGuardar(Expedientes expedientesDG, List<PersonasExpedientes> expedientesDP, List<SujetosObligadosExpedientes> expedientesDS) {
		boolean valido = true;
		String mensajeFinal = "";
		String errorCamposObligatoriosPestanyaGeneral = "";
		String errorCamposObligatoriosPestanyaPersonas = "";
		String errorCamposObligatoriosPestanyaSujetos = "";
		
		// -----------VALIDACIONES DATOS GENERALES------------------//
		if (expedientesDG == null 
//				|| (StringUtils.isBlank(expedientesDG.getNombreExpediente())
								|| (expedientesDG.getValorSituacionExpediente() == null
								|| expedientesDG.getFechaEntrada() == null
								|| expedientesDG.getResponsable() == null)
				) {
			valido = false;			
			errorCamposObligatoriosPestanyaGeneral = mensajesProperties.getString("campos.obligatorios.pestanya.general");
		}

		// -----------VALIDACIONES DATOS PERSONAS------------------//
		if (expedientesDP == null || expedientesDP.isEmpty()) {
			valido = false;
			errorCamposObligatoriosPestanyaPersonas = mensajesProperties.getString("campos.obligatorios.pestanya.personas");
		}
		// -----------VALIDACIONES DATOS SUJETOS------------------//
		if (expedientesDS == null || expedientesDS.isEmpty()) {
			valido = false;
			errorCamposObligatoriosPestanyaSujetos = mensajesProperties.getString("campos.obligatorios.pestanya.sujetos");
		}
		
		if(!errorCamposObligatoriosPestanyaGeneral.isBlank()) {
			mensajeFinal = errorCamposObligatoriosPestanyaGeneral;
		} 
		if(!errorCamposObligatoriosPestanyaPersonas.isBlank()){
			mensajeFinal += "\n"+errorCamposObligatoriosPestanyaPersonas;
		}
		if(!errorCamposObligatoriosPestanyaSujetos.isBlank()) {
			mensajeFinal += "\n"+errorCamposObligatoriosPestanyaSujetos;
		}
		if(!mensajeFinal.isBlank()) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajeFinal);
			PrimeFaces.current().dialog().showMessageDynamic(message);
		}

		// -----------------------------------------------------------//
		return valido;
	}

	public String saveExpedientes() {
		Expedientes expedientesDG = (Expedientes) JsfUtils.getSessionAttribute("expedientesDG");
		@SuppressWarnings("unchecked")
		List<PersonasExpedientes> expedientesDP = (List<PersonasExpedientes>) JsfUtils.getSessionAttribute("expedientesDP");
		@SuppressWarnings("unchecked")
		List<SujetosObligadosExpedientes> expedientesDS = (List<SujetosObligadosExpedientes>) JsfUtils.getSessionAttribute("expedientesDS");
		@SuppressWarnings("unchecked")
		List<ArticulosAfectadosExpedientes> expedientesDA = (List<ArticulosAfectadosExpedientes>) JsfUtils.getSessionAttribute("expedientesDA");
		@SuppressWarnings("unchecked")
		List<DerechosReclamadosExpedientes> expedientesDD = (List<DerechosReclamadosExpedientes>) JsfUtils.getSessionAttribute("expedientesDD");
		@SuppressWarnings("unchecked")
		List<MateriasExpedientes> expedientesMaterias = (List<MateriasExpedientes>) JsfUtils.getSessionAttribute("expedientesMaterias");
		boolean puedoGuardar = validacionesGuardar(expedientesDG, expedientesDP, expedientesDS);
		String vueltaListado = "";
		
		try {
			if (puedoGuardar) {
				
				String numeroExpediente = seriesService.nextNumeroSerie(expedientesDG.getValorTipoExpediente().getCodigo(), expedientesDG.getFechaEntrada());
				expedientesDG.setNumExpediente(numeroExpediente);
				

				guardadoExpedienteSeg(expedientesDG);
				
				guardadoExpedienteDG(expedientesDG);
				
				guardadoExpedienteDP(expedientesDP);
								
				guardadoExpedienteDS(expedientesDS);
								
				actualizaArticulosAfectados(expedientesDA);
				actualizaDerechosReclamados(expedientesDD);
				actualizaMaterias(expedientesMaterias);
				
				Date fechaLimite = null;
				
				if ((expedientesDG.getValorTipoExpediente().getCodigo().equals("RCE")) || (expedientesDG.getValorTipoExpediente().getCodigo().equals("RCO"))) {
						fechaLimite = utilsComun.generarPlazoExpdte(null, null, expedientes.getFechaEntrada(), Constantes.COD_VAL_DOM_ACU, Constantes.COD_VAL_DOM_DN, 0, "A", expedientes);}
				
				if (expedientesDG.getValorTipoExpediente().getCodigo().equals("PSAN")) {
					fechaLimite = expedientes.getFechaEntrada();}	

				utilsComun.generarPlazoExpdte(null, null, fechaLimite, Constantes.COD_VAL_DOM_RES, Constantes.COD_VAL_DOM_DN, 0, "A", expedientes);
				
				/**
				 * UNA VEZ GUARDADO EL EXPEDIENTE CREAREMOS EL/LOS TRAMITE/S INICIAL/ES PARA EL MISMO.
				 * */
				
				crearTramitesInicialesExpAutom(expedientesDG);

				JsfUtils.setSessionAttribute(Constantes.ULTIMO_EXPEDIENTE_CREADO, expedientes.getNumExpediente());
				JsfUtils.setSessionAttribute(Constantes.ULTIMO_TIPO_EXPEDIENTE_CREADO, expedientesDG.getValorTipoExpediente().getCodigo());
				
				
				if(Constantes.XPC.equals(expedientes.getValorTipoExpediente().getCodigo()))
				{
					vueltaListado = volverBean.onVolver(DatosPersonasBean.VOLVERPERSONAS,ListadoNavegaciones.LISTADO_ENTRADAS.getRegla());	
				}else {
					vueltaListado = volverBean.onVolver(DatosPersonasBean.VOLVERPERSONAS,ListadoNavegaciones.LISTADO_EXPEDIENTES.getRegla());	
				}
				
				
				vueltaListado = volverBean.onVolver(Constantes.VOLVERMESATAREAS, vueltaListado);
			}

			volverBean.limpiarContexto();
		} catch (final BaseException e) {
			FacesMessage message = null;
			if(e.getMessage().contains("serie")) {
				message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString("no.existe.registro.config"));
			}else {
				message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MENSAJEERROR));
			}
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error("saveExpedientes - " + e.getMessage());
		}
		
		volverBean.limpiarContexto();
		return vueltaListado;

	}
	
	
	public void crearTramitesInicialesExpAutom(Expedientes expedientesDG)
	{
		Long idTipoExp = expedientesDG.getValorTipoExpediente().getId();
		ValoresDominio valorSituacionExpediente = expedientesDG.getValorSituacionExpediente();
		
		List<CfgExpedienteTramite> listadoExpTramAut = cfgExpedienteTramiteService.findExpTramitesByTipExpSitExpAut(idTipoExp, valorSituacionExpediente.getId());
		
		var datTramExpBean = FacesUtils.getVar("datosTramiteExpedienteBean", DatosTramiteExpedienteBean.class);
		
		if(listadoExpTramAut != null && !listadoExpTramAut.isEmpty())
		{
			for(CfgExpedienteTramite cfgExpedienteTramite: listadoExpTramAut) {
				datTramExpBean.altaTramite(TRAMITE, ORIGENALTAEXPEDIENTE, cfgExpedienteTramite);
			}	
		}
	}
	

	private void guardadoExpedienteSeg(Expedientes expedientesDG) {
	
		CfgTipoExpediente cfgTe= cfgTipoExpedienteService.obtenerCfgTipoExpedientePorValorTipoExpediente(expedientesDG.getValorTipoExpediente().getId());
		
		if(cfgTe.getTipoTramiteSeg1() != null) {
			expedientesDG.setDescSeguimiento1(cfgTe.getTipoTramiteSeg1().getDescripcionAbrev() + ": ");
			}
		
		if(cfgTe.getTipoTramiteSeg2() != null) {
			expedientesDG.setDescSeguimiento2(cfgTe.getTipoTramiteSeg2().getDescripcionAbrev() + ": ");
			}
		
		if(cfgTe.getTipoTramiteSeg3() != null) {
			expedientesDG.setDescSeguimiento3(cfgTe.getTipoTramiteSeg3().getDescripcionAbrev() + ": ");
			}
		
		
	}

	
	private void guardadoExpedienteDG(Expedientes expedientesDG) throws BaseException {
		if (expedientesDG != null) {
			expedientes = expedientesDG;
			expedientes = expedientesService.guardar(expedientes);			
			
			JsfUtils.setSessionAttribute("expedienteFormulario", expedientes);
		}
	}
	
	
	
	private void guardadoExpedienteDP (List<PersonasExpedientes> expedientesDP) throws BaseException {
		if (expedientesDP != null && !expedientesDP.isEmpty()) {
			for (int i = 0; i < expedientesDP.size(); i++) {
				personasExpedientes = expedientesDP.get(i);
				personasExpedientes.setExpediente(expedientes);
				personasExpedientes = personasExpedientesService.guardar(personasExpedientes);
				
				personaExpedientePrincipal();
			}

		}
	}
	
	private void personaExpedientePrincipal() {
		if(Boolean.TRUE.equals(personasExpedientes.getPrincipal())) {
			this.setPersona(personasExpedientes.getPersonas().getNombreAp());
			if(personasExpedientes.getPersonasRepre() != null) {
				this.setRepresentante(personasExpedientes.getPersonasRepre().getNombreAp());
			}							
		}
	}
	
	private void guardadoExpedienteDS(List<SujetosObligadosExpedientes> expedientesDS) throws BaseException {
		if(expedientesDS != null && !expedientesDS.isEmpty()) {
			for(int i=0;i< expedientesDS.size();i++) {
				sujetosObligadosExpedientes= expedientesDS.get(i);
				sujetosObligadosExpedientes.setExpediente(expedientes);
				sujetosObligadosExpedientes = sujetosObligadosExpedientesService.guardar(sujetosObligadosExpedientes);
			
				if(Boolean.TRUE.equals(sujetosObligadosExpedientes.getPrincipal())) {
					this.setSujetoObligado(sujetosObligadosExpedientes.getSujetosObligados().getDescripcion());
					this.setListaAnidamientosSujetoObligado(obtenerSujetosObligadosAscendentes(sujetosObligadosExpedientes.getSujetosObligados()));
				}
			}
		}
	}
		
	private void actualizaArticulosAfectados(List<ArticulosAfectadosExpedientes> expedientesDA) throws BaseException {
		List<ArticulosAfectadosExpedientes> artAfectClean = articulosAfectadosExpedientesService.obtenerListArticulosAfectadosExpedientePorExpediente(expedientes.getId());
		if(artAfectClean != null && !artAfectClean.isEmpty()) {
			for(ArticulosAfectadosExpedientes artAfect: artAfectClean) {
				articulosAfectadosExpedientesService.delete(artAfect.getId());
			}
		}
		
		if(expedientesDA != null && !expedientesDA.isEmpty()) {
			for(ArticulosAfectadosExpedientes artAfect: expedientesDA) {
				artAfect.setExpediente(expedientes);
				articulosAfectadosExpedientesService.guardar(artAfect);
			}
		}
	}
	
	private void actualizaDerechosReclamados(List<DerechosReclamadosExpedientes> expedientesDD) throws BaseException {
		List<DerechosReclamadosExpedientes> dereReclClean = derechosReclamadosExpedientesService.obtenerListDerechosReclamadosExpedientePorExpediente(expedientes.getId()); 
		if(dereReclClean != null && !dereReclClean.isEmpty()) {
			for(DerechosReclamadosExpedientes derRecl: dereReclClean) {
				derechosReclamadosExpedientesService.delete(derRecl.getId());
			}
		}
		
		if(expedientesDD != null && !expedientesDD.isEmpty()) {
			for(DerechosReclamadosExpedientes derRecl: expedientesDD) {
				derRecl.setExpediente(expedientes);
				derechosReclamadosExpedientesService.guardar(derRecl);
			}
		}
	}
	
	public String altaPersona() {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, numerosSaltos+1);		
		JsfUtils.setFlashAttribute(EDITABLE, true);
		ContextoVolver contexto = volverBean.crearContexto(ListadoNavegaciones.FORM_EXPEDIENTES.getRegla());
		contexto.put(IDEXPSESSION,  expedientes.getId());
		contexto.put(IDPERSONAREPRE, true);
		contexto.put(PERSONASEXPEDIENTESTXT, personasExpedientes);
		contexto.put(SUJETOSOBLIGADOSEXPEDIENTESTXT, sujetosObligadosExpedientes);
		navegacionBean.setTxtMigaPan(Constantes.ALTA_PERSONA);
		return ListadoNavegaciones.FORM_PERSONAS.getRegla();
	}
	
	public String ejecutarVolver() {
		String vuelta = volverBean.onVolver(null,ListadoNavegaciones.LISTADO_EXPEDIENTES.getRegla());
		
		if(Boolean.TRUE.equals(JsfUtils.getSessionAttribute(Constantes.VUELTA_PERSONAS))) {
			vuelta = volverBean.onVolver(DatosPersonasBean.VOLVERPERSONAS,ListadoNavegaciones.LISTADO_EXPEDIENTES.getRegla());
		}else if(Boolean.TRUE.equals(JsfUtils.getSessionAttribute(Constantes.VUELTAS_CONTROLPLAZOS))) {
			vuelta = volverBean.onVolver(ControlPlazosBean.VUELTACONTROLPLAZOS,ListadoNavegaciones.LISTADO_EXPEDIENTES.getRegla());
		}else if(Boolean.TRUE.equals(JsfUtils.getSessionAttribute(Constantes.VUELTA_EXPRELACIONADOS))) {
			vuelta = volverBean.onVolver(DatosExpedientesRelacionadosBean.VOLVERFORMEXPRELACIONADOS,ListadoNavegaciones.LISTADO_EXPEDIENTES.getRegla());
		}else if(Boolean.TRUE.equals(JsfUtils.getSessionAttribute(Constantes.VUELTA_TRAMITES_ABIERTOS))) {
			vuelta = volverBean.onVolver(TramitesAbiertosBean.VOLVERLISTADOTRAMITESABIERTOS,ListadoNavegaciones.LISTADO_EXPEDIENTES.getRegla());
		}else if(Boolean.TRUE.equals(JsfUtils.getSessionAttribute(Constantes.VUELTA_NOTIFICACIONES_ABIERTAS))) {
			vuelta = volverBean.onVolver(NotificacionesAbiertasBean.VOLVERLISTADONOTIFICACIONESABIERTAS,ListadoNavegaciones.LISTADO_EXPEDIENTES.getRegla());
		}else if(Boolean.TRUE.equals(JsfUtils.getSessionAttribute(Constantes.VUELTA_NOTIFICACIONES_PENDIENTES))) {
			vuelta = volverBean.onVolver(NotificacionesPendientesBean.VOLVERLISTADONOTIFICACIONESPENDIENTES,ListadoNavegaciones.LISTADO_EXPEDIENTES.getRegla());
		}else if(Boolean.TRUE.equals(JsfUtils.getSessionAttribute(Constantes.VUELTA_FIRMAS_ABIERTAS))) {
			vuelta = volverBean.onVolver(FirmasAbiertasBean.VOLVERLISTADOFIRMASABIERTAS,ListadoNavegaciones.LISTADO_EXPEDIENTES.getRegla());
		}else if(Boolean.TRUE.equals(JsfUtils.getSessionAttribute(Constantes.VUELTA_LISTADO_ENTRADAS))) {
			vuelta = volverBean.onVolver(ExpedientesBean.VOLVERLISTADOENTRADAS,ListadoNavegaciones.LISTADO_ENTRADAS.getRegla());
		}else if(Boolean.TRUE.equals(JsfUtils.getSessionAttribute(Constantes.VUELTA_LISTADO_TAREAS_ENVIADAS_PDTES))) {
			vuelta = volverBean.onVolver(TareasEnviadasPendientesBean.VOLVERLISTADOTAREASENVIADASPDTES,ListadoNavegaciones.LISTADO_TAREAS_ENVIADAS_PENDIENTES.getRegla());
		}
		
		vuelta = volverBean.onVolver(Constantes.VOLVERMESATAREAS,vuelta);
		
		cargaMigaPan();

		JsfUtils.removeSessionAttribute(Constantes.VUELTA_PERSONAS);
		JsfUtils.removeSessionAttribute(Constantes.VUELTAS_CONTROLPLAZOS);
		JsfUtils.removeSessionAttribute(Constantes.IDPLAZOSELECCIONADO);
		JsfUtils.removeSessionAttribute(Constantes.IDPERSONASELECCIONADA);
		JsfUtils.removeSessionAttribute(Constantes.IDEXPEDIENTERELACIONADOSELECCIONADO);
		JsfUtils.removeSessionAttribute(Constantes.VUELTA_EXPRELACIONADOS);
		JsfUtils.removeSessionAttribute(Constantes.VUELTA_TRAMITES_ABIERTOS);
		JsfUtils.removeSessionAttribute(Constantes.VUELTA_LISTADO_ENTRADAS);
		JsfUtils.removeSessionAttribute(Constantes.VUELTA_NOTIFICACIONES_ABIERTAS);
		JsfUtils.removeSessionAttribute(Constantes.VUELTA_NOTIFICACIONES_PENDIENTES);
		return vuelta;
	}
	
	private void cargaMigaPan() {
		ContextoVolver contextoControlPlazos = volverBean.recogerContexto(ControlPlazosBean.VUELTACONTROLPLAZOS);
		ContextoVolver contextoPersonas = volverBean.recogerContexto(DatosPersonasBean.VOLVERPERSONAS);
		ContextoVolver contextoExpRelacionados = volverBean.recogerContexto(DatosExpedientesRelacionadosBean.VOLVERFORMEXPRELACIONADOS);
		
		if(contextoControlPlazos != null && contextoControlPlazos.getVista().contains("listadoControlPlazos")) {
			volverBean.migaPanVolver(Constantes.LISTADO_CONTROLPLAZOS);	
		}else if(contextoPersonas != null && contextoPersonas.getVista().contains("/personas/formulario")) {
			Personas persona = personasService.obtener((Long) contextoPersonas.get(IDPERSONA));
			if((boolean) contextoPersonas.get(EDITABLE)) {
				volverBean.migaPanVolver(Constantes.EDICION_PERSONA+persona.getNifCif());				
			}else {
				volverBean.migaPanVolver(Constantes.CONSULTA_PERSONA+persona.getNifCif());
			}
		}else if(contextoExpRelacionados != null && contextoExpRelacionados.getVista().contains("/expedientes/expedientes/formFormularioExpedientes")) {
			Expedientes expediente = expedientesService.obtener((Long) contextoExpRelacionados.get(IDEXPEDIENTEVUELTAEXPRELACIONADOS));
			if((boolean) contextoExpRelacionados.get(EDITABLE)) {
				volverBean.migaPanVolver(Constantes.EDICION_EXPEDIENTE+expediente.getMigaDePan()+navegacionBean.expedientesRelacionadosMotivoPsan(expediente.getId()));
			}else {
				volverBean.migaPanVolver(Constantes.CONSULTA_EXPEDIENTE+expediente.getMigaDePan()+navegacionBean.expedientesRelacionadosMotivoPsan(expediente.getId()));
			}
		}else{
			cargaMigaPan2();
		}
	}

	private void cargaMigaPan2() {
		ContextoVolver contextoMiMesa = volverBean.recogerContexto(Constantes.VOLVERMESATAREAS);
		ContextoVolver contextoTramitesAbiertos = volverBean.recogerContexto(Constantes.VOLVERTRAMITESABIERTOS);
		ContextoVolver contextoNotificacionesAbiertas = volverBean.recogerContexto(Constantes.VOLVERNOTIFICACIONESABIERTAS);
		ContextoVolver contextoNotificacionesPendientes = volverBean.recogerContexto(Constantes.VOLVERNOTIFICACIONESPENDIENTES);
		ContextoVolver contextoFirmasAbiertas = volverBean.recogerContexto(Constantes.VOLVERFIRMASABIERTAS);
		ContextoVolver contextoTareasEnviadasPdtes = volverBean.recogerContexto(Constantes.VOLVERLISTADOTAREASENVIADAS);
		
		if (contextoMiMesa != null && contextoMiMesa.getVista().contains("listadoMiMesa")) {
			volverBean.migaPanVolver(Constantes.LISTADO_MIMESA);	
		}else if (contextoTramitesAbiertos != null && contextoTramitesAbiertos.getVista().contains("listadoTramitesAbiertos")) {
			volverBean.migaPanVolver(Constantes.LISTADO_TRAMITES_ABIERTOS);	
		}else if (contextoNotificacionesAbiertas != null && contextoNotificacionesAbiertas.getVista().contains("listadoNotificacionesAbiertas")) {
			volverBean.migaPanVolver(Constantes.LISTADO_NOTIFICACIONES_ABIERTAS);	
		}else if (contextoNotificacionesPendientes != null && contextoNotificacionesPendientes.getVista().contains("listadoNotificacionesPendientes")) {
			volverBean.migaPanVolver(Constantes.LISTADO_NOTIFICACIONES_PENDIENTES);	
		}else if (contextoFirmasAbiertas != null && contextoFirmasAbiertas.getVista().contains("listadoFirmasAbiertas")) {
			volverBean.migaPanVolver(Constantes.LISTADO_FIRMAS_ABIERTAS);	
		} else if(contextoTareasEnviadasPdtes != null && contextoTareasEnviadasPdtes.getVista().contains("listadoTareasEnviadasPdtes")) {
			volverBean.migaPanVolver(Constantes.LISTADO_TAREAS_ENVIADAS_PENDIENTES);
		}else {
			volverBean.migaPanVolver(Constantes.LISTADO_EXPEDIENTES);
		}
	}

	
	public void actualizarCabecera (Expedientes expedienteGuardado, PersonasExpedientes personaExpedienteGuardado, SujetosObligadosExpedientes sujetosObligadosExpedientesGuardado) {
		this.comunExpedientesBean.actualizarCabecera(expedienteGuardado, personaExpedienteGuardado, sujetosObligadosExpedientesGuardado);
	}
	
	public void actualizarCabecera (Expedientes expedienteGuardado, PersonasExpedientes personaExpedienteGuardado, SujetosObligadosExpedientes sujetosObligadosExpedientesGuardado, List<PlazosExpdte> listaPlazosExpdteGuardado) {
		this.comunExpedientesBean.actualizarCabecera(expedienteGuardado, personaExpedienteGuardado, sujetosObligadosExpedientesGuardado, listaPlazosExpdteGuardado);
	}
	
	private List<SujetosObligados> obtenerSujetosObligadosAscendentes(SujetosObligados sujetosObligadosSeleccionado) {
		List<SujetosObligados> listaSujObliAscendentes = new ArrayList<>();
		SujetosObligados sujObliPadre = null;
		if(sujetosObligadosSeleccionado.getSujetosObligadosPadre() != null && sujetosObligadosSeleccionado.getSujetosObligadosPadre().getId() != null) {
			sujObliPadre = sujetosObligadosService.obtener(sujetosObligadosSeleccionado.getSujetosObligadosPadre().getId());
		}

		while (sujObliPadre != null && sujObliPadre.getActiva()) {
			listaSujObliAscendentes.add(sujObliPadre);
			if(sujObliPadre.getSujetosObligadosPadre() != null && sujObliPadre.getSujetosObligadosPadre().getId() != null) {
				sujObliPadre = sujetosObligadosService.obtener(sujObliPadre.getSujetosObligadosPadre().getId());
			}else{
				sujObliPadre = null;
			}
		}

		return listaSujObliAscendentes;
	}

	public String onEditarByForm(Long idExp) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, numerosSaltos+1);
		numerosSaltos = (Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS);
		JsfUtils.setFlashAttribute(EDITABLE, true);
		JsfUtils.setFlashAttribute(IDEXPSESSION, idExp);
		JsfUtils.setSessionAttribute(EDITABLE, true);
		if(Boolean.TRUE.equals(JsfUtils.getSessionAttribute(Constantes.VUELTAS_CONTROLPLAZOS))) {
			ContextoVolver v = volverBean.crearContexto(ListadoNavegaciones.LISTADO_CONTROL_PLAZOS.getRegla(), VUELTACONTROLPLAZOS);
			v.put("idControlPlazos", JsfUtils.getSessionAttribute(Constantes.IDPLAZOSELECCIONADO));
			v.put(EDITABLE, this.getFormEditable());
		}else if(Boolean.TRUE.equals(JsfUtils.getSessionAttribute(Constantes.VUELTA_PERSONAS))) {
			ContextoVolver v = volverBean.crearContexto(ListadoNavegaciones.FORM_PERSONAS.getRegla(), DatosPersonasBean.VOLVERPERSONAS);
			v.put(IDPERSONA, JsfUtils.getSessionAttribute(Constantes.IDPERSONASELECCIONADA));
			v.put(EDITABLE, this.getFormEditable());
		}else if(Boolean.TRUE.equals(JsfUtils.getSessionAttribute(Constantes.VUELTA_EXPRELACIONADOS))) {
			ContextoVolver v = volverBean.crearContexto(ListadoNavegaciones.FORM_EXPEDIENTES.getRegla(), DatosExpedientesRelacionadosBean.VOLVERFORMEXPRELACIONADOS);
			v.put(IDEXPSESSION, JsfUtils.getSessionAttribute(Constantes.IDEXPEDIENTERELACIONADOSELECCIONADO));
			v.put(EDITABLE, this.getFormEditable());
		}
		Expedientes expedienteEdit = expedientesService.obtener(idExp);
		navegacionBean.setTxtMigaPan(Constantes.EDICION_EXPEDIENTE+expedienteEdit.getMigaDePan()+navegacionBean.expedientesRelacionadosMotivoPsan(expedienteEdit.getId()));
		return ListadoNavegaciones.FORM_EXPEDIENTES.getRegla();
	}
	
	public String onConsultarPersonaExp(Long idPersonaExp) {	
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, numerosSaltos+1);
		JsfUtils.setSessionAttribute(EDITABLE, false);
		JsfUtils.setFlashAttribute(IDPERSONAEXP, idPersonaExp);		
		JsfUtils.setSessionAttribute(IDPERSONAEXP, idPersonaExp);
		ContextoVolver contexto = volverBean.crearContexto(ListadoNavegaciones.FORM_EXPEDIENTES.getRegla());
		contexto.put(IDEXPSESSION,  expedientes.getId());		
		contexto.put(PERSONASEXPEDIENTESTXT, personasExpedientes);
		contexto.put(SUJETOSOBLIGADOSEXPEDIENTESTXT, sujetosObligadosExpedientes);
		contexto.put(IDPERSONAREPRE, true);
		PersonasExpedientes personasExpedientesCons = personasExpedientesService.obtener(idPersonaExp);
		navegacionBean.setTxtMigaPan(Constantes.CONSULTA_PERSONA+personasExpedientesCons.getPersonas().getNifCif());
		return ListadoNavegaciones.FORM_PERSONAS.getRegla();
	}
	
	public String onConsultarPersonaRepre(Long idPersonaRepre) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, numerosSaltos+1);
		JsfUtils.setSessionAttribute(EDITABLE, false);
		JsfUtils.setFlashAttribute(IDPERSONAREPRE, idPersonaRepre);
		JsfUtils.setSessionAttribute(IDPERSONAREPRE, idPersonaRepre);
		ContextoVolver contexto = volverBean.crearContexto(ListadoNavegaciones.FORM_EXPEDIENTES.getRegla());
		contexto.put(IDEXPSESSION,  expedientes.getId());		
		contexto.put(PERSONASEXPEDIENTESTXT, personasExpedientes);
		contexto.put(SUJETOSOBLIGADOSEXPEDIENTESTXT, sujetosObligadosExpedientes);
		contexto.put(IDPERSONAREPRE, true);
		Personas personas = personasService.obtener(idPersonaRepre);
		navegacionBean.setTxtMigaPan(Constantes.CONSULTA_PERSONA+personas.getNifCif());
		return ListadoNavegaciones.FORM_PERSONAS.getRegla();
	}
	
	public String onConsultarSujetos(Long idSujObligExp) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, numerosSaltos+1);
		JsfUtils.setFlashAttribute(EDITABLE, false);
		JsfUtils.setFlashAttribute("idSujObligExp", idSujObligExp);
		JsfUtils.setSessionAttribute("idSujObligExp", idSujObligExp);
		ContextoVolver contexto = volverBean.crearContexto(ListadoNavegaciones.FORM_EXPEDIENTES.getRegla());
		contexto.put(IDEXPSESSION,  expedientes.getId());		
		contexto.put(PERSONASEXPEDIENTESTXT, personasExpedientes);
		contexto.put(SUJETOSOBLIGADOSEXPEDIENTESTXT, sujetosObligadosExpedientes);
		contexto.put(IDPERSONADPD, true);
		SujetosObligadosExpedientes sujetosObligadosExpedientesCons = sujetosObligadosExpedientesService.obtener(idSujObligExp);
		navegacionBean.setTxtMigaPan(Constantes.CONSULTA_SUJETOOBLIGADO+sujetosObligadosExpedientesCons.getSujetosObligados().getDescripcion());
		return ListadoNavegaciones.FORM_SUJETOS_OBLIGADOS.getRegla();
	}
	
	public String onConsultarSujetosDpd(Long idPersonaDpd) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, numerosSaltos+1);
		JsfUtils.setSessionAttribute(EDITABLE, false);
		JsfUtils.setFlashAttribute(IDPERSONADPD, idPersonaDpd);
		JsfUtils.setSessionAttribute(IDPERSONADPD, idPersonaDpd);
		ContextoVolver contexto = volverBean.crearContexto(ListadoNavegaciones.FORM_EXPEDIENTES.getRegla());
		contexto.put(IDEXPSESSION,  expedientes.getId());		
		contexto.put(PERSONASEXPEDIENTESTXT, personasExpedientes);
		contexto.put(SUJETOSOBLIGADOSEXPEDIENTESTXT, sujetosObligadosExpedientes);
		contexto.put(IDPERSONADPD, true);
		Personas personas = personasService.obtener(idPersonaDpd);
		navegacionBean.setTxtMigaPan(Constantes.CONSULTA_PERSONA+personas.getNifCif());
		return ListadoNavegaciones.FORM_PERSONAS.getRegla();
	}
	
	private void actualizaMaterias(List<MateriasExpedientes> expedientesMaterias) throws BaseException {
		List<MateriasExpedientes> materiasClean = materiasExpedientesService.obtenerListMateriasExpedientesPorExpediente(expedientes.getId());
		if(materiasClean != null && !materiasClean.isEmpty()) {
			for(MateriasExpedientes materias: materiasClean) {
				materiasExpedientesService.delete(materias.getId());
			}
		}
		
		if(expedientesMaterias != null && !expedientesMaterias.isEmpty()) {
			for(MateriasExpedientes materias: expedientesMaterias) {
				materias.setExpediente(expedientes);
				materiasExpedientesService.guardar(materias);
			}
		}
	}
	
	public String altaPersonaRepre(PersonasExpedientes personasExpedientesSeleccionada) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, numerosSaltos+1);
		JsfUtils.setFlashAttribute(EDITABLE, true);
		JsfUtils.setSessionAttribute("personaRepre", true);
		ContextoVolver contexto = volverBean.crearContexto(ListadoNavegaciones.FORM_EXPEDIENTES.getRegla());
		contexto.put(IDEXPSESSION,  expedientes.getId());
		contexto.put(IDPERSONAREPRE, true);
		contexto.put(PERSONASEXPEDIENTESTXT, personasExpedientesSeleccionada);
		contexto.put(SUJETOSOBLIGADOSEXPEDIENTESTXT, sujetosObligadosExpedientes);
		navegacionBean.setTxtMigaPan(Constantes.ALTA_PERSONA);
		return ListadoNavegaciones.FORM_PERSONAS.getRegla();
	}
	
	public String altaPersonaDpd(SujetosObligadosExpedientes sujetosObligadosExpedientesSeleccionado) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, numerosSaltos+1);
		JsfUtils.setFlashAttribute(EDITABLE, true);
		JsfUtils.setSessionAttribute("personaDpd", true);
		ContextoVolver contexto = volverBean.crearContexto(ListadoNavegaciones.FORM_EXPEDIENTES.getRegla());
		contexto.put(IDEXPSESSION,  expedientes.getId());
		contexto.put(ESPERSONADPD, true);
		contexto.put(PERSONASEXPEDIENTESTXT, personasExpedientes);
		contexto.put(SUJETOSOBLIGADOSEXPEDIENTESTXT, sujetosObligadosExpedientesSeleccionado);
		contexto.put(IDPERSONADPD, true);
		navegacionBean.setTxtMigaPan(Constantes.ALTA_PERSONA);
		return ListadoNavegaciones.FORM_PERSONAS.getRegla();
	}
	
	public String obtenerCabeceraCTPDA(Long idExped) {
		Expedientes exped = expedientesService.obtener(idExped);
		String res="";
		String numExpediente="";
		if(exped!=null) {		
		numExpediente=exped.getNumExpediente();		
		idTipoExpediente=null;
		ValoresDominio tipoExpediente= exped.getValorTipoExpediente();
		idTipoExpediente=tipoExpediente.getId();
		CfgTipoExpediente cfgTe= cfgTipoExpedienteService.obtenerCfgTipoExpedientePorValorTipoExpediente(idTipoExpediente);
		if(exped.getResponsable()!=null) {
			res=numExpediente+" "+"-"+ " "+cfgTe.getValorTipoCtpda().getDescripcion()+" "+"-"+ " "+ exped.getResponsable().getDescripcion();
		}else {
			res=numExpediente+" "+"-"+ " "+cfgTe.getValorTipoCtpda().getDescripcion();
		}		
		}
		return res;
		}
	
	public String obtenerCabeceraAnteriorCTPDA() {
		String res="";
		String numExpediente="";
		if(this.expedientes!=null) {		
		numExpediente=this.expedientes.getNumExpediente();		
		idTipoExpediente=null;
		ValoresDominio tipoExpediente= this.expedientes.getValorTipoExpediente();
		idTipoExpediente=tipoExpediente.getId();
		CfgTipoExpediente cfgTe= cfgTipoExpedienteService.obtenerCfgTipoExpedientePorValorTipoExpediente(idTipoExpediente);
		if(this.expedientes.getResponsable()!=null) {
			res=numExpediente+" "+"-"+ " "+cfgTe.getValorTipoCtpda().getDescripcion()+" "+"-"+ " "+ this.expedientes.getResponsable().getDescripcion();
		}else {
			res=numExpediente+" "+"-"+ " "+cfgTe.getValorTipoCtpda().getDescripcion();
		}		
		}
		return res;
		}

	public void actualizarSituacionAdicional(Long expId) {
		
		
		if(expId ==null) {
			return;
		}
			
			    Expedientes exp = expedientesService.obtener(expId);
			    
			    String sitNivelExpediente = sitAdicionalesExpediente(expId);
			    
				List<TramiteExpediente> tramExp = tramiteExpedienteService.findTramitesExpAbiertos(expId);
				String txtSitAdicional = null;
				final StringBuilder txt = new StringBuilder();
				
				if (sitNivelExpediente != null) {
					txt.append(sitNivelExpediente);
				}
				
				
				for(TramiteExpediente trExp:tramExp) {
					
					if (trExp.getSituacionAdicional() != null) {
						if (txt.length() > 0) {
							txt.append("; "); 
							}			
						txt.append(trExp.getSituacionAdicional()); 

					}
					
				}
				
								
				if (txt.length() > 0) {
					txtSitAdicional = txt.toString();
				}
				
				

				
				exp.setSituacionAdicional(txtSitAdicional);
				this.expedientes.setSituacionAdicional(txtSitAdicional);
				
				try {
					expedientesService.guardar(exp);
				} catch (BaseException e) {
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
							mensajesProperties.getString(MENSAJEERROR));
					PrimeFaces.current().dialog().showMessageDynamic(message);
					log.error(e.getMessage());
				}
				
		}
	

	public String sitAdicionalesExpediente(Long expId)  {
	
		String txtSitAdicional = null;
		final StringBuilder txt = new StringBuilder();
		
		String sitWeb = sitAdicionalPWEB(expId);
		
		if (sitWeb != null) {
			txt.append(sitWeb);
		}
		
		String sitRec = sitAdicionalRecurso(expId);
		
		if (sitRec != null) {

			if (txt.length() > 0) {
				txt.append("; "); 
				}								
			txt.append(sitRec);
		}

		String sitMedidas = sitAdicionalMedidas(expId);
		
		if (sitMedidas != null) {

			if (txt.length() > 0) {
				txt.append("; "); 
				}								
			txt.append(sitMedidas);
		}

		if (txt.length() > 0) {
			txtSitAdicional = txt.toString();
		}

		return txtSitAdicional;
		
	}
	
	public String sitAdicionalPWEB(Long expId)  {
		
		String txtSitAdic = null;
		
		/** RESOLUCION PENDIENTE DE PUBLICAR: 
		 * - SE BUSCA EN TABLA DE RESOLUCIONES/EXPEDIENTES SI HAY RESOLUCIONES PENDIENTES DE PUBLICAR.
		 * - **/
		
		List<ResolucionExpediente> resolExpedientes = resolucionExpedienteService.findListResolucionExpByIdExpediente(expId);
		
		for(ResolucionExpediente resolExp : resolExpedientes){
			
			if(resolExp.getResolucion().getFechaPublicacionWeb() == null  ) {
				txtSitAdic ="Pendiente publicación WEB";
			}
		
		}
		
		return txtSitAdic;
		
	}
	

	public String sitAdicionalRecurso(Long expId)  {
		
		String txtSitAdic = null;
		
		/** RESOLUCION PENDIENTE DE PUBLICAR: 
		 * - SE BUSCA EN TABLA DE RESOLUCIONES/EXPEDIENTES SI HAY RESOLUCIONES PENDIENTES DE PUBLICAR.
		 * - **/
		
		ValoresDominio valPlazo =  valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_TIPO_PLAZO, Constantes.COD_VAL_DOM_REC);
		
		PlazosExpdte plazosExped = plazosExpdteService.findPlazosExpdteByExpTipPla(expId, valPlazo.getId());
		
		if (plazosExped != null) {
		
				txtSitAdic ="Pendiente Res. Recurso";
			}
		
		
		return txtSitAdic;
		
	}
	
	public String sitAdicionalMedidas(Long expId)  {
		
		String txtSitAdic = null;
		
		/** EXPEDIENTE CON MEDIDAS IMPUESTAS: SE COMPRUEBA EL INDICADOR DE MEDIDAS IMPUESTAS EN EL EXPEDIENTE. 
		 * ESTE INDICADOR SE PONE A TRUE CUANDO SE IMPONEN MEDIDAS EN ACUERDO DE INADMISIÓN O EN RESOLUCION, Y A FALSE CUANDO SE ACREDITA EL CUMPLIMIENTO.
		 * - **/
		
		Expedientes exped = expedientesService.obtener(expId);
		
		if (Boolean.TRUE.equals(exped.getImposicionMedidas())) {
				txtSitAdic ="Medidas pdte. acreditar";
		}
		
		return txtSitAdic;
		
	}

	
		public String onConsultarExp(Long idExp) {
			JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, numerosSaltos+1);
			numerosSaltos = (Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS);
			JsfUtils.setFlashAttribute(IDEXPSESSION, idExp);
			JsfUtils.setSessionAttribute(Constantes.VUELTA_EXPRELACIONADOS, true);
			JsfUtils.setSessionAttribute(Constantes.IDEXPEDIENTERELACIONADOSELECCIONADO, expedientes);
			ContextoVolver v = volverBean.crearContexto(ListadoNavegaciones.FORM_EXPEDIENTES.getRegla(), "_volverExpRelacionados_");
			v.put(IDEXPRELACIONADOACTUAL, expedientes.getId());
			v.put(IDEXPRELACIONADOCONSULTA,idExp);
			v.put(EDITABLE, JsfUtils.getSessionAttribute(Constantes.EDITABLE));
			Expedientes expedientesCons = expedientesService.obtener(idExp);
			navegacionBean.setTxtMigaPan(Constantes.CONSULTA_EXPEDIENTE+expedientesCons.getMigaDePan()+ navegacionBean.expedientesRelacionadosMotivoPsan(expedientesCons.getId()));
			return ListadoNavegaciones.FORM_EXPEDIENTES.getRegla();
		}

}
