package es.juntadeandalucia.ctpda.gestionpdt.web.expedientes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.CfgTipoExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.ExpedientesMaestra;
import es.juntadeandalucia.ctpda.gestionpdt.model.Personas;
import es.juntadeandalucia.ctpda.gestionpdt.model.PersonasExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.SujetosObligadosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.Usuario;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.service.CfgTipoExpedienteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.PersonasExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.PersonasService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ValoresDominioService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.util.FechaUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.Constantes;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.JsfUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.LazyDataModelByQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.MyFilterMeta;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.BaseBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.ContextoVolver;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.UtilsComun;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.VolverBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@ViewScoped
public class DatosExpedientesDatosPersonasBean extends BaseBean implements Serializable {

	private static final String MENSAJESFORMULARIO = "messagesFormulario";
	private static final String MESSAGESDIALOGPERSONAS = "messagesDialogPersonas";
	private static final String ANYADIDACORRECTAMENTE = "anyadida.correctamente";
	private static final String GUARDADACORRECTAMENTE = "guardada.correctamente";
	private static final String MENSAJEPERSONA = "Persona ";
	private static final String ACTUALIZADOCORRECTAMENTE = "actualizada.correctamente";
	private static final String MENSAJESPESTANYAPERSONA = "messagesFormularioListadoDatosPersona";
	private static final String MENSAJEERROR = "error";
	private static final String TABLAPERSONAEXPEDIENTENULO = "formFormularioExpedientes:tabViewPestanasExpediente:tablaPersonasExpedientesExpNulo";
	private static final String TABLAPERSONAEXPEDIENTENONULO = "formFormularioExpedientes:tabViewPestanasExpediente:tablaPersonasExpedientes";
	private static final String GUARDARPERSISTENCIA = "cambios.efectivos.boton.guardar";
	private static final String EXPEDIENTESDP = "expedientesDP";
	private static final String TABPERSONAREPRE = "formFormularioExpedientes:tabViewPestanasExpediente:bloqueDatosRepresentante";
	private static final String BUSCADORREPRESENTANTE = "buscador.representante";
	private static final String BUSCADORPERSONA = "buscador.persona";
	private static final String PARAELEXPEDIENTE = " para el expediente ";
	private static final String EXPEDIENTEFORMULARIO = "expedienteFormulario";
	
	private static final long serialVersionUID = 1L;
	@Getter
	private LazyDataModelByQueryService<PersonasExpedientes> lazyModelPersonasExpedientes;
	@Getter
	private LazyDataModelByQueryService<Personas> lazyModelPersonas;
	@Getter
	@Setter
	private Expedientes expedientes;
	@Getter
	@Setter
	private Personas personas;
	@Getter
	@Setter
	private Personas personasRepre;
	@Getter
	@Setter
	private PersonasExpedientes personaExpedienteSeleccionada;
	@Getter
	@Setter
	private PersonasExpedientes personasExpedientesPrincipal;
	@Getter
	@Setter
	private PersonasExpedientes personasExpedientes;
	@Getter
	@Setter
	private Expedientes expedientePersonasExpedientes;
	@Getter
	@Setter
	private ExpedientesMaestra expedientesMaestra;
	@Getter
	@Setter
	private List<PersonasExpedientes> listaPersonasExpedientes;
	@Getter
	@Setter
	private List<PersonasExpedientes> listaPersonasExpedientesAux;
	@Autowired
	private ExpedientesService expedientesService;
	@Autowired
	private PersonasExpedientesService personasExpedientesService;
	@Autowired
	private PersonasService personasService;

	@Getter
	@Setter
	private Boolean principal;
	
	@Getter
	@Setter
	private Boolean interesado;
	
	@Getter
	@Setter
	private PersonasExpedientes idPersonas;

	@Getter
	@Setter
	private PersonasExpedientes selectedExpedientesPersonas;
	@Getter
	@Setter
	private Personas selectedPersonas;

	@Getter
	@Setter
	private Long selectedNuevoMotivoRelacionId;
	@Getter
	@Setter
	private String nombreRazonsocial;
	@Getter
	@Setter
	private String primerApellido;
	@Getter
	@Setter
	private String segundoApellido;

	@Getter
	@Setter
	private ValoresDominio valoresRelacionExpPer;

	@Getter
	@Setter
	private Long idPersona;
	@Getter
	@Setter
	private Long idPersonaRepre;

	@Getter
	@Setter
	boolean soloLectura;
	@Getter
	@Setter
	private PersonasExpedientes nuevaPersonaExp;
	@Getter
	@Setter
	private Personas nuevaPersona;
	@Getter
	@Setter
	private PersonasExpedientes personaAux;

	@Getter
	@Setter
	private String nombreRazonSocialFiltro;

	@Getter
	@Setter
	private String cifnifFiltro;
	
	@Getter
	@Setter
	private String nombreRazonSocialFiltroRepre;

	@Getter
	@Setter
	private String cifnifFiltroRepre;

	@Getter
	@Setter
	private List<ValoresDominio> listaValoresMotivosRelacionPersonaExpediente;

	@Getter
	@Setter
	private boolean plegado;

	
	@Autowired
	private ValoresDominioService valoresDominioService;

	@Getter
	@Setter
	private String cabeceraDialogoMotivoRelacion;

