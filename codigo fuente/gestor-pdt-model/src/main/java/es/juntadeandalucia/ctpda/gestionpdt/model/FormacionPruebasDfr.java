package es.juntadeandalucia.ctpda.gestionpdt.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import es.juntadeandalucia.ctpda.gestionpdt.model.core.Auditable;
import es.juntadeandalucia.ctpda.gestionpdt.model.core.AuditorAwareImpl;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@EntityListeners(AuditorAwareImpl.class)
@Audited
@AuditTable(value = "FOR_PRUEBAS_DFR_H")
@Table(name = "FOR_PRUEBAS_DFR")
public class FormacionPruebasDfr extends Auditable{

    /**
     * serial id
     */
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PRU_ID")
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_FOR_PRUEBAS_DFR")
    @SequenceGenerator(name = "S_FOR_PRUEBAS_DFR", sequenceName = "S_FOR_PRUEBAS_DFR", allocationSize = 1)
    @Getter
    @Setter
    private Long id;

    @Column(name = "L_ACTIVA")
    @NotNull
    @ColumnDefault("1")
    @Getter
    @Setter
    private Boolean activo;

    @Column(name = "D_DESCRIPCION")
    @Size(max = 255)
    @Getter
    @Setter
    private String descripcion;

    @Column(name = "C_CODIGO")
    @NotNull
    @Size(max = 50)
    @Getter
    @Setter
    private String codigo;

    @Version
    @Column(name = "N_VERSION")
    @Getter
    @Setter
    private Long nVersion;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FORPRU_DFR_USU", foreignKey = @ForeignKey(name = "FORPRU_DFR_USU_FK"))
    private Usuario usuario;

}
