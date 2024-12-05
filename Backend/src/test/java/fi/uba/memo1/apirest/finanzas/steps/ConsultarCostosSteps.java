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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

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

    @Given("I am on the page that allows me to get the costs of all roles")
    public void iAmOnThePageThatAllowsMeToGetTheCostsOfAllRoles() {

    }

    @And("I added a role cost with name {string}, experience {string}, cost {string}")
    public void iHaveAddedARoleCostWithNameExperienceCost(String name, String experience, String cost) {
        List<CostosMensualesRequest> requestList = new ArrayList<>();
        CostosMensualesRequest request = new CostosMensualesRequest();
        request.setNombre(name);
        request.setExperiencia(experience);
        request.setCosto(Double.parseDouble(cost));
        requestList.add(request);
        try {
            ResponseEntity<List<CostosMensualesResponse>> costoObtenido = webClient.post()
                    .uri("/api/v1/finanzas/cargar-costo")
                    .body(Mono.just(requestList), new ParameterizedTypeReference<List<CostosMensualesResponse>>() {})
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<List<CostosMensualesResponse>>() {})
                    .block();

            this.costosAgregados.add(Objects.requireNonNull(Objects.requireNonNull(costoObtenido).getBody()).get(costoObtenido.getBody().size() -1));
        } catch (WebClientResponseException e) {
            this.exception = e;
        }
    }

    @When("I try to get the costs of all roles")
    public void iTryToGetTheCostsOfAllRoles() {
        try {
            this.response = webClient.get()
                    .uri("/api/v1/finanzas/costos")
                    .retrieve()
                    .toEntity(String.class)
                    .block();
        } catch (WebClientResponseException e) {
            this.exception = e;
        }
    }

    @Then("the system should return the costs of the roles correctly")
    public void theSystemShouldReturnTheCostsOfTheRolesCorrectly() throws JsonProcessingException {
        assertTrue(this.response.getStatusCode().is2xxSuccessful());

        ArrayList<CostosMensualesResponse> responseCostos = StepsHelper.jsonToCostoMensualesResponseArray(this.response.getBody());

        assertTrue(responseCostos.containsAll(this.costosAgregados));
    }

    @When("I try to get the costs of the role")
    public void iTryToGetTheCostsOfTheRole() {
        try {
            this.response = webClient.get()
                    .uri("/api/v1/finanzas/costos/" + this.costosAgregados.get(0).getId())
                    .retrieve()
                    .toEntity(String.class)
                    .block();
        } catch (WebClientResponseException e) {
            this.exception = e;
        }
    }

    @Then("the system should return the cost of the role correctly")
    public void theSystemShouldReturnTheCostOfTheRoleCorrectly() throws JsonProcessingException {
        assertTrue(this.response.getStatusCode().is2xxSuccessful());

        CostosMensualesResponse costoObtenido = StepsHelper.jsonToCostoMensualesResponse(this.response.getBody());

        assertEquals(this.costosAgregados.get(0), costoObtenido);
    }

    @When("I try to get the costs of the role with an invalid id")
    public void iTryToGetTheCostsOfTheRoleWithAnInvalidId() {
        try {
            this.response = webClient.get()
                    .uri("/api/v1/finanzas/costos/1000")
                    .retrieve()
                    .toEntity(String.class)
                    .block();
        } catch (WebClientResponseException e) {
            this.exception = e;
        }
    }

    @Then("the system should return an error message")
    public void theSystemShouldReturnAnErrorMessage() {
        assertTrue(Objects.nonNull(this.exception));

        assertTrue(this.exception.getStatusCode().is4xxClientError());
    }
}
