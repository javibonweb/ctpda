package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.DetalleExpdteTram;
import es.juntadeandalucia.ctpda.gestionpdt.model.QDetalleExpdteTram;
import es.juntadeandalucia.ctpda.gestionpdt.model.QTramiteExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.repository.DetalleExpdteTramRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DetalleExpdteTramService extends AbstractCRUDService<DetalleExpdteTram>{
	
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	private DetalleExpdteTramRepository detalleExpdteTramRepository;
	
	
	/**
	 * Constructor que inyecta bean de calculos matematicos y el resto de beans que hacen falta
	 * */
	public DetalleExpdteTramService(@Autowired MathsQueryService mathsQueryService, @Autowired DetalleExpdteTramRepository detalleExpdteTramRepository){
		super(mathsQueryService,
				detalleExpdteTramRepository,
				QDetalleExpdteTram.detalleExpdteTram);
		//también lo guardo para mi por si quiero hacer consultas personalizadas.
		this.detalleExpdteTramRepository=detalleExpdteTramRepository;
		
		this.joinBuilder = query -> {
			final QDetalleExpdteTram qDet = QDetalleExpdteTram.detalleExpdteTram;
			final QTramiteExpediente qTram = new QTramiteExpediente("qTram");

			query.innerJoin(qDet.tramiteExpediente, qTram).fetchJoin();
			
			return query;
		};

	}
	
	@Override
	public void checkSiPuedoGrabar(DetalleExpdteTram dto) throws BaseException {
		log.debug("Verifico si puedo grabar y elevo excepción sino");
	}

	@Override
	public void checkSiPuedoEliminar(Long id) throws BaseException {
		log.debug("Verifico si puedo eliminar y elevo excepción sino");		
	}
	
	//*************************************************************
	
	public DetalleExpdteTram obtenerObjeto(Long idDetExpTram) {
		return detalleExpdteTramRepository.obtenerObjeto(idDetExpTram);
	}
	
	public DetalleExpdteTram findDetalleTramiteExp(Long idExp, Long idTramExp) {
		return detalleExpdteTramRepository.findDetalleTramiteExp(idExp, idTramExp);
	}
	
	public List<DetalleExpdteTram> findDetalleTramiteExpAfectaPlazos(Long idExp, Long idTramExp)
	{
		return detalleExpdteTramRepository.findDetalleTramiteExpAfectaPlazos(idExp, idTramExp);
	}
	
	public List<DetalleExpdteTram> findDetalleTramExpSubsanaAfectaPlazos(Long idExp, String codTipoTram)
	{
		return detalleExpdteTramRepository.findDetalleTramExpSubsanaAfectaPlazos(idExp, codTipoTram);
	}
	
	public List<DetalleExpdteTram> findDetalleTramExpAfectaPlazosByCodTipoTram(Long idExp, String codTipoTram)
	{
		return detalleExpdteTramRepository.findDetalleTramExpAfectaPlazosByCodTipoTram(idExp, codTipoTram);
	}
	
	public List<DetalleExpdteTram> findAllDetalleTramExpByExp(Long idExp) {
		return detalleExpdteTramRepository.findAllDetalleTramExpByExp(idExp);
	}

	public List<DetalleExpdteTram> findDetalleTramExpByExpConExtractos(Long idExp) {
		return detalleExpdteTramRepository.findDetalleTramExpByExpConExtractos(idExp);
	}

	public List<DetalleExpdteTram> findDetalleTramExpByExpConAntec(Long idExp) {
		return detalleExpdteTramRepository.findDetalleTramExpByExpConAntec(idExp);
	}
	
	public List<DetalleExpdteTram> findDetalleTramExpActivoByExpedienteYTramiteNotifYResultNotif(Long idExpediente, String codTipoTramite, String codResultadoNotif1, String codResultadoNotif2){
		return detalleExpdteTramRepository.findDetalleTramExpActivoByExpedienteYTramiteNotifYResultNotif(idExpediente,codTipoTramite,codResultadoNotif1,codResultadoNotif2);
	}
	
	public Date findFechaMayorNotifDetalleExpActivoByExpediente(Long idExpediente,String codTipoTramite1, String codTipoTramite2) {
		return detalleExpdteTramRepository.findFechaMayorNotifDetalleExpActivoByExpediente(idExpediente,codTipoTramite1,codTipoTramite2);
	}
	
	public List<DetalleExpdteTram> findSubtramitesByExpYTipSubtramiteYTipTramiteSup(Long idExpediente, String codTipoSubTramite, String codTipoTramiteSup){
		return detalleExpdteTramRepository.findSubtramitesByExpYTipSubtramiteYTipTramiteSup(idExpediente, codTipoSubTramite, codTipoTramiteSup);
	}
	
	public Date findFechaMayorNotifDetalleExpActivoByExpedienteAndTipoTramiteSup(Long idExpediente, String codTipoSubTramite, String codTipoTramiteSup) {
		return detalleExpdteTramRepository.findFechaMayorNotifDetalleExpActivoByExpedienteAndTipoTramiteSup(idExpediente, codTipoSubTramite, codTipoTramiteSup);
	}

	public Date findFechaMayorEnvioDetalleExpActivoByExpedienteAndTipoTramiteSup(Long idExpediente, String codTipoSubTramite, String codTipoTramiteSup) {
		return detalleExpdteTramRepository.findFechaMayorEnvioDetalleExpActivoByExpedienteAndTipoTramiteSup(idExpediente, codTipoSubTramite, codTipoTramiteSup);
	}

/**
 * Filtros custom.
 * Se pueden añadir predicados querydsl a manini, o dejar a nulo.
 * Se añade el filtro 
 * */
	@Override
	protected BooleanBuilder aniadirFiltrosCustom(List<FiltroDTO> filtros) {
		final BooleanBuilder bb = new BooleanBuilder();
		
		if(filtros != null) {
			filtros.stream().filter(x -> x.getCampo().equals("#extractoNotNull")).forEach(f ->
				bb.and(QDetalleExpdteTram.detalleExpdteTram.textoExtractoExpediente.isNotNull())
			);
		}
		
		if(filtros != null) {
			filtros.stream().filter(x -> x.getCampo().equals("#antecedentesNotNull")).forEach(f ->
				bb.and(QDetalleExpdteTram.detalleExpdteTram.textoAntecedentesExpediente.isNotNull())
			);
		}
		
		return bb;
	}

}
