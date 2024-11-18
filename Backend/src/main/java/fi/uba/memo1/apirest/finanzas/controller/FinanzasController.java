package fi.uba.memo1.apirest.finanzas.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import fi.uba.memo1.apirest.finanzas.dto.CargarCostoRequest;
import fi.uba.memo1.apirest.finanzas.model.CostosMensuales;
import fi.uba.memo1.apirest.finanzas.service.CostosMensualesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/finanzas")
public class FinanzasController {

    private static final String ROLES_URL = "https://anypoint.mulesoft.com/mocking/api/v1/sources/exchange/assets/32c8fe38-22a6-4fbb-b461-170dfac937e4/roles-api/1.0.0/m/roles";
    private final CostosMensualesService service;

    public FinanzasController(CostosMensualesService service) {
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
    public ResponseEntity<?> cargarCosto(@RequestBody CargarCostoRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map[]> res = restTemplate.getForEntity(ROLES_URL, Map[].class);

        if(res.getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener los roles");
        }

        Map[] roles = res.getBody();
        if (roles == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener los roles");
        }

        String rolID = null;
        for (Map rol : roles) {
            if (request.getNombre().equalsIgnoreCase(rol.get("nombre").toString()) && request.getExperiencia().equalsIgnoreCase(rol.get("experiencia").toString())) {
                rolID = rol.get("id").toString();
                break;
            }
        }

        if (rolID == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Rol no encontrado");
        }

        LocalDate currentDate = LocalDate.now();
        int mes = currentDate.getMonthValue();
        int anio = currentDate.getYear();

        CostosMensuales costoMensual = new CostosMensuales();
        costoMensual.setId_rol(rolID);
        costoMensual.setMes(String.valueOf(mes));
        costoMensual.setAnio(String.valueOf(anio));
        costoMensual.setCosto(request.getCosto());

        service.save(costoMensual);

        return ResponseEntity.status(HttpStatus.OK).body("Costo cargado correctamente");
    }
}
