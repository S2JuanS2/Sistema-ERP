package fi.uba.memo1.apirest.finanzas.controller;


import fi.uba.memo1.apirest.finanzas.service.IFinanzasService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/finanzas")
public class FinanzasController {

    private final IFinanzasService finanzasService;

    FinanzasController(IFinanzasService finanzasService) {
        this.finanzasService = finanzasService;
    }

    @GetMapping("/hola-mundo")
    public ResponseEntity<?> holaMundo() {
        return ResponseEntity.status(HttpStatus.OK).body(finanzasService.getHelloWorld());
    }

    @GetMapping("/proyectos")
    public ResponseEntity<?> getProjects() {
        return ResponseEntity.status(HttpStatus.OK).body(finanzasService.getProjects());
    }

    @GetMapping("/test")
    public ResponseEntity<?> getTasksWithDetails() {
        return ResponseEntity.status(HttpStatus.OK).body(finanzasService.getTasksWithDetails());
    }
}
