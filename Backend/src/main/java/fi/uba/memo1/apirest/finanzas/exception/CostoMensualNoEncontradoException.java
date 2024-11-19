package fi.uba.memo1.apirest.finanzas.exception;

public class CostoMensualNoEncontradoException extends BusinessException {

    public CostoMensualNoEncontradoException() {
        super("Costo mensual no encontrado");
    }
}
