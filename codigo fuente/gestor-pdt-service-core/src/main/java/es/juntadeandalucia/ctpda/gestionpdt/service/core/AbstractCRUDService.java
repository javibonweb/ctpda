package es.juntadeandalucia.ctpda.gestionpdt.service.core;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.function.Function;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.JPQLQuery;

import es.juntadeandalucia.ctpda.gestionpdt.model.core.EntidadBasica;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.OrdenacionDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.PaginaDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.utils.FiltroQDSLUtils;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.utils.PaginacionUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * The type Abstract crud service.
 *
 * @param <E> the type parameter
 */

/** The Constant log. */
@Slf4j
@Transactional
public abstract class AbstractCRUDService<E extends EntidadBasica> implements CrudQueryService<E> {

	/** serial uid. */
	private static final long serialVersionUID = 1L;
	
	private static final String TRADICIONALQUERY = " TRADICIONAL QUERY";

	/**
	 * Servicio general para calculos matematicos.
	 */
	protected MathsQueryService mathsQueryService;

	/** Repositorio spring data a utilizar para acceder a los datos . */
	protected AbstractCrudRepository<E> crudRepository;
	/**
	 * Objeto Q base para el servicio. Con este objeto se construyen todos los
	 * filtros automaticos.
	 */
	private EntityPathBase objetoQbase;

	/**
	 * The Value type entity.
	 */
	protected Class<E> valueTypeEntity;

	/**
	 * Lambda de construcción de joins querydsl forzosos (con su posible fetch) para
	 * los listados. se puede setear en el contrauctor local de cada clase. ej:
	 * this.joinBuilder = (query) -> query.leftJoin(QMunicipio.municipio.provincia,
	 * QProvincia.provincia).fetchJoin();
	 */
	protected transient	Function<JPQLQuery<E>, JPQLQuery<E>> joinBuilder;

	/**
	 * Instantiates a new abstract CRUD service.
	 *
	 * @param mathsQueryService the maths query service
	 * @param crudRepository    the crud repository
	 * @param objetoQbase       the objeto qbase
	 */
	protected AbstractCRUDService(MathsQueryService mathsQueryService, AbstractCrudRepository<E> crudRepository,
			EntityPathBase objetoQbase) {
		this.mathsQueryService = mathsQueryService;
		this.objetoQbase = objetoQbase;
		this.crudRepository = crudRepository;
		loadMappersAndFilters();
	}

	/**
	 * Load mappers and filters.
	 */
	private void loadMappersAndFilters() {
		this.valueTypeEntity = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
	}

	/**
	 * Verifica si puedo grabar. En las clases implementadoras, realizar las
	 * comprobaciones pertinentes y elevar excepción con el mensaje que se quiera
	 * utilizar para el usuario.
	 *
	 * @param dto the dto
	 * @throws BaseException the csf backend exception
	 */
	public abstract void checkSiPuedoGrabar(E dto) throws BaseException;

	/**
	 * Verifica si puedo eliminar. En las clases implementadoras, realizar las
	 * comprobaciones pertinentes y elevar excepción con el mensaje que se quiera
	 * utilizar para el usuario.
	 *
	 * @param id the id
	 * @throws BaseException the csf backend exception
	 */
	public abstract void checkSiPuedoEliminar(Long id) throws BaseException;

	/**
	 * Find pagina all pagina dto.
	 *
	 * @param pageable   the pageable
	 * @param multiOrder the multi order
	 * @param filtros    the filtros
	 * @return the pagina dto
	 */
	public PaginaDTO<E> findPaginaAll(Pageable pageable, OrdenacionDTO multiOrder, List<FiltroDTO> filtros) {

		// Ordenacion
		Pageable customPageable = FiltroQDSLUtils.obtenerOrdenacion(pageable, multiOrder);

		// Filtrado
		BooleanBuilder predi = null;
		if (filtros != null) {
			predi = FiltroQDSLUtils.obtenerFiltro(this.objetoQbase, filtros);
		}

		predi = this.addFiltroCuston(predi, filtros);
		// Llamada a BBDD
		Page<E> organo;
		if (predi != null) {
			organo = this.findAllRepository(predi, customPageable);
		} else {
			organo = this.findAllRepository(customPageable);
		}

		return PaginacionUtils.asPaginaDTO(organo, organo.getContent());
	}

	/**
	 * Adds the filtro custon.
	 *
	 * @param predi   the predi
	 * @param filtros the filtros
	 * @return the boolean builder
	 */
	private BooleanBuilder addFiltroCuston(BooleanBuilder predi, List<FiltroDTO> filtros) {
		BooleanBuilder filtroCustom = aniadirFiltrosCustom(filtros);
		BooleanBuilder mypredi = predi;
		if (filtroCustom != null && filtroCustom.hasValue()) {
			mypredi = predi == null ? new BooleanBuilder() : predi;
			mypredi.and(filtroCustom);

		}
		return mypredi;
	}

	/**
	 * FUNCION PARA HEREDARLA Y USAR PARA CREAR LOS FILTROS.SE AÑADE COMO AND A LOS
	 * ANTERIORES
	 *
	 * @param filtros the filtros
	 * @return the boolean builder
	 */
	protected abstract BooleanBuilder aniadirFiltrosCustom(List<FiltroDTO> filtros);

