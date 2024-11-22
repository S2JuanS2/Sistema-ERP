package fi.uba.memo1.apirest.finanzas.dto;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class CostosMensualesResponse implements Serializable {
    private Long id;
    private Rol rol;
    private String mes;
    private String anio;
    private Double costo;
}
