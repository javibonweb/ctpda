package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import es.juntadeandalucia.ctpda.gestionpdt.model.ArticuloAfectadoResolucion;
import es.juntadeandalucia.ctpda.gestionpdt.model.ArticulosAfectadosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.DerechoReclamadoResolucion;
import es.juntadeandalucia.ctpda.gestionpdt.model.DerechosReclamadosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.PersonasExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.QExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.QResolucionExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.QValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.model.Resolucion;
import es.juntadeandalucia.ctpda.gestionpdt.model.ResolucionExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.ResolucionPersona;
import es.juntadeandalucia.ctpda.gestionpdt.model.ResolucionSujetoObligado;
import es.juntadeandalucia.ctpda.gestionpdt.model.SujetosObligadosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.repository.ResolucionExpedienteRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.ValidacionException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ResolucionExpedienteService extends AbstractCRUDService<ResolucionExpediente> {

	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	private ResolucionExpedienteRepository resolucionExpedienteRepository;

	@Autowired
	private ResolucionService resolucionService;
	@Autowired
	private ExpedientesService expedientesService;
	
	@Autowired
	private PersonasExpedientesService personasExpedientesService;
	@Autowired
	private SujetosObligadosExpedientesService sujetosObligadosExpedientesService;
	@Autowired
	private DerechosReclamadosExpedientesService derechosReclamadosExpedientesService;
	@Autowired
	private ArticulosAfectadosExpedientesService articulosAfectadosExpedientesService;
		
	@Autowired
	private ResolucionPersonaService resolucionPersonaService;
	@Autowired
	private ResolucionSujetoObligadoService resolucionSujetoObligadoService;
	@Autowired
	private DerechoReclamadoResolucionService derechoReclamadoResolucionService;
	@Autowired
	private ArticuloAfectadoResolucionService articuloAfectadoResolucionService;
	
	
	
	protected ResolucionExpedienteService(@Autowired MathsQueryService mathsQueryService,
			@Autowired ResolucionExpedienteRepository resolucionExpedienteRepository) {
		super(mathsQueryService, resolucionExpedienteRepository, QResolucionExpediente.resolucionExpediente);
		// también lo guardo para mi por si quiero hacer consultas personalizadas.
		this.resolucionExpedienteRepository = resolucionExpedienteRepository;

		this.joinBuilder = query -> {
			final QResolucionExpediente qresolexp = QResolucionExpediente.resolucionExpediente;
			
			final QExpedientes qExpediente = new QExpedientes("qExpediente");
			final QValoresDominio qSentidoResol = new QValoresDominio("qSentidoResol");
			
			query.innerJoin(qresolexp.expediente, qExpediente).fetchJoin();
			query.leftJoin(qresolexp.valorSentidoResolucion, qSentidoResol).fetchJoin();
			
			return query;
		};

	}

	public ResolucionExpediente findResolucionExpByIdResolucion(Long idResol)
	{
		return resolucionExpedienteRepository.findResolucionExpByIdResolucion(idResol);
	}
	
	public List<ResolucionExpediente> findListResolucionExpByIdResolucion(Long idResol)
	{
		return resolucionExpedienteRepository.findListResolucionExpByIdResolucion(idResol);
	}
	
	@Override
	public void checkSiPuedoGrabar(ResolucionExpediente dto) throws BaseException {
		log.debug("Verifico si puedo grabar y elevo excepción sino");

	}

	@Override
	public void checkSiPuedoEliminar(Long id) throws BaseException {
		log.debug("Verifico si puedo eliminar y elevo excepción sino");

	}
	
	
	@Override
	protected BooleanBuilder aniadirFiltrosCustom(List<FiltroDTO> filtros) {
		return new BooleanBuilder();
	}
	
	//****************************
	
	public List<ResolucionExpediente> findByResolucionId(Long idResol){
		return this.resolucionExpedienteRepository.findByResolucionId(idResol);
	}
	
	public ResolucionExpediente findExpedientePrincipal(Long idResolucion) {
		return this.resolucionExpedienteRepository.getByResolucionIdAndPrincipalTrue(idResolucion);
	}
		
	public ResolucionExpediente findResolucionExpByIdExpediente(Long idExpediente) {
		return this.resolucionExpedienteRepository.findResolucionExpByIdExpediente(idExpediente);
	}
	
	
	public List<ResolucionExpediente> findListResolucionExpByIdExpediente(Long idExpediente) {
		return this.resolucionExpedienteRepository.findListResolucionExpByIdExpediente(idExpediente);
	}
	
	public List<Long> findIdsExpRelacionadosByResolId(Long idResol){
		return this.resolucionExpedienteRepository.findIdsExpRelacionadosByResolId(idResol);
	}
	
	//***********************************************
	
	@Transactional(value=TxType.REQUIRED, rollbackOn = Exception.class)
	public void setPrincipal(Long idRelacion) throws BaseException {
		ResolucionExpediente nuevoPrinc = this.obtener(idRelacion);
		
		Long idResol = nuevoPrinc.getResolucion().getId();
		ResolucionExpediente expPrincipal = this.findExpedientePrincipal(idResol);
		if(null != expPrincipal) {
			expPrincipal.setPrincipal(false);
			this.guardar(expPrincipal);
		}
		
		//Esto hay que hacerlo después, para que Hibernate 
		//no lo incluya en la query de findExpedientePrincipal.
		nuevoPrinc.setPrincipal(true);		
		this.guardar(nuevoPrinc);
	}

	//---------------------------------------------------------------------------------------
	//Agregar un expediente a una resolución
	
	@Transactional(value=TxType.REQUIRED, rollbackOn = Exception.class)
	public void agregarExpediente(Long idResolucion, Long idExpediente) throws BaseException {
		Resolucion resol = resolucionService.obtener(idResolucion);
		Expedientes exp = expedientesService.obtener(idExpediente);
		
		comprobarExpedienteYaRelacionado(resol, exp);
		
		guardarNuevaRelacionExpediente(resol, exp);
		guardarNuevaRelacionExpedientePersonas(resol, exp);
		guardarNuevaRelacionExpedienteSujetos(resol, exp);
		guardarNuevaRelacionExpedienteDerechos(resol, exp);
		guardarNuevaRelacionExpedienteArticulos(resol, exp);
	}
	
	private void comprobarExpedienteYaRelacionado(Resolucion resolucion, Expedientes expedientes) throws ValidacionException {
		final Long idExp = expedientes.getId();
		List<Long> idsRelacionados = resolucionExpedienteRepository.findIdsExpRelacionadosByResolId(resolucion.getId());
		boolean relacionado = idsRelacionados.stream().anyMatch(id -> id.equals(idExp));
		
		if(relacionado) {
			throw new ValidacionException("El expediente ya se encuentra relacionado con la resolución");
		}
	}
	
	private void guardarNuevaRelacionExpediente(Resolucion resolucion, Expedientes expedientes) throws BaseException {
		ResolucionExpediente resolExp = new ResolucionExpediente();
		resolExp.setResolucion(resolucion);
		resolExp.setExpediente(expedientes);
		resolExp.setPrincipal(false);
		resolExp.setValorSentidoResolucion(null);
		this.guardar(resolExp);
	}
	
	//-------------------------
	//¿Deberían estar en sus respectivos services? (con transactional-mandatory)
	
	private void guardarNuevaRelacionExpedientePersonas(Resolucion resolucion, Expedientes expedientes) throws BaseException {
		//Personas del expediente
		List<PersonasExpedientes> personasExpediente = 
				personasExpedientesService.obtenerListaPersonaExpedientePorExpedientePrincipalYNoPrincipal(expedientes.getId());
		
		for(PersonasExpedientes persExp : personasExpediente) {
			//Crear elemento ResolucionPersona
			ResolucionPersona resolPers = new ResolucionPersona();
			resolPers.setResolucion(resolucion);
			resolPers.setPersona(persExp.getPersonas());
			resolPers.setExpediente(expedientes);
			resolPers.setPrincipal(false);
			resolPers.setFechaNotificacion(null);
			//guardar elemento
			resolucionPersonaService.guardar(resolPers);
		}
	}
	
	private void guardarNuevaRelacionExpedienteSujetos(Resolucion resolucion, Expedientes expedientes) throws BaseException {
		//Sujetos del expediente
		List<SujetosObligadosExpedientes> sujetosExpediente = 
				sujetosObligadosExpedientesService.obtenerSujetosObligadosExpedientePrincipalYNoPrincipalPorExpediente(expedientes.getId()); 
		
		for(SujetosObligadosExpedientes sujExp : sujetosExpediente) {
			//Crear elemento ResolucionSujetoObligado
			ResolucionSujetoObligado resolSuj= new ResolucionSujetoObligado();
			resolSuj.setResolucion(resolucion);
			resolSuj.setSujetoObligado(sujExp.getSujetosObligados());
			resolSuj.setExpediente(expedientes);
			resolSuj.setPrincipal(false);
			resolSuj.setFechaNotificacion(null);
			//guardar elemento
			resolucionSujetoObligadoService.guardar(resolSuj);
		}
	}
	
	private void guardarNuevaRelacionExpedienteDerechos(Resolucion resolucion, Expedientes expedientes) throws BaseException {
		//Derechos del expediente
		List<DerechosReclamadosExpedientes> derechosExpediente = 
				derechosReclamadosExpedientesService.obtenerListDerechosReclamadosExpedientePorExpediente(expedientes.getId());
		
		for(DerechosReclamadosExpedientes derExp : derechosExpediente) {
			//Crear elemento DerechoReclamadoResolucion
			DerechoReclamadoResolucion derResol = new DerechoReclamadoResolucion();
			derResol.setResolucion(resolucion);
			derResol.setValoresDerReclResol(derExp.getValoresDerReclExp());
			//guardar elemento
			derechoReclamadoResolucionService.guardar(derResol);
		}
	}
	
	private void guardarNuevaRelacionExpedienteArticulos(Resolucion resolucion, Expedientes expedientes) throws BaseException {
		List<ArticulosAfectadosExpedientes> articulosExpediente = 
				articulosAfectadosExpedientesService.obtenerListArticulosAfectadosExpedientePorExpediente(expedientes.getId());
		
		for(ArticulosAfectadosExpedientes artExp : articulosExpediente) {
			//Crear elemento ArticuloAfectadoResolucion
			ArticuloAfectadoResolucion artResol = new ArticuloAfectadoResolucion();
			artResol.setResolucion(resolucion);
			artResol.setValorArticulo(artExp.getValoresArtAfectExp());
			//guardar elemento
			articuloAfectadoResolucionService.guardar(artResol);
		}
	}

	
}
