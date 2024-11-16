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

    @GetMapping("/hola-mundo") // TODO: endpoint para probar el servicio
    public ResponseEntity<?> holaMundo() {
        return ResponseEntity.status(HttpStatus.OK).body(finanzasService.getHelloWorld());
    }

    @GetMapping("/test-proyectos") // TODO: endpoint para probar la API
    public ResponseEntity<?> getProjects() {
        return ResponseEntity.status(HttpStatus.OK).body(finanzasService.getProjectsTest());
    }

    @GetMapping("/test-composicion")  // TODO: endpoint para probar la composicion de las APIs
    public ResponseEntity<?> getTasksWithDetails() {
        return ResponseEntity.status(HttpStatus.OK).body(finanzasService.getTasksWithDetailsTest());
    }
}