	/**
	 * Find all list.
	 *
	 * @param pageable   the pageable
	 * @param multiOrder the multi order
	 * @param filtros    the filtros
	 * @return the list
	 */
	@Override
	public List<E> findAll(Pageable pageable, OrdenacionDTO multiOrder, List<FiltroDTO> filtros) {
		// Llamada a BBDD
		List<E> organo;
		if (pageable == null) {
			// Ordenacion
			Sort orden = FiltroQDSLUtils.obtenerOrdenacion(multiOrder);

			// Filtrado
			BooleanBuilder predi = null;
			if (filtros != null) {
				predi = FiltroQDSLUtils.obtenerFiltro(this.objetoQbase, filtros);
			}
			predi = this.addFiltroCuston(predi, filtros);
			if (predi != null) {
				organo = PaginacionUtils.toList(this.findAllRepository(predi, orden));
			} else {
				organo = PaginacionUtils.toList(this.findAllRepository(orden));
			}
		} else {// tratamos la paginacion
			// Ordenacion
			Pageable customPageable = FiltroQDSLUtils.obtenerOrdenacion(pageable, multiOrder);

			// Filtrado
			BooleanBuilder predi = null;
			if (filtros != null) {
				predi = FiltroQDSLUtils.obtenerFiltro(this.objetoQbase, filtros);
			}
			predi = this.addFiltroCuston(predi, filtros);
			if (predi != null) {
				organo = PaginacionUtils.toList(this.findAllRepository(predi, customPageable));
			} else {
				organo = PaginacionUtils.toList(this.findAllRepository(customPageable));
			}
		}
		return organo;

	}

	/**
	 * Obtener entidad por id.
	 *
	 * @param idL the id l
	 * @return the d
	 */
	@Override
	@Transactional(readOnly = true)
	public E obtener(Long idL) {
		return this.crudRepository.getById(idL);
	}

	/**
	 * Borrar entidad por BD.
	 *
	 * @param idL the id l
	 * @throws BaseException the csf backend exception
	 */
	public void delete(Long idL) throws BaseException {
		checkSiPuedoEliminar(idL);
		this.crudRepository.deleteById(idL);
	}

	/**
	 * Guardar entidad por BD.
	 *
	 * @param body the body
	 * @return the d
	 * @throws BaseException the csf backend exception
	 */
	@Transactional(readOnly = false)
	public E guardar(E body) throws BaseException {
		checkSiPuedoGrabar(body);
		E entidad;
		if (body.getId() != null && body.getId() > 0) {
			entidad = this.crudRepository.getById(body.getId());
			BeanUtils.copyProperties(body, entidad, "id");
		} else {
			entidad = body;
		}

		return this.crudRepository.save(entidad);
	}

	/**
	 * Count num Elementos.
	 *
	 * @param multiOrder the multi order
	 * @param filtros    the filtros
	 * @return the long
	 */
	@Override
	public Long count(OrdenacionDTO multiOrder, List<FiltroDTO> filtros) {

		Long resultado;

		// Filtrado
		BooleanBuilder predi = null;
		if (filtros != null) {
			predi = FiltroQDSLUtils.obtenerFiltro(this.objetoQbase, filtros);
		}
		predi = this.addFiltroCuston(predi, filtros);
		// Llamada a BBDD
		if (predi != null) {
			resultado = this.crudRepository.count(predi);
		} else {
			resultado = this.crudRepository.count();
		}

		return resultado;

	}

	/**
	 * Calcula pequeñas opearciones sobre un campo de la tabla.
	 *
	 * @param filtros  the filtros
	 * @param campo    the campo
	 * @param operador the operador
	 * @return Double
	 */
	@Override
	public Object calculadora(List<FiltroDTO> filtros, String campo, String operador) {
		return mathsQueryService.calculadoraCustom(filtros, campo, operador, this.objetoQbase);
	}

	/**
	 * Find all repository.
	 *
	 * @param customPageable the custom pageable
	 * @return the page
	 */
	public Page<E> findAllRepository(Pageable customPageable) {
		if (this.joinBuilder != null) {
			return this.crudRepository.findAllJoined(customPageable, this.objetoQbase, this.joinBuilder);
		} else {
			log.trace(this.crudRepository.getClass().getName() + TRADICIONALQUERY);
			return this.crudRepository.findAll(customPageable);
		}
	}

	/**
	 * Find all repository.
	 *
	 * @param predi          the predi
	 * @param customPageable the custom pageable
	 * @return the page
	 */
	public Page<E> findAllRepository(BooleanBuilder predi, Pageable customPageable) {
		if (this.joinBuilder != null) {
			return this.crudRepository.findAllJoined(predi, customPageable, this.objetoQbase, this.joinBuilder);
		} else {
			log.trace(this.crudRepository.getClass().getName() + TRADICIONALQUERY);
			return this.crudRepository.findAll(predi, customPageable);
		}
	}

	public Iterable<E> findAll(){
		if (this.joinBuilder != null) {
			return this.crudRepository.findAllJoined(this.objetoQbase, this.joinBuilder);
		} else {
			log.trace(this.crudRepository.getClass().getName() + TRADICIONALQUERY);
			return this.crudRepository.findAll();
		}
	}
	
	/**
	 * Find all repository.
	 *
	 * @param orden the orden
	 * @return the iterable
	 */
	public Iterable<E> findAllRepository(Sort orden) {
		if (this.joinBuilder != null) {
			return this.crudRepository.findAllJoined(orden, this.objetoQbase, this.joinBuilder);
		} else {
			log.trace(this.crudRepository.getClass().getName() + TRADICIONALQUERY);
			return this.crudRepository.findAll(orden);
		}
	}

	/**
	 * Find all repository.
	 *
	 * @param predi the predi
	 * @param orden the orden
	 * @return the iterable
	 */
	public Iterable<E> findAllRepository(BooleanBuilder predi, Sort orden) {

		if (this.joinBuilder != null) {

			return this.crudRepository.findAllJoined(predi, orden, this.objetoQbase, this.joinBuilder);
		} else {
			log.trace(this.crudRepository.getClass().getName() + TRADICIONALQUERY);
			return this.crudRepository.findAll(predi, orden);
		}

	}
	
}
