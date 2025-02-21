package fi.uba.memo1.apirest.finanzas.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CargaDeHoras {
    private List<HorasMensuales> projects;
}
