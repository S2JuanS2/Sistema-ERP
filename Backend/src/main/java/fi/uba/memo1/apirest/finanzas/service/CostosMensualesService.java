package fi.uba.memo1.apirest.finanzas.service;

import fi.uba.memo1.apirest.finanzas.dto.CostosMensualesRequest;
import fi.uba.memo1.apirest.finanzas.dto.CostosMensualesResponse;
import fi.uba.memo1.apirest.finanzas.dto.Rol;
import fi.uba.memo1.apirest.finanzas.model.CostosMensuales;
import fi.uba.memo1.apirest.finanzas.repository.CostosMensualesRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;
import java.util.List;

@Service
public class CostosMensualesService implements ICostosMensualesService{

    @Autowired
    @Qualifier("rolesWebClient")
    private WebClient rolesWebClient;

    @Autowired
    private CostosMensualesRepository repository;

    @Override
    public List<CostosMensuales> findAll() {
        return repository.findAll();
    }

    @Override
    public CostosMensuales findById(String id) {
        return repository.findById(Long.parseLong(id)).orElse(null);
    }


    @Transactional
    @Override
    public Mono<CostosMensualesResponse> save(CostosMensualesRequest costos) {
        Mono<List<Rol>> rolesMono = rolesWebClient
                .get()
                .uri("/roles")
                .retrieve()
                .bodyToFlux(Rol.class)
                .collectList();

        // Buscar el primer rol que coincida con nombre y experiencia
        Mono<Rol> matchingRolMono = rolesMono.flatMap(roles ->
                Mono.justOrEmpty(
                        roles.stream()
                                .filter(rol ->
                                        costos.getNombre().equalsIgnoreCase(rol.getNombre()) &&
                                                costos.getExperiencia().equalsIgnoreCase(rol.getExperiencia()))
                                .findFirst()
                )
        );
        // Procesar el rol encontrado o manejar si no se encuentra
        return matchingRolMono.flatMap(matchingRol -> {
            LocalDate currentDate = LocalDate.now();
            CostosMensuales costosMensuales = new CostosMensuales();
            costosMensuales.setIdRol(matchingRol.getId());
            costosMensuales.setMes(String.valueOf(currentDate.getMonthValue()));
            costosMensuales.setAnio(String.valueOf(currentDate.getYear()));
            costosMensuales.setCosto(costos.getCosto());

            // Guardar en un Scheduler dedicado
            return Mono.fromCallable(() -> repository.save(costosMensuales))
                    .subscribeOn(Schedulers.boundedElastic()) // Ejecuta en un hilo adecuado para tareas bloqueantes
                    .map(savedCostos ->
                            new CostosMensualesResponse(
                                    savedCostos.getId(),
                                    savedCostos.getIdRol(),
                                    savedCostos.getMes(),
                                    savedCostos.getAnio(),
                                    savedCostos.getCosto()
                            )
                    );
        });
    }}
