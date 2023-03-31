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

import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.FormacionPruebasGRS;
import es.juntadeandalucia.ctpda.gestionpdt.model.Usuario;
import es.juntadeandalucia.ctpda.gestionpdt.service.FormacionPruebasGRSService;
import es.juntadeandalucia.ctpda.gestionpdt.service.UsuarioService;
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
	
	@Getter
	@Setter
	private String modoAcceso;
	
	
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
	}
	
	public String redireccionMenu() {
		init();
		return ListadoNavegaciones.LISTADO_FORMACIONGRS.getRegla();
	}
	
	public String onCrear() {
		init();
		return ListadoNavegaciones.FORM_FORMACIONGRS.getRegla();
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
		
		ordenGRS = SortMeta.builder().field("codigo").order(SortOrder.ASCENDING).priority(1).build();
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
