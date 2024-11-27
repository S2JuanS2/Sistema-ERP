package fi.uba.memo1.apirest.finanzas.dto;

import java.util.List;

public class TotalCostosProyectoResponse {
    private List<CostosProyectoResponse> proyectos;
    private Double costoTotalGlobal;

    public TotalCostosProyectoResponse(List<CostosProyectoResponse> proyectos){
        this.proyectos = proyectos;
        this.costoTotalGlobal = 0.0;
    }

    public List<CostosProyectoResponse> getProyectos() {
        return proyectos;
    }

    public void setProyectos(List<CostosProyectoResponse> proyectos) {
        this.proyectos = proyectos;
    }

    public Double getCostoTotalGlobal() {
        return costoTotalGlobal;
    }

    public void setCostoTotalGlobal(Double costoTotalGlobal) {
        this.costoTotalGlobal = costoTotalGlobal;
    }

    public void sumarCosto(Double costo){
        this.costoTotalGlobal += costo;
    }
}

