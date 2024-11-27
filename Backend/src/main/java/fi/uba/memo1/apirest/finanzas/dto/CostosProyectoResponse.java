package fi.uba.memo1.apirest.finanzas.dto;

import java.util.HashMap;
import java.util.Map;

public class CostosProyectoResponse {
    private String nombreProyecto;
    private Map<String, Double> costoPorMes;

    public CostosProyectoResponse() {
        this.costoPorMes = new HashMap<>();
    }

    public CostosProyectoResponse(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
        this.costoPorMes = new HashMap<>();;
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    public Map<String, Double> getCostoPorMes() {
        return costoPorMes;
    }

    public void setCostoPorMes(Map<String, Double> costoPorMes) {
        this.costoPorMes = costoPorMes;
    }
}
