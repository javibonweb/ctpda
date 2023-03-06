package es.juntadeandalucia.ctpda.gestionpdt.web.documentos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentosTramiteMaestra;
import lombok.Data;

@Data
//Son AgrupacionesDTO, pero se muestran en la pestaña de trámites con los datos de la categoría a la que pertenecen
public class TramiteDocDTO extends NodoDocDTO{
	private static final long serialVersionUID = 1L;
	
	private List<AgrupacionDTO> agrupaciones;		
	private Map<Long, AgrupacionDTO> mapAgrupaciones = new HashMap<>();
	
	public void addDocumento(DocumentosTramiteMaestra docTr) {
		getAgrupacion(docTr).addDocumento(docTr);			
	}
	
	private AgrupacionDTO getAgrupacion(DocumentosTramiteMaestra docTr) {
		Long key = docTr.getIdAgrupacionExpediente();
		
		if(!mapAgrupaciones.containsKey(key)){
			mapAgrupaciones.put(key, crearAgrupacion(docTr));
		}	
		
		return mapAgrupaciones.get(key);
	}
	
	private AgrupacionDTO crearAgrupacion(DocumentosTramiteMaestra docTr) {
		var agr =  new AgrupacionDTO(docTr);
		
		//En nuestro caso la agrupación se carga con los datos de categoría
		agr.setDescripcion(docTr.getCategoria());
		agr.setOrden(docTr.getOrdenCategoria());

		return agr;
	}
	
	public List<AgrupacionDTO> getAgrupaciones(){
		if(null == this.agrupaciones) {
			agrupaciones = new ArrayList<>(mapAgrupaciones.values());
			agrupaciones.forEach(a -> Collections.sort(a.getDocumentos()));
			Collections.sort(agrupaciones);
		}
		
		return this.agrupaciones;
	}
	
}



