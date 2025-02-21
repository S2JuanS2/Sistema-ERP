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
public class Recurso implements Serializable {
    private String id;
    private String nombre;
    private String apellido;
    private String dni;
    private String rolId;
}
