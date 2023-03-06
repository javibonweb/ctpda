package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.AccesosDocumentos;
import es.juntadeandalucia.ctpda.gestionpdt.model.Documentos;
import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.DocumentosExpedientesTramites;
import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.QAccesosDocumentos;
import es.juntadeandalucia.ctpda.gestionpdt.model.QExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.QTareasExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.QTramiteExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.QUsuario;
import es.juntadeandalucia.ctpda.gestionpdt.model.QValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.model.TareasExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.TramiteExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.Usuario;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.repository.AccesosDocumentosRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.util.FechaUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccesosDocumentosService extends AbstractCRUDService< AccesosDocumentos>{
	
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private DocumentosExpedientesService documentosExpedientesService;
	@Autowired
	private DocumentosService documentosService;
	@Autowired
	private ValoresDominioService valoresDominioService;
	/**
	 * Constructor que inyecta bean de calculos matematicos y el resto de beans que hacen falta
	 * */
	public AccesosDocumentosService(@Autowired MathsQueryService mathsQueryService, @Autowired AccesosDocumentosRepository accesosDocumentosRepository){
		super(mathsQueryService,
				accesosDocumentosRepository,
				QAccesosDocumentos.accesosDocumentos);
		this.joinBuilder = query -> {
			final QAccesosDocumentos qAcc = QAccesosDocumentos.accesosDocumentos;
			final QUsuario qUsu = new QUsuario("qUsu");
			final QValoresDominio qValDomTipoAcceso = new QValoresDominio("qValDomTipoAcceso");
			final QExpedientes qExp = new QExpedientes("qExp");
			final QTramiteExpediente qTram = new QTramiteExpediente("qTram");
			final QTareasExpediente qTarea = new QTareasExpediente("qTarea");
						
			query.innerJoin(qAcc.usuario, qUsu).fetchJoin();
			query.innerJoin(qAcc.valorTipoAcceso, qValDomTipoAcceso).fetchJoin();
			query.innerJoin(qAcc.expediente, qExp).fetchJoin();
			query.leftJoin(qAcc.tramite, qTram).fetchJoin();
			query.leftJoin(qAcc.tarea, qTarea).fetchJoin();
			
			return query;
		};

	}	
	
	@Override
	public void checkSiPuedoGrabar(AccesosDocumentos dto) throws BaseException {
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

	//****************************
	
	
	//****************************
	
	private AccesosDocumentos nuevoAcceso(Usuario usu, Documentos doc, Expedientes exp, TramiteExpediente tramExp, TareasExpediente tarea, ValoresDominio tipoAcceso) {
		AccesosDocumentos acc = new AccesosDocumentos();
		
		acc.setUsuario(usu);
		acc.setDocumento(doc);
		acc.setNVersionDoc(doc.getNVersion());
		acc.setExpediente(exp);
		acc.setTramite(tramExp);
		acc.setTarea(tarea);
		acc.setValorTipoAcceso(tipoAcceso);
		acc.setEvolucion(Boolean.FALSE);
		acc.setResolucion(Boolean.FALSE);
		acc.setFechaHoraAcceso(FechaUtils.ahora());
		
		return acc;
	}
	
	private AccesosDocumentos nuevoAccesoEvolucion(Usuario usu, Documentos doc, Expedientes exp, TramiteExpediente tramExp, TareasExpediente tarea) {
		ValoresDominio tipoAcceso = findTipoAcceso(AccesosDocumentos.TIPO_ACCESO_CONSULTA);
		AccesosDocumentos acc = nuevoAcceso(usu, doc, exp, tramExp, tarea, tipoAcceso);
		acc.setEvolucion(Boolean.TRUE);
		
		return acc;
	}
	
	private AccesosDocumentos nuevoAccesoResolucion(Usuario usu, Documentos doc, Expedientes exp, TramiteExpediente tramExp, TareasExpediente tarea) {
		ValoresDominio tipoAcceso = findTipoAcceso(AccesosDocumentos.TIPO_ACCESO_CONSULTA);
		AccesosDocumentos acc = nuevoAcceso(usu, doc, exp, tramExp, tarea, tipoAcceso);
		acc.setResolucion(Boolean.TRUE);
		
		return acc;
	}
	
	private ValoresDominio findTipoAcceso(String codTipoAcceso) {
		return valoresDominioService.findValoresDominioByCodigoDomCodValDom("TIP_ACC",codTipoAcceso);
	}
	// Acceso pestaña Documentos
	@Transactional(TxType.REQUIRED)
	public void registrarAccesoDocumentos(Usuario usu, Documentos doc, Expedientes exp, String codTipoAcceso) throws BaseException {
		ValoresDominio tipoAcceso = findTipoAcceso(codTipoAcceso);
		this.guardar(nuevoAcceso(usu, doc, exp, null, null, tipoAcceso));
	}
	
	//Acceso pestaña Tramitación
	@Transactional(TxType.REQUIRED)
	public void registrarAccesoTramitacion(Usuario usu, Documentos doc, Expedientes exp, TramiteExpediente tramExp, String codTipoAcceso) throws BaseException {
		ValoresDominio tipoAcceso = findTipoAcceso(codTipoAcceso);
		this.guardar(nuevoAcceso(usu, doc, exp, tramExp, null, tipoAcceso));
	}
	
	//Acceso pestaña Evolución
	@Transactional(TxType.REQUIRED)
	public void registrarAccesoEvolucion(Usuario usu, Documentos doc, Expedientes exp, TramiteExpediente tramExp) throws BaseException {
		this.guardar(nuevoAccesoEvolucion(usu, doc, exp, tramExp, null));
	}
	
	//Acceso Mi mesa de tareas
	@Transactional(TxType.REQUIRED)
	public void registrarAccesoMiMesa(Usuario usu, TareasExpediente tarea) throws BaseException {
		DocumentosExpedientesTramites docExpTram = tarea.getDocumentoExpedienteTramite();
		
		DocumentosExpedientes docExp = documentosExpedientesService.obtener(docExpTram.getDocumentoExpediente().getId());
		Expedientes exp = docExp.getExpediente();
		TramiteExpediente tramExp = docExpTram.getTramiteExpediente();
		
		Documentos doc = docExp.getDocumento();
		String codTipoAcceso = esAccesoEdicion(doc)? AccesosDocumentos.TIPO_ACCESO_EDICION : AccesosDocumentos.TIPO_ACCESO_CONSULTA;
		ValoresDominio tipoAcceso = findTipoAcceso(codTipoAcceso);

		this.guardar(nuevoAcceso(usu, doc, exp, tramExp, tarea, tipoAcceso));
	}
	
	//Acceso documento resolución
	@Transactional(TxType.REQUIRED)
	public void registrarAccesoDocumentoResolucion(Usuario usu, Documentos doc, Expedientes exp) throws BaseException {
		this.guardar(nuevoAccesoResolucion(usu, doc, exp, null, null));
	}
	
	/** Si un documento es editable, según su atributo "Editable" y si es una extensión válida para editar (odt, doc, etc.).
	 * Esto es independiente de si existen otras condiciones en la aplicación además de estas.
	 */
	public boolean esAccesoEdicion(Documentos doc) {
		return documentosService.esDocumentoEditable(doc);
	}	
	
}
