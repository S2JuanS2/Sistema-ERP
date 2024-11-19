package fi.uba.memo1.apirest.finanzas.unit;

import fi.uba.memo1.apirest.finanzas.dto.CargarCostoRequest;
import fi.uba.memo1.apirest.finanzas.model.CostosMensuales;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FinanzasApplicationTests {
    private static final String COSTOS_URL = "/api/v1/finanzas/costos";
    private final String CARGAR_COSTOS_URL = "/api/v1/finanzas/cargar-costo";
    private WebClient webClient;

    @LocalServerPort
    private int port;


    @BeforeEach
    void init() {
        this.webClient = WebClient.builder().baseUrl("http://localhost:" + port).build();
    }

    @Test
    void cargarCostoExitosamente() {
        CargarCostoRequest request = new CargarCostoRequest();
        request.setCosto(1000);
        request.setNombre("Desarrollador");
        request.setExperiencia("Senior");

        Mono<String> response = webClient.post()
                .uri(CARGAR_COSTOS_URL)
                .body(Mono.just(request), CargarCostoRequest.class)
                .retrieve()
                .bodyToMono(String.class);

        String res = response.block();
        assertTrue(Objects.requireNonNull(res).startsWith("Se cargo el costo con ID:"));
    }

    @Test
    void noSePuedeCargarCostoConNombreInexistente() {
        CargarCostoRequest request = new CargarCostoRequest();
        request.setCosto(1000);
        request.setNombre("Administrador");
        request.setExperiencia("Senior");

        WebClientResponseException exception = assertThrows(WebClientResponseException.class, () -> {
            Mono<String> response = webClient.post()
                    .uri(CARGAR_COSTOS_URL)
                    .body(Mono.just(request), CargarCostoRequest.class)
                    .retrieve()
                    .bodyToMono(String.class);

            response.block();
        });

        assertEquals(404, exception.getStatusCode().value());
        assertEquals("Rol no encontrado", exception.getResponseBodyAsString());
    }


    @Test
    void noSePuedeCargarCostoConExperienciaInexistente() {
        CargarCostoRequest request = new CargarCostoRequest();
        request.setCosto(1000);
        request.setNombre("Desarrollador");
        request.setExperiencia("Entry");

        WebClientResponseException exception = assertThrows(WebClientResponseException.class, () -> {
            Mono<String> response = webClient.post()
                    .uri(CARGAR_COSTOS_URL)
                    .body(Mono.just(request), CargarCostoRequest.class)
                    .retrieve()
                    .bodyToMono(String.class);

            response.block();
        });

        assertEquals(404, exception.getStatusCode().value());
        assertEquals("Rol no encontrado", exception.getResponseBodyAsString());
    }

    @Test
    void seAgregaElCostoALaTablaCorrectamente() {
        Mono<List<CostosMensuales>> response = webClient.get()
                .uri(COSTOS_URL)
                .retrieve()
                .bodyToFlux(CostosMensuales.class)
                .collectList();

        List<CostosMensuales> costos = response.block();
        int count = Objects.requireNonNull(costos).size();

        CargarCostoRequest request = new CargarCostoRequest();
        request.setCosto(1000);
        request.setNombre("Desarrollador");
        request.setExperiencia("Senior");

        Mono<String> response2 = webClient.post()
                .uri(CARGAR_COSTOS_URL)
                .body(Mono.just(request), CargarCostoRequest.class)
                .retrieve()
                .bodyToMono(String.class);

        response2.block();

        Mono<List<CostosMensuales>> response3 = webClient.get()
                .uri(COSTOS_URL)
                .retrieve()
                .bodyToFlux(CostosMensuales.class)
                .collectList();

        List<CostosMensuales> costos2 = response3.block();

        assertEquals(count + 1, Objects.requireNonNull(costos2).size());
    }
}
