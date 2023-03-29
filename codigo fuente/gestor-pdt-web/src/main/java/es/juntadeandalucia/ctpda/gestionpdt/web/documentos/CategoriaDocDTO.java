package es.juntadeandalucia.ctpda.gestionpdt.web.documentos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentosTramiteMaestra;
import es.juntadeandalucia.ctpda.gestionpdt.util.ListUtils;
import es.juntadeandalucia.ctpda.gestionpdt.util.StringUtils;
import lombok.Data;

@Data
public class CategoriaDocDTO extends NodoDocDTO {
	private static final long serialVersionUID = 1L;

	private List<AgrupacionDTO> agrupaciones;
	private Map<Long, AgrupacionDTO> mapAgrupaciones = new HashMap<>();
	
	public void addDocumento(DocumentosTramiteMaestra docTr) {
		getAgrupacion(docTr).addDocumento(docTr);			
	}
	
	/** Cuando se hayan terminado de añadir documentos se debe llamar a este */
	public void finCargaDocumentos() {
		agrupaciones = new ArrayList<>(mapAgrupaciones.values());
		agrupaciones.removeIf(Predicate.not(AgrupacionDTO::isVisible));
		
		agrupaciones.forEach(a -> Collections.sort(a.getDocumentos()));
		Collections.sort(agrupaciones);
	}
	
	//-----------------------------------
	
	
	private AgrupacionDTO getAgrupacion(DocumentosTramiteMaestra docTr) {
		Long key = docTr.getIdAgrupacionExpediente();
		
		if(!mapAgrupaciones.containsKey(key)){
			mapAgrupaciones.put(key, crearAgrupacion(docTr));
		}	
		
		return mapAgrupaciones.get(key);
	}
	
	private AgrupacionDTO crearAgrupacion(DocumentosTramiteMaestra docTr) {
		return new AgrupacionDTO(docTr);
	}
		
	public boolean contieneTipo(Long idTipo) {
		return this.agrupaciones.stream().anyMatch(a -> idTipo.equals(a.getIdTipo()));
	}
	
	public String indicesPorTipoAgrupacion(Long idTipo) {
		if(idTipo == null) return StringUtils.EMPTY;
		
		List<Long> indices = new ArrayList<>();		
		for(int i=0;i<agrupaciones.size();++i) {
			if(idTipo.equals(agrupaciones.get(i).getIdTipo())){
				indices.add((long)i);
			}
		}
		
		return ListUtils.join(indices, StringUtils.COMA);
	}
	
}
