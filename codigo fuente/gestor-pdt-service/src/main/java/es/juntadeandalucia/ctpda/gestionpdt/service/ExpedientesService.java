package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;

import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.QExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.TareasExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.Usuario;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.repository.ExpedientesRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.TareasExpedienteService.AccionTarea;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.ValidacionException;
import es.juntadeandalucia.ctpda.gestionpdt.util.FechaUtils;
import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class ExpedientesService extends AbstractCRUDService<Expedientes> {
	
	@Autowired
	private SituacionesExpedientesService situacionesExpedientesService;
	@Autowired
	private TareasExpedienteService tareasExpedienteService;
	@Autowired
	private UsuarioService usuarioService;
	
	private ExpedientesRepository expedientesRepository;
	
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	protected ExpedientesService(@Autowired MathsQueryService mathsQueryService,
			@Autowired ExpedientesRepository expedientesRepository) {
		super(mathsQueryService, expedientesRepository, QExpedientes.expedientes);
		this.expedientesRepository = expedientesRepository;
	}

	@Override
	public void checkSiPuedoGrabar(Expedientes dto) throws BaseException {
		log.debug("Verifico si puedo grabar y elevo excepción sino");

	}

	@Override
	public void checkSiPuedoEliminar(Long id) throws BaseException {
		log.debug("Verifico si puedo eliminar y elevo excepción sino");

	}

	@Override
	protected BooleanBuilder aniadirFiltrosCustom(List<FiltroDTO> filtros) {
		final BooleanBuilder bb = new BooleanBuilder();
		
		if(filtros != null) {
			filtros.stream().filter(x -> x.getCampo().equals("#notID")).forEach(f -> {
				if(f.getValue() != null) {
					bb.and(QExpedientes.expedientes.id.ne((Long.valueOf(f.getValue().toString()))));
				}				
			});
			
			filtros.stream().filter(fx-> fx.getCampo().equals("#notIDExpRelacionados")).forEach(
				fx -> {
					if (fx.getValue()!=null) {
						bb.and(QExpedientes.expedientes.id.notIn((List)fx.getValue()));
					}
				}
			);
			
		}		
		
		return bb;
	}

	public Optional<Expedientes> findByNumeroExpediente(String numExp) {
		final BooleanExpression whereNumExp = QExpedientes.expedientes
				.numExpediente.eq(numExp);
		return this.crudRepository.findOne(whereNumExp);
	}
	
	
	@Override
	@Transactional(value=TxType.REQUIRED, rollbackOn=Exception.class)
	public Expedientes guardar(Expedientes exp) throws BaseException {
		if(exp.getId() != null && exp.cambiaResponsable()) {
			cambioResponsableExp(exp);
		} 
		
		exp = super.guardar(exp);
		
		if(enSituacionFinal(exp)) {
			cerrarTareasExpediente(exp);
		}
		
		return exp;
	}
	
	private void cambioResponsableExp(Expedientes exp) throws BaseException {
		final Usuario usr = usuarioService.findUsuarioLogado();
		final Long idRespActual = exp.getResponsableActual().getId();
		final List<TareasExpediente> tareasExp = tareasExpedienteService.findTareasPendientesExpedienteDeResponsable(exp.getId(), idRespActual);
		
		if(!tareasExp.isEmpty()) {
			for(TareasExpediente tarea : tareasExp) {
				tarea.setResponsableTramitacion(exp.getResponsable());				
				//Para anular la detección de cambio de responsable y así evitar generar tareas derivadas de dicho cambio.
				tarea.setResponsableOriginal(exp.getResponsable());
				
				tareasExpedienteService.guardar(tarea, usr);
			}
		} else {
			final AccionTarea accion = tareasExpedienteService.nuevaAccionTareaDirecta("AEXP", usr);
			accion.setExpediente(exp);
			accion.setResponsable(exp.getResponsable());
			tareasExpedienteService.crearTareasAuto(accion);
		}		
	}
	
	private boolean enSituacionFinal(Expedientes exp) throws ValidacionException {
		ValoresDominio tipoExp = exp.getValorTipoExpediente();
		ValoresDominio situ = exp.getValorSituacionExpediente();
		return situacionesExpedientesService.esSituacionFinal(tipoExp.getId(), situ.getId());
	}
	
	private void cerrarTareasExpediente(Expedientes exp) throws BaseException {
		List<TareasExpediente> tareasExp = tareasExpedienteService.findTareasPendientesExpediente(exp.getId());
		Usuario usuario = usuarioService.findByLogin(exp.getUsuModificacion());
		String motivo = "Tarea cerrada automáticamente al finalizar el expediente. "
				+ "Usuario: " + exp.getUsuModificacion() + " y fecha: " + FechaUtils.dateToStringFechaYHora(FechaUtils.ahora());
		
		for(TareasExpediente tarea : tareasExp) {			
			tareasExpedienteService.cerrarTareaInmediatamente(tarea, usuario, motivo);
		}
	}
	
	public List<Expedientes> findExpedientesBySituacion(String codSituacion){
		return expedientesRepository.findExpedientesBySituacion(codSituacion);
	}
	
	public List<Expedientes> findExpedientesBySituacionExpNoSupervisada (){
		return expedientesRepository.findExpedientesBySituacionExpNoSupervisada();
	}
	
	public List<Expedientes> findExpedientesByTipExpSituacion(String codTipExpediente, String codSituacion){
		return expedientesRepository.findExpedientesByTipExpSituacion(codTipExpediente, codSituacion);
	}
}
