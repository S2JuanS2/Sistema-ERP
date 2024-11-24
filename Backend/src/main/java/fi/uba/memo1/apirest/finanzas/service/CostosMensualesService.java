package fi.uba.memo1.apirest.finanzas.service;

import fi.uba.memo1.apirest.finanzas.dto.CostosMensualesRequest;
import fi.uba.memo1.apirest.finanzas.dto.CostosMensualesResponse;
import fi.uba.memo1.apirest.finanzas.dto.Rol;
import fi.uba.memo1.apirest.finanzas.exception.RolNoEncontradoException;
import fi.uba.memo1.apirest.finanzas.model.CostosMensuales;
import fi.uba.memo1.apirest.finanzas.repository.CostosMensualesRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;
import java.util.List;

@Service
public class CostosMensualesService implements ICostosMensualesService {

    @Autowired
    @Qualifier("rolesWebClient")
    private WebClient rolesWebClient;

    private final CostosMensualesRepository repository;

    public CostosMensualesService(CostosMensualesRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<List<CostosMensualesResponse>> findAll() {
        return rolesWebClient
                .get()
                .uri("/roles")
                .retrieve()
                .bodyToFlux(Rol.class)
                .collectList()
                .flatMapMany(roles -> Flux.fromIterable(repository.findAll()).map(
                        costosMensuales -> {
                            Rol matchingRol = roles.stream()
                                    .filter(rol -> rol.getId().equals(costosMensuales.getIdRol()))
                                    .findFirst()
                                    .orElseThrow(RolNoEncontradoException::new);

                            return new CostosMensualesResponse(
                                    costosMensuales.getId(),
                                    matchingRol,
                                    costosMensuales.getMes(),
                                    costosMensuales.getAnio(),
                                    costosMensuales.getCosto()
                            );
                        })
                )
                .collectList();
    }

    @Override
    public Mono<CostosMensualesResponse> findById(Long id) {
        return rolesWebClient
                .get()
                .uri("/roles")
                .retrieve()
                .bodyToFlux(Rol.class)
                .collectList()
                .flatMap(roles -> Mono.justOrEmpty(repository.findById(id))
                        .switchIfEmpty(Mono.error(new RolNoEncontradoException()))
                        .map(costosMensuales -> {
                            Rol matchingRol = roles.stream()
                                    .filter(rol -> rol.getId().equals(costosMensuales.getIdRol()))
                                    .findFirst()
                                    .orElseThrow(RolNoEncontradoException::new);

                            return new CostosMensualesResponse(
                                    costosMensuales.getId(),
                                    matchingRol,
                                    costosMensuales.getMes(),
                                    costosMensuales.getAnio(),
                                    costosMensuales.getCosto()
                            );
                        })
                );
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
                                .filter(rol -> costos.getNombre().equalsIgnoreCase(rol.getNombre()) && costos.getExperiencia().equalsIgnoreCase(rol.getExperiencia()))
                                .findFirst()
                )
        ).switchIfEmpty(Mono.error(new RolNoEncontradoException()));

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
                                    matchingRol,
                                    savedCostos.getMes(),
                                    savedCostos.getAnio(),
                                    savedCostos.getCosto()
                            )
                    );
        });
    }
}
