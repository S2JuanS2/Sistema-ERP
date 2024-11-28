package fi.uba.memo1.apirest.finanzas.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TotalCostosProyectoResponse {
    private List<CostosProyectoResponse> proyectos;
    private Double costoTotalGlobal;

    public TotalCostosProyectoResponse(List<CostosProyectoResponse> proyectos){
        this.proyectos = proyectos;
        this.costoTotalGlobal = 0.0;
    }

    public void sumarCosto(Double costo){
        this.costoTotalGlobal += costo;
    }
}

