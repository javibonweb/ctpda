package es.juntadeandalucia.ctpda.gestionpdt.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtils {
	
	private FileUtils () {
		throw new IllegalStateException("Excepción en FileUtils");
	}

	//Unir nombre y extensión
	public static String getNombreExt(String nombre, String ext) {
		return nombre + "." + ext;
	}

	public static String getNombreSinExt(String nombre) {
		return split(nombre)[0];
	}
	
	//Separar nombre y extensión
	public static String[] split(String str) {
		final int p = str.lastIndexOf(".");
		return new String[]{ str.substring(0, p), str.substring(p+1) };
	}
	
	/**
	 * Dada una cadena con extensiones de archivo separadas por comas (espacios opcionales) devuelve el array
	 * correspondiente con las extensiones "sanitizadas" eliminando los puntos iniciales si los hubiera.
	 * Por ejemplo: "odt, .docx,pdf,.doc" -> ["odt", "docx", "pdf", "doc"]
	 * @param strExtensiones
	 * @return
	 */
	public static List<String> listaDeExtensiones(String strExtensiones) {
		List<String> ls = Arrays.asList(strExtensiones.split("\\s*,\\s*"));
		return ls.stream()
				.filter(s -> s.matches("\\.?\\w+"))
				.map(s -> s.replaceAll("[^\\w]", "").toLowerCase()) //limpio
				.collect(Collectors.toList());
	}
	
	public static String[] arrayDeExtensiones(String strExtensiones) {
		return listaDeExtensiones(strExtensiones).toArray(new String[0]);
	}
	
	//------------------------------
	
	public static byte[] leerBytesURLRemota(String strUrl) throws IOException {
		URL url = new URL(strUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(5 * 1000);
		InputStream inStream = conn.getInputStream();
		return readInputStream(inStream);
	}

	public byte[] leerBytesURLLocal(String strUrl) throws IOException {
		File imageFile = new File(strUrl);
		InputStream inStream = new FileInputStream(imageFile);
		return readInputStream(inStream);
	}

	private static byte[] readInputStream(InputStream inStream) throws IOException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[10240];
		int len = 0;
		
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		
		inStream.close();
		return outStream.toByteArray();
	}
}
