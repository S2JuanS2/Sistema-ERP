package fi.uba.memo1.apirest.finanzas.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import fi.uba.memo1.apirest.finanzas.dto.CostosMensualesRequest;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CargarCostosSteps {
    private String name;
    private double cost;
    private String experience;
    private ResponseEntity<?> response;
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
        CostosMensualesRequest request = new CostosMensualesRequest();
        request.setNombre(name);
        request.setExperiencia(experience);
        request.setCosto(cost);

        try {
            Mono<ResponseEntity<String>> res = webClient.post()
                    .uri(route)
                    .body(Mono.just(request), CostosMensualesRequest.class)
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
    public void theResponseShouldBe(String text) throws IOException {
        if (this.exception != null) {
            // Si hubo una excepción, comparar el cuerpo de la respuesta con el texto esperado
            String responseBody = this.exception.getResponseBodyAsString();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode responseJson = mapper.readTree(responseBody);
            assertEquals(text, responseJson.get("message").asText());
            return;
        }

        // Si no hubo excepción, verificar el texto en la respuesta completa
        assertTrue(Objects.requireNonNull(this.response.toString()).contains(text));
    }

    @And("the response should be the object")
    public void theResponseShouldBeTheObject() throws JsonProcessingException {
        if(this.exception != null) {
            return;
        }
        ObjectMapper mapper = new ObjectMapper();
        JsonNode responseJson = mapper.readTree(Objects.requireNonNull(this.response.getBody()).toString());

        assertTrue(responseJson.has("id"));
        assertTrue(responseJson.has("idRol"));
        assertTrue(responseJson.has("mes"));
        assertTrue(responseJson.has("anio"));
        assertTrue(responseJson.has("costo"));
    }
}
