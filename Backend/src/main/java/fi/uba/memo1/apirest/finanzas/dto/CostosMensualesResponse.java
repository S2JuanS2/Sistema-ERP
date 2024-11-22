package fi.uba.memo1.apirest.finanzas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CostosMensualesResponse implements Serializable {
    private Long id;
    private Rol rol;
    private String mes;
    private String anio;
    private Double costo;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CostosMensualesResponse that = (CostosMensualesResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(idRol, that.idRol) && Objects.equals(mes, that.mes) && Objects.equals(anio, that.anio) && Objects.equals(costo, that.costo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idRol, mes, anio, costo);
    }
}
