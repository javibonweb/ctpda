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

package es.juntadeandalucia.ctpda.gestionpdt.web.formaciongrs;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;


import org.primefaces.PrimeFaces;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.FormacionPruebasGRS;
import es.juntadeandalucia.ctpda.gestionpdt.model.Usuario;
import es.juntadeandalucia.ctpda.gestionpdt.service.FormacionPruebasGRSService;
import es.juntadeandalucia.ctpda.gestionpdt.service.UsuarioService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.JsfUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.LazyDataModelByQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.MyFilterMeta;
import es.juntadeandalucia.ctpda.gestionpdt.web.menu.NavegacionBean.ListadoNavegaciones;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.BaseBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@ViewScoped
@Slf4j
public class FormacionGRSBean extends BaseBean implements Serializable {


	
	private static final long serialVersionUID = 1L;
	
	private static final String MODOACCESO = "modoAcceso";
	private static final String EDITABLE = "editable";
	
	
	@Getter
	private LazyDataModelByQueryService<FormacionPruebasGRS> lazyModel;
	
	@Autowired
	private FormacionPruebasGRSService formacionPruebasGRSService;

	@Getter
	private  SortMeta ordenGRS;
	
	@Getter
	@Setter
	private FormacionPruebasGRS selectedFormacionPruebasGRS;

	@Getter
	@Setter
	private String codigoFiltro;	
	
	@Getter
	@Setter
	private String descripcionFiltro;	
	
	@Getter
	@Setter
	private Boolean activaFiltro;
	
	@Getter
	@Setter
	private Long versionFiltro;
	
	@Getter
	@Setter
	private Date fechaCreacionFiltro;
	
	@Getter
	@Setter
	private Long selectedUsuarioFiltro;
	
	@Getter
	@Setter
	private List<Usuario> listaUsuarios;
	
	@Autowired
	private UsuarioService usuarioService;
	
	
	
	@PostConstruct
	@Override
	public void init(){
		super.init();
		
		activaFiltro = true;
		
		listaUsuarios = new ArrayList<>();
		
		listaUsuarios = usuarioService.findUsuariosActivos();
		
		inicializaLazyModel();
		
		//ordenGRS = SortMeta.builder().field("codigo").order(SortOrder.ASCENDING).priority(1).build();
	}
	
	public String redireccionMenu() {
		init();
		return ListadoNavegaciones.LISTADO_FORMACIONGRS.getRegla();
	}
	
	public String onCrear() {
		JsfUtils.setFlashAttribute(EDITABLE, true);
		JsfUtils.setSessionAttribute(MODOACCESO, "alta");
		log.info("Accedemos al formulario en modo alta");
		return ListadoNavegaciones.FORM_FORMACIONGRS.getRegla();
	}
	
	public String onEditar(Long idFormacionGRS) {
		JsfUtils.setFlashAttribute(EDITABLE, true);
		JsfUtils.setSessionAttribute(MODOACCESO, "edicion");
		JsfUtils.setFlashAttribute("idFormacionGRS", idFormacionGRS);
		return ListadoNavegaciones.FORM_FORMACIONGRS.getRegla();
	}
	
	public String onConsultar(Long idFormacionGRS) {
		JsfUtils.setFlashAttribute(EDITABLE, false);
		JsfUtils.setSessionAttribute(MODOACCESO, "consulta");
		JsfUtils.setFlashAttribute("idFormacionGRS", idFormacionGRS);
		return ListadoNavegaciones.FORM_FORMACIONGRS.getRegla();
	}
	
	public void eliminarFormacionGRS (FormacionPruebasGRS formacionPruebasGRS) {
		try {
			formacionPruebasGRSService.delete(formacionPruebasGRS.getId());
			FacesContext.getCurrentInstance().addMessage("messagesListadoFormaciongrs",new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Formacion GRS borrado correctamente"));
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",mensajesProperties.getString("error"));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
	}
	
	public void desactivarFormacionGRS (FormacionPruebasGRS formacionPruebasGRS) {
		try {
			formacionPruebasGRS.setActiva(false);		
			formacionPruebasGRSService.guardar(formacionPruebasGRS);
			FacesContext.getCurrentInstance().addMessage("messagesListadoFormaciongrs",new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Formacion GRS desactivado correctamente"));
		} catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",mensajesProperties.getString("error"));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}
	}
	
	public void limpiarFiltro () {
		codigoFiltro = "";
		descripcionFiltro = "";
		activaFiltro = true;
		versionFiltro = null;
		fechaCreacionFiltro = null;
		selectedUsuarioFiltro = null;		
	}
	
	
	private void inicializaLazyModel() {
		lazyModel = new LazyDataModelByQueryService<>(FormacionPruebasGRS.class, formacionPruebasGRSService);
		lazyModel.setPreproceso((a, b, c, filters) ->
			filtrosLazyModel(filters)
		);
	
		JsfUtils.removeSessionAttribute(MODOACCESO);
		
	}
	
	
	private void filtrosLazyModel(Map<String, FilterMeta> filters) {
		if (codigoFiltro != null && !codigoFiltro.isEmpty()) {
			filters.put("codigo", new MyFilterMeta(codigoFiltro));
		}
		if (descripcionFiltro != null && !descripcionFiltro.isEmpty()) {
			filters.put("descripcion", new MyFilterMeta(descripcionFiltro));
		}
		if (Boolean.TRUE.equals(activaFiltro)) {
			filters.put("activa", new MyFilterMeta(activaFiltro));
		}
		if (versionFiltro != null && !Long.toString(versionFiltro).isEmpty()) {
			filters.put("nVersion", new MyFilterMeta(versionFiltro));
		}
		if (fechaCreacionFiltro != null) {
			filters.put("fechaCreacion", new MyFilterMeta(fechaCreacionFiltro));
		}
		if (selectedUsuarioFiltro != null) {
			filters.put("usuario.id", new MyFilterMeta(selectedUsuarioFiltro));
		}
	}
}
