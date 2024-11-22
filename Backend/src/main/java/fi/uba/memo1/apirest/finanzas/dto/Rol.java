package fi.uba.memo1.apirest.finanzas.dto;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Rol implements Serializable {
    private String id;
    private String nombre;
    private String experiencia;
}
