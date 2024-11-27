package fi.uba.memo1.apirest.finanzas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HorasMensuales implements Serializable {
    private String id; //del proyecto
    private Map<String, Map<String, Map<String, Integer>>> years; //Año-Mes-WorkerId-Cant.Horas
}
