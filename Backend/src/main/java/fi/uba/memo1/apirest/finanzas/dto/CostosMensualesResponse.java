package fi.uba.memo1.apirest.finanzas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CostosMensualesResponse implements Serializable {
    private Long id;
    private String idRol;
    private String mes;
    private String anio;
    private Double costo;
}