	@Getter
	@Setter
	private String tipoPersonaSeleccionado;
	@Getter
	@Setter
	private Long idTipoPersonaRepresentanteSeleccionado;
	@Getter
	@Setter
	private Boolean esPersonaFisica;
	@Getter
	@Setter
	private String personaPrimerApellido;
	@Getter
	@Setter
	private String personaSegundoApellido;
	@Getter
	@Setter
	private String personaIniciales;
	@Getter
	@Setter
	private String personaNIFCIF;
	@Getter
	@Setter
	private String personaSexo;
	@Getter
	@Setter
	private String personaDireccion;
	@Getter
	@Setter
	private String personaProvincia;
	@Getter
	@Setter
	private String personaLocalidad;
	@Getter
	@Setter
	private String personaCodigoPostal;
	@Getter
	@Setter
	private Long personaTelefono;
	@Getter
	@Setter
	private String personaEmail;
	@Getter
	@Setter
	private String personaRepreNombre;
	@Getter
	@Setter
	private String personaReprePrimerApellido;
	@Getter
	@Setter
	private String personaRepreSegundoApellido;
	@Getter
	@Setter
	private String personaRepreIniciales;
	@Getter
	@Setter
	private String personaRepreNIFCIF;
	@Getter
	@Setter
	private String personaRepreSexo;
	@Getter
	@Setter
	private String personaRepreDireccion;
	@Getter
	@Setter
	private String personaRepreProvincia;
	@Getter
	@Setter
	private String personaRepreLocalidad;
	@Getter
	@Setter
	private String personaRepreCodigoPostal;
	@Getter
	@Setter
	private Long personaRepreTelefono;
	@Getter
	@Setter
	private String personaRepreEmail;
	
	@Getter
	@Setter
	private List<String> listaCodigoPermisos;

	@Getter	@Setter
	private Boolean permisoBusExpPer;
	
	@Getter	@Setter
	private Boolean permisoNewExpPer;
	
	@Getter	@Setter
	private Boolean permisoDelExpPer;
	
	@Getter	@Setter
	private Boolean permisoEditExpPer;
	
	@Getter	@Setter
	private Boolean permisoBusExpPerRep;
	
	@Getter	@Setter
	private Boolean permisoDelExpPerRep;

	@Autowired
	private VolverBean volverBean;
	
	@Autowired
	private DatosExpedientesBean datosExpedientesBean;
	
	@Autowired
	private UtilsComun utilsComun;
	@Getter
	@Setter
	private SujetosObligadosExpedientes sujetosObligadosExpedientes;
	
	@Getter
	@Setter
	private Long idPersonaExpSesion;
	
	@Getter
	@Setter
	private Long idPersonaRepreSesion;
	
	@Getter
	@Setter
	private Long idPersonaDpd;
	@Getter
	@Setter
	private Long idSujObligExp;
	
	@Getter
	@Setter
	private Long idTipoExpediente;
	
	@Getter
	@Setter
	private String codigoTipoExpediente;
	
	@Getter
	@Setter
	private String cabeceraDialogBuscadorPersona;
	@Getter
	@Setter
	private String cabeceraDialogBuscadorPersonaRepre;
	
	@Autowired
	private CfgTipoExpedienteService cfgTipoExpedienteService;
	
	@Getter	@Setter
	private Boolean permisoConsPers;

	@PostConstruct
	@Override
	public void init() {
		super.init();
		
		
		/**
		 * GESTION DE PERMISOS
		 * */
		listaCodigoPermisos = (List<String>) JsfUtils.getSessionAttribute(Constantes.LISTACODIGOPERMISOS);
		
		permisoBusExpPer = listaCodigoPermisos.contains(Constantes.PERMISO_BUS_EXPPER);
		
		permisoNewExpPer = listaCodigoPermisos.contains(Constantes.PERMISO_NEW_EXPPER);
		
		permisoDelExpPer = listaCodigoPermisos.contains(Constantes.PERMISO_DEL_EXPPER);
		
		permisoEditExpPer = listaCodigoPermisos.contains(Constantes.PERMISO_EDIT_EXPPER);
		
		permisoBusExpPerRep = listaCodigoPermisos.contains(Constantes.PERMISO_BUS_EXPPERREP);
		
		permisoDelExpPerRep = listaCodigoPermisos.contains(Constantes.PERMISO_DEL_EXPPERREP);
		
		permisoConsPers = listaCodigoPermisos.contains(Constantes.PERMISO_CONS_PERS);
				
		idPersonaExpSesion=(Long) JsfUtils.getSessionAttribute("idPersonaExp");
		idPersonaRepreSesion=(Long) JsfUtils.getSessionAttribute("idPersonaRepre");		
		idPersonaDpd=(Long) JsfUtils.getSessionAttribute("idPersonaDpd");
		idSujObligExp=(Long) JsfUtils.getSessionAttribute("idSujObligExp");
		idTipoExpediente=(Long) JsfUtils.getSessionAttribute("idTipoExpediente");
		
		cargarExpediente();
		
		JsfUtils.removeSessionAttribute("idPersonaExp");
		JsfUtils.removeSessionAttribute("idPersonaRepre");
		JsfUtils.removeSessionAttribute("idPersonaDpd");
		JsfUtils.removeSessionAttribute("idSujObligExp");
		JsfUtils.removeSessionAttribute("personaRepre");
		JsfUtils.removeSessionAttribute("personaDpd");

	
		lazyModelPersonasExpedientes = new LazyDataModelByQueryService<>(PersonasExpedientes.class,personasExpedientesService);
		lazyModelPersonasExpedientes.setPreproceso((a, b, c, filters) -> {
			if (expedientes.getId() != null) {
				filters.put("expediente.id", new MyFilterMeta(expedientes.getId()));
			} else {
				filters.put("expediente.id", new MyFilterMeta(null));

			}
		});
		
		calcularTipoExpediente(idTipoExpediente, expedientes);
		
		lazyModelPersonas = new LazyDataModelByQueryService<>(Personas.class, personasService);
		lazyModelPersonas.setPreproceso((a, b, c, filters) -> {
			if (nombreRazonSocialFiltro != null && !nombreRazonSocialFiltro.isEmpty()) {
				filters.put("#nombreAp", new MyFilterMeta(nombreRazonSocialFiltro));
			}
			if (cifnifFiltro != null && !cifnifFiltro.isEmpty()) {
				filters.put("nifCif", new MyFilterMeta(cifnifFiltro));
			}
			
			if (nombreRazonSocialFiltroRepre != null && !nombreRazonSocialFiltroRepre.isEmpty()) {
				filters.put("#nombreAp", new MyFilterMeta(nombreRazonSocialFiltroRepre));
			}
			if (cifnifFiltroRepre != null && !cifnifFiltroRepre.isEmpty()) {
				filters.put("nifCif", new MyFilterMeta(cifnifFiltroRepre));
			}

			filters.put("activa", new MyFilterMeta("true"));

		});

		listaValoresMotivosRelacionPersonaExpediente = valoresDominioService
				.findValoresMotivosRelacionPersonaExpediente();
		personasExpedientes = new PersonasExpedientes();
		
		cabeceraDialogoMotivoRelacion = "";
		
		plegado = true;
		
		personasRepre = null;
		
		cabeceraDialogBuscadorPersona();
		
		anyadePersonaPorDefectoSegunCfgTipoExpediente();
	}
	
