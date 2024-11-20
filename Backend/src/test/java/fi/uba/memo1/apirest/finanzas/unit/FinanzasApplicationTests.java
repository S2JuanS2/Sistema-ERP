package fi.uba.memo1.apirest.finanzas.unit;

import fi.uba.memo1.apirest.finanzas.dto.CostosMensualesRequest;
import fi.uba.memo1.apirest.finanzas.dto.CostosMensualesResponse;
import fi.uba.memo1.apirest.finanzas.dto.ErrorResponse;
import fi.uba.memo1.apirest.finanzas.model.CostosMensuales;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
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
        CostosMensualesRequest request = new CostosMensualesRequest();
        request.setCosto(1000);
        request.setNombre("Desarrollador");
        request.setExperiencia("Senior");

        Mono<CostosMensualesResponse> response = webClient.post()
                .uri(CARGAR_COSTOS_URL)
                .body(Mono.just(request), CostosMensualesRequest.class)
                .retrieve()
                .bodyToMono(CostosMensualesResponse.class);

        CostosMensualesResponse costosMensualesResponse = response.block();
        assertNotNull(costosMensualesResponse);
        assertEquals(1000, costosMensualesResponse.getCosto());
    }

    @Test
    void noSePuedeCargarCostoConNombreInexistente() {
        CostosMensualesRequest request = new CostosMensualesRequest();
        request.setCosto(1000);
        request.setNombre("Administrador");
        request.setExperiencia("Senior");

        WebClientResponseException exception = assertThrows(WebClientResponseException.class, () -> {
            Mono<String> response = webClient.post()
                    .uri(CARGAR_COSTOS_URL)
                    .body(Mono.just(request), CostosMensualesRequest.class)
                    .retrieve()
                    .bodyToMono(String.class);

            response.block();
        });

        assertEquals(404, exception.getStatusCode().value());
        assertEquals("No se encontró un rol con nombre y experiencia coincidentes", exception.getResponseBodyAs(ErrorResponse.class).getMessage());
    }


    @Test
    void noSePuedeCargarCostoConExperienciaInexistente() {
        CostosMensualesRequest request = new CostosMensualesRequest();
        request.setCosto(1000);
        request.setNombre("Desarrollador");
        request.setExperiencia("Entry");

        WebClientResponseException exception = assertThrows(WebClientResponseException.class, () -> {
            Mono<String> response = webClient.post()
                    .uri(CARGAR_COSTOS_URL)
                    .body(Mono.just(request), CostosMensualesRequest.class)
                    .retrieve()
                    .bodyToMono(String.class);

            response.block();
        });

        assertEquals(404, exception.getStatusCode().value());
        assertEquals("No se encontró un rol con nombre y experiencia coincidentes", exception.getResponseBodyAs(ErrorResponse.class).getMessage());
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

        CostosMensualesRequest request = new CostosMensualesRequest();
        request.setCosto(1000);
        request.setNombre("Desarrollador");
        request.setExperiencia("Senior");

        Mono<String> response2 = webClient.post()
                .uri(CARGAR_COSTOS_URL)
                .body(Mono.just(request), CostosMensualesRequest.class)
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
