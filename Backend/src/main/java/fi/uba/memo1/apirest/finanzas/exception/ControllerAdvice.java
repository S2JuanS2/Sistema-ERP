package fi.uba.memo1.apirest.finanzas.exception;


import fi.uba.memo1.apirest.finanzas.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(RolNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> rolNoEncontradoException(RolNoEncontradoException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode("404")
                .status("Not Found")
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(CostoMensualNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> costoMensualNoEncontradoException(CostoMensualNoEncontradoException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode("404")
                .status("Not Found")
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(CostoMensualNegativoException.class)
    public ResponseEntity<ErrorResponse> CostoNegativoException(CostoMensualNegativoException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode("400")
                .status("Bad Request")
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(FechaInvalidaException.class)
    public ResponseEntity<ErrorResponse> FechaInvalidaException(FechaInvalidaException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode("400")
                .status("Bad Request")
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> RecursoNoEncontradoException(RecursoNoEncontradoException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode("404")
                .status("Not Found")
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    
}