	private void cabeceraDialogBuscadorPersona () {
		if(expedientes.getId()!=null) {
			cabeceraDialogBuscadorPersonaRepre=getMessage(BUSCADORREPRESENTANTE)+" del expediente "+ expedientes.getNumExpediente();
			cabeceraDialogBuscadorPersona=getMessage(BUSCADORPERSONA)+PARAELEXPEDIENTE+ expedientes.getNumExpediente();
		}else {
			cabeceraDialogBuscadorPersonaRepre=getMessage(BUSCADORREPRESENTANTE);
			cabeceraDialogBuscadorPersona=getMessage(BUSCADORPERSONA);
		}
	}
	
	/**CALCULAMOS EL TIPO DE EXPEDIENTE CON EL QUE ESTAMOS TRABAJANDO. 
	 * SI EL TIPO DE EXPEDIENTE LO TENEMOS DEL IDTIPOEXPEDIENTE (SESION) ES QUE ESTAMOS EN UN ALTA Y POR TANTO TENEMOS EL TIPO DE EXPEDIENTE DE SESIÓN.
	 * SI NO TENEMOS EL IDTIPOEXPEDIENTE (DE SESION) ENTONCES VERIFICAMOS QUE ESTAMOS EN LA EDICIÓN DE UN EXPEDIENTE Y LO RECUPERAMOS DEL PROPIO EXPEDIENTE. **/
	private void calcularTipoExpediente(Long idTipoExpediente, Expedientes expedientes)
	{
		if(idTipoExpediente != null)
		{
			ValoresDominio valDomTipExp = valoresDominioService.obtener(idTipoExpediente);
			if(valDomTipExp != null)
			{
				codigoTipoExpediente = valDomTipExp.getCodigo();
			}
		}else if(expedientes.getId() != null){
			codigoTipoExpediente = expedientes.getValorTipoExpediente().getCodigo();
		}
		
	}
	
	private void cargarExpediente() {
		Boolean esPersonaRepre = (Boolean) JsfUtils.getSessionAttribute("personaRepre");
		Boolean esPersonaDpd = (Boolean) JsfUtils.getSessionAttribute("personaDpd");
		ContextoVolver cv = volverBean.getContexto(); //Sólo get, ya se ha recogido en DatosExpedientesBean
		
		if(cv != null) {
			cifnifFiltro = (String) cv.get("identificador_persona");
			cifnifFiltroRepre = (String) cv.get("identificador_persona");
			if(cifnifFiltro != null || cifnifFiltroRepre != null) {
				PersonasExpedientes personaExpedienteSeleccionadaAux = (PersonasExpedientes) cv.get("personasExpedientes");
				if(personaExpedienteSeleccionadaAux != null && personaExpedienteSeleccionadaAux.getId() != null) {				
					personaExpedienteSeleccionada = personasExpedientesService.obtener(personaExpedienteSeleccionadaAux.getId());
				}
				
				final FacesMessage msgVueltaAtras = (FacesMessage)cv.get("mensaje_ok");
				if(null != msgVueltaAtras) {
					FacesContext.getCurrentInstance().addMessage(MESSAGESDIALOGPERSONAS, msgVueltaAtras);
				}
			}else {
				personaExpedienteSeleccionada = null;
			}			
			
			listaPersonasExpedientes = (List<PersonasExpedientes>) JsfUtils.getSessionAttribute(EXPEDIENTESDP);
			executeScriptCargaExpediente(esPersonaRepre,esPersonaDpd);
		} else {
			personaExpedienteSeleccionada = null;
			listaPersonasExpedientes = new ArrayList<>();
			JsfUtils.setSessionAttribute(EXPEDIENTESDP, listaPersonasExpedientes);
		}
		
		//carga normal
		Expedientes expedienteFormulario = (Expedientes) JsfUtils.getSessionAttribute(EXPEDIENTEFORMULARIO);
		
		if (expedienteFormulario.getId() != null) {
			expedientes = expedientesService.obtener(expedienteFormulario.getId());
		} else {
			expedientes = new Expedientes();
		}	
		//fin
	}
	
