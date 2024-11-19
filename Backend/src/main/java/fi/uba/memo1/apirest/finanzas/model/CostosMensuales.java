package fi.uba.memo1.apirest.finanzas.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table
public class CostosMensuales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String idRol;

    @Setter
    @Column(nullable = false)
    private String mes;

    @Setter
    @Column(nullable = false)
    private String anio;

    @Setter
    @Column(nullable = false)
    private Double costo;
}
