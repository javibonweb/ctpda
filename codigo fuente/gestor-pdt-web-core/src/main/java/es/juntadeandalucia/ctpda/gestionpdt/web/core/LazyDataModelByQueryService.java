package es.juntadeandalucia.ctpda.gestionpdt.web.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import es.juntadeandalucia.ctpda.gestionpdt.model.core.EntidadBasica;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.CrudQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.OrdenDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.OrdenacionDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.enums.DireccionEnum;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.exception.FrontendException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;


/**
 * Primefaces lazydatatable.
 * Utilizando como patron de basqueda servicio con interfaz QueryService<T>, se monta objeto
 * capaz de hacer de puente entre el servicio de consultas y el renderizador de primefaces.
 *
 * @param <T> the generic type
 */

/**
 * To string.
 *
 * @return the java.lang. string
 */
@Data

/**
 * Hash code.
 *
 * @return the int
 */
@EqualsAndHashCode(callSuper = true)

/** The Constant log. */
@Slf4j
public class LazyDataModelByQueryService<T extends EntidadBasica> extends LazyDataModel<T> {

	
	/** SERIAL UID. */
	private static final long serialVersionUID = 1L;

	/** Servicio de consulta CRUD. Tiene unicamente los servicios de consulta de información*/
	private final CrudQueryService<T> crudService; 
	
	/** The local actualizado contador. */
	private boolean localActualizadoContador = false;
	
	/** The local value type. */
	private Class<T> localValueType;
	
	/** The list. */
	private List<T> list;
	
	/** The filter custom. */
	private Map<String, FilterMeta> filterCustom = new HashMap<>();
	
	/**
	 * Lambda que premite inyectar comportamiento en la lista de resultados.
	 * Basicamente ejecuta foreach(Consumer<T>) sobre los elementos retornados por el backend, o sea, la pagina activa.
	 * */
	private transient Consumer<T> eventoTrasCarga;
	/**
	 * Lambda que premite inyectar comportamiento en la lista de ordenaciones obtenidas del tag datatable del frontend.
	 * Basicamente ejecuta foreach(Consumer<SortMeta>) sobre los elementos de ordenacian enviados desde tag p.datatable.
	 * */	
	private transient BiConsumer<String,SortMeta> eventoPreprocesarOrdenacion;
	/**
	 * Lambda que premite inyectar comportamiento en la lista de filtros obtenidos del tag datatable del frontend (+ los custom).
	 * Basicamente ejecuta foreach(BiConsumer<String,Object>) sobre los elementos de filtrado enviados desde tag p.datatable.
	 * */	
	private transient BiConsumer<String,Object> eventoPreprocesarFilters;
	/**
	 * Instancia de la interfaz funcional Proceso que se ejecutara antes de realizar la carga del listado.
	 * */	
	private Proceso preproceso;
	/**
	 *	Instancia de la interfaz funcional Proceso que se ejecutara tras realizar la carga del listado.
	 * */	
	private Proceso postproceso;
	
	/**
	 * Constructor de la clase.
	 *
	 * @param valueType the value type
	 * @param crudService the crud service
	 */
	public LazyDataModelByQueryService(Class<T> valueType, CrudQueryService<T> crudService) {
		super();
		this.localValueType = valueType;
		this.crudService=crudService;
 	}

	/**
	 * Evento load de primefaces.
	 *
	 * @param first the first
	 * @param pageSize the page size
	 * @param multiSortMeta the multi sort meta
	 * @param filters the filters
	 * @return the list
	 */
	@Override
	public List<T> load(int first, int pageSize, Map<String,SortMeta> multiSortMeta, Map<String, FilterMeta> filters) {
		int pagina = 0;

		/*
		 * Evento de Preprocesamiento
		 */
		if(preproceso!=null) {
			preproceso.procesar(first, pageSize, multiSortMeta, filters);
		}
		
		try {

			if (first == 0 || first < 0) {
				pagina = 0;
			} else {
				pagina = first / pageSize;
			}

			if (filterCustom != null && !filterCustom.isEmpty()) {
				filters.putAll(filterCustom);
			}
			if (multiSortMeta!=null && eventoPreprocesarOrdenacion!=null) {
				multiSortMeta.forEach(eventoPreprocesarOrdenacion);
			}
			if (eventoPreprocesarFilters!=null && filters!=null) {
				filters.forEach(eventoPreprocesarFilters);
			}

			//creamos llamada counter en paralelo.
			log.debug("INICIAMOS COUNT");
			Future<Integer> counter = callCount(multiSortMeta, filters);//lanzamos count() pero no esperamos a que lo procese.
	        list = new ArrayList<>();
	        log.debug("INICIAMOS SELECT");
	        List<T> listaDatos = obtenerListadoPaginaActiva(pagina, pageSize, multiSortMeta, filters);
			list.addAll(listaDatos);
			log.debug("FINALIZAMOS SELECT");
			this.setRowCount(counter.get());//aqui es donde se produce el bloqueo si el count aun no ha terminado
		} catch (ExecutionException | InterruptedException e) {
			log.error(new FrontendException( e).getMessage());
			 Thread.currentThread().interrupt();
		}
		localActualizadoContador = true;
		/*
		 * Eventos de Postprocesamiento
		 */
		if(eventoTrasCarga!=null && list!=null) {
			list.forEach(eventoTrasCarga);
		}
		if (postproceso!=null) {
			postproceso.procesar(first,  pageSize, multiSortMeta, filters); 
		}
		return list;
	}



