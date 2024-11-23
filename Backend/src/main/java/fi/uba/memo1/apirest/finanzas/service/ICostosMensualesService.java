package fi.uba.memo1.apirest.finanzas.service;

import fi.uba.memo1.apirest.finanzas.dto.CostosMensualesRequest;
import fi.uba.memo1.apirest.finanzas.dto.CostosMensualesResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ICostosMensualesService {

    Mono<List<CostosMensualesResponse>> findAll();

    Mono<CostosMensualesResponse> findById(Long id);

    Mono<CostosMensualesResponse> save(CostosMensualesRequest costos);
}
