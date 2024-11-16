package fi.uba.memo1.apirest.finanzas.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Test implements Serializable {


    private String id;

    private String nombre;

    private String descripcion;

    private Resource recurso;

    private Project proyecto;


}
