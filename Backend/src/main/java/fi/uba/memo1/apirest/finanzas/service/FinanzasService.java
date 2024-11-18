package fi.uba.memo1.apirest.finanzas.service;


import fi.uba.memo1.apirest.finanzas.model.Proyecto;
import fi.uba.memo1.apirest.finanzas.model.Recurso;
import fi.uba.memo1.apirest.finanzas.model.Tarea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class FinanzasService implements IFinanzasService {

    @Autowired
    @Qualifier("proyectosWebClient")
    private WebClient proyectosWebClient;

    @Autowired
    @Qualifier("recursosWebClient")
    private WebClient recursosWebClient;

    @Autowired
    @Qualifier("tareasWebClient")
    private WebClient tareasWebClient;



        /*
    @Override
    public Mono<List<Test>> getTasksWithDetailsTest() {
        // Obtener los datos de las tres APIs
        Mono<List<Tarea>> tareasMono = tareasWebClient
                .get()
                .uri("/tareas")
                .retrieve()
                .bodyToFlux(Tarea.class)
                .collectList();

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

        // Combinar los datos
        return Mono.zip(tareasMono, recursosMono, proyectosMono)
                .map(tuple -> {
                    List<Tarea> tareas = tuple.getT1();
                    List<Recurso> recursos = tuple.getT2();
                    List<Proyecto> proyectos = tuple.getT3();

                    // Mapear las tareas con los detalles combinados
                    return tareas.stream().map(tarea -> {
                        Test response = new Test();

                        response.setId(tarea.getId());
                        response.setNombre(tarea.getNombre());
                        response.setDescripcion(tarea.getDescripcion());

                        response.setRecurso(
                                recursos.stream()
                                        .filter(recurso -> recurso.getId().equals(tarea.getRecursoId()))
                                        .findFirst()
                                        .orElse(null)
                        );

                        response.setProyecto(
                                proyectos.stream()
                                        .filter(proyecto -> proyecto.getId().equals(tarea.getProyectoId()))
                                        .findFirst()
                                        .orElse(null)
                        );

                        return response;
                    }).toList();
                });
    }

         */

}
