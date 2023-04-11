package es.juntadeandalucia.ctpda.gestionpdt.service;

import com.querydsl.core.BooleanBuilder;
import es.juntadeandalucia.ctpda.gestionpdt.model.QTipoTramiteDfr;
import es.juntadeandalucia.ctpda.gestionpdt.model.TipoTramiteDfr;
import es.juntadeandalucia.ctpda.gestionpdt.repository.TipoTramiteRepositoryDfr;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.AbstractCRUDService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.MathsQueryService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.dto.FiltroDTO;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TipoTramiteServiceDfr extends AbstractCRUDService<TipoTramiteDfr> {

    /**
     * serial id
     */
    private static final long serialVersionUID = 1L;

    private TipoTramiteRepositoryDfr tipoTramiteRepositoryDfr;

    /**
     * Constructor que inyecta bean de calculos matematicos y el resto de beans que hacen falta
     * */
    public TipoTramiteServiceDfr(@Autowired MathsQueryService mathsQueryService, @Autowired TipoTramiteRepositoryDfr tipoTramiteRepositoryDfr){
        super(mathsQueryService,
                tipoTramiteRepositoryDfr,
                QTipoTramiteDfr.tipoTramiteDfr);
        //también lo guardo para mi por si quiero hacer consultas personalizadas.
        this.tipoTramiteRepositoryDfr=tipoTramiteRepositoryDfr;
    }

    public TipoTramiteDfr findTipoTramiteActivoNotif(){
        return tipoTramiteRepositoryDfr.findTipoTramiteActivoNotif();
    }

    public TipoTramiteDfr findTipoTramiteActivoFirm(){
        return tipoTramiteRepositoryDfr.findTipoTramiteActivoFirm();
    }

    public List<TipoTramiteDfr> findByComportamiento(String comp){
        return tipoTramiteRepositoryDfr.findByComportamiento(comp);
    }


    public List<TipoTramiteDfr> findTipoTramitesActivos(){
        return tipoTramiteRepositoryDfr.findTipoTramitesActivos();
    }

    public List<TipoTramiteDfr> findTipoTramitesActivosOrdenAlfab(){
        return tipoTramiteRepositoryDfr.findTipoTramitesActivosOrdenAlfab();
    }

    @Override
    public void checkSiPuedoGrabar(TipoTramiteDfr dto) throws BaseException {
        log.debug("Verifico si puedo grabar y elevo excepción sino");

    }

    @Override
    public void checkSiPuedoEliminar(Long id) throws BaseException {
        log.debug("Verifico si puedo eliminar y elevo excepción sino");
    }

    /**
     * Filtros custom.
     * Se pueden añadir predicados querydsl a manini, o dejar a nulo.
     * Se añade el filtro
     * */
    @Override
    protected BooleanBuilder aniadirFiltrosCustom(List<FiltroDTO> filtros) {
        return new BooleanBuilder();
    }

    public TipoTramiteDfr getByCodigo(String codigo) {
        return this.tipoTramiteRepositoryDfr.getByCodigo(codigo);
    }
}
