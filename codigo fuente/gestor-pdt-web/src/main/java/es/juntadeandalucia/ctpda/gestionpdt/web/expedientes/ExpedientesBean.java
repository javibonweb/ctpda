package es.juntadeandalucia.ctpda.gestionpdt.web.expedientes;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.ExpedientesMaestra;
import es.juntadeandalucia.ctpda.gestionpdt.model.PersonasExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.ResponsablesTramitacion;
import es.juntadeandalucia.ctpda.gestionpdt.model.SujetosObligadosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.repository.ExpedientesRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.ExpedientesMaestraService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ResponsablesTramitacionService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ValoresDominioService;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.Constantes;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.JsfUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.LazyDataModelByQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.MyFilterMeta;
import es.juntadeandalucia.ctpda.gestionpdt.web.menu.NavegacionBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.menu.NavegacionBean.ListadoNavegaciones;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.BaseBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.ContextoVolver;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.FacesUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.VolverBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@ViewScoped
@Slf4j
public class ExpedientesBean extends BaseBean implements Serializable {
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	private static final String EDITABLE = "editable";
	private static final String FORMDIALOGEXPEDIENTES = "formDialogExpedientes";
	private static final String ESEXPEDIENTEFINAL = "esExpedienteFinal";
	private static final String LISTADOENTRADAS = "listadoEntradas";
	private static final String LISTADOEXPEDS = "listadoExpeds";
	

	public static final String VOLVERLISTADOENTRADAS = "_volverListadoEntradas_";

	@Getter
	@Setter
	private String nombreListado;
	
	@Getter
	@Setter
	private String volverMenu;

	@Autowired
	private ValoresDominioService valoresDominioService;

	@Autowired
	private ExpedientesMaestraService expedientesMaestraService;

	@Autowired
	private ResponsablesTramitacionService responsablesTramitacionService;

	@Getter
	private LazyDataModelByQueryService<ExpedientesMaestra> lazyModel;

	@Getter
	private ExpedientesRepository expedientesRepository;

	@Getter
	@Setter
	private Long selectedTipoExpedienteIdFiltro;

	@Getter
	@Setter
	private Long selectedTipoExpedienteXPCIdFiltro;

	@Getter
	@Setter
	private Long selectedNuevoTipoExpedienteId;

	@Getter
	@Setter
	private Long selectedSituacionIdFiltro;

	@Getter
	@Setter
	private Long selectedMateriaIdFiltro;

	@Getter
	@Setter
	private Long selectedNuevoTipoInfraccionId;

	@Getter
	@Setter
	private Long selectedNuevaMateriaId;
	@Getter
	@Setter
	private Long selectedNuevoCanalEntId;

	@Getter
	@Setter
	private Long selectedExpediente;
	@Getter
	@Setter
	private Long selectedNuevaSituacionId;

	@Getter
	@Setter
	private Long selectedResponsableIdFiltro;

	@Getter
	@Setter
	private String numeroExpedienteFiltro;

	@Getter
	@Setter
	private String responsableFiltro;

	@Getter
	@Setter
	private Date fechaEntradaInicialFiltro;

	@Getter
	@Setter
	private Date fechaEntradaFinalFiltro;

	@Getter
	@Setter
	private String personaFiltro;

	@Getter
	@Setter
	private String identPersonaFiltro;

	@Getter
	@Setter
	private String sujetoObligadoFiltro;
	@Getter
	@Setter
	private ExpedientesMaestra selectedExpedientes;

	@Getter
	@Setter
	private List<ValoresDominio> listaValoresDominioTipoExp;

	@Getter
	@Setter
	private List<ValoresDominio> listaValoresDominioTipExp;

	@Getter
	@Setter
	private List<ValoresDominio> listaValoresDominioSituacion;
	
	@Getter
	@Setter
	private List<ValoresDominio> listaValoresDominioSituacionXPC;
	
	
	@Getter
	@Setter
	private List<ValoresDominio> listaValoresDominioMaterias;

	@Getter
	@Setter
	private List<ResponsablesTramitacion> listaResponsablesTramitacion;

	@Getter
	@Setter
	private List<ExpedientesMaestra> listaExpedientesMaestra;
	@Getter
	@Setter
	private List<Expedientes> listaExpedientes;
	@Getter
	@Setter
	private Expedientes expedientes;

	@Getter
	@Setter
	private PersonasExpedientes personasExpedientes;

