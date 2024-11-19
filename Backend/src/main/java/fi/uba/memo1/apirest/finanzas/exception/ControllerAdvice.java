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
}
