package es.juntadeandalucia.ctpda.gestionpdt.web.valoresdominio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.primefaces.PrimeFaces;
import org.primefaces.event.UnselectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.Dominio;
import es.juntadeandalucia.ctpda.gestionpdt.model.MateriaTipoExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.service.MateriaTipoExpedienteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ValoresDominioService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.Constantes;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.JsfUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.menu.NavegacionBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.menu.NavegacionBean.ListadoNavegaciones;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.BaseBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.FacesUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@ViewScoped
@Slf4j
public class DatosValoresDominioBean extends BaseBean implements Serializable{

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String MENSAJESFORMULARIO = "messagesFormulario";
	private static final String MENSAJEVALORDOMINIO= "Valor de dominio ";
	private static final String ELCAMPO= "el.campo";
	private static final String MENSAJEERROR= "error";
	private static final String EXISTASISTEMA= "existe.sistema";	
	private static final String ACTUALIZADOCORRECTAMENTE = "actualizado.correctamente";
	private static final String GUARDADOCORRECTAMENTE = "guardado.correctamente";
	private static final String EDITABLE = "editable";
	private static final String TIPOOPERACION = "tipoOperacion";
	
	@Autowired
	private ValoresDominioService valoresDominioService;
	
	
	@Autowired
	private MateriaTipoExpedienteService materiaTipoExpedienteService;
	
	
	@Getter @Setter
	private List<ValoresDominio> listaValoresDominioSup;
	
	@Getter @Setter
	private List<ValoresDominio> listaValoresDominioAscendentes;
	
	@Getter
	@Setter
	private List<ValoresDominio> selectedTiposExpedientes;
	
	@Getter
	@Setter
	private ValoresDominio valorDominio;
	
	@Getter
	@Setter
	private ValoresDominio valorDominioMateria;
	
	@Getter
	@Setter
	private ValoresDominio valorDominioTipoExpediente;
	
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
	private Long nivelAnidamientoMax;
	
	@Getter
	@Setter
	private String codigoDominio;
	
	@Getter	@Setter
	private String descripValorDom;
	
	@Getter	@Setter
	private String datosValorDom;
	
	
	@Getter	@Setter
	private Dominio dominio;
	
	@Getter	@Setter
	private String descripValorDomSup;
	
	@Getter	@Setter
	private String descripSelecValorDomSup;
	
	@Getter
	@Setter
	private Long selectedNuevoValorDominioId;
	
	@Getter
	@Setter
	private List<String> listaTiposExpediente;
	
	@Getter
	@Setter
	private List<ValoresDominio> tiposExpediente;

	@Getter
	@Setter
	private MateriaTipoExpediente materiaTipoExp;
	
	
	
	@Getter
	@Setter
	private List<String> listaCodigoPermisos;
	
	@Getter	@Setter
	private Boolean permisoNewValorDom;
	
	@Getter	@Setter
	private Boolean permisoConsValorDom;
	
	@Getter	@Setter
	private Boolean permisoEditValorDom;
	
	@Getter	@Setter
	private Boolean permisoDesacValorDom;
	
	@Getter	@Setter
	private Boolean permisoActValorDom;
	
	@Getter	@Setter
	private Boolean permisoSaveValorDom;
	
	@Getter @Setter
	private String errorDescripcionRepetida;
	
	@Autowired
	private NavegacionBean navegacionBean;
	
	/** desarrollo realizado para el control de cambios. Pendiente revisar para controlar con un solo click los cambios
	@Getter
	@Setter
	private String mensajeValoresModificadosSinGuardar;
	*/
	
	@Getter @Setter
	private Integer numerosSaltos;
	
