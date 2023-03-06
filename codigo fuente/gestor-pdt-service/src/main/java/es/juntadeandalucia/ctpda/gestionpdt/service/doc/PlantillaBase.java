package es.juntadeandalucia.ctpda.gestionpdt.service.doc;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlantillaBase {

	private final Map<String, Object> data = new HashMap<>();
	private final Path rutaPlantilla;

	public PlantillaBase(Path rutaPlantilla){
		this.rutaPlantilla = rutaPlantilla;

	}

	//**********************************************************

	protected Object getData(Object arg0) {
		return this.data.get(arg0);
	}

	protected Object putData(String arg0, String arg1) {
		return this.data.put(arg0, arg1);
	}

	protected Object putData(String arg0, List<String> arg1) {
		return this.data.put(arg0, arg1);
	}

	public Map<String, Object> getDataMap() {
		return this.data;
	}

	//---------------------

	public Path getRutaPlantilla() {
		return this.rutaPlantilla;
	}

}
