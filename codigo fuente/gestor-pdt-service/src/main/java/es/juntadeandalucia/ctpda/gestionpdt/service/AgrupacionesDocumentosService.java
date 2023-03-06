package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.AgrupacionesDocumentos;
import es.juntadeandalucia.ctpda.gestionpdt.model.AgrupacionesExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentosExpedientesTramites;
import es.juntadeandalucia.ctpda.gestionpdt.model.QAgrupacionesDocumentos;
import es.juntadeandalucia.ctpda.gestionpdt.repository.AgrupacionesDocumentosRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AgrupacionesDocumentosService extends AbstractCRUDService<AgrupacionesDocumentos> {

	/**
	 * Serial id
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private AgrupacionesExpedientesService agrupacionesExpedientesService;
	
	
	AgrupacionesDocumentosRepository agrupacionesDocumentosRepository;
	
	
	protected AgrupacionesDocumentosService (@Autowired MathsQueryService mathsQueryService,
			@Autowired AgrupacionesDocumentosRepository agrupacionesDocumentosRepository) {
		super(mathsQueryService, agrupacionesDocumentosRepository, QAgrupacionesDocumentos.agrupacionesDocumentos);
		// Se guarda para mi por si quiero hacer consultas personalizadas.
		this.agrupacionesDocumentosRepository = agrupacionesDocumentosRepository;
	}

	@Override
	public void checkSiPuedoGrabar(AgrupacionesDocumentos dto) throws BaseException {
		log.debug("Verifico si puedo grabar y elevo excepción sino");
		
	}

	@Override
	public void checkSiPuedoEliminar(Long id) throws BaseException {
		log.debug("Verifico si puedo eliminar y elevo excepción sino");
		
	}
	public Long obtenerNumDocumentosAgrupExpediente(Long idExp) {
		return agrupacionesDocumentosRepository.obtenerNumDocumentosAgrupExpediente(idExp);
	}
	public List<AgrupacionesDocumentos> obtenerListaDocAgrupExpediente(Long idExp){
		return agrupacionesDocumentosRepository.obtenerListaDocAgrupExpediente(idExp);
	}
	
	public AgrupacionesDocumentos obtenerDocAgrupExpediente(Long idExp) {
		return agrupacionesDocumentosRepository.obtenerDocAgrupExpediente(idExp);
	}

	
	@Override
	protected BooleanBuilder aniadirFiltrosCustom(List<FiltroDTO> filtros) {
		return new BooleanBuilder();
	}


	//**********************************************
	
	public boolean existsByAgrupacionExp(Long idAgrExp) {
		return this.agrupacionesDocumentosRepository.existsByAgrupacionExpedientesId(idAgrExp);
	}
	
	public AgrupacionesDocumentos findAgrupacion(Long idDocExpTr) {
		return this.agrupacionesDocumentosRepository.getByDocExpTram(idDocExpTr);
	}
	
	
	@Override
	@Transactional(value=TxType.REQUIRED, rollbackOn = Exception.class)
	public AgrupacionesDocumentos guardar(AgrupacionesDocumentos agr) throws BaseException {

		agr.setAgrupacionExpedientes(this.guardarAgrupacionesExp(agr));

		aplicarOrden(agr);
		
		return super.guardar(agr);
	}
	
	private AgrupacionesExpedientes guardarAgrupacionesExp(AgrupacionesDocumentos agr) throws BaseException {
		DocumentosExpedientesTramites det = agr.getDocumentosExpedientesTramites();
		AgrupacionesExpedientes agrExp = obtenerAgrupacionExpediente(det);
		if(agrExp.getId() == null) {
			agrExp = agrupacionesExpedientesService.guardar(agrExp);
		}
		
		return agrExp;
	}
	
	//Obtener el AgrupacionExpediente existente, si no, crear uno nuevo
	private AgrupacionesExpedientes obtenerAgrupacionExpediente(DocumentosExpedientesTramites det) {
		AgrupacionesExpedientes agrExp = agrupacionesExpedientesService.findAgrupacion(det);
		
		if(agrExp == null) {
			agrExp = agrupacionesExpedientesService.nuevaAgrupacion(det);
		}
		
		return agrExp;
	}
	
	private void aplicarOrden(AgrupacionesDocumentos agr) {
		if(null == agr.getId()) { //alta
			agr.setOrden(1 + this.getUltimoOrden(agr));
		} else {
			DocumentosExpedientes docExp = agr.getDocumentosExpedientesTramites().getDocumentoExpediente();
			if(docExp.cambiaCategoria()) {
				agr.setOrden(1 + this.getUltimoOrden(agr));
			}
		}
	}
	
	
	public AgrupacionesDocumentos nuevaAgrupacion(DocumentosExpedientesTramites det) {
		final AgrupacionesDocumentos agr = new AgrupacionesDocumentos();
		
		agr.setActivo(true);
		agr.setVinculado(false);
		agr.setDocumentosExpedientesTramites(det);
		
		return agr;
	}
	
	//******************************************
	
	private Long getUltimoOrden(AgrupacionesDocumentos agr) {
		final Long idAgrExp = agr.getAgrupacionExpedientes().getId();
		return (null != agr.getId())? 
				this.agrupacionesDocumentosRepository.getUltimoOrden(agr.getId(), idAgrExp)
				: this.agrupacionesDocumentosRepository.getUltimoOrden(idAgrExp);
	}
	
	
	@Override
	@Transactional(value=TxType.MANDATORY, rollbackOn = Exception.class)
	public void delete(Long id) throws BaseException {
		AgrupacionesDocumentos agrDoc = this.obtener(id);
		Long idAgrExp = agrDoc.getAgrupacionExpedientes().getId();
		
		super.delete(id);
		this.agrupacionesExpedientesService.eliminarAgrupacionSinDocumentos(idAgrExp);
	}

}
