package es.juntadeandalucia.ctpda.gestionpdt.repository;

import java.io.Serializable;
import java.util.List;

import es.juntadeandalucia.ctpda.gestionpdt.model.CfgDocExpedienteTramitacion;
import es.juntadeandalucia.ctpda.gestionpdt.model.TipoDocumento;
import es.juntadeandalucia.ctpda.gestionpdt.model.TipoTramite;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;


public interface CfgDocExpedienteTramitacionRepositoryCustom extends Serializable  {

	List<TipoDocumento> findTiposDocumentos(ValoresDominio tipoExp, ValoresDominio situacion, TipoTramite tipoTramite, TipoTramite tipoTramiteSup);
	List<CfgDocExpedienteTramitacion> findTiposDocumentosCfg(ValoresDominio tipoExp, ValoresDominio situacion, TipoTramite tipoTramite, TipoTramite tipoTramiteSup, String codOrigen);
	
}
