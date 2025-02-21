package fi.uba.memo1.apirest.finanzas.service;

import fi.uba.memo1.apirest.finanzas.dto.CostosMensualesRequest;
import fi.uba.memo1.apirest.finanzas.dto.CostosMensualesResponse;
import fi.uba.memo1.apirest.finanzas.dto.TotalCostosProyectoResponse;
import fi.uba.memo1.apirest.finanzas.dto.CostoRequest;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface ICostosMensualesService {

    Mono<List<CostosMensualesResponse>> findAll();

    Mono<CostosMensualesResponse> findById(Long id);

    Mono<List<CostosMensualesResponse>> save(List<CostosMensualesRequest> costos);

    Mono<CostosMensualesResponse> update(Long id, CostoRequest costoRequest);

    Mono<List<CostosMensualesResponse>> updateAll(Map<Long, CostoRequest> costosRequest);

    Mono<TotalCostosProyectoResponse> obtenerCostosDeProyectos(String anio);
}
