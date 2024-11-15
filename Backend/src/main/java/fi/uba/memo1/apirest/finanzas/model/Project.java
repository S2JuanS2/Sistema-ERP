package fi.uba.memo1.apirest.finanzas.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Project {

    private Integer id;
    private String nombre;
    private String descripcion;
}
