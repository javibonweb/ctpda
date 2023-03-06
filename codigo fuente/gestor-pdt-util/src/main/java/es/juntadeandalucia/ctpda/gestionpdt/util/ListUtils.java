package es.juntadeandalucia.ctpda.gestionpdt.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class ListUtils {

	private static int dividirDefaultSize = 1000;
	
	private ListUtils () {
		throw new IllegalStateException("Excepción en ListUtils");
	}

	/**
	 * Toma una lista y la divide en trozos de tamaño maxSizeTramo
	 * @param lista Lista a trocear.
	 * @param maxSizeTramo Tamaño de los trozos.
	 * @return Lista de sublistas.
	 */
	public static <E> List<List<E>> dividir(List<E> lista, int maxSizeTramo){
		final List<List<E>> trozos = new ArrayList<>();

		final int PASO = maxSizeTramo;
		final int num_trozos =(int)Math.ceil((double)lista.size()/PASO);

		if(num_trozos>0){
			for(int i=0;i<num_trozos-1;i++){
				trozos.add(lista.subList(i*PASO, (i+1)*(PASO)));
			}
			trozos.add(lista.subList((num_trozos-1)*PASO, lista.size()));
		}


		return trozos;
	}

	/** Divide por defecto en trozos de 1000 elementos
	    (el máximo permitido por la cláusula IN de Oracle)
	*/
	public static <E> List<List<E>> dividir(List<E> lista){
		return dividir(lista, dividirDefaultSize);
	}

	public static <T, R> List<R> collect(final Collection<T> lista, final Function<T, R> func){
		return lista.stream()
				.map(func)
				.collect(Collectors.toList());
	}

	public static <T> String join(final Collection<T> lista, final String separator){
		return lista.stream().map(Object::toString)
				.collect(Collectors.joining(separator));
	}

	public static <T, R> String join(final Collection<T> lista, final Function<T, R> mapFunc, final String separator){
		return lista.stream()
				.map(mapFunc).map(Object::toString)
				.collect(Collectors.joining(separator));
	}
	
	public static <T, R> Set<R> collectSet(final Collection<T> lista, final Function<T, R> func){
		return new HashSet<>(collect(lista, func));
	}
	
	public static <T> List<T> filter(final Collection<T> lista, final Predicate<T> func){
		return lista.stream()
				.filter(func)
				.collect(Collectors.toList());
	}

	@SafeVarargs
	/**
	 * A diferencia de List.of devuelve un ArrayList mutable.
	 * @param t Array de parámetros
	 * @return
	 */
	public static <T> List<T> lista(T... t){
		return new ArrayList<>(Arrays.asList(t));
	}

	public static <T, R> Map<R, T> mapBy(List<T> lista, Function<T, R> getter) {
		return lista.stream().collect(Collectors.toMap(getter, Function.identity()));
	}

	public static <T> List<T> toList(Iterable<T> it){
		List<T> lista =  new java.util.ArrayList<>();
		
		for(T t : it) {
			lista.add(t);
		}

		return lista;
	}
	
}
