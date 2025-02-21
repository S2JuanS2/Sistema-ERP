package fi.uba.memo1.apirest.finanzas.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorResponse {
    private String statusCode;
    private String status;
    private String message;
}
