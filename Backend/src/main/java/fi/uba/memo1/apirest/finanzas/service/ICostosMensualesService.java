package fi.uba.memo1.apirest.finanzas.service;

import fi.uba.memo1.apirest.finanzas.dto.CostosMensualesRequest;
import fi.uba.memo1.apirest.finanzas.dto.CostosMensualesResponse;
import fi.uba.memo1.apirest.finanzas.model.CostosMensuales;
import jakarta.transaction.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ICostosMensualesService {

    List<CostosMensuales> findAll();

    CostosMensuales findById(Long id);

    Mono<CostosMensualesResponse> save(CostosMensualesRequest costos);
}
