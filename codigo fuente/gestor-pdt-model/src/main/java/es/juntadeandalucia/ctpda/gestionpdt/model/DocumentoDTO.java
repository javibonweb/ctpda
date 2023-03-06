package es.juntadeandalucia.ctpda.gestionpdt.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import lombok.Data;

@Data
public class DocumentoDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String nombreFichero; /**extensionFichero;*/
	private String descripcion;
	private String descripcionAbrev;
	private Boolean origenIncorporado;
	
	private byte[] bytes;
	
	private Long documentoId;
	private Long tramiteExpedienteId;
	private Long expedienteId;
	private String tipoTramite;
	private Long tipoDocumentoId;
	private Long plantillaId;

	private Boolean deTrabajo; //BORRAR
	private Boolean editable;
	private Boolean firmado;
	private Boolean sellado;
	private Boolean anonimizado;
	private Boolean anonimizadoParcial;
	private Long categoriaId;
	
	//Atributos temporales para los beans
	//Vista previa documentos
	private String claveVistaPrevia; //Código temporal
	private String urlVistaPrevia; //Url temporal
	//Otros
	private List<CfgDocExpedienteDescripcion> listaCfgDescripcionesTipoDoc = Collections.emptyList();
	private Long cfgDocExpTramitacionId;
	private Long cfgDocExpDescripcionId;
	
	//-------------------
	
	public String getNombreFicheroVistaPrevia() {
		return this.getDescripcion() + "_" + this.getClaveVistaPrevia() + ".pdf";
	}
		
}
