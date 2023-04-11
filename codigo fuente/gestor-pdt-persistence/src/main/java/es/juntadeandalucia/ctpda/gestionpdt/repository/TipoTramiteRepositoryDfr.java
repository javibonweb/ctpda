package es.juntadeandalucia.ctpda.gestionpdt.repository;

import es.juntadeandalucia.ctpda.gestionpdt.model.TipoTramiteDfr;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.AbstractCrudRepository;
import es.juntadeandalucia.ctpda.gestionpdt.persitence.core.JoinedQDSLPredicateExecutorCustom;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public interface TipoTramiteRepositoryDfr extends AbstractCrudRepository<TipoTramiteDfr>, JoinedQDSLPredicateExecutorCustom<TipoTramiteDfr>, Serializable {
    TipoTramiteDfr getByCodigo(String cod);

    List<TipoTramiteDfr> findByComportamiento(String comp);

    @Query("SELECT tipTram FROM TipoTramite tipTram WHERE tipTram.activo = 1 and tipTram.codigo = 'NOT'")
    public TipoTramiteDfr findTipoTramiteActivoNotif();

    @Query("SELECT tipTram FROM TipoTramite tipTram WHERE tipTram.activo = 1 and tipTram.codigo = 'FIRM'")
    public TipoTramiteDfr findTipoTramiteActivoFirm();

    @Query("SELECT tipTram FROM TipoTramite tipTram WHERE tipTram.activo = 1")
    public List<TipoTramiteDfr> findTipoTramitesActivos();

    @Query("SELECT tipTram FROM TipoTramite tipTram WHERE tipTram.activo = 1 order by tipTram.descripcion asc")
    public List<TipoTramiteDfr> findTipoTramitesActivosOrdenAlfab();
}
