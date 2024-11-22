package fi.uba.memo1.apirest.finanzas.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import fi.uba.memo1.apirest.finanzas.dto.CostosMensualesRequest;
import fi.uba.memo1.apirest.finanzas.dto.CostosMensualesResponse;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConsultarCostosSteps {
    private ResponseEntity<String> response;
    private WebClientResponseException exception;
    private WebClient webClient;
    private List<CostosMensualesResponse> costosAgregados;

    @LocalServerPort
    private int port;

    @Before
    public void init() {
        this.webClient = WebClient.builder().baseUrl("http://localhost:" + port).build();
        this.costosAgregados = new ArrayList<>();
    }

    @Given("I add a role cost with name {string}, experience {string}, cost {string}")
    public void iAddARoleCostWithNameExperienceCost(String name, String experience, String cost) {
        CostosMensualesRequest request = new CostosMensualesRequest();
        request.setNombre(name);
        request.setExperiencia(experience);
        request.setCosto(Double.parseDouble(cost));

        try {
            CostosMensualesResponse costoObtenido = webClient.post()
                    .uri("/api/v1/finanzas/cargar-costo")
                    .body(Mono.just(request), CostosMensualesRequest.class)
                    .retrieve()
                    .bodyToMono(CostosMensualesResponse.class)
                    .block();

            this.costosAgregados.add(costoObtenido);
        } catch (WebClientResponseException e) {
            this.exception = e;
        }
    }

    @When("I GET to the route {string}")
    public void iGETToTheRoute(String route) {
        try {
            this.response = webClient.get()
                    .uri(route)
                    .retrieve()
                    .toEntity(String.class)
                    .block();
        } catch (WebClientResponseException e) {
            this.exception = e;
        }
    }

    @And("the response should only contain the first and second roles")
    public void theResponseShouldOnlyContainTheFirstAndSecondRoles() throws JsonProcessingException {
        ArrayList<CostosMensualesResponse> responseCostos = StepsHelper.jsonToCostoMensualesResponseArray(this.response.getBody());

        assertTrue(responseCostos.contains(this.costosAgregados.get(0)));
        assertTrue(responseCostos.contains(this.costosAgregados.get(1)));
    }

    @Then("the response should have status {int}")
    public void theResponseShouldHaveStatus(int status) {
        if (this.exception != null) {
            assertEquals(status, this.exception.getStatusCode().value());
            return;
        }

        assertEquals(status, this.response.getStatusCode().value());
    }

    @When("I GET to the route {string} with the id of the first role cost")
    public void iGETToTheRouteWithTheIdOfTheFirstRoleCost(String route) {
        try {
            this.response = webClient.get()
                    .uri(route + this.costosAgregados.get(0).getId())
                    .retrieve()
                    .toEntity(String.class)
                    .block();
        } catch (WebClientResponseException e) {
            this.exception = e;
        }
    }

    @And("the response should contain the first role cost")
    public void theResponseShouldContainTheFirstRoleCost() throws JsonProcessingException {
        CostosMensualesResponse costoObtenido = StepsHelper.jsonToCostoMensualesResponse(this.response.getBody());

        assertEquals(this.costosAgregados.get(0), costoObtenido);
    }

    @When("I GET to the route {string} with an invalid id")
    public void iGETToTheRouteWithAnInvalidId(String route) {
        try {
            this.response = webClient.get()
                    .uri(route + "1000")
                    .retrieve()
                    .toEntity(String.class)
                    .block();

        } catch (WebClientResponseException e) {
            this.exception = e;
        }
    }

    @And("the response should contain the message {string}")
    public void theResponseShouldContainTheMessage(String message) throws JsonProcessingException {
        String responseBody = StepsHelper.jsonErrorToMessage(exception.getResponseBodyAsString());

        assertEquals(message, responseBody);
    }
}
