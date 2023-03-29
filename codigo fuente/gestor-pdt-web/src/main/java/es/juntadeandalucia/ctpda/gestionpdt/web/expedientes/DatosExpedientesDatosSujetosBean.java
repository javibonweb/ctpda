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
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.CfgTipoExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.Personas;
import es.juntadeandalucia.ctpda.gestionpdt.model.SujetosObligados;
import es.juntadeandalucia.ctpda.gestionpdt.model.SujetosObligadosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.TipoAgrupacion;
import es.juntadeandalucia.ctpda.gestionpdt.model.Usuario;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.service.CfgTipoExpedienteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.PersonasService;
import es.juntadeandalucia.ctpda.gestionpdt.service.SujetosObligadosExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.SujetosObligadosService;
import es.juntadeandalucia.ctpda.gestionpdt.service.TipoAgrupacionService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ValoresDominioService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.util.FechaUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.Constantes;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.JsfUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.LazyDataModelByQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.MyFilterMeta;
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
public class DatosExpedientesDatosSujetosBean extends BaseBean implements Serializable {
	
	private static final String MENSAJESFORMULARIO = "messagesFormulario";
	private static final String MENSAJESASIGNARSUJETOS = "messagesFormularioAsignarSujetos";
	private static final String GUARDADOCORRECTAMENTE = "anyadido.correctamente";
	private static final String MENSAJESUJETO = "Sujeto ";
	private static final String MENSAJEPERSONA = "Persona ";
	private static final String ACTUALIZADOCORRECTAMENTE = "actualizado.correctamente";
	private static final String MENSAJEERROR = "error";
	private static final String MENSAJESUJETOPRINCIPAL = "sujeto.debe.ser.principal";
	private static final String MENSAJESPESTANYASUJETO = "messagesFormularioListadoSujetosObligados";
	private static final String TABLASUJETOEXPEDIENTENULO = "formFormularioExpedientes:tabViewPestanasExpediente:tablaSujetosExpedientesExpNulo";
	private static final String TABLASUJETOEXPEDIENTENONULO = "formFormularioExpedientes:tabViewPestanasExpediente:tablaSujetosExpedientes";
	private static final String GUARDARPERSISTENCIA = "cambios.efectivos.boton.guardar";
	private static final String EXPEDIENTESDS = "expedientesDS";
	private static final String BLOQUEDPD = "formFormularioExpedientes:tabViewPestanasExpediente:bloqueDelegProtDatSujOblExp";
	private static final String PARAELEXPEDIENTE = " para el expediente ";
	private static final String BUSCADORPERSONA = "buscador.persona";
	private static final String INCORPORACIONAUTOMATICASUJOBLIG = "incorporacion.autom.suj.oblig";
	private static final String EXISTENTEULTIMEXP = "suj.oblig.existe.ultim.exp";
	
	private static final long serialVersionUID = 1L;
	
	@Getter
	private LazyDataModelByQueryService<SujetosObligadosExpedientes> lazyModelSujetosExpedientes;
	@Getter
	private LazyDataModelByQueryService<Personas> lazyModelSujetos;
	@Getter
	private LazyDataModelByQueryService<Personas> lazyModelPersonas;
	
	@Autowired
	private SujetosObligadosExpedientesService sujetosObligadosExpedientesService;
	@Autowired
	private SujetosObligadosService sujetosObligadosService;
	@Autowired
	private ExpedientesService expedientesService;
	@Autowired
	private PersonasService personasService;
	@Autowired
	private ValoresDominioService valoresDominioService;
	@Autowired
	private TipoAgrupacionService tipoAgrupacionService;
	
	@Getter
	@Setter
	private String descripcionFiltro;
	@Getter
	@Setter
	private Long sujetosObligadosSupIdFiltro;
	@Getter
	@Setter
	private Long tipoAgrupacionIdFiltro;
	
	@Getter
	@Setter
	private List<ValoresDominio> listaValoresMotivosRelacionSujetosExpediente;
	
	@Getter
	@Setter
	private SujetosObligadosExpedientes sujetosObligadosExpedientes;
	@Getter
	@Setter
	private SujetosObligados sujetosObligados;
	@Getter
	@Setter
	private String sujetosObligadosConDescripcion;
	
	@Getter
	@Setter
	private String sujObligNombreRazonsocial;
	
	@Getter
	@Setter
	private String sujObligApellidos;
	
	@Getter
	@Setter
	private String sujObligTelefono;
	
	
	@Getter
	@Setter
	private String sujObligEmail;
	
	@Getter
	@Setter
	private Boolean sujObligDpd;
	@Getter
	@Setter
	private Expedientes expedientes;
	@Getter
	@Setter
	private List<SujetosObligadosExpedientes> listaSujetosObligadosExpedientes;
	@Getter
	@Setter
	private String cabeceraDialogoMotivoRelacion;
	@Getter
	@Setter
	boolean soloLectura;
	@Getter
	@Setter
	private Long selectedNuevoMotivoRelacionId;
	@Getter
	@Setter
	private SujetosObligados nuevoSujeto;
	@Getter
	@Setter
	private SujetosObligadosExpedientes nuevoSujetoExp;
	@Getter
	@Setter
	private SujetosObligadosExpedientes selectedExpedientesSujetos;
	@Getter
	@Setter
	private Boolean sujetoPrincipal;
	
	@Getter
	@Setter
	private boolean plegado;
	@Getter
	@Setter
	private SujetosObligadosExpedientes sujetosObligadosExpedientesSeleccionado;
	@Getter
	@Setter
	private Personas personas;
	
	@Getter
	@Setter
	private String nombreRazonSocialFiltro;
	
	@Getter
	@Setter
	private String cifnifFiltro;
	
	@Getter
	@Setter
	private boolean mostrarDpd = true;
	
	@Getter
	@Setter
	private List<SujetosObligados> listaSujetosObligadosSup;
	@Getter
	@Setter
	private List<TipoAgrupacion> listaTipoAgrupaciones;
	