	@Getter
	@Setter
	private SujetosObligadosExpedientes sujetosObligadosExpedientes;
	@Getter
	@Setter
	private ExpedientesMaestra expedientesMaestra;
	@Getter
	@Setter
	private String tipoExpedienteFiltro;
	@Getter
	@Setter
	private String situacionFiltro;
	@Getter
	@Setter
	private String cabeceraDialogo;
	@Getter
	@Setter
	private Expedientes nuevoExpediente;
	@Getter
	@Setter
	private String fechaEntrada;

	@Getter
	private SortMeta defaultOrden;

	@Getter
	@Setter
	private List<String> listaCodigoPermisos;

	@Getter
	@Setter
	private Boolean permisoNewExp;

	@Getter
	@Setter
	private Boolean permisoConsExp;

	@Getter
	@Setter
	private Boolean permisoEditExp;

	@Getter
	@Setter
	private Boolean permisoSaveExp;

	@Getter
	@Setter
	private Boolean mostrarMensajeUltimoExpedienteCreado;

	@Getter
	@Setter
	private String mensajeExpedienteCreado;

	@Getter
	@Setter
	private Boolean noFinalizadosFiltro;
	@Autowired
	private VolverBean volverBean;

	@Autowired
	private NavegacionBean navegacionBean;

	@Autowired
	private ExpedientesService expedientesService;

	@PostConstruct
	@Override
	public void init() {
		super.init();

		this.nombreListado = FacesUtils.getVar("nombreListado", String.class);

		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, 0);
		
		
		if (volverMenu != null && volverMenu.equals("entradas")) {
			navegacionBean.setTxtMigaPan(Constantes.LISTADO_ENTRADAS);
		} else if (volverMenu != null){
			navegacionBean.setTxtMigaPan(Constantes.LISTADO_EXPEDIENTES);
		}

		/**
		 * GESTION DE PERMISOS
		 */
		listaCodigoPermisos = (List<String>) JsfUtils.getSessionAttribute(Constantes.LISTACODIGOPERMISOS);

		permisoNewExp = listaCodigoPermisos.contains(Constantes.PERMISO_NEW_EXP);

		permisoConsExp = listaCodigoPermisos.contains(Constantes.PERMISO_CONS_EXP);

		permisoEditExp = listaCodigoPermisos.contains(Constantes.PERMISO_EDIT_EXP);

		permisoSaveExp = listaCodigoPermisos.contains(Constantes.PERMISO_SAVE_EXP);

		FacesUtils.setAttribute(EDITABLE, JsfUtils.getFlashAttribute(EDITABLE));

