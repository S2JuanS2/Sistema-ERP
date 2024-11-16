package fi.uba.memo1.apirest.finanzas.service;


import fi.uba.memo1.apirest.finanzas.model.Project;
import fi.uba.memo1.apirest.finanzas.model.Resource;
import fi.uba.memo1.apirest.finanzas.model.Task;
import fi.uba.memo1.apirest.finanzas.model.Test;
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


    @Override
    public String getHelloWorld() {
        return "Hola mundo";
    }

    @Override
    public Mono<List<Project>> getProjects() {
        return proyectosWebClient
                .get()
                .uri("/proyectos")
                .retrieve()
                .bodyToFlux(Project.class)
                .collectList();
        }

    @Override
    public Mono<List<Test>> getTasksWithDetails() {
        // Obtener los datos de las tres APIs
        Mono<List<Task>> tareasMono = tareasWebClient
                .get()
                .uri("/tareas")
                .retrieve()
                .bodyToFlux(Task.class)
                .collectList();

        Mono<List<Resource>> recursosMono = recursosWebClient
                .get()
                .uri("/recursos")
                .retrieve()
                .bodyToFlux(Resource.class)
                .collectList();

        Mono<List<Project>> proyectosMono = proyectosWebClient
                .get()
                .uri("/proyectos")
                .retrieve()
                .bodyToFlux(Project.class)
                .collectList();

        // Combinar los datos
        return Mono.zip(tareasMono, recursosMono, proyectosMono)
                .map(tuple -> {
                    List<Task> tareas = tuple.getT1();
                    List<Resource> recursos = tuple.getT2();
                    List<Project> proyectos = tuple.getT3();

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

}