	private void executeScriptCargaExpediente(Boolean esPersonaRepre, Boolean esPersonaDpd) {
		if(idPersonaExpSesion!=null || idPersonaRepreSesion!=null) {				
			PrimeFaces.current().executeScript("PF('tabViewPestanasExpediente').show()");	
		}else if(idPersonaDpd!=null) {
			PrimeFaces.current().executeScript("PF('tabPestanaDatosSujetos').show()");	
		}else if(idSujObligExp!=null) {
			PrimeFaces.current().executeScript("PF('tabPestanaDatosSujetos').show()");	
		}else if(Boolean.TRUE.equals(esPersonaDpd)) {
			PrimeFaces.current().executeScript("PF('dialogPersonaDpd').show()");
		}else if(Boolean.TRUE.equals(esPersonaRepre)){
			PrimeFaces.current().executeScript("PF('dialogPersonaRepre').show()");
		}else {
			//Abrimos el diálogo de búsqueda de personas
			PrimeFaces.current().executeScript("PF('dialogPersonaExp').show()");
		}
	}

	public void abrirAsignarMotRela(PersonasExpedientes personaExpedientesSeleccionada, Personas personaSeleccionada,String accion) {

		soloLectura = false;
		selectedNuevoMotivoRelacionId = null;
		Expedientes expedienteFormulario = (Expedientes) JsfUtils.getSessionAttribute(EXPEDIENTEFORMULARIO);
		if(expedienteFormulario.getId()!=null) {
			expedientes = expedientesService.obtener(expedienteFormulario.getId());
			
			if(expedientes.getValorTipoExpediente() != null)
			{
				codigoTipoExpediente = expedientes.getValorTipoExpediente().getCodigo();
			}
		}
		

		if ("asignarMotRela".equals(accion)) {
			cabeceraDialogoMotivoRelacion = "Seleccionar motivo de relación";
		} else if ("editarPersonaExp".equals(accion)) {
			if(expedientes.getId()!=null) {
				cabeceraDialogoMotivoRelacion = "Editar motivo de relación del expediente "+ expedientes.getNumExpediente();
			}else {
				cabeceraDialogoMotivoRelacion = "Editar motivo de relación del expediente";
			}
			
		}
		
		personasExpedientes = personaExpedientesSeleccionada;
		if (personaExpedientesSeleccionada != null) {			
			this.selectedNuevoMotivoRelacionId = personaExpedientesSeleccionada.getValoresRelacionExpPer().getId();
			this.interesado = personaExpedientesSeleccionada.getInteresado();
		}
		nuevaPersona = personaSeleccionada;
		
		CfgTipoExpediente cfgTipoExpediente = cfgTipoExpedienteService.obtenerCfgTipoExpedientePorValorTipoExpediente((expedientes.getId()!=null)?expedientes.getValorTipoExpediente().getId():expedienteFormulario.getValorTipoExpediente().getId());
		if(cfgTipoExpediente != null && cfgTipoExpediente.getValorMotivoRelacionPersona() != null) {
			selectedNuevoMotivoRelacionId = cfgTipoExpediente.getValorMotivoRelacionPersona().getId();
		}
		
		
		
		PrimeFaces.current().executeScript("PF('dialogAsignarMotRela').show();");
	}

	public void limpiarFiltro() {
		nombreRazonSocialFiltro = "";
		cifnifFiltro = "";
		nombreRazonSocialFiltroRepre = "";
		cifnifFiltroRepre = "";

	}