	/**
	 * Evento getRowData de primefaces.
	 *
	 * @param rowKey the row key
	 * @return the row data
	 */
	@Override
	public T getRowData(String rowKey) {
		Optional<T> optional = list.stream().filter(obj -> rowKey.equalsIgnoreCase(obj.getId()==null?"":obj.getId().toString())).findFirst();
		if (optional.isPresent()) {
			return optional.get();
		} else {
			return null;
		}
	}

	/**
	 * Evento getRowkey de primefaces
	 *
	 * @param object the object
	 * @return the row key
	 */
	@Override
	public String getRowKey(T object) {
		return object.getId().toString();
	}
	
	/**
	 * Metodo para forzar refrescar el contador, evitando la cache.
	 */
	public void refreshrowCount () {
		localActualizadoContador = true;
	}

	/**
	 * Cuenta el numero de filas.
	 *
	 * @return the row count
	 */
	@Override
	public int getRowCount() {
		if (!localActualizadoContador) {//solo lo calculo por mi cuenta si no he hecho el load antes (a veces ocurre en la primera carga y hay que contemplarlo)
			int totalElements = 0;
			try {
				totalElements = callCount(null,null).get(); 
			} catch (ExecutionException | InterruptedException e) {
				log.error(e.getMessage());
				Thread.currentThread().interrupt();
			}
			this.setRowCount(totalElements);
			localActualizadoContador = true;
		}
		return super.getRowCount();
	}
	
	/**
	 * Invocación a contar los elementos.
	 *
	 * @param multiSortMeta the multi sort meta
	 * @param filters the filters
	 * @return Futurible con el resultado del count.
	 */
	private Future<Integer> callCount(Map<String, SortMeta> multiSortMeta, Map<String, FilterMeta> filters){
		
		final OrdenacionDTO orden=buildOrdenacion(multiSortMeta);
		final List<FiltroDTO> fitros=buildFilters(filters);
		Callable<Integer> omega = () ->{
			int data = this.crudService.count(orden, fitros).intValue();
			log.debug("SE TERMINA DE PROCESAR COUNT");
			return data;
		};
		ExecutorService servicio= Executors.newFixedThreadPool(1);	     
		return servicio.submit(omega);
	}
	
	
	/**
	 * Builds the ordenacion.
	 *
	 * @param multiSortMeta the multi sort meta
	 * @return the ordenacion DTO
	 */
	private OrdenacionDTO buildOrdenacion(Map<String, SortMeta> multiSortMeta) {
		final OrdenacionDTO ordenacion = new OrdenacionDTO();
		if (multiSortMeta != null && !multiSortMeta.isEmpty()) {

			List<OrdenDTO> listOrden = new ArrayList<>();
			multiSortMeta.entrySet().stream().forEach(e -> {
				
				OrdenDTO orden = new OrdenDTO();
				orden.setCampo(e.getValue().getField());
				if (e.getValue().getOrder().equals(SortOrder.DESCENDING)) {
					orden.setOrden(DireccionEnum.DESC.toString());
				} else {
					orden.setOrden(DireccionEnum.ASC.toString());
				}

				listOrden.add(orden);
			});
			ordenacion.setListaOrden(listOrden);
		}
		return ordenacion;
	}
	
	/**
	 * Builds the filters.
	 *
	 * @param filters the filters
	 * @return the list
	 */
	private List<FiltroDTO> buildFilters(Map<String, FilterMeta> filters) {
		if (filters != null && !filters.isEmpty()) {
			final List<FiltroDTO> lista= new ArrayList<>();
			filters.keySet().stream().forEach(e -> {
				FiltroDTO filtro = new FiltroDTO();
				filtro.setCampo(e);
				filtro.setValue(filters.get(e).getFilterValue());
				lista.add(filtro);
			});
			return lista;
		}else {
			return Collections.emptyList();
		}
	}

	
	/**
	 * Obtiene el listado de elemetos
	 *
	 * @param page the page
	 * @param size the size
	 * @param multiSortMeta the multi sort meta
	 * @param filters the filters
	 * @return the list
	 */
	private List<T> obtenerListadoPaginaActiva(int page, int size, Map<String, SortMeta> multiSortMeta, Map<String, FilterMeta> filters){
		final OrdenacionDTO orden=buildOrdenacion(multiSortMeta);
		final List<FiltroDTO> fitros=buildFilters(filters);
		Pageable pagina= PageRequest.of(page, size);
		return this.crudService.findAll(pagina, orden, fitros);
		
	}

	/**
	 * Interfaz funcional para definir procesos a partir de expresiones Lambda en la clase.
	 * Recibira todos los parametros que definen el listado.
	 */
	@FunctionalInterface
	public interface Proceso extends Serializable{
		
		/**
		 * Procesar.
		 *
		 * @param first the first
		 * @param pageSize the page size
		 * @param multiSortMeta the multi sort meta
		 * @param filters the filters
		 */
		abstract void procesar(int first, int pageSize, Map<String, SortMeta> multiSortMeta, Map<String, FilterMeta> filters);
	}

	
}
