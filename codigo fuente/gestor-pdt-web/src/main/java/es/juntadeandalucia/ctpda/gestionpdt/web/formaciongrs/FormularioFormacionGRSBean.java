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
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.primefaces.PrimeFaces;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.FormacionPruebasBlh;
import es.juntadeandalucia.ctpda.gestionpdt.model.FormacionPruebasGRS;
import es.juntadeandalucia.ctpda.gestionpdt.model.Usuario;
import es.juntadeandalucia.ctpda.gestionpdt.service.FormacionPruebasBlhService;
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
public class FormularioFormacionGRSBean extends BaseBean implements Serializable {


	
	private static final long serialVersionUID = 1L;
	
	@Getter @Setter
	private String modoAcceso;
	
	@Getter @Setter
	private String codigo;
	
	@Getter @Setter
	private String descripcion;
	
	@Getter @Setter
	private Boolean activo;
	
	@Getter @Setter
	private FormacionPruebasGRS formacionPruebasGRS;
	
	@Autowired
	private FormacionPruebasGRSService formacionPruebasGRSService;
	
	@Getter @Setter
	private Long selectedUsuario;
	
	@Getter @Setter
	private List<Usuario> listadoUsuarios;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Getter @Setter
	private Long idFormacionGRS;
	
	@Getter @Setter
	private boolean editableFormulario;
	
	
	@PostConstruct
	@Override
	public void init(){
		super.init();
		
		modoAcceso = (String) JsfUtils.getSessionAttribute("modoAcceso");
		idFormacionGRS = (Long) JsfUtils.getFlashAttribute("idFormacionGRS");
		editableFormulario = (boolean) JsfUtils.getFlashAttribute("editable");
		
		formacionPruebasGRS = new FormacionPruebasGRS();
		
		if(idFormacionGRS != null) {
			formacionPruebasGRS = formacionPruebasGRSService.obtener(idFormacionGRS);
			codigo = formacionPruebasGRS.getCodigo();
			descripcion = formacionPruebasGRS.getDescripcion();
			activo = formacionPruebasGRS.getActiva();
			selectedUsuario = (formacionPruebasGRS.getUsuario() != null)?formacionPruebasGRS.getUsuario().getId():null;
		}else {
			codigo = "";
			descripcion = "";
			activo = true;
			selectedUsuario = null;
		}
		
		listadoUsuarios = new ArrayList<>();
		listadoUsuarios = usuarioService.findUsuariosActivos();
	}
	
	public void saveFormacionGRS() {
		if(codigo != null && codigo.isEmpty()) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "El código tiene que ser obligatorio");
			PrimeFaces.current().dialog().showMessageDynamic(message);
		
		}else {
			try {
				formacionPruebasGRS.setCodigo(codigo);
				formacionPruebasGRS.setDescripcion(descripcion);
				formacionPruebasGRS.setActiva(activo);
				
				if(selectedUsuario != null) {
					Usuario usuarioSeleccionado = usuarioService.obtener(selectedUsuario);
					formacionPruebasGRS.setUsuario(usuarioSeleccionado);
				}				
								
				formacionPruebasGRSService.guardar(formacionPruebasGRS);
				log.info("Formacion GRS guardado");
				FacesContext.getCurrentInstance().addMessage("messagesFormularioFormacionGRS",new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Formacion " +formacionPruebasGRS.getCodigo()+ " GRS guardado correctamente"));
				
			} catch (BaseException e) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",mensajesProperties.getString("error"));
				PrimeFaces.current().dialog().showMessageDynamic(message);
				log.error(e.getMessage());
			}
		}
	}
}
