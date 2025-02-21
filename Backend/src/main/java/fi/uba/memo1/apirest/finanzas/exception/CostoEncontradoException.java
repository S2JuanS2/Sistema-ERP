package fi.uba.memo1.apirest.finanzas.exception;

public class CostoEncontradoException extends BusinessException{
    
    public CostoEncontradoException() {
        super("El rol en ese mes y año ya tiene un costo cargado");
    }
}
