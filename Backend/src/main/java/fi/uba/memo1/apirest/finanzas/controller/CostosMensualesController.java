package fi.uba.memo1.apirest.finanzas.controller;

import fi.uba.memo1.apirest.finanzas.dto.CostosMensualesRequest;
import fi.uba.memo1.apirest.finanzas.dto.CostosMensualesResponse;
import fi.uba.memo1.apirest.finanzas.dto.TotalCostosProyectoResponse;
import fi.uba.memo1.apirest.finanzas.dto.CostoRequest;
import fi.uba.memo1.apirest.finanzas.service.CostosMensualesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/finanzas")
public class CostosMensualesController {
    private final CostosMensualesService service;

    public CostosMensualesController(CostosMensualesService service) {
        this.service = service;
    }

    @GetMapping("/costos")
    public ResponseEntity<Mono<List<CostosMensualesResponse>>> getCostos() {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAll());
    }
    @GetMapping("/costos/{id}")
    public ResponseEntity<Mono<CostosMensualesResponse>> getCostos(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findById(id));
    }

    @PostMapping("/cargar-costo")
    public ResponseEntity<Mono<List<CostosMensualesResponse>>> cargarCosto(@RequestBody List<CostosMensualesRequest> costosMensualesRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(costosMensualesRequest));
    }

    @PutMapping("/actualizar-costo/{id}")
    public ResponseEntity<Mono<CostosMensualesResponse>> actualizarCosto(@PathVariable Long id, @RequestBody CostoRequest costoRequest){
        return ResponseEntity.status(HttpStatus.OK).body(service.update(id, costoRequest));
    }

    @PutMapping("/actualizar-costos")
    public ResponseEntity<Mono<List<CostosMensualesResponse>>> actualizarCostos(@RequestBody Map<Long, CostoRequest> costosRequest){
        return ResponseEntity.status(HttpStatus.OK).body(service.updateAll(costosRequest));
    }
    
    @GetMapping("/proyectos/{anio}")
    public ResponseEntity<Mono<TotalCostosProyectoResponse>> getProyectos(@PathVariable String anio){
        return ResponseEntity.status(HttpStatus.OK).body(service.obtenerCostosDeProyectos(anio));
    }
    
}