	/**
	* Initialize default attributes.
	 * @throws BaseException 
	 * @throws BaseFrontException 
	*/
	@PostConstruct
	@Override
	public void init(){
		numerosSaltos = (Integer) JsfUtils.getSessionAttribute(Constantes.NUMERO_SALTOS);
		
		/**
		 * GESTION DE PERMISOS
		 * */
		
		listaCodigoPermisos = (List<String>) JsfUtils.getSessionAttribute(Constantes.LISTACODIGOPERMISOS);
		
		permisoNewValorDom = listaCodigoPermisos.contains(Constantes.PERMISO_NEW_VALORDOM);
		
		permisoConsValorDom = listaCodigoPermisos.contains(Constantes.PERMISO_CONS_VALORDOM);
		
		permisoEditValorDom = listaCodigoPermisos.contains(Constantes.PERMISO_EDIT_VALORDOM);
		
		permisoDesacValorDom = listaCodigoPermisos.contains(Constantes.PERMISO_DESAC_VALORDOM);
				
		permisoActValorDom = listaCodigoPermisos.contains(Constantes.PERMISO_ACT_VALORDOM);
		
		permisoSaveValorDom = listaCodigoPermisos.contains(Constantes.PERMISO_SAVE_VALORDOM);
		
		mensajesProperties = ResourceBundle.getBundle("es.juntadeandalucia.ctpda.gestionpdt.web.Messages_es");
		
		FacesUtils.setAttribute(EDITABLE, JsfUtils.getFlashAttribute(EDITABLE));
		
		Long idValorDominio = (Long) JsfUtils.getFlashAttribute("idValDom");
		dominio = (Dominio) JsfUtils.getSessionAttribute("dominio");
		
		String descripDom = dominio.getDescripcion();
		datosValorDom = mensajesProperties.getString("datos.de")+ " " + descripDom;
		
		
		//EN FUNCION DEL TIPO DE OPERACION Y DEL VALOR DE DOMINIO MOSTRAMOS DISTINTAS DESCRIPCIONES EN EL FORMULARIO
		descripValorDom = mensajesProperties.getString("formulario.de")+ " " + descripDom;		
		descripValorDomSup = descripDom + " superior";
		descripSelecValorDomSup = "Seleccione " + descripValorDomSup;
		
		
		if(idValorDominio != null){
			valorDominio = valoresDominioService.obtener(idValorDominio);
			
			if(valorDominio != null && valorDominio.getValorDominioPadre() != null) {
				selectedNuevoValorDominioId = valorDominio.getValorDominioPadre().getId();
			}
			
		}else {
			nivelAnidamientoMax = dominio.getNivelAnidamientoMaximo();
			valorDominio = new ValoresDominio();
		}
		
		nivelAnidamientoMax = dominio.getNivelAnidamientoMaximo();
		
		codigoDominio = dominio.getCodigo();
		
		/*
		 * SI EL NIVEL DE ANIDAMIENTO ES MAYOR DE CERO DEBEMOS: 
		 * 1. CARGAR EL COMBO CON LOS VALORES DE DOMINIO SUPERIORES (QUE PODRAN SER VALORES DE DOMINIO PADRES).
		 * 2. CARGAR EL LISTADO CON LOS VALORES DE DOMINIO ASCENDENTES PARA EL VALOR DE DOMINIO SELECCIONADO.
		 * */
		if(nivelAnidamientoMax > 0) {
			listaValoresDominioSup = valoresDominioService.findValoresDominioActivosByCodigoDominio(dominio.getCodigo());
			listaValoresDominioAscendentes= obtenerValoresDominioAscendentes(valorDominio);
		}
		
		/*
		 *	SI EL VALOR DE DOMINIO ES DEL DOMINIO 'MATERIA' CARGAMOS:
		 * 1. LA LISTA DE TIPOS DE EXPEDIENTES ACTIVOS PARA EL COMBO
		 * 2. LOS TIPOS DE EXPEDIENTES ASOCIADOS A LA MATERIA SOBRE LA QUE ESTEMOS TRABAJANDO
		 * */
		if(dominio.getCodigo().equals("MAT"))
		{
			tiposExpediente = valoresDominioService.findValoresDominioActivosByCodigoDominio("TIP_EXP");
			selectedTiposExpedientes = materiaTipoExpedienteService.findValoresDominioTipExp(idValorDominio);
			
			materiaTipoExp = new MateriaTipoExpediente();
		}
		
		/** desarrollo realizado para el control de cambios. Pendiente revisar para controlar con un solo click los cambios
		mensajeValoresModificadosSinGuardar = mensajesProperties.getString(MENSAJECANCELAR);
		*/
	}
	