	@Getter
	private List<SortMeta> defaultOrdenList;

	@Autowired
	private VolverBean volverBean;
	
	@Getter
	@Setter
	private List<String> listaCodigoPermisos;

	@Getter	@Setter
	private Boolean permisoBusExpSujObl;
	
	@Getter	@Setter
	private Boolean permisoDelExpSujObl;
	
	@Getter	@Setter
	private Boolean permisoEditExpSujObl;
	
	@Getter	@Setter
	private Boolean permisoBusExpSujOblDpd;
	
	@Getter	@Setter
	private Boolean permisoDelExpSujOblDpd;
	
	@Getter @Setter
	private List<Long> sujetosObligadosActivosConPadre;
	
	@Autowired
	private DatosExpedientesBean datosExpedientesBean;
	
	@Getter @Setter
	private String errorObligatorios;
	
	@Autowired
	private UtilsComun utilsComun;
	
	@Getter
	@Setter
	private String cabeceraBuscadorSujeto;
	
	@Getter
	@Setter
	private String cabeceraBuscadorSujetoDpd;
	@Getter
	@Setter
	private String sujetosObligadosSupDescripcionFiltro;
	
	@Autowired
	private CfgTipoExpedienteService cfgTipoExpedienteService;


	@PostConstruct
	@Override
	public void init() {
		super.init();
		
		/**
		 * GESTION DE PERMISOS
		 * */
		listaCodigoPermisos = (List<String>) JsfUtils.getSessionAttribute(Constantes.LISTACODIGOPERMISOS);
		
		permisoBusExpSujObl = listaCodigoPermisos.contains(Constantes.PERMISO_BUS_EXPSUJOBL);
		
		permisoDelExpSujObl = listaCodigoPermisos.contains(Constantes.PERMISO_DEL_EXPSUJOBL);
		
		permisoEditExpSujObl = listaCodigoPermisos.contains(Constantes.PERMISO_EDIT_EXPSUJOBL);
		
		permisoBusExpSujOblDpd = listaCodigoPermisos.contains(Constantes.PERMISO_BUS_EXPSUJOBLDPD);
		
		permisoDelExpSujOblDpd = listaCodigoPermisos.contains(Constantes.PERMISO_DEL_EXPSUJOBLDPD);
		
		cargarExpediente();
	
		inicializaLazyModelSujetosExpedientes(expedientes);
		inicializaLazyModelSujetos();
		inicializaLazyModelPersonas();

		defaultOrdenList = new ArrayList<>();
		defaultOrdenList.add(SortMeta.builder().field("nivelAnidamiento").order(SortOrder.ASCENDING).priority(1).build());
		defaultOrdenList.add(SortMeta.builder().field("ordenVisualizacion").order(SortOrder.ASCENDING).priority(2).build());
		defaultOrdenList.add(SortMeta.builder().field("descripcion").order(SortOrder.ASCENDING).priority(3).build());

		listaValoresMotivosRelacionSujetosExpediente = valoresDominioService.findValoresMotivosRelacionSujetosExpediente();
		sujetosObligadosExpedientes = new SujetosObligadosExpedientes();
		cabeceraDialogoMotivoRelacion = "";
		
		plegado = true;
		
		listaSujetosObligadosSup = sujetosObligadosService.findSujetosObligadosActivos();		
		listaTipoAgrupaciones =  tipoAgrupacionService.findTiposAgrupacionActivas();
		
		sujetosObligadosActivosConPadre = sujetosObligadosService.findSujetosObligadosActivosConPadre();
		
		if(expedientes.getId()!=null) {
			cabeceraBuscadorSujetoDpd=getMessage(BUSCADORPERSONA)+PARAELEXPEDIENTE+ expedientes.getNumExpediente();
		}else {
			cabeceraBuscadorSujetoDpd=getMessage(BUSCADORPERSONA);
		}
		
		anyadeSujetoPorDefectoSegunCfgTipoExpediente();
	}
	
