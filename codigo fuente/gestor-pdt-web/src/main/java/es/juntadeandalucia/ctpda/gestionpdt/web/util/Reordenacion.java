package es.juntadeandalucia.ctpda.gestionpdt.web.util;

import java.io.Serializable;
import java.util.List;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.ReorderEvent;
import org.primefaces.model.SortMeta;

import es.juntadeandalucia.ctpda.gestionpdt.model.core.Ordenable;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.ValidacionException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class Reordenacion<E extends Ordenable> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String ORDEN="orden";
	
	public abstract List<E> cargarDatos(DataTable dt);
	public abstract SortMeta getSortMeta(DataTable dt);
	public abstract String getMensajeFaltaOrden();
	public abstract String getMensajeOrdenDesc();
	public abstract void reordenar(List<E> lista, Long orden0) throws BaseException;
	
	
	public void onRowReorder(ReorderEvent event) throws BaseException {
		final DataTable dt = (DataTable) event.getComponent();
		
			//Pendiente: aplicar orden correcto según ordenación asc/desc. Por ahora no permitimos reordenar si el orden es DESC. 
			if(esOrdenDesc(dt)) {
				throw new ValidacionException(getMensajeOrdenDesc());
			}
			
			List<E> ls = cargarDatos(dt);
			List<E> lsSub; //sublista con las filas ya reordenadas
			
			//Paginación
			final int pag = dt.getPage(); //Empieza en 0
			final int tamPag = ls.size();
			
			//Ajusto los índices del evento a la página actual
			//pag0: índice global del primer elemento de la página
			//Si el tam. de página es 10 -> página1: pag0=0; página2: pag0=10
			final int pag0 = pag * tamPag; 
			
			final int from = Math.min(event.getFromIndex(), event.getToIndex()) - pag0;
			
			//Obtenemos la sublista entre los elementos implicados
			//Miramos si la tabla se soporta en un List o en un Lazy
			if(!(dt.getValue() instanceof List)) {	
				lsSub = this.obtenerSubListaReordenar(ls, event.getFromIndex(), event.getToIndex(), pag0);
			} else {
				lsSub = ls;
			}
			
			//Pendiente: aplicar orden correcto según ordenación asc/desc. Por ahora no permitimos reordenar si el orden es DESC. 
			final long orden0 = (long)pag0 + 1 + from;
			
			//Aplicamos orden en BD
			this.reordenar(lsSub, orden0);
	}
	
	/** Código usado por PF para reordenar filas. Se puede ver cómo hace para el caso List.
	 *    @Override
	 *    org.primefaces.component.datatable.feature.DraggableRowsFeature
	 *    
	    public void decode(FacesContext context, DataTable table) {
	        MethodExpression me = table.getDraggableRowsFunction();
	        if (me != null) {
	            me.invoke(context.getELContext(), new Object[]{table});
	        }
	        else {
	            Map<String, String> params = context.getExternalContext().getRequestParameterMap();
	            String clientId = table.getClientId(context);
	            int fromIndex = Integer.parseInt(params.get(clientId + "_fromIndex"));
	            int toIndex = Integer.parseInt(params.get(clientId + "_toIndex"));
	            table.setRowIndex(fromIndex);
	            Object value = table.getValue();

	            if (value instanceof List) {
	                List list = (List) value;

	                if (toIndex >= fromIndex) {
	                    Collections.rotate(list.subList(fromIndex, toIndex + 1), -1);
	                }
	                else {
	                    Collections.rotate(list.subList(toIndex, fromIndex + 1), 1);
	                }
	            }
	            else {
	                LOGGER.info("Row reordering is only available for list backed datatables. "
	                            + "Use rowReorder AJAX behavior with listener and manually handle the model update.");
	            }
	        }
	    }
	*/
	private boolean esOrdenDesc(DataTable dt) throws ValidacionException {
		final SortMeta sort = this.getSortMeta(dt);
		
		if(sort == null) {
			throw new ValidacionException(this.getMensajeFaltaOrden());
		}
		
		/** Ahora mismo no se tiene en cuenta sortBy ni sortOrder
		 * obligando a usar SortMeta
		 * Quizás esto funcione
		 * 
		 * boolean esDesc = false;
		if(sort == null) {
			String sortMode = dt.getSortMode();
			if(StringUtils.isBlank(sortMode) {
			//ni sortmeta ni sortBy
			throw new ValidacionException("..."); //mensaje "ni sortMeta ni nada"
			}
			
			esDesc = !sortMode.equals("ascending");
		} else {
			esDesc = sort.getOrder().isDescending();
		}
		
		else 
		
		
		
		 */
		
		
		return sort.getOrder().isDescending();
	}
	
	private List<E> obtenerSubListaReordenar(List<E> list, int eventFromIndex, int eventToIndex, int pag0) {				
		final int from = Math.min(eventFromIndex, eventToIndex) - pag0;
		final int to = Math.max(eventFromIndex, eventToIndex) - pag0;
		List<E> lsSub = list.subList(from, to+1);

		if(eventFromIndex > eventToIndex) { //hacia arriba
			int ultimaPos = lsSub.size()-1;
			E elem = lsSub.get(ultimaPos);
			
			//Colocamos en primera posición el elemento arrastrado y soltado (que está en la última posición)
			lsSub.remove(ultimaPos); //ojo al orden add/remove
			lsSub.add(0, elem);
		} else { //hacia abajo
			//Colocamos en última posición el elemento arrastrado y soltado (que está en la primera posición)
			lsSub.add(lsSub.get(0));
			lsSub.remove(0);
		}
		
		return lsSub;
	}

}
