package es.juntadeandalucia.ctpda.gestionpdt.service;

import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;

import es.juntadeandalucia.ctpda.gestionpdt.model.Personas;
import es.juntadeandalucia.ctpda.gestionpdt.model.PersonasRepresentantes;
import es.juntadeandalucia.ctpda.gestionpdt.model.QPersonas;
import es.juntadeandalucia.ctpda.gestionpdt.repository.PersonasRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PersonasService extends AbstractCRUDService<Personas>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private PersonasRepository personasRepository;
	
	@Autowired
	private PersonasRepresentantesService personasRepresentantesService;
	
	/**
	 * Constructor que inyecta bean de calculos matematicos y el resto de beans que hacen falta
	 * */
	public PersonasService(@Autowired MathsQueryService mathsQueryService, @Autowired PersonasRepository personasRepository){
		super(mathsQueryService, 
				personasRepository,
				QPersonas.personas);
		//también lo guardo para mi por si quiero hacer consultas personalizadas.
		this.personasRepository=personasRepository;
	}
	
	public Personas nuevaPersona() {
		Personas p = new Personas();
		
		p.setActiva(Boolean.TRUE);
		//No se tienen en cuenta sexo, tipo, etc.
		
		return p;
	}
	
	public List<Personas> findPersonasByTipIdentif(long idTipIdentif)
	{
		return personasRepository.findPersonasByTipIdentif(idTipIdentif);
	}
	
	
	@Override
	public void checkSiPuedoGrabar(Personas dto) throws BaseException {
		log.debug("Verifico si puedo grabar y elevo excepción si no");
	}

	@Override
	public void checkSiPuedoEliminar(Long id) throws BaseException {
		log.debug("Verifico si puedo eliminar y elevo excepción si no");
	}
	
	public String calcularIniciales(Personas p) {
		final int TAM_INIC = 3;
	
		String i = "";

		if (!StringUtils.isEmpty(p.getNombreRazonsocial())) {
			i += StringUtils.inic(p.getNombreRazonsocial(), TAM_INIC);
		}

		if (!StringUtils.isEmpty(p.getPrimerApellido())) {
			i += StringUtils.inic(p.getPrimerApellido(), TAM_INIC);
		}

		if (!StringUtils.isEmpty(p.getSegundoApellido())) {
			i += StringUtils.inic(p.getSegundoApellido(), TAM_INIC);
		}

		String inic = StringUtils.left(i, 5);
		
		// Tenemos asegurado que this.iniciales contienen dato
		return inic.toUpperCase();
	}
	
	
	
	
/**
 * Filtros custom.
 * Se pueden añadir predicados querydsl a manini, o dejar a nulo.
 * Se añade el filtro 
 * */
	@Override
	protected BooleanBuilder aniadirFiltrosCustom(List<FiltroDTO> filtros) {
		final BooleanBuilder bb = new BooleanBuilder();
		
		filtros.stream().filter(fx-> fx.getCampo().equals("#pers_filtro")).forEach(
				fx -> {
					if (fx.getValue()!=null) {
						BooleanExpression be = QPersonas.personas.nifCif.containsIgnoreCase(fx.getValue().toString());
						bb.and(be);
					}
				}
			);

		filtros.stream().filter(fx-> fx.getCampo().equals("#nombreAp")).forEach(
				fx -> {
					if (fx.getValue()!=null) {
						String filtro = fx.getValue().toString();
						BooleanExpression be = QPersonas.personas.nombreRazonsocial
												.concat(StringUtils.SPACE)
												.concat(QPersonas.personas.primerApellido)
												.concat(StringUtils.SPACE)
												.concat(QPersonas.personas.segundoApellido)
												.toUpperCase()
												.like(Expressions.stringTemplate("'%'")
										               .concat(Expressions.stringTemplate("{0}", filtro.toUpperCase())
										            		   .concat(Expressions.stringTemplate("'%'"))));
						bb.and(be);
					}
				}
			);
		
		filtros.stream().filter(fx-> fx.getCampo().equals("#notID")).forEach(
				fx -> {
					if (fx.getValue()!=null) {
						BooleanExpression be = QPersonas.personas.id.ne((Long)fx.getValue());
						bb.and(be);
					}
				}
			);
		
		filtros.stream().filter(fx-> fx.getCampo().equals("#notRepre")).forEach(
				fx -> {
					if (fx.getValue()!=null) {
						BooleanExpression be = QPersonas.personas.id.notIn((List)fx.getValue());
						bb.and(be);
					}
				}
			);
		return bb;
	}

	@Override
	@Transactional(value=TxType.REQUIRED)
	public Personas guardar(Personas p) throws BaseException {
		prepararPersona(p);
		
		if(p.getId()!=null && p.getEsPF()) {
			eliminarRepresentantes(p);
		}
		
		return super.guardar(p);
	}
	
	private void prepararPersona(Personas p) {
		if(!p.getEsPF()) {
			p.setPrimerApellido(null);
			p.setSegundoApellido(null);
			p.setIniciales(null);
			p.setValorSexo(null);
		} else {
			String inic=p.getIniciales();
			if (StringUtils.isEmpty(inic)) {
				p.setIniciales(this.calcularIniciales(p));
			}

			//No se tiene en cuenta sexo, provincia, etc.
		}
		
		if(!StringUtils.isBlank(p.getNifCif())){
			p.setNifCif(p.getNifCif().toUpperCase());
		}
	}
	
	private void eliminarRepresentantes(Personas p) throws BaseException {
		List<PersonasRepresentantes> listaPR = personasRepresentantesService.findRepresentantes(p.getId());
		
		for(PersonasRepresentantes pr : listaPR) {
			personasRepresentantesService.delete(pr.getId());
		}
	}
	
	public Personas desactivar(Personas p) throws BaseException {
		p.setActiva(false);
		return super.guardar(p);
	}

}
