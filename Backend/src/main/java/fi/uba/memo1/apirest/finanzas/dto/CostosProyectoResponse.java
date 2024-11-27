package fi.uba.memo1.apirest.finanzas.dto;

import java.util.Map;
import java.util.TreeMap;

public class CostosProyectoResponse {
    private String nombreProyecto;
    private Map<String, Double> costoPorMes;
    private Double costoTotal;

    public CostosProyectoResponse() {
        this.costoPorMes = new TreeMap<>((a, b) -> Integer.compare(Integer.parseInt(a), Integer.parseInt(b)));
    }

    public CostosProyectoResponse(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
        this.costoPorMes = new TreeMap<>((a, b) -> Integer.compare(Integer.parseInt(a), Integer.parseInt(b)));
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    public Double getCostoTotal(){
        return this.costoTotal;
    }

    public void setCostoTotal(Double costoTotal){
        this.costoTotal = costoTotal;
    }

    public Map<String, Double> getCostoPorMes() {
        return costoPorMes;
    }

    public void setCostoPorMes(Map<String, Double> costoPorMes) {
        this.costoPorMes = costoPorMes;
    }
}
