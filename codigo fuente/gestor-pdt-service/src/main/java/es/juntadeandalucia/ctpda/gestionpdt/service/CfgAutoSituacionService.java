package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.CfgAutoSituacion;
import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.QCfgAutoSituacion;
import es.juntadeandalucia.ctpda.gestionpdt.model.TipoTramite;
import es.juntadeandalucia.ctpda.gestionpdt.model.TramiteExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.repository.CfgAutoSituacionRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.ValidacionException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CfgAutoSituacionService extends AbstractCRUDService<CfgAutoSituacion>{
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private SituacionesExpedientesService situacionesExpedientesService;
	@Autowired
	private TramiteExpedienteService tramiteExpedienteService;
	@Autowired
	private ExpedientesService expedientesService;
	@Autowired
	private ValoresDominioService valoresDominioService;
	
	private CfgAutoSituacionRepository cfgAutoSituacionRepository;
	
	/**
	 * Constructor que inyecta bean de calculos matematicos y el resto de beans que hacen falta
	 * */
	public CfgAutoSituacionService(@Autowired MathsQueryService mathsQueryService, @Autowired CfgAutoSituacionRepository cfgAutoSituacionRepository){
		super(mathsQueryService, 
				cfgAutoSituacionRepository,
				QCfgAutoSituacion.cfgAutoSituacion);
		//también lo guardo para mi por si quiero hacer consultas personalizadas.
		this.cfgAutoSituacionRepository=cfgAutoSituacionRepository;
	}

	@Override
	public void checkSiPuedoGrabar(CfgAutoSituacion dto) throws BaseException {
		log.debug("Verifico si puedo grabar y elevo excepción sino");
		
	}

	@Override
	public void checkSiPuedoEliminar(Long id) throws BaseException {
		log.debug("Verifico si puedo eliminar y elevo excepción sino");		
	}

/**
 * Filtros custom.
 * Se pueden añadir predicados querydsl a manini, o dejar a nulo.
 * Se añade el filtro 
 * */
	@Override
	protected BooleanBuilder aniadirFiltrosCustom(List<FiltroDTO> filtros) {
		return new BooleanBuilder();
	}
	
	//Debería ser transaccional mandatory
	public ValoresDominio cambiarSituacionSegunAltaTramite(Long idTramite) throws BaseException {
		return this.cambiarSituacionSegunTramite(idTramite, null, CfgAutoSituacion.OP_ALTA);
	}
	
	//Debería ser transaccional mandatory
	public ValoresDominio cambiarSituacionSegunFinTramite(Long idTramite, String condicion) throws BaseException {
		return this.cambiarSituacionSegunTramite(idTramite, condicion, CfgAutoSituacion.OP_FIN);
	}
		
	private ValoresDominio cambiarSituacionSegunTramite(Long idTramite, String condicion, String operacion) throws BaseException {
		TramiteExpediente tramiteExp = tramiteExpedienteService.obtener(idTramite);
		
		Expedientes expediente = tramiteExp.getExpediente();
		
		ValoresDominio valorTipoExp = expediente.getValorTipoExpediente();
		ValoresDominio valorSituacion = expediente.getValorSituacionExpediente();
		TipoTramite tipoTramite = tramiteExp.getTipoTramite();
		
		if(log.isDebugEnabled()) {
			//Para mostrar el código tengo que recuperar el objeto valor de bd
			valorSituacion = valoresDominioService.obtener(valorSituacion.getId());
			log.debug("Trámite: " + tramiteExp.getDescripcion() + " ; " +
					"Tipo expedte.: " + valorTipoExp.getCodigo() + " ; " + 
					"Tipo trámite: " + tipoTramite.getCodigo() + " ; " +
					"Sit. origen: " + valorSituacion.getCodigo() + " ; " +
					"Cod. condición: " + condicion);
		}
		
		CfgAutoSituacion cfgAuto = this.obtenerCfg(
						valorTipoExp.getId(), tipoTramite.getId(), valorSituacion.getId(), condicion, operacion);

		if(cfgAuto == null) {
			log.debug("CfgAuto no encontrado, no hacemos nada");
			return null; //no hacemos nada
		}
		
		
		//TODA ESTA PARTE DEBERIA ESTAR EN SERVICE
		//Hay configuración, seguimos
		log.debug("CfgAuto encontrado, comprobando configuración de situaciones");
		if(situacionesExpedientesService.esSituacionFinal(valorTipoExp.getId(), valorSituacion.getId())){
			comprobarTramitesPendientes(expediente.getId(), tramiteExp.getId());
		}
		
		//Ok, procedemos al cambio de situación
		log.debug("Comprobaciones ok, cambiamos la situación de " + valorSituacion.getCodigo() + " a " + cfgAuto.getValorSituacionDestino().getCodigo());
		expediente.setValorSituacionExpediente(cfgAuto.getValorSituacionDestino());
		//FIN ---------
		
		expedientesService.guardar(expediente);
		
		return cfgAuto.getValorSituacionDestino();
	}
	
	private CfgAutoSituacion obtenerCfg(Long valorTipoExpId, Long tipoTramiteId, Long valorSituacionId, String condicion, String operacion) throws ValidacionException {
		List<CfgAutoSituacion> listaCfg = this.findCfgAutoSituaciones(valorTipoExpId, tipoTramiteId, valorSituacionId, condicion, operacion);
		
		switch(listaCfg.size()) {
		case 0: return null;
		case 1: return listaCfg.get(0); //ok
		default://Error cfg
			throw new ValidacionException("Configuración de situación incorrecta");//MSJ_001
		}

	}
	
	private List<CfgAutoSituacion> findCfgAutoSituaciones(Long valorTipoExpedienteId, Long tipoTramiteId, Long valorSituacionOrigen, String condicion, String operacion) {
		return condicion == null?
				this.cfgAutoSituacionRepository.findByValorTipoExpedienteIdAndTipoTramiteIdAndValorSituacionOrigenIdAndCondicionNullAndOperacion(valorTipoExpedienteId, tipoTramiteId, valorSituacionOrigen, operacion) 
				: this.cfgAutoSituacionRepository.findByValorTipoExpedienteIdAndTipoTramiteIdAndValorSituacionOrigenIdAndCondicionAndOperacion(valorTipoExpedienteId, tipoTramiteId, valorSituacionOrigen, condicion, operacion);
	}
	
	private void comprobarTramitesPendientes(Long expedienteId, Long tramiteExpId) throws ValidacionException {
		log.debug("Configuración de situaciones encontrada, comprobando trámites activos no finalizados");
		boolean existeTramite = tramiteExpedienteService.existeTramiteActivoNoFinalizadoDistinto(expedienteId, tramiteExpId);
		
		if(existeTramite) {
			throw new ValidacionException("Tramites incompletos");//MSJ_001
		}
	}

	public CfgAutoSituacion obtenerCfgFinalDeTramite(Long idTramite, String condicion) throws ValidacionException {
		TramiteExpediente tramiteExp = tramiteExpedienteService.obtener(idTramite);
		
		Expedientes expediente = tramiteExp.getExpediente();
		ValoresDominio valorTipoExp = expediente.getValorTipoExpediente();
		ValoresDominio valorSituacion = expediente.getValorSituacionExpediente();
		TipoTramite tipoTramite = tramiteExp.getTipoTramite();

		return this.obtenerCfg(
				valorTipoExp.getId(), tipoTramite.getId(), valorSituacion.getId(), condicion, CfgAutoSituacion.OP_FIN);
	}
	
}

