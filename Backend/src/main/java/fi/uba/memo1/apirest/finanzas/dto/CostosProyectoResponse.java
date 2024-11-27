package fi.uba.memo1.apirest.finanzas.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.TreeMap;

@Getter
@Setter
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
}
