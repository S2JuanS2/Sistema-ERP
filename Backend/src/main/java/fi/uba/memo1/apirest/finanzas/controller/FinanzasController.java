package fi.uba.memo1.apirest.finanzas.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/finanzas")
public class FinanzasController {

    FinanzasController() {
    }

    @GetMapping("/hola-mundo")
    public ResponseEntity<?> holaMundo() {
        return ResponseEntity.status(HttpStatus.OK).body("Hola mundo");
    }
}
