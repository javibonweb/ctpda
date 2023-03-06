package es.juntadeandalucia.ctpda.gestionpdt.web.core;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public class JsfUtils {

		/** Constructor basico. */
		private JsfUtils() {
			super();
		}




		/**
		 * Recupera el atributo de sesion con el nombre 'name'.
		 * @param name String
		 * @return Object
		 */
		public static Object getSessionAttribute(final String name) {
			return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(name);
		}


		/**
		 * Asigna al atributo de sesion con el nombre 'name' con el valor del objeto
		 * 'obj'.
		 * @param name String
		 * @param obj Object
		 */
		public static void setSessionAttribute(final String name, final Object obj) {
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(name, obj);
		}


		/**
		 * Elimina el atributo de sesion con el nombre 'name'.
		 * @param name String
		 */
		public static void removeSessionAttribute(final String name) {
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(name);
		}
		
		/**
		 * Recupera el atributo de flash con el nombre 'name'.
		 * @param name String
		 * @return Object
		 */
		public static Object getFlashAttribute(final String name) {
			return FacesContext.getCurrentInstance().getExternalContext().getFlash().get(name);
		}

		/**
		 * Recupera el parámetro de request con el nombre 'name'.
		 * @param name String
		 * @return Object
		 */
		public static String getRequestParam(final String name) {
			return ((HttpServletRequest)FacesContext.getCurrentInstance()
					.getExternalContext().getRequest())
					.getParameter(name);
		}


		/**
		 * Asigna al atributo de flash con el nombre 'name' con el valor del objeto
		 * 'obj'.
		 * @param name String
		 * @param obj Object
		 */
		public static void setFlashAttribute(final String name, final Object obj) {
			FacesContext.getCurrentInstance().getExternalContext().getFlash().put(name, obj);
		}
		


	}

