package fi.uba.memo1.apirest.finanzas.steps;

import fi.uba.memo1.apirest.finanzas.dto.CargarCostoRequest;
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

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CargarCostosSteps {
    private String name;
    private double cost;
    private String experience;
    private ResponseEntity<String> response;
    private WebClientResponseException exception;

    private WebClient webClient;

    @LocalServerPort
    private int port;

    @Before
    public void init() {
        this.webClient = WebClient.builder().baseUrl("http://localhost:" + port).build();
    }


    @And("a role experience {string}")
    public void aRoleExperience(String experience) {
        this.experience = experience;
    }

    @Given("I have a role name {string}")
    public void iHaveARoleName(String name) {
        this.name = name;
    }

    @When("I POST to the route {string}")
    public void iPOSTToTheRoute(String route) {
        CargarCostoRequest request = new CargarCostoRequest();
        request.setNombre(name);
        request.setExperiencia(experience);
        request.setCosto(cost);

        try {
            Mono<ResponseEntity<String>> res = webClient.post()
                    .uri(route)
                    .body(Mono.just(request), CargarCostoRequest.class)
                    .retrieve()
                    .toEntity(String.class);

            this.response = res.block();
        } catch (WebClientResponseException e) {
            this.exception = e;
        }

    }

    @And("a role cost {string}")
    public void aRoleCost(String cost) {
        this.cost = Double.parseDouble(cost);
    }

    @Then("the status code should be {int}")
    public void theStatusCodeShouldBe(int statusCode) {
        if (this.exception != null) {
            assertEquals(statusCode, this.exception.getStatusCode().value());
            return;
        }

        assertEquals(statusCode, response.getStatusCode().value());
    }

    @And("the response should be {string}")
    public void theResponseShouldBe(String text) {
        if (this.exception != null) {
            assertEquals(text, this.exception.getResponseBodyAsString());
            return;
        }

        assertTrue(Objects.requireNonNull(this.response.getBody()).startsWith(text));
    }
}
