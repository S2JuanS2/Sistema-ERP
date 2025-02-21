package fi.uba.memo1.apirest.finanzas.steps;

import fi.uba.memo1.apirest.finanzas.dto.CostosMensualesRequest;
import fi.uba.memo1.apirest.finanzas.dto.CostosMensualesResponse;
import io.cucumber.java.Before;
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
public class CargarCostosSteps {
    private ResponseEntity<List<CostosMensualesResponse>> response;
    private WebClientResponseException exception;
    private Double newCost;

    private WebClient webClient;

    @LocalServerPort
    private int port;

    @Before
    public void init() {
        this.webClient = WebClient.builder().baseUrl("http://localhost:" + port).build();
    }


    @Given("I am on the page that allows me to load the cost of a role")
    public void iAmOnThePageThatAllowsMeToLoadTheCostOfARole() {
    }

    @When("I try to add a role with name {string} and experience {string} and cost {string}")
    public void iTryToAddARoleWithNameAndExperienceAndCost(String name, String experience, String cost) {
        List<CostosMensualesRequest> requestList = new ArrayList<>();
        CostosMensualesRequest request = new CostosMensualesRequest();
        request.setNombre(name);
        request.setExperiencia(experience);
        request.setCosto(Double.parseDouble(cost));

        requestList.add(request);

        try {
            this.response = webClient.post()
                    .uri("/api/v1/finanzas/cargar-costo")
                    .body(Mono.just(requestList), new ParameterizedTypeReference<List<CostosMensualesResponse>>() {})
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<List<CostosMensualesResponse>>() {})
                    .block();
        } catch (WebClientResponseException e) {
            this.exception = e;
        }
    }

    @Then("the system should load the role correctly")
    public void theSystemShouldLoadTheRoleCorrectly() {
        List<CostosMensualesResponse> costoObtenido = this.response.getBody();

        assertTrue(this.response.getStatusCode().is2xxSuccessful());
        assertFalse(Objects.requireNonNull(costoObtenido).isEmpty());
    }

    @Then("the system should throw an error and not load the role")
    public void theSystemShouldThrowAnErrorAndNotLoadTheRole() {
        assertNull(this.response);
        assertTrue(this.exception.getStatusCode().is4xxClientError());
    }

    @When("I try to add a role with name {string} and experience {string} and cost {string} and month {string} and year {string}")
    public void iTryToAddARoleWithNameAndExperienceAndCostAndMonthAndYear(String name, String experience, String cost, String month, String year) {
        List<CostosMensualesRequest> requestList = new ArrayList<>();
        CostosMensualesRequest request = new CostosMensualesRequest();
        request.setNombre(name);
        request.setExperiencia(experience);
        request.setCosto(Double.parseDouble(cost));
        request.setMes(month);
        request.setAnio(year);
        this.newCost = Double.parseDouble(cost);

        requestList.add(request);

        try {
            this.response = webClient.post()
                    .uri("/api/v1/finanzas/cargar-costo")
                    .body(Mono.just(requestList), new ParameterizedTypeReference<List<CostosMensualesResponse>>() {})
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<List<CostosMensualesResponse>>() {})
                    .block();
        } catch (WebClientResponseException e) {
            this.exception = e;
        }
    }

    @Then("the system should return the new cost of the role correctly")
    public void theSystemShouldReturnTheNewCostOfTheRoleCorrectly() {
        List<CostosMensualesResponse> costoObtenido = this.response.getBody();

        assertTrue(this.response.getStatusCode().is2xxSuccessful());
        assertEquals(newCost, Objects.requireNonNull(costoObtenido).get(0).getCosto());
    }
}