	@SuppressWarnings("unchecked")
	private void cargarExpediente() {
		FacesUtils.setAttribute("editable", JsfUtils.getFlashAttribute("editable"));
		
		ContextoVolver cv = volverBean.getContexto(); //Sólo get, ya se ha recogido en DatosExpedientesBean
		
		if(cv != null) {	
			cifnifFiltro = (String) cv.get("identificador_persona");
			sujetosObligadosExpedientesSeleccionado = (SujetosObligadosExpedientes) cv.get("sujetosObligadosExpedientes"); 
			listaSujetosObligadosExpedientes = (List<SujetosObligadosExpedientes>) JsfUtils.getSessionAttribute(EXPEDIENTESDS);
		} else {
			listaSujetosObligadosExpedientes = new ArrayList<>();
			sujetosObligadosExpedientesSeleccionado = null;
			JsfUtils.setSessionAttribute(EXPEDIENTESDS, listaSujetosObligadosExpedientes);
		}

		Expedientes expedienteFormulario = (Expedientes) JsfUtils.getSessionAttribute("expedienteFormulario");
		if (expedienteFormulario.getId() != null) {
			expedientes = expedientesService.obtener(expedienteFormulario.getId());
			this.mostrarSeccionDpd();
		} else {
			expedientes = new Expedientes();
			if(expedienteFormulario.getValorTipoExpediente() != null) {
				expedientes.setValorTipoExpediente(valoresDominioService.obtener(expedienteFormulario.getValorTipoExpediente().getId()));
				this.mostrarSeccionDpd();
			}else {
				mostrarDpd = true;
			}			
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void inicializaLazyModelSujetosExpedientes(Expedientes expedienteFormulario) {
		lazyModelSujetosExpedientes = new LazyDataModelByQueryService<>(SujetosObligadosExpedientes.class, 
				sujetosObligadosExpedientesService);
		lazyModelSujetosExpedientes.setPreproceso((a, b, c, filters) -> {
			if (expedienteFormulario.getId() != null) {
				filters.put("expediente.id", new MyFilterMeta(expedienteFormulario.getId()));
			} else {
				filters.put("expediente.id", new MyFilterMeta(null));

			}
		});
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void inicializaLazyModelSujetos() {
		lazyModelSujetos = new LazyDataModelByQueryService(SujetosObligados.class, sujetosObligadosService);
		lazyModelSujetos.setPreproceso((a, b, c, filters) -> {
			if (descripcionFiltro != null && !descripcionFiltro.isEmpty()) {
				filters.put("descripcion", new MyFilterMeta(descripcionFiltro));
			}
			if (sujetosObligadosSupIdFiltro != null) {
				filters.put("sujetosObligadosPadre",
						new MyFilterMeta(sujetosObligadosService.obtener(sujetosObligadosSupIdFiltro)));
			}
			if (tipoAgrupacionIdFiltro != null) {
				filters.put("tipoAgrupacion",
						new MyFilterMeta(tipoAgrupacionService.obtener(tipoAgrupacionIdFiltro)));
			}

			filters.put("activa", new MyFilterMeta("true"));

		});
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void inicializaLazyModelPersonas() {
		lazyModelPersonas = new LazyDataModelByQueryService(Personas.class, personasService);
		lazyModelPersonas.setPreproceso((a, b, c, filters) -> {
			if (nombreRazonSocialFiltro != null && !nombreRazonSocialFiltro.isEmpty()) {
				filters.put("#nombreAp", new MyFilterMeta(nombreRazonSocialFiltro));
			}
			if (cifnifFiltro != null && !cifnifFiltro.isEmpty()) {
				filters.put("nifCif", new MyFilterMeta(cifnifFiltro));
			}

			filters.put("activa", new MyFilterMeta("true"));

		});
	}
	
	public boolean isSujetoObligadoPadre(SujetosObligados sujetoSeleccionado) {
		boolean isSujetoObligadoPadre = false;
		if(!sujetosObligadosActivosConPadre.isEmpty() && sujetosObligadosActivosConPadre.contains(sujetoSeleccionado.getId())) {
			isSujetoObligadoPadre = true;
		}
		return isSujetoObligadoPadre;
	}
	
	private void mostrarSeccionDpd() {
		
		if(this.expedientes.getValorTipoExpediente().getCodigo().equals(Constantes.RCE) || 
				this.expedientes.getValorTipoExpediente().getCodigo().equals(Constantes.RCO)) {
			this.mostrarDpd = true;
		}else {
			this.mostrarDpd = false;
		}
		// Esto es para que siempre se muestre la ventana de DPD.  No se quita la funcionalidad anterior por si hiciera falta volver a usarla.
		this.mostrarDpd = true;
	}

	public void limpiarFiltro() {
		descripcionFiltro = null;
		sujetosObligadosSupIdFiltro = null;
		sujetosObligadosSupDescripcionFiltro = "";
		tipoAgrupacionIdFiltro = null;
	}
	
	public void abrirAsignarMotRela(SujetosObligadosExpedientes sujetoExpedientesSeleccionado,
			SujetosObligados sujetoSeleccionado) {
		
		limpiarAsignarMotRelaSujeto();

		soloLectura = false;
		selectedNuevoMotivoRelacionId = null;
		if (expedientes.getId() == null && listaSujetosObligadosExpedientes.isEmpty()) {
			sujetoPrincipal = true;
		} else if (expedientes.getId() == null && !listaSujetosObligadosExpedientes.isEmpty()) {
			sujetoPrincipal = false;
		} else if (expedientes.getId() != null) {
			sujetoPrincipal = false;
		}

		
		if (sujetoExpedientesSeleccionado != null) {
			sujetosObligadosExpedientes = sujetoExpedientesSeleccionado;
			this.selectedNuevoMotivoRelacionId = sujetoExpedientesSeleccionado.getValoresRelacionExpSuj().getId();
			sujetosObligadosConDescripcion = sujetoExpedientesSeleccionado.getSujetosObligados().getDescripcion();
			this.sujObligNombreRazonsocial = sujetoExpedientesSeleccionado.getNombreRazonsocial();
			this.sujObligApellidos = sujetoExpedientesSeleccionado.getApellidos();
			
			this.sujObligTelefono = sujetoExpedientesSeleccionado.getTelefono();
			
			this.sujObligEmail = sujetoExpedientesSeleccionado.getEmail();
			this.sujObligDpd = sujetoExpedientesSeleccionado.getDpd();

			
			sujetoPrincipal = sujetoExpedientesSeleccionado.getPrincipal();
		}
		if (sujetoSeleccionado != null) {
			nuevoSujeto = sujetoSeleccionado;
			
			buscarDatosSujObligEnAsociacionSujOblExp(nuevoSujeto);
			sujetosObligadosConDescripcion = nuevoSujeto.getDescripcion();
		}
		
		Expedientes expedienteFormulario = (Expedientes) JsfUtils.getSessionAttribute("expedienteFormulario");
		CfgTipoExpediente cfgTipoExpediente = cfgTipoExpedienteService.obtenerCfgTipoExpedientePorValorTipoExpediente((expedientes.getId()!=null)?expedientes.getValorTipoExpediente().getId():expedienteFormulario.getValorTipoExpediente().getId());
		if(cfgTipoExpediente != null && cfgTipoExpediente.getValorMotivoRelacionSujeto() != null) {
			selectedNuevoMotivoRelacionId = cfgTipoExpediente.getValorMotivoRelacionSujeto().getId();
		}
		
		
		PrimeFaces.current().ajax()
				.update(TABLASUJETOEXPEDIENTENULO);
		PrimeFaces.current().ajax()
				.update(TABLASUJETOEXPEDIENTENONULO);
		PrimeFaces.current().ajax().update("dialogAsignarMotRelaSujetos");

		PrimeFaces.current().executeScript("PF('dialogAsignarMotRelaSujetos').show();");
	}
	
	private void buscarDatosSujObligEnAsociacionSujOblExp(SujetosObligados sujetoSeleccionado)
	{
		SujetosObligadosExpedientes sujObligExpAux = null;
		Expedientes expAux = null;
		/** CONSULTAR EL SUJETO OBLIGADO DEL EXPEDIENTE MAS RECIENTE SI ES QUE EXISTE PARA OFRECER ASI LOS DATOS
		 * DEL SUJETO OBLIGADO YA CUMPLIMENTADOS AL USUARIO. **/
		Expedientes expUltimoSujOblig = sujetosObligadosExpedientesService.obtenerExpMaxPorSujetoOblig(sujetoSeleccionado.getId());
		if(expUltimoSujOblig != null)
		{
			expAux = expUltimoSujOblig;
			sujObligExpAux = sujetosObligadosExpedientesService.obtenerSujetosObligadosExpedientePorSujOblig(expUltimoSujOblig.getId(), sujetoSeleccionado.getId());
		}
		
		if(sujObligExpAux!= null)
		{
			this.sujObligApellidos = sujObligExpAux.getApellidos();
			this.sujObligEmail = sujObligExpAux.getEmail();
			this.sujObligDpd = sujObligExpAux.getDpd();
			this.sujObligNombreRazonsocial = sujObligExpAux.getNombreRazonsocial();
			this.sujObligTelefono = sujObligExpAux.getTelefono();
			
			FacesContext.getCurrentInstance().addMessage(MENSAJESASIGNARSUJETOS, new FacesMessage(FacesMessage.SEVERITY_INFO, "", mensajesProperties.getString(INCORPORACIONAUTOMATICASUJOBLIG) + " " + sujetoSeleccionado.getDescripcion()
			+ " " + mensajesProperties.getString(EXISTENTEULTIMEXP) + " " + expAux.getNumExpediente()));
			
		}		
	}
	
	public void asignarSujetoObligadoSup(SujetosObligados sujetoSeleccionado) {
		sujetosObligadosSupIdFiltro = sujetoSeleccionado!=null?sujetoSeleccionado.getId():null;
		sujetosObligadosSupDescripcionFiltro = sujetoSeleccionado!=null?sujetoSeleccionado.getDescripcion():null;
		tipoAgrupacionIdFiltro = null;
		descripcionFiltro = "";
		
		PrimeFaces.current().ajax().update("formFormularioExpedientes:tabViewPestanasExpediente:tipoAgrupacionFiltro");
		PrimeFaces.current().ajax().update("formFormularioExpedientes:tabViewPestanasExpediente:descripcionFiltro");
		PrimeFaces.current().ajax().update("formFormularioExpedientes:tabViewPestanasExpediente:sujetosObligadosSuperiorFiltro");
		PrimeFaces.current().ajax().update("formFormularioExpedientes:tabViewPestanasExpediente:tablaSujetosFiltro");
	}

	public boolean validacionesGuardar() {
		boolean valido = true;
		String mensajeFinal = "";
		String errorSujetoPrincipal = "";
		errorObligatorios = "";

		valido = campoObligatorioRelleno(valido);

		// validacion check
		if (expedientes.getId() == null && listaSujetosObligadosExpedientes.isEmpty()
				&& Boolean.TRUE.equals(!sujetoPrincipal)) {
			valido = false;
			errorSujetoPrincipal = mensajesProperties.getString(MENSAJESUJETOPRINCIPAL);
		}

		if (expedientes.getId() == null && sujetosObligadosExpedientes != null
				&& sujetosObligadosExpedientes.getSujetosObligados() != null && selectedNuevoMotivoRelacionId != null
				&& sujetosObligadosExpedientes.getPrincipal().equals(true) && sujetoPrincipal.equals(false)) {
				valido = false;
				errorSujetoPrincipal = mensajesProperties.getString(MENSAJESUJETOPRINCIPAL);
		}
		
		imprimirMensajeError(mensajeFinal,errorObligatorios,errorSujetoPrincipal);

		return valido;

	}
	
	private boolean campoObligatorioRelleno (boolean valido) {
		if (this.selectedNuevoMotivoRelacionId == null) {
			valido = false;
			errorObligatorios = mensajesProperties.getString("campos.obligatorios");
		}
		
		return valido;
	}
	
	private void imprimirMensajeError (String mensajeFinal, String errorObligatorios, String errorSujetoPrincipal) {
		if(!errorObligatorios.isBlank()) {
			mensajeFinal = errorObligatorios;
		} 
		if(!errorSujetoPrincipal.isBlank()){
			mensajeFinal += "\n"+errorSujetoPrincipal;
		}
		if(!mensajeFinal.isBlank()) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajeFinal);
			PrimeFaces.current().dialog().showMessageDynamic(message);
		}
	}

	public void asignarMotRelaSujetoExpediente() {
		boolean puedoGuardar = validacionesGuardar();
		try {
			if (puedoGuardar) {
				ValoresDominio tipRela = valoresDominioService.obtener(this.selectedNuevoMotivoRelacionId);
				if (sujetosObligadosExpedientes.getId() == null) {
					if (expedientes.getId() == null) {
						FacesContext.getCurrentInstance().addMessage(MENSAJESPESTANYASUJETO, new FacesMessage(FacesMessage.SEVERITY_INFO, "", MENSAJESUJETO +sujetosObligadosConDescripcion +" "+ mensajesProperties.getString(GUARDADOCORRECTAMENTE) + mensajesProperties.getString(GUARDARPERSISTENCIA)));
					} else {
						FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, new FacesMessage(FacesMessage.SEVERITY_INFO, "", MENSAJESUJETO+sujetosObligadosConDescripcion+PARAELEXPEDIENTE+ expedientes.getNumExpediente()+" "+ mensajesProperties.getString(ACTUALIZADOCORRECTAMENTE)));
					}
				}

				if (expedientes.getId() == null) {
					asignarByExpedienteNull(tipRela);
				} else {
					asignarByExpedienteNotNull(tipRela);
				}

				limpiarAsignarMotRelaSujeto();
				
				

			}
		} catch (final BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
	}
	
	private void asignarByExpedienteNotNull (ValoresDominio tipRela) throws BaseException {
		SujetosObligadosExpedientes nuevoSujetoAux= new SujetosObligadosExpedientes();		
		if (nuevoSujetoAux.getSujetosObligados() != null
				&& selectedNuevoMotivoRelacionId != null) {
			nuevoSujetoAux= sujetosObligadosExpedientesService.obtener(sujetosObligadosExpedientes.getId());
			// opcion editar sujeto expediente
			List<SujetosObligadosExpedientes> listaSujetosConExpediente = sujetosObligadosExpedientesService
					.obtenerSujetosObligadosExpedientePrincipalYNoPrincipalPorExpediente(
							expedientes.getId());
			for (SujetosObligadosExpedientes sujExp : listaSujetosConExpediente) {
				if (sujExp.getId().equals(nuevoSujetoAux.getId())
						&& sujetoPrincipal.equals(true)) {
					nuevoSujetoAux.setPrincipal(true);

				} else if (!(sujExp.getId().equals(nuevoSujetoAux.getId()))
						&& sujetoPrincipal.equals(true)) {
					sujExp.setPrincipal(false);
					nuevoSujetoAux.setPrincipal(true);
				}
			}
			nuevoSujetoAux.setValoresRelacionExpSuj(tipRela);
			nuevoSujetoAux = sujetosObligadosExpedientesService.guardar(nuevoSujetoAux);
			expedientes = expedientesService.obtener(expedientes.getId());
			expedientes = utilsComun.expedienteUltimaModificacion(expedientes,nuevoSujetoAux.getFechaModificacion(),nuevoSujetoAux.getFechaCreacion(),nuevoSujetoAux.getUsuModificacion(),nuevoSujetoAux.getUsuCreacion());
		} else {
			// opcion guardar sujeto expedientes
			nuevoSujetoByExpedienteNotNull(tipRela);
		}
		
		if(Boolean.TRUE.equals(sujetosObligadosExpedientes.getPrincipal())) {
			datosExpedientesBean.actualizarCabecera(expedientes,null,sujetosObligadosExpedientes,null);
		}else {
			datosExpedientesBean.actualizarCabecera(expedientes,null,null,null);
		}
	}
	
	private void nuevoSujetoByExpedienteNotNull (ValoresDominio tipRela) throws BaseException {
		if (sujetosObligadosExpedientes != null && sujetosObligadosExpedientes.getSujetosObligados() != null) {
			// opcion editar sujeto expediente
			sujetosObligadosExpedientes.setValoresRelacionExpSuj(tipRela);
			sujetosObligadosExpedientes = sujetosObligadosExpedientesService.guardar(sujetosObligadosExpedientes);
		}else
		{
			sujetosObligadosExpedientes = new SujetosObligadosExpedientes();
		
		List<SujetosObligadosExpedientes> listaSujetosConExpediente = sujetosObligadosExpedientesService
				.obtenerSujetosObligadosExpedientePrincipalYNoPrincipalPorExpediente(
						expedientes.getId());
		if (!listaSujetosConExpediente.isEmpty()) {
			for (SujetosObligadosExpedientes sujExp : listaSujetosConExpediente) {
				seteoPrincipalSujetoExpediente(sujExp);
			}
		} else {
			sujetosObligadosExpedientes.setPrincipal(true);
		}
		sujetosObligadosExpedientes.setExpediente(expedientes);
		sujetosObligadosExpedientes.setSujetosObligados(nuevoSujeto);
		
		}
		
		sujetosObligadosExpedientes.setValoresRelacionExpSuj(tipRela);
		sujetosObligadosExpedientes.setNombreRazonsocial(this.sujObligNombreRazonsocial);
		sujetosObligadosExpedientes.setApellidos(this.sujObligApellidos);
		sujetosObligadosExpedientes.setEmail(this.sujObligEmail);
		sujetosObligadosExpedientes.setTelefono(this.sujObligTelefono);
		sujetosObligadosExpedientes.setDpd(this.sujObligDpd);	
		sujetosObligadosExpedientes = sujetosObligadosExpedientesService.guardar(sujetosObligadosExpedientes);
		
		expedientes = expedientesService.obtener(expedientes.getId());
		expedientes = utilsComun.expedienteUltimaModificacion(expedientes,sujetosObligadosExpedientes.getFechaModificacion(),sujetosObligadosExpedientes.getFechaCreacion(),sujetosObligadosExpedientes.getUsuModificacion(),sujetosObligadosExpedientes.getUsuCreacion());
		PrimeFaces.current().ajax()
				.update(TABLASUJETOEXPEDIENTENONULO);
		
	}
	
	private void asignarByExpedienteNull(ValoresDominio tipRela) {
		if (sujetosObligadosExpedientes != null && sujetosObligadosExpedientes.getSujetosObligados() != null
				&& selectedNuevoMotivoRelacionId != null) {

			// opcion editar sujetos expedientes
			for (SujetosObligadosExpedientes sujetoExpedienteAux : listaSujetosObligadosExpedientes) {

				if (sujetoExpedienteAux.equals(sujetosObligadosExpedientes)) {
					ValoresDominio valoresRelacionExpPer = valoresDominioService
							.obtener(selectedNuevoMotivoRelacionId);
					sujetoExpedienteAux.setValoresRelacionExpSuj(valoresRelacionExpPer);

				} else if (Boolean.TRUE.equals(sujetoPrincipal) && sujetoExpedienteAux.getPrincipal().equals(true)) {
					sujetosObligadosExpedientes.setPrincipal(true);
					sujetoExpedienteAux.setPrincipal(false);
					/*
					 * si edito uno que no es principal y lo pongo a principal, lo seteo a principal
					 * y al que estaba principal lo pongo a false
					 */
				}
			}

		} else {
			// opcion guardar sujetos expedientes
			nuevoSujetoExpediente(tipRela);
		}

		JsfUtils.setSessionAttribute(EXPEDIENTESDS, listaSujetosObligadosExpedientes);
		PrimeFaces.current().ajax().update(
				TABLASUJETOEXPEDIENTENULO);
	}
	
	private void nuevoSujetoExpediente (ValoresDominio tipRela) {
		nuevoSujetoExp = new SujetosObligadosExpedientes();
		nuevoSujetoExp.setValoresRelacionExpSuj(tipRela);
		nuevoSujetoExp.setSujetosObligados(nuevoSujeto);
		nuevoSujetoExp.setPrincipal(sujetoPrincipal);
		nuevoSujetoExp.setNombreRazonsocial(this.sujObligNombreRazonsocial);
		nuevoSujetoExp.setApellidos(this.sujObligApellidos);
		nuevoSujetoExp.setEmail(this.sujObligEmail);
		nuevoSujetoExp.setTelefono(this.sujObligTelefono);
		nuevoSujetoExp.setDpd(this.sujObligDpd);	
		
		
		if (!listaSujetosObligadosExpedientes.isEmpty()) {
			for (SujetosObligadosExpedientes s : listaSujetosObligadosExpedientes) {
				if (!s.equals(nuevoSujetoExp) && Boolean.TRUE.equals(sujetoPrincipal)) {
					s.setPrincipal(false);
				}
			}
		}
		listaSujetosObligadosExpedientes.add(nuevoSujetoExp);
	}

	public void eliminarSujetosObligados(SujetosObligadosExpedientes sujetosObligadosExpedienteSeleccionado) {

		try {
			if (expedientes.getId() != null) {
				List<SujetosObligadosExpedientes> listaSujetosConExpediente = sujetosObligadosExpedientesService
						.obtenerSujetosObligadosExpedientePrincipalYNoPrincipalPorExpediente(expedientes.getId());
				for (SujetosObligadosExpedientes sujExp : listaSujetosConExpediente) {

					if (sujetosObligadosExpedienteSeleccionado.getId().equals(sujExp.getId())) {
						if (sujExp.getPrincipal().equals(false)) {
							sujetosObligadosExpedientesService.delete(sujetosObligadosExpedienteSeleccionado.getId());
							FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, new FacesMessage(FacesMessage.SEVERITY_INFO, "", MENSAJESUJETO +" "+sujetosObligadosExpedienteSeleccionado.getSujetosObligados().getDescripcion()+" "+ PARAELEXPEDIENTE+ expedientes.getNumExpediente()+" "+ mensajesProperties.getString(ACTUALIZADOCORRECTAMENTE)));
						
							Usuario usuarioLogado = (Usuario) JsfUtils.getSessionAttribute(Constantes.USUARIO);
							expedientes = expedientesService.obtener(expedientes.getId());
							expedientes = utilsComun.expedienteUltimaModificacion(expedientes,FechaUtils.fechaYHoraActualDate(),FechaUtils.fechaYHoraActualDate(),usuarioLogado.getLogin(),usuarioLogado.getLogin());

						} else {
							FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString("sujeto.no.puede.eliminar"));
							PrimeFaces.current().dialog().showMessageDynamic(message);
						}
					}
				}
				datosExpedientesBean.actualizarCabecera(expedientes,null,null,null);
			} else {
				eliminarSujetoByExpedienteNull(sujetosObligadosExpedienteSeleccionado);
			}
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}

	}
	
