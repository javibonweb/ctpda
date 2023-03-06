package es.juntadeandalucia.ctpda.gestionpdt.web.personas;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.apache.commons.lang3.BooleanUtils;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.Personas;
import es.juntadeandalucia.ctpda.gestionpdt.model.PersonasRepresentantes;
import es.juntadeandalucia.ctpda.gestionpdt.service.PersonasRepresentantesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.PersonasService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.util.StringUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.Constantes;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.JsfUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.LazyDataModelByQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.MyFilterMeta;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.BaseBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.FacesUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@ViewScoped
public class DatosPersonasRepresentantesBean extends BaseBean implements Serializable {

	private static final String ANYADIDOCORRECTAMENTE = "anyadido.correctamente";
	private static final String MENSAJEREPRE = "Representante legal ";
	private static final String ACTUALIZADOCORRECTAMENTE = "actualizado.correctamente";
	private static final String ELIMINADOCORRECTAMENTE = "eliminado.correctamente";
	private static final String MENSAJESREPRES = "messagesFormularioListadoPersonasRepresentantes";
	private static final String TABLAREPRESENTANTES = "formFormulario:tablaPersonasRepresentantes";
	private static final String MENSAJEERROR = "error";


	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private Personas persona;
	@Getter
	@Setter
	private PersonasRepresentantes personaRepresentante;
	
	@Autowired
	private PersonasRepresentantesService personasRepresentantesService;
	@Autowired
	private PersonasService personasService;

	@Getter
	private LazyDataModelByQueryService<PersonasRepresentantes> lazyModelPersonasRepresentantes;
	@Getter
	private LazyDataModelByQueryService<Personas> lazyModelNuevosRepresentantes;

	@Getter
	@Setter
	private String nombreRazonSocialFiltro;
	@Getter
	@Setter
	private String nifCifFiltro;
	
	@Getter
	@Setter
	boolean soloLectura;
	
	@Autowired	
	private ContextoPersonasBean contextoPersonasBean;	
	
	@Getter
	@Setter
	private List<String> listaCodigoPermisos;

	@Getter	@Setter
	private Boolean permisoBusPersRep;
	
	@Getter	@Setter
	private Boolean permisoDelPersRep;
	
	@Getter	@Setter
	private Boolean esUnicoRepresentanteLegal;
	@Getter	@Setter
	private PersonasRepresentantes personaRepreSeleccionada;
	
	@Getter @Setter
	private Integer numerosSaltos;
	
	@PostConstruct
	@Override
	public void init() {
		super.init();
		
		numerosSaltos = (Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS);
		
		/**
		 * GESTION DE PERMISOS
		 * */
		listaCodigoPermisos = (List<String>) JsfUtils.getSessionAttribute(Constantes.LISTACODIGOPERMISOS);
		
		permisoBusPersRep = listaCodigoPermisos.contains(Constantes.PERMISO_BUS_PERSREP);
		
		permisoDelPersRep = listaCodigoPermisos.contains(Constantes.PERMISO_DEL_PERSREP);
		
		
		persona = contextoPersonasBean.getPersonas();
					
		lazyModelPersonasRepresentantes = new LazyDataModelByQueryService<>(PersonasRepresentantes.class,personasRepresentantesService);
		lazyModelPersonasRepresentantes.setPreproceso((a, b, c, filters) -> {
			if(persona.getId() != null) {
				filters.put("persona.id", new MyFilterMeta(persona.getId()));	
			}
		});
		
		lazyModelNuevosRepresentantes = new LazyDataModelByQueryService<>(Personas.class,personasService);
		lazyModelNuevosRepresentantes.setPreproceso((a, b, c, filters) -> {
			if (!StringUtils.isBlank(nombreRazonSocialFiltro)){
				filters.put("#nombreAp", new MyFilterMeta(nombreRazonSocialFiltro));				
			}
			
			if (!StringUtils.isBlank(nifCifFiltro)) {
				filters.put("nifCif", new MyFilterMeta(nifCifFiltro));
			}
			
			if(persona!=null && null!=persona.getId()) {
				filters.put("#notID", new MyFilterMeta(persona.getId()));
				
				List<Long> idsRepre = listaIdsRepresentantes(persona.getId());
				if(!idsRepre.isEmpty()) {
					filters.put("#notRepre", new MyFilterMeta(idsRepre));
				}
			}
			
		});
		
		if(persona.getId() != null) {
			Boolean situacionEditable = Boolean.TRUE; //Pendiente de definir
			FacesUtils.setAttribute("editable", BooleanUtils.toBoolean((Boolean)JsfUtils.getFlashAttribute("editable")) && situacionEditable );
		}
		nifCifFiltro= contextoPersonasBean.getCifNifRepresentante();

		
	}
	
	private void actualizarRepresentantes() {
		PrimeFaces.current().ajax().update(TABLAREPRESENTANTES);	
	}
	
