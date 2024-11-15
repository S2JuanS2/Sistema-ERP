package fi.uba.memo1.apirest.finanzas.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/finanzas")
public class FinanzasController {

    @GetMapping("/hola-mundo")
    public ResponseEntity<?> holaMundo() {
        return ResponseEntity.status(HttpStatus.OK).body("Hola mundo");
    }
}
