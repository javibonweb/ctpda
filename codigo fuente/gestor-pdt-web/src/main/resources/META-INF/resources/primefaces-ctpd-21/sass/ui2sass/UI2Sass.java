import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.swing.*;

public class UI2Sass {

	private static String FICHERO_UI_VARIABLES = "..\\abstracts\\_ui-variables.scss";
	private static String RUTA_FICHEROS_JQUERY_UI = "..\\themes";

	private String nombreApp;
	private String temaJsf;
	private String urlJQueryUI;
	
	public static void main(String[] args) {
		UI2Sass ui = new UI2Sass();
		
		ui.generarFicheroVars();
		ui.actualizaURLs();

		// Las aplicaciones Swing no se cierran solas.
		// Hay formas mejores de hacerlo, pero esta nos vale aquí.
		System.exit(0);
	}
	
	public UI2Sass() {
		ResourceBundle rb = ResourceBundle.getBundle("ui2sass");
		nombreApp = rb.getString("nombre-app");
		temaJsf = rb.getString("tema-jsf");
		urlJQueryUI = rb.getString("url-jquery-ui");
	}

	private MapVariables loadSassVars() {
		MapVariables vars = new MapVariables();

		if (urlJQueryUI != null) {
			String[] ss = urlJQueryUI.split("&");
			if (ss.length > 1) {
				for (int i = 1; i < ss.length; ++i) {
					String[] var = ss[i].split("=");
					String key = this.normalizeKey(var[0]);
					String val = (var.length > 1) ? this.normalizeVal(var[1]) : "";
					vars.put(key, val);

					System.out.println(var[0] + " --> " + key + " = " + vars.get(key));
				}
			}
		}
		
		return vars;
	}
	
	private class MapVariables {
		Map<String, String> vars = new LinkedHashMap<>();
		
		void put(String k, String v) {
			vars.put(k,v);
		}
		
		String get(String k) {
			return vars.get(k);
		}
		
		Map<String, List<Map.Entry<String, String>>> getMap(){
			return vars.entrySet().stream()
				.collect(Collectors.groupingBy(
						e ->{ String[] ss = e.getKey().split("-"); 
						return ss[ss.length-1];}));
		}
		
	}

	private void generarFicheroVars() {
		MapVariables vars = this.loadSassVars();
		
		try (FileWriter fw = new FileWriter(FICHERO_UI_VARIABLES)) {
			fw.write("//Fichero generado con UI2Sass.java.\n");

			for(Map.Entry<String, List<Map.Entry<String, String>>> categoriaEntry : vars.getMap().entrySet()) {
				fw.write("\n\n//------------------------------------------\n");
				fw.write(    "// " + categoriaEntry.getKey() + "\n");
				fw.write(    "//------------------------------------------\n");
				
				for (Map.Entry<String, String> e : categoriaEntry.getValue()) {
					String v = e.getValue();
					String linea = e.getKey() + ":\t\t" + v + ";\n";
					linea = (v.equals("") ? "// " : "") + linea;
					fw.write(linea);
				}
			}
			
			System.out.println("\nFichero '" + FICHERO_UI_VARIABLES + "' generado.");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*private void loadConf() {
		ResourceBundle rb = ResourceBundle.getBundle("ui2sass");
		nombreApp = rb.getString("nombre-app");
		temaJsf = rb.getString("tema-jsf");
		urlJQueryUI = rb.getString("url-jquery-ui");

		
		String url = null;
		Path p = Paths.get("url-jquery-ui.txt");
		
		if(Files.exists(p)) {
			System.out.println("Encontrado fichero " + p.toString());
			try {
				url = Files.readAllLines(p).get(0);
			} catch(IOException e) {
				e.printStackTrace();
				System.exit(0);
			}
		} else {
			System.out.println("Fichero " + p.toString() + " no encontrado");
			url = JOptionPane.showInputDialog(new JFrame(), "Introduzca URL de jQuery-UI");
		}
		
		return url;
	}*/

	private String normalizeKey(String key) {
		return "$ui-" + camel2Snake(key.trim());
	}

	public static String camel2Snake(String str) {
		String regex = "([a-z])([A-Z]+)";
		String replacement = "$1-$2";

		// Replace the given regex with replacement string
		// and convert it to lower case.
		str = str.replaceAll(regex, replacement).toLowerCase();

		// return string
		return str;
	}

	private String normalizeVal(String val) {
		try {
			return URLDecoder.decode(val.trim(), "UTF-8");
		} catch (UnsupportedEncodingException uee) {
			uee.printStackTrace();
			return "";
		}
	}
	
	private static Pattern patronUrlImg=Pattern.compile("(^\\s*background-image:\\s*)url\\(\\\"(images)\\/(ui-icons_\\w+\\.png)\\\"\\);\\s*$");
	//private static String patronUrlSust="$1url(\"\\#{resource['primefaces-ctpd-21:$2/$3']}\");";
	private static String patronUrlSust="$1url(\"/{0}/javax.faces.resource/$2/$3.xhtml?ln={1}&v=10.0.0&e=10.0.2\");";
	
	private void actualizaURLs() {
		Path ruta = Paths.get(RUTA_FICHEROS_JQUERY_UI);
		java.util.function.Predicate<Path> filtro = 
				p->p.getFileName().toString().matches("_jquery-ui(\\.\\w+)?.scss");
		String strSust=MessageFormat.format(patronUrlSust, nombreApp, temaJsf);
		
		try {
			UI2Sass ui = this;
			Files.list(ruta).filter(filtro).forEach(p->{
				try {
				List<String> lineas = Files.readAllLines(p);
				if(ui.actualizaURLs(lineas, strSust))
					System.out.println("Actualizado " + p.toString());
					Files.write(p, lineas);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private boolean actualizaURLs(List<String> lineas, String strSust) {
		//String strSust=patronUrlSust; // MessageFormat.format(patronUrlSust, nombreApp, temaJsf);
		boolean b=false;
		
		for(int i=0; i<lineas.size(); ++i) {
			String ln = lineas.get(i);
			
			Matcher m = patronUrlImg.matcher(ln);
			if(m.find()) {
				lineas.set(i, m.replaceFirst(strSust));
				b=true;
			}
		}
		
		return b;
	}

}