	public boolean validacionesGuardar() {
		boolean valido = true;

		if (this.selectedNuevoMotivoRelacionId == null) {
			valido = false;
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString("campos.obligatorios"));
			PrimeFaces.current().dialog().showMessageDynamic(message);
		}
		return valido;

	}

	public void asignarMotRelaPersonaExpediente() throws BaseException {
		Expedientes expedienteFormulario = (Expedientes) JsfUtils.getSessionAttribute(EXPEDIENTEFORMULARIO);
		boolean puedoGuardar = validacionesGuardar();
		if (puedoGuardar) {
			ValoresDominio tipRela = valoresDominioService.obtener(this.selectedNuevoMotivoRelacionId);
			if (personasExpedientes == null) { // estoy añadiendo una persona nueva
				if (expedientes.getId() == null) {
					FacesContext.getCurrentInstance().addMessage(MENSAJESPESTANYAPERSONA, new FacesMessage(FacesMessage.SEVERITY_INFO, "", 
							MENSAJEPERSONA + nuevaPersona.getNombreRazonsocial() +" " + mensajesProperties.getString(ANYADIDACORRECTAMENTE) + mensajesProperties.getString(GUARDARPERSISTENCIA)));
				} else {
					FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
							MENSAJEPERSONA + nuevaPersona.getNombreRazonsocial() + PARAELEXPEDIENTE+ expedienteFormulario.getNumExpediente()+" "+ mensajesProperties.getString(ACTUALIZADOCORRECTAMENTE)));
				}

			}else if (expedientes.getId() != null) { // estoy modificando el motivo de relación de la persona
				FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
								MENSAJEPERSONA +" "+personasExpedientes.getPersonas().getNombreRazonsocial()+PARAELEXPEDIENTE+ expedienteFormulario.getNumExpediente()+" "+ mensajesProperties.getString(ACTUALIZADOCORRECTAMENTE)));
			}else {
				FacesContext.getCurrentInstance().addMessage(MENSAJESPESTANYAPERSONA, new FacesMessage(FacesMessage.SEVERITY_INFO, "", 
						MENSAJEPERSONA +" "+personasExpedientes.getPersonas().getNombreRazonsocial()+" "+ mensajesProperties.getString(GUARDADACORRECTAMENTE) + mensajesProperties.getString(GUARDARPERSISTENCIA)));
			}

			asignarByIdExpedienteNull(tipRela);
			
			limpiarAsignarMotRelaPersona();
			
			if(expedientes.getId() != null) {
				datosExpedientesBean.actualizarCabecera(expedientes,null,null,null);
			}			
		}
	}
	
	private void asignarByIdExpedienteNull(ValoresDominio tipRela) throws BaseException {
		if (expedientes.getId() == null) {
			if (personasExpedientes != null && personasExpedientes.getPersonas() != null && selectedNuevoMotivoRelacionId != null) {
				// opcion editar personas expedientes
				for (PersonasExpedientes personaExpedienteAux : listaPersonasExpedientes) {
					if (personaExpedienteAux.equals(personasExpedientes)) {
						ValoresDominio valoresRelacionExpPerAux = valoresDominioService
								.obtener(selectedNuevoMotivoRelacionId);
						personaExpedienteAux.setValoresRelacionExpPer(valoresRelacionExpPerAux);
						actualizaInteresado(personaExpedienteAux);
					}
				}
			} else {
				// opcion guardar personas expedientes
				PersonasExpedientes personaExp = new PersonasExpedientes();

				boolean esPrincipal = esPrincipal(listaPersonasExpedientes);
				personaExp.setPrincipal(esPrincipal);
				
				personaExp.setValoresRelacionExpPer(tipRela);
				actualizaInteresado(personaExp);				
				personaExp.setPersonas(nuevaPersona);

				listaPersonasExpedientes.add(personaExp);
			}

			JsfUtils.setSessionAttribute(EXPEDIENTESDP, listaPersonasExpedientes);
			PrimeFaces.current().ajax().update(TABLAPERSONAEXPEDIENTENULO);

		} else {
			asignarByIdExpedienteNotNull(tipRela);
		}
	}
	
	private void asignarByIdExpedienteNotNull (ValoresDominio tipRela) throws BaseException {
		if (personasExpedientes != null && personasExpedientes.getPersonas() != null && selectedNuevoMotivoRelacionId != null) {
			// opcion editar persona expediente
			personasExpedientes.setValoresRelacionExpPer(tipRela);
			actualizaInteresado(personasExpedientes);
			personasExpedientes = personasExpedientesService.guardar(personasExpedientes);
		} else {
			personasExpedientes = new PersonasExpedientes();
			// opcion guardar personas expedientes
			List<PersonasExpedientes> listaPersonasConExpediente = personasExpedientesService.obtenerListaPersonaExpedientePorExpedientePrincipalYNoPrincipal(expedientes.getId());
			boolean esPrincipal = esPrincipal(listaPersonasConExpediente);
			personasExpedientes.setPrincipal(esPrincipal);
			
			personasExpedientes.setExpediente(expedientes);
			personasExpedientes.setPersonas(nuevaPersona);
			personasExpedientes.setValoresRelacionExpPer(tipRela);
			actualizaInteresado(personasExpedientes);
			personasExpedientes = personasExpedientesService.guardar(personasExpedientes);
		}
		
		expedientes = expedientesService.obtener(expedientes.getId());
		expedientes = utilsComun.expedienteUltimaModificacion(expedientes,personasExpedientes.getFechaModificacion(),personasExpedientes.getFechaCreacion(),personasExpedientes.getUsuModificacion(),personasExpedientes.getUsuCreacion());
		
		datosExpedientesBean.actualizarCabecera(expedientes,personasExpedientes, null, null);
	}
	
	private boolean esPrincipal (List<PersonasExpedientes> personasExpedientes) {
		boolean res = false;		
		if(personasExpedientes.isEmpty()) {
			res = true;
		}
		return res;
	}
	
	private void actualizaInteresado(PersonasExpedientes personasExpedientes)
	{
		if(personasExpedientes != null)
		{
			if(Constantes.PSAN.equals(codigoTipoExpediente))
			{
				personasExpedientes.setInteresado(this.interesado);
			}else{
				personasExpedientes.setInteresado(false);
			}
		}
	}

	private void limpiarAsignarMotRelaPersona() {
		nuevaPersonaExp = new PersonasExpedientes();
		this.selectedNuevoMotivoRelacionId = null;
		this.interesado = false;
		PrimeFaces.current().executeScript("PF('dialogAsignarMotRela').hide();");
		PrimeFaces.current().executeScript("PF('dialogPersonaExp').hide();");
	}

	public void cambioCheckPrincipal(PersonasExpedientes personaExpedienteSeleccionada) {
		/** desmarcar principal */
		if (Boolean.FALSE.equals(personaExpedienteSeleccionada.getPrincipal())) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "La persona debe ser principal. El expediente siempre debe tener una persona principal relacionada con él.");
			PrimeFaces.current().dialog().showMessageDynamic(message);
		}

		/** de una manera u otra siempre va a ponerse principal a true */

		/** caso de nuevo expediente */
		if (expedientes.getId() == null) {
			for (PersonasExpedientes p : listaPersonasExpedientes) {
				p.setPrincipal(false);
				if (p.equals(personaExpedienteSeleccionada)) {
					p.setPrincipal(true);
				}
			}
			PrimeFaces.current().ajax().update(TABLAPERSONAEXPEDIENTENULO);
			FacesContext.getCurrentInstance().addMessage(MENSAJESPESTANYAPERSONA, new FacesMessage(FacesMessage.SEVERITY_INFO, "", 
					MENSAJEPERSONA +" "+personasExpedientes.getPersonas().getNombreRazonsocial()+PARAELEXPEDIENTE+ mensajesProperties.getString(GUARDADACORRECTAMENTE) + mensajesProperties.getString(GUARDARPERSISTENCIA)));

		} else {
			/** caso expediente ya creado */
			cambioCheckExpedienteCreado(personaExpedienteSeleccionada);
		}
		
		if(expedientes.getId() != null) {
			datosExpedientesBean.actualizarCabecera(expedientes,null,null,null);
		}
	}

	private void cambioCheckExpedienteCreado (PersonasExpedientes personaExpedienteSeleccionada) {
		List<PersonasExpedientes> listaPersonasConExpediente = personasExpedientesService
				.obtenerListaPersonaExpedientePorExpedientePrincipalYNoPrincipal(expedientes.getId());
		PersonasExpedientes personaPrincipalActual = new PersonasExpedientes();
		
		for (PersonasExpedientes perConExp : listaPersonasConExpediente) {
			if (perConExp.getId().equals(personaExpedienteSeleccionada.getId())) {
				perConExp.setPrincipal(true);
				personaPrincipalActual = perConExp;
			} else if (Boolean.TRUE.equals(perConExp.getPrincipal())) {
				perConExp.setPrincipal(false);
			}
			try {
				perConExp = personasExpedientesService.guardar(perConExp);
				expedientes = expedientesService.obtener(expedientes.getId());
				expedientes = utilsComun.expedienteUltimaModificacion(expedientes,perConExp.getFechaModificacion(),perConExp.getFechaCreacion(),perConExp.getUsuModificacion(),perConExp.getUsuCreacion());
			} catch (final BaseException e) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MENSAJEERROR));
				PrimeFaces.current().dialog().showMessageDynamic(message);
				log.error(e.getMessage());
			}
		}
		PrimeFaces.current().ajax().update(TABLAPERSONAEXPEDIENTENONULO);
		FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
				MENSAJEPERSONA +" "+personasExpedientes.getPersonas().getNombreRazonsocial()+PARAELEXPEDIENTE+ expedientes.getNumExpediente()+" "+ mensajesProperties.getString(ACTUALIZADOCORRECTAMENTE)));
		
		if(Boolean.TRUE.equals(personaPrincipalActual.getPrincipal())) {
			datosExpedientesBean.actualizarCabecera(expedientes,personaPrincipalActual,null,null);
		}else {
			datosExpedientesBean.actualizarCabecera(expedientes,null,null,null);
		}
	}
	
	public void asignarPersonaRepre(Personas personaRepresentanteSeleccionada) throws BaseException {	
		this.personasRepre = personaRepresentanteSeleccionada;
		if(expedientes.getId() != null) {
			asignarPersonaRepreExpedienteIdNotNull(personaRepresentanteSeleccionada);
		}else {
			if(personaExpedienteSeleccionada==null) {
				for (PersonasExpedientes p : listaPersonasExpedientes) {
						p.setPersonasRepre(personaRepresentanteSeleccionada);					
				}
				PrimeFaces.current().ajax().update(TABLAPERSONAEXPEDIENTENULO);
				FacesContext.getCurrentInstance().addMessage(MENSAJESPESTANYAPERSONA,new FacesMessage(FacesMessage.SEVERITY_INFO, "","Representante asignado correctamente a la persona expediente seleccionada" + mensajesProperties.getString(GUARDARPERSISTENCIA)));
			}else {
			personaExpedienteSeleccionada.setPersonasRepre(personaRepresentanteSeleccionada);
			for (PersonasExpedientes p : listaPersonasExpedientes) {
				if (p.equals(personaExpedienteSeleccionada)) {
					p.setPersonasRepre(personaRepresentanteSeleccionada);
				}
			}
			PrimeFaces.current().ajax().update(TABLAPERSONAEXPEDIENTENULO);
			FacesContext.getCurrentInstance().addMessage(MENSAJESPESTANYAPERSONA,new FacesMessage(FacesMessage.SEVERITY_INFO, "","Representante asignado correctamente a la persona expediente seleccionada" + mensajesProperties.getString(GUARDARPERSISTENCIA)));
		} 
		}

		PrimeFaces.current().executeScript("PF('dialogPersonaRepre').hide();");
		PrimeFaces.current().ajax().update(TABPERSONAREPRE);
		PrimeFaces.current().ajax().update("formFormularioExpedientes:expedienteRepresentante");
	}

	private void asignarPersonaRepreExpedienteIdNotNull (Personas personaRepresentanteSeleccionada) throws BaseException {
		PersonasExpedientes personaExpedienteSeleccionadaAux = personasExpedientesService.obtener(personaExpedienteSeleccionada.getId());
		personaExpedienteSeleccionadaAux.setPersonasRepre(personaRepresentanteSeleccionada);
		personaExpedienteSeleccionadaAux = personasExpedientesService.guardar(personaExpedienteSeleccionadaAux);
		
		expedientes = expedientesService.obtener(expedientes.getId());
		expedientes = utilsComun.expedienteUltimaModificacion(expedientes,personaExpedienteSeleccionadaAux.getFechaModificacion(),personaExpedienteSeleccionadaAux.getFechaCreacion(),personaExpedienteSeleccionadaAux.getUsuModificacion(),personaExpedienteSeleccionadaAux.getUsuCreacion());
		
		PrimeFaces.current().ajax().update(TABLAPERSONAEXPEDIENTENONULO);
		FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO,new FacesMessage(FacesMessage.SEVERITY_INFO, "",MENSAJEPERSONA +" "+personaExpedienteSeleccionadaAux.getPersonas().getNombreRazonsocial()+PARAELEXPEDIENTE+ expedientes.getNumExpediente()+" "+ mensajesProperties.getString(ACTUALIZADOCORRECTAMENTE)));
		
		if(Boolean.TRUE.equals(personaExpedienteSeleccionada.getPrincipal())) {
			datosExpedientesBean.actualizarCabecera(expedientes,personaExpedienteSeleccionadaAux,null,null);
		}else {
			datosExpedientesBean.actualizarCabecera(expedientes,null,null,null);
		}
	}

	public void onRowSelect(final SelectEvent<PersonasExpedientes> event) {
		plegado = false;						
		personaExpedienteSeleccionada = event.getObject();
		this.personas = event.getObject().getPersonas();
		this.personasRepre = event.getObject().getPersonasRepre();
		PrimeFaces.current().ajax().update("formFormularioExpedientes:tabViewPestanasExpediente:bloqueDatosPersona");
		PrimeFaces.current().ajax().update(TABPERSONAREPRE);
	}

	public void quitarPersonaRepre() throws BaseException {
		if(expedientes.getId() != null) {
			PersonasExpedientes personaExpedienteSeleccionadaAux = personasExpedientesService.obtener(personaExpedienteSeleccionada.getId());
			this.personasRepre = new Personas();
			personaExpedienteSeleccionadaAux.setPersonasRepre(null);			
			personaExpedienteSeleccionadaAux = personasExpedientesService.guardar(personaExpedienteSeleccionadaAux);
			
			expedientes = expedientesService.obtener(expedientes.getId());
			expedientes = utilsComun.expedienteUltimaModificacion(expedientes,personaExpedienteSeleccionadaAux.getFechaModificacion(),personaExpedienteSeleccionadaAux.getFechaCreacion(),personaExpedienteSeleccionadaAux.getUsuModificacion(),personaExpedienteSeleccionadaAux.getUsuCreacion());
			
			FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO,new FacesMessage(FacesMessage.SEVERITY_INFO, "",MENSAJEPERSONA +" "+personaExpedienteSeleccionada.getPersonas().getNombreRazonsocial()+PARAELEXPEDIENTE+ expedientes.getNumExpediente()+" "+ mensajesProperties.getString(ACTUALIZADOCORRECTAMENTE)));
			PrimeFaces.current().ajax().update(TABLAPERSONAEXPEDIENTENONULO);
			
			if(Boolean.TRUE.equals(personaExpedienteSeleccionada.getPrincipal())) {
				datosExpedientesBean.actualizarCabecera(expedientes,personaExpedienteSeleccionadaAux,null,null);
			}else {
				datosExpedientesBean.actualizarCabecera(expedientes,null,null,null);
			}		
		}else {
			personaExpedienteSeleccionada.setPersonasRepre(null);
			for (PersonasExpedientes p : listaPersonasExpedientes) {
				if (p.equals(personaExpedienteSeleccionada)) {
					p.setPersonasRepre(null);
				}
			}
			FacesContext.getCurrentInstance().addMessage(MENSAJESPESTANYAPERSONA,new FacesMessage(FacesMessage.SEVERITY_INFO, "","Representante desasignado correctamente de la persona expediente seleccionada" + mensajesProperties.getString(GUARDARPERSISTENCIA)));
			PrimeFaces.current().ajax().update(TABLAPERSONAEXPEDIENTENULO);
		} 
		
		personasRepre = null;
		PrimeFaces.current().ajax().update(TABPERSONAREPRE);
		PrimeFaces.current().ajax().update("formFormularioExpedientes:tabViewPestanasExpediente:bloqueDatosPersona");
		PrimeFaces.current().ajax().update("formFormularioExpedientes:expedienteRepresentante");
	}
	
	public void eliminarPersonaExpediente(PersonasExpedientes personasExpedientesEliminar) {
		try {
			if (expedientes.getId() != null) {
				List<PersonasExpedientes> listaPersonasDelExpediente = personasExpedientesService.obtenerListaPersonaExpedientePorExpedientePrincipalYNoPrincipal(expedientes.getId());
				for (PersonasExpedientes perExp : listaPersonasDelExpediente) {
					if (personasExpedientesEliminar.getId().equals(perExp.getId())) {
						if (perExp.getPrincipal().equals(false)) {
							personasExpedientesService.delete(personasExpedientesEliminar.getId());
							FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO,new FacesMessage(FacesMessage.SEVERITY_INFO, "",MENSAJEPERSONA +" "+personasExpedientesEliminar.getPersonas().getNombreRazonsocial()+PARAELEXPEDIENTE+ expedientes.getNumExpediente()+" "+ mensajesProperties.getString("actualizado.correctamente")));
							
							Usuario usuarioLogado = (Usuario) JsfUtils.getSessionAttribute(Constantes.USUARIO);
							expedientes = expedientesService.obtener(expedientes.getId());
							expedientes = utilsComun.expedienteUltimaModificacion(expedientes,FechaUtils.fechaYHoraActualDate(),FechaUtils.fechaYHoraActualDate(),usuarioLogado.getLogin(),usuarioLogado.getLogin());
						} else {
							FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString("persona.no.puede.eliminar"));
							PrimeFaces.current().dialog().showMessageDynamic(message);
						}
					}
				}
				datosExpedientesBean.actualizarCabecera(expedientes,null,null,null);
			} else {
				eliminarPersonaByIdExpedienteNull(personasExpedientesEliminar);
			}
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}

	}
	
	private void eliminarPersonaByIdExpedienteNull (PersonasExpedientes personasExpedientesEliminar) {
		for (PersonasExpedientes perExp : listaPersonasExpedientes) {

			if (personasExpedientesEliminar.equals(perExp)) {
				listaPersonasExpedientes.remove(personasExpedientesEliminar);
				FacesContext.getCurrentInstance().addMessage(MENSAJESPESTANYAPERSONA,new FacesMessage(FacesMessage.SEVERITY_INFO, "",MENSAJEPERSONA +" "+personasExpedientesEliminar.getPersonas().getNombreRazonsocial()+" "+ mensajesProperties.getString(GUARDADACORRECTAMENTE)+ mensajesProperties.getString(GUARDARPERSISTENCIA)));
				break;
			}
		}
	}
	
	public void abrirBuscadorPersonas (){
		nombreRazonSocialFiltroRepre ="";
		cifnifFiltroRepre = "";		
		nombreRazonSocialFiltro ="";
		cifnifFiltro = "";
		Expedientes expedienteFormulario = (Expedientes) JsfUtils.getSessionAttribute(EXPEDIENTEFORMULARIO);
		
		if (expedienteFormulario.getId() != null) {
			expedientes = expedientesService.obtener(expedienteFormulario.getId());
		} else {
			expedientes = new Expedientes();
		}	
		if(expedientes.getId()!=null) {
			cabeceraDialogBuscadorPersona=getMessage(BUSCADORPERSONA)+PARAELEXPEDIENTE+ expedientes.getNumExpediente();
		}else {
			cabeceraDialogBuscadorPersona=getMessage(BUSCADORPERSONA);
		}
		
		PrimeFaces.current().executeScript("PF('dialogPersonaExp').show();");
	}
		
	public void abrirBuscadorPersonasRepresentante (){
		nombreRazonSocialFiltro ="";
		cifnifFiltro = "";
		nombreRazonSocialFiltroRepre ="";
		cifnifFiltroRepre = "";		
		
		Expedientes expedienteFormulario = (Expedientes) JsfUtils.getSessionAttribute(EXPEDIENTEFORMULARIO);
		
		if (expedienteFormulario.getId() != null) {
			expedientes = expedientesService.obtener(expedienteFormulario.getId());
		} else {
			expedientes = new Expedientes();
		}	
		if(expedientes.getId()!=null) {
			cabeceraDialogBuscadorPersonaRepre=getMessage(BUSCADORREPRESENTANTE)+" del expediente "+ expedientes.getNumExpediente();
		}else {
			cabeceraDialogBuscadorPersonaRepre=getMessage(BUSCADORREPRESENTANTE);
		}
		
		PrimeFaces.current().executeScript("PF('dialogPersonaRepre').show();");
	}
	
	private void anyadePersonaPorDefectoSegunCfgTipoExpediente () {
		CfgTipoExpediente cfgTipoExpediente = cfgTipoExpedienteService.obtenerCfgTipoExpedienteByCodigoTipoExp(codigoTipoExpediente);
		if(expedientes.getId() == null && cfgTipoExpediente.getPersonaPorDefecto() != null && cfgTipoExpediente.getValorMotivoRelacionPersona() != null) {
			PersonasExpedientes personasExpedientesNew = new PersonasExpedientes();
			Personas personasAux = personasService.obtener(cfgTipoExpediente.getPersonaPorDefecto().getId());
			/** Con los dos get siguientes se fuerza la carga de la persona para el xhtml, no solo por la referencia del metodo obtener desde el Id*/
			personasAux.getNombreAp();
			personasAux.getNombreRazonsocial();
			personasExpedientesNew.setPersonas(personasAux);
			personasExpedientesNew.setInteresado(false);
			personasExpedientesNew.setPrincipal(true);
			personasExpedientesNew.setValoresRelacionExpPer(cfgTipoExpediente.getValorMotivoRelacionPersona());			
			listaPersonasExpedientes.add(personasExpedientesNew);		
			
			JsfUtils.setSessionAttribute(EXPEDIENTESDP, listaPersonasExpedientes);
			PrimeFaces.current().ajax().update(TABLAPERSONAEXPEDIENTENULO);
		}		
	}

}
