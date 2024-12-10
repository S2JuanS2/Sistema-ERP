package fi.uba.memo1.apirest.finanzas.steps;

import fi.uba.memo1.apirest.finanzas.dto.CostoRequest;
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
public class ModificarCostoSteps {
    private Long id;
    private ResponseEntity<CostosMensualesResponse> response;
    private WebClientResponseException exception;

    private WebClient webClient;


    @LocalServerPort
    private int port;

    @Before
    public void init() {
        this.webClient = WebClient.builder().baseUrl("http://localhost:" + port).build();
    }

    @Given("I am on the page that allows me to edit the costs of all roles")
    public void iAmOnThePageThatAllowsMeToEditTheCostsOfAllRoles() {
    }

    @When("I update the role cost to {string}")
    public void iUpdateTheRoleCostTo(String string) {
        CostoRequest request = new CostoRequest();
        request.setCosto(Double.parseDouble(string));

        try {
            this.response = webClient.put()
                    .uri("/api/v1/finanzas/actualizar-costo/" + this.id)
                    .body(Mono.just(request), CostoRequest.class)
                    .retrieve()
                    .toEntity(CostosMensualesResponse.class)
                    .block();

        } catch (WebClientResponseException e) {
            this.exception = e;
        }
    }

    @Then("the system should return the role with the new cost {string} correctly")
    public void theSystemShouldReturnTheRoleWithTheNewCostCorrectly(String string) {
        assert response.getBody() != null;
        double costoActualizado = Objects.requireNonNull(response.getBody()).getCosto();
        assertEquals(Double.parseDouble(string), costoActualizado, 0.01);
    }

    @And("I previously added a role cost with name {string}, experience {string}, cost {string}")
    public void iPreviouslyAddedARoleCostWithNameExperienceCost(String name, String experience, String cost) {
        List<CostosMensualesRequest> requestList = new ArrayList<>();
        CostosMensualesRequest request = new CostosMensualesRequest();
        request.setNombre(name);
        request.setExperiencia(experience);
        request.setCosto(Double.parseDouble(cost));

        requestList.add(request);

        try {
            ResponseEntity<List<CostosMensualesResponse>> res = webClient.post()
                    .uri("/api/v1/finanzas/cargar-costo")
                    .body(Mono.just(requestList), new ParameterizedTypeReference<List<CostosMensualesResponse>>() {})
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<List<CostosMensualesResponse>>() {})
                    .block();

            this.id = Objects.requireNonNull(Objects.requireNonNull(res).getBody()).get(0).getId();
        } catch (WebClientResponseException e) {
            this.exception = e;
        }
    }

    @Then("the system should return an error")
    public void theSystemShouldReturnAnErrorMessageDueToNegativeAmount() {
        assertNotNull(this.exception);
        assertTrue(this.exception.getStatusCode().is4xxClientError());
    }

    @And("I have not added any role cost")
    public void iHaveNotAddedAnyRoleCost() {
    }
}
