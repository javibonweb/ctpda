package es.juntadeandalucia.ctpda.gestionpdt.web.formaciondfr;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.FormacionPruebasDfr;
import es.juntadeandalucia.ctpda.gestionpdt.model.Usuario;
import es.juntadeandalucia.ctpda.gestionpdt.service.FormacionPruebasDfrService;
import es.juntadeandalucia.ctpda.gestionpdt.service.UsuarioService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.JsfUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.BaseBean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@ViewScoped
@Slf4j
public class FormularioFormacionDfrBean extends BaseBean implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Getter @Setter
    private String modoAcceso;

    @Getter @Setter
    private String codigo;

    @Getter @Setter
    private String descripcion;

    @Getter @Setter
    private Boolean activo;

    @Getter @Setter
    private FormacionPruebasDfr formacionPruebasDfr;

    @Autowired
    private FormacionPruebasDfrService formacionPruebasDfrService;

    @Getter @Setter
    private Long selectedUsuario;

    @Getter @Setter
    private List<Usuario> listadoUsuarios;

    @Autowired
    private UsuarioService usuarioService;

    @Getter @Setter
    private Long idFormacionDfr;

    @Getter @Setter
    private boolean editableFormulario;


    @PostConstruct
    @Override
    public void init(){
        super.init();

        modoAcceso = (String) JsfUtils.getSessionAttribute("modoAcceso");
        idFormacionDfr = (Long) JsfUtils.getFlashAttribute("idFormacionDfr");
        editableFormulario = (boolean) JsfUtils.getFlashAttribute("editable");

        formacionPruebasDfr = new FormacionPruebasDfr();

        if(idFormacionDfr != null) {
            formacionPruebasDfr = formacionPruebasDfrService.obtener(idFormacionDfr);
            codigo = formacionPruebasDfr.getCodigo();
            descripcion = formacionPruebasDfr.getDescripcion();
            activo = formacionPruebasDfr.getActivo();
            selectedUsuario = (formacionPruebasDfr.getUsuario() != null)?formacionPruebasDfr.getUsuario().getId():null;
        }else {
            codigo = "";
            descripcion = "";
            activo = true;
            selectedUsuario = null;
        }

        listadoUsuarios = new ArrayList<>();
        listadoUsuarios = usuarioService.findUsuariosActivos();
    }

    public void saveFormacionDfr() {
        if(codigo != null && codigo.isEmpty()) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "El código tiene que ser obligatorio");
            PrimeFaces.current().dialog().showMessageDynamic(message);

        }else {
            try {
                formacionPruebasDfr.setCodigo(codigo);
                formacionPruebasDfr.setDescripcion(descripcion);
                formacionPruebasDfr.setActivo(activo);

                if(selectedUsuario != null) {
                    Usuario usuarioSeleccionado = usuarioService.obtener(selectedUsuario);
                    formacionPruebasDfr.setUsuario(usuarioSeleccionado);
                }

                formacionPruebasDfrService.guardar(formacionPruebasDfr);
                log.info("Formacion DFR guardado");
                FacesContext.getCurrentInstance().addMessage("messagesFormularioFormacionDfr",new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Formacion " +formacionPruebasDfr.getCodigo()+ " DFR guardado correctamente"));

            } catch (BaseException e) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "",mensajesProperties.getString("error"));
                PrimeFaces.current().dialog().showMessageDynamic(message);
                log.error(e.getMessage());
            }
        }
    }
}