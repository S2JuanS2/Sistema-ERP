package fi.uba.memo1.apirest.finanzas.exception;

public class RolNoEncontradoException extends BusinessException {

    public RolNoEncontradoException() {
        super("No se encontró un rol con nombre y experiencia coincidentes");
    }
}
