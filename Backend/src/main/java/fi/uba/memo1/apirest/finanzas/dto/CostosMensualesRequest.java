package fi.uba.memo1.apirest.finanzas.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CostosMensualesRequest {
    private String nombre;
    private String experiencia;
    private double costo;
    private String anio;
    private String mes;
}
