package fi.uba.memo1.apirest.finanzas.exception;

public class RecursoNoEncontradoException extends BusinessException{

    public RecursoNoEncontradoException() {
        super("Trabajador inválido");
    }
}
