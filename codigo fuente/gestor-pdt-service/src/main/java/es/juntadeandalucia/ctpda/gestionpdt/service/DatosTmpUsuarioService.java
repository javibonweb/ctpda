package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.DatosTmpUsuario;
import es.juntadeandalucia.ctpda.gestionpdt.model.QDatosTmpUsuario;
import es.juntadeandalucia.ctpda.gestionpdt.model.Usuario;
import es.juntadeandalucia.ctpda.gestionpdt.repository.DatosTmpUsuarioRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DatosTmpUsuarioService extends AbstractCRUDService<DatosTmpUsuario>{

	private static final long serialVersionUID = 1L;

	private DatosTmpUsuarioRepository datosTmpUsuarioRepository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	/**
	 * Constructor que inyecta bean de calculos matematicos y el resto de beans que hacen falta
	 * */
	public DatosTmpUsuarioService(@Autowired MathsQueryService mathsQueryService, @Autowired DatosTmpUsuarioRepository datosTmpUsuarioRepository){
		super(mathsQueryService, 
				datosTmpUsuarioRepository,
				QDatosTmpUsuario.datosTmpUsuario);
		//también lo guardo para mi por si quiero hacer consultas personalizadas.
		this.datosTmpUsuarioRepository=datosTmpUsuarioRepository;
	}
		
	@Override
	public void checkSiPuedoGrabar(DatosTmpUsuario dto) throws BaseException {
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

	//-----------------------------------------------
	//Métodos públicos
	@Transactional(value=TxType.REQUIRES_NEW, rollbackOn = Exception.class)
	public DatosTmpUsuario guardarBytesEnTmp(byte[] bytes, String nombreTmp, String mimeType) throws BaseException {
		return this.guardarBytesEnTmpImpl(bytes, nombreTmp, mimeType);
	}
	
	@Transactional(value=TxType.REQUIRES_NEW, rollbackOn = Exception.class)
	public DatosTmpUsuario guardarBytesOdtEnTmp(byte[] bytes, String nombreTmp) throws BaseException {
		return this.guardarBytesEnTmpImpl(bytes, nombreTmp, "application/vnd.oasis.opendocument.text");
	}
	
	@Transactional(value=TxType.REQUIRES_NEW, rollbackOn = Exception.class)
	public DatosTmpUsuario guardarBytesPdfEnTmp(byte[] bytes, String nombreTmp) throws BaseException {
		return this.guardarBytesEnTmpImpl(bytes, nombreTmp, "application/pdf");
	}
	
	/** 
	 * Puede que este metodo haga falta para la funcionalidad de vista previa.
	 * Lo mantenemos miestras esta esté en pruebas.
	@Override
	public DatosTmpUsuario guardar(DatosTmpUsuario tmp) throws BaseException {
		tmp = super.guardar(tmp);
		//this.crudRepository.flush();
		return tmp;
	}*/
	
	
	//----------
	//Impl: Implementaciónde la operación
	private DatosTmpUsuario guardarBytesEnTmpImpl(byte[] bytes, String nombreTmp, String mimeType) throws BaseException {
		final DatosTmpUsuario tmp = this.getDatosUsuario();
		tmp.limpiar();
		tmp.setBytes(bytes);
		tmp.setNombre(nombreTmp);
		tmp.setTipoContenido(mimeType); //Los ficheros generados son odt.
		return this.guardar(tmp);
	}
		
	public DatosTmpUsuario getDatosUsuario(Usuario u) {
		DatosTmpUsuario tmp = datosTmpUsuarioRepository.getByUsuarioId(u.getId());
		
		if(tmp == null) {
			tmp = new DatosTmpUsuario();
			tmp.setUsuario(u);
		}
		
		return tmp;
	}
	
	public DatosTmpUsuario getDatosUsuario() {
		return getDatosUsuario(usuarioService.findUsuarioLogado());
	}
	 
}
