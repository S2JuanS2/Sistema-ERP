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
public class Tarea implements Serializable {
    private String id;
    private String nombre;
    private String descripcion;
    private String recursoId;
    private String proyectoId;
}
