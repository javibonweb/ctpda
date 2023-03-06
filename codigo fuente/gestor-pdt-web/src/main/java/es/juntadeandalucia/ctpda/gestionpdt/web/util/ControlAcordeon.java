package es.juntadeandalucia.ctpda.gestionpdt.web.util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.view.ViewScoped;

import org.primefaces.component.accordionpanel.AccordionPanel;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@ViewScoped
public class ControlAcordeon implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	//Dado un id de acordeón me devuelve sus índices activos
	private Map<String, Set<String>> mapIndicesActivos = new HashMap<>();
	
	//-----------
	
	public void mantenerIndices(@SuppressWarnings("rawtypes") TabChangeEvent event) {
	    AccordionPanel panel = (AccordionPanel) event.getComponent();
	    String index = Integer.toString(panel.getIndex());
	    abrirPanel(panel, index);
	}
	
	public void mantenerIndices(@SuppressWarnings("rawtypes") TabCloseEvent event) {
	    AccordionPanel panel = (AccordionPanel) event.getComponent();
	    String index = Integer.toString(panel.getIndex());
	    cerrarPanel(panel, index);
	}
	
	//-----------
	
	public void abrirPanel(AccordionPanel panel, String index) {
		Set<String> indices = getIndicesAcordeon(panel);		
		indices.add(index);		
		setIndicesAcordeon(panel, indices);		
	}
	
	public void cerrarPanel(AccordionPanel panel, String index) {
		Set<String> indices = getIndicesAcordeon(panel);		
		indices.remove(index);		
		setIndicesAcordeon(panel, indices);		
	}
	
	public void abrirPanel(AccordionPanel panel, Integer index) {
		abrirPanel(panel, index.toString());
	}
	
	public void cerrarPanel(AccordionPanel panel, Integer index) {
		cerrarPanel(panel, index.toString());
	}

	
	//---------------------
	
	private Set<String> getIndicesAcordeon(AccordionPanel panel){
		Set<String> indices = mapIndicesActivos.get(panel.getClientId());
		
		if(indices == null) {
			indices = crearSetIndices(panel);
			mapIndicesActivos.put(panel.getClientId(), indices);
		}

		return indices; 
	}
	
	private Set<String> crearSetIndices(AccordionPanel panel){
		final String activeIndex = panel.getActiveIndex();
		final List<String> lista = StringUtils.isBlank(activeIndex)? 
											Collections.emptyList() 		
											: Arrays.asList(activeIndex.split("\\s*,\\s*"));
		return new HashSet<>(lista); 
	}
	
	private void setIndicesAcordeon(AccordionPanel panel, Set<String> indices){
		final String strIndices = indicesToString(indices);
	    panel.setActiveIndex(strIndices);
	    this.mapIndicesActivos.put(panel.getClientId(), indices);
	}
	
	private String indicesToString(Set<String> indices) {
		final String[] ss = indices.toArray(new String[0]);
		return String.join(",", ss);
	}
}
