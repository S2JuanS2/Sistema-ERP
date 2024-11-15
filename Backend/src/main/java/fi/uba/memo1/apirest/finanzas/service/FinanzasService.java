package fi.uba.memo1.apirest.finanzas.service;


import fi.uba.memo1.apirest.finanzas.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;

@Service
public class FinanzasService implements IFinanzasService {

    @Autowired
    @Qualifier("proyectosWebClient")
    private WebClient proyectosWebClient;


    @Override
    public String getHelloWorld() {
        return "Hola mundo";
    }

    @Override
    public List<Project> getProjects() {
        return proyectosWebClient
                .get()
                .uri("/proyectos")
                .retrieve()
                .bodyToFlux(Project.class)
                .collectList()
                .block(Duration.ofMillis(100_000));
        }
}
