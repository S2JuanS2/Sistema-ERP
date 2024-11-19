package fi.uba.memo1.apirest.finanzas.controller;

import fi.uba.memo1.apirest.finanzas.dto.CostosMensualesRequest;
import fi.uba.memo1.apirest.finanzas.model.CostosMensuales;
import fi.uba.memo1.apirest.finanzas.service.CostosMensualesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/finanzas")
public class CostosMensualesController {
    private final CostosMensualesService service;

    public CostosMensualesController(CostosMensualesService service) {
        this.service = service;
    }

    @GetMapping("/costos")
    public ResponseEntity<?> getCostos() {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAll());
    }

    @GetMapping("/costos/{id}")
    public ResponseEntity<?> getCostos(@PathVariable String id) {
        CostosMensuales costos = service.findById(id);
        if (costos == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Costo no encontrado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(costos);
    }

    @PostMapping("/cargar-costo")
    public ResponseEntity<?> cargarCosto(@RequestBody CostosMensualesRequest costosMensualesRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(costosMensualesRequest));
    }
}
