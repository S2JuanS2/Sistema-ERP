package fi.uba.memo1.apirest.finanzas.unit;

import fi.uba.memo1.apirest.finanzas.dto.CostoRequest;
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
        assertEquals("No se encontró un rol con nombre y experiencia coincidentes", Objects.requireNonNull(exception.getResponseBodyAs(ErrorResponse.class)).getMessage());
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
        assertEquals("No se encontró un rol con nombre y experiencia coincidentes", Objects.requireNonNull(exception.getResponseBodyAs(ErrorResponse.class)).getMessage());
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

    @Test
    void seBuscaElIDCorrectamente() {
        CostosMensualesRequest request = new CostosMensualesRequest();
        request.setCosto(1000);
        request.setNombre("Desarrollador");
        request.setExperiencia("Senior");

        Mono<CostosMensualesResponse> response2 = webClient.post()
                .uri(CARGAR_COSTOS_URL)
                .body(Mono.just(request), CostosMensualesRequest.class)
                .retrieve()
                .bodyToMono(CostosMensualesResponse.class);

        CostosMensualesResponse costosMensualesResponse = response2.block();

        Mono<List<CostosMensuales>> response = webClient.get()
                .uri(COSTOS_URL + "/" + Objects.requireNonNull(costosMensualesResponse).getId().toString())
                .retrieve()
                .bodyToFlux(CostosMensuales.class)
                .collectList();

        List<CostosMensuales> costos = response.block();
        CostosMensuales ultimoCosto = Objects.requireNonNull(costos).get(costos.size() - 1);

        assertEquals(1000, ultimoCosto.getCosto());
    }

    @Test
    void noSePuedeBuscarCostoInexistente() {
        WebClientResponseException exception = assertThrows(WebClientResponseException.class, () -> {
            Mono<String> response = webClient.get()
                    .uri(COSTOS_URL + "/1000")
                    .retrieve()
                    .bodyToMono(String.class);

            response.block();
        });

        assertEquals(404, exception.getStatusCode().value());
        assertEquals("No se encontró un rol con nombre y experiencia coincidentes", Objects.requireNonNull(exception.getResponseBodyAs(ErrorResponse.class)).getMessage());
    }

    @Test
    void seModificaElCostoCorrectamente() {
        CostosMensualesRequest request = new CostosMensualesRequest();
        request.setCosto(1000);
        request.setNombre("Desarrollador");
        request.setExperiencia("Senior");

        Mono<CostosMensualesResponse> response2 = webClient.post()
                .uri(CARGAR_COSTOS_URL)
                .body(Mono.just(request), CostosMensualesRequest.class)
                .retrieve()
                .bodyToMono(CostosMensualesResponse.class);

        CostosMensualesResponse costosMensualesResponse = response2.block();

        CostoRequest costoRequest = new CostoRequest();
        costoRequest.setCosto(1200);

        Mono<CostosMensualesResponse> response3 = webClient.put()
                .uri("/api/v1/finanzas/actualizar-costo/" + costosMensualesResponse.getId())
                .body(Mono.just(costoRequest), CostoRequest.class)
                .retrieve()
                .bodyToMono(CostosMensualesResponse.class);

        CostosMensualesResponse updatedCostosMensualesResponse = response3.block();

        assertNotNull(updatedCostosMensualesResponse);
        assertEquals(1200, updatedCostosMensualesResponse.getCosto());
        assertEquals(costosMensualesResponse.getId(), updatedCostosMensualesResponse.getId());
    }

    @Test
    void noSePuedeCrearCostoConValorNegativo() {
        CostosMensualesRequest request = new CostosMensualesRequest();
        request.setCosto(-100);
        request.setNombre("Desarrollador");
        request.setExperiencia("Senior");

        WebClientResponseException exception = assertThrows(WebClientResponseException.class, () -> {
            Mono<String> response = webClient.post()
                    .uri(CARGAR_COSTOS_URL)
                    .body(Mono.just(request), CostosMensualesRequest.class)
                    .retrieve()
                    .bodyToMono(String.class);

            response.block();
        });

        assertEquals(400, exception.getStatusCode().value());
        assertEquals("El nuevo costo del recurso no puede ser un monto negativo", 
        Objects.requireNonNull(exception.getResponseBodyAs(ErrorResponse.class)).getMessage());
    }

    @Test
    void noSePuedeModificarCostoConValorNegativo() {

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

        CostoRequest costoRequest = new CostoRequest();
        costoRequest.setCosto(-500);

        WebClientResponseException exception = assertThrows(WebClientResponseException.class, () -> {
            Mono<String> response2 = webClient.put()
                    .uri("/api/v1/finanzas/actualizar-costo/" + Objects.requireNonNull(costosMensualesResponse).getId())
                    .body(Mono.just(costoRequest), CostoRequest.class)
                    .retrieve()
                    .bodyToMono(String.class);

            response2.block();
        });

        assertEquals(400, exception.getStatusCode().value());
        assertEquals("El nuevo costo del recurso no puede ser un monto negativo", 
        Objects.requireNonNull(exception.getResponseBodyAs(ErrorResponse.class)).getMessage());
    }


}
