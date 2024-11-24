package fi.uba.memo1.apirest.finanzas.steps;

import fi.uba.memo1.apirest.finanzas.dto.CostoRequest;
import fi.uba.memo1.apirest.finanzas.dto.CostosMensualesResponse;
import fi.uba.memo1.apirest.finanzas.repository.CostosMensualesRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ModificarCostoSteps {
    private double nuevoCosto;
    private Long id;
    private ResponseEntity<CostosMensualesResponse> response;
    private WebClientResponseException exception;

    private WebClient webClient;

    @Autowired
    private CostosMensualesRepository costoRepository;;
    @LocalServerPort
    private int port;

    @Before
    public void init() {
        this.webClient = WebClient.builder().baseUrl("http://localhost:" + port).build();
    }

    @Given("the new cost {string}")
    public void theNewCost(String nuevoCosto) {
        this.nuevoCosto = Double.parseDouble(nuevoCosto);
    }

    @Given("I do not have a role with ID {int}")
    public void iDoNotHaveARoleWithId(Integer id) {
        this.id = Long.valueOf(id.longValue());
    }

    @When("I PUT to the route {string} with rol id {int}")
    public void iPUTToTheRoute(String route, Integer id) {
        this.id = Long.valueOf(id.longValue());
        CostoRequest request = new CostoRequest();
        request.setNuevoCosto(nuevoCosto);

        try {
            this.response = webClient.put()
                    .uri(route)
                    .body(Mono.just(request), CostoRequest.class)
                    .retrieve()
                    .toEntity(CostosMensualesResponse.class)
                    .block();

        } catch (WebClientResponseException e) {
            this.exception = e;
        }

    }

    @Then("The response status must be {int}")
    public void theStatusCodeShouldBe(int statusCode) {
        if (this.exception != null) {
            assertEquals(statusCode, this.exception.getStatusCode().value());
            return;
        }

        assertEquals(statusCode, response.getStatusCode().value());
    }

    @Then("the cost should now be {string}")
    public void theCostShouldNowBe(String string) {

        assert response.getBody() != null;
        double costoActualizado = Objects.requireNonNull(response.getBody()).getCosto();
        assertEquals(Double.parseDouble(string), costoActualizado, 0.01);
    }

    @Then("the cost should remain {string}")
    public void theCostShouldRemain(String string) {
        double costo = costoRepository.findById(this.id).get().getCosto();
        assertEquals(Double.parseDouble(string), costo, 0.01);
    }

}
