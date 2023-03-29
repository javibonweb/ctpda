/*
 * Copyright 2016-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.juntadeandalucia.ctpda.gestionpdt.web.personas;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.Personas;
import es.juntadeandalucia.ctpda.gestionpdt.service.PersonasService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.Constantes;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.JsfUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.LazyDataModelByQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.MyFilterMeta;
import es.juntadeandalucia.ctpda.gestionpdt.web.menu.NavegacionBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.menu.NavegacionBean.ListadoNavegaciones;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.BaseBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@ViewScoped
@Slf4j
public class PersonasBean extends BaseBean implements Serializable {

	@Autowired
	private PersonasService personasService;
	
	private static final long serialVersionUID = 1L;
	
	private static final String MENSAJESLISTADO = "messagesListado";
	private static final String MENSAJEPERSONA= "Persona ";
	private static final String MENSAJEERROR= "error";
	private static final String ELIMINADOCORRECTAMENTE = "eliminada.correctamente";
	private static final String EDITABLE= "editable";

	@Getter
	private LazyDataModelByQueryService<Personas> lazyModel;
	@Getter
	@Setter
	private Personas selectedPersonas;	
	
	@Getter
	@Setter
	private Long idPersonaSeleccionada;	
	
	@Getter
	@Setter
	private Personas personasBorrar;
	
	@Getter
	@Setter
	private String nombreRazonSocialFiltro;
	
	@Getter
	@Setter
	private String primerApellidoFiltro;
	
	@Getter
	@Setter
	private String segundoApellidoFiltro;
	
	@Getter
	@Setter
	private String nifCifFiltro;
	

	@Getter
	@Setter
	private List<String> listaCodigoPermisos;
	
	@Getter	@Setter
	private Boolean permisoNewPers;
	
	@Getter	@Setter
	private Boolean permisoConsPers;
	
	@Getter	@Setter
	private Boolean permisoDesacPers;

	
	@Getter	@Setter
	private Boolean permisoEditPers;
	
	@Autowired
	private NavegacionBean navegacionBean;
	
	/**
	* Initialize default attributes.
	 * @throws BaseFrontException 
	*/
	@PostConstruct
	@Override
	public void init(){
		super.init();
		
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, 0);
		
		navegacionBean.setTxtMigaPan(Constantes.LISTADO_PERSONAS);
		
		listaCodigoPermisos = (List<String>) JsfUtils.getSessionAttribute(Constantes.LISTACODIGOPERMISOS);
		
		permisoNewPers = listaCodigoPermisos.contains(Constantes.PERMISO_NEW_PERS);
		
		permisoConsPers = listaCodigoPermisos.contains(Constantes.PERMISO_CONS_PERS);
		
		permisoDesacPers = listaCodigoPermisos.contains(Constantes.PERMISO_DESAC_PERS);

		permisoEditPers = listaCodigoPermisos.contains(Constantes.PERMISO_EDIT_PERS);

		lazyModel= new LazyDataModelByQueryService<>(Personas.class,personasService);	
		lazyModel.setPreproceso((a,b,c,filters)->{
			if (nombreRazonSocialFiltro != null && !nombreRazonSocialFiltro.isEmpty()){
				filters.put("nombreRazonsocial", new MyFilterMeta(nombreRazonSocialFiltro));				
			}
			if (primerApellidoFiltro != null && !primerApellidoFiltro.isEmpty()){
				filters.put("primerApellido", new MyFilterMeta(primerApellidoFiltro));				
			}
			if (segundoApellidoFiltro != null && !segundoApellidoFiltro.isEmpty()){
				filters.put("segundoApellido", new MyFilterMeta(segundoApellidoFiltro));				
			}
			if (nifCifFiltro != null && !nifCifFiltro.isEmpty()){
				filters.put("nifCif", new MyFilterMeta(nifCifFiltro));				
			}
			
			filters.put("activa", new MyFilterMeta("true"));
			
		});
		
	}
	
	public String redireccionMenu() {
		init();
		return ListadoNavegaciones.LISTADO_PERSONAS.getRegla();
	}
	
	public void limpiarFiltro () {
		nombreRazonSocialFiltro = "";
		primerApellidoFiltro = "";
		segundoApellidoFiltro = "";
		nifCifFiltro = "";
	}

	public void	ejecutarComando(int numero) {
		log.info("BOTON PULSADO "+numero);
	}
	
	//*****************************************************************
	
	
	public String onCrear() {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, ((Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS))+1);
		//No se ha enviado id, la persona viene inicializada por defecto.
		JsfUtils.setFlashAttribute(EDITABLE, true);
		navegacionBean.setTxtMigaPan(Constantes.ALTA_PERSONA);
		return ListadoNavegaciones.FORM_PERSONAS.getRegla();
	}
	public String onEdita(Long idPersona) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, ((Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS))+1);
		//Se ha enviado id, la persona viene de BD.
		JsfUtils.setFlashAttribute(EDITABLE, true);
		JsfUtils.setFlashAttribute("idPersona", idPersona);
		Personas personas = personasService.obtener(idPersona);
		navegacionBean.setTxtMigaPan(Constantes.EDICION_PERSONA+personas.getNifCif());
		return ListadoNavegaciones.FORM_PERSONAS.getRegla();
	}
	
	public String onConsulta(Long idPersona) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, ((Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS))+1);
		JsfUtils.setFlashAttribute(EDITABLE, false);
		JsfUtils.setFlashAttribute("idPersona", idPersona);
		Personas personas = personasService.obtener(idPersona);
		navegacionBean.setTxtMigaPan(Constantes.CONSULTA_PERSONA+personas.getNifCif());
		return ListadoNavegaciones.FORM_PERSONAS.getRegla();
	}
	
	public void onPersRowDblClkSelect(final SelectEvent<Personas> event) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, ((Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS))+1);
		JsfUtils.setFlashAttribute(EDITABLE, false);
		Personas p = event.getObject();
		PrimeFaces.current().ajax().addCallbackParam("id", p.getId());
		navegacionBean.setTxtMigaPan(Constantes.CONSULTA_PERSONA+p.getNifCif());
	}
		
	//**************************************************************
	
	public String deletePersonas(Personas personasBorrar) {
		try {
			//Borrado lógico
			this.personasService.desactivar(personasBorrar);
			FacesContext.getCurrentInstance().addMessage(MENSAJESLISTADO, new FacesMessage(FacesMessage.SEVERITY_INFO, "", MENSAJEPERSONA +" "+ personasBorrar.getNombreAp()+" " + mensajesProperties.getString(ELIMINADOCORRECTAMENTE)));
			PrimeFaces.current().ajax().update(":formBuscador:tablaPersona");
		} catch (final BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());			
		}

		return null;
	}
	
}