	public void abrirBusquedaRepresentantes() {
		PrimeFaces.current().ajax().update("formFormulario:tablaNuevosRepresentantes");	
		PrimeFaces.current().ajax().update("formFormulario:dialogBuscarRepresentantes");
		PrimeFaces.current().executeScript("PF('dialogBuscarRepresentantes').show();");
	}
	
	private List<Long> listaIdsRepresentantes(Long idExp){
		Collection<PersonasRepresentantes> col = this.personasRepresentantesService.findRepresentantes(idExp);
		return col.stream().map(PersonasRepresentantes::getRepresentante).map(Personas::getId).collect(Collectors.toList());
	}

    public void buscarNuevosRepresentantes() {
    	//nada
    }

	public void limpiarFiltro() {
		nombreRazonSocialFiltro = "";
		nifCifFiltro = "";
	}

	public void asignarRepresentante(Personas nuevoRepresentante) {		
		try {
			Personas nuevoRepresentante2 = personasService.obtener(nuevoRepresentante.getId());
			personaRepresentante = new PersonasRepresentantes();
			personaRepresentante.setPersona(persona);
			personaRepresentante.setRepresentante(nuevoRepresentante2);
			
			personasRepresentantesService.guardar(personaRepresentante);
			
			// Si ha ido bien se muestra el mensaje
			FacesContext.getCurrentInstance().addMessage(MENSAJESREPRES,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "",
							MENSAJEREPRE+" "+ nuevoRepresentante.getNombreAp()+" " + getMessage(ANYADIDOCORRECTAMENTE)));

			actualizarRepresentantes();
			PrimeFaces.current().executeScript("PF('dialogBuscarRepresentantes').hide();");
		} catch (final BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
	}
		
	public void cambioCheckPrincipal(PersonasRepresentantes personaRepSeleccionada) {
		if (Boolean.FALSE.equals(personaRepSeleccionada.getPrincipal())) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", getMessage("representante.principal"));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			personaRepSeleccionada.setPrincipal(true);
		} else {
			try {
				personasRepresentantesService.setRepresentantePrincipal(personaRepSeleccionada);
				FacesContext.getCurrentInstance().addMessage(MENSAJESREPRES,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "",
								MENSAJEREPRE +" "+ personaRepSeleccionada.getRepresentante().getNombreAp()+" " + getMessage(ACTUALIZADOCORRECTAMENTE)));
			} catch (final BaseException e) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MENSAJEERROR));
				PrimeFaces.current().dialog().showMessageDynamic(message);
				log.error(e.getMessage());
			}
		}
		
		actualizarRepresentantes();
	}


	
	public void eliminarRepresentante(PersonasRepresentantes rep) {
		personaRepreSeleccionada=rep;
		
		List<PersonasRepresentantes> listaPersonasRepre= lazyModelPersonasRepresentantes.getList();
		Integer tamanyoLista= listaPersonasRepre.size();
	
		if(Boolean.TRUE.equals(rep.getPrincipal()) && (tamanyoLista>1)) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", getMessage("representante.no.puede.eliminar"));
			PrimeFaces.current().dialog().showMessageDynamic(message);

		} else if(Boolean.TRUE.equals(rep.getPrincipal()) && (tamanyoLista==1) ){		
			PrimeFaces.current().executeScript("PF('eliminarRepreUnicoVar').show()");
		}else if(Boolean.FALSE.equals(rep.getPrincipal())) {
			
			try {		
				this.personasRepresentantesService.delete(rep.getId());
				FacesContext.getCurrentInstance().addMessage(MENSAJESREPRES,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "",
								MENSAJEREPRE +" "+ personaRepreSeleccionada.getRepresentante().getNombreAp()+" " + getMessage(ELIMINADOCORRECTAMENTE)));
				
				actualizarRepresentantes();
			} catch (final BaseException e) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MENSAJEERROR));
				PrimeFaces.current().dialog().showMessageDynamic(msg);
				log.error(e.getMessage());
			}
		}
	}
	
	public void eliminarRepresentanteUnico() {
		
		try {		
			this.personasRepresentantesService.delete(personaRepreSeleccionada.getId());
			FacesContext.getCurrentInstance().addMessage(MENSAJESREPRES,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "",
							MENSAJEREPRE +" "+ personaRepreSeleccionada.getRepresentante().getNombreAp()+" " + getMessage(ELIMINADOCORRECTAMENTE)));
			
			actualizarRepresentantes();
		} catch (final BaseException e) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(msg);
			log.error(e.getMessage());
		}
		
		PrimeFaces.current().executeScript("PF('eliminarRepreUnicoVar').hide()");
	}
	
	
	
	public void ocultarEliminarRepreUnicoVar() {
		PrimeFaces.current().executeScript("PF('eliminarRepreUnicoVar').hide()");
	}

}
