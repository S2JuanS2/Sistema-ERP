package fi.uba.memo1.apirest.finanzas.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table
public class CostosMensuales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String idRol;

    @Column(nullable = false)
    private String mes;

    @Column(nullable = false)
    private String anio;

    @Column(nullable = false)
    private Double costo;
}
