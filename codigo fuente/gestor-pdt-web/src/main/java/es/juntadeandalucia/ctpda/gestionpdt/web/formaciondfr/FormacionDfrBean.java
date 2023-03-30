package es.juntadeandalucia.ctpda.gestionpdt.web.formaciondfr;

import es.juntadeandalucia.ctpda.gestionpdt.model.FormacionPruebasDfr;
import es.juntadeandalucia.ctpda.gestionpdt.model.Usuario;
import es.juntadeandalucia.ctpda.gestionpdt.service.FormacionPruebasDfrService;
import es.juntadeandalucia.ctpda.gestionpdt.service.UsuarioService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.JsfUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.LazyDataModelByQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.MyFilterMeta;
import es.juntadeandalucia.ctpda.gestionpdt.web.menu.NavegacionBean.ListadoNavegaciones;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.BaseBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@ViewScoped
@Slf4j
public class FormacionDfrBean extends BaseBean implements Serializable {

    private static final long serialVersionUID = 1L;

    // Constantes. Sonar si queja si no se usa así
    private static final String MODOACCESO = "modoAcceso";
    private static final String EDITABLE = "editable";

    @Getter @Setter
    private String codigoFiltro;

    @Getter @Setter
    private String descripcionFiltro;

    @Getter @Setter
    private Boolean activoFiltro;

    @Getter @Setter
    private Long versionFiltro;

    @Getter @Setter
    private Date fechaCreacionFiltro;

    @Getter @Setter
    private Long selectedUsuarioFiltro;

    @Getter @Setter
    private List<Usuario> listaUsuarios;

    // Tabla Lazy
    @Getter
    private LazyDataModelByQueryService<FormacionPruebasDfr> lazyModel;

    @Getter
    private SortMeta defaultOrdenListadoFormacionDfr;

    @Getter @Setter
    private FormacionPruebasDfr selectedFormacionPruebasDfr;

    @Autowired
    private FormacionPruebasDfrService formacionPruebasDfrService;

    @Autowired
    private UsuarioService usuarioService;

    @PostConstruct
    @Override
    public void init(){
        super.init();
        activoFiltro = true; // Por defecto activo el checkbox de activo
        listaUsuarios = new ArrayList<>();
        listaUsuarios = usuarioService.findUsuariosActivos();

        // LazyModel
        lazyModel = new LazyDataModelByQueryService<>(FormacionPruebasDfr.class, formacionPruebasDfrService);
        lazyModel.setPreproceso((a, b, c, filters) -> {
            //filtros
            if (codigoFiltro != null && !codigoFiltro.isEmpty()) {
                filters.put("codigo", new MyFilterMeta(codigoFiltro)); // Primero, nombre del campo del model
            }
            if (descripcionFiltro != null && !descripcionFiltro.isEmpty()) {
                filters.put("descripcion", new MyFilterMeta(descripcionFiltro));
            }
            if (Boolean.TRUE.equals(activoFiltro)) {
                filters.put("activo", new MyFilterMeta(activoFiltro));
            }
            //filters.put("activo", new MyFilterMeta(true)); // Forzamos a que se muestren los activos por defecto
            if (versionFiltro != null) {
                filters.put("nVersion", new MyFilterMeta(versionFiltro));
            }
            if (fechaCreacionFiltro != null) {
                filters.put("fechaCreacion", new MyFilterMeta(fechaCreacionFiltro));
            }
            if (selectedUsuarioFiltro != null) {
                filters.put("usuario.id", new MyFilterMeta(selectedUsuarioFiltro));
            }
        });
        // Ordenación por defecto. Ordena por codigo, viene del model (FormacionPruebasDfr)
        defaultOrdenListadoFormacionDfr = SortMeta.builder().field("codigo").order(SortOrder.ASCENDING).priority(1).build();

        JsfUtils.removeSessionAttribute(MODOACCESO);
    }

    public String redireccionMenu() {
        init();
        return ListadoNavegaciones.LISTADO_FORMACIONDFR.getRegla();
    }

    public String onCrear() {
        JsfUtils.setFlashAttribute(EDITABLE, true);
        JsfUtils.setSessionAttribute(MODOACCESO, "alta");
        log.info("Accedemos al formulario en modo alta");
        return ListadoNavegaciones.FORM_FORMACIONDFR.getRegla();
    }

    public void limpiarFiltros() {
        codigoFiltro = "";
        descripcionFiltro = "";
        activoFiltro = true;
        versionFiltro = null;
        fechaCreacionFiltro = null;
        selectedUsuarioFiltro = null;
    }

    public String onEditar(Long idFormacionDfr) {
        JsfUtils.setFlashAttribute(EDITABLE, true);
        JsfUtils.setSessionAttribute(MODOACCESO, "edicion");
        JsfUtils.setFlashAttribute("idFormacionDfr", idFormacionDfr);
        return ListadoNavegaciones.FORM_FORMACIONDFR.getRegla();
    }

    public String onConsultar(Long idFormacionDfr) {
        JsfUtils.setFlashAttribute(EDITABLE, false);
        JsfUtils.setSessionAttribute(MODOACCESO, "consulta");
        JsfUtils.setFlashAttribute("idFormacionDfr", idFormacionDfr);
        return ListadoNavegaciones.FORM_FORMACIONDFR.getRegla();
    }

    public void eliminarFormacionDfr (FormacionPruebasDfr formacionPruebasDfr) {
        try {
            formacionPruebasDfrService.delete(formacionPruebasDfr.getId());
            FacesContext.getCurrentInstance().addMessage("messagesListadoFormaciondfr",new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Formacion DFR borrada correctamente"));
        } catch (BaseException e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",mensajesProperties.getString("error"));
            PrimeFaces.current().dialog().showMessageDynamic(message);
            log.error(e.getMessage());
        }
    }


    public void desactivarFormacionDfr (FormacionPruebasDfr formacionPruebasDfr) {
        try {
            formacionPruebasDfr.setActivo(false);
            formacionPruebasDfrService.guardar(formacionPruebasDfr);
            FacesContext.getCurrentInstance().addMessage("messagesListadoFormaciondfr",new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Formacion DFR desactivada correctamente"));
        } catch (BaseException e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",mensajesProperties.getString("error"));
            PrimeFaces.current().dialog().showMessageDynamic(message);
            log.error(e.getMessage());
        }
    }
}
