package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.CfgTareas;
import es.juntadeandalucia.ctpda.gestionpdt.model.QCfgTareas;
import es.juntadeandalucia.ctpda.gestionpdt.repository.CfgTareasRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CfgTareasService extends AbstractCRUDService<CfgTareas>{
	
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String MSG_NO_CFG = "No se encuentra el registro de configuración. Consulte con su administrador.";
	
	private CfgTareasRepository cfgTareasRepository;
	
	/**
	 * Constructor que inyecta bean de calculos matematicos y el resto de beans que hacen falta
	 * */
	public CfgTareasService(@Autowired MathsQueryService mathsQueryService, @Autowired CfgTareasRepository cfgTareasRepository){
		super(mathsQueryService,
				cfgTareasRepository,
				QCfgTareas.cfgTareas);
		//también lo guardo para mi por si quiero hacer consultas personalizadas.
		this.cfgTareasRepository=cfgTareasRepository;
	}
	

	
	@Override
	public void checkSiPuedoGrabar(CfgTareas dto) throws BaseException {
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
	
	public List<CfgTareas> findTiposTareasActivasCfg(Long idTipoExp, Long idTipoTr, Long idTipoSubtr, boolean documento) throws BaseException{
		List<CfgTareas> res = this.cfgTareasRepository.findTiposTareasActivasCfg(idTipoExp, idTipoTr, idTipoSubtr, documento);
		if(res.isEmpty()) {
			throw new BaseException(MSG_NO_CFG);
		}
		return res;
	}
	
	public List<CfgTareas> findTiposTareasManualesActivasCfg(Long idTipoExp, Long idTipoTr, Long idTipoSubtr, boolean documento) throws BaseException{
		List<CfgTareas> res = this.cfgTareasRepository.findTiposTareasManualesActivasCfg(idTipoExp, idTipoTr, idTipoSubtr, documento);
		if(res.isEmpty()) {
			throw new BaseException(MSG_NO_CFG);
		}
		return res;
	}
	
	public List<CfgTareas> findCfgTareasSiguientes(Long idTipoExp, Long idTipoTr, Long idTipoSubtr, Long idTipoTarea, boolean documento) {
		List<CfgTareas> res = this.cfgTareasRepository.findCfgTareasSiguientes(idTipoExp, idTipoTr, idTipoSubtr, idTipoTarea, documento);
		if(res.isEmpty()) {
			log.warn(msgWarnNoCfg("findCfgTareasSiguientes", idTipoExp, idTipoTr, idTipoSubtr, idTipoTarea, documento));
		}
		return res;
	}
	
	public List<CfgTareas> findCfgTareas(Long idTipoExp, Long idTipoTr, Long idTipoSubtr, Long idTipoTarea, boolean documento) {
		List<CfgTareas> res = this.cfgTareasRepository.findCfgTareas(idTipoExp, idTipoTr, idTipoSubtr, idTipoTarea, documento);
		if(res.isEmpty()) {
			log.warn(msgWarnNoCfg("findCfgTareas", idTipoExp, idTipoTr, idTipoSubtr, idTipoTarea, documento));
		}
		return res;
	}

	public List<CfgTareas> findCfgTareasTodas(Long idTipoExp, Long idTipoTr, Long idTipoSubtr, boolean documento) {
		List<CfgTareas> res = this.cfgTareasRepository.findTiposTareasActivasCfg(idTipoExp, idTipoTr, idTipoSubtr, documento);
		if(res.isEmpty()) {
			log.warn(msgWarnNoCfg("findCfgTareasTodas", idTipoExp, idTipoTr, idTipoSubtr, null, documento));
		}
		return res;
	}
	
	
	public List<CfgTareas> findTareasByTipExpTipTramTipSubTramNull(Long idTipoExp, Long idTipoTram){
		return cfgTareasRepository.findTareasByTipExpTipTramTipSubTramNull(idTipoExp, idTipoTram);
	}


	private String msgWarnNoCfg(String metodo, Long idTipoExp, Long idTipoTr, Long idTipoSubtr, Long idTipoTarea, boolean documento) {
		final StringBuilder sb = new StringBuilder();
		
		sb.append(metodo).append(StringUtils.SPACE);
		sb.append("idTipoExp ").append(idTipoExp).append(", ");
		sb.append("idTipoTr ").append(idTipoTr).append(", ");
		sb.append("idTipoSubtr ").append(idTipoSubtr).append(", ");
		sb.append("idTipoTarea ").append(idTipoTarea).append(", ");
		sb.append("documento? ").append(documento);
		
		sb.append(" - ").append(MSG_NO_CFG);
		
		return sb.toString();
	}

	
	public boolean existeCfgTareaSiguiente(Long idTipoExp, Long idTipoTr, Long idTipoSubtr, Long idTipoTarea, boolean documento){
		return this.cfgTareasRepository.existeCfgTareaSiguiente(idTipoExp, idTipoTr, idTipoSubtr, idTipoTarea, documento);
	}

}
