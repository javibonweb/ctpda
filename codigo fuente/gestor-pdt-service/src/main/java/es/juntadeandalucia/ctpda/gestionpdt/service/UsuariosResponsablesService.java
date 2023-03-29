package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;

import es.juntadeandalucia.ctpda.gestionpdt.model.QUsuariosResponsables;
import es.juntadeandalucia.ctpda.gestionpdt.model.ResponsablesTramitacion;
import es.juntadeandalucia.ctpda.gestionpdt.model.UsuariosResponsables;
import es.juntadeandalucia.ctpda.gestionpdt.repository.UsuariosResponsablesRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UsuariosResponsablesService extends AbstractCRUDService<UsuariosResponsables>{
	
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	private UsuariosResponsablesRepository usuariosResponsablesRepository;
	
	@Autowired
	private ResponsablesTramitacionService responsablesTramitacionService;

	private transient Comparator<ResponsablesTramitacion> comparadorResponsables;
	
	@PostConstruct
	private void initComparador() {
		comparadorResponsables = (rt0, rt1) -> {
				if(rt0 == null && rt1 == null) { //no debería ocurrir
					return 0;
				}
				
				if(rt0 != null && rt1 == null) { //no debería ocurrir
					return 1;
				}
				
				if(rt0 == null) { //no debería ocurrir
					return -1;
				}
				
				//Descripción es siempre != null
				return rt0.getDescripcion().compareTo(rt1.getDescripcion());
			};
	}
	
	/**
	 * Constructor que inyecta bean de calculos matematicos y el resto de beans que hacen falta
	 * */
	public UsuariosResponsablesService(@Autowired MathsQueryService mathsQueryService, @Autowired UsuariosResponsablesRepository usuariosResponsablesRepository){
		super(mathsQueryService,
				usuariosResponsablesRepository,
				QUsuariosResponsables.usuariosResponsables);
		//también lo guardo para mi por si quiero hacer consultas personalizadas.
		this.usuariosResponsablesRepository=usuariosResponsablesRepository;
	}
	

	
	@Override
	public void checkSiPuedoGrabar(UsuariosResponsables dto) throws BaseException {
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

	//****************************************
	
	public List<UsuariosResponsables> findByUsuarioId(Long idUsuario){
		return this.usuariosResponsablesRepository.findByUsuarioId(idUsuario);
	}
	
	public boolean esResponsableDeUsuario(Long idResponsable, Long idUsuario) {
		if(idResponsable == null || idUsuario == null) {
			return false;
		}
		
		BooleanExpression qIdResp = QUsuariosResponsables.usuariosResponsables.responsable.id.eq(idResponsable);
		BooleanExpression qIdUsr = QUsuariosResponsables.usuariosResponsables.usuarioResponsable.id.eq(idUsuario);
				
		return this.crudRepository.exists(qIdResp.and(qIdUsr));
	}
	
	
	//--------------------------------------------------
	//Método para desplegable de búsqueda en "Mi mesa"
	
	public List<UsuariosResponsables> findResponsablesYEquiposUsuario(Long idUsuario){
		final List<UsuariosResponsables> responsablesUsuario = this.findByUsuarioId(idUsuario);
		
		//Completamos la lista con los equipos de cada responsable.
		anyadirEquipos(responsablesUsuario);
		
		return responsablesUsuario;
	}
	
	private Set<ResponsablesTramitacion> obtenerEquipos(List<UsuariosResponsables> responsablesUsuario) {
		final Set<ResponsablesTramitacion> equipos = new TreeSet<>(this.comparadorResponsables);
		
		for(UsuariosResponsables usuResp : responsablesUsuario) {
			equipos.addAll(
					responsablesTramitacionService.findEquipoResponsable(
							usuResp.getResponsable().getId()));
		}
		
		return equipos;	
	}
	
	private void anyadirEquipos(List<UsuariosResponsables> responsablesUsuario) {
		obtenerEquipos(responsablesUsuario).stream()
			.map(this::toUsuResp).forEach(responsablesUsuario::add);
	}
	
	private UsuariosResponsables toUsuResp(ResponsablesTramitacion resp) {
		final UsuariosResponsables ur = new UsuariosResponsables();
		ur.setPorDefecto(false); //no hace falta, pero lo explicitamos
		ur.setResponsable(resp);
		
		return ur;
	}
	
}

