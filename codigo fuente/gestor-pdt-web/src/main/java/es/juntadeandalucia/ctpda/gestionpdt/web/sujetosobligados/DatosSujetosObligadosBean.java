package es.juntadeandalucia.ctpda.gestionpdt.web.sujetosobligados;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.Localidades;
import es.juntadeandalucia.ctpda.gestionpdt.model.Paises;
import es.juntadeandalucia.ctpda.gestionpdt.model.Provincias;
import es.juntadeandalucia.ctpda.gestionpdt.model.SujetosObligados;
import es.juntadeandalucia.ctpda.gestionpdt.model.SujetosObligadosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.TipoAgrupacion;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.service.ExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.LocalidadesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.PaisesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ProvinciasService;
import es.juntadeandalucia.ctpda.gestionpdt.service.SujetosObligadosExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.SujetosObligadosService;
import es.juntadeandalucia.ctpda.gestionpdt.service.TipoAgrupacionService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ValoresDominioService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.util.NIF;
import es.juntadeandalucia.ctpda.gestionpdt.util.StringUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.Constantes;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.JsfUtils;
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
@Slf4j
@ViewScoped
public class DatosSujetosObligadosBean extends BaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String MENSAJESFORMULARIO = "messagesFormulario";
	private static final String GUARDADOCORRECTAMENTE = "guardado.correctamente";
	private static final String MENSAJESUJETOSOBLIGADOS = "Organismo/Sujeto obligado ";
	private static final String MENSAJEERROR = "error";
	private static final String ACTUALIZADOCORRECTAMENTE = "actualizado.correctamente";
	private static final String ELCAMPO= "el.campo";
	private static final String EDITABLE= "editable";
	private static final String IDSUJOBLIG= "idSujOblig";
	public static final String VOLVERSUJETOS = "_volverSujetos_";

	@Getter
	@Setter
	private SujetosObligados sujetosObligados;
	@Getter
	@Setter
	private TipoAgrupacion tipoAgrupacion;
	@Getter
	@Setter
	private SujetosObligados sujetosObligadosPadre;

	@Getter
	@Setter
	private List<SujetosObligados> listaSujetosObligadosSup;

	@Getter
	@Setter
	private List<TipoAgrupacion> listaTipoAgrupacion;
	@Getter
	@Setter
	private List<Paises> listaPaises;
	@Getter
	@Setter
	private List<Provincias> listaProvincias;
	@Getter
	@Setter
	private List<Localidades> listaLocalidades;
	@Getter
	@Setter
	private List<SujetosObligados> listaSujetosObligadosAscendentes;
	@Getter
	@Setter
	private List<ValoresDominio> listaValoresDominioTipoIdentificador;	
	@Getter
	@Setter
	private List<ValoresDominio> listaValoresDominioViaComunicacion;	
	@Getter
	@Setter
	private List<ValoresDominio> listaValoresDominioTipologia;	

	@Getter
	@Setter
	@Autowired
	private SujetosObligadosService sujetosObligadosService;
	@Getter
	@Setter
	@Autowired
	private TipoAgrupacionService tipoAgrupacionService;

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
	@Getter
	@Setter
	@Autowired
	private ValoresDominioService valoresDominioService;
	
	@Getter
	@Setter
	private Long idSujetosObligadosPadre;
	@Getter
	@Setter
	private Long selectedNuevoTipAgrupId;
	@Getter
	@Setter
	private Long selectedNuevoPaisId;
	@Getter
	@Setter
	private Long selectedNuevaProvinciaId;
	@Getter
	@Setter
	private Long selectedNuevaLocalidadId;
	@Getter
	@Setter
	private Long selectedNuevoSujetoObligadoId;
	@Getter
	@Setter	
	private Long selectedTipoIdentificadorId;	
	@Getter
	@Setter	
	private Long selectedViaComunicacionId;	
	@Getter
	@Setter	
	private Long selectedTipologiaId;	

	@Getter
	@Setter
	private Boolean modoAccesoEditar;

	@Getter
	@Setter
	private Boolean modoAccesoConsulta;
	@Getter
	@Setter
	private String cabeceraDialogo;

	
	@Getter
	@Setter
	private List<String> listaCodigoPermisos;
	
	@Getter	@Setter
	private Boolean permisoSaveSujObl;
	
	@Getter	@Setter
	private Boolean permisoEditSujObl;
	
	@Getter
	@Setter
	private SujetosObligadosExpedientes sujetosObligadosExpedientes;
	
	@Getter
	@Setter
	@Autowired
	private SujetosObligadosExpedientesService sujetosObligadosExpedientesService;
	
	@Getter	@Setter
	private Boolean esSujetoObligado;
	
	@Getter	@Setter
	private Boolean esSujetoObligadoExp;
	
	@Autowired
	private VolverBean volverBean;
	
	@Autowired	
	private ContextoSujetosBean contextoSujetosBean;	
	
	@Getter @Setter
	private Long idSujObligInit;
	
	@Getter @Setter
	private Long idSujObligExpInit;
	
	@Getter @Setter
	private String errorDpd;
	
	@Getter @Setter
	private String errorUnidadTransparencia;

	@Autowired
	private NavegacionBean navegacionBean;
	
	@Autowired
	private ExpedientesService expedientesService;
	
	@Getter @Setter
	private Integer numerosSaltos;
	
	@PostConstruct
	@Override
	public void init() {
		super.init();
		
		numerosSaltos = (Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS);
		
		esSujetoObligado=false;
		esSujetoObligadoExp=false;

		/**
		 * GESTION DE PERMISOS
		 * */
		listaCodigoPermisos = (List<String>) JsfUtils.getSessionAttribute(Constantes.LISTACODIGOPERMISOS);				
		permisoSaveSujObl = listaCodigoPermisos.contains(Constantes.PERMISO_SAVE_SUJOBL);
		permisoEditSujObl = listaCodigoPermisos.contains(Constantes.PERMISO_EDIT_SUJOBL);
				
		FacesUtils.setAttribute(EDITABLE, JsfUtils.getFlashAttribute(EDITABLE));

		Sort sortListaPaises = Sort.by(new Sort.Order(Direction.ASC, "descripcion"));
		listaPaises = (List<Paises>) paisesService.findAllRepository(sortListaPaises);
		listaValoresDominioTipoIdentificador = valoresDominioService.findValoresTipoIdentificador();
		listaValoresDominioViaComunicacion = valoresDominioService.findValoresViaComunicacion();
		listaValoresDominioTipologia = valoresDominioService.findValoresTipologia();
		
		cargaInicialRecogerContexto();
		
		cargaInicialSujObligExp();
		
		if (idSujObligInit != null) {
			esSujetoObligado=true;
			
			sujetosObligados = sujetosObligadosService.obtener(idSujObligInit);

			if(null != sujetosObligados.getPais()) {
				selectedNuevoPaisId = sujetosObligados.getPais().getId();
			}
			
			cargaCPostal();
			
			if(null != sujetosObligados.getProvincia()) {
				selectedNuevaProvinciaId=sujetosObligados.getProvincia().getId();
				recargaLocalidades();
			}
			if(null != sujetosObligados.getLocalidad()) {
				selectedNuevaLocalidadId = sujetosObligados.getLocalidad().getId();
			}

			sujetosObligadosPadre = sujetosObligados.getSujetosObligadosPadre();
			tipoAgrupacion = sujetosObligados.getTipoAgrupacion();
			listaTipoAgrupacion = (List<TipoAgrupacion>) tipoAgrupacionService.findAll();

			cargaSeleccionListados();
			
			listaSujetosObligadosAscendentes = obtenerSujetosObligadosAscendentes(sujetosObligados);

		} else if(idSujObligExpInit == null) {
			sujetosObligados = new SujetosObligados();
			sujetosObligados.setUnidadTransparencia(false);
			sujetosObligados.setDpd(false);
			
			listaSujetosObligadosAscendentes = null;
			listaTipoAgrupacion = tipoAgrupacionService.findTiposAgrupacionesActivaSinAnidamiento();
			
			listaProvincias = Collections.emptyList();
			listaLocalidades = Collections.emptyList();
			selectedNuevaProvinciaId = null;
			selectedNuevaLocalidadId = null;
			Optional<Paises> op = listaPaises.stream().filter(p -> p.getCodigo().equals("ES")).findFirst();
			selectedNuevoPaisId = op.isPresent()? op.get().getId() : null;
		}
		
		//Comparto el objeto
		contextoSujetosBean.setSujetosObligados(sujetosObligados);
		
		listaSujetosObligadosSup = sujetosObligadosService.findSujetosObligadosActivos();		

	}
	
	private void cargaInicialSujObligExp () {
		if (idSujObligExpInit != null) {
			esSujetoObligadoExp=true;
			
			sujetosObligadosExpedientes = sujetosObligadosExpedientesService.obtener(idSujObligExpInit);
			SujetosObligados s= sujetosObligadosExpedientes.getSujetosObligados();
			Long idS=s.getId();
			sujetosObligados = sujetosObligadosService.obtener(idS);			
			if(null != sujetosObligados.getPais()) {
				selectedNuevoPaisId = sujetosObligados.getPais().getId();
			}
			
			cargaCPostal();
			
			if(null != sujetosObligados.getProvincia()) {
				selectedNuevaProvinciaId=sujetosObligados.getProvincia().getId();
				recargaLocalidades();
			}
			if(null != sujetosObligados.getLocalidad()) {
				selectedNuevaLocalidadId = sujetosObligados.getLocalidad().getId();
			}

			sujetosObligadosPadre = sujetosObligados.getSujetosObligadosPadre();
			tipoAgrupacion = sujetosObligados.getTipoAgrupacion();
			Sort sortListaTipoAgrupacion = Sort.by(new Sort.Order(Direction.ASC, "descripcion")); 
			listaTipoAgrupacion = (List<TipoAgrupacion>) tipoAgrupacionService.findAllRepository(sortListaTipoAgrupacion);

			cargaSeleccionListados();
			
			listaSujetosObligadosAscendentes = obtenerSujetosObligadosAscendentes(sujetosObligados);
		}
	}
	
	private void cargaInicialRecogerContexto () {
		ContextoVolver cp = volverBean.recogerContexto(VOLVERSUJETOS);		
		if(cp != null) {
			idSujObligInit = (Long) cp.get(IDSUJOBLIG);
			idSujObligExpInit= (Long) cp.get("idSujObligExp");
			this.setFormEditable((boolean)cp.get(EDITABLE));
		}else {
			idSujObligInit = (Long) JsfUtils.getFlashAttribute(IDSUJOBLIG);
			idSujObligExpInit = (Long) JsfUtils.getFlashAttribute("idSujObligExp");
		}
		
		ContextoVolver cv = volverBean.recogerContexto();
		if(cv != null) {
			if(idSujObligExpInit!=null) {
				FacesUtils.setAttribute(EDITABLE, Boolean.FALSE);
			}else {
				FacesUtils.setAttribute(EDITABLE, Boolean.TRUE);
			}			
		} else {
			FacesUtils.setAttribute(EDITABLE, JsfUtils.getFlashAttribute(EDITABLE));
		}
	}
	
	private void cargaSeleccionListados () {
		if (tipoAgrupacion != null) {
			this.selectedNuevoTipAgrupId = tipoAgrupacion.getId();
		}

		if (sujetosObligadosPadre != null) {
			this.selectedNuevoSujetoObligadoId = sujetosObligadosPadre.getId();
		}
		
		if(null!= sujetosObligados.getValorTipoIdentificador()) {
			selectedTipoIdentificadorId = sujetosObligados.getValorTipoIdentificador().getId();
		}

		if(null!= sujetosObligados.getValorViaComunicacion()) {
			selectedViaComunicacionId = sujetosObligados.getValorViaComunicacion().getId();
		}
		
		if(null!= sujetosObligados.getValorTipologia()) {
			selectedTipologiaId = sujetosObligados.getValorTipologia().getId();
		}
	}

	public void onChangeTipoIdentificador() {
		if(null == selectedTipoIdentificadorId) {
			this.sujetosObligados.setNif(null);
		}
	}
	
	public void onChangeCodigoPostal() {
		cargaCPostal();
		//Dejo esto fuera de cargaCPostal
		//Si solo tengo una provincia (lo más probable) me quedo con ella
		if (1==listaProvincias.size()) {
			selectedNuevaProvinciaId = listaProvincias.get(0).getId();
			recargaLocalidades();
		}

	}
	
	private void cargaCPostal() {
		listaProvincias = (List<Provincias>) provinciasService.findAllByCPostal(sujetosObligados.getCodigoPostal());
		listaLocalidades = Collections.emptyList();
		selectedNuevaProvinciaId = null; 
		selectedNuevaLocalidadId = null;
	}
	
	public void onChangeProvincia() {
		recargaLocalidades();
	}
	
	public void recargaLocalidades() {
		listaLocalidades = (List<Localidades>) localidadesService.findAllByIdProvincia(selectedNuevaProvinciaId);
		selectedNuevaLocalidadId = null;
	}

	public boolean validacionesGuardar() throws BaseException {
		boolean valido = true;
		String mensajeFinal = "";
		String errorSujetoExistente = "";
		String errorIdentificadorNoValido = "";
		String errorEmailNoValido = "";
		String errorTipoIdentificadorRelleno = "";
		errorUnidadTransparencia="";
		errorDpd="";

		
		// validacion datos obligatorios
		boolean datosObligatorios = validacionesGuardarDatosObligatorios();
		if(!datosObligatorios) {
			return false;
		}

		// validación descripción única
		SujetosObligados sujetoByDescripcion = sujetosObligadosService.obtenerSujetosObligadosConDescripcion(sujetosObligados.getDescripcion());
		if(sujetoByDescripcion != null && ((sujetosObligados.getId() == null) || (sujetosObligados.getId() != null && !sujetoByDescripcion.getId().equals(sujetosObligados.getId())))) {
			valido = false;
			errorSujetoExistente = mensajesProperties.getString(ELCAMPO) + " " + "Organismo/Sujeto obligado" + " " + mensajesProperties.getString("existe.sistema");								
		}			
		
		if(null != selectedTipoIdentificadorId && !StringUtils.isBlank(sujetosObligados.getNif())) {
			final String tipoIden = valoresDominioService.getCodigoTipoIdentificador(listaValoresDominioTipoIdentificador,selectedTipoIdentificadorId);
			
			//Tengo asegurado que no es null
			sujetosObligados.setNif(sujetosObligados.getNif().toUpperCase());
			
			if (tipoIden != null && !NIF.esIdentificadorValido(tipoIden, sujetosObligados.getNif())) {
				valido = false;
				errorIdentificadorNoValido = getMessages(ELCAMPO, "identificador", "no.valido");
			}
		}
		
		if(StringUtils.isBlank(sujetosObligados.getNif()) && selectedTipoIdentificadorId != null) {
			valido = false;
			errorTipoIdentificadorRelleno = "Debe rellenar el campo identificador para el tipo de identificador seleccionado.";
		}


		String email = sujetosObligados.getEmailContacto();
		//validación email correcto
		if(!StringUtils.esEmailValido(email)) {
			valido = false;
			errorEmailNoValido = getMessage(ELCAMPO) + " " + getMessage("email") + " " + getMessage("no.valido");
		}
		
		//validacion unidadTransparencia	
		valido = validacionesGuardarUnidadTransparencia(valido);
		
		//validacion dpd
//		valido = validacionesGuardarDPD(valido);
		
		mensajeFinal (mensajeFinal, errorSujetoExistente, errorIdentificadorNoValido, errorEmailNoValido, errorTipoIdentificadorRelleno,errorUnidadTransparencia,errorDpd);
		
		return valido;
	}
	
	private boolean validacionesGuardarUnidadTransparencia (boolean valido) {
		if(sujetosObligados.getUnidadTransparencia()!=null && !sujetosObligados.getUnidadTransparencia() && (!sujetosObligados.getContactoUnidadTransparencia().isEmpty() || !sujetosObligados.getContactoUnidadTransparencia().isBlank())) {
			valido=false;
			errorUnidadTransparencia= getMessage(ELCAMPO) + " " + getMessage("contacto.unidad.transparencia") + " " + getMessage("debe.vacio");
		}
		return valido;
	}
	
	private boolean validacionesGuardarDPD(boolean valido) {
		if(sujetosObligados.getDpd()!=null && !sujetosObligados.getDpd() && (!sujetosObligados.getContactoDpd().isEmpty() || !sujetosObligados.getContactoDpd().isBlank())) {
			valido=false;
			errorDpd= getMessage(ELCAMPO) + " " + getMessage("contacto.dpd") + " " + getMessage("debe.vacio");
		}
		
		return valido;
	}
	
	private boolean validacionesGuardarDatosObligatorios() {
		boolean res = true;
		if (StringUtils.isBlank(sujetosObligados.getDescripcion())
				|| StringUtils.isBlank(sujetosObligados.getAbreviatura1()) 
				|| StringUtils.isBlank(sujetosObligados.getAbreviatura2()) 
				|| StringUtils.isBlank(sujetosObligados.getAbreviatura3())
				|| StringUtils.isBlank(sujetosObligados.getOrdenVisualizacion()) || selectedNuevoTipAgrupId == null) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString("campos.obligatorios"));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			res = false;
		}
		return res;
	}
	
	private void mensajeFinal (String mensajeFinal, String errorSujetoExistente, String errorIdentificadorNoValido, String errorEmailNoValido, String errorTipoIdentificadorRelleno, String errorUnidadTransparencia,String errorDpd) {
		if(!errorSujetoExistente.isBlank()) {
			mensajeFinal = errorSujetoExistente;
		} 
		if(!errorIdentificadorNoValido.isBlank()){
			mensajeFinal += "\n"+errorIdentificadorNoValido;
		}
		if(!errorTipoIdentificadorRelleno.isBlank()) {
			mensajeFinal += "\n"+errorTipoIdentificadorRelleno;
		}
		if(!errorEmailNoValido.isBlank()) {
			mensajeFinal += "\n"+errorEmailNoValido;
		}
		if(!errorUnidadTransparencia.isBlank()) {
			mensajeFinal += "\n"+errorUnidadTransparencia;
		}
		if(!errorDpd.isBlank()) {
			mensajeFinal += "\n"+errorDpd;
		}
		
		if(!mensajeFinal.isBlank()) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajeFinal);
			PrimeFaces.current().dialog().showMessageDynamic(message);
		}
	}
	
	public void saveSujetosObligados() {
		try {
			boolean puedoGuardar = validacionesGuardar();			
			if (puedoGuardar) {
				// ALTA SUJETOS OBLIGADOS
				
				elementosListadosSeleccionados();
								
				if (sujetosObligados.getId() == null) {
					sujetosObligados.setActiva(true);
					FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "",
									MENSAJESUJETOSOBLIGADOS +" "+sujetosObligados.getDescripcion()+" "+ mensajesProperties.getString(GUARDADOCORRECTAMENTE)));
					// MODIFICACION SUJETOS OBLIGADOS

				} else if (sujetosObligados.getId() != null) {
					FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "",
									MENSAJESUJETOSOBLIGADOS +" "+sujetosObligados.getDescripcion()+" "+ mensajesProperties.getString(ACTUALIZADOCORRECTAMENTE)));
				}

				if (sujetosObligados.getNivelAnidamiento() == null) {
					sujetosObligados.setNivelAnidamiento(0L);
				}

				sujetosObligados.setTipoAgrupacion(tipoAgrupacionService.obtener(this.selectedNuevoTipAgrupId));
				if (this.selectedNuevoSujetoObligadoId == null || this.selectedNuevoSujetoObligadoId == 0L) {
					sujetosObligados.setSujetosObligadosPadre(null);
				} else {
					sujetosObligados.setSujetosObligadosPadre(
							sujetosObligadosService.obtener(this.selectedNuevoSujetoObligadoId));
				}
				
				
				sujetosObligados.setErroneo(false);
				sujetosObligadosService.guardar(sujetosObligados);

				listaSujetosObligadosAscendentes = obtenerSujetosObligadosAscendentes(sujetosObligados);
				listaSujetosObligadosSup = sujetosObligadosService.findSujetosObligadosActivos();

				modoAccesoConsulta = true;

				PrimeFaces.current().ajax().update("formListadoSujetosObligados");
				
				navegacionBean.setTxtMigaPan(Constantes.EDICION_SUJETOOBLIGADO+sujetosObligados.getDescripcion());
				PrimeFaces.current().ajax().update("textoMigaPan");
			}
		} catch (final BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}

	}
	
	private void elementosListadosSeleccionados () {
		if (selectedNuevoPaisId != null) {
			sujetosObligados.setPais(paisesService.obtener(selectedNuevoPaisId));
		} else {
			sujetosObligados.setPais(null);
		}
		if (selectedNuevaProvinciaId != null) {
			sujetosObligados.setProvincia(provinciasService.obtener(selectedNuevaProvinciaId));
		} else {
			sujetosObligados.setProvincia(null);
		}
		if (selectedNuevaLocalidadId != null) {
			sujetosObligados.setLocalidad(localidadesService.obtener(selectedNuevaLocalidadId));
		} else {
			sujetosObligados.setLocalidad(null);
		}
		if(selectedTipoIdentificadorId!=null) {
			sujetosObligados.setValorTipoIdentificador(valoresDominioService.obtener(selectedTipoIdentificadorId));
		}else {
			sujetosObligados.setValorTipoIdentificador(null);
		}
		if(selectedViaComunicacionId!=null) {
			sujetosObligados.setValorViaComunicacion(valoresDominioService.obtener(selectedViaComunicacionId));
		}else {
			sujetosObligados.setValorViaComunicacion(null);
		}
		if(selectedTipologiaId!=null) {
			sujetosObligados.setValorTipologia(valoresDominioService.obtener(selectedTipologiaId));
		}else {
			sujetosObligados.setValorTipologia(null);
		}
	}

	public void modoAccesoFormulario(SujetosObligados sujetosObligadosSeleccionado, String modoAcceso) {
		if (modoAcceso.contains("consulta")) {
			modoAccesoConsulta = true;
			modoAccesoEditar = false;
			cabeceraDialogo = "Consulta organismo/sujeto obligado";
		} else if (modoAcceso.contains("editar")) {
			modoAccesoConsulta = false;
			modoAccesoEditar = true;
			cabeceraDialogo = "Edición organismo/sujeto obligado";
		}

		if (sujetosObligadosSeleccionado != null && sujetosObligadosSeleccionado.getId() != null) {
			sujetosObligados = sujetosObligadosSeleccionado;

			if (sujetosObligadosSeleccionado.getSujetosObligadosPadre() != null)
				this.selectedNuevoSujetoObligadoId = sujetosObligados.getSujetosObligadosPadre().getId();
			else
				this.selectedNuevoSujetoObligadoId = null;

		}
		PrimeFaces.current().executeScript("PF('dialogSujetosObligados').show();");
	}

	public List<SujetosObligados> obtenerSujetosObligadosAscendentes(SujetosObligados sujetosObligadosSeleccionado) {
		List<SujetosObligados> listaSujObliAscendentes = new ArrayList<>();
		SujetosObligados sujObliPadre = null;
		SujetosObligados sujObliHijo = sujetosObligadosSeleccionado;

		sujObliPadre = sujObliHijo.getSujetosObligadosPadre();

		while (sujObliPadre != null && sujObliPadre.getActiva()) {
			listaSujObliAscendentes.add(sujObliPadre);
			sujObliPadre = sujObliPadre.getSujetosObligadosPadre();
		}

		return listaSujObliAscendentes;
	}

	public void calcularNivelAnidamiento() {
		SujetosObligados sujetosObligadosPadreAux = null;
		Long nivelAnidamientoNuevo = 0L;

		if (selectedNuevoSujetoObligadoId != null) {
			sujetosObligadosPadreAux = sujetosObligadosService.obtener(selectedNuevoSujetoObligadoId);
			nivelAnidamientoNuevo = sujetosObligadosPadreAux.getNivelAnidamiento() + 1;
			sujetosObligados.setNivelAnidamiento(nivelAnidamientoNuevo);
		} else {
			sujetosObligados.setNivelAnidamiento(null);
		}

		if(nivelAnidamientoNuevo > 5)
		{
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString("sujeto.oblig.nivel.anidamiento"));
			PrimeFaces.current().dialog().showMessageDynamic(message);
		}
		
		PrimeFaces.current().ajax().update("formFormularioSujetosObligados:nivelAnidamiento");

	}

	public void controlTipoAgrupaciones() {

		if (selectedNuevoSujetoObligadoId == null) {
			listaTipoAgrupacion= tipoAgrupacionService.findTiposAgrupacionesActivaSinAnidamiento();

		} else {
			SujetosObligados sujetosObligadosPadreAux2 = sujetosObligadosService.obtener(selectedNuevoSujetoObligadoId);
			TipoAgrupacion tipAgrupacionSujetoSeleccionado = null;
			tipAgrupacionSujetoSeleccionado = sujetosObligadosPadreAux2.getTipoAgrupacion();
			
			listaTipoAgrupacion=tipoAgrupacionService.findTiposAgrupacionesHijasActivas(tipAgrupacionSujetoSeleccionado.getId());

		}

		

		PrimeFaces.current().ajax().update("formFormularioSujetosObligados:comboTipoAgrupacion");

	}
	
	public String onEditarByForm(Long idSujOblig) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, numerosSaltos+1);
		JsfUtils.setFlashAttribute(EDITABLE, true);
		JsfUtils.setFlashAttribute(IDSUJOBLIG, idSujOblig);
		SujetosObligados sujetosObligadosEdit = sujetosObligadosService.obtener(idSujOblig);
		navegacionBean.setTxtMigaPan(Constantes.EDICION_SUJETOOBLIGADO+sujetosObligadosEdit.getDescripcion());
		return ListadoNavegaciones.FORM_SUJETOS_OBLIGADOS.getRegla();
	}
	
	public String ejecutarVolver() {
		ContextoVolver contexto = volverBean.getContexto();
		if(contexto != null && contexto.getVista().contains("formFormularioExpedientes")) {
			Expedientes expediente = expedientesService.obtener((Long) contexto.get("idExp"));
			volverBean.migaPanVolver(Constantes.EDICION_EXPEDIENTE+expediente.getMigaDePan()+navegacionBean.expedientesRelacionadosMotivoPsan(expediente.getId()));
		}else {
			volverBean.migaPanVolver(Constantes.LISTADO_SUJETOSOBLIGADOS);
		}
		
		return volverBean.onVolver(ListadoNavegaciones.LISTADO_SUJETOS_OBLIGADOS.getRegla());
	}
	

}
