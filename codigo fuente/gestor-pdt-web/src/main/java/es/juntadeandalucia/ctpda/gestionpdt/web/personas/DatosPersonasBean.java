package es.juntadeandalucia.ctpda.gestionpdt.web.personas;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;

import org.primefaces.PrimeFaces;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.ExpedientesPersonasExpedientesMaestra;
import es.juntadeandalucia.ctpda.gestionpdt.model.Localidades;
import es.juntadeandalucia.ctpda.gestionpdt.model.Paises;
import es.juntadeandalucia.ctpda.gestionpdt.model.Personas;
import es.juntadeandalucia.ctpda.gestionpdt.model.PersonasExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.Provincias;
import es.juntadeandalucia.ctpda.gestionpdt.model.QPersonas;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.service.ExpedientesPersonasExpedientesMaestraService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.LocalidadesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.PaisesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.PersonasExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.PersonasService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ProvinciasService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ValoresDominioService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.util.NIF;
import es.juntadeandalucia.ctpda.gestionpdt.util.StringUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.Constantes;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.JsfUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.LazyDataModelByQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.MyFilterMeta;
import es.juntadeandalucia.ctpda.gestionpdt.web.menu.NavegacionBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.menu.NavegacionBean.ListadoNavegaciones;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.BaseBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.ContextoVolver;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.VolverBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@ViewScoped
public class DatosPersonasBean extends BaseBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private static final String MENSAJEPERSONA= "Persona ";
	private static final String MENSAJESFORMULARIO = "messagesFormulario";
	private static final String GUARDADOCORRECTAMENTE = "guardada.correctamente";
	private static final String ACTUALIZADOCORRECTAMENTE = "actualizada.correctamente";
	private static final String MENSAJEERROR= "error";
	private static final String ELCAMPO = "el.campo";
	private static final String IDENTIFICADOR = "identificador";
	private static final String EDITABLE = "editable";
	private static final String IDPERSONATXT = "idPersona";
	public static final String VOLVERPERSONAS = "_volverPersonas_";
	public static final String NOESNUEVAPERSONA = "noEsNuevaPersona";
	public static final String NUEVAPERSONAREPRE = "nuevaPersonaRepre";
	private static final String PERSONATXT = "persona";
	private static final String IDEXP = "idExp";


	@Getter
	private LazyDataModelByQueryService<ExpedientesPersonasExpedientesMaestra> lazyModel;
	@Getter
	private SortMeta defaultOrden;
	
	@Getter
	@Setter
	@Autowired
	private PersonasService personasService;
	@Getter
	@Setter
	@Autowired
	private ValoresDominioService valoresDominioService;	
	@Getter
	@Setter
	@Autowired
	private PaisesService paisesService;
	@Getter
	@Setter
	@Autowired
	private ProvinciasService provinciasService;
	@Getter
	@Setter
	@Autowired
	private LocalidadesService localidadesService;
	
	//----------------------
	
	@Getter
	@Setter
	private Personas persona;
	
	@Getter
	@Setter	
	private Long idTipoPersonaSeleccionado;
	@Getter
	@Setter	
	private Long idSexoSeleccionado;
	@Getter
	@Setter	
	private Long idViaComunicacionSeleccionada;
	
	@Getter
	@Setter
	private List<Paises> listaPaises;

	@Getter
	@Setter	
	private Long idPaisSeleccionado;
	
	@Getter
	@Setter
	private List<Provincias> listaProvincias;
	@Getter
	@Setter	
	private Long idProvinciaSeleccionada;

	@Getter
	@Setter
	private List<Localidades> listaLocalidades;
	@Getter
	@Setter	
	private Long idLocalidadSeleccionada;
		
	@Getter
	@Setter
	private List<ValoresDominio> listaValoresDominioTipoPersona;
	@Getter
	@Setter
	private List<ValoresDominio> listaValoresDominioSexo;
	@Getter
	@Setter
	private List<ValoresDominio> listaValoresDominioViaComunicacion;	
	@Getter
	@Setter
	private List<ValoresDominio> listaValoresDominioTipoIdentificador;	
	@Getter
	@Setter	
	private Long idTipoIdentificadorSeleccionado;	
	
	@Autowired	
	private ContextoPersonasBean contextoPersonasBean;	

	@Autowired
	private VolverBean volverBean;

	@Getter
	@Setter
	private List<String> listaCodigoPermisos;
		
	@Getter	@Setter
	private Boolean permisoSavePers;
	
	@Autowired
	private ExpedientesPersonasExpedientesMaestraService expedientesPersonasExpedientesMaestraService;
	
	@Getter	
	@Setter
	private Boolean permisoConsPerExp;
	
	@Getter @Setter
	private String errorIdentificadorNoValido;
	
	@Getter	@Setter
	private Boolean permisoEditPers;
	
	@Getter
	@Setter
	private PersonasExpedientes personasExpedientes;
	
	@Autowired
	private PersonasExpedientesService personasExpedientesService;
	
	@Getter @Setter
	private String tituloPestPersonasRepresentantes;
	
	@Getter @Setter

	private Long idPersona;
	@Getter @Setter
	Boolean noEsNuevaPersonaJuridica;
	@Getter @Setter
	Long idPersonaExp;
	@Getter @Setter
	Long idPersonaDpd;
	@Getter @Setter
	Long idPersonaRepre;

	@Autowired
	private ExpedientesService expedientesService;
	
	@Autowired
	private NavegacionBean navegacionBean;
	
	@Getter @Setter
	private Integer numerosSaltos;
	
	/**
	* Initialize default attributes.
	 * @throws BaseFrontException 
	*/
	@PostConstruct
	@Override
	public void init() {			
		super.init();
		
		numerosSaltos = (Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS);

		/**
		 * CONSULTA DE PERMISOS DEL USUARIO
		 * */
		listaCodigoPermisos = (List<String>) JsfUtils.getSessionAttribute(Constantes.LISTACODIGOPERMISOS);
	
		permisoSavePers = listaCodigoPermisos.contains(Constantes.PERMISO_SAVE_PERS);
		permisoConsPerExp = listaCodigoPermisos.contains(Constantes.PERMISO_CONS_PEREXP);
		permisoEditPers = listaCodigoPermisos.contains(Constantes.PERMISO_EDIT_PERS);
		
		Sort sortListaPaises = Sort.by(new Sort.Order(Direction.ASC, "descripcion"));
		listaPaises = (List<Paises>) paisesService.findAllRepository(sortListaPaises);
		listaValoresDominioTipoPersona = valoresDominioService.findValoresDominioTipoPersonaFisicaYJuridica();
		listaValoresDominioSexo= valoresDominioService.findValoresSexo();
		listaValoresDominioViaComunicacion = valoresDominioService.findValoresViaComunicacion();
		listaValoresDominioTipoIdentificador = valoresDominioService.findValoresTipoIdentificador();
		
		cargarPersona();
		
		ContextoVolver cp = volverBean.recogerContexto(VOLVERPERSONAS);
		if(cp != null) {
			idPersona = (Long) cp.get(IDPERSONATXT);
			idPersonaExp= (Long) cp.get("idPersonaExp");
			idPersonaDpd=(Long) cp.get("idPersonaDpd");
			idPersonaRepre=(Long) cp.get("idPersonaRepre");
			setFormEditable((boolean) cp.get(EDITABLE));
		} else {
			idPersonaExp = (Long) JsfUtils.getFlashAttribute("idPersonaExp");
			idPersonaDpd= (Long) JsfUtils.getFlashAttribute("idPersonaDpd");
			idPersonaRepre= (Long) JsfUtils.getFlashAttribute("idPersonaRepre");
			JsfUtils.setSessionAttribute(EDITABLE, JsfUtils.getSessionAttribute(EDITABLE));
			if(JsfUtils.getSessionAttribute(EDITABLE) != null) {
				this.setFormEditable((boolean) JsfUtils.getSessionAttribute(EDITABLE));	
			}else {
				this.setFormEditable((boolean)JsfUtils.getFlashAttribute(EDITABLE));
			}
		}
		persona = Optional.ofNullable(idPersona)
					.map(s -> personasService.obtener(s))
					.orElse( personasService.nuevaPersona());
		
		if(idPersonaExp!=null) {
			personasExpedientes= personasExpedientesService.obtener(idPersonaExp);
			persona= personasExpedientes.getPersonas();
		}
		if(idPersonaDpd!=null) {
			persona=personasService.obtener(idPersonaDpd);
		}
		if(idPersonaRepre!=null) {
			persona=personasService.obtener(idPersonaRepre);
		}
		if(persona.getId() != null) {
			this.comprobacionesAtributosPersonas();			
		} else {
			listaProvincias = Collections.emptyList();
			listaLocalidades = Collections.emptyList();
			idProvinciaSeleccionada = null;
			idLocalidadSeleccionada = null;
			Optional<Paises> op = listaPaises.stream()
											.filter(p -> p.getCodigo().equals("ES"))
											.findFirst();
			idPaisSeleccionado = op.isPresent()? op.get().getId() : null;
			idViaComunicacionSeleccionada = null;
			try {
				idTipoIdentificadorSeleccionado = valoresDominioService.getTipoIdentificadorNIF(listaValoresDominioTipoIdentificador).getId();
			} catch (BaseException e) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MENSAJEERROR));
				PrimeFaces.current().dialog().showMessageDynamic(message);
				log.error(e.getMessage());
			}
		}
		
		//Comparto el objeto
		contextoPersonasBean.setPersonas(persona);
				
		this.inicializaLazyModelExpPerExp();
	}
	
	private void inicializaLazyModelExpPerExp() {
		lazyModel= new LazyDataModelByQueryService<>(ExpedientesPersonasExpedientesMaestra.class,expedientesPersonasExpedientesMaestraService);
		lazyModel.setPreproceso((a,b,c,filters)->{
			/** añadamos siempre un orden por id en los casos de vistas */
			if (c!=null) {
				final SortMeta ordenacionById = SortMeta.builder().field("id").order(SortOrder.ASCENDING).priority(c.size()+1).build();
				c.put("id", ordenacionById);
			}
			
			if (persona.getId() != null){
				filters.put("persona.id", new MyFilterMeta(persona.getId()));				
			}			
		});
		
		defaultOrden = SortMeta.builder().field("persPrincipal").order(SortOrder.ASCENDING).priority(1).build();
	}
	
	private void cargarPersona() {
		ContextoVolver cv = volverBean.recogerContexto();
		idPersona = (Long) JsfUtils.getFlashAttribute(IDPERSONATXT);
		
		if(cv != null) {			
			idPersona = (Long) cv.get(IDPERSONATXT);
			persona = (Personas) cv.get(PERSONATXT);
			
			//recuperamos la id de la nueva persona creada con anterioridad
			if(cv.get(NOESNUEVAPERSONA)!=null && cv.get(NOESNUEVAPERSONA).equals(true)) {
				String varContexto=(String) cv.get("identificador_persona");

				contextoPersonasBean.setCifNifRepresentante(varContexto);
				
				PrimeFaces.current().executeScript("PF('dialogBuscarRepresentantes').show()");
				volverBean.limpiarContexto();
				
				//en este else if comprobamos si vamos a crear una nueva persona y ponemos una variable dentro del contexto
				//para indicar que ya no es nueva persona y asi coge la id de esa persona creada para el volver
			}else if(cv.get(NUEVAPERSONAREPRE)!=null && cv.get(NUEVAPERSONAREPRE).equals(true)) {
				idPersona= null;			
				cv.put(NOESNUEVAPERSONA, true);
				
			}
			
			if(null != cv.get("personaRepreJuridica") && cv.get("personaRepreJuridica").equals(true) ){
				//Mostrar pestaña personas si vengo de una vuelta atrás
				PrimeFaces.current().executeScript("$(\"[id$='tituloTabPersonasRepresentantes']\").parent().click();");	
			}
		} else {
			idPersona = (Long) JsfUtils.getFlashAttribute(IDPERSONATXT);
			cargarDatosPanelPersona();
		}
		
		if(idPersona == null) {
			// Se crea un nuevo expediente.
			persona = new Personas();
		} else {
			persona = personasService.obtener(idPersona);
		}
		
		
	}
	
	private void cargarDatosPanelPersona() {
		if(idPersona != null) {
			// Consulta para obtener de la BBDD la persona
			persona = personasService.obtener(idPersona);
		}
	}

	
	
	
	private void comprobacionesAtributosPersonas() {
		if(null != persona.getPais()) {
			idPaisSeleccionado=persona.getPais().getId();
		}
		
		cargaCPostal();
			
		if(null != persona.getProvincia()) {
			idProvinciaSeleccionada=persona.getProvincia().getId();
			recargaLocalidades();
		}
		if(null != persona.getLocalidad()) {
			idLocalidadSeleccionada = persona.getLocalidad().getId();
		}
		
		if(null!= persona.getValorTipoPersona()) {
			idTipoPersonaSeleccionado = persona.getValorTipoPersona().getId();
		}
		if(null!= persona.getValorSexo()) {
			idSexoSeleccionado = persona.getValorSexo().getId();
		}
		if(null!= persona.getValorTipoIdentificador()) {
			idTipoIdentificadorSeleccionado = persona.getValorTipoIdentificador().getId();
		}
		if(null != persona.getValorViaComunicacion()) {
			idViaComunicacionSeleccionada = persona.getValorViaComunicacion().getId();
		}
	}

	public void onChangeTipoPersona(AjaxBehaviorEvent ev) {
		final Long idTipo = (Long)((UIInput)ev.getSource()).getValue();
		this.persona.setValorTipoPersona(valoresDominioService.obtener(idTipo));
		
	}	
	
	public void onChangeTipoIdentificador() {
		if(null == idTipoIdentificadorSeleccionado) {
			this.persona.setNifCif(null);
		}
	}
	
	public void onChangeCodigoPostal() {
		cargaCPostal();
		//Dejo esto fuera de cargaCPostal
		//Si solo tengo una provincia (lo más probable) me quedo con ella
		if (1==listaProvincias.size()) {
			idProvinciaSeleccionada = listaProvincias.get(0).getId();
			recargaLocalidades();
		}
	}
	
	private void cargaCPostal() {
		if(persona.getCodigoPostal() != null && !persona.getCodigoPostal().isBlank()) {
			listaProvincias = (List<Provincias>) provinciasService.findAllByCPostal(persona.getCodigoPostal());
		}else {
			listaProvincias = Collections.emptyList();
		}
		listaLocalidades = Collections.emptyList();
		idProvinciaSeleccionada = null;
		idLocalidadSeleccionada = null;
	}
	
	public void onChangeProvincia() {
		recargaLocalidades();
	}
	
	public void recargaLocalidades() {
		listaLocalidades = (List<Localidades>) localidadesService.findAllByIdProvincia(idProvinciaSeleccionada);
		idLocalidadSeleccionada = null;
	}
	
	public void calcularIniciales() {
		this.persona.setIniciales(this.personasService.calcularIniciales(this.persona));
	}

	//------------------------------------------------------------------
	
	public boolean validacionesGuardar () throws BaseException {
		boolean validoGuardar = true;
		boolean obligatoriosOk = true;
		boolean identificadorValido = true;
		String mensajeFinal = "";
		String errorObligatorios = "";
		errorIdentificadorNoValido = "";
		String errorIdentificadorRepetido = "";
		String errorIdentificadorRepetidoInactivo = "";
		String errorEmailNoValido = "";

		if (null == persona.getValorTipoPersona() || persona.getValorTipoPersona() == null) {
			// no debe ocurrir
			validoGuardar = false;
		} else {
			obligatoriosOk = validacionesObligatoriosTipoPersona(obligatoriosOk);
			
			if(!obligatoriosOk) {
				errorObligatorios = getMessage("campos.obligatorios");
				validoGuardar = false;
			}
			
			/** identificador no valido **/
			validoGuardar = errorIdentificadorNoValido(obligatoriosOk,validoGuardar);
			identificadorValido = validoGuardar;
			
			if(obligatoriosOk && identificadorValido && existeNIFPersona(persona)) {
				validoGuardar = false;
				errorIdentificadorRepetido = "El identificador (" + persona.getNifCif() + ") " + getMessage("existe.sistema");
			}else
			if(obligatoriosOk && identificadorValido && existeNIFPersonaInactiva(persona)) {
				validoGuardar = false;
				errorIdentificadorRepetidoInactivo = "El identificador (" + persona.getNifCif() + ") " + getMessage("existe.sistema.persona.inactiva");
			}
			
			if(obligatoriosOk && persona.getEmail() != null && !persona.getEmail().isBlank() && !StringUtils.esEmailValido(persona.getEmail())) {
				validoGuardar = false;
				errorEmailNoValido = getMessages(ELCAMPO, "email", "no.valido");
			}
		}
		
		mensajeFinalGuardar(mensajeFinal,errorObligatorios,errorIdentificadorNoValido,errorIdentificadorRepetido,errorIdentificadorRepetidoInactivo,errorEmailNoValido);
	
		return validoGuardar;
	}
	
	private boolean errorIdentificadorNoValido (boolean obligatoriosOk, boolean validoGuardar) throws BaseException {
		if(obligatoriosOk && null != idTipoIdentificadorSeleccionado && !StringUtils.isBlank(persona.getNifCif())) {
			final String tipoIden = valoresDominioService.getCodigoTipoIdentificador(listaValoresDominioTipoIdentificador, idTipoIdentificadorSeleccionado);
			
			//Tengo asegurado que no es null
			persona.setNifCif(persona.getNifCif().toUpperCase());
			
			if (tipoIden != null && !NIF.esIdentificadorValido(tipoIden, persona.getNifCif())) {
				validoGuardar = false;
				errorIdentificadorNoValido = getMessages(ELCAMPO, IDENTIFICADOR, "no.valido");
			}
		}
		return validoGuardar;
	}
	
	private boolean validacionesObligatoriosTipoPersona (boolean obligatoriosOk) {
		if(persona.getEsPF()) {
			if (StringUtils.isBlank(persona.getNombreRazonsocial())
					|| StringUtils.isBlank(persona.getPrimerApellido()) 
					|| idTipoIdentificadorSeleccionado == null
					|| StringUtils.isBlank(persona.getNifCif())
					|| idSexoSeleccionado == null ) {
				obligatoriosOk = false;
			}
		} else { //es PJ
			if(StringUtils.isBlank(persona.getNombreRazonsocial()) 
					|| idTipoIdentificadorSeleccionado == null
					|| StringUtils.isBlank(persona.getNifCif())){
				obligatoriosOk = false;
			}
		}
		return obligatoriosOk;
	}
	
	private void mensajeFinalGuardar (String mensajeFinal,String errorObligatorios,String errorIdentificadorNoValido,
										String errorIdentificadorRepetido,String errorIdentificadorRepetidoInactivo,String errorEmailNoValido) {
		if(!errorObligatorios.isBlank()) {
			mensajeFinal = errorObligatorios;
		} 
		if(!errorEmailNoValido.isBlank()){
			mensajeFinal += errorEmailNoValido;
		}
		if(!errorIdentificadorNoValido.isBlank()) {
			mensajeFinal += "\n"+errorIdentificadorNoValido;
		}else
		if(!errorIdentificadorRepetido.isBlank()) {
			mensajeFinal += "\n"+errorIdentificadorRepetido;
		}else
		if(!errorIdentificadorRepetidoInactivo.isBlank()) {
			mensajeFinal += "\n"+errorIdentificadorRepetidoInactivo;
		}			
			
		if(!mensajeFinal.isBlank()) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajeFinal);
			PrimeFaces.current().dialog().showMessageDynamic(message);
		}
	}
	
	private boolean existeNIFPersona(Personas p) {
		boolean returnPersonasRepetidas = false;
		BooleanBuilder bb= new BooleanBuilder();
		if(p.getId() != null) {
			bb.and(QPersonas.personas.id.ne(p.getId()));
		}
		bb.and(QPersonas.personas.nifCif.eq(p.getNifCif()));
		bb.and(QPersonas.personas.activa.eq(true));	//Activas	
		List<Personas> personasRepetidas =  (List<Personas>) personasService.findAllRepository(bb,Sort.unsorted());
		if(!personasRepetidas.isEmpty()) {
			returnPersonasRepetidas = true;
		}
		
		return returnPersonasRepetidas;
	}
	
	private boolean existeNIFPersonaInactiva(Personas p) {
		boolean returnPersonasRepetidas = false;
		BooleanBuilder bb= new BooleanBuilder();
		if(p.getId() != null) {
			bb.and(QPersonas.personas.id.ne(p.getId()));
		}
		bb.and(QPersonas.personas.nifCif.eq(p.getNifCif()));
		bb.and(QPersonas.personas.activa.eq(false)); //Inactivas		
		List<Personas> personasRepetidas = (List<Personas>) personasService.findAllRepository(bb,Sort.unsorted());
		if(!personasRepetidas.isEmpty()) {
			returnPersonasRepetidas = true;
		}
		
		return returnPersonasRepetidas;
	}
		
	public void savePersonas() {
		try {
			if (validacionesGuardar()) {
				String msgAccion = persona.getId() == null ? GUARDADOCORRECTAMENTE : ACTUALIZADOCORRECTAMENTE;
	
				listadosDireccion();
				if (idSexoSeleccionado!=null) {
					persona.setValorSexo(valoresDominioService.obtener(idSexoSeleccionado));
				} else {
					persona.setValorSexo(null);
				}
				if(idViaComunicacionSeleccionada!=null) {
					persona.setValorViaComunicacion(valoresDominioService.obtener(idViaComunicacionSeleccionada));
				}else {
					persona.setValorViaComunicacion(null);
				}
				if(idTipoIdentificadorSeleccionado!=null) {
					persona.setValorTipoIdentificador(valoresDominioService.obtener(idTipoIdentificadorSeleccionado));
				}else {
					persona.setValorTipoIdentificador(null);
				}
				
				this.personasService.guardar(this.persona);
				FacesMessage msgExito = new FacesMessage(FacesMessage.SEVERITY_INFO, "", MENSAJEPERSONA + " "+ persona.getNombreAp()+" "+ getMessage(msgAccion));
								
				contextoVolver(msgExito);
				
				navegacionBean.setTxtMigaPan(Constantes.EDICION_PERSONA+persona.getNifCif());	
				PrimeFaces.current().ajax().update("textoMigaPan");
			}			
		} catch (final BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
	}
	
	private void listadosDireccion () {
		if (idPaisSeleccionado != null) {
			persona.setPais(paisesService.obtener(idPaisSeleccionado));
		} else {
			persona.setPais(null);
		}
		if (idProvinciaSeleccionada != null) {
			persona.setProvincia(provinciasService.obtener(idProvinciaSeleccionada));
		} else {
			persona.setProvincia(null);
		}
		if (idLocalidadSeleccionada != null) {
			persona.setLocalidad(localidadesService.obtener(idLocalidadSeleccionada));
		} else {
			persona.setLocalidad(null);
		}
	}
	
	private void contextoVolver (FacesMessage msgExito) {
		ContextoVolver cv = volverBean.getContexto();
		if(cv != null) {
			//Tal como guardamos volvemos al expediente
			cv.put("identificador_persona", persona.getNifCif());
			cv.put("mensaje_ok", msgExito);
			volverBean.pasarContexto(cv);
			PrimeFaces.current().executeScript("$(\"[id$='volver']\").click()");
		} else {
			FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, msgExito);
			PrimeFaces.current().ajax().update("formFormulario");					
		}
	}
	
	public String onConsultar(Long idExp) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, numerosSaltos+1);
		JsfUtils.setFlashAttribute(EDITABLE, false);
		JsfUtils.setFlashAttribute(IDEXP, idExp);
		JsfUtils.setSessionAttribute(Constantes.VUELTA_PERSONAS, true);
		JsfUtils.setSessionAttribute(Constantes.IDPERSONASELECCIONADA, persona.getId());
		ContextoVolver v = volverBean.crearContexto(ListadoNavegaciones.FORM_PERSONAS.getRegla(), VOLVERPERSONAS);
		v.put(IDPERSONATXT, persona.getId());
		v.put(EDITABLE, this.getFormEditable());
		Expedientes expedientes = expedientesService.obtener(idExp);
		navegacionBean.setTxtMigaPan(Constantes.CONSULTA_EXPEDIENTE+expedientes.getMigaDePan()+ navegacionBean.expedientesRelacionadosMotivoPsan(expedientes.getId()));
		return ListadoNavegaciones.FORM_EXPEDIENTES.getRegla();
	}
	
	public String onEditarByForm(Long idPersona) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, numerosSaltos+1);
		JsfUtils.setFlashAttribute(IDPERSONATXT, idPersona);
		JsfUtils.setFlashAttribute(EDITABLE, true);
		Personas personas = personasService.obtener(idPersona);
		navegacionBean.setTxtMigaPan(Constantes.EDICION_PERSONA+personas.getNifCif());
		return ListadoNavegaciones.FORM_PERSONAS.getRegla();
	}
	
	public String altaPersonaRepre() {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, numerosSaltos+1);
		JsfUtils.setSessionAttribute(EDITABLE, true);
		JsfUtils.setFlashAttribute(EDITABLE, true);
		ContextoVolver contexto = volverBean.crearContexto(ListadoNavegaciones.FORM_PERSONAS.getRegla());		
		contexto.put(NUEVAPERSONAREPRE, true);
		contexto.put(IDPERSONATXT, idPersona);
		contexto.put(PERSONATXT, persona);
		navegacionBean.setTxtMigaPan(Constantes.ALTA_PERSONA);
		return ListadoNavegaciones.FORM_PERSONAS.getRegla();
	}
	
	public String ejecutarVolver() {
		ContextoVolver contexto = volverBean.getContexto();
		if(contexto != null && contexto.getVista().contains("formFormularioExpedientes")) {
			if(contexto.get(IDEXP) != null) {
				Expedientes expediente = expedientesService.obtener((Long) contexto.get(IDEXP));
				volverBean.migaPanVolver(Constantes.EDICION_EXPEDIENTE+expediente.getMigaDePan()+navegacionBean.expedientesRelacionadosMotivoPsan(expediente.getId()));	
			}else {
				volverBean.migaPanVolver(Constantes.ALTA_EXPEDIENTE);
			}	
		}else if(contexto != null && contexto.getVista().contains("/personas/formulario")){
			Personas personasVolver = (Personas) contexto.get(PERSONATXT);
			volverBean.migaPanVolver(Constantes.EDICION_PERSONA+personasVolver.getNifCif());	
		} else {
			volverBean.migaPanVolver(Constantes.LISTADO_PERSONAS);
		}
		
		return volverBean.onVolver(ListadoNavegaciones.LISTADO_PERSONAS.getRegla());
	}



}
