package fi.uba.memo1.apirest.finanzas.service;

import fi.uba.memo1.apirest.finanzas.dto.CostosMensualesRequest;
import fi.uba.memo1.apirest.finanzas.dto.CostosMensualesResponse;
import fi.uba.memo1.apirest.finanzas.dto.CargaDeHoras;
import fi.uba.memo1.apirest.finanzas.dto.HorasMensuales;
import fi.uba.memo1.apirest.finanzas.dto.Proyecto;
import fi.uba.memo1.apirest.finanzas.dto.CostosProyectoResponse;
import fi.uba.memo1.apirest.finanzas.dto.Recurso;
import fi.uba.memo1.apirest.finanzas.dto.Rol;
import fi.uba.memo1.apirest.finanzas.dto.TotalCostosProyectoResponse;
import fi.uba.memo1.apirest.finanzas.dto.CostoRequest;
import fi.uba.memo1.apirest.finanzas.exception.RolNoEncontradoException;
import fi.uba.memo1.apirest.finanzas.exception.CostoMensualNegativoException;
import fi.uba.memo1.apirest.finanzas.exception.CostoMensualNoEncontradoException;
import fi.uba.memo1.apirest.finanzas.exception.FechaInvalidaException;
import fi.uba.memo1.apirest.finanzas.exception.RecursoNoEncontradoException;
import fi.uba.memo1.apirest.finanzas.exception.CostoEncontradoException;
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
import java.time.Year;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CostosMensualesService implements ICostosMensualesService {

    public static final int ENERO = 1;
    public static final int DICIEMBRE = 12;
    public static final int ANIO_MINIMO = 2000;

    @Autowired
    @Qualifier("rolesWebClient")
    private WebClient rolesWebClient;
    
    @Autowired
    @Qualifier("proyectosWebClient")
    private WebClient proyectosWebClient;

    @Autowired
    @Qualifier("recursosWebClient")
    private WebClient recursosWebClient;

    @Autowired
    @Qualifier("HorasWebClient")
    private WebClient horasWebClient;

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
        
        if (!(costos.getAnio() == null && costos.getMes() == null)){
                int anio = Integer.parseInt(costos.getAnio());
                int anioActual = Year.now().getValue();
                if (anio < ANIO_MINIMO || anio > anioActual) {
                        return Mono.error(new FechaInvalidaException());
                }
                int mes = Integer.parseInt(costos.getMes());
                if (mes < ENERO || mes > DICIEMBRE) {
                        return Mono.error(new FechaInvalidaException());
                }
        }
            
        if (costos.getCosto() < 0) {
                return Mono.error(new CostoMensualNegativoException());
        }

        Mono<List<Rol>> rolesMono = rolesWebClient
                .get()
                .uri("/roles")
                .retrieve()
                .bodyToFlux(Rol.class)
                .collectList();

        Mono<Rol> matchingRolMono = rolesMono.flatMap(roles ->
                Mono.justOrEmpty(
                        roles.stream()
                                .filter(rol -> costos.getNombre().equalsIgnoreCase(rol.getNombre()) && costos.getExperiencia().equalsIgnoreCase(rol.getExperiencia()))
                                .findFirst()
                )
        ).switchIfEmpty(Mono.error(new RolNoEncontradoException()));

        return matchingRolMono.flatMap(matchingRol -> {
                
            if(repository.existsByIdRolAndAnioAndMes(matchingRol.getId(), costos.getAnio(), costos.getMes())){
                return Mono.error(new CostoEncontradoException());
            }   
            LocalDate currentDate = LocalDate.now();
            CostosMensuales costosMensuales = new CostosMensuales();
            costosMensuales.setIdRol(matchingRol.getId());
            if (costos.getMes() != null){
                costosMensuales.setMes(costos.getMes());
            }else{
                costosMensuales.setMes(String.valueOf(currentDate.getMonthValue()));
            }
            if (costos.getAnio() != null){
                costosMensuales.setAnio(costos.getAnio());
            }else{
                costosMensuales.setAnio(String.valueOf(currentDate.getYear()));
            }
            costosMensuales.setCosto(costos.getCosto());

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

    @Transactional
    @Override
    public Mono<CostosMensualesResponse> update(Long id, CostoRequest costoRequest) {

        if (costoRequest.getCosto() < 0) {
                return Mono.error(new CostoMensualNegativoException());
        }

        return rolesWebClient
            .get()
            .uri("/roles")
            .retrieve()
            .bodyToFlux(Rol.class)
            .collectList()
            .flatMap(roles -> Mono.fromCallable(() -> repository.findById(id)
                    .orElseThrow(CostoMensualNoEncontradoException::new))
                    .flatMap(costoExistente -> {

                        Rol matchingRol = roles.stream()
                                .filter(rol -> rol.getId().equals(costoExistente.getIdRol()))
                                .findFirst()
                                .orElseThrow(RolNoEncontradoException::new);

                        costoExistente.setCosto(costoRequest.getCosto());
                        return Mono.fromCallable(() -> repository.save(costoExistente))
                                .map(costoActualizado -> new CostosMensualesResponse(
                                        costoActualizado.getId(),
                                        matchingRol,
                                        costoActualizado.getMes(),
                                        costoActualizado.getAnio(),
                                        costoActualizado.getCosto()
                                ));
                    })
            )
            .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<TotalCostosProyectoResponse> obtenerCostosDeProyectos(String anio) {
        Mono<List<CostosMensuales>> costosMensualesMono = Mono.just(repository.findAll());
    
        Mono<List<Recurso>> recursosMono = recursosWebClient
                .get()
                .uri("/recursos")
                .retrieve()
                .bodyToFlux(Recurso.class)
                .collectList();
    
        Mono<List<Proyecto>> proyectosMono = proyectosWebClient
                .get()
                .uri("/proyectos")
                .retrieve()
                .bodyToFlux(Proyecto.class)
                .collectList();
     
        return horasWebClient
                .get()
                .uri("/projects")
                .retrieve()
                .bodyToMono(CargaDeHoras.class)
                .flatMap(response -> processProjectCosts(response.getProjects(), anio, costosMensualesMono, recursosMono, proyectosMono));
    }
    
    private Mono<TotalCostosProyectoResponse> processProjectCosts(
            List<HorasMensuales> costosProyectos, 
            String anio,
            Mono<List<CostosMensuales>> costosMensualesMono, 
            Mono<List<Recurso>> recursosMono, 
            Mono<List<Proyecto>> proyectosMono) {
    
        return Mono.zip(proyectosMono, recursosMono, costosMensualesMono)
                .flatMap(tuple -> {
                    List<Proyecto> proyectos = tuple.getT1();
                    List<Recurso> recursos = tuple.getT2();
                    List<CostosMensuales> costosMensuales = tuple.getT3();
                    
                    List<CostosProyectoResponse> costosProyectoResponses = costosProyectos.stream()
                    .map(costoProyecto -> buildProjectResponse(costoProyecto, anio, recursos, costosMensuales, proyectos))
                    .collect(Collectors.toList());
                    
                    TotalCostosProyectoResponse totalCostosProyectoResponse = new TotalCostosProyectoResponse(costosProyectoResponses);
                    for(int i = 0; i < costosProyectoResponses.size(); i++){
                        totalCostosProyectoResponse.sumarCosto(costosProyectoResponses.get(i).getCostoTotal()); 
                    }
                    return Mono.just(totalCostosProyectoResponse);
                });
    }
    
    private CostosProyectoResponse buildProjectResponse(
            HorasMensuales costoProyecto, 
            String anio, 
            List<Recurso> recursos, 
            List<CostosMensuales> costosMensuales, 
            List<Proyecto> proyectos) {
    
        CostosProyectoResponse proyectoResponse = new CostosProyectoResponse();

        for (int i = ENERO; i <= DICIEMBRE; i++) {
            proyectoResponse.getCostoPorMes().put(String.valueOf(i), 0.0);
        }
        if (costoProyecto.getYears().containsKey(anio)) {
            Map<String, Map<String, Integer>> yearMap = costoProyecto.getYears().get(anio);
    
            yearMap.forEach((month, workersMap) -> {
                workersMap.forEach((workerId, hoursWorked) -> {
                    Double costoMensual = calculateMonthlyCost(workerId, month, anio, recursos, costosMensuales);
                    if (costoMensual != null) {
                        Double costoTotalMes = hoursWorked * costoMensual;
                        proyectoResponse.getCostoPorMes().merge(month, costoTotalMes, Double::sum);
                    }
                });
            });
        }
        Double costoTotalAño = 0.0;
        for (Map.Entry<String, Double> entry : proyectoResponse.getCostoPorMes().entrySet()){
                costoTotalAño += entry.getValue();
        }
        proyectoResponse.setCostoTotal(costoTotalAño);

        proyectos.stream()
                .filter(proyecto -> proyecto.getId().equals(costoProyecto.getId()))
                .findFirst()
                .ifPresent(proyecto -> proyectoResponse.setNombreProyecto(proyecto.getNombre()));
    
        return proyectoResponse;
    }
    
    private Double calculateMonthlyCost(
            String workerId, 
            String month, 
            String anio, 
            List<Recurso> recursos, 
            List<CostosMensuales> costosMensuales) {
    
        String rolId = recursos.stream()
                .filter(recurso -> recurso.getId().equals(workerId))
                .map(Recurso::getRolId)
                .findFirst()
                .orElse(null);
    
        if (rolId == null) {
                throw new RecursoNoEncontradoException();
        }
    
        return costosMensuales.stream()
                .filter(costo -> costo.getAnio().equals(anio) &&
                                 costo.getMes().equals(month) &&
                                 costo.getIdRol().equals(rolId))
                .map(CostosMensuales::getCosto)
                .findFirst()
                .orElse(null);
    }

}