	public void guardarValoresDominio() {
		boolean puedoGuardar = validacionesGuardar();
		ValoresDominio valDomPadre = null;
		
		try {
			if(puedoGuardar) {
				//ALTA DE VALOR DOMINIO
				if(valorDominio.getId() == null) { 					
					valorDominio.setActivo(true);

					FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, new FacesMessage(FacesMessage.SEVERITY_INFO, "", MENSAJEVALORDOMINIO +" "+valorDominio.getDescripcion()+" "+ mensajesProperties.getString(GUARDADOCORRECTAMENTE)));
				
				//MODIFICACION DEL VALOR DE DOMINIO
				}else if(valorDominio.getId() != null) {
					
					FacesContext.getCurrentInstance().addMessage(MENSAJESFORMULARIO, new FacesMessage(FacesMessage.SEVERITY_INFO, "", MENSAJEVALORDOMINIO +" "+valorDominio.getDescripcion()+" "+ mensajesProperties.getString(ACTUALIZADOCORRECTAMENTE)));
				}
				
				valorDominio.setDominio(this.dominio);
				
				if(selectedNuevoValorDominioId != null){
					valDomPadre = valoresDominioService.obtener(selectedNuevoValorDominioId);
					valorDominio.setValorDominioPadre(valDomPadre);
				}else {
					valorDominio.setNivelAnidamiento(0L);
				}
				
				valoresDominioService.guardar(valorDominio);

				/*
				 * SI EL VALOR DE DOMINIO PERTENECE AL DOMINIO 'MATERIAS' TENDEREMOS QUE VER SI EXISTEN TIPOS DE EXPEDIENTES RELACIONDOS
				 * 
				 *  TANTO SI SE TRATA DE ALTA COMO DE MODIFICACION DEL VALOR DE DOMINIO DEBEMOS RECUPERAR LAS RELACIONES
				 *  QUE EXISTEN ENTRE LA MATERIA Y LOS TIPOS DE EXPEDIENTES. DICHAS RELACIONES LAS TENEMOS EN MATERIATIPOEXPEDIENTES.
				 *  SE ELIMINAN DE BD Y SE GUARDAN LAS RELACIONES NUEVAS ENTRE LA MATERIA EN CUESTION Y LOS TIPOS DE EXPEDIENTES QUE SE HAYAN SELECCIONADO.
				 * */
				guardarValorDominioMateria ();
								
				if(nivelAnidamientoMax > 0) {
					listaValoresDominioSup = valoresDominioService.findValoresDominioActivosByCodigoDominio(dominio.getCodigo());
					listaValoresDominioAscendentes= obtenerValoresDominioAscendentes(valorDominio);
				}
				
				
				
				PrimeFaces.current().ajax().update("formBuscadorValoresDominio");
				
				navegacionBean.setTxtMigaPan(Constantes.EDICION_DOMINIO+dominio.getDescripcion()+" "+valorDominio.getDescripcion());
				PrimeFaces.current().ajax().update("textoMigaPan");
			}
		}catch (BaseException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString(MENSAJEERROR));
			PrimeFaces.current().dialog().showMessageDynamic(message);
			log.error(e.getMessage());
		}	
	}	
	
	private void guardarValorDominioMateria () throws BaseException {
		if(dominio.getCodigo().equals("MAT"))
		{
			ValoresDominio selectedValDom = null;
			/*
			 * RECUPERAMOS LAS RELACIONES PARA EL VALOR DE DOMINIO SI ES QUE EXISTEN Y LAS ELIMINAMOS
			 *  */
			List<MateriaTipoExpediente> materiasTipExpEliminar =  materiaTipoExpedienteService.findMateriasTipoExpedienteIdMateria(valorDominio.getId());
			MateriaTipoExpediente matTipExp = null;
			for(int i=0; i<materiasTipExpEliminar.size(); ++i) {
				matTipExp = materiasTipExpEliminar.get(i);
				
				materiaTipoExpedienteService.delete(matTipExp.getId());	
			}

			/*
			 * SI SE HAN SELECCIONADO TIPOS DE EXPEDIENTES PARA LA MATERIA, LOS RECUPERAMOS Y CREAMOS LAS ASOACIACIONES
			 * CORRESPONDIENTES.
			 */
			if(selectedTiposExpedientes != null)
			{
				for(int i=0; i<selectedTiposExpedientes.size(); ++i) {
					selectedValDom = selectedTiposExpedientes.get(i);
					
					/*
					 * COMPROBAMOS QUE EL TIPO DE EXPDIENTE NO ESTE ASOCIADO A DICHA MATERIA Y SI ES ASI GUARDAMOS LA RELACION.
					 * */
					if((materiaTipoExpedienteService.findMateriasTipoExpedienteIdMateria(valorDominio.getId(), selectedValDom.getId())) == null)
					{
						materiaTipoExp = new MateriaTipoExpediente();
						materiaTipoExp.setValorDominioTipoExpediente(selectedValDom);
						materiaTipoExp.setValorDominioMateria(valorDominio);
						materiaTipoExpedienteService.guardar(materiaTipoExp);
					}

				}
			}
			
		}
	}
	
	public boolean validacionesGuardar () {
		boolean validoGuardar = true;
		String codigo = valorDominio.getCodigo();
		String descripcion = valorDominio.getDescripcion();
		String abreviatura = valorDominio.getAbreviatura();
		List<ValoresDominio> valDomIgualCod = null;
		Long orden = valorDominio.getOrden();
		String errorObligatorios = "";
		errorDescripcionRepetida = "";
		String errorCodigoRepetido = "";
		String errorAnidamientoMaxSuperado = "";
		String mensajeFinal = "";
		
		/*
		 * VALIDACIONES OBLIGATORIEDAD
		 */
		if((descripcion == null || descripcion.isEmpty()) || (abreviatura == null || abreviatura.isEmpty())  || (codigo == null || codigo.isEmpty()) ||(orden == null)){
			validoGuardar = false;
			errorObligatorios = mensajesProperties.getString("campos.obligatorios");
		}
		
		/*
		 * VALIDACION PARA NO REPETIR DATOS ASOCIADOS AL VALOR DE DOMINIO: 
		 * VALIDAMOS QUE NO EXISTA EN EL SISTEMA UN VALOR DE DOMINIO CON LA MISMA DESCRIPCION.
		 */
		validoGuardar = validacionNoDatosRepetidos(validoGuardar,descripcion);		
		
		/*
		 * VALIDACION PARA NO REPETIR DATOS ASOCIADOS AL VALOR DE DOMINIO: 
		 * VALIDAMOS QUE NO EXISTA EN EL SISTEMA UN VALOR DE DOMINIO CON EL MISMO CODIGO.
		 */
		if(codigo != null && !codigo.isEmpty()) 
		{
			if(valorDominio.getId() != null)
			{
				valDomIgualCod = valoresDominioService.findValoresDominioByIdCodigo(valorDominio.getId(), dominio.getId(), codigo);	
			}else {
				valDomIgualCod = valoresDominioService.findValoresDominioByCodigo(dominio.getId(),codigo);
			}
			
			
			
			if( valDomIgualCod != null && !valDomIgualCod.isEmpty()) {
				errorCodigoRepetido = mensajesProperties.getString(ELCAMPO) + " "  + mensajesProperties.getString("codigo.valor.dominio")+ " "  + mensajesProperties.getString(EXISTASISTEMA) + " " + mensajesProperties.getString("otro.valor.dominio");
				validoGuardar = false;
			}		
		}
		
		/*
		 * VALIDACION PARA COMPROBAR QUE EL VALOR DE ANIDAMIENTO NO SEA SUPERIOR AL MÁXIMO PERMITIDO PARA EL DOMINIO ASOCIADO.
		 * */
		
		if(valorDominio.getNivelAnidamiento() != null && dominio.getNivelAnidamientoMaximo() != null && valorDominio.getNivelAnidamiento() > dominio.getNivelAnidamientoMaximo())
		{
			validoGuardar = false;
			errorAnidamientoMaxSuperado = mensajesProperties.getString("valor.dominio.nivel.anidamiento");			
		}
			

		//VALIDACION ORDEN
		
		/**if((orden != null))
		{
			List<ValoresDominio> valDomByMismoOrden = null;
			/*
			 * SI EL NIVEL DE ANIDAMIENTO ES SUPERIOR A '0' ES QUE PUEDE EXISTIR ANIDAMIENTO Y POR TANTO VALORES DE DOMINIO SUPERIORES.
			 * SI EL NIVEL DE ANIDAMIENTO ES CERO, NO EXISTEN SUPERIORES. 
			 * HACEMOS VALIDACION DE ORDEN DIFERENTE PARA CADA CASO:
			 * */
			
			/**if(dominio.getNivelAnidamientoMaximo() > 0)
			{
				 SI NO SE HA SELECCIONADO UN SUPERIOR O PADRE, COMPROBAMOS QUE NO EXISTA EL ORDEN PARA EL DOMINIO.
				if (selectedNuevoValorDominioId == null) {
					if(valorDominio.getId() != null)
					{
						valDomByMismoOrden = valoresDominioService.obtenerValoresDominioModNullConMismoOrdenVisualizacion(valorDominio.getOrden(), dominio.getId(), valorDominio.getId());
					}else {
						valDomByMismoOrden = valoresDominioService.obtenerValoresDominioNullConMismoOrdenVisualizacion(valorDominio.getOrden(), dominio.getId());	
					}
					
				SI SE SELECCIONA UN SUPERIOR O PADRE, COMPROBAMOS QUE NO TENGA MISMO ORDEN QUE UN VALOR DE DOMINIO QUE TENGA EL MISMO SUPERIOR SELECCIONADO.
				} else if (selectedNuevoValorDominioId != null) {
					if(valorDominio.getId() != null)
					{
						valDomByMismoOrden = valoresDominioService.obtenerValorDominioModConMismoOrdenVisualizacion(valorDominio.getOrden(), selectedNuevoValorDominioId, dominio.getId(), valorDominio.getId());
					}else{
						valDomByMismoOrden = valoresDominioService.obtenerValorDominioConMismoOrdenVisualizacion(valorDominio.getOrden(), selectedNuevoValorDominioId, dominio.getId());	
					}
					
				}
			}
			SI NO EXISTE NIVEL DE ANIDAMIENTO COMPROBAMOS QUE NO EXISTA EL ORDEN PARA OTRO VALOR DE DOMINIO ASOCIADO AL MISMO DOMINIO.
			else {
				
				if(valorDominio.getId() != null)
				{
					valDomByMismoOrden = valoresDominioService.obtenerValorDominioModConMismoOrdenVisualizacionSinAnidamiento(valorDominio.getOrden(), dominio.getId(), valorDominio.getId());
				}else {
					valDomByMismoOrden = valoresDominioService.obtenerValorDominioConMismoOrdenVisualizacionSinAnidamiento(valorDominio.getOrden(), dominio.getId());					
				}
			}
			
			SI EXISTE UN VALOR DE DOMINIO EN EL SISTEMA CON EL ORDEN ENTONCES MOSTRAMOS MENSAJE DE ERROR.
			if (valDomByMismoOrden != null && !valDomByMismoOrden.isEmpty())
			{
				validoGuardar = false;
				errorOrdenRepetido = mensajesProperties.getString(ELCAMPO) + " " + mensajesProperties.getString("orden.visualizacion") + " " + mensajesProperties.getString(EXISTASISTEMA);
			}
		}*/
		
		mensajeFinal (errorObligatorios, errorDescripcionRepetida, errorCodigoRepetido, errorAnidamientoMaxSuperado, mensajeFinal);

		return validoGuardar;
	}
	
	private boolean validacionNoDatosRepetidos (boolean validoGuardar, String descripcion) {
		if(descripcion != null && !descripcion.isEmpty()) 
		{
			List<ValoresDominio> listValDomIgualDesc = null;
			if(valorDominio.getId() != null)
			{
				listValDomIgualDesc = valoresDominioService.findValoresDominioByIdDescripcion(valorDominio.getId(), dominio.getId(), descripcion);	
			}else {
				listValDomIgualDesc = valoresDominioService.findValoresDominioByDescripcion(dominio.getId(), descripcion);
			}
			
			
			if(listValDomIgualDesc != null && !listValDomIgualDesc.isEmpty()) {
				errorDescripcionRepetida = mensajesProperties.getString(ELCAMPO) + " "  + mensajesProperties.getString("descripcion")+ " "  + mensajesProperties.getString(EXISTASISTEMA) + " " + mensajesProperties.getString("otro.valor.dominio");
				validoGuardar = false;
			}
				
		}
		return validoGuardar;
	}

	private void mensajeFinal (String errorObligatorios, String errorDescripcionRepetida, String errorCodigoRepetido, String errorAnidamientoMaxSuperado, String mensajeFinal) {
		if(!errorObligatorios.isBlank()) {
			mensajeFinal = errorObligatorios;
		} 
		if(!errorDescripcionRepetida.isBlank()){
			mensajeFinal += "\n"+errorDescripcionRepetida;
		}
		if(!errorCodigoRepetido.isBlank()) {
			mensajeFinal += "\n"+errorCodigoRepetido;
		}
		if(!errorAnidamientoMaxSuperado.isBlank()) {
			mensajeFinal += "\n"+errorAnidamientoMaxSuperado;
		}
		if(!mensajeFinal.isBlank()) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajeFinal);
			PrimeFaces.current().dialog().showMessageDynamic(message);
		}
	}
	
	public List<ValoresDominio> obtenerValoresDominioAscendentes(ValoresDominio valorDominioSeleccionado)
	{
		List<ValoresDominio> listaValDomAscendentes = new ArrayList<>();
		ValoresDominio valorDominioPadre = null;
		ValoresDominio valDomHijo = valorDominioSeleccionado;
		
		valorDominioPadre = valDomHijo.getValorDominioPadre();
		
		while(valorDominioPadre != null && valorDominioPadre.getActivo())
		{
			listaValDomAscendentes.add(valorDominioPadre);
			valorDominioPadre = valorDominioPadre.getValorDominioPadre();
		}
		
		return listaValDomAscendentes;
	}	
	
	public  void calcularNivelAnidamiento()
	{
		ValoresDominio valDomPadre	= null;
		Long nivelAnidamientoNuevo = 0L;
		
		if(selectedNuevoValorDominioId != null)
		{
			valDomPadre = valoresDominioService.obtener(selectedNuevoValorDominioId);
			nivelAnidamientoNuevo = valDomPadre.getNivelAnidamiento() + 1;
			valorDominio.setNivelAnidamiento(nivelAnidamientoNuevo);
		}else {
			valorDominio.setNivelAnidamiento(0L);
		}
		
		if(nivelAnidamientoNuevo > dominio.getNivelAnidamientoMaximo())
		{
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", mensajesProperties.getString("valor.dominio.nivel.anidamiento"));
			PrimeFaces.current().dialog().showMessageDynamic(message);	
		}
		
		/** desarrollo realizado para el control de cambios. Pendiente revisar para controlar con un solo click los cambios
		//control de modificado del campo descripcion
    	mensajeValoresModificadosSinGuardar = mensajesProperties.getString(MENSAJECANCELARDATOSMODIFICADOS);
      	PrimeFaces.current().ajax().update(IDFORMULARIO);
      	*/
		
		PrimeFaces.current().ajax().update("formFormularioValoresDominio:nivelAnidamiento");

	}	
	
	public void calcularAbreviatura()
	{
		String descrip = valorDominio.getDescripcion();
		String palabra="";
		String abreviatura = "";
		StringBuilder abreviaturaAux = new StringBuilder();
		
		StringTokenizer palabras = new StringTokenizer(descrip);
		
		while (palabras.hasMoreTokens()) {
			palabra= palabras.nextToken();
			abreviaturaAux.append(palabra.charAt(0));
		}
		abreviatura = abreviaturaAux.toString();
		valorDominio.setAbreviatura(abreviatura);
		
		/** desarrollo realizado para el control de cambios. Pendiente revisar para controlar con un solo click los cambios
		//control de modificado del campo descripcion
		mensajeValoresModificadosSinGuardar = mensajesProperties.getString(MENSAJECANCELARDATOSMODIFICADOS);
	  	PrimeFaces.current().ajax().update(IDFORMULARIO);		
		PrimeFaces.current().ajax().update("formFormularioValoresDominio:abreviatura");
		*/

	}		
	
	public String redireccionMenu() {
		init();
		return ListadoNavegaciones.LISTADO_TIPOS_AGRUPACION.getRegla();
	}		
	
    public <T> void onItemUnselect(UnselectEvent<T> event) {
    	/** Evento sin funcion*/
    	
    	/** desarrollo realizado para el control de cambios. Pendiente revisar para controlar con un solo click los cambios
    	//control de modificado del campo descripcion
    	mensajeValoresModificadosSinGuardar = mensajesProperties.getString(MENSAJECANCELARDATOSMODIFICADOS);
      	PrimeFaces.current().ajax().update(IDFORMULARIO);
      	*/
    }	
    
    /** desarrollo realizado para el control de cambios. Pendiente revisar para controlar con un solo click los cambios
    public void valorModificado () {
    	mensajeValoresModificadosSinGuardar = mensajesProperties.getString(MENSAJECANCELARDATOSMODIFICADOS);
    	PrimeFaces.current().ajax().update(IDFORMULARIO);
    }
    */
	
	public String onEditarByForm(Long idValDom) {
		JsfUtils.setSessionAttribute(Constantes.NUMERO_SALTOS, numerosSaltos+1);
		
		JsfUtils.setFlashAttribute("idValDom", idValDom);
		JsfUtils.setFlashAttribute(EDITABLE, true);
		JsfUtils.setFlashAttribute(TIPOOPERACION, "editar");
		ValoresDominio valoresDominio = valoresDominioService.obtener(idValDom);
		navegacionBean.setTxtMigaPan(Constantes.EDICION_DOMINIO+dominio.getDescripcion()+" "+valoresDominio.getDescripcion());
		return ListadoNavegaciones.FORM_VALORES_DOMINIO.getRegla();
	}
	
}
	