		selectedTipoExpedienteXPCIdFiltro = valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_TIPO_EXPEDIENTE,Constantes.XPC)
				.getId();

		/** mensaje estatico para el ultimo expediente creado */
		ContextoVolver cv = volverBean.recogerContexto();
		if (cv != null) {
			if (cv.get(ESEXPEDIENTEFINAL) != null && cv.get(ESEXPEDIENTEFINAL).equals(true)) {
				mensajeUltimoExpFinalizado();
			}
		} else {

			mensajeUltimoExpCreado();
		}

		lazyModel = new LazyDataModelByQueryService<>(ExpedientesMaestra.class, expedientesMaestraService);

		lazyModel.setPreproceso((a, b, c, filters) -> {
			/** añadamos siempre un orden por id en los casos de vistas */
			if (c != null) {
				final SortMeta ordenacionById = SortMeta.builder().field("id").order(SortOrder.ASCENDING)
						.priority(c.size() + 1).build();
				c.put("id", ordenacionById);
			}
			
			Boolean filtroFinalizados = filtrosLazyModelAux();
			if (filtroFinalizados != null) {
				filters.put("finalizados", new MyFilterMeta(filtroFinalizados));
			}
			filtrosLazyModel(filters);
		});

		listaValoresDominioTipoExp = valoresDominioService.findValoresTipoExpedienteSinXPC();

		listaValoresDominioSituacion = valoresDominioService.findValoresSituacion();

		listaValoresDominioSituacionXPC = valoresDominioService.findValoresSituacionXPC();
		
		listaValoresDominioMaterias = valoresDominioService.findValoresMaterias();

		listaResponsablesTramitacion = responsablesTramitacionService.findResponsablesActivos();

		listaValoresDominioTipExp = valoresDominioService.findValoresTipoExpediente();

		expedientes = new Expedientes();

		defaultOrden = SortMeta.builder().field("numExpediente").order(SortOrder.ASCENDING).priority(1).build();

		noFinalizadosFiltro = true;

		/**
		 * se elimina de sesion todos los atributos usados para el control en los
		 * formularios
		 */
		JsfUtils.removeSessionAttribute("expedienteFormulario");
		JsfUtils.removeSessionAttribute("expedientesDG");
		JsfUtils.removeSessionAttribute("expedientesDA");
		JsfUtils.removeSessionAttribute("expedientesDD");
		JsfUtils.removeSessionAttribute("expedientesDP");
		JsfUtils.removeSessionAttribute("expedientesDS");
		JsfUtils.removeSessionAttribute(Constantes.LISTA_TRAM_EXP);
		JsfUtils.removeSessionAttribute(Constantes.LISTA_SUB_TRAM_EXP_OTRO);
		JsfUtils.removeSessionAttribute(Constantes.LISTA_SUB_TRAM_EXP_TRDPD);
		JsfUtils.removeSessionAttribute("expediente");
		JsfUtils.removeSessionAttribute(EDITABLE);
		JsfUtils.removeSessionAttribute(Constantes.ULTIMO_EXPEDIENTE_CREADO);
		JsfUtils.removeSessionAttribute(Constantes.ULTIMO_TIPO_EXPEDIENTE_CREADO);
		JsfUtils.removeSessionAttribute(Constantes.ULTIMO_EXPEDIENTE_FINALIZADO);
		JsfUtils.removeSessionAttribute("idTipoExpediente");

	}

	private void filtrosLazyModel(Map<String, FilterMeta> filters) {
		if (this.nombreListado.equals(LISTADOENTRADAS)) {
			String abreviatura = valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_TIPO_EXPEDIENTE,Constantes.XPC).getAbreviatura();
			filters.put("tipoExpediente", new MyFilterMeta(abreviatura));
		} else if (selectedTipoExpedienteIdFiltro != null) {
			String abreviatura = valoresDominioService.obtener(selectedTipoExpedienteIdFiltro).getAbreviatura();
			filters.put("tipoExpediente", new MyFilterMeta(abreviatura));
		} else if (this.nombreListado.equals(LISTADOEXPEDS)) {
			String abreviatura = valoresDominioService.findValoresDominioByCodigoDomCodValDom(Constantes.COD_TIPO_EXPEDIENTE,Constantes.XPC).getAbreviatura();
			filters.put("#tipoExpedienteExcluido", new MyFilterMeta(abreviatura));
		}
		if (numeroExpedienteFiltro != null && !numeroExpedienteFiltro.isEmpty()) {
			filters.put("numExpediente", new MyFilterMeta(numeroExpedienteFiltro));
		}
		if (selectedSituacionIdFiltro != null) {
			String descripcion = valoresDominioService.obtener(selectedSituacionIdFiltro).getDescripcion();
			filters.put("situacion", new MyFilterMeta(descripcion));
		}
		if (!this.nombreListado.equals(LISTADOENTRADAS) && selectedMateriaIdFiltro != null) {
			filters.put("idMateria", new MyFilterMeta("-" + selectedMateriaIdFiltro.toString() + "-"));
		}
		if (fechaEntradaInicialFiltro != null) {
			filters.put("#fechaInicialEntrada", new MyFilterMeta(fechaEntradaInicialFiltro));
		}
		if (fechaEntradaFinalFiltro != null) {
			filters.put("#fechaFinalEntrada", new MyFilterMeta(fechaEntradaFinalFiltro));
		}
		if (personaFiltro != null && !personaFiltro.isEmpty()) {
			filters.put("persona", new MyFilterMeta(personaFiltro));
		}
		if (identPersonaFiltro != null && !identPersonaFiltro.isEmpty()) {
			filters.put("identPersona", new MyFilterMeta(identPersonaFiltro));
		}
		if (sujetoObligadoFiltro != null && !sujetoObligadoFiltro.isEmpty()) {
			filters.put("sujetoObligado", new MyFilterMeta(sujetoObligadoFiltro));
		}
		if (selectedResponsableIdFiltro != null) {
			String descripcion = responsablesTramitacionService.obtener(selectedResponsableIdFiltro).getDescripcion();
			filters.put("responsable", new MyFilterMeta(descripcion));
		}		
	}

	private Boolean filtrosLazyModelAux() {
		ContextoVolver cv = volverBean.recogerContexto();
		Boolean res = null;
		if (cv != null) {
			if ((cv.get(ESEXPEDIENTEFINAL) != null && cv.get(ESEXPEDIENTEFINAL).equals(true))
					&& (noFinalizadosFiltro != null && noFinalizadosFiltro)) {
				res = true;
			}
		} else {
			if (noFinalizadosFiltro != null && noFinalizadosFiltro) {
				res = false;
			}
		}
		return res;
	}

	private void mensajeUltimoExpCreado() {
		mostrarMensajeUltimoExpedienteCreado = false;
		String ultimoNumeroExpedienteCreado = (String) JsfUtils
				.getSessionAttribute(Constantes.ULTIMO_EXPEDIENTE_CREADO);
		String ultimoTipoExpedienteCreado = (String) JsfUtils
				.getSessionAttribute(Constantes.ULTIMO_TIPO_EXPEDIENTE_CREADO);
		if (ultimoNumeroExpedienteCreado != null && !ultimoNumeroExpedienteCreado.isBlank()) {
			mostrarMensajeUltimoExpedienteCreado = true;
			if (ultimoTipoExpedienteCreado.equals("XPC")) {
				mensajeExpedienteCreado = "Entrada con número " + ultimoNumeroExpedienteCreado + " "
						+ mensajesProperties.getString("guardada.correctamente");
			} else {
				mensajeExpedienteCreado = "Expediente con número " + ultimoNumeroExpedienteCreado + " "
						+ mensajesProperties.getString("guardado.correctamente");
			}
			numeroExpedienteFiltro = ultimoNumeroExpedienteCreado;
		}
	}

	private void mensajeUltimoExpFinalizado() {
		mostrarMensajeUltimoExpedienteCreado = false;
		String ultimoNumeroExpedienteCreado = (String) JsfUtils
				.getSessionAttribute(Constantes.ULTIMO_EXPEDIENTE_FINALIZADO);
		if (ultimoNumeroExpedienteCreado != null && !ultimoNumeroExpedienteCreado.isBlank()) {
			mostrarMensajeUltimoExpedienteCreado = true;
			mensajeExpedienteCreado = "Expediente con número " + ultimoNumeroExpedienteCreado + " " + "finalizado";
			numeroExpedienteFiltro = ultimoNumeroExpedienteCreado;
		}

	}

	public String redireccionMenu() {
		this.volverMenu = "expedientes";
		this.init();
		return ListadoNavegaciones.LISTADO_EXPEDIENTES.getRegla();
	}

	public String redireccionMenuListadoEntradas() {
		this.volverMenu = "entradas";
		this.init();
		return ListadoNavegaciones.LISTADO_ENTRADAS.getRegla();
	}

	public void limpiarFiltro() {
		selectedTipoExpedienteIdFiltro = null;
		numeroExpedienteFiltro = "";
		selectedSituacionIdFiltro = null;
		selectedMateriaIdFiltro = null;
		fechaEntradaInicialFiltro = null;
		fechaEntradaFinalFiltro = null;
		personaFiltro = "";
		identPersonaFiltro = "";
		sujetoObligadoFiltro = "";
		noFinalizadosFiltro = true;
		selectedResponsableIdFiltro = null;

	}

	public String onCrear(String codTipExp) {

		if (codTipExp.equals(Constantes.XPC)) {
			this.selectedNuevoTipoExpedienteId = valoresDominioService
					.findValoresDominioByCodigoDomCodValDom(Constantes.COD_TIPO_EXPEDIENTE,Constantes.XPC).getId();
			JsfUtils.setSessionAttribute(Constantes.VUELTA_LISTADO_ENTRADAS, true);
		}

		expedientes = new Expedientes();
		String navegacion = "";
		if (this.selectedNuevoTipoExpedienteId == null) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					mensajesProperties.getString("campos.obligatorios"));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			PrimeFaces.current().ajax().update(FORMDIALOGEXPEDIENTES);
		} else {
			// No se ha enviado id, la persona viene inicializada por defecto.
			JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS,
					((Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS)) + 1);
			JsfUtils.setFlashAttribute(EDITABLE, true);
			JsfUtils.setSessionAttribute("idTipoExpediente", this.selectedNuevoTipoExpedienteId);
			navegacionBean.setTxtMigaPan(Constantes.ALTA_EXPEDIENTE);
			navegacion = ListadoNavegaciones.FORM_EXPEDIENTES.getRegla();
		}
		return navegacion;
	}

	public String onEditar(Long idExp) {

		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS,
				((Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS)) + 1);
		JsfUtils.setFlashAttribute(EDITABLE, true);
		JsfUtils.setFlashAttribute("idExp", idExp);
		JsfUtils.setSessionAttribute(EDITABLE, true);
		Expedientes expediente = expedientesService.obtener(idExp);
		if (Constantes.XPC.equals(expediente.getValorTipoExpediente().getCodigo())) {
			this.selectedNuevoTipoExpedienteId = valoresDominioService
					.findValoresDominioByCodigoDomCodValDom(Constantes.COD_TIPO_EXPEDIENTE,Constantes.XPC).getId();
			JsfUtils.setSessionAttribute(Constantes.VUELTA_LISTADO_ENTRADAS, true);
			navegacionBean.setTxtMigaPan(Constantes.EDICION_ENTRADA + " - " + expediente.getMigaDePan()
					+ navegacionBean.expedientesRelacionadosMotivoPsan(expediente.getId()));
		} else {
			navegacionBean.setTxtMigaPan(Constantes.EDICION_EXPEDIENTE + " - " + expediente.getMigaDePan()
					+ navegacionBean.expedientesRelacionadosMotivoPsan(expediente.getId()));
		}
		return ListadoNavegaciones.FORM_EXPEDIENTES.getRegla();
	}

	public String onConsultar(Long idExp) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS,
				((Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS)) + 1);
		JsfUtils.setFlashAttribute(EDITABLE, false);
		JsfUtils.setSessionAttribute(EDITABLE, false);
		JsfUtils.setFlashAttribute("idExp", idExp);
		Expedientes expediente = expedientesService.obtener(idExp);

		if (Constantes.XPC.equals(expediente.getValorTipoExpediente().getCodigo())) {
			this.selectedNuevoTipoExpedienteId = valoresDominioService
					.findValoresDominioByCodigoDomCodValDom(Constantes.COD_TIPO_EXPEDIENTE,Constantes.XPC).getId();
			JsfUtils.setSessionAttribute(Constantes.VUELTA_LISTADO_ENTRADAS, true);
			navegacionBean.setTxtMigaPan(Constantes.CONSULTA_ENTRADA + " - " + expediente.getMigaDePan()
					+ navegacionBean.expedientesRelacionadosMotivoPsan(expediente.getId()));
		} else {
			navegacionBean.setTxtMigaPan(Constantes.CONSULTA_EXPEDIENTE + " - " + expediente.getMigaDePan()
					+ navegacionBean.expedientesRelacionadosMotivoPsan(expediente.getId()));
		}
		return ListadoNavegaciones.FORM_EXPEDIENTES.getRegla();
	}

	public void onTipAgrupRowDblClkSelect(final SelectEvent<ExpedientesMaestra> event) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS,
				((Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS)) + 1);
		JsfUtils.setFlashAttribute(EDITABLE, false);
		ExpedientesMaestra so = event.getObject();
		PrimeFaces.current().ajax().addCallbackParam("id", so.getId());
		navegacionBean.setTxtMigaPan(Constantes.CONSULTA_EXPEDIENTE + so.getMigaDePan()
				+ navegacionBean.expedientesRelacionadosMotivoPsan(so.getId()));
	}

	public void abrirExpediente(Expedientes expedienteSeleccionado, String accion) {

		if ("altaExpedientes".equals(accion)) {
			cabeceraDialogo = "Seleccionar tipo de expediente";
			limpiarFormularioExpediente();

		}

		if (expedienteSeleccionado != null) {
			nuevoExpediente = expedienteSeleccionado;
			this.selectedNuevoTipoExpedienteId = expedienteSeleccionado.getValorTipoExpediente().getId();
		}

		PrimeFaces.current().ajax().update(FORMDIALOGEXPEDIENTES);
		PrimeFaces.current().executeScript("PF('dialogExpedientes').show();");

	}

	private void limpiarFormularioExpediente() {
		nuevoExpediente = new Expedientes();
		this.selectedNuevoTipoExpedienteId = null;
		PrimeFaces.current().ajax().update(FORMDIALOGEXPEDIENTES);
		PrimeFaces.current().executeScript("PF('dialogExpedientes').hide();");
	}

}
