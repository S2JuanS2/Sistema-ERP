package fi.uba.memo1.apirest.finanzas.exception;

public class FechaInvalidaException extends BusinessException{
    
    public FechaInvalidaException(){
        super("Fecha inválida");
    }
}
