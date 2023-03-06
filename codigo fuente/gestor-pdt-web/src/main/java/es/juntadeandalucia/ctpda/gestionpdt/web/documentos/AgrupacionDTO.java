package es.juntadeandalucia.ctpda.gestionpdt.web.documentos;

import java.util.ArrayList;
import java.util.List;

import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentosTramiteMaestra;
import lombok.Data;

@Data
public class AgrupacionDTO extends NodoDocDTO {
	private static final long serialVersionUID = 1L;
	
	private Long idTramite;
	private Long idTramiteSup;
	private Long idTipo;
	private List<DocumentosTramiteMaestra> documentos = new ArrayList<>();
	private List<AgrupacionDTO> subAgrupaciones = new ArrayList<>();
	private boolean visible = true;
	
	AgrupacionDTO(DocumentosTramiteMaestra docTr) {
		this.setId(docTr.getIdAgrupacionExpediente());
		this.setIdTipo(docTr.getIdTramiteExpediente()); //El "tipo" es el id de trámite
		this.setDescripcion(docTr.getAgrupacionExpediente());
		this.setIdTramite(docTr.getIdTramiteExpediente());
		this.setIdTramiteSup(docTr.getIdTramiteExpedienteSup());
		this.setVisible(docTr.getVerEnPestDocumentos());
		this.setOrden(docTr.getOrdenAgrupacionExp());
	}
	
	void addDocumento(DocumentosTramiteMaestra docTr) {
		this.documentos.add(docTr);			
	}
	
}