	private void eliminarSujetoByExpedienteNull (SujetosObligadosExpedientes sujetosObligadosExpedienteSeleccionado) {
		for (SujetosObligadosExpedientes sujExp : listaSujetosObligadosExpedientes) {

			if (sujetosObligadosExpedienteSeleccionado.equals(sujExp)) {
				listaSujetosObligadosExpedientes.remove(sujetosObligadosExpedienteSeleccionado);
				FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, new FacesMessage(FacesMessage.SEVERITY_INFO, "", MENSAJESUJETO + mensajesProperties.getString(ACTUALIZADOCORRECTAMENTE)));
				break;
			}
		}
	}

	private void limpiarAsignarMotRelaSujeto() {
		sujetosObligadosExpedientes = new SujetosObligadosExpedientes();
		nuevoSujetoExp = new SujetosObligadosExpedientes();
		this.selectedNuevoMotivoRelacionId = null;
		this.sujObligApellidos = null;
		this.sujObligNombreRazonsocial = null;
		this.sujObligEmail = null;
		this.sujObligTelefono = null;
		this.sujObligDpd = null;
		PrimeFaces.current().executeScript("PF('dialogAsignarMotRelaSujetos').hide();");
		PrimeFaces.current().executeScript("PF('dialogSujetoExp').hide();");
	}
	
	public void onRowSelect(final SelectEvent<SujetosObligadosExpedientes> event) {
		plegado = false;						
		sujetosObligadosExpedientesSeleccionado = event.getObject();
		this.personas = event.getObject().getPersonas();
		this.sujObligApellidos = sujetosObligadosExpedientesSeleccionado.getApellidos();
		this.sujObligNombreRazonsocial = sujetosObligadosExpedientesSeleccionado.getNombreRazonsocial();
		this.sujObligEmail = sujetosObligadosExpedientesSeleccionado.getEmail();
		this.sujObligDpd = sujetosObligadosExpedientesSeleccionado.getDpd();
		this.sujObligTelefono = sujetosObligadosExpedientesSeleccionado.getTelefono();
		PrimeFaces.current().ajax().update(BLOQUEDPD);
	}
	
	public void limpiarFiltroPersDpd() {
		nombreRazonSocialFiltro = null;
		cifnifFiltro = null;

	}
	
	public void asignarPersonaDpd(Personas personaRepresentanteSeleccionada) throws BaseException {	
		this.personas = personaRepresentanteSeleccionada;
		if(expedientes.getId() != null) {
			sujetosObligadosExpedientesSeleccionado.setPersonas(personas);
			sujetosObligadosExpedientesSeleccionado = sujetosObligadosExpedientesService.guardar(sujetosObligadosExpedientesSeleccionado);
			expedientes = expedientesService.obtener(expedientes.getId());
			expedientes = utilsComun.expedienteUltimaModificacion(expedientes,sujetosObligadosExpedientesSeleccionado.getFechaModificacion(),sujetosObligadosExpedientesSeleccionado.getFechaCreacion(),sujetosObligadosExpedientesSeleccionado.getUsuModificacion(),sujetosObligadosExpedientesSeleccionado.getUsuCreacion());
		
		
			PrimeFaces.current().ajax().update(TABLASUJETOEXPEDIENTENONULO);
			FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO,new FacesMessage(FacesMessage.SEVERITY_INFO, "",MENSAJEPERSONA +" "+personaRepresentanteSeleccionada.getNombreRazonsocial()+" "+ PARAELEXPEDIENTE+ expedientes.getNumExpediente()+" "+ mensajesProperties.getString(ACTUALIZADOCORRECTAMENTE)));
			
			datosExpedientesBean.actualizarCabecera(expedientes,null,null,null);
		}else {
			if(sujetosObligadosExpedientesSeleccionado==null) {
				for (SujetosObligadosExpedientes s : listaSujetosObligadosExpedientes) {
					s.setPersonas(personaRepresentanteSeleccionada);	
			}
				PrimeFaces.current().ajax().update(TABLASUJETOEXPEDIENTENULO);
				FacesContext.getCurrentInstance().addMessage(MENSAJESPESTANYASUJETO,new FacesMessage(FacesMessage.SEVERITY_INFO, "","Representante asignado correctamente a la persona expediente seleccionada" + mensajesProperties.getString(GUARDARPERSISTENCIA)));							
		}else {
			sujetosObligadosExpedientesSeleccionado.setPersonas(personaRepresentanteSeleccionada);
			PrimeFaces.current().ajax().update(TABLASUJETOEXPEDIENTENULO);
			FacesContext.getCurrentInstance().addMessage(MENSAJESPESTANYASUJETO,new FacesMessage(FacesMessage.SEVERITY_INFO, "","Representante asignado correctamente a la persona expediente seleccionada" + mensajesProperties.getString(GUARDARPERSISTENCIA)));
			}
			
		} 					
		
		this.limpiarFiltroPersDpd();
		PrimeFaces.current().executeScript("PF('dialogPersonaDpd').hide();");
		PrimeFaces.current().ajax().update(BLOQUEDPD);
		PrimeFaces.current().ajax().update("formFormularioExpedientes:tabViewPestanasExpediente:bloqueFiltroBusquedaDialogDpd");
		PrimeFaces.current().ajax().update("formFormularioExpedientes:tabViewPestanasExpediente:panelResultadosBusquedaPersonasDpd");
	}						

	public void quitarPersonaDpd() throws BaseException {
		Personas personasBorrar = sujetosObligadosExpedientesSeleccionado.getPersonas();
		sujetosObligadosExpedientesSeleccionado.setPersonas(null);
		if(expedientes.getId() != null) {
			sujetosObligadosExpedientesSeleccionado = sujetosObligadosExpedientesService.guardar(sujetosObligadosExpedientesSeleccionado);
			expedientes = expedientesService.obtener(expedientes.getId());
			expedientes = utilsComun.expedienteUltimaModificacion(expedientes,sujetosObligadosExpedientesSeleccionado.getFechaModificacion(),sujetosObligadosExpedientesSeleccionado.getFechaCreacion(),sujetosObligadosExpedientesSeleccionado.getUsuModificacion(),sujetosObligadosExpedientesSeleccionado.getUsuCreacion());
		
			FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO,new FacesMessage(FacesMessage.SEVERITY_INFO, "",MENSAJEPERSONA +" "+personasBorrar.getNombreRazonsocial()+" "+ "para el expediente "+ expedientes.getNumExpediente()+" "+ mensajesProperties.getString(ACTUALIZADOCORRECTAMENTE)));
			PrimeFaces.current().ajax().update(TABLASUJETOEXPEDIENTENONULO);
			
			datosExpedientesBean.actualizarCabecera(expedientes,null,null,null);
		}else {
			sujetosObligadosExpedientesSeleccionado.setPersonas(null);
			for (SujetosObligadosExpedientes s : listaSujetosObligadosExpedientes) {
				if (s.equals(sujetosObligadosExpedientesSeleccionado)) {
					s.setPersonas(null);
				}
			}
			FacesContext.getCurrentInstance().addMessage(MENSAJESPESTANYASUJETO,new FacesMessage(FacesMessage.SEVERITY_INFO, "","Representante desasignado correctamente de la persona seleccionada" + mensajesProperties.getString(GUARDARPERSISTENCIA)));
			PrimeFaces.current().ajax().update(TABLASUJETOEXPEDIENTENULO);
		} 
		
		personas = null;			
		PrimeFaces.current().ajax().update(BLOQUEDPD);
	}

	
	public void abrirBuscadorPersonas (){
		nombreRazonSocialFiltro ="";
		cifnifFiltro = "";
		
		if(expedientes.getId()!=null) {
			cabeceraBuscadorSujetoDpd=getMessage(BUSCADORPERSONA)+PARAELEXPEDIENTE+ expedientes.getNumExpediente();
		}else {
			cabeceraBuscadorSujetoDpd=getMessage(BUSCADORPERSONA);
		}
		
		PrimeFaces.current().executeScript("PF('dialogPersonaDpd').show();");
	}
	
	public void abrirBuscadorSujetos () {
		descripcionFiltro = "";
		sujetosObligadosSupIdFiltro = null;
		sujetosObligadosSupDescripcionFiltro = "";
		tipoAgrupacionIdFiltro = null;		
		if(expedientes.getId()!=null) {
			cabeceraBuscadorSujeto=getMessage("buscador.sujeto")+PARAELEXPEDIENTE+ expedientes.getNumExpediente();
		}else {
			cabeceraBuscadorSujeto=getMessage("buscador.sujeto");
		}
		
		PrimeFaces.current().executeScript("PF('dialogSujetoExp').show();");
	}
	
	public void cambioCheckPrincipal(SujetosObligadosExpedientes sujetoExpedienteSeleccionado) {
		/** desmarcar principal */
		if (Boolean.FALSE.equals(sujetoExpedienteSeleccionado.getPrincipal())) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "El sujeto debe ser principal. El expediente siempre debe tener un sujeto principal relacionado con él.");
			PrimeFaces.current().dialog().showMessageDynamic(message);
		}

		/** de una manera u otra siempre va a ponerse principal a true */

		/** caso de nuevo expediente */
		if (expedientes.getId() == null) {
			for (SujetosObligadosExpedientes s : listaSujetosObligadosExpedientes) {
				s.setPrincipal(false);
				if (s.equals(sujetoExpedienteSeleccionado)) {
					s.setPrincipal(true);
				}
			}
			PrimeFaces.current().ajax().update(TABLASUJETOEXPEDIENTENULO);
			FacesContext.getCurrentInstance().addMessage(MENSAJESPESTANYASUJETO, new FacesMessage(FacesMessage.SEVERITY_INFO, "", 
					MENSAJESUJETO + mensajesProperties.getString(GUARDADOCORRECTAMENTE) + mensajesProperties.getString(GUARDARPERSISTENCIA)));

		} else {
			/** caso expediente ya creado */
			cambioCheckExpedienteCreado(sujetoExpedienteSeleccionado);
		}
		
		if(expedientes.getId() != null) {
			datosExpedientesBean.actualizarCabecera(expedientes,null,null,null);
		}
	}
	
	private void cambioCheckExpedienteCreado (SujetosObligadosExpedientes sujetoExpedienteSeleccionado) {
		
		List<SujetosObligadosExpedientes> listaSujetoConExpediente=sujetosObligadosExpedientesService.obtenerSujetosObligadosExpedientePrincipalYNoPrincipalPorExpediente(expedientes.getId());
		SujetosObligadosExpedientes sujetoPrincipalActual = new SujetosObligadosExpedientes();
		
		for (SujetosObligadosExpedientes sujConExp : listaSujetoConExpediente) {
			if (sujConExp.getId().equals(sujetoExpedienteSeleccionado.getId())) {
				sujConExp.setPrincipal(true);
				sujetoPrincipalActual = sujConExp;
			} else if (Boolean.TRUE.equals(sujConExp.getPrincipal())) {
				sujConExp.setPrincipal(false);
			}
			try {
				sujConExp = sujetosObligadosExpedientesService.guardar(sujConExp);
				expedientes = expedientesService.obtener(expedientes.getId());
				expedientes = utilsComun.expedienteUltimaModificacion(expedientes,sujConExp.getFechaModificacion(),sujConExp.getFechaCreacion(),sujConExp.getUsuModificacion(),sujConExp.getUsuCreacion());
			} catch (final BaseException e) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MENSAJEERROR));
				PrimeFaces.current().dialog().showMessageDynamic(message);
				log.error(e.getMessage());
			}
		}
		PrimeFaces.current().ajax().update(TABLASUJETOEXPEDIENTENONULO);
		FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
				MENSAJESUJETO +" "+sujetoExpedienteSeleccionado.getSujetosObligados().getDescripcion()+" "+ "para el expediente "+ expedientes.getNumExpediente()+" "+ mensajesProperties.getString(ACTUALIZADOCORRECTAMENTE)));
		
		if(Boolean.TRUE.equals(sujetoPrincipalActual.getPrincipal())) {
			datosExpedientesBean.actualizarCabecera(expedientes,null,sujetoPrincipalActual,null);
		}else {
			datosExpedientesBean.actualizarCabecera(expedientes,null,null,null);
		}
	}
	
	private void seteoPrincipalSujetoExpediente (SujetosObligadosExpedientes sujExp) {
		if (sujetosObligadosExpedientes.getId() != null && sujExp.getId().equals(sujetosObligadosExpedientes.getId()) && sujetoPrincipal.equals(true)) {
			sujetosObligadosExpedientes.setPrincipal(true);
		} else if (!(sujExp.getId().equals(sujetosObligadosExpedientes.getId())) && sujetoPrincipal.equals(true)) {
			sujExp.setPrincipal(false);
			sujetosObligadosExpedientes.setPrincipal(true);
		} else if (sujetoPrincipal.equals(false)) {
			sujetosObligadosExpedientes.setPrincipal(false);
		}
	}
	
	private void anyadeSujetoPorDefectoSegunCfgTipoExpediente () {
		ValoresDominio valoresDominioTipoExpActual = null;
		Long idTipoExpediente = (Long) JsfUtils.getSessionAttribute("idTipoExpediente");
		if(idTipoExpediente != null) { /** alta de expediente */
			valoresDominioTipoExpActual = valoresDominioService.obtener(idTipoExpediente);
		}else { /** edicion o consulta */
			valoresDominioTipoExpActual = expedientes.getValorTipoExpediente();
		}
		
		CfgTipoExpediente cfgTipoExpediente = cfgTipoExpedienteService.obtenerCfgTipoExpedienteByCodigoTipoExp(valoresDominioTipoExpActual.getCodigo());
		if(expedientes.getId() == null && cfgTipoExpediente.getSujetoPorDefecto() != null && cfgTipoExpediente.getValorMotivoRelacionSujeto() != null) {
			SujetosObligadosExpedientes sujetosObligadosExpedientesNew = new SujetosObligadosExpedientes();
			sujetosObligadosExpedientesNew.setSujetosObligados(cfgTipoExpediente.getSujetoPorDefecto());
			sujetosObligadosExpedientesNew.setPrincipal(true);
			sujetosObligadosExpedientesNew.setValoresRelacionExpSuj(cfgTipoExpediente.getValorMotivoRelacionSujeto());	
			listaSujetosObligadosExpedientes.add(sujetosObligadosExpedientesNew);		
			
			JsfUtils.setSessionAttribute(EXPEDIENTESDS, listaSujetosObligadosExpedientes);
			PrimeFaces.current().ajax().update(TABLASUJETOEXPEDIENTENULO);
		}		
	}

}
