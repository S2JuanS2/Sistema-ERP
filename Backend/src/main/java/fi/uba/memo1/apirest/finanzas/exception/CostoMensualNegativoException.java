package fi.uba.memo1.apirest.finanzas.exception;

public class CostoMensualNegativoException extends BusinessException {
    
    public CostoMensualNegativoException() {
        super("El nuevo costo del recurso no puede ser un monto negativo");
    }
}
