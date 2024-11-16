package fi.uba.memo1.apirest.finanzas.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

/*
{
"id": "f635b4ca-c091-472c-8b5a-cb3086d1973",
"nombre": "Diseño de la Base de Datos",
"descripcion": "Definir la estructura de la base de datos para almacenar la información de los proyectos, usuarios, y otros elementos relacionados. Esto incluye la creación de tablas, relaciones, restricciones, índices y claves foráneas para asegurar la integridad y eficiencia del sistema",
"recursoId": "ff14a491-e26d-4092-86ea-d76f20c165d1",
"proyectoId": "a6e2167f-67a1-4f60-b9e9-6bae7bc3a15"
}
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Task implements Serializable {
    private String id;
    private String nombre;
    private String descripcion;
    private String recursoId;
    private String proyectoId;

}
