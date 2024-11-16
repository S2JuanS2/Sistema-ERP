package fi.uba.memo1.apirest.finanzas.service;

import fi.uba.memo1.apirest.finanzas.model.Project;
import fi.uba.memo1.apirest.finanzas.model.Test;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IFinanzasService {

    String getHelloWorld(); // TODO: prueba

    Mono<List<Project>> getProjectsTest(); // TODO: prueba api

    Mono<List<Test>> getTasksWithDetailsTest(); // TODO: prueba api composicion
}
