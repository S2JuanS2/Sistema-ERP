package fi.uba.memo1.apirest.finanzas.steps;

import fi.uba.memo1.apirest.finanzas.CucumberSpringConfiguration;
import io.cucumber.spring.CucumberContextConfiguration;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@CucumberContextConfiguration
public class MyStepdefs extends CucumberSpringConfiguration {
    private String route;
    private ResponseEntity<String> response;

    @Given("the route {string}")
    public void theRoute(String endpoint) {
        this.route = "http://localhost:8080" + endpoint;
    }

    @When("I send a GET request")
    public void iSendAGETRequest() {
        response = testRestTemplate.getForEntity(route, String.class);
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int statusCode) {
        assertEquals(HttpStatusCode.valueOf(statusCode), response.getStatusCode());
    }

    @And("the response body should be {string}")
    public void theResponseBodyShouldBe(String body) {
        assertEquals(body, response.getBody());
    }
}
