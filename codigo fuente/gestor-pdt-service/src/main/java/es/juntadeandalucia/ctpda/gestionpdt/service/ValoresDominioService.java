package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.QValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.repository.ValoresDominioRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ValoresDominioService extends AbstractCRUDService<ValoresDominio> {

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String COD_AUTCOMP = "AUT_COMP";
	public static final String COD_COM = "COM";
	public static final String COD_TIPADM = "TIP_ADM";
	public static final String COD_ARTIC = "ARTICULO";
	public static final String COD_DERRECL = "DER_RECL";
	public static final String COD_TIP_INT = "TIP_INT";
	public static final String COD_ACTO_REC = "ACTO_REC";
	
	
	
	private ValoresDominioRepository valoresDominioRepository;
	
	protected ValoresDominioService(@Autowired MathsQueryService mathsQueryService,
			@Autowired ValoresDominioRepository valoresDominioRepository) {
		super(mathsQueryService, valoresDominioRepository, QValoresDominio.valoresDominio);
		// tambien lo guardo para mi por si quiero hacer consultas personalizadas
		this.valoresDominioRepository= valoresDominioRepository;
		
		this.joinBuilder= query ->
			query
		;
	}

	@Override
	public void checkSiPuedoGrabar(ValoresDominio dto) throws BaseException {
		log.debug("Verifico si puedo grabar y elevo excepción sino");
		
	}

	@Override
	public void checkSiPuedoEliminar(Long id) throws BaseException {
		log.debug("Verifico si puedo eliminar y elevo excepción sino");
		
	}
	

	
	public List<ValoresDominio> findValoresDominioByIdDescripcion(Long idValDom, Long idDom, String desc){
		return valoresDominioRepository.findValoresDominioByIdDescripcion(idValDom, idDom, desc);
	}
	
	public List<ValoresDominio> findValoresDominioByIdCodigo(Long idValDom, Long idDom, String cod){
		return valoresDominioRepository.findValoresDominioByIdCodigo(idValDom,idDom, cod);
	}
	
	public List<ValoresDominio> findValoresDominioByDescripcion(Long idDom,String desc){
		return valoresDominioRepository.findValoresDominioByDescripcion(idDom, desc);
	}

	public ValoresDominio findValorDominioById(Long idValDom){
		return valoresDominioRepository.findValorDominioById(idValDom);
	}
	
	public List<ValoresDominio> findValoresDominioByCodigo(Long idDom, String cod){
		return valoresDominioRepository.findValoresDominioByCodigo(idDom,cod);
	}
	
	//Buscar por id
	public List<ValoresDominio> findValoresDominioActivosByDominio(Long idDominio){
		return valoresDominioRepository.findValoresDominioActivosByDominio(idDominio);
	}
	
	//Buscar por código
	public List<ValoresDominio> findValoresDominioActivosByCodigoDominio(String codigoDominio){
		return valoresDominioRepository.findValoresDominioActivosByCodigoDominio(codigoDominio);
	}
	
	public List<String> findValoresDominioDescActivosByCodigoDominio(String codigoDominio){
		return valoresDominioRepository.findValoresDominioDescActivosByCodigoDominio(codigoDominio);
	}

	//Buscar por código ordenado por orden
	public List<ValoresDominio> findValoresDominioActivosByCodigoDominioOrden(String codigoDominio){
		return valoresDominioRepository.findValoresDominioActivosByCodigoDominioOrden(codigoDominio);
	}

	
	//********************

	public List<ValoresDominio> findValoresDominioTipoPersonaFisicaYJuridica(){
		return valoresDominioRepository.findValoresDominioTipoPersonaFisicaYJuridica();
	}
	public List<ValoresDominio> findValoresSexo(){
		return valoresDominioRepository.findValoresSexo();
	}
	public List<ValoresDominio> findValoresTipoExpediente(){
		return valoresDominioRepository.findValoresTipoExpediente();
	}
	
	public List<ValoresDominio> findValoresTipoExpedienteSinXPC(){
		return valoresDominioRepository.findValoresTipoExpedienteSinXPC();
	}
	
	public List<ValoresDominio> findValoresSituacion(){
		return valoresDominioRepository.findValoresSituacion();
	}
	
	public List<ValoresDominio> findValoresSituacionXPC(){
		return valoresDominioRepository.findValoresSituacionXPC();
	}
	
	
	public List<ValoresDominio> findValoresInfraccion(){
		return valoresDominioRepository.findValoresInfraccion();
	}
	public List<ValoresDominio> findValoresMaterias(){
		return valoresDominioRepository.findValoresMaterias();
	}
	public List<ValoresDominio> findValoresCanalEntrada(){
		return valoresDominioRepository.findValoresCanalEntrada();
	}
	public List<ValoresDominio> findValoresMotivosRelacionPersonaExpediente(){
		return valoresDominioRepository.findValoresMotivosRelacionPersonaExpediente();
	}
	public List<ValoresDominio> findValoresMotivosRelacionSujetosExpediente(){
		return valoresDominioRepository.findValoresMotivosRelacionSujetosExpediente();
	}	
	public List<ValoresDominio> findValoresViaComunicacion(){
		return valoresDominioRepository.findValoresDominioActivosByCodigoDominio("COM");
	}
	public List<ValoresDominio> findValoresTipoIdentificador(){
		return valoresDominioRepository.findValoresDominioActivosByCodigoDominio("TIP_IDE");
	}
	public List<ValoresDominio> findValoresTipologia(){
		return valoresDominioRepository.findValoresDominioActivosByCodigoDominio("TIPOLO");
	}
	public List<ValoresDominio> findValoresMotivosRelacionExpediente(){
		return valoresDominioRepository.findValoresDominioActivosByCodigoDominio("MOT_REL");
	}
	public List<ValoresDominio> findValoresTiposTarea(){
		return valoresDominioRepository.findValoresDominioActivosByCodigoDominio("TAREAS");
	}
	public List<ValoresDominio> findValoresTipoObservacionExpediente(){
		return valoresDominioRepository.findValoresDominioActivosByCodigoDominio("TIP_OBS");
	}
	public List<ValoresDominio> findValoresInstructoresAPI(){
		return valoresDominioRepository.findValoresDominioActivosByCodigoDominio("INSTAPI");
	}
	public List<ValoresDominio> findValoresCategoriasDocumento(){
		return valoresDominioRepository.findValoresDominioActivosByCodigoDominioOrden("CAT_DOC");
	}
	
	public List<ValoresDominio> findValoresTipoResolucion(){
		return valoresDominioRepository.findValoresDominioActivosByCodigoDominio("TIPORESOL");
	}
	public List<ValoresDominio> findValoresSentidoResolucion(){
		return valoresDominioRepository.findValoresDominioActivosByCodigoDominio("SENT_RESOL");
	}
	public List<ValoresDominio> findValoresDerechosReclamados(){
		return valoresDominioRepository.findValoresDominioActivosByCodigoDominio(COD_DERRECL);
	}
	public List<ValoresDominio> findValoresArticulosAfectados(){
		return valoresDominioRepository.findValoresDominioActivosByCodigoDominio(COD_ARTIC);
	}
	
	//----------
		
	public List<ValoresDominio> findValoresDominioByCodigoDominio(String codigoDominio){
		return valoresDominioRepository.findValoresDominioByCodigoDominio(codigoDominio);
	}
	
	public ValoresDominio findValoresDominioByCodigoDomCodValDom(String codDom, String codValDom) {
		return valoresDominioRepository.findValoresDominioByCodigoDomCodValDom(codDom, codValDom);
	}
	
	public List<Long> findIdsValDomActivosTramExp(String codigoDominio){
		return valoresDominioRepository.findIdsValDomActivosTramExp(codigoDominio);
	}
	
	//---------------------
	
	public String getCodigoTipoIdentificador(List<ValoresDominio> listaValoresDominioTipoIdentificador, Long idTipo) throws BaseException {
		//Obtenemos el valor correspondiente al id de tipo seleccionado
		final Optional<ValoresDominio> opt = listaValoresDominioTipoIdentificador.stream()
				.filter(v -> v.getId().equals(idTipo)).findFirst();
		String codigo = null;
		
		try {
			if(opt.isPresent()) {
				codigo = opt.get().getCodigo();
			}
		} catch(NoSuchElementException nse) { 
			//El id de tipo no existe. No debería ocurrir.
			throw new BaseException(nse);
		}
		
		return codigo;
	}
	
	public ValoresDominio getTipoIdentificadorNIF(List<ValoresDominio> listaValoresDominioTipoIdentificador) throws BaseException {
		//Obtenemos el valor correspondiente al codigo de tipo NIF
		final Optional<ValoresDominio> opt = listaValoresDominioTipoIdentificador.stream()
				.filter(v -> v.getCodigo().equals("NIF")).findFirst();
		ValoresDominio tipo = null;
		
		try {
			if(opt.isPresent()) {
				tipo = opt.get();
			}			
		} catch(NoSuchElementException nse) { 
			//El código de tipo NIF no existe. No debería ocurrir.
			throw new BaseException(nse);
		}
		
		return tipo;
	}
	
	//---------------------

	public List<ValoresDominio> findValoresDominioHijosActivos(Long idDominio, Long idValorDominio) {
		return valoresDominioRepository.findValoresDominioHijosActivos(idDominio,idValorDominio);	
	}
	
	public List<ValoresDominio> obtenerValoresDominioNullConMismoOrdenVisualizacion(long orden,long dominioId) {
		return valoresDominioRepository.obtenerValoresDominioNullConMismoOrdenVisualizacion(orden, dominioId);
	}
	
	public List<ValoresDominio> obtenerValoresDominioModNullConMismoOrdenVisualizacion(long orden,long dominioId, long valDomId) {
		return valoresDominioRepository.obtenerValoresDominioModNullConMismoOrdenVisualizacion(orden, dominioId,valDomId);
	}
	
	public List<ValoresDominio> obtenerValorDominioConMismoOrdenVisualizacionSinAnidamiento(long orden,long dominioId) {
		return valoresDominioRepository.obtenerValorDominioConMismoOrdenVisualizacionSinAnidamiento(orden, dominioId);
	}
	
	public List<ValoresDominio> obtenerValorDominioModConMismoOrdenVisualizacionSinAnidamiento(long orden,long dominioId, long valDomId) {
		return valoresDominioRepository.obtenerValorDominioModConMismoOrdenVisualizacionSinAnidamiento(orden, dominioId,valDomId);
	}	
	
	public List<ValoresDominio> obtenerValorDominioConMismoOrdenVisualizacion(long orden, long valDomPadreId, long dominioId) {
		return valoresDominioRepository.obtenerValorDominioConMismoOrdenVisualizacion(orden, valDomPadreId,dominioId);
	}

	public List<ValoresDominio> obtenerValorDominioModConMismoOrdenVisualizacion(long orden, long valDomPadreId, long dominioId, long valDomId) {
		return valoresDominioRepository.obtenerValorDominioModConMismoOrdenVisualizacion(orden, valDomPadreId,dominioId, valDomId);
	}

	public List<ValoresDominio> findValoresTipoExpedienteNoActualNoParam(Long idValDomTipExpActual, Long idNoTipExp){
		return valoresDominioRepository.findValoresTipoExpedienteNoActualNoParam(idValDomTipExpActual, idNoTipExp);
	}

	
	@Override
	protected BooleanBuilder aniadirFiltrosCustom(List<FiltroDTO> filtros) {
		return new BooleanBuilder();
	}

}