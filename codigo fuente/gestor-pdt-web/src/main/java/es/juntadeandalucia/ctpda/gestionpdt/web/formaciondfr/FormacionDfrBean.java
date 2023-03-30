package es.juntadeandalucia.ctpda.gestionpdt.web.formaciondfr;

import es.juntadeandalucia.ctpda.gestionpdt.model.FormacionPruebasDfr;
import es.juntadeandalucia.ctpda.gestionpdt.model.Usuario;
import es.juntadeandalucia.ctpda.gestionpdt.service.FormacionPruebasDfrService;
import es.juntadeandalucia.ctpda.gestionpdt.service.UsuarioService;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.LazyDataModelByQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.MyFilterMeta;
import es.juntadeandalucia.ctpda.gestionpdt.web.menu.NavegacionBean.ListadoNavegaciones;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.BaseBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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
    private SortMeta defaultOrden;

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
        defaultOrden = SortMeta.builder().field("codigo").order(SortOrder.ASCENDING).priority(1).build();
    }

    public String redireccionMenu() {
        init();
        return ListadoNavegaciones.LISTADO_FORMACIONDFR.getRegla();
    }

    public String onCrear() {
        return "";
    }

    public void limpiarFiltros() {
        codigoFiltro = "";
        descripcionFiltro = "";
        activoFiltro = true;
        versionFiltro = null;
        fechaCreacionFiltro = null;
        selectedUsuarioFiltro = null;
    }
}
