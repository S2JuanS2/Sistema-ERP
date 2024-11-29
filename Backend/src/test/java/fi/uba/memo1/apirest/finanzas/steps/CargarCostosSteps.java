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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CargarCostosSteps {
    private String name;
    private double cost;
    private String experience;
    private String month;
    private String year;
    private ResponseEntity<List<CostosMensualesResponse>> response;
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

    @Given("a month {string}")
    public void aMonth(String month) {
        this.month = month;
        
    }
    @Given("a year {string}")
    public void aYear(String year) {
        this.year = year;
    }

    @When("I POST to the route {string}")
    public void iPOSTToTheRoute(String route) {
        List<CostosMensualesRequest> requestList = new ArrayList<>();
        CostosMensualesRequest request = new CostosMensualesRequest();
        request.setNombre(name);
        request.setExperiencia(experience);
        request.setCosto(cost);
        if(!(this.month == null && this.year == null)){
            request.setMes(month);
            request.setAnio(year);
        }
        requestList.add(request);

        try {
        this.response = webClient.post()
                .uri(route)
                .body(Mono.just(requestList), new ParameterizedTypeReference<List<CostosMensualesResponse>>() {})
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<CostosMensualesResponse>>() {})
                .block();

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
    public void theResponseShouldBe(String text) throws JsonProcessingException {
        // Solo si hay una excepción, si no, el mensaje es el objeto
        assertEquals(text, StepsHelper.jsonErrorToMessage(this.exception.getResponseBodyAsString()));
    }

    @And("the response should be the object")
    public void theResponseShouldBeTheObject() {
        if (this.exception != null) {
            return;
        }
        CostosMensualesResponse costoObtenido = this.response.getBody().get(this.response.getBody().size() -1);

        assertEquals(this.cost, Objects.requireNonNull(costoObtenido).getCosto());
    }
}
