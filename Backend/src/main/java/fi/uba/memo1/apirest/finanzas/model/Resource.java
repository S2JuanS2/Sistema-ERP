package fi.uba.memo1.apirest.finanzas.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

/*
"id": "ff14a491-e26d-4092-86ea-d76f20c165d1",
"nombre": "Martin",
"apellido": "Garcia",
"dni": "33834234",
"rolId": "1f14a491-e26d-4092-86ea-d76f20c165d1"
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Resource implements Serializable {
    private String id;
    private String nombre;
    private String apellido;
    private String dni;
    private String rolId;
}
