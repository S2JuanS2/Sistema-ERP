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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        List<CostosMensualesRequest> requestList = new ArrayList<>();
        CostosMensualesRequest request = new CostosMensualesRequest();
        request.setCosto(1000);
        request.setNombre("Desarrollador");
        request.setExperiencia("Senior");
        requestList.add(request);
    
        ParameterizedTypeReference<List<CostosMensualesResponse>> responseType =
                new ParameterizedTypeReference<List<CostosMensualesResponse>>() {};
    
        List<CostosMensualesResponse> responseList = webClient.post()
                .uri(CARGAR_COSTOS_URL)
                .bodyValue(requestList)
                .retrieve()
                .bodyToMono(responseType)
                .block();
    
        assertNotNull(responseList);
        assertEquals(1, responseList.size());
        CostosMensualesResponse costosMensualesResponse = responseList.get(0);
        assertEquals(1000, costosMensualesResponse.getCosto());
    }
    

    @Test
    void noSePuedeCargarCostoConNombreInexistente() {
        List<CostosMensualesRequest> requestList = new ArrayList<>();
        CostosMensualesRequest request = new CostosMensualesRequest();
        request.setCosto(1000);
        request.setNombre("Administrador");
        request.setExperiencia("Senior");
        requestList.add(request);
    
        WebClientResponseException exception = assertThrows(WebClientResponseException.class, () -> {
            webClient.post()
                    .uri(CARGAR_COSTOS_URL)
                    .bodyValue(requestList)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        });
    
        assertEquals(404, exception.getStatusCode().value());
        ErrorResponse errorResponse = exception.getResponseBodyAs(ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals("No se encontró un rol con nombre y experiencia coincidentes", errorResponse.getMessage());
    }
    


    @Test
    void noSePuedeCargarCostoConExperienciaInexistente() {
        List<CostosMensualesRequest> requestList = new ArrayList<>();
        CostosMensualesRequest request = new CostosMensualesRequest();
        request.setCosto(1000);
        request.setNombre("Desarrollador");
        request.setExperiencia("Entry");
        requestList.add(request);
    
        WebClientResponseException exception = assertThrows(WebClientResponseException.class, () -> {
            webClient.post()
                    .uri(CARGAR_COSTOS_URL)
                    .bodyValue(requestList)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        });
    
        assertEquals(404, exception.getStatusCode().value());
        ErrorResponse errorResponse = exception.getResponseBodyAs(ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals("No se encontró un rol con nombre y experiencia coincidentes", errorResponse.getMessage());
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

        List<CostosMensualesRequest> requestList = new ArrayList<>();
        CostosMensualesRequest request = new CostosMensualesRequest();
        request.setCosto(1000);
        request.setNombre("Desarrollador");
        request.setExperiencia("Senior");
        requestList.add(request);

        Mono<String> response2 = webClient.post()
                .uri(CARGAR_COSTOS_URL)
                .body(Mono.just(requestList), new ParameterizedTypeReference<List<CostosMensualesResponse>>() {})
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
        List<CostosMensualesRequest> requestList = new ArrayList<>();
        CostosMensualesRequest request = new CostosMensualesRequest();
        request.setCosto(1000);
        request.setNombre("Desarrollador");
        request.setExperiencia("Senior");
        requestList.add(request);

        ParameterizedTypeReference<List<CostosMensualesResponse>> responseType =
                new ParameterizedTypeReference<List<CostosMensualesResponse>>() {};

        Mono<List<CostosMensualesResponse>> response2 = webClient.post()
                .uri(CARGAR_COSTOS_URL)
                .body(Mono.just(requestList), new ParameterizedTypeReference<List<CostosMensualesResponse>>() {})
                .retrieve()
                .bodyToMono(responseType);

        CostosMensualesResponse costosMensualesResponse = response2.block().get(response2.block().size()-1);

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
        List<CostosMensualesRequest> requestList = new ArrayList<>();
        CostosMensualesRequest request = new CostosMensualesRequest();
        request.setCosto(1000);
        request.setNombre("Desarrollador");
        request.setExperiencia("Senior");
        requestList.add(request);

        ParameterizedTypeReference<List<CostosMensualesResponse>> responseType =
                new ParameterizedTypeReference<List<CostosMensualesResponse>>() {};

        Mono<List<CostosMensualesResponse>> response2 = webClient.post()
                .uri(CARGAR_COSTOS_URL)
                .body(Mono.just(requestList), new ParameterizedTypeReference<List<CostosMensualesResponse>>() {})
                .retrieve()
                .bodyToMono(responseType);

        CostosMensualesResponse costosMensualesResponse = response2.block().get(response2.block().size()-1);

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
    void seModificanLosCostosCorrectamente() {
        List<CostosMensualesRequest> requestList = new ArrayList<>();
        CostosMensualesRequest request = new CostosMensualesRequest();
        request.setCosto(1000);
        request.setNombre("Desarrollador");
        request.setExperiencia("Senior");
        requestList.add(request);
    
        ParameterizedTypeReference<List<CostosMensualesResponse>> responseType =
                new ParameterizedTypeReference<>() {};
    
        List<CostosMensualesResponse> initialResponses = webClient.post()
                .uri(CARGAR_COSTOS_URL)
                .body(Mono.just(requestList), ParameterizedTypeReference.forType(List.class))
                .retrieve()
                .bodyToMono(responseType)
                .block();
    
        assertNotNull(initialResponses);
        assertFalse(initialResponses.isEmpty());
    
        CostosMensualesResponse costosMensualesResponse = initialResponses.get(initialResponses.size() - 1);
    
        Map<Long, CostoRequest> costosList = new HashMap<>();
        CostoRequest costoRequest = new CostoRequest();
        costoRequest.setCosto(1200);
        costosList.put(costosMensualesResponse.getId(), costoRequest);
    
        ParameterizedTypeReference<List<CostosMensualesResponse>> updateResponseType =
                new ParameterizedTypeReference<>() {};
    
        List<CostosMensualesResponse> updatedResponses = webClient.put()
                .uri("/api/v1/finanzas/actualizar-costos")
                .body(Mono.just(costosList), ParameterizedTypeReference.forType(Map.class))
                .retrieve()
                .bodyToMono(updateResponseType)
                .block();
    
        assertNotNull(updatedResponses);
        assertFalse(updatedResponses.isEmpty());
    
        CostosMensualesResponse updatedCostosMensualesResponse = updatedResponses.stream()
                .filter(response -> response.getId().equals(costosMensualesResponse.getId()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("El costo actualizado no se encontró en la respuesta."));
    
        assertEquals(1200, updatedCostosMensualesResponse.getCosto());
        assertEquals(costosMensualesResponse.getId(), updatedCostosMensualesResponse.getId());
    }
    

    @Test
    void noSePuedeCrearCostoConValorNegativo() {
        List<CostosMensualesRequest> requestList = new ArrayList<>();
        CostosMensualesRequest request = new CostosMensualesRequest();
        request.setCosto(-100);
        request.setNombre("Desarrollador");
        request.setExperiencia("Senior");
        requestList.add(request);

        WebClientResponseException exception = assertThrows(WebClientResponseException.class, () -> {
            Mono<String> response = webClient.post()
                    .uri(CARGAR_COSTOS_URL)
                    .body(Mono.just(requestList), new ParameterizedTypeReference<List<CostosMensualesResponse>>() {})
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
        List<CostosMensualesRequest> requestList = new ArrayList<>();
        CostosMensualesRequest request = new CostosMensualesRequest();
        request.setCosto(1000);
        request.setNombre("Desarrollador");
        request.setExperiencia("Senior");
        requestList.add(request);

        ParameterizedTypeReference<List<CostosMensualesResponse>> responseType =
                new ParameterizedTypeReference<List<CostosMensualesResponse>>() {};

        Mono<List<CostosMensualesResponse>> response = webClient.post()
                .uri(CARGAR_COSTOS_URL)
                .body(Mono.just(requestList), new ParameterizedTypeReference<List<CostosMensualesResponse>>() {})
                .retrieve()
                .bodyToMono(responseType);

        CostosMensualesResponse costosMensualesResponse = response.block().get(response.block().size() -1);

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
