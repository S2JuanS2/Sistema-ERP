package fi.uba.memo1.apirest.finanzas.controller;

import fi.uba.memo1.apirest.finanzas.dto.CargarCostoRequest;
import fi.uba.memo1.apirest.finanzas.dto.Rol;
import fi.uba.memo1.apirest.finanzas.model.CostosMensuales;
import fi.uba.memo1.apirest.finanzas.service.CostosMensualesService;
import fi.uba.memo1.apirest.finanzas.service.FinanzasService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/finanzas")
public class FinanzasController {
    private final CostosMensualesService service;
    private final FinanzasService finanzasService;

    public FinanzasController(CostosMensualesService service, FinanzasService finanzasService) {
        this.service = service;
        this.finanzasService = finanzasService;
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
    public Mono<ResponseEntity<String>> cargarCosto(@RequestBody CargarCostoRequest request) {
        return finanzasService.getRoles()
                .flatMap(roles -> {
                    String idFound = null;
                    for (Rol rol : roles) {
                        if (rol.getNombre().equalsIgnoreCase(request.getNombre()) && rol.getExperiencia().equalsIgnoreCase(request.getExperiencia())) {
                            idFound = rol.getId();
                            break;
                        }
                    }

                    if (idFound == null) {
                        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rol no encontrado"));
                    }

                    LocalDate current = LocalDate.now();

                    CostosMensuales costos = new CostosMensuales();
                    costos.setIdRol(idFound);
                    costos.setCosto(request.getCosto());
                    costos.setMes(String.valueOf(current.getMonthValue()));
                    costos.setAnio(String.valueOf(current.getYear()));

                    CostosMensuales savedCosto = service.save(costos);
                    return Mono.just(ResponseEntity.status(HttpStatus.CREATED).body("Se cargo el costo con ID:" + savedCosto.getId()));
                })
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener los roles")));
    }
}
